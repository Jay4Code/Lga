package com.lga.control.wificontrol.client;

import android.os.Build;
import android.os.StrictMode;

import com.lga.control.wificontrol.config.Config;
import com.lga.util.LogUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public abstract class UdpBroadcast {

	private class ReceiveData implements Runnable {
		private boolean stop;
		private String msg;
		private Thread thread;
		private ArrayList<String> dataList;

		private ReceiveData(String msg) {
			this.msg = msg;
			thread = new Thread(this);
			dataList = new ArrayList<>();
		}

		@Override
		public void run() {
			stop = false;

			while (!stop) {
				try {
					DatagramPacket packetToReceive = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
					socket.receive(packetToReceive);
					dataList.add(new String(packetToReceive.getData(), 0, packetToReceive.getLength()));
				} catch (SocketTimeoutException e) {
					LogUtil.e(DEBUG, "Receive packet timeout!");
					break;
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}

			if (!stop) {
                String data = dataList.get(0);
                dataList.remove(0);
				if(!data.equals(msg)) {
					onError(Config.ERROR_INTERFERENCE);
				} else {
					onReceived(dataList);
				}
			}

            close();
			stop = true;
		}

		void start() {
			thread.start();
		}

		void stop() {
			stop = true;
		}

		boolean isStoped() {
			return stop;
		}
	}

	private static final boolean DEBUG = false;
    private static final int BUFFER_SIZE = Config.UDP_RECEIVE_DATA_LENGTH;
	private int port = Config.UDP_PORT;
	private DatagramSocket socket;
	private DatagramPacket packetToSend;
	private InetAddress inetAddress;
	private ReceiveData receiveData;

	public UdpBroadcast() {
		super();

		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Open udp socket
	 */
	public void open() {
		try {
			socket = new DatagramSocket(port);
			socket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
            onError(Config.ERROR_PORT_USED);
		}
	}

	/**
	 * Close udp socket
	 */
	public void close() {
		stopReceive();
		if (socket != null) {
			socket.close();
		}
	}

	/**
	 * broadcast msg
	 * @param message the msg to broadcast
	 */
	public void send(final String message, final int timeout) {
		if (socket == null || message == null) {
			return;
		}

		packetToSend = new DatagramPacket(message.getBytes(), message.getBytes().length, inetAddress, port);

		try {
			socket.setSoTimeout(200);
			stopReceive();

			new Thread() {
				@Override
				public void run() {

					//remove the data in read chanel
					DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
					long time = System.currentTimeMillis();
					while (System.currentTimeMillis()-time < 300) {
						try {
							socket.receive(packet);
						} catch (Exception e) {
							break;
						}
					}

					//send data
					try {
						socket.setSoTimeout(timeout);
						socket.send(packetToSend);
					} catch (IOException e) {
						e.printStackTrace();
					}

					//receive response
					receiveData = new ReceiveData(message);
					receiveData.start();
				}
			}.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop to receive
	 */
	public void stopReceive() {

		if (receiveData!=null && !receiveData.isStoped()) {
			receiveData.stop();
		}
	}

	public abstract void onReceived(ArrayList<String> dataList);

	public abstract void onError(int errorType);
}

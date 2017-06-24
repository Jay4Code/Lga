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

public class UdpUnicast implements INetworkTransmission{

	public interface UdpUnicastListener {
		void onReceived(byte[] data, int length);
        void onReceivedOver();
	}

	private class ReceiveData implements Runnable {

		private boolean stop;
		private Thread thread;

		private ReceiveData() {
			thread = new Thread(this);
		}

		@Override
		public void run() {
			while (!stop) {
				try {
					DatagramPacket packetToReceive = new DatagramPacket(buffer, BUFFER_SIZE);
					socket.receive(packetToReceive);
					onReceive(buffer, packetToReceive.getLength());
				} catch (SocketTimeoutException e) {
					LogUtil.e(DEBUG, "UdpUnicast Receive packet timeout!");

                    setTimeout(0);
                    onReceivedOver();
				}catch (IOException e1) {
					LogUtil.e(DEBUG, "UdpUnicast Socket is closed!");
				}
			}
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
	private String ip;
	private int port = Config.UDP_PORT;
	private DatagramSocket socket;
	private DatagramPacket packetToSend;
	private InetAddress inetAddress;
	private ReceiveData receiveData;
	private UdpUnicastListener listener;
	private byte[] buffer = new byte[BUFFER_SIZE];

	public UdpUnicast(String ip, int port) {
		this();
		this.ip = ip;
		this.port = port;
	}

	public UdpUnicast() {
		super();
		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(UdpUnicastListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the listener
	 */
	public UdpUnicastListener getListener() {
		return listener;
	}

	/**
	 * Open udp socket
	 */
	public synchronized boolean open(int timeout) {
		try {
			inetAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}

		try {
			socket = new DatagramSocket(port);
            setTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
            close();
			return false;
		}

		//receive response
		receiveData = new ReceiveData();
		receiveData.start();
		return true;
	}

    private void setTimeout(int timeout) {
        if(socket != null) {
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Close udp socket
	 */
	public synchronized void close() {
		stopReceive();
		if (socket != null) {
			socket.close();
		}
	}

	/**
	 * send message
	 * @param text the message to broadcast
	 */
	public synchronized boolean send(String text) {
		if (socket == null) {
			return false;
		}

		if (text == null) {
			return true;
		}

		packetToSend = new DatagramPacket(text.getBytes(), text.getBytes().length, inetAddress, port);

		//send data
		try {
			socket.send(packetToSend);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Stop to receive
	 */
    public void stopReceive() {
        if (receiveData != null && !receiveData.isStoped()) {
            receiveData.stop();
        }
    }

	@Override
	public void setParameters(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void onReceive(byte[] buffer, int length) {
		if (listener != null) {
			listener.onReceived(buffer, length);
		}
	}

    public void onReceivedOver() {
        if (listener != null) {
            listener.onReceivedOver();
        }
    }
}
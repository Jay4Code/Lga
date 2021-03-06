package com.lga.control.wificontrol.client;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.lga.control.wificontrol.config.Config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer implements INetworkTransmission{

	public interface TCPServerListener {
		void onReceive(byte[] buffer, int length);
	}

	private String ip;
	private int port;
	private boolean stop;
	private ServerSocket server;
	private Socket socket;
	private BufferedInputStream inputStream;
	private BufferedOutputStream outputStream;
	private TCPServerListener listener;
	private byte[] buffer;
	
	public TCPServer(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		buffer = new byte[Config.TCP_RECEIVE_DATA_LENGTH];

		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	public void setParameters(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * @return the listener
	 */
	public TCPServerListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(TCPServerListener listener) {
		this.listener = listener;
	}

	@Override
	public synchronized boolean open(int timeout) {
		try {
			server = new ServerSocket(port, 1, InetAddress.getByName(ip));
			server.setSoTimeout(timeout);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					while (!stop) {
						try {
							socket = server.accept();
							inputStream = new BufferedInputStream(socket.getInputStream());
							outputStream = new BufferedOutputStream(socket.getOutputStream());
							
							int length;
							while (true) {
								length = inputStream.read(buffer);
								if (length > 0) {
									onReceive(buffer, length);
								}
								
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		stop = true;
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized boolean send(String text) {
		Log.d("outputStream:", outputStream.toString());
		
		if (outputStream != null) {
			try {
				Log.d("TCP Server Send:", text);
				outputStream.write(text.getBytes(), 0, text.getBytes().length);
				outputStream.flush();
				Log.d("TCP Server Send2:", text);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
	}

	@Override
	public void onReceive(byte[] buffer, int length) {
		if (listener != null) {
			listener.onReceive(buffer, length);
		}
	}
}

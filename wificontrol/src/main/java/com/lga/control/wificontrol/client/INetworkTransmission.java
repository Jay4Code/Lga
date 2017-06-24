package com.lga.control.wificontrol.client;

public interface INetworkTransmission {

	void setParameters(String ip, int port);
	boolean open(int timeout);
	void close();
	boolean send(String text);
	void onReceive(byte[] buffer, int length);
}

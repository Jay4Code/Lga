package com.lga.control.wificontrol.client;

public interface TransparentTransmissionListener {

	void onOpen(boolean success);
//	void onClose(boolean success);
	void onReceive(byte[] data, int length);

	void onReceivedOver();
}

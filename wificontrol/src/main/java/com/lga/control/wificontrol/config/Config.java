package com.lga.control.wificontrol.config;

/**
 * Created by Jay.X
 */

public class Config {

    /**UDP广播地址*/
    public static final String BROADCAST_IP = "255.255.255.255";
    /**WiFi模块默认地址*/
    public static final String MODULE_IP = "10.10.100.254";
    /**UDP通信端口*/
    public static final int UDP_PORT = 48899;
    /**UDP端接收到的消息的最大字节长度*/
    public static final int UDP_RECEIVE_DATA_LENGTH = 2048;
    /**UDP端读取数据超时时长*/
    public static final int UDP_READ_TIMEOUT = 3000;

    /**UDP扫描gateway超时时长*/
    public static final int CMD_SCAN_MODULES_TIMEOUT = 2500;


    /**TCP通信端口*/
    public static final int TCP_PORT = 8899;
    /**UDP端接收到的消息的最大字节长度*/
    public static final int TCP_RECEIVE_DATA_LENGTH = 2048;
    /**TCP端读取数据超时时长*/
    public static final int TCP_READ_TIMEOUT = 5000;

    /**gateway命令*/
    public static final String CMD_Q = "AT+Q\r";
    public static final String CMD_SCAN_MODULES = "HF-A11ASSISTHREAD";
    public static final String CMD_OK = "+ok";
    public static final String CMD_WSCAN = "AT+WSCAN\n";


    private static final int CONSTANT = 0;
    public static final int ERROR_INTERFERENCE = CONSTANT + 1;
    public static final int ERROR_PORT_USED = CONSTANT + 2;
    public static final int ERROR_UNCONNECT = CONSTANT + 3;
}

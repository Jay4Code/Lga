package com.lga.control.wificontrol.constant;

/**
 * Created by Jay.X
 */

public class Constant {
    private static final int CONSTANT = 0;

    public static final int MSG_WLAN_OFF = CONSTANT + 1;
    public static final int MSG_PORT_USED = CONSTANT + 2;

    /*scan gateway*/
    public static final int MSG_INTERFERENCE = CONSTANT + 3;
    public static final int MSG_SCAN_GATEWAY_RESULT = CONSTANT + 4;

    public static final int MSG_CONNECT_GATEWAY_RESULT = CONSTANT + 5;

    /*scan wlan*/
    public static final int MSG_NO_DEVICE = CONSTANT + 3;
    public static final int MSG_HOME_NET_ALREADY = CONSTANT + 4;
    public static final int MSG_SCAN_WLAN_RESULT = CONSTANT + 5;
}

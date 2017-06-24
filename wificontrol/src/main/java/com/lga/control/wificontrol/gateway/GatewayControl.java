package com.lga.control.wificontrol.gateway;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.lga.control.wificontrol.IControlImp;
import com.lga.control.wificontrol.client.TCPClient;
import com.lga.control.wificontrol.client.UdpBroadcast;
import com.lga.control.wificontrol.config.Config;
import com.lga.control.wificontrol.constant.Constant;
import com.lga.util.LogUtil;
import com.lga.util.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by Jay.X
 */
public class GatewayControl extends IControlImp {

    private static class SingletonHolder {
        private static final GatewayControl instance = new GatewayControl();
    }

    private final boolean DEBUG = true;
    private final int DELAY = 500;
    private Handler mHandler;
    private static ArrayList<String> mGatewayList;
    private UdpBroadcast mUdpBroadcast = new UdpBroadcast() {
        @Override
        public void onReceived(ArrayList<String> dataList) {
            mHandler.obtainMessage(Constant.MSG_SCAN_GATEWAY_RESULT, dataList).sendToTarget();

            for (String data : dataList) {
                LogUtil.e(true, "UdpBroadcast onReceived:" + data);
            }
        }

        @Override
        public void onError(int errorType) {
            switch (errorType) {
                case Config.ERROR_PORT_USED:
                    mHandler.sendEmptyMessageDelayed(Constant.MSG_PORT_USED, DELAY);
                    break;
                case Config.ERROR_INTERFERENCE:
                    mHandler.sendEmptyMessage(Constant.MSG_INTERFERENCE);
                    break;
            }
        }
    };

    private TCPClient.TCPClientListener mTcpListener = new TCPClient.TCPClientListener() {
        @Override
        public void onConnect(boolean success) {
            LogUtil.e(DEBUG, "TCPClientListener onConnect:" + success);
            mHandler.sendEmptyMessage(Constant.MSG_CONNECT_GATEWAY_RESULT);
        }

        @Override
        public void onReceive(byte[] buffer, int length) {
            LogUtil.e(DEBUG, "TCPClientListener onReceive:" + length);
        }
    };

    private GatewayControl() {}

    public static GatewayControl getInstance() {
        return SingletonHolder.instance;
    }

    public static void setGatewayList(ArrayList<String> gatewayList) {
        mGatewayList = gatewayList;
    }

    /**
     * 扫描网关
     * @param context context
     * @param handler handler
     */
    @Override
    public void scan(Context context, Handler handler) {
        if(!NetworkUtil.isNetworkConnected(context, ConnectivityManager.TYPE_WIFI)) {
            handler.sendEmptyMessageDelayed(Constant.MSG_WLAN_OFF, DELAY);
            return;
        }

        mHandler = handler;
        mUdpBroadcast.open();
        mUdpBroadcast.send(Config.CMD_SCAN_MODULES, Config.CMD_SCAN_MODULES_TIMEOUT);
    }

    /**
     * 连接网关
     * @param context context
     * @param handler handler
     * @param gatewayList gatewayList
     */
    @Override
    public void connect(Context context, Handler handler, ArrayList<String> gatewayList) {
        if(!NetworkUtil.isNetworkConnected(context, ConnectivityManager.TYPE_WIFI)) {
            handler.sendEmptyMessage(Constant.MSG_WLAN_OFF);
            return;
        }

        mHandler = handler;
        for (String gateway : gatewayList) {
            String ip = gateway.split(",")[0];
            TCPClient client = new TCPClient(ip, Config.TCP_PORT);
            client.setListener(mTcpListener);
            client.open(Config.TCP_READ_TIMEOUT);
        }
    }
}

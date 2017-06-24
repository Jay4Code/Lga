package com.lga.control.wificontrol;

import android.content.Context;
import android.os.Handler;

import com.lga.control.wificontrol.gateway.GatewayControl;
import com.lga.util.LogUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jay.X
 */
public class ControlCenter {

    private static final boolean DEBUG = false;
    private static HashSet<Socket> mSocketList;

    /**
     * 扫描网关
     * @param context context
     * @param handler handler
     */
    public static void scanGateway(Context context, Handler handler) {
        GatewayControl control = GatewayControl.getInstance();
        control.scan(context, handler);
    }

    /**
     * 连接网关
     * @param context context
     * @param handler handler
     * @param gatewayList gatewayList
     */
    public static void connectGateway(Context context, Handler handler, ArrayList<String> gatewayList) {
        if(mSocketList != null && !mSocketList.isEmpty()) {
            for (Socket socket : mSocketList) {
                try {
                    socket.close();
                    LogUtil.e(DEBUG, "close old socket");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        IControl control = GatewayControl.getInstance();
        control.connect(context, handler, gatewayList);
    }

    /**
     * 缓存已连接网关的socketList
     * @param socketList socketList
     */
    public static void setSocketList(HashSet<Socket> socketList) {
        mSocketList = socketList;
    }
}

package com.lga.control.wificontrol;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by Jay.X
 */
interface IControl {
    /**
     * 扫描
     * @param context context
     * @param handler handler
     */
    void scan(Context context, Handler handler);

    /**
     * 连接
     * @param context context
     * @param handler handler
     * @param gatewayList gatewayList(gateway中包含主机ip，型号等信息)
     */
    void connect(Context context, Handler handler, ArrayList<String> gatewayList);
}

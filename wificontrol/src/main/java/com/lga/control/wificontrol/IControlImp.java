package com.lga.control.wificontrol;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by Jay.X
 */

public abstract class IControlImp implements IControl {
    @Override
    public void scan(Context context, Handler handler) {}

    @Override
    public void connect(Context context, Handler handler, ArrayList<String> gatewayList) {}
}

package com.sunricher.app.demo;

import android.app.Activity;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lga.control.wificontrol.ControlCenter;
import com.lga.control.wificontrol.constant.Constant;
import com.lga.control.wificontrol.gateway.GatewayControl;
import com.lga.util.ActivityUtil;
import com.lga.util.HandlerUtil;

import java.util.ArrayList;

/**
 * Created by Jay.X
 */

public class ScanActivity extends BaseActivity {

    private static final boolean DEBUG = false;
    private ViewGroup mLayoutProgress;
    private ViewGroup mLayoutAlert;
    private TextView mAlert;

    private HandlerUtil<?> mHandler = new HandlerUtil<Activity>(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScanActivity activity = (ScanActivity) getOuterClass().get();
            switch (msg.what) {
                case Constant.MSG_WLAN_OFF:
                    stopScan(R.string.wlan_off);
                    break;
                case Constant.MSG_PORT_USED:
                    stopScan(R.string.port_used);
                    break;
                case Constant.MSG_INTERFERENCE:
                    stopScan(R.string.interference);
                    break;
                case Constant.MSG_SCAN_GATEWAY_RESULT:
                    ArrayList<String> gatewayList = (ArrayList<String>) msg.obj;
                    if(gatewayList == null || gatewayList.isEmpty()) {
                        stopScan(R.string.device_not_found);
                    } else {
                        GatewayControl.setGatewayList(gatewayList);

//                        activity.doConnect(gatewayList);
                        finish();
                    }
                    break;
                case Constant.MSG_CONNECT_GATEWAY_RESULT:
                    /*HashSet<Socket> socketList = (HashSet<Socket>) msg.obj;
                    if(socketList.isEmpty()) {
                        stopScan(R.string.device_not_connected);
                    } else {
                        ControlCenter.setSocketList(socketList);
                        finish();
                    }*/
                    finish();
                    break;
            }
        }
    };

    /**
     * 修正状态栏的背景色
     */
    @Override
    protected void preprocess() {
        super.preprocess();
        ActivityUtil.setStatusBarBackgroundColor(this, R.color.translucence_black);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void findView() {
        mLayoutProgress = (ViewGroup) findViewById(R.id.layout_progress);
        mLayoutAlert = (ViewGroup) findViewById(R.id.layout_alert);
        mAlert = (TextView) findViewById(R.id.tv_alert);
    }

    @Override
    protected void initView() {
        doScan();
    }

    private void doScan() {
        mLayoutAlert.setVisibility(View.GONE);
        mLayoutProgress.setVisibility(View.VISIBLE);

        ControlCenter.scanGateway(this, mHandler);
    }

    private void stopScan(int msgId) {
        mLayoutProgress.setVisibility(View.GONE);
        mAlert.setText(getText(msgId));
        mLayoutAlert.setVisibility(View.VISIBLE);
    }

    private void doConnect(ArrayList<String> gatewayList) {
        ControlCenter.connectGateway(this, mHandler, gatewayList);
    }

    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_close) {
            onBackPressed();
        } else if(id == R.id.btn_retry) {
            doScan();
        }
    }

    @Override
    public void onBackPressed() {
        if(mLayoutProgress.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        }
    }
}

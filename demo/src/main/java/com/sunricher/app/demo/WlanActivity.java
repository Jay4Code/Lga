package com.sunricher.app.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lga.app.view.RippleView;
import com.lga.control.wificontrol.bean.Wlan;
import com.lga.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.sunricher.app.demo.R.string.wlan;

/**
 * Created by Jay.X
 */
public class WlanActivity extends BaseActivity {

    private WifiManager mManager;
    private TextView mTitleTv;
    private RippleView mRipple;
    private TextView mAlertTv;
    private ListView mWlanLv;
    private WlanAdapter mAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                ArrayList<Wlan> wlanList = new ArrayList<>();
                List<ScanResult> resultList = mManager.getScanResults();
                for (int i = 0; i < resultList.size(); i++) {
                    ScanResult result = resultList.get(i);
                    Wlan wlan = new Wlan();
                    wlan.setId(i);
                    wlan.setSsid(result.SSID);
                    wlan.setMac(result.BSSID);
                    wlan.setEncryption(result.capabilities);
                    wlan.setRssi(result.level);
                    LogUtil.e(true, wlan.toString());
                    wlanList.add(wlan);
                }
            }
        }
    };

    @Override
    protected void preprocess() {
        super.preprocess();
        mManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wlans;
    }

    @Override
    protected void findView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTitleTv = (TextView) findViewById(R.id.title);
        mRipple = (RippleView) findViewById(R.id.ripple_view);
        mAlertTv = (TextView) findViewById(R.id.tv_alert);
        mWlanLv = (ListView) findViewById(R.id.list_view);
        mAdapter = new WlanAdapter(this, R.layout.iv_wlan_list, null);
        mWlanLv.setAdapter(mAdapter);
        mWlanLv.setEmptyView(mAlertTv);
    }

    @Override
    protected void initView() {}

    @Override
    protected void onResume() {
        super.onResume();
        startScan();

        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
        mManager.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan("");

        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void startScan() {
        if(!mRipple.isStarting()) {
            mRipple.start();

            mTitleTv.setText(R.string.scanning);
            mAlertTv.setVisibility(View.GONE);
            mRipple.setVisibility(View.VISIBLE);
        }
    }

    private void stopScan(int msgId) {
        if(mRipple != null && mRipple.isStarting()) {
            mRipple.stop();

            mTitleTv.setText(wlan);
            mRipple.setVisibility(View.GONE);
            mAlertTv.setText(msgId);
            mAlertTv.setVisibility(View.VISIBLE);
        }
    }

    private void stopScan(String msg) {
        if(mRipple != null && mRipple.isStarting()) {
            mRipple.stop();

            mTitleTv.setText(wlan);
            mRipple.setVisibility(View.GONE);
            mAlertTv.setText(msg);
            mAlertTv.setVisibility(View.VISIBLE);
        }
    }

    private void showWlanList(Object obj) {
        if(obj == null || !(obj instanceof ArrayList)) {
            mAlertTv.setText(R.string.no_wlan);
        } else {
            ArrayList<Wlan> wlanList = (ArrayList<Wlan>) obj;
            if(wlanList.isEmpty()) {
                mAlertTv.setText(R.string.no_wlan);
            }
            mAdapter.setDataList(wlanList);
            mAdapter.notifyDataSetChanged();
            mWlanLv.setVisibility(View.VISIBLE);
        }
    }
}

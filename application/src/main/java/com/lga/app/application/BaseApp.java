package com.lga.app.application;

import android.app.Application;

import com.lga.util.LogUtil;

/**
 * Created by Jay.X
 */
public abstract class BaseApp extends Application {

    private final boolean DEBUG = false;
    private TaskSwitch mTaskSwitch;
    private TaskSwitch.OnTaskSwitchListener mOnTaskSwitchListener = new TaskSwitch.OnTaskSwitchListener() {
        @Override
        public void onTaskSwitchToForeground() {
            LogUtil.e(DEBUG, "切换到前台");

            goScanActivity();
        }

        @Override
        public void onTaskSwitchToBackground() {
            LogUtil.e(DEBUG, "切换到后台");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mTaskSwitch = TaskSwitch.getInstance();
        mTaskSwitch.registerCallbacks(this);
        mTaskSwitch.setOnTaskSwitchListener(mOnTaskSwitchListener);
    }

    protected abstract void goScanActivity();
}

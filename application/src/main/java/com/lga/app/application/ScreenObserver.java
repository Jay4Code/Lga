package com.lga.app.application;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.view.Display;

/**
 * Created by Jay.X
 */
public class ScreenObserver {

    public interface OnScreenStateListener {
        void onScreenOn();
        void onScreenOff();
        void onUserPresent();
    }

    static final class SingletonHolder {
        private static ScreenObserver instance = new ScreenObserver();
    }

    /**
     * screen状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mOnScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mOnScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mOnScreenStateListener.onUserPresent();
            }
        }
    }

    private ScreenBroadcastReceiver mScreenReceiver;
    private OnScreenStateListener mOnScreenStateListener;

    public static ScreenObserver getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 监听screen状态
     */
    public void registerScreenBroadcast(Context context) {
        mScreenReceiver = new ScreenBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        context.registerReceiver(mScreenReceiver, filter);
    }

    /**
     * 停止监听screen状态
     */
    public void unregisterScreenBroadcast(Context context) {
        if(mScreenReceiver != null) {
            context.unregisterReceiver(mScreenReceiver);
        }
    }

    private void getScreenState(Context context) {
        if (isScreenOn(context)) {
            if (mOnScreenStateListener != null) {
                mOnScreenStateListener.onScreenOn();
            }
        } else {
            if (mOnScreenStateListener != null) {
                mOnScreenStateListener.onScreenOff();
            }
        }
    }

    public boolean isScreenOn(Context context) {
        if (Build.VERSION.SDK_INT >= 20) {
            // I'm counting STATE_DOZE, STATE_OFF, STATE_DOZE_SUSPENDED all as "OFF"
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = dm.getDisplays();
            for (Display display : displays) {
                if (display.getState () == Display.STATE_ON || display.getState () == Display.STATE_UNKNOWN) {
                    return true;
                }
            }
            return false;
        } else { // If you use less than API20:
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return powerManager.isScreenOn();
        }
    }

    public void setOnScreenStateListener(OnScreenStateListener listener) {
        mOnScreenStateListener = listener;
    }

    public boolean isScreenLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }
}
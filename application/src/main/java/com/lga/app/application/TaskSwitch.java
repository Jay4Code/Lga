package com.lga.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Created by Jay.X
 */
public class TaskSwitch extends ActivityLifecycleCallbacksImpl {

    public interface OnTaskSwitchListener {
        void onTaskSwitchToForeground();
        void onTaskSwitchToBackground();
    }

    static final class SingletonHolder {
        private static TaskSwitch instance = new TaskSwitch();
    }

    private Context mContext;
    private static TaskSwitch mTaskSwitch;
    private int mCount = 0;
    private OnTaskSwitchListener mListener;
    private ScreenObserver mScreenObserver;
    private boolean isScreenOn = true;
    private String[] mActivityFilter;

    private ScreenObserver.OnScreenStateListener mOnScreenStateListener = new ScreenObserver.OnScreenStateListener() {
        @Override
        public void onScreenOn() {}

        @Override
        public void onScreenOff() {

        }

        @Override
        public void onUserPresent() {
            if(mCount > 0) {
                mListener.onTaskSwitchToForeground();
            }
        }
    };

    public static TaskSwitch getInstance() {
        mTaskSwitch = SingletonHolder.instance;
        return mTaskSwitch;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(!hasListener()) {
            return;
        }

        if(isInActivityFilter(activity)) {
            return;
        }

        if(mCount++ == 0 & isScreenOn) {
            mListener.onTaskSwitchToForeground();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if(!hasListener()) {
            return;
        }

        if(isInActivityFilter(activity)) {
            return;
        }

        if(--mCount == 0) {
            isScreenOn = mScreenObserver.isScreenOn(activity);

            mListener.onTaskSwitchToBackground();
        }
    }

    public TaskSwitch registerCallbacks(Application app) {
        mContext = app;

        // 监听activity生命周期
        app.registerActivityLifecycleCallbacks(mTaskSwitch);

        // 监听screen状态（on/off/unlock）
        mScreenObserver = ScreenObserver.getInstance();
        mScreenObserver.registerScreenBroadcast(app);
        mScreenObserver.setOnScreenStateListener(mOnScreenStateListener);

        mActivityFilter = app.getResources().getStringArray(R.array.activity_filter);
        return mTaskSwitch;
    }

    public void unregisterCallbacks(Application app) {
        if(mTaskSwitch != null) {
            app.unregisterActivityLifecycleCallbacks(mTaskSwitch);
        }

        if(mScreenObserver != null) {
            mScreenObserver.unregisterScreenBroadcast(app);
        }
    }

    /**
     * 排除SplashActivity和GuideActivity
     */
    private boolean isInActivityFilter(Activity activity) {
        String name = activity.getClass().getSimpleName();
        for(String filter : mActivityFilter) {
            if(filter.equals(name)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasListener() {
        return mListener != null;
    }

    public void setOnTaskSwitchListener(OnTaskSwitchListener listener) {
        mListener = listener;
    }
}

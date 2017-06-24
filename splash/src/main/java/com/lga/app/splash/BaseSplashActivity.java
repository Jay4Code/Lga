package com.lga.app.splash;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.lga.util.ActivityUtil;

/**
 * Created by Jay.X
 */
public abstract class BaseSplashActivity extends Activity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.setFullScreenDisplay(this, isFullScreenDiaplay(), isTransparentStatusBar());

        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goGuideActivity();
            }
        }, getDelay());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * @return true：全屏（api版本低于19），或显示透明状态栏（api版本不低于19）；false：不全屏显示
     */
    protected abstract boolean isFullScreenDiaplay();

    /**
     * @return true：显示透明状态栏（api版本不低于19）；false：不显示透明效果
     */
    protected abstract boolean isTransparentStatusBar();

    protected abstract long getDelay();

    protected abstract void goGuideActivity();

    /**
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}

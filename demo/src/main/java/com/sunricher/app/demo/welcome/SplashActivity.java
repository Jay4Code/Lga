package com.sunricher.app.demo.welcome;

import android.content.Intent;
import android.content.SharedPreferences;

import com.lga.app.guide.constant.Key;
import com.lga.app.splash.BaseSplashActivity;
import com.lga.sharedpreferences.SecureFactory;
import com.sunricher.app.demo.MainActivity;

/**
 * Created by Jay.X
 */
public class SplashActivity extends BaseSplashActivity {

    @Override
    protected boolean isFullScreenDiaplay() {
        return true;
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }

    @Override
    protected long getDelay() {
        return 2500;
    }

    @Override
    protected void goGuideActivity() {
        SharedPreferences prefs = SecureFactory.getPreferences(this);
        boolean isFirstUse = prefs.getBoolean(Key.IS_FIRST_USE, true);
        if (isFirstUse) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}

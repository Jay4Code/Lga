package com.sunricher.app.demo.welcome;

import android.content.Intent;
import android.content.SharedPreferences;

import com.lga.app.guide.BaseGuideActivity;
import com.lga.app.guide.constant.Key;
import com.lga.sharedpreferences.SecureFactory;
import com.sunricher.app.demo.MainActivity;
import com.sunricher.app.demo.R;

/**
 * Created by Jay.X
 */
public class GuideActivity extends BaseGuideActivity {
    @Override
    protected boolean isFullScreenDiaplay() {
        return true;
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }

    @Override
    public void goMainActivity() {
        SharedPreferences prefs = SecureFactory.getPreferences(this);
        prefs.edit().putBoolean(Key.IS_FIRST_USE, false).apply();

        createShortcut(this, getString(R.string.app_name), R.mipmap.ic_launcher, SplashActivity.class);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

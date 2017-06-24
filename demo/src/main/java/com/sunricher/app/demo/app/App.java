package com.sunricher.app.demo.app;

import android.content.Intent;

import com.lga.app.application.BaseApp;
import com.sunricher.app.demo.ScanActivity;

/**
 * Created by Jay.X
 */

public class App extends BaseApp {

    @Override
    protected void goScanActivity() {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

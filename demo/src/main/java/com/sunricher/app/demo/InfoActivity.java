package com.sunricher.app.demo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jay.X
 */
public class InfoActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
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

        TextView version = (TextView) findViewById(R.id.version);
        try {
            String versionNo = getString(R.string.version_no, getAppVersion(this));
            version.setText(getString(R.string.app_name) + versionNo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.setText(R.string.app_name);
        }

    }

    @Override
    protected void initView() {}

    private String getAppVersion(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    private PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
    }
}

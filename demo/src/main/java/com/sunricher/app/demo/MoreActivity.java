package com.sunricher.app.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Jay.X
 */
public class MoreActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more;
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
    }

    @Override
    protected void initView() {}

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.connect_home_net:
                Snackbar.make(v, "Is your mobile phone already connected to the WiFi controller?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MoreActivity.this, WlanActivity.class));
                            }
                        })
                        .setActionTextColor(Color.parseColor("#0099cc"))
                        .show();

                break;
            case R.id.reset:

                break;
            case R.id.info:
//                startActivity(new Intent(this, InfoActivity.class));
                String info;
                try {
                    String versionNo = getString(R.string.version_no, getAppVersion(this));
                    info = getString(R.string.app_name) + versionNo;
                } catch (PackageManager.NameNotFoundException e) {
                    info = getString(R.string.app_name);
                    e.printStackTrace();
                }
                Snackbar.make(v, info, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private String getAppVersion(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    private PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
    }
}

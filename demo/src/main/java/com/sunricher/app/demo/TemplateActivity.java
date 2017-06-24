package com.sunricher.app.demo;

import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Jay.X
 */
public class TemplateActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_template; // x
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
}

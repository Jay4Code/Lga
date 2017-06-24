package com.sunricher.app.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lga.util.ActivityUtil;

/**
 * Created by Jay.X
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preprocess();
        setContentView(getLayoutId());
        findView();
        initView();
    }

    /**
     * 预处理（如设置window属性：全屏、透明状态栏等）
     */
    protected void preprocess() {
        ActivityUtil.setFullScreenDisplay(this, false, true);
    }

    protected abstract int getLayoutId();

    protected abstract void findView();

    protected abstract void initView();
}

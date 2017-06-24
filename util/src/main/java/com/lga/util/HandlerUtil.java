package com.lga.util;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Jay.X
 */

public class HandlerUtil<T> extends Handler {
    private WeakReference<T> mOuterClass;

    public HandlerUtil(T t) {
        mOuterClass = new WeakReference<>(t);
    }

    public WeakReference<T> getOuterClass() {
        return mOuterClass;
    }

    public void setOuterClass(WeakReference<T> outerClass) {
        mOuterClass = outerClass;
    }
}

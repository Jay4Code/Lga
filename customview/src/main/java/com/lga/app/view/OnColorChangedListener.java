package com.lga.app.view;

import android.view.View;

/**
 * Created by Sunricher on 2016/7/22.
 */
public interface OnColorChangedListener {

    void onStartTrackingTouch(View view);

    void onColorChanged(View view, int color, int progress, boolean fromUser);

    void onStopTrackingTouch(View view, int color, int progress);
}

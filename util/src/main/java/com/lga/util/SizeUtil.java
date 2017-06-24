package com.lga.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * Created by Jay.X
 */
public class SizeUtil {

    /**
     * 获取WindowManager对象
     */
    private static WindowManager getWindowManager(Context context) {
        WindowManager manager;
        if(context instanceof Activity) {
            manager = ((Activity) context).getWindowManager();
        } else {
            manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return manager;
    }

    /**
     * 获取屏幕宽度（px）
     */
    public static int getScreenWidth(Context context) {
        int width = 0;
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager(context).getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return width;
    }

    /**
     * 获取屏幕高度（px）
     */
    public static int getScreenHeight(Context context) {
        int height = 0;
        try {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager(context).getDefaultDisplay().getMetrics(metric);
            height = metric.heightPixels;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取状态栏高度（px）
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static int getStatusBarHeight(Activity activity, boolean hasStatusBar) {
        int height = 0;
        if (hasStatusBar) {
            Rect localRect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
            height = localRect.top;
            if (0 == height){
                Class<?> localClass;
                try {
                    localClass = Class.forName("com.android.internal.R$dimen");
                    Object localObject = localClass.newInstance();
                    // 获取原始的状态栏高度，此时的高度值不可用，需要转化
                    int statusBarOriHeight = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                    height = activity.getResources().getDimensionPixelSize(statusBarOriHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return height;
    }

    /**
     * 获取view宽度（px）
     */
    public static int getViewWidth(View view) {
        int width = 0;
        try {
            view.measure(0, 0);
            width = view.getMeasuredWidth();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return width;
    }

    /**
     * 获取view高度（px）
     */
    public static int getViewHeight(View view) {
        int height = 0;
        try {
            view.measure(0, 0);
            height = view.getMeasuredHeight();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取listview的总高度（px）
     */
    public static int getListViewHeight(ListView listView) {
        int totalHeight = 0;
        try {
            int count = listView.getCount();
            for (int i = 0; i < count; i++) {
                View itemView = listView.getChildAt(i);
                itemView.measure(0, 0); // 计算子项View 的宽高
                totalHeight += itemView.getMeasuredHeight(); // 统计所有子项的总高度
            }
            totalHeight += (listView.getDividerHeight() * (count - 1));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return totalHeight;
    }

    /**
     * 获取listview的LayoutParams对象，目的是动态设置listview的高度，最大高度不超过maxHeight
     */
    public static ViewGroup.LayoutParams getListViewLayoutParamsBasedOnChildren(ListView listView, int maxHeight) {
        int minHeight = Math.min(maxHeight, getListViewHeight(listView));
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if(params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minHeight);
        } else {
            params.height = minHeight;
        }
        return params;
    }

    /**
     * 获取屏幕密度
     */
    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        return Math.round(dp * getDensity(context));
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float px) {
        return Math.round(px / getDensity(context));
    }

    /**
     * 获取字体显示在屏幕上时的拉伸因数
     */
    private static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float sp) {
        return Math.round(sp * getScaledDensity(context));
    }

    /**
     * px转sp
     */
    public static int px2sp(Context context, float px) {
        return Math.round(px / getScaledDensity(context));
    }

    /**
     * 触发滑动的最小距离（px）
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static int getTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
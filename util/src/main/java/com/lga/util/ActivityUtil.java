package com.lga.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jay.X
 */
public class ActivityUtil {

    /**
     * 透明状态栏/全屏/普通状态栏
     * @param activity activity
     * @param isFullScreenDisplay true：全屏（api版本低于19），或显示透明状态栏（api版本不低于19）；false：不全屏显示
     * @param isImmersive true：显示透明状态栏（api版本不低于19）；false：不显示透明效果
     */
    public static void setFullScreenDisplay(Activity activity, boolean isFullScreenDisplay, boolean isImmersive) {
        Window window = activity.getWindow();
        if (isFullScreenDisplay) {
            // 全屏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if(isImmersive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                setStatusBarBackgroundColor(activity, R.color.colorPrimary);
            }
        }
    }

    /**
     * 设置 状态栏/导航栏 背景色
     * @param activity activity
     * @param color color-int
     */
    public static void setStatusBarBackgroundColor(Activity activity, int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(activity.getResources().getColor(color));
    }

    /*
	 声明：Activity包括actionbar，content两部分，布局上actionbar不会覆盖content
	 且布局文件中没有声明android:fitsSystemWindows="true"
	 且下列属性仅与View.SYSTEM_UI_FLAG_LAYOUT_STABLE配合使用

	 View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	 	提升content布局层级，即actionbar会覆盖content；
	 	帮助维持一个稳定的布局，防止系统栏隐藏时内容区域大小发生变化
	 View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	 	Activity全屏显示，状态栏可见，content全屏显示，即content会被状态栏和actionbar覆盖（状态栏不覆盖actionbar）
	 View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	 	效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	 View.SYSTEM_UI_LAYOUT_FLAGS
	 	效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	 View.SYSTEM_UI_FLAG_VISIBLE
	 	状态栏不覆盖content
	 View.INVISIBLE
	 	状态栏INVISIBLE，占据屏幕内容；actionbar gone
	 View.SYSTEM_UI_FLAG_FULLSCREEN
	 	效果同View.INVISIBLE
	 View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	 	导航栏gone,但content不会延伸（因为与SYSTEM_UI_FLAG_LAYOUT_STABLE配合使用，单独使用时content延伸）,有些手机会用虚拟按键来代替物理按键,
	 View.SYSTEM_UI_FLAG_LOW_PROFILE
	 	状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏，导航栏图标INVISIBLE
	 View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
	 	修改状态栏的字体和图标颜色为灰色
	 View.SYSTEM_UI_FLAG_IMMERSIVE
		与其他flag配合使用提供沉浸式体验就，用户滑动展示系统栏，展示后，系统栏保持可见
	 View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		与其他flag配合使用提供沉浸式体验，系统栏在一段时间后自动隐藏，不会触发任何的监听器，因为在这个模式下展示的系统栏是处于暂时的状态
	 */
    /**
     * 全代码设置欢迎界面沉浸式效果
     * @param activity activity
     */
    public static void setWelcomeImmersive(Activity activity) {
        int sdk_int = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏（包括ActionBar）
        if(sdk_int < 16) {
            // 全屏显示
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }

        if(sdk_int >= 16) {
            View decorView = window.getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            if(sdk_int >= 19) {
                // 沉浸式效果
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            if (Build.VERSION.SDK_INT >= 11) {
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    /**
     * xml主题NoActionBar.Fullscreen结合代码设置欢迎界面沉浸式效果（轻量级）
     * @param activity activity
     */
    public static void setWelcomeLightImmersive(Activity activity) {
        int sdk_int = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if(sdk_int >= 16) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            if(sdk_int >= 19) {
                // 沉浸式效果
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            if (Build.VERSION.SDK_INT >= 11) {
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }
}

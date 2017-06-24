package com.lga.app.guide;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lga.app.guide.fragment.GuideFragment;
import com.lga.app.view.CircleIndicator;
import com.lga.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay.X
 */
public abstract class BaseGuideActivity extends FragmentActivity {

    private Context mContext;
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.setFullScreenDisplay(this, isFullScreenDiaplay(), isTransparentStatusBar());

        setContentView(R.layout.activity_guide);

        initView();
    }

    /**
     * @return true：全屏（api版本低于19），或显示透明状态栏（api版本不低于19）；false：不全屏显示
     */
    protected abstract boolean isFullScreenDiaplay();

    /**
     * @return true：显示透明状态栏（api版本不低于19）；false：不显示透明效果
     */
    protected abstract boolean isTransparentStatusBar();

    private void initView() {
        mContext = this;
        int[] guideImgIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        String[] guideLabels = getResources().getStringArray(R.array.guide_labels);
        ArrayList<Fragment> fragments = new ArrayList<>();
        int len = guideImgIds.length;
        for (int i = 0; i < len; i++) {
            Fragment fragment = GuideFragment.newInstance(guideImgIds[i], guideLabels[i]);
            fragments.add(fragment);
        }

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(len);
        pager.addOnPageChangeListener(mOnPageChangeListener);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.skip) {
            goMainActivity();
        }
    }

    public abstract void goMainActivity();

    /**
     * 创建快捷方式
     * @param context context
     * @param name 快捷方式的名称
     * @param iconId 快捷方式的图标id
     * @param targetActivity 点击快捷方式后要启动的activity
     */
    protected void createShortcut(Context context, String name, int iconId, Class targetActivity) {
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, iconId);
        if(shortcutExisted(name, iconRes)) {
            return;
        }

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 无效，模拟器上测试任然会重复创建
        shortcut.putExtra("duplicate", false);//设置是否重复创建
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, targetActivity);//设置第一个页面
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);
    }

    /**
     * 判断快捷方式是否存在
     * @param name 快捷方式的名称
     * @param iconRes 快捷方式的图标
     * @return true存在
     */
    private boolean shortcutExisted(String name, Intent.ShortcutIconResource iconRes) {
        ContentResolver cr = getContentResolver();
        String authority = getAuthorityFromPermission(this, "com.android.launcher.permission.READ_SETTINGS");
        Uri CONTENT_URI = Uri.parse("content://" + authority + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?", new String[] { name }, null);
        if(c != null && c.getCount() > 0) {
            c.close();
            return true;
        }
        return false;
    }

    /**
     * 通过permission来获取准确的Authority
     *
     * @param context context
     * @param permission com.android.launcher.permission.READ_SETTINGS or com.android.launcher.permission.WRITE_SETTINGS
     * @return
     */
    private String getAuthorityFromPermission(Context context, String permission) {
        if(permission == null) {
            return null;
        }
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if(packs == null) {
            return null;
        }
        for(PackageInfo pack : packs) {
            ProviderInfo[] providers = pack.providers;
            if(providers == null) {
                continue;
            }
            for(ProviderInfo provider : providers) {
                if(permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                    return provider.authority;
                }
            }
        }
        return null;
    }
}

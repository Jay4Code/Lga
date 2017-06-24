package com.lga.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by Jay.X
 */

public class NetworkUtil {

    /**
     * 判断指定的网络是否可以传输数据
     * <p>传输数据前调用此方法
     * @param context context
     * @param networkType 网络类型
     * @return true if the specified network is connected
     */
    public static boolean isNetworkConnected(Context context, int networkType) {
        NetworkInfo info = getNetworkInfo(context, networkType);
        return info != null && info.isConnected();
    }

    /**
     * 获取指定网络的NetworkInfo实例
     * @param context context
     * @param networkType 网络类型
     * @return NetworkInfo
     */
    private static NetworkInfo getNetworkInfo(Context context, int networkType) {
        NetworkInfo info = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null) {
            return info;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] works = cm.getAllNetworks();
            for(Network work : works) {
                info = cm.getNetworkInfo(work);
                if(info != null && info.getType() == networkType) {
                    break;
                }
            }
        } else {
            info = cm.getNetworkInfo(networkType);
        }
        return info;
    }
}

package com.item.jiejie;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by wuzongjie on 2017/10/23.
 */

public class ToastUtils {



    /**
     * 判断是否有指定的权限
     *
     * @param activity    上下文
     * @param permissions 权限
     * @return 是否有权限
     */
    public static boolean hasPermission(AppCompatActivity activity, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请指定的权限
     *
     * @param activity    上下文
     * @param code        code
     * @param permissions 权限
     */
    public static void requestPermission(AppCompatActivity activity, int code, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(permissions, code);
        }
    }


}

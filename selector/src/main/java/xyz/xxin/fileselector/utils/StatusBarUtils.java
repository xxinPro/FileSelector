package xyz.xxin.fileselector.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

/**
 * 修改状态栏工具类
 */
public class StatusBarUtils {

    /**
     * 获取状态栏高度
     * @param activity 指定activity
     * @return 状态栏高度px
     */
    public static int getStatusHeight(Activity activity){
        int statusBarHeight = 0;
        int identifier = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(identifier);
        }
        return statusBarHeight;
    }



    /**
     * 修改状态栏颜色，支持Android5.0以上版本
     * @param activity 指定activity
     * @param colorId 颜色
     */
    public static void setStatusBarColor(Activity activity, int colorId){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //清理状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //清理导航栏
        window.setStatusBarColor(activity.getResources().getColor(colorId));
    }

    /**
     * 修改底部导航栏颜色，支持Android5.0以上版本
     */
    public static void setNavigationBarColor(Activity activity, int colorId) {
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, colorId));
    }

    /**
     * 设置状态栏透明，支持Android5.0以上版本
     * @param activity 指定activity
     */
    public static void setTranslucentStatus(Activity activity){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置底部虚拟导航栏透明
     */
    public static void setTranslucentNavigation(Activity activity){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }


    /**
     * 设置状态栏字体颜色，支持Android6.0以上版本
     * @param activity 指定activity
     * @param isDark 是否深色
     */
    public static void setStatusTextColor(Activity activity, boolean isDark){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else {
                decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}

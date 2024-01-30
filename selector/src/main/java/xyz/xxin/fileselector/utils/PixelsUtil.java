package xyz.xxin.fileselector.utils;

import android.content.Context;

public class PixelsUtil {
    public static int dp2px(double dp, Context context){
        return (int)(dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(double px, Context context){
        return (int)(px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(float sp, Context context) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int px2sp(float px, Context context) {
        return (int) (px / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}

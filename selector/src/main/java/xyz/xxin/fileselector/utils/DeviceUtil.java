package xyz.xxin.fileselector.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import xyz.xxin.fileselector.beans.ConfigBean;

public class DeviceUtil {

    /**
     * 震动设备
     */
    public static void vibrateDevice(Context context, int mill) {
        if (!ConfigBean.getInstance().isVibrator) return;

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // 检查设备是否支持震动
        if (vibrator != null && vibrator.hasVibrator()) {
            // 在 Android 26 及以上的版本中使用 VibrationEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(mill, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(vibrationEffect);
            } else {
                // 在 Android 25 及以下的版本中使用旧的 vibrate 方法
                vibrator.vibrate(mill);
            }
        }
    }

}

package xyz.xxin.fileselector.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class ResourcesUtil {

    /**
     * 通过color id查找颜色
     */
    public static int getColorById(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    /**
     * 通过drawable id查找drawable
     */
    public static Drawable getDrawableById(Context context, int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    public static String getStringById(Context context, int stringId) {
        return context.getResources().getString(stringId);
    }
}

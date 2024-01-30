package xyz.xxin.fileselector.utils;

import android.annotation.SuppressLint;

public class SizeUtil {

    @SuppressLint("DefaultLocale")
    public static String getSize(long length) {
        if (length > (1024 * 1024 * 1024)) {   // 达到G级
            return String.format("%.2fG", (float)(length / (1024 * 1024 * 1024)));
        } else if (length > (1024 * 1024)) {    // 达到M级
            return String.format("%.2fM", (float)(length / (1024 * 1024)));
        } else if (length > 1024) {             // 达到K级
            return String.format("%.2fK", (float)(length / 1024));
        } else {    // B级
            return String.format("%dB", length);
        }
    }

}

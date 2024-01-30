package xyz.xxin.fileselector;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public class FileSelector {

    public static FileSelectConfig create(Activity activity) {
        return new FileSelectConfig(activity);
    }

    public static FileSelectConfig create(Fragment fragment) {
        return new FileSelectConfig(fragment);
    }
}

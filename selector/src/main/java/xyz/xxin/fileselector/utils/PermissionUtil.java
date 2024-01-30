package xyz.xxin.fileselector.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtil {


    /**
     * Android 13细化内存读取权限
     * 将READ_EXTERNAL_STORAGE权限细化为为READ_MEDIA_IMAGES、READ_MEDIA_VIDEO、READ_MEDIA_AUDIO
     * 其中READ_MEDIA_IMAGES和READ_MEDIA_VIDEO在同一个权限组
     */
    public static void requestReadMedia(Activity activity, int requestCode) {
        if (!isReadMedia(activity) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
            }, requestCode);
        }
    }


    /**
     * Android 13细化内存读取权限
     * 将READ_EXTERNAL_STORAGE权限细化为为READ_MEDIA_IMAGES、READ_MEDIA_VIDEO、READ_MEDIA_AUDIO
     */
    public static boolean isReadMedia(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }



    /**
     * 请求外部储存写入和读取权限，安卓6.0及之前不需要动态申请
     */
    public static void requestWriteExternal(Activity activity, int requestCode){
        if (!isWriteExternal(activity)){
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },requestCode);
        }
    }

    public static void requestWriteExternal(Fragment fragment, int requestCode){
        if (!isWriteExternal(fragment.getContext())) {
            fragment.requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },requestCode);
        }
    }

    /**
     * 是否拥有外部储存写入和读取权限，安卓6.0及之前不需要动态申请
     */
    public static boolean isWriteExternal(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 申请所有文件访问权限，安卓10及之前无需申请
     * @param activity 上下文
     * @param requestCode 请求权限请求码
     */
    @SuppressLint("InlinedApi")
    public static void requestAllFilePermission(Activity activity, int requestCode){
        if (!isAllFilePermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void requestAllFilePermission(Fragment fragment, int requestCode){
        if (!isAllFilePermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + fragment.getContext().getPackageName()));
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 是否拥有所有文件访问权限，安卓10及之前无需申请
     * @return 是否拥有所有文件访问权限
     */
    public static boolean isAllFilePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true;
    }
}

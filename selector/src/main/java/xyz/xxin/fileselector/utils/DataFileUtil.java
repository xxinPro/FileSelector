package xyz.xxin.fileselector.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.xxin.fileselector.beans.FileItemBean;

public class DataFileUtil {
    /**
     * 获取Android/data目录下的所有文件的列表
     */
    public static List<FileItemBean> getDataFileList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // 表示获取所有应用包名
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);

        // 存放所有Android/data/下的直系子目录
        List<FileItemBean> packageNameList = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applications) {
            // 组合得到Android/data/下的直系子目录，这个有点奇怪，可以使用File.exists判断是否存在
            File file = new File(SAFUtil.ANDROID_DATA_PATH + "/" + applicationInfo.packageName);
            // 判断是否是三方软件 (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
            // 这里只判断了对应的目录是否存在
            if (file.exists() && file.isDirectory()) {
                packageNameList.add(new FileItemBean(file));
            }
        }

        return packageNameList;
    }

    /**
     * 获取Android/obb目录下的所有文件的列表
     */
    public static List<FileItemBean> getObbFileList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // 表示获取所有应用包名
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);

        // 存放所有Android/data/下的直系子目录
        List<FileItemBean> packageNameList = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applications) {
            // 组合得到Android/data/下的直系子目录，这个有点奇怪，可以使用File.exists判断是否存在
            File file = new File(SAFUtil.ANDROID_OBB_PATH + "/" + applicationInfo.packageName);
            // 判断是否是三方软件 (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
            // 这里只判断了对应的目录是否存在
            if (file.exists() && file.isDirectory()) {
                packageNameList.add(new FileItemBean(file));
            }
        }

        return packageNameList;
    }
}

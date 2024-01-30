package xyz.xxin.fileselector.utils;

import xyz.xxin.fileselector.beans.ConfigBean;

public class FileUtil {

    /**
     * 获取文件的后缀名
     */
    public static String getSuffix(String fileName) {
        int pointIndex = fileName.lastIndexOf(".");

        // 必须有“.”，并且“.”不能在最后一个位置
        if (pointIndex >= 0 && pointIndex != fileName.length() - 1) {
            return fileName.substring(pointIndex + 1);
        }
        return fileName;
    }

    /**
     * 判断文件是否是允许显示略缩图的文件类型
     */
    public static boolean isThumbnailType(String suffix) {
        return ConfigBean.getInstance().thumbnailSuffixSet.contains(suffix);
    }
}

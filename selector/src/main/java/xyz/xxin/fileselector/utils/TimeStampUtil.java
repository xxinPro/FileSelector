package xyz.xxin.fileselector.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampUtil {
    private final static String defaultPattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 通过时间戳获取具体时间
     */
    public static String timeStamp2Time(long timeStamp) {
        return timeStamp2Time(timeStamp, defaultPattern);
    }

    public static String timeStamp2Time(long timeStamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

}

package xyz.xxin.fileselector.beans;

import android.app.Activity;
import android.os.Environment;

import androidx.fragment.app.Fragment;

import java.util.Comparator;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.dao.DisplaySuffixSet;
import xyz.xxin.fileselector.dao.FileIconMap;
import xyz.xxin.fileselector.dao.SuffixFilterSet;
import xyz.xxin.fileselector.enums.SortRule;
import xyz.xxin.fileselector.interfaces.ImageEngine;
import xyz.xxin.fileselector.interfaces.OnResultCallbackListener;
import xyz.xxin.fileselector.interfaces.OnSelectCallBack;
import xyz.xxin.fileselector.style.FileSelectorFootBarStyle;
import xyz.xxin.fileselector.style.FileSelectorListViewStyle;
import xyz.xxin.fileselector.style.FileSelectorPathBarStyle;
import xyz.xxin.fileselector.style.FileSelectorTitleBarStyle;
import xyz.xxin.fileselector.style.FileSelectorViewStyle;

public class ConfigBean {
    private static volatile ConfigBean configBean;  // 单例

    public Activity activity;                       // activity启动模式使用的activity
    public Fragment fragment;                       // fragment注入模式中的fragment
    public boolean isActivityMode;                  // 是否是activity启动模式
    public boolean isEnableOnBackPressedListener;   // 是否启用返回按键监听，默认为true，只在fragment注入模式中生效

    public String initPath;                         // 初始目录
    public SortRule sortRule;                       // 排序方式
    public boolean isReverseOrder;                  // 是否逆序
    public String lastModifiedPattern;              // 文件修改时间的格式化pattern
    public ImageEngine imageEngine;                 // 加载略缩图所需的引擎
    public boolean isDisplayThumbnail;              // 图片、视频文件是否显示略缩图
    public DisplaySuffixSet thumbnailSuffixSet;     // 支持显示略缩图的文件后缀名类型集合
    public FileIconMap fileIconMap;                 // 储存文件图标
    public Comparator<FileItemBean> fileComparator; // 自定义的文件列表排序方式
    public boolean isOnlyDisplayFolder;             // 是否只显示文件夹
    public boolean isOnlySelectFile;                // 是否只能选择文件
    public SuffixFilterSet suffixFilterSet;         // 储存需要显示的文件类型
    public boolean isSingle;                        // 是否是单选模式
    public int maxSelectValue;                      // 文件最大选择数，小于0时无限选择
    public OnSelectCallBack onSelectCallBack;       // 文件选择方法回调
    public boolean isVibrator;                      // 是否开启震动
    public int statusBarColorId;                    // 状态栏颜色
    public int navigationBarColorId;                // 导航栏颜色
    public boolean statusBarIsDarkText;             // 状态栏中文字是否是黑色
    public boolean isAutoOpenSelectMode;            // 是否自动打开选择模式

    public FileSelectorViewStyle fileSelectorViewStyle;         // 根view的样式设置
    public FileSelectorTitleBarStyle fileSelectorTitleBarStyle; // 顶部栏的样式设置
    public FileSelectorPathBarStyle fileSelectorPathBarStyle;   // 路径栏的样式设置
    public FileSelectorFootBarStyle fileSelectorFootBarStyle;   // 底部栏的样式设置
    public FileSelectorListViewStyle fileSelectorListViewStyle; // 列表样式设置

    public OnResultCallbackListener onResultCallbackListener;   // 选择文件回调

    public static final String DEFAULT_FOLDER_KEY = "fs_default_folder";
    public static final String DEFAULT_FILE_KEY = "fs_default_file";

    private ConfigBean() {}

    public void init() {
        activity = null;
        fragment = null;
        isActivityMode = true;
        isEnableOnBackPressedListener = true;

        sortRule = SortRule.Name;
        isReverseOrder = false;
        lastModifiedPattern = "yyyy-MM-dd HH:mm:ss";
        initPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileComparator = null;
        onResultCallbackListener = null;

        fileIconMap = new FileIconMap();
        initIcon();

        imageEngine = null;
        isDisplayThumbnail = false;
        thumbnailSuffixSet = new DisplaySuffixSet();
        initImageDisplaySuffix();

        isOnlyDisplayFolder = false;
        isOnlySelectFile = false;
        suffixFilterSet = new SuffixFilterSet();
        maxSelectValue = -1;
        onSelectCallBack = null;
        isSingle = false;
        isVibrator = true;
        statusBarColorId = R.color.fs_blue;
        navigationBarColorId = R.color.fs_white;
        statusBarIsDarkText = false;
        isAutoOpenSelectMode = false;

        fileSelectorViewStyle = null;
        fileSelectorTitleBarStyle = null;
        fileSelectorPathBarStyle = null;
        fileSelectorFootBarStyle = null;
        fileSelectorListViewStyle = null;
    }

    private void initImageDisplaySuffix() {
        // 图片
        thumbnailSuffixSet.add("jpg", "jpe", "jpeg", "png", "webp", "gif", "tif", "tiff", "bmp", "dib", "rle", "heif", "heic");
        // 视频
        thumbnailSuffixSet.add("mp4", "avi", "mkv", "mov", "wmv", "flv", "mpeg", "mpg", "3gp", "webm", "ogg");
        // 动图
        thumbnailSuffixSet.add("gif");
    }

    private void initIcon() {
        fileIconMap.put("txt", R.drawable.fs_txt);                  // 文本
        fileIconMap.put(DEFAULT_FILE_KEY, R.drawable.fs_file);      // 未知格式文件
        fileIconMap.put(DEFAULT_FOLDER_KEY, R.drawable.fs_folder);  // 文件夹

        // 图片
        String[] imgKey = new String[] {"jpg", "jpe", "jpeg", "png", "webp", "gif", "tif", "tiff", "bmp", "dib", "rle", "heif", "heic"};
        fileIconMap.put(imgKey, R.drawable.fs_img);
        // 音乐
        String[] musicKey = new String[] {"mp3", "amr", "wav", "flac", "aac", "ogg", "wma", "m4a", "aif", "aiff", "mid", "midi"};
        fileIconMap.put(musicKey, R.drawable.fs_music);
        // 压缩包
        String[] zipKey = new String[] {"zip", "rar", "7z", "tar", "gz", "bz2", "xz", "tgz"};
        fileIconMap.put(zipKey, R.drawable.fs_zip);
        // 视频
        String[] videoKey = new String[] {"mp4", "avi", "mkv", "mov", "wmv", "flv", "mpeg", "mpg", "3gp", "webm", "ogg"};
        fileIconMap.put(videoKey, R.drawable.fs_mv);
    }

    public static ConfigBean getInstance() {
        if (configBean == null) {
            synchronized (ConfigBean.class) {
                if (configBean == null) {
                    configBean = new ConfigBean();
                }
            }
        }
        return configBean;
    }
}

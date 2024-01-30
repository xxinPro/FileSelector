package xyz.xxin.fileselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.Comparator;

import xyz.xxin.fileselector.activities.FileSelectorActivity;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.enums.SortRule;
import xyz.xxin.fileselector.fragments.FileSelectorFragment;
import xyz.xxin.fileselector.interfaces.ImageEngine;
import xyz.xxin.fileselector.interfaces.OnResultCallbackListener;
import xyz.xxin.fileselector.interfaces.OnSelectCallBack;
import xyz.xxin.fileselector.style.FileSelectorFootBarStyle;
import xyz.xxin.fileselector.style.FileSelectorListViewStyle;
import xyz.xxin.fileselector.style.FileSelectorPathBarStyle;
import xyz.xxin.fileselector.style.FileSelectorTitleBarStyle;
import xyz.xxin.fileselector.style.FileSelectorViewStyle;
import xyz.xxin.fileselector.utils.SAFUtil;

public class FileSelectConfig {
    private final ConfigBean configBean;  // 插件配置类

    FileSelectConfig(Activity activity) {
        configBean = ConfigBean.getInstance();
        configBean.init();
        configBean.activity = activity;
    }

    FileSelectConfig(Fragment fragment) {
        this(fragment.getActivity());
    }

    /**
     * 设置FileSelector底部栏样式
     *
     * @param fileSelectorFootBarStyle style控制类
     */
    public FileSelectConfig setFileSelectorFootBarStyle(FileSelectorFootBarStyle fileSelectorFootBarStyle) {
        configBean.fileSelectorFootBarStyle = fileSelectorFootBarStyle;
        return this;
    }

    /**
     * 设置FileSelector列表样式
     *
     * @param fileSelectorListViewStyle style控制类
     */
    public FileSelectConfig setFileSelectorListViewStyle(FileSelectorListViewStyle fileSelectorListViewStyle) {
        configBean.fileSelectorListViewStyle = fileSelectorListViewStyle;
        return this;
    }

    /**
     * 设置FileSelector路径显示栏的样式
     *
     * @param fileSelectorPathBarStyle style控制类
     */
    public FileSelectConfig setFileSelectorPathBatStyle(FileSelectorPathBarStyle fileSelectorPathBarStyle) {
        configBean.fileSelectorPathBarStyle = fileSelectorPathBarStyle;
        return this;
    }

    /**
     * 设置FileSelector顶部栏的样式
     *
     * @param fileSelectorTitleBarStyle style控制类
     */
    public FileSelectConfig setFileSelectorTitleBarStyle(FileSelectorTitleBarStyle fileSelectorTitleBarStyle) {
        configBean.fileSelectorTitleBarStyle = fileSelectorTitleBarStyle;
        return this;
    }

    /**
     * 设置FileSelector最下面一层view的样式
     *
     * @param fileSelectorViewStyle style控制类
     */
    public FileSelectConfig setFileSelectorViewStyle(FileSelectorViewStyle fileSelectorViewStyle) {
        configBean.fileSelectorViewStyle = fileSelectorViewStyle;
        return this;
    }

    /**
     * 设置状态栏字体颜色
     *
     * @param isDark 为true时黑色，否则是白色
     */
    public FileSelectConfig setStatusBarTextColor(boolean isDark) {
        configBean.statusBarIsDarkText = isDark;
        return this;
    }

    /**
     * 设置状态栏颜色，需要注意的是在fragment注入模式下同样生效
     *
     * @param colorId 颜色的id
     */
    public FileSelectConfig setStatusBarColor(int colorId) {
        configBean.statusBarColorId = colorId;
        return this;
    }

    /**
     * 设置虚拟导航栏的颜色，在fragment注入模式下同样生效
     *
     * @param colorId 颜色的id
     */
    public FileSelectConfig setNavigationBarColor(int colorId) {
        configBean.navigationBarColorId = colorId;
        return this;
    }

    /**
     * 是否自动打开选择模式
     *
     * @param is 为ture将在进入文件选择页面后自动开启选择文件
     */
    public FileSelectConfig isAutoOpenSelectMode(boolean is) {
        configBean.isAutoOpenSelectMode = is;
        return this;
    }

    /**
     * 是否开启震动
     *
     * @param is 为true时点击item将轻微震动设备
     */
    public FileSelectConfig isVibrator(boolean is) {
        configBean.isVibrator = is;
        return this;
    }

    /**
     * 是否是单选模式；注意，单选模式不同于最大选择数为1
     *
     * @param is 为true时开启单选模式
     */
    public FileSelectConfig isSingle(boolean is) {
        configBean.isSingle = is;
        return this;
    }

    /**
     * 设置文件的最大选择数
     *
     * @param selectMax 最大选择数
     */
    public FileSelectConfig setMaxSelectValue(int selectMax) {
        return setMaxSelectValue(selectMax, null);
    }

    /**
     * 设置文件的最大选择数，并设置选择文件的回调
     *
     * @param selectMax      最大选择数
     * @param selectCallBack 文件选择回调
     */
    public FileSelectConfig setMaxSelectValue(int selectMax, OnSelectCallBack selectCallBack) {
        configBean.maxSelectValue = selectMax;
        configBean.onSelectCallBack = selectCallBack;
        return this;
    }

    /**
     * 仅显示指定后缀名的文件类型，可以设置多个
     * 如果不添加则显示所有文件类型
     *
     * @param suffix 需要显示的文件后缀名
     */
    public FileSelectConfig addDisplayType(String... suffix) {
        configBean.suffixFilterSet.add(suffix);
        return this;
    }

    /**
     * 是否只能选中文件
     *
     * @param is 为true时，文件夹无法被选中，只能选中文件
     */
    public FileSelectConfig isOnlySelectFile(boolean is) {
        configBean.isOnlySelectFile = is;
        return this;
    }

    /**
     * 是否只显示文件夹
     *
     * @param is 为true时，列表中将只显示文件夹，不显示文件
     */
    public FileSelectConfig isOnlyDisplayFolder(boolean is) {
        configBean.isOnlyDisplayFolder = is;
        return this;
    }

    /**
     * 设置文件修改时间的格式化pattern
     *
     * @param lastModifiedPattern 文件修改时间的格式化pattern
     */
    public FileSelectConfig setLastModifiedPattern(String lastModifiedPattern) {
        configBean.lastModifiedPattern = lastModifiedPattern;
        return this;
    }

    /**
     * 是否开启视频、图片文件略缩图加载，会消耗一定的内存
     *
     * @param isDisplay 若传入true，视频、图片文件图标将会显示略缩图，同时需要设置加载引擎，否则不生效
     */
    public FileSelectConfig isDisplayThumbnail(boolean isDisplay) {
        configBean.isDisplayThumbnail = isDisplay;
        return this;
    }

    /**
     * 设置加载略缩图所需的引擎
     *
     * @param imageEngine 加载视频、图片文件略缩图所需的引擎
     */
    public FileSelectConfig setImageEngine(ImageEngine imageEngine) {
        configBean.imageEngine = imageEngine;
        return this;
    }

    /**
     * 给指定的文件后缀名添加图标，可添加多个
     *
     * @param suffix 文件后缀名，如 jpg 等
     * @param iconDrawableId 图标的drawable资源id
     */
    public FileSelectConfig addIconBySuffix(String suffix, int iconDrawableId) {
        configBean.fileIconMap.put(suffix, iconDrawableId);
        return this;
    }

    /**
     * 批量给指定的文件后缀名添加图标，这些文件公用同一个图标
     *
     * @param suffixes              文件后缀名，如 jpg 等
     * @param publicIconDrawableId  图标的drawable资源id
     */
    public FileSelectConfig addBatchesIconBySuffix(String[] suffixes, int publicIconDrawableId ) {
        configBean.fileIconMap.put(suffixes, publicIconDrawableId);
        return this;
    }

    /**
     * 批量给指定的文件后缀名添加图标，注意两个数组中的元素需要一一对应
     *
     * @param suffixes          文件后缀名，如 jpg 等
     * @param iconDrawableIds   图标的drawable资源id
     */
    public FileSelectConfig addBatchesIconBySuffix(String[] suffixes, int[] iconDrawableIds) {
        configBean.fileIconMap.put(suffixes, iconDrawableIds);
        return this;
    }

    /**
     * 设置文件夹的图标
     *
     * @param iconDrawableId 图标的drawable资源id
     */
    public FileSelectConfig setDefaultFolderIcon(int iconDrawableId) {
        configBean.fileIconMap.put(ConfigBean.DEFAULT_FOLDER_KEY, iconDrawableId);
        return this;
    }

    /**
     * 设置未知类型文件的图标
     *
     * @param iconDrawableId 图标的drawable资源id
     */
    public FileSelectConfig setDefaultFileIcon(int iconDrawableId) {
        configBean.fileIconMap.put(ConfigBean.DEFAULT_FILE_KEY, iconDrawableId);
        return this;
    }

    /**
     * 设置排序规则
     *
     * @param sortRule 排序规则
     */
    public FileSelectConfig setSortRule(SortRule sortRule) {
        return setSortRule(sortRule, false);
    }

    /**
     * 设置排序规则
     *
     * @param sortRule       排序规则
     * @param isReverseOrder 是否逆序
     */
    public FileSelectConfig setSortRule(SortRule sortRule, boolean isReverseOrder) {
        if (sortRule != null) {
            configBean.sortRule = sortRule;
        }
        configBean.isReverseOrder = isReverseOrder;
        return this;
    }

    /**
     * 自定义排序规则
     * 设置自定义排序规则后通过setSortRule设置的规则将不起作用
     *
     * @param fileComparator 排序规则
     */
    public FileSelectConfig selfSortRule(Comparator<FileItemBean> fileComparator) {
        if (fileComparator != null) {
            configBean.fileComparator = fileComparator;
        }
        return this;
    }

    /**
     * 设置文件选择器打开的起始路径
     *
     * @param path 指定路径
     */
    public FileSelectConfig setInitPath(String path) {
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        File file = new File(path);
        // 检查普通外部储存目录是否存在
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 如果存在则存在
            if (file.exists()) {
                configBean.initPath = path;
            }
            // 如果不存在有可能是Android/data/packageName下的目录
            else if (isAndroidDataChild(path)) {
                configBean.initPath = path;
            } else {
                throw new NullPointerException("指定的储存目录:" + path + "不存在");
            }
        } else if (file.exists()){
            configBean.initPath = path;
        } else {
            throw new NullPointerException("指定的储存目录:" + path + "不存在");
        }
        return this;
    }

    /**
     * 判断/storage/emulated/0/Android/data/packageName是否存在
     * 或者/storage/emulated/0/Android/obb/packageName是否存在
     */
    private boolean isAndroidDataChild(String path) {
        if (!path.startsWith(SAFUtil.ANDROID_DATA_PATH) && !path.startsWith(SAFUtil.ANDROID_OBB_PATH))
            return false;

        if (path.startsWith("/"))
            path = path.substring(1);
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        String[] split = path.split("/");
        if (split.length < 6) return false;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append("/").append(split[i]);
        }

        return new File(stringBuilder.toString()).exists();
    }

    /**
     * 启动activity页面
     *
     * @param onResultCallbackListener 回调
     */
    public void forResult(OnResultCallbackListener onResultCallbackListener) {
        configBean.isActivityMode = true;
        configBean.isEnableOnBackPressedListener = false;
        configBean.onResultCallbackListener = onResultCallbackListener;

        Intent intent = new Intent(configBean.activity, FileSelectorActivity.class);
        configBean.activity.startActivity(intent);
    }

    /**
     * 以frameLayout注入方式启动
     *
     * @param containerViewId           包含fragment的frameLayout
     * @param onResultCallbackListener  回调
     */
    public void forResult(int containerViewId, OnResultCallbackListener onResultCallbackListener) {
        forResult(containerViewId, true, onResultCallbackListener);
    }

    /**
     * 以frameLayout注入方式启动
     *
     * @param containerViewId               包含fragment的frameLayout
     * @param isEnableOnBackPressedListener 是否启用返回按键监听
     * @param onResultCallbackListener      回调
     */
    public void forResult(int containerViewId, boolean isEnableOnBackPressedListener, OnResultCallbackListener onResultCallbackListener) {
        FragmentManager fragmentManager = null;
        if (configBean.activity instanceof AppCompatActivity) {
            fragmentManager = ((AppCompatActivity) configBean.activity).getSupportFragmentManager();
        } else if (configBean.activity instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) configBean.activity).getSupportFragmentManager();
        }

        if (fragmentManager == null) {
            throw new NullPointerException("FragmentManager为空");
        }

        FileSelectorFragment fileSelectorFragment = FileSelectorFragment.newInstance();
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(fileSelectorFragment.getFragmentTag());
        if (fragmentByTag != null) {
            fragmentManager.beginTransaction().remove(fragmentByTag).commitAllowingStateLoss();
        }

        configBean.isActivityMode = false;
        configBean.isEnableOnBackPressedListener = isEnableOnBackPressedListener;
        configBean.fragment = fileSelectorFragment;
        configBean.onResultCallbackListener = onResultCallbackListener;

        fragmentManager.beginTransaction()
                .add(containerViewId, fileSelectorFragment, fileSelectorFragment.getFragmentTag())
                .addToBackStack(fileSelectorFragment.getFragmentTag())
                .commitAllowingStateLoss();
    }

}

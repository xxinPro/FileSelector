package xyz.xxin.fileselector.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.adapters.FileListAdapter;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.dao.Code;
import xyz.xxin.fileselector.dao.FirstVisiblePositionStack;
import xyz.xxin.fileselector.enums.FileItemBeanType;
import xyz.xxin.fileselector.style.FileSelectorTitleBarStyle;
import xyz.xxin.fileselector.style.FileSelectorViewStyle;
import xyz.xxin.fileselector.utils.DataFileUtil;
import xyz.xxin.fileselector.utils.ResourcesUtil;
import xyz.xxin.fileselector.utils.SAFUtil;
import xyz.xxin.fileselector.utils.FileSortUtil;
import xyz.xxin.fileselector.utils.FileUtil;
import xyz.xxin.fileselector.utils.PixelsUtil;

public class FileSelectorView extends RelativeLayout {
    public FileSelectorView(Context context) {
        super(context);
        init();
    }

    public FileSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private FirstVisiblePositionStack firstVisiblePositionStack;    // 储存上级目录浏览到的item索引

    private LinearLayoutManager layoutManager;      // 列表布局方式

    private FileSelectorListView file_selector_list_view;   // 文件列表
    private FileSelectorTitleBar file_selector_title_bar;   // 标题栏
    private FileSelectorPathBar file_selector_path_bar;     // 路径栏
    private FileSelectorFootBar file_selector_foot_bar;     // 底部功能栏

    private MyHandler handler;                      // 刷新列表UI
    private LoadListThread loadThread;              // 加载列表数据使用的进程
    private SAFUtil SAFUtil;      // documentFile操作工具
    private ConfigBean configBean;                  // 配置类
    private List<FileItemBean> fileItemBeans;       // 当前所在目录下的所有文件及文件夹对象集合

    private boolean isLoadListing = false;          // 列表是否在加载中

    private int currentFileCount = 0;               // 统计当前目录中文件的个数
    private int currentFolderCount = 0;             // 统计当前目录中文件夹的个数

    private class MyHandler extends Handler {
        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == Code.LOAD_LIST) {
                // 显示当前目录下文件夹和文件的个数
                setShowFileCount(currentFileCount);
                setShowFolderCount(currentFolderCount);

                // 判断文件夹是否为空
                if (fileItemBeans.size() > 0) {
                    // 不为空显示文件列表
                    showFileList(fileItemBeans);
                } else {
                    // 如果为空则显示空文件夹的提示
                    showEmptyFolder();
                }
                isLoadListing = false;
                // 隐藏加载等待指示器
                hideLoadIndicator();
                // 关闭刷新指示器的显示
                closeRefreshIndicator();
            } else if (msg.what == Code.LOAD_LAST) {
                // 加载列表
                this.sendEmptyMessage(Code.LOAD_LIST);
                // 将列表滚动到上个列表的第一个可见item的位置
                scrollToParentFirstVisibleItem();
            }
        }
    }

    private void init() {
        loadLayout();
        findView();
        initStyle();
        initData();
        initTitleBar();
        initPathBar();
        initFileList();
        initFootBar();
    }

    /* file list */

    private void initFileList() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        file_selector_list_view.setLayoutManager(layoutManager);
    }

    public interface OnFileListItemClickListener {
        // 在非select mode下调用的click
        void onNormalClick(FileItemBean fileItemBean, int position);
        // 在select mode下调用的click
        void onSelectClick(FileItemBean fileItemBean, int position);
    }

    public interface OnFileListItemLongClickListener{
        // 在非select mode下调用的long click
        boolean onNormalClick(FileItemBean fileItemBean, int position);
        // 在select mode下调用的long click
        boolean onSelectClick(FileItemBean fileItemBean, int position);
    }

    /**
     * 设置文件列表item click
     */
    public void setFileListItemClickListener(OnFileListItemClickListener onFileListItemClickListener) {
        if (onFileListItemClickListener == null) return;

        file_selector_list_view.setOnFileListItemClickListener(new FileListAdapter.OnFileListItemClickListener() {
            @Override
            public void onClick(FileItemBean fileItemBean, int position, boolean isStartSelect) {
                if (isStartSelect) {
                    onFileListItemClickListener.onSelectClick(fileItemBean, position);
                } else {
                    if (fileItemBean.isDirectory()) {
                        // 第一个可见item的position入栈
                        firstVisiblePositionStack.push(layoutManager.findFirstVisibleItemPosition());
                        pushPathBarData(fileItemBean);
                        // 回到顶部
                        scrollToPosition(0);
                    }
                    onFileListItemClickListener.onNormalClick(fileItemBean, position);
                }
            }
        });
    }

    /**
     * 设置文件列表item long click
     */
    public void setFileListItemLongClickListener(OnFileListItemLongClickListener onFileListItemLongClickListener) {
        if (onFileListItemLongClickListener == null) return;

        file_selector_list_view.setOnFileListItemLongClickListener(new FileListAdapter.OnFileListItemLongClickListener() {
            @Override
            public boolean onLongClick(FileItemBean fileItemBean, int position, boolean isStartSelect) {
                if (isStartSelect) {
                    return onFileListItemLongClickListener.onSelectClick(fileItemBean, position);
                }
                return onFileListItemLongClickListener.onNormalClick(fileItemBean, position);
            }
        });
    }

    /**
     * 设置列表下拉刷新监听
     */
    public void setFileListRefreshListener(FileSelectorListView.OnFileListRefreshListener onFileListRefreshListener) {
        file_selector_list_view.setOnFileListRefreshListener(onFileListRefreshListener);
    }

    /* title bar */

    private void initTitleBar() {
        FileSelectorTitleBarStyle fileSelectorTitleBarStyle = ConfigBean.getInstance().fileSelectorTitleBarStyle;
        if (fileSelectorTitleBarStyle != null) {
            // 动态设置title bar的高度
            ViewGroup.LayoutParams layoutParams = file_selector_title_bar.getLayoutParams();
            layoutParams.height = PixelsUtil.dp2px(fileSelectorTitleBarStyle.getHeight(), getContext());
        }
    }

    public void setTitleBarButtonClickListener(FileSelectorTitleBar.OnTitleClickListener onTitleClickListener) {
        file_selector_title_bar.setOnTitleClickListener(onTitleClickListener);
    }


    /* path bar */

    private void initPathBar() {

    }

    /**
     * 给path bar一个初始地址
     */
    public void initPathBarData(FileItemBean fileItemBean) {
        file_selector_path_bar.initData(fileItemBean);
    }

    public interface OnPathBarItemClickListener {
        void onItemClick(FileItemBean pointFile, int position);
    }

    public void setPathBarItemClickListener(OnPathBarItemClickListener onPathBarItemClickListener) {
        file_selector_path_bar.setOnPathBarItemClickListener(new FileSelectorPathBar.OnItemClickListener() {
            @Override
            public void onItemClick(FileItemBean pointFile, int position) {
                // 子目录与点击的所属的父目录之间所有未出栈的数据删除
                firstVisiblePositionStack.sub(position);
                onPathBarItemClickListener.onItemClick(pointFile, position);
            }
        });
    }

    /* foot bar */

    private void initFootBar() {

    }

    /**
     * 设置底部栏中两个按钮的click
     */
    public void setFootBarButtonClick(FileSelectorFootBar.OnButtonClickListener onButtonClickListener) {
        file_selector_foot_bar.setOnButtonClickListener(onButtonClickListener);
    }

    /* information */

    /**
     * 获取DocumentFile处理工具
     */
    public SAFUtil getSAFUtil() {
        return SAFUtil;
    }

    /**
     * 获取列表是否在加载过程中
     */
    public boolean isLoadListing() {
        return isLoadListing;
    }

    /**
     * 获取当前列表中文件类型的数量
     */
    public int getFileCount() {
        return currentFileCount;
    }

    /**
     * 获取当前列表中文件夹类型的数量
     */
    public int getCurrentFolderCount() {
        return currentFolderCount;
    }

    /**
     * 是否处于文件选择模式
     */
    public boolean isSelectMode() {
        return file_selector_list_view.isSelectMode();
    }

    /**
     * 获取选中的item的个数
     */
    public int getSelectedCount() {
        return file_selector_list_view.getSelectedCount();
    }

    /**
     * 获取item总数
     */
    public int getTotalCount() {
        return file_selector_list_view.getTotalCount();
    }

    /**
     * 获取可选的item总数
     * 当开启仅选择文件时，文件夹将被标注为不可选item
     */
    public int getAvailableTotalCount() {
        // 当开启仅选择文件时，把文件类型总数返回
        if (configBean.isOnlySelectFile) {
            return getFileCount();
        }
        return getTotalCount();
    }

    /**
     * 获取已选中的所有FileItemBean的集合
     */
    public List<FileItemBean> getSelectedList() {
        return file_selector_list_view.getSelectedList();
    }

    /**
     * 获取已选中的所有FileBean的集合
     */
    public List<FileBean> getFileBeanList() {
        List<FileBean> fileBeanList = new ArrayList<>();

        for (FileItemBean fileItemBean : file_selector_list_view.getSelectedList()) {
            fileBeanList.add(fileItemBean.getFileBean());
        }

        return fileBeanList;
    }

    /* action */

    public void setShowFileCount(int count) {
        file_selector_path_bar.setFileCount(count);
    }

    public void setShowFolderCount(int count) {
        file_selector_path_bar.setFolderCount(count);
    }

    /**
     * 将列表滚动到上个列表的第一个可见item的位置
     */
    public void scrollToParentFirstVisibleItem() {
        // 直接父目录的第一个可见item的position出栈
        int position = firstVisiblePositionStack.pop();
        if (position != -1) {
            file_selector_list_view.scrollToPosition(position);
        }
    }

    /**
     * 关闭刷新治时期
     */
    public void closeRefreshIndicator() {
        file_selector_list_view.closeRefresh();
    }

    /**
     * 显示文件列表，隐藏空文件夹提示
     */
    public void showFileList(List<FileItemBean> fileItemBeans) {
        file_selector_list_view.showFileList(fileItemBeans);
    }

    /**
     * 显示空文件夹提示，隐藏文件列表
     */
    public void showEmptyFolder() {
        file_selector_list_view.showEmptyFolder();
    }

    /**
     * 隐藏列表加载过程中的等待指示器，显示文件列表
     */
    public void hideLoadIndicator() {
        file_selector_list_view.hideLoad();
    }

    /**
     * 显示列表加载过程中的等待指示器，隐藏文件列表
     */
    public void showLoadIndicator() {
        file_selector_list_view.showLoad();
    }

    /**
     * 添加数据到path bar
     */
    public void pushPathBarData(FileItemBean fileItemBean) {
        file_selector_path_bar.pushData(fileItemBean);
    }

    /**
     * 移除path bar中的最后一个数据
     */
    public FileItemBean popPathBarData() {
        return file_selector_path_bar.popData();
    }

    /**
     * 设置文件列表的下拉刷新
     */
    public void setRefreshEnabled(boolean enabled) {
        file_selector_list_view.setRefreshEnabled(enabled);
    }

    /**
     * 进入文件选择模式
     */
    public void startSelectMode() {
        file_selector_list_view.startSelectMode();
        file_selector_title_bar.startSelectMode();
        file_selector_path_bar.hide();
        file_selector_foot_bar.animShow();
    }

    /**
     * 结束文件选择模式
     */
    public void endSelectMode() {
        file_selector_list_view.endSelectMode();
        file_selector_title_bar.endSelectMode();
        file_selector_path_bar.show();
        file_selector_foot_bar.animHide();

        // 取消选中所有
        deselectAll();
    }

    /**
     * 取消选中指定的item
     * 注意：开启单选模式后该方法不生效
     */
    public void deselect(int position) {
        // 没有可选item或开启了单选时不执行
        if (getAvailableTotalCount() <= 0 || configBean.isSingle) return;

        file_selector_list_view.deselectItem(position);

        if (getSelectedCount() < getAvailableTotalCount()) {
            file_selector_foot_bar.beforeSelectedAll();
        }
    }

    /**
     * 选中指定的item
     */
    public void select(int position) {
        // 没有可选item时不执行
        if (getAvailableTotalCount() <= 0) return;

        // 如果开启了单选模式
        if (configBean.isSingle) {
            // 则取消选择上个item
            int previousPosition = file_selector_list_view.getPreviousPosition();
            if (previousPosition >= 0) {
                file_selector_list_view.deselectItem(previousPosition);
            }
        }
        // 如果指定了最大选择数
        else if (configBean.maxSelectValue >= 0) {
            // 回调
            if (configBean.onSelectCallBack != null)
                configBean.onSelectCallBack.callBack(configBean.maxSelectValue, getSelectedCount());

            // 当前选择数达到最大选择数时，不再选中item
            if (getSelectedCount() >= configBean.maxSelectValue)
                return;
        }

        file_selector_list_view.selectItem(position);

        if (getSelectedCount() == getAvailableTotalCount()) {
            file_selector_foot_bar.afterSelectedAll();
        }
    }

    /**
     * 全选所有item
     */
    public void selectAll() {
        // 没有可选item、开启最大选择数或开启了单选模式时不执行
        if (getAvailableTotalCount() <= 0 || configBean.maxSelectValue >= 0 || configBean.isSingle) return;

        file_selector_list_view.selectAll();
        // 设置底部栏右边button的text
        file_selector_foot_bar.afterSelectedAll();
    }

    /**
     * 取消选中所有item
     */
    public void deselectAll() {
        // 没有可选item、开启最大选择数或开启了单选模式时不执行
        if (getAvailableTotalCount() <= 0 || configBean.maxSelectValue >= 0 || configBean.isSingle) return;

        file_selector_list_view.deselectAll();
        // 设置底部栏左边button的text
        file_selector_foot_bar.beforeSelectedAll();
    }

    /**
     * 将指定item置顶显示
     */
    public void scrollToPosition(int position) {
        file_selector_list_view.scrollToPosition(position);
    }

    /* initialization */

    private void initData() {
        firstVisiblePositionStack = new FirstVisiblePositionStack();
        handler = new MyHandler(Looper.myLooper());
        configBean = ConfigBean.getInstance();
    }

    private void findView() {
        file_selector_list_view = findViewById(R.id.file_refresh_list);
        file_selector_title_bar = findViewById(R.id.file_title_bar);
        file_selector_path_bar = findViewById(R.id.file_path_bar);
        file_selector_foot_bar = findViewById(R.id.file_foot_bar);
    }

    private void loadLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.fs_select_view, this);
    }

    private void initStyle() {
        FileSelectorViewStyle fileSelectorViewStyle = ConfigBean.getInstance().fileSelectorViewStyle;
        if (fileSelectorViewStyle != null) {
            // 背景色
            setBackgroundColor(ResourcesUtil.getColorById(getContext(), fileSelectorViewStyle.getBackgroundColorId()));
            // 背景图
            int backgroundDrawableId = fileSelectorViewStyle.getBackgroundDrawableId();
            if (backgroundDrawableId != -1) {
                setBackground(ResourcesUtil.getDrawableById(getContext(), backgroundDrawableId));
            }
        } else {
            setBackgroundColor(ResourcesUtil.getColorById(getContext(), R.color.fs_white));
        }
    }

    /* load list */

    /**
     * 加载列表数据
     */
    public void loadListData(FileItemBean openFileItemBean, int loadType, Activity activity) {
        if (openFileItemBean == null || openFileItemBean.isFile()) return;

        isLoadListing = true;

        // 隐藏文件列表，显示加载等待指示器
        showLoadIndicator();

        currentFileCount = 0;
        currentFolderCount = 0;

        // 载入过程中设置文件夹和文件的个数为0
        // setShowFileCount(0);
        // setShowFolderCount(0);

        // 中断上个线程
        if (loadThread != null)
            loadThread.interrupt();

        loadThread = new LoadListThread(openFileItemBean, loadType, activity);
        loadThread.start();
    }

    private class LoadListThread extends Thread {
        private final FileItemBean openFileItemBean;
        private final int loadType;
        private final Activity activity;

        public LoadListThread(FileItemBean openFileItemBean, int loadType, Activity activity) {
            this.activity = activity;
            this.loadType = loadType;
            this.openFileItemBean = openFileItemBean;
        }

        @Override
        public void run() {
            super.run();
            List<FileItemBean> _fileItemBeans = new ArrayList<>();

            // 对documentFile目录处理
            if (openFileItemBean.getBeanType() == FileItemBeanType.DOCUMENT_FILE) {
                DocumentFile[] files = openFileItemBean.getDocumentFile().listFiles();

                for (DocumentFile file : files) {
                    if (Thread.currentThread().isInterrupted()) break;

                    FileItemBean fileItemBean = new FileItemBean(file);
                    if (fileItemBean.isDirectory()) {
                        currentFolderCount++;
                        _fileItemBeans.add(fileItemBean);
                    } else if (!configBean.isOnlyDisplayFolder) {
                        // 仅添加需要显示的文件类型
                        if (configBean.suffixFilterSet.isEmpty() || configBean.suffixFilterSet.contains(FileUtil.getSuffix(fileItemBean.getFileName()))) {
                            currentFileCount++;
                            _fileItemBeans.add(fileItemBean);
                        }
                    }
                }
            }
            // 对普通外部储存目录的处理
            else if (openFileItemBean.getBeanType() == FileItemBeanType.EXTERNAL_FILE) {
                File[] files = openFileItemBean.getFile().listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (Thread.currentThread().isInterrupted()) break;

                        FileItemBean fileItemBean = new FileItemBean(file);
                        if (fileItemBean.isDirectory()) {
                            currentFolderCount++;
                            _fileItemBeans.add(fileItemBean);
                        } else if (!configBean.isOnlyDisplayFolder) {
                            // 仅添加需要显示的文件类型
                            if (configBean.suffixFilterSet.isEmpty() || configBean.suffixFilterSet.contains(FileUtil.getSuffix(fileItemBean.getFileName()))) {
                                currentFileCount++;
                                _fileItemBeans.add(fileItemBean);
                            }
                        }
                    }
                }
            }
            // 对Android/data、Android/obb目录及其直接、间接子目录的处理
            else {
                String filePath = openFileItemBean.getFile().getAbsolutePath();
                // 对于Android 13及之后的系统，无法向SAF框架申请Android/data或Android/obb目录的访问权限，所以这两个目录会返回true
                if (isSAFPermission(filePath)) {
                    // Android 13及之后的系统返回true需要特殊判断
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            (openFileItemBean.getBeanType() == FileItemBeanType.DATA_DIR ||
                                    openFileItemBean.getBeanType() == FileItemBeanType.OBB_DIR)) {

                        if (openFileItemBean.getBeanType() == FileItemBeanType.DATA_DIR) {
                            _fileItemBeans = DataFileUtil.getDataFileList(getContext());
                        } else {
                            _fileItemBeans = DataFileUtil.getObbFileList(getContext());
                        }
                        // 文件数统计
                        currentFolderCount = _fileItemBeans.size();
                    } else {
                        // 拿到目录的DocumentFile对象
                        DocumentFile documentFile = SAFUtil.getDocumentFile(filePath, false);

                        // 获取所有子文件
                        DocumentFile[] listFiles = documentFile.listFiles();

                        for (DocumentFile file : listFiles) {
                            if (Thread.currentThread().isInterrupted()) break;

                            FileItemBean fileItemBean = new FileItemBean(file);
                            if (fileItemBean.isDirectory()) {
                                currentFolderCount++;
                                _fileItemBeans.add(fileItemBean);
                            } else if (!configBean.isOnlyDisplayFolder) {
                                // 仅添加需要显示的文件类型
                                if (configBean.suffixFilterSet.isEmpty() || configBean.suffixFilterSet.contains(FileUtil.getSuffix(fileItemBean.getFileName()))) {
                                    currentFileCount++;
                                    _fileItemBeans.add(fileItemBean);
                                }
                            }
                        }
                    }
                } else if (configBean.isActivityMode) {
                    SAFUtil.requestPermission(activity, Code.REQUEST_DATA_OBB_LOAD);
                } else {
                    SAFUtil.requestPermission(configBean.fragment, Code.REQUEST_DATA_OBB_LOAD);
                }
            }

            // 如果线程被中断就不需要再排序更新列表了
            if (!Thread.currentThread().isInterrupted()) {
                sortFile(loadType, _fileItemBeans);
            }
        }
    }

    /**
     * 排序文件列表
     */
    private void sortFile(int loadType, List<FileItemBean> _fileItemBeans) {
        // 判断是否启用了自定义排序
        if (configBean.fileComparator != null) {
            this.fileItemBeans = FileSortUtil.sort(_fileItemBeans, configBean.fileComparator);
        } else {
            switch (configBean.sortRule) {
                case Name:  // 根据文件名排序
                    this.fileItemBeans = FileSortUtil.sortByName(_fileItemBeans);
                    break;
                case Time:  // 根据文件修改时间排序
                    this.fileItemBeans = FileSortUtil.sortByTime(_fileItemBeans);
                    break;
                case Size:  // 根据文件大小排序
                    this.fileItemBeans = FileSortUtil.sortBySize(_fileItemBeans);
                    break;
            }
        }

        // 发送通知，更新列表
        handler.sendEmptyMessage(loadType);
    }

    /**
     * 判断指定目录是否有SAF框架访问权限
     */
    public boolean isSAFPermission(String filePath) {
        // Android 10 及之前的系统版本中不需要申请SAF框架访问权限
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q)
            return true;

        // 如果不是Android/data文件夹，也不是Android/obb文件夹，不需要申请SAF框架访问权限
        if (!filePath.startsWith(SAFUtil.ANDROID_DATA_PATH) && !filePath.startsWith(SAFUtil.ANDROID_OBB_PATH))
            return true;

        // 移除目录头尾的斜杠
        if (filePath.startsWith("/"))
            filePath = filePath.substring(1);
        if (filePath.endsWith("/"))
            filePath = filePath.substring(0, filePath.length() - 1);

        // 将路径根据斜杠划分为数组
        String[] splitPathName = filePath.split("/");

        int loopCount; // 代表后面循环的次数

        // Android 13 及以上的版本必须单独授予权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 循环6次，取到Android/data的直接子目录
            loopCount = 6;
        } else {
            // 循环5次，取到Android/data目录
            loopCount = 5;
        }

        // 数组的长度不满足循环的次数，说明传入的路径不对
        if (splitPathName.length < loopCount)
            return true;

        // 累加路径
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < loopCount; i++) {
            stringBuilder.append("/").append(splitPathName[i]);
        }

        // 使用路径得到对应的documentFile工具
        SAFUtil = new SAFUtil(getContext(), stringBuilder.toString());

        // 是否对该路径拥有SAF框架访问权限
        return SAFUtil.isPermission();
    }
}

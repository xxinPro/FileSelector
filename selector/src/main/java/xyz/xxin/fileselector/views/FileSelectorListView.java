package xyz.xxin.fileselector.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.adapters.FileListAdapter;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.style.FileSelectorListViewStyle;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class FileSelectorListView extends LinearLayout {
    private SwipeRefreshLayout swipeRefreshLayout;  // 下拉刷新控件，文件列表和空文件夹提示都包含在其中
    private LinearLayout load_wait;                 // 加载等待页
    private TextView load_progress_description;     // 加载过程的提示
    private ProgressBar load_progress_bar;          // 加载进度条
    private RecyclerView file_list_view;            // 文件列表页
    private TextView folder_empty;                  // 空文件夹提示页

    private FileListAdapter fileListAdapter;        // recyclerView的适配器
    private LinearLayoutManager layoutManager;      // 布局管理器

    private int previousPosition = -1;                   // 上一个选中的item的position

    public FileSelectorListView(Context context) {
        super(context);
        init();
    }

    public FileSelectorListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileSelectorListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        loadLayout();
        findView();
        initStyle();
        initList();
    }

    /**
     * 初始化recyclerView
     */
    private void initList() {
        // 绑定adapter
        fileListAdapter = new FileListAdapter(getContext());
        file_list_view.setAdapter(fileListAdapter);

        // 列表显示滚动条
        int scrollBarWidth = (int)(5 * getResources().getDisplayMetrics().density + 0.5f);
        file_list_view.setScrollBarSize(scrollBarWidth);
        file_list_view.setVerticalScrollBarEnabled(true);
    }

    public interface OnFileListRefreshListener {
        void onRefresh();
    }

    /**
     * 列表下拉刷新完毕的监听
     */
    public void setOnFileListRefreshListener(OnFileListRefreshListener onFileListRefreshListener) {
        if (onFileListRefreshListener != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onFileListRefreshListener.onRefresh();
                }
            });
        }
    }

    /**
     * 列表item的click
     */
    public void setOnFileListItemClickListener(FileListAdapter.OnFileListItemClickListener onFileListItemClickListener) {
        fileListAdapter.setOnFileListItemClickListener(onFileListItemClickListener);
    }

    /**
     * 列表item的long click
     */
    public void setOnFileListItemLongClickListener(FileListAdapter.OnFileListItemLongClickListener onFileListItemLongClickListener) {
        fileListAdapter.setOnFileListItemLongClickListener(onFileListItemLongClickListener);
    }

    /**
     * 设置列表的layoutManager
     */
    public void setLayoutManager(LinearLayoutManager layoutManager) {
        file_list_view.setLayoutManager(layoutManager);

        this.layoutManager = layoutManager;
    }

    /**
     * 移动到指定item
     */
    public void scrollToPosition(int position) {
        file_list_view.scrollToPosition(position);
        layoutManager.scrollToPositionWithOffset(position, 0);
    }

    /**
     * 调用该方法停止刷新指示器
     */
    public void closeRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 显示加载等待页，隐藏文件列表显示
     */
    public void showLoad() {
        load_wait.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏加载等待页，显示文件列表
     */
    public void hideLoad() {
        load_wait.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示空文件夹提示，隐藏文件列表
     */
    public void showEmptyFolder() {
        folder_empty.setVisibility(View.VISIBLE);
        file_list_view.setVisibility(View.GONE);
    }

    /**
     * 显示文件列表，隐藏空文件夹提示
     */
    public void showFileList(List<FileItemBean> fileItemBeans) {
        // 隐藏空文件夹的提示
        folder_empty.setVisibility(View.GONE);
        // 显示文件列表
        file_list_view.setVisibility(View.VISIBLE);
        // 刷新列表
        fileListAdapter.notifyData(fileItemBeans);
        // 列表加载动画
        showAnim();
    }

    /**
     * 显示列表加载动画
     */
    private void showAnim() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fs_fade_in);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        layoutAnimationController.setDelay(0.1f);
        file_list_view.setLayoutAnimation(layoutAnimationController);
    }

    /**
     * 获取上一个选中的item的position
     */
    public int getPreviousPosition() {
        return previousPosition;
    }

    /**
     * 开启文件选中模式
     */
    public void startSelectMode() {
        fileListAdapter.setSelectMode(true);
        setRefreshEnabled(false);
    }

    /**
     * 结束文件选中模式
     */
    public void endSelectMode() {
        fileListAdapter.setSelectMode(false);
        setRefreshEnabled(true);
    }

    /**
     * 是否开启了文件选中模式
     */
    public boolean isSelectMode() {
        return fileListAdapter.isSelectMode();
    }

    /**
     * 选中指定position的item
     */
    public void selectItem(int position) {
        previousPosition = position;
        fileListAdapter.setSelectItem(position, true);
    }

    /**
     * 取消选中指定position的item
     */
    public void deselectItem(int position) {
        fileListAdapter.setSelectItem(position, false);
    }

    /**
     * 全选
     */
    public void selectAll() {
        fileListAdapter.setSelectAll(true);
    }

    /**
     * 全不选
     */
    public void deselectAll() {
        fileListAdapter.setSelectAll(false);
    }

    /**
     * 获取选中的item的个数
     */
    public int getSelectedCount() {
        return fileListAdapter.getSelectedCount();
    }

    /**
     * 获取选中的所有item的FileItemBean
     */
    public List<FileItemBean> getSelectedList() {
        return fileListAdapter.getSelectedList();
    }

    /**
     * 获取item的总数
     */
    public int getTotalCount() {
        return fileListAdapter.getItemCount();
    }

    /**
     * 启用或禁用下拉刷新
     */
    public void setRefreshEnabled(boolean enabled) {
        swipeRefreshLayout.setEnabled(enabled);
    }

    /**
     * 绑定view
     */
    private void findView() {
        file_list_view = findViewById(R.id.file_list_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        folder_empty = findViewById(R.id.folder_empty);
        load_wait = findViewById(R.id.load_bar);
        load_progress_description = findViewById(R.id.load_progress_description);
        load_progress_bar = findViewById(R.id.load_progress_bar);
    }

    /**
     * 绑定layout
     */
    private void loadLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.fs_list_view, this);
    }

    private void initStyle() {
        FileSelectorListViewStyle fileSelectorListViewStyle = ConfigBean.getInstance().fileSelectorListViewStyle;
        if (fileSelectorListViewStyle != null) {
            setBackgroundColor(getColorById(fileSelectorListViewStyle.getBackgroundColorId()));
            // 文件夹为空的提示
            folder_empty.setText(fileSelectorListViewStyle.getEmptyFolderDescription());
            folder_empty.setTextColor(getColorById(fileSelectorListViewStyle.getEmptyFolderDescriptionTextColorId()));
            folder_empty.setTextSize(fileSelectorListViewStyle.getEmptyFolderDescriptionTextSize());
            folder_empty.setBackgroundColor(getColorById(fileSelectorListViewStyle.getEmptyFolderBackgroundColorId()));
            // 加载等待页
            load_wait.setBackgroundColor(getColorById(fileSelectorListViewStyle.getLoadProgressBackgroundColorId()));
            load_progress_description.setText(fileSelectorListViewStyle.getLoadProgressDescription());
            load_progress_description.setTextColor(getColorById(fileSelectorListViewStyle.getLoadProgressDescriptionTextColorId()));
            load_progress_description.setTextSize(fileSelectorListViewStyle.getLoadProgressDescriptionTextSize());
            // 设置加载进度条的颜色
            Drawable indeterminateDrawable = load_progress_bar.getIndeterminateDrawable();
            indeterminateDrawable.setColorFilter(getColorById(fileSelectorListViewStyle.getLoadProgressBarColorId()), PorterDuff.Mode.SRC_IN);
            load_progress_bar.setProgressDrawable(indeterminateDrawable);
            // 设置下拉刷新进度条的颜色
            swipeRefreshLayout.setColorSchemeColors(getColorById(fileSelectorListViewStyle.getLoadProgressBarColorId()));
            // 给item添加分割线
            if (fileSelectorListViewStyle.getItemDecoration() != null) {
                file_list_view.addItemDecoration(fileSelectorListViewStyle.getItemDecoration());
            }
        } else {
            setBackgroundColor(getColorById(R.color.fs_white));
            // 设置下拉刷新进度条的颜色
            swipeRefreshLayout.setColorSchemeColors(getColorById(R.color.fs_blue));
        }
    }

    private int getColorById(int colorId) {
        return ResourcesUtil.getColorById(getContext(), colorId);
    }

    private Drawable getDrawableById(int drawableId) {
        return ResourcesUtil.getDrawableById(getContext(), drawableId);
    }
}

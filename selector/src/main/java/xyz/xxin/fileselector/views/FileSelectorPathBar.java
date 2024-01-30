package xyz.xxin.fileselector.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.adapters.PathListAdapter;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.style.FileSelectorPathBarStyle;
import xyz.xxin.fileselector.utils.DeviceUtil;
import xyz.xxin.fileselector.utils.SAFUtil;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class FileSelectorPathBar extends LinearLayout {
    private TextView head_path;
    private ImageView arrow;
    private RecyclerView show_path_list;
    private TextView folder_count;
    private TextView file_count;
    private TextView folder_count_description;
    private TextView file_count_description;
    private View separator;

    private FileItemBean initFileItemBean;      // 路径列表中的第一个item代表的FileBean

    private PathListAdapter pathListAdapter;

    public FileSelectorPathBar(Context context) {
        super(context);
        init();
    }

    public FileSelectorPathBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileSelectorPathBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initLayout();
        findView();
        initStyle();
        initList();
    }

    /**
     * 初始化FilePathBar的数据，需要传入文件选择器打开的第一个目录
     */
    public void initData(FileItemBean initFileItemBean) {
        if (initFileItemBean == null) return;

        this.initFileItemBean = initFileItemBean;

        String textContent = initFileItemBean.getFileName();

        // 判断路径是否是内部储存设备
        if (!initFileItemBean.isDocumentFileType() &&
                initFileItemBean.getFile().getPath().equals(SAFUtil.PRIMARY_STORAGE))
            textContent = "内部储存设备";

        head_path.setText(textContent);
    }

    public interface OnItemClickListener {
        void onItemClick(FileItemBean pointFile, int position);
    }

    /**
     * path bar中item的click
     */
    public void setOnPathBarItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) return;

        // 初始地址的click
        head_path.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtil.vibrateDevice(getContext(), 1);
                // 将init_path视作是列表中的首item，所以init_path的position是0
                onItemClickListener.onItemClick(initFileItemBean, 0);
                // 点击init_path清空全部列表数据即可，因为initFileBean不会被清空
                subAllData();
            }
        });

        pathListAdapter.setOnPathItemClickListener(new PathListAdapter.OnPathItemClickListener() {
            @Override
            public void onClickListener(FileItemBean pointFile, int position) {
                DeviceUtil.vibrateDevice(getContext(), 1);
                // 因为root_path的存在，所以列表中所有item的position从1开始，故此将adapter中给的position+1
                onItemClickListener.onItemClick(pointFile, position + 1);
                // 删除指定position之后的所有数据
                subData(position);
            }
        });
    }

    /**
     * 设置显示的文件个数
     */
    public void setFileCount(int count) {
        file_count.setText(String.valueOf(count));
    }

    /**
     * 设置显示的文件夹个数
     */
    public void setFolderCount(int count) {
        folder_count.setText(String.valueOf(count));
    }

    /**
     * 添加数据
     */
    public void pushData(FileItemBean fileItemBean) {
        pathListAdapter.pushData(fileItemBean);
        show_path_list.scrollToPosition(pathListAdapter.getItemCount() - 1);
    }

    /**
     * 删减最后一个元素
     */
    public FileItemBean popData() {
        return pathListAdapter.popData();
    }

    /**
     * 删除指定position之后的所有数据（不包括position）
     * 由于将root_path视作是列表中的首item，所以列表中所有item的position从1开始
     * 故此调用adapter删除数据时把position-1
     */
    private void subData(int position) {
        pathListAdapter.subData(position);
    }

    /**
     * 清空adapter中所有item
     */
    private void subAllData() {
        pathListAdapter.subAllData();
    }

    /**
     * 显示
     */
    public void show() {
        this.setVisibility(VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fs_top_in);
        this.setAnimation(animation);
    }

    /**
     * 隐藏
     */
    public void hide() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fs_top_out);
        this.setAnimation(animation);
        this.setVisibility(GONE);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        show_path_list.setLayoutManager(linearLayoutManager);

        pathListAdapter = new PathListAdapter(getContext());
        show_path_list.setAdapter(pathListAdapter);
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.fs_path_bar, this);
        setOrientation(VERTICAL);
    }

    private void findView() {
        head_path = findViewById(R.id.head_path);
        arrow = findViewById(R.id.arrow);
        show_path_list = findViewById(R.id.show_path_list);
        folder_count_description = findViewById(R.id.folder_count_description);
        folder_count = findViewById(R.id.folder_count);
        file_count_description = findViewById(R.id.file_count_description);
        file_count = findViewById(R.id.file_count);
        separator = findViewById(R.id.separator);
    }

    private void initStyle() {
        FileSelectorPathBarStyle fileSelectorPathBarStyle = ConfigBean.getInstance().fileSelectorPathBarStyle;
        if (fileSelectorPathBarStyle != null) {
            setBackgroundColor(getColorById(fileSelectorPathBarStyle.getBackgroundColorId()));
            // 设置head_path的背景颜色
            Drawable drawable = head_path.getBackground().mutate();
            drawable.setColorFilter(ContextCompat.getColor(getContext(), fileSelectorPathBarStyle.getHeadItemBackgroundColorId()), PorterDuff.Mode.SRC_IN);
            head_path.setBackground(drawable);
            // 设置head_path的字体颜色
            head_path.setTextColor(getColorById(fileSelectorPathBarStyle.getHeadItemTextColorId()));
            // 设置head_path的字体大小
            head_path.setTextSize(fileSelectorPathBarStyle.getItemTextSize());
            // 设置箭头image
            arrow.setImageDrawable(getDrawableById(fileSelectorPathBarStyle.getArrowImageDrawableId()));
            // 设置文件和文件夹个数的描述
            file_count_description.setText(fileSelectorPathBarStyle.getFileCountDescription());
            folder_count_description.setText(fileSelectorPathBarStyle.getFolderCountDescription());
            // 设置文件文件夹个数及描述的字体颜色和字体大小
            file_count.setTextColor(getColorById(fileSelectorPathBarStyle.getDescriptionColorId()));
            folder_count.setTextColor(getColorById(fileSelectorPathBarStyle.getDescriptionColorId()));
            file_count_description.setTextColor(getColorById(fileSelectorPathBarStyle.getDescriptionColorId()));
            folder_count_description.setTextColor(getColorById(fileSelectorPathBarStyle.getDescriptionColorId()));
            separator.setBackgroundColor(getColorById(fileSelectorPathBarStyle.getDescriptionColorId()));

            file_count.setTextSize(fileSelectorPathBarStyle.getDescriptionTextSize());
            folder_count.setTextSize(fileSelectorPathBarStyle.getDescriptionTextSize());
            file_count_description.setTextSize(fileSelectorPathBarStyle.getDescriptionTextSize());
            folder_count_description.setTextSize(fileSelectorPathBarStyle.getDescriptionTextSize());
        } else {
            setBackgroundColor(getColorById(R.color.fs_white));
        }
    }

    private int getColorById(int colorId) {
        return ResourcesUtil.getColorById(getContext(), colorId);
    }

    private Drawable getDrawableById(int drawableId) {
        return ResourcesUtil.getDrawableById(getContext(), drawableId);
    }
}

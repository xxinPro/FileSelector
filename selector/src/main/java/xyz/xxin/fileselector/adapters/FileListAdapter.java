package xyz.xxin.fileselector.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.style.FileSelectorListViewStyle;
import xyz.xxin.fileselector.utils.DeviceUtil;
import xyz.xxin.fileselector.utils.FileUtil;
import xyz.xxin.fileselector.utils.ResourcesUtil;
import xyz.xxin.fileselector.utils.SizeUtil;
import xyz.xxin.fileselector.utils.TimeStampUtil;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private List<FileItemBean> fileItemBeanList;    // 列表数据集合
    private final List<FileItemBean> selectList;    // 选中的数据集合
    private final Context context;

    private boolean isSelectMode = false;   // 是否是选择模式

    public FileListAdapter(Context context) {
        fileItemBeanList = new ArrayList<>();
        selectList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fs_list_file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListAdapter.ViewHolder holder, int position) {
        FileItemBean fileItemBean = fileItemBeanList.get(position);
        ConfigBean configBean = ConfigBean.getInstance();

        String fileName = fileItemBean.getFileName();

        holder.name.setText(fileName);
        holder.time.setText(TimeStampUtil.timeStamp2Time(fileItemBean.getLastModified(), configBean.lastModifiedPattern));

        if (fileItemBean.isDirectory()) {
            // 不显示文件大小
            holder.size.setVisibility(View.GONE);

            // 设置文件夹图标
            Integer fs_folder_id = configBean.fileIconMap.get(ConfigBean.DEFAULT_FOLDER_KEY);
            holder.icon.setImageDrawable(context.getDrawable(fs_folder_id));
        } else {
            // 显示文件大小
            holder.size.setVisibility(View.VISIBLE);
            holder.size.setText(SizeUtil.getSize(fileItemBean.getFileLength()));

            // 获取后缀名
            String fileSuffix = FileUtil.getSuffix(fileName);

            // 获取对应图标drawable ID
            Integer fs_file_id = configBean.fileIconMap.get(fileSuffix);
            if (fs_file_id == null) {
                // 默认文件图标
                fs_file_id = configBean.fileIconMap.get(ConfigBean.DEFAULT_FILE_KEY);
            }

            // 判断是否符合略缩图加载条件
            if (isLoadThumbnail(fileSuffix)) {
                // 对File和DocumentFile类型略缩图做不同的处理
                if (fileItemBean.isDocumentFileType()) {
                    configBean.imageEngine
                            .loadDocumentFileImage(context, fileItemBean.getDocumentFile(), holder.icon, fs_file_id);
                } else {
                    configBean.imageEngine
                            .loadFileImage(context, fileItemBean.getFile(), holder.icon, fs_file_id);
                }
            } else {
                // 无需加载略缩图，设置图标
                holder.icon.setImageDrawable(context.getDrawable(fs_file_id));
            }
        }

        // 进入选择模式
        if (isSelectMode) {
            // 是否设置了只能选择文件
            if (configBean.isOnlySelectFile && fileItemBean.isDirectory()) {
                holder.check_box.setVisibility(View.GONE);
                holder.in_dir_indicator.setVisibility(View.VISIBLE);
            } else {
                holder.check_box.setVisibility(View.VISIBLE);
                holder.check_box.setChecked(fileItemBean.isSelected());
                // 隐藏进入目录的指示器
                holder.in_dir_indicator.setVisibility(View.GONE);
            }
        }
        // 退出选择模式
        else {
            holder.check_box.setVisibility(View.GONE);
            // 显示进入目录的指示器
            if (fileItemBean.isDirectory()) {
                holder.in_dir_indicator.setVisibility(View.VISIBLE);
            } else {
                holder.in_dir_indicator.setVisibility(View.GONE);
            }
        }

        // item按下时显示缩小效果
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 按下时缩小效果
                        v.setScaleX(0.98f);
                        v.setScaleY(0.96f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 松开时恢复原状
                        v.setScaleX(1.0f);
                        v.setScaleY(1.0f);
                        break;
                }
                return false;
            }
        });

        // click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果自定义了click事件
                if (onFileListItemClickListener != null) {
                    DeviceUtil.vibrateDevice(context, 1);
                    onFileListItemClickListener.onClick(fileItemBean, holder.getAdapterPosition(), isSelectMode);
                }
            }
        });

        // long click
        if (onFileListItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DeviceUtil.vibrateDevice(context, 1);
                    return onFileListItemLongClickListener.onLongClick(fileItemBean, holder.getAdapterPosition(), isSelectMode);
                }
            });
        }
    }

    /**
     * 开启了略缩图显示，并且加载引擎存在，并且文件类型属于目标类型
     */
    private boolean isLoadThumbnail(String fileSuffix) {
        return FileUtil.isThumbnailType(fileSuffix) &&
                ConfigBean.getInstance().isDisplayThumbnail && ConfigBean.getInstance().imageEngine != null;
    }

    /**
     * 是否开启了文件选中模式
     */
    public boolean isSelectMode() {
        return isSelectMode;
    }

    /**
     * 设置选中模式的开关
     */
    public void setSelectMode(boolean select) {
        DeviceUtil.vibrateDevice(context, 1);
        isSelectMode = select;
        notifyDataSetChanged();
    }

    /**
     * 设置指定item的选中状态
     */
    public void setSelectItem(int position, boolean isSelect) {
        // 防止越界
        if (position >= fileItemBeanList.size() || position < 0) return;

        FileItemBean fileItemBean = fileItemBeanList.get(position);

        // 仅选择文件模式下，禁止选择文件夹
        if (ConfigBean.getInstance().isOnlySelectFile && fileItemBean.isDirectory())
            return;

        // 仅显示文件夹模式下，禁止选择文件
        if (ConfigBean.getInstance().isOnlyDisplayFolder && fileItemBean.isFile())
            return;

        boolean fileBeanSelected = fileItemBean.isSelected();

        if (!fileBeanSelected && isSelect) {
            selectList.add(fileItemBean);
        } else if (fileBeanSelected && !isSelect) {
            selectList.remove(fileItemBean);
        }

        fileItemBean.setSelected(isSelect);

        notifyDataSetChanged();
    }

    /**
     * 设置所有item的选中状态
     */
    public void setSelectAll(boolean isSelect) {
        selectList.clear();

        for (FileItemBean fileItemBean : fileItemBeanList) {
            if (!ConfigBean.getInstance().isOnlySelectFile || fileItemBean.isFile()) {
                fileItemBean.setSelected(isSelect);
                if (isSelect) {
                    selectList.add(fileItemBean);
                }
            }
        }

        notifyDataSetChanged();
    }

    /**
     * 获取选中的item的个数
     */
    public int getSelectedCount() {
        return selectList.size();
    }

    /**
     * 获取选中的所有item的FileBean
     */
    public List<FileItemBean> getSelectedList() {
        return selectList;
    }

    /**
     * 更新列表数据
     */
    public void notifyData(List<FileItemBean> fileItemBeanList) {
        this.fileItemBeanList = fileItemBeanList;
        notifyDataSetChanged();
    }

    public interface OnFileListItemClickListener {
        void onClick(FileItemBean fileItemBean, int position, boolean isStartSelect);
    }
    public interface OnFileListItemLongClickListener {
        boolean onLongClick(FileItemBean fileItemBean, int position, boolean isStartSelect);
    }

    private OnFileListItemClickListener onFileListItemClickListener;
    private OnFileListItemLongClickListener onFileListItemLongClickListener;

    public void setOnFileListItemClickListener(OnFileListItemClickListener onFileListItemClickListener) {
        this.onFileListItemClickListener = onFileListItemClickListener;
    }

    public void setOnFileListItemLongClickListener(OnFileListItemLongClickListener onFileListItemLongClickListener) {
        this.onFileListItemLongClickListener = onFileListItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return fileItemBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView time;
        private final TextView size;
        private final ImageView icon;
        private final ImageView in_dir_indicator;
        private final CheckBox check_box;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.file_name);
            time = itemView.findViewById(R.id.file_time);
            size = itemView.findViewById(R.id.file_size);
            icon = itemView.findViewById(R.id.file_icon);
            in_dir_indicator = itemView.findViewById(R.id.in_dir_indicator);
            check_box = itemView.findViewById(R.id.check_box);

            FileSelectorListViewStyle fileSelectorListViewStyle = ConfigBean.getInstance().fileSelectorListViewStyle;
            if (fileSelectorListViewStyle != null && fileSelectorListViewStyle.getFileSelectorListItemStyle() != null) {
                FileSelectorListViewStyle.FileSelectorListItemStyle fileFileSelectorListItemStyle = fileSelectorListViewStyle.getFileSelectorListItemStyle();
                // item的背景色
                StateListDrawable stateListDrawable = createBackgroundDrawable(R.drawable.fs_button_click, fileFileSelectorListItemStyle.getItemBackgroundColorId(), fileFileSelectorListItemStyle.getItemActionDownBackgroundColorId());
                itemView.setBackground(stateListDrawable);
                // 文件名
                name.setTextColor(getColorById(fileFileSelectorListItemStyle.getFileNameTextColorId()));
                name.setTextSize(fileFileSelectorListItemStyle.getFileNameTextSize());
                // 文件修改时间
                time.setTextColor(getColorById(fileFileSelectorListItemStyle.getLastModifiedTextColorId()));
                time.setTextSize(fileFileSelectorListItemStyle.getLastModifiedTextSize());
                // 文件大小
                size.setTextColor(getColorById(fileFileSelectorListItemStyle.getFileSizeTextColorId()));
                size.setTextSize(fileFileSelectorListItemStyle.getFileSizeTextSize());
                // 进入目录的图标
                in_dir_indicator.setImageDrawable(getDrawableById(fileFileSelectorListItemStyle.getArrowDrawableId()));
                // check box图标
                StateListDrawable checkBoxDrawable = createCheckBoxDrawable(fileFileSelectorListItemStyle.getSelectedDrawableId(), fileFileSelectorListItemStyle.getDeselectedDrawableId());
                check_box.setBackground(checkBoxDrawable);
            }
        }

        private StateListDrawable createCheckBoxDrawable(int selectedDrawableId, int deselectedDrawableId) {
            Drawable selectedDrawable = getDrawableById(selectedDrawableId);
            Drawable deselectedDrawable = getDrawableById(deselectedDrawableId);

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, selectedDrawable);
            stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, deselectedDrawable);

            return stateListDrawable;
        }

        private StateListDrawable createBackgroundDrawable(int drawableId, int enabledColorId, int pressedColorId) {
            Drawable enabledDrawable = getDrawableById(drawableId).mutate();
            Drawable pressedDrawable = getDrawableById(drawableId).mutate();

            enabledDrawable.setColorFilter(getColorById(enabledColorId), PorterDuff.Mode.SRC_IN);
            pressedDrawable.setColorFilter(getColorById(pressedColorId), PorterDuff.Mode.SRC_IN);

            StateListDrawable stateListDrawable = new StateListDrawable();
            // 修改按下状态时按钮背景色
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            // 修改默认状态时按钮背景色
            stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, enabledDrawable);

            return stateListDrawable;
        }

        private int getColorById(int colorId) {
            return ResourcesUtil.getColorById(context, colorId);
        }

        private Drawable getDrawableById(int drawableId) {
            return ResourcesUtil.getDrawableById(context, drawableId);
        }
    }
}

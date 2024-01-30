package xyz.xxin.fileselector.style;

import androidx.recyclerview.widget.RecyclerView;

import xyz.xxin.fileselector.R;

public class FileSelectorListViewStyle {
    private int backgroundColorId;                      // list view背景色id

    private String emptyFolderDescription;              // 文件夹为空时的提示
    private int emptyFolderDescriptionTextColorId;      // 空文件夹提示的字体颜色id
    private int emptyFolderDescriptionTextSize;         // 空文件夹提示的字体大小，此处以sp为单位
    private int emptyFolderBackgroundColorId;           // 文件夹为空时的背景色id

    private String loadProgressDescription;             // 加载过程的提示
    private int loadProgressDescriptionTextSize;        // 加载过程提示的字体大小，此处以sp为单位
    private int loadProgressDescriptionTextColorId;     // 加载过程提示的字体颜色id
    private int loadProgressBarColorId;                 // 环形进度条的颜色id
    private int loadProgressBackgroundColorId;          // 加载页面的背景颜色id

    private RecyclerView.ItemDecoration itemDecoration; // item装饰，用于添加分割线、修改边距等

    private FileSelectorListItemStyle fileSelectorListItemStyle;// item style

    public FileSelectorListViewStyle() {
        backgroundColorId = R.color.fs_white;
        emptyFolderDescription = "文件夹为空";
        emptyFolderDescriptionTextColorId = R.color.fs_black;
        emptyFolderDescriptionTextSize = 20;
        emptyFolderBackgroundColorId = R.color.fs_white;
        loadProgressDescription = "正在加载";
        loadProgressDescriptionTextSize = 20;
        loadProgressDescriptionTextColorId = R.color.fs_black;
        loadProgressBarColorId = R.color.fs_blue;
        loadProgressBackgroundColorId = R.color.fs_white;
        itemDecoration = null;
        fileSelectorListItemStyle = null;
    }

    public int getEmptyFolderDescriptionTextSize() {
        return emptyFolderDescriptionTextSize;
    }

    public void setEmptyFolderDescriptionTextSize(int emptyFolderDescriptionTextSize) {
        this.emptyFolderDescriptionTextSize = emptyFolderDescriptionTextSize;
    }

    public int getLoadProgressDescriptionTextSize() {
        return loadProgressDescriptionTextSize;
    }

    public void setLoadProgressDescriptionTextSize(int loadProgressDescriptionTextSize) {
        this.loadProgressDescriptionTextSize = loadProgressDescriptionTextSize;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return itemDecoration;
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.itemDecoration = itemDecoration;
    }

    public int getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        this.backgroundColorId = backgroundColorId;
    }

    public String getEmptyFolderDescription() {
        return emptyFolderDescription;
    }

    public void setEmptyFolderDescription(String emptyFolderDescription) {
        this.emptyFolderDescription = emptyFolderDescription;
    }

    public int getEmptyFolderDescriptionTextColorId() {
        return emptyFolderDescriptionTextColorId;
    }

    public void setEmptyFolderDescriptionTextColorId(int emptyFolderDescriptionTextColorId) {
        this.emptyFolderDescriptionTextColorId = emptyFolderDescriptionTextColorId;
    }

    public int getEmptyFolderBackgroundColorId() {
        return emptyFolderBackgroundColorId;
    }

    public void setEmptyFolderBackgroundColorId(int emptyFolderBackgroundColorId) {
        this.emptyFolderBackgroundColorId = emptyFolderBackgroundColorId;
    }

    public String getLoadProgressDescription() {
        return loadProgressDescription;
    }

    public void setLoadProgressDescription(String loadProgressDescription) {
        this.loadProgressDescription = loadProgressDescription;
    }

    public int getLoadProgressDescriptionTextColorId() {
        return loadProgressDescriptionTextColorId;
    }

    public void setLoadProgressDescriptionTextColorId(int loadProgressDescriptionTextColorId) {
        this.loadProgressDescriptionTextColorId = loadProgressDescriptionTextColorId;
    }

    public int getLoadProgressBarColorId() {
        return loadProgressBarColorId;
    }

    public void setLoadProgressBarColorId(int loadProgressBarColorId) {
        this.loadProgressBarColorId = loadProgressBarColorId;
    }

    public int getLoadProgressBackgroundColorId() {
        return loadProgressBackgroundColorId;
    }

    public void setLoadProgressBackgroundColorId(int loadProgressBackgroundColorId) {
        this.loadProgressBackgroundColorId = loadProgressBackgroundColorId;
    }

    public FileSelectorListItemStyle getFileSelectorListItemStyle() {
        return fileSelectorListItemStyle;
    }

    public void setFileSelectorListItemStyle(FileSelectorListItemStyle fileSelectorListItemStyle) {
        this.fileSelectorListItemStyle = fileSelectorListItemStyle;
    }

    public static class FileSelectorListItemStyle {
        private int itemBackgroundColorId;          // item背景色id
        private int itemActionDownBackgroundColorId;// item被点击时的背景色id

        private int fileNameTextColorId;            // 文件名的字体颜色id
        private int fileNameTextSize;               // 文件名的字体大小，此处以sp为单位

        private int lastModifiedTextColorId;        // 文件修改时间的字体颜色id
        private int lastModifiedTextSize;           // 文件修改时间的字体大小，此处以sp为单位

        private int fileSizeTextColorId;            // 文件大小的字体颜色id
        private int fileSizeTextSize;               // 文件大小的字体大小，此处以sp为单位

        private int arrowDrawableId;                // 箭头的drawable资源id

        private int selectedDrawableId;             // item被选中后，checkBox的drawable资源id
        private int deselectedDrawableId;           // item未被选中时，checkBox的drawable资源id

        public FileSelectorListItemStyle() {
            itemBackgroundColorId = R.color.fs_white;
            itemActionDownBackgroundColorId = R.color.fs_light_gray;
            fileNameTextColorId = R.color.fs_black;
            fileNameTextSize = 16;
            lastModifiedTextColorId = R.color.fs_dark_gray;
            lastModifiedTextSize = 16;
            fileSizeTextColorId = R.color.fs_dark_gray;
            fileSizeTextSize = 16;
            arrowDrawableId = R.drawable.fs_next_arrow;
            selectedDrawableId = R.drawable.fs_selected;
            deselectedDrawableId = R.drawable.fs_deselectd;
        }

        public int getFileNameTextSize() {
            return fileNameTextSize;
        }

        public void setFileNameTextSize(int fileNameTextSize) {
            this.fileNameTextSize = fileNameTextSize;
        }

        public int getLastModifiedTextSize() {
            return lastModifiedTextSize;
        }

        public void setLastModifiedTextSize(int lastModifiedTextSize) {
            this.lastModifiedTextSize = lastModifiedTextSize;
        }

        public int getFileSizeTextSize() {
            return fileSizeTextSize;
        }

        public void setFileSizeTextSize(int fileSizeTextSize) {
            this.fileSizeTextSize = fileSizeTextSize;
        }

        public int getItemBackgroundColorId() {
            return itemBackgroundColorId;
        }

        public void setItemBackgroundColorId(int itemBackgroundColorId) {
            this.itemBackgroundColorId = itemBackgroundColorId;
        }

        public int getItemActionDownBackgroundColorId() {
            return itemActionDownBackgroundColorId;
        }

        public void setItemActionDownBackgroundColorId(int itemActionDownBackgroundColorId) {
            this.itemActionDownBackgroundColorId = itemActionDownBackgroundColorId;
        }

        public int getFileNameTextColorId() {
            return fileNameTextColorId;
        }

        public void setFileNameTextColorId(int fileNameTextColorId) {
            this.fileNameTextColorId = fileNameTextColorId;
        }

        public int getLastModifiedTextColorId() {
            return lastModifiedTextColorId;
        }

        public void setLastModifiedTextColorId(int lastModifiedTextColorId) {
            this.lastModifiedTextColorId = lastModifiedTextColorId;
        }

        public int getFileSizeTextColorId() {
            return fileSizeTextColorId;
        }

        public void setFileSizeTextColorId(int fileSizeTextColorId) {
            this.fileSizeTextColorId = fileSizeTextColorId;
        }

        public int getArrowDrawableId() {
            return arrowDrawableId;
        }

        public void setArrowDrawableId(int arrowDrawableId) {
            this.arrowDrawableId = arrowDrawableId;
        }

        public int getSelectedDrawableId() {
            return selectedDrawableId;
        }

        public void setSelectedDrawableId(int selectedDrawableId) {
            this.selectedDrawableId = selectedDrawableId;
        }

        public int getDeselectedDrawableId() {
            return deselectedDrawableId;
        }

        public void setDeselectedDrawableId(int deselectedDrawableId) {
            this.deselectedDrawableId = deselectedDrawableId;
        }
    }
}

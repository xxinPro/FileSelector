package xyz.xxin.fileselector.style;

import xyz.xxin.fileselector.R;

public class FileSelectorPathBarStyle {
    private int backgroundColorId;          // path bar背景颜色id

    private int headItemBackgroundColorId;  // 头item的背景色id
    private int headItemTextColorId;        // 头item的字体颜色id
    private int itemBackgroundColorId;      // 其余item的背景色id
    private int itemTextColorId;            // 其余item的字体颜色id
    private int itemTextSize;               // item的字体大小，此处以sp为单位

    private int arrowImageDrawableId;       // 箭头图片drawable资源id

    private String fileCountDescription;    // 文件数量描述
    private String folderCountDescription;  // 文件夹数量描述
    private int descriptionColorId;         // 文件及文件夹数量描述的字体颜色id
    private int descriptionTextSize;        // 文件及文件夹数量描述的字体大小，此处以sp为单位

    public FileSelectorPathBarStyle() {
        backgroundColorId = R.color.fs_white;
        headItemBackgroundColorId = R.color.fs_blue;
        headItemTextColorId = R.color.fs_white;
        itemBackgroundColorId = R.color.fs_light_gray;
        itemTextColorId = R.color.fs_black;
        itemTextSize = 16;
        arrowImageDrawableId = R.drawable.fs_next_arrow;
        fileCountDescription = "文件个数：";
        folderCountDescription = "文件夹个数：";
        descriptionColorId = R.color.fs_black;
        descriptionTextSize = 16;
    }

    public int getItemTextSize() {
        return itemTextSize;
    }

    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    public int getDescriptionTextSize() {
        return descriptionTextSize;
    }

    public void setDescriptionTextSize(int descriptionTextSize) {
        this.descriptionTextSize = descriptionTextSize;
    }

    public int getDescriptionColorId() {
        return descriptionColorId;
    }

    public void setDescriptionColorId(int descriptionColorId) {
        this.descriptionColorId = descriptionColorId;
    }

    public int getHeadItemBackgroundColorId() {
        return headItemBackgroundColorId;
    }

    public void setHeadItemBackgroundColorId(int headItemBackgroundColorId) {
        this.headItemBackgroundColorId = headItemBackgroundColorId;
    }

    public int getItemBackgroundColorId() {
        return itemBackgroundColorId;
    }

    public void setItemBackgroundColorId(int itemBackgroundColorId) {
        this.itemBackgroundColorId = itemBackgroundColorId;
    }

    public int getHeadItemTextColorId() {
        return headItemTextColorId;
    }

    public void setHeadItemTextColorId(int headItemTextColorId) {
        this.headItemTextColorId = headItemTextColorId;
    }

    public int getItemTextColorId() {
        return itemTextColorId;
    }

    public void setItemTextColorId(int itemTextColorId) {
        this.itemTextColorId = itemTextColorId;
    }

    public int getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        this.backgroundColorId = backgroundColorId;
    }

    public int getArrowImageDrawableId() {
        return arrowImageDrawableId;
    }

    public void setArrowImageDrawableId(int arrowImageDrawableId) {
        this.arrowImageDrawableId = arrowImageDrawableId;
    }

    public String getFileCountDescription() {
        return fileCountDescription;
    }

    public void setFileCountDescription(String fileCountDescription) {
        this.fileCountDescription = fileCountDescription;
    }

    public String getFolderCountDescription() {
        return folderCountDescription;
    }

    public void setFolderCountDescription(String folderCountDescription) {
        this.folderCountDescription = folderCountDescription;
    }
}

package xyz.xxin.fileselector.style;

import xyz.xxin.fileselector.R;

public class FileSelectorViewStyle {
    private int backgroundColorId;      // 背景颜色id
    private int backgroundDrawableId;   // 背景drawable资源id

    public FileSelectorViewStyle() {
        backgroundColorId = R.color.fs_white;
        backgroundDrawableId = -1;
    }

    public int getBackgroundDrawableId() {
        return backgroundDrawableId;
    }

    public void setBackgroundDrawableId(int backgroundDrawableId) {
        this.backgroundDrawableId = backgroundDrawableId;
    }

    public int getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        this.backgroundColorId = backgroundColorId;
    }
}

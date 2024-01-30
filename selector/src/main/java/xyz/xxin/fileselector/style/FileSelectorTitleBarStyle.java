package xyz.xxin.fileselector.style;

import xyz.xxin.fileselector.R;

public class FileSelectorTitleBarStyle {

    private int height;                     // title bar高度，此处以dp为单位
    private int backgroundColorId;          // title bar背景颜色

    private int overImageDrawableId;        // 结束按钮图片的drawable资源id

    private String titleText;               // 中间的标题
    private int titleTextSize;              // 标题的字体大小，此处以sp为单位
    private int titleTextColorId;           // 标题字体颜色的id

    private String startControlText;        // 启动选择模式之后，右侧控制按钮的text
    private String endControlText;          // 结束选择模式之后，右侧控制按钮的text
    private int controlTextSize;            // 控制按钮的字体大小
    private int controlTextColorId;         // 控制按钮字体颜色id

    public FileSelectorTitleBarStyle() {
        height = 50;
        backgroundColorId = R.color.fs_blue;
        overImageDrawableId = R.drawable.fs_ic_back;
        titleText = "请选择文件";
        titleTextSize = 20;
        titleTextColorId = R.color.fs_white;
        startControlText = "取消";
        endControlText = "选择";
        controlTextSize = 16;
        controlTextColorId = R.color.fs_white;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public int getControlTextSize() {
        return controlTextSize;
    }

    public void setControlTextSize(int controlTextSize) {
        this.controlTextSize = controlTextSize;
    }

    public int getControlTextColorId() {
        return controlTextColorId;
    }

    public void setControlTextColorId(int controlTextColorId) {
        this.controlTextColorId = controlTextColorId;
    }

    public String getStartControlText() {
        return startControlText;
    }

    public void setStartControlText(String startControlText) {
        this.startControlText = startControlText;
    }

    public String getEndControlText() {
        return endControlText;
    }

    public void setEndControlText(String endControlText) {
        this.endControlText = endControlText;
    }

    public int getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        this.backgroundColorId = backgroundColorId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTitleTextColorId() {
        return titleTextColorId;
    }

    public void setTitleTextColorId(int titleTextColorId) {
        this.titleTextColorId = titleTextColorId;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getOverImageDrawableId() {
        return overImageDrawableId;
    }

    public void setOverImageDrawableId(int overImageDrawableId) {
        this.overImageDrawableId = overImageDrawableId;
    }
}

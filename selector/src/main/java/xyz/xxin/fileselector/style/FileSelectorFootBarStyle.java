package xyz.xxin.fileselector.style;

import xyz.xxin.fileselector.R;

public class FileSelectorFootBarStyle {
    private int backgroundColorId;              // foot bar背景色id

    private int btnBackgroundColorId;           // 按钮背景色id
    private int btnActionDownBackgroundColorId; // 按钮按下后变化的背景色id

    private String leftButtonText;              // 左按钮的text
    private String leftButtonSelectedAllText;   // 列表全选之后左按钮的text
    private String rightButtonText;             // 右按钮的text

    private int buttonTextColorId;              // 按钮字体颜色id
    private int buttonTextSize;                 // 按钮字体大小，此处以sp为单位

    public FileSelectorFootBarStyle() {
        backgroundColorId = R.color.fs_light_gray;
        btnBackgroundColorId = R.color.fs_white;
        btnActionDownBackgroundColorId = R.color.fs_light_gray;
        leftButtonText = "全选";
        leftButtonSelectedAllText = "全不选";
        rightButtonText = "确定";
        buttonTextColorId = R.color.fs_black;
        buttonTextSize = 16;
    }

    public int getButtonTextSize() {
        return buttonTextSize;
    }

    public void setButtonTextSize(int buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
    }

    public int getButtonTextColorId() {
        return buttonTextColorId;
    }

    public void setButtonTextColorId(int buttonTextColorId) {
        this.buttonTextColorId = buttonTextColorId;
    }

    public int getBackgroundColorId() {
        return backgroundColorId;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        this.backgroundColorId = backgroundColorId;
    }

    public int getBtnBackgroundColorId() {
        return btnBackgroundColorId;
    }

    public void setBtnBackgroundColorId(int btnBackgroundColorId) {
        this.btnBackgroundColorId = btnBackgroundColorId;
    }

    public int getBtnActionDownBackgroundColorId() {
        return btnActionDownBackgroundColorId;
    }

    public void setBtnActionDownBackgroundColorId(int btnActionDownBackgroundColorId) {
        this.btnActionDownBackgroundColorId = btnActionDownBackgroundColorId;
    }

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public void setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
    }

    public String getLeftButtonSelectedAllText() {
        return leftButtonSelectedAllText;
    }

    public void setLeftButtonSelectedAllText(String leftButtonSelectedAllText) {
        this.leftButtonSelectedAllText = leftButtonSelectedAllText;
    }

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
    }
}

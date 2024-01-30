package xyz.xxin.fileselector.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.style.FileSelectorFootBarStyle;
import xyz.xxin.fileselector.utils.DeviceUtil;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class FileSelectorFootBar extends LinearLayout implements View.OnClickListener {
    private Button btn_left;
    private Button btn_right;

    private OnButtonClickListener onButtonClickListener;

    public FileSelectorFootBar(Context context) {
        super(context);
        init();
    }

    public FileSelectorFootBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileSelectorFootBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        loadLayout();
        findView();
        initStyle();
    }

    public interface OnButtonClickListener {
        // 左边按钮的click
        void onLeftButtonClick(View view);
        // 右边按钮的click
        void onRightButtonClick(View view);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onButtonClickListener == null) return;

        int id = v.getId();
        if (id == R.id.btn_left) {
            DeviceUtil.vibrateDevice(getContext(), 1);
            onButtonClickListener.onLeftButtonClick(v);
        } else if (id == R.id.btn_right) {
            DeviceUtil.vibrateDevice(getContext(), 1);
            onButtonClickListener.onRightButtonClick(v);
        }
    }

    /**
     * 全选之后底部栏做出的改变
     */
    public void afterSelectedAll() {
        FileSelectorFootBarStyle fileSelectorFootBarStyle = ConfigBean.getInstance().fileSelectorFootBarStyle;
        if (fileSelectorFootBarStyle != null) {
            btn_left.setText(fileSelectorFootBarStyle.getLeftButtonSelectedAllText());
        } else {
            btn_left.setText(ResourcesUtil.getStringById(getContext(), R.string.deselect_all));
        }
    }

    /**
     * 全选之前底部栏做出的改变
     */
    public void beforeSelectedAll() {
        FileSelectorFootBarStyle fileSelectorFootBarStyle = ConfigBean.getInstance().fileSelectorFootBarStyle;
        if (fileSelectorFootBarStyle != null) {
            btn_left.setText(fileSelectorFootBarStyle.getLeftButtonText());
        } else {
            btn_left.setText(ResourcesUtil.getStringById(getContext(), R.string.select_all));
        }
    }

    /**
     * 显示
     */
    public void animShow() {
        this.setVisibility(VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fs_bottom_in);
        this.setAnimation(animation);
    }

    /**
     * 隐藏
     */
    public void animHide() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fs_bottom_out);
        this.setAnimation(animation);
        this.setVisibility(GONE);
    }

    private void findView() {
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
    }

    private void loadLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.fs_foot_bar, this);
        setOrientation(VERTICAL);
    }

    private void initStyle() {
        FileSelectorFootBarStyle fileSelectorFootBarStyle = ConfigBean.getInstance().fileSelectorFootBarStyle;
        if (fileSelectorFootBarStyle != null) {
            setBackgroundColor(getColorById(fileSelectorFootBarStyle.getBackgroundColorId()));

            int drawableId = R.drawable.fs_button_click;
            int colorId = fileSelectorFootBarStyle.getBtnBackgroundColorId();
            int actionDownColorId = fileSelectorFootBarStyle.getBtnActionDownBackgroundColorId();
            // 创建一个StateListDrawable以包装修改后的Drawable
            StateListDrawable btnLeftStateListDrawable = createStateListDrawable(drawableId, colorId, actionDownColorId);
            StateListDrawable btnRightStateListDrawable = createStateListDrawable(drawableId, colorId, actionDownColorId);
            // 设置按钮背景色
            btn_left.setBackground(btnLeftStateListDrawable);
            btn_right.setBackground(btnRightStateListDrawable);

            // 设置按钮字体颜色
            btn_left.setTextColor(getColorById(fileSelectorFootBarStyle.getButtonTextColorId()));
            btn_right.setTextColor(getColorById(fileSelectorFootBarStyle.getButtonTextColorId()));

            // 设置按钮字体大小
            btn_left.setTextSize(fileSelectorFootBarStyle.getButtonTextSize());
            btn_right.setTextSize(fileSelectorFootBarStyle.getButtonTextSize());

            // 设置按钮text
            beforeSelectedAll();
            btn_right.setText(fileSelectorFootBarStyle.getRightButtonText());
        } else {
            setBackgroundColor(getColorById(R.color.fs_light_gray));
        }
    }

    private StateListDrawable createStateListDrawable(int drawableId, int enabledColorId, int pressedColorId) {
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
        return ResourcesUtil.getColorById(getContext(), colorId);
    }

    private Drawable getDrawableById(int drawableId) {
        return ResourcesUtil.getDrawableById(getContext(), drawableId);
    }
}

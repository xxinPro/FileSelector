package xyz.xxin.fileselector.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.style.FileSelectorTitleBarStyle;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class FileSelectorTitleBar extends LinearLayout implements View.OnClickListener {
    private TextView title_bar_text;
    private TextView select_control;
    private ImageView fs_back;

    private OnTitleClickListener onTitleClickListener;

    public FileSelectorTitleBar(Context context) {
        super(context);
        init();
    }

    public FileSelectorTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileSelectorTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        loadLayout();
        findView();
        initStyle();
    }

    public interface OnTitleClickListener{
        // 返回按钮click
        void onBackClick(View view);
        // 标题click
        void onTitleClick(View view);
        // 选择按钮click
        void onSelectClick(View view);
    }

    /**
     * 设置三个按钮的click
     */
    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;

        fs_back.setOnClickListener(this);
        title_bar_text.setOnClickListener(this);
        select_control.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onTitleClickListener == null) return;

        int id = v.getId();
        if (id == R.id.fs_back) {
            onTitleClickListener.onBackClick(v);
        } else if (id == R.id.title_bar_text) {
            onTitleClickListener.onTitleClick(v);
        } else if (id == R.id.select_control) {
            onTitleClickListener.onSelectClick(v);
        }
    }

    /**
     * 选择模式启动之后，title bar做出的改变
     */
    public void startSelectMode() {
        FileSelectorTitleBarStyle fileSelectorTitleBarStyle = ConfigBean.getInstance().fileSelectorTitleBarStyle;
        if (fileSelectorTitleBarStyle != null) {
            select_control.setText(fileSelectorTitleBarStyle.getStartControlText());
        } else {
            select_control.setText(ResourcesUtil.getStringById(getContext(), R.string.fs_select_cancel));
        }
    }

    /**
     * 选择模式结束之后，title bar做出的改变
     */
    public void endSelectMode() {
        FileSelectorTitleBarStyle fileSelectorTitleBarStyle = ConfigBean.getInstance().fileSelectorTitleBarStyle;
        if (fileSelectorTitleBarStyle != null) {
            select_control.setText(fileSelectorTitleBarStyle.getEndControlText());
        } else {
            select_control.setText(ResourcesUtil.getStringById(getContext(), R.string.fs_select_start));
        }
    }

    private void loadLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.fs_title_bar, this);
    }

    private void findView() {
        title_bar_text = findViewById(R.id.title_bar_text);
        select_control = findViewById(R.id.select_control);
        fs_back = findViewById(R.id.fs_back);
    }

    private void initStyle() {
        FileSelectorTitleBarStyle fileSelectorTitleBarStyle = ConfigBean.getInstance().fileSelectorTitleBarStyle;
        if (fileSelectorTitleBarStyle != null) {
            setBackgroundColor(getColorById(fileSelectorTitleBarStyle.getBackgroundColorId()));
            // 标题设置
            title_bar_text.setText(fileSelectorTitleBarStyle.getTitleText());
            title_bar_text.setTextSize(fileSelectorTitleBarStyle.getTitleTextSize());
            title_bar_text.setTextColor(getColorById(fileSelectorTitleBarStyle.getTitleTextColorId()));
            // 返回按钮图片设置
            fs_back.setImageDrawable(getDrawableById(fileSelectorTitleBarStyle.getOverImageDrawableId()));
            // 右侧选择控制按钮text
            select_control.setTextSize(fileSelectorTitleBarStyle.getControlTextSize());
            select_control.setTextColor(getColorById(fileSelectorTitleBarStyle.getControlTextColorId()));
            endSelectMode();
        } else {
            setBackgroundColor(getColorById(R.color.fs_blue));
        }
    }

    private int getColorById(int colorId) {
        return ResourcesUtil.getColorById(getContext(), colorId);
    }

    private Drawable getDrawableById(int drawableId) {
        return ResourcesUtil.getDrawableById(getContext(), drawableId);
    }
}

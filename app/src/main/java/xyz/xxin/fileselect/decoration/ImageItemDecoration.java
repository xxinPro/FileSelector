package xyz.xxin.fileselect.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.xxin.fileselector.utils.PixelsUtil;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class ImageItemDecoration extends RecyclerView.ItemDecoration {
    private final Context context;
    private final int imageDrawableId;

    public ImageItemDecoration(Context context, int imageDrawableId) {
        this.context = context;
        this.imageDrawableId = imageDrawableId;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int dp10 = PixelsUtil.dp2px(10, context);
        outRect.set(0, dp10, 0, dp10);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Drawable drawable = ResourcesUtil.getDrawableById(context, imageDrawableId);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            int dp20 = PixelsUtil.dp2px(20, context);

            int left = child.getLeft() + dp20;     // item左边界
            int top = child.getBottom();         // item上边界
            int right = child.getRight() - dp20;   // item右边界
            int bottom = child.getBottom() + dp20; // item下边界

            // 设置图片绘制区域
            drawable.setBounds(left, top, right, bottom);
            // 绘制图片
            drawable.draw(c);
        }
    }
}

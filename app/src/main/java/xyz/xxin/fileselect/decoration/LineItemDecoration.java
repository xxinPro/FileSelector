package xyz.xxin.fileselect.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.xxin.fileselect.R;
import xyz.xxin.fileselector.utils.PixelsUtil;

public class LineItemDecoration extends RecyclerView.ItemDecoration {
    private final Context context;
    private final int lineStrokeWidth;

    public LineItemDecoration(Context context, int lineStrokeWidth) {
        this.context = context;
        this.lineStrokeWidth = lineStrokeWidth;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 在相邻item之间绘制一条分割线
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.black));
        paint.setStrokeWidth(PixelsUtil.dp2px(lineStrokeWidth, context));

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            c.drawLine(child.getLeft() + 20, child.getBottom(), child.getRight() - 20, child.getBottom(), paint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 相当于设置item的margin，在底部或顶部空出空间，用于显示onDraw中绘制的分割线
        outRect.set(0, 0, 0, PixelsUtil.dp2px(lineStrokeWidth, context));
        // 设置item的padding，不会影响onDraw中绘制的分割线
        view.setPadding(0,0,0,0);
        // 设置item宽高
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        // layoutParams.setMargins(0,0,0,0); // 也可以通过layoutParams设置item的margin
        view.setLayoutParams(layoutParams);
    }
}

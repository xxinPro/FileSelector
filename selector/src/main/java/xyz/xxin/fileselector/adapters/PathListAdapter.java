package xyz.xxin.fileselector.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.style.FileSelectorPathBarStyle;
import xyz.xxin.fileselector.utils.ResourcesUtil;

public class PathListAdapter extends RecyclerView.Adapter<PathListAdapter.ViewHolder> {
    private final Context context;
    private final List<FileItemBean> fileItemBeanList;

    private OnPathItemClickListener onPathItemClickListener;

    public PathListAdapter(Context context) {
        this.context = context;
        fileItemBeanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PathListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fs_path_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathListAdapter.ViewHolder holder, int position) {
        FileItemBean currentFileItemBean = fileItemBeanList.get(position);

        holder.current_path.setText(currentFileItemBean.getFileName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPathItemClickListener != null) {
                    onPathItemClickListener.onClickListener(currentFileItemBean, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileItemBeanList.size();
    }

    /**
     * 添加一个数据到地址显示栏尾部
     */
    public void pushData(FileItemBean fileItemBean) {
        fileItemBeanList.add(fileItemBean);
        notifyDataSetChanged();
    }

    /**
     * 删除地址显示栏最后一个元素
     */
    public FileItemBean popData() {
        FileItemBean fileItemBean = fileItemBeanList.remove(fileItemBeanList.size() - 1);
        notifyDataSetChanged();
        return fileItemBean;
    }

    /**
     * 删除指定position之后的所有元素（不包括position）
     */
    public void subData(int position) {
        if (fileItemBeanList.size() > position + 1) {
            fileItemBeanList.subList(position + 1, fileItemBeanList.size()).clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 删除地址显示栏的所有元素，该方法不会影响第一个item
     */
    public void subAllData() {
        fileItemBeanList.clear();
        notifyDataSetChanged();
    }

    public interface OnPathItemClickListener{
        void onClickListener(FileItemBean pointFile, int position);
    }

    public void setOnPathItemClickListener(OnPathItemClickListener onPathItemClickListener) {
        this.onPathItemClickListener = onPathItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView current_path;
        private final ImageView current_arrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            current_path = itemView.findViewById(R.id.current_path);
            current_arrow = itemView.findViewById(R.id.current_arrow);

            FileSelectorPathBarStyle fileSelectorPathBarStyle = ConfigBean.getInstance().fileSelectorPathBarStyle;
            if (fileSelectorPathBarStyle != null) {
                // 设置current_path的背景颜色
                Drawable drawable = current_path.getBackground().mutate();
                drawable.setColorFilter(ResourcesUtil.getColorById(context, fileSelectorPathBarStyle.getItemBackgroundColorId()), PorterDuff.Mode.SRC_IN);
                current_path.setBackground(drawable);
                // 设置current_path的字体颜色
                current_path.setTextColor(ResourcesUtil.getColorById(context, fileSelectorPathBarStyle.getItemTextColorId()));
                // 设置current_path的字体大小
                current_path.setTextSize(fileSelectorPathBarStyle.getItemTextSize());
                // 设置箭头image
                current_arrow.setImageDrawable(ResourcesUtil.getDrawableById(context, fileSelectorPathBarStyle.getArrowImageDrawableId()));
            }
        }
    }
}

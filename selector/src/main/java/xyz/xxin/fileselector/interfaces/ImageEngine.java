package xyz.xxin.fileselector.interfaces;

import android.content.Context;
import android.widget.ImageView;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

public interface ImageEngine {
    /**
     * 加载file对象图片的略缩图
     * @param context           所属activity的context
     * @param file              图片文件对象
     * @param imageView         需要加载的目标imageView
     * @param defaultDrawableId 默认的略缩图
     */
    void loadFileImage(Context context, File file, ImageView imageView, int defaultDrawableId);

    /**
     * 加载Document File对象图片的略缩图
     * @param context           所属activity的context
     * @param documentFile      图片文件对象
     * @param imageView         需要加载的目标imageView
     * @param defaultDrawableId 默认的略缩图
     */
    void loadDocumentFileImage(Context context, DocumentFile documentFile, ImageView imageView, int defaultDrawableId);
}

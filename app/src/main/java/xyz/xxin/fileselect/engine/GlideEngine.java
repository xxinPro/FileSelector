package xyz.xxin.fileselect.engine;

import android.content.Context;
import android.widget.ImageView;

import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import xyz.xxin.fileselector.interfaces.ImageEngine;

public class GlideEngine implements ImageEngine {
    private static volatile GlideEngine glideEngine;

    private GlideEngine () {}

    public static GlideEngine createGlideEngine() {
        if (glideEngine == null) {
            synchronized (GlideEngine.class) {
                if (glideEngine == null) {
                    glideEngine = new GlideEngine();
                }
            }
        }
        return glideEngine;
    }

    @Override
    public void loadFileImage(Context context, File file, ImageView imageView, int defaultDrawableId) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(defaultDrawableId);
        Glide.with(context)
                .load(file)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadDocumentFileImage(Context context, DocumentFile documentFile, ImageView imageView, int defaultDrawableId) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(defaultDrawableId);
        Glide.with(context)
                .load(documentFile.getUri())
                .apply(options)
                .into(imageView);
    }
}

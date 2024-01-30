package xyz.xxin.fileselector.beans;

import android.os.Build;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

import xyz.xxin.fileselector.utils.SAFUtil;

/**
 * 每个FileBean2中仅包含一个可用的File或DocumentFile对象
 * 在Android 11及之的系统中，有些目录仅能通过SAF框架访问，所以从这些目录中取到的文件是DocumentFile对象
 * 除此之外的其他文件均为File对象
 */
public class FileBean2 {
    private File file = null;
    private DocumentFile documentFile = null;

    public FileBean2(File file) {
        this.file = file;
    }

    public FileBean2(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    public FileBean2() {
    }

    /**
     * 是否是File类型的文件
     */
    public boolean isFile() {
        return file != null && file.isFile();
    }

    /**
     * 是否是File类型的目录
     */
    public boolean isDirectory() {
        return file != null && file.isDirectory();
    }

    /**
     * 是否是DocumentFile类型的文件
     */
    public boolean isDocumentFile() {
        return documentFile != null && documentFile.isFile();
    }

    /**
     * 是否是DocumentFile类型的目录
     */
    public boolean isDocumentDierctory() {
        return documentFile != null && documentFile.isDirectory();
    }

    /**
     * 取DocumentFile对象
     */
    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    /**
     * 取file对象
     */
    public File getFile() {
        return file;
    }

    /**
     * 取最后修改事件
     */
    public long lastModified() {
        if (documentFile != null) {
            return documentFile.lastModified();
        }
        return file.lastModified();
    }

    /**
     * 取文件大小
     */
    public long length() {
        if (documentFile != null) {
            return documentFile.length();
        }
        return file.length();
    }

    /**
     * 取文件名
     */
    public String getName() {
        if (documentFile != null) {
            return documentFile.getName();
        }
        return file.getName();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    /**
     * 取父目录
     */
    public FileBean2 getParentFileBean2() {
        // 在Android 13及之后的系统中，如果DocumentFile类型文件没有父目录，说明它的父目录是Android/data或Android/obb目录
        // 对于Android 11、12，如果DocumentFile类型文件没有父目录，说明它的父目录是Android目录
        if (documentFile != null) {
            DocumentFile parentFile = documentFile.getParentFile();
            if (parentFile == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    String uriToPath = SAFUtil.uriToPath(this.documentFile);
                    // 判断是否是/storage/emulated/0/Android/data目录
                    if (uriToPath.startsWith(SAFUtil.ANDROID_DATA_PATH))
                        return new FileBean2(new File(SAFUtil.ANDROID_DATA_PATH));
                    // /storage/emulated/0/Android/obb目录
                    return new FileBean2(new File(SAFUtil.ANDROID_OBB_PATH));
                }
                // /storage/emulated/0/Android目录
                return new FileBean2(new File(SAFUtil.ANDROID_PATH));
            }
            return new FileBean2(parentFile);
        } else if (file.getAbsolutePath().startsWith(SAFUtil.ANDROID_DATA_PATH)) {
            // Android 13及之后的系统中，无法获取/storage/emulated/0/Android/data目录的访问权限，
            // 所以无法直接得到data目录下的直接子文件列表，但是已知data目录下直接子文件的路径时，可以通过该路径创建File对象来判断文件是否存在，
            // 于是创建了data目录下的直接子文件的FileItemBean，其中包含该子文件的File对象，在获取该FileItemBean的父FileItemBean时，将会触发
            return new FileBean2(new File(SAFUtil.ANDROID_DATA_PATH));
        } else if (file.getAbsolutePath().startsWith(SAFUtil.ANDROID_OBB_PATH)) {
            // 触发条件同上
            return new FileBean2(new File(SAFUtil.ANDROID_OBB_PATH));
        }
        return new FileBean2(file.getParentFile());
    }
}

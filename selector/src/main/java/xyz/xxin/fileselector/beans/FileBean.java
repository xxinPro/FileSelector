package xyz.xxin.fileselector.beans;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

/**
 * 每个FileBean中仅包含一个可用的File或DocumentFile对象
 * 在Android 11及之的系统中，有些目录仅能通过SAF框架访问，所以从这些目录中取到的文件是DocumentFile对象
 * 除此之外的其他文件均为File对象
 */
public class FileBean {
    private File file = null;
    private DocumentFile documentFile = null;

    public FileBean(File file) {
        this.file = file;
    }

    public FileBean(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    /**
     * 是否是File类型的文件
     */
    public boolean isFileType() {
        return file != null;
    }

    /**
     * 是否是DocumentFile类型的文件
     */
    public boolean isDocumentFileType() {
        return documentFile != null;
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
     * 取最后修改时间
     */
    public long lastModified() {
        if (isDocumentFileType()) {
            return documentFile.lastModified();
        }
        return file.lastModified();
    }

    /**
     * 取文件大小
     */
    public long length() {
        if (isDocumentFileType()) {
            return documentFile.length();
        }
        return file.length();
    }

    /**
     * 取文件名
     */
    public String getName() {
        if (isDocumentFileType()) {
            return documentFile.getName();
        }
        return file.getName();
    }
}

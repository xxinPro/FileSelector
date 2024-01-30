package xyz.xxin.fileselector.beans;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;

import xyz.xxin.fileselector.enums.FileItemBeanType;
import xyz.xxin.fileselector.enums.SortRule;
import xyz.xxin.fileselector.utils.SAFUtil;

public class FileItemBean {
    private final ConfigBean configBean;

    private FileItemBeanType fileItemBeanType = null;  // 类型

    private File file = null;                  // file对象
    private DocumentFile documentFile = null;  // document对象

    private String fileName = null;            // 文件名
    private long lastModified = -1;            // 最后修改时间
    private long fileLength = -1;              // 文件长度

    private boolean isDirectory = false;       // 是否是文件夹
    private boolean isDocumentFile = false;    // 是否是DocumentFile

    private boolean isSelected = false;        // 是否被选中

    public FileItemBean(File file) {
        configBean = ConfigBean.getInstance();

        if (file == null) return;

        // Android 10 及以下的版本不需要向SAF框架申请访问权限
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            setBeanType(FileItemBeanType.EXTERNAL_FILE);
            setFile(file);
            return;
        }

        if (file.getAbsolutePath().equals(SAFUtil.ANDROID_DATA_PATH)) {
            setBeanType(FileItemBeanType.DATA_DIR);
            setDataFile(file);
        } else if (file.getAbsolutePath().equals(SAFUtil.ANDROID_OBB_PATH)) {
            setBeanType(FileItemBeanType.OBB_DIR);
            setObbFile(file);
        } else if (file.getAbsolutePath().startsWith(SAFUtil.ANDROID_DATA_PATH)) {
            setBeanType(FileItemBeanType.DATA_CHILD_DIR);
            setDataFileChild(file);
        } else if (file.getAbsolutePath().startsWith(SAFUtil.ANDROID_OBB_PATH)) {
            setBeanType(FileItemBeanType.OBB_CHILD_DIR);
            setObbFileChild(file);
        } else {
            setBeanType(FileItemBeanType.EXTERNAL_FILE);
            setFile(file);
        }
    }

    public FileItemBean(DocumentFile documentFile) {
        configBean = ConfigBean.getInstance();

        if (documentFile == null) return;

        this.setBeanType(FileItemBeanType.DOCUMENT_FILE);
        this.setDocumentFile(documentFile);
    }

    private void setFile(File file) {
        this.file = file;

        fileName = file.getName();
        isDirectory = file.isDirectory();
        isDocumentFile = false;

        if (configBean.fileComparator != null) {
            lastModified = file.lastModified();

            if (!isDirectory)
                fileLength = file.length();
        } else if (configBean.sortRule == SortRule.Size) {
            if (!isDirectory)
                fileLength = file.length();
        } else if (configBean.sortRule == SortRule.Time) {
            lastModified = file.lastModified();
        }
    }

    /**
     * 这个方法因为调用了太多documentFile的属性，所以非常影响效率
     * 当需要创建多个包含DocumentFile的Bean对象时，异常的缓慢
     * 但是暂时又没办法合理的规避
     */
    private void setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;

        fileName = documentFile.getName();
        isDirectory = documentFile.isDirectory();
        isDocumentFile = true;

        if (configBean.fileComparator != null) {
            lastModified = documentFile.lastModified();

            if (!isDirectory)
                fileLength = documentFile.length();
        } else if (configBean.sortRule == SortRule.Size) {
            if (!isDirectory)
                fileLength = documentFile.length();
        } else if (configBean.sortRule == SortRule.Time) {
            lastModified = documentFile.lastModified();
        }
    }

    private void setDataFileChild(File file) {
        this.file = file;

        fileName = file.getName();
        lastModified = file.lastModified();
        isDirectory = true;
        isDocumentFile = false;
    }

    private void setObbFileChild(File file) {
        this.file = file;

        fileName = file.getName();
        lastModified = file.lastModified();
        isDirectory = true;
        isDocumentFile = false;
    }

    private void setDataFile(File file) {
        this.file = file;

        fileName = file.getName();
        lastModified = file.lastModified();
        isDirectory = true;
        isDocumentFile = false;
    }

    private void setObbFile(File file) {
        this.file = file;

        fileName = file.getName();
        lastModified = file.lastModified();
        isDirectory = true;
        isDocumentFile = false;
    }

    private void setBeanType(FileItemBeanType fileItemBeanType) {
        this.fileItemBeanType = fileItemBeanType;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * 获取FileItemBean的父目录的FileItemBean
     */
    public FileItemBean getParentBean() {
        if (fileItemBeanType == FileItemBeanType.DOCUMENT_FILE) {
            // 在Android 13及之后的系统中，如果DocumentFile类型文件没有父目录，说明它的父目录是Android/data或Android/obb目录
            // 对于Android 11、12，如果DocumentFile类型文件没有父目录，说明它的父目录是Android目录
            DocumentFile parentFile = this.documentFile.getParentFile();
            if (parentFile == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    String uriToPath = SAFUtil.uriToPath(this.documentFile);
                    // 判断是否是Android/data目录
                    if (uriToPath.startsWith(SAFUtil.ANDROID_DATA_PATH))
                        return new FileItemBean(new File(SAFUtil.ANDROID_DATA_PATH));
                    // Android/obb目录
                    return new FileItemBean(new File(SAFUtil.ANDROID_OBB_PATH));
                }
                return new FileItemBean(new File(SAFUtil.ANDROID_PATH));
            }
            return new FileItemBean(parentFile);
        } else if (fileItemBeanType == FileItemBeanType.DATA_CHILD_DIR) {
            // 触发的条件很苛刻，Android 13及之后的系统中，无法获取/storage/emulated/0/Android/data目录的访问权限，
            // 所以无法直接得到data目录下的直接子文件列表，但是已知data目录下直接子文件的路径时，可以通过该路径创建File对象来判断文件是否存在，
            // 于是创建了data目录下的直接子文件的FileItemBean，其中包含该子文件的File对象，并将FileItemBean递交给FilePathBar，
            // 如果点击的FilePathBar的item是data目录的直接子文件，在获取该FileItemBean的父FileItemBean时，将会触发
            return new FileItemBean(new File(SAFUtil.ANDROID_DATA_PATH));
        } else if (fileItemBeanType == FileItemBeanType.OBB_CHILD_DIR) {
            // 触发条件同上
            return new FileItemBean(new File(SAFUtil.ANDROID_OBB_PATH));
        } else {
            // 如果进入这个逻辑，说明当前FileItemBean是Android/data目录Android/obb目录或普通外部储存目录
            // 需要注意的是Android/data和Android/obb目录除无法使用listFiles之外与普通外部储存目录没有太大区别
            return new FileItemBean(this.file.getParentFile());
        }
    }

    /**
     * 当前FileItemBean是否被选中
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * 获取Bean中包含文件的文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取Bean中包含文件的最后修改时间
     */
    public long getLastModified() {
        if (configBean.sortRule == SortRule.Size || configBean.sortRule == SortRule.Name) {
            if (isDocumentFile) {
                return documentFile.lastModified();
            } else {
                return file.lastModified();
            }
        }

        return lastModified;
    }

    /**
     * 获取Bean中包含文件的文件长度
     */
    public long getFileLength() {
        if (configBean.sortRule == SortRule.Time || configBean.sortRule == SortRule.Name) {
            if (!isDirectory && isDocumentFile) {
                return documentFile.length();
            } else if (!isDirectory) {
                return file.length();
            }
        }

        return fileLength;
    }

    /**
     * 判断Bean中包含文件对象是否是目录
     * 文件对象可能是普通的File对象，也可能是DocumentFile对象，
     * 可以进一步通过isDocumentFileType()判断
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * 判断Bean中包含文件对象是否是普通文件
     * 文件对象可能是普通的File对象，也可能是DocumentFile对象，
     * 可以进一步通过isDocumentFileType()判断
     */
    public boolean isFile() {
        return !isDirectory;
    }

    /**
     * 判断Bean中包含文件或目录是否是DocumentFile文件对象
     * 在Android 13及之后的系统中，Android/data和Android/obb的所有非直接子文件是DocumentFile文件对象
     * 在Android 11、12及的系统中，Android/data和Android/obb的直接和非直接子文件是DocumentFile文件对象
     * （直接子文件，如Android/data/com.test.package；非直接子文件，如Android/data/com.test.package/folderName）
     * 在Android 10及之前的系统中，不会出现DocumentFile文件对象
     */
    public boolean isDocumentFileType() {
        return isDocumentFile;
    }

    /**
     * 获取Bean中包含文件的类型
     */
    public FileItemBeanType getBeanType() {
        return fileItemBeanType;
    }

    /**
     * 获取Bean中包含的File
     */
    public File getFile() {
        return file;
    }

    /**
     * 获取Bean中包含的DocumentFile
     */
    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof FileItemBean))
            return false;

        if (this == obj)
            return true;

        FileItemBean fileItemBean = (FileItemBean) obj;

        if (fileItemBean.fileItemBeanType == fileItemBeanType) {
            if (isDocumentFile) {
                return fileItemBean.documentFile.getUri().toString().equals(documentFile.getUri().toString());
            } else {
                return fileItemBean.file.getPath().equals(file.getPath());
            }
        }

        return false;
    }

    public FileBean getFileBean() {
        if (isDocumentFileType()) {
            return new FileBean(getDocumentFile());
        }
        return new FileBean(getFile());
    }
}

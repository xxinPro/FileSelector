package xyz.xxin.fileselector.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileItemBean;

public class FileSortUtil {

    public static List<FileItemBean> sortByName(List<FileItemBean> fileItemBeans) {
        Collator collator = Collator.getInstance(Locale.CHINA);

        return sort(fileItemBeans, new Comparator<FileItemBean>() {
            @Override
            public int compare(FileItemBean fileItemBean1, FileItemBean fileItemBean2) {
                boolean o1_directory = fileItemBean1.isDirectory();
                boolean o2_directory = fileItemBean2.isDirectory();
                // 文件夹放在前面
                if (o1_directory && !o2_directory) {
                    return -1;
                } else if (!o1_directory && o2_directory) {
                    return 1;
                }

                // 判断是否逆序
                if (ConfigBean.getInstance().isReverseOrder) {
                    return -1 * collator.compare(fileItemBean1.getFileName(), fileItemBean2.getFileName());
                }
                return collator.compare(fileItemBean1.getFileName(), fileItemBean2.getFileName());
            }
        });
    }

    public static List<FileItemBean> sortBySize(List<FileItemBean> fileItemBeans) {
        Collator collator = Collator.getInstance(Locale.CHINA);

        return sort(fileItemBeans, new Comparator<FileItemBean>() {
            @Override
            public int compare(FileItemBean fileItemBean1, FileItemBean fileItemBean2) {
                boolean o1_directory = fileItemBean1.isDirectory();
                boolean o2_directory = fileItemBean2.isDirectory();
                int sortValue;
                // 文件夹放在前面
                if (o1_directory && !o2_directory) {
                    return -1;
                } else if (!o1_directory && o2_directory) {
                    return 1;
                } else if (o1_directory) {
                    // 都是文件夹，按名称排序
                    sortValue =  collator.compare(fileItemBean1.getFileName(), fileItemBean2.getFileName());
                } else {
                    // 都是文件，按文件大小排序
                    long diff = fileItemBean1.getFileLength() - fileItemBean2.getFileLength();
                    if (diff > 0) {
                        sortValue = 1;
                    } else if (diff == 0) {
                        sortValue = collator.compare(fileItemBean1.getFileName(), fileItemBean2.getFileName());
                    } else {
                        sortValue = -1;
                    }
                }

                // 判断是否逆序
                if (ConfigBean.getInstance().isReverseOrder) {
                    return -1 * sortValue;
                }
                return sortValue;
            }
        });
    }

    public static List<FileItemBean> sortByTime(List<FileItemBean> fileItemBeans) {
        Collator collator = Collator.getInstance(Locale.CHINA);

        return sort(fileItemBeans, new Comparator<FileItemBean>() {
            @Override
            public int compare(FileItemBean fileItemBean1, FileItemBean fileItemBean2) {
                boolean o1_directory = fileItemBean1.isDirectory();
                boolean o2_directory = fileItemBean2.isDirectory();
                // 文件夹放在前面
                if (o1_directory && !o2_directory) {
                    return -1;
                } else if (!o1_directory && o2_directory) {
                    return 1;
                }

                int sortValue;
                long diff = fileItemBean1.getLastModified() - fileItemBean2.getLastModified();
                if (diff > 0) {
                    sortValue = 1;
                } else if (diff == 0) {
                    // 文件修改时间相同，根据文件名排序
                    sortValue = collator.compare(fileItemBean1.getFileName(), fileItemBean2.getFileName());
                } else {
                    sortValue = -1;
                }

                // 判断是否逆序
                if (ConfigBean.getInstance().isReverseOrder) {
                    return -1 * sortValue;
                }
                return sortValue;
            }
        });
    }

    public static List<FileItemBean> sort(List<FileItemBean> fileItemBeans, Comparator<FileItemBean> fileComparator) {
        if (fileItemBeans == null || fileComparator == null) return null;

        FileItemBean[] _fileItemBeanArr = fileItemBeans.toArray(new FileItemBean[0]);

        Arrays.sort(_fileItemBeanArr, fileComparator);

        return new ArrayList<>(Arrays.asList(_fileItemBeanArr));
    }
}

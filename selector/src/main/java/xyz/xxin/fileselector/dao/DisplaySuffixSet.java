package xyz.xxin.fileselector.dao;

import java.util.HashSet;
import java.util.Set;

/**
 * 储存支持预览略缩图的图片类型后缀名
 */
public class DisplaySuffixSet {
    private final Set<String> stringSet;

    public DisplaySuffixSet() {
        stringSet = new HashSet<>();
    }

    private void add(String str) {
        str = str.toLowerCase().replaceAll(" ", "");
        if (!"".equals(str)) {
            stringSet.add(str);
        }
    }

    public void add(String... strings) {
        for (String str : strings) {
            add(str);
        }
    }

    public boolean contains(String str) {
        str = str.toLowerCase();
        return stringSet.contains(str);
    }

    public Set<String> getAll() {
        return stringSet;
    }
}

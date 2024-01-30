package xyz.xxin.fileselector.dao;

import java.util.HashSet;
import java.util.Set;

public class SuffixFilterSet {
    private final Set<String> stringSet;

    public SuffixFilterSet() {
        stringSet = new HashSet<>();
    }

    public void add(String... strArr) {
        if (strArr.length == 0) return;

        for (String str : strArr) {
            str = str.toLowerCase().replaceAll(" ", "");
            if (!"".equals(str)) {
                stringSet.add(str);
            }
        }
    }

    public boolean contains(String str) {
        str = str.toLowerCase();
        return stringSet.contains(str);
    }

    public boolean isEmpty() {
        return stringSet.isEmpty();
    }

    public void clear() {
        stringSet.clear();
    }
}

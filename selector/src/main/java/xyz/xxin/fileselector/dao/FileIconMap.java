package xyz.xxin.fileselector.dao;

import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * 储存不同类型文件的图标
 * key: 文件类型
 * value: 图标drawable的id
 */
public class FileIconMap {
    private final HashMap<String, Integer> hashMap;

    public FileIconMap() {
        hashMap = new HashMap<>();
    }

    public int size() {
        return hashMap.size();
    }

    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public Integer get(String key) {
        key = key.toLowerCase();
        return hashMap.get(key);
    }

    public Integer put(String key, int value) {
        key = key.toLowerCase().replace(" ", "");
        if (!"".equals(key)) {
            return hashMap.put(key, value);
        }
        return -1;
    }

    public Integer[] put(String[] keys, int[] values) {
        int len = Math.min(keys.length, values.length);

        Integer[] integerArr = new Integer[len];

        for (int i = 0; i < len; i++) {
            integerArr[i] = put(keys[i], values[i]);
        }
        return integerArr;
    }

    public Integer[] put(String[] keys, int publicValue) {
        Integer[] integerArr = new Integer[keys.length];

        for (int i = 0; i < keys.length; i++) {
            integerArr[i] = put(keys[i], publicValue);
        }
        return integerArr;
    }

    public Integer remove(String key) {
        key = key.toLowerCase();
        return hashMap.remove(key);
    }

    public void clear() {
        hashMap.clear();
    }
}

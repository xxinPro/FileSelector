package xyz.xxin.fileselector.dao;

import java.util.ArrayList;
import java.util.List;

public class FirstVisiblePositionStack {
    private final List<Integer> positionList;

    public FirstVisiblePositionStack() {
        positionList = new ArrayList<>();
    }

    public void push(int position) {
        positionList.add(position);
    }

    public int pop() {
        if (positionList.size() == 0) {
            return -1;
        }
        return positionList.remove(positionList.size() - 1);
    }

    /**
     * 移除position自身及之后的元素
     */
    public void sub(int position) {
        for (int i = positionList.size() - 1; i >= position; i--) {
            positionList.remove(position);
        }
    }

    public void clear() {
        positionList.clear();
    }

    public int length() {
        return positionList.size();
    }
}

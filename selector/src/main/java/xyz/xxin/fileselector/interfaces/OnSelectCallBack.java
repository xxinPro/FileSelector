package xyz.xxin.fileselector.interfaces;

public interface OnSelectCallBack {
    /**
     * @param maxSelectCount     最大选择个数
     * @param currentSelectCount 当前选择个数
     */
    void callBack(int maxSelectCount, int currentSelectCount);
}

package xyz.xxin.fileselector.interfaces;

import java.util.List;

import xyz.xxin.fileselector.beans.FileBean;

public interface OnResultCallbackListener {

    void onResult(List<FileBean> result);

    void onCancel();
}

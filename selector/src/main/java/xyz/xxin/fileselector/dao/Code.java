package xyz.xxin.fileselector.dao;

public class Code {
    public static final int LOAD_LIST = 10001;    // 加载列表
    public static final int LOAD_LAST = 10002;    // 返回上个目录

    public static final int REQUEST_DATA_OBB_LOAD = 11001;    // 点击item时请求SAF框架访问权限
    public static final int REQUEST_EXTERNAL = 11002;         // 申请外部储存读写权限
    public static final int REQUEST_EXTERNAL_MANAGER = 11003; // 申请所有文件访问权限时的请求码
    public static final int REQUEST_DATA_OBB_INIT = 11004;    // 初始化时请求SAF框架访问权限
}

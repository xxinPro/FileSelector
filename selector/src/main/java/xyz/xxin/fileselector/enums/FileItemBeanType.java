package xyz.xxin.fileselector.enums;

public enum FileItemBeanType {
    EXTERNAL_FILE,  // 外部储存中基本的File类型文件
    DATA_DIR,       // Android/data目录自身专用类型
    DATA_CHILD_DIR, // Android/data下直接子目录专用的File类型文件
    OBB_DIR,        // Android/obb目录自身专用类型
    OBB_CHILD_DIR,  // Android/obb下直接子目录专用的File类型文件
    DOCUMENT_FILE   // DocumentFile类型文件
}

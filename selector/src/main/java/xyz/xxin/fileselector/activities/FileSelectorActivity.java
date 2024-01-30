package xyz.xxin.fileselector.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.util.List;

import xyz.xxin.fileselector.R;
import xyz.xxin.fileselector.beans.ConfigBean;
import xyz.xxin.fileselector.beans.FileBean;
import xyz.xxin.fileselector.beans.FileItemBean;
import xyz.xxin.fileselector.dao.Code;
import xyz.xxin.fileselector.enums.FileItemBeanType;
import xyz.xxin.fileselector.utils.SAFUtil;
import xyz.xxin.fileselector.utils.PermissionUtil;
import xyz.xxin.fileselector.utils.StatusBarUtils;
import xyz.xxin.fileselector.views.FileSelectorFootBar;
import xyz.xxin.fileselector.views.FileSelectorListView;
import xyz.xxin.fileselector.views.FileSelectorView;
import xyz.xxin.fileselector.views.FileSelectorTitleBar;

public class FileSelectorActivity extends AppCompatActivity {
    private FileItemBean currentDir;                // 当前所在目录文件夹的文件对象
    private FileSelectorView file_selector_view;    // view
    private ConfigBean configBean;                  // 配置类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        findView();
        initData();
        initList();
    }

    /**
     * 初始化
     */
    private void initList() {
        if (permissionManager()) {
            initCurrentDir();
            initView();
            initTitleBar();
            initPathBar();
            initFileList();
            initFootBar();
        }
    }

    private void initView() {
        // 设置状态栏颜色
        StatusBarUtils.setStatusBarColor(this, configBean.statusBarColorId);
        // 状态栏字体颜色
        StatusBarUtils.setStatusTextColor(this, configBean.statusBarIsDarkText);
        // 设置底部虚拟导航栏颜色
        StatusBarUtils.setNavigationBarColor(this, configBean.navigationBarColorId);

        if (configBean.isAutoOpenSelectMode) {
            file_selector_view.startSelectMode();
        }
    }

    private void initFootBar() {
        file_selector_view.setFootBarButtonClick(new FileSelectorFootBar.OnButtonClickListener() {
            @Override
            public void onLeftButtonClick(View view) {
                if (file_selector_view.getSelectedCount() == file_selector_view.getAvailableTotalCount()) {
                    file_selector_view.deselectAll();
                } else {
                    file_selector_view.selectAll();
                }
            }

            @Override
            public void onRightButtonClick(View view) {
                finish();
            }
        });
    }

    /**
     * 管理所需权限，有权限返回true，无权限则申请权限并返回false
     */
    private boolean permissionManager() {
        boolean writeExternal = PermissionUtil.isWriteExternal(this);
        boolean allFilePermission = PermissionUtil.isAllFilePermission();
        if (!writeExternal) {
            PermissionUtil.requestWriteExternal(this, Code.REQUEST_EXTERNAL);
            return false;
        } else if (!allFilePermission) {
            PermissionUtil.requestAllFilePermission(this, Code.REQUEST_EXTERNAL_MANAGER);
            return false;
        } else if (!file_selector_view.isSAFPermission(configBean.initPath)) {
            file_selector_view.getSAFUtil().requestPermission(this, Code.REQUEST_DATA_OBB_INIT);
            return false;
        }
        return true;
    }

    /**
     * 根据传入的根目录，初始化当前所在目录文件夹的文件对象
     */
    private void initCurrentDir() {
        // 获取配置中传入的根目录
        String initPath = configBean.initPath;

        // Android 11 及以上的版本处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 如果documentFileUtil不为空，说明此前申请过SAF框架访问权限，
            // 那么initPath可能是Android/data或Android/obb的直接或间接子目录，
            // Android 11、12的版本中也可能是Android/data或Android/obb目录
            if (file_selector_view.getSAFUtil() != null) {
                DocumentFile documentFile = file_selector_view.getSAFUtil().getDocumentFile(initPath, false);
                currentDir = new FileItemBean(documentFile);
            } else {
                currentDir = new FileItemBean(new File(initPath));
            }
        }
        // Android 10 及以下的版本处理
        else {
            currentDir = new FileItemBean(new File(initPath));
        }
    }

    private void initPathBar() {
        if (currentDir == null) return;

        file_selector_view.initPathBarData(currentDir);

        file_selector_view.setPathBarItemClickListener(new FileSelectorView.OnPathBarItemClickListener() {
            @Override
            public void onItemClick(FileItemBean pointFile, int position) {
                loadListData(pointFile, Code.LOAD_LIST);
            }
        });
    }

    private void initTitleBar() {
        file_selector_view.setTitleBarButtonClickListener(new FileSelectorTitleBar.OnTitleClickListener() {
            @Override
            public void onBackClick(View view) {
                if (file_selector_view.isSelectMode()) {
                    file_selector_view.endSelectMode();
                }
                finish();
            }

            @Override
            public void onTitleClick(View view) {
                file_selector_view.scrollToPosition(0);
            }

            @Override
            public void onSelectClick(View view) {
                if (file_selector_view.isSelectMode()) {
                    file_selector_view.endSelectMode();
                } else if (!file_selector_view.isLoadListing()){
                    file_selector_view.startSelectMode();
                } else {
                    Toast.makeText(FileSelectorActivity.this, "文件正在加载中", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        configBean = ConfigBean.getInstance();
    }

    private void initFileList() {
        if (currentDir == null) return;

        file_selector_view.setFileListItemClickListener(new FileSelectorView.OnFileListItemClickListener() {
            @Override
            public void onNormalClick(FileItemBean fileItemBean, int position) {
                if (!fileItemBean.isDirectory()) return;
                // 加载列表
                loadListData(fileItemBean, Code.LOAD_LIST);
            }

            @Override
            public void onSelectClick(FileItemBean fileItemBean, int position) {
                // 选中或取消选中点击的item
                if (fileItemBean.isSelected()) {
                    file_selector_view.deselect(position);
                } else {
                    file_selector_view.select(position);
                }
            }
        });

        file_selector_view.setFileListItemLongClickListener(new FileSelectorView.OnFileListItemLongClickListener() {
            @Override
            public boolean onNormalClick(FileItemBean fileItemBean, int position) {
                // 进入选择模式并选中自身
                file_selector_view.startSelectMode();
                file_selector_view.select(position);
                return true;
            }

            @Override
            public boolean onSelectClick(FileItemBean fileItemBean, int position) {
                file_selector_view.select(position);
                return true;
            }
        });

        // 启用下拉刷新，列表下拉刷新默认是禁用状态
        if (!configBean.isAutoOpenSelectMode) {
            file_selector_view.setRefreshEnabled(true);
        }

        file_selector_view.setFileListRefreshListener(new FileSelectorListView.OnFileListRefreshListener() {
            @Override
            public void onRefresh() {
                loadListData(currentDir, Code.LOAD_LIST);
            }
        });

        // 加载列表数据
        loadListData(currentDir, Code.LOAD_LIST);
    }

    /**
     * 加载列表数据
     */
    private void loadListData(FileItemBean openFileItemBean, int loadType) {
        currentDir = openFileItemBean;

        // 加载列表数据
        file_selector_view.loadListData(openFileItemBean, loadType, this);
    }

    private void findView() {
        file_selector_view = findViewById(R.id.file_select_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<FileBean> fileBeanList = file_selector_view.getFileBeanList();
        if (fileBeanList.size() > 0) {
            // 回传选择的数据
            configBean.onResultCallbackListener.onResult(fileBeanList);
        } else {
            configBean.onResultCallbackListener.onCancel();
        }
    }

    @Override
    public void onBackPressed() {
        // 如果处于文件选中模式，先关闭文件选中
        if (file_selector_view.isSelectMode()) {
            file_selector_view.endSelectMode();
        } else {
            String initPath = configBean.initPath;
            // 如果已经回到根目录则退出
            if (!(currentDir.getBeanType() == FileItemBeanType.DOCUMENT_FILE) &&
                    initPath.equals(currentDir.getFile().getAbsolutePath())) {
                finish();
            } else if (currentDir.getBeanType() == FileItemBeanType.DOCUMENT_FILE &&
                    initPath.equals(SAFUtil.uriToPath(currentDir.getDocumentFile())) ) {
                finish();
            } else {
                // 否则返回上个目录
                loadListData(currentDir.getParentBean(), Code.LOAD_LAST);
                file_selector_view.popPathBarData();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Code.REQUEST_DATA_OBB_INIT) {
            file_selector_view.getSAFUtil().savePermission(Code.REQUEST_DATA_OBB_INIT, data);

            if (file_selector_view.getSAFUtil().isPermission()) {
                initList();
            } else {
                // 还不给权限可以退出了
                finish();
            }
        } else if (requestCode == Code.REQUEST_DATA_OBB_LOAD) {
            file_selector_view.getSAFUtil().savePermission(Code.REQUEST_DATA_OBB_LOAD, data);

            if (file_selector_view.getSAFUtil().isPermission()) {
                loadListData(currentDir, Code.LOAD_LIST);
            } else {
                loadListData(currentDir.getParentBean(), Code.LOAD_LAST);
                file_selector_view.popPathBarData();
            }
        } else if (requestCode == Code.REQUEST_EXTERNAL_MANAGER && PermissionUtil.isAllFilePermission()) {
            initList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Code.REQUEST_EXTERNAL && PermissionUtil.isWriteExternal(this)) {
            initList();
        }
    }
}
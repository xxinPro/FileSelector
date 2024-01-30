package xyz.xxin.fileselect.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import xyz.xxin.fileselect.engine.GlideEngine;
import xyz.xxin.fileselect.R;
import xyz.xxin.fileselect.decoration.ImageItemDecoration;
import xyz.xxin.fileselector.FileSelectConfig;
import xyz.xxin.fileselector.FileSelector;
import xyz.xxin.fileselector.beans.FileBean;
import xyz.xxin.fileselector.enums.SortRule;
import xyz.xxin.fileselector.interfaces.OnResultCallbackListener;
import xyz.xxin.fileselector.interfaces.OnSelectCallBack;
import xyz.xxin.fileselector.style.FileSelectorFootBarStyle;
import xyz.xxin.fileselector.style.FileSelectorListViewStyle;
import xyz.xxin.fileselector.style.FileSelectorPathBarStyle;
import xyz.xxin.fileselector.style.FileSelectorTitleBarStyle;
import xyz.xxin.fileselector.style.FileSelectorViewStyle;
import xyz.xxin.fileselector.utils.SAFUtil;
import xyz.xxin.fileselector.utils.StatusBarUtils;

public class SelectActivity extends AppCompatActivity implements OnSelectCallBack {
    private final String TAG = SelectActivity.class.getName();

    private Button selector_btn;
    private Button inject_fragment;
    private Button select_path;
    private EditText edit_path;
    private RadioGroup sort_rule;
    private CheckBox is_reverse_order;
    private CheckBox is_display_thumbnail;
    private EditText edit_pattern;
    private CheckBox is_only_display_folder;
    private CheckBox is_only_select_file;
    private EditText file_type;
    private EditText max_select_count;
    private CheckBox is_single;
    private CheckBox is_vibrator;
    private CheckBox is_auto_open_select;
    private RadioGroup icon_set;
    private RadioGroup status_set;
    private RadioGroup navigation_set;
    private RadioGroup back_set;
    private RadioGroup title_set;
    private RadioGroup path_set;
    private RadioGroup list_set;
    private RadioGroup foot_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        findView();
        initView();
        addEvent();
    }

    private void initView() {
        StatusBarUtils.setStatusBarColor(this, R.color.blue);
        StatusBarUtils.setStatusTextColor(this, false);

        edit_path.setText(Environment.getExternalStorageDirectory().getAbsolutePath());

        sort_rule.check(R.id.by_name);
    }

    private void addEvent() {
        // 选择路径
        select_path.setOnClickListener(view -> {
            Toast.makeText(this, "请选择一个目录作为初始路径", Toast.LENGTH_SHORT).show();

            FileSelector.create(this)
                    .isSingle(true)             // 单选模式
                    .isOnlyDisplayFolder(true)  // 只显示文件夹
                    .forResult(new OnResultCallbackListener() {
                        @Override
                        public void onResult(List<FileBean> result) {
                            FileBean fileBean = result.get(0);
                            if (fileBean.isFileType()) {
                                edit_path.setText(fileBean.getFile().getAbsolutePath());
                            } else {
                                // documentFile类型，需要将uri地址转换成文件地址
                                edit_path.setText(SAFUtil.uriToPath(fileBean.getDocumentFile().getUri()));
                            }
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(SelectActivity.this, "没有选择路径", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        inject_fragment.setOnClickListener(view -> {
            FileSelectConfig fileSelectConfig = createFileSelectConfig();

            fileSelectConfig.forResult(R.id.frame_layout, new OnResultCallbackListener() {
                @Override
                public void onResult(List<FileBean> result) {
                    // 文件处理逻辑
                    resultProcess(result);
                }

                @Override
                public void onCancel() {
                    // 未选择处理逻辑
                    Toast.makeText(SelectActivity.this, "未选中文件", Toast.LENGTH_SHORT).show();
                }
            });
        });


        selector_btn.setOnClickListener(view -> {

            FileSelectConfig fileSelectConfig = createFileSelectConfig();

            // 启动
            fileSelectConfig.forResult(new OnResultCallbackListener() {
                @Override
                public void onResult(List<FileBean> result) {
                    // 文件处理逻辑
                    resultProcess(result);
                }

                @Override
                public void onCancel() {
                    // 未选择处理逻辑
                    Toast.makeText(SelectActivity.this, "未选中文件", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void resultProcess(List<FileBean> result) {
        // 选择总数
        int totalCount = result.size();
        // 文件的个数
        int fileCount = 0;
        // 文件夹的个数
        int directoryCount = 0;
        // 文件类型
        String fileBeanType = result.get(0).isFileType() ? "file" : "DocumentFile";

        for (FileBean fileBean : result) {
            // 处理外部储存文件
            if (fileBean.isFileType()) {
                File file = fileBean.getFile();
                if (file.isDirectory()) {
                    directoryCount++;
                } else {
                    fileCount++;
                }
            }
            // 处理Android/data(obb)目录下的文件
            else {
                DocumentFile documentFile = fileBean.getDocumentFile();
                if (documentFile.isDirectory()) {
                    directoryCount++;
                } else {
                    fileCount++;
                }
            }
        }

        Toast.makeText(this, "共计选择 " + fileBeanType + " 类型文件 " + totalCount + " 个，其中文件 " +
                fileCount + " 个，文件夹 " + directoryCount + "个", Toast.LENGTH_SHORT).show();
    }

    private FileSelectConfig createFileSelectConfig() {
        FileSelectConfig fileSelectConfig = FileSelector.create(this)
                .setInitPath(edit_path.getText().toString())                // 设置起始路径
                .setSortRule(getSortRule(), is_reverse_order.isChecked())   // 设置列表排序规则
                .isDisplayThumbnail(is_display_thumbnail.isChecked())       // 设置略缩图加载
                .setImageEngine(GlideEngine.createGlideEngine())            // 设置略缩图加载引擎
                .setLastModifiedPattern(edit_pattern.getText().toString())  // 设置文件修改时间的格式化格式
                .isOnlyDisplayFolder(is_only_display_folder.isChecked())    // 设置是否只显示文件夹
                .isOnlySelectFile(is_only_select_file.isChecked())          // 设置是否只能选中文件
                .addDisplayType(getFileSuffix())                            // 设置需要显示的文件类型
                .setMaxSelectValue(getMaxSelectCount(), this)   // 设置最大选择数，同时设置选择回调
                .isSingle(is_single.isChecked())                            // 是否单选
                .isVibrator(is_vibrator.isChecked())                        // 是否震动
                .isAutoOpenSelectMode(is_auto_open_select.isChecked());     // 是否自动进入选择模式

        // 设置图标
        setIcon(fileSelectConfig);
        // 设置系统状态栏
        setStatus(fileSelectConfig);
        // 设置系统导航栏
        setNavigation(fileSelectConfig);
        // 设置底层背景
        setFileSelectorViewStyle(fileSelectConfig);
        // 设置标题栏
        setFileSelectorTitleBarStyle(fileSelectConfig);
        // 设置路径栏
        setFileSelectorPathBatStyle(fileSelectConfig);
        // 设置列表
        setFileSelectorListViewStyle(fileSelectConfig);
        // 设置底部栏
        setFileSelectorFootBarStyle(fileSelectConfig);

        return fileSelectConfig;
    }

    private void setFileSelectorFootBarStyle(FileSelectConfig fileSelectConfig) {
        FileSelectorFootBarStyle fileSelectorFootBarStyle = new FileSelectorFootBarStyle();

        int checkedRadioButtonId = foot_set.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.custom_foot1) {
            fileSelectorFootBarStyle.setBackgroundColorId(R.color.black);
            fileSelectorFootBarStyle.setBtnBackgroundColorId(R.color.black);
            fileSelectorFootBarStyle.setBtnActionDownBackgroundColorId(R.color.white_transparent);
            fileSelectorFootBarStyle.setButtonTextColorId(R.color.white);
        } else if (checkedRadioButtonId == R.id.custom_foot2) {
            fileSelectorFootBarStyle.setBackgroundColorId(R.color.pink);
            fileSelectorFootBarStyle.setBtnBackgroundColorId(R.color.white);
            fileSelectorFootBarStyle.setBtnActionDownBackgroundColorId(R.color.pink);
        }
        fileSelectConfig.setFileSelectorFootBarStyle(fileSelectorFootBarStyle);
    }

    private void setFileSelectorListViewStyle(FileSelectConfig fileSelectConfig) {
        FileSelectorListViewStyle fileSelectorListViewStyle = new FileSelectorListViewStyle();
        FileSelectorListViewStyle.FileSelectorListItemStyle fileSelectorListItemStyle = new FileSelectorListViewStyle.FileSelectorListItemStyle();

        int checkedRadioButtonId = list_set.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.custom_list1) {
            // 设置列表背景色
            fileSelectorListViewStyle.setBackgroundColorId(R.color.white_transparent);
            // 设置当前目录为空时页面的背景色
            fileSelectorListViewStyle.setEmptyFolderBackgroundColorId(R.color.transparent);
            // 设置加载时环形进度条的颜色
            fileSelectorListViewStyle.setLoadProgressBarColorId(R.color.black);
            // 设置加载页面的背景色
            fileSelectorListViewStyle.setLoadProgressBackgroundColorId(R.color.transparent);
            // item装饰
            fileSelectorListViewStyle.setItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    view.setPadding(0,10,0,10);
                }
            });

            // 设置item背景色
            fileSelectorListItemStyle.setItemBackgroundColorId(R.color.transparent);
            // 设置item被点击时变化的背景色
            fileSelectorListItemStyle.setItemActionDownBackgroundColorId(R.color.white_transparent);
            // 设置item中文件修改时间的字体颜色
            fileSelectorListItemStyle.setLastModifiedTextColorId(R.color.black);
            // 设置item中文件大小的字体颜色
            fileSelectorListItemStyle.setFileSizeTextColorId(R.color.black);
            fileSelectorListItemStyle.setFileNameTextSize(20);
            fileSelectorListItemStyle.setLastModifiedTextSize(16);
            fileSelectorListItemStyle.setFileSizeTextSize(16);
            // 设置item被选中后，checkBox显示的图片drawable资源id
            fileSelectorListItemStyle.setSelectedDrawableId(R.drawable.selected2);
            // 设置item未被选中时，checkBox显示的图片drawable资源id
            fileSelectorListItemStyle.setDeselectedDrawableId(R.drawable.deselected);

            fileSelectorListViewStyle.setFileSelectorListItemStyle(fileSelectorListItemStyle);
        } else if (checkedRadioButtonId == R.id.custom_list2) {
            // 设置加载时环形进度条的颜色
            fileSelectorListViewStyle.setLoadProgressBarColorId(R.color.pink);

            fileSelectorListViewStyle.setItemDecoration(new ImageItemDecoration(this, R.drawable.hello_kitty));

            // 设置item被点击时变化的背景色
            fileSelectorListItemStyle.setItemActionDownBackgroundColorId(R.color.pink);
            // 设置item中文件修改时间的字体颜色
            fileSelectorListItemStyle.setLastModifiedTextColorId(R.color.black);
            // 设置item中文件大小的字体颜色
            fileSelectorListItemStyle.setFileSizeTextColorId(R.color.black);
            // 设置item被选中后，checkBox显示的图片drawable资源id
            fileSelectorListItemStyle.setSelectedDrawableId(R.drawable.selected);
            // 设置item未被选中时，checkBox显示的图片drawable资源id
            fileSelectorListItemStyle.setDeselectedDrawableId(R.drawable.deselected);

            fileSelectorListViewStyle.setFileSelectorListItemStyle(fileSelectorListItemStyle);
        }
        fileSelectConfig.setFileSelectorListViewStyle(fileSelectorListViewStyle);
    }

    private void setFileSelectorPathBatStyle(FileSelectConfig fileSelectConfig) {
        FileSelectorPathBarStyle fileSelectorTitleBarStyle = new FileSelectorPathBarStyle();

        int checkedRadioButtonId = path_set.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.custom_path1) {
            fileSelectorTitleBarStyle.setBackgroundColorId(R.color.black);
            fileSelectorTitleBarStyle.setItemTextColorId(R.color.white);
            fileSelectorTitleBarStyle.setHeadItemTextColorId(R.color.white);
            fileSelectorTitleBarStyle.setArrowImageDrawableId(R.drawable.arrow_forward);
            fileSelectorTitleBarStyle.setItemBackgroundColorId(R.color.white_transparent);
            fileSelectorTitleBarStyle.setHeadItemBackgroundColorId(R.color.white_transparent);
            fileSelectorTitleBarStyle.setDescriptionColorId(R.color.white);
        } else if (checkedRadioButtonId == R.id.custom_path2) {
            fileSelectorTitleBarStyle.setBackgroundColorId(R.color.white);
            fileSelectorTitleBarStyle.setItemBackgroundColorId(R.color.pink);
            fileSelectorTitleBarStyle.setHeadItemBackgroundColorId(R.color.pink);
            fileSelectorTitleBarStyle.setHeadItemTextColorId(R.color.black);
            fileSelectorTitleBarStyle.setItemTextColorId(R.color.black);
        }
        fileSelectConfig.setFileSelectorPathBatStyle(fileSelectorTitleBarStyle);
    }

    private void setFileSelectorTitleBarStyle(FileSelectConfig fileSelectConfig) {
        FileSelectorTitleBarStyle fileSelectorTitleBarStyle = new FileSelectorTitleBarStyle();

        int checkedRadioButtonId = title_set.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.custom_title1) {
            // 设置顶部栏背景色
            fileSelectorTitleBarStyle.setBackgroundColorId(R.color.black);
        } else if (checkedRadioButtonId == R.id.custom_title2) {
            // 设置顶部栏背景色
            fileSelectorTitleBarStyle.setBackgroundColorId(R.color.pink);
            // 设置顶部栏中间标题的字体颜色
            fileSelectorTitleBarStyle.setTitleTextColorId(R.color.black);
            // 设置顶部栏右侧的按钮的字体颜色
            fileSelectorTitleBarStyle.setControlTextColorId(R.color.black);
            // 结束按钮
            fileSelectorTitleBarStyle.setOverImageDrawableId(R.drawable.black_back);
        }
        fileSelectConfig.setFileSelectorTitleBarStyle(fileSelectorTitleBarStyle);
    }

    private void setFileSelectorViewStyle(FileSelectConfig fileSelectConfig) {
        FileSelectorViewStyle fileSelectorViewStyle = new FileSelectorViewStyle();

        int checkedRadioButtonId = back_set.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.custom_back1) {
            fileSelectorViewStyle.setBackgroundDrawableId(R.drawable.background);
        } else if (checkedRadioButtonId == R.id.custom_back2) {
            fileSelectorViewStyle.setBackgroundColorId(R.color.white);
        }
        fileSelectConfig.setFileSelectorViewStyle(fileSelectorViewStyle);
    }

    private void setStatus(FileSelectConfig fileSelectConfig) {
        int checkedRadioButtonId = status_set.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.custom_status1) {
            fileSelectConfig.setStatusBarColor(R.color.black)
                    .setStatusBarTextColor(false);
        } else if (checkedRadioButtonId == R.id.custom_status2) {
            fileSelectConfig.setStatusBarColor(R.color.pink)
                    .setStatusBarTextColor(true);
        }
    }

    private void setNavigation(FileSelectConfig fileSelectConfig) {
        int checkedRadioButtonId = navigation_set.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.custom_navigation1) {
            fileSelectConfig.setNavigationBarColor(R.color.black);
        } else if (checkedRadioButtonId == R.id.custom_navigation2) {
            fileSelectConfig.setNavigationBarColor(R.color.white);
        }
    }

    private void setIcon(FileSelectConfig fileSelectConfig) {
        // 图片
        String[] imgKey = new String[] {"jpg", "jpe", "jpeg", "png", "webp", "gif", "tif", "tiff", "bmp", "dib", "rle", "heif", "heic"};
        // 音乐
        String[] musicKey = new String[] {"mp3", "amr", "wav", "flac", "aac", "ogg", "wma", "m4a", "aif", "aiff", "mid", "midi"};
        // 压缩包
        String[] zipKey = new String[] {"zip", "rar", "7z", "tar", "gz", "bz2", "xz", "tgz"};
        // 视频
        String[] videoKey = new String[] {"mp4", "avi", "mkv", "mov", "wmv", "flv", "mpeg", "mpg", "3gp", "webm", "ogg"};

        int checkedRadioButtonId = icon_set.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.custom_icon1) {
            fileSelectConfig.setDefaultFolderIcon(R.drawable.fs_folder2)
                    .setDefaultFileIcon(R.drawable.fs_file2)
                    .addIconBySuffix("txt", R.drawable.fs_txt2)
                    .addBatchesIconBySuffix(imgKey, R.drawable.fs_img2)
                    .addBatchesIconBySuffix(musicKey, R.drawable.fs_music2)
                    .addBatchesIconBySuffix(zipKey, R.drawable.fs_zip2)
                    .addBatchesIconBySuffix(videoKey, R.drawable.fs_mv2);
        } else if (checkedRadioButtonId == R.id.custom_icon2) {
            fileSelectConfig.setDefaultFolderIcon(R.drawable.fs_folder3)
                    .setDefaultFileIcon(R.drawable.fs_file3)
                    .addIconBySuffix("txt", R.drawable.fs_txt3)
                    .addBatchesIconBySuffix(imgKey, R.drawable.fs_img3)
                    .addBatchesIconBySuffix(musicKey, R.drawable.fs_music3)
                    .addBatchesIconBySuffix(zipKey, R.drawable.fs_zip3)
                    .addBatchesIconBySuffix(videoKey, R.drawable.fs_mv3);
        }
    }

    private SortRule getSortRule() {
        int checkedRadioButtonId = sort_rule.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.by_time) {
            return SortRule.Time;
        } else if (checkedRadioButtonId == R.id.by_size) {
            return SortRule.Size;
        }

        return SortRule.Name;
    }

    private int getMaxSelectCount() {
        String string = max_select_count.getText().toString();
        if ("".equals(string))
            return -1;
        return Integer.parseInt(string);
    }

    private String[] getFileSuffix() {
        String fileTypes = file_type.getText().toString();

        Log.d(TAG, "getFileSuffix: fileTypes = " + fileTypes);
        return fileTypes.split(",");
    }

    private void findView() {
        inject_fragment = findViewById(R.id.inject_fragment);
        selector_btn = findViewById(R.id.selector_btn);
        select_path = findViewById(R.id.select_path);
        edit_path = findViewById(R.id.edit_path);
        sort_rule = findViewById(R.id.sort_rule);
        is_reverse_order = findViewById(R.id.is_reverse_order);
        is_display_thumbnail = findViewById(R.id.is_display_thumbnail);
        edit_pattern = findViewById(R.id.edit_pattern);
        is_only_display_folder = findViewById(R.id.is_only_display_folder);
        is_only_select_file = findViewById(R.id.is_only_select_file);
        file_type = findViewById(R.id.file_type);
        max_select_count = findViewById(R.id.max_select_count);
        is_single = findViewById(R.id.is_single);
        is_vibrator = findViewById(R.id.is_vibrator);
        is_auto_open_select = findViewById(R.id.is_auto_open_select);
        icon_set = findViewById(R.id.icon_set);
        status_set = findViewById(R.id.status_set);
        navigation_set = findViewById(R.id.navigation_set);
        back_set = findViewById(R.id.back_set);
        title_set = findViewById(R.id.title_set);
        path_set = findViewById(R.id.path_set);
        list_set = findViewById(R.id.list_set);
        foot_set = findViewById(R.id.foot_set);
    }

    @Override
    public void callBack(int maxSelectCount, int currentSelectCount) {
        if (currentSelectCount >= maxSelectCount) {
            Toast.makeText(SelectActivity.this, "最多选择" + maxSelectCount + "个文件~", Toast.LENGTH_SHORT).show();
        }
    }
}





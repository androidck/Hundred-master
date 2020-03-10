package com.community.hundred.modules.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.community.hundred.R;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.common.dialog.BaseCustomDialog;
import com.community.hundred.common.download.DownloadUtil;
import com.community.hundred.common.network.permission.Permission;
import com.community.hundred.modules.manager.BookUtils;
import com.community.hundred.modules.manager.entry.BookEntry;
import com.community.hundred.modules.ui.main.fragment.entry.NovEntry;
import com.community.hundred.modules.ui.startup.StartUpActivity;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.thl.reader.ReadActivity;
import com.thl.reader.db.BookList;

import java.io.File;
import java.util.List;

// 下载dialog
public class DownloadDialog extends BaseCustomDialog {

    private ProgressBar progressBar;
    private TextView tv_percentage;
    private Context mContext;
    private Activity mActivity;
    private OnSuccessListener onSuccessListener;
    private NovEntry entry;

    public DownloadDialog(@NonNull Context context, Activity mActivity, NovEntry entry) {
        super(context);
        this.mContext = context;
        this.mActivity = mActivity;
        this.entry = entry;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_download;
    }

    @Override
    public void initView() {
        progressBar = findViewById(R.id.progressBar);
        tv_percentage = findViewById(R.id.tv_percentage);
    }

    @Override
    public void initData() {
        downLoad();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public int showGravity() {
        return Gravity.CENTER;
    }

    public interface OnSuccessListener {
        void onSuccess(int state);
    }

    // 下载
    public void downLoad() {
        XXPermissions.with(mActivity)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            String mTag = entry.getTitle();
                            new DownloadUtil().download(mTag, KeyConstant.NOVEL_SAVE_PATH, entry.getTitle() + ".txt", entry.getContent(), new DownloadUtil.Callback() {
                                @Override
                                public void onSuccess(File file) {
                                    if (!TextUtils.isEmpty(file.getAbsolutePath())) {
                                        BookEntry bookEntry = new BookEntry();
                                        bookEntry.setId(null);
                                        bookEntry.setName(entry.getTitle());
                                        bookEntry.setTags(mTag);
                                        bookEntry.setImages(entry.getImages());
                                        bookEntry.setPath(file.getAbsolutePath());
                                        //保存信息
                                        BookUtils.getInstance().saveBook(bookEntry);
                                        BookList bookList = new BookList();
                                        bookList.setBookname(entry.getTitle());
                                        bookList.setBookpath(file.getAbsolutePath());
                                        ReadActivity.openBook(bookList, mActivity);
                                        dismiss();
                                    }
                                }

                                @Override
                                public void onProgress(int progress) {
                                    progressBar.setProgress(progress);
                                    tv_percentage.setText(progress + "%");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    dismiss();
                                    toast("小说处理失败，请稍后重试");
                                }
                            });
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            toast("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        }
                    }
                });

    }
}

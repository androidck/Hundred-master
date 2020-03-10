package com.community.hundred.modules.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.adapter.GridImageAdapter;
import com.community.hundred.common.adapter.ImageAdapter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.constant.KeyConstant;
import com.community.hundred.common.dialog.InputDialog;
import com.community.hundred.common.dialog.MenuDialog;
import com.community.hundred.common.dialog.MessageDialog;
import com.community.hundred.common.manager.FullyGridLayoutManager;
import com.community.hundred.common.network.permission.Permission;
import com.community.hundred.common.upload.FileUploadObserver;
import com.community.hundred.common.upload.RetrofitClient;
import com.community.hundred.common.upload.UploadFileApi;
import com.community.hundred.common.util.GlideEngine;
import com.community.hundred.common.util.PhotoUtils;
import com.community.hundred.modules.dialog.SelectPhotoDialog;
import com.community.hundred.modules.eventbus.ModifyUserWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.post.SendPostActivity;
import com.community.hundred.modules.ui.user.entry.UserAlbumEntry;
import com.community.hundred.modules.ui.user.entry.UserInfoEntry;
import com.community.hundred.modules.ui.user.presenter.ModifyUserDataPresenter;
import com.community.hundred.modules.ui.user.presenter.view.IModifyUserView;
import com.hjq.base.BaseDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.widget.layout.SettingBar;
import com.hjq.widget.view.ClearEditText;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.ToastUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;

import static com.community.hundred.common.util.Util.hasSdcard;

// 资料修改
@Route(path = ActivityConstant.MODIFY_USER_DATA)
public class ModifyUserDataActivity extends MyActivity<IModifyUserView, ModifyUserDataPresenter> implements View.OnClickListener {
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;

    private View headerView;

    private SettingBar update_user_head, update_nick_name, update_sex, update_age, update_sign;
    private CircleImageView user_head;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 9;//最多到几张还可以选择

    //拍照
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.png");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.png");
    private Uri imageUri;
    private Uri cropImageUri;

    private UserInfoEntry userInfoEntry;

    private String image;
    private String nickName;
    private String sex;
    private String age;
    private String qianmin;

    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private int themeId;
    private List<UserAlbumEntry> imageList = new ArrayList<>();

    @Override
    protected ModifyUserDataPresenter createPresenter() {
        return new ModifyUserDataPresenter(this);
    }


    private int language = -1;

    private ImageAdapter mImageAdapter;
    private int checkPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_user_data;
    }


    @Override
    protected void initView() {
        setTitle("修改个人资料");
        setRightTitle("保存");
        setLeftIcon(R.mipmap.bar_icon_back_white);
        EventBus.getDefault().register(this);
        getTitleBar().getRightView().setTextColor(getResources().getColor(R.color.white));
        recyclerView.setLayoutManager(new GridLayoutManager(ModifyUserDataActivity.this, 3));
        headerView = getLayoutInflater().inflate(R.layout.view_header_modify_user_data, recyclerView, false);
        recyclerView.addHeaderView(headerView);
        user_head = headerView.findViewById(R.id.user_head);
        update_user_head = headerView.findViewById(R.id.update_user_head);
        update_nick_name = headerView.findViewById(R.id.update_nick_name);
        update_sex = headerView.findViewById(R.id.update_sex);
        update_age = headerView.findViewById(R.id.update_age);
        update_sign = headerView.findViewById(R.id.update_sign);
        user_head = headerView.findViewById(R.id.user_head);


        update_user_head.setOnClickListener(this);
        update_nick_name.setOnClickListener(this);
        update_sex.setOnClickListener(this);
        update_age.setOnClickListener(this);
        update_sign.setOnClickListener(this);

        themeId = R.style.picture_default_style;
        getDefaultStyle();

        // 相册处理
        mImageAdapter = new ImageAdapter(imageList);
        mImageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onTakePhotoClick() {
                photoIsVideo();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (imageList.size() != 0) {
                    new MessageDialog.Builder(ModifyUserDataActivity.this)
                            .setTitle("删除照片")
                            .setMessage("确认要删除照片吗？")
                            .setConfirm(getString(R.string.common_confirm))
                            .setCancel(getString(R.string.common_cancel))
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    mPresenter.delUserImg(imageList.get(position).getId());
                                    checkPosition = position;
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

            }
        });
        recyclerView.setAdapter(mImageAdapter);
    }


    // 默认主题
    private void getDefaultStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(this, R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = getResources().getColor(R.color.colorAccent);
    }

    @Override
    protected void initData() {
        mPresenter.getUserData();
        mPresenter.setOnUserDataListener(entry -> {
            Glide.with(this).load(HttpConstant.BASE_HOST + entry.getImage()).into(user_head);
            update_nick_name.setRightText(entry.getNickname());
            userInfoEntry = entry;
            if ("1".equals(entry.getSex())) {
                update_sex.setRightText("男");
            } else {
                update_sex.setRightText("女");
            }
            update_age.setRightText(entry.getAge() + "岁");
            if (!TextUtils.isEmpty(entry.getQianming())) {
                update_sign.setRightText(entry.getQianming());
            }
            if (entry.getAlbum().size() != 0) {
                imageList.addAll(entry.getAlbum());
                mImageAdapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModifyUserWrap(ModifyUserWrap modifyUserWrap) {
        if (modifyUserWrap.code == 200) {
            imageList.remove(checkPosition);
            mImageAdapter.notifyItemRemoved(checkPosition);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        updateUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_user_head:
                updateUserHead();
                break;
            case R.id.update_nick_name:
                setNickName();
                break;
            case R.id.update_sex:
                setSex();
                break;
            case R.id.update_age:
                setAge();
                break;
            case R.id.update_sign:
                setQianMin();
                break;
        }
    }

    // 输入昵称
    public void setNickName() {
        new InputDialog.Builder(this)
                .setTitle("昵称修改")
                .setHint("请输入新昵称")
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        nickName = content;
                        update_nick_name.setRightText(nickName);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 选择性别
    public void setSex() {
        List<String> data = new ArrayList<>();
        data.add("男");
        data.add("女");
        // 底部选择框
        new MenuDialog.Builder(this)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        if (string.equals("男")) {
                            sex = "1";
                        } else if (string.equals("女")) {
                            sex = "2";
                        }
                        update_sex.setRightText(string);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // 选择年龄
    public void setAge() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            data.add("" + i);
        }
        // 底部选择框
        new MenuDialog.Builder(this)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        age = string;
                        update_age.setRightText(string + "岁");
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // 设置签名
    public void setQianMin() {
        new InputDialog.Builder(this)
                .setTitle("修改签名")
                .setHint("请输入签名")
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        qianmin = content;
                        update_sign.setRightText(content);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 修改头像dialog
    public void updateUserHead() {
        new SelectPhotoDialog(this, position -> {
            switch (position) {
                case 1:
                    getPhoto();
                    break;
                case 2:
                    getAlbum();
                    break;
            }
        }).show();
    }

    // 拍照
    public void getPhoto() {
        XXPermissions.with(ModifyUserDataActivity.this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA_GROUP) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            if (hasSdcard()) {
                                imageUri = Uri.fromFile(fileUri);
                                //通过FileProvider创建一个content类型的Uri
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    imageUri = FileProvider.getUriForFile(getActivity(), KeyConstant.provider, fileUri);
                                }
                                //调用系统相机
                                Intent intentCamera = new Intent();
                                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                //将拍照结果保存至photo_file的Uri中，不保留在相册中
                                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intentCamera, CODE_CAMERA_REQUEST);
                            } else {
                                toast("设备没有SD卡！");
                            }
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(ModifyUserDataActivity.this);
                        } else {
                            toast("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(ModifyUserDataActivity.this);
                        }
                    }
                });
    }

    // 相册选择
    public void getAlbum() {
        XXPermissions.with(ModifyUserDataActivity.this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA_GROUP) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(photoPickerIntent, CODE_GALLERY_REQUEST);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(ModifyUserDataActivity.this);
                        } else {
                            toast("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(ModifyUserDataActivity.this);
                        }
                    }
                });
    }

    // 更新资料
    public void updateUserInfo() {
        if (TextUtils.isEmpty(nickName)) {
            nickName = userInfoEntry.getNickname();
        } else if (TextUtils.isEmpty(image)) {
            image = userInfoEntry.getImage();
        } else if (TextUtils.isEmpty(qianmin)) {
            qianmin = userInfoEntry.getQianming();
        } else if (TextUtils.isEmpty(age)) {
            age = userInfoEntry.getAge();
        } else if (TextUtils.isEmpty(sex)) {
            sex = userInfoEntry.getSex();
        } else {
            mPresenter.setUserData(nickName, image, qianmin, age, sex);
        }

    }

    // 选择视频还是照片
    public void photoIsVideo() {
        PictureSelector.create(ModifyUserDataActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setLanguage(language)// 设置语言，默认中文
                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                .maxSelectNum((maxSelectNum - imageList.size()))// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .isGif(false)// 是否显示gif图片
                //.selectionMedia(selectList)// 是否传入已选图片
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    List<File> files = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        File file = new File(selectList.get(i).getPath());
                        files.add(file);
                    }
                    getAlbumUpload(files);

                    break;
                // 相机返回
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    cropImageUri(imageUri);
                    break;
                // 相册返回
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(getActivity(), data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(getActivity(), KeyConstant.provider, new File(newUri.getPath()));
                        }
                        cropImageUri(newUri);
                    } else {
                        toast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (fileCropUri != null) {
                        userHeadUpload(fileCropUri);
                    }
                    break;
            }
        }
    }

    /**
     * @param orgUri 剪裁原图的Uri
     */
    public void cropImageUri(Uri orgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        //发送裁剪信号
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }


    // 头像上传
    public void userHeadUpload(File files) {
        RetrofitClient.getInstance().upLoadFile(UploadFileApi.UPLOAD_FILE_URL, "", files,
                new FileUploadObserver<ResponseBody>() {
                    @Override
                    public void onUploadSuccess(ResponseBody responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            image = jsonObject.getString("data");
                            Glide.with(ModifyUserDataActivity.this).load(HttpConstant.BASE_HOST + image).into(user_head);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onUploadFail(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.d("进度", String.valueOf(progress));
                    }
                });
    }

    // 相册上传
    public void getAlbumUpload(List<File> files) {
        RetrofitClient.getInstance().upLoadFiles(UploadFileApi.SEND_POST_ULR, files,
                new FileUploadObserver<ResponseBody>() {
                    @Override
                    public void onUploadSuccess(ResponseBody responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            image = jsonObject.getString("data");
                            // 这里刷新数据
                            int code = jsonObject.getInt("ret");
                            if (code == 200) {
                                imageList.clear();
                                mPresenter.getUserData();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onUploadFail(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.d("进度", String.valueOf(progress));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.community.hundred.modules.ui.post;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.adapter.GridImageAdapter;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyActivity;
import com.community.hundred.common.constant.ActivityConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.dialog.MenuDialog;
import com.community.hundred.common.listener.DragListener;
import com.community.hundred.common.manager.FullyGridLayoutManager;
import com.community.hundred.common.upload.FileUploadObserver;
import com.community.hundred.common.upload.RetrofitClient;
import com.community.hundred.common.upload.UploadFileApi;
import com.community.hundred.common.util.GlideEngine;
import com.community.hundred.common.util.LocationUtils;
import com.community.hundred.modules.adapter.SendPostCircleAdapter;
import com.community.hundred.modules.eventbus.LocationWrap;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildHeaderEntry;
import com.community.hundred.modules.ui.post.entry.CircleEntry;
import com.community.hundred.modules.ui.post.presenter.SendPostPresenter;
import com.community.hundred.modules.ui.post.presenter.view.ISendPostView;
import com.community.hundred.modules.ui.user.ModifyUserDataActivity;
import com.hjq.base.BaseDialog;
import com.hjq.widget.view.ClearEditText;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;


// 发布帖子
@Route(path = ActivityConstant.SEND_POST)
public class SendPostActivity extends MyActivity<ISendPostView, SendPostPresenter> {

    private static final String TAG = "SendPostActivity";
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;

    private View headView;

    private RecyclerView recycler;
    private TextView tv_location;
    private GridImageAdapter mAdapter;

    private int maxSelectNum = 6;

    private int language = -1;
    private boolean isUpward;

    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private int themeId;

    private List<LocalMedia> selectList;

    private ClearEditText edContent;

    private List<String> imageList = new ArrayList<>();


    private String qzId;

    // 圈子列表
    private List<CircleChildHeaderEntry> list = new ArrayList<>();

    private List<CircleChildHeaderEntry> newList = new ArrayList<>();

    private SendPostCircleAdapter adapter;


    private String image;

    private String cityStr;

    @Override
    protected SendPostPresenter createPresenter() {
        return new SendPostPresenter(this);
    }

    // 立即发布按钮
    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        isVerification();
    }

    @Override
    public void isVerification() {
        super.isVerification();
        if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
            toast("圈子内容不能为空");
        } else {
            for (int i = 0; i < selectList.size(); i++) {
                File file = new File(selectList.get(i).getPath());
                imgUpload(file);
            }

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_post;
    }


    @Override
    protected void initView() {
        setRightTitle("立即发布");

        // 注册eventBus
        EventBus.getDefault().register(this);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        headView = getLayoutInflater().inflate(R.layout.view_header_post, recyclerView, false);
        recyclerView.addHeaderView(headView);
        recycler = headView.findViewById(R.id.recycler);
        edContent = headView.findViewById(R.id.ed_content);
        tv_location = headView.findViewById(R.id.tv_location);
        adapter = new SendPostCircleAdapter(this, newList);
        recyclerView.setAdapter(adapter);
        getTitleBar().getRightView().setTextColor(getResources().getColor(R.color.white));
        adapter.setOnItemClickListener(position -> {
            for (int i = 0; i < newList.size(); i++) {
                if (position == i) {
                    newList.get(i).setSelect(true);
                    qzId = newList.get(i).getId();
                } else {
                    newList.get(i).setSelect(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
        // 照片选择器
        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(fullyGridLayoutManager);
        recycler.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(this, 8), false));
        themeId = R.style.picture_default_style;
        getDefaultStyle();
        mAdapter.setSelectMax(maxSelectNum);
        recycler.setAdapter(mAdapter);
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        // adapter 点击事件
        mAdapter.setOnItemClickListener((position, v) -> {
            List<LocalMedia> selectList = mAdapter.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(SendPostActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(SendPostActivity.this)
                                .externalPictureAudio(
                                        media.getPath().startsWith("content://") ? media.getAndroidQToPath() : media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(SendPostActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });

        // 注册外部预览图片删除按钮回调
        BroadcastManager.getInstance(SendPostActivity.this).registerReceiver(broadcastReceiver,
                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
    }


    @Override
    protected void initData() {
        getQzClassify();
        LocationUtils.getInstance(this).init();
        LocationUtils.getInstance(this).startLocation();
    }

    @Override
    public boolean isStatusBarEnabled() {
        return super.isStatusBarEnabled();
    }

    // 获取圈子
    public void getQzClassify() {
        mPresenter.getQzFl();
        mPresenter.setOnClassifyListener(list1 -> {
            list.addAll(list1);
            if (TextUtils.isEmpty(qzId)) {
                qzId = list.get(0).getId();
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    list.get(i).setSelect(true);
                } else
                    list.get(i).setSelect(false);
                newList.add(list.get(i));
            }

            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean statusBarDarkFont() {
        return !super.statusBarDarkFont();
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


    private int gallery; // 类型
    private int maxNum; // 最大数量
    private int multiple;// 单选or多选

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            List<String> data = new ArrayList<>();
            data.add("照片");
            data.add("视频");
            // 底部选择框
            new MenuDialog.Builder(SendPostActivity.this)
                    .setList(data)
                    .setListener(new MenuDialog.OnListener<String>() {
                        @Override
                        public void onSelected(BaseDialog dialog, int position, String string) {
                            if (string.equals("照片")) {
                                // 照片类型
                                if (selectList != null) {
                                    selectList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                gallery = PictureMimeType.ofImage();
                                maxNum =maxSelectNum;
                                multiple = PictureConfig.MULTIPLE;
                                photoIsVideo(gallery, maxNum, multiple);
                            } else if (string.equals("视频")) {
                                if (selectList != null) {
                                    selectList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                gallery = PictureMimeType.ofVideo();
                                maxNum = 1;
                                multiple = PictureConfig.SINGLE;
                                photoIsVideo(gallery, maxNum, multiple);
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };

    // 选择视频还是照片
    public void photoIsVideo(int gallery, int maxNum, int multiple) {
        PictureSelector.create(SendPostActivity.this)
                .openGallery(gallery)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .setLanguage(language)// 设置语言，默认中文
                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                .maxSelectNum(maxNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(multiple)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .isGif(false)// 是否显示gif图片
                .selectionMedia(mAdapter.getData())// 是否传入已选图片
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    // 权限管理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE:
                // 存储权限
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
                    } else {
                        Toast.makeText(SendPostActivity.this,
                                getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapter.getData());
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras;
            switch (action) {
                case BroadcastAction.ACTION_DELETE_PREVIEW_POSITION:
                    // 外部预览删除按钮回调
                    extras = intent.getExtras();
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.s(SendPostActivity.this, "delete image index:" + position);
                    if (position < mAdapter.getData().size()) {
                        mAdapter.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            BroadcastManager.getInstance(SendPostActivity.this).unregisterReceiver(broadcastReceiver,
                    BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
        LocationUtils.getInstance(this).destroyLocation();
        EventBus.getDefault().unregister(this);
    }

    // 图片上传
    public void imgUpload(File files) {
        showLoading();
        RetrofitClient.getInstance().upLoadFile(UploadFileApi.UPLOAD_FILE_URL, "", files,
                new FileUploadObserver<ResponseBody>() {
                    @Override
                    public void onUploadSuccess(ResponseBody responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            Log.d("sendQzResult", responseBody.string());
                            image = jsonObject.getString("data");
                            imageList.add(image);
                            if (imageList.size() == selectList.size()) {
                                StringBuilder builder = new StringBuilder();
                                for (int j = 0; j < imageList.size(); j++) {
                                    builder.append(imageList.get(j) + ",");
                                }
                                String images = builder.toString().substring(0, builder.toString().length() - 1);
                                mPresenter.sendQz(edContent.getText().toString().trim(), cityStr, qzId, images);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationWrap(LocationWrap locationWrap) {
        cityStr = locationWrap.location.getCity();
        tv_location.setText(cityStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i(TAG, "是否压缩:" + media.isCompressed());
                        Log.i(TAG, "压缩:" + media.getCompressPath());
                        Log.i(TAG, "原图:" + media.getPath());
                        Log.i(TAG, "是否裁剪:" + media.isCut());
                        Log.i(TAG, "裁剪:" + media.getCutPath());
                        Log.i(TAG, "是否开启原图:" + media.isOriginal());
                        Log.i(TAG, "原图路径:" + media.getOriginalPath());
                        Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                    }
                    mAdapter.setList(selectList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case PictureConfig.PREVIEW_VIDEO_CODE:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mAdapter.setList(selectList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}

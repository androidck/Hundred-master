package com.community.hundred.common.upload;

import android.text.TextUtils;
import android.util.Log;

import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.post.SendPostActivity;
import com.community.hundred.modules.ui.post.entry.SendPostEntry;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MultipartBuilder {
    /**
     * 单文件上传构造.
     *
     * @param file        文件
     * @param requestBody 请求体
     * @return MultipartBody
     */
    public static MultipartBody fileToMultipartBody(String uid,File file, RequestBody requestBody) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", file.getName(), requestBody);
        if (!TextUtils.isEmpty(uid)){
            builder.addFormDataPart("uid", uid);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    /**
     * 多文件上传构造.
     *
     * @param files              文件列表
     * @param fileUploadObserver 文件上传回调
     * @return MultipartBody
     */
    public static MultipartBody filesToMultipartBody(List<File> files,
                                                     FileUploadObserver<ResponseBody> fileUploadObserver) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            UploadFileRequestBody uploadFileRequestBody =
                    new UploadFileRequestBody(file, fileUploadObserver);
            builder.addFormDataPart("file", file.getName(), uploadFileRequestBody);
            builder.addFormDataPart("uid", LoginUtils.getInstance().getUid());
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}

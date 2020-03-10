package com.community.hundred.modules.eventbus;

import com.amap.api.location.AMapLocation;

// 发送评论通知
public class CommentWrap {

    public final int code;

    private CommentWrap(int code) {
        this.code = code;
    }

    public static CommentWrap getInstance(int code) {
        return new CommentWrap(code);
    }
}

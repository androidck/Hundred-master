package com.community.hundred.modules.eventbus;


public class PostDetailsWrap {

    public final int code;

    private PostDetailsWrap(int code) {
        this.code = code;
    }

    public static PostDetailsWrap getInstance(int code) {
        return new PostDetailsWrap(code);
    }
}

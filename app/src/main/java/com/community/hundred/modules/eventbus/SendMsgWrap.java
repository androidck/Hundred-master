package com.community.hundred.modules.eventbus;

public class SendMsgWrap {
    public final int code;

    private SendMsgWrap(int code) {
        this.code = code;
    }

    public static SendMsgWrap getInstance(int code) {
        return new SendMsgWrap(code);
    }
}

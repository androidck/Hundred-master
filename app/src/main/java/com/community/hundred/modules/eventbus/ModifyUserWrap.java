package com.community.hundred.modules.eventbus;

public class ModifyUserWrap {

    public final int code;

    private ModifyUserWrap(int code) {
        this.code = code;
    }

    public static ModifyUserWrap getInstance(int code) {
        return new ModifyUserWrap(code);
    }
}

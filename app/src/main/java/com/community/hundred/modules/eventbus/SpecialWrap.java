package com.community.hundred.modules.eventbus;

// event bus
public class SpecialWrap {

    public final int code;

    private SpecialWrap(int code) {
        this.code = code;
    }

    public static SpecialWrap getInstance(int code) {
        return new SpecialWrap(code);
    }
}

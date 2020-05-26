package com.community.hundred.modules.eventbus;

// 发送通知
public class TabWrap {

    public final int position;

    private TabWrap(int position) {
        this.position = position;
    }

    public static TabWrap getInstance(int position) {
        return new TabWrap(position);
    }
}

package com.community.hundred.modules.eventbus;

import com.community.hundred.modules.ui.main.fragment.entry.SendGiftEntry;

// 送礼物 发送消息
public class SendGiftWrap {

    public final SendGiftEntry entry;

    private SendGiftWrap(SendGiftEntry entry) {
        this.entry = entry;
    }

    public static SendGiftWrap getInstance(SendGiftEntry entry) {
        return new SendGiftWrap(entry);
    }
}

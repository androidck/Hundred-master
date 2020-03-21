package com.community.hundred.modules.ui.chat.entry;

// 消息实体类
public class MsgEntry {

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SEND = 1;

    private String content;

    private String userHead;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public MsgEntry() {
    }

    public MsgEntry(String content, String userHead, int type) {
        this.content = content;
        this.userHead = userHead;
        this.type = type;
    }
}

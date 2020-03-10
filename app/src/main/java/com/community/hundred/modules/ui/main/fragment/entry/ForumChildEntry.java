package com.community.hundred.modules.ui.main.fragment.entry;

import java.util.List;

// 实体类
public class ForumChildEntry {

    private String id;

    private String userHead;

    private String nickName;

    private String weight;

    private String height;

    private String cup;;

    private String desc;

    private List<ForumChildVideoEntry> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ForumChildVideoEntry> getList() {
        return list;
    }

    public void setList(List<ForumChildVideoEntry> list) {
        this.list = list;
    }
}

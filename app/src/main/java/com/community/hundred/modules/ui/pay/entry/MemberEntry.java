package com.community.hundred.modules.ui.pay.entry;

// 会员选择实体类
public class MemberEntry {

    private String id;

    private String time;

    private String money;

    public MemberEntry(String id, String time, String money, boolean isSelect) {
        this.id = id;
        this.time = time;
        this.money = money;
        this.isSelect = isSelect;
    }

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

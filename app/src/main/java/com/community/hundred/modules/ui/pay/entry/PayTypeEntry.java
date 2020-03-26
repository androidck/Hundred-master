package com.community.hundred.modules.ui.pay.entry;

// 支付实体类
public class PayTypeEntry {

    private String name;

    private int icon;

    private boolean isSelect;

    public PayTypeEntry(String name, int icon, boolean isSelect) {
        this.name = name;
        this.icon = icon;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

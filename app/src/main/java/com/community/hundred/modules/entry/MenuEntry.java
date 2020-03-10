package com.community.hundred.modules.entry;

// 二级分类筛选菜单
public class MenuEntry {

    private String id;

    private String title;

    private boolean isSelect;

    public MenuEntry(String id, String title, boolean isSelect) {
        this.id = id;
        this.title = title;
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

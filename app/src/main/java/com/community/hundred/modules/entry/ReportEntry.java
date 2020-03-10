package com.community.hundred.modules.entry;

// 举报实体类
public class ReportEntry {

    private String content;

    private boolean isSelect;

    public ReportEntry() {
    }

    public ReportEntry(String content, boolean isSelect) {
        this.content = content;
        this.isSelect = isSelect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

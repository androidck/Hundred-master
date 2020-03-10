package com.community.hundred.modules.ui.secondlevel.entry;

import com.samluys.filtertab.base.BaseFilterBean;

import java.util.ArrayList;
import java.util.List;

public class SecondTabEntry extends BaseFilterBean {
    /**
     * 选项ID
     */
    private int tid;
    /**
     * 选项名称
     */
    private String name;
    /**
     * 选择状态
     */
    private int selected;

    public SecondTabEntry(int tid, String name, int selected) {
        this.tid = tid;
        this.name = name;
        this.selected = selected;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public void setCondition(String str) {

    }

    @Override
    public int getId() {
        return tid;
    }

    @Override
    public int getSelecteStatus() {
        return selected;
    }

    @Override
    public void setSelecteStatus(int status) {
        this.selected = status;
    }

    @Override
    public String getSortTitle() {
        return null;
    }

    @Override
    public List getChildList() {
        return null;
    }

    @Override
    public String getSortKey() {
        return null;
    }

}

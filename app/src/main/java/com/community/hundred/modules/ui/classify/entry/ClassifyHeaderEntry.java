package com.community.hundred.modules.ui.classify.entry;

import java.util.ArrayList;

// 一级实体类
public class ClassifyHeaderEntry {

    /**
     * id : 22
     * name : 明星专区
     * image : http://yy.jsmjsw.cn/app/q8.tp
     * parent_id : 0
     * sort : 0
     * status : 1
     * record_time : null
     * sorts : 1
     */

    private String id;
    private String name;
    private String image;
    private String parent_id;
    private String sort;
    private String status;
    private Object record_time;
    private String sorts;
    private ArrayList<ClassifyChildEntry> son;

    public ArrayList<ClassifyChildEntry> getSon() {
        return son;
    }

    public void setSon(ArrayList<ClassifyChildEntry> son) {
        this.son = son;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRecord_time() {
        return record_time;
    }

    public void setRecord_time(Object record_time) {
        this.record_time = record_time;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }
}

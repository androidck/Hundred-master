package com.community.hundred.modules.ui.main.fragment.entry;

import java.util.List;

public class HomeEntry {
    /**
     * id : 1
     * name : 推荐
     * image : http://yinshi.papang.top/
     * er : [{"id":2,"name":"排行","image":""},{"id":3,"name":"每日","image":""},{"id":4,"name":"助眠","image":""},{"id":5,"name":"精品","image":""},{"id":6,"name":"小雅","image":""}]
     */

    private String id;
    private String name;
    private String image;
    private List<HomeChildMenuEntry> er;

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

    public List<HomeChildMenuEntry> getEr() {
        return er;
    }

    public void setEr(List<HomeChildMenuEntry> er) {
        this.er = er;
    }

    @Override
    public String toString() {
        return "HomeEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", er=" + er +
                '}';
    }
}

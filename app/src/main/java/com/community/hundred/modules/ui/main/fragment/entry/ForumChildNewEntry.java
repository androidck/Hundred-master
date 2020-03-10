package com.community.hundred.modules.ui.main.fragment.entry;

import java.util.List;

public class ForumChildNewEntry {
    /**
     * id : 3
     * name : 神咲詩織
     * image : http://yinshi.papang.top/uploads/picture/20200205/0c1772a4af06b4f39c79fc66f3a3c0be.jpg
     * shengao : 161
     * tizhong : 69
     * zhaobei : B
     * description : 神咲诗织（かみさき しおり），1990年8月出生于日本东京，AV女优。2011年3月，加入AV界
     * video : [{"id":4,"image":"http://yinshi.papang.top/uploads/picture/20190920/timg2.jpg","link":"http://yinshi.papang.top/uploads/video/2019/20190917/8575ff5609cc8b6bd93ed19b5a4bf5ec.mp4","name":"两情侣在阳台上疯狂搞 住他对面的幸福了"}]
     */

    private String id;
    private String name;
    private String image;
    private String shengao;
    private String tizhong;
    private String zhaobei;
    private String description;
    private List<ForumChildNewVideoEntry> video;

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

    public String getShengao() {
        return shengao;
    }

    public void setShengao(String shengao) {
        this.shengao = shengao;
    }

    public String getTizhong() {
        return tizhong;
    }

    public void setTizhong(String tizhong) {
        this.tizhong = tizhong;
    }

    public String getZhaobei() {
        return zhaobei;
    }

    public void setZhaobei(String zhaobei) {
        this.zhaobei = zhaobei;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<ForumChildNewVideoEntry> getVideo() {
        return video;
    }

    public void setVideo(List<ForumChildNewVideoEntry> video) {
        this.video = video;
    }
}

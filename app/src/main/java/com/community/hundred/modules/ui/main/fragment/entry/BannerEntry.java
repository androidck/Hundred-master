package com.community.hundred.modules.ui.main.fragment.entry;

// banner 实体类
public class BannerEntry {
    /**
     * id : 15
     * image : http://yinshi.papang.top//uploads/picture/20200114/f48a2309688617e8be80a0af6e7419ea.jpg
     * link : http://www.baidu.com
     */

    private int id;
    private String image;
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "BannerEntry{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}

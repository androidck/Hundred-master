package com.community.hundred.modules.manager.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BookEntry {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String path;

    private String images;

    private String tags;

    @Generated(hash = 350642338)
    public BookEntry(Long id, String name, String path, String images,
            String tags) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.images = images;
        this.tags = tags;
    }

    @Generated(hash = 137482324)
    public BookEntry() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImages() {
        return this.images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    
}

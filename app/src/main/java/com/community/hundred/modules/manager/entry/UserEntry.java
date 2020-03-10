package com.community.hundred.modules.manager.entry;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

//存储用户信息
@Entity
public class UserEntry{

    @Id(autoincrement = true)
    private Long id;

    private String userId;

    private String phone;

    @Generated(hash = 799922127)
    public UserEntry(Long id, String userId, String phone) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
    }

    @Generated(hash = 1412082065)
    public UserEntry() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    
}

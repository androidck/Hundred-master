package com.community.hundred.modules.ui.post.entry;

public class CommentEntry {

    /**
     * id : 24
     * circle_id : 42
     * user_id : 9
     * content : 哈喽
     * love : 0
     * record_time : 1582819980
     * status : 0
     * userinfo : {"id":"9","nickname":"轮回的悲伤","image":"/api/public/uploads/b1451b97228b5c6f06c4744122e692a1.png","sex":"1","age":"18","city":"开创","period":"1798970703","isvip":1}
     */

    private String id;
    private String circle_id;
    private String user_id;
    private String content;
    private String love;
    private String record_time;
    private String status;
    private CommentUserInfo userinfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CommentUserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(CommentUserInfo userinfo) {
        this.userinfo = userinfo;
    }
}

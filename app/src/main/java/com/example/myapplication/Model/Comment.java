package com.example.myapplication.Model;

public class Comment {
    private String _id;
    private String comicId;
    private String userId;
    private String content;
    private String timestamp;


    public Comment(String _id, String comicId, String userId, String content,String timestamp) {
        this._id = _id;
        this.comicId = comicId;
        this.userId = userId;
        this.content = content;
this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.zamanak.msglib.model;

import java.io.Serializable;

/**
 * Created by PirFazel on 1/29/2017.
 */

public class Message implements Serializable {

    private int user_id;
    private String seenDate;
    private int seen;
    private String message;
    private String id;
    private String title;
    private Object data;
    private String seenAt;
    private String createdAtP;
    private long createdAt;

    public int getSeen() {
        return seen;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(String seenAt) {
        this.seenAt = seenAt;
    }

    public String getCreatedAtP() {
        return createdAtP;
    }

    public void setCreatedAtP(String createdAtP) {
        this.createdAtP = createdAtP;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSeenDate() {
        return seenDate;
    }

    public void setSeenDate(String seenDate) {
        this.seenDate = seenDate;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}

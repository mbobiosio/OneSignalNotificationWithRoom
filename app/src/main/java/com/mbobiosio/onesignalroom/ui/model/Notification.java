package com.mbobiosio.onesignalroom.ui.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Mbuodile Obiosio on Jul 25,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
@Entity(tableName = "notifications")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String message;
    private Boolean seen;
    private String img_url;
    private String time;

    public Notification(String title, String message, String time, Boolean seen) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.seen = seen;
    }
    @Ignore
    public Notification(String title, String message, String time, Boolean seen, String img_url) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.seen = seen;
        this.img_url = img_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.lawgimenez.wasssup.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lawrence Gimenez on 02/05/2017.
 * Copyright wasssup
 */

public class Message extends RealmObject {

    public static final String KEY_DATE = "date";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USERNAME = "username";

    @PrimaryKey
    private String messageId;
    private String date;
    private String userId;
    private String content;
    private String username;

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }
}

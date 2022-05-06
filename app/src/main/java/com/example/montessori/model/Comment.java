package com.example.montessori.model;

import androidx.annotation.NonNull;

public class Comment {
    public static final String NAME_FIELD = "name";
    public static final String COMMENT_FIELD = "comment";
    public static final String DATE_FIELD = "date";
    public static final String TIMESTAMP_FIELD = "timestamp";


    String name, comment, date;
    Long timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    @NonNull
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", time=" + timestamp +
                '}';
    }
}
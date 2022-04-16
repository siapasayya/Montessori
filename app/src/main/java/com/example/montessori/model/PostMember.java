package com.example.montessori.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostMember implements Parcelable {
    String name, url, postUri, time, uid, type, desc;

    public PostMember() {
    }

    protected PostMember(Parcel in) {
        name = in.readString();
        url = in.readString();
        postUri = in.readString();
        time = in.readString();
        uid = in.readString();
        type = in.readString();
        desc = in.readString();
    }

    public static final Creator<PostMember> CREATOR = new Creator<PostMember>() {
        @Override
        public PostMember createFromParcel(Parcel in) {
            return new PostMember(in);
        }

        @Override
        public PostMember[] newArray(int size) {
            return new PostMember[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(url);
        parcel.writeString(postUri);
        parcel.writeString(time);
        parcel.writeString(uid);
        parcel.writeString(type);
        parcel.writeString(desc);
    }
}
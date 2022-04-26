package com.example.montessori.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.montessori.util.Helper;
import com.google.firebase.firestore.Exclude;

public class PostMember implements Parcelable {
    private String id, name, username, uid, postUri, desc, pem, umur, time, type;
    private boolean approved = false;

    public PostMember() {
    }

    protected PostMember(Parcel in) {
        id = in.readString();
        name = in.readString();
        username = in.readString();
        postUri = in.readString();
        time = in.readString();
        uid = in.readString();
        type = in.readString();
        desc = in.readString();
        pem = in.readString();
        umur = in.readString();
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

    @Exclude
    public boolean isDataFilled() {
        return !Helper.isNullOrBlank(name)
                && !Helper.isNullOrBlank(username)
                && !Helper.isNullOrBlank(uid)
                // Karena tautan gambar belum didapatkan apabila belum diunggah.
                // && !Helper.isNullOrBlank(postUri)
                && !Helper.isNullOrBlank(desc)
                && !Helper.isNullOrBlank(pem)
                && !Helper.isNullOrBlank(umur)
                && !Helper.isNullOrBlank(time)
                && !Helper.isNullOrBlank(type);
    }

    @Exclude
    public String getFullUsername() {
        return !Helper.isNullOrBlank(username) ? username : "Tanpa username";
    }

    @Exclude
    public String getFullName() {
        return !Helper.isNullOrBlank(name) ? name : "Tanpa nama";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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

    public String getPem() {
        return pem;
    }

    public void setPem(String pem) {
        this.pem = pem;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeString(postUri);
        parcel.writeString(time);
        parcel.writeString(uid);
        parcel.writeString(type);
        parcel.writeString(desc);
        parcel.writeString(pem);
        parcel.writeString(umur);
    }
}
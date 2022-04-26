package com.example.montessori.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String UserName, FullName, UserEmail, role;

    public User() {
    }

    protected User(Parcel in) {
        UserName = in.readString();
        FullName = in.readString();
        UserEmail = in.readString();
        role = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(UserName);
        parcel.writeString(FullName);
        parcel.writeString(UserEmail);
        parcel.writeString(role);
    }
}
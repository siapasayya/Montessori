package com.example.montessori.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.montessori.R;
import com.example.montessori.util.Helper;
import com.google.firebase.firestore.Exclude;

public class PostMember implements Parcelable {
    private static final int WORD_LIMIT = 20;
    private static final int CHARACTER_LIMIT = 100;
    private static final String APPROVED = "Approved";
    private static final String WAITING_FOR_APPROVAL = "Waiting for Approval";
    private static final String REJECTED = "Rejected";

    private String id, name, username, uid, postUri, title, desc, pem, umur, time, type;
    private boolean approved = false;
    private boolean checked = false;

    public PostMember() {
    }

    protected PostMember(Parcel in) {
        id = in.readString();
        name = in.readString();
        username = in.readString();
        uid = in.readString();
        postUri = in.readString();
        title = in.readString();
        desc = in.readString();
        pem = in.readString();
        umur = in.readString();
        time = in.readString();
        type = in.readString();
        approved = in.readByte() != 0;
        checked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(uid);
        dest.writeString(postUri);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(pem);
        dest.writeString(umur);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeByte((byte) (approved ? 1 : 0));
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
    public String getPostTitle() {
        return !Helper.isNullOrBlank(title) ? title : "Untitled";
    }

    @Exclude
    public String getTitleName(Context context) {
        return String.format(Helper.getLocale(), context.getString(R.string.name_template), getFullName(), getFullUsername());
    }

    @Exclude
    public String getFullUsername() {
        return !Helper.isNullOrBlank(username) ? username : "No username";
    }

    @Exclude
    public String getFullName() {
        return !Helper.isNullOrBlank(name) ? name : "No name";
    }

    // NOTE: Setelah 20 kata atau 80 huruf, potong deskripsinya.
    @Exclude
    public String getShortDescription() {
        String[] splitDesc = desc.split(" ");
        if (splitDesc.length > WORD_LIMIT) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < WORD_LIMIT; i++) {
                builder.append(splitDesc[i]);
                if (i != WORD_LIMIT - 1) {
                    builder.append(" ");
                }
            }
            builder.append("...");
            return builder.toString();
        } else if (desc.length() > CHARACTER_LIMIT) {
            return desc.substring(0, CHARACTER_LIMIT) + "...";
        } else {
            return desc;
        }
    }

    @Exclude
    public String getCategory(Context context) {
        return String.format(Helper.getLocale(), context.getString(R.string.category_template), pem, umur);
    }

    @Exclude
    public String getStatus(Context context) {
        String status;
        if (checked && approved) {
            status = APPROVED;
        } else if (checked) {
            status = REJECTED;
        } else {
            status = WAITING_FOR_APPROVAL;
        }

        return String.format(Helper.getLocale(), context.getString(R.string.status_template), status);
    }

    @Exclude
    public boolean isNotCheckedOrApproved() {
        return !isChecked() || !isApproved();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
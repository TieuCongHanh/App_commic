package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Chapter implements Parcelable {
    private String _id;
    private String comicId;
    private String chapNumber;
    private List<String> content;

    public Chapter() {
    }

    public Chapter(String _id, String comicId, String chapNumber, List<String> content) {
        this._id = _id;
        this.comicId = comicId;
        this.chapNumber = chapNumber;
        this.content = content;
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

    public String getChapNumber() {
        return chapNumber;
    }

    public void setChapNumber(String chapNumber) {
        this.chapNumber = chapNumber;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
    protected Chapter(Parcel in) {
        _id = in.readString();
        comicId = in.readString();
        chapNumber = in.readString();
        content = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(comicId);
        dest.writeString(chapNumber);
        dest.writeStringList(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}

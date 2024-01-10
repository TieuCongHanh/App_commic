package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Comics implements Parcelable {

    private String _id;
    private String name;
    private String description;
    private String author;
    private String year;
    private boolean love;
    private String coverImage;
    private List<String> images;

    public Comics(String _id, String name, String description, String author, String year,boolean love, String coverImage, List<String> images) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.year = year;
        this.love = love;
        this.coverImage = coverImage;
        this.images = images;
    }

    protected Comics(Parcel in) {
        _id = in.readString();
        name = in.readString();
        description = in.readString();
        author = in.readString();
        year = in.readString();
        love = in.readInt() == 1; // Đọc giá trị boolean từ int
        coverImage = in.readString();
        images = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeString(year);
        dest.writeInt(love ? 1 : 0); // Ghi giá trị boolean dưới dạng int
        dest.writeString(coverImage);
        dest.writeStringList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comics> CREATOR = new Creator<Comics>() {
        @Override
        public Comics createFromParcel(Parcel in) {
            return new Comics(in);
        }

        @Override
        public Comics[] newArray(int size) {
            return new Comics[size];
        }
    };
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isLove() {
        return love;
    }

    public void setLove(boolean love) {
        this.love = love;
    }
}

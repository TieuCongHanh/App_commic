package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("_id")
    private String _id;

    @SerializedName("email")
    private String email;
    @SerializedName("fullname")
    private String fullname;

    public String toString(){
        return " "+_id + ""+username + " "+password;
    }


    public UserData(String username, String password, String _id , String fullname, String email) {
        this.username = username;
        this.password = password;
        this._id = _id;
        this.fullname = fullname;
        this.email = email;
    }

    protected UserData(Parcel in) {
        username = in.readString();
        password = in.readString();
        _id = in.readString();
        fullname = in.readString();
        email = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(_id);
        dest.writeString(fullname);
        dest.writeString(email);
    }
}

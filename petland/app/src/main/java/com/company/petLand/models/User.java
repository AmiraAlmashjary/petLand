package com.company.petLand.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String id;
    public String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
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
}

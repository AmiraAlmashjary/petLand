package com.company.petLand.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    public String id;
    public Account account;
    public String serviceName;
    public String time;
    public String date;
    public double total;
    public String petname;
    public String type;

    public Booking() {
    }

    protected Booking(Parcel in) {
        id = in.readString();
        serviceName = in.readString();
        time = in.readString();
        date = in.readString();
        total = in.readDouble();
        petname = in.readString();
        type = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(serviceName);
        parcel.writeString(time);
        parcel.writeString(date);
        parcel.writeDouble(total);
        parcel.writeString(petname);
        parcel.writeString(type);
    }
}

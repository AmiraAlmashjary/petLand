package com.company.petLand.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public String id;
    public String name;
    public String description;
    public double price;
    public int amount=1;

    public void setId(String id) {
        this.id = id;
    }

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;

    }

    public Product() {
    }


    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        amount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}


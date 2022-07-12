package com.company.petLand.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pet implements Parcelable {
    public String id;
    public String name;
    public String birth;
    public String age;
    public String color;
    public String weight;
    public String species;
    public Pet() {
    }


    protected Pet(Parcel in) {
        id = in.readString();
        name = in.readString();
        birth = in.readString();
        age = in.readString();
        color = in.readString();
        weight = in.readString();
        species = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(birth);
        parcel.writeString(age);
        parcel.writeString(color);
        parcel.writeString(weight);
        parcel.writeString(species);
    }
}

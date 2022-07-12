package com.company.petLand.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
  public   String id,title,description;

    public Article() {
    }

  protected Article(Parcel in) {
    id = in.readString();
    title = in.readString();
    description = in.readString();
  }

  public static final Creator<Article> CREATOR = new Creator<Article>() {
    @Override
    public Article createFromParcel(Parcel in) {
      return new Article(in);
    }

    @Override
    public Article[] newArray(int size) {
      return new Article[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(id);
    parcel.writeString(title);
    parcel.writeString(description);
  }
}

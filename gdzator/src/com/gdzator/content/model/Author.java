package com.gdzator.content.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Author extends Model implements Parcelable {

    public String surname;
    public String name;
    public String patronymic;

    @Override
    public ContentValues toCV() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.patronymic);
        dest.writeValue(this.id);
    }

    private Author(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.patronymic = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static Creator<Author> CREATOR = new Creator<Author>() {
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    @Override
    public String toString() {
        return String.format("%s %s.%s.", this.surname, this.name.substring(0, 1), this.patronymic.substring(0, 1));
    }
}

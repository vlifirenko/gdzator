package com.gdzator.content.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.gdzator.content.provider.Contract;
import com.gdzator.content.provider.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Subject extends Model implements Parcelable {

    public String name;
    public String image;
    public String description;
    public int clazz;

    public Subject() {
    }

    public Subject(long id) {
        this.id = id;
    }

    public Subject(Cursor c) {
        this.id = c.getLong(c.getColumnIndex(Contract.Subject.MODEL_ID));
        this.name = c.getString(c.getColumnIndex(Contract.Subject.NAME));
        this.image = c.getString(c.getColumnIndex(Contract.Subject.IMAGE));
        this.description = c.getString(c.getColumnIndex(Contract.Subject.DESCRIPTION));
        this.clazz = c.getInt(c.getColumnIndex(Contract.Subject.CLAZZ));
    }

    @Override
    public ContentValues toCV() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Subject.MODEL_ID, this.id);
        cv.put(Contract.Subject.NAME, this.name);
        cv.put(Contract.Subject.IMAGE, this.image);
        cv.put(Contract.Subject.DESCRIPTION, this.description);
        cv.put(Contract.Subject.CLAZZ, this.clazz);
        return cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeInt(this.clazz);
        dest.writeValue(this.id);
    }

    private Subject(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.clazz = in.readInt();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static Creator<Subject> CREATOR = new Creator<Subject>() {
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public static void save(final Context context, final List<Subject> list) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.delete(Contract.Subject.TABLE_NAME, Contract.Subject.CLAZZ + " = " + list.get(0).clazz, null);
        for (Subject subject : list) {
            db.insert(Contract.Subject.TABLE_NAME, null, subject.toCV());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Subject> getSubjects(Context context, int clazz) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.query(Contract.Subject.TABLE_NAME, null, Contract.Subject.CLAZZ + " = " + clazz,
                null, null, null, null);
        c.moveToFirst();
        List<Subject> list = new ArrayList<>();
        while (!c.isAfterLast()) {
            list.add(new Subject(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }
}

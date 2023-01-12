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

public class Section extends Model implements Parcelable {

    public String name;
    public Long parent;
    public int clazz;
    public long subject;
    public long book;

    public Section() {
    }

    public Section(long id) {
        this.id = id;
    }

    public Section(Cursor c) {
        this.id = c.getLong(c.getColumnIndex(Contract.Section.MODEL_ID));
        this.name = c.getString(c.getColumnIndex(Contract.Section.NAME));
        this.parent = c.getLong(c.getColumnIndex(Contract.Section.PARENT));
        this.clazz = c.getInt(c.getColumnIndex(Contract.Section.CLAZZ));
        this.subject = c.getLong(c.getColumnIndex(Contract.Section.SUBJECT));
        this.book = c.getLong(c.getColumnIndex(Contract.Section.BOOK));
    }

    @Override
    public ContentValues toCV() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Section.MODEL_ID, this.id);
        cv.put(Contract.Section.NAME, this.name);
        cv.put(Contract.Section.PARENT, this.parent != null ? this.parent : -1);
        cv.put(Contract.Section.CLAZZ, this.clazz);
        cv.put(Contract.Section.SUBJECT, this.subject);
        cv.put(Contract.Section.BOOK, this.book);
        return cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.name);
        dest.writeLong(this.parent != null ? this.parent : -1);
        dest.writeLong(this.getId());
    }

    private Section(Parcel in) {
        this.name = in.readString();
        this.parent = in.readLong();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static Creator<Section> CREATOR = new Creator<Section>() {
        public Section createFromParcel(Parcel source) {
            return new Section(source);
        }

        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public static void save(final Context context, final List<Section> list, final int clazz, final long subject, final long book)
            throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.delete(Contract.Section.TABLE_NAME, Contract.Section.CLAZZ + " = " + clazz + " AND " +
                Contract.Section.SUBJECT + " = " + subject + " AND " + Contract.Section.BOOK + " = " + book, null);
        for (Section section : list) {
            section.clazz = clazz;
            section.subject = subject;
            section.book = book;
            db.insert(Contract.Section.TABLE_NAME, null, section.toCV());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Section> getSections(Context context, int clazz, long subject, long book) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.query(Contract.Section.TABLE_NAME, null, Contract.Section.CLAZZ + " = " + clazz + " AND " +
                        Contract.Section.SUBJECT + " = " + subject + " AND " + Contract.Section.BOOK + " = " + book,
                null, null, null, null);
        c.moveToFirst();
        List<Section> list = new ArrayList<>();
        while (!c.isAfterLast()) {
            list.add(new Section(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }
}

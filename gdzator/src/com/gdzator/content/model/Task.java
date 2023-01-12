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

public class Task extends Model implements Parcelable {

    public Long book;
    public String number;
    public Long section;
    public String url;

    public Task() {
    }

    public Task(long id) {
        this.id = id;
    }

    public Task(Cursor c) {
        this.id = c.getLong(c.getColumnIndex(Contract.Task.MODEL_ID));
        this.book = c.getLong(c.getColumnIndex(Contract.Task.BOOK));
        this.number = c.getString(c.getColumnIndex(Contract.Task.NUMBER));
        this.section = c.getLong(c.getColumnIndex(Contract.Task.SECTION));
        this.url = c.getString(c.getColumnIndex(Contract.Task.TEXT));
    }

    @Override
    public ContentValues toCV() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Task.MODEL_ID, this.id);
        cv.put(Contract.Task.BOOK, this.book);
        cv.put(Contract.Task.NUMBER, this.number);
        cv.put(Contract.Task.SECTION, this.section);
        cv.put(Contract.Task.TEXT, this.url);
        return cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(this.book);
        dest.writeString(this.number);
        dest.writeLong(this.section != null ? this.section : -1);
        dest.writeString(this.url);
        dest.writeValue(this.id);
    }

    private Task(Parcel in) {
        this.book = in.readLong();
        this.number = in.readString();
        this.section = in.readLong();
        this.url = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static Creator<Task> CREATOR = new Creator<Task>() {
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public static void save(final Context context, List<Task> list, Section section, long book)
            throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        if (section == null)
            section = new Section(0);
        db.beginTransaction();
        db.delete(Contract.Task.TABLE_NAME, Contract.Task.BOOK + " = " + book + " AND "
                + Contract.Task.SECTION + " = " + section.getId(), null);
        for (Task task : list) {
            db.insert(Contract.Task.TABLE_NAME, null, task.toCV());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Task> getTasks(Context context, long book, Section section) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        if (section == null)
            section = new Section(0);
        Cursor c = db.query(Contract.Task.TABLE_NAME, null, Contract.Task.BOOK + " = " + book
                        + " AND " + Contract.Task.SECTION + " = " + section.getId(),
                null, null, null, null);
        c.moveToFirst();
        List<Task> list = new ArrayList<>();
        while (!c.isAfterLast()) {
            list.add(new Task(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }
}

package com.gdzator.content.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.gdzator.content.provider.Contract;
import com.gdzator.content.provider.DatabaseHelper;
import com.gdzator.content.rest.BooksResponse;

import java.util.ArrayList;
import java.util.List;

public class Book extends Model implements Parcelable {

    public String name;
    public List<Author> authors = new ArrayList<>();
    private String authorsString;
    public String[] classes;
    public BooksResponse.Image image = new BooksResponse.Image();
    public String description;
    public long subject;
    public int clazz;
    public boolean hasSections = true;

    public Book() {
    }

    public Book(long id) {
        this.id = id;
    }

    public Book(Cursor c) {
        this.id = c.getLong(c.getColumnIndex(Contract.Book.MODEL_ID));
        this.authorsString = c.getString(c.getColumnIndex(Contract.Book.AUTHORS));
        this.image.url = c.getString(c.getColumnIndex(Contract.Book.IMAGE));
        this.name = c.getString(c.getColumnIndex(Contract.Book.NAME));
        this.description = c.getString(c.getColumnIndex(Contract.Book.DESCRIPTION));
        this.hasSections = c.getInt(c.getColumnIndex(Contract.Book.HAS_SECTIONS)) == 1;
        this.subject = c.getLong(c.getColumnIndex(Contract.Book.SUBJECT));
        this.clazz = c.getInt(c.getColumnIndex(Contract.Book.CLAZZ));
    }

    @Override
    public ContentValues toCV() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Book.MODEL_ID, this.id);
        cv.put(Contract.Book.AUTHORS, this.getAuthors());
        cv.put(Contract.Book.IMAGE, this.image != null ? this.image.url : "");
        cv.put(Contract.Book.NAME, this.name);
        cv.put(Contract.Book.DESCRIPTION, this.description);
        cv.put(Contract.Book.HAS_SECTIONS, this.hasSections ? 1 : 0);
        cv.put(Contract.Book.SUBJECT, this.subject);
        cv.put(Contract.Book.CLAZZ, this.clazz);
        return cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.name);
        dest.writeString(this.image != null ? this.image.url : "");
        dest.writeString(this.description);
        dest.writeValue(this.id);
        dest.writeInt(this.hasSections ? 1 : 0);
        dest.writeStringArray(this.classes);
        dest.writeList(this.authors);
    }

    private Book(Parcel in) {
        this.name = in.readString();
        this.image.url = in.readString();
        this.description = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.hasSections = in.readInt() == 1;
        if (classes != null)
            in.readStringArray(classes);
        authors = new ArrayList<>();
        try {
            in.readList(authors, Author.class.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getAuthors() {
        if (authorsString != null)
            return authorsString;
        if (authors.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(authors.get(0));
        for (int i = 1; i < authors.size(); i++)
            sb.append(", ").append(authors.get(i));
        return sb.toString();
    }

    public static void save(final Context context, final List<Book> list, final int clazz, final long subject)
            throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        db.delete(Contract.Book.TABLE_NAME, Contract.Book.CLAZZ + " = " + clazz +
                " AND " + Contract.Book.SUBJECT + " = " + subject, null);
        for (Book book : list) {
            book.subject = subject;
            book.clazz = clazz;
            db.insert(Contract.Book.TABLE_NAME, null, book.toCV());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Book> getBooks(Context context, int clazz, long subject) throws Exception {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor c = db.query(Contract.Book.TABLE_NAME, null, Contract.Book.CLAZZ + " = " + clazz +
                " AND " + Contract.Book.SUBJECT + " = " + subject, null, null, null, null);
        c.moveToFirst();
        List<Book> list = new ArrayList<>();
        while (!c.isAfterLast()) {
            list.add(new Book(c));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }
}

package com.gdzator.content.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "gdzator.db";
    private static final int DATABASE_VERSION = 8;
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.PurchaseData.SQL_CREATE);
        sqLiteDatabase.execSQL(Contract.TryData.SQL_CREATE);
        sqLiteDatabase.execSQL(Contract.Subject.SQL_CREATE);
        sqLiteDatabase.execSQL(Contract.Section.SQL_CREATE);
        sqLiteDatabase.execSQL(Contract.Book.SQL_CREATE);
        sqLiteDatabase.execSQL(Contract.Task.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL(Contract.PurchaseData.SQL_DELETE_ENTRIES);
            db.execSQL(Contract.TryData.SQL_DELETE_ENTRIES);
            db.execSQL(Contract.Subject.SQL_DELETE_ENTRIES);
            db.execSQL(Contract.Section.SQL_DELETE_ENTRIES);
            db.execSQL(Contract.Book.SQL_DELETE_ENTRIES);
            db.execSQL(Contract.Task.SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
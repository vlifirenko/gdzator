package com.gdzator.content.provider;

import android.provider.BaseColumns;

public class Contract {
    public static abstract class PurchaseData implements BaseColumns {
        public static final String TABLE_NAME = "purchase_data";
        public static final String COLUMN_NAME_CODE = "class";
        public static final String COLUMN_NAME_KEY = "key";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_CODE + " TEXT not null unique," +
                        COLUMN_NAME_KEY + " TEXT" +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class TryData implements BaseColumns {
        public static final String TABLE_NAME = "try_data";
        public static final String COLUMN_NAME_TRY_CODE = "try_code";

        public static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_TRY_CODE + " TEXT" +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Subject {
        public static final String TABLE_NAME = "subjects";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String IMAGE = "image";
        public static final String DESCRIPTION = "description";
        public static final String CLAZZ = "clazz";
        public static final String MODEL_ID = "model_id";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + "  INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                IMAGE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CLAZZ + " INTEGER, " +
                MODEL_ID + " INTEGER, " +
                "UNIQUE (" + ID + ") ON CONFLICT REPLACE)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Section {
        public static final String TABLE_NAME = "sections";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String PARENT = "parent";
        public static final String MODEL_ID = "model_id";
        public static final String CLAZZ = "clazz";
        public static final String SUBJECT = "subject";
        public static final String BOOK = "book";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + "  INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                PARENT + " INTEGER, " +
                MODEL_ID + " INTEGER, " +
                CLAZZ + " INTEGER, " +
                SUBJECT + " INTEGER, " +
                BOOK + " INTEGER, " +
                "UNIQUE (" + ID + ") ON CONFLICT REPLACE)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Book {
        public static final String TABLE_NAME = "books";

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String AUTHORS = "authors";
        public static final String CLAZZES = "clazzes";
        public static final String IMAGE = "image";
        public static final String DESCRIPTION = "description";
        public static final String MODEL_ID = "model_id";
        public static final String CLAZZ = "clazz";
        public static final String SUBJECT = "subject";
        public static final String HAS_SECTIONS = "book";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + "  INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                AUTHORS + " TEXT, " +
                CLAZZES + " TEXT, " +
                IMAGE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                MODEL_ID + " INTEGER, " +
                HAS_SECTIONS + " INTEGER, " +
                CLAZZ + " INTEGER, " +
                SUBJECT + " INTEGER, " +
                "UNIQUE (" + ID + ") ON CONFLICT REPLACE)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Task {
        public static final String TABLE_NAME = "tasks";

        public static final String ID = BaseColumns._ID;
        public static final String BOOK = "book";
        public static final String NUMBER = "number";
        public static final String SECTION = "section";
        public static final String TEXT = "url";
        public static final String MODEL_ID = "model_id";
        public static final String CLAZZ = "clazz";
        public static final String SUBJECT = "subject";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + "  INTEGER PRIMARY KEY, " +
                BOOK + " INTEGER, " +
                NUMBER + " TEXT, " +
                SECTION + " INTEGER, " +
                TEXT + " TEXT, " +
                MODEL_ID + " INTEGER, " +
                CLAZZ + " INTEGER, " +
                SUBJECT + " INTEGER, " +
                "UNIQUE (" + ID + ") ON CONFLICT REPLACE)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}

package com.my.android_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.my.android_app.constants.DatabaseConstants;

public class DatabaseSQLHelper extends SQLiteOpenHelper {

    public DatabaseSQLHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(DatabaseConstants.CREATE_ENTRIES_CLIENTS);
            sqLiteDatabase.execSQL(DatabaseConstants.CREATE_ENTRIES_DATE);
            sqLiteDatabase.execSQL(DatabaseConstants.CREATE_ENTRIES_BOOKING);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseConstants.DELETE_ENTRIES3);
        sqLiteDatabase.execSQL(DatabaseConstants.DELETE_ENTRIES);
        sqLiteDatabase.execSQL(DatabaseConstants.DELETE_ENTRIES2);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

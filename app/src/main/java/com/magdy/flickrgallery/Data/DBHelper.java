package com.magdy.flickrgallery.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.magdy.flickrgallery.Data.Contract.Image;


class DBHelper extends SQLiteOpenHelper {

    private static final String NAME = "Images.db";
    private static final int VERSION = 1;

    DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String builder = "CREATE TABLE " + Image.TABLE_NAME + " ("
                + Image.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Image.COLUMN_IMAGE_LINK + " TEXT NOT NULL) ";
        sqLiteDatabase.execSQL(builder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(" DROP TABLE IF EXISTS " + Image.TABLE_NAME);
        onCreate(db);
    }
}

package com.magdy.flickrgallery.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class ImageProvider extends ContentProvider {
    private static final int Image = 100;
    private DBHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Contract.AUTHORITY, Contract.PATH_IMAGES, Image);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        returnCursor = db.query(
                Contract.Image.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        Context context = getContext();
        if (context != null) {
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        db.insert(
                Contract.Image.TABLE_NAME,
                null,
                values
        );
        returnUri = Contract.Image.URI;

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }

        rowsDeleted = db.delete(
                Contract.Image.TABLE_NAME,
                selection,
                selectionArgs
        );

        if (rowsDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                db.insert(
                        Contract.Image.TABLE_NAME,
                        null,
                        value
                );
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnCount;
    }
}

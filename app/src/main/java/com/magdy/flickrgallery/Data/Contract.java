package com.magdy.flickrgallery.Data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.ImmutableList;

public class Contract {

    public static final String ACTION_DATA_UPDATED = "com.magdy.flickrgallery.ACTION_DATA_UPDATED";
    static final String AUTHORITY = "com.magdy.flickrgallery";
    static final String PATH_IMAGES = "images";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private Contract() {
    }

    @SuppressWarnings("unused")
    public static final class Image implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_IMAGES).build();
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_IMAGE_LINK = "image_link";
        public static final int POSITION_ID = 0;
        public static final int POSITION_IMAGE_LINK = 1;
        public static final ImmutableList<String> IMAGE_COLUMNS = ImmutableList.of(
                COLUMN_ID,
                COLUMN_IMAGE_LINK
        );
        static final String TABLE_NAME = "images";

        public static Uri makeUriForImage(String imageid) {
            return URI.buildUpon().appendPath(imageid).build();
        }

        static String getImageFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }


    }
}

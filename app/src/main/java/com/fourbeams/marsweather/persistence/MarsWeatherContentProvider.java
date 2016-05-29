package com.fourbeams.marsweather.persistence;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class MarsWeatherContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.fourbeams.marsweather.persistence.MarsWeatherContentProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/temperature";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> TEMPERATURE_PROJECTION_MAP;

    //fields
    public static final String TERRESTRIAL_DATE = "terrestrial_date";
    public static final String MIN_TEMP_C = "min_temp_c";
    public static final String MAX_TEMP_C = "max_temp_c";

    static final int TEMPERATURE = 1;
    //static final int TEMPERATURE_TERRESTRIAL_DATE = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "temperature", TEMPERATURE);
        //uriMatcher.addURI(PROVIDER_NAME, "temperature/#", TEMPERATURE_TERRESTRIAL_DATE);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "MarsWeatherBD";
    static final String TABLE_NAME = "temperature";
    static final int DATABASE_VERSION = 3;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (terrestrial_date STRING, " +
                    " min_temp_c DOUBLE NOT NULL, " +
                    " max_temp_c DOUBLE NOT NULL); ";

    //static final String CREATE_DB_TABLE = "DROP TABLE " + TABLE_NAME;
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                qb.setProjectionMap(TEMPERATURE_PROJECTION_MAP);
                break;
            //case TEMPERATURE_TERRESTRIAL_DATE:
            //    qb.appendWhere(TERRESTRIAL_DATE + "=" + uri.getPathSegments().get(1));
            //    break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case TEMPERATURE:
                return "vnd.android.cursor.dir/vnd.com.fourbeams.marsweather.persistence.temperature";

            /**
             * Get a particular student
             */
            //case TEMPERATURE_TERRESTRIAL_DATE:
            //    return "vnd.android.cursor.item/vnd.com.fourbeams.marsweather.persistence.temperature";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        /**
         * Add a new row/??
         */
        long rowID = db.insert(TABLE_NAME, "", contentValues);

        /**
         * If row is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a row " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                count = db.delete(TABLE_NAME, s, strings);
                break;

            //case TEMPERATURE_TERRESTRIAL_DATE:
            //    String ter_Date = uri.getPathSegments().get(1);
            //    count = db.delete(TABLE_NAME, TERRESTRIAL_DATE +  " = " + ter_Date +
            //            (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
            //    break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
       getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                count = db.update(TABLE_NAME, contentValues, s, strings);
                break;

            //case TEMPERATURE_TERRESTRIAL_DATE:
            //    count = db.update(TABLE_NAME, contentValues, TERRESTRIAL_DATE + " = " + uri.getPathSegments().get(1) +
            //            (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
            //    break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

package com.fourbeams.marsweather.persistence;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fourbeams.marsweather.domain.MyWidgetProvider;

@SuppressWarnings("ConstantConditions")
public class MarsWeatherContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.fourbeams.marsweather.persistence.MarsWeatherContentProvider";
    private static final String URL = "content://" + PROVIDER_NAME + "/temperature";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //fields in DB
    private static final String _ID = "_id";
    public static final String TERRESTRIAL_DATE = "terrestrial_date";
    public static final String MIN_TEMP_C = "min_temp_c";
    public static final String MAX_TEMP_C = "max_temp_c";
    public static final String SEASON = "season";

    //projection
    public static final String[] TEMPERATURE_PROJECTION = new String[]{
        MarsWeatherContentProvider._ID,
        MarsWeatherContentProvider.TERRESTRIAL_DATE,
        MarsWeatherContentProvider.MIN_TEMP_C,
        MarsWeatherContentProvider.MAX_TEMP_C,
        MarsWeatherContentProvider.SEASON,
    };

    //data URIs
    private static final int TEMPERATURE = 1;
    private static final int TEMPERATURE_LAST_DATE = 2;
    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "temperature", TEMPERATURE);
        uriMatcher.addURI(PROVIDER_NAME, "temperature/last_date", TEMPERATURE_LAST_DATE);
    }

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "MarsWeatherBD";
    private static final String TABLE_NAME = "temperature";
    private static final int DATABASE_VERSION = 8;
    private static final String CREATE_DB_TABLE =
        " CREATE TABLE " + TABLE_NAME +
        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        " terrestrial_date STRING, " +
        " min_temp_c DOUBLE NOT NULL, " +
        " max_temp_c DOUBLE NOT NULL, " +
        " season STRING NOT NULL); ";


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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TEMPERATURE_LAST_DATE:
                c = qb.query(
                    db,                                                 // database,
                    projection,                                         // projection
                    " _id = (SELECT MAX(_id) FROM " + TABLE_NAME + " )",// selection
                    null,                                               // selectionArgs
                    null,                                               // group by
                    null,                                               // having
                    sortOrder                                           // sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        // register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                return "vnd.android.cursor.dir/vnd.com.fourbeams.marsweather.persistence.temperature";
            case TEMPERATURE_LAST_DATE:
                return "vnd.android.cursor.item/vnd.com.fourbeams.marsweather.persistence.temperature";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        long rowID = db.insert(TABLE_NAME, "", contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            // notify widgets and observers
            notifyChange(_uri);
            return _uri;
        }
        throw new SQLException("Failed to add a row " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        int count;
        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                count = db.delete(TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // notify widgets and observers
        notifyChange(uri);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count;
        switch (uriMatcher.match(uri)){
            case TEMPERATURE:
                count = db.update(TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        // notify widgets and observers
        notifyChange(uri);
        return count;
    }

    private void notifyChange(Uri uri){
    //Notify registered observers that a row was updated
    getContext().getContentResolver().notifyChange(uri, null);
    //Notify widget by sending broadcast
    Intent intent = new Intent(getContext(), MyWidgetProvider.class);
    intent.setAction("com.fourbeams.marsweather.intent.action.DATA_CHANGED_IN_PROVIDER");
    getContext().sendBroadcast(intent);
    }
}

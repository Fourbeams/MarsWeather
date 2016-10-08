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

/**
 * Provider stores Mars temperature data.
 * <br/>Consists of SQLite database {@value #DATABASE_NAME} with table {@value #TABLE_NAME}
 * <br/>including such fields, as: {@value #_ID}, {@value #TERRESTRIAL_DATE},
 * {@value #MIN_TEMP_C}, {@value #MAX_TEMP_C}, {@value #SEASON}
 * <p/>
 * Provider could be accessed through the following URIs:
 * <br/> content://{@value #PROVIDER_NAME}...
 * <br/> <i>/temperature/last_date</i> - provides last date only, sortOrder and projection parameters allowed
 * <br/> <i>/temperature</i> - provides data according with the given projection, selection, selectionArgs, and sortOrder param
 * <p/>
 * Method {@link #query} holds side effect of registering to watch a content URI for changes.
 * <br/> {@link #delete}, {@link #update}, {@link #insert} methods notifies observers when content changed and
 * <br/> sends intent (com.fourbeams.marsweather.intent.action.DATA_CHANGED_IN_PROVIDER)
 * <br/> to let the widget be updated as well.
 */
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
    private static final int DATABASE_VERSION = 9;
    private static final String CREATE_DB_TABLE =
        " CREATE TABLE " + TABLE_NAME +
        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        " terrestrial_date STRING, " +
        " min_temp_c DOUBLE NOT NULL, " +
        " max_temp_c DOUBLE NOT NULL, " +
        " season STRING NOT NULL); ";
    private static final String INSERT_INITIAL_VALUES =
            " INSERT INTO " + TABLE_NAME +
                    " (" + TERRESTRIAL_DATE + ", " +
                    MIN_TEMP_C + ", " +
                    MAX_TEMP_C + ", " +
                    SEASON + ") " +
                    " VALUES " +
                    " ('2016-10-05', '-70', '1', 'Month 8'); ";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
            db.execSQL(INSERT_INITIAL_VALUES);
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

    private void notifyChange(Uri uri){
    //Notify registered observers that a row was updated
    getContext().getContentResolver().notifyChange(uri, null);
    //Notify widget by sending broadcast
    Intent intent = new Intent(getContext(), MyWidgetProvider.class);
    intent.setAction("com.fourbeams.marsweather.intent.action.DATA_CHANGED_IN_PROVIDER");
    getContext().sendBroadcast(intent);
    }
}

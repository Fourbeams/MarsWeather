package com.fourbeams.marsweather.presentation;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.domain.ServiceHelper;
import com.fourbeams.marsweather.domain.MyWidgetProvider;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int TEMPERATURE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ServiceHelper.setContext(getApplicationContext());
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this);
        getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, // observed URI
                true,
                new MarsWeatherContentProviderObserver(new Handler()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            }
        });
        new MyWidgetProvider();
    }

    private class MarsWeatherContentProviderObserver extends ContentObserver {
        public MarsWeatherContentProviderObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            getLoaderManager().getLoader(TEMPERATURE_LOADER).forceLoad();
        }
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId){
            case (TEMPERATURE_LOADER):
                return new CursorLoader(
                        this,                                               // context
                        MarsWeatherContentProvider.CONTENT_URI,             // dataUri,
                        MarsWeatherContentProvider.TEMPERATURE_PROJECTION,  // projection
                        null,                                               // selection
                        null,                                               // selectionArgs
                        null                                                // sort ordering
                );
            default: return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        Cursor cursor = (Cursor) o;
        if (cursor.moveToLast()){
            final String _id = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider._ID));
            final String ter_date = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
            final String min_temp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MIN_TEMP_C));
            final String max_temp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MAX_TEMP_C));
            final TextView terrestrial_date = (TextView)findViewById(R.id.terrestrial_date);
            terrestrial_date.post(new Runnable() {
                public void run() {
                    terrestrial_date.setText(ter_date);
                }
            });
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader loader) {}

}

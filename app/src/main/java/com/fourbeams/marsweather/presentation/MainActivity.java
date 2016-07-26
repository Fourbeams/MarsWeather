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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.domain.ServiceHelper;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int TEMPERATURE_LOADER = 0;
    private MarsWeatherContentProviderObserver marsWeatherContentProviderObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton refreshButton = (ImageButton) findViewById(R.id.widget_button_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceHelper.getInstance(getApplicationContext()).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            }
        });
        marsWeatherContentProviderObserver = new MarsWeatherContentProviderObserver(new Handler());
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this).forceLoad();
    }

    @Override
    protected void onPause (){
        super.onPause();
        getContentResolver().unregisterContentObserver(marsWeatherContentProviderObserver);
        getLoaderManager().destroyLoader(TEMPERATURE_LOADER);
    }

    @Override
    protected void onResume (){
        super.onResume();
        getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, true, marsWeatherContentProviderObserver);
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this).forceLoad();
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
            final String terrestrialDate = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
            final String minTemp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MIN_TEMP_C));
            final String maxTemp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MAX_TEMP_C));
            final TextView terrestrialDateView = (TextView)findViewById(R.id.terrestrial_date_activity);
            final TextView minTempView = (TextView)findViewById(R.id.min_temp_c_activity);
            final TextView maxTempView = (TextView)findViewById(R.id.max_temp_c_activity);
            terrestrialDateView.post(new Runnable() {
                public void run() {
                    terrestrialDateView.setText(terrestrialDate);
                }
            });
            minTempView.post(new Runnable() {
                public void run() {
                    minTempView.setText(minTemp);
                }
            });
            maxTempView.post(new Runnable() {
                public void run() {
                    maxTempView.setText(maxTemp);
                }
            });
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader loader) {}

}

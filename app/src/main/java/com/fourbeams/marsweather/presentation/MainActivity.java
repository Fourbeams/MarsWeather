package com.fourbeams.marsweather.presentation;

import android.app.LoaderManager;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.domain.Processor;
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
                displayLoadingIndicator();
                ServiceHelper.getInstance(getApplicationContext()).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            }
        });
        marsWeatherContentProviderObserver = new MarsWeatherContentProviderObserver(new Handler());

        //video playing setup
/*        final VideoView videoView = (VideoView) findViewById(R.id.video_view);
        try {
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nasa_mars_rotation));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });*/
        // registering receiver for incoming intents, that processor complete work with no new data inserted in to content provider
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Processor.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER));

        displayLoadingIndicator();
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this).forceLoad();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideLoadingIndicator();
        }
    };

    @Override
    protected void onPause (){
        super.onPause();
        getContentResolver().unregisterContentObserver(marsWeatherContentProviderObserver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        getLoaderManager().destroyLoader(TEMPERATURE_LOADER);
    }

    @Override
    protected void onResume (){
        super.onResume();
        getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, true, marsWeatherContentProviderObserver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Processor.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER));
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
            displayLoadingIndicator();
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
        hideLoadingIndicator();
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    public void hideLoadingIndicator(){
        findViewById(R.id.progress_spinner).setVisibility(View.GONE);
        findViewById(R.id.widget_button_refresh).setVisibility(View.VISIBLE);
    }

    private void displayLoadingIndicator(){
        findViewById(R.id.progress_spinner).setVisibility(View.VISIBLE);
        findViewById(R.id.widget_button_refresh).setVisibility(View.GONE);
    }

}

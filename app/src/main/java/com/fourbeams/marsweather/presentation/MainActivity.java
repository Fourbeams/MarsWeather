package com.fourbeams.marsweather.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.domain.DateAndTimeUtil;
import com.fourbeams.marsweather.domain.Processor;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.domain.ServiceHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int TEMPERATURE_LOADER = 0;
    private MarsWeatherContentProviderObserver marsWeatherContentProviderObserver;
    private DateAndTimeUtil dateAndTimeUtil;
    private BroadcastReceiver sysTimeChangeBroadcastReceiver;

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

        ImageButton infoButton = (ImageButton) findViewById(R.id.activity_button_info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playShowViewAnimation();
            }
        });
        LinearLayout infoView = (LinearLayout) findViewById(R.id.info_layout);
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playHideViewAnimation(view);
            }
        });

        marsWeatherContentProviderObserver = new MarsWeatherContentProviderObserver(new Handler());
        dateAndTimeUtil = new DateAndTimeUtil();
        TextView sol = (TextView) findViewById(R.id.activityMarsSol);
        sol.setText(dateAndTimeUtil.getMarsSol() + "  ");

        final TextView marsTimeView = (TextView) findViewById(R.id.marsTime);
        marsTimeView.post(new Runnable() {
            public void run() {
                marsTimeView.setText(dateAndTimeUtil.getMarsTime());
            }
        });
        sysTimeChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    final TextView marsTimeView = (TextView) findViewById(R.id.marsTime);
                    marsTimeView.post(new Runnable() {
                        public void run() {
                            marsTimeView.setText(dateAndTimeUtil.getMarsTime());
                        }
                    });
                }
            }
        };

        registerReceiver(sysTimeChangeBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
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
        unregisterReceiver(sysTimeChangeBroadcastReceiver);
    }

    @Override
    protected void onResume (){
        super.onResume();
        getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, true, marsWeatherContentProviderObserver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Processor.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER));
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this).forceLoad();
        registerReceiver(sysTimeChangeBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
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
            final String season = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.SEASON));
            final TextView terrestrialDateView = (TextView)findViewById(R.id.terrestrial_date_activity);
            final TextView minTempView = (TextView)findViewById(R.id.min_temp_c_activity);
            final TextView maxTempView = (TextView)findViewById(R.id.max_temp_c_activity);
            final TextView seasonView = (TextView)findViewById(R.id.marsMonth);
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
            seasonView.post(new Runnable() {
                public void run() {
                    seasonView.setText(season);
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

    private void playShowViewAnimation(){
        // previously invisible view
        View myView = findViewById(R.id.info_layout);
        // get the center for the clipping circle
        //int cx = (myView.getLeft() + myView.getRight()) / 2;
        //int cy = (myView.getTop() + myView.getBottom()) / 2;
        ImageButton infoButtonView = (ImageButton) findViewById(R.id.activity_button_info);
        int cx = infoButtonView.getRight();
        int cy = infoButtonView.getBottom();
        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void playHideViewAnimation (View view){
        // previously visible view
        final View myView = view;
        // get the center for the clipping circle
        ImageButton infoButtonView = (ImageButton) findViewById(R.id.activity_button_info);
        int cx = infoButtonView.getRight();
        int cy = infoButtonView.getBottom();
        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth();
        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });
        // start the animation
        anim.start();
    }

}

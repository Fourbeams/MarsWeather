package com.fourbeams.marsweather.presentation;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.domain.ServiceHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final int TEMPERATURE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(TEMPERATURE_LOADER, null, this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ServiceHelper.setContext(getApplicationContext());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
                //System.out.print("Updating Mars weather data from server ...");
                Snackbar.make(view, "Updating Mars weather data from server ...", Snackbar.LENGTH_SHORT)
                        .show();

            }
        });
    }

    private static final String[] TEMPERATURE_PROJECTION = new String[]{
            MarsWeatherContentProvider.TERRESTRIAL_DATE,
            MarsWeatherContentProvider.MIN_TEMP_C,
            MarsWeatherContentProvider.MAX_TEMP_C,
    };

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId){
            case (TEMPERATURE_LOADER):
                return new CursorLoader(
                        this,                                               // context
                        MarsWeatherContentProvider.CONTENT_URI,             // dataUri,
                        TEMPERATURE_PROJECTION,                             // projection
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
            do{
                final String ter_date = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
                final String min_temp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MIN_TEMP_C));
                final String max_temp = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.MAX_TEMP_C));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                        TextView terrestrial_date = (TextView)findViewById(R.id.terrestrial_date);
                        TextView min_temp_c = (TextView)findViewById(R.id.min_temp_c);
                        TextView max_temp_c = (TextView)findViewById(R.id.max_temp_c);
                        terrestrial_date.setText(ter_date);
                        min_temp_c.setText(min_temp);
                        max_temp_c.setText(max_temp);
                    }
                });
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader loader) {}
}

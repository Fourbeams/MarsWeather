package com.fourbeams.marsweather.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import retrofit2.Call;

import java.io.IOException;

import static com.fourbeams.marsweather.domain.RESTClient.MarsWeatherService.retrofit;

public class Processor {

    private Context context;

    public Processor(Context context) {
        this.context = context;
    }

    public void startGetProcessor(String task){
        if (task.equals (ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER.toString())){
            try {
                getNewWeatherData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getNewWeatherData() throws IOException {
        RESTClient.MarsWeatherService marsWeatherService = retrofit.create(RESTClient.MarsWeatherService.class);
        // getting data from JSON, type of returned object defined in generic
        final Call<POJO.ReportResponse> call = marsWeatherService.getJSON();
        POJO.ReportResponse report = call.execute().body();
        String terrestrialDate = report.getReport().getTerrestrialDate(); // date from server

        // obtaining date from provider
        String latestDateInContentProvider = "";
        String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
        Cursor cursor = context.getContentResolver().query(Uri.parse(URL), MarsWeatherContentProvider.TEMPERATURE_PROJECTION, null, null, null);
        if (cursor.moveToLast()){
            latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
        }
        cursor.close();

        // if date from server != date in provider, then insert new row
        if (!terrestrialDate.equals(latestDateInContentProvider)){
            double minTemp = report.getReport().getMinTemp();
            double maxTemp = report.getReport().getMaxTemp();
            // save result to contentProvider through contentResolver
            ContentValues contentValues = new ContentValues();
            contentValues.put(MarsWeatherContentProvider.TERRESTRIAL_DATE, terrestrialDate);
            contentValues.put(MarsWeatherContentProvider.MIN_TEMP_C, minTemp);
            contentValues.put(MarsWeatherContentProvider.MAX_TEMP_C, maxTemp);
            context.getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);
        }
/*      ContentValues contentValues = new ContentValues();
        contentValues.put(MarsWeatherContentProvider.TERRESTRIAL_DATE, "0000-00-00");
        contentValues.put(MarsWeatherContentProvider.MIN_TEMP_C, -11);
        contentValues.put(MarsWeatherContentProvider.MAX_TEMP_C, 11);
        context.getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);*/
    }
}

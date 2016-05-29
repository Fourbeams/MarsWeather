package com.fourbeams.marsweather.domain;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import retrofit2.Call;

import java.io.IOException;

import static com.fourbeams.marsweather.domain.RESTClient.MarsWeatherService.retrofit;


public class Processor {

    Context context;

    public Processor(Context context) {
        this.context = context;
    }

    public void startGetProcessor(String task){
        if (task.equals (ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER.toString())){
                try {
                    getNewWeatherDataFromServer();
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO don't forget to remove
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "error reading data from REST", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }
    }

    private void getNewWeatherDataFromServer() throws IOException {
        RESTClient.MarsWeatherService marsWeatherService = retrofit.create(RESTClient.MarsWeatherService.class);
        // getting data from dematerialized JSON stored in Call object, type of returned object defined by generics
        final Call<POJO.ReportResponse> call = marsWeatherService.getJSON();
        POJO.ReportResponse report = call.execute().body();
        String terrestrialDate = report.getReport().getTerrestrialDate();
        double minTemp = report.getReport().getMinTemp();
        double maxTemp = report.getReport().getMaxTemp();
        // save result to contentProvider through contentResolver
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarsWeatherContentProvider.TERRESTRIAL_DATE, terrestrialDate);
        contentValues.put(MarsWeatherContentProvider.MIN_TEMP_C, minTemp);
        contentValues.put(MarsWeatherContentProvider.MAX_TEMP_C, maxTemp);
        // loader notified at ContentProvider's Update/Insert method by invoking
        // getContext().getContentResolver().notifyChange(_uri, null)
        context.getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);
    }
}

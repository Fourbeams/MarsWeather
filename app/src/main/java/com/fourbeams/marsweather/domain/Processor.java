package com.fourbeams.marsweather.domain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;

import static com.fourbeams.marsweather.domain.RESTClient.MarsWeatherService.retrofit;

public class Processor {

    private Context context;
    public static final String PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER
            = "com.fourbeams.marsweather.intent.action.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER";

    //TODO remove / in use for testing
    //private static int counter;

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

        //TODO remove / in use for testing
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        counter++;
        terrestrialDate = counter + "";*/
        //TODO remove / in use for testing

        // obtaining last date from content provider
        String latestDateInContentProvider = "";
        String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
        Cursor cursor = context.getContentResolver().query(Uri.parse(URL), MarsWeatherContentProvider.TEMPERATURE_PROJECTION, null, null, null);
        if (cursor.moveToLast()){
            latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
        }
        cursor.close();

        // if last date from server != last date in content provider, then insert new row
        if (!terrestrialDate.equals(latestDateInContentProvider)){
            double minTemp = report.getReport().getMinTemp();
            double maxTemp = report.getReport().getMaxTemp();
            // saving result to contentProvider through contentResolver
            ContentValues contentValues = new ContentValues();
            contentValues.put(MarsWeatherContentProvider.TERRESTRIAL_DATE, terrestrialDate);
            contentValues.put(MarsWeatherContentProvider.MIN_TEMP_C, minTemp);
            contentValues.put(MarsWeatherContentProvider.MAX_TEMP_C, maxTemp);
            context.getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);
        }

        // if date from server == date in provider,  do not insert new row, but hide send intent to stop loading indicator
        sendMessage();
    }

    // sending message that there is no new data at server and loading indicator should stops
    private void sendMessage() {
        Intent intent = new Intent(PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER);
        // using LocalBroadcastManager to update an activity
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        // due to  widget works in separate process we can't listen any broadcasts
        // through registered receivers through LocalBroadcastManager. So we have to use context.sendBroadcast
        context.sendBroadcast(intent);
    }
}

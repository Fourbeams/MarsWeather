package com.fourbeams.marsweather.domain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.fourbeams.marsweather.domain.REST.MarsWeatherService;
import com.fourbeams.marsweather.domain.REST.RESTServiceGenerator;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import retrofit2.Call;

import java.io.IOException;

public class Processor {

    private Context context;
    public static final String PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER
            = "com.fourbeams.marsweather.intent.action.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER";

    Processor(Context context) {
        this.context = context;
    }

    /**
     * Starts processor to obtain data from server through REST Service.
     * </br>If there is new data at server, then data is saved at content provider.
     * </br>If there is NO new data, intent PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER is being sent.
     * </br>In case API is not available the exception is catched and it considered
     * as API is reachable, but no new data available.
     */
    void startGetProcessor(String task){
        if (task.equals (ServiceFacade.task.GET_NEW_WEATHER_DATA_FROM_SERVER.toString())){
            try {
                getNewWeatherData();
            } catch (IOException e) {
                sendAPINotAvailableMessage();
                e.printStackTrace();
            }
        }
    }

    private void getNewWeatherData() throws IOException {
        // Create a REST adapter which points Mars Weather API endpoint
        MarsWeatherService marsWeatherService = RESTServiceGenerator.createService(MarsWeatherService.class);
        // Fetch a list of the weather parameters
        final Call<POJO> call = marsWeatherService.getJSON();
        // Execute - this throws exception if API not available or it reached the timeout values
        POJO report = call.execute().body();
        int arrayLenth = report.getSoles().size();
        POJO.Sole lastElement = report.getSoles().get(0);
        String terrestrialDate = lastElement.getTerrestrialDate(); // date from server
        // obtaining last date from content provider
        String latestDateInContentProvider = "";
        String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
        Cursor cursor = context.getContentResolver()
                .query(Uri.parse(URL), MarsWeatherContentProvider.TEMPERATURE_PROJECTION, null, null, null);
        if (cursor != null && cursor.moveToLast()){
            latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
        }
        if (cursor != null) cursor.close();
        // if last date from server != last date in content provider, then insert new row
        if (!terrestrialDate.equals(latestDateInContentProvider)){
            double minTemp = Double.valueOf(lastElement.getMinTemp());
            double maxTemp = Double.valueOf(lastElement.getMaxTemp());
            String season = lastElement.getSeason();
            // saving result to contentProvider through contentResolver
            ContentValues contentValues = new ContentValues();
            contentValues.put(MarsWeatherContentProvider.TERRESTRIAL_DATE, terrestrialDate);
            contentValues.put(MarsWeatherContentProvider.MIN_TEMP_C, minTemp);
            contentValues.put(MarsWeatherContentProvider.MAX_TEMP_C, maxTemp);
            contentValues.put(MarsWeatherContentProvider.SEASON, season);
            context.getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);
        // if date from server == date in provider, do not insert new row, but loading indicator should be hidden
        } else sendNoNewDataMessage();
    }

    private void sendAPINotAvailableMessage(){
        // in case API is not available we just consider it as API is reachable, but no new data available
        // sending message that there is no new data at server and loading indicator should stops
        sendNoNewDataMessage();
    }

    private void sendNoNewDataMessage() {
        Intent intent = new Intent(PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER);
        // using LocalBroadcastManager to update an activity
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        // due to widget works in separate process we can't listen any broadcasts
        // through registered receivers through LocalBroadcastManager. So we have to use context.sendBroadcast
        context.sendBroadcast(intent);
    }

}

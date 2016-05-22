package com.fourbeams.marsweather.domain;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

import static com.fourbeams.marsweather.domain.RESTClient.MarsWeatherService.retrofit;

public class Processor {

    public Processor() {}

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
                            Toast.makeText(App.getContext(), "error reading data from REST", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }
    }

    private void getNewWeatherDataFromServer() throws IOException {
        RESTClient.MarsWeatherService marsWeatherService = retrofit.create(RESTClient.MarsWeatherService.class);
        Call<List<RESTClient.MarsWeatherTemperatureData>> call = marsWeatherService.getJSON();
        final List<RESTClient.MarsWeatherTemperatureData> result = call.execute().body();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getContext(), result.toString(), Toast.LENGTH_LONG).show();
            }
        });
        // save result to contentProvider through contentResolver
        ContentValues contentValues = new ContentValues();
        contentValues.put("new", result.toString());
        App.getContext().getContentResolver().insert(MarsWeatherContentProvider.CONTENT_URI, contentValues);

        // restart Loader
    }

}

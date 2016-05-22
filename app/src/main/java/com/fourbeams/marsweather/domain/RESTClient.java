package com.fourbeams.marsweather.domain;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.List;

public class RESTClient {

    public RESTClient() throws IOException {}

    public class MarsWeatherTemperatureData {

        String terrestrial_date;
        int min_temp_c;
        int max_temp_c;

        @Override
        public String toString() {
            return terrestrial_date + " (min:" + min_temp_c + ")" + " (max:" + max_temp_c + ")";
        }
    }

    interface MarsWeatherService {
        @GET("v1/latest/?format=json")
        Call<List<MarsWeatherTemperatureData>> getJSON(
                //@Path("owner") String owner,
                //@Path("repo") String repo
                );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://marsweather.ingenology.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

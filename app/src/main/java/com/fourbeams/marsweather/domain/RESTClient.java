package com.fourbeams.marsweather.domain;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.io.IOException;

public class RESTClient {

    public RESTClient() throws IOException {}

    public interface MarsWeatherService {
        @GET("v1/latest/?format=json")
        Call<POJO.ReportResponse> getJSON();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://marsweather.ingenology.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

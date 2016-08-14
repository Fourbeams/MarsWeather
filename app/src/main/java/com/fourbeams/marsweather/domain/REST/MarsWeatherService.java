package com.fourbeams.marsweather.domain.REST;

import com.fourbeams.marsweather.domain.POJO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MarsWeatherService {
    @GET("v1/latest/?format=json")
    Call<POJO.ReportResponse> getJSON();
}

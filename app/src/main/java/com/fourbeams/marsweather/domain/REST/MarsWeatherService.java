package com.fourbeams.marsweather.domain.REST;

import com.fourbeams.marsweather.domain.POJO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MarsWeatherService {
    @GET("rems/wp-content/plugins/marsweather-widget/api.php")
    Call<POJO> getJSON();
}

package com.fourbeams.marsweather.domain.REST;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Generates REST service by setting http client, timeouts and converter factory to the given service class.
 * <br/> Setts OkHttpClient as http client.
 * <br/> Sets read from server timeout to {@value #READ_TIMEOUT_SECONDS}
 * <br/> Sets connect to server timeout to {@value #CONNECT_TIMEOUT_SECONDS}
 * <br/> Sets GsonConverterFactory as converter factory
 */
public class RESTServiceGenerator {

    private static final String API_BASE_URL = "http://marsweather.ingenology.com/";
    private static final int CONNECT_TIMEOUT_SECONDS = 15;
    private static final int READ_TIMEOUT_SECONDS = 15;

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build())
                .build();
        return retrofit.create(serviceClass);
    }

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
}

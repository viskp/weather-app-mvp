package com.vishal.weatherapp.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vishal.weatherapp.utils.Utils.DEFAULT_CONNECT_TIMEOUT_IN_MS;
import static com.vishal.weatherapp.utils.Utils.DEFAULT_READ_TIMEOUT_IN_MS;
import static com.vishal.weatherapp.utils.Utils.DEFAULT_WRITE_TIMEOUT_IN_MS;
import static com.vishal.weatherapp.utils.Utils.END_POINT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * This class holds the configuration for calling weather APIS, end point and other timeouts are
 * defined here. And common header can be defined as a interceptor in
 * {@link WeatherAPIClient#getClient()}
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
public class WeatherAPIClient {

    private static Retrofit retrofit = null;

    /**
     * Binds and returns the retorfit client object. We declares global headers, logger and other
     * required interceptors.
     *
     * @return wrapped configuration object of type {@link Retrofit}
     */
    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_MS, MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_MS, MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT_IN_MS, MILLISECONDS);
        oktHttpClient.addInterceptor(logging);
        oktHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(END_POINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(oktHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}
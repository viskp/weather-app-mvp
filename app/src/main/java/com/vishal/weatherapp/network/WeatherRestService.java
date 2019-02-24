package com.vishal.weatherapp.network;


import com.vishal.weatherapp.pojo.TemperatureResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.vishal.weatherapp.utils.Utils.API_KEY;


public interface WeatherRestService {

    @GET("v1/forecast.json?" + API_KEY)
    Observable<TemperatureResponse> getWeatherInfo(@Query("q") String cityName,
                                                   @Query("days") String days);

}

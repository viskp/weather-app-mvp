package com.vishal.weatherapp.network;


import com.vishal.weatherapp.pojo.TemperatureResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.vishal.weatherapp.utils.Utils.API_KEY;

/**
 * This class is responsible for holding all APIs need to be invoked by the application.
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
public interface WeatherRestService {
    /**
     * Returns weather info response which will be observed by the callee.
     *
     * @param cityName name of the city to be searched
     * @param days     no. of days for forecast data
     * @return {@link Observable<TemperatureResponse>}
     */
    @GET("v1/forecast.json?" + API_KEY)
    Observable<TemperatureResponse> getWeatherInfo(@Query("q") String cityName,
                                                   @Query("days") String days);

}

package com.vishal.weatherapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.vishal.weatherapp.network.WeatherAPIClient;
import com.vishal.weatherapp.network.WeatherRestService;
import com.vishal.weatherapp.pojo.TemperatureResponse;
import com.vishal.weatherapp.utils.Utils;

import io.reactivex.Observable;

/**
 * Model implementation for {@link WeatherActivity} that holds the data required by the
 * {@link WeatherPresenterImpl}.
 * Here we can define get the datas from different sources like, SharedPref, LocalDB, Network call
 * and so on.
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
public class WeatherModelImpl implements WeatherContract.Model {
    private WeatherRestService weatherRestService;
    private Context context;

    public WeatherModelImpl(Context context) {
        this.context = context;
        weatherRestService = WeatherAPIClient.getClient().create(WeatherRestService.class);
    }

    /**
     * Initiates the API call for getting the weather info.
     *
     * @param cityName name of the city
     * @return {@link Observable<TemperatureResponse>}
     */
    @Override
    public Observable<TemperatureResponse> initiateWeatherInfoCall(String cityName) {
        return weatherRestService.getWeatherInfo(cityName, Utils.FORECAST_DAYS);
        //Other stuffs like caching the data to local DB or shared prefs.
    }

    /**
     * Fetches the correct icon for weather condition based on condition code give by the API
     *
     * @param code of the condition type
     * @return icon to be used for weather condition
     */
    @Override
    public Drawable getConditionIcon(int code) {
        int icon = R.mipmap.clear;
        switch (code) {
            case 1000:
                icon = R.mipmap.sun;
                break;
            case 1003:
                icon = R.mipmap.clear;
                break;
            case 1006:
                icon = R.mipmap.clouds;
                break;
            case 1180:
            case 1183:
            case 1186:
            case 1189:
            case 1192:
            case 1195:
            case 1198:
            case 1201:
                icon = R.mipmap.rain;
                break;
            case 1117:
                icon = R.mipmap.storm;
                break;
        }
        return ContextCompat.getDrawable(context, icon);
    }
}

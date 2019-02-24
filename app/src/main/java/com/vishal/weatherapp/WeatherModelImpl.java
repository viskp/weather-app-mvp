package com.vishal.weatherapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.vishal.weatherapp.network.WeatherAPIClient;
import com.vishal.weatherapp.network.WeatherRestService;
import com.vishal.weatherapp.pojo.TemperatureResponse;
import com.vishal.weatherapp.utils.Utils;

import io.reactivex.Observable;

public class WeatherModelImpl implements WeatherContract.Model {
    private WeatherRestService weatherRestService;
    private Context context;

    public WeatherModelImpl(Context context) {
        this.context = context;
        weatherRestService = WeatherAPIClient.getClient().create(WeatherRestService.class);
    }

    @Override
    public Observable<TemperatureResponse> initiateWeatherInfoCall(String cityName) {
        return weatherRestService.getWeatherInfo(cityName, Utils.FORECAST_DAYS);
        //Other stuffs like caching the data to local DB or shared prefs.
    }

    @Override
    public Drawable getConditionIcon(int code) {
        int icon = R.mipmap.clear;
        switch (code) {
            case 1000:
                icon = R.mipmap.sun;
            case 1003:
                icon = R.mipmap.clear;
            case 1006:
                icon = R.mipmap.clouds;
            case 1180:
            case 1183:
            case 1186:
            case 1189:
            case 1192:
            case 1195:
            case 1198:
            case 1201:
                icon = R.mipmap.rain;
            case 1117:
                icon = R.mipmap.storm;
        }
        return ContextCompat.getDrawable(context, icon);
    }
}

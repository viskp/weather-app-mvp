package com.vishal.weatherapp;

import android.graphics.drawable.Drawable;

import com.vishal.weatherapp.pojo.ForecastDataModel;
import com.vishal.weatherapp.pojo.TemperatureResponse;

import java.util.List;

import io.reactivex.Observable;

public interface WeatherContract {
    interface View {
        void onInitView();

        void handleLoaderView(boolean show);

        void handleWeatherView(boolean show);

        void handleErrorView(boolean show);

        void setCityCurrentTemperature(String cityName, String temperature);

        void showErrorMessage(String message);

        void showForeCastData(List<ForecastDataModel> forecastData);
    }

    interface Presenter {
        void initView();

        void getWeatherData();

        void destroyView();
    }

    interface Model {
        Observable<TemperatureResponse> initiateWeatherInfoCall(String cityName);

        Drawable getConditionIcon(int code);
    }
}

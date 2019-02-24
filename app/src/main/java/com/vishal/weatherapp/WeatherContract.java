package com.vishal.weatherapp;

import android.graphics.drawable.Drawable;

import com.vishal.weatherapp.pojo.ForecastDataModel;
import com.vishal.weatherapp.pojo.TemperatureResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract class for {@link WeatherActivity} that holds all the required method used by the View,
 * Model and Presenter.
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
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

        void getWeatherData(String cityName);

        void destroyView();
    }

    interface Model {
        Observable<TemperatureResponse> initiateWeatherInfoCall(String cityName);

        Drawable getConditionIcon(int code);
    }
}

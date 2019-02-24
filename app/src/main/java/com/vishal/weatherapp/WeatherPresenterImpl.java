package com.vishal.weatherapp;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.vishal.weatherapp.pojo.Condition;
import com.vishal.weatherapp.pojo.Current;
import com.vishal.weatherapp.pojo.Day;
import com.vishal.weatherapp.pojo.Error;
import com.vishal.weatherapp.pojo.Forecast;
import com.vishal.weatherapp.pojo.ForecastDataModel;
import com.vishal.weatherapp.pojo.Forecastday;
import com.vishal.weatherapp.pojo.Location;
import com.vishal.weatherapp.pojo.TemperatureResponse;
import com.vishal.weatherapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class WeatherPresenterImpl implements WeatherContract.Presenter {
    private WeatherContract.View weatherView;
    private WeatherContract.Model weatherModel;
    private Scheduler processThread;
    private Scheduler mainThread;
    private CompositeDisposable compositeDisposable;
    private char degreeChar = (char) 0x00B0;

    public WeatherPresenterImpl(WeatherContract.View weatherView, WeatherContract.Model weatherModel
            , Scheduler processThread, Scheduler mainThread) {
        this.weatherView = weatherView;
        this.weatherModel = weatherModel;
        this.processThread = processThread;
        this.mainThread = mainThread;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void initView() {
        weatherView.onInitView();
        weatherView.handleWeatherView(false);
        weatherView.handleErrorView(false);
    }


    @Override
    public void getWeatherData() {
        weatherView.handleLoaderView(true);
        weatherView.handleWeatherView(false);
        weatherView.handleErrorView(false);
        compositeDisposable.add(weatherModel.
                initiateWeatherInfoCall("Bengaluru")
                .subscribeOn(processThread)
                .observeOn(mainThread)
                .subscribeWith(new DisposableObserver<TemperatureResponse>() {
                    @Override
                    public void onNext(TemperatureResponse temperatureResponse) {
                        handleTemperatureResponse(temperatureResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        weatherView.handleErrorView(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void handleTemperatureResponse(TemperatureResponse temperatureResponse) {
        if (null != temperatureResponse) {
            Error error = temperatureResponse.getError();
            if (null != error) {
                weatherView.showErrorMessage(error.getMessage());
                showErrorView();
            } else {
                Location location = temperatureResponse.getLocation();
                Current current = temperatureResponse.getCurrent();
                if (null != location && null != current) {
                    String cityName = location.getName();
                    Double temperature = current.getTempC();
                    if (!TextUtils.isEmpty(cityName) && null != temperature) {
                        weatherView.handleLoaderView(false);
                        weatherView.handleErrorView(false);
                        weatherView.handleWeatherView(true);
                        weatherView.setCityCurrentTemperature(cityName, String.valueOf(temperature));
                    } else {
                        showErrorView();
                    }
                } else {
                    showErrorView();
                }
                Forecast forecast = temperatureResponse.getForecast();
                if (null != forecast) {
                    List<ForecastDataModel> forecastData = new ArrayList<>();
                    List<Forecastday> allForeCast = forecast.getForecastday();
                    for (Forecastday forecastday : allForeCast) {
                        String day = "";
                        String condition = "";
                        Drawable conditionIcon = null;
                        String minMaxTemp = "";
                        Day dayDetails = forecastday.getDay();
                        if (null != dayDetails) {
                            Double minTemp = dayDetails.getMintempC();
                            Double maxTemp = dayDetails.getMaxtempC();
                            day = Utils.getFormattedDate(forecastday.getDate());
                            Condition conditions = dayDetails.getCondition();
                            if (null != conditions) {
                                condition = conditions.getText();
                                conditionIcon = weatherModel.getConditionIcon(conditions.getCode());
                            }
                            if (null != minTemp && null != maxTemp) {
                                minMaxTemp = minTemp + degreeChar + "/" + maxTemp + degreeChar;
                            }
                            forecastData.add(new ForecastDataModel(day, condition, conditionIcon,
                                    minMaxTemp));
                        }
                    }
                }
            }
        } else {
            weatherView.handleErrorView(true);
        }
    }

    public void showErrorView() {
        weatherView.handleLoaderView(false);
        weatherView.handleErrorView(true);
    }

    @Override
    public void destroyView() {
        compositeDisposable.dispose();
    }
}

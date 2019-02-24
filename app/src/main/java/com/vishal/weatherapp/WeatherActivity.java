package com.vishal.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vishal.weatherapp.pojo.ForecastDataModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This activity will be the first activity to be shown to user. Initially a leader view
 * {@link WeatherActivity#loaderMainViewRL} will be shown to user unless weather data is fetched.
 * For now Bangalore is added the default city to be shown and with the help of view
 * {@link WeatherActivity#searchCityET} user can search city for checking the weather and forecast.
 * ================================================================================================
 * This class follows MVP design pattern, where Model, View and Presenter's contract is commonly
 * defined in {@link WeatherContract} class. And implementations are defined in
 * {@link WeatherModelImpl} and {@link WeatherPresenterImpl} classes.
 */
//TODO Adding feature of getting current location and showing the weather info.
public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {
    private WeatherContract.Presenter weatherPresenter;
    private WeatherContract.Model weatherModel;
    private RelativeLayout loaderMainViewRL;
    private ImageView loaderIV;
    private LinearLayout weatherForecastViewLL;
    private TextView temperatureTV;
    private TextView cityNameTV;
    private RecyclerView forecastRV;
    private RelativeLayout errorViewRL;
    private WeatherActivity activity;
    private EditText searchCityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.weather_activity);

        weatherModel = new WeatherModelImpl(activity);
        weatherPresenter = new WeatherPresenterImpl(activity, weatherModel, Schedulers.io()
                , AndroidSchedulers.mainThread());
        weatherPresenter.initView();
        weatherPresenter.getWeatherData(getTextToBeSearched());
    }

    /**
     * Callback give from {@link WeatherPresenterImpl} for initializing the views and there
     * listeners if they have any.
     */
    @Override
    public void onInitView() {
        loaderMainViewRL = findViewById(R.id.loader_main_view);
        loaderIV = findViewById(R.id.loader);
        weatherForecastViewLL = findViewById(R.id.weather_forecast_view);
        temperatureTV = findViewById(R.id.temperature);
        cityNameTV = findViewById(R.id.city_name);
        forecastRV = findViewById(R.id.forecast);
        errorViewRL = findViewById(R.id.retry_main_view);
        searchCityET = findViewById(R.id.search_city);
        errorViewRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherPresenter.getWeatherData(getTextToBeSearched());
            }
        });
        searchCityET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchCityET.getRight() - searchCityET.
                            getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        weatherPresenter.getWeatherData(getTextToBeSearched());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Handles the visibility of loader view.
     *
     * @param show show the view if true else hide it.
     */
    @Override
    public void handleLoaderView(boolean show) {
        if (show) {
            Animation rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            rotation.setRepeatMode(Animation.RESTART);
            loaderIV.startAnimation(rotation);
            loaderMainViewRL.setVisibility(View.VISIBLE);
        } else {
            loaderMainViewRL.setVisibility(View.GONE);
        }
    }

    /**
     * Handles the visibility of weather view.
     *
     * @param show show the view if true else hide it.
     */
    @Override
    public void handleWeatherView(boolean show) {
        weatherForecastViewLL.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Handles the visibility of error view.
     *
     * @param show show the view if true else hide it.
     */
    @Override
    public void handleErrorView(boolean show) {
        errorViewRL.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Sets the city name and temperature of that city.
     *
     * @param cityName    name of the city.
     * @param temperature of the city.
     */
    @Override
    public void setCityCurrentTemperature(String cityName, String temperature) {
        cityNameTV.setText(cityName);
        temperatureTV.setText(temperature);
    }

    /**
     * A common method for showing any message in Toast format.
     *
     * @param message to be shown
     */
    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays the forecast information based on searched city. It also applies a slide up
     * animation for all forecast rows.
     *
     * @param forecastData
     */
    @Override
    public void showForeCastData(List<ForecastDataModel> forecastData) {
        forecastRV.setLayoutManager(new LinearLayoutManager(this));
        forecastRV.setAdapter(new ForecastAdapter(forecastData));
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(activity, R.anim.aslide_up_anim);
        forecastRV.setLayoutAnimation(controller);
        forecastRV.getAdapter().notifyDataSetChanged();
        forecastRV.scheduleLayoutAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        weatherPresenter.destroyView();
    }

    /**
     * @return text value of {@link WeatherActivity#searchCityET}
     */
    private String getTextToBeSearched() {
        return searchCityET.getText().toString();
    }
}

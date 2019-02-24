package com.vishal.weatherapp.pojo;

import android.graphics.drawable.Drawable;

public class ForecastDataModel {
    private String day;
    private String condition;
    private Drawable conditionIcon;
    private String minMaxTemp;

    public ForecastDataModel(String day, String condition, Drawable conditionIcon, String minMaxTemp) {
        this.day = day;
        this.condition = condition;
        this.conditionIcon = conditionIcon;
        this.minMaxTemp = minMaxTemp;
    }

    public String getDay() {
        return day;
    }

    public String getCondition() {
        return condition;
    }

    public Drawable getIcon() {
        return conditionIcon;
    }

    public String getMinMaxTemp() {
        return minMaxTemp;
    }
}

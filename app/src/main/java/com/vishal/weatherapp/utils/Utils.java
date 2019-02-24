package com.vishal.weatherapp.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static final long DEFAULT_CONNECT_TIMEOUT_IN_MS = 90000;
    public static final long DEFAULT_WRITE_TIMEOUT_IN_MS = 90000;
    public static final long DEFAULT_READ_TIMEOUT_IN_MS = 90000;
    public static final String END_POINT = "http://api.apixu.com/";
    public static final String API_KEY = "key=727b31d5c027441f8e4102934192202";
    public static final String FORECAST_DAYS = "7";
    public static char degreeChar = (char) 0x00B0;

    public static String getFormattedDate(String inputDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(inputDateString);

            Calendar inputDate = Calendar.getInstance();
            inputDate.setTime(date);

            Calendar now = Calendar.getInstance();

            if (now.get(Calendar.DATE) == inputDate.get(Calendar.DATE)) {
                return "Today";
            } else if (inputDate.get(Calendar.DATE) - now.get(Calendar.DATE) == 1) {
                return "Tomorrow";
            } else {
                return DateFormat.format("dd MMM yyyy", inputDate).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String addDegreeSymbol(Double temp) {
        return String.valueOf(Math.round(temp)) + degreeChar;
    }
}

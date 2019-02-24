package com.vishal.weatherapp.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a utility class that can be access from anywhere in the application.It contains the
 * commonly used utility methods.
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
public class Utils {
    public static final long DEFAULT_CONNECT_TIMEOUT_IN_MS = 90000;
    public static final long DEFAULT_WRITE_TIMEOUT_IN_MS = 90000;
    public static final long DEFAULT_READ_TIMEOUT_IN_MS = 90000;
    public static final String END_POINT = "http://api.apixu.com/";
    public static final String API_KEY = "key=727b31d5c027441f8e4102934192202";
    public static final String FORECAST_DAYS = "7";
    private static final char DEGREE_CHAR = (char) 0x00B0;
    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String FORECAST_VIEW_DATE_FORMAT = "dd MMM yyyy";

    /**
     * Calculates the display date from a give date inputDateString, uses the
     * FORECAST_VIEW_DATE_FORMAT for dates other that today and tomorrow.
     *
     * @param inputDateString date to be formatted
     * @return formatted date in FORECAST_VIEW_DATE_FORMAT
     */
    public static String getFormattedDate(String inputDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(INPUT_DATE_FORMAT);
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
                return DateFormat.format(FORECAST_VIEW_DATE_FORMAT, inputDate).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Appends a degree symbol to give double value.
     *
     * @param temp vale to be postfixes with degree
     * @return formatted text with degree
     */
    public static String addDegreeSymbol(Double temp) {
        return String.valueOf(Math.round(temp)) + DEGREE_CHAR;
    }
}

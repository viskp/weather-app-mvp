package com.vishal.weatherapp.utils;

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
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String FORECAST_VIEW_DATE_FORMAT = "dd MMM yyyy";

    /**
     * Appends a degree symbol to give double value.
     *
     * @param temp vale to be postfixes with degree
     * @return formatted text with degree
     */
    public static String addDegreeSymbol(Double temp) {
        if (null == temp) {
            return "-" + DEGREE_CHAR;
        }
        return String.valueOf(Math.round(temp)) + DEGREE_CHAR;
    }
}

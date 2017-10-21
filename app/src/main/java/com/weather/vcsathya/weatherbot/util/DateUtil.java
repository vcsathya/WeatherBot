package com.weather.vcsathya.weatherbot.util;


import java.text.DateFormat;
import java.util.Date;

public class DateUtil {
    public String getCurrentDate() {
        return DateFormat.getDateInstance().format(new Date());
    }
}

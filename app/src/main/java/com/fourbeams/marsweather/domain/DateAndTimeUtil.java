package com.fourbeams.marsweather.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeUtil {

    private final static DateAndTimeUtil INSTANCE = new DateAndTimeUtil();

    private DateAndTimeUtil(){}

    public static DateAndTimeUtil getInstance(){
        return INSTANCE;
    }

    public String getMarsSol(){
        long marsSol = (long) currentMarsSolCalculating();
        return Long.toString(marsSol);
    }

    public String getMarsTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("general time zones "));
        double marsSolDate = currentMarsSolCalculating();
        double marsTimeHours = (marsSolDate * 24) % 24;
        long marsTimeMillis = (long) (marsTimeHours * 3600 * 1000);
        return dateFormat.format(new Date(marsTimeMillis));
    }

    private double currentMarsSolCalculating(){
        Date date = new Date();
        double currentTerrestrialDate = date.getTime() / 1000;
        //returning current Mars sol
        double currentMarsSol = (((currentTerrestrialDate + 36) / 88775.244147) + 34127.2954262);
        return currentMarsSol;
    }

}

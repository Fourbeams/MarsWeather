package com.fourbeams.marsweather.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeUtil {

    public DateAndTimeUtil(){}

    private double marsSolDateCalculating(){
        Date date = new Date();
        double currentData = date.getTime()/1000;
        double marsSolDate = (((currentData+36)/88775.244147)+34127.2954262);
        return marsSolDate;
    }

    public String getMarsSol(){
        long marsSol = (long) marsSolDateCalculating();
        return Long.toString(marsSol);
    }

    public String getMarsTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("general time zones "));
        double marsSolDate = marsSolDateCalculating();
        double marsTimeHours = (marsSolDate * 24) % 24;
        long marsTimeMillis = (long) (marsTimeHours * 3600 * 1000);
        return dateFormat.format(new Date(marsTimeMillis));
    }



}

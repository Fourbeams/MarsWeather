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

    /**
     * Converts Earth current date provided in milliseconds to Mars sol
     * @param date date object to be converted to sol
     * @return Mars sol in String format
     */
    public String calculateMarsSol(Date date){
        long marsSol = (long) dateConverterToMarsSol(date);
        return Long.toString(marsSol);
    }

    /**
     * Converts Earth current date provided in milliseconds to Mars time
     * @param date date object to be converted to Mars time
     * @return Mars time in String format HH:mm
     */
    public String timeConverterToMarsTime(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("general time zones "));
        double marsSolDate = dateConverterToMarsSol(date);
        double marsTimeHours = (marsSolDate * 24) % 24;
        long marsTimeMillis = (long) (marsTimeHours * 3600 * 1000);
        return dateFormat.format(new Date(marsTimeMillis));
    }

    private double dateConverterToMarsSol(Date date){
        double currentTerrestrialDate = date.getTime() / 1000;
        //returning current Mars sol
        double currentMarsSol = (((currentTerrestrialDate + 36) / 88775.244147) + 34127.2954262);
        return currentMarsSol;
    }

}

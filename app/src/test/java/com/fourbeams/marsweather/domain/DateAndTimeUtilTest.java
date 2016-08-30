package com.fourbeams.marsweather.domain;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.Assert.*;

public class DateAndTimeUtilTest {

    private DateAndTimeUtil dateAndTimeUtil = DateAndTimeUtil.getInstance();
    private static Date dateMock;

    @BeforeClass
    public static void setUp() {
        dateMock = Mockito.mock(Date.class);
        final long MOCKED_TIME = 1472476078944L;
        Mockito.when(dateMock.getTime()).thenReturn(MOCKED_TIME);
    }

    @Test
    public void dateConverterToMarsSolShouldReturnCorrectString() {
        String marsSolString = dateAndTimeUtil.calculateMarsSol(dateMock);
        String CORRECT_MARS_SOL_STRING = "50713";
        assertEquals(marsSolString, CORRECT_MARS_SOL_STRING);
    }

    @Test
    public void timeConverterToMarsTimeShouldReturnCorrectString() {
        String marsSolString = dateAndTimeUtil.timeConverterToMarsTime(dateMock);
        String CORRECT_MARS_TIME_STRING = "20:35";
        assertEquals(marsSolString, CORRECT_MARS_TIME_STRING);
    }

}
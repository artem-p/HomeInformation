package ru.artempugachev.homeinformation;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CoordinateTest {
    @Test
    public void toUrlParam() throws Exception {
        // todo make test pass

        Coordinate coordinate = new Coordinate("60", "30");
        String expectedUrlParam = "60, 30";
        assertEquals(expectedUrlParam, coordinate.toUrlPath());

    }
}

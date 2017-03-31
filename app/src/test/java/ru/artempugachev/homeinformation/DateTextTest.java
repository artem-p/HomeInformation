package ru.artempugachev.homeinformation;

import org.junit.Test;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class DateTextTest {

    @Test
    public void formatRu() throws Exception {
        DateText dateText = new DateText();

        Calendar calendar = new GregorianCalendar(2017, 2, 31);
        String formatted = dateText.print(calendar, new Locale("ru"));
        assertEquals("31 марта 2017 г.", formatted);

    }

    @Test
    public void formatEnUs() throws Exception {
        DateText dateText = new DateText();

        Calendar calendar = new GregorianCalendar(2017, 2, 31);
        String formatted = dateText.print(calendar, new Locale("en_US"));
        assertEquals("March 31, 2017", formatted);

    }
}

package ru.artempugachev.homeinformation;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Date text view
 */

public class DateText {

    /**
     * print formatted date
     * */

    public String print() {
        return print(Calendar.getInstance(), Locale.getDefault());
    }

    public String print(Calendar calendar, Locale locale) {
        String formatted;
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
        formatted = df.format(calendar.getTime());
        return formatted;
    }
}

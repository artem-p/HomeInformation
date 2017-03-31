package ru.artempugachev.homeinformation;

import java.text.DateFormat;
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
        return print(new Date(), Locale.getDefault());
    }

    public String print(Date date, Locale locale) {
        String formatted;
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        formatted = df.format(date);
        return formatted;
    }
}

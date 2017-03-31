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
        return print(new Date());
    }

    public String print(Date date) {
        String formatted;
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        formatted = df.format(date);
        return formatted;
    }
}

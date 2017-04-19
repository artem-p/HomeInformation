package ru.artempugachev.homeinformation;

import android.content.Context;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

/**
 * Get data from calendar
 */

public class CalendarModule {
    public CalendarModule() {
    }

    public String getEvents(Context context) {
        CalendarProvider calendarProvider = new CalendarProvider(context);
        List<Calendar> calendars = calendarProvider.getCalendars().getList();

        if (!calendars.isEmpty()) {
            Calendar calendar = calendars.get(0);
            List<Event> events = calendarProvider.getEvents(calendar.id).getList();

            if (!events.isEmpty()) {
                String event = events.get(0).title;
                return event;
            }
        }
        return null;
    }
}

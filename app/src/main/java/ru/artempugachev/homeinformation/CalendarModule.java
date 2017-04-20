package ru.artempugachev.homeinformation;

import android.content.Context;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.android.calendar.Instance;

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
            List<Instance> instances = getTodayInstances(calendarProvider);
            String events = "";
            if (!instances.isEmpty()) {
                Set<Instance> sortedInstances = getSortedInstances(instances);
                for (Instance instance : sortedInstances) {
                    Event event = calendarProvider.getEvent(instance.eventId);
                    String eventTitle = event.title;
                    events += eventTitle + "\n";
                }
                return events;
            }
        }
        return null;
    }

    /**
     * Sort instances by time begin
     * */
    private Set<Instance> getSortedInstances(List<Instance> instances) {
        Set<Instance> sortedInstances = new TreeSet<Instance>(new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                return o1.begin > o2.begin ? 1 : -1;
            }
        });

        sortedInstances.addAll(instances);

        return sortedInstances;
    }

    /**
     * get beg of today in millis
     * */
    private long getBegOfToday() {
        java.util.Calendar begCalendar = java.util.Calendar.getInstance();
        begCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        begCalendar.set(java.util.Calendar.MINUTE, 0);
        begCalendar.set(java.util.Calendar.SECOND, 0);
        return begCalendar.getTimeInMillis();
    }


    /**
     * get end of today in millis
     * */
    private long getEndOfToday() {
        java.util.Calendar endCalendar = java.util.Calendar.getInstance();
        endCalendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(java.util.Calendar.MINUTE, 59);
        endCalendar.set(java.util.Calendar.SECOND, 59);
        return endCalendar.getTimeInMillis();
    }


    /**
     * Get all instances of events for today
     * */
    private List<Instance> getTodayInstances(CalendarProvider calendarProvider) {
        long beg = getBegOfToday();
        long end = getEndOfToday();
        return calendarProvider.getInstances(beg, end).getList();
    }
}

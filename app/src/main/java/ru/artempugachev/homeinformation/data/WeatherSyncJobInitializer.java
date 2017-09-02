package ru.artempugachev.homeinformation.data;

import android.content.Context;
import com.firebase.jobdispatcher.*;

/**
 * Initialize weather sync job service
 */
public class WeatherSyncJobInitializer {
    private final static String WEATHER_SYNC_TAG = "weather-sync";
    private final static int SYNC_INTERVAL_SECONDS = 600;
    private final static int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private boolean initialized = false;

    /**
     * Schedule weather sync job service
     * */
    synchronized
    public void scheduleWeatherSyncJobService(Context context) {
        if (initialized) return;
        GooglePlayDriver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job weatherSyncJob = dispatcher.newJobBuilder()
                .setService(WeatherSyncJobService.class)
                .setTag(WEATHER_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(weatherSyncJob);
        initialized = true;
    }

}
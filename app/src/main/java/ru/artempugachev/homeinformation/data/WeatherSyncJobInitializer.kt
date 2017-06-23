package ru.artempugachev.homeinformation.data

import android.content.Context
import com.firebase.jobdispatcher.*

/**
 * Initialize weather sync job service
 */

private val WEATHER_SYNC_TAG = "weather-sync"
private val SYNC_INTERVAL_SECONDS = 600
private val SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3


class WeatherSyncJobInitializer {
    private var initialized: Boolean = false
    /**
     * Schedule weather sync job service
     * */
    @Synchronized fun scheduleWeatherSyncJobService(context: Context) {
        if (initialized) return
        val driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)
        val weatherSyncJob = dispatcher.newJobBuilder()
                .setService(WeatherSyncJobService::class.java)
                .setTag(WEATHER_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build()

        dispatcher.schedule(weatherSyncJob)
        initialized = true
    }

}
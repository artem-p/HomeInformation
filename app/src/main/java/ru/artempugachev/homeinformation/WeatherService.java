package ru.artempugachev.homeinformation;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Background service to fetching weather data
 */

public class WeatherService extends IntentService {

    public WeatherService() {
        super(WeatherService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}

package ru.artempugachev.homeinformation.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;

import ru.artempugachev.homeinformation.BuildConfig;
import ru.artempugachev.homeinformation.weather.Coordinate;
import ru.artempugachev.homeinformation.data.model.Weather;

/**
 * Service for weather sync
 * It is subclass of {@link IntentService} so it will be on separate thread
 * Get weather from weather provider and put in to database via content provider
 */

public class WeatherSyncService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeatherSyncService(String name) {
        super(name);
    }

    public WeatherSyncService() {
        super("WeatherSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sync();
    }

    void sync() {
        syncWeather(WeatherSyncService.this);
    }

    synchronized
    private static void syncWeather(Context context) {
        DarkSkyProvider darkSkyProvider = new DarkSkyProvider(BuildConfig.DARK_SKY_API_KEY);

        // todo fetch for real coordinates
        Weather.WeatherData weatherData = null;
        try {
            weatherData = darkSkyProvider.fetchWeatherData(new Coordinate("59.93", "30.29"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (weatherData != null) {
            val dataProvider = DataProvider(context)
            dataProvider.deleteData()
            val rowsInserted = dataProvider.writeWeather(weatherData)
        }
    }
}




    fun startWeatherSyncNow(context: Context) {
        val syncIntent = Intent(context, WeatherSyncService::class.java)
        context.startService(syncIntent)
    }


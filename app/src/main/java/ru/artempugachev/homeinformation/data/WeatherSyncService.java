package ru.artempugachev.homeinformation.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.artempugachev.homeinformation.BuildConfig;
import ru.artempugachev.homeinformation.data.api.DarkSkyApiClient;
import ru.artempugachev.homeinformation.data.api.DarkSkyApiInterface;
import ru.artempugachev.homeinformation.data.model.DarkSkyResponse;
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
    private static void syncWeather(final Context context) {
        DarkSkyApiInterface darkSkyApiInterface = DarkSkyApiClient.getClient().create(DarkSkyApiInterface.class);


        // todo fetch for real coordinates
        Call<DarkSkyResponse> darkSkyCall = darkSkyApiInterface.getCurrentWeatherAndForecast(59.93, 30.29);

        darkSkyCall.enqueue(new Callback<DarkSkyResponse>() {
            @Override
            public void onResponse(Call<DarkSkyResponse> call, Response<DarkSkyResponse> response) {
                if (response.isSuccessful()) {
                    Weather currentWeather = response.body().getCurrentWeather();
                    DataProvider dataProvider = new DataProvider(context);
                    dataProvider.deleteData();

                    List<Weather> weatherList = new ArrayList<Weather>();
                    weatherList.add(currentWeather);
                    dataProvider.writeWeather(weatherList);
                }
            }

            @Override
            public void onFailure(Call<DarkSkyResponse> call, Throwable throwable) {
                // todo show toast in main activity
                // todo standard approach via interface
            }
        });
    }

    public static void startWeatherSyncNow(Context context) {
        Intent syncIntent = new Intent(context, WeatherSyncService.class);
        context.startService(syncIntent);
    }
}






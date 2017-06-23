package ru.artempugachev.myweather.data

import android.app.IntentService
import android.content.Context
import android.content.Intent
import ru.artempugachev.myweather.BuildConfig
import ru.artempugachev.myweather.weather.Coordinate
import ru.artempugachev.myweather.weather.DarkSkyProvider

/**
 * Service for weather sync
 * It is subclass of {@link IntentService} so it will be on separate thread
 * Get weather from weather provider and put in to database via content provider
 */

class WeatherSyncService : IntentService("WeatherSyncService") {
    override fun onHandleIntent(intent: Intent?) {
        sync()
    }


    fun sync() {
        syncWeather(this@WeatherSyncService)
    }
}

@Synchronized
fun syncWeather(context: Context) {
    val darkSkyProvider = DarkSkyProvider(BuildConfig.DARK_SKY_API_KEY)
    val weatherData = darkSkyProvider.fetchWeatherData(Coordinate("59.93", "30.29"))

    if (!weatherData.isEmpty()) {
        val dataProvider = DataProvider(context)
        dataProvider.deleteData()
        dataProvider.writeWeather(weatherData)
    }
}

fun startWeatherSyncNow(context: Context) {
    val syncIntent = Intent(context, WeatherSyncService::class.java)
    context.startService(syncIntent)
}
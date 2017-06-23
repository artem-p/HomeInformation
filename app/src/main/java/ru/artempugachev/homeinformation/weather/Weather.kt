package ru.artempugachev.myweather.weather

import android.content.ContentValues
import ru.artempugachev.myweather.R
import ru.artempugachev.myweather.data.WeatherContract

/**
 * Representation of weather
 * */
class Weather(val timestamp: Int, val minTemp: Double, val maxTemp: Double, val icon: String,
              val description: String, val wind: Wind) {

    /**
     * Transform to data class for binding
     */
    fun toWeatherData(): WeatherData {
        // todo stub
        return WeatherData(timestamp, minTemp, maxTemp, R.drawable.cloudy, description, wind)
    }

    /**
     * Transform to content values for db writing
     * */
    fun toContentValues(): ContentValues {
        val cv: ContentValues = ContentValues()
        cv.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, timestamp)
        cv.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, minTemp)
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, icon)
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, description)
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, wind.direction)
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, wind.speed)

        cv.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, maxTemp)
        // todo not implemented vals yet
        cv.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 0)
        cv.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 0)


        return cv
    }
}


/**
 * Data class only for databinding
 * */
data class WeatherData(val timestamp: Int, val minTemp: Double, val maxTemp: Double, val weatherIcon: Int,
                              val weatherDescription: String, val wind: Wind) {

}
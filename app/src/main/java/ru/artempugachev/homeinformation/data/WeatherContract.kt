package ru.artempugachev.homeinformation.data

import android.net.Uri
import android.provider.BaseColumns

val AUTHORITY = "ru.artempugachev.homeinformation"
val BASE_CONTENT_URI = Uri.parse("content://${AUTHORITY}")
val PATH_WEATHER = "weather"
val WEATHER_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_WEATHER)
        .build()

class WeatherContract private constructor(){
    class WeatherEntry : BaseColumns {
        companion object {
            val TABLE_NAME: String = "weather"
            val _ID: String = "_id"   // it is in BaseColumns though, but I don't know how to
                                            // access it from this kotlin class
            val COLUMN_TIMESTAMP = "timestamp"
            val COLUMN_MAX_TEMPERATURE = "max_temperature"
            val COLUMN_MIN_TEMPERATURE = "min_temperature"
            val COLUMN_HUMIDITY = "humidity"
            val COLUMN_PRESSURE = "pressure"
            val COLUMN_WEATHER_ICON = "weather_icon"
            val COLUMN_WEATHER_DESCRIPTION = "description"
            val COLUMN_WIND_DIRECTION = "wind_direction"
            val COLUMN_WIND_SPEED = "wind_speed"
        }
    }
}

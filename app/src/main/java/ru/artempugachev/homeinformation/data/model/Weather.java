package ru.artempugachev.homeinformation.data.model;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import ru.artempugachev.homeinformation.R;
import ru.artempugachev.homeinformation.data.WeatherContract;
import ru.artempugachev.homeinformation.weather.Wind;

/**
 * Representation of weather
 * */

public class Weather {
    @SerializedName("time")
    private int timestamp;
    @SerializedName("temperature")
    private double temperature;
    @SerializedName("icon")
    private String icon;
    @SerializedName("summary")
    private String description;
    @SerializedName("windSpeed")
    private double windSpeed;
    @SerializedName("windBearing")
    private double windDir;

    public Weather(int timestamp, double temperature, String icon, String description, double windSpeed, double windDir) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.icon = icon;
        this.description = description;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
    }

    /**
     * Transform to content values for db writing
     * */
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, timestamp);
        cv.put(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE, temperature);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, icon);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, description);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, windDir);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);

        return cv;
    }
}



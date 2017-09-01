package ru.artempugachev.homeinformation.weather;

import android.content.ContentValues;
import ru.artempugachev.homeinformation.R;
import ru.artempugachev.homeinformation.data.WeatherContract;

/**
 * Representation of weather
 * */
public class Weather {
    private int timestamp;
    private double minTemp;
    private double maxTemp;
    private String icon;
    private String description;
    private Wind wind;

    public Weather(int timestamp, double minTemp, double maxTemp, String icon, String description, Wind wind) {
        this.timestamp = timestamp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.icon = icon;
        this.description = description;
        this.wind = wind;
    }

    /**
     * Transform to data class for binding
     */
    public WeatherData toWeatherData() {
        return new WeatherData(timestamp, minTemp, maxTemp, R.drawable.cloud, description, wind);
    }

    /**
     * Transform to content values for db writing
     * */
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, timestamp);
        cv.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, minTemp);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, icon);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, description);
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, wind.getDirection());
        cv.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, wind.getSpeed());
        cv.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, maxTemp);

        // todo not implemented vals yet
        cv.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 0);
        cv.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 0);

        return cv;
    }

    /**
     * Data class only for databinding
     * */
    public class WeatherData {
        private int timestamp;
        private double minTemp;
        private double maxTemp;
        private int weatherIcon;
        private String weatherDescription;
        private Wind wind;

        public WeatherData(int timestamp, double minTemp, double maxTemp, int weatherIcon, String weatherDescription, Wind wind) {
            this.timestamp = timestamp;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.weatherIcon = weatherIcon;
            this.weatherDescription = weatherDescription;
            this.wind = wind;
        }
    }
}



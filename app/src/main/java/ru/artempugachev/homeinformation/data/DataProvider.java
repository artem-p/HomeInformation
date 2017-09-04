package ru.artempugachev.homeinformation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.artempugachev.homeinformation.data.model.Weather;
import ru.artempugachev.homeinformation.weather.Wind;

/**
 * Wrapper on WeatherProvider with helper methods
 */

public class DataProvider {
    private Context context;

    public DataProvider(Context context) {
        this.context = context;
    }

    /**
     * Query data with provider
     * */
    public Weather getCurrentData() {

        Cursor cursor = context.getContentResolver().query(WeatherContract.WEATHER_URI,
                null, null, null, null);

        Weather weather = null;

        if (cursor.moveToFirst()) {
            int timestamp = cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP));
            double temperature = cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE));
            String weatherIcon = cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON));
            String weatherDescription = cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION));
            double windSpeed = cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED));
            double windDir = cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION));

            weather = new Weather(timestamp, temperature, weatherIcon, weatherDescription, windSpeed, windDir);
        }

        cursor.close();

        return weather;
    }

    /**
     * Write array of weatherArray data to db
     * */
    public int writeWeather(List<Weather> weatherList) {
        int rowsInserted = 0;
        if (!weatherList.isEmpty()) {
            ContentValues[] weatherCv = new ContentValues[weatherList.size()];

            for (int weatherCount = 0; weatherCount < weatherList.size(); weatherCount++) {
                weatherCv[weatherCount] = weatherList.get(weatherCount).toContentValues();

            }

            rowsInserted = context.getContentResolver().bulkInsert(WeatherContract.WEATHER_URI, weatherCv);
        }

        return rowsInserted;
    }

    /**
     * Delete all the data
     * */
    public void deleteData() {
        context.getContentResolver().delete(WeatherContract.WEATHER_URI, null, null);
        context.getContentResolver().delete(WeatherContract.SUMMARY_URI, null, null);
    }

    /**
     * Write hourly and daily summary
     * */
    public void writeSummary(String hourlyIcon, String dailyIcon) {
        ContentValues cv = new ContentValues();
        cv.put(WeatherContract.Summary.COLUMN_HOURLY_ICON, hourlyIcon);
        cv.put(WeatherContract.Summary.COLUMN_DAILY_ICON, dailyIcon);
        context.getContentResolver().insert(WeatherContract.SUMMARY_URI, cv);
    }
}

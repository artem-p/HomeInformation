package ru.artempugachev.homeinformation.data;


import android.net.Uri;
import android.provider.BaseColumns;

public final class WeatherContract {
    private WeatherContract() {

    }

    public static final String  AUTHORITY = "ru.artempugachev.homeinformation";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String  PATH_WEATHER = "weather";
    public static final Uri WEATHER_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_WEATHER)
            .build();

    public static class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_WEATHER_ICON = "weather_icon";
        public static final String COLUMN_WEATHER_DESCRIPTION = "description";
        public static final String COLUMN_WIND_DIRECTION = "wind_direction";
        public static final String COLUMN_WIND_SPEED = "wind_speed";
    }
}

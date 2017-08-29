package ru.artempugachev.homeinformation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public final class WeatherDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "homeinformation.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " +
            WeatherContract.WeatherEntry.TABLE_NAME + " (" +
            WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WeatherContract.WeatherEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE + " REAL NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE + " REAL NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " INTEGER NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON + " TEXT NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION + " TEXT NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION + " INTEGER NOT NULL, " +
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
            " UNIQUE" + "(" + WeatherContract.WeatherEntry.COLUMN_TIMESTAMP + ")" + " ON CONFLICT REPLACE);";

    private static final String  SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME;


    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // we use database only for cache, so just drop and recreate it
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
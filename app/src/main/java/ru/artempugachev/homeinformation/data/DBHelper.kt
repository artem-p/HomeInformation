package ru.artempugachev.myweather.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME = "myweather.db"
val DATABASE_VERSION = 1
val SQL_CREATE_WEATHER_TABLE = """CREATE TABLE ${WeatherContract.WeatherEntry.TABLE_NAME}
               (${WeatherContract.WeatherEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${WeatherContract.WeatherEntry.COLUMN_TIMESTAMP} INTEGER NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE} REAL NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE} REAL NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_HUMIDITY} INTEGER NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_PRESSURE} REAL NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON} STRING NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION} STRING NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION} INTEGER NOT NULL,
                ${WeatherContract.WeatherEntry.COLUMN_WIND_SPEED} REAL NOT NULL,
                 UNIQUE (${WeatherContract.WeatherEntry.COLUMN_TIMESTAMP}) ON CONFLICT REPLACE

               );     """

val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${WeatherContract.WeatherEntry.TABLE_NAME}"

class WeatherDbHelper : SQLiteOpenHelper {
    constructor(context: Context): super(context, DATABASE_NAME, null, DATABASE_VERSION)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_WEATHER_TABLE)
    }


    /**
     * As we use database only for cache, we can simply drop and recreate it
     * */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }
}
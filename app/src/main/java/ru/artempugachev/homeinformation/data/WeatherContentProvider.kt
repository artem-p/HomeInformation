package ru.artempugachev.myweather.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

val CODE_WEATHER = 100
val CODE_WEATHER_WITH_DATE = 101

class WeatherContentProvider : ContentProvider() {
    lateinit var dbHelper: WeatherDbHelper
    val uriMatcher = buildUriMatcher()

    private fun buildUriMatcher(): UriMatcher {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        val authority = AUTHORITY
        matcher.addURI(authority, PATH_WEATHER, CODE_WEATHER)
        matcher.addURI(authority, "${PATH_WEATHER}/#", CODE_WEATHER_WITH_DATE)
        return matcher
    }

    override fun onCreate(): Boolean {
        dbHelper = WeatherDbHelper(context)
        return true
    }


    /**
     * Insert multiple rows (current weather + forecast)
     * */
    override fun bulkInsert(uri: Uri?, values: Array<out ContentValues>?): Int {
        val db = dbHelper.writableDatabase

        when (uriMatcher.match(uri)) {
            CODE_WEATHER -> {
                db.beginTransaction()
                var rowsInserted = 0
                try {
                    if (values != null) {
                        for (value in values) {
                            val id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,
                                    null, value)
                            if (id != -1L) {
                                rowsInserted++
                            }
                        }
                    }
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
                return rowsInserted
            }

            else -> {
                return super.bulkInsert(uri, values)
            }
        }
    }


    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            CODE_WEATHER -> {
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                        null,
                        selection, selectionArgs,
                        null, null, sortOrder)
            }

            CODE_WEATHER_WITH_DATE -> {
                val date: String? = uri?.pathSegments?.get(1)
                if (date != null) {
                    val where: String = "${WeatherContract.WeatherEntry.COLUMN_TIMESTAMP}=?"
                    val whereArgs: Array<String> = arrayOf(date)
                    cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                            projection, where, whereArgs, null, null, sortOrder)
                } else {
                    throw UnsupportedOperationException("Date is null: $uri")
                }
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val numRowsDeleted: Int

        // if selection == null we delete all rows, but
        // we won't know how many rows were deleted
        // by passing "1" we can delete all rows and do know number of rows deleted
        val selStr = selection ?: "1"

        when (uriMatcher.match(uri)) {
            CODE_WEATHER -> {
                numRowsDeleted = dbHelper.writableDatabase.delete(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        selStr,
                        selectionArgs
                )
            }

            else -> {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        }

        if (numRowsDeleted != 0) {
            // notify about a change with this Uri
            context.contentResolver.notifyChange(uri, null)
        }
        return numRowsDeleted

    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

/**
 * Some data for test purposes
 * */
fun getTestWeatherContentValues(): Array<ContentValues> {
    val cv1: ContentValues = ContentValues()
    cv1.put(WeatherContract.WeatherEntry._ID, 1)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496528520)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, 11.3)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, 11.3)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 75)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1015.5)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud")
    cv1.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud")
    cv1.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270)
    cv1.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5)

    val cv2: ContentValues = ContentValues()
    cv2.put(WeatherContract.WeatherEntry._ID, 2)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496534400)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, 12.2)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, 14.3)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 75)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1015.5)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud")
    cv2.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud")
    cv2.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270)
    cv2.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5)

    val cv3: ContentValues = ContentValues()
    cv3.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496530800)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, 11.3)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, 14.3)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 75)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1015.5)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud")
    cv3.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud")
    cv3.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270)
    cv3.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5)

    val cv4: ContentValues = ContentValues()
    cv4.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496630801)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, 14.6)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, 15.6)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 75)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1015.5)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud")
    cv4.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud")
    cv4.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270)
    cv4.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5)

    return arrayOf(cv1, cv2, cv3)
}
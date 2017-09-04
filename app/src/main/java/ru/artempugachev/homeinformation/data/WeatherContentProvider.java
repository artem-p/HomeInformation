package ru.artempugachev.homeinformation.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class WeatherContentProvider extends ContentProvider {
    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;
    public static final int CODE_SUMMARY = 200;

    private WeatherDbHelper dbHelper;
    private UriMatcher uriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = WeatherContract.AUTHORITY;
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, CODE_WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/#", CODE_WEATHER_WITH_DATE);
        matcher.addURI(authority, WeatherContract.PATH_SUMMARY, CODE_SUMMARY);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WeatherDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_WEATHER: {
                cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                        null,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }

            case CODE_WEATHER_WITH_DATE: {
                String time = uri.getPathSegments().get(1);
                if (time != null) {
                    String where = WeatherContract.WeatherEntry.COLUMN_TIMESTAMP + "=?";
                    String[] whereArgs  = new String[]{time};
                    cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                            projection, where, whereArgs, null, null, sortOrder);
                } else {
                    throw new UnsupportedOperationException("Date is null: " + uri);
                }

                break;
            }

            case CODE_SUMMARY: {
                cursor = db.query(WeatherContract.Summary.TABLE_NAME, null, null, null, null, null, null);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case CODE_SUMMARY:
                long id = db.insert(WeatherContract.Summary.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(WeatherContract.SUMMARY_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    /**
     * Insert multiple rows (current weather + forecast)
     * */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case CODE_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,
                                null, value);
                        if (id != -1L) {
                            rowsInserted++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return rowsInserted;

            default: {
                return super.bulkInsert(uri, values);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        // if selection == null we delete all rows, but
        // we won't know how many rows were deleted
        // by passing "1" we can delete all rows and do know number of rows deleted
        String selStr = selection != null ? selection : "1";

        switch (uriMatcher.match(uri)) {
            case CODE_WEATHER:
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        selStr,
                        selectionArgs
                );

                break;

            case CODE_SUMMARY:
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        WeatherContract.Summary.TABLE_NAME,
                        selStr,
                        selectionArgs
                );

                break;

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numRowsDeleted != 0) {
            // notify about a change with this Uri
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * Some data for test purposes
     * */
    public static ContentValues[] getTestWeatherContentValues() {
        ContentValues cv1 = new ContentValues();
        cv1.put(WeatherContract.WeatherEntry._ID, 1);
        cv1.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496528520);
        cv1.put(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE, 11.3);
        cv1.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud");
        cv1.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud");
        cv1.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270);
        cv1.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5);

        ContentValues cv2 = new ContentValues();
        cv2.put(WeatherContract.WeatherEntry._ID, 2);
        cv2.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496534400);
        cv2.put(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE, 12.2);
        cv2.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud");
        cv2.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud");
        cv2.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270);
        cv2.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5);

        ContentValues cv3 = new ContentValues();
        cv3.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496530800);
        cv3.put(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE, 11.3);
        cv3.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud");
        cv3.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud");
        cv3.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270);
        cv3.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5);

        ContentValues cv4 = new ContentValues();
        cv4.put(WeatherContract.WeatherEntry.COLUMN_TIMESTAMP, 1496630801);
        cv4.put(WeatherContract.WeatherEntry.COLUMN_TEMPERATURE, 14.6);
        cv4.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, "cloud");
        cv4.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_DESCRIPTION, "Cloud");
        cv4.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, 270);
        cv4.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5);

        return new ContentValues[] {cv1, cv2, cv3};
    }
}


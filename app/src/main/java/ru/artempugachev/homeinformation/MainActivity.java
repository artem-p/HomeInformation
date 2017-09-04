package ru.artempugachev.homeinformation;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

import ru.artempugachev.homeinformation.data.WeatherContract;
import ru.artempugachev.homeinformation.data.WeatherSyncJobInitializer;
import ru.artempugachev.homeinformation.data.WeatherSyncService;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final static int WEATHER_LOADER_ID = 42;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int REQUEST_LOCATION = 100;

    private  GoogleApiClient googleApiClient;
    private SharedPreferences sharedPreferences;
    private TextView curTempTextView;
    private ProgressBar progressBar;
    private TextView dailySummaryIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setUpViews();

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        getSupportLoaderManager().initLoader(WEATHER_LOADER_ID, null, this);

        WeatherSyncJobInitializer weatherSyncJobInitializer = new WeatherSyncJobInitializer();
        weatherSyncJobInitializer.scheduleWeatherSyncJobService(this);
        WeatherSyncService.startWeatherSyncNow(this);

    }


    private void setUpViews() {
        curTempTextView = (TextView) findViewById(R.id.curTempTextView);
        progressBar = (ProgressBar) findViewById(R.id.pb_weather);
        dailySummaryIcon = (TextView) findViewById(R.id.daySummaryIcon);
        setUpDateView();
    }


    private void saveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  request permissions. Then catch callback and get location there
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permissions granted, get location
            getLocation();
        }
    }

    private void getLocation() {
        Location location = getLastLocation();
        if (location != null) {
            writeLocationToPrefs(location, sharedPreferences);
        } else {
            Toast.makeText(MainActivity.this, R.string.cannot_get_location, Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private void writeLocationToPrefs(Location location, SharedPreferences preferences) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        String latStr = String.valueOf(lat);
        String lonStr = String.valueOf(lon);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_lat), latStr);
        editor.putString(getString(R.string.pref_lon), lonStr);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Get location.
                    getLocation();
                } else {
                    // location permission denied.
                }
                return;
            }

        }
    }


    private void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.no_google_play_services, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;

    }

    private void setUpDateView() {
        TextClock dateView = (TextClock) findViewById(R.id.dateView);

        String skeleton = "EEEEMMMMd";
        String formatPattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton);
        dateView.setFormat12Hour(formatPattern);
        dateView.setFormat24Hour(formatPattern);
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideStatusBar();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Google api callback methods
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
        saveLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * Fetch weather from weather provider and update ui
     */
//    private inner class UpdateWeatherTask : AsyncTask<Void, Void, Weather?>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            if (curTempTextView!!.text == "") {
//                //  Show progress bar when first loading
//                showProgressBar()
//            }
//        }
//
//        override fun doInBackground(vararg params: Void): Weather? {
//            // first we need coordinates
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            val coord = getCoordsFromPrefs(preferences)
//            var weather: Weather? = null
//            if (coord != null) {
//                try {
//                    val provider = WeatherProvider(BuildConfig.DARK_SKY_API_KEY)
//                    weather = provider.fetchCurrent(coord)
//                    return weather
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//            return weather
//        }
//
//        override fun onPostExecute(weather: Weather?) {
//            super.onPostExecute(weather)
//            showWeatherViews()
//
//            if (weather != null) {
//                val curWeatherTextView = findViewById(R.id.curWeatherTextView) as TextView
//                curWeatherTextView.text = weather.toCurrent()
//
//                val forecastTextView = findViewById(R.id.forecastTextView) as TextView
//                forecastTextView.text = weather.toForecastSummary()
//            }
//        }
//
//        /**
//         * When loading, show progress bar and hide views
//         */
//        private fun showProgressBar() {
//            curTempTextView!!.visibility = View.INVISIBLE
//            mForecastTextView!!.visibility = View.INVISIBLE
//            progressBar!!.visibility = View.VISIBLE
//        }
//
//
//        /**
//         * After loading, hide progress bar and show views
//         */
//        private fun showWeatherViews() {
//            curTempTextView!!.visibility = View.VISIBLE
//            mForecastTextView!!.visibility = View.VISIBLE
//            progressBar!!.visibility = View.INVISIBLE
//        }
//
//        /**
//         * Read coordinates from preferences
//         */
//        private fun getCoordsFromPrefs(preferences: SharedPreferences): Coordinate? {
//            val lat = preferences.getString(getString(R.string.pref_lat), "")
//            val lon = preferences.getString(getString(R.string.pref_lon), "")
//
//            if (lat == "" || lon == "") {
//                return null
//            } else {
//
//                return Coordinate(lat, lon)
//            }
//        }
//    }

    /**
     * Loader methods. We load data from database with cursor loader
     * It monitors URI and loads new data when new data puts in database
     * */


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case WEATHER_LOADER_ID: {
                return new CursorLoader(this,
                        WeatherContract.WEATHER_URI,
                        null, null, null, null);
            }

            default: {
                throw new RuntimeException("Loader not implemented: " + loaderId);
            }
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            double curTemp = cursor.getDouble(cursor.getColumnIndex(
                    WeatherContract.WeatherEntry.COLUMN_TEMPERATURE));
            curTempTextView.setText(getString(R.string.format_temp, curTemp));
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

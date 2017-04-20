package ru.artempugachev.homeinformation;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CALENDAR = 300;
    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_LOCATION = 100;
    private SharedPreferences mSharedPreferences;
    private Timer mWeatherTimer;
    private TextView mCurWeatherTextView;
    private TextView mForecastTextView;
    private ProgressBar mWeatherProgressBar;
    private TextView mEventsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpViews();

        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        runWeatherTask();
        runCalendarTask();
    }

    private void runCalendarTask() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            //  request permissions. Then catch callback
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_CALENDAR);
        } else {
            // calendar permission granted
            getCalendarEvents();
        }

    }

    private void getCalendarEvents() {
        CalendarModule calendarModule = new CalendarModule();
        String events = calendarModule.getEvents(this);
        // todo add calendar icon to xml
        if (!events.isEmpty()) {
            mEventsTextView.setText(events);
        } else {
            mEventsTextView.setText(R.string.no_calendar_events);
        }
    }

    private void setUpViews() {
        mCurWeatherTextView = (TextView) findViewById(R.id.curWeatherTextView);
        mForecastTextView = (TextView) findViewById(R.id.forecastTextView);
        mWeatherProgressBar = (ProgressBar) findViewById(R.id.pb_weather);
        mEventsTextView = (TextView) findViewById(R.id.eventsTextView);
        setUpDateView();
    }

    private void runWeatherTask() {
        final Handler handler = new Handler();
        mWeatherTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new UpdateWeatherTask().execute();
                    }
                });
            }
        };

        mWeatherTimer.schedule(task, 0, WeatherProvider.UPDATE_INTERVAL_MS);
    }

    private void saveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  request permissions. Then catch callback and get location there
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permissions granted, get location
            getLocation();
        }
    }

    private void getLocation() {
        Location location = getLastLocation();
        if (location != null) {
            writeLocationToPrefs(location, mSharedPreferences);
        } else {
            Toast.makeText(MainActivity.this, R.string.cannot_get_location, Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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

            case REQUEST_CALENDAR:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCalendarEvents();
                } else {
                    // calendar permission denied
                }
                return;
        }
    }


    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
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
        DateText dateText = new DateText();
        dateView.setFormat12Hour(dateText.print());
        dateView.setFormat24Hour(dateText.print());
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
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeatherTimer.cancel();
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
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     *
     * Fetch weather from weather provider and update ui
     * */
    private class UpdateWeatherTask extends AsyncTask<Void, Void, Weather> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCurWeatherTextView.getText().equals("")) {
                //  Show progress bar when first loading
                showProgressBar();
            }
        }

        @Override
        protected Weather doInBackground(Void... params) {
            // first we need coordinates
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            Coordinate coord = getCoordsFromPrefs(preferences);
            Weather weather = null;
            if (coord != null) {
                try {
                    WeatherProvider provider = new WeatherProvider(BuildConfig.DARK_SKY_API_KEY);
                    weather = provider.fetchCurrent(coord);
                    return weather;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            showWeatherViews();

            if (weather != null) {
                TextView curWeatherTextView = (TextView) findViewById(R.id.curWeatherTextView);
                curWeatherTextView.setText(weather.toCurrent());

                TextView forecastTextView = (TextView) findViewById(R.id.forecastTextView);
                forecastTextView.setText(weather.toForecastSummary());
            }
        }

        /**
         * When loading, show progress bar and hide views
         * */
        private void showProgressBar() {
            mCurWeatherTextView.setVisibility(View.INVISIBLE);
            mForecastTextView.setVisibility(View.INVISIBLE);
            mWeatherProgressBar.setVisibility(View.VISIBLE);
        }


        /**
         * After loading, hide progress bar and show views
         * */
        private void showWeatherViews() {
            mCurWeatherTextView.setVisibility(View.VISIBLE);
            mForecastTextView.setVisibility(View.VISIBLE);
            mWeatherProgressBar.setVisibility(View.INVISIBLE);
        }

        /**
         * Read coordinates from preferences
         * */
        @Nullable
        private Coordinate getCoordsFromPrefs(SharedPreferences preferences) {
            String lat = preferences.getString(getString(R.string.pref_lat), "");
            String lon = preferences.getString(getString(R.string.pref_lon), "");

            if (lat.equals("") || lon.equals("")) {
                return null;
            } else {

                return new Coordinate(lat, lon);
            }
        }
    }
}

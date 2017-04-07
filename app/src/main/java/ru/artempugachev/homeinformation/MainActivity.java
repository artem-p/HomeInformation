package ru.artempugachev.homeinformation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_LOCATION = 1;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideStatusBar();
        setUpDateView();

        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        TextView curWeatherTextView = (TextView) findViewById(R.id.curWeatherTextView);
        curWeatherTextView.setText(BuildConfig.DARK_SKY_API_KEY);

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        startWeatherService();
    }

    private void startWeatherService() {
        Intent weatherServiceIntent = new Intent(MainActivity.this, WeatherService.class);
        startService(weatherServiceIntent);
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
            Location location = getLocation();
            if (location != null) {
                writeLocationToPrefs(location, mSharedPreferences);
            } else {
                Toast.makeText(MainActivity.this, R.string.cannot_get_location, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location getLocation() {
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
                    // todo
                    // location permission denied.
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
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
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

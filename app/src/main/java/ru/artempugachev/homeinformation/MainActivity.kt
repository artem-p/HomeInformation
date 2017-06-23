package ru.artempugachev.homeinformation

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.View
import android.widget.ProgressBar
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import ru.artempugachev.homeinformation.data.WEATHER_URI
import ru.artempugachev.homeinformation.data.WeatherContract
import ru.artempugachev.homeinformation.data.WeatherSyncJobInitializer
import ru.artempugachev.homeinformation.data.startWeatherSyncNow

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {
    private var googleApiClient: GoogleApiClient? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var curTempTextView: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViews()

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)

        if (checkPlayServices()) {
            buildGoogleApiClient()
        }

        supportLoaderManager.initLoader(WEATHER_LOADER_ID, null, this);

        val weatherSyncJobInitializer = WeatherSyncJobInitializer()
        weatherSyncJobInitializer.scheduleWeatherSyncJobService(this)
        startWeatherSyncNow(this)

    }

    private fun setUpViews() {
        curTempTextView = findViewById(R.id.curTempTextView) as TextView
        progressBar = findViewById(R.id.pb_weather) as ProgressBar
        setUpDateView()
    }


    private fun saveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  request permissions. Then catch callback and get location there
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION)
        } else {
            // permissions granted, get location
            getLocation()
        }
    }

    private fun getLocation() {
        val location = lastLocation
        if (location != null) {
            writeLocationToPrefs(location, sharedPreferences)
        } else {
            Toast.makeText(this@MainActivity, R.string.cannot_get_location, Toast.LENGTH_SHORT).show()
        }
    }

    private val lastLocation: Location?
        get() = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

    private fun writeLocationToPrefs(location: Location, preferences: SharedPreferences) {
        val lat = location.latitude
        val lon = location.longitude
        val latStr = lat.toString()
        val lonStr = lon.toString()
        val editor = preferences.edit()
        editor.putString(getString(R.string.pref_lat), latStr)
        editor.putString(getString(R.string.pref_lon), lonStr)
        editor.apply()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Get location.
                    getLocation()
                } else {
                    // location permission denied.
                }
                return
            }
        }
    }


    private fun buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }

    }

    private fun checkPlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Toast.makeText(applicationContext,
                        R.string.no_google_play_services, Toast.LENGTH_LONG)
                        .show()
                finish()
            }
            return false
        }
        return true

    }

    private fun setUpDateView() {
        val dateView = findViewById(R.id.dateView) as TextClock
        val dateText = DateText()
        dateView.format12Hour = dateText.print()
        dateView.format24Hour = dateText.print()
    }

    private fun hideStatusBar() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    override fun onStart() {
        super.onStart()
        hideStatusBar()
        if (googleApiClient != null) {
            googleApiClient!!.connect()
        }
    }

    override fun onResume() {
        super.onResume()
        checkPlayServices()
    }

    /**
     * Google api callback methods
     */

    override fun onConnected(bundle: Bundle?) {
        // Once connected with google api, get the location
        saveLocation()
    }

    override fun onConnectionSuspended(i: Int) {
        googleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

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
     * It monitors URI and load new data when new data puts in database
     * */
    override fun onCreateLoader(loaderId: Int, args: Bundle?): Loader<Cursor> {
        when (loaderId) {
            WEATHER_LOADER_ID -> {
                val sortOrder = "${WeatherContract.WeatherEntry.COLUMN_TIMESTAMP} ASC"

                return CursorLoader(this, WEATHER_URI, null, null, null, sortOrder)
            }
            else -> {
                throw RuntimeException("Loader not implemented: " + loaderId)
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {

    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {

    }

    companion object {
        private val WEATHER_LOADER_ID = 42
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private val REQUEST_CALENDAR = 300
        private val REQUEST_LOCATION = 100
    }
}

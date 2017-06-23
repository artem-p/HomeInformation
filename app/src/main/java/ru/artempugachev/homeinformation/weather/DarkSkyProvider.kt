package ru.artempugachev.myweather.weather

import android.net.Uri
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Class for weather provider. Weather provider responds for
 * fetching weather data
 */

class DarkSkyProvider(private val mApiKey: String) {
    private val BASE_URL = "https://api.darksky.net/forecast"
    // parameters here https://darksky.net/dev/docs/forecast
    private val LANG_PARAM_KEY = "lang"
    private val LANG_PARAM_VALUE = "ru"
    private val UNITS_PARAM_KEY = "units"
    private val UNITS_PARAM_VALUE = "si"
    private val CURRENTLY_JSON_PARAM = "currently"
    private val DAILY_JSON_PARAM = "daily"
    private val TIMESTAMP_JSON_PARAM = "time"
    private val SUMMARY_JSON_PARAM = "summary"
    private val ICON_JSON_PARAM = "icon"
    private val TEMPERATURE_JSON_PARAM = "temperature"
    private val TEMPERATURE_MIN_JSON_PARAM = "temperatureMin"
    private val TEMPERATURE_MAX_JSON_PARAM = "temperatureMax"
    private val APPARENT_TEMPERATURE_JSON_PARAM = "apparentTemperature"
    private val HUMIDITY_JSON_PARAM = "humidity"
    private val WIND_SPEED_JSON_PARAM = "windSpeed"
    private val WIND_DIR_JSON_PARAM = "windBearing"
    private val DATA_JSON_PARAM = "data"


    @Throws(IOException::class, JSONException::class)
    fun fetchWeatherData(coordinate: Coordinate): List<Weather> {
        val url = buildCurrentUrl(coordinate)
        val respStr = fetchByUrl(url)
        val weatherJson = JSONObject(respStr)
        val currentlyJson = weatherJson.getJSONObject(CURRENTLY_JSON_PARAM)
        val forecastJson = weatherJson.getJSONObject(DAILY_JSON_PARAM)
        return getWeatherListFromJson(currentlyJson, forecastJson)
    }

    @Throws(IOException::class)
    private fun fetchByUrl(url: URL): String {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }

    @Throws(JSONException::class)
    fun getWeatherListFromJson(jsonCurrent: JSONObject, jsonForecast: JSONObject): List<Weather> {
        val weatherList: MutableList<Weather> = mutableListOf()

        val currentWeather = getWeatherFromJson(jsonCurrent, false)
        weatherList.add(currentWeather)
        val forecastList = getForecastListFromJson(jsonForecast)
        weatherList.addAll(forecastList)

        val outputWeatherList: List<Weather> = weatherList
        return outputWeatherList
    }

    @Throws(JSONException::class)
    fun getForecastListFromJson(jsonForecast: JSONObject): List<Weather> {
        val forecastList: MutableList<Weather> = mutableListOf()
        val forecastJsonArray = jsonForecast.getJSONArray(DATA_JSON_PARAM)

        for (i in 0..(forecastJsonArray.length() - 1)) {
            val forecastJson = forecastJsonArray.getJSONObject(i)
            val weather = getWeatherFromJson(forecastJson, true)
            forecastList.add(weather)
        }

        val outputForecastList: List<Weather> = forecastList
        return outputForecastList
    }

    fun getWeatherFromJson(jsonWeather: JSONObject, isForecast: Boolean): Weather {
        val timestampStr = jsonWeather.getString(TIMESTAMP_JSON_PARAM)
        val summary = jsonWeather.getString(SUMMARY_JSON_PARAM)
        val icon = jsonWeather.getString(ICON_JSON_PARAM)

        // get current temperature for current weather and min max for forecast
        val minTempStr: String
        val maxTempStr: String

        /*
       * We use the same class for current weather and forecast, as it differs only in temps
       * For current weather put current temperature to min and to max as well
       * */
        if (!isForecast) {
            minTempStr = jsonWeather.getString(TEMPERATURE_JSON_PARAM)
            maxTempStr = jsonWeather.getString(TEMPERATURE_JSON_PARAM)
        } else {
            minTempStr = jsonWeather.getString(TEMPERATURE_MIN_JSON_PARAM)
            maxTempStr = jsonWeather.getString(TEMPERATURE_MAX_JSON_PARAM)
        }

//        val apparentTemperatureStr = jsonWeather.getString(APPARENT_TEMPERATURE_JSON_PARAM)
        val humidityStr = jsonWeather.getString(HUMIDITY_JSON_PARAM)
        val windSpeedStr = jsonWeather.getString(WIND_SPEED_JSON_PARAM)
        val windDirStr = jsonWeather.getString(WIND_DIR_JSON_PARAM)
//        val forecastSummary = jsonWeather.getString(SUMMARY_JSON_PARAM)

        return Weather(timestampStr.toInt(), minTempStr.toDouble(), maxTempStr.toDouble(),
                icon, summary, Wind(windSpeedStr.toDouble(), windDirStr.toInt()))
    }


    @Throws(MalformedURLException::class)
    fun buildCurrentUrl(coordinate: Coordinate): URL {
        val builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mApiKey)
                .appendPath(coordinate.toUrlPath())
                .appendQueryParameter(LANG_PARAM_KEY, LANG_PARAM_VALUE)
                .appendQueryParameter(UNITS_PARAM_KEY, UNITS_PARAM_VALUE).build()

        return URL(builtUri.toString())
    }

    companion object {
        val UPDATE_INTERVAL_MS = 60 * 1000 * 15 // 15 min
    }
}

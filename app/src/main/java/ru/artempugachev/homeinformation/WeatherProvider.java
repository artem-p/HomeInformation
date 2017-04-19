package ru.artempugachev.homeinformation;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class for weather provider. Weather provider responds for
 * fetching weather data
 */

public class WeatherProvider {
    private final String mApiKey;
    private final String BASE_URL = "https://api.darksky.net/forecast";
    public final static int UPDATE_INTERVAL_MS = 60*1000*15; // 15 min
    // parameters here https://darksky.net/dev/docs/forecast
    // todo add locale support for lang and units
    private final String LANG_PARAM_KEY = "lang";
    private final String LANG_PARAM_VALUE = "ru";
    private final String UNITS_PARAM_KEY = "units";
    private final String UNITS_PARAM_VALUE = "si";
    private final String CURRENTLY_JSON_PARAM = "currently";
    private final String HOURLY_JSON_PARAM = "hourly";
    private final String TIMESTAMP_JSON_PARAM = "time";
    private final String SUMMARY_JSON_PARAM = "summary";
    private final String ICON_JSON_PARAM = "icon";
    private final String TEMPERATURE_JSON_PARAM = "temperature";
    private final String APPARENT_TEMPERATURE_JSON_PARAM = "apparentTemperature";
    private final String HUMIDITY_JSON_PARAM = "humidity";
    private final String WIND_SPEED_JSON_PARAM = "windSpeed";
    private final String WIND_DIR_JSON_PARAM = "windBearing";



    public WeatherProvider(@NonNull String mApiKey) {
        this.mApiKey = mApiKey;
    }


    public Weather fetchCurrent(Coordinate coordinate) throws IOException, JSONException {
        URL url = buildCurrentUrl(coordinate);
        String respStr = fetchByUrl(url);
        JSONObject weatherJson = new JSONObject(respStr);
        JSONObject currentlyJson = weatherJson.getJSONObject(CURRENTLY_JSON_PARAM);
        JSONObject forecastJson = weatherJson.getJSONObject(HOURLY_JSON_PARAM);
        return buildWeatherFromJson(currentlyJson, forecastJson);
    }

    private String fetchByUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Weather buildWeatherFromJson(JSONObject jsonCurrent, JSONObject jsonForecast) throws JSONException {
        String timestampStr = jsonCurrent.getString(TIMESTAMP_JSON_PARAM);
        String summary = jsonCurrent.getString(SUMMARY_JSON_PARAM);
        String icon = jsonCurrent.getString(ICON_JSON_PARAM);
        String temperatureStr = jsonCurrent.getString(TEMPERATURE_JSON_PARAM);
        String apparentTemperatureStr = jsonCurrent.getString(APPARENT_TEMPERATURE_JSON_PARAM);
        String humidityStr = jsonCurrent.getString(HUMIDITY_JSON_PARAM);
        String windSpeedStr = jsonCurrent.getString(WIND_SPEED_JSON_PARAM);
        String windDirStr = jsonCurrent.getString(WIND_DIR_JSON_PARAM);
        String forecastSummary = jsonForecast.getString(SUMMARY_JSON_PARAM);
        return new Weather(Long.parseLong(timestampStr), summary, forecastSummary, icon,
                Float.parseFloat(temperatureStr), Float.parseFloat(apparentTemperatureStr),
                Float.parseFloat(humidityStr), Float.parseFloat(windSpeedStr), Float.parseFloat(windDirStr));
    }


    public URL buildCurrentUrl(Coordinate coordinate) throws MalformedURLException {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mApiKey)
                .appendPath(coordinate.toUrlPath())
                .appendQueryParameter(LANG_PARAM_KEY, LANG_PARAM_VALUE)
                .appendQueryParameter(UNITS_PARAM_KEY, UNITS_PARAM_VALUE).build();

        return new URL(builtUri.toString());
    }
}

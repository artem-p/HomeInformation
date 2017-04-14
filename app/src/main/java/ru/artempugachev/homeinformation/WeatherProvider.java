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

    // parameters here https://darksky.net/dev/docs/forecast
    // todo add locale support for lang and units
    private final String LANG_PARAM_KEY = "lang";
    private final String LANG_PARAM_VALUE = "ru";
    private final String UNITS_PARAM_KEY = "units";
    private final String UNITS_PARAM_VALUE = "si";
    private final String CURRENTLY_JSON_PARAM = "currently";


    public WeatherProvider(@NonNull String mApiKey) {
        this.mApiKey = mApiKey;
    }


    public Weather fetchCurrent(Coordinate coordinate) throws IOException, JSONException {
        URL url = buildCurrentUrl(coordinate);
        String respStr = fetchByUrl(url);
        JSONObject forecastJson = new JSONObject(respStr);
        JSONObject currentlyJson = forecastJson.getJSONObject(CURRENTLY_JSON_PARAM);
        return buildWeatherFromJson(currentlyJson);
    }

    private String fetchByUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Weather buildWeatherFromJson(JSONObject jsonWeather) {

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

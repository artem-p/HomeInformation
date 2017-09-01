package ru.artempugachev.homeinformation.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.artempugachev.homeinformation.BuildConfig;

/**
 * Dark sky api client for retrofit
 */

public class DarkSkyApiClient {
    public static final String BASE_URL = "https://api.darksky.net/forecast/" + BuildConfig.DARK_SKY_API_KEY + "/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

package ru.artempugachev.homeinformation.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.artempugachev.homeinformation.data.model.DarkSkyResponse;

/**
 * Retrofit endpoints for darksky
 */

public interface DarkSkyApiInterface {
    @GET("{lat}, {lon}")
    Call<DarkSkyResponse> getCurrentWeatherAndForecast(@Path("lat") double lat, @Path("lon") double lon, @Query("units") String units);
}

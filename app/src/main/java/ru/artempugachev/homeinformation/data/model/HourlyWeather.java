package ru.artempugachev.homeinformation.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Dark sky provides hourly weather. It is summary for today and list of weather data up to 48 hours
 */

public class HourlyWeather {
    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("data")
    private List<Weather> weather;

    public HourlyWeather(String summary, String icon, List<Weather> weather) {
        this.summary = summary;
        this.icon = icon;
        this.weather = weather;
    }
}

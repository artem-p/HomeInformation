package ru.artempugachev.homeinformation.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Dark sky provides hourly forecast. It is summary for today and list of forecast data up to 48 hours
 */

public class HourlyWeather {
    @SerializedName("summary")
    private String summary;

    @SerializedName("icon")
    private String icon;

    @SerializedName("data")
    private List<Weather> forecast;

    public HourlyWeather(String summary, String icon, List<Weather> forecast) {
        this.summary = summary;
        this.icon = icon;
        this.forecast = forecast;
    }

    public List<Weather> getForecast() {
        return forecast;
    }

    public String getIcon() {
        return icon;
    }
}

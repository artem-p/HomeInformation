package ru.artempugachev.homeinformation.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Dark sky api response
 */

public final class DarkSkyResponse {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("currently")
    private Weather currentWeather;

    @SerializedName("hourly")
    private HourlyWeather hourlyWeather;

    public DarkSkyResponse(double latitude, double longitude, String timezone, Weather currentWeather, HourlyWeather hourlyWeather) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.currentWeather = currentWeather;
        this.hourlyWeather = hourlyWeather;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public HourlyWeather getHourlyWeather() {
        return hourlyWeather;
    }

    public void setHourlyWeather(HourlyWeather hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }
}

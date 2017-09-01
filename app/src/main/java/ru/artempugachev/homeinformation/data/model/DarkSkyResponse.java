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
    private List<Weather> hourly;

    public DarkSkyResponse(double latitude, double longitude, String timezone, Weather currentWeather, List<Weather> hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.currentWeather = currentWeather;
        this.hourly = hourly;
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

    public List<Weather> getHourly() {
        return hourly;
    }

    public void setHourly(List<Weather> hourly) {
        this.hourly = hourly;
    }
}

package ru.artempugachev.homeinformation;

/**
 * Current weather and forecast summary
 */

public class Weather {
    private final long timestamp;
    private final String summary;
    private final String icon;
    private final float temperature;
    private final float apparentTemperature;
    private final float humidity;
    private final float windSpeed;
    private final float windDirection;

    public Weather(long timestamp, String summary, String icon, float temperature, float apparentTemperature, float humidity, float windSpeed, float windDirection) {
        this.timestamp = timestamp;
        this.summary = summary;
        this.icon = icon;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }
}

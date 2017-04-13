package ru.artempugachev.homeinformation;

/**
 * Coordinate with String parameters
 * We need to read coordinates from preferences and put it to url to fetch weather
 * So we need Strings
 */

public final class Coordinate {
    private final String lat;
    private final String lon;

    public Coordinate(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
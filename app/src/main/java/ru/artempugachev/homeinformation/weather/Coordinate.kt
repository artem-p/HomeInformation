package ru.artempugachev.homeinformation.weather

/**
 * Coordinate with String parameters
 * We need to read coordinates from preferences and put it to url to fetch weather
 * So we need Strings
 */

class Coordinate(private val lat: String, private val lon: String) {

    /**
     * Return String with weather URl path (60, 30)
     */
    fun toUrlPath(): String {
        return lat + "," + lon
    }
}

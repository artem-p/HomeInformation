package ru.artempugachev.homeinformation;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeatherTest {
    @Test
    public void toCurrentWeather() throws Exception {
        Weather weather = new Weather(0, "Light snow", null, null, -1.56f, 0, 0, 0, 0);
        String expectedCurrentWeather = "-1.6 °C Light snow";
        assertEquals(expectedCurrentWeather, weather.toCurrent());
    }

}

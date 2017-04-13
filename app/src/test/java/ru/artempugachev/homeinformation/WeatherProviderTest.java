package ru.artempugachev.homeinformation;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WeatherProviderTest {
    @Test
    public void buildUrl() throws Exception{
        WeatherProvider provider = new WeatherProvider();

        assertEquals(null, provider.buildCurrentUrl());
    }
}

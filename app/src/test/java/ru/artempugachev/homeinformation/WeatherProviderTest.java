package ru.artempugachev.homeinformation;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WeatherProviderTest {
    @Test
    public void buildUrl() throws Exception{
        // todo get api key
        WeatherProvider provider = new WeatherProvider();

        // todo make test
        assertEquals(null, provider.buildCurrentUrl());
    }
}

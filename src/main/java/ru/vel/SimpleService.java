package ru.vel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.util.Map;

@Path("/weather")
public class SimpleService {

    @RestClient
    YandexWeatherClient yandexWeatherClient;

    @Inject
    ObjectMapper objectMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getWeather() {
        return yandexWeatherClient.getForecast(Map.of(
                "lat", "55.75",
                "lon", "37.62"
        ));
    }

    @GET
    @Path("/temp")
    @Produces(MediaType.APPLICATION_JSON)
    public Double getTemp() {
        String forecast = yandexWeatherClient.getForecast(Map.of(
                "lat", "55.75",
                "lon", "37.62"
        ));

        var temp = 0.0;
        try {
            JsonNode jsonNode = objectMapper.readTree(forecast);
            jsonNode = jsonNode.get("fact");
            temp = jsonNode.get("temp").asDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    @GET
    @Path("/temp/avg")
    @Produces(MediaType.APPLICATION_JSON)
    public Double getAvgTemp() {
        String forecast = yandexWeatherClient.getForecast(Map.of(
                "lat", "55.75",
                "lon", "37.62",
                "limit", "3"
        ));

        var temp = 0.0;
        var count = 0;
        try {
            JsonNode jsonNode = objectMapper.readTree(forecast);
            var daysNode = jsonNode.get("forecasts");

            if (daysNode.isArray()) {
                for (JsonNode day : daysNode) {
                    JsonNode hoursNode = day.get("hours");
                    if (hoursNode.isArray()) {
                        for (JsonNode hour : hoursNode) {
                            temp += hour.get("temp").asDouble();
                            count++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp / count;
    }
}

package ru.vel;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Map;

@RegisterRestClient(configKey = "yandex-weather")
public interface YandexWeatherClient {

    @GET
    @ClientHeaderParam(name = "X-Yandex-Weather-Key", value = "${yandex.key}")
    @Path("/forecast")
    String getForecast(@RestQuery Map<String, Object> filters);
}

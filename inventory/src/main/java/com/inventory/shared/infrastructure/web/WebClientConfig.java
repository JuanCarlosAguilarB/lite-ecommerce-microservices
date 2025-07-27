package com.inventory.shared.infrastructure.web;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@Slf4j
public class WebClientConfig {

    private final Duration timeout = Duration.ofSeconds(3);
    private final String apiKey = "123456789";

    private HttpClient httpClient() {
        return HttpClient.create()
                .responseTimeout(timeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout.toMillis());
    }


    public WebClient buildWebClient(String baseUrl) {
        log.info("Creating WebClient with base URL: {}", baseUrl);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "ApiKey " + apiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

}
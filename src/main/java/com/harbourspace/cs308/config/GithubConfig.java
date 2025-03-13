package com.harbourspace.cs308.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;


@Configuration
public class GithubConfig {

    @Value("${github.token:}")
    private String githubToken;

    @Bean
    public WebClient gitHubWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10)) // Set timeout
                .compress(true); // Enable gzip compression

        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(headers -> {
                    headers.set("Accept", "application/vnd.github+json");
                    if (githubToken != null && !githubToken.isEmpty()) {
                        headers.setBearerAuth(githubToken);
                    }
                })
                .build();              
    }
}

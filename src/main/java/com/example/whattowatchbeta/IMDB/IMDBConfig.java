package com.example.whattowatchbeta.IMDB;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix="imdb")
public class IMDBConfig {
    private String baseUrl;
    private String endUrl;
}

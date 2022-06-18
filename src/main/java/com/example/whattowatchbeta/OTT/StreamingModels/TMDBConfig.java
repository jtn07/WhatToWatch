package com.example.whattowatchbeta.OTT.StreamingModels;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "tmdb")
@Data
public class TMDBConfig {
    private String startUrl;
    private String endUrl;
}

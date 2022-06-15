package com.example.whattowatchbeta;

import com.example.whattowatchbeta.IMDB.IMDBConfig;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableConfigurationProperties({IMDBConfig.class})
@EnableScheduling
@NoArgsConstructor
public abstract class WhatToWatchBetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatToWatchBetaApplication.class, args);
    }

}

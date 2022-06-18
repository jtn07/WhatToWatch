package com.example.whattowatchbeta;

import com.example.whattowatchbeta.IMDB.IMDBConfig;
import com.example.whattowatchbeta.Mail.SendGridConfig;
import com.example.whattowatchbeta.OTT.StreamingModels.TMDBConfig;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableConfigurationProperties({IMDBConfig.class, TMDBConfig.class, SendGridConfig.class})
@EnableScheduling
@NoArgsConstructor
public abstract class WhatToWatchBetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatToWatchBetaApplication.class, args);
    }

}

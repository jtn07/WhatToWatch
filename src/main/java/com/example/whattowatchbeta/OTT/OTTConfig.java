package com.example.whattowatchbeta.OTT;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
@Configuration
@ConfigurationProperties(prefix = "ott")
@Data
public class OTTConfig {

    private String OTTAvailabilityCheckUrl;
    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}

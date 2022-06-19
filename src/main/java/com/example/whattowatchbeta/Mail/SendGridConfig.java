package com.example.whattowatchbeta.Mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sendgrid")
public class SendGridConfig {
    private String from;
    private String name;
    private String apiKey;
}

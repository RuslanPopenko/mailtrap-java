package com.railsware.mailtrap.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("client.mailtrap")
public class MailtrapClientProperties {

    private String url;

    private String token;

    private int connectTimeoutMillis = 10000;

    private int readTimeoutMillis = 15000;
}


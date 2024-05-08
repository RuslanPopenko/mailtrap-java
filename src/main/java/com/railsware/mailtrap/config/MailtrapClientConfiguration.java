package com.railsware.mailtrap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsware.mailtrap.client.MailtrapApi;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(MailtrapClientProperties.class)
public class MailtrapClientConfiguration {

    private final Encoder encoder;
    private final Decoder decoder;
    private final String url;
    private final Client client;
    private final Contract contract;
    private final ErrorDecoder errorDecoder;
    private final Logger.Level logLevel;
    private final Logger logger;
    private final Request.Options options;
    private final RequestInterceptor apiTokenRequestInterceptor;

    @Autowired
    public MailtrapClientConfiguration(MailtrapClientProperties properties,
                                       Optional<Client> client,
                                       Optional<ObjectMapper> objectMapper,
                                       Optional<Logger.Level> logLevel,
                                       Optional<Logger> logger) {
        this.url = Optional.ofNullable(properties.getUrl())
                .orElseThrow(() -> new IllegalArgumentException("URL is required"));
        this.contract = new SpringMvcContract();
        this.client = client.orElseGet(this::defaultClient);
        final var mapper = objectMapper.orElseGet(ObjectMapper::new);
        this.encoder = new JacksonEncoder(mapper);
        this.decoder = new JacksonDecoder(mapper);
        this.errorDecoder = new MailtrapErrorDecoder(decoder);
        this.logLevel = logLevel.orElse(Logger.Level.NONE);
        this.logger = logger.orElseGet(Logger.NoOpLogger::new);
        this.options = getOptions(properties);
        final var apiToken = Optional.ofNullable(properties.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token is required"));
        this.apiTokenRequestInterceptor = new ApiTokenRequestInterceptor(apiToken);
    }

    @Bean
    public MailtrapApi mailtrapApi() {
        return createApi(MailtrapApi.class);
    }

    private <T> T createApi(Class<T> clazz) {
        return Feign.builder()
                .client(client)
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .logLevel(logLevel)
                .logger(logger)
                .options(options)
                .requestInterceptor(apiTokenRequestInterceptor)
                .target(clazz, url);
    }

    private Request.Options getOptions(MailtrapClientProperties properties) {
        return new Request.Options(
                properties.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS,
                properties.getReadTimeoutMillis(), TimeUnit.MILLISECONDS,
                true);
    }

    private Client defaultClient() {
        return new Client.Default(null, null);
    }
}

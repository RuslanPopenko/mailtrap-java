package com.railsware.mailtrap.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApiTokenRequestInterceptor implements RequestInterceptor {

    private static final String API_TOKEN_HEADER = "Api-Token";

    private final String apiToken;

    public ApiTokenRequestInterceptor(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(API_TOKEN_HEADER, apiToken);
    }
}

package com.railsware.mailtrap.config;

import com.railsware.mailtrap.error.MailtrapErrorResponse;
import com.railsware.mailtrap.error.MailtrapException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class MailtrapErrorDecoder implements ErrorDecoder {

    private final Decoder decoder;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            final var error = decoder.decode(response, MailtrapErrorResponse.class);
            return new MailtrapException(response.status(), (MailtrapErrorResponse) error);
        } catch (IOException e) {
            return new MailtrapException(response.status(), null);
        }
    }
}

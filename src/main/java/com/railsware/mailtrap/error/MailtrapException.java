package com.railsware.mailtrap.error;

import feign.FeignException;
import lombok.Getter;

@Getter
public class MailtrapException extends FeignException {

    private final MailtrapErrorResponse error;

    public MailtrapException(int status, MailtrapErrorResponse error) {
        super(status,
                error == null || error.getErrors().isEmpty()
                        ? "Mailtrap API error" : error.getErrors().getFirst());
        this.error = error;
    }
}

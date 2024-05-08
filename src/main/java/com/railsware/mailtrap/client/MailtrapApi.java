package com.railsware.mailtrap.client;

import com.railsware.mailtrap.error.MailtrapException;
import com.railsware.mailtrap.model.EmailSendRequest;
import com.railsware.mailtrap.model.EmailSendResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface MailtrapApi {

    @PostMapping(value = "/api/send", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    EmailSendResponse sendEmail(@RequestBody EmailSendRequest emailSendRequest) throws MailtrapException;

}

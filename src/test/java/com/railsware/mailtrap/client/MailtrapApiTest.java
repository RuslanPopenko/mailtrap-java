package com.railsware.mailtrap.client;

import com.railsware.mailtrap.config.MailtrapClientConfigurationTest;
import com.railsware.mailtrap.error.MailtrapErrorResponse;
import com.railsware.mailtrap.error.MailtrapException;
import com.railsware.mailtrap.model.AddressRequest;
import com.railsware.mailtrap.model.AttachmentRequest;
import com.railsware.mailtrap.model.EmailSendRequest;
import com.railsware.mailtrap.model.EmailSendResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@SpringBootTest(classes = MailtrapClientConfigurationTest.class)
@AutoConfigureWireMock(stubs = "classpath:mocks")
class MailtrapApiTest {

    @Autowired
    private MailtrapApi mailtrapApi;

    @Test
    void sendEmail_whenEmailWithTextAndHtml_thenSuccess() {
        var actual = mailtrapApi.sendEmail(createEmailSendRequest());

        assertThat(actual)
                .isNotNull()
                .isEqualTo(createExpected());
    }

    @Test
    void sendEmail_whenMailtrapError_thenThrowException() {
        final var mailtrapException = catchThrowableOfType(
                () -> mailtrapApi.sendEmail(createErrorRequest("eror@error.com")),
                MailtrapException.class);

        assertThat(mailtrapException)
                .isNotNull();

        assertThat(mailtrapException.getMessage())
                .isEqualTo("Email is not valid");
        assertThat(mailtrapException.getError())
                .isEqualTo(createMailtrapErrorResponse());

    }

    @Test
    void sendEmail_whenMailtrapErrorWhichCanNotBeDecoded_thenThrowException() {
        final var mailtrapException = catchThrowableOfType(
                () -> mailtrapApi.sendEmail(createErrorRequest("decode@error.com")),
                MailtrapException.class);

        assertThat(mailtrapException)
                .isNotNull();

        assertThat(mailtrapException.getMessage()).isEqualTo("Mailtrap API error");
        assertThat(mailtrapException.getError()).isNull();

    }

    private EmailSendRequest createEmailSendRequest() {
        return EmailSendRequest.builder()
                .from(createAddress("sales@example.com", "Example Sales Team"))
                .to(List.of(createAddress("john_doe@example.com", "John Doe")))
                .subject("Your Example Order Confirmation")
                .text("Congratulations on your order no. 1234")
                .html("<p>Congratulations on your order no. <strong>1234</strong>.</p>")
                .attachments(List.of(createAttachment()))
                .build();
    }

    private AddressRequest createAddress(String email, String name) {
        return AddressRequest.builder()
                .email(email)
                .name(name)
                .build();
    }

    private AttachmentRequest createAttachment() {
        return AttachmentRequest.builder()
                .type("text/html")
                .filename("index.html")
                .disposition("attachment")
                .content("PCFET0NUWVBFIGh0bWw+CjxodG1sIGxhbmc9ImVuIj4KCiAgICA8aGVhZD4KICAgICAgICA8bWV0YSBjaGFyc2V0PSJVVEYtOCI+CiAgICAgICAgPG1ldGEgaHR0cC1lcXVpdj0iWC1VQS1Db21wYXRpYmxlIiBjb250ZW50PSJJRT1lZGdlIj4KICAgICAgICA8bWV0YSBuYW1lPSJ2aWV3cG9ydCIgY29udGVudD0id2lkdGg9ZGV2aWNlLXdpZHRoLCBpbml0aWFsLXNjYWxlPTEuMCI+CiAgICAgICAgPHRpdGxlPkRvY3VtZW50PC90aXRsZT4KICAgIDwvaGVhZD4KCiAgICA8Ym9keT4KCiAgICA8L2JvZHk+Cgo8L2h0bWw+Cg==")
                .contentId("example")
                .build();
    }

    private static EmailSendResponse createExpected() {
        final var response = new EmailSendResponse();
        response.setSuccess(true);
        response.setMessageIds(List.of("0c7fd939-02cf-11ed-88c2-0a58a9feac02"));
        return response;
    }

    private EmailSendRequest createErrorRequest(String email) {
        return EmailSendRequest.builder()
                .from(createAddress(email, null))
                .build();
    }

    private MailtrapErrorResponse createMailtrapErrorResponse() {
        final var error = new MailtrapErrorResponse();
        error.setSuccess(false);
        error.setErrors(List.of("Email is not valid"));
        return error;
    }

}
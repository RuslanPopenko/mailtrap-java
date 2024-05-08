package com.railsware.mailtrap.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailSendRequest {

    AddressRequest from;

    List<AddressRequest> to;

    String subject;

    String text;

    String html;

    List<AttachmentRequest> attachments;

}

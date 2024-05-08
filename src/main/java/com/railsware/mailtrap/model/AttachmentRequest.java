package com.railsware.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentRequest {

    String content;

    String type;

    String filename;

    String disposition;

    @JsonProperty("content_id")
    String contentId;
}

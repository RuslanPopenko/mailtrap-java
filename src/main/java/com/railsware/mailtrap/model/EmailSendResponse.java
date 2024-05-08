package com.railsware.mailtrap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailSendResponse {

    boolean success;

    @JsonProperty("message_ids")
    List<String> messageIds;

}

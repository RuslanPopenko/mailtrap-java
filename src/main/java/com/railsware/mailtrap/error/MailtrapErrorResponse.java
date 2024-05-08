package com.railsware.mailtrap.error;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailtrapErrorResponse {

    boolean success;

    List<String> errors = Collections.emptyList();

}

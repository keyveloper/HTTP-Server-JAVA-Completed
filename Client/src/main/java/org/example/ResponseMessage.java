package org.example;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseMessage {
    private final String protocol;
    private final String statusCode;
    private final String statusPhrase;
    private final Map<String, String> headerMap;
    private final Map<String, Object> bodyMap;
}

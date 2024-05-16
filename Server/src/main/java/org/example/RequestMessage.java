package org.example;

import lombok.Data;

import java.util.Map;

@Data
public class RequestMessage {
    private final String method;
    private final String uri;
    private final String protocol;
    private final Map<String, String> headerMap;
    private final Map<String, Object> bodyMap;
}

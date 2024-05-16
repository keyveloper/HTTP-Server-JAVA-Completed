package org.example;

import lombok.Data;

@Data
public class Response {
    private final String protocol;
    private final StatusCode statusCode;
    private String serverVersion;
    private int bodyLength;
    private String bodyType;
    private final String responseJsonBody;
}
package org.example;

import lombok.Data;

@Data
public class Response {
    private final String protocol = "HTTP/1.1";
    private final StatusCode statusCode;
    private int bodyLength;
    private String bodyType;
    private byte[] body;
}
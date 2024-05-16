package org.example;

import lombok.Data;

@Data
public class Request {
    private final String method;
    private final String uri;
    private final String protocol;
    private final String hostName;
    private String contentType;
    private String contentLength;
    private String requestJsonBody;
}

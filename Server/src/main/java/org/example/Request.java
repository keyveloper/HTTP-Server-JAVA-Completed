package org.example;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.Socket;
import java.util.Map;

@Data
@NoArgsConstructor
public class Request {
    private String method;
    private String uri;
    private String protocol;
    private Map<String, String> headerMap;
    private String body;
}

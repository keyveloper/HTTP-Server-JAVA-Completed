package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
public class RequestHandler {
    private final Server owner;

    public byte[] handleRequest(RequestMessage requestMessage) {
        switch (requestMessage.getMethod()) {
            case "GET" -> {
                return handleGet(requestMessage.getUri());
            }
            case "POST" -> {
                return handlePost(requestMessage.getUri(), requestMessage.getBodyMap());
            }
            case "Delete" -> {
                return handleDelete(requestMessage.getUri());
            }
            default -> {
                return null;
            }
        }
    }

    private byte[] handleGet(String uri) {
        if (uri.startsWith("/time")) {

        }
    }

    private byte[] handlePost(String uri, Map<String, Object> bodyMap) {

    }

    private byte[] handleDelete(String uri) {

    }
}
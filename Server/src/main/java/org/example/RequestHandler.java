package org.example;

import lombok.Data;

import java.util.Map;

@Data
public class RequestHandler {
    private final Server owner;

    public byte[] handleRequest(RequestMessage requestMessage) {
        switch (requestMessage.getMethod()) {
            case "GET" -> handleGet(requestMessage.getUri());
            case "POST" -> handlePost(requestMessage.getUri(), requestMessage.getBodyMap());
            case "Delete" -> handleDelete(requestMessage.getUri());
        }
    }

    private byte[] handleGet(String uri) {

    }

    private byte[] handlePost(String uri, Map<String, Object> bodyMap) {

    }

    private byte[] handleDelete(String uri) {

    }
}
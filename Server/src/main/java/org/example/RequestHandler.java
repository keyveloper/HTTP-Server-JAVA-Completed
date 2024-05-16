package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@AllArgsConstructor
public class RequestHandler {
    private final Server server;

    public void handleRequest(RequestMessage requestMessage) {
        switch (requestMessage.getMethod()) {
            case "GET" -> handleGet(requestMessage);

            case "POST" -> handlePost(requestMessage.getUri(), requestMessage.getBodyMap());

            case "Delete" -> handleDelete(requestMessage.getUri());
        }
    }

    private void handleGet(RequestMessage requestMessage) {
        if (requestMessage.getUri().startsWith("/time")) {
            sendTimeResponse(requestMessage.getProtocol());
        }
    }

    private void handlePost(String uri, Map<String, Object> bodyMap) {

    }

    private void handleDelete(String uri) {

    }

    private void sendTimeResponse(String protocol) {
        String body = getNowTime();
        Response response = new Response(protocol, StatusCode.OK, body);
        response.setBodyLength(body.length());
        response.setBodyType("application/json");

        byte[] responseBytes = HttpResponseMaker.makePacket(response);
        server.getResponseSender().sendResponse(responseBytes);
    }

    private String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(now);
        } catch (JsonProcessingException e) {
            System.out.println("getNowTime error: " + e.getMessage());
        }
    }
}
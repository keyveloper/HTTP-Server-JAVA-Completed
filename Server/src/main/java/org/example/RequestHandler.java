package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
public class RequestHandler {
    private final ClientHandler clientHandler;

    public void handleRequest(RequestMessage requestMessage) {
        System.out.println("handle Request start!! \n" + requestMessage);
        System.out.println("handle" + requestMessage.getMethod() + " " + requestMessage.getUri());
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
        System.out.println("start to send Time response!! \n");
        String body = getNowTime();
        if (body == null) {
            Response response = new Response(protocol, StatusCode.SERVICE_UNAVAILABLE, null);
            clientHandler.sendHttpResponse(response);
            return;
        }
        Response response = new Response(protocol, StatusCode.OK, body);
        response.setBodyLength(body.length());
        response.setBodyType("application/json");
        System.out.println("Response made: " + response + "\n");
        System.out.println("Response JsonBody: " + body);
        clientHandler.sendHttpResponse(response);
    }

    private String getNowTime() {
        try {
            LocalDateTime now = LocalDateTime.now();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(now);
        } catch (JsonProcessingException e) {
            System.out.println("getNowTime error: " + e.getMessage());
        }
        return null;
    }
}
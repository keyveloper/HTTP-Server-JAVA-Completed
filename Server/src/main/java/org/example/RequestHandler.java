package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class RequestHandler {

    public Response handleRequest(Request request) {
        System.out.println("start to handleRequest!!");
        switch (request.getMethod()) {
            case "GET" -> {
                return handleGet(request.getUri());
            }
            case "POST" -> handlePost(request.getUri(), request.getBodyBytes());
            case "DELETE" -> handleDelete(request.getUri());


        }
        // send error status!
        return null;
    }

    private Response handleGet(String uri) {
        if (uri.startsWith("/time")) {
            return responseTime();
        }
    }

    private Response handlePost(String uri, byte[] body) {

    }

    private Response handleDelete(String uri) {

    }

    private Response responseTime() {
        System.out.println("start to send Time response!! \n");
        Response response;
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("now: " + formattedNow);

        // Json
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("currentTime", formattedNow);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponse = objectMapper.writeValueAsString(responseMap);

            response = new Response(StatusCode.OK);
            response.setBodyLength(jsonResponse.length());
            response.setBodyType("application/json");
            response.setJsonResponseBody(jsonResponse);
            return response;
        } catch (JsonProcessingException e) {
            System.out.println("Request Handler - responseTime error: " + e.getMessage());
        }
        return null;
    }
}
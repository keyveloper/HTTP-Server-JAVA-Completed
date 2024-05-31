package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class RequestHandler {
    public Response handleRequest(Request request) {
        System.out.println("start to handleRequest!!");
        switch (request.getMethod()) {
            case "GET" -> {
                return handleGet(request.getUri());
            }
            case "POST" -> {
                return handlePost(request.getUri(), request.getBody());
            }
            case "DELETE" -> {
                return handleDelete(request.getUri());
            }

            default -> {
                return new Response(StatusCode.BAD_REQUEST);
            }
        }
    }


    private Response handleGet(String uri) {
        System.out.println("<handleGET>\nuri: " + uri + "\n");
        if (uri.equals("/time")) {
            return responseGetTime();
        }

        if (uri.equals("/textall")) {
            System.out.println("response All text \nuri: " + uri);
            return responseGetAllText();
        }

        if (uri.startsWith("/text") && uri.substring(5).startsWith("/")) {
            return getText(uri.substring(6));
        }

        if (uri.startsWith("/image") && uri.substring(5).startsWith("/")) {
            return responseGetImage(uri.substring(6));
        }
        return new Response(StatusCode.NO_CONTENT);
    }

    private Response responseGetImage(String parameter) {
        byte[] imageBytes = DataBaseManager.getImage(Integer.parseInt(parameter));
        if (imageBytes == null) {
            return new Response(StatusCode.BAD_REQUEST);
        }
        Response response = new Response(StatusCode.OK);
        response.setBodyType("image/jpeg");
        response.setBodyLength(imageBytes.length);
        response.setBody(imageBytes);
        return response;
    }

    private Response getText(String key) {
        // check valid parameter path
        String body = DataBaseManager.getText(key);
        System.out.println("<responseGetText>\ntextKey: " + key + "\nresult: " +  body);
        if (body == null) {
            return new Response(StatusCode.NOT_FOUND);
        }
        Response response = new Response(StatusCode.OK);
        response.setBodyLength(body.length());
        response.setBodyType("text/plain");
        response.setBody(body.getBytes());
        return response;
    }

    private Response responseGetAllText() {
        System.out.println("\n<<start get All>>\n");
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> textsMap = DataBaseManager.getTextAll();
        if (textsMap.isEmpty()) {
            return new Response(StatusCode.NO_CONTENT);
        }

        try {
            String jsonTexts = objectMapper.writeValueAsString(textsMap);
            System.out.println("<getAll>\njsonBody: " + jsonTexts);
            Response response = new Response(StatusCode.OK);
            response.setBodyLength(jsonTexts.length());
            response.setBodyType("application/json");
            response.setBody(jsonTexts.getBytes());

            return response;
        } catch (JsonProcessingException e) {
            System.out.println("Request Handler: responseGetAllText() err" + e.getMessage());
        }
        return new Response(StatusCode.SERVICE_UNAVAILABLE);
    }

    private Response handlePost(String uri, byte[] body) {
        if (uri.startsWith("/text") && uri.substring(5).startsWith("/")) {
            return postText(uri.substring(6), body);
        }

        if (uri.equals("/image")) {
            return postImage(body);
        }
        return new Response(StatusCode.NOT_FOUND);
    }

    private Response postImage(byte[] body) {
        if (DataBaseManager.putImage(body)) {
            return new Response(StatusCode.CREATED);
        }
        return new Response(StatusCode.SERVICE_UNAVAILABLE);
    }

    private Response postText(String parameter, byte[] body) {
        if (DataBaseManager.putText(parameter, new String(body))) {
            return new Response(StatusCode.CREATED);
        }
        return new Response(StatusCode.SERVICE_UNAVAILABLE);
    }

    private Response handleDelete(String uri) {
        if (uri.startsWith("/text") && uri.substring(5).startsWith("/")) {
            return deleteText(uri.substring(6));
        }
        return new Response(StatusCode.NOT_FOUND);
    }

    private Response deleteText(String key) {
        if (DataBaseManager.deleteText(key)) {
            return new Response(StatusCode.ACCEPTED);
        }
        return new Response(StatusCode.NOT_FOUND);
    }

    private Response responseGetTime() {
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
            response.setBody(jsonResponse.getBytes());
            return response;
        } catch (JsonProcessingException e) {
            System.out.println("Request Handler - responseTime error: " + e.getMessage());
        }
        return null;
    }
}
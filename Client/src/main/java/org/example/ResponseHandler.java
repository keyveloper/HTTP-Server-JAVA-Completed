package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseHandler {
    public void handleResponse(ResponseMessage responseMessage) {
        System.out.println("[ResponseHandler]\n");
        System.out.println("start to handle Response\nresponseMessage: \n" + responseMessage);

        switch (responseMessage.getStatusCode()) {
            case "ok" -> {
                for (String key: responseMessage.getBodyMap().keySet()) {
                    System.out.println(key + responseMessage.getBodyMap().get(key) + "\n");
                }
            }
        }
    }
}

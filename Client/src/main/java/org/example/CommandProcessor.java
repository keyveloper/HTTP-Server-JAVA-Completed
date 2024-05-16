package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandProcessor {
    private final Client client;
    public Request extractRequest(String command) {
        if (command.startsWith("get ") || command.startsWith("GET ")) {
            String uri = command.substring(4);
            return handleGetCommand(uri);
        }

        return null;
    }

    private Request handleGetCommand(String command) {
        if (command.startsWith("/time")) {
            return handleGetTime();
        }
        return null;
    }

    private  Request handleGetTime() {
        return new Request("GET", "/time", "HTTP/1.1", client.getHostName());
    }
}

package org.example;

public class CommandProcessor {
    public static ProcessedCommand extractRequest(String command) {
        if (command.startsWith("get ") || command.startsWith("GET ")) {
            String uri = command.substring(4);
            return new ProcessedCommand("GET", uri);
        }

        return null;
    }
}

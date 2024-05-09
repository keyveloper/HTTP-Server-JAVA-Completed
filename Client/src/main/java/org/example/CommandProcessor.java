package org.example;

public class CommandProcessor {
    public static Request extractRequest(String command) {
        if (command.startsWith("/gettime")) {
            return new Request(RequestCode.GET_TIME);
        }

        if (command.startsWith("/posttext")) {
            Request request = new Request(RequestCode.POST_TEXT);
        }

        return null;
    }
}

package org.example;

import lombok.Data;

@Data
public class ProcessedCommand {
    private final String method;
    private final String uri;
    private String jsonBody = null;
}

package org.example;

import lombok.Data;

@Data
public class Request {
    private final RequestCode requestCode;
    private String textId;    private String body;
}

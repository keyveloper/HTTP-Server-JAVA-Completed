package org.example;

import lombok.Data;

@Data
public class ResponseReader implements Runnable{
    private final Client owner;
    @Override
    public void run() {

    }
}

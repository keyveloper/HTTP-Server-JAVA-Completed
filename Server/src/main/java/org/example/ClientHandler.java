package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

@AllArgsConstructor
public class ClientHandler implements Runnable{
    private final Socket client;

    @Override
    public void run() {
        RequestReader requestReader = new RequestReader(client);
        try {
            while (true) {
                Request request = requestReader.readRequest();
                if (!processRequest(request)) {
                    client.close();

                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client Handler Error: " + e.getMessage());
        }
    }

    private int processRequest(Request request) {
        if (!Objects.equals(request.getProtocol(), "HTTP/1.1") || request.getMethod() == null || request.getUri() == null) {
            return 0;
        } else {
            response(request);
            return 1;
        }
    }

    private void response(Request request) {
        RequestHandler requestHandler =  new RequestHandler();
        Response response = requestHandler.handleRequest(request);
        sendHttpResponse(response);
    }

    public void sendHttpResponse(Response response) {
        ResponseSender responseSender = new ResponseSender(client);
        // send ResponsePacket with http header
        responseSender.sendHttpResponse(HttpResponseMaker.makePacket(response));
    }
}

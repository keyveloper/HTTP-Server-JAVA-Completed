package org.example;

import lombok.AllArgsConstructor;

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
                if (!isValidRequest(request)) {
                    client.close();
                    break;
                }
                response(request);
            }
        } catch (IOException e) {
            System.out.println("Client Handler Error: " + e.getMessage());
        }
    }

    private boolean isValidRequest(Request request) {
        return Objects.equals(request.getProtocol(), "HTTP/1.1") && request.getMethod() !=
                null && request.getUri() != null;
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

package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Socket;

@AllArgsConstructor
public class ClientHandler implements Runnable{
    private final Socket client;


    @Override
    public void run() {
        RequestReader requestReader = new RequestReader(client);
        while (true) {
            RequestMessage requestMessage = requestReader.readRequest();
            processRequest(requestMessage);
        }
    }

    private void processRequest(RequestMessage requestMessage) {
        RequestHandler requestHandler = new RequestHandler(this);
        requestHandler.handleRequest(requestMessage);
    }

    public void sendHttpResponse(Response response) {
        ResponseSender responseSender = new ResponseSender(client);
        // send ResponsePacket with http header
        responseSender.sendHttpResponse(HttpResponseMaker.makePacket(response));
    }
}

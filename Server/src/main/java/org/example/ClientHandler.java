package org.example;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

@AllArgsConstructor
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final RequestHandler requestHandler;
    @Override
    public void run() {
        try {
            while (true) {
                RequestReader requestReader = new RequestReader(clientSocket);
                Request request = requestReader.readRequest();
                System.out.println("request: " + request);
                if (!isValidRequest(request)) {
                    clientSocket.close();
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
        Response response = requestHandler.handleRequest(request);
        System.out.println("\n[Response made]\nresponse: " + response);
        if (response == null) {
            sendHttpResponse(new Response(StatusCode.NOT_FOUND));
            return;
        }
        sendHttpResponse(response);
    }

    public void sendHttpResponse(Response response) {
        ResponseSender responseSender = new ResponseSender(clientSocket);
        // send ResponsePacket with http header
        responseSender.sendHttpResponse(HttpResponseMaker.makePacket(response));
    }
}

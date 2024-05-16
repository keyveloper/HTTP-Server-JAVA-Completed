package org.example;

import lombok.Data;

@Data
public class HttpRequestMaker {
    public static byte[] makeHttpRequest(Request request) {
        StringBuilder requestString = new StringBuilder();
        // add essential line
        requestString.append(request.getMethod()).append(" ").append(request.getUri()).append(" ")
                .append(request.getProtocol()).append("\r\n")
                .append("Host: ").append(request.getHostName()).append("\r\n");
        if (request.getRequestJsonBody() != null) {
            requestString.append("Content-Type: ").append(request.getContentType()).append("\r\n")
                    .append("Content-Length: ").append(request.getContentLength()).append("\r\n");
        }
        requestString.append(request.getRequestJsonBody());

        return requestString.toString().getBytes();
    }
}

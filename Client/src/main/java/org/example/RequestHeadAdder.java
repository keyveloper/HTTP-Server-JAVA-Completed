package org.example;

import lombok.Data;

@Data
public class RequestHeadAdder {
    public static byte[] addHeader(String method, String uri, String hostName, String jsonBody) {
        StringBuilder request = new StringBuilder();
        request.append(method).append(" ").append(uri).append(" HTTP/1.1\r\n")
                .append("Host: ").append(hostName).append("\r\n");
        if (jsonBody != null) {
            request.append("Content-Length: ").append(jsonBody.length()).append("\r\n");
        }
        request.append(jsonBody);

        return request.toString().getBytes();
    }
}

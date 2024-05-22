package org.example;

public class HttpResponseMaker {
    public static byte[] makePacket(Response response) {
        StringBuilder responseString = new StringBuilder();
        // status Line
        responseString.append(response.getProtocol()).append(" ").append(response.getStatusCode().getCode()).append(" ")
                .append(response.getStatusCode().getReasonPhrase()).append("\r\n")
        // essential Body
                .append("Date: ").append(new java.util.Date()).append("\r\n");
        if (response.getJsonResponseBody() != null){
            responseString.append("Content-Type: ").append(response.getBodyType()).append("\r\n")
                    .append("Content-Length: ").append(response.getBodyLength()).append("\r\n")
                    .append("Connection: close\r\n")
                    .append("\r\n");
            responseString.append(response.getJsonResponseBody());
            return responseString.toString().getBytes();
        }
        responseString.append("Connection: close\r\n").append("\r\n");

        return responseString.toString().getBytes();
    }
}

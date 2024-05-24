package org.example;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class HttpResponseMaker {
    public static byte[] makePacket(Response response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        StringBuilder responseString = new StringBuilder();
        // status Line
        responseString.append(response.getProtocol()).append(" ").append(response.getStatusCode().getCode()).append(" ")
                .append(response.getStatusCode().getReasonPhrase()).append("\r\n")
        // essential Body
                .append("Date: ").append(dateFormat.format(new java.util.Date())).append("\r\n");
        if (response.getBody() != null){
            responseString.append("Content-Type: ").append(response.getBodyType()).append("\r\n")
                    .append("Content-Length: ").append(response.getBodyLength()).append("\r\n")
                    .append("\r\n");
            responseString.append(response.getBody());
            return responseString.toString().getBytes();
        }
        responseString.append("\r\n");

        return responseString.toString().getBytes();
    }
}

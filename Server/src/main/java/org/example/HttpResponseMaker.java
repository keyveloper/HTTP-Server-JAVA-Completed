package org.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class HttpResponseMaker {
    public static byte[] makePacket(Response response) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // status Line
            String statusLine = response.getProtocol() + " " + response.getStatusCode().getCode() + " "
                    + response.getStatusCode().getReasonPhrase() + "\r\n";
            byteArrayOutputStream.write(statusLine.getBytes());

            // Essential Header
            String date = "Date: " + dateFormat.format(new java.util.Date()) + "\r\n";
            byteArrayOutputStream.write(date.getBytes());

            // optional body
            if (response.getBody() != null) {
                String contentType = "Content-Type: " + response.getBodyType() + "\r\n";
                String contentLength = "Content-Length: " + response.getBody().length + "\r\n";
                byteArrayOutputStream.write(contentType.getBytes());
                byteArrayOutputStream.write(contentLength.getBytes());
                byteArrayOutputStream.write("\r\n".getBytes());
                byteArrayOutputStream.write(response.getBody());
            } else {
                byteArrayOutputStream.write("\r\n".getBytes());
            }

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            System.out.println("HttpResponseMaker error: " + e.getMessage());
        }
        return null;
    }
}

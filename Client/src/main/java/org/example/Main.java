package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client("my client");
        client.run();

        try {

        while (1) {
                String command = bufferedReader.readLine();
                client.processCommand(command);
            }
        }
        catch (IOException e) {
            System.out.println("BufferReader IOException: " + e.getMessage());
        }
    }
}
package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class TextManager {
    private final HashMap<String, String> textMap = new HashMap<>();
    private final Object textLock = new Object();

    public boolean put(String key, String value) {
        synchronized (textLock) {
            if (textMap.containsKey(key)) {
                return false;
            }
            textMap.put(key, value);
            return true;
        }
    }

    public String get(String key) {
        synchronized (textLock) {
            if (textMap.containsKey(key)) {
                return textMap.get(key);
            }
        }
        return null;
    }

    public boolean remove(String key) {
        synchronized (textMap) {
            if (textMap.containsKey(key)) {
                textMap.remove(key);
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return textMap.isEmpty();
    }

    public String getAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData;
        synchronized (textLock) {
            try {
                jsonData = objectMapper.writeValueAsString(textMap);
                return jsonData;
            } catch (JsonProcessingException e) {
                System.out.println("Text Manager err: " + e.getMessage());
            }
        }
        return null;
    }

    public void print() {
        for (String key: textMap.keySet()) {
            System.out.println(key + ": " + textMap.get(key));
        }
    }




}

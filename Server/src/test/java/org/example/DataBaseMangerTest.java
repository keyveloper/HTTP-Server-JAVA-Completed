package org.example;

import org.junit.jupiter.api.Test;

public class DataBaseMangerTest {
    @Test
    public void connectionTest() {
        // Given
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        // when
        dataBaseManager.putText("key1", "value1");
    }
}

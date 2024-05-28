package org.example;

import lombok.Getter;

import java.sql.*;

@Getter
public class DataBaseManager {
    private static DataBaseManager instance;
    private Connection connection;
    private static final Object dataBaseLock = new Object();
    private final String url = "jdbc:mariadb://localhost:3306/http";
    private final String user = "root";
    private final String password = "1234";

    private DataBaseManager() {
        System.out.println("url: " + url);
        startConnect();
    }
    private void startConnect() {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("connecting success");
        } catch (SQLException e) {
            System.out.println("DataBaseManger - sql err: " + e.getMessage());
        }
    }

    public static DataBaseManager getInstance() {
        synchronized (dataBaseLock) {
            if (instance == null) {
                instance = new DataBaseManager();
            }
        }
        return instance;
    }
    public void putText(String key, String value) {
        String sql = "INSERT INTO texts (ID, value) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, key);
                preparedStatement.setString(2, value);

                int effectedRows = preparedStatement.executeUpdate();

                if (effectedRows > 0) {
                    System.out.println("A new row has been inserted successfully.");
                } else {
                   System.out.println("A new row insertion failed");
                }
        } catch (SQLException e) {
            System.out.println("DataBase Manger: putText err: " + e.getMessage());
        }
    }

    public void putImage(String imgName, byte[] bytes) {

    }

    public void deleteId(String key) {

    }

    public String getValue(String key) {
        return null;
    }
}

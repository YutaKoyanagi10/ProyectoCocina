package org.proyectococinav2.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseConfig {
    private static final String URL = "jdbc:sqlite:proyectococina.db";
    private static DataBaseConfig instance;
    private Connection connection;

    private DataBaseConfig() {}

    public static DataBaseConfig getInstance() {
        if (instance == null) {
            instance = new DataBaseConfig();
        }
        return instance;
    }

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error disconnecting from the database", e);
        }
    }

    public Connection getConnection() {
        connect();
        return connection;
    }

    public void createTables() {
        connect();
        String[] tableDefinitions = {
            "CREATE TABLE IF NOT EXISTS recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "instructions TEXT NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");",
            "CREATE TABLE IF NOT EXISTS ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "supplier_id INTEGER," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE" +
            ");",
            "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                "recipe_id INTEGER," +
                "ingredient_id INTEGER," +
                "serving_per_person REAL NOT NULL CHECK (serving_per_person > 0)," +
                "unit VARCHAR(10) NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (recipe_id, ingredient_id)," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE" +
            ");",
            "CREATE TABLE IF NOT EXISTS suppliers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "contact_info TEXT NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");",
        };

        try {
            for (String tableDefinition : tableDefinitions) {
                try (PreparedStatement pstmt = connection.prepareStatement(tableDefinition)) {
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables", e);
        } finally {
            disconnect();
        }
    }

}
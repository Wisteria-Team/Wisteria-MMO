package dev.beabueno.wisteriammo.persistence;

import dev.beabueno.wisteriammo.WisteriaMMO;
import dev.beabueno.wisteriammo.config.DatabaseConfig;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

@Log
public class Database {

    private static Connection connection;

    private static void connect() throws SQLException {
        DatabaseConfig dbConfig = WisteriaMMO.getPluginConfig().getDatabaseConfig();
        Database.connection = DriverManager.getConnection(dbConfig.getUri(), dbConfig.getUser(), dbConfig.getPw());
    }

    public static Connection getConnection() {
        if (!isConnected()) {
            try {
                connect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Database.connection;
    }

    public static boolean isConnected() {
        try {
            return Database.connection != null && !Database.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    public static void disconnect() {
        try {
            if (isConnected()) {
                Database.connection.close();
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }
}

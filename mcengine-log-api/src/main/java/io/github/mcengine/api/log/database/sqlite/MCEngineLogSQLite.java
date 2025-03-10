package io.github.mcengine.api.log.database.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * The {@code MCEngineLogSQLite} handles SQLite database operations for logging.
 */
public class MCEngineLogSQLite {

    private Plugin plugin;
    private String dbPath;
    private Connection connection;

    public MCEngineLogSQLite(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.dbPath = config.getString("dbPath", "logs.sqlite");
    }

    /**
     * Establishes a connection to the SQLite database.
     * @return Connection object or null if the connection fails.
     */
    public Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                File databaseFile = new File(plugin.getDataFolder(), dbPath);
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
                System.out.println("Connected to SQLite database: " + databaseFile.getAbsolutePath());
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to SQLite database: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the SQLite database connection.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("SQLite database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close SQLite database connection: " + e.getMessage());
        }
    }

    /**
     * Checks if the connection to the database is active.
     * @return true if connected, false otherwise.
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

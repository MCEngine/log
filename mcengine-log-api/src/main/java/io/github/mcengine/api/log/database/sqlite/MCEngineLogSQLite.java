package io.github.mcengine.api.log.database.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
     * Creates the necessary database tables if they do not exist.
     */
    public void initialize() {
        if (!isConnected()) {
            connect();
        }
        
        if (connection == null) {
            plugin.getLogger().severe("Database connection is null, initialization failed.");
            return;
        }

        String sql = "CREATE TABLE IF NOT EXISTS logs (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "uuid_player VARCHAR(36), " +
                     "type TEXT, " +
                     "message TEXT, " +
                     "timestamp TEXT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Database initialized successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
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

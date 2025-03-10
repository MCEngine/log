package io.github.mcengine.api.log.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * The {@code MCEngineLogMySQL} handles MySQL database operations for logging.
 */
public class MCEngineLogMySQL {

    private Plugin plugin;
    private String host, port, ssl, database, username, password;
    private Connection connection;

    /**
     * Constructs an instance of MCEngineLogMySQL.
     * This initializes the MySQL database configuration using the plugin's config file.
     * 
     * @param plugin The plugin instance used to retrieve configuration settings.
     */
    public MCEngineLogMySQL(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();

        // Retrieves MySQL database connection details from the configuration file.
        this.host = config.getString("mysql.host", "localhost");     // Default: localhost
        this.port = config.getString("mysql.port", "3306");          // Default: 3306
        this.ssl = config.getString("mysql.ssl", "false");           // Default: false (No SSL)
        this.database = config.getString("mysql.database", "logs");  // Default: logs
        this.username = config.getString("mysql.username", "root");  // Default: root
        this.password = config.getString("mysql.password", "");      // Default: empty password
    }

    /**
     * Establishes a connection to the MySQL database.
     * @return Connection object or null if the connection fails.
     */
    public Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl + "&autoReconnect=true";
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to MySQL database: " + database);
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database: " + e.getMessage());
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
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "uuid_player VARCHAR(36), " +
                     "type TEXT, " +
                     "message TEXT, " +
                     "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Database initialized successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Closes the MySQL database connection.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("MySQL database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close MySQL database connection: " + e.getMessage());
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

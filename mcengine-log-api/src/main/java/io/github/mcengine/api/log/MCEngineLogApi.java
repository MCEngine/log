package io.github.mcengine.api.log;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import java.util.logging.Logger;

/**
 * Utility class for handling database initialization in the MCEngineLog plugin.
 * This class loads the database configuration and initializes the appropriate database handler
 * based on the configured database type (MySQL or SQLite).
 */
public class MCEngineLogApi {
    private final Object databaseInstance;
    private final Plugin plugin;

    /**
     * Constructs an instance of {@code MCEngineLogApi} with the specified plugin reference.
     *
     * @param plugin The Bukkit plugin instance that owns this API.
     */
    public MCEngineLogApi(Plugin plugin) {
        this.plugin = plugin;
        this.databaseInstance = initialize();
    }

    /**
     * Initializes the database connection based on the configuration settings.
     * Supports MySQL and SQLite database types.
     *
     * @return An instance of the appropriate database handler.
     * @throws RuntimeException if an unsupported database type is specified or if initialization fails.
     */
    private Object initialize() {
        // Load the configuration file
        FileConfiguration config = plugin.getConfig();

        // Retrieve database type from config (default to SQLite)
        String dbType = config.getString("dbType", "sqlite");

        // Log the selected database type to the console
        getLogger().info("Database Type: " + dbType);

        Object tempInstance;
        try {
            if (dbType.equalsIgnoreCase("mysql")) {
                // Initialize MySQL database handler
                tempInstance = MCEngineApiUtil.initialize(
                        "io.github.mcengine.api.log.database.mysql.MCEngineLogApiMySQL",
                        plugin
                );
            } else if (dbType.equalsIgnoreCase("sqlite")) {
                // Initialize SQLite database handler
                tempInstance = MCEngineApiUtil.initialize(
                        "io.github.mcengine.api.log.database.sqlite.MCEngineLogApiSQLite",
                        plugin
                );
            } else {
                throw new IllegalArgumentException("Unsupported SQL type: " + dbType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading database implementation: " + e.getMessage(), e);
        }
        return tempInstance;
    }

    /**
     * Retrieves the logger instance from the plugin.
     *
     * @return The logger associated with the plugin.
     */
    private Logger getLogger() {
        return plugin.getLogger();
    }
}

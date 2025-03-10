package io.github.mcengine.api.log.database.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/* 
 * The {@code MCEngineLogSQLite} is designed for SQLite functions to manage and store logs in logs.sqlite.
 */
public class MCEngineLogSQLite {

    private Plugin plugin;
    private String dbPath;

    public MCEngineLogSQLite(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.dbPath = config.getString("dbPath", "logs.sqlite");
    }
}
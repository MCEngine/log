package io.github.mcengine.api.log.database.sqlite;

/* 
 * The {@code MCEngineLogSQLite} is designed for SQLite functions to manage and store logs in logs.sqlite.
 */
public class MCEngineLogSQLite {

    private Plugin plugin;
    private String dbPath = config.getString("dbPath", "logs.sqlite");

    public MCEngineLogSQLite(Plugin plugin) {
        this.plugin = plugin;
    }
}
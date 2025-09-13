package ru.korshun.importantz.api.database;

import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.utils.ConfigUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    protected static Connection getConnection() {
        if(connection == null) {
            try {
                String databaseType = ConfigUtils.Database.getDatabaseType();
                if(!databaseType.equalsIgnoreCase("sqlite") && !databaseType.equalsIgnoreCase("mysql")) {
                    databaseType = "sqlite";
                }
                if (databaseType.equalsIgnoreCase("sqlite")) {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + ImportantZ.getInstance().getDataFolder().getAbsolutePath() + "/importantz.db");
                }
                else if(databaseType.equalsIgnoreCase("mysql")) {
                    connection = DriverManager.getConnection("jdbc:mysql://" + ConfigUtils.Database.getHost() + ":" + ConfigUtils.Database.getPort() + "/" + ConfigUtils.Database.getName(), ConfigUtils.Database.getUsername(), ConfigUtils.Database.getPassword());
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}

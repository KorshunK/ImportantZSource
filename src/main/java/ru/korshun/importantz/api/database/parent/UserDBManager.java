package ru.korshun.importantz.api.database.parent;

import ru.korshun.importantz.api.database.DatabaseManager;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

public class UserDBManager extends DatabaseManager {
    public static UserDBManager INSTANCE = new UserDBManager();
    private Statement stmt;

    public UserDBManager() {
        try {
            stmt = getConnection().createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users(id INTEGER AUTO_INCREMENT, uuid TEXT, username TEXT, custom_nickname TEXT, homes TEXT, lastLoginTime bigint, lastLogoffTime bigint);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setup(User user) {
        new Thread(() -> {
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user.getName() + "'");
                if (rs.next()) {
                    return;
                } else {
                    stmt.execute("INSERT INTO users(username, uuid) VALUES('" + user.getName() + "', '" + user.getUUID().toString() + "');");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setLastLoginTime(User user, long time) {
//        new Thread(() -> {
            try {
                stmt.execute("UPDATE users SET lastLoginTime = '" + time + "' WHERE username = '" + user.getName() + "' AND uuid = '" + user.getUUID().toString() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }).start();
    }

    public void setLastLogoffTime(User user, long time) {
        new Thread(() -> {
            try {
                stmt.execute("UPDATE users SET lastLogoffTime = '" + time + "' WHERE username = '" + user.getName() + "' AND uuid = '" + user.getUUID().toString() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public long getLastLoginTime(OfflineUser user) {
        long time = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user.getName() + "' AND uuid = '" + user.getUUID().toString() + "'");
            if (rs.next()) {
                time = rs.getLong("lastLoginTime");
            }
        } catch (SQLException e) {
        }
        return time;
    }

    public long getLastLogoffTime(OfflineUser user) {
        long time = -1;
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user.getName() + "' AND uuid = '" + user.getUUID().toString() + "'");
            if (rs.next()) {
                time = rs.getLong("lastLogoffTime");
            }
        } catch (SQLException e) {
        }
        return time;
    }
}

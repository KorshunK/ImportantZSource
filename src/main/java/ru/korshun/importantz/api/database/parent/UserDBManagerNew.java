package ru.korshun.importantz.api.database.parent;

import ru.korshun.importantz.api.database.DatabaseManager;
import ru.korshun.importantz.api.user.OfflineUser;
import ru.korshun.importantz.api.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class UserDBManagerNew extends DatabaseManager{
    public static UserDBManager INSTANCE = new UserDBManager();
    private final Object dbLock = new Object(); // Объект блокировки
    private Statement stmt;

    public UserDBManagerNew() {
        try {
            stmt = getConnection().createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users(id INTEGER AUTO_INCREMENT, uuid TEXT, username TEXT, custom_nickname TEXT, homes TEXT, lastLoginTime BIGINT, lastLogoffTime BIGINT);");
            stmt.close();
        } catch (SQLException e) {

        }
    }

    public void setup(User user) {
        new Thread(() -> {
            synchronized (dbLock) { // Синхронизированный блок
                try (PreparedStatement checkStmt = getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?")) {
                    checkStmt.setString(1, user.getUUID().toString());
                    ResultSet rs = checkStmt.executeQuery();
                    if (!rs.next()) {
                        try (PreparedStatement insertStmt = getConnection().prepareStatement("INSERT INTO users(username, uuid) VALUES(?, ?);")) {
                            insertStmt.setString(1, user.getName());
                            insertStmt.setString(2, user.getUUID().toString());
                            insertStmt.executeUpdate();
                            insertStmt.close();
                            getLogger().info("Создана запись в БД для пользователя: " + user.getName());
                        } catch (SQLException e) {
                            getLogger().log(Level.SEVERE, "Ошибка при создании записи в БД для пользователя " + user.getName() + ":", e);
                        }
                    }
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Ошибка при проверке существования пользователя " + user.getName() + " в БД:", e);
                }
            }
        }).start();
    }

    public void setLastLoginTime(User user, long time) {
        new Thread(() -> {
            synchronized (dbLock) { // Синхронизированный блок
                try (PreparedStatement pstmt = getConnection().prepareStatement("UPDATE users SET lastLoginTime = ? WHERE uuid = ?")) {
                    pstmt.setLong(1, time);
                    pstmt.setString(2, user.getUUID().toString());
                    pstmt.executeUpdate();
                    getLogger().info("Обновлено время последнего входа для пользователя " + user.getName() + " в БД.");
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Ошибка при обновлении времени последнего входа для пользователя " + user.getName() + ":", e);
                }
            }
        }).start();
    }

    public void setLastLogoffTime(User user, long time) {
        new Thread(() -> {
            synchronized (dbLock) { // Синхронизированный блок
                try (PreparedStatement pstmt = getConnection().prepareStatement("UPDATE users SET lastLogoffTime = ? WHERE uuid = ?")) {
                    pstmt.setLong(1, time);
                    pstmt.setString(2, user.getUUID().toString());
                    pstmt.executeUpdate();
                    getLogger().info("Обновлено время последнего выхода для пользователя " + user.getName() + " в БД.");
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Ошибка при обновлении времени последнего выхода для пользователя " + user.getName() + ":", e);
                }
            }
        }).start();
    }

    public long getLastLoginTime(OfflineUser user) {
        final long[] time = {-1};
        synchronized (dbLock) { // Синхронизированный блок
            try (PreparedStatement pstmt = getConnection().prepareStatement("SELECT lastLoginTime FROM users WHERE uuid = ?")) {
                pstmt.setString(1, user.getUUID().toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        time[0] = rs.getLong("lastLoginTime");
                    }
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Ошибка при получении времени последнего входа для пользователя " + user.getName() + ":", e);
                }
            } catch (SQLException e) {
                getLogger().log(Level.SEVERE, "Ошибка при получении времени последнего входа для пользователя " + user.getName() + ":", e);

            }
        }
        return time[0];
    }

    public long getLastLogoffTime(OfflineUser user) {
        final long[] time = {-1};
        synchronized (dbLock) { // Синхронизированный блок
            try (PreparedStatement pstmt = getConnection().prepareStatement("SELECT lastLogoffTime FROM users WHERE uuid = ?")) {
                pstmt.setString(1, user.getUUID().toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        time[0] = rs.getLong("lastLogoffTime");
                    }
                } catch (SQLException e) {
                    getLogger().log(Level.SEVERE, "Ошибка при получении времени последнего выхода для пользователя " + user.getName() + ":", e);
                }
            } catch (SQLException e) {
                getLogger().log(Level.SEVERE, "Ошибка при получении времени последнего выхода для пользователя " + user.getName() + ":", e);

            }
        }
        return time[0];
    }

    private java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getLogger("ImportantZ"); // Или другой логгер вашего плагина
    }
}

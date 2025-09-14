package ru.korshun.importantz.api.database.parent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import ru.korshun.importantz.ImportantZ;
import ru.korshun.importantz.api.database.DatabaseManager;
import ru.korshun.importantz.api.home.Home;
import ru.korshun.importantz.api.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomesDBManager extends DatabaseManager {
    public static final HomesDBManager INSTANCE = new HomesDBManager();
    private Statement stmt;

    public HomesDBManager() {
        try {
            stmt = getConnection().createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users(id INTEGER AUTO_INCREMENT, uuid TEXT, username TEXT, custom_nickname TEXT, homes TEXT, lastLoginTime bigint, lastLogoffTime bigint);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHome(String name, User owner, Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = stmt.executeQuery("SELECT uuid, homes FROM users WHERE uuid = '" + owner.getUUID() + "'");
                    if(!rs.next()) {
                        stmt.execute("INSERT INTO users(uuid, username, homes) VALUES('" + owner.getUUID().toString() + "', '" + owner.getName() + "', '" + parseHomeString(name, location) + "');");
                    } else {
                        stmt.execute("UPDATE users SET homes = '" + rs.getString("homes") + parseHomeString(name, location) + "'");
                    }
                } catch (SQLException ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void removeHome(Home home) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = stmt.executeQuery("SELECT homes FROM users WHERE uuid = '" + home.getOwner().getUUID().toString() + "'");
                    if(!rs.next()) {
                        return;
                    }
                    String s = rs.getString("homes");
                    s = removeStrHome(s, home.getName());
                    stmt.execute("UPDATE users SET homes = '" + s + "'");
                } catch (SQLException ignored) {}
            }
        }).start();
    }

    public List<Home> getHomes(User user) {
        List<Home> homes = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = stmt.executeQuery("SELECT homes FROM users WHERE uuid = '" + user.getUUID() + "'");
                    if (rs.next()) {
                        String s = rs.getString("homes");
                        if(s != null) {
                            String[] array = s.split(";");
                            for (int i = 0; i < array.length; i++) {
                                String[] nameAndLocation = array[i].split("\\$\\$%%");
                                String name = nameAndLocation[0];
                                String sLoc = nameAndLocation[1];
                                String[] locArray = sLoc.split(":");
                                Location location = new Location(Bukkit.getWorld(locArray[0]), Double.parseDouble(locArray[1]), Double.parseDouble(locArray[2]), Double.parseDouble(locArray[3]), Float.parseFloat(locArray[4]), Float.parseFloat(locArray[5].replace(";", "")));
                                homes.add(ImportantZ.createHome(name, user, location));
                            }
                        }
                    }
                } catch (SQLException e) {}
            }
        }).start();
        return homes;
    }
    private String removeStrHome(String input, String homeName) {
        if (input == null || input.isEmpty() || homeName == null || homeName.isEmpty()) {
            return input; // Nothing to do
        }

        String[] houses = input.split(";", -1); // Split into individual house entries.  Keep trailing delimiter

        StringBuilder result = new StringBuilder();
        boolean first = true; // To handle leading separator, and only add separator.
        for (String house : houses) {
            if (house.startsWith(homeName + "$$%%")) { //Found match
                // Skip this house
            } else {
                if (!first) {
                    result.append(";"); // Add separator if it's not the first entry
                }
                result.append(house);
                first = false;
            }
        }

        return result.toString();
    }

    private String parseHomeString(String name, Location location) {
        String total = "";
        total += name;
        total += "$$%%";
        total += location.getWorld().getName() + ":";
        double x = (double) Math.round(location.getX() * 100) / 100;
        double z = (double) Math.round(location.getZ() * 100) / 100;
        float yaw = (float) Math.round(location.getYaw() * 100) / 100;
        float pitch = (float) Math.round(location.getPitch() * 100) / 100;
        total += x + ":";
        total += location.getY() + ":";
        total += z + ":";
        total += yaw + ":";
        total += pitch + ";";
        return total;
    }
}

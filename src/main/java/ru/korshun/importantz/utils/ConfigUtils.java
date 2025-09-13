package ru.korshun.importantz.utils;

import ru.korshun.importantz.ImportantZ;

public class ConfigUtils {
    public static String getString(String path) {
        return ImportantZ.getInstance().getConfig().getString(path);
    }

    public static int getInt(String path) {
        return ImportantZ.getInstance().getConfig().getInt(path);
    }

    public static boolean getBoolean(String path) {
        return ImportantZ.getInstance().getConfig().getBoolean(path);
    }

    public static String getLanguage() {
        return getString("language");
    }

    public static String getPrefix() {
        return ChatUtil.translate(getString("prefix"));
    }

    public static String getCommand(String command, String param) {
        return getString("commands." + command + "." + param);
    }

    public static class Database {
        public static String getDatabaseType() {
            return getString("database.type");
        }
        public static String getHost() {
            return getString("database.host");
        }
        public static int getPort() {
            return getInt("database.port");
        }
        public static String getUsername() {
            return getString("database.username");
        }
        public static String getPassword() {
            return getString("database.password");
        }
        public static String getName() {
            return getString("database.dbName");
        }
    }

    public static class Time {
        public static String getSeconds() {
            return getString("time.seconds");
        }
        public static String getMinutes() {
            return getString("time.minutes");
        }
        public static String getHours() {
            return getString("time.hours");
        }
        public static String getDays() {
            return getString("time.days");
        }
        public static String getMonths() {
            return getString("time.months");
        }
        public static String getYears() {
            return getString("time.years");
        }

        public static String getNow() {
            return getString("time.now");
        }
    }
}

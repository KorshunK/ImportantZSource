package ru.korshun.importantz.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    private static final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
    private static final int maxYears = 100000;

    public static class Time {
        private long years;
        private long months;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;

        public void setYears(long years) {
            this.years = years;
        }

        public void setMonths(long months) {
            this.months = months;
        }

        public void setDays(long days) {
            this.days = days;
        }

        public void setHours(long hours) {
            this.hours = hours;
        }

        public void setMinutes(long minutes) {
            this.minutes = minutes;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

        public long getYears() {
            return years;
        }

        public long getMonths() {
            return months;
        }

        public long getDays() {
            return days;
        }

        public long getHours() {
            return hours;
        }

        public long getMinutes() {
            return minutes;
        }

        public long getSeconds() {
            return seconds;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (years > 0) {
                builder.append(years).append(" ").append(ConfigUtils.Time.getYears()).append(" ");
            }
            if (months > 0) {
                builder.append(months).append(" ").append(ConfigUtils.Time.getMonths()).append(" ");
            }
            if (days > 0) {
                builder.append(days).append(" ").append(ConfigUtils.Time.getDays()).append(" ");
            }
            if (hours > 0) {
                builder.append(hours).append(" ").append(ConfigUtils.Time.getHours()).append(" ");
            }
            if (minutes > 0) {
                builder.append(minutes).append(" ").append(ConfigUtils.Time.getMinutes()).append(" ");
            }
            if (seconds > 0) {
                builder.append(seconds).append(" ").append(ConfigUtils.Time.getSeconds()).append(" ");
            }
            return builder.toString().trim();
        }
    }

    public static Time parseFromLong(long longTime) {
        Time time = new Time();
        long seconds = longTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;
        if (seconds > 0) {
            time.setSeconds(seconds);
        }
        if (minutes > 0) {
            time.setMinutes(minutes);
        }
        if (hours > 0) {
            time.setHours(hours);
        }
        if (days > 0) {
            time.setDays(days);
        }
        if (months > 0) {
            time.setMonths(months);
        }
        if (years > 0) {
            time.setYears(years);
        }
        return time;
    }

    public static String formatDateDiff(long date) {
        long now = System.currentTimeMillis();
        long diff = now - date;

        if (diff < 0) {
            return "в будущем"; // Или обработайте этот случай по-другому
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long days = TimeUnit.MILLISECONDS.toDays(diff) % 30; //Приближенно, но так работает исходный код.
        long months = TimeUnit.MILLISECONDS.toDays(diff) / 30 % 12; //Приближенно
        long years = TimeUnit.MILLISECONDS.toDays(diff) / 365;

        StringBuilder sb = new StringBuilder();

        if (years > 0) {
            sb.append(years).append(" ").append(getLocalizedYear(years)).append(" ");
        }
        if (months > 0) {
            sb.append(months).append(" ").append(getLocalizedMonth(months)).append(" ");
        }
        if (days > 0) {
            sb.append(days).append(" ").append(getLocalizedDay(days)).append(" ");
        }
        if (hours > 0) {
            sb.append(hours).append(" ").append(getLocalizedHour(hours)).append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" ").append(getLocalizedMinute(minutes)).append(" ");
        }
        if (seconds > 0 || sb.length() == 0) {  //Всегда показывать секунды, если нет других единиц.
            sb.append(seconds).append(" ").append(getLocalizedSecond(seconds));
        }

        return sb.toString().trim();
    }

    private static String getLocalizedYear(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getYears();
        } else {
            return ConfigUtils.Time.getYears();
        }
    }

    private static String getLocalizedMonth(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getMonths();
        } else {
            return ConfigUtils.Time.getMonths();
        }
    }

    private static String getLocalizedDay(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getDays();
        } else {
            return ConfigUtils.Time.getDays();
        }
    }

    private static String getLocalizedHour(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getHours();
        } else {
            return ConfigUtils.Time.getHours();
        }
    }

    private static String getLocalizedMinute(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getMinutes();
        } else {
            return ConfigUtils.Time.getMinutes();
        }
    }

    private static String getLocalizedSecond(long count) {
        if (count == 1) {
            return ConfigUtils.Time.getSeconds();
        } else {
            return ConfigUtils.Time.getSeconds();
        }
    }
}

package ru.korshun.importantz.utils;

public class TimeUtils {
    private static String second = ConfigUtils.Time.getSecond();
    private static String seconds = ConfigUtils.Time.getSeconds();
    private static String minute = ConfigUtils.Time.getMinute();
    private static String minutes = ConfigUtils.Time.getMinutes();
    private static String hour = ConfigUtils.Time.getHour();
    private static String hours = ConfigUtils.Time.getHours();
    private static String day = ConfigUtils.Time.getDay();
    private static String days = ConfigUtils.Time.getDays();
    private static String month = ConfigUtils.Time.getMonth();
    private static String months = ConfigUtils.Time.getMonths();
    private static String year = ConfigUtils.Time.getYear();
    private static String years = ConfigUtils.Time.getYears();
    private static String now = ConfigUtils.Time.getNow();

    public static String parseTimeFromSeconds(long secs) {
        StringBuilder builder = new StringBuilder();
        if(secs < 1) {
            return now;
        }
        long remainingSecs = secs;

        long lYears = remainingSecs / (365 * 24 * 60 * 60);
        remainingSecs = Math.round(remainingSecs % (365 * 24 * 60 * 60));

        long lMonths = remainingSecs / (30 * 24 * 60 * 60);
        remainingSecs = Math.round(remainingSecs % (30 * 24 * 60 * 60));

        long lDays = remainingSecs / (24 * 60 * 60);
        remainingSecs = Math.round(remainingSecs % (24 * 60 * 60));

        long lHours = remainingSecs / (60 * 60);
        remainingSecs = Math.round(remainingSecs % (60 * 60));

        long lMinutes = remainingSecs / 60;
        remainingSecs = Math.round(remainingSecs % 60);

        long lSeconds = remainingSecs;

        if (lYears >= 1) {
            if (lYears < 2) {
                builder.append(lYears).append(" ").append(year).append(" ");
            } else {
                builder.append(lYears).append(" ").append(years).append(" ");
            }
        }
        if(lMonths >= 1) {
            if(lMonths < 2) {
                builder.append(lMonths).append(" ").append(month).append(" ");
            } else {
                builder.append(lMonths).append(" ").append(months).append(" ");
            }
        }
        if(lDays >= 1) {
            if(lDays < 2) {
                builder.append(lDays).append(" ").append(day).append(" ");
            } else {
                builder.append(lDays).append(" ").append(days).append(" ");
            }
        }
        if(lHours >= 1) {
            if(lHours < 2) {
                builder.append(lHours).append(" ").append(hour).append(" ");
            } else {
                builder.append(lHours).append(" ").append(hours).append(" ");
            }
        }
        if(lMinutes >= 1) {
            if(lMinutes < 2) {
                builder.append(lMinutes).append(" ").append(minute).append(" ");
            } else {
                builder.append(lMinutes).append(" ").append(minutes).append(" ");
            }
        }
        if(lSeconds >= 1) {
            if(lSeconds < 2) {
                builder.append(lSeconds).append(" ").append(second);
            } else {
                builder.append(lSeconds).append(" ").append(seconds);
            }
        }
        return builder.toString();
    }
}

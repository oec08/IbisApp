package com.codepath.apps.ibisapp.utils;

/**
 * Created by ocarty on 10/28/2016.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Given a date String of the format given by the Twitter API, returns a display-formatted
 * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
 * depending on how great the time difference between now and the given date is.
 * This, as of 2016-06-29, matches the behavior of the official Twitter app.
 */
public class TimeFormatter {
    public static final String TWITTER_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"; //"Mon Apr 01 21:16:23 +0000 2014"
    public static String getDuration(String date, String format) {
        long time = getTime(date, format);
        long diff = (System.currentTimeMillis() - time) / 1000;
        if (diff < 5)
            return "Just now";
        else if (diff < 60)
            return String.format(Locale.ENGLISH, "%ds", diff);
        else if (diff < 60 * 60)
            return String.format(Locale.ENGLISH, "%dm", diff / 60);
        else if (diff < 60 * 60 * 24)
            return String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
        else if (diff < 60 * 60 * 24 * 30)
            return String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
        else {
            Calendar now = Calendar.getInstance();
            Calendar then = getCalendar(time);
            if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                return String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            } else {
                return String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " "
                        + String.valueOf(then.get(Calendar.YEAR) - 2000);
            }
        }
    }

    private static long getTime(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            sdf.setLenient(true);
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

}
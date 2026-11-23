package com.example.ProjectWorkTracking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility functions for date formatting, zero-padding, parsing, and deadline diff calculation.
 */
public final class DateUtils {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    private DateUtils() {
        // Non-instantiable utility class
    }

    /**
     * Formats day, month (0-indexed Calendar month), and year into standard zero-padded dd/MM/yyyy.
     */
    public static String formatDate(int dayOfMonth, int monthOfYear, int year) {
        return String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
    }

    /**
     * Formats a Date object into dd/MM/yyyy.
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return sdf.format(date);
    }

    /**
     * Safely parses a date string in dd/MM/yyyy format.
     * Returns null if string is null, empty, or cannot be parsed.
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr.trim());
        } catch (ParseException e) {
            // Fallback: try flexible parsing if day/month single digits were saved
            try {
                SimpleDateFormat flexibleSdf = new SimpleDateFormat("d/M/yyyy", Locale.US);
                return flexibleSdf.parse(dateStr.trim());
            } catch (ParseException ex) {
                return null;
            }
        }
    }

    /**
     * Checks if a date string is valid according to dd/MM/yyyy format.
     */
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }

    /**
     * Calculates days remaining between today (midnight) and the specified end date.
     * Returns a negative value if the date is in the past.
     * Returns null if endDateStr cannot be parsed.
     */
    public static Long calculateDaysRemaining(String endDateStr) {
        Date targetDate = parseDate(endDateStr);
        if (targetDate == null) {
            return null;
        }

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(targetDate);
        targetCal.set(Calendar.HOUR_OF_DAY, 0);
        targetCal.set(Calendar.MINUTE, 0);
        targetCal.set(Calendar.SECOND, 0);
        targetCal.set(Calendar.MILLISECOND, 0);

        long diffMillis = targetCal.getTimeInMillis() - todayCal.getTimeInMillis();
        return TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns true if the end date is strictly before today (midnight).
     */
    public static boolean isOverdue(String endDateStr) {
        Long days = calculateDaysRemaining(endDateStr);
        return days != null && days < 0;
    }
}

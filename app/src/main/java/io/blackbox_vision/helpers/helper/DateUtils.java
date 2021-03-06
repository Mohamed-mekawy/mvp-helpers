package io.blackbox_vision.helpers.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public final class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();

    private DateUtils() { }

    public static String formatDate(@NonNull String pattern, @NonNull Locale locale, @NonNull Date dateToFormat) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        String dateString = "";

        try {
            dateString = simpleDateFormat.format(dateToFormat);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex.getCause());
        }

        return dateString;
    }

    public static String formatDate(@NonNull Date dateToFormat) {
        return formatDate("dd/MM/yyyy", Locale.getDefault(), dateToFormat);
    }

    public static Date fromString(@NonNull String dateString, @NonNull String delimiter) {
        final String[] datePieces = dateString.split(delimiter);

        final int year = Integer.valueOf(datePieces[2]);
        final int month = Integer.valueOf(datePieces[1]);
        final int dayOfMonth = Integer.valueOf(datePieces[0]);

        final Calendar calendar = Calendar.getInstance(Locale.getDefault());

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return calendar.getTime();
    }

    public static Calendar fromDate(@NonNull Date date) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);

        return calendar;
    }
}

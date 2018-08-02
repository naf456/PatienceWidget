package com.nathanielbennett.android.patience;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

public class PatienceDataManager {

    private static final String PREF_PREFIX_KEY = "patience_";
    private static final String PREF_FUTURE_EPOCH = PREF_PREFIX_KEY + "epoch_";
    private static final String PREF_WIDGET_MESSAGE = PREF_PREFIX_KEY + "widget_message_";

    public static CountdownData getCountdownDataForWidget(Context context, int appWidgetId){
        AndroidThreeTen.init(context);

        LocalDateTime nowDateTime = LocalDateTime.now();
        long futureEpoch = fetchFutureEpoch(context, appWidgetId);
        LocalDateTime futureDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(futureEpoch), ZoneId.systemDefault());
        Duration duration = Duration.between(nowDateTime, futureDateTime);

        boolean isCompleted = duration.isNegative() || duration.isZero();

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();

        return new CountdownData(isCompleted, hours, minutes, seconds);
    }

    public static String fetchWidgetMessage(Context context, int appWidgetId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_WIDGET_MESSAGE + appWidgetId;
        return preferences.getString(preferenceKey, "!Couldn't Find Message!");
    }

    public static void saveWidgetMessage(Context context, int appWidgetId, String message) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_WIDGET_MESSAGE + appWidgetId;
        preferences.edit().putString(preferenceKey, message).apply();
    }

    public static void deleteWidgetMessage(Context context, int appWidgetId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_WIDGET_MESSAGE + appWidgetId;
        preferences.edit().remove(preferenceKey).apply();
    }

    public static long fetchFutureEpoch(Context context, int appWidgetId){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_FUTURE_EPOCH + appWidgetId;
        return preferences.getLong(preferenceKey, 0);
    }

    public static void saveFutureEpoch(Context context, int appWidgetId, long futureEpoch) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_FUTURE_EPOCH + appWidgetId;
        preferences.edit().putLong(preferenceKey, futureEpoch).apply();
    }

    public static void deleteFutureEpoch(Context context, int appWidgetId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceKey = PREF_FUTURE_EPOCH + appWidgetId;
        preferences.edit().remove(preferenceKey).apply();
    }

    public static void deleteWidgetData(Context context, int appWidgetId) {
        deleteFutureEpoch(context, appWidgetId);
        deleteWidgetMessage(context, appWidgetId);
    }
}

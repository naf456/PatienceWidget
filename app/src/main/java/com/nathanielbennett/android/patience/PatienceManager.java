package com.nathanielbennett.android.patience;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

public class PatienceManager {

    private static final String PREF_PREFIX_KEY = "patience_";
    private static final String PREF_FUTURE_EPOCH = PREF_PREFIX_KEY + "epoch_";
    private static final String PREF_WIDGET_MESSAGE = PREF_PREFIX_KEY + "widget_message_";

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

    public static LocalDateTime fetchWidgetCountdownDateTime(Context context, int appWidgetId){
        long futureEpoch = fetchFutureEpoch(context, appWidgetId);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(futureEpoch), ZoneId.systemDefault());
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

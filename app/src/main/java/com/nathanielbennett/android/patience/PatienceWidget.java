package com.nathanielbennett.android.patience;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import static com.nathanielbennett.android.patience.CustomReceivers.*;

public class PatienceWidget extends AppWidgetProvider {

    public static CountdownData GetCountdownDataForWidget(Context context, int appWidgetId){
        AndroidThreeTen.init(context);
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime futureDateTime = PatienceManager.fetchWidgetCountdownDateTime(context, appWidgetId);
        Duration duration = Duration.between(nowDateTime, futureDateTime);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();

        return new CountdownData(hours, minutes, seconds);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_patience);

        CountdownData countdownData = GetCountdownDataForWidget(context, appWidgetId);
        String message = PatienceManager.fetchWidgetMessage(context, appWidgetId);

        views.setTextViewText(R.id.hours, countdownData.getHoursLeft());
        views.setTextViewText(R.id.minutes, countdownData.getMinutesLeft());
        views.setTextViewText(R.id.countdown_message, message);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            PatienceManager.deleteWidgetData(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        restartAll(context);
    }

    @Override
    public void onDisabled(Context context) {
        Intent serviceBG = new Intent(context.getApplicationContext(), PatienceBroadcastService.class);
        serviceBG.putExtra("SHUTDOWN", true);
        context.getApplicationContext().startService(serviceBG);
        context.getApplicationContext().stopService(serviceBG);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), PatienceWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        if (intent.getAction().equals(ACTION_SETTINGS_CHANGED)) {
            onUpdate(context, appWidgetManager, appWidgetIds);
            if (appWidgetIds.length > 0) {
                restartAll(context);
            }
        }

        if (intent.getAction().equals(ACTION_JOB_TICK) || intent.getAction().equals(ACTION_COUNTDOWN_TICK) ||
                intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                || intent.getAction().equals(Intent.ACTION_DATE_CHANGED)
                || intent.getAction().equals(Intent.ACTION_TIME_CHANGED)
                || intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

    }

    private void restartAll(Context context){
        Intent serviceBG = new Intent(context.getApplicationContext(), PatienceBroadcastService.class);
        context.getApplicationContext().startService(serviceBG);
    }
}


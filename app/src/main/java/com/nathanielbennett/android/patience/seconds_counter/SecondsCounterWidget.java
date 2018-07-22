package com.nathanielbennett.android.patience.seconds_counter;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.nathanielbennett.android.patience.R;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import static com.nathanielbennett.android.patience.CustomReceivers.*;

/**
 * Implementation of App Widget functionality.
 */
public class SecondsCounterWidget extends AppWidgetProvider {

    private static LocalDateTime initialDateTime;

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Duration duration = Duration.between(initialDateTime, LocalDateTime.now());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_seconds_counter);
        views.setTextViewText(R.id.seconds_counter, String.valueOf(duration.getSeconds()));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(IntervalUpdateService.ACTION_INTERVAL_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), SecondsCounterWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        AndroidThreeTen.init(context);
        initialDateTime = LocalDateTime.now();
        context.getApplicationContext().startService(new Intent(context.getApplicationContext(), IntervalUpdateService.class));
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


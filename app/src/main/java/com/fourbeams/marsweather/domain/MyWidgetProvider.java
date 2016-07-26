package com.fourbeams.marsweather.domain;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;

public class MyWidgetProvider extends AppWidgetProvider {

    private final String UPDATE_TEMPERATURE_BUTTON_PRESSED
            = "com.fourbeams.marsweather.intent.action.UPDATE_TEMPERATURE_BUTTON_PRESSED";
    private final String DATA_CHANGED_IN_PROVIDER
            = "com.fourbeams.marsweather.intent.action.DATA_CHANGED_IN_PROVIDER";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // obtaining new data from server
        ServiceHelper.getInstance(context.getApplicationContext()).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, MyWidgetProvider.class);
            intent.setAction(UPDATE_TEMPERATURE_BUTTON_PRESSED);
            intent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_button_refresh, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction(DATA_CHANGED_IN_PROVIDER);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(UPDATE_TEMPERATURE_BUTTON_PRESSED)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetManager.INVALID_APPWIDGET_ID);
            ServiceHelper.getInstance(context.getApplicationContext()).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        if (intent.getAction().equals(DATA_CHANGED_IN_PROVIDER)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int [] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));

            String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    Uri.parse(URL),
                    MarsWeatherContentProvider.TEMPERATURE_PROJECTION,
                    null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor.moveToLast()) {
                String latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
                Double maxTempCInContentProvider = cursor.getDouble(cursor.getColumnIndex(MarsWeatherContentProvider.MAX_TEMP_C));
                Double minTempCInContentProvider = cursor.getDouble(cursor.getColumnIndex(MarsWeatherContentProvider.MIN_TEMP_C));
                //remoteViews.setTextViewText(R.id.textView, latestDateInContentProvider);
                remoteViews.setTextViewText(R.id.textView2, String.valueOf(maxTempCInContentProvider));
                //remoteViews.setTextViewText(R.id.textView3, String.valueOf(minTempCInContentProvider));
                for (int i=0; i<appWidgetIds.length; i++) {
                    appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
                }
            }
        }
        super.onReceive(context, intent);
    }
}

package com.fourbeams.marsweather.domain;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;

public class MyWidgetProvider extends AppWidgetProvider{

    private final String UPDATE_TEMPERATURE_BUTTON_PRESSED
            = "com.fourbeams.marsweather.intent.action.UPDATE_TEMPERATURE_BUTTON_PRESSED";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        if (ServiceHelper.isContextNull() )  ServiceHelper.setContext(context);

        context.getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, // observed URI
                true,
                new MarsWeatherContentProviderObserver(new Handler(), context));
    }

    private class MarsWeatherContentProviderObserver extends ContentObserver {
        private Context context;

        public MarsWeatherContentProviderObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //TODO - should works in parallel thread
            // obtaining date from provider
            String latestDateInContentProvider;
            String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
            Cursor cursor = context.getContentResolver().query(Uri.parse(URL), MarsWeatherContentProvider.TEMPERATURE_PROJECTION, null, null, null);
            if (cursor.moveToLast()){
                latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));
                Log.d("TAG", "latestDateInContentProvider=" + latestDateInContentProvider);
            }
            cursor.close();

            /*RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            remoteViews.setTextViewText(R.id.textView, "text Changed");
            //ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);*/


        }
    }

    @Override
    // onUpdate is called by framework in response of android.appwidget.action.APPWIDGET_UPDATE intent every
    // android:updatePeriodMillis or when the widget created
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        for (int i=0; i<appWidgetIds.length; i++) {
            Intent intent = new Intent(context, MyWidgetProvider.class);
            intent.setAction(UPDATE_TEMPERATURE_BUTTON_PRESSED);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, /*0*/PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_button_refresh, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(UPDATE_TEMPERATURE_BUTTON_PRESSED)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            remoteViews.setTextViewText(R.id.textView, "text Changed");
            ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onReceive(context, intent);
    }

}
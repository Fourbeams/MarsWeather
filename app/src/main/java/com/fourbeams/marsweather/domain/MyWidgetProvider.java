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
import android.widget.RemoteViews;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.presentation.MyWidgetIntentReceiver;

public class MyWidgetProvider extends AppWidgetProvider{

   /* @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.getContentResolver().registerContentObserver(
                MarsWeatherContentProvider.CONTENT_URI, // observed URI
                true,
                new MarsWeatherContentProviderObserver(new Handler(), context));
    }
*/

/*    private class MarsWeatherContentProviderObserver extends ContentObserver {

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
            // obtaining date from provider
            String latestDateInContentProvider = "";
            String URL = "content://" + MarsWeatherContentProvider.PROVIDER_NAME + "/temperature/last_date";
            Cursor cursor = context.getContentResolver().query(Uri.parse(URL), MarsWeatherContentProvider.TEMPERATURE_PROJECTION, null, null, null);
            if (cursor.moveToLast()){
                latestDateInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.TERRESTRIAL_DATE));

            }
            cursor.close();
        }
    }*/

    @Override
    // onUpdate is called by framework in response of android.appwidget.action.APPWIDGET_UPDATE intent every
    // android:updatePeriodMillis or when the widget created
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // RemoteView - A class that describes a view hierarchy that can be displayed in another process
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // PendingIntent - By giving a PendingIntent to another application,
        // you are granting it the right to perform the operation you have specified as if the other application was yourself
        Intent intent = new Intent(context, MyWidgetIntentReceiver.class);
        intent.setAction("com.fourbeams.marsweather.intent.action.UPDATE_TEMPERATURE");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
}
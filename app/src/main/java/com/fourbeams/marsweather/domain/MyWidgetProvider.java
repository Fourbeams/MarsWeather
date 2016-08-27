package com.fourbeams.marsweather.domain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.presentation.MainActivity;


public class MyWidgetProvider extends AppWidgetProvider {

    private final String UPDATE_TEMPERATURE_BUTTON_PRESSED
            = "com.fourbeams.marsweather.intent.action.UPDATE_TEMPERATURE_BUTTON_PRESSED";
    private final String DATA_CHANGED_IN_PROVIDER
            = "com.fourbeams.marsweather.intent.action.DATA_CHANGED_IN_PROVIDER";

    @Override
    public void onReceive(Context context, Intent intent) {
        // if the button to update data from server where pressed
        if (intent.getAction().equals(UPDATE_TEMPERATURE_BUTTON_PRESSED)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            displayLoadingIndicator(remoteViews);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            ServiceHelper.getInstance(context.getApplicationContext()).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        // if data changed at provider or processor responded with no data changes at content provider
        if (intent.getAction().equals(DATA_CHANGED_IN_PROVIDER) ||
                intent.getAction().equals(Processor.PROCESSOR_RESPONDED_WITH_NO_NEW_DATA_AT_SERVER)) {
            loadDataFromProvider(context);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d ("TAG", "onUpdate called");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // obtaining new data from server
        ServiceHelper.getInstance(context).runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
        for (int i = 0; i < appWidgetIds.length; i++) {
            displayLoadingIndicator(remoteViews);
            setOpenActivityWhenClicked(context, remoteViews); // open main activity when widget area clicked
            setRefreshWhenButtonClicked(context, remoteViews, appWidgetIds, i); // set onClick listener to refresh button
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        setAlarmManager(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void setOpenActivityWhenClicked(Context context, RemoteViews remoteViews){
        // open main activity when widget area clicked
        Intent startMainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent startMainActivityPendingIntent = PendingIntent.getActivity(context, 0, startMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_top_layout, startMainActivityPendingIntent);
    }

    private void setRefreshWhenButtonClicked(Context context, RemoteViews remoteViews, int[] appWidgetIds, int i){
        // make refresh button clickable
        Intent updateButtonPressedIntent = new Intent(context, MyWidgetProvider.class);
        updateButtonPressedIntent.setAction(UPDATE_TEMPERATURE_BUTTON_PRESSED);
        updateButtonPressedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
        PendingIntent updateButtonPressedPendingIntent = PendingIntent.getBroadcast(context, 0, updateButtonPressedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_button_refresh, updateButtonPressedPendingIntent);
    }

    private void setAlarmManager(Context context){
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction(DATA_CHANGED_IN_PROVIDER);
        // force sending intents that data changed at provider periodically via alarm manager
        AlarmManager aManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        final int UPDATE_PERIOD = 5000;
        aManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), UPDATE_PERIOD, pi);
    }

    private void loadDataFromProvider(Context context){
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
            Integer maxTempCInContentProvider = cursor.getInt(cursor.getColumnIndex(MarsWeatherContentProvider.MAX_TEMP_C));
            Integer minTempCInContentProvider = cursor.getInt(cursor.getColumnIndex(MarsWeatherContentProvider.MIN_TEMP_C));
            String seasonInContentProvider = cursor.getString(cursor.getColumnIndex(MarsWeatherContentProvider.SEASON));
            remoteViews.setTextViewText(R.id.textView, " " + latestDateInContentProvider);
            remoteViews.setTextViewText(R.id.textView2, " " + String.valueOf(maxTempCInContentProvider));
            remoteViews.setTextViewText(R.id.textView3, " " + String.valueOf(minTempCInContentProvider));
            remoteViews.setTextViewText(R.id.marsMonth, seasonInContentProvider);
            DateAndTimeUtil dateAndTimeUtil = DateAndTimeUtil.getInstance();
            remoteViews.setTextViewText(R.id.marsTime, dateAndTimeUtil.getMarsTime());
            remoteViews.setTextViewText(R.id.marsSol, "Sol " + dateAndTimeUtil.getMarsSol() + "  ");
            for (int i = 0; i < appWidgetIds.length; i++) {
                hideLoadingIndicator(remoteViews);
                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            }
        }
    }

    private void hideLoadingIndicator(RemoteViews remoteViews){
        remoteViews.setViewVisibility(R.id.progress_spinner,View.GONE);
        remoteViews.setViewVisibility(R.id.widget_button_refresh,View.VISIBLE);
    }

    private void displayLoadingIndicator(RemoteViews remoteViews){
        remoteViews.setViewVisibility(R.id.progress_spinner,View.VISIBLE);
        remoteViews.setViewVisibility(R.id.widget_button_refresh,View.GONE);
    }

}

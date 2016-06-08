package com.fourbeams.marsweather.presentation;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.fourbeams.marsweather.R;
import com.fourbeams.marsweather.domain.ServiceHelper;

public class MyWidgetIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }

/*   @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        // Set the RemoteViews to use for all AppWidget instances for the supplied AppWidget provider
        int [] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        if(intent.getAction().equalsIgnoreCase("com.fourbeams.marsweather.intent.action.UPDATE_TEMPERATURE_BUTTON_PRESSED")){
            ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
            updateWidgetAndButtonListener(remoteViews, appWidgetIds, appWidgetManager);
        }
    }*/

    private void updateWidgetAndButtonListener(RemoteViews remoteViews, int [] appWidgetIds, AppWidgetManager appWidgetManager) {
        //remoteViews.setTextViewText(R.id.textView, "text Changed");
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
}
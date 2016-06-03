package com.fourbeams.marsweather;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.fourbeams.marsweather.domain.ServiceHelper;
import com.fourbeams.marsweather.persistence.MarsWeatherContentProvider;
import com.fourbeams.marsweather.presentation.MainActivity;

public class MyWidgetProvider extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widget_button, buildButtonPendingIntent(context));

        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent buildButtonPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.fourbeams.marsweather.intent.action.CHANGE_TEMPERATURE");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}

/*            // start REST service when button is clicked
            Button updateButton = (Button) ((Activity) context).findViewById(R.id.button);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO - does it work?
                    Toast.makeText(view.getContext(), "widget added", Toast.LENGTH_SHORT).show();
                    ServiceHelper.getInstance().runService(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
                }
            });
            // update data on widget

            // Tell the AppWidgetManager to perform an update on the current app widget

            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }
} */

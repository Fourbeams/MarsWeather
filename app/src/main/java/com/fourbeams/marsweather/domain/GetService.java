package com.fourbeams.marsweather.domain;

import android.app.IntentService;
import android.content.Intent;

public class GetService extends IntentService {

    public GetService() {
        super("GetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String task = intent.getStringExtra("TASK");
        new Processor(getApplicationContext()).startGetProcessor(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceFacade.getInstance(getApplicationContext())
                .removeServiceFromPool(ServiceFacade.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
    }
}
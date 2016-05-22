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
        new Processor().startGetProcessor(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceHelper.getInstance().removeServiceFromPool(ServiceHelper.task.GET_NEW_WEATHER_DATA_FROM_SERVER);
    }
}
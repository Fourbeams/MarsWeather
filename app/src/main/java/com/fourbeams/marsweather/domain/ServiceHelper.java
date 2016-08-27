package com.fourbeams.marsweather.domain;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

/**
 * The service helper is a common interface for upper layers to start the particular service
 */
public class ServiceHelper {

    private final static ServiceHelper INSTANCE = new ServiceHelper();
    private static Context context;
    private Set <String> runningServicesPool = new HashSet <>();

    public enum task{
        GET_NEW_WEATHER_DATA_FROM_SERVER {
            @Override
            public String toString() {
                return "GET_NEW_WEATHER_DATA_FROM_SERVER";
            }
        }
    }

    private ServiceHelper(){}

    public static ServiceHelper getInstance(Context context) {
        if (ServiceHelper.context == null) ServiceHelper.context = context;
        return INSTANCE;
    }

    public void runService (ServiceHelper.task task){
        switch (task){
            case GET_NEW_WEATHER_DATA_FROM_SERVER: startGetService(task);
                break;
        }
    }

    void removeServiceFromPool(task task){
        String key = task.toString();
        runningServicesPool.remove(key);
    }

    private void startGetService(task task){
        String taskKey = task.toString();
        if (!runningServicesPool.contains(taskKey)) {
            runningServicesPool.add(taskKey);
            Intent startServiceIntent = new Intent(context, GetService.class);
            startServiceIntent.putExtra("TASK", taskKey);
            context.startService(startServiceIntent);
        }
    }
}
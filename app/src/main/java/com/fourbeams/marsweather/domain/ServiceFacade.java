package com.fourbeams.marsweather.domain;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

/**
 * The service facade is a common interface for upper layers to start the particular service.
 * <br/>Method {@link #runService(task)} starts new service through sending intent
 * <br/>and registers service as running at {@link #runningServicesPool}.
 * <br/>The new service starts only in case it is not currently running (is not at {@link #runningServicesPool})
 */
public class ServiceFacade {

    private final static ServiceFacade INSTANCE = new ServiceFacade();
    private static Context context;
    private Set <String> runningServicesPool = new HashSet <>();

    private ServiceFacade(){}

    public enum task{
        GET_NEW_WEATHER_DATA_FROM_SERVER {
            @Override
            public String toString() {
                return "GET_NEW_WEATHER_DATA_FROM_SERVER";
            }
        }

    }

    public static ServiceFacade getInstance(Context context) {
        if (ServiceFacade.context == null) ServiceFacade.context = context;
        return INSTANCE;
    }

    public void runService (ServiceFacade.task task){
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
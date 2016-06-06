package com.fourbeams.marsweather.domain;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

/**
 * The service helper is a common interface for upper layers to start the particular service
 */
public class ServiceHelper {

    private final static ServiceHelper INSTANCE = new ServiceHelper();
    private static Context mContext;
    private HashMap<String, Object> runningServicesPool = new HashMap<> ();

    public enum task{
        GET_NEW_WEATHER_DATA_FROM_SERVER {
            @Override
            public String toString() {
                return "GET_NEW_WEATHER_DATA_FROM_SERVER";
            }
        }
    }

    private ServiceHelper(){}

    public static ServiceHelper getInstance() {
        return INSTANCE;
    }

    public static void setContext (Context context) {
        mContext = context;
    }

    public static boolean isContextNull () {
        return (mContext == null);
    }

    public void runService (ServiceHelper.task task){
        switch (task){
            case GET_NEW_WEATHER_DATA_FROM_SERVER: startGetService(task);
                break;
        }
    }

    public void removeServiceFromPool(task task){
        String key = task.toString();
        runningServicesPool.remove(key);
    }

    private void startGetService(task task){
        String key = task.GET_NEW_WEATHER_DATA_FROM_SERVER.toString();
        if (!runningServicesPool.containsKey(key)) {
            runningServicesPool.put(key, null );
            Intent startServiceIntent = new Intent(mContext, GetService.class);
            startServiceIntent.putExtra("TASK", key);
            mContext.startService(startServiceIntent);
        }
    }
}
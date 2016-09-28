package com.example.ezras.travelagencies.model.backend;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ezra_Steinmetz on August 2016
 *
 * this service checks every 10 seconds if there are any new updates and in case there are - sends
 * a broadcast intent to notify registered apps
 */
public class DBUpdateCheckerService extends Service {
    //for Log usage
    private final static String TAG = "DBUpdateCheckerService";

    private static DB_Manager manager = DBManagerFactory.getManager();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * this function runs when the service is created
     */
    @Override
    public void onCreate() {
//starting an async-task that will check for updates every 10 seconds
        //(since the checking is calling to the DB we must use an async-task for that)
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(final Void... params) {
                checkForDBUpdates();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);

    }

    /**
     * the function checks for updates every 10 seconds and sends an appropriate intent if there is
     * a new update.
     *
     */
    private void checkForDBUpdates() {
        Intent agenciesUpdate, tripsUpdate;
        //sets the appropriate intents in advance
        tripsUpdate = new Intent("com.example.ezras.newUpdates").putExtra("table", 't');
        agenciesUpdate = new Intent("com.example.ezras.newUpdates").putExtra("table", 'a');

        while (true) {

            try {
                //checking for agencies updates
                if (manager.newAgenciesUpdates()) {
                    Log.d(TAG, "Sending newAgencies intent...");
                    sendBroadcast(agenciesUpdate);
                }
                if (manager.newTripsUpdates()) {
                    Log.d(TAG, "Sending newTrips intent...");
                    sendBroadcast(tripsUpdate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //wait 10 seconds
            SystemClock.sleep(10000);
        }
    }
}

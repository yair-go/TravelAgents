package com.example.ezras.travelagencies.model.backend;

import java.util.Date;

/**
 * Created by Ezra_Steinmetz on August 2016
 * This class simulates a server that gets notified whenever it's updated and let's other
 * classes to check when the last time it was updated
 */
public class ServerUpdate {
    private static Date lastUpdate = new Date();

    //get the last update date
    public static Date getLastUpdate(){
        return lastUpdate;
    }

    //notify the server about a new update
    public static void update(){
        lastUpdate = new Date();
    }
}

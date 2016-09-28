package com.example.ezras.travelagencies.model.backend;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.ezras.travelagencies.model.entities.Agency;
import com.example.ezras.travelagencies.model.entities.Trip;
import com.example.ezras.travelagencies.model.entities.User;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Ezra_Steinmetz on 06-Aug-16.
 *
 * this is an interface for the DB manager, a DB manager is any class that takes care of saving
 * and restoring data about trips, agencies and users
 */
public interface DB_Manager {

    //region adders
    /**
     * adds a new trip
     * @param trip - a ContentValues object that contains the trip's details
     */
    public void addTrip(ContentValues trip);
    /**
     * adds a new agency
     * @param agency - a ContentValues object that contains the agency's details
     */
    public void addAgency(ContentValues agency);
    /**
     * adds a new user
     * @param user - a ContentValues object that contains the user's details
     */
    public void addUser(ContentValues user);
    //endregion

    //region update-checkers
    /**
     * checks if there's any new trips
     * @return true if there is, false if there isn't
     * @throws Exception - for the server implementation, the network connection might throw an exception
     */
    public boolean newTripsUpdates() throws Exception;
    /**
     * checks if there's any new agencies
     * @return true if there is, false if there isn't
     * @throws Exception - for the server implementation, the network connection might throw an exception
     */
    public boolean newAgenciesUpdates() throws Exception;
    //endregion

    //region getters
    /**
     * @return the list of agencies that are in the DB
     * @throws Exception - for the server implementation, the network connection might throw an exception
     */
    public Cursor getAgencies() throws Exception;

    /**
     * @return the list of trips that are in the DB
     * @throws Exception - for the server implementation, the network connection might throw an exception
     */
    public Cursor getTrips() throws Exception;
    /**
     * @return the list of users that are in the DB
     * @throws Exception - for the server implementation, the network connection might throw an exception
     */
    public Cursor getUsers() throws Exception;
    //endregion
}

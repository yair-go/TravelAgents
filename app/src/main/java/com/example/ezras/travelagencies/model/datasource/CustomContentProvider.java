package com.example.ezras.travelagencies.model.datasource;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DB_Manager;

/**
 * Created by Ezra_Steinmetz on August 2016
 * <p/>
 * This content provider provides the ability to retrieve the trips and agencies lists from the server from any
 * other application via the URIs "com.example.ezras.travelagencies/Agencies", "com.example.ezras.travelagencies/Trips"
 */
public class CustomContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static DB_Manager manager = DBManagerFactory.getManager();
    private final static String TAG = "CustomContentProvider";

    //sets the uris
    static {
        sUriMatcher.addURI("com.example.ezras.travelagencies", "agencies", 1);
        sUriMatcher.addURI("com.example.ezras.travelagencies", "trips", 2);
        sUriMatcher.addURI("com.example.ezras.travelagencies", "users", 3);

    }

    @Override
    public boolean onCreate() {
        return false;
    }


    /*
    The function returns a cursor containing a table of agencies or trips according to the given uri
    */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        /*
       ### The uri.getPath() function returns the path with the preceding "/", if you want to get rid of it you can simply use the substring function
            as shown below.
         */
        String table = uri.getPath().substring(1);
        if (table.equalsIgnoreCase("agencies")) {

            try {
                return manager.getAgencies();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (table.equalsIgnoreCase("trips")) {

            try {
                return manager.getTrips();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if(table.equalsIgnoreCase("users")){
            try {
                return manager.getUsers();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //if the uri doesn't match one of the tables above
        throw new IllegalArgumentException("Content Provider can return only agencies or trips not " + table);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    /*
    adds a trip to the DB
    PARAMETERS
    uri - must contain the path to the table Trips
    Trip - must contain the values of the trips with the following filed names
    agencyID, image, country, startDate (string - dd/MM/yyy), endDate (string - dd/MM/yyy), price
    RETURN VALUE
    returns a null Uri, since the calling application has no use of the address of the given trip
     */
    public Uri insert(Uri uri, ContentValues obj) {
        String table = uri.getPath().substring(1);
        if (table.equalsIgnoreCase("Trips")) {
            manager.addTrip(obj);
            return null;
        }
        if(table.equalsIgnoreCase("Agencies")){
            manager.addAgency(obj);
            return null;
        }
        if(table.equalsIgnoreCase("Users")){
            manager.addUser(obj);
            return null;
        }
        throw new IllegalArgumentException("This Content Provider supports only trips insertion");
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

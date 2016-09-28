package com.example.ezras.travelagencies.model.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

import com.example.ezras.travelagencies.model.backend.DB_Manager;
import com.example.ezras.travelagencies.model.entities.Agency;
import com.example.ezras.travelagencies.model.entities.Trip;
import com.example.ezras.travelagencies.model.entities.User;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ezra_Steinmetz on 06-Aug-16.
 *
 * this is implementation of the DB manager using local lists
 * for documentation of this class see {@link DB_Manager}
 */

public class LocalDBManager implements DB_Manager {

    private ArrayList<Trip> trips = new ArrayList<>();
    private ArrayList<Agency> agencies = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private boolean tripsUpdates = true;
    private boolean agenciesUpdates = true;
    Date localUpdate;
    private static final String TAG = "LocalDBManager";

    //initializing lists with data for testing
    public LocalDBManager(){
        //init();
    }

    private void init(){
        //initializes the DB for debugging
        agencies.add(new Agency("Agency1", "USA", "New York", "Some Street", 654, "8455522320", "ezra250@gmail.com"));
        agencies.add(new Agency("CoolAgency", "Israel", "Jerusalem", "Rashi", 20, "026546455", "yosef@gmail.com"));
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        try {
            trips.add(new Trip(0, new URL("https://upload.wikimedia.org/wikipedia/commons/8/86/Trips_'05_-_Garibaldi_Lake_-_05_-_Black_Tusk.jpg"),
                    "Egypt", LocalDate.parse("25/8/2017", formatter), LocalDate.parse("10/9/2017", formatter), 499.9));
            trips.add(new Trip(1, new URL("https://c1.staticflickr.com/3/2892/9528631236_70c0608650_b.jpg"), "Jordan"
                    , LocalDate.parse("25/10/2017", formatter), LocalDate.parse("15/11/2017", formatter), 999.9));
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
        }
        users.add(new User("Ezra","asdf123"));
        users.add(new User("Yosef", "asdf321"));
    }



    /*
    adds a trip to the DB
    PARAMETERS
    Trip - must contain the values of the trips with the following filed names
    agencyID, imageURL, country, startDate (string - dd/MM/yyy), endDate (string - dd/MM/yyy), price
     */
    @Override
    public void addTrip(ContentValues trip) {
        tripsUpdates = true;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        try {
            trips.add(new Trip(
                    trip.getAsInteger("agencyID"),
                    new URL( trip.getAsString("imageURL")),
                    trip.getAsString("country"),
                    LocalDate.parse(trip.getAsString("startDate"), formatter),
                    LocalDate.parse(trip.getAsString("endDate"), formatter),
                    trip.getAsDouble("price")
                    ));
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public void addAgency(ContentValues agency) {
        agenciesUpdates = true;
        agencies.add(new Agency(
                agency.getAsString("name"),
                agency.getAsString("country"),
                agency.getAsString("city"),
                agency.getAsString("street"),
                agency.getAsInteger("houseNo"),
                agency.getAsString("phoneNumber"),
                agency.getAsString("email")
        ));
    }

    @Override
    public void addUser(ContentValues user) {
        users.add(new User(
                user.getAsString("userName"),
                user.getAsString("password")
        ));
    }

    @Override
    public boolean newTripsUpdates() {
        if(tripsUpdates){
            tripsUpdates = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean newAgenciesUpdates() {
        if(agenciesUpdates){
            agenciesUpdates = false;
            return true;
        }
        return false;
    }

    @Override
    /*
    Returns the agencies list as a cursor with the following headers
    "_ID", "Name", "Country", "City", "Street","HouseNumber", "PhoneNumber", "Email"
     */
    public Cursor getAgencies() {
        Agency agency;
        MatrixCursor agenciesCursor = new MatrixCursor(new String[] {"_ID", "Name", "Country", "City", "Street",
                "HouseNumber", "PhoneNumber", "Email"});
        for(int i = 0; i < agencies.size(); i++){
            agency = agencies.get(i);
            agenciesCursor.addRow(new Object[]{ agency.getID(), agency.getName(), agency.getCountry(), agency.getCity(),
                    agency.getStreet(), agency.getHouseNo(), agency.getPhoneNumber(), agency.getEmail()} );
        }
        return agenciesCursor;
    }

    @Override
    /*
    Returns the trips list as a cursor with the following headers
    "_ID", "AgencyID", "ImageURL", "Country", "StartDate","EndDate", "Price"
     */
    public Cursor getTrips() {
        Trip trip;
        MatrixCursor tripsCursor = new MatrixCursor(new String[] {"_ID", "AgencyID", "ImageURL", "Country", "StartDate",
                "EndDate", "Price"});
        for(int i = 0; i < trips.size(); i++){
            trip = trips.get(i);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            tripsCursor.addRow(new Object[]{ (int) i, trip.getAgencyID(), trip.getImage().toString(), trip.getCountry()
                    , trip.getStartDate().toString(formatter),  trip.getEndDate().toString(formatter), trip.getPrice()} );
        }
        return tripsCursor;
    }

    @Override
    public Cursor getUsers() {
        MatrixCursor usersCursor = new MatrixCursor(new String[]{"ID", "UserName", "Password"});
        for (User user:users){
            usersCursor.addRow(new Object[]{
                    user.getUserID(),
                    user.getUserName(),
                    user.getPassword()
            });
        }
        return usersCursor;
    }

    /*
    //CANCELED
    @Override
    public void addTrip(Trip trip) {
        tripsUpdates = true;
        trips.add(trip);
    }

    @Override
    public void addAgency(Agency agency) {
        agenciesUpdates = true;
        agencies.add(agency);
    }

    @Override
    public ArrayList<Agency> getAgencies() {
        return agencies;
    }

    @Override
    public ArrayList<Trip> getTrips() {
        return trips;
    }

    //checks if there's any new updates
    @Override
    public boolean anyUpdates() {
        if(localUpdate == null || localUpdate.before(ServerUpdate.getLastUpdate())){
            localUpdate = ServerUpdate.getLastUpdate();
            return true;
        }
        return false;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }
    */
}

package com.example.ezras.travelagencies.model.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;

import com.example.ezras.travelagencies.model.backend.DB_Manager;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ezra_Steinmetz on August 2016
 *
 * this is implementation of the DB manager using the remote server
 * for documentation of this class see {@link DB_Manager}
 */
public class ServerDBManager implements DB_Manager {

    private final static DateTimeFormatter serverFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final static DateTimeFormatter appFormat = DateTimeFormat.forPattern("dd/MM/yyyy");

    final private static String WEB_URL = "http://esteinme.vlab.jct.ac.il/";
    int lastAgenciesSize = 0;
    int lastTripsSize = 0;
    private final static String TAG = "ServerDBManager";

    /**
     * this is a generic function that runs the get request from the server
     * @param url - the url to run in the get request
     * @return - the answer of the server
     * @throws Exception
     */
    @NonNull
    private static String GET(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            return "";
        }
    }

    /**
     * this is a generic function that sends a post request to the server using the PHP request structure
     * @param url - the url to send the request
     * @param params - the parameters associated with the post request, a name (string) and a value (any object)
     * @return String: the response of the server
     * @throws IOException
     */
    @NonNull
    private static String POST(String url, Map<String, Object> params) throws IOException {

        //Convert Map<String,Object> into key=value&key=value pairs.
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
        // For POST only - END


        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else return "Error - response code: " + responseCode;
    }


    @Override
    public void addTrip(ContentValues trip) {

        try {
            //setting the parameters
            Map<String, Object> params = new LinkedHashMap<>();

            params.put("agency_id", trip.getAsInteger("agencyID"));
            params.put("image", trip.getAsString("image"));
            params.put("country", trip.getAsString("country"));
            params.put("start_date", LocalDate.parse(trip.getAsString("startDate"), appFormat).toString(serverFormat));
            params.put("end_date", LocalDate.parse(trip.getAsString("endDate"), appFormat).toString(serverFormat));
            params.put("price", trip.getAsDouble("price"));

            //sending the post request
            String results = POST(WEB_URL + "addTrip.php", params);
            /*
            ### Since a server can't throw an exception I used the word "Error" as a prefix to the returned string
                in order to signal an error (another way is to signal success rather than an error and to throw an
                exception in any cas that there wasn't success).
             */
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void addAgency(ContentValues agency) {
        try {
            //setting the arguments
            Map<String, Object> params = new LinkedHashMap<>();


            params.put("name", agency.getAsString("name"));
            params.put("country", agency.getAsString("country"));
            params.put("city", agency.getAsString("city"));
            params.put("street", agency.getAsString("street"));
            params.put("house_no", agency.getAsInteger("houseNo"));
            params.put("phone_number", agency.getAsString("phoneNumber"));
            params.put("email", agency.getAsString("email"));
            //sending the post request
            String results = POST(WEB_URL + "addAgency.php", params);
            /*
            ### Since a server can't throw an exception I used the word "Error" as a prefix to the returned string
                in order to signal an error (another way is to signal success rather than an error and to throw an
                exception in any cas that there wasn't success).
             */
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void addUser(ContentValues user) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put("user_name", user.getAsString("userName"));
            params.put("password", user.getAsString("password"));

            String results = POST(WEB_URL + "addUser.php", params);
            /*
            ### Since a server can't throw an exception I used the word "Error" as a prefix to the returned string
                in order to signal an error (another way is to signal success rather than an error and to throw an
                exception in any cas that there wasn't success).
             */
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public boolean newTripsUpdates() throws Exception {
        int size = new JSONObject(GET(WEB_URL + "/getTripsSize.php")).getInt("tripsSize");
        if (lastTripsSize != size) {
            lastTripsSize = size;
            return true;
        }
        return false;
    }

    @Override
    public boolean newAgenciesUpdates() throws Exception {
        int agenciesSize = new JSONObject(GET(WEB_URL + "/getAgenciesSize.php")).getInt("agenciesSize");
        if (agenciesSize != lastAgenciesSize) {
            lastAgenciesSize = agenciesSize;
            return true;
        }
        return false;
    }

    @Override
    public Cursor getAgencies() throws Exception {

        MatrixCursor agenciesCursor = new MatrixCursor(new String[]{"_ID", "Name", "Country", "City", "Street", "HouseNumber", "PhoneNumber", "Email"});
        JSONArray array = new JSONObject(GET(WEB_URL + "/getAgencies.php")).getJSONArray("agencies");
        for (int i = 0; i < array.length(); i++) {
            JSONObject agency = array.getJSONObject(i);
            agenciesCursor.addRow(new Object[]{
                    agency.getInt("id"),
                    agency.getString("name"),
                    agency.getString("country"),
                    agency.getString("city"),
                    agency.getString("street"),
                    agency.getInt("house_no"),
                    agency.getString("phone_number"),
                    agency.getString("email")
            });
        }
        return agenciesCursor;
    }

    @Override
    public Cursor getTrips() throws Exception {
        MatrixCursor tripsCursor = new MatrixCursor(new String[]{"AgencyID", "ImageURL", "Country", "StartDate",
                "EndDate", "Price"});
        JSONArray array = new JSONObject(GET(WEB_URL + "/getTrips.php")).getJSONArray("trips");
        for (int i = 0; i < array.length(); i++) {
            JSONObject trip = array.getJSONObject(i);
            tripsCursor.addRow(new Object[]{
                    trip.getInt("agency_id"),
                    trip.getString("image"),
                    trip.getString("country"),
                    LocalDate.parse(trip.getString("start_date"), serverFormat).toString(appFormat),
                    LocalDate.parse(trip.getString("end_date"), serverFormat).toString(appFormat),
                    trip.getDouble("price")
            });
        }
        return tripsCursor;
    }

    @Override
    public Cursor getUsers() throws Exception {
        MatrixCursor users = new MatrixCursor(new String[]{"UserID", "UserName", "Password"});
        JSONArray array = new JSONObject(GET(WEB_URL + "/getUsers.php")).getJSONArray("users");
        for (int i = 0; i < array.length(); i++) {
            JSONObject user = array.getJSONObject(i);
            users.addRow(new Object[]{
                    user.getInt("user_id"),
                    user.getString("user_name"),
                    user.getString("password")
            });
        }
        return users;


    }
}

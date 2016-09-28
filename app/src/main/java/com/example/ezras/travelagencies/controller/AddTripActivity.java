package com.example.ezras.travelagencies.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DB_Manager;
import com.example.ezras.travelagencies.model.CustomAsyncTask;
import com.example.ezras.travelagencies.model.entities.Agency;
import com.example.ezras.travelagencies.model.entities.Trip;
import com.example.ezras.travelagencies.R;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.DatePicker.*;

/**
 * @author Ezra Steinmetz
 *
 * this activity gets input from the user and adds a new trip with the given input
 */
public class AddTripActivity extends AppCompatActivity {
    private DB_Manager manager = DBManagerFactory.getManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        final Spinner agencySpinner = (Spinner) findViewById(R.id.agencyID_spinner);

        //Gets the agencies list, and adds them to the agencySpinner view
        //Please notice that the doInBackground isn't covered by the try-catch block
        new CustomAsyncTask<List<Agency>>(getApplicationContext(), " retrieve list of agencies", (ProgressBar) findViewById(R.id.agenciesSpinner_progressBar)) {
            @Override
            //get the agencies
            protected List<Agency> doInBackground(Void... voids) {
                List<Agency> agencies = new ArrayList<Agency>();
                Cursor cursor = null;
                try {
                    //pseudo progress
                    for (int i = 0; i < 11; i++) {
                        publishProgress(i * 10);
                        SystemClock.sleep(100);
                    }
                    cursor = getContentResolver().query(Uri.parse("content://com.example.ezras.travelagencies/agencies"), null, null, null, null);
                    if(cursor != null) {
                        while (cursor.moveToNext()) {
                            agencies.add(new Agency(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                                    , cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7)));
                        }
                    }
                } catch (Exception e) {//save the exception to handle later
                    this.e = e;
                }

                if (cursor != null) {
                    cursor.close();
                }
                return agencies;
            }

            @Override
            //set the agencies list to the spinner's adapter
            protected void onPostExecute(List<Agency> results) {
                super.onPostExecute(results);
                ArrayAdapter<Agency> adapter = new ArrayAdapter<Agency>(agencySpinner.getContext(), android.R.layout.simple_spinner_item, results);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                agencySpinner.setAdapter(adapter);
            }
        }.execute();
    }

    /**
     * add trip click event - adds a new trip with user's input
     * @param view
     */
    public void addTrip(View view) {

        //gets the views that contains user's input
        EditText startDate = (EditText) findViewById(R.id.startDate_EditText);
        EditText endDate = (EditText) findViewById(R.id.EndDate_EditText);
        EditText price = (EditText) findViewById(R.id.TripPrice_EditText);
        EditText country = (EditText) findViewById(R.id.TripCountry_EditText);
        EditText imgURL = (EditText) findViewById(R.id.ImgURL_EditText);

        //Tries to create a trip instance out of the user's input, and then add it via the DB manager
        //Please notice that the doInBackground isn't covered by the try-catch block
        try {
            final Spinner agencySpinner = (Spinner) findViewById(R.id.agencyID_spinner);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            final ContentValues trip = new ContentValues();
            trip.put("agencyID", ((Agency) agencySpinner.getSelectedItem()).getID());
            trip.put("image", imgURL.getText().toString());
            trip.put("country", country.getText().toString());
            trip.put("startDate", startDate.getText().toString());
            trip.put("endDate", endDate.getText().toString());
            trip.put("price", Double.parseDouble(price.getText().toString()));

            new CustomAsyncTask<Void>(this, " add a new trip", (ProgressBar) findViewById(R.id.addTripProgresBar)) {
                @Override
                protected Void doInBackground(Void... voids) {
                    /*
                    ### The do-in-background part (which is the asynchronous part) isn't covered by the try catch that surrounds the async task
                     therefore you need to create an inner try-catch block in order to catch exceptions within the do-in-background function
                     */
                    try {
                        getContentResolver().insert(Uri.parse("content://com.example.ezras.travelagencies/trips"), trip);
                        //pseudo progress
                        for (int i = 0; i < 11; i++) {
                            publishProgress(i * 10);
                            SystemClock.sleep(100);
                        }
                    } catch (Exception e) {
                        this.e = e;//save the exception to handle later in onPostExecute
                    }
                    return null;
                }

                protected void onPostExecute(Void v) {
                    super.onPostExecute(v);
                    if (e == null) {
                        finish();
                    }
                }
            }.execute();
        } catch (Exception ex) {//for any exception that occurred in the rest of the function
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Home button onClick, closes the current activity and returns to home activity
    public void homeButtonClick(View view) {
        finish();
    }
}

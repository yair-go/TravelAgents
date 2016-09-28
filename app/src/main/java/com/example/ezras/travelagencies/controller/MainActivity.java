package com.example.ezras.travelagencies.controller;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ezras.travelagencies.model.backend.DBUpdateCheckerService;
import com.example.ezras.travelagencies.model.backend.SaveSharedPreference;
import com.example.ezras.travelagencies.model.entities.User;
import com.example.ezras.travelagencies.R;

/**@author Ezra Steinmetz
 *
 * min API for app - 21
 *
 * this is the main activity, although this is not the launcher activity (i.e. the first activity to show
 * on launch), the login activity is the first screen that appears on launch.
 *
 *the main activity lets the user to open the add-trip activity and add-agency dialog and add a new
 * trip/dialog.
 * the activity also provides an exit button to exit the app.
 */
public class MainActivity extends AppCompatActivity {

    //private static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets an hello message with the user's name
        User user = LoginActivity.getCurrentUser();
        if(user != null){
            TextView hello = (TextView) findViewById(R.id.hello_textView);
            hello.setText(String.format("%s%s%s", getString(R.string.hello), user.getUserName(), getString(R.string.screamer)));
        }
        //sets add agency button on click
        Button addAgencyButton = (Button) findViewById(R.id.addAgency_openDialogButton);
        addAgencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAgency_buttonClick(view);
            }
        });

        //setting exit button on click - exits the app
        findViewById(R.id.exitAppButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        //setting the static progress bar to be the progress bar of the current instance
        //progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
    }

    /*
    called when the add agency button is clicked, opens the add agency dialog
     */
    public void addAgency_buttonClick(View view){
        FragmentManager FM = getFragmentManager();
        AddAgency_dialog dialog = new AddAgency_dialog();
        dialog.show(FM, "AddAgency Dialog");
    }

    /*
    Called when the add trip button is clicked, starts the add trip activity
     */
    public void addTrip_buttonClick(View view) {
        Intent TripIntent = new Intent(this, AddTripActivity.class);
        startActivity(TripIntent);
    }
    /** //a getter for the static progress bar for the second activity
    public static ProgressBar getProgressBar(){
        return progressBar;
    }*/

    /**
     *logout click method
     *Finishes the current activity, clears login data and opens the login activity
     */
    public void logout(View v){
        //clearing login data
        SaveSharedPreference.clear(this);
        //finishes the current activity
        finish();
        //starts the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /*
    TODO

    Change getSize php implementation
     */

    /*
    ### A service or an async task can't produce a Toast message since they aren't on the main thread,
        in the case of async task you can do it on the onPostExecute, in the case of service
         or if you must do it in the doInBackground - check up this link: http://stackoverflow.com/a/5420929/4483033 .
        Please notice that for debugging purposes you can just use the log (Log.v function)

    ### In the case of Toast messages and async tasks, it's important to make sure that you've showed them
        (.show() with Toasts) or executed them (.execute()), in both cases the IDE should warn you about that but
        you can often miss that warning.

    ### If you want to use the internet in your application you must add the internet permission in the manifest xml file.

    ### Watch out from the Ctrl + Y keyboard shortcuts, in many programs it's used to redo changes, here it's just deleting a line.
        It's better to just cancel it from the keymap section in the settings.

    ### ctrl + F will only search in the current document, in order to search the entire project you can either use
        'find usage' (clicking on an object and then alt + f7) or 'find in path' - click on a folder in the project tab and
        then press ctrl + shift + f.

    ### Sometimes debugging/running an application that calls the content provider of the other wouldn't display all of the details about exception that occurred in the second
        app and wouldn't let you debug the code inside the second app, the simplest way to solve that is to insert code to the second app
        that calls it's own content provider - this way you can simulate the call from the first application and debug the content provider.
     */
}

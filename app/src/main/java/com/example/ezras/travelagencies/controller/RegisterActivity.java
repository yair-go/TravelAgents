package com.example.ezras.travelagencies.controller;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezras.travelagencies.model.CustomAsyncTask;
import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DB_Manager;
import com.example.ezras.travelagencies.model.entities.User;
import com.example.ezras.travelagencies.R;

/**
 * @author Ezra Steinmetz
 *
 * this activity lets the user to add a new {@link User} to the DB.
 * the input fields for the new user are - user name and password
 */

public class RegisterActivity extends AppCompatActivity {

    private DB_Manager manager = DBManagerFactory.getManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        EditText passwordET = (EditText) findViewById(R.id.pass_input);
        //this changes the bottom-right button of the keyboard to show the word 'register'
        passwordET.setImeActionLabel("register", KeyEvent.KEYCODE_ENTER);
        //this changes the bottom-right button click event to the register function
        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                register();
                return true;
            }
        });
        //setting the click function for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    /**
     * this function registers the user from the input that the user entered into as
     * user name and password
     */
    private void register() {
        //getting the input data from the EditTexts
        String userName = ((EditText) findViewById(R.id.userName_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.pass_input)).getText().toString();
        final ContentValues user = new ContentValues();
        user.put("userName", userName);
        user.put("password", password);
        //adding the user in an async task
        try {
            new CustomAsyncTask<Void>(this, " register " + userName, (ProgressBar) findViewById(R.id.registerProgressBar)) {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        getContentResolver().insert(Uri.parse("content://com.example.ezras.travelagencies/users"), user);
                    }catch (Exception e){
                        this.e = e;
                    }
                    //pseudo progress
                    for(int i = 0; i < 11; i++){
                        publishProgress( i * 10);
                        SystemClock.sleep(70);
                    }
                    return null;
                }
                protected void onPostExecute(Void v){
                    super.onPostExecute(v);
                    //closes the activity only if there was no exception during the adding process
                    if(e == null){
                        finish();
                    }
                }
            }.execute();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}

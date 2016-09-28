package com.example.ezras.travelagencies.controller;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezras.travelagencies.model.CustomAsyncTask;
import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DBUpdateCheckerService;
import com.example.ezras.travelagencies.model.backend.DB_Manager;
import com.example.ezras.travelagencies.model.backend.SaveSharedPreference;
import com.example.ezras.travelagencies.model.entities.User;
import com.example.ezras.travelagencies.R;

import java.util.ArrayList;

/**
 * @author Ezra Steinmetz
 *
 * this is the login activity, which is also the first activity to show on launching.
 * the activity first checks if there's any user logged in to the app and if there is the activity
 * will set him as the current user and log in to {@link MainActivity}, otherwise the user will need
 * to log in with the user name and password of an existing user.
 * the user also can navigate to the {@link RegisterActivity} and add there a new user and than
 * come back to here and register with the new user name and password.
 */

public class LoginActivity extends AppCompatActivity {
    private DB_Manager manager = DBManagerFactory.getManager();
    //shows the current user logged in for other activities
    private static User currentUser;
    private ProgressBar progressBar;

    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        //starting update checker service
        startService(new Intent(this, DBUpdateCheckerService.class));

        TextView passInput = (TextView) findViewById(R.id.pass_input);
        /** sets the right-bottom button of the keyboard to the login-function
         * (i.e. when the user will press that button the {@link #logIn(View)} function will
         * run.
         */
        passInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                logIn(null);
                return true;
            }
        });
        //sets the right-bottom button text to 'login'
        passInput.setImeActionLabel("login", KeyEvent.KEYCODE_ENTER);

        //test();
        //checking if there's any user login saved on this device
        final int ID = SaveSharedPreference.getUserID(getApplicationContext());
        if (ID != -1) {
            /**in case that there is a user saved in the shared preference the app will find that
             * user in the list and set him as the {@link currentUser}.
             */
            try {
                new CustomAsyncTask<Boolean>(this, " checking for login saved data", progressBar) {

                    @Override
                    public Boolean doInBackground(Void... params) {
                        Cursor cursor = null;
                        //a separate try-catch for the doInBackground
                        try {

                             cursor = getContentResolver().query(Uri.parse("content://com.example.ezras.travelagencies/users"), null, null, null, null);
                            if(cursor == null){
                                throw new Exception("cursor is null");
                            }
                            while (cursor.moveToNext()) {
                                if (cursor.getInt(0) == ID) {
                                    currentUser = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                                    cursor.close();
                                    return true;
                                }
                            }
                        }catch (Exception e){
                            this.e = e;
                        }
                        if(cursor != null)
                            cursor.close();
                        return false;
                    }

                    @Override
                    public void onPostExecute(Boolean success) {
                        super.onPostExecute(success);
                        if (success) {
                            //in case of success finishes the current activity and starts main
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            //if the stored user ID doesn't match the one of the existing user's ID - clearing the stored user ID
                            Toast.makeText(getApplicationContext(), "An error occurred while trying to retrieve saved login data, clearing login data...",
                                    Toast.LENGTH_LONG).show();
                            SaveSharedPreference.clear(getApplicationContext());
                        }
                    }
                }.execute();
            }catch (Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    //something to do with the graphics probably
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * the login button event - this will check if the user name and password match any existing
     * user and if it is will login the user into the {@link MainActivity}
     * @param v
     */
    public void logIn(View v) {
        //since a views cannot be accessed from an AsyncTask we need to define final variables with the necessary values
        final String userName = ((EditText) findViewById(R.id.userName_input)).getText().toString();
        final String password = ((EditText) findViewById(R.id.pass_input)).getText().toString();
        final boolean stayLoggedIn = ((CheckBox) findViewById(R.id.stayLogedIn)).isChecked();

        //Checks if the entered login user name and password matches any of the existing users
        try {
            new CustomAsyncTask<Boolean>(this, " verify login information", progressBar) {
                @Override
                protected Boolean doInBackground(final Void... params) {
                    try {

                        Cursor cursor = getContentResolver().query(Uri.parse("content://com.example.ezras.travelagencies/users"), null, null, null, null);
                        if(cursor == null){
                            throw new Exception();
                        }
                        while (cursor.moveToNext()) {
                            if (cursor.getString(1).equalsIgnoreCase(userName) &&
                                    cursor.getString(2).equalsIgnoreCase(password)) {
                                //in case the user asks to stay logged in - save the user ID in the
                                //shared preference of this app
                                if (stayLoggedIn)
                                    SaveSharedPreference.setUserID(LoginActivity.this, cursor.getInt(0));
                                //set the current user to be the logged in user
                                currentUser = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                                cursor.close();
                                return true;
                            }
                        }
                    }catch (Exception e){
                        this.e = e;
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    super.onPostExecute(success);
                    if (success) {
                        //in case of successful login - finishes the current activity and starts a new one
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        //in case of login failure - posting a message to the user
                        Toast.makeText(getApplicationContext(), "User name or password are incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Register button onClick - starts the register activity
    public void startRegisterActivity(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


}

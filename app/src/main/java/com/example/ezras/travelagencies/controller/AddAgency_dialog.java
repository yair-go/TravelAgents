package com.example.ezras.travelagencies.controller;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DB_Manager;
import com.example.ezras.travelagencies.model.CustomAsyncTask;
import com.example.ezras.travelagencies.model.entities.Agency;
import com.example.ezras.travelagencies.R;

import java.net.URI;

/**@author Ezra Steinmetz
 *
 * this class represents the add agency dialog, the dialog gets input from the user
 * and adds a new agency with the given input.
 */
public class AddAgency_dialog extends DialogFragment {

    private DB_Manager manager = DBManagerFactory.getManager();

    private EditText name;
    private EditText country;
    private EditText city;
    private EditText street;
    private EditText houseNumber;
    private EditText phoneNumber;
    private EditText email;

    public AddAgency_dialog(){}

    /**
     * this function runs when the dialog is created, the returned view will be the dialog's view
     * @param inflater     LayoutInflater: The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container     ViewGroup: If non-null, this is the parent view that the fragment's UI should be attached to.
     *                      The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState     Bundle: If non-null, this fragment is being re-constructed from
     *                               a previous saved state as given here.
     * @return the dialog view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);



        /*
        ### Please notice that any operation on the inner views must be done on the view that we will return later,
        working on one view and returning another will make 2 instances of the dialog view that has nothing
        to do with each other.
         */
        View view = inflater.inflate(R.layout.dialog_add_agency, container, false); //to find a dialog view by id

        //saving the different views of the dialog to use later
        name = (EditText) view.findViewById(R.id.NameET);
        country = (EditText) view.findViewById(R.id.countryEditText);
        city = (EditText) view.findViewById(R.id.cityEditText);
        street = (EditText) view.findViewById(R.id.streetEditText);
        houseNumber = (EditText) view.findViewById(R.id.houseNumberEditText);
        phoneNumber = (EditText) view.findViewById(R.id.phoneNumber_ET);
        email = (EditText) view.findViewById(R.id.email_EditText);

        //setting the home button onClick - closes the dialog and returns to home
        ImageButton homeButton = (ImageButton) view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //setting the add button click event - adds a new agency and closed the dialog on success
        Button addButton = (Button) view.findViewById(R.id.addAgency_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick(view);
            }
        });
        //***
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        //Graphics' settings, sets the width and height of the dialog window
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

    }
/*
The add agency button onClick function
Tries to add the agency with user's input and closes the dialog on success
 */
    public void onAddClick(View view) {
        //
        try {
            //creating a content value that contains the new agency's details
            final ContentValues agency = new ContentValues();
            agency.put("name", name.getText().toString());
            agency.put("country", country.getText().toString());
            agency.put("city",city.getText().toString());
            agency.put("street", street.getText().toString());
            agency.put("houseNo", Integer.parseInt(houseNumber.getText().toString()));
            agency.put("phoneNumber",  phoneNumber.getText().toString());
            agency.put("email", email.getText().toString());
            //adding the new agency in an async task via the content provider
            new CustomAsyncTask<Void>(this.getActivity(), " add a new agency", (ProgressBar) getDialog().findViewById(R.id.addAgencyProgressBar)){
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        getActivity().getContentResolver().insert(Uri.parse("content://com.example.ezras.travelagencies/agencies"), agency);
                        //pseudo progress
                        for (int i = 0; i < 11; i++) {
                            publishProgress(i * 10);
                            SystemClock.sleep(500);
                        }
                    }catch (Exception e){//save the exception to handle later in onPostExecute
                        this.e = e;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void v){
                    super.onPostExecute(v);//this will show a message about the exception, if there is
                    //in case of success (no exception) close the dialog
                    if(e == null){
                        //### To close a dialog you need to use 'dismiss' rather than 'finish'
                        getDialog().dismiss();
                    }
                }
            }.execute();
        } catch (Exception ex) {//in case of any exception in the rest of the function show toast3
                                //with the exception's details
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}

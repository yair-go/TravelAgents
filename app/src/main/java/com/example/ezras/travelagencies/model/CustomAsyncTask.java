package com.example.ezras.travelagencies.model;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ezras.travelagencies.controller.MainActivity;
import com.example.ezras.travelagencies.model.backend.DBManagerFactory;
import com.example.ezras.travelagencies.model.backend.DB_Manager;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Ezra_Steinmetz on August 2016
 *
 * this is a custom implementation of the async task, the class takes care of
 * the publish progress function and prints an error message in case of an exception (although the
 * programmer still needs to save the exception in  {@link #doInBackground(Void...)})
 */
public class CustomAsyncTask<T> extends AsyncTask<Void, Integer, T>{
    private DB_Manager manager = DBManagerFactory.getManager();
    private ProgressBar progressBar;
    protected Exception e;
    private String taskDescription = "";
    private Context context;

    /**
     * constructor.
     * @param context a context to use to post an error message in {@link #onPostExecute(Object)}
     * @param taskDescription - a description of the task - used in {@link #onPostExecute(Object)}
     * @param progressBar - the progress bar to use while publishing progress
     */
    public CustomAsyncTask(Context context, String taskDescription, ProgressBar progressBar){
        this.progressBar = progressBar;
        this.context = context;
        this.taskDescription = taskDescription;
    }
    @Override
    protected void onPreExecute(){
        //showing the progress bar
        progressBar.setVisibility(VISIBLE);
    }
    @Override
    protected void onPostExecute(T success){
        //posting a toast in case of error
        if(e != null) {
            Toast.makeText(context.getApplicationContext(), "An error occurred while trying to " + taskDescription + ":\n\t" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //hiding the progress bar
        progressBar.setVisibility(INVISIBLE);
    }

    /**
     * a void implementation of this function, the programmer should override this when
     * building a new instance of this class
     */
    @Override
    protected T doInBackground(Void... voids) {
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... progress){
        //publishing progress to the progress bar
        progressBar.setProgress(progress[0]);
    }
}

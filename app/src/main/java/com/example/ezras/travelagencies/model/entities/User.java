package com.example.ezras.travelagencies.model.entities;

/**
 * Created by Ezra_Steinmetz on August 2016
 *
 * The class represents a user with a user-ID, user-name and password
 * The user-ID is automatically assigned with serial numbers
 */
public class User {
    private int userID;
    private String userName;
    private String password;
    private static int IDStamper = 0;

    public User(int userID, String userName, String password) {
        if(userID < 0){
            throw new IllegalArgumentException(userID + " isn't a velid user ID");
        }

        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password) {
        userID = IDStamper++;
        this.userName = userName;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }

}

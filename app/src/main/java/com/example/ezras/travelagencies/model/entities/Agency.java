package com.example.ezras.travelagencies.model.entities;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by Ezra_Steinmetz on 05-Aug-16.
 *
 * The class represents a travel agency
 * FIELDS
 * name - the name of the agency
 * address - the address of the agency (as presented as detailed in the address class
 * phoneNumber - the phone number of the agency
 * email - the email of the agency
 */
public class Agency implements Serializable{

    private static int IDStamper = 0;
    private int ID;
    private String name;
    private String country;
    private String city;
    private String street;
    private int houseNo;
    private String phoneNumber;
    private String email;


    public Agency(int ID, String name, String country, String city, String street, int houseNo, String phoneNumber, String email) {
        if (phoneNumber.length() != 10 && phoneNumber.length() != 7 && phoneNumber.length() != 9)
            throw new IllegalArgumentException("A phone number cannot be " + phoneNumber.length() + " numbers long");
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9')
                throw new IllegalArgumentException("Phone number could contain only numbers");
        }
        if (!isValidEmail(email))
            throw new IllegalArgumentException("\"" + email + "\" isn't a valid email address");
        if (houseNo <= 0)
            throw new IllegalArgumentException("House number has to be a positive number");
        if(ID < 0){
            throw new IllegalArgumentException(ID + " isn't a valid ID number");
        }
        this.ID = ID;
        this.name = name;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNo = houseNo;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Agency(String name, String country, String city, String street, int houseNo, String phoneNumber, String email) {
        if (phoneNumber.length() != 10 && phoneNumber.length() != 7 && phoneNumber.length() != 9)
            throw new IllegalArgumentException("A phone number cannot be " + phoneNumber.length() + " numbers long");
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9')
                throw new IllegalArgumentException("Phone number could contain only numbers");
        }
        if (!isValidEmail(email))
            throw new IllegalArgumentException("\"" + email + "\" isn't a valid email address");
        if (houseNo <= 0)
            throw new IllegalArgumentException("House number has to be a positive number");
        this.ID = IDStamper++;
        this.name = name;
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNo = houseNo;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


    protected static int getIDstamper() {
        return IDStamper;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNo() {
        return houseNo;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    /*
    set function for the phone number, makes sure that the phone is 7 or 10 digits
    PARAMETERS
    phoneNumber - a string with the length of 7 or 10, must contain numbers solely
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 10 && phoneNumber.length() != 7)
            throw new IllegalArgumentException("A phone number cannot be " + phoneNumber.length() + " numbers long");
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9')
                throw new IllegalArgumentException("Phone number could contain only numbers");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (!isValidEmail(email))
            throw new IllegalArgumentException("\"" + email + "\" isn't a valid email address");
        this.email = email;
    }

    /*
    The function checks weather 'email' is a valid email address,
    some simplifications of the rules were made in order to keep the job simple (ordinary email addresses will still pass the check though)
    The function uses the regex library of java.
    PARAMETERS
    email - the string to check
    RETURN VALUE
    bool - true if the string is a valid email address, false otherwise
     */
    private Boolean isValidEmail(String email) {
        String p = "([a-zA-Z0-9]+\\x2E)*[a-zA-Z0-9]+@(([a-zA-Z0-9]+\\x2D)*[a-zA-Z0-9]+\\x2E)+[a-zA-Z0-9]+[a-zA-Z0-9]";
        return Pattern.matches(p, email);
    }

    @Override
    public String toString(){
        return name;
    }

}

package com.example.ezras.travelagencies.model.entities;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by Ezra_Steinmetz on 05-Aug-16.
 *
 * The class represents an object of a trip.
 * FIELDS
 * agencyID - the ID of the agency that provides the trip - must be an ID of an existing agency
 * country - the country where the trip takes place
 * image - a URL that contains an image from the location/trip
 * startDate - the date the trip starts (must be later than current date)
 * endDate - the date trip ends (must be after start date)
 */
public class Trip implements Serializable{

    // LocalDate uses joda-time, to see how to add it to your project visit the following link
    // http://stackoverflow.com/a/38030885/4483033

    private int agencyID;
    private URL image;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;

    public Trip(int agencyID, URL image, String country, LocalDate startDate, LocalDate endDate, Double price) {
        if(agencyID > Agency.getIDstamper() || agencyID < 0)
            throw new IllegalArgumentException(agencyID + " isn't a valid agency ID");
        if(startDate.isBefore(new LocalDate()))
            throw new IllegalArgumentException("Start date cannot be before the current date");
        if(endDate.isBefore(startDate))
            throw new IllegalArgumentException("The end date cannot be before the start date");
        if(price < 0)
            throw new IllegalArgumentException("Price must be a positive number");
        this.agencyID = agencyID;
        this.image = image;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public int getAgencyID() {
        return agencyID;
    }

    public URL getImage() {
        return image;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getPrice() {
        return price;
    }
}

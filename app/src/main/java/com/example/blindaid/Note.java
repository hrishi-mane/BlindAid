package com.example.blindaid;

import java.util.Date;

public class Note {


    private String source;
    private String destination;
    private String bus_number;
    private Date arrival_time;
    private Date departure_time;
    private String travel_time;
    private Double latitude;
    private Double longitude;

    public Note(){
        //public no-arg constructor needed
    }

    public Note(String source, String destination, String bus_number, Date arrival_time,
                Date departure_time, String travel_time,Double latitude, Double longitude ){
        this.source = source;
        this.destination = destination;
        this.bus_number = bus_number;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.travel_time = travel_time;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getBus_number() {
        return bus_number;
    }

    public Date getArrival_time() {
        return arrival_time;
    }

    public Date getDeparture_time() {
        return departure_time;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

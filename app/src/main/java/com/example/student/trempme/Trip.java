package com.example.student.trempme;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Trip {
    private String tripId;
    private String fromId;
    private String fromName;
    private String toId;
    private String toName;
    private long departureTime;
    private int numOfAvailableSits;
    private List<Tremp> Tremps;
    private String userId;

    public Trip(String tripId, String fromId,String fromName,String toId,String toName, long departureTime, int numOfAvailableSits,String userId, List<Tremp> Tremps ){
        this.fromId=fromId;
        this.fromName=fromName;
        this.toId=toId;
        this.toName=toName;
        this.numOfAvailableSits=numOfAvailableSits;
        this.departureTime=departureTime;
        this.userId=userId;
        this.Tremps=Tremps;
        this.tripId=tripId;
    }

    public Trip(){

    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public int getNumOfAvailableSits() {
        return numOfAvailableSits;
    }

    public void setNumOfAvailableSits(int numOfAvailableSits) {
        this.numOfAvailableSits = numOfAvailableSits;
    }

    public List<Tremp> getTremps() {
        return Tremps;
    }

    public void setTremps(List<Tremp> tremps) {
        Tremps = tremps;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrempTime(){
        long ms = this.departureTime;
        Date date = new Date(ms);
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
        Log.w("getTrempTime",dateformat.format(date));
        return dateformat.format(date);
    }
    public String getTrempDate(){
        long ms = this.departureTime;
        Date date = new Date(ms);
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd");
        Log.w("getTrempDate",dateformat.format(date));
        return dateformat.format(date);
    }
}

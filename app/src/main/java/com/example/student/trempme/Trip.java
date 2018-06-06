package com.example.student.trempme;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Trip {
    @NonNull private String tripId;
    private String fromId;
    private String toId;
    private long departureTime;
    private int numOfAvailableSits;
    private List<Tremp> tremps;
    private String userId;

    public Trip(String tripId, String fromId,String toId, long departureTime, int numOfAvailableSits,String userId, List<Tremp> tremps ){
        this.fromId=fromId;
        this.toId=toId;
        this.numOfAvailableSits=numOfAvailableSits;
        this.departureTime=departureTime;
        this.userId=userId;
        this.tripId=tripId;
        this.tremps=tremps;
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


    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
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
        return tremps;
    }

    public void setTremps(List<Tremp> tremps) {
        this.tremps = tremps;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getTripTime(){
//        long ms = this.departureTime;
//        Date date = new Date(ms);
//        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
//        Log.w("getTrempTime",dateformat.format(date));
//        return dateformat.format(date);
//    }
//    public String getTripDate(){
//        long ms = this.departureTime;
//        Date date = new Date(ms);
//        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd");
//        Log.w("getTrempDate",dateformat.format(date));
//        return dateformat.format(date);
//    }
//
//    public void serTripTime(String time){}
//
//    public void serTripDate(String Date){}
//

}

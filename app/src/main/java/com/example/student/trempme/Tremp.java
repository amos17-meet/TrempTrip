package com.example.student.trempme;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tremp {

    private String fromId;
    private String fromName;
    private String toId;
    private String toName;
    private long departureTime;
    private int numOfAvailableSits;
    private String userId;

    public Tremp(String fromId,String fromName,String toId,String toName,long departureTime, int numOfAvailableSits,String userId ){
        this.fromId=fromId;
        this.fromName=fromName;
        this.toId=toId;
        this.toName=toName;
        this.numOfAvailableSits=numOfAvailableSits;

        this.departureTime=departureTime;
        this.userId=userId;
    }

    public Tremp(){

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

    public void setTrempTime(String time){

    }
    public void setTrempDate(String date){

    }

}

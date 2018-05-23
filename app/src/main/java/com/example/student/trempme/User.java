package com.example.student.trempme;

import java.util.List;

public class User {
    private String fullName;
    private String email;
    private String password;
    private String groupId;
    private List<String> myTrempsIds;
    private List<String> myTripsIds;

    public User(String groupId, String email, String password, String fullName,List<String> myTrempsIds,List<String> myTripsIds)
    {
        this.fullName=fullName;
        this.email=email;
        this.password=password;
        this.groupId=groupId;
        this.myTrempsIds=myTrempsIds;
        this.myTripsIds=myTripsIds;
    }
    public User(){

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMyTrempsIds() {
        return myTrempsIds;
    }

    public void setMyTrempsIds(List<String> myTrempsIds) {
        this.myTrempsIds = myTrempsIds;
    }

    public List<String> getMyTripsIds() {
        return myTripsIds;
    }

    public void setMyTripsIds(List<String> myTripsIds) {
        this.myTripsIds = myTripsIds;
    }
}

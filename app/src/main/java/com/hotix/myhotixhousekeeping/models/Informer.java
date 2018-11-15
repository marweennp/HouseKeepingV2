package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Informer {

    @SerializedName("Room")
    @Expose
    private String room;
    @SerializedName("Operation")
    @Expose
    private String operation;
    @SerializedName("User")
    @Expose
    private String user;
    @SerializedName("Poste")
    @Expose
    private String poste;
    @SerializedName("Date")
    @Expose
    private String date;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Arrival {

    @SerializedName("SocId")
    @Expose
    private Integer socId;
    @SerializedName("Resa")
    @Expose
    private Integer resa;
    @SerializedName("Client")
    @Expose
    private String client;
    @SerializedName("DateArrive")
    @Expose
    private String dateArrive;
    @SerializedName("DateDepart")
    @Expose
    private String dateDepart;
    @SerializedName("Room")
    @Expose
    private String room;
    @SerializedName("Comment")
    @Expose
    private String comment;

    public Integer getSocId() {
        return socId;
    }

    public void setSocId(Integer socId) {
        this.socId = socId;
    }

    public Integer getResa() {
        return resa;
    }

    public void setResa(Integer resa) {
        this.resa = resa;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(String dateArrive) {
        this.dateArrive = dateArrive;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
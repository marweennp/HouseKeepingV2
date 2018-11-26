package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Arrangement")
    @Expose
    private String arrangement;
    @SerializedName("PremierService")
    @Expose
    private String premierService;
    @SerializedName("DernierService")
    @Expose
    private String dernierService;
    @SerializedName("ResaId")
    @Expose
    private Integer resaId;
    @SerializedName("ResaPaxId")
    @Expose
    private Integer resaPaxId;
    @SerializedName("ResaGroupeId")
    @Expose
    private Integer resaGroupeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public String getPremierService() {
        return premierService;
    }

    public void setPremierService(String premierService) {
        this.premierService = premierService;
    }

    public String getDernierService() {
        return dernierService;
    }

    public void setDernierService(String dernierService) {
        this.dernierService = dernierService;
    }

    public Integer getResaId() {
        return resaId;
    }

    public void setResaId(Integer resaId) {
        this.resaId = resaId;
    }

    public Integer getResaPaxId() {
        return resaPaxId;
    }

    public void setResaPaxId(Integer resaPaxId) {
        this.resaPaxId = resaPaxId;
    }

    public Integer getResaGroupeId() {
        return resaGroupeId;
    }

    public void setResaGroupeId(Integer resaGroupeId) {
        this.resaGroupeId = resaGroupeId;
    }

}

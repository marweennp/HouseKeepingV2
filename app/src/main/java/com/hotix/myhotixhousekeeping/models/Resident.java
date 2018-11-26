package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Resident {

    @SerializedName("Room")
    @Expose
    private String room;
    @SerializedName("NbrA")
    @Expose
    private Integer nbrA;
    @SerializedName("NbrE")
    @Expose
    private Integer nbrE;
    @SerializedName("NbrB")
    @Expose
    private Integer nbrB;
    @SerializedName("DateDepart")
    @Expose
    private String dateDepart;
    @SerializedName("Arrangement")
    @Expose
    private String arrangement;
    @SerializedName("PremierService")
    @Expose
    private String premierService;
    @SerializedName("DernierService")
    @Expose
    private String dernierService;
    @SerializedName("Societe")
    @Expose
    private String societe;
    @SerializedName("Code")
    @Expose
    private Object code;
    @SerializedName("ResaId")
    @Expose
    private Integer resaId;
    @SerializedName("ResaPaxId")
    @Expose
    private Integer resaPaxId;
    @SerializedName("ResaGroupeId")
    @Expose
    private Integer resaGroupeId;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("Clients")
    @Expose
    private ArrayList<Client> clients = null;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getNbrA() {
        return nbrA;
    }

    public void setNbrA(Integer nbrA) {
        this.nbrA = nbrA;
    }

    public Integer getNbrE() {
        return nbrE;
    }

    public void setNbrE(Integer nbrE) {
        this.nbrE = nbrE;
    }

    public Integer getNbrB() {
        return nbrB;
    }

    public void setNbrB(Integer nbrB) {
        this.nbrB = nbrB;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
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

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

}
package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Panne {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Lieu")
    @Expose
    private String lieu;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Duree")
    @Expose
    private String duree;
    @SerializedName("Urgent")
    @Expose
    private Boolean urgent;
    @SerializedName("Nom")
    @Expose
    private String nom;
    @SerializedName("Prenom")
    @Expose
    private String prenom;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Technicien")
    @Expose
    private String technicien;

    public Panne() {
        this.id = -101;
        this.lieu = "";
        this.type = "";
        this.duree = "";
        this.urgent = false;
        this.nom = "";
        this.prenom = "";
        this.image = "";
        this.description = "";
        this.date = "";
        this.technicien = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public Boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTechnicien() {
        return technicien;
    }

    public void setTechnicien(String technicien) {
        this.technicien = technicien;
    }

}

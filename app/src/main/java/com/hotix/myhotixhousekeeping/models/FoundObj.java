package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoundObj {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Lieu")
    @Expose
    private String lieu;
    @SerializedName("NomTrouve")
    @Expose
    private String nomTrouve;
    @SerializedName("PrenomTrouve")
    @Expose
    private String prenomTrouve;
    @SerializedName("NomRendu")
    @Expose
    private String nomRendu;
    @SerializedName("Prenomrendu")
    @Expose
    private String prenomrendu;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Commentaire")
    @Expose
    private String commentaire;

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

    public String getNomTrouve() {
        return nomTrouve;
    }

    public void setNomTrouve(String nomTrouve) {
        this.nomTrouve = nomTrouve;
    }

    public String getPrenomTrouve() {
        return prenomTrouve;
    }

    public void setPrenomTrouve(String prenomTrouve) {
        this.prenomTrouve = prenomTrouve;
    }

    public String getNomRendu() {
        return nomRendu;
    }

    public void setNomRendu(String nomRendu) {
        this.nomRendu = nomRendu;
    }

    public String getPrenomrendu() {
        return prenomrendu;
    }

    public void setPrenomrendu(String prenomrendu) {
        this.prenomrendu = prenomrendu;
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

}

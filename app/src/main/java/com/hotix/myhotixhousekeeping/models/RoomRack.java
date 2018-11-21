package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoomRack {

    @SerializedName("TypeHebergement")
    @Expose
    private Integer typeHebergement;
    @SerializedName("Etage")
    @Expose
    private Integer etage;
    @SerializedName("TypeProduitId")
    @Expose
    private Integer typeProduitId;
    @SerializedName("TypeProduit")
    @Expose
    private String typeProduit;
    @SerializedName("ProdId")
    @Expose
    private Integer prodId;
    @SerializedName("NumChb")
    @Expose
    private String numChb;
    @SerializedName("StatutId")
    @Expose
    private Integer statutId;
    @SerializedName("Statut")
    @Expose
    private String statut;
    @SerializedName("isAttributed")
    @Expose
    private Boolean isAttributed;
    @SerializedName("Guests")
    @Expose
    private ArrayList<Generic> guests = null;
    @SerializedName("EtatTV")
    @Expose
    private Boolean etatTV;
    @SerializedName("EtatBar")
    @Expose
    private Boolean etatBar;
    @SerializedName("EtatServiette")
    @Expose
    private Boolean etatServiette;

    public Integer getTypeHebergement() {
        return typeHebergement;
    }

    public void setTypeHebergement(Integer typeHebergement) {
        this.typeHebergement = typeHebergement;
    }

    public Integer getEtage() {
        return etage;
    }

    public void setEtage(Integer etage) {
        this.etage = etage;
    }

    public Integer getTypeProduitId() {
        return typeProduitId;
    }

    public void setTypeProduitId(Integer typeProduitId) {
        this.typeProduitId = typeProduitId;
    }

    public String getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public String getNumChb() {
        return numChb;
    }

    public void setNumChb(String numChb) {
        this.numChb = numChb;
    }

    public Integer getStatutId() {
        return statutId;
    }

    public void setStatutId(Integer statutId) {
        this.statutId = statutId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getIsAttributed() {
        return isAttributed;
    }

    public void setIsAttributed(Boolean isAttributed) {
        this.isAttributed = isAttributed;
    }

    public ArrayList<Generic> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<Generic> guests) {
        this.guests = guests;
    }

    public Boolean getEtatTV() {
        return etatTV;
    }

    public void setEtatTV(Boolean etatTV) {
        this.etatTV = etatTV;
    }

    public Boolean getEtatBar() {
        return etatBar;
    }

    public void setEtatBar(Boolean etatBar) {
        this.etatBar = etatBar;
    }

    public Boolean getEtatServiette() {
        return etatServiette;
    }

    public void setEtatServiette(Boolean etatServiette) {
        this.etatServiette = etatServiette;
    }

}
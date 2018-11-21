package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Login {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ProfileId")
    @Expose
    private Integer profileId;
    @SerializedName("Nom")
    @Expose
    private String nom;
    @SerializedName("Prenom")
    @Expose
    private String prenom;
    @SerializedName("DateFront")
    @Expose
    private String dateFront;
    @SerializedName("Etages")
    @Expose
    private ArrayList<Etage> etages = null;
    @SerializedName("Techniciens")
    @Expose
    private ArrayList<Generic> techniciens = null;
    @SerializedName("TypesPanne")
    @Expose
    private ArrayList<Generic> typesPanne = null;
    @SerializedName("FemmesMenage")
    @Expose
    private ArrayList<Generic> femmesMenage = null;
    @SerializedName("StatusProduit")
    @Expose
    private ArrayList<Generic> statusProduit = null;
    @SerializedName("Error")
    @Expose
    private Integer error;
    @SerializedName("HasConfig")
    @Expose
    private Boolean hasConfig;
    @SerializedName("HasAddPanne")
    @Expose
    private Boolean hasAddPanne;
    @SerializedName("HasAddObjet")
    @Expose
    private Boolean hasAddObjet;
    @SerializedName("HasClosePanne")
    @Expose
    private Boolean hasClosePanne;
    @SerializedName("HasCloseObjet")
    @Expose
    private Boolean hasCloseObjet;
    @SerializedName("HasMouchard")
    @Expose
    private Boolean hasMouchard;
    @SerializedName("HasChangeStatut")
    @Expose
    private Boolean hasChangeStatut;
    @SerializedName("HasEtatLieu")
    @Expose
    private Boolean hasEtatLieu;
    @SerializedName("HasViewClient")
    @Expose
    private Boolean hasViewClient;
    @SerializedName("HasFM")
    @Expose
    private Boolean hasFM;
    @SerializedName("Hotel")
    @Expose
    private String hotel;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
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

    public String getDateFront() {
        return dateFront;
    }

    public void setDateFront(String dateFront) {
        this.dateFront = dateFront;
    }

    public ArrayList<Etage> getEtages() {
        return etages;
    }

    public void setEtages(ArrayList<Etage> etages) {
        this.etages = etages;
    }

    public ArrayList<Generic> getTechniciens() {
        return techniciens;
    }

    public void setTechniciens(ArrayList<Generic> techniciens) {
        this.techniciens = techniciens;
    }

    public ArrayList<Generic> getTypesPanne() {
        return typesPanne;
    }

    public void setTypesPanne(ArrayList<Generic> typesPanne) {
        this.typesPanne = typesPanne;
    }

    public ArrayList<Generic> getFemmesMenage() {
        return femmesMenage;
    }

    public void setFemmesMenage(ArrayList<Generic> femmesMenage) {
        this.femmesMenage = femmesMenage;
    }

    public ArrayList<Generic> getStatusProduit() {
        return statusProduit;
    }

    public void setStatusProduit(ArrayList<Generic> statusProduit) {
        this.statusProduit = statusProduit;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Boolean getHasConfig() {
        return hasConfig;
    }

    public void setHasConfig(Boolean hasConfig) {
        this.hasConfig = hasConfig;
    }

    public Boolean getHasAddPanne() {
        return hasAddPanne;
    }

    public void setHasAddPanne(Boolean hasAddPanne) {
        this.hasAddPanne = hasAddPanne;
    }

    public Boolean getHasAddObjet() {
        return hasAddObjet;
    }

    public void setHasAddObjet(Boolean hasAddObjet) {
        this.hasAddObjet = hasAddObjet;
    }

    public Boolean getHasClosePanne() {
        return hasClosePanne;
    }

    public void setHasClosePanne(Boolean hasClosePanne) {
        this.hasClosePanne = hasClosePanne;
    }

    public Boolean getHasCloseObjet() {
        return hasCloseObjet;
    }

    public void setHasCloseObjet(Boolean hasCloseObjet) {
        this.hasCloseObjet = hasCloseObjet;
    }

    public Boolean getHasMouchard() {
        return hasMouchard;
    }

    public void setHasMouchard(Boolean hasMouchard) {
        this.hasMouchard = hasMouchard;
    }

    public Boolean getHasChangeStatut() {
        return hasChangeStatut;
    }

    public void setHasChangeStatut(Boolean hasChangeStatut) {
        this.hasChangeStatut = hasChangeStatut;
    }

    public Boolean getHasEtatLieu() {
        return hasEtatLieu;
    }

    public void setHasEtatLieu(Boolean hasEtatLieu) {
        this.hasEtatLieu = hasEtatLieu;
    }

    public Boolean getHasViewClient() {
        return hasViewClient;
    }

    public void setHasViewClient(Boolean hasViewClient) {
        this.hasViewClient = hasViewClient;
    }

    public Boolean getHasFM() {
        return hasFM;
    }

    public void setHasFM(Boolean hasFM) {
        this.hasFM = hasFM;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

}
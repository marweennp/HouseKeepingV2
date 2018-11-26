package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPermissions {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Login")
    @Expose
    private String login;
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
    @SerializedName("HasControlePensionnaire")
    @Expose
    private Boolean hasControlePensionnaire;
    @SerializedName("HasFM")
    @Expose
    private Boolean hasFM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public Boolean getHasControlePensionnaire() { return hasControlePensionnaire; }

    public void setHasControlePensionnaire(Boolean hasControlePensionnaire) { this.hasControlePensionnaire = hasControlePensionnaire; }

    public Boolean getHasFM() {
        return hasFM;
    }

    public void setHasFM(Boolean hasFM) {
        this.hasFM = hasFM;
    }

}

package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelSettings {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("IPLocal")
    @Expose
    private String iPLocal;
    @SerializedName("IPPublic")
    @Expose
    private String iPPublic;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("AppIsActive")
    @Expose
    private Boolean appIsActive;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIPLocal() {
        return iPLocal;
    }

    public void setIPLocal(String iPLocal) {
        this.iPLocal = iPLocal;
    }

    public String getIPPublic() {
        return iPPublic;
    }

    public void setIPPublic(String iPPublic) {
        this.iPPublic = iPPublic;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getAppIsActive() {
        return appIsActive;
    }

    public void setAppIsActive(Boolean appIsActive) {
        this.appIsActive = appIsActive;
    }

}

package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffectedRoom {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("prodId")
    @Expose
    private Integer prodId;
    @SerializedName("typeHebId")
    @Expose
    private Integer typeHebId;
    @SerializedName("statutId")
    @Expose
    private Integer statutId;
    @SerializedName("typeProd")
    @Expose
    private Integer typeProd;
    @SerializedName("attributed")
    @Expose
    private Boolean attributed;
    @SerializedName("prodNum")
    @Expose
    private String prodNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getTypeHebId() {
        return typeHebId;
    }

    public void setTypeHebId(Integer typeHebId) {
        this.typeHebId = typeHebId;
    }

    public Integer getStatutId() {
        return statutId;
    }

    public void setStatutId(Integer statutId) {
        this.statutId = statutId;
    }

    public Integer getTypeProd() {
        return typeProd;
    }

    public void setTypeProd(Integer typeProd) {
        this.typeProd = typeProd;
    }

    public Boolean getAttributed() {
        return attributed;
    }

    public void setAttributed(Boolean attributed) {
        this.attributed = attributed;
    }

    public String getProdNum() {
        return prodNum;
    }

    public void setProdNum(String prodNum) {
        this.prodNum = prodNum;
    }

}

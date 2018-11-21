package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Forecast {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("CapaCHB")
    @Expose
    private String capaCHB;
    @SerializedName("CapaPax")
    @Expose
    private String capaPax;
    @SerializedName("ResCHB")
    @Expose
    private String resCHB;
    @SerializedName("ResPax")
    @Expose
    private String resPax;
    @SerializedName("ArrCHB")
    @Expose
    private String arrCHB;
    @SerializedName("ArrPax")
    @Expose
    private String arrPax;
    @SerializedName("DepCHB")
    @Expose
    private String depCHB;
    @SerializedName("DepPax")
    @Expose
    private String depPax;
    @SerializedName("TotCHB")
    @Expose
    private String totCHB;
    @SerializedName("TotPax")
    @Expose
    private String totPax;
    @SerializedName("OccCHB")
    @Expose
    private String occCHB;
    @SerializedName("OccPax")
    @Expose
    private String occPax;
    @SerializedName("OPTCHB")
    @Expose
    private String oPTCHB;
    @SerializedName("OPTPax")
    @Expose
    private String oPTPax;
    @SerializedName("Reste")
    @Expose
    private String reste;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCapaCHB() {
        return capaCHB;
    }

    public void setCapaCHB(String capaCHB) {
        this.capaCHB = capaCHB;
    }

    public String getCapaPax() {
        return capaPax;
    }

    public void setCapaPax(String capaPax) {
        this.capaPax = capaPax;
    }

    public String getResCHB() {
        return resCHB;
    }

    public void setResCHB(String resCHB) {
        this.resCHB = resCHB;
    }

    public String getResPax() {
        return resPax;
    }

    public void setResPax(String resPax) {
        this.resPax = resPax;
    }

    public String getArrCHB() {
        return arrCHB;
    }

    public void setArrCHB(String arrCHB) {
        this.arrCHB = arrCHB;
    }

    public String getArrPax() {
        return arrPax;
    }

    public void setArrPax(String arrPax) {
        this.arrPax = arrPax;
    }

    public String getDepCHB() {
        return depCHB;
    }

    public void setDepCHB(String depCHB) {
        this.depCHB = depCHB;
    }

    public String getDepPax() {
        return depPax;
    }

    public void setDepPax(String depPax) {
        this.depPax = depPax;
    }

    public String getTotCHB() {
        return totCHB;
    }

    public void setTotCHB(String totCHB) {
        this.totCHB = totCHB;
    }

    public String getTotPax() {
        return totPax;
    }

    public void setTotPax(String totPax) {
        this.totPax = totPax;
    }

    public String getOccCHB() {
        return occCHB;
    }

    public void setOccCHB(String occCHB) {
        this.occCHB = occCHB;
    }

    public String getOccPax() {
        return occPax;
    }

    public void setOccPax(String occPax) {
        this.occPax = occPax;
    }

    public String getOPTCHB() {
        return oPTCHB;
    }

    public void setOPTCHB(String oPTCHB) {
        this.oPTCHB = oPTCHB;
    }

    public String getOPTPax() {
        return oPTPax;
    }

    public void setOPTPax(String oPTPax) {
        this.oPTPax = oPTPax;
    }

    public String getReste() {
        return reste;
    }

    public void setReste(String reste) {
        this.reste = reste;
    }

}

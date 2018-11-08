package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoundObjData {

    @SerializedName("data")
    @Expose
    private ArrayList<FoundObj> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<FoundObj> getData() {
        return data;
    }

    public void setData(ArrayList<FoundObj> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
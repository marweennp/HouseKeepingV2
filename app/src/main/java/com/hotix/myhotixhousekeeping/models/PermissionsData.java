package com.hotix.myhotixhousekeeping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PermissionsData {

    @SerializedName("data")
    @Expose
    private ArrayList<UserPermissions> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public ArrayList<UserPermissions> getData() {
        return data;
    }

    public void setData(ArrayList<UserPermissions> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
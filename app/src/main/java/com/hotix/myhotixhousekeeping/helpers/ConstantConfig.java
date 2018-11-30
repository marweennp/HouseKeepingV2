package com.hotix.myhotixhousekeeping.helpers;

import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.models.Forecast;
import com.hotix.myhotixhousekeeping.models.FoundObj;
import com.hotix.myhotixhousekeeping.models.Generic;
import com.hotix.myhotixhousekeeping.models.Login;
import com.hotix.myhotixhousekeeping.models.Panne;
import com.hotix.myhotixhousekeeping.models.Resident;
import com.hotix.myhotixhousekeeping.models.RoomRack;

import java.util.ArrayList;

public class ConstantConfig {

    /********************** *****************( Final )************************  *******************/
    //FINAL BASE URL
    public static final String FINAL_BASE_URL = "http://41.228.21.123:99/";
    //public static final String FINAL_BASE_URL = "http://192.168.0.6:99/";
    //FINAL App Id
    public static final String FINAL_APP_ID = "2";

    public static final int SETTINGS_RESULT = 101;

    /***************************************(Non Final )*******************************************/
    //BASE URL
    public static String BASE_URL = "";

    //Global Login data
    public static Login GLOBAL_LOGIN_DATA = new Login();
    public static boolean GLOBAL_LOGEDIN = false;

    //Global Panne
    public static Panne GLOBAL_PANNE = new Panne();

    //Global FemmesMenage
    public static Generic GLOBAL_FM = new Generic();

    //Global Rooms
    public static ArrayList<AffectedRoom> GLOBAL_ROOMS = new ArrayList<AffectedRoom>();
    public static ArrayList<AffectedRoom> GLOBAL_UNASSIGNED_ROOMS = new ArrayList<AffectedRoom>();
    public static ArrayList<AffectedRoom> GLOBAL_ASSIGNED_ROOMS = new ArrayList<AffectedRoom>();
    public static RoomRack GLOBAL_ROOM_RACK = new RoomRack();
    public static ArrayList<RoomRack> GLOBAL_RACK = new ArrayList<RoomRack>();

    //Global Residents
    public static Resident GLOBAL_RESIDENT = new Resident();
    public static ArrayList<Resident> GLOBAL_RESIDENT_LIST= new ArrayList<Resident>();

    //Global Found Obj
    public static FoundObj GLOBAL_FOUND_OBJ = new FoundObj();

    //Global Rooms
    public static ArrayList<Forecast> GLOBAL_FORECASTS = new ArrayList<Forecast>();

    // Update Available
    public static boolean NWE_VERSION = false;

}

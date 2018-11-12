package com.hotix.myhotixhousekeeping.helpers;

import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.models.FemmesMenage;
import com.hotix.myhotixhousekeeping.models.FoundObj;
import com.hotix.myhotixhousekeeping.models.LoginData;
import com.hotix.myhotixhousekeeping.models.Panne;

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
    public static LoginData GLOBAL_LOGIN_DATA = new LoginData();

    //Global Panne
    public static Panne GLOBAL_PANNE = new Panne();

    //Global FemmesMenage
    public static FemmesMenage GLOBAL_FM = new FemmesMenage();

    //Global Rooms
    public static ArrayList<AffectedRoom> GLOBAL_ROOMS = new ArrayList<AffectedRoom>();
    public static ArrayList<AffectedRoom> GLOBAL_UNASSIGNED_ROOMS = new ArrayList<AffectedRoom>();
    public static ArrayList<AffectedRoom> GLOBAL_ASSIGNED_ROOMS = new ArrayList<AffectedRoom>();

    //Global Found Obj
    public static FoundObj GLOBAL_FOUND_OBJ = new FoundObj();


    // Update Available
    public static boolean NWE_VERSION = false;

}

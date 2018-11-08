package com.hotix.myhotixhousekeeping.helpers;

import com.hotix.myhotixhousekeeping.models.FoundObj;
import com.hotix.myhotixhousekeeping.models.LoginData;
import com.hotix.myhotixhousekeeping.models.Panne;

public class ConstantConfig {

    /********************** *****************( Final )************************  *******************/
    //FINAL BASE URL
    public static final String FINAL_BASE_URL = "http://41.228.21.123:99/";
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

    //Global Found Obj
    public static FoundObj GLOBAL_FOUND_OBJ = new FoundObj();

    // Update Available
    public static boolean NWE_VERSION = false;

}

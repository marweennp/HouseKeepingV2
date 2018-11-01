package com.hotix.myhotixhousekeeping.helpers;

import com.hotix.myhotixhousekeeping.models.LoginData;
import com.hotix.myhotixhousekeeping.models.Panne;

public class ConstantConfig {

    /********************** *****************( Final )************************  *******************/

    public static final int SETTINGS_RESULT = 101;

    /***************************************(Non Finol )*******************************************/
    //BASE URL
    //public static String BASE_URL = "http://192.168.0.109/";
    public static String BASE_URL = "";

    //Global Login data
    public static LoginData GLOBAL_LOGIN_DATA = new LoginData();

    //Global Panne
    public static Panne GLOBAL_LOGIN_PANNE = new Panne();

    // Update Available
    public static boolean NWE_VERSION = false;

}

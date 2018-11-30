package com.hotix.myhotixhousekeeping.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.BASE_URL;

public class Utils {

    /*hide Keyboard*/
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*Show a SnackBar with msg*/
    public static void showSnackbar(View view, String msg) {
        final Snackbar snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        }).setActionTextColor(Color.WHITE).show();
    }

    /*Set BASE_URL*/
    public static void setBaseUrl(Context context) {
        MySettings mySettings = new MySettings(context);

        if (mySettings.getLocalIpEnabled() && mySettings.getPublicIpEnabled()){

            BASE_URL = mySettings.getLocalIpDefault() ? mySettings.getLocalBaseUrl() : mySettings.getPublicBaseUrl();
            try {
                ping(context);
            } catch (Exception e) {
                Log.e("Exception",e.toString());
            }

        } else {
            BASE_URL = mySettings.getLocalIpEnabled() ? mySettings.getLocalBaseUrl() : mySettings.getPublicBaseUrl();
            //mySettings.setPublicIpEnabled(mySettings.getLocalIpEnabled() ? true : false);
        }

    }

    public static void ping(Context context) {
        final MySettings mMySettings = new MySettings(context);

        RetrofitInterface service = RetrofitClient.getClientPing().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.isConnectedQuery();
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.raw().code() == 200) {
                    Log.e("BASE_URL OK 200", BASE_URL);

                } else {
                    BASE_URL = mMySettings.getLocalIpDefault() ? mMySettings.getPublicBaseUrl() : mMySettings.getLocalBaseUrl();
                    mMySettings.setLocalIpDefault(!mMySettings.getLocalIpDefault());
                    Log.e("BASE_URL Switshed", BASE_URL);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                BASE_URL = mMySettings.getLocalIpDefault() ? mMySettings.getPublicBaseUrl() : mMySettings.getLocalBaseUrl();
                mMySettings.setLocalIpDefault(!mMySettings.getLocalIpDefault());
                Log.e("BASE_URL onFailure", BASE_URL);
            }
        });

    }

    //validate date range
    public static boolean validDates(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date start;
        Date end;
        try {

            start = df.parse(startDate);
            end = df.parse(endDate);
            if (start.compareTo(end) == 0) {
                return true;
            } else {
                return start.before(end);
            }

        } catch (Exception e) {
        }
        return false;
    }

    /**
     * String Empty Or Null (String)
     * EX stringEmptyOrNull("hello")
     *
     * @param str //the String to check for null or empty value
     * @return true if the String is !null & !empty false if not
     */
    public static boolean stringEmptyOrNull(String str) {

        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Date dateFromString(String date, String fromFormat) {
        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        Calendar c = Calendar.getInstance();
        Date result;
        result = Calendar.getInstance().getTime();
        try {
            if (stringEmptyOrNull(date)) {
                result = Calendar.getInstance().getTime();
            } else {
                result = sdf_from.parse(date);
            }
        } catch (Exception e) {
        }
        return result;
    }



    /**
     ***********************************************************************************************
     */

    /**
     * Date formatter (String, String, String)
     * EX dateFormater("2000-01-01'T'00:00:00", "yyyy-MM-dd'T'hh:mm:ss", "dd MMM yyyy")
     *
     * @param date       //the original date to format
     * @param fromFormat //the original date string format EX "yyyy-MM-dd'T'hh:mm:ss"
     * @param toFormat   //the string format to transform to EX "dd MMM yyyy"
     * @return the String Date
     */
    public static String dateFormater(String date, String fromFormat, String toFormat) {
        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdf_to = new SimpleDateFormat(toFormat);
        Date result;
        String dateResult = "";
        try {
            if (stringEmptyOrNull(date)) {
                result = Calendar.getInstance().getTime();
            } else {
                result = sdf_from.parse(date);
            }
            dateResult = sdf_to.format(result);
        } catch (Exception e) {
        }
        return dateResult;
    }

    /**
     * Date formatter (String, String, String, int)
     * EX dateFormater("2000-01-01'T'00:00:00", "yyyy-MM-dd'T'hh:mm:ss", "dd MMM yyyy", 7)
     *
     * @param date       //the original date to format
     * @param fromFormat //the original date string format EX "yyyy-MM-dd'T'hh:mm:ss"
     * @param toFormat   //the string format to transform to EX "dd MMM yyyy"
     * @param days       //the nuber of days to add
     * @return the String Date
     */
    public static String dateFormater(String date, String fromFormat, String toFormat, int days) {
        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdf_to = new SimpleDateFormat(toFormat);
        Calendar c = Calendar.getInstance();
        Date result;
        String dateResult = "";
        try {
            if (stringEmptyOrNull(date)) {
                result = Calendar.getInstance().getTime();
            } else {
                result = sdf_from.parse(date);
            }
            c.setTime(result);
            c.add(Calendar.DATE, days);
            dateResult = sdf_to.format(c.getTime());
        } catch (Exception e) {
        }
        return dateResult;
    }

    /**
     * Date Colored (String, String, String)
     * EX dateFormater("2000-01-01'T'00:00:00", "yyyy-MM-dd'T'hh:mm:ss", "dd MMM yyyy")
     *
     * @param date       //the original date to format
     * @param col_1      //coler 1 default #757575
     * @param col_1      //coler 2 default #424242
     * @param fromFormat //the original date string format EX "yyyy-MM-dd'T'hh:mm:ss"
     * @param full       //if tru return "dd MMM yyyy" else return "dd MMM "
     * @return the String Colered Date
     */
    public static String dateColored(String date, String col_1, String col_2, String fromFormat, boolean full) {

        String color1 = "#9E9E9E";//default color
        String color2 = "#757575";//default color

        SimpleDateFormat sdf_from = new SimpleDateFormat(fromFormat);
        SimpleDateFormat sdf_d = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_m = new SimpleDateFormat("MMM");
        SimpleDateFormat sdf_y = new SimpleDateFormat("yyyy");
        Date result;
        String dateResult = "";
        String st_d = "";
        String st_m = "";
        String st_y = "";

        if (!stringEmptyOrNull(col_1)) {
            color1 = col_1;
        }

        if (!stringEmptyOrNull(col_2)) {
            color2 = col_2;
        }

        try {
            result = sdf_from.parse(date);
            st_d = sdf_d.format(result);
            st_m = sdf_m.format(result);
            st_y = sdf_y.format(result);
        } catch (Exception e) {
        }

        if (full) {
            dateResult = "<font color=" + color1 + ">" + st_d + "</font> <font color=" + color2 + "><b>" + st_m + "</b></font>" + "<font color=" + color1 + "> " + st_y;
        } else {
            dateResult = "<font color=" + color1 + ">" + st_d + "</font> <font color=" + color2 + "><b>" + st_m + "</b></font>";
        }


        return dateResult;
    }

}

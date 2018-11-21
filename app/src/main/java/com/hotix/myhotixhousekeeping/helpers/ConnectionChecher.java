package com.hotix.myhotixhousekeeping.helpers;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.hotix.myhotixhousekeeping.R;

public class ConnectionChecher {

    // Connection Checker Snackbar
    public static boolean checkNetwork(View view, final Context context) {
        Snackbar snackBar = Snackbar.make(view, R.string.error_message_no_internet, Snackbar.LENGTH_INDEFINITE);
        Snackbar snackBarServ = Snackbar.make(view, R.string.error_message_unreachable_server, Snackbar.LENGTH_INDEFINITE);
        if (!isNetworkAvailable(context)) {
            snackBar.setAction(R.string.all_retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetwork(view, context); //recursive call.
                }
            }).setActionTextColor(Color.WHITE).show();
            return false;
        } else if (!isOnline()) {
            snackBarServ.setAction(R.string.all_retry, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetwork(view, context); //recursive call.
                }
            }).setActionTextColor(Color.WHITE).show();
            return false;
        }
        snackBar.dismiss();
        return true;
    }
    // Connection Checker
    public static boolean checkNetwork(final Context context) {
        if (!isNetworkAvailable(context)) {
            return false;
        } else if (!isOnline()) {
            return false;
        }
        return true;
    }

    /*Android Network Availability*/
    private static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /* Check if Serveur Online (ping) */
    private static boolean isOnline() {
        try {
            return (Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Check if Serveur Online (ping) */
    public static boolean pingServeur(String ip) {
        try {
            return (Runtime.getRuntime().exec("ping -c 1 -w 2 "+ip).waitFor() == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

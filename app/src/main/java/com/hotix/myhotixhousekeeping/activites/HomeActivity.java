package com.hotix.myhotixhousekeeping.activites;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.MySettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class HomeActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_home_lost_and_found)
    AppCompatTextView tvLostFound;
    @BindView(R.id.tv_home_maintenance_team)
    AppCompatTextView tvMintenanceTeam;

    private MySettings mMySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseUrl(this);
    }

    @Override
    public void onBackPressed() {
        startExitDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        if (!GLOBAL_LOGIN_DATA.getHasConfig()) {
            MenuItem item = menu.findItem(R.id.action_settings);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_settings:
                //Start the UsersSettingsActivity
                Intent i = new Intent(this, UsersSettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_logout:
                startLogoutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    @OnClick(R.id.rl_home_rack_room)
    public void roomRack() {
        showSnackbar(findViewById(android.R.id.content), "Room Rack");
    }

    @OnClick(R.id.rl_home_maintenance_orders)
    public void maintenanceOrders() {
        //Start the MaintenanceOrdersActivity
        Intent i = new Intent(this, MaintenanceOrdersActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.rl_home_forecast)
    public void forecast() {
        showSnackbar(findViewById(android.R.id.content), "forecast");
    }

    @OnClick(R.id.rl_home_check_in)
    public void checkIn() {
        //Start the GuestArrivalsActivity
        Intent i = new Intent(this, GuestArrivalsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.rl_home_lost_and_found)
    public void lostFound() {

        if (GLOBAL_LOGIN_DATA.getProfileId() == 16) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_not_authorized));
        } else {
            //Start the LostAndFoundActivity
            Intent i = new Intent(this, LostAndFoundActivity.class);
            startActivity(i);
        }

    }

    @OnClick(R.id.rl_home_maintenance_team)
    public void maintenanceTeam() {

        if (GLOBAL_LOGIN_DATA.getProfileId() == 16) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_not_authorized));
        } else {
            showSnackbar(findViewById(android.R.id.content), "Maintenance Team");
        }

    }

    /**********************************************************************************************/
    //init views
    private void init() {

        setSupportActionBar(toolbar);

        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getHotel())) {
            getSupportActionBar().setTitle(GLOBAL_LOGIN_DATA.getHotel());
        } else {
            getSupportActionBar().setTitle(R.string.text_toolbar_dashbord);
        }

        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getFullName())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getFullName());
        } else {
            getSupportActionBar().setSubtitle("");
        }

        if (GLOBAL_LOGIN_DATA.getProfileId() == 16) {
            tvLostFound.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
            tvMintenanceTeam.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
        } else {
            tvLostFound.setTextColor(ContextCompat.getColor(this, R.color.light_bg_dark_secondary_text));
            tvMintenanceTeam.setTextColor(ContextCompat.getColor(this, R.color.light_bg_dark_secondary_text));
        }


    }

    //This method show exit dialog.
    private void startExitDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        AppCompatButton btnYes = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_yes);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //This method show logout dialog.
    private void startLogoutDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        AppCompatButton btnYes = (AppCompatButton) mView.findViewById(R.id.btn_dialog_logout_yes);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_logout_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the LoginActivity
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    /**********************************************************************************************/


}

package com.hotix.myhotixhousekeeping.activites;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.DashbordGridAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.DasbordItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGEDIN;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class HomeActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gv_home_dashbord)
    GridView gvDashbord;

    private ArrayList<DasbordItem> mItems;
    private DashbordGridAdapter mGridAdapter;

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

        if (!GLOBAL_LOGEDIN) {
            //Start the LoginActivity
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseUrl(this);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
    //init views
    private void init() {

        setSupportActionBar(toolbar);

        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getHotel())) {
            getSupportActionBar().setTitle(GLOBAL_LOGIN_DATA.getHotel());
        } else {
            getSupportActionBar().setTitle(R.string.text_toolbar_dashbord);
        }

        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }

        mItems = new ArrayList<DasbordItem>();

        if (!(GLOBAL_LOGIN_DATA.getHasEtatLieu() == null) && (GLOBAL_LOGIN_DATA.getHasEtatLieu())) {
            mItems.add(new DasbordItem(1, getString(R.string.text_room_rack), getResources().getDrawable(R.drawable.ic_room_rack)));
        }

        if (!(GLOBAL_LOGIN_DATA.getHasAddPanne() == null) && (GLOBAL_LOGIN_DATA.getHasAddPanne())) {
            mItems.add(new DasbordItem(2, getString(R.string.text_maintenance_orders), getResources().getDrawable(R.drawable.ic_maintenance_orders)));
        }

        mItems.add(new DasbordItem(3, getString(R.string.text_forecast), getResources().getDrawable(R.drawable.ic_statistics)));

        if (!(GLOBAL_LOGIN_DATA.getHasViewClient() == null) && (GLOBAL_LOGIN_DATA.getHasViewClient())) {
            mItems.add(new DasbordItem(4, getString(R.string.text_check_in), getResources().getDrawable(R.drawable.ic_check_in)));
        }

        if (!(GLOBAL_LOGIN_DATA.getHasAddObjet() == null) && (GLOBAL_LOGIN_DATA.getHasAddObjet())) {
            mItems.add(new DasbordItem(5, getString(R.string.text_lost_and_found), getResources().getDrawable(R.drawable.ic_lost_and_found)));
        }

        if (!(GLOBAL_LOGIN_DATA.getHasFM() == null) && (GLOBAL_LOGIN_DATA.getHasFM())) {
            mItems.add(new DasbordItem(6, getString(R.string.text_maintenance_team), getResources().getDrawable(R.drawable.ic_maintenance_oteam)));
        }

        if (!(GLOBAL_LOGIN_DATA.getHasControlePensionnaire() == null) && (GLOBAL_LOGIN_DATA.getHasControlePensionnaire())) {
            mItems.add(new DasbordItem(7, getString(R.string.text_residents), getResources().getDrawable(R.drawable.ic_residents)));
        }

        mGridAdapter = new DashbordGridAdapter(getApplicationContext(), mItems);
        gvDashbord.setAdapter(mGridAdapter);

        gvDashbord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (mItems.get(position).getId()) {
                    case 1:
                        //Start the RoomRackActivity
                        i = new Intent(getApplicationContext(), RoomRackActivity.class);
                        startActivity(i);
                        break;

                    case 2:
                        //Start the MaintenanceOrdersActivity
                        i = new Intent(getApplicationContext(), MaintenanceOrdersActivity.class);
                        startActivity(i);
                        break;

                    case 3:
                        //Start the ForecastActivity
                        i = new Intent(getApplicationContext(), ForecastActivity.class);
                        startActivity(i);
                        break;

                    case 4:
                        //Start the GuestArrivalsActivity
                        i = new Intent(getApplicationContext(), GuestArrivalsActivity.class);
                        startActivity(i);
                        break;

                    case 5:
                        //Start the LostAndFoundActivity
                        i = new Intent(getApplicationContext(), LostAndFoundActivity.class);
                        startActivity(i);
                        break;

                    case 6:
                        //Start the MaintenanceTeamActivity
                        i = new Intent(getApplicationContext(), MaintenanceTeamActivity.class);
                        startActivity(i);
                        break;

                    case 7:
                        //Start the MaintenanceTeamActivity
                        i = new Intent(getApplicationContext(), ResidentsActivity.class);
                        startActivity(i);
                        break;
                }

            }
        });


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

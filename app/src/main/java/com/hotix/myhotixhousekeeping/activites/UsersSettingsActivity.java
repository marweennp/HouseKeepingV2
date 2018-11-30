package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.UsersSpinnerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.PermissionsData;
import com.hotix.myhotixhousekeeping.models.UserPermissions;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;

public class UsersSettingsActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sw_users_settings_configuration)
    SwitchCompat swConfiguration;
    @BindView(R.id.sw_users_settings_housekeepers)
    SwitchCompat swHousekeepers;
    @BindView(R.id.sw_users_settings_declare_orders)
    SwitchCompat swDeclareOrders;
    @BindView(R.id.sw_users_settings_close_orders)
    SwitchCompat swCloseOrders;
    @BindView(R.id.sw_users_settings_declare_found)
    SwitchCompat swDeclareFound;
    @BindView(R.id.sw_users_settings_close_found)
    SwitchCompat swCloseFound;
    @BindView(R.id.sw_users_settings_room_status)
    SwitchCompat swRoomStatus;
    @BindView(R.id.sw_users_settings_room_state)
    SwitchCompat swRoomState;
    @BindView(R.id.sw_users_settings_location_state)
    SwitchCompat swLocationState;
    @BindView(R.id.sw_users_settings_view_geusts)
    SwitchCompat swViewGeusts;
    @BindView(R.id.sw_users_settings_controle_pensionnaire)
    SwitchCompat swControlePensionnaire;
    @BindView(R.id.sp_users_settings)
    AppCompatSpinner spUsersSettings;

    // Loading View & Empty ListView
    @BindView(R.id.ll_loading_view)
    LinearLayout llLoadingView;
    @BindView(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;
    @BindView(R.id.tv_empty_view_text)
    AppCompatTextView tvEmptyViewText;
    @BindView(R.id.img_empty_view_icon)
    AppCompatImageView imgEmptyViewIcon;
    @BindView(R.id.btn_empty_view_refresh)
    AppCompatButton btnEmptyViewRefresh;
    @BindView(R.id.rl_users_settings)
    RelativeLayout rlUsersSettings;

    private MySettings mMySettings;
    private UsersSpinnerAdapter mSpinnerAdapter;
    private ArrayList<UserPermissions> mUsers;
    private int mUserId = -2;
    private int mSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_settings);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        init();

        spUsersSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mSelected = position;
                mUserId = mUsers.get(position).getId();
                loadSettings(mUsers.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseUrl(this);
        try {
            loadUsers();
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setings_menu, menu);
        MenuItem item = menu.findItem(R.id.action_synic);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.action_test);
        item2.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        try {
            loadUsers();
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        AppCompatTextView toolbarTitle = (AppCompatTextView) toolbar.findViewById(R.id.toolbar_center_title);
        toolbarTitle.setText(R.string.text_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swConfiguration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swConfiguration.setText(R.string.all_on);
                } else {
                    swConfiguration.setText(R.string.all_off);
                }
            }
        });

        swHousekeepers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swHousekeepers.setText(R.string.all_on);
                } else {
                    swHousekeepers.setText(R.string.all_off);
                }
            }
        });

        swDeclareOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swDeclareOrders.setText(R.string.all_on);
                } else {
                    swDeclareOrders.setText(R.string.all_off);
                }
            }
        });

        swCloseOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swCloseOrders.setText(R.string.all_on);
                } else {
                    swCloseOrders.setText(R.string.all_off);
                }
            }
        });

        swDeclareFound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swDeclareFound.setText(R.string.all_on);
                } else {
                    swDeclareFound.setText(R.string.all_off);
                }
            }
        });

        swCloseFound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swCloseFound.setText(R.string.all_on);
                } else {
                    swCloseFound.setText(R.string.all_off);
                }
            }
        });

        swRoomStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swRoomStatus.setText(R.string.all_on);
                } else {
                    swRoomStatus.setText(R.string.all_off);
                }
            }
        });

        swRoomState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swRoomState.setText(R.string.all_on);
                } else {
                    swRoomState.setText(R.string.all_off);
                }
            }
        });

        swLocationState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swLocationState.setText(R.string.all_on);
                } else {
                    swLocationState.setText(R.string.all_off);
                }
            }
        });

        swViewGeusts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swViewGeusts.setText(R.string.all_on);
                } else {
                    swViewGeusts.setText(R.string.all_off);
                }
            }
        });

        swControlePensionnaire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {

                if (bChecked) {
                    swControlePensionnaire.setText(R.string.all_on);
                } else {
                    swControlePensionnaire.setText(R.string.all_off);
                }
            }
        });

    }

    private void loadSettings(UserPermissions user) {

        if (!(user.getHasConfig() == null)) {
            swConfiguration.setChecked(user.getHasConfig());
            swConfiguration.setEnabled(!user.getHasConfig());
        }
        if (!(user.getHasFM() == null)) {
            swHousekeepers.setChecked(user.getHasFM());
        }
        if (!(user.getHasAddPanne() == null)) {
            swDeclareOrders.setChecked(user.getHasAddPanne());
        }
        if (!(user.getHasClosePanne() == null)) {
            swCloseOrders.setChecked(user.getHasClosePanne());
        }
        if (!(user.getHasAddObjet() == null)) {
            swDeclareFound.setChecked(user.getHasAddObjet());
        }
        if (!(user.getHasCloseObjet() == null)) {
            swCloseFound.setChecked(user.getHasCloseObjet());
        }
        if (!(user.getHasMouchard() == null)) {
            swRoomStatus.setChecked(user.getHasMouchard());
        }
        if (!(user.getHasChangeStatut() == null)) {
            swRoomState.setChecked(user.getHasChangeStatut());
        }
        if (!(user.getHasEtatLieu() == null)) {
            swLocationState.setChecked(user.getHasEtatLieu());
        }
        if (!(user.getHasControlePensionnaire() == null)) {
            swControlePensionnaire.setChecked(user.getHasControlePensionnaire());
        }

    }

    private void saveSettings() {
        try {
            updateUser();
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    /**********************************************************************************************/

    private void loadUsers() {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<PermissionsData> userCall = service.getAutorisationQuery("-1");

        llLoadingView.setVisibility(View.VISIBLE);
        rlUsersSettings.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<PermissionsData>() {
            @Override
            public void onResponse(Call<PermissionsData> call, Response<PermissionsData> response) {

                llLoadingView.setVisibility(View.GONE);
                rlUsersSettings.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    PermissionsData mData = response.body();
                    mUsers = mData.getData();
                    mSpinnerAdapter = new UsersSpinnerAdapter(getApplicationContext(), mUsers);
                    spUsersSettings.setAdapter(mSpinnerAdapter);
                    spUsersSettings.setSelection(mSelected);

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<PermissionsData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });

    }

    private void updateUser() {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.updateAutorisationQuery(String.valueOf(mUserId),
                swConfiguration.isChecked() ? "1" : "0",
                swDeclareOrders.isChecked() ? "1" : "0",
                swDeclareFound.isChecked() ? "1" : "0",
                swCloseOrders.isChecked() ? "1" : "0",
                swCloseFound.isChecked() ? "1" : "0",
                swRoomStatus.isChecked() ? "1" : "0",
                swRoomState.isChecked() ? "1" : "0",
                swLocationState.isChecked() ? "1" : "0",
                swViewGeusts.isChecked() ? "1" : "0",
                swHousekeepers.isChecked() ? "1" : "0",
                swControlePensionnaire.isChecked() ? "1" : "0");

        final ProgressDialog progressDialog = new ProgressDialog(UsersSettingsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {

                    mUsers.get(mSelected).setHasConfig(swConfiguration.isChecked());
                    mUsers.get(mSelected).setHasFM(swHousekeepers.isChecked());
                    mUsers.get(mSelected).setHasAddPanne(swDeclareOrders.isChecked());
                    mUsers.get(mSelected).setHasClosePanne(swCloseOrders.isChecked());
                    mUsers.get(mSelected).setHasAddObjet(swDeclareFound.isChecked());
                    mUsers.get(mSelected).setHasCloseObjet(swCloseFound.isChecked());
                    mUsers.get(mSelected).setHasMouchard(swRoomStatus.isChecked());
                    mUsers.get(mSelected).setHasChangeStatut(swRoomState.isChecked());
                    mUsers.get(mSelected).setHasEtatLieu(swLocationState.isChecked());
                    mUsers.get(mSelected).setHasViewClient(swViewGeusts.isChecked());
                    mUsers.get(mSelected).setHasControlePensionnaire(swControlePensionnaire.isChecked());

                    showSnackbar(findViewById(android.R.id.content), getString(R.string.message_successfully_updated));
                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });

    }

}

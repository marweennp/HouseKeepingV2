package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.HousekeeperAdapter;
import com.hotix.myhotixhousekeeping.adapters.RoomsGridAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.models.AffectedRoomData;
import com.hotix.myhotixhousekeeping.models.Generic;
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

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_ASSIGNED_ROOMS;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_FM;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_ROOMS;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_UNASSIGNED_ROOMS;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class MaintenanceTeamActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Loading View & Empty ListView
    private LinearLayout llLoadingView;
    private RelativeLayout rlEmptyView;
    private AppCompatTextView tvEmptyViewText;
    private AppCompatImageView imgEmptyViewIcon;
    private AppCompatButton btnEmptyViewRefresh;
    private ListView lvHousekeeprsList;
    private LinearLayoutCompat llMainContainer;
    private AppCompatTextView tvUnassignedRooms;

    //--
    private AppCompatTextView tvHousekeeper;
    private AppCompatTextView tvAddAll;
    private AppCompatTextView tvRemoveAll;
    private AppCompatImageButton imgbAddAll;
    private AppCompatImageButton imgbRemoveAll;
    private GridView gvAssigned;
    private GridView gvUnassigned;

    private RoomsGridAdapter mAssignedGridAdapter;
    private RoomsGridAdapter mUnassignedGridAdapter;

    private AffectedRoom mRoom;

    private HousekeeperAdapter mListAdapter;
    private ArrayList<Generic> mHousekeepers;

    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_team);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        init();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            initPortrait();
        } else {
            initLandscape();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseUrl(this);
        try {
            loadAffectionList();
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
        getMenuInflater().inflate(R.menu.housekeeprs_menu, menu);
        int orientation = this.getResources().getConfiguration().orientation;
        MenuItem item = menu.findItem(R.id.action_save);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_save:
                //////////////////
                try {
                    updateAffectedRooms();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;



            case R.id.action_refresh:
                //////////////////
                try {
                    loadAffectionList();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        try {
            loadAffectionList();
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    /**********************************************************************************************/
    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_housekeepers_list);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom()+" "+GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lvHousekeeprsList = (ListView) findViewById(R.id.lv_housekeeprs_list);

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);
        llMainContainer = (LinearLayoutCompat) findViewById(R.id.ll_main_container);

        tvUnassignedRooms = (AppCompatTextView) findViewById(R.id.tv_unassigned_rooms_total_text);

    }

    private void initPortrait() {

        lvHousekeeprsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                GLOBAL_FM = mHousekeepers.get(position);
                //Start the RoomAssignmentActivity
                Intent i = new Intent(getApplicationContext(), RoomAssignmentActivity.class);
                startActivity(i);
            }
        });


    }

    private void initLandscape() {

        tvHousekeeper = (AppCompatTextView) findViewById(R.id.tv_room_assign_bottom_head);
        tvAddAll = (AppCompatTextView) findViewById(R.id.tv_room_assign_add_all);
        tvRemoveAll = (AppCompatTextView) findViewById(R.id.tv_room_assign_remove_all);
        imgbAddAll = (AppCompatImageButton) findViewById(R.id.imgb_room_assign_add_all);
        imgbRemoveAll = (AppCompatImageButton) findViewById(R.id.imgb_room_assign_remove_all);
        gvAssigned = (GridView) findViewById(R.id.gv_room_assign_assigned);
        gvUnassigned = (GridView) findViewById(R.id.gv_room_assign_unassigned);

        lvHousekeeprsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                GLOBAL_FM = mHousekeepers.get(position);
                loadDetails();
            }
        });

        imgbRemoveAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GLOBAL_UNASSIGNED_ROOMS.addAll(GLOBAL_ASSIGNED_ROOMS);
                GLOBAL_ASSIGNED_ROOMS.clear();
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();
            }
        });

        imgbAddAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GLOBAL_ASSIGNED_ROOMS.addAll(GLOBAL_UNASSIGNED_ROOMS);
                GLOBAL_UNASSIGNED_ROOMS.clear();
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();
            }
        });

        tvRemoveAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GLOBAL_UNASSIGNED_ROOMS.addAll(GLOBAL_ASSIGNED_ROOMS);
                GLOBAL_ASSIGNED_ROOMS.clear();
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();
            }
        });

        tvAddAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GLOBAL_ASSIGNED_ROOMS.addAll(GLOBAL_UNASSIGNED_ROOMS);
                GLOBAL_UNASSIGNED_ROOMS.clear();
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();
            }
        });

        //----------------------------------------------------------------------------------------\\
        gvAssigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mRoom = GLOBAL_ASSIGNED_ROOMS.get(position);
                GLOBAL_ASSIGNED_ROOMS.remove(mRoom);
                GLOBAL_UNASSIGNED_ROOMS.add(mRoom);
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();


            }
        });

        //----------------------------------------------------------------------------------------\\
        gvUnassigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mRoom = GLOBAL_UNASSIGNED_ROOMS.get(position);
                GLOBAL_UNASSIGNED_ROOMS.remove(mRoom);
                GLOBAL_ASSIGNED_ROOMS.add(mRoom);
                mAssignedGridAdapter.notifyDataSetChanged();
                mUnassignedGridAdapter.notifyDataSetChanged();


            }
        });

    }

    private void loadDetails() {

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            int x = GLOBAL_FM.getId();
            GLOBAL_ASSIGNED_ROOMS.clear();
            for (AffectedRoom ar : GLOBAL_ROOMS) {
                if (ar.getId() == x) {
                    GLOBAL_ASSIGNED_ROOMS.add(ar);
                }
            }

            mAssignedGridAdapter = new RoomsGridAdapter(getApplicationContext(), GLOBAL_ASSIGNED_ROOMS);
            mUnassignedGridAdapter = new RoomsGridAdapter(getApplicationContext(), GLOBAL_UNASSIGNED_ROOMS);
            gvAssigned.setAdapter(mAssignedGridAdapter);
            gvUnassigned.setAdapter(mUnassignedGridAdapter);

            tvHousekeeper.setText(GLOBAL_FM.getName());
        }

    }

    /**********************************************************************************************/

    private String buildRoomAssignment(ArrayList<AffectedRoom> rooms) {
        String str = "";
        if (rooms.size() > 0) {
            for (AffectedRoom ar : rooms) {
                str = str + String.valueOf(ar.getTypeHebId())
                        + "," + String.valueOf(ar.getTypeProd())
                        + "," + String.valueOf(ar.getProdId()) + ";";
            }
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**********************************************************************************************/

    private void updateAffectedRooms() {

        String id = String.valueOf(GLOBAL_FM.getId());
        String roomAssignment = buildRoomAssignment(GLOBAL_ASSIGNED_ROOMS);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.affectationFemmeMenageQuery(id, roomAssignment);

        final ProgressDialog progressDialog = new ProgressDialog(MaintenanceTeamActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.text_successfully_updated));
                    try {
                        loadAffectionList();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }

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

    private void loadAffectionList() {

        String id = "1";

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<AffectedRoomData> userCall = service.getListAffectionFMQuery(id);

        llLoadingView.setVisibility(View.VISIBLE);
        llMainContainer.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);
        btnEmptyViewRefresh.setVisibility(View.GONE);
        tvUnassignedRooms.setText(String.valueOf(0));

        userCall.enqueue(new Callback<AffectedRoomData>() {
            @Override
            public void onResponse(Call<AffectedRoomData> call, Response<AffectedRoomData> response) {

                llLoadingView.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    AffectedRoomData mData = response.body();
                    if (!(mData.getData() == null)) {
                        GLOBAL_ROOMS = mData.getData();
                        GLOBAL_UNASSIGNED_ROOMS.clear();
                        for (AffectedRoom ar : GLOBAL_ROOMS) {
                            if (ar.getId() == -1) {
                                GLOBAL_UNASSIGNED_ROOMS.add(ar);
                            }
                        }
                        tvUnassignedRooms.setText(String.valueOf(GLOBAL_UNASSIGNED_ROOMS.size()));
                    }

                    tvEmptyViewText.setText(R.string.message_no_housekeepers_to_show);
                    imgEmptyViewIcon.setImageResource(R.drawable.ic_people_white_24dp);
                    btnEmptyViewRefresh.setVisibility(View.GONE);

                    if (GLOBAL_LOGIN_DATA.getFemmesMenage().size() > 0) {
                        mHousekeepers = GLOBAL_LOGIN_DATA.getFemmesMenage();
                        mListAdapter = new HousekeeperAdapter(mHousekeepers, GLOBAL_ROOMS, getApplicationContext());
                        lvHousekeeprsList.setAdapter(mListAdapter);
                        GLOBAL_FM = mHousekeepers.get(0);
                        loadDetails();
                    } else {
                        lvHousekeeprsList.setEmptyView(rlEmptyView);
                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<AffectedRoomData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                btnEmptyViewRefresh.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                tvUnassignedRooms.setText(String.valueOf(0));
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });

    }

}

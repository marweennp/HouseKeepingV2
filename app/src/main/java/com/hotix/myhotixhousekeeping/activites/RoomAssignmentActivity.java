package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.RoomsGridAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.AffectedRoom;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class RoomAssignmentActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AppCompatTextView tvHousekeeper;
    private AppCompatTextView tvAddAll;
    private AppCompatTextView tvRemoveAll;
    private AppCompatImageButton imgbAddAll;
    private AppCompatImageButton imgbRemoveAll;
    private GridView gvAssigned;
    private GridView gvUnassigned;

    private RoomsGridAdapter mAssignedGridAdapter;
    private RoomsGridAdapter mUnassignedGridAdapter;

    private MySettings mMySettings;
    private MySession mMySession;

    private AffectedRoom mRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_assignment);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.housekeeprs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_save:
                //Reload Orders List
                try {
                    updateAffectedRooms();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/
    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_rooms_assignment);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getFullName())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getFullName());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvHousekeeper = (AppCompatTextView) findViewById(R.id.tv_room_assign_bottom_head);
        tvAddAll = (AppCompatTextView) findViewById(R.id.tv_room_assign_add_all);
        tvRemoveAll = (AppCompatTextView) findViewById(R.id.tv_room_assign_remove_all);
        imgbAddAll = (AppCompatImageButton) findViewById(R.id.imgb_room_assign_add_all);
        imgbRemoveAll = (AppCompatImageButton) findViewById(R.id.imgb_room_assign_remove_all);
        gvAssigned = (GridView) findViewById(R.id.gv_room_assign_assigned);
        gvUnassigned = (GridView) findViewById(R.id.gv_room_assign_unassigned);

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

    /**********************************************************************************************/

    private String buildRoomAssignment(ArrayList<AffectedRoom> rooms) {
        String str = "";
        for (AffectedRoom ar : rooms) {
            str = str + String.valueOf(ar.getTypeHebId())
                    + "," + String.valueOf(ar.getTypeProd())
                    + "," + String.valueOf(ar.getProdId()) + ";";
        }
        return str.substring(0, str.length() - 1);
    }

    /**********************************************************************************************/

    private void updateAffectedRooms() {

        String id = String.valueOf(GLOBAL_FM.getId());
        String roomAssignment = buildRoomAssignment(GLOBAL_ASSIGNED_ROOMS);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.affectationFemmeMenageQuery(id, roomAssignment);

        final ProgressDialog progressDialog = new ProgressDialog(RoomAssignmentActivity.this);
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

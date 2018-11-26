package com.hotix.myhotixhousekeeping.activites;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.ClientAdapter;
import com.hotix.myhotixhousekeeping.adapters.ResidentGridAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Client;
import com.hotix.myhotixhousekeeping.models.Resident;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_RESIDENT;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_RESIDENT_LIST;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ResidentsActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Loading View & Empty ListView
    private LinearLayout llLoadingView;
    private RelativeLayout rlEmptyView;
    private AppCompatTextView tvEmptyViewText;
    private AppCompatImageView imgEmptyViewIcon;
    private AppCompatButton btnEmptyViewRefresh;
    private GridView gvResidents;
    private Resident mResident;
    private ArrayList<Resident> mResidents;
    private ResidentGridAdapter mGridAdapter;
    private AppCompatEditText etSearchFilter;
    //-----------------------------------------
    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residents);
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
        try {
            loadResidents();
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
        getMenuInflater().inflate(R.menu.residents_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                //
                etSearchFilter.setText("");
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try {
                    loadResidents();
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
        etSearchFilter.setText("");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            loadResidents();
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_residents);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gvResidents = (GridView) findViewById(R.id.gv_residents_rooms);

        mResidents = new ArrayList<Resident>();

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);

        rlEmptyView.setVisibility(View.GONE);

        etSearchFilter = (AppCompatEditText) findViewById(R.id.et_residents_filter);

        etSearchFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                sortGrid(etSearchFilter.getText().toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //----------------------------------------------------------------------------------------\\
        gvResidents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GLOBAL_RESIDENT = mResidents.get(position);
                startResidentspDetailsDialog();
            }
        });

    }

    private void startResidentspDetailsDialog() {

        AlertDialog.Builder mBuilder;
        View mView ;
        if (GLOBAL_RESIDENT.getClients().size() > 2) {
            mBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            mView = getLayoutInflater().inflate(R.layout.dialog_residents_details_full, null);
        } else {
            mBuilder = new AlertDialog.Builder(this );
            mView = getLayoutInflater().inflate(R.layout.dialog_residents_details, null);
        }

        AppCompatTextView tvArreng = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_residents_arrangement);
        AppCompatTextView tvRoom = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_residents_details_room);
        AppCompatTextView tvA = (AppCompatTextView) mView.findViewById(R.id.tv_residents_adult);
        AppCompatTextView tvE = (AppCompatTextView) mView.findViewById(R.id.tv_residents_kid);
        AppCompatTextView tvB = (AppCompatTextView) mView.findViewById(R.id.tv_residents_bb);
        AppCompatTextView tvFirst = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_residents_first_serv);
        AppCompatTextView tvLast = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_residents_last_serv);
        AppCompatTextView tvDate = (AppCompatTextView) mView.findViewById(R.id.tv_residents_date);

        AppCompatImageView imgA = (AppCompatImageView) mView.findViewById(R.id.img_residents_adult);
        AppCompatImageView imgE = (AppCompatImageView) mView.findViewById(R.id.img_residents_kid);
        AppCompatImageView imgB = (AppCompatImageView) mView.findViewById(R.id.img_residents_bb);
        AppCompatImageView imgDate = (AppCompatImageView) mView.findViewById(R.id.img_residents_date_icon);

        ListView guestList = (ListView) mView.findViewById(R.id.lv_residents_guest_list);

        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_residents_ok);


        tvRoom.setText(GLOBAL_RESIDENT.getRoom());

        if (!stringEmptyOrNull(GLOBAL_RESIDENT.getArrangement())) {
            tvArreng.setVisibility(View.VISIBLE);
            tvArreng.setText(GLOBAL_RESIDENT.getArrangement());
        }

        if (GLOBAL_RESIDENT.getNbrA() > 0) {
            imgA.setVisibility(View.VISIBLE);
            tvA.setVisibility(View.VISIBLE);
            tvA.setText(String.valueOf(GLOBAL_RESIDENT.getNbrA()));
        }

        if (GLOBAL_RESIDENT.getNbrE() > 0) {
            imgE.setVisibility(View.VISIBLE);
            tvE.setVisibility(View.VISIBLE);
            tvE.setText(String.valueOf(GLOBAL_RESIDENT.getNbrE()));
        }

        if (GLOBAL_RESIDENT.getNbrB() > 0) {
            imgB.setVisibility(View.VISIBLE);
            tvB.setVisibility(View.VISIBLE);
            tvB.setText(String.valueOf(GLOBAL_RESIDENT.getNbrB()));
        }

        if (!stringEmptyOrNull(GLOBAL_RESIDENT.getPremierService())) {
            tvFirst.setVisibility(View.VISIBLE);
            tvFirst.setText(getString(R.string.text_first) + " " + GLOBAL_RESIDENT.getPremierService());
        }

        if (!stringEmptyOrNull(GLOBAL_RESIDENT.getDernierService())) {
            tvLast.setVisibility(View.VISIBLE);
            tvLast.setText(getString(R.string.text_last) + " " + GLOBAL_RESIDENT.getDernierService());
        }

        if (!stringEmptyOrNull(GLOBAL_RESIDENT.getDateDepart())) {
            imgDate.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(dateFormater(GLOBAL_RESIDENT.getDateDepart(), "dd/MM/yyyy", "ddMMMyyyy"));
        }

        ArrayList<Client> clients = new ArrayList<Client>();
        clients.addAll(GLOBAL_RESIDENT.getClients());
        ClientAdapter listAdapter = new ClientAdapter(clients, getApplicationContext());
        guestList.setAdapter(listAdapter);

        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void sortGrid(String str) {

        mResidents.clear();
        rlEmptyView.setVisibility(View.GONE);

        if (str != null && str.length() > 0) {
            for (Resident res : GLOBAL_RESIDENT_LIST) {
                if (res.getRoom().toLowerCase().contains(str.toLowerCase())) {
                    mResidents.add(res);
                }
            }

        } else {
            mResidents.addAll(GLOBAL_RESIDENT_LIST);
        }

        if (mResidents.size() > 0) {

            gvResidents.setVisibility(View.VISIBLE);
            rlEmptyView.setVisibility(View.GONE);
            mGridAdapter = new ResidentGridAdapter(getApplicationContext(), mResidents);
            gvResidents.setAdapter(mGridAdapter);

        } else {

            gvResidents.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.VISIBLE);
            tvEmptyViewText.setText(R.string.message_no_maintenance_rooms_to_show);
            imgEmptyViewIcon.setImageResource(R.drawable.ic_priority_high_white_48dp);

        }


    }

    /**********************************************************************************************/

    private void loadResidents() {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ArrayList<Resident>> userCall = service.getListPensionnairesQuery();

        llLoadingView.setVisibility(View.VISIBLE);
        gvResidents.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<ArrayList<Resident>>() {
            @Override
            public void onResponse(Call<ArrayList<Resident>> call, Response<ArrayList<Resident>> response) {

                llLoadingView.setVisibility(View.GONE);
                gvResidents.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {

                    mResidents = response.body();
                    GLOBAL_RESIDENT_LIST.clear();
                    GLOBAL_RESIDENT_LIST.addAll(mResidents);


                    if (mResidents.size() > 0) {
                        mGridAdapter = new ResidentGridAdapter(getApplicationContext(), mResidents);
                        gvResidents.setAdapter(mGridAdapter);
                    } else {

                        gvResidents.setVisibility(View.GONE);
                        rlEmptyView.setVisibility(View.VISIBLE);
                        tvEmptyViewText.setText(R.string.message_no_maintenance_rooms_to_show);
                        imgEmptyViewIcon.setImageResource(R.drawable.ic_priority_high_white_48dp);

                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Resident>> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                gvResidents.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });
    }
}

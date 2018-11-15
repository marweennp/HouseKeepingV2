package com.hotix.myhotixhousekeeping.activites;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.FloorsSpinnerAdapter;
import com.hotix.myhotixhousekeeping.adapters.InformerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Etage;
import com.hotix.myhotixhousekeeping.models.Informer;
import com.hotix.myhotixhousekeeping.models.InformerData;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;
import static com.hotix.myhotixhousekeeping.helpers.Utils.validDates;

public class MouchardRoomRackActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Loading View & Empty ListView
    private LinearLayout llLoadingView;
    private RelativeLayout rlEmptyView;
    private AppCompatTextView tvEmptyViewText;
    private AppCompatImageView imgEmptyViewIcon;
    private AppCompatButton btnEmptyViewRefresh;
    private AppCompatEditText etEndDate;
    private AppCompatEditText etStartDate;
    private AppCompatSpinner spFloors;
    private ListView lvMoucharList;
    private MySettings mMySettings;
    private MySession mMySession;

    private InformerAdapter mListAdapter;
    private ArrayList<Informer> mInformers;

    private ArrayList<Etage> mFloors;
    private FloorsSpinnerAdapter mSpinnerAdapter;
    private int mFloorId = -1;
    private int mBlockId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouchard_room_rack);
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
        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadeMouchard();
            } catch (Exception e) {
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
            }
        } else {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
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
        getMenuInflater().inflate(R.menu.room_mochar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeMouchard();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    @OnClick(R.id.et_mouchar_start_date)
    public void getStartDate() {
        startDatePickerDialog(etStartDate);
    }

    @OnClick(R.id.et_mouchar_end_date)
    public void getEndDate() {
        startDatePickerDialog(etEndDate);
    }

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadeMouchard();
            } catch (Exception e) {
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
            }
        } else {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
        }

    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_mouchard);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spFloors = (AppCompatSpinner) findViewById(R.id.sp_mouchar_floor);

        etStartDate = (AppCompatEditText) findViewById(R.id.et_mouchar_start_date);
        etEndDate = (AppCompatEditText) findViewById(R.id.et_mouchar_end_date);
        lvMoucharList = (ListView) findViewById(R.id.lv_mouchar_list);

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);

        etStartDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));
        etEndDate.setText(dateFormater(null, "dd/MM/yyyy", "dd/MM/yyyy"));

        mFloors = new ArrayList<Etage>();
        mFloors.add(new Etage(-1, -1, getResources().getString(R.string.all_all)));
        mFloors.addAll(GLOBAL_LOGIN_DATA.getEtages());

        mSpinnerAdapter = new FloorsSpinnerAdapter(getApplicationContext(), mFloors);
        spFloors.setAdapter(mSpinnerAdapter);

        spFloors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mFloorId = mFloors.get(position).getId();
                mBlockId = mFloors.get(position).getBlocId();

                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeMouchard();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        etStartDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeMouchard();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        etEndDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeMouchard();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


    }

    private void startDatePickerDialog(final AppCompatEditText et) {
        Calendar currentTime = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                et.setText(dateFormatter.format(newDate.getTime()));
            }

        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    /**********************************************************************************************/

    private void loadeMouchard() {

        String startDate = dateFormater(etStartDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");
        String endDate = dateFormater(etEndDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");

        String fId = String.valueOf(mFloorId);
        String bId = String.valueOf(mBlockId);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<InformerData> userCall = service.getMouchardRackRoomQuery(fId, bId, startDate, endDate);

        llLoadingView.setVisibility(View.VISIBLE);
        lvMoucharList.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<InformerData>() {
            @Override
            public void onResponse(Call<InformerData> call, Response<InformerData> response) {

                llLoadingView.setVisibility(View.GONE);
                lvMoucharList.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    InformerData mData = response.body();
                    if (!(mData.getData() == null)) {
                        mInformers = mData.getData();
                        mListAdapter = new InformerAdapter(mInformers, getApplicationContext());
                        lvMoucharList.setAdapter(mListAdapter);
                        tvEmptyViewText.setText(R.string.message_no_activity_to_show);
                        imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                        lvMoucharList.setEmptyView(rlEmptyView);
                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<InformerData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                lvMoucharList.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });


    }

}

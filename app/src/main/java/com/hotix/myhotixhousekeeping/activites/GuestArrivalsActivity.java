package com.hotix.myhotixhousekeeping.activites;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.ArrivalAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Arrival;
import com.hotix.myhotixhousekeeping.models.ArrivalData;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFromString;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;
import static com.hotix.myhotixhousekeeping.helpers.Utils.validDates;

public class GuestArrivalsActivity extends AppCompatActivity {

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
    private ListView lvGuestList;
    private AppCompatTextView tvTotalArrival;
    private AppCompatTextView tvTotalArrivalRooms;

    private RelativeLayout rlArrivalAdult;
    private AppCompatTextView tvArrivalAdult;
    private RelativeLayout rlArrivalKid;
    private AppCompatTextView tvArrivalKid;
    private RelativeLayout rlArrivalBb;
    private AppCompatTextView tvArrivalBb;

    private ArrivalAdapter mListAdapter;
    private ArrayList<Arrival> mArrivals;

    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_arrivals);
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
                loadeGuests();
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
        getMenuInflater().inflate(R.menu.guest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                //Reload Orders List
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeGuests();
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

    @OnClick(R.id.et_guests_arrival_start_date)
    public void getStartDate() {
        startDatePickerDialog(etStartDate, dateFromString(etStartDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.et_guests_arrival_end_date)
    public void getEndDate() {
        startDatePickerDialog(etEndDate, dateFromString(etStartDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadeGuests();
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
        getSupportActionBar().setTitle(R.string.text_guest_arrivals);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvTotalArrivalRooms = (AppCompatTextView) findViewById(R.id.tv_geust_arrival_total_room_text);

        rlArrivalAdult = (RelativeLayout) findViewById(R.id.rl_arrival_adult);
        tvArrivalAdult = (AppCompatTextView) findViewById(R.id.tv_arrival_adult);
        rlArrivalKid = (RelativeLayout) findViewById(R.id.rl_arrival_kid);
        tvArrivalKid = (AppCompatTextView) findViewById(R.id.tv_arrival_kid);
        rlArrivalBb = (RelativeLayout) findViewById(R.id.rl_arrival_bb);
        tvArrivalBb = (AppCompatTextView) findViewById(R.id.tv_arrival_bb);

        etStartDate = (AppCompatEditText) findViewById(R.id.et_guests_arrival_start_date);
        etEndDate = (AppCompatEditText) findViewById(R.id.et_guests_arrival_end_date);
        lvGuestList = (ListView) findViewById(R.id.lv_guests_arrival_list);

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);

        etStartDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));
        etEndDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));

        etStartDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeGuests();
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
                        loadeGuests();
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

    private void startDatePickerDialog(final AppCompatEditText et, Date date) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);
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

    private void loadeGuests() {

        String startDate = dateFormater(etStartDate.getText().toString(), "dd/MM/yyyy", "dd/MM/yyyy");
        String endDate = dateFormater(etEndDate.getText().toString(), "dd/MM/yyyy", "dd/MM/yyyy");

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ArrivalData> userCall = service.getArriveesPrevuesQuery(startDate, endDate);

        llLoadingView.setVisibility(View.VISIBLE);
        lvGuestList.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);
        tvTotalArrivalRooms.setText(String.valueOf(0));

        userCall.enqueue(new Callback<ArrivalData>() {
            @Override
            public void onResponse(Call<ArrivalData> call, Response<ArrivalData> response) {

                llLoadingView.setVisibility(View.GONE);
                lvGuestList.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    ArrivalData mData = response.body();
                    if (!(mData.getData() == null)) {
                        mArrivals = mData.getData();
                        mListAdapter = new ArrivalAdapter(mArrivals, getApplicationContext());
                        lvGuestList.setAdapter(mListAdapter);

                        int mRooms = 0;
                        int mPaxA = 0;
                        int mPaxK = 0;
                        int mPaxB = 0;
                        for (Arrival ar : mArrivals) {
                            mPaxA += ar.getA();
                            mPaxK += ar.getE();
                            mPaxB += ar.getB();
                            mRooms += ar.getRoomCount();
                        }

                        if (mPaxA > 0) {
                            rlArrivalAdult.setVisibility(View.VISIBLE);
                            tvArrivalAdult.setText(String.valueOf(mPaxA));
                        } else {
                            rlArrivalAdult.setVisibility(View.GONE);
                            tvArrivalAdult.setText("");
                        }

                        if (mPaxK > 0) {
                            rlArrivalKid.setVisibility(View.VISIBLE);
                            tvArrivalKid.setText(String.valueOf(mPaxK));
                        } else {
                            rlArrivalKid.setVisibility(View.GONE);
                            tvArrivalKid.setText("");
                        }

                        if (mPaxB > 0) {
                            rlArrivalBb.setVisibility(View.VISIBLE);
                            tvArrivalBb.setText(String.valueOf(mPaxB));
                        } else {
                            rlArrivalBb.setVisibility(View.GONE);
                            tvArrivalBb.setText("");
                        }

                        tvTotalArrivalRooms.setText(String.valueOf(mRooms));
                        tvEmptyViewText.setText(R.string.message_no_scheduled_arrivals_to_show);
                        imgEmptyViewIcon.setImageResource(R.drawable.ic_people_white_24dp);
                        lvGuestList.setEmptyView(rlEmptyView);

                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrivalData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                lvGuestList.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvTotalArrivalRooms.setText(String.valueOf(0));
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });


    }

}

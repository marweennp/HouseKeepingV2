package com.hotix.myhotixhousekeeping.activites;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.hotix.myhotixhousekeeping.adapters.ForecastAdapter;
import com.hotix.myhotixhousekeeping.adapters.InformerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Forecast;
import com.hotix.myhotixhousekeeping.models.ForecastData;
import com.hotix.myhotixhousekeeping.models.Informer;
import com.hotix.myhotixhousekeeping.models.InformerData;
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

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_FORECASTS;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGEDIN;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFromString;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;
import static com.hotix.myhotixhousekeeping.helpers.Utils.validDates;

public class ForecastActivity extends AppCompatActivity {

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
    private ListView lvForecastList;
    private ForecastAdapter mListAdapter;
    private ArrayList<Forecast> mForecasts;
    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
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
                loadeForecast();
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
        getMenuInflater().inflate(R.menu.room_forecaste_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeForecast();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
                return true;

            case R.id.action_chart:
                //Start the TableChartActivity
                Intent i = new Intent(getApplicationContext(), TableChartActivity.class);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    @OnClick(R.id.et_forecast_start_date)
    public void getStartDate() {
        startDatePickerDialog(etStartDate, dateFromString(etStartDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.et_forecast_end_date)
    public void getEndDate() {
        startDatePickerDialog(etEndDate,dateFromString(etEndDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadeForecast();
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
        getSupportActionBar().setTitle(R.string.text_forecast);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etStartDate = (AppCompatEditText) findViewById(R.id.et_forecast_start_date);
        etEndDate = (AppCompatEditText) findViewById(R.id.et_forecast_end_date);
        lvForecastList = (ListView) findViewById(R.id.lv_forecast_list);

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);

        etStartDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));
        etEndDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy",7));

        etStartDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadeForecast();
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
                        loadeForecast();
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

    private void loadeForecast() {

        String startDate = dateFormater(etStartDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");
        String endDate = dateFormater(etEndDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ForecastData> userCall = service.getPrevisionQuery( startDate, endDate);

        llLoadingView.setVisibility(View.VISIBLE);
        lvForecastList.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {

                llLoadingView.setVisibility(View.GONE);
                lvForecastList.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    ForecastData mData = response.body();
                    if (!(mData.getData() == null)) {
                        GLOBAL_FORECASTS = mData.getData();
                        mListAdapter = new ForecastAdapter(GLOBAL_FORECASTS, getApplicationContext());
                        lvForecastList.setAdapter(mListAdapter);
                        tvEmptyViewText.setText(R.string.message_no_data_to_show);
                        imgEmptyViewIcon.setImageResource(R.drawable.ic_pie_chart_white_48dp);
                        lvForecastList.setEmptyView(rlEmptyView);
                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                lvForecastList.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });


    }

}

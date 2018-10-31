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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.OrdersTypesSpinnerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.State;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class MaintenanceOrdersActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_orders_start_date)
    AppCompatEditText etStartDate;
    @BindView(R.id.et_orders_end_date)
    AppCompatEditText etEndDate;
    @BindView(R.id.sp_orders_types)
    AppCompatSpinner spOrdersTypes;

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
    @BindView(R.id.rl_orders_container)
    RelativeLayout rlOrdersContainer;

    private MySettings mMySettings;
    private ArrayList<State> mPannes;
    private OrdersTypesSpinnerAdapter mSpinnerAdapter;
    private int mTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_orders);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orders_menu, menu);
        if (!GLOBAL_LOGIN_DATA.getHasAddPanne()) {
            MenuItem item = menu.findItem(R.id.action_add);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                //
                return true;

            case R.id.action_add:
                //
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    @OnClick(R.id.et_orders_start_date)
    public void getStartDate() {
        startDatePickerDialog(etStartDate);
    }

    @OnClick(R.id.et_orders_end_date)
    public void getEndDate() {
        startDatePickerDialog(etEndDate);
    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_maintenance);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getFullName())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getFullName());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etStartDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));
        etEndDate.setText(dateFormater(null, "dd/MM/yyyy", "dd/MM/yyyy"));

        mPannes = new ArrayList<State>();
        mPannes.add(new State(1, getString(R.string.text_state_outstanding)));
        mPannes.add(new State(2, getString(R.string.text_state_fenced)));

        mSpinnerAdapter = new OrdersTypesSpinnerAdapter(getApplicationContext(), mPannes);
        spOrdersTypes.setAdapter(mSpinnerAdapter);

        spOrdersTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mTypeId = mPannes.get(position).getId();
                Toast.makeText(MaintenanceOrdersActivity.this, "" + mTypeId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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


}

package com.hotix.myhotixhousekeeping.activites;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Forecast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_FORECASTS;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class TableChartActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TableLayout tl;
    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_chart);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        getMenuInflater().inflate(R.menu.room_table_forecaste_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_chart:
                //Start the TableChartActivity
                Intent i = new Intent(getApplicationContext(), ForecastActivity.class);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

        tl = (TableLayout) findViewById(R.id.table_forecast);

        for (Forecast fr : GLOBAL_FORECASTS) {
            addRow(fr, tl);
        }

    }

    public void addRow(Forecast obj, TableLayout tab) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mView = inflater.inflate(R.layout.item_table_chart_row, null);

        AppCompatTextView tvDate = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_date);

        AppCompatTextView tvCapRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_cap_room);
        AppCompatTextView tvCapPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_cap_pax);

        AppCompatTextView tvRecRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_rec_room);
        AppCompatTextView tvRecPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_rec_pax);

        AppCompatTextView tvArrRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_arr_room);
        AppCompatTextView tvArrPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_arr_pax);

        AppCompatTextView tvDepRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_dep_room);
        AppCompatTextView tvDepPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_dep_pax);

        AppCompatTextView tvTotRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_tot_room);
        AppCompatTextView tvTotPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_tot_pax);

        AppCompatTextView tvOccRoom = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_occ_room);
        AppCompatTextView tvOccPax = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_occ_pax);

        AppCompatTextView tvrest = (AppCompatTextView) mView.findViewById(R.id.tv_table_chart_row_rest);

        tvDate.setText(Html.fromHtml(dateColored(obj.getDate(), "#616161", "#1ab394", "yyyyMMdd", true)));
        tvCapRoom.setText(obj.getCapaCHB());
        tvCapPax.setText(obj.getCapaPax());

        tvRecRoom.setText(obj.getResCHB());
        tvRecPax.setText(obj.getResPax());

        tvArrRoom.setText(obj.getArrCHB());
        tvArrPax.setText(obj.getArrPax());

        tvDepRoom.setText(obj.getDepCHB());
        tvDepPax.setText(obj.getDepPax());

        tvTotRoom.setText(obj.getTotCHB());
        tvTotPax.setText(obj.getTotPax());

        tvOccRoom.setText(obj.getOccCHB() + "%");
        tvOccPax.setText(obj.getOccPax() + "%");

        tvrest.setText(obj.getReste());

        TableRow tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tr.addView(mView);

        tab.addView(tr);
    }
}

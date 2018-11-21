package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.TechniciansSpinnerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Generic;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_PANNE;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ShowOrderActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_details_urgent)
    AppCompatTextView tvUrgent;
    @BindView(R.id.tv_order_details_location)
    AppCompatTextView tvLocation;
    @BindView(R.id.tv_order_details_type)
    AppCompatTextView tvType;
    @BindView(R.id.tv_order_details_date)
    AppCompatTextView tvDate;
    @BindView(R.id.tv_order_details_time)
    AppCompatTextView tvTime;
    @BindView(R.id.tv_order_details_treatment_time)
    AppCompatTextView tvTreatTime;
    @BindView(R.id.tv_order_details_declared_by)
    AppCompatTextView tvDeclaredBy;
    @BindView(R.id.tv_order_details_repaired_by)
    AppCompatTextView tvRepairedBy;
    @BindView(R.id.tv_order_details_description)
    AppCompatTextView tvDescription;

    private AppCompatTextView etError;
    private AppCompatEditText etComment;

    private ArrayList<Generic> mTechs;
    private TechniciansSpinnerAdapter mTecSpinnerAdapter;
    private int techId = -1;
    private MySettings mMySettings;
    private MySession mMySession;
    private boolean mBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        //Force portrait on phones
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mBtnClose = getIntent().getExtras().getBoolean("btnClose");

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
        getMenuInflater().inflate(R.menu.show_orders_menu, menu);
        if (!mBtnClose) {
            MenuItem item = menu.findItem(R.id.action_close);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_close:
                startClaimClosingDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_maintenance);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (GLOBAL_PANNE.getUrgent()) {
            tvUrgent.setVisibility(View.VISIBLE);
        } else {
            tvUrgent.setVisibility(View.GONE);
        }

        tvLocation.setText(GLOBAL_PANNE.getLieu());
        tvType.setText(GLOBAL_PANNE.getType());
        tvDate.setText(Html.fromHtml(dateColored(GLOBAL_PANNE.getDate(), "#616161", "#1ab394", "dd/MM/yyyy hh:mm", true)));
        tvTime.setText(dateFormater(GLOBAL_PANNE.getDate(), "dd/MM/yyyy hh:mm", "HH:mm"));
        tvTreatTime.setText(GLOBAL_PANNE.getDuree());
        tvDeclaredBy.setText(GLOBAL_PANNE.getPrenom() + " " + GLOBAL_PANNE.getNom());
        tvRepairedBy.setText(GLOBAL_PANNE.getTechnicien());
        tvDescription.setText(GLOBAL_PANNE.getDescription());

    }

    //This method show claim closing dialog.
    private void startClaimClosingDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_claim_closing, null);

        AppCompatSpinner spTech = (AppCompatSpinner) mView.findViewById(R.id.sp_dialog_claim_closing_technician);
        etError = (AppCompatTextView) mView.findViewById(R.id.et_dialog_claim_closing_error);
        etComment = (AppCompatEditText) mView.findViewById(R.id.et_dialog_claim_closing_comment);
        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_claim_closing_ok);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_claim_closing_cancel);

        Generic tec = new Generic(-1, getString(R.string.all_select_technician));
        mTechs = new ArrayList<>();
        mTechs.add(tec);
        mTechs.addAll(GLOBAL_LOGIN_DATA.getTechniciens());
        mTecSpinnerAdapter = new TechniciansSpinnerAdapter(getApplicationContext(), mTechs);
        spTech.setAdapter(mTecSpinnerAdapter);

        spTech.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                techId = mTechs.get(position).getId();
                etError.setText("");
                etError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = etComment.getText().toString().trim();

                if (techId != -1) {
                    try {
                        closeOrder(String.valueOf(GLOBAL_PANNE.getId()), mMySession.getLogin(), comment, String.valueOf(techId));
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    etError.setText(R.string.error_message_select_technician);
                    etError.setVisibility(View.VISIBLE);
                }
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

    private void closeOrder(String id, String login, String comment, String tecId) {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.cloturePanneQuery(id, login, comment, tecId);

        final ProgressDialog progressDialog = new ProgressDialog(ShowOrderActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.text_successfully_closed));
                    mBtnClose = false;
                    invalidateOptionsMenu();
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

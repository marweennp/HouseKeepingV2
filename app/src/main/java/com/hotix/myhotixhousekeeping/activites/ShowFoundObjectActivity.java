package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.hotix.myhotixhousekeeping.models.Technicien;
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

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_FOUND_OBJ;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_PANNE;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class ShowFoundObjectActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_lost_and_found_details_location)
    AppCompatTextView tvLocation;
    @BindView(R.id.tv_lost_and_found_details_date)
    AppCompatTextView tvDate;
    @BindView(R.id.tv_lost_and_found_details_time)
    AppCompatTextView tvTime;
    @BindView(R.id.tv_lost_and_found_details_description)
    AppCompatTextView tvDescription;
    @BindView(R.id.tv_lost_and_found_details_found_by)
    AppCompatTextView tvFoundBy;
    @BindView(R.id.tv_lost_and_found_details_belong_to)
    AppCompatTextView tvBelongTo;
    @BindView(R.id.tv_lost_and_found_details_comment)
    AppCompatTextView tvComment;

    private MySettings mMySettings;
    private MySession mMySession;

    private boolean mBtnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_found_object);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

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
        getSupportActionBar().setTitle(R.string.text_lost_and_found);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom()+" "+GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvLocation.setText(GLOBAL_FOUND_OBJ.getLieu());
        tvDate.setText(Html.fromHtml(dateColored(GLOBAL_FOUND_OBJ.getDate(), "#616161", "#1ab394", "dd/MM/yyyy hh:mm", true)));
        tvTime.setText(dateFormater(GLOBAL_FOUND_OBJ.getDate(), "dd/MM/yyyy hh:mm", "HH:mm"));
        tvDescription.setText(GLOBAL_FOUND_OBJ.getDescription());
        tvFoundBy.setText(GLOBAL_FOUND_OBJ.getPrenomTrouve() + " " + GLOBAL_FOUND_OBJ.getNomTrouve());
        tvBelongTo.setText(GLOBAL_FOUND_OBJ.getPrenomrendu() + " " + GLOBAL_FOUND_OBJ.getNomRendu());
        tvComment.setText(GLOBAL_FOUND_OBJ.getCommentaire());

    }

    //This method show claim closing dialog.
    private void startClaimClosingDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_lost_and_found_closing, null);

        AppCompatButton btnYes = (AppCompatButton) mView.findViewById(R.id.btn_dialog_lost_and_found_yes);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_lost_and_found_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    closeFoundObj(String.valueOf(GLOBAL_FOUND_OBJ.getId()));
                    dialog.dismiss();
                } catch (Exception e) {
                    dialog.dismiss();
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
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

    private void closeFoundObj(String id) {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.clotureObjetTrouveQuery(id);

        final ProgressDialog progressDialog = new ProgressDialog(ShowFoundObjectActivity.this);
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

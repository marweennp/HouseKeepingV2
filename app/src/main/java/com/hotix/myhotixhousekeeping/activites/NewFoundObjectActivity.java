package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.TypesPannesSpinnerAdapter;
import com.hotix.myhotixhousekeeping.helpers.InputValidation;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.TypesPanne;
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
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class NewFoundObjectActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.il_new_object_found_location)
    TextInputLayout ilLocation;
    @BindView(R.id.et_new_object_found_location)
    AppCompatEditText etLocation;
    @BindView(R.id.il_new_object_found_description)
    TextInputLayout ilDescription;
    @BindView(R.id.et_new_object_found_description)
    AppCompatEditText etDescription;
    @BindView(R.id.il_new_object_found_first_name)
    TextInputLayout ilFFirstName;
    @BindView(R.id.et_new_object_found_first_name)
    AppCompatEditText etFFirstName;
    @BindView(R.id.il_new_object_found_last_name)
    TextInputLayout ilFLastName;
    @BindView(R.id.et_new_object_found_last_name)
    AppCompatEditText etFLastName;
    @BindView(R.id.il_new_object_found_belong_first_name)
    TextInputLayout ilBFirstName;
    @BindView(R.id.et_new_object_found_belong_first_name)
    AppCompatEditText etBFirstName;
    @BindView(R.id.il_new_object_found_belong_last_name)
    TextInputLayout ilBLastName;
    @BindView(R.id.et_new_object_found_belong_last_name)
    AppCompatEditText etBLastName;
    @BindView(R.id.il_new_object_found_comment)
    TextInputLayout ilComment;
    @BindView(R.id.et_new_object_found_comment)
    AppCompatEditText etComment;


    private InputValidation mInputValidation;
    private MySettings mMySettings;
    private MySession mMySession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_found_object);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        mInputValidation = new InputValidation(this);
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
        getMenuInflater().inflate(R.menu.new_orders_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_send:
                //Add New Orders List
                if (inputTextValidation()) {
                    try {
                        addNewLostOject();
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_new_lost_object);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom()+" "+GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etFFirstName.setText(GLOBAL_LOGIN_DATA.getPrenom());
        etFLastName.setText(GLOBAL_LOGIN_DATA.getNom());

    }

    //This method is for EditText valus validation.
    private boolean inputTextValidation() {

        if (!mInputValidation.isInputEditTextFilled(etLocation, ilLocation, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etDescription, ilDescription, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etFFirstName, ilFFirstName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etFLastName, ilFLastName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etBFirstName, ilBFirstName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etBLastName, ilBLastName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etComment, ilComment, getString(R.string.error_message_field_required))) {
            return false;
        }

       //Return true if all the inputs are valid
        return true;

    }

    /**********************************************************************************************/

    private void addNewLostOject() {

        String prodNum = etLocation.getText().toString().trim();
        String objTrouveDesc = etDescription.getText().toString().trim();
        String objTrouveNom = etFLastName.getText().toString().trim();
        String objTrouvePrenom = etFFirstName.getText().toString().trim();
        String objTrouveLieu = etLocation.getText().toString().trim();
        String objTrouveRenduNom =  etBLastName.getText().toString().trim();
        String objTrouveRenduPrenom =  etBFirstName.getText().toString().trim();
        String login = mMySession.getLogin();
        String comment =  etComment.getText().toString().trim();
        String ImageByteArray = "";

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.declarerObjetTrouvesQuery(prodNum, objTrouveDesc, objTrouveNom, objTrouvePrenom, objTrouveLieu, objTrouveRenduNom, objTrouveRenduPrenom, login, comment, ImageByteArray );

        final ProgressDialog progressDialog = new ProgressDialog(NewFoundObjectActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {
                    finish();
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.text_successfully_added));
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

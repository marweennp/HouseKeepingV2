package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import static com.hotix.myhotixhousekeeping.helpers.Utils.validDates;

public class NewOrderActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.il_new_order_location)
    TextInputLayout ilLocation;
    @BindView(R.id.et_new_order_location)
    AppCompatEditText etLocation;
    @BindView(R.id.sp_new_order_service_types)
    AppCompatSpinner spServiceTypes;
    @BindView(R.id.chb_new_order_priority)
    AppCompatCheckBox chbPriority;
    @BindView(R.id.il_new_order_time)
    TextInputLayout ilTime;
    @BindView(R.id.et_new_order_time)
    AppCompatEditText etTime;
    @BindView(R.id.il_new_order_first_name)
    TextInputLayout ilFirstName;
    @BindView(R.id.et_new_order_first_name)
    AppCompatEditText etFirstName;
    @BindView(R.id.il_new_order_last_name)
    TextInputLayout ilLastName;
    @BindView(R.id.et_new_order_last_name)
    AppCompatEditText etLastName;
    @BindView(R.id.il_new_order_description)
    TextInputLayout ilDescription;
    @BindView(R.id.et_new_order_description)
    AppCompatEditText etDescription;
    @BindView(R.id.tv_new_order_service_types_error)
    AppCompatTextView tvServiceTypesError;


    private InputValidation mInputValidation;
    private ArrayList<TypesPanne> mTypesPannes;
    private TypesPannesSpinnerAdapter mSpinnerAdapter;
    private MySettings mMySettings;
    private MySession mMySession;
    private int mTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
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
                        addNewOrder();
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
        getSupportActionBar().setTitle(R.string.text_new_order);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getFullName())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getFullName());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTypesPannes = new ArrayList<>();
        mTypesPannes.add(new TypesPanne(-1, getString(R.string.text_select_type)));
        mTypesPannes.addAll(GLOBAL_LOGIN_DATA.getTypesPanne());
        mSpinnerAdapter = new TypesPannesSpinnerAdapter(getApplicationContext(), mTypesPannes);
        spServiceTypes.setAdapter(mSpinnerAdapter);

        spServiceTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mTypeId = mTypesPannes.get(position).getId();
                tvServiceTypesError.setText("");
                tvServiceTypesError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    //This method is for EditText valus validation.
    private boolean inputTextValidation() {

        if (!mInputValidation.isInputEditTextFilled(etLocation, ilLocation, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etFirstName, ilFirstName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etLastName, ilLastName, getString(R.string.error_message_field_required))) {
            return false;
        }
        if (!mInputValidation.isInputEditTextFilled(etDescription, ilDescription, getString(R.string.error_message_field_required))) {
            return false;
        }

        if (mTypeId == -1) {
            tvServiceTypesError.setText(R.string.error_message_select_type);
            tvServiceTypesError.setVisibility(View.VISIBLE);
            return false;
        }

        //Return true if all the inputs are valid
        return true;

    }

    /**********************************************************************************************/

    private void addNewOrder() {

        String prodId = "-1";
        String typePanneId = String.valueOf(mTypeId);
        String urgent = String.valueOf(chbPriority.isChecked());
        String duree = etTime.getText().toString().trim();
        String lieu = etLocation.getText().toString().trim();
        String nom =  etFirstName.getText().toString().trim();
        String prenom =  etLastName.getText().toString().trim();
        String user_login = mMySession.getLogin();
        String comment =  etDescription.getText().toString().trim();
        String ImageByteArray = "";

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.reclamePanneQuery(prodId, typePanneId, urgent, duree, lieu, nom, prenom, user_login, comment, ImageByteArray );

        final ProgressDialog progressDialog = new ProgressDialog(NewOrderActivity.this);
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

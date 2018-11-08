package com.hotix.myhotixhousekeeping.activites;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.HotelSettings;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.FINAL_APP_ID;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class SettingsActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.il_settings_public_ip)
    TextInputLayout ilPublicIp;
    @BindView(R.id.il_settings_Local_ip)
    TextInputLayout ilLocalIp;
    @BindView(R.id.il_settings_hotel_code)
    TextInputLayout ilHotelCode;
    @BindView(R.id.il_settings_api_public_url)
    TextInputLayout ilApiPublicUrl;
    @BindView(R.id.il_settings_api_local_url)
    TextInputLayout ilApiLocalUrl;
    @BindView(R.id.et_settings_public_ip)
    AppCompatEditText etPublicIp;
    @BindView(R.id.et_settings_local_ip)
    AppCompatEditText etLocalIp;
    @BindView(R.id.et_settings_api_public_url)
    AppCompatEditText etApiPublicUrl;
    @BindView(R.id.et_settings_api_local_url)
    AppCompatEditText etApiLocalUrl;
    @BindView(R.id.et_settings_hotel_code)
    AppCompatEditText etHotelCode;
    @BindView(R.id.sw_settings_picture)
    SwitchCompat swPicture;
    @BindView(R.id.sw_settings_ssl)
    SwitchCompat swSsl;
    @BindView(R.id.chb_settings_public_ip)
    AppCompatCheckBox chbPublicIp;
    @BindView(R.id.chb_settings_Local_ip)
    AppCompatCheckBox chbLocalIp;
    @BindView(R.id.pb_settings)
    ProgressBar pbSettings;

    private MySettings mMySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
    public void onBackPressed() {
        saveSettings();
        setBaseUrl(this);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
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
        getMenuInflater().inflate(R.menu.setings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                return true;

            case R.id.action_synic:
                try {
                    lodeHotelInfos();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Report photos feature
        swPicture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swPicture.setText(R.string.show);
                    mMySettings.setShowPicture(true);
                } else {
                    swPicture.setText(R.string.hide);
                    mMySettings.setShowPicture(false);
                }
            }
        });

        // SSL Config
        swSsl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swSsl.setText(R.string.all_on);
                    etApiPublicUrl.setText("https://" + etPublicIp.getText().toString().trim() + "/");
                    etApiLocalUrl.setText("https://" + etLocalIp.getText().toString().trim() + "/");
                    mMySettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mMySettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());

                } else {
                    swSsl.setText(R.string.all_off);
                    etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    mMySettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mMySettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
                }
            }
        });

        //Public IP
        etPublicIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
                    if (swSsl.isChecked()) {
                        etApiPublicUrl.setText("https://" + etPublicIp.getText().toString().trim() + "/");
                    } else {
                        etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    }
                    mMySettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
                    mMySettings.setPublicIp(etPublicIp.getText().toString().trim());
                } else {
                    etApiPublicUrl.setText("http://" + etPublicIp.getText().toString().trim() + "/");
                    mMySettings.setPublicIp("0.0.0.0");
                    mMySettings.setPublicBaseUrl("http://0.0.0.0/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //Local IP
        etLocalIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
                    if (swSsl.isChecked()) {
                        etApiLocalUrl.setText("https://" + etLocalIp.getText().toString().trim() + "/");
                    } else {
                        etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    }
                    mMySettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
                    mMySettings.setLocalIp(etLocalIp.getText().toString().trim());
                } else {
                    etApiLocalUrl.setText("http://" + etLocalIp.getText().toString().trim() + "/");
                    mMySettings.setLocalIp("0.0.0.0");
                    mMySettings.setLocalBaseUrl("http://0.0.0.0/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //Public IP Eanbled
        chbPublicIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    etPublicIp.setEnabled(true);
                    ilApiPublicUrl.setVisibility(View.VISIBLE);
                    mMySettings.setPublicIpEnabled(true);
                } else {
                    etPublicIp.setEnabled(false);
                    ilApiPublicUrl.setVisibility(View.GONE);
                    mMySettings.setPublicIpEnabled(false);
                }
            }
        });

        //Local IP Eanbled
        chbLocalIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    etLocalIp.setEnabled(true);
                    ilApiLocalUrl.setVisibility(View.VISIBLE);
                    mMySettings.setLocalIpEnabled(true);
                } else {
                    etLocalIp.setEnabled(false);
                    ilApiLocalUrl.setVisibility(View.GONE);
                    mMySettings.setLocalIpEnabled(false);
                }
            }
        });


    }

    /**********************************************************************************************/

    private void loadSettings() {

        swSsl.setChecked(mMySettings.getSsl());

        chbPublicIp.setChecked(mMySettings.getPublicIpEnabled());
        chbLocalIp.setChecked(mMySettings.getLocalIpEnabled());

        etPublicIp.setText(mMySettings.getPublicIp().trim());
        etLocalIp.setText(mMySettings.getLocalIp().trim());
        etApiPublicUrl.setText(mMySettings.getPublicBaseUrl().trim());
        etApiLocalUrl.setText(mMySettings.getLocalBaseUrl().trim());
        etHotelCode.setText(mMySettings.getHotelCode().trim());

        swPicture.setChecked(mMySettings.getShowPicture());
    }

    private void saveSettings() {

        mMySettings.setSsl(swSsl.isChecked());

        if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
            mMySettings.setPublicIp(etPublicIp.getText().toString().trim());
        } else {
            mMySettings.setPublicIp("0.0.0.0");
        }

        if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
            mMySettings.setLocalIp(etLocalIp.getText().toString().trim());
        } else {
            mMySettings.setLocalIp("0.0.0.0");
        }

        if (!stringEmptyOrNull(etApiPublicUrl.getText().toString().trim())) {
            mMySettings.setPublicBaseUrl(etApiPublicUrl.getText().toString().trim());
        } else {
            mMySettings.setPublicBaseUrl("http://0.0.0.0/");
        }

        if (!stringEmptyOrNull(etApiLocalUrl.getText().toString().trim())) {
            mMySettings.setLocalBaseUrl(etApiLocalUrl.getText().toString().trim());
        } else {
            mMySettings.setLocalBaseUrl("http://0.0.0.0/");
        }

        mMySettings.setPublicIpEnabled(chbPublicIp.isChecked());
        mMySettings.setLocalIpEnabled(chbLocalIp.isChecked());

        if (!stringEmptyOrNull(etHotelCode.getText().toString().trim())) {
            mMySettings.setHotelCode(etHotelCode.getText().toString().trim());
        } else {
            mMySettings.setLocalIp("0000");
        }

        mMySettings.setConfigured(true);

        setBaseUrl(this);

        finish();

    }

    /**********************************************************************************************/

    public void lodeHotelInfos() {

        String code = etHotelCode.getText().toString();
        RetrofitInterface service = RetrofitClient.getHotixSupportApi().create(RetrofitInterface.class);
        Call<HotelSettings> userCall = service.getInfosQuery(code, FINAL_APP_ID);

        pbSettings.setVisibility(View.VISIBLE);

        userCall.enqueue(new Callback<HotelSettings>() {
            @Override
            public void onResponse(Call<HotelSettings> call, Response<HotelSettings> response) {

                pbSettings.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    HotelSettings hotelSettings = response.body();

                    //Check if the hotel is active or not
                    if (!hotelSettings.getIsActive()) {
                        //Hotel not avtivz
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_hotel_not_active));
                    } else {
                        //Hotel active
                        //Check if the hotel can use the app
                        if (!hotelSettings.getAppIsActive()) {
                            //Hotel can't use app
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_app_not_active));
                        } else {
                            //Hotel can use app

                            //Get Public IP
                            if (!stringEmptyOrNull(hotelSettings.getIPPublic())) {
                                mMySettings.setPublicIp(hotelSettings.getIPPublic());
                                mMySettings.setPublicIpEnabled(true);
                            } else {
                                mMySettings.setPublicIp("0.0.0.0");
                                mMySettings.setPublicIpEnabled(false);
                            }

                            //Get Local IP
                            if (!stringEmptyOrNull(hotelSettings.getIPLocal())) {
                                mMySettings.setLocalIp(hotelSettings.getIPLocal());
                                mMySettings.setLocalIpEnabled(true);
                            } else {
                                mMySettings.setLocalIp("0.0.0.0");
                                mMySettings.setLocalIpEnabled(false);
                            }

                            //Get Hotel ID
                            if (!stringEmptyOrNull(hotelSettings.getCode())) {
                                mMySettings.setHotelCode(hotelSettings.getCode());
                            } else {
                                mMySettings.setHotelCode("0000");
                            }

                        }

                    }
                    mMySettings.setSettingsUpdated(true);
                    loadSettings();

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }

            }

            @Override
            public void onFailure(Call<HotelSettings> call, Throwable t) {
                pbSettings.setVisibility(View.GONE);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_down));
            }
        });

    }

}

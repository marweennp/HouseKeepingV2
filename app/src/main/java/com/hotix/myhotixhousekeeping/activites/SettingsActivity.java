package com.hotix.myhotixhousekeeping.activites;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.MySettings;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.et_settings_public_ip)
    AppCompatEditText etPublicIp;
    @BindView(R.id.et_settings_local_ip)
    AppCompatEditText etLocalIp;
    @BindView(R.id.et_settings_hotel_code)
    AppCompatEditText etHotelCode;
    @BindView(R.id.sw_settings_picture)
    SwitchCompat swPicture;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        AppCompatTextView toolbarTitle = (AppCompatTextView) toolbar.findViewById(R.id.toolbar_center_title);
        toolbarTitle.setText(R.string.text_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //___________________________
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

        //_______________________________
        etPublicIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
                    mMySettings.setPublicIp(etPublicIp.getText().toString().trim());
                } else {
                    mMySettings.setPublicIp("http://0.0.0.0/");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //_______________________________
        etLocalIp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
                    mMySettings.setLocalIp(etLocalIp.getText().toString().trim());
                } else {
                    mMySettings.setLocalIp("http://0.0.0.0/");
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

    @Override
    protected void onResume() {
        super.onResume();
        loadSettings();
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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**********************************************************************************************/

    private void loadSettings() {

        etPublicIp.setText(mMySettings.getPublicIp().trim());
        etLocalIp.setText(mMySettings.getLocalIp().trim());
        etHotelCode.setText(mMySettings.getHotelCode().trim());

        swPicture.setChecked(mMySettings.getShowPicture());
    }

    private void saveSettings() {

        if (!stringEmptyOrNull(etPublicIp.getText().toString().trim())) {
            mMySettings.setPublicIp(etPublicIp.getText().toString().trim());
        } else {
            mMySettings.setPublicIp("http://0.0.0.0/");
        }

        if (!stringEmptyOrNull(etLocalIp.getText().toString().trim())) {
            mMySettings.setLocalIp(etLocalIp.getText().toString().trim());
        } else {
            mMySettings.setLocalIp("http://0.0.0.0/");
        }

        if (!stringEmptyOrNull(etHotelCode.getText().toString().trim())) {
            mMySettings.setHotelCode(etHotelCode.getText().toString().trim());
        } else {
            mMySettings.setLocalIp("0000");
        }

        mMySettings.setConfigured(true);

        setBaseUrl(this);

        showSnackbar(findViewById(android.R.id.content), getString(R.string.message_settings_updated));

        finish();


    }

}

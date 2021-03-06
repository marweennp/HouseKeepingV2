package com.hotix.myhotixhousekeeping.activites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.InputValidation;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.helpers.UpdateChecker;
import com.hotix.myhotixhousekeeping.models.HotelSettings;
import com.hotix.myhotixhousekeeping.models.Login;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;
import com.hotix.myhotixhousekeeping.views.kbv.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConnectionChecher.checkNetwork;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.BASE_URL;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.FINAL_APP_ID;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGEDIN;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    // Butter Knife BindView
    @BindView(R.id.il_login_login)
    TextInputLayout ilLogin;
    @BindView(R.id.il_login_password)
    TextInputLayout ilPassword;
    @BindView(R.id.et_login_login)
    AppCompatEditText etLogin;
    @BindView(R.id.et_login_password)
    AppCompatEditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.pb_login)
    ProgressBar pbLogin;
    @BindView(R.id.tv_login_version)
    AppCompatTextView tvVersion;
    @BindView(R.id.ibtn_login_setting)
    AppCompatImageButton btnSetting;

    private TextInputLayout ilHotelCode;
    private AppCompatEditText etHotelCode;

    //MySettings
    private MySettings mMySettings;
    //MySession
    private MySession mMySession;

    private KenBurnsView mKenBurns;
    private InputValidation mInputValidation;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mInputValidation = new InputValidation(this);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMySettings.getFirstStart()) {
            Log.e(TAG, "MySettings FirstStart : " + mMySettings.getFirstStart());
            startDownloadSettingsDialog();
        }
        setBaseUrl(this);
        loadeImage();
    }

    @Override
    public void onBackPressed() {
        startExitDialog();
    }

    /**********************************************************************************************/

    @OnClick(R.id.btn_login)
    public void startLogin() {
        if (checkNetwork(this)) {
            if (inputTextValidation()) {
                btnLogin.setEnabled(false);
                try {
                    login();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
            }
        } else {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_no_internet));
        }
    }

    @OnClick(R.id.ibtn_login_setting)
    public void startSettings() {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }

    /**********************************************************************************************/
    //This method is for EditText valus validation.
    private boolean inputTextValidation() {

        if (!mInputValidation.isInputEditTextFilled(etLogin, ilLogin, getString(R.string.error_message_field_required))) {
            return false;
        }
//        if (!mInputValidation.isInputEditTextFilled(etPassword, ilPassword, getString(R.string.error_message_field_required))) {
//            return false;
//        }

        //Return true if all the inputs are valid
        return true;

    }

    //This method is for init Views.
    private void init() {

        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mKenBurns.setImageResource(R.drawable.hotel);

        etLogin.setText(mMySession.getLogin());

        loadeImage();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(getString(R.string.text_vertion) + " " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadeImage() {

        Picasso.get().load(BASE_URL + "Android/pics_house_keeping/hotel.jpg").into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                mKenBurns.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

        });
    }

    //This method show exit dialog.
    private void startExitDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        AppCompatButton btnYes = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_yes);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_exit_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //This method show Download Hotel Settings dialog.
    private void startDownloadSettingsDialog() {

        Log.e(TAG, "Download Settings Dialog Started");

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_hotel_settings, null);
        AppCompatButton btnDownload = (AppCompatButton) mView.findViewById(R.id.btn_dialog_hotel_settings_download);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_hotel_settings_cancel);
        ilHotelCode = (TextInputLayout) mView.findViewById(R.id.il_dialog_hotel_settings_code);
        etHotelCode = (AppCompatEditText) mView.findViewById(R.id.et_dialog_hotel_settings_code);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInputValidation.isInputEditTextFilled(etHotelCode, ilHotelCode, getString(R.string.error_message_field_required))) {
                    try {
                        Log.e(TAG, "Download Clicked");
                        lodeHotelInfos(etHotelCode.getText().toString());
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_something_wrong));
                    }
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

    }

    //This method show Contact Support dialog.
    private void startContactSupportDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_contact_support, null);
        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_contact_support_ok);
        AppCompatButton btnRtery = (AppCompatButton) mView.findViewById(R.id.btn_dialog_contact_support_retry);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        btnRtery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownloadSettingsDialog();
                dialog.dismiss();
            }
        });

    }

    /*************************************(  Login Logic  )****************************************/
    public void login() {

        final String sLogin = etLogin.getText().toString().trim().toUpperCase();
        final String sPassword = etPassword.getText().toString().trim();
        mMySession.clearSessionDetails();
        mMySession.setLogin(sLogin);

        pbLogin.setVisibility(View.VISIBLE);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<Login> userCall = service.loginQuery(sLogin, sPassword);
        userCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.raw().code() == 200) {
                    Login login = response.body();
                    GLOBAL_LOGIN_DATA = response.body();

                    if (!(login.getError() == -1)) {

                        switch (login.getError()) {

                            case 0: // this User is not Authorised To Login
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_user_not_authorised));
                                break;

                            case 1: // this Account is not Active
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_inactive_account));
                                break;

                            case 2: // Password expired
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_expired_pass));
                                break;

                            case 3: // Wrong password
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_wrong_pass));
                                break;

                            case 4: // User does not exist
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_wrong_pass));
                                break;

                            case 5: // unknown
                                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_something_wrong));
                                break;
                        }

                    } else {

                        //Start the HomeActivity
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        GLOBAL_LOGEDIN = true;
                        startActivity(i);
                        finish();
                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_down));
            }
        });

    }

    public void lodeHotelInfos(String code) {

        Log.e(TAG, "Hotel Infos Loading Started : " + code);

        RetrofitInterface service = RetrofitClient.getHotixSupportApi().create(RetrofitInterface.class);
        Call<HotelSettings> userCall = service.getInfosQuery(code, FINAL_APP_ID);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<HotelSettings>() {
            @Override
            public void onResponse(Call<HotelSettings> call, Response<HotelSettings> response) {
                Log.e(TAG, "Response : " + response.raw().code());
                progressDialog.dismiss();

                if (response.raw().code() == 200) {

                    HotelSettings hotelSettings = response.body();
                    //Check if hotel id > 0
                    if (!(hotelSettings.getId() > 0)) {
                        //Hotel do not exist
                        startContactSupportDialog();
                    } else {

                        //Get Public IP
                        if (!stringEmptyOrNull(hotelSettings.getIPPublic())) {
                            mMySettings.setPublicIp(hotelSettings.getIPPublic());
                            mMySettings.setPublicBaseUrl("http://" + hotelSettings.getIPPublic() + "/");
                            mMySettings.setPublicIpEnabled(true);
                        } else {
                            mMySettings.setPublicIp("xxx.xxx.xxx.xxx");
                            mMySettings.setPublicIpEnabled(false);
                        }

                        //Get Local IP
                        if (!stringEmptyOrNull(hotelSettings.getIPLocal())) {
                            mMySettings.setLocalIp(hotelSettings.getIPLocal());
                            mMySettings.setLocalBaseUrl("http://" + hotelSettings.getIPLocal() + "/");
                            mMySettings.setLocalIpEnabled(true);
                        } else {
                            mMySettings.setLocalIp("xxx.xxx.xxx.xxx");
                            mMySettings.setLocalIpEnabled(false);
                        }

                        //Get Hotel ID
                        if (!stringEmptyOrNull(hotelSettings.getCode())) {
                            mMySettings.setHotelCode(hotelSettings.getCode());
                        } else {
                            mMySettings.setHotelCode("0000");
                        }

                        mMySettings.setFirstStart(false);
                        mMySettings.setConfigured(true);
                        mMySettings.setSettingsUpdated(true);

                        Log.e(TAG, "MySettings FirstStart : " + mMySettings.getFirstStart());

                        showSnackbar(findViewById(android.R.id.content), getString(R.string.message_settings_updated));
                        setBaseUrl(getApplicationContext());
                    }

                } else {
                    startDownloadSettingsDialog();
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }

            }

            @Override
            public void onFailure(Call<HotelSettings> call, Throwable t) {
                Log.e(TAG, "Failure : " + t.toString());
                progressDialog.dismiss();
                startDownloadSettingsDialog();
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_down));
            }
        });

    }

}
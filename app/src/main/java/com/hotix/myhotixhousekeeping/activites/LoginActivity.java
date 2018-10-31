package com.hotix.myhotixhousekeeping.activites;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.InputValidation;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.helpers.UpdateChecker;
import com.hotix.myhotixhousekeeping.models.Login;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;
import com.hotix.myhotixhousekeeping.views.kbv.KenBurnsView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConnectionChecher.checkNetwork;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.NWE_VERSION;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.SETTINGS_RESULT;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;

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
    //MySettings
    private MySettings mMySettings;

    private boolean permissionGranted = true;
    private KenBurnsView mKenBurns;
    private InputValidation mInputValidation;
    // Update
    private UpdateChecker mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mInputValidation = new InputValidation(this);
        mChecker = new UpdateChecker(this, true);
        firstStartInit();
        setBaseUrl(this);
        //Checking MySettings
        if (!mMySettings.getConfigured()) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        if (NWE_VERSION) {
            if (!permissionGranted) {
                startPermissionDialog();
            } else {
                startUpdateDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == SETTINGS_RESULT) {
            if (NWE_VERSION) {
                startUpdateDialog();
            }
        }
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
                try {
                    login();
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                btnLogin.setEnabled(false);
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
        if (!mInputValidation.isInputEditTextFilled(etPassword, ilPassword, getString(R.string.error_message_field_required))) {
            return false;
        }

        //Return true if all the inputs are valid
        return true;

    }

    //This method is for init Views.
    private void init() {

        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mKenBurns.setImageResource(R.drawable.hotel);

        Picasso.get().load(mMySettings.getLocalIp() + "Android/pics_house_keeping/hotel.jpg").into(new Target() {

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

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(getString(R.string.text_vertion) + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //This method import old settings at first start.
    private void firstStartInit() {

        if (mMySettings.getFirstStart()) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            mMySettings.setLocalIp(sp.getString("serveur", "http://0.0.0.0/"));
            mMySettings.setShowPicture(sp.getBoolean("image", false));
            mMySettings.setFirstStart(false);
        }
    }

    //This method check for permissions -> download APK -> install.
    private void updateApp() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        try {
                            mChecker.downloadAndInstall(mMySettings.getLocalIp() + "Android/appHouseKeeping.apk");
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), e.toString());
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        permissionGranted = false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //This method show update dialog.
    private void startUpdateDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_update_app, null);
        AppCompatButton btnUpdate = (AppCompatButton) mView.findViewById(R.id.btn_dialog_update);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateApp();
                dialog.dismiss();
            }
        });

    }

    //This method show permission dialog.
    private void startPermissionDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_permission_needed, null);
        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_permission_ok);
        AppCompatButton btnSettings = (AppCompatButton) mView.findViewById(R.id.btn_dialog_permission_settings);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateApp();
                dialog.dismiss();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, SETTINGS_RESULT);
                permissionGranted = true;
                dialog.dismiss();
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

    /*************************************(  Login Logic  )****************************************/
    public void login() {

        final String sLogin = etLogin.getText().toString().trim();
        final String sPassword = etPassword.getText().toString().trim();

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<Login> userCall = service.loginQuery(sLogin, sPassword);

        pbLogin.setVisibility(View.VISIBLE);

        userCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.raw().code() == 200) {
                    Login login = response.body();
                    GLOBAL_LOGIN_DATA = login.getData();

                    if (!(login.getData().getError() == -1)) {

                        switch (login.getData().getError()) {

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

    /**********************************************************************************************/
}
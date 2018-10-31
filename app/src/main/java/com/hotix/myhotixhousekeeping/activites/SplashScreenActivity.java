package com.hotix.myhotixhousekeeping.activites;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.helpers.UpdateChecker;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.NWE_VERSION;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    // Butter Knife BindViews
    @BindView(R.id.img_splash_screen_logo)
    AppCompatImageView imgLogo;
    @BindView(R.id.tv_splash_screen_footer)
    AppCompatTextView tvFooter;
    // Update
    private UpdateChecker mChecker;
    private MySettings mMySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mChecker = new UpdateChecker(this, false);

        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        CheckForUpdates check = new CheckForUpdates();
        try {
            check.execute();
        } catch (Exception e) {
            Log.e(TAG,getString(R.string.error_message_check_settings));
        }


        //load logo img
        Picasso.get().load(R.mipmap.ic_launcher).fit().placeholder(R.mipmap.ic_launcher).into(imgLogo);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

    /**********************************************************************************************/

    private class CheckForUpdates extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            mChecker.checkForUpdateByVersionCode(mMySettings.getLocalIp() + "Android/versionHouseKeeping.txt");
            return String.valueOf(mChecker.isUpdateAvailable());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            NWE_VERSION = Boolean.valueOf(s);
        }
    }
}

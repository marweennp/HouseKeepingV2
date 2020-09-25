package com.hotix.myhotixhousekeeping.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.FloorsSpinnerAdapter;
import com.hotix.myhotixhousekeeping.adapters.GuestAdapter;
import com.hotix.myhotixhousekeeping.adapters.RoomRackGridAdapter;
import com.hotix.myhotixhousekeeping.helpers.InputValidation;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Etage;
import com.hotix.myhotixhousekeeping.models.Generic;
import com.hotix.myhotixhousekeeping.models.RoomRack;
import com.hotix.myhotixhousekeeping.models.RoomRackData;
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

import static android.graphics.Color.parseColor;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_RACK;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_ROOM_RACK;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;

public class RoomRackActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Loading View & Empty ListView
    private LinearLayout llLoadingView;
    private RelativeLayout rlEmptyView;
    private AppCompatTextView tvEmptyViewText;
    private AppCompatImageView imgEmptyViewIcon;
    private AppCompatButton btnEmptyViewRefresh;
    private AppCompatSpinner spHotelFloors;
    private AppCompatSpinner spRoomStatus;
    private GridView gvRoomRack;
    private RoomRack mRoom;
    private ArrayList<RoomRack> mRoomRack;
    private RoomRackGridAdapter mGridAdapter;
    private ArrayList<Etage> mFloors;
    private ArrayList<Etage> mStatus;
    private FloorsSpinnerAdapter mSpinnerAdapter;
    private FloorsSpinnerAdapter mRoomSpinnerAdapter;
    private int mFloorId = -1;
    private int mBlockId = -1;
    private int mStatusId = -1;
    private InputValidation mInputValidation;
    //-----------------------------------------
    private MySettings mMySettings;
    private MySession mMySession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_rack);
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
        try {
            loadRooms(mFloorId, mBlockId);
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_rack_menu, menu);
        if (!GLOBAL_LOGIN_DATA.getHasMouchard()) {
            MenuItem item = menu.findItem(R.id.action_histo);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                //
                try {
                    loadRooms(mFloorId, mBlockId);
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
                return true;

            case R.id.action_info:
                //
                startLegendDialog();
                return true;

            case R.id.action_histo:
                //Start the MouchardRoomRackActivity
                Intent i = new Intent(this, MouchardRoomRackActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        try {
            loadRooms(mFloorId, mBlockId);
        } catch (Exception e) {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
        }

    }

    /**********************************************************************************************/

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_room_rack);
        if (!stringEmptyOrNull(GLOBAL_LOGIN_DATA.getNom())) {
            getSupportActionBar().setSubtitle(GLOBAL_LOGIN_DATA.getPrenom() + " " + GLOBAL_LOGIN_DATA.getNom());
        } else {
            getSupportActionBar().setSubtitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spHotelFloors = (AppCompatSpinner) findViewById(R.id.sp_hotel_floors);
        spRoomStatus = (AppCompatSpinner) findViewById(R.id.sp_room_status);
        gvRoomRack = (GridView) findViewById(R.id.gv_rooms_rack);

        mRoomRack = new ArrayList<RoomRack>();

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);


        rlEmptyView.setVisibility(View.GONE);

        mFloors = new ArrayList<Etage>();
        mFloors.add(new Etage(-1, -1, getResources().getString(R.string.all_all_floors)));
        mFloors.addAll(GLOBAL_LOGIN_DATA.getEtages());

        mSpinnerAdapter = new FloorsSpinnerAdapter(getApplicationContext(), mFloors);
        spHotelFloors.setAdapter(mSpinnerAdapter);

        mStatus = new ArrayList<Etage>();
        mStatus.add(new Etage(-1, -1, getResources().getString(R.string.all_all_status)));
        mStatus.add(new Etage(1, 1, getResources().getString(R.string.dialog_legend_vac_clean)));
        mStatus.add(new Etage(2, 2, getResources().getString(R.string.dialog_legend_vac_dirty)));
        mStatus.add(new Etage(3, 3, getResources().getString(R.string.dialog_legend_occ_clean)));
        mStatus.add(new Etage(4, 4, getResources().getString(R.string.dialog_legend_occ_dirty)));
        mStatus.add(new Etage(5, 5, getResources().getString(R.string.dialog_legend_expect_dep)));
        mStatus.add(new Etage(6, 6, getResources().getString(R.string.dialog_legend_day_use)));
        mStatus.add(new Etage(7, 7, getResources().getString(R.string.dialog_legend_out_of_order)));
        mStatus.add(new Etage(8, 8, getResources().getString(R.string.dialog_legend_attributed)));

        mRoomSpinnerAdapter = new FloorsSpinnerAdapter(getApplicationContext(), mStatus);
        spRoomStatus.setAdapter(mRoomSpinnerAdapter);

        spHotelFloors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mFloorId = mFloors.get(position).getId();
                mBlockId = mFloors.get(position).getBlocId();
                try {
                    loadRooms(mFloorId, mBlockId);
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spRoomStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mStatusId = mStatus.get(position).getId();
                sortGrid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //----------------------------------------------------------------------------------------\\
        gvRoomRack.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GLOBAL_ROOM_RACK = mRoomRack.get(position);
                startRoomRackOptions();

            }
        });

    }

    private void startLegendDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_legend, null);

        CardView cv1 = (CardView) mView.findViewById(R.id.cv_vac_clean);
        CardView cv2 = (CardView) mView.findViewById(R.id.cv_vac_dirty);
        CardView cv3 = (CardView) mView.findViewById(R.id.cv_occ_clean);
        CardView cv4 = (CardView) mView.findViewById(R.id.cv_occ_dirty);
        CardView cv5 = (CardView) mView.findViewById(R.id.cv_expect_dep);
        CardView cv6 = (CardView) mView.findViewById(R.id.cv_day_use);
        CardView cv7 = (CardView) mView.findViewById(R.id.cv_out_of_order);
        CardView cv8 = (CardView) mView.findViewById(R.id.cv_attributed);

        for (Generic sp : GLOBAL_LOGIN_DATA.getStatusProduit()) {
            switch (sp.getId()) {
                case 1:
                    cv1.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 2:
                    cv2.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 3:
                    cv3.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 4:
                    cv4.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 5:
                    cv5.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 6:
                    cv6.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 7:
                    cv7.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
                case 8:
                    cv8.setCardBackgroundColor(parseColor(sp.getName()));
                    break;
            }

        }

        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_legend_ok);

        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void startRoomRackOptions() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_room_rack_options, null);

        AppCompatTextView title = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_rack_options_title);
        title.setText(GLOBAL_ROOM_RACK.getTypeProduit() + " " + GLOBAL_ROOM_RACK.getNumChb());

        AppCompatTextView comment = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_rack_options_comment);
        comment.setText("");

        RelativeLayout rlGuests = (RelativeLayout) mView.findViewById(R.id.rl_dialog_room_rack_options_guests);
        AppCompatTextView guests = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_rack_options_guests);

        if (GLOBAL_ROOM_RACK.getGuests().size() > 0){
            rlGuests.setVisibility(View.VISIBLE);
            guests.setText(GLOBAL_ROOM_RACK.getGuests().size() + "");
        }

        AppCompatButton btnCheckGuest = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_options_view_guest);
        AppCompatButton btnClaimOrder = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_options_claim_order);
        AppCompatButton btnChangeState = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_options_change_state);
        AppCompatButton btnCheckState = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_options_view_room_state);

        View vCheckGuest = (View) mView.findViewById(R.id.v_dialog_room_rack_options_view_guest);
        View vClaimOrder = (View) mView.findViewById(R.id.v_dialog_room_rack_options_claim_order);
        View vChangeState = (View) mView.findViewById(R.id.v_dialog_room_rack_options_change_state);
        View vCheckState = (View) mView.findViewById(R.id.v_dialog_room_rack_options_view_room_state);

        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_options_cancel);

        //__________________________________________________________________________________________

        if (!GLOBAL_LOGIN_DATA.getHasViewClient()) {
            btnCheckGuest.setVisibility(View.GONE);
            vCheckGuest.setVisibility(View.GONE);
        }

        if (!GLOBAL_LOGIN_DATA.getHasChangeStatut()) {
            btnChangeState.setVisibility(View.GONE);
            vChangeState.setVisibility(View.GONE);
        }

        if (!GLOBAL_LOGIN_DATA.getHasAddPanne()) {
            btnClaimOrder.setVisibility(View.GONE);
            vClaimOrder.setVisibility(View.GONE);
        }

        if (!GLOBAL_LOGIN_DATA.getHasEtatLieu()) {
            btnCheckState.setVisibility(View.GONE);
            vCheckState.setVisibility(View.GONE);
        }

        if ((GLOBAL_ROOM_RACK.getStatutId() == 5) || (GLOBAL_ROOM_RACK.getStatutId() == 6) || (GLOBAL_ROOM_RACK.getStatutId() == 8)) {
            btnChangeState.setVisibility(View.GONE);
            vChangeState.setVisibility(View.GONE);
        }

//        if (GLOBAL_ROOM_RACK.getIsAttributed()) {
//            btnChangeState.setVisibility(View.GONE);
//            vChangeState.setVisibility(View.GONE);
//        }

        if ((GLOBAL_ROOM_RACK.getStatutId() == 1) || (GLOBAL_ROOM_RACK.getStatutId() == 2) || (GLOBAL_ROOM_RACK.getStatutId() == 7) || (GLOBAL_ROOM_RACK.getStatutId() == 8)) {
            btnCheckState.setVisibility(View.GONE);
            vCheckState.setVisibility(View.GONE);
        }

        if (GLOBAL_ROOM_RACK.getGuests().size() < 1) {
            btnCheckGuest.setVisibility(View.GONE);
            vCheckGuest.setVisibility(View.GONE);
        }


        //__________________________________________________________________________________________

        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCheckGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRoomCheckGuest();
                dialog.dismiss();
            }
        });

        btnClaimOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the NewOrderActivity
                Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
                i.putExtra("Room", true);
                startActivity(i);
                dialog.dismiss();
            }
        });

        btnChangeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRoomChangeSatate();
                dialog.dismiss();
            }
        });

        btnCheckState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRoomCheckState();
                dialog.dismiss();
            }
        });

    }

    private void startRoomChangeSatate() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_room_rack_change_state, null);

        final RelativeLayout rlError = (RelativeLayout) mView.findViewById(R.id.rl_dialog_room_change_state_error);
        AppCompatTextView tvError = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_change_state_error);

        AppCompatTextView title = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_change_state_title);
        title.setText(GLOBAL_ROOM_RACK.getTypeProduit() + " " + GLOBAL_ROOM_RACK.getNumChb());


        AppCompatButton btnVacClean = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_vac_clean);
        AppCompatButton btnVacDirty = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_vac_dirty);
        AppCompatButton btnOccClean = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_occ_clean);
        AppCompatButton btnOccDirty = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_occ_dirty);
        AppCompatButton btnOutOfOrder = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_out_of_order);

        View vVacClean = (View) mView.findViewById(R.id.v_dialog_room_change_state_vac_clean);
        View vVacDirty = (View) mView.findViewById(R.id.v_dialog_room_change_state_vac_dirty);
        View vOccClean = (View) mView.findViewById(R.id.v_dialog_room_change_state_occ_clean);
        View vOccDirty = (View) mView.findViewById(R.id.v_dialog_room_change_state_occ_dirty);
        View vOutOfOrder = (View) mView.findViewById(R.id.v_dialog_room_change_state_out_of_order);

        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_change_state_cancel);

        //__________________________________________________________________________________________

        if ((GLOBAL_ROOM_RACK.getStatutId() == 1) || (GLOBAL_ROOM_RACK.getStatutId() == 2) || (GLOBAL_ROOM_RACK.getStatutId() == 7)) {

            btnOccClean.setVisibility(View.GONE);
            vOccClean.setVisibility(View.GONE);
            btnOccDirty.setVisibility(View.GONE);
            vOccDirty.setVisibility(View.GONE);
        }

        if ((GLOBAL_ROOM_RACK.getStatutId() == 3) || (GLOBAL_ROOM_RACK.getStatutId() == 4)) {

            btnVacClean.setVisibility(View.GONE);
            vVacClean.setVisibility(View.GONE);
            btnVacDirty.setVisibility(View.GONE);
            vVacDirty.setVisibility(View.GONE);
            btnOutOfOrder.setVisibility(View.GONE);
            vOutOfOrder.setVisibility(View.GONE);
        }

        switch (GLOBAL_ROOM_RACK.getStatutId()) {
            case 1:
                btnVacClean.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                btnVacDirty.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;
            case 3:
                btnOccClean.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 4:
                btnOccDirty.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 7:
                btnOutOfOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
        //__________________________________________________________________________________________


        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startRoomRackOptions();
            }
        });

        btnVacClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlError.setVisibility(View.GONE);

                if (!(GLOBAL_ROOM_RACK.getStatutId() == 1)) {
                    if ((GLOBAL_ROOM_RACK.getStatutId() == 2) || (GLOBAL_ROOM_RACK.getStatutId() == 7)) {

                        String user = mMySession.getLogin();
                        String prodId = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                        String typeHebId = String.valueOf(GLOBAL_ROOM_RACK.getTypeHebergement());
                        String typeProdId = String.valueOf(GLOBAL_ROOM_RACK.getTypeProduitId());
                        String statut = "1";
                        String oldStatut = String.valueOf(GLOBAL_ROOM_RACK.getStatutId());
                        String comment = "";

                        dialog.dismiss();

                        try {
                            changerProduitStaut(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                        }

                    } else {
                        rlError.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        btnVacDirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlError.setVisibility(View.GONE);

                if (!(GLOBAL_ROOM_RACK.getStatutId() == 2)) {
                    if ((GLOBAL_ROOM_RACK.getStatutId() == 1) || (GLOBAL_ROOM_RACK.getStatutId() == 7)) {

                        String user = mMySession.getLogin();
                        String prodId = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                        String typeHebId = String.valueOf(GLOBAL_ROOM_RACK.getTypeHebergement());
                        String typeProdId = String.valueOf(GLOBAL_ROOM_RACK.getTypeProduitId());
                        String statut = "2";
                        String oldStatut = String.valueOf(GLOBAL_ROOM_RACK.getStatutId());
                        String comment = "";

                        dialog.dismiss();

                        try {
                            changerProduitStaut(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                        }

                    } else {
                        rlError.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        btnOccClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlError.setVisibility(View.GONE);

                if (!(GLOBAL_ROOM_RACK.getStatutId() == 3)) {
                    if ((GLOBAL_ROOM_RACK.getStatutId() == 4)) {

                        String user = mMySession.getLogin();
                        String prodId = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                        String typeHebId = String.valueOf(GLOBAL_ROOM_RACK.getTypeHebergement());
                        String typeProdId = String.valueOf(GLOBAL_ROOM_RACK.getTypeProduitId());
                        String statut = "3";
                        String oldStatut = String.valueOf(GLOBAL_ROOM_RACK.getStatutId());
                        String comment = "";

                        dialog.dismiss();

                        try {
                            changerProduitStaut(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                        }

                    } else {
                        rlError.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        btnOccDirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlError.setVisibility(View.GONE);

                if (!(GLOBAL_ROOM_RACK.getStatutId() == 4)) {
                    if ((GLOBAL_ROOM_RACK.getStatutId() == 3)) {

                        String user = mMySession.getLogin();
                        String prodId = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                        String typeHebId = String.valueOf(GLOBAL_ROOM_RACK.getTypeHebergement());
                        String typeProdId = String.valueOf(GLOBAL_ROOM_RACK.getTypeProduitId());
                        String statut = "4";
                        String oldStatut = String.valueOf(GLOBAL_ROOM_RACK.getStatutId());
                        String comment = "";

                        dialog.dismiss();

                        try {
                            changerProduitStaut(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);
                        } catch (Exception e) {
                            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                        }

                    } else {
                        rlError.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        btnOutOfOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlError.setVisibility(View.GONE);

                if (!(GLOBAL_ROOM_RACK.getStatutId() == 7)) {
                    if ((GLOBAL_ROOM_RACK.getStatutId() == 1) || (GLOBAL_ROOM_RACK.getStatutId() == 2)) {
                        startCommentDialog();
                        dialog.dismiss();
                    } else {
                        rlError.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

    }

    private void startRoomCheckState() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_room_rack_check_state, null);

        AppCompatTextView title = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_check_state_title);
        title.setText(GLOBAL_ROOM_RACK.getTypeProduit() + " " + GLOBAL_ROOM_RACK.getNumChb());


        final SwitchCompat swTv = (SwitchCompat) mView.findViewById(R.id.sw_dialog_room_check_state_tv);
        final SwitchCompat swMiniBar = (SwitchCompat) mView.findViewById(R.id.sw_dialog_room_check_state_mini_bar);
        final SwitchCompat swTowels = (SwitchCompat) mView.findViewById(R.id.sw_dialog_room_check_state_towels);

        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_check_state_cancel);
        AppCompatButton btnSave = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_check_state_save);

        //__________________________________________________________________________________________

        swTv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swTv.setText(R.string.all_valid);
                } else {
                    swTv.setText(R.string.all_invalid);
                }
            }
        });

        swTowels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swTowels.setText(R.string.all_valid);
                } else {
                    swTowels.setText(R.string.all_invalid);
                }
            }
        });

        swMiniBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    swMiniBar.setText(R.string.all_valid);
                } else {
                    swMiniBar.setText(R.string.all_invalid);
                }
            }
        });

        swTv.setChecked(GLOBAL_ROOM_RACK.getEtatTV());
        swMiniBar.setChecked(GLOBAL_ROOM_RACK.getEtatBar());
        swTowels.setChecked(GLOBAL_ROOM_RACK.getEtatServiette());

        //__________________________________________________________________________________________

        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                String tv = swTv.isChecked() ? "1" : "0";
                String bar = swMiniBar.isChecked() ? "1" : "0";
                String serv = swTowels.isChecked() ? "1" : "0";

                dialog.dismiss();

                try {
                    updateEtatLieu(id, tv, bar, serv);
                } catch (Exception e) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startRoomRackOptions();
            }
        });


    }

    private void startRoomCheckGuest() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_room_rack_check_guests, null);

        AppCompatTextView title = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_check_guest_title);
        title.setText(GLOBAL_ROOM_RACK.getTypeProduit() + " " + GLOBAL_ROOM_RACK.getNumChb());

        ListView guestList = (ListView) mView.findViewById(R.id.lv_dialog_room_check_guest_list);
        ArrayList<Generic> guests = new ArrayList<Generic>();
        guests.addAll(GLOBAL_ROOM_RACK.getGuests());
        GuestAdapter listAdapter = new GuestAdapter(guests, getApplicationContext());
        guestList.setAdapter(listAdapter);

        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_check_guest_ok);


        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRoomRackOptions();
                dialog.dismiss();
            }
        });

    }

    private void startCommentDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_room_rack_comment, null);

        AppCompatTextView title = (AppCompatTextView) mView.findViewById(R.id.tv_dialog_room_rack_comment_title);
        title.setText(GLOBAL_ROOM_RACK.getTypeProduit() + " " + GLOBAL_ROOM_RACK.getNumChb());

        final TextInputLayout ilComment = (TextInputLayout) mView.findViewById(R.id.il_dialog_room_rack_comment);
        final AppCompatEditText etComment = (AppCompatEditText) mView.findViewById(R.id.et_dialog_room_rack_comment);

        AppCompatButton btnOk = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_comment_ok);
        AppCompatButton btnCancel = (AppCompatButton) mView.findViewById(R.id.btn_dialog_room_rack_comment_cancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(true);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ilComment.setErrorEnabled(false);
                if (etComment.getText().toString().isEmpty()) {
                    ilComment.setError(getString(R.string.error_message_field_required));
                } else {

                    String user = mMySession.getLogin();
                    String prodId = String.valueOf(GLOBAL_ROOM_RACK.getProdId());
                    String typeHebId = String.valueOf(GLOBAL_ROOM_RACK.getTypeHebergement());
                    String typeProdId = String.valueOf(GLOBAL_ROOM_RACK.getTypeProduitId());
                    String statut = "7";
                    String oldStatut = String.valueOf(GLOBAL_ROOM_RACK.getStatutId());
                    String comment = etComment.getText().toString().trim();

                    dialog.dismiss();

                    try {
                        changerProduitStaut(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRoomRackOptions();
                dialog.dismiss();

            }
        });

    }

    private void sortGrid() {

        mRoomRack.clear();
        rlEmptyView.setVisibility(View.GONE);

        if (mStatusId == -1) {
            mRoomRack = GLOBAL_RACK;
        } else if (mStatusId == 8) {
            for (RoomRack rr : GLOBAL_RACK) {
                if (rr.getIsAttributed()) {
                    mRoomRack.add(rr);
                }
            }
        } else {
            for (RoomRack rr : GLOBAL_RACK) {
                if (rr.getStatutId() == mStatusId && !(rr.getIsAttributed())) {
                    mRoomRack.add(rr);
                }
            }
        }

        if (mRoomRack.size() > 0) {

            gvRoomRack.setVisibility(View.VISIBLE);
            rlEmptyView.setVisibility(View.GONE);
            mGridAdapter = new RoomRackGridAdapter(getApplicationContext(), mRoomRack);
            gvRoomRack.setAdapter(mGridAdapter);

        } else {

            gvRoomRack.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.VISIBLE);
            tvEmptyViewText.setText(R.string.message_no_maintenance_rooms_to_show);
            imgEmptyViewIcon.setImageResource(R.drawable.ic_priority_high_white_48dp);

        }


    }

    /**********************************************************************************************/

    private void loadRooms(int floor, int block) {

        String floorId = String.valueOf(floor);
        String blockId = String.valueOf(block);

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<RoomRackData> userCall = service.getEtatRackRoomQuery(floorId, blockId);

        llLoadingView.setVisibility(View.VISIBLE);
        gvRoomRack.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<RoomRackData>() {
            @Override
            public void onResponse(Call<RoomRackData> call, Response<RoomRackData> response) {

                llLoadingView.setVisibility(View.GONE);
                gvRoomRack.setVisibility(View.VISIBLE);
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    RoomRackData mData = response.body();
                    if (!(mData.getData() == null)) {
                        mRoomRack = mData.getData();
                        GLOBAL_RACK.clear();
                        GLOBAL_RACK.addAll(mRoomRack);

                        if (!(mStatusId == -1)) {
                            mRoomRack.clear();
                            if (mStatusId == 8) {
                                for (RoomRack rr : GLOBAL_RACK) {
                                    if (rr.getIsAttributed()) {
                                        mRoomRack.add(rr);
                                    }
                                }
                            } else {
                                for (RoomRack rr : GLOBAL_RACK) {
                                    if (rr.getStatutId() == mStatusId && !(rr.getIsAttributed())) {
                                        mRoomRack.add(rr);
                                    }
                                }
                            }
                        }

                        if (mRoomRack.size() > 0) {
                            mGridAdapter = new RoomRackGridAdapter(getApplicationContext(), mRoomRack);
                            gvRoomRack.setAdapter(mGridAdapter);
                        } else {

                            gvRoomRack.setVisibility(View.GONE);
                            rlEmptyView.setVisibility(View.VISIBLE);
                            tvEmptyViewText.setText(R.string.message_no_maintenance_rooms_to_show);
                            imgEmptyViewIcon.setImageResource(R.drawable.ic_priority_high_white_48dp);
                        }

                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<RoomRackData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                gvRoomRack.setVisibility(View.GONE);
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });
    }

    private void changerProduitStaut(String user, String prodId, String typeHebId, String typeProdId, String statut, String oldStatut, String comment) {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.changerProduitStautQuery(user, prodId, typeHebId, typeProdId, statut, oldStatut, comment);

        final ProgressDialog progressDialog = new ProgressDialog(RoomRackActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.message_room_successfully_updated));

                    try {
                        loadRooms(mFloorId, mBlockId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }

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

    private void updateEtatLieu(String brodId, final String tv, final String bar, final String serv) {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.updateEtatLieuQuery(brodId, tv, bar, serv);

        final ProgressDialog progressDialog = new ProgressDialog(RoomRackActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.all_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if (response.raw().code() == 200) {

                    showSnackbar(findViewById(android.R.id.content), getString(R.string.message_room_successfully_updated));

                    if (tv.equals("1")) {
                        GLOBAL_ROOM_RACK.setEtatTV(true);
                    } else {
                        GLOBAL_ROOM_RACK.setEtatTV(false);
                    }

                    if (bar.equals("1")) {
                        GLOBAL_ROOM_RACK.setEtatBar(true);
                    } else {
                        GLOBAL_ROOM_RACK.setEtatBar(false);
                    }

                    if (serv.equals("1")) {
                        GLOBAL_ROOM_RACK.setEtatServiette(true);
                    } else {
                        GLOBAL_ROOM_RACK.setEtatServiette(false);
                    }


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

package com.hotix.myhotixhousekeeping.activites;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.adapters.OrderAdapter;
import com.hotix.myhotixhousekeeping.adapters.OrdersTypesSpinnerAdapter;
import com.hotix.myhotixhousekeeping.adapters.TechniciansSpinnerAdapter;
import com.hotix.myhotixhousekeeping.helpers.MySession;
import com.hotix.myhotixhousekeeping.helpers.MySettings;
import com.hotix.myhotixhousekeeping.models.Generic;
import com.hotix.myhotixhousekeeping.models.Panne;
import com.hotix.myhotixhousekeeping.models.PannesData;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitClient;
import com.hotix.myhotixhousekeeping.retrofit2.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_LOGIN_DATA;
import static com.hotix.myhotixhousekeeping.helpers.ConstantConfig.GLOBAL_PANNE;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFormater;
import static com.hotix.myhotixhousekeeping.helpers.Utils.dateFromString;
import static com.hotix.myhotixhousekeeping.helpers.Utils.setBaseUrl;
import static com.hotix.myhotixhousekeeping.helpers.Utils.showSnackbar;
import static com.hotix.myhotixhousekeeping.helpers.Utils.stringEmptyOrNull;
import static com.hotix.myhotixhousekeeping.helpers.Utils.validDates;

public class MaintenanceOrdersActivity extends AppCompatActivity {

    // Butter Knife BindView Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //landscap
    private AppCompatTextView tvUrgent;
    private AppCompatTextView tvLocation;
    private AppCompatTextView tvType;
    private AppCompatTextView tvDate;
    private AppCompatTextView tvTime;
    private AppCompatTextView tvTreatTime;
    private AppCompatTextView tvDeclaredBy;
    private AppCompatTextView tvRepairedBy;
    private AppCompatTextView tvDescription;
    private NestedScrollView nsvDetailsContainer;

    // Loading View & Empty ListView
    private LinearLayout llLoadingView;
    private RelativeLayout rlEmptyView;
    private AppCompatTextView tvEmptyViewText;
    private AppCompatImageView imgEmptyViewIcon;
    private AppCompatButton btnEmptyViewRefresh;
    private AppCompatEditText etEndDate;
    private AppCompatSpinner spOrdersTypes;
    private SwipeMenuListView lvOrdersList;
    private AppCompatEditText etStartDate;
    private AppCompatTextView etError;
    private AppCompatEditText etComment;
    private MySettings mMySettings;
    private MySession mMySession;
    private ArrayList<Generic> mStates;
    private ArrayList<Panne> mPannes;
    private OrdersTypesSpinnerAdapter mSpinnerAdapter;
    private TechniciansSpinnerAdapter mTecSpinnerAdapter;
    private OrderAdapter mListAdapter;
    private ArrayList<Generic> mTechs;
    private int mTypeId = 1;
    private int techId = -1;
    private boolean btnClose = true;
    private boolean land = false;
    private Drawable mIconOne, mIconTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_orders);
        ButterKnife.bind(this);
        //settings
        mMySettings = new MySettings(getApplicationContext());
        mMySession = new MySession(getApplicationContext());
        //Force portrait on phones
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Check android vertion
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            mIconOne = getResources().getDrawable(R.drawable.ic_visibility_white_36dp, this.getTheme());
            mIconTwo = getResources().getDrawable(R.drawable.ic_check_circle_white_36dp, this.getTheme());
        } else {
            mIconOne = VectorDrawableCompat.create(this.getResources(), R.drawable.ic_visibility_white_36dp, this.getTheme());
            mIconTwo = VectorDrawableCompat.create(this.getResources(), R.drawable.ic_check_circle_white_36dp, this.getTheme());
        }

        init();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            initPortrait();
            land = false;
        } else {
            initLandscape();
            land = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseUrl(this);
        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadOrders(mTypeId);
            } catch (Exception e) {
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
            }
        } else {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
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
        getMenuInflater().inflate(R.menu.orders_menu, menu);
        if (!GLOBAL_LOGIN_DATA.getHasAddPanne()) {
            MenuItem item = menu.findItem(R.id.action_add);
            item.setVisible(false);
        }
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            MenuItem item = menu.findItem(R.id.action_close);
            item.setVisible(false);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE && !btnClose) {
            MenuItem item = menu.findItem(R.id.action_close);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                //Reload Orders List
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadOrders(mTypeId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
                return true;

            case R.id.action_add:
                //Start the NewOrderActivity
                Intent i = new Intent(this, NewOrderActivity.class);
                startActivity(i);
                return true;

            case R.id.action_close:
                startClaimClosingDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**********************************************************************************************/

    @OnClick(R.id.et_orders_start_date)
    public void getStartDate() {
        startDatePickerDialog(etStartDate, dateFromString(etStartDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.et_orders_end_date)
    public void getEndDate() {
        startDatePickerDialog(etEndDate, dateFromString(etEndDate.getText().toString(), "dd/MM/yyyy"));
    }

    @OnClick(R.id.btn_empty_view_refresh)
    public void refreshData() {

        if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
            try {
                loadOrders(mTypeId);
            } catch (Exception e) {
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
            }
        } else {
            showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
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

        etStartDate = (AppCompatEditText) findViewById(R.id.et_orders_start_date);
        etEndDate = (AppCompatEditText) findViewById(R.id.et_orders_end_date);
        spOrdersTypes = (AppCompatSpinner) findViewById(R.id.sp_orders_types);
        lvOrdersList = (SwipeMenuListView) findViewById(R.id.lv_orders_list);

        // Loading View & Empty ListView
        llLoadingView = (LinearLayout) findViewById(R.id.ll_loading_view);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        tvEmptyViewText = (AppCompatTextView) findViewById(R.id.tv_empty_view_text);
        imgEmptyViewIcon = (AppCompatImageView) findViewById(R.id.img_empty_view_icon);
        btnEmptyViewRefresh = (AppCompatButton) findViewById(R.id.btn_empty_view_refresh);

        etStartDate.setText(dateFormater(GLOBAL_LOGIN_DATA.getDateFront(), "dd/MM/yyyy", "dd/MM/yyyy"));
        etEndDate.setText(dateFormater(null, "dd/MM/yyyy", "dd/MM/yyyy"));

        mStates = new ArrayList<Generic>();
        mStates.add(new Generic(1, getString(R.string.text_state_outstanding)));
        mStates.add(new Generic(2, getString(R.string.text_state_fenced)));

        mSpinnerAdapter = new OrdersTypesSpinnerAdapter(getApplicationContext(), mStates);
        spOrdersTypes.setAdapter(mSpinnerAdapter);

        etStartDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadOrders(mTypeId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        etEndDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadOrders(mTypeId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
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

    private void initPortrait() {

        //Create Swipe list menue 1
        final SwipeMenuCreator menuOne = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "checked" item
                SwipeMenuItem checkedItem = new SwipeMenuItem(getApplicationContext());
                checkedItem.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondaryDark)));
                checkedItem.setWidth(180);
                checkedItem.setIcon(mIconOne);
                menu.addMenuItem(checkedItem);

                // create "show" item
                SwipeMenuItem showItem = new SwipeMenuItem(getApplicationContext());
                showItem.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondary)));
                showItem.setWidth(180);
                showItem.setIcon(mIconTwo);
                menu.addMenuItem(showItem);
            }
        };

        //Create Swipe list menue 2
        final SwipeMenuCreator menuTow = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "checked" item
                SwipeMenuItem checkedItem = new SwipeMenuItem(getApplicationContext());
                checkedItem.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondaryDark)));
                checkedItem.setWidth(180);
                checkedItem.setIcon(mIconOne);
                menu.addMenuItem(checkedItem);
            }
        };

        // set menu
        if (mTypeId == 1) {
            lvOrdersList.setMenuCreator(menuOne);
        } else {
            lvOrdersList.setMenuCreator(menuTow);
        }

        lvOrdersList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // show
                        GLOBAL_PANNE = mPannes.get(position);
                        //Start the ShowOrderActivity
                        Intent i = new Intent(getApplicationContext(), ShowOrderActivity.class);
                        if (mTypeId == 1) {
                            i.putExtra("btnClose", true);
                        } else {
                            i.putExtra("btnClose", false);
                        }
                        startActivity(i);
                        break;
                    case 1:
                        // checked
                        GLOBAL_PANNE = mPannes.get(position);
                        startClaimClosingDialog();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        lvOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                lvOrdersList.smoothOpenMenu(position);
            }
        });

        spOrdersTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mTypeId = mStates.get(position).getId();
                if (mTypeId == 1) {
                    lvOrdersList.setMenuCreator(menuOne);
                } else {
                    lvOrdersList.setMenuCreator(menuTow);
                }
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadOrders(mTypeId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void initLandscape() {

        tvDescription = (AppCompatTextView) findViewById(R.id.tv_order_detail_description);
        tvRepairedBy = (AppCompatTextView) findViewById(R.id.tv_order_detail_repaired_by);
        tvDeclaredBy = (AppCompatTextView) findViewById(R.id.tv_order_detail_declared_by);
        tvTreatTime = (AppCompatTextView) findViewById(R.id.tv_order_detail_treatment_time);
        tvTime = (AppCompatTextView) findViewById(R.id.tv_order_detail_time);
        tvDate = (AppCompatTextView) findViewById(R.id.tv_order_detail_date);
        tvType = (AppCompatTextView) findViewById(R.id.tv_order_detail_type);
        tvLocation = (AppCompatTextView) findViewById(R.id.tv_order_detail_location);
        tvUrgent = (AppCompatTextView) findViewById(R.id.tv_order_detail_urgent);

        nsvDetailsContainer = (NestedScrollView) findViewById(R.id.nsv_details_container);

        lvOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                GLOBAL_PANNE = mPannes.get(position);
                loadDetails();
            }
        });

        spOrdersTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                mTypeId = mStates.get(position).getId();
                if (mTypeId == 1) {
                    btnClose = true;
                } else {
                    btnClose = false;
                }
                invalidateOptionsMenu();
                if (validDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim())) {
                    try {
                        loadOrders(mTypeId);
                    } catch (Exception e) {
                        showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_check_settings));
                    }
                } else {
                    showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_invalid_date));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void startDatePickerDialog(final AppCompatEditText et, Date date) {
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                et.setText(dateFormatter.format(newDate.getTime()));
            }

        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadDetails() {

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

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

    private void loadOrders(int typeId) {

        String id = String.valueOf(typeId);
        String pic = mMySettings.getShowPicture() ? "1" : "0";
        String startDate = dateFormater(etStartDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");
        String endDate = dateFormater(etEndDate.getText().toString(), "dd/MM/yyyy", "yyyyMMdd");

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<PannesData> userCall = service.getListPannesClotureQuery(id, pic, startDate, endDate);

        llLoadingView.setVisibility(View.VISIBLE);
        if (land) {
            nsvDetailsContainer.setVisibility(View.GONE);
        }
        lvOrdersList.setVisibility(View.GONE);
        rlEmptyView.setVisibility(View.GONE);

        userCall.enqueue(new Callback<PannesData>() {
            @Override
            public void onResponse(Call<PannesData> call, Response<PannesData> response) {

                llLoadingView.setVisibility(View.GONE);
                lvOrdersList.setVisibility(View.VISIBLE);
                if (land) {
                    nsvDetailsContainer.setVisibility(View.VISIBLE);
                }
                rlEmptyView.setVisibility(View.GONE);

                if (response.raw().code() == 200) {
                    PannesData mData = response.body();
                    if (!(mData.getData() == null)) {
                        mPannes = mData.getData();
                        GLOBAL_PANNE = new Panne();
                        if (mPannes.size() > 0) {
                            GLOBAL_PANNE = mPannes.get(0);
                        }
                        loadDetails();
                        mListAdapter = new OrderAdapter(mPannes, getApplicationContext());
                        lvOrdersList.setAdapter(mListAdapter);
                        tvEmptyViewText.setText(R.string.message_no_maintenance_orders_to_show);
                        imgEmptyViewIcon.setImageResource(R.drawable.ic_sms_failed_white_48dp);
                        lvOrdersList.setEmptyView(rlEmptyView);

                    }

                } else {
                    showSnackbar(findViewById(android.R.id.content), response.message());
                }
            }

            @Override
            public void onFailure(Call<PannesData> call, Throwable t) {
                llLoadingView.setVisibility(View.GONE);
                lvOrdersList.setVisibility(View.GONE);
                if (land) {
                    nsvDetailsContainer.setVisibility(View.GONE);
                }
                rlEmptyView.setVisibility(View.VISIBLE);
                tvEmptyViewText.setText(R.string.error_message_server_unreachable);
                imgEmptyViewIcon.setImageResource(R.drawable.ic_dns_white_48dp);
                showSnackbar(findViewById(android.R.id.content), getString(R.string.error_message_server_unreachable));
            }
        });

    }

    private void closeOrder(String id, String login, String comment, String tecId) {

        RetrofitInterface service = RetrofitClient.getClientHngApi().create(RetrofitInterface.class);
        Call<ResponseBody> userCall = service.cloturePanneQuery(id, login, comment, tecId);

        final ProgressDialog progressDialog = new ProgressDialog(MaintenanceOrdersActivity.this);
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

                    try {
                        loadOrders(mTypeId);
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

}

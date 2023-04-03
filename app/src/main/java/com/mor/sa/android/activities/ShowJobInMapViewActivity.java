package com.mor.sa.android.activities;

import static com.checker.sa.android.adapter.JobItemAdapter.customAlert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.ArchiveData;
import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.Expiration;
import com.checker.sa.android.data.InProgressAnswersData;
import com.checker.sa.android.data.Lists;
import com.checker.sa.android.data.Note;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.POS_Shelf;
import com.checker.sa.android.data.Picture;
import com.checker.sa.android.data.Price;
import com.checker.sa.android.data.Quantity;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Sets;
import com.checker.sa.android.data.SubmitQuestionnaireData;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.UploadingProgressBars;
import com.checker.sa.android.data.filePathDataID;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.data.pngItem;
import com.checker.sa.android.data.validationSets;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.RefusalReasonDialog;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.LanguageDialog;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowJobInMapViewActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    private GoogleMap mMap;
    MarkerOptions markerOptions = new MarkerOptions();
    private SupportMapFragment mapfragment;

    public static ArrayList<orderListItem> joborders;
    ArrayList<Order> jobordersss = null;

    private HashMap<Marker, orderListItem> markersHash;
    private String orderid;
    private CardView cardView;
    private ImageView iv_close, ivLocation, iv_map_list, iv_language;
    ListView lvjdsurvey_quotas_list, lvjdsurvey_allocations_list;
    LinearLayout llSchedulesExpandLayout, llAssignedExpandLayout, llStartJob;
    View view1;
    TextView tv_show_status, tvReject, tvAccept, tv, tvBranchFullName, tvBranch, tvCount, tvDec, tvLocation, tvSurveyName, tvClientDesc, tvQuestionnaire, tvBranch_s, tvBranchFullName_s, tvCity, tvAddress, tvBranchPhone, tvOpeningHours, tvStartTime, tvEndTime, tvPurchaseDescription, tvMakePurchase, tvNonRefundableServicePayment, tvTransportationPayment, tvCriticismPayment, tvBonusPayment, tvStatus, tvTime, tvDate;
    private final int QUESTIONNAIRE_ACTIVITY_CODE = 1;
    private final int JOB_DETAIL_ACTIVITY_CODE = 2;
    private final int JOB_ARCHIVE_ACTIVITY_CODE = 69;
    orderListItem cardItemDetails;
    private ArrayList<Cert> pendingCerts = null;
    private static jobBoardCertsListener certCallBack;
    SharedPreferences myPrefs;

    public int msgId;

    private boolean refreshJoblist;
    private String groupedNumber;
    private int serverGroupedNumber;

    JobbListTask jobListTaskHandler = new JobbListTask(false, false);
    private GoogleApiClient mApiClient;
    String statusname;
    private boolean isBriefing;
    private final int JOB_GPS_CODE = 678;
    Survey survey;
    boolean isSurvey;

    boolean upload_comp_jobs = false, isJobselected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_job_in_map_view);

        cardView = findViewById(R.id.cardView);
        iv_close = findViewById(R.id.iv_close);

        tv = (TextView) findViewById(R.id.tv1);
        tvBranch = (TextView) findViewById(R.id.tvbranch);
        tvCount = (TextView) findViewById(R.id.tvcount);
        tvDec = (TextView) findViewById(R.id.tvDec);
        tvAccept = (TextView) findViewById(R.id.tv_accept);
        tvSurveyName = (TextView) findViewById(R.id.tv_survey_name);
        tvBranchFullName = (TextView) findViewById(R.id.tv_branch_full_name);
        tvClientDesc = (TextView) findViewById(R.id.tv_client_desc);
        tvQuestionnaire = (TextView) findViewById(R.id.tv_questionnaire);
        tvBranch_s = (TextView) findViewById(R.id.tv_branch_s);
        tvBranchFullName_s = (TextView) findViewById(R.id.tv_branch_full_name_s);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvBranchPhone = (TextView) findViewById(R.id.tv_branch_phone);
        tvOpeningHours = (TextView) findViewById(R.id.tv_opening_hours);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvPurchaseDescription = (TextView) findViewById(R.id.tv_purchase_description);
        tvMakePurchase = (TextView) findViewById(R.id.tv_make_purchase);
        tvNonRefundableServicePayment = (TextView) findViewById(R.id.tv_nonRefundableServicePayment);
        tvTransportationPayment = (TextView) findViewById(R.id.tv_transportation_payment);
        tvCriticismPayment = (TextView) findViewById(R.id.tv_criticism_payment);
        tvBonusPayment = (TextView) findViewById(R.id.tv_bonus_payment);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvReject = (TextView) findViewById(R.id.tv_reject);
        tv_show_status = (TextView) findViewById(R.id.tv_show_status);
        tvDate = (TextView) findViewById(R.id.tv_date);

        ivLocation = (ImageView) findViewById(R.id.iv_location);
        iv_map_list = (ImageView) findViewById(R.id.iv_map_list);
        iv_language = (ImageView) findViewById(R.id.iv_language);

        lvjdsurvey_quotas_list = (ListView) findViewById(R.id.quotas_list);
        lvjdsurvey_allocations_list = (ListView) findViewById(R.id.allocations_list);

        llSchedulesExpandLayout = (LinearLayout) findViewById(R.id.ll_schedules_expand_layout);
        llAssignedExpandLayout = (LinearLayout) findViewById(R.id.ll_assigned_expand_layout);
        llStartJob = (LinearLayout) findViewById(R.id.ll_start_job);

        view1 = (View) findViewById(R.id.view1);

        cardView.setVisibility(View.GONE);

        iv_map_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
            }
        });

        mapfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapfragment.getMapAsync(this);

        //...........
        Log.e("onCreate", "true");
        initGoogleApiClient();
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor outState = myPrefs.edit();

        if (outState != null) {
            outState.putBoolean("ispaused", false);
            outState.commit();
        }

        outState.putString(Constants.Crash_Last_SETID, "");
        outState.putString(Constants.Crash_Last_ORDERID, "");
        outState.commit();

        try {
            LongOperation op = new LongOperation();
            op.execute();
        } catch (Exception ex) {
        }

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        Bundle b = getIntent().getExtras();
        boolean login_check = false;
        if (b != null) {
            login_check = b.getBoolean(Constants.IS_LOGIN);
        }

        Log.e("login_check_map", String.valueOf(login_check));
        DBAdapter db = new DBAdapter(this.getApplicationContext());

        try {

            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

            db.createDataBase(Helper.getSystemURLfromDB(),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Surveys.setSets(DBHelper.getSurveyyRecords());
        Helper.setConfigChange(false);

        loadOfflineJobs(login_check);

        iv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapView(joborders);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "true");

        cardView.setVisibility(View.GONE);
        try {
            if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                mMap.clear();
                LongOperation op = new LongOperation();
                op.execute();
                setMapView(joborders);
                // add markers from database to the map
            }
        } catch (Exception e) {
        }

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId, int width, int height) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void setMapView(List<orderListItem> orders) {
        for (int i = 0; i < orders.size(); i++) {
            try {


                double latitude = Double.parseDouble(orders.get(i).orderItem.getBranchLat());
                double longitude = Double.parseDouble(orders.get(i).orderItem.getBranchLong());
                LatLng latlng = new LatLng(latitude, longitude);
                markerOptions.position(latlng);
                markerOptions.title(orders.get(i).orderItem.getBranchFullname());

                if (orders.get(i).orderItem.getStatusName().equals("Assigned")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.assigned_job_location, 100, 140));
                } else if (orders.get(i).orderItem.getStatusName().equals("Scheduled") || orders.get(i).orderItem.getStatusName().equals("cert")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.scheduled_job_location, 120, 160));
                } else if (orders.get(i).orderItem.getStatusName().equals("Completed")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.completed_job_location, 140, 180));
                } else if (orders.get(i).orderItem.getStatusName().equals("in progress") || orders.get(i).orderItem.getStatusName().equals("In progress") || orders.get(i).orderItem.getStatusName().toLowerCase().equals("archived")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.in_progress_job_location, 160, 200));
                }

                if (markersHash == null)
                    markersHash = new HashMap<Marker, orderListItem>();
                if ((orderid == null || orderid.equals("-1"))
                        || orders.get(i).orderItem.getOrderID().equals(orderid)) {
                    // Animating to the touched position
                    mMap.animateCamera(CameraUpdateFactory
                            .newLatLng(latlng));

                    // Placing a marker on the touched position
                    Marker thismarker = mMap.addMarker(markerOptions);
                    markersHash.put(thismarker, orders.get(i));

                    // Setting a custom info window adapter for the google map
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoContents(Marker arg0) {

                            cardView.setVisibility(View.VISIBLE);

                            final orderListItem thiItem = markersHash.get(arg0);
                            cardItemDetails = thiItem;
                            if (thiItem != null) {
                                initJobDetails(thiItem, ShowJobInMapViewActivity.this);
                            }

                            return null;
                        }

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                    });

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                        }
                    });
                }
            } catch (Exception ex) {

            }
        }
    }


    private void initJobDetails(orderListItem thiItem, Context con) {

        if (thiItem.orderItem.getOrderID().contains("-"))
            tv_show_status.setText(con.getString(R.string.jd_begin_btn_text));
        else if (thiItem.orderItem.getStatusName().equals("Assigned"))
            tv_show_status.setText(con.getString(R.string.jd_accept_btn_text));
        else if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert"))
            tv_show_status.setText(con.getString(R.string.jd_begin_review_btn_text));
        else if (thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().toLowerCase().equals("archived"))// (getString(R.string.jd_begin_button_status_inprogress)))
            tv_show_status.setText(con.getString(R.string.jd_continue_review_btn_text));
        else if (thiItem.orderItem.getStatusName().equals("Completed"))// (getString(R.string.jd_begin_button_status_completed)))
        {
            // rejectbtn.setText(getString(R.string.jd_synch_button_text));
            tv_show_status.setText(con.getString(R.string.jd_continue_review_btn_text));
        }

        if (thiItem.orderItem.getOrderID().contains("-")) {
            llAssignedExpandLayout.setVisibility(View.VISIBLE);
            llSchedulesExpandLayout.setVisibility(View.GONE);
        } else if (thiItem.orderItem != null && thiItem.orderItem.getSetName() != null
                && !thiItem.orderItem.getSetName().equals("")) {
            llAssignedExpandLayout.setVisibility(View.GONE);
            llSchedulesExpandLayout.setVisibility(View.VISIBLE);
        }

        if (thiItem.orderItem.getStatusName().equals("Assigned") || thiItem.orderItem.getStatusName().equals("Survey")) {
            llStartJob.setVisibility(View.GONE);
            tvAccept.setVisibility(View.VISIBLE);
            Log.e("getStatusName", thiItem.orderItem.getStatusName() + " , " + thiItem.orderItem.getOrderID());
        } else {
            llStartJob.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.GONE);
            Log.e("getStatusName_else", thiItem.orderItem.getStatusName() + " , " + thiItem.orderItem.getOrderID());
        }

        //Assigned tab data
        if (thiItem.orderItem.getOrderID().contains("-")) {
            Survey s = Surveys.getCurrentSurve(thiItem.orderItem.getOrderID().replace(
                    "-", ""));
            if (s != null && s.getSurveyName() != null && s.getBranchFullName() != null) {
                // tv.setText(order.getOrderID()+s.getSurveyName());
                tv.setText(s.getSurveyName());
                tvSurveyName.setText(s.getSurveyName());
                tvBranchFullName.setText(s.getBranchFullName());

            } else {
                if (!Constants.getDateFilter())
//                    ivRight.setVisibility(RelativeLayout.GONE);
                    // iv.setBackgroundResource(getResource("wrong"));

                    // Spanned sp = Html.fromHtml(s.getSurveyName());
                    tv.setText("Could not retrieve Survey...");
//                return row;
            }

            if (s.getBranchName() != null) {
                tvBranch.setVisibility(View.VISIBLE);
                tvBranch.setText(s.getBranchName());
            } else {
                tvBranch.setVisibility(View.GONE);
            }


            tvCount.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);

            try {
                if (thiItem.orderItem.getBranchLong() != null && thiItem.orderItem.getBranchLat() != null) {
                    double location_latitude = Double.parseDouble(thiItem.orderItem.getBranchLat());
                    double location_longitude = Double.parseDouble(thiItem.orderItem.getBranchLong());
                    String get_location = String.valueOf(CalculationByDistance(Constants.user_location_latitude, location_latitude, Constants.user_location_longitude, location_longitude));
                    tvLocation.setText(get_location + " Km");
                    tvLocation.setVisibility(View.VISIBLE);
                    ivLocation.setVisibility(View.VISIBLE);
                } else if (thiItem.orderItem.getBranchLong() == null || thiItem.orderItem.getBranchLat() == null) {
                    tvLocation.setVisibility(View.GONE);
                    ivLocation.setVisibility(View.GONE);
                }
            } catch (NumberFormatException e) {

            }


            if (s != null && s.getArrayQuotas() != null) {
                String str[] = s.getArrayQuotas();

                ArrayAdapter adapter = new ArrayAdapter(con,
                        UIHelper.getListLayoutSize(con), str);
                lvjdsurvey_quotas_list.setAdapter(adapter);

                setListViewHeightBased(lvjdsurvey_quotas_list);
            }


            if (s != null && s.getArrayAllocations() != null) {

                String str[] = s.getArrayAllocations();

                ArrayAdapter adapter = new ArrayAdapter(con,
                        UIHelper.getListLayoutSize(con), str);
                lvjdsurvey_allocations_list.setAdapter(adapter);
                setListViewHeightBased(lvjdsurvey_allocations_list);
            }


        } else if (thiItem.orderItem != null && thiItem.orderItem.getSetName() != null
                && !thiItem.orderItem.getSetName().equals("")) {
            if (thiItem.orderItem.getClientName() == null)
                thiItem.orderItem.setClientName("");
            if (thiItem.orderItem.getSetName() == null)
                thiItem.orderItem.setSetName("");
//                Spanned sp = Html.fromHtml(order.getClientName() + ", "
//                        + order.getSetName());

            tv = getTextFromHtmlFormate(thiItem.orderItem.getClientName(), tv);
            tvDec = getTextFromHtmlFormate(thiItem.orderItem.getSetName(), tvDec);
            tvClientDesc = getTextFromHtmlFormate(thiItem.orderItem.getDescription(), tvClientDesc);
            tvQuestionnaire = getTextFromHtmlFormate(thiItem.orderItem.getSetName(), tvQuestionnaire);
            tvBranch_s = getTextFromHtmlFormate(thiItem.orderItem.getBranchName(), tvBranch_s);
            tvBranch = getTextFromHtmlFormate(thiItem.orderItem.getBranchName(), tvBranch);
            tvBranchFullName_s = getTextFromHtmlFormate(thiItem.orderItem.getBranchFullname(), tvBranchFullName_s);
            tvCity = getTextFromHtmlFormate(thiItem.orderItem.getCityName(), tvCity);
            tvAddress = getTextFromHtmlFormate(thiItem.orderItem.getAddress(), tvAddress);
            tvBranchPhone = getTextFromHtmlFormate(thiItem.orderItem.getBranchPhone(), tvBranchPhone);
            tvOpeningHours = getTextFromHtmlFormate(thiItem.orderItem.getOpeningHours(), tvOpeningHours);
            tvStartTime = getTextFromHtmlFormate(thiItem.orderItem.getTimeStart(), tvStartTime);
            tvTime = getTextFromHtmlFormate(thiItem.orderItem.getTimeStart(), tvTime);
            tvEndTime = getTextFromHtmlFormate(thiItem.orderItem.getTimeEnd(), tvEndTime);
            tvPurchaseDescription = getTextFromHtmlFormate(thiItem.orderItem.getsPurchaseDescription(), tvPurchaseDescription);
            tvNonRefundableServicePayment = getTextFromHtmlFormate(thiItem.orderItem.getsNonRefundableServicePayment(), tvNonRefundableServicePayment);
            tvTransportationPayment = getTextFromHtmlFormate(
                    thiItem.orderItem.getsTransportationPayment(), tvTransportationPayment);
            tvCriticismPayment = getTextFromHtmlFormate(
                    thiItem.orderItem.getsCriticismPayment(), tvCriticismPayment);
            tvBonusPayment = getTextFromHtmlFormate(thiItem.orderItem.getsBonusPayment(),
                    tvBonusPayment);
            tvStatus = getTextFromHtmlFormate(thiItem.orderItem.getStatusName(), tvStatus);
            Spanned sp = Html.fromHtml(getDate(thiItem.orderItem.getDate()));
            tvDate.setText(sp.toString());

            try {
                if (thiItem.orderItem.getBranchLong() != null && thiItem.orderItem.getBranchLat() != null) {
                    double location_latitude = Double.parseDouble(thiItem.orderItem.getBranchLat());
                    double location_longitude = Double.parseDouble(thiItem.orderItem.getBranchLong());
                    String get_location = String.valueOf(CalculationByDistance(Constants.user_location_latitude, location_latitude, Constants.user_location_longitude, location_longitude));
                    tvLocation.setText(get_location + " Km");
                    tvLocation.setVisibility(View.VISIBLE);
                    ivLocation.setVisibility(View.VISIBLE);
                } else {
                    tvLocation.setVisibility(View.GONE);
                    ivLocation.setVisibility(View.GONE);
                }
            } catch (NumberFormatException e) {

            }


            if ((thiItem.orderItem != null && thiItem.orderItem.getsPurchase() != null && thiItem.orderItem
                    .getsPurchase().equals("1"))) {
                tvMakePurchase = getTextFromHtmlFormate(
                        con.getString(R.string.questionnaire_exit_delete_alert_yes),
                        tvMakePurchase);
            } else {
                tvMakePurchase = getTextFromHtmlFormate(
                        con.getString(R.string.questionnaire_exit_delete_alert_no),
                        tvMakePurchase);
            }
        } else {
            if (thiItem.orderItem.getClientName() == null)
                thiItem.orderItem.setClientName("");
            Spanned sp = Html.fromHtml(thiItem.orderItem.getClientName());
            tv.setText(sp.toString());
        }


        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_show_status.getText().toString()
                        .equals(getString(R.string.jd_begin_btn_text))) {
                    if (thiItem.orderItem.getOrderID().contains("-")) {
                        if (thiItem.orderItem.getStatusName().equals("survey")) {
                            Survey s = Surveys.getCurrentSurve(thiItem.orderItem.getOrderID().replace(
                                    "-", ""));
                            if (s != null && s.isAllocationReached()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowJobInMapViewActivity.this);
                                builder.setMessage(
                                                getResources()
                                                        .getString(
                                                                R.string.questionnaire_open_survey_alert))
                                        .setTitle(
                                                getResources().getString(
                                                        R.string._alert_title))
                                        .setCancelable(false)
                                        .setPositiveButton(
                                                getResources().getString(
                                                        R.string.button_ok),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                SplashScreen.addLog(new BasicLog(
                                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + thiItem.orderItem.getSetName() + "status:" + thiItem.orderItem.getStatusName(), thiItem.orderItem.getOrderID()));

                                startLocationChecker(thiItem);
                            }

                        } else {
                            SplashScreen.addLog(new BasicLog(
                                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting survey!" + thiItem.orderItem.getSetName() + "status:" + thiItem.orderItem.getStatusName(), thiItem.orderItem.getOrderID()));

                            startLocationChecker(thiItem);
                        }
                    }
                }
                if (tv_show_status.getText().toString()
                        .equals(getString(R.string.jd_accept_btn_text))) {
                    int ins = IsInternetConnectted_AcceptBtn();
                    if (ins >= 0) {
                        ShowAlert(ShowJobInMapViewActivity.this,
                                getString(R.string.jd_parsing_alert_title),
                                getString(R.string.accept_job_fail_alert),
                                getString(R.string.alert_btn_lbl_ok));
                        return;
                    }

                    someMethod(getString(R.string.jd_accept_btn_text));

                } else if (tv_show_status.getText().toString()
                        .equals(getString(R.string.jd_begin_review_btn_text))) {
                    if (isBriefing == true) {
                        isBriefing = false;
//                        showBriefing();
                    } else {
                        SplashScreen.addLog(new BasicLog(
                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + thiItem.orderItem.getOrderID() + " = " + thiItem.orderItem.getSetName() + "status:" + thiItem.orderItem.getStatusName(), thiItem.orderItem.getOrderID()));

                        startLocationChecker(thiItem);
                    }

                } else if (tv_show_status.getText().toString()
                        .equals(getString(R.string.jd_continue_review_btn_text))) {
                    SplashScreen.addLog(new BasicLog(
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + thiItem.orderItem.getOrderID() + " = " + thiItem.orderItem.getSetName() + "status:" + thiItem.orderItem.getStatusName(), thiItem.orderItem.getOrderID()));

                    if (Settings.Secure.getString(
                            ShowJobInMapViewActivity.this.getContentResolver(),
                            Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
                        MockGPSALERT();
                        return;
                    }

                    BeginReview(false, thiItem);
                }

                llStartJob.setVisibility(View.VISIBLE);
                tvAccept.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
            }
        });

        llStartJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if (this.isBriefing == true) {
//                        isBriefing = false;
//                        showBriefing();
//                    } else {
//                        SplashScreen.addLog(new BasicLog(
//                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting Order!" + order.getOrderID() + " = " + order.getSetName() + "status:" + order.getStatusName(), order.getOrderID()));
//
//                        startLocationChecker();
//                    }
                //"OrderID" - Assigned
                //"SurveyID" - Scheduled,Other

//                if (thiItem.orderItem.getOrderID().contains("-")) {
////                        Toast.makeText(con, "Assigned tab", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(con.getApplicationContext(),
//                            QuestionnaireActivity.class);
//                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
//                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
////                        if (isFromWatch)
////                            con.startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
////                        else
//                    con.startActivity(intent);
//                } else if (thiItem.orderItem != null && thiItem.orderItem.getSetName() != null
//                        && !thiItem.orderItem.getSetName().equals("")) {
////                        Toast.makeText(con, "Scheduled tab", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(con.getApplicationContext(),
//                            QuestionnaireActivity.class);
//                    if (thiItem.orderItem == null)
//                        setOrder(thiItem);
//                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
//                            thiItem.orderItem.getOrderID());
//                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
////                        if (isFromWatch)
////                            startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
////                        else
//                    con.startActivity(intent);
//
//                }

//                if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert")) {
//                    BeginReview(true, thiItem);
//
//                } else if (thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().toLowerCase().equals("archived")) {
//                    BeginReview(true, thiItem);
//                }

//                if (thiItem.orderItem.getOrderID().contains("-")) {
//                    Intent intent = new Intent(getApplicationContext(),
//                            QuestionnaireActivity.class);
//                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
//                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
//
//                    if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert"))
//                        startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                    else
//                        startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
//
//                } else {
//                    Intent intent = new Intent(getApplicationContext(),
//                            QuestionnaireActivity.class);
//                    if (thiItem.orderItem == null)
//                        setOrder(thiItem);
//                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
//                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
//                    if (thiItem.orderItem.getStatusName().equals("Completed") || thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().equals("archived"))
//                        startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                    else
//                        startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
//                }

                //...........
                isJobselected = true;
                Intent intent = new Intent(ShowJobInMapViewActivity.this
                        .getApplicationContext(), JobDetailActivity.class);
                if (thiItem != null) {
                    if (thiItem.orderItem != null) {
//                            TODO OrderID
                        intent.putExtra("OrderID", thiItem.orderItem.getOrderID());
                        intent.putExtra(
                                Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                                thiItem.orderItem
                                        .getCount() + "");
                        String OrderID = thiItem.orderItem.getOrderID();
                        String surveyId = "";
                        if (OrderID.contains("-")) {
                            surveyId = (OrderID.replace("-", ""));
                            Survey survey = Surveys.getCurrentSurve(surveyId);
                            boolean b = survey.isAllocationReached();
                            if (b)//ALLOCATION REACHED
                            {
                                Toast.makeText(ShowJobInMapViewActivity.this,
                                        getString(R.string.questionnaire_open_survey_alert)
                                        , Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } else if (thiItem.surveyItem != null) {
//                            TODO SurveyID
                        intent.putExtra("SurveyID", thiItem.surveyItem.getSurveyID());

                    }

                } else
                    intent.putExtra(
                            Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                            "1");
                startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
            }
        });

        tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thiItem.orderItem != null
                        && thiItem.orderItem.getAllowShoppersToRejectJobs() != null
                        && thiItem.orderItem.getAllowShoppersToRejectJobs().equals("2")) {
                    Toast.makeText(ShowJobInMapViewActivity.this,
                            "You are not allowed to reject jobs.",
                            Toast.LENGTH_LONG).show();
                }
                if (thiItem.orderItem != null
                        && thiItem.orderItem.getAllowShoppersToRejectJobs() != null
                        && thiItem.orderItem.getAllowShoppersToRejectJobs().equals("1")
                        && !thiItem.orderItem.getStatusName().equals("Assigned")) {

                    Toast.makeText(ShowJobInMapViewActivity.this,
                            "You are not allowed to reject this job.",
                            Toast.LENGTH_LONG).show();
                }
                Constants.screen_type_dialog = 3;
                RefusalReasonDialog dialog = new RefusalReasonDialog(ShowJobInMapViewActivity.this);
                dialog.show();
            }
        });


    }

    private TextView getTextFromHtmlFormate(String html, TextView tv) {
        if (html == null)
            html = "";
        Spanned sp = Html.fromHtml(html);
        if (tv != null && sp != null)
            tv.setText(sp, TextView.BufferType.SPANNABLE);
        return tv;
    }

    public void setListViewHeightBased(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            try {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception ex) {

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = 150 + totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public double CalculationByDistance(double StartP_latitude, double EndP_latitude, double StartP_longitude, double EndP_longitude) {
        int Radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(EndP_latitude - StartP_latitude);
        double dLon = Math.toRadians(EndP_longitude - StartP_longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(StartP_latitude))
                * Math.cos(Math.toRadians(EndP_latitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Integer.valueOf(newFormat.format(km));
    }

    private void setOrder(orderListItem thiItem) {
        Bundle b = getIntent().getExtras();

        if (b == null)
            return;

        String OrderID = thiItem.orderItem.getOrderID();
        String SurveyID = thiItem.surveyItem.getSurveyID();

        if (SurveyID != null && !SurveyID.equals("")
                && Surveys.getSets() != null) {
            for (int i = 0; i < Surveys.getSets().size(); i++) {
                survey = Surveys.getSets().get(i);
                thiItem.orderItem = null;
                if (survey.getSurveyID().equals(SurveyID))
                    break;
            }
            isBriefing = false;
        } else {
            for (int i = 0; i < Orders.getOrders().size(); i++) {
                thiItem.orderItem = Orders.getOrders().get(i);
                if (thiItem.orderItem != null && thiItem.orderItem.getOrderID().equals(OrderID))
                    break;
            }

            if (thiItem.orderItem != null) {
                isSurvey = OrderID.contains("-");

                if (thiItem.orderItem.getBriefingContent() != null
                        && !thiItem.orderItem.getBriefingContent().equals("")) {
                    isBriefing = true;
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean dontrun = false;
        comunicator.JobList = ShowJobInMapViewActivity.this;
        switch (requestCode) {
            case (JOB_ARCHIVE_ACTIVITY_CODE): {
                if (ArchiveActivity.toBeUploadedSQ != null) {
                    SubmitSurveyTask sbmtSurveyTask = new SubmitSurveyTask(ArchiveActivity.toBeUploadedSQ);
                    sbmtSurveyTask.execute();
                }
                break;
            }
            case (QUESTIONNAIRE_ACTIVITY_CODE):
                if (data != null && data.hasExtra("from_watch")) {
                    Intent intent = new Intent();
                    intent.putExtra("from_watch", true);
                    setResult(2, intent);
                    finish();
                }
                break;
            case (JOB_DETAIL_ACTIVITY_CODE): {
                if (data != null && data.hasExtra("from_watch")) {
                    ShowDBJobs();
                    return;
                }
                String orderid = "";
                if (data != null) {
                    // stopLocationChecker();
                    Bundle b = data.getExtras();
                    if (b != null
                            && b.getBoolean(Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY)) {

                        // new SubmitSurveyTask().execute();
                        executeJobList(false, false);
                        // ShowDBJobs();
                        return;
                    } else if (b
                            .getBoolean(Constants.JOB_DETAIL_IS_INVALID_LOGIN_FIELD_KEY)) {
                        Intent intent = new Intent(this.getApplicationContext(),
                                NewLoginActivity.class);
                        // comunicator.JobList = null;
                        startActivity(intent);
                        finish();
                        return;
                    }
                    orderid = b.get(Constants.DB_TABLE_QUESTIONNAIRE_ORDERID)
                            .toString();

                    if (data.getExtras().getInt(Constants.QUESTIONNAIRE_STAUS) == 1) {
                        SplashScreen.addLog(new BasicLog(
                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                                "Job moved to Completed tab" + orderid, orderid));

                        DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
                                new String[]{Constants.DB_TABLE_ORDERS_ORDERID,
                                        Constants.DB_TABLE_ORDERS_STATUS,
                                        Constants.DB_TABLE_ORDERS_START_TIME,},
                                orderid, "Completed", "", null);
                        if (orderid != null && orderid.contains("CC")) {
                            upload_comp_jobs = true;
                        }
                        if (upload_comp_jobs == true || Helper.isMisteroMenu
                                || Constants.isUploadingEnabled()) {
                            start_uploading(false);
                            dontrun = true;
                        } else
                            ShowDBJobs();
                    } else if (data.getExtras().getInt(
                            Constants.QUESTIONNAIRE_STAUS) == 2
                            && !orderid.contains("CC")) {
                        DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
                                new String[]{Constants.DB_TABLE_ORDERS_ORDERID,
                                        Constants.DB_TABLE_ORDERS_STATUS,
                                        Constants.DB_TABLE_ORDERS_START_TIME,},
                                orderid, "Scheduled", "", null);
                    } else if (data.getExtras().getInt(
                            Constants.QUESTIONNAIRE_STAUS) == 142
                            || data.getExtras().getInt(
                            Constants.QUESTIONNAIRE_STAUS) == 42) {
                        // if (IsInternetConnectted()) {
                        // start_uploading(false);
                        // upload_comp_jobs = true;
                        // return;
                        // }
                        ShowDBJobs();

                    } else if (!orderid.contains("-")) {
                        changeJobStatus("In progress", orderid, b.getString(Constants.DB_TABLE_ORDERS_START_TIME), b.getString(Constants.DB_TABLE_ORDERS_LASTDATAID));
                    }
                }
                setOrderList();
                setMapView(joborders);
                if (!dontrun) {
                    setMapView(joborders);
                    ShowOrphanFiles();
                }
                break;
            }
        }

    }


    public void rejectJob(String str) {
        if (IsInternetConnecttedAdapter() >= 0) {
            ShowAlertButton(this,
                    getString(R.string.jd_parsing_alert_title),
                    getString(R.string.reject_job_fail_alert),
                    getString(R.string.alert_btn_lbl_ok));
            return;
        }
        new JobTask().execute("", str);
    }

    public int IsInternetConnecttedAdapter() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        conMgr = null;
        if (i == null)
            return 0;
        return -1;
    }

    public void ShowAlertButton(Context context, String title, final String message,
                                String button_lbl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.mapview_custom_dialog, null);
        builder.setView(customLayout);

        TextView title_text = customLayout.findViewById(R.id.title);
        title_text.setText(title);
        TextView message_text = customLayout.findViewById(R.id.text);
        message_text.setText(message);

        Button btn = customLayout.findViewById(R.id.dialogButtonOK);
        btn.setText(button_lbl);

        // create the alert dialog
        AlertDialog dialog_ = builder.create();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_.dismiss();
                Revamped_Loading_Dialog.hide_dialog();
                if (message
                        .equals(getString(R.string.reject_job_sucess_alert))
                        || message
                        .equals(getString(R.string.error_order_not_assigned))
                        || message
                        .equals(getString(R.string.alert_sync_jobs_again)))
                    showJobList();
            }
        });

        // show the alert dialog
        dialog_.show();
    }

    private void showJobList() {
        Intent intent = new Intent();
        intent.putExtra(Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY, true);
        setResult(RESULT_OK, intent);

//        TODO
        jobListTaskHandler = new JobbListTask(false, false);
        jobListTaskHandler.execute();
    }

    public class JobTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this, null);
        }

        private ArrayList<Cert> parseCertificateResult(String result) {

            Parser parser = new Parser();
            parser.parseXMLValues(result, Constants.CERTS_FIELD_PARAM);
            return parser.listCerts;

        }

        @Override
        protected String doInBackground(String... params) {
            if (Connector.cookies == null) {
                if (showLoginAdapter(doLogin()))
                    return "SessionExpire";
            }

            String result = "";
            if (params[0].equals(getString(R.string.jd_accept_btn_text))) {
                Set set = null;
                try {
                    set = (Set) DBHelper.convertFromBytes(cardItemDetails.orderItem.getSetID());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (set != null && set.getCertificates() != null
                        && set.getCertificates().size() > 0) {
                    List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
                    String data = Connector.postForm(
                            Constants.getCheckerTificates("9.7", true), extraDataList);
                    if (data.contains("<script>")) {
                        doLogin();
                        data = Connector
                                .postForm(Constants.getCheckerTificates("9.7", true),
                                        extraDataList);
                    }
                    if (data != null && data.contains("<status>1</status>")) {
                        return null;
                    } else {
                        // no error parse here
                        ArrayList<Cert> listOfCerts = parseCertificateResult(data);

                        ArrayList<Cert> shortList = set.getCertificates();

                        if (shortList != null && listOfCerts != null) {
                            ArrayList<Cert> templistOfCerts = new ArrayList<Cert>();
                            for (int i = 0; i < listOfCerts.size(); i++) {
                                boolean isPresent = false;
                                for (int j = 0; j < shortList.size(); j++) {
                                    String certID = shortList.get(j)
                                            .getCertificateID();
                                    if (listOfCerts.get(i) != null
                                            && listOfCerts.get(i)
                                            .getCertificateID() != null
                                            && listOfCerts.get(i)
                                            .getCertificateID()
                                            .equals(certID)
                                            && !listOfCerts.get(i).getStatus().toLowerCase()
                                            .equals("passed")) {
                                        isPresent = true;
                                        break;
                                    }

                                }
                                if (isPresent)
                                    templistOfCerts.add(listOfCerts.get(i));
                            }
                            if (templistOfCerts != null && templistOfCerts.size() > 0) {
                                ShowJobInMapViewActivity.this.pendingCerts = templistOfCerts;
                                return null;
                            }
                        }
                    }
                }

                doLogin();

                result = AcceptJob();
                return result;
            } else {
                doLogin();
                return RejectJob(params[1]);
            }
        }

        public String doLogin() {
            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            return loginPost(
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
                    Constants.POST_VALUE_LOGIN_DO_LOGIN);
        }

        private String loginPost(final String username, final String password,
                                 String dologin) {
            // Initialize the login data to POST
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_LOGIN_USERNAME, username));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_LOGIN_PASSWORD, password));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_LOGIN_DO_LOGIN, dologin));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_LOGIN_NO_REDIR,
                    Constants.POST_VALUE_LOGIN_NO_REDIR));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_LOGIN_IS_APP,
                    Constants.POST_VALUE_LOGIN_IS_APP));
            return Connector.postForm(Constants.getLoginURL(), extraDataList);
        }

        private void On_ExitanddeleteButton_Click(String orderID) {
            String where = Constants.DB_TABLE_QUESTIONNAIRE_ORDERID + "="
                    + "\"" + orderID + "\"";
            DBAdapter.openDataBase();
            DBAdapter.LogCommunication("checkerDBLog.txt",
                    "jobdetail-deleteThisQuestionnaire=" + where);
            DBAdapter.db.delete(Constants.DB_TABLE_QUESTIONNAIRE, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_ANSWERS, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_POS, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_ORDERS, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_SUBMITSURVEY, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_JOBLIST, where, null);

        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null && pendingCerts != null) {
                ShowCertAlert(pendingCerts);
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }
            if (result == null || result.equals("SessionExpire")) {
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }
            if (result != null && result.endsWith("r")) {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Rejecting Order!" + cardItemDetails.orderItem.getOrderID() + " Reply from server= " + result, cardItemDetails.orderItem.getOrderID()));

                result = result.substring(0, result.length() - 1);
                // dialog.onPostExecute();
                if (CheckResponseAdapter(result, getString(R.string.reject_job_fail_alert))) {
                    ShowAlertButton(ShowJobInMapViewActivity.this,
                            getString(R.string.jd_parsing_alert_title),
                            getString(msgId),
                            getString(R.string.alert_btn_lbl_ok));

                    // On_ExitanddeleteButton_Click(order.getOrderID());
                    DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
                            new String[]{Constants.DB_TABLE_ORDERS_ORDERID,
                                    Constants.DB_TABLE_ORDERS_STATUS,
                                    Constants.DB_TABLE_ORDERS_START_TIME,},
                            cardItemDetails.orderItem.getOrderID(), "Scheduled", "", null);
                } else {
                    if (refreshJoblist == true) {
                        // showSyncAlert();
                    }
                }
                Revamped_Loading_Dialog.hide_dialog();
            } else {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Accepting Order!" + cardItemDetails.orderItem.getOrderID() + " Reply from server= " + result, cardItemDetails.orderItem.getOrderID()));

                if (CheckResponseAdapter(result,
                        getString(R.string.invalid_server_response_alert))) {
//                    tvAccept.setText(getString(R.string.jd_begin_review_btn_text));
                    if (cardItemDetails.orderItem.getCount() < 2
                            || (groupedNumber != null
                            && groupedNumber.length() > 0 && serverGroupedNumber <= 1)) {
                        saveOfflineQuestionaire();
                        Revamped_Loading_Dialog.hide_dialog();
                        return;
                    }
                    {
                        // SQLiteDatabase db = DBAdapter.openDataBase();
                        for (int i = 0; i < Orders.getOrders().size(); i++) {
                            Order innerorder = Orders.getOrders().get(i);
                            if ((cardItemDetails.orderItem.getBranchLink().equals(innerorder
                                    .getBranchLink()))
                                    && (cardItemDetails.orderItem.getMassID().equals(innerorder
                                    .getMassID()))
                                    && (cardItemDetails.orderItem.getDate().equals(innerorder
                                    .getDate()))
                                    && (cardItemDetails.orderItem.getSetLink().equals(innerorder
                                    .getSetLink()))
                                    && (cardItemDetails.orderItem.getStatusName().equals(innerorder
                                    .getStatusName()))) {
                                if (!cardItemDetails.orderItem.getOrderID().equals(
                                        innerorder.getOrderID())) {
                                    Orders.getOrders().get(i)
                                            .setStatusName("Scheduled");
                                    DBHelper.updateOrders(
                                            Constants.DB_TABLE_ORDERS,
                                            new String[]{
                                                    Constants.DB_TABLE_ORDERS_ORDERID,
                                                    Constants.DB_TABLE_ORDERS_STATUS,
                                                    Constants.DB_TABLE_ORDERS_START_TIME,},
                                            innerorder.getOrderID(),
                                            "Scheduled", "", null);
                                }
                            }
                        }
                    }
                    // DBAdapter.closeDataBase(db);
                    saveOfflineQuestionaire();
                    Revamped_Loading_Dialog.hide_dialog();
                } else {
                    if (refreshJoblist == true) {
                        // showSyncAlert();
                    }
                }

            }
        }

        public String RejectJob(String reason) {
            if (cardItemDetails.orderItem == null || cardItemDetails.orderItem.getOrderID() == null)
                return null;
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_VALUE_JOB_DETAIL_REJECT,
                    Constants.POST_VALUE_JOB_DETAIL_PARAM_VALUE));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_JOB_DETAIL_ORDER_ID,
                    cardItemDetails.orderItem.getOrderID()));

            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_JOB_DETAIL_REFUSAL_REASON, reason));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                    groupedNumber));

            // dialog.onPostExecute();
            String result = Connector.postForm(Constants.getJobStartURL(),
                    extraDataList);
            try {
                if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                    mMap.clear();
                    Log.e("reject", "true");
                    cardView.setVisibility(View.GONE);
                    // add markers from database to the map
                }
            } catch (Exception e) {

            }
            return result + "r";
        }

        private String AcceptJob() {
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            String result = null;
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_VALUE_JOB_DETAIL_ACCEPT,
                    Constants.POST_VALUE_JOB_DETAIL_PARAM_VALUE));
            if (cardItemDetails.orderItem == null || cardItemDetails.orderItem.getOrderID() == null)
                setOrder(cardItemDetails);
            if (cardItemDetails.orderItem != null && cardItemDetails.orderItem.getOrderID() != null) {
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_JOB_DETAIL_ORDER_ID,
                        cardItemDetails.orderItem.getOrderID()));
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                        groupedNumber));
                result = Connector.postForm(Constants.getJobStartURL(),
                        extraDataList);
            }
            try {
                Log.e("accept", "true");
                cardView.setVisibility(View.GONE);
            } catch (Exception e) {

            }
            return result;
        }
    }

    private boolean showLoginAdapter(String result) {
        String result1 = new Parser().getValue(result,
                Constants.LOGIN_RESP_FIELD_PARAM);
        if (result1 == null)
            return false;
        if (result1.equals("0")) {
            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, false);
            prefsEditor.commit();
            Intent intent = new Intent(this.getApplicationContext(),
                    NewLoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    public void ShowCertAlert(ArrayList<Cert> pendingCerts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ShowJobInMapViewActivity.this);
        builder.setCancelable(false);
        builder.setMessage(
                        getResources().getString(R.string.attached_certificate_msg))
                .setPositiveButton(getResources().getString(R.string.questionnaire_exit_delete_alert_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ShowJobInMapViewActivity.certCallBack != null)
                            ShowJobInMapViewActivity.certCallBack
                                    .certCallBack(ShowJobInMapViewActivity.this.pendingCerts);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.questionnaire_exit_delete_alert_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false).show();
    }

    public boolean CheckResponseAdapter(String result, String failMessage) {

        msgId = R.string.reject_job_sucess_alert;
        // <GroupedNumber>1</GroupedNumber>
        if (result != null && result.toLowerCase().contains("<groupednumber>")) {
            String parsed = result.toLowerCase();
            int indexOf = parsed.indexOf("<groupednumber>") + 15;
            int indexOf2 = parsed.indexOf("</groupednumber>");
            int i = 1;
            try {
                parsed = parsed.substring(indexOf, indexOf2);

                i = Integer.parseInt(parsed);
            } catch (Exception ex) {

            }


            if (groupedNumber != null && !groupedNumber.equals("")) {
                refreshJoblist = false;
                serverGroupedNumber = i;
                return true;
            } else {
                refreshJoblist = false;
                return true;
            }
        }

        if (!Helper.IsValidResponse(result,
                Constants.JOB_DETAIL_RESP_FIELD_PARAM)) {
            ShowAlertButton(this,
                    getString(R.string.error_alert_title), failMessage,
                    getString(R.string.alert_btn_lbl_ok));
            return false;
        }

        if (result != null
                && result.toLowerCase().contains("order+is+not+assiged")) {
            msgId = R.string.error_order_not_assigned;
            return true;
        }
        result = result.substring(
                result.indexOf(Constants.JOB_DETAIL_RESP_FIELD_PARAM),
                result.indexOf("</status>"));
        if (!(result.endsWith("1"))) {
            ShowAlertButton(this,
                    getString(R.string.error_alert_title), failMessage,
                    getString(R.string.alert_btn_lbl_ok));
            return false;
        }
        return true;
    }

    public void saveOfflineQuestionaire() {
        // SQLiteDatabase db = DBAdapter.openDataBase();
        if (cardItemDetails.orderItem == null)
            setOrder(cardItemDetails);
        DBHelper.updateOrders(Constants.DB_TABLE_ORDERS, new String[]{
                        Constants.DB_TABLE_ORDERS_ORDERID,
                        Constants.DB_TABLE_ORDERS_STATUS,
                        Constants.DB_TABLE_ORDERS_START_TIME,}, cardItemDetails.orderItem.getOrderID(),
                "Scheduled", "", null); // getString(R.string.jd_begin_button_status_scheduled),
        // db);
        // DBAdapter.closeDataBase(db);
        for (int oid = 0; oid < joborders.size(); oid++) {
            if (joborders.get(oid).orderItem.getOrderID().equals(cardItemDetails.orderItem.getOrderID())) {
                joborders.get(oid).orderItem.setStatusName("Scheduled");
                break;
            }
        }
    }


    //TODO Languages Select Dialog..
    Revamped_Loading_Dialog dialog;
    private boolean isBranchPropErr;

    private static final String START_ACTIVITY = "/start_activity";
    private static final String STOP_UPLOAD = "/stop_upload";
    private static final String STOP_DOWNLOAD = "/stop_download";
    ArrayList<BranchProperties> branchProps = null;
    Parser parser;

    private ShowDBListTask showListTaskHandler;

    ArrayList<pngItem> pngItems;

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mApiClient.connect();
    }

    public void showLanguageDialog(String string, final boolean isFirstTime) {
        final LanguageDialog langDialog = new LanguageDialog(
                ShowJobInMapViewActivity.this, string);
        final ArrayList<AltLanguage> allLangs = DBHelper.getLanguages(false);
        langDialog.setBtnListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Integer> selectedList = langDialog.getSelectedIndicies();
                for (int i = 0; i < allLangs.size(); i++) {
                    if (allLangs != null) {
                        allLangs.get(i).setIsSelected("0");
                    }
                }
                for (int i = 0; i < selectedList.size(); i++) {
                    if (allLangs != null) {
                        allLangs.get(selectedList.get(i)).setIsSelected("1");
                    }
                }
                DBHelper.saveLanguages(allLangs, false);
                executeJobList(false, false);
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFirstTime)
                    executeJobList(false, false);
            }
        });
        if (allLangs != null && allLangs.size() > 0)
            langDialog.performClick(allLangs);
        else
            executeLangList();

    }

    private void executeLangList() {
        try {
            LangTask langTaskHandler = new LangTask();
            langTaskHandler.execute();

        } catch (Exception ex) {
            ShowAlert(ShowJobInMapViewActivity.this,
                    ShowJobInMapViewActivity.this.getString(R.string.alert_working),
                    ShowJobInMapViewActivity.this.getString(R.string.alert_working_msg),
                    ShowJobInMapViewActivity.this.getString(R.string.button_ok));
        }
    }

    public class LangTask extends AsyncTask<Void, Integer, String> {
        public LangTask() {
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(
                    ShowJobInMapViewActivity.this,
                    getResources().getString(
                            R.string.downloading_system_languages));
        }

        @Override
        protected void onPostExecute(String result) {

            if (DBHelper.getLanguages(false).size() == 0) {
                Revamped_Loading_Dialog.hide_dialog();
                Toast.makeText(
                        ShowJobInMapViewActivity.this,
                        getResources().getString(
                                R.string.no_alternative_or_server_side),
                        Toast.LENGTH_LONG).show();
                executeJobList(false, false);
            } else {
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language),
                        true);
                Revamped_Loading_Dialog.hide_dialog();
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            checkConnectionPost();
            getLanguages();
            return "0";
        }
    }

    public void executeJobList(boolean isPull, boolean isOrderOnly) {
        try {
            jobListTaskHandler = new JobbListTask(isPull, isOrderOnly);
            jobListTaskHandler.execute();

        } catch (Exception ex) {
            try {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string.alert_working),
                        getString(R.string.alert_working_msg),
                        getString(R.string.button_ok));

            } catch (Exception e) {
            }
        }
    }

    public class JobbListTask extends AsyncTask<Void, Integer, String> {
        boolean isPull = false;
        boolean onlyOrder;
        String loginResponse;

        public JobbListTask(boolean ispull, boolean onlyOrder) {
            this.isPull = ispull;
            this.onlyOrder = onlyOrder;
        }

        @Override
        protected void onPreExecute() {

            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
            loginResponse = myPrefs.getString(Constants.POST_FIELD_LOGIN_RESPONSE, null);

            Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this,
                    getString(R.string.job_download_alert));
            dialog = Revamped_Loading_Dialog.getDialog();

            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
            String ip = getLocalIpAddress();
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putString(Constants.DOWNLOADIP, ip);
            prefsEditor.commit();
        }

        @Override
        protected void onPostExecute(String result) {
            // dialog.onPostExecute();
            if (result.equals("SessionExpire"))
                return;
            if (result.equals("offline")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
            } else if (result.equals("invalid")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }

            if (result.equals("offline")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
            } else if (result.equals("error=")) {
                ShowAlert(ShowJobInMapViewActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));

            }
            sendMessage(STOP_DOWNLOAD, null);
            getInProgressJobs(dialog, Orders.checkDifferenceBWListOfORderIds());
            // executeShowListTask(dialog);
        }

        @Override
        protected String doInBackground(Void... params) {

            if (!checkConnectionPost())
                return "offline";

            //if (Connector.cookies == null)
            {
                if (showLogin(doLogin()))
                    return "invalid";
            }
            if (!this.onlyOrder) {
                dialog.changeMessage(getResources().getString(
                        R.string.update_system_languages));

                getLanguages();
            }
            Parser parser = new Parser(dialog);
            {
                dialog.changeMessage(getResources().getString(
                        R.string.aadownloading_Assigned_jobs));
                String result = JobListPost();
                if (result != null && result.indexOf("<orders>") == -1
                        && result.contains("<script>")) {
                    doLogin();
                    result = JobListPost();
                    // fieldResult = FieldListPost();
                }
                if (result == null || result.contains("timeout"))
                    return "timeout";
                if (result.contains("error="))
                    return "invalid";
                if (result != null) {
                    Orders.setListOfOldORderIds();
                    Orders.clearProps();
                    branchProps = Orders.getBranchProps();
                }
                SplashScreen.addLog(new BasicLog(Constants.getJobListURL("11.90"),
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "DOWNLOADING jobs" + result, "DOWNLOADING"));
                int ret_code = parser.parseXMLValues(result,
                        Constants.JOB_LIST_RESP_FIELD_PARAM);

                if (ret_code == -123) {
                    isBranchPropErr = true;
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putBoolean(Constants.ALREADY_BRANCHPROPERR,
                            isBranchPropErr);

                    prefsEditor.commit();
                } else {
                    isBranchPropErr = false;
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    prefsEditor.putBoolean(Constants.ALREADY_BRANCHPROPERR,
                            isBranchPropErr);

                    prefsEditor.commit();
                }
                Log.e("RESUbLTTTTTTTTTT", result);
                if (this.onlyOrder && Sets.verifySetIds() == 0)
                    return result;
            }

            dialog.changeMessage(getResources().getString(
                    R.string.aadownloading_Assigned_surveys));
            String fieldResult = FieldListPost();

            Helper helper = new Helper();
            helper.deleteFilesInFolder();
            // helper.deleteSetsInFolder();

            dialog.changeMessage(getResources().getString(
                    R.string.aaparsing_assigned_jobs));

            dialog.changeMessage(getResources().getString(
                    R.string.aaparsing_assigned_surveys));
            if (fieldResult != null && fieldResult.contains("<data>"))
                new Parser(dialog).parseXMLValues(fieldResult,
                        Constants.SURVEY_LIST_RESP_FIELD_PARAM);
            else
                new Parser(dialog).parseXMLValues(fieldResult,
                        Constants.SURVEY_LIST_RESP_FIELD_PARAM_OLD);
            if (dialog != null) {
                dialog.changeMessage(getResources().getString(
                        R.string.aasaving_assigned_jobs_surveys_job));
            }

            if (!checkConnectionPost())
                return "offline";

            if (Connector.cookies == null) {
                if (showLogin(doLogin()))
                    return "SessionExpire";
            }

            if (dialog != null) {
                dialog.changeMessage(getString(R.string.questionnaire_downlaod_alert));
            } else {
                Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this,
                        getString(R.string.questionnaire_downlaod_alert));
                dialog = Revamped_Loading_Dialog.getDialog();
            }

            String result = QuestionnaireListPost();
            // Log.e("RESULTTTTTTTTTT", result);
            if (result == null || result.contains("<script>")) {
                doLogin();
                result = QuestionnaireListPost();
            }
            if (result == null)
                return "timeout";
            if (result.contains("timeout"))
                return "timeout";
            if (result.contains("error="))
                return "error=";

            // downloadVideo(
            // "VIDEO LINK",
            // "birthday.mp4");
            saveSetDatatoLocalDB();

            return result;
        }

        private boolean saveSetDatatoLocalDB() {
            if (Sets.getSets() == null || Sets.getSets().isEmpty())
                return false;
            try {
                if (dialog != null)
                    dialog.changeMessage(dialog.getContext().getResources()
                            .getString(R.string.aaparsing_complete));
                DBHelper.deleteRecords(null);
                Parser.allCorrectSets = DBHelper.AddSetss(Sets.getSets(),
                        Revamped_Loading_Dialog.getDialog(), Lists.getAllLists());
            } catch (Exception e) {
            }

            // DBAdapter.closeDataBase(db);
            return true;
        }

        private String FieldListPost() {
            // Initialize the login data to POST
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
            try {
                app_ver = ShowJobInMapViewActivity.this.getPackageManager()
                        .getPackageInfo(ShowJobInMapViewActivity.this.getPackageName(),
                                0).versionName;
            } catch (PackageManager.NameNotFoundException e) {

            }
            String url = Constants.getFieldListURL(app_ver);
            return Connector.postForm(url, extraDataList);
        }

        private String JobListPost() {
            // Initialize the login data to POST
            String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
            try {
                app_ver = ShowJobInMapViewActivity.this.getPackageManager()
                        .getPackageInfo(ShowJobInMapViewActivity.this.getPackageName(),
                                0).versionName;
            } catch (PackageManager.NameNotFoundException e) {

            }

            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            return Connector.postForm(Constants.getJobListURL(app_ver),
                    extraDataList);
        }

    }

    private boolean checkConnectionPost() {

        String newUrlForEUClients =
                Constants.CompareWithNewUrlList(getResources().getStringArray(R.array.eusystems));

        if (newUrlForEUClients != null) {
            String chkurl = Constants.getcheckConnectionURL(newUrlForEUClients);
            boolean isOk = Connector.checkConnection(chkurl);
            if (isOk) {
                setAlternateURL(newUrlForEUClients, myPrefs);
                return isOk;
            }
        }

        String chkurl = Constants.getcheckConnectionURL();
        if (chkurl != null && chkurl.toLowerCase().contains("http://")) {
            String newloginurl = chkurl.replace("http://", "https://");
            boolean isOk = Connector.checkConnection(newloginurl);
            if (isOk) {
                setHttps();
                return isOk;
            }
        }
        // Initialize the login data to POST
        return Connector.checkConnection(chkurl);
    }

    public static void setAlternateURL(String newEurURL, SharedPreferences myPrefs) {
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, newEurURL);
        prefsEditor.commit();
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));

    }

    public void getLanguages() {
        String resultLang = LangListPost();
        if (resultLang.contains("<script>") || resultLang.contains("error")) {
            doLogin();
            resultLang = LangListPost();
        }
        if (resultLang != null && resultLang.indexOf("<lang_list>") > 0
                && resultLang.indexOf("</lang_list>") > 0) {
            resultLang = resultLang.replace("<lang_list>", "<lang_lists>");
            int lastIndexOfLang = resultLang.lastIndexOf("</lang_list>");
            resultLang = resultLang.substring(0, lastIndexOfLang - 1);
            resultLang += "</lang_lists>";

        }
        new Parser(dialog).parseXMLValues(resultLang,
                Constants.DB_TABLE_language);

    }

    private String LangListPost() {
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = ShowJobInMapViewActivity.this.getPackageManager().getPackageInfo(
                    ShowJobInMapViewActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        return Connector.postForm(Constants.getLangListURL(app_ver),
                extraDataList);
    }

    public void ShowAlert(Context context, String title, final String message,
                          String button_lbl) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        TextView textView = new TextView(context);

        setFontSize(textView);
        textView.setText(message);
        alert.setView(textView);
        alert.setPositiveButton(button_lbl,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private void setFontSize(View v) {
        try {
            TextView textView = (TextView) v;
            textView.setTextSize(UIHelper.getFontSize(ShowJobInMapViewActivity.this,
                    textView.getTextSize()));
            if (Helper.getTheme(ShowJobInMapViewActivity.this) == 0) {
                textView.setTextColor(getResources().getColor(
                        android.R.color.white));
            }
        } catch (Exception ex) {

        }

        try {
            Button btnView = (Button) v;
            btnView.setTextSize(UIHelper.getFontSize(ShowJobInMapViewActivity.this,
                    btnView.getTextSize()));
            btnView.setTextColor(getResources().getColor(android.R.color.black));

        } catch (Exception ex) {

        }

    }

    public String getLocalIpAddress() {
        if (IsInternetConnectted()) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("ques", ex.toString());
            }
        } else {

        }
        return null;
    }

    public boolean IsInternetConnectted() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        conMgr = null;
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;

        return true;
    }

    public int IsInternetConnectted_AcceptBtn() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        conMgr = null;
        if (i == null)
            return 0;
        return -1;
    }

    private void sendMessage(final String path, final Object text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (QuestionnaireActivity.QUESTION_LAST.equals(path)) {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                            .getConnectedNodes(mApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        if (text == null) {
                            try {
                                MessageApi.SendMessageResult result = Wearable.MessageApi
                                        .sendMessage(mApiClient, node.getId(),
                                                path, null).await();

                                if (result.getStatus().isSuccess()) {
                                    int i = 0;
                                    i++;
                                } else {
                                    int i = 0;
                                    i++;
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }
                }

                if (STOP_DOWNLOAD.equals(path) || STOP_UPLOAD.equals(path)) {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                            .getConnectedNodes(mApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        if (text == null) {
                            try {
                                MessageApi.SendMessageResult result = Wearable.MessageApi
                                        .sendMessage(mApiClient, node.getId(),
                                                path, null).await();

                            } catch (Exception ex) {
                            }
                        }
                    }
                }

            }
        }).start();
    }

    public void getInProgressJobs(Revamped_Loading_Dialog dialogg,
                                  final ArrayList<String> arrayList) {
        String wheree = null;
        if (arrayList != null) {
            wheree = "";
            for (int i = 0; i < arrayList.size(); i++) {
                wheree = wheree + Constants.DB_TABLE_QUESTIONNAIRE_ORDERID + "=" + "\"" + arrayList.get(i) + "\"";
                if (i < arrayList.size() - 1) {
                    wheree = wheree + " OR ";
                }
            }
        }
        try {
            if (Helper.isMisteroMenu && wheree != null) {
                DBHelper.deleteJoblistRecordsFromIP(wheree);
            } else if (Helper.isMisteroMenu == false && wheree != null) {
                // checker
                DBHelper.updateDeletedJobsStatus(wheree);
            }
        } catch (Exception ex) {
            int i = 0;
            i++;
        }
        class RefundTask extends
                AsyncTask<Void, Integer, ArrayList<QuestionnaireData>> {
            private String updateDate;
            private Revamped_Loading_Dialog dialogg;

            public RefundTask(Revamped_Loading_Dialog dialogg) {
                this.dialogg = dialogg;
            }

            @Override
            protected void onPreExecute() {
                if (dialogg == null) {
                    Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this,
                            getString(R.string.ufinsheddwnloding));
                    dialogg = Revamped_Loading_Dialog.getDialog();
                } else
                    dialogg.changeMessage(getString(R.string.ufinsheddwnloding));
                this.updateDate = null;
                parser = new Parser();
            }

            public boolean checkTime(String serverTime, String appTime) {

                Date dServerTime;
                Date dAppTime = null;
                String serverPattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat inputFormat = new SimpleDateFormat(
                        serverPattern);
                String appPattern = "yyyy-MM-dd kk:mm:ss";
                SimpleDateFormat outputFormat = new SimpleDateFormat(appPattern);

                try {
                    dServerTime = inputFormat.parse(serverTime);
                    try {
                        dAppTime = outputFormat.parse(appTime);
                    } catch (Exception e) {
                        try {
                            dAppTime = inputFormat.parse(appTime);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (dServerTime.compareTo(dAppTime) >= 1)
                        return true;// server time is latest
                    else
                        return false;// app time is latest
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            }

            public String parseDateToddMMyyyy(String time, String inputPattern,
                                              String outputPattern) {
                // inputPattern = "yyyy-MM-dd HH:mm:ss";
                // outputPattern = "dd-MMM-yyyy h:mm a";
                SimpleDateFormat inputFormat = new SimpleDateFormat(
                        inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(
                        outputPattern);

                Date date = null;
                String str = time;

                try {
                    date = inputFormat.parse(time);
                    str = outputFormat.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return str;
            }

            @Override
            protected void onPostExecute(ArrayList<QuestionnaireData> result) {
                // dialogg.onPostExecute();

                // parse it here
                InProgressAnswersData thisOrder = null;
                ArrayList<InProgressAnswersData> datas = new ArrayList<InProgressAnswersData>();
                for (int i = 0; result != null && i < result.size(); i++) {
                    if (thisOrder == null
                            || (result.get(i).getOrderID() != null && !result
                            .get(i).getOrderID()
                            .equals(thisOrder.getFileOrderID()))) {
                        if (thisOrder != null)
                            datas.add(thisOrder);

                        thisOrder = new InProgressAnswersData(result.get(i)
                                .getDataID(), result.get(i).getOrderID(),
                                result.get(i).getSetName(), result.get(i)
                                .getClientName(), result.get(i)
                                .getBranch());
                    }
                    thisOrder.setItem(result.get(i));
                }
                if (thisOrder != null)
                    datas.add(thisOrder);
                if (datas.size() > 0)
                    saveThisInProgressJob(datas, 0);
                else
                    executeShowListTask(dialogg);
            }

            private void saveThisInProgressJob(
                    final ArrayList<InProgressAnswersData> datas, final int i) {
                String orderid = "";
                String startTime = null;
                boolean isCompletedJob = false;
                ArrayList<Order> ordrs = DBHelper.getOrders(DBHelper.whereOrderNotArchived,
                        Constants.DB_TABLE_ORDERS, new String[]{
                                Constants.DB_TABLE_ORDERS_ORDERID,
                                Constants.DB_TABLE_ORDERS_STATUS,
                                Constants.DB_TABLE_ORDERS_START_TIME,},
                        Constants.DB_TABLE_ORDERS_ORDERID);
                for (int j = 0; ordrs != null && j < ordrs.size(); j++) {
                    if (ordrs.get(j).getOrderID() != null
                            && ordrs.get(j).getOrderID()
                            .equals(datas.get(i).getFileOrderID())
                            && ordrs.get(j).getStatusName() != null
                            && ordrs.get(j).getStatusName()
                            .contains("ompleted")) {
                        isCompletedJob = true;
                    }
                }

                try {
                    orderid = datas.get(i).getFileOrderID();
                    startTime = DBHelper.getTimeStamp(datas.get(i)
                            .getFileOrderID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!isCompletedJob
                        && startTime != null
                        && startTime.length() > 0
                        && datas.get(i).getItems() != null
                        && datas.get(i).getItems().size() > 0
                        && datas.get(i).getItems().get(0).getStartTime() != null
                        && datas.get(i).getItems().get(0).getStartTime()
                        .length() > 0) {
                    boolean isServerTimeLatest = checkTime(datas.get(i)
                            .getItems().get(0).getStartTime(), startTime);
                    //isServerTimeLatest=true;
                    if (isServerTimeLatest)// server latest update
                    {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        // Yes button clicked
                                        saveThisItem(datas.get(i));
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat(
                                                    "yyyy-MM-dd  kk:mm:ss",
                                                    Locale.ENGLISH);
                                            DBHelper.updateTimeStamp(sdf
                                                            .format(new Date()),
                                                    datas.get(i).getFileOrderID());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (i + 1 == datas.size()
                                                || datas.size() == 0)
                                            executeShowListTask(dialogg);
                                        else
                                            saveThisInProgressJob(datas, i + 1);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        // No button clicked
                                        if (i + 1 == datas.size()
                                                || datas.size() == 0)
                                            executeShowListTask(dialogg);
                                        else
                                            saveThisInProgressJob(datas, i + 1);
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ShowJobInMapViewActivity.this);
                        builder.setCancelable(false);
                        builder.setMessage(
                                        "WARNING: "
                                                + getResources().getString(
                                                R.string.replace_inprog_job)
                                                + datas.get(i).getClientName()
                                                + " ,"
                                                + datas.get(i).getSetName()
                                                + " ,"
                                                + datas.get(i).getBranch()
                                                + " (OrderId:"
                                                + datas.get(i).getFileOrderID()
                                                + ") "
                                                + getResources().getString(
                                                R.string.remainin_msg))
                                .setPositiveButton(
                                        getString(R.string.s_item_column_0_line_344_file_210),
                                        dialogClickListener)
                                .setNegativeButton(
                                        getString(R.string.s_item_column_0_line_346_file_210),
                                        dialogClickListener)
                                .setCancelable(false).show();
                    } else // app time latest
                    {
                        Toast.makeText(
                                ShowJobInMapViewActivity.this,
                                getResources().getString(
                                        R.string.alreadyExistingJob),
                                Toast.LENGTH_LONG).show();
                        if (i + 1 == datas.size() || datas.size() == 0)
                            executeShowListTask(dialogg);
                        else
                            saveThisInProgressJob(datas, i + 1);
                    }
                } else if (!isCompletedJob) // no job in prohgress save from
                // server
                {
                    // Toast.makeText(
                    // JobListActivity.this,"fresh job",
                    // Toast.LENGTH_SHORT).show();
                    DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
                            new String[]{Constants.DB_TABLE_ORDERS_ORDERID,
                                    Constants.DB_TABLE_ORDERS_STATUS,
                                    Constants.DB_TABLE_ORDERS_START_TIME,},
                            orderid, "In progress", datas.get(i).getItems()
                                    .get(0).getStartTime(), null);

                    saveThisFreshItem(datas.get(i));
                    if (i + 1 == datas.size() || datas.size() == 0)
                        executeShowListTask(dialogg);
                    else
                        saveThisInProgressJob(datas, i + 1);
                } else {
                    if (i + 1 == datas.size() || datas.size() == 0)
                        executeShowListTask(dialogg);
                    else
                        saveThisInProgressJob(datas, i + 1);
                }

            }

            private void saveThisFreshItem(InProgressAnswersData answers) {

                ArrayList<QuestionnaireData> results = answers.getItems();

                for (int i = 0; i < results.size(); i++) {
                    QuestionnaireData result = results.get(i);
                    String mitype = result.getMiType();
                    String time = result.getMi();

                    if (mitype != null && mitype.equals("3") && time != null
                            && !time.equals("")) // 2017-10-19 to 19-10-2017
                    {
                        time = parseDateToddMMyyyy(time, "yyyy-MM-dd",
                                "dd-MM-yyyy");
                    }
                    if (mitype != null && mitype.equals("4") && time != null
                            && !time.equals(""))// HH:MM:SS
                    {
                        // no change required
                    }
                    if (mitype != null && mitype.equals("6") && time != null
                            && !time.equals(""))// HH:MM 16:35:00 to 16:35
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "HH:mm");
                    }
                    if (mitype != null && mitype.equals("7") && time != null
                            && !time.equals(""))// MM:SS 00:35:54 to 35:54
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "mm:ss");
                    }
                    if (mitype != null && mitype.equals("8") && time != null
                            && !time.equals(""))// SS 00:54 to 54
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "ss");
                    }

                    result.setMi(time);
                    DBHelper.updateThisQuestionnaire(
                            Constants.DB_TABLE_QUESTIONNAIRE,
                            new String[]{
                                    Constants.DB_TABLE_QUESTIONNAIRE_DATAID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QTEXT,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QVALUE,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QTL,
                                    Constants.DB_TABLE_QUESTIONNAIRE_OT,
                                    Constants.DB_TABLE_ANSWERS_BRANCHID,
                                    Constants.DB_TABLE_ANSWERS_WORKERID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_ORDERID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_FT,
                                    Constants.DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME,
                                    Constants.DB_TABLE_QUESTIONNAIRE_LoopInfo},
                            result, null, result.getSetID());

                    try {
                        DBHelper.convertToBytes(parser.attached_files,
                                "inprogress_data");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            private void saveThisItem(InProgressAnswersData answers) {

                String where = Constants.DB_TABLE_QUESTIONNAIRE_ORDERID + "="
                        + "\"" + answers.getFileOrderID() + "\"";
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.DB_TABLE_QUESTIONNAIRE, where,
                        null);
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.DB_TABLE_ANSWERS, where, null);
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE, where, null);
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.DB_TABLE_POS, where, null);
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.DB_TABLE_ORDERS, where, null);
                DBAdapter.openDataBase();
                DBAdapter.db.delete(Constants.DB_TABLE_SUBMITSURVEY, where,
                        null);

                ArrayList<QuestionnaireData> results = answers.getItems();

                for (int i = 0; i < results.size(); i++) {
                    QuestionnaireData result = results.get(i);
                    String mitype = result.getMiType();
                    String time = result.getMi();

                    if (mitype != null && mitype.equals("3") && time != null
                            && !time.equals("")) // 2017-10-19 to 19-10-2017
                    {
                        time = parseDateToddMMyyyy(time, "yyyy-MM-dd",
                                "dd-MM-yyyy");
                    }
                    if (mitype != null && mitype.equals("4") && time != null
                            && !time.equals(""))// HH:MM:SS
                    {
                        // no change required
                    }
                    if (mitype != null && mitype.equals("6") && time != null
                            && !time.equals(""))// HH:MM 16:35:00 to 16:35
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "HH:mm");
                    }
                    if (mitype != null && mitype.equals("7") && time != null
                            && !time.equals(""))// MM:SS 00:35:54 to 35:54
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "mm:ss");
                    }
                    if (mitype != null && mitype.equals("8") && time != null
                            && !time.equals(""))// SS 00:54 to 54
                    {
                        time = parseDateToddMMyyyy(time, "HH:mm:ss", "ss");
                    }

                    result.setMi(time);
                    DBHelper.updateThisQuestionnaire(
                            Constants.DB_TABLE_QUESTIONNAIRE,
                            new String[]{
                                    Constants.DB_TABLE_QUESTIONNAIRE_DATAID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QTEXT,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QVALUE,
                                    Constants.DB_TABLE_QUESTIONNAIRE_QTL,
                                    Constants.DB_TABLE_QUESTIONNAIRE_OT,
                                    Constants.DB_TABLE_ANSWERS_BRANCHID,
                                    Constants.DB_TABLE_ANSWERS_WORKERID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_ORDERID,
                                    Constants.DB_TABLE_QUESTIONNAIRE_FT,
                                    Constants.DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME,
                                    Constants.DB_TABLE_QUESTIONNAIRE_LoopInfo},
                            result, null, result.getSetID());

                    try {
                        DBHelper.convertToBytes(parser.attached_files,
                                "inprogress_data");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            protected ArrayList<QuestionnaireData> doInBackground(
                    Void... params) {
                checkConnectionPost();
                String data = InProgressPost();
                if (data.contains("<script>")) {
                    doLogin();
                    data = InProgressPost();
                }
                if (data != null && data.contains("<crits>") && data.contains("</crits>")) {
                    int start = data.indexOf("<crits>");
                    int end = data.indexOf("</crits>") + 8;
                    data = data.substring(start, end);
                    data = data.replace("<1", "<d1");
                    data = data.replace("</1", "</d1");
                    data = data.replace("<2", "<d2");
                    data = data.replace("</2", "</d2");
                    data = data.replace("<3", "<d3");
                    data = data.replace("</3", "</d3");
                    data = data.replace("<4", "<d4");
                    data = data.replace("</4", "</d4");
                    data = data.replace("<5", "<d5");
                    data = data.replace("</5", "</d5");
                    data = data.replace("<6", "<d6");
                    data = data.replace("</6", "</d6");
                    data = data.replace("<7", "<d7");
                    data = data.replace("</7", "</d7");
                    data = data.replace("<8", "<d8");
                    data = data.replace("</8", "</d8");
                    data = data.replace("<9", "<d9");
                    data = data.replace("</9", "</d9");
                    data = data.replace("<>", "<empty_tag>");
                    data = data.replace("</>", "</empty_tag>");

                    if (data != null) {
                        Parser thisParser = new Parser(
                                Revamped_Loading_Dialog.getDialog());
                        thisParser.parseXMLValues(data,
                                Constants.QUES_RESP_FIELD_PARAM, true);
                        if (thisParser.inProgressSets != null
                                && thisParser.inProgressSets.size() > 0) {
                            Iterator it = thisParser.inProgressSets.entrySet()
                                    .iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry) it.next();
                                Set set = thisParser.inProgressSets.get(pair
                                        .getKey());
                                if (set.getWasSentBack() != null
                                        && set.getWasSentBack().equals("1")) {
                                    DBHelper.updateOrderOnServerStatus(pair
                                            .getKey().toString());
                                }
                                try {
                                    DBHelper.convertToBytes(set, set.getSetID(),
                                            pair.getKey().toString(), true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                it.remove(); // avoids a
                                // ConcurrentModificationException
                            }
                        }
                        // separate set code

                        parser.parseXMLValues(data,
                                Constants.INPROGRESS_RESP_FIELD_PARAM);
                    }
                }
                if (parser.inProgressData != null)
                    return parser.inProgressData;
                else return new ArrayList<QuestionnaireData>();
            }

            private String InProgressPost() {
                List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
                String app_ver = "";
                try {
                    app_ver = ShowJobInMapViewActivity.this.getPackageManager()
                            .getPackageInfo(
                                    ShowJobInMapViewActivity.this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {

                }
                return Connector.postForm(
                        Constants.getInProgressJobsURL(app_ver), extraDataList);
            }
        }
        RefundTask refundtaskobj = new RefundTask(dialogg);
        refundtaskobj.execute();
    }


    public boolean showLogin(String result) {
        if (result.contains("error="))
            return false;
        String result1 = new Parser().getValue(result,
                Constants.LOGIN_RESP_FIELD_PARAM);
        if (result1.equals("0")) {

            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, false);
            prefsEditor.commit();
            Intent intent = new Intent(this.getApplicationContext(),
                    NewLoginActivity.class);
            // comunicator.JobList = null;
            startActivity(intent);
            finish();
            return true;
        }
        SharedPreferences.Editor prefsEditor = myPrefs.edit();

        prefsEditor.putString(Constants.POST_FIELD_LOGIN_RESPONSE,
                result);
        prefsEditor.commit();

        return false;
    }

    public String doLogin() {
        SharedPreferences myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        return loginPost(
                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
                Constants.POST_VALUE_LOGIN_DO_LOGIN, true);
    }

    private String loginPost(final String username, final String password,
                             String dologin, boolean detail) {
        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_USERNAME, username));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_PASSWORD, password));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_DO_LOGIN, dologin));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_NO_REDIR,
                Constants.POST_VALUE_LOGIN_NO_REDIR));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_IS_APP,
                Constants.POST_VALUE_LOGIN_IS_APP));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_LOGIN_SHOPPER_DETAIL,
                Constants.POST_VALUE_LOGIN_IS_APP));
        Connector.setContext(ShowJobInMapViewActivity.this);
        return Connector.postForm(Constants.getLoginURL(), extraDataList);
    }


    private String QuestionnaireListPost() {

        String allSetData = "";
        String app_ver = "";
        try {
            app_ver = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        if (Sets.getSetIds() != null && Sets.getSetIds().size() > 0) {
            Sets.getSets().clear();
            ArrayList<AltLanguage> allLanguages = DBHelper.getLanguages(true);
            while (Sets.getSetIds().size() > 0) {
                String currentSet = Sets.getSetIds().get(0);
                String commaSeperatedOrderIds = Orders.getListOfOldORderIds(currentSet);
                List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
                String thisSet = Connector.postForm(Constants
                                .getQestionnaireOneByOneURL(app_ver, currentSet, commaSeperatedOrderIds),
                        extraDataList);
                thisSet = removeGarbageFromTop(thisSet);
                if (thisSet != null && thisSet.contains("Blocks")) {
                    thisSet = thisSet.replace("<1", "<d1");
                    thisSet = thisSet.replace("</1", "</d1");
                    thisSet = thisSet.replace("<2", "<d2");
                    thisSet = thisSet.replace("</2", "</d2");
                    thisSet = thisSet.replace("<3", "<d3");
                    thisSet = thisSet.replace("</3", "</d3");
                    thisSet = thisSet.replace("<4", "<d4");
                    thisSet = thisSet.replace("</4", "</d4");
                    thisSet = thisSet.replace("<5", "<d5");
                    thisSet = thisSet.replace("</5", "</d5");
                    thisSet = thisSet.replace("<6", "<d6");
                    thisSet = thisSet.replace("</6", "</d6");
                    thisSet = thisSet.replace("<7", "<d7");
                    thisSet = thisSet.replace("</7", "</d7");
                    thisSet = thisSet.replace("<8", "<d8");
                    thisSet = thisSet.replace("</8", "</d8");
                    thisSet = thisSet.replace("<9", "<d9");
                    thisSet = thisSet.replace("</9", "</d9");
                }
                saveThisSet(thisSet, currentSet, "");
                if (thisSet != null && getSetActualLength(thisSet) > 0) {

                    Parser thisParser = new Parser(
                            Revamped_Loading_Dialog.getDialog());
                    if (thisSet != null && thisSet.contains(""))
                        thisParser.parseXMLValues(thisSet,
                                Constants.QUES_RESP_FIELD_PARAM, null, null,
                                null);
                    Set thissSet = thisParser.set;
                    if (thissSet != null
                            && thissSet.getAllowCheckerToSetLang() != null
                            && thissSet.getAllowCheckerToSetLang().equals("1")) {
                        for (int i = 0; i < allLanguages.size(); i++) {
                            thisSet = Connector.postForm(Constants
                                            .getQestionnaireOneByOneURL(app_ver,
                                                    currentSet, allLanguages.get(i)
                                                            .getAltLangID(),
                                                    Orders.getListOfOldORderIds(currentSet)),
                                    extraDataList);
                            thisSet = removeGarbageFromTop(thisSet);
                            if (thisSet != null && thisSet.contains("Blocks")) {
                                thisSet = thisSet.replace("<1", "<d1");
                                thisSet = thisSet.replace("</1", "</d1");
                                thisSet = thisSet.replace("<2", "<d2");
                                thisSet = thisSet.replace("</2", "</d2");
                                thisSet = thisSet.replace("<3", "<d3");
                                thisSet = thisSet.replace("</3", "</d3");
                                thisSet = thisSet.replace("<4", "<d4");
                                thisSet = thisSet.replace("</4", "</d4");
                                thisSet = thisSet.replace("<5", "<d5");
                                thisSet = thisSet.replace("</5", "</d5");
                                thisSet = thisSet.replace("<6", "<d6");
                                thisSet = thisSet.replace("</6", "</d6");
                                thisSet = thisSet.replace("<7", "<d7");
                                thisSet = thisSet.replace("</7", "</d7");
                                thisSet = thisSet.replace("<8", "<d8");
                                thisSet = thisSet.replace("</8", "</d8");
                                thisSet = thisSet.replace("<9", "<d9");
                                thisSet = thisSet.replace("</9", "</d9");
                            }

                            saveThisSet(thisSet, currentSet, "-"
                                    + allLanguages.get(i).getAltLangID());

                            thisParser = new Parser(
                                    Revamped_Loading_Dialog.getDialog());
                            thisParser.parseXMLValues(thisSet,
                                    Constants.QUES_RESP_FIELD_PARAM, thissSet,
                                    allLanguages.get(i).getAltLangID(),
                                    allLanguages.get(i).getAltLangName());
                        }

                    }
                } else {
                    Sets.getSetIds().remove(0);
                }
            }
            return allSetData + "\r\n</sets>";
        } else {
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            String thisSet = Connector.postForm(
                    Constants.getQestionnaireURL(app_ver), extraDataList);
            if (thisSet != null) {
                new Parser(Revamped_Loading_Dialog.getDialog()).parseXMLValues(
                        thisSet, Constants.QUES_RESP_FIELD_PARAM);
            }
            return thisSet;
        }
    }

    private void setHttps() {
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY, Helper
                .getSystemURL().replace("http://", "https://"));
        prefsEditor.commit();

        Helper.setSystemURL(myPrefs.getString(
                Constants.SETTINGS_SYSTEM_URL_KEY, ""));
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));

    }

    private void executeShowListTask(Revamped_Loading_Dialog dialog) {
        try {
            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(
                    Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, true);
            prefsEditor.commit();
            showListTaskHandler = new ShowDBListTask(dialog);
            showListTaskHandler.execute();

        } catch (Exception ex) {
        }
    }

    private String removeGarbageFromTop(String thisSet) {
        return thisSet;
    }

    private void saveThisSet(String thisSet, String id, String langid) {
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/mnt/sdcard/CheckerFiles";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path + "/file_" + id + langid + ".xml");
            file.createNewFile();
            FileWriter writer = new FileWriter(path + "/file_" + id + langid
                    + ".xml");

            writer.write(thisSet);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getSetActualLength(String thisSet) {
        // <?xml version="1.0"?>
        // <sets>
        //
        // </sets>
        thisSet = thisSet.replace("<?xml version=\"1.0\"?>", "");
        thisSet = thisSet.replace("<sets>", "");

        thisSet = thisSet.replace("</sets>", "");
        thisSet = thisSet.replace("\r", "");
        thisSet = thisSet.replace("\n", "");
        thisSet = thisSet.replace(" ", "");
        return thisSet.length();
    }

    private void setOrderList() {

        ArrayList<Order> ordrs = DBHelper.getOrders(DBHelper.whereOrderNotArchived,
                Constants.DB_TABLE_ORDERS, new String[]{
                        Constants.DB_TABLE_ORDERS_ORDERID,
                        Constants.DB_TABLE_ORDERS_STATUS,
                        Constants.DB_TABLE_ORDERS_START_TIME,},
                Constants.DB_TABLE_ORDERS_ORDERID);

        Orders.replaceistOrders(ordrs);
        Order[] ordersArr = null;
        if (ordrs != null) {
            ordersArr = new Order[ordrs.size()];
            ordrs.toArray(ordersArr);
        }
        Order order, innerorder;
        if (joborders == null)
            joborders = new ArrayList<orderListItem>();
        // joborders.clear();
        ArrayList<Order> delete = new ArrayList<Order>();
        ArrayList<orderListItem> temporder1 = new ArrayList<orderListItem>();
        ArrayList<Order> temporder = new ArrayList<Order>();
        temporder = (ArrayList<Order>) Orders.getOrders().clone();
        int size = temporder.size();
        for (int index = 0; index < size; index++) {
            order = temporder.get(index);
            String status = getStatusByOrderID(order.getOrderID(), ordersArr);
            if (!status.equals("") && !status.toLowerCase().equals("archived"))
                temporder.get(index).setStatusName(status);
        }

        for (int index = 0; index < size; index++) {
            order = temporder.get(index);
            order.setJobCount(0);

            for (int innerindex = 0; innerindex < size; innerindex++) {
                innerorder = temporder.get(innerindex);
                try {
                    if ((Constants.getDateFilter() || order.getBranchLink()
                            .equals(innerorder.getBranchLink()))
                            && (Constants.getDateFilter() || order.getMassID()
                            .equals(innerorder.getMassID()))
                            && (order.getDate().equals(innerorder.getDate()))
                            && (Constants.getDateFilter() || order
                            .getBranchName().equals(
                                    innerorder.getBranchName()))
                            && (Constants.getDateFilter() || order.getSetLink()
                            .equals(innerorder.getSetLink()))
                            && (order.getStatusName().equals(innerorder
                            .getStatusName()))) {
                        delete.add(innerorder);
                        order.setCount();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            temporder1.add(new orderListItem(order, delete));
            for (int deleteindex = 1; deleteindex < delete.size(); deleteindex++) {
                temporder.remove(delete.get(deleteindex));
            }
            size = temporder.size();
            delete = new ArrayList<Order>();
        }
        joborders = temporder1;
        delete = null;
    }

    private String getStatusByOrderID(String orderid, Order[] ordersArr) {
        if (ordersArr == null)
            return "";
        for (int Ocount = 0; Ocount < ordersArr.length; Ocount++) {
            if (orderid.equals(ordersArr[Ocount].getOrderID()))
                return ordersArr[Ocount].getStatusName();
        }
        return "";
    }

    public class ShowDBListTask extends AsyncTask<Void, Integer, String> {

        private String certorderid;

        public ShowDBListTask(Revamped_Loading_Dialog dialog2,
                              String certorderid) {
            this.certorderid = certorderid;
        }

        public ShowDBListTask(Revamped_Loading_Dialog dialog2) {

        }

        Locale current = null;

        @Override
        protected void onPreExecute() {
            {
                current = getResources().getConfiguration().locale;
                try {
                    Revamped_Loading_Dialog.hide_dialog();

                } catch (Exception ex) {
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (joborders != null && joborders.size() >= 1) {
                Orders.setListOrders(jobordersss);
                Orders.setBranchProps(branchProps);
                Surveys.setSets(DBHelper.getSurveyyRecords());
                setOrderList();
            }
            Revamped_Loading_Dialog.hide_dialog();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<Survey> surveys = Surveys.getSets();
            if (this.certorderid != null) {
            } else {
                saveOfflineData();
                DBHelper.AddSurveys(surveys, true);
            }
            jobordersss = DBHelper
                    .getOrders(
                            null,
                            Constants.DB_TABLE_JOBLIST,
                            new String[]{
                                    Constants.DB_TABLE_JOBLIST_ORDERID,
                                    Constants.DB_TABLE_JOBLIST_DATE,
                                    Constants.DB_TABLE_JOBLIST_SN,
                                    Constants.DB_TABLE_JOBLIST_DESC,
                                    Constants.DB_TABLE_JOBLIST_SETNAME,
                                    Constants.DB_TABLE_JOBLIST_SETLINK,
                                    Constants.DB_TABLE_JOBLIST_CN,
                                    Constants.DB_TABLE_JOBLIST_BFN,
                                    Constants.DB_TABLE_JOBLIST_BN,
                                    Constants.DB_TABLE_JOBLIST_CITYNAME,
                                    Constants.DB_TABLE_JOBLIST_ADDRESS,
                                    Constants.DB_TABLE_JOBLIST_BP,
                                    Constants.DB_TABLE_JOBLIST_OH,
                                    Constants.DB_TABLE_JOBLIST_TS,
                                    Constants.DB_TABLE_JOBLIST_TE,
                                    Constants.DB_TABLE_JOBLIST_SETID,
                                    Constants.DB_TABLE_JOBLIST_BL,
                                    Constants.DB_TABLE_JOBLIST_BLNG,
                                    Constants.DB_TABLE_JOBLIST_FN,
                                    Constants.DB_TABLE_JOBLIST_JC,
                                    Constants.DB_TABLE_JOBLIST_JI,
                                    Constants.DB_TABLE_JOBLIST_BLINK,
                                    Constants.DB_TABLE_JOBLIST_MID,
                                    Constants.DB_TABLE_CHECKER_CODE,
                                    Constants.DB_TABLE_CHECKER_LINK,
                                    Constants.DB_TABLE_BRANCH_CODE,
                                    Constants.DB_TABLE_SETCODE,
                                    Constants.DB_TABLE_PURCHASE_DESCRIPTION,
                                    Constants.DB_TABLE_PURCHASE,
                                    Constants.DB_TABLE_JOBLIST_BRIEFING,
                                    Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
                                    Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
                                    Constants.DB_TABLE_JOBLIST_sTransportationPayment,
                                    Constants.DB_TABLE_JOBLIST_sCriticismPayment,
                                    Constants.DB_TABLE_JOBLIST_sBonusPayment,
                                    Constants.DB_TABLE_JOBLIST_AllowShopperToReject,
                                    Constants.DB_TABLE_JOBLIST_sinprogressonserver,
                                    Constants.DB_TABLE_JOBLIST_sProjectName,
                                    Constants.DB_TABLE_JOBLIST_sRegionName,
                                    Constants.DB_TABLE_JOBLIST_sdeletedjob,
                                    Constants.DB_TABLE_JOBLIST_sProjectID,},
                            Constants.DB_TABLE_JOBLIST_JI);

            branchProps = DBHelper.getBranchPropds(
                    Constants.DB_TABLE_BRANCH_PROPS, new String[]{
                            Constants.DB_TABLE_BRANCH_PROPS_ValueID,
                            Constants.DB_TABLE_BRANCH_PROPS_PropID,
                            Constants.DB_TABLE_BRANCH_PROPS_PropertyName,
                            Constants.DB_TABLE_BRANCH_PROPS_Content,
                            Constants.DB_TABLE_BRANCH_PROPS_BranchID,},
                    Constants.DB_TABLE_BRANCH_PROPS_PropID);

            if (jobordersss != null) {
                try {
                    pngItems = validateIcons(DBHelper.getIcons(), jobordersss);
                } catch (Exception ex) {
                    pngItems = new ArrayList<pngItem>();
                }
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                isBranchPropErr = myPrefs.getBoolean(
                        Constants.ALREADY_BRANCHPROPERR, false);

                joborders = new ArrayList<orderListItem>();
                for (int i = 0; i < jobordersss.size(); i++) {
                    joborders.add(new orderListItem(jobordersss.get(i), null));
                }

            }

            return "";
        }

    }

    private ArrayList<pngItem> validateIcons(ArrayList<pngItem> icons,
                                             ArrayList<Order> jobordersss) {
        if (icons == null)
            return new ArrayList<pngItem>();

        ArrayList<pngItem> tmp_icons = new ArrayList<pngItem>();
        for (int i = 0; i < icons.size(); i++) {
            pngItem icon = icons.get(i);

            Helper helper = new Helper();

            if (icon.MediaFile != null && helper.validateIcon(icon.MediaFile)) {
                // icons.remove(i);
            } else {
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                boolean isDownloadedYet = myPrefs.getBoolean(
                        Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, false);
                Bitmap bmp = helper.readFile(icon.MediaFile, isDownloadedYet);
                if (bmp != null) {

                } else {
                    Drawable d = new BitmapDrawable(getResources(), bmp);
                    tmp_icons.add(icons.get(i));
                }
            }
        }
        return tmp_icons;
    }

    private void saveOfflineData() {
        String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
                + "\"Assigned\"";
        DBHelper.deleteJoblistRecords(where);

        DBHelper.deleteProps();
        DBHelper.saveProps(Orders.getBranchProps());
        branchProps = Orders.getBranchProps();
        DBHelper.AddOrders(Orders.getOrders());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendMessage(START_ACTIVITY, "hiii");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final String START_JOB = "/start_job";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (messageEvent.getPath().equalsIgnoreCase(START_JOB)) {

                    }

                } catch (Exception ex) {
                    Toast.makeText(ShowJobInMapViewActivity.this,
                            ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //TODO Accept
    void startLocationChecker(orderListItem thiItem) {
        Context context = ShowJobInMapViewActivity.this;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
                MockGPSALERT();
                return;
            }
            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

            for (int i = 0; i < joborders.size(); i++) {
                if (joborders.get(i).orderItem.getOrderID()
                        .equals(thiItem.orderItem.getOrderID())) {
                    joborders.get(i).orderItem
                            .setStatusName("Scheduled");
                }
            }

            BeginReview(false, thiItem);

        }

        else {
            // OPen GPS settings

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                            getResources().getString(R.string.questionnaire_gps_alert))
                    .setTitle(getResources().getString(R.string._alert_title))
                    .setCancelable(false)
                    .setPositiveButton(
                            getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    startActivityForResult(
                                            new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                            JOB_GPS_CODE);

                                    dialog.dismiss();

                                }
                            })
                    .setNegativeButton(
                            getResources()
                                    .getString(
                                            R.string.questionnaire_exit_delete_alert_no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // startLocationChecker();
                                    dialog.dismiss();
                                    myPrefs = getSharedPreferences("pref",
                                            MODE_PRIVATE);

                                    String userName = myPrefs
                                            .getString(
                                                    Constants.POST_FIELD_LOGIN_USERNAME,
                                                    "");
                                    BeginReview(false, thiItem);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    void MockGPSALERT() {
        // OPen GPS settings
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                        getResources().getString(R.string.questionnaire_mock_gps_alert))
                .setTitle(getResources().getString(R.string._alert_title))
                .setCancelable(false)
                .setPositiveButton(
                        getResources().getString(
                                R.string.questionnaire_exit_delete_alert_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                startActivityForResult(
                                        new Intent(
                                                android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                                        JOB_GPS_CODE);

                                dialog.dismiss();

                            }
                        })
                .setNegativeButton(
                        getResources().getString(
                                R.string.questionnaire_exit_delete_alert_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                BeginReview(false, cardItemDetails);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void BeginReview(boolean isFromWatch, orderListItem thiItem) {
        if (survey != null) {
            int i = 0;
            i++;
        }
        if (thiItem.orderItem.getOrderID().contains("-")) {
            Intent intent = new Intent(this.getApplicationContext(),
                    QuestionnaireActivity.class);
            intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
            intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
            if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert") || isFromWatch) {
                startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                finish();
            } else
                startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
        } else {
            Intent intent = new Intent(this.getApplicationContext(),
                    QuestionnaireActivity.class);
            if (thiItem.orderItem == null)
                setOrder(thiItem);
            intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
                    thiItem.orderItem.getOrderID());
            intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
            if (isFromWatch || thiItem.orderItem.getStatusName().equals("Completed") || thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().equals("archived")) {
                startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                finish();
            } else
                startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    String getDate(String date) {
        Date d = null;
        try {
            d = sdf.parse(date);
            DateFormat dateFormat = android.text.format.DateFormat
                    .getDateFormat(this);
            String str = dateFormat.format(d);
            return str;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    //TODO
    private class LongOperation extends AsyncTask<String, Void, ArrayList<orderListItem>> {

        public Context con;
        ArrayList<BranchProperties> branchProps;
        ArrayList<Order> jobordersss = null;

        @Override
        protected ArrayList<orderListItem> doInBackground(String... params) {
            //String ret = ShowDBJobss("task");

            try {
                Log.e("LongOperation", "true");
                jobordersss = DBHelper
                        .getOrders(
                                DBHelper.whereJobListNotArchived,
                                Constants.DB_TABLE_JOBLIST,
                                new String[]{
                                        Constants.DB_TABLE_JOBLIST_ORDERID,
                                        Constants.DB_TABLE_JOBLIST_DATE,
                                        Constants.DB_TABLE_JOBLIST_SN,
                                        Constants.DB_TABLE_JOBLIST_DESC,
                                        Constants.DB_TABLE_JOBLIST_SETNAME,
                                        Constants.DB_TABLE_JOBLIST_SETLINK,
                                        Constants.DB_TABLE_JOBLIST_CN,
                                        Constants.DB_TABLE_JOBLIST_BFN,
                                        Constants.DB_TABLE_JOBLIST_BN,
                                        Constants.DB_TABLE_JOBLIST_CITYNAME,
                                        Constants.DB_TABLE_JOBLIST_ADDRESS,
                                        Constants.DB_TABLE_JOBLIST_BP,
                                        Constants.DB_TABLE_JOBLIST_OH,
                                        Constants.DB_TABLE_JOBLIST_TS,
                                        Constants.DB_TABLE_JOBLIST_TE,
                                        Constants.DB_TABLE_JOBLIST_SETID,
                                        Constants.DB_TABLE_JOBLIST_BL,
                                        Constants.DB_TABLE_JOBLIST_BLNG,
                                        Constants.DB_TABLE_JOBLIST_FN,
                                        Constants.DB_TABLE_JOBLIST_JC,
                                        Constants.DB_TABLE_JOBLIST_JI,
                                        Constants.DB_TABLE_JOBLIST_BLINK,
                                        Constants.DB_TABLE_JOBLIST_MID,
                                        Constants.DB_TABLE_CHECKER_CODE,
                                        Constants.DB_TABLE_CHECKER_LINK,
                                        Constants.DB_TABLE_BRANCH_CODE,
                                        Constants.DB_TABLE_SETCODE,
                                        Constants.DB_TABLE_PURCHASE_DESCRIPTION,
                                        Constants.DB_TABLE_PURCHASE,
                                        Constants.DB_TABLE_JOBLIST_BRIEFING,
                                        Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
                                        Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
                                        Constants.DB_TABLE_JOBLIST_sTransportationPayment,
                                        Constants.DB_TABLE_JOBLIST_sCriticismPayment,
                                        Constants.DB_TABLE_JOBLIST_sBonusPayment,
                                        Constants.DB_TABLE_JOBLIST_AllowShopperToReject,
                                        Constants.DB_TABLE_JOBLIST_sinprogressonserver,
                                        Constants.DB_TABLE_JOBLIST_sProjectName,
                                        Constants.DB_TABLE_JOBLIST_sRegionName,
                                        Constants.DB_TABLE_JOBLIST_sdeletedjob,
                                        Constants.DB_TABLE_JOBLIST_sProjectID,},
                                Constants.DB_TABLE_JOBLIST_JI);

                branchProps = DBHelper.getBranchPropds(
                        Constants.DB_TABLE_BRANCH_PROPS, new String[]{
                                Constants.DB_TABLE_BRANCH_PROPS_ValueID,
                                Constants.DB_TABLE_BRANCH_PROPS_PropID,
                                Constants.DB_TABLE_BRANCH_PROPS_PropertyName,
                                Constants.DB_TABLE_BRANCH_PROPS_Content,
                                Constants.DB_TABLE_BRANCH_PROPS_BranchID,},
                        Constants.DB_TABLE_BRANCH_PROPS_PropID);

                if (jobordersss != null) {
                    try {
                        pngItems = validateIcons(DBHelper.getIcons(), jobordersss);
                    } catch (Exception ex) {
                        pngItems = new ArrayList<pngItem>();
                    }
                    myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                    isBranchPropErr = myPrefs.getBoolean(
                            Constants.ALREADY_BRANCHPROPERR, false);


                }
            } catch (Exception ex) {
                int i = 0;
                i++;
            }

            Log.e("jobordersss_map", String.valueOf(jobordersss.size()));

//            ArrayList<orderListItem> joborders = null;

            try {

                if (jobordersss != null) {
                    if (pngItems != null && pngItems.size() > 0 || isBranchPropErr == true) {
//                        btnErr.setVisibility(RelativeLayout.VISIBLE);
                    } else {
//                        btnErr.setVisibility(RelativeLayout.GONE);
                    }
                    joborders = new ArrayList<orderListItem>();
                    for (int i = 0; i < jobordersss.size(); i++) {

                        joborders.add(new orderListItem(jobordersss.get(i), null));
                    }
                    Log.e("joborders_map_", String.valueOf(joborders.size()));
                }


            } catch (Exception ex) {
            }

            if (joborders != null && joborders.size() >= 1) {
                Orders.setListOrders(jobordersss);
                Orders.setBranchProps(branchProps);
                Surveys.setSets(DBHelper.getSurveyyRecords());
                ArrayList<Order> ordrs = DBHelper.getOrders(DBHelper.whereOrderNotArchived,
                        Constants.DB_TABLE_ORDERS, new String[]{
                                Constants.DB_TABLE_ORDERS_ORDERID,
                                Constants.DB_TABLE_ORDERS_STATUS,
                                Constants.DB_TABLE_ORDERS_START_TIME,},
                        Constants.DB_TABLE_ORDERS_ORDERID);

                Orders.replaceistOrders(ordrs);
                Order[] ordersArr = null;
                if (ordrs != null) {
                    ordersArr = new Order[ordrs.size()];
                    ordrs.toArray(ordersArr);
                }
                Order order, innerorder;
                if (joborders == null)
                    joborders = new ArrayList<orderListItem>();
                // joborders.clear();
                ArrayList<Order> delete = new ArrayList<Order>();
                ArrayList<orderListItem> temporder1 = new ArrayList<orderListItem>();
                ArrayList<Order> temporder = new ArrayList<Order>();
                temporder = (ArrayList<Order>) Orders.getOrders().clone();
                int size = temporder.size();
                for (int index = 0; index < size; index++) {
                    order = temporder.get(index);
                    String status = getStatusByOrderID(order.getOrderID(), ordersArr);
                    if (!status.equals("") && !status.toLowerCase().equals("archived"))
                        temporder.get(index).setStatusName(status);
                }

                for (int index = 0; index < size; index++) {
                    order = temporder.get(index);
                    order.setJobCount(0);

                    for (int innerindex = 0; innerindex < size; innerindex++) {
                        innerorder = temporder.get(innerindex);
                        try {
                            if ((Constants.getDateFilter() || order.getBranchLink()
                                    .equals(innerorder.getBranchLink()))
                                    && (Constants.getDateFilter() || order.getMassID()
                                    .equals(innerorder.getMassID()))
                                    && (order.getDate().equals(innerorder.getDate()))
                                    && (Constants.getDateFilter() || order
                                    .getBranchName().equals(
                                            innerorder.getBranchName()))
                                    && (Constants.getDateFilter() || order.getSetLink()
                                    .equals(innerorder.getSetLink()))
                                    && (order.getStatusName().equals(innerorder
                                    .getStatusName()))) {
                                delete.add(innerorder);
                                order.setCount();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // order.setIndex(index);
                    temporder1.add(new orderListItem(order, delete));
                    for (int deleteindex = 1; deleteindex < delete.size(); deleteindex++) {
                        temporder.remove(delete.get(deleteindex));
                    }
                    size = temporder.size();
                    // delete.clear();
                    delete = new ArrayList<Order>();
                }
                joborders = temporder1;
                // delete.clear();
                delete = null;

            }
            return joborders;
        }

        @Override
        protected void onPostExecute(ArrayList<orderListItem> joborders) {
            Log.e("LongOperation_onPost", "true");
            SplashScreen.addLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "POST_STARTED", "LONG_OPERATION"));
            try {
                if (joborders != null && joborders.size() >= 1) {
                    joborders = joborders;
//                    showDbjobsPostPart();
                    setMapView(joborders);
//                    if (mFilter.equals("completed")) {
                    ShowOrphanFiles();
//                    }
                }
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "POST_END", "LONG_OPERATION"));
            } catch (Exception ex) {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        ex.getMessage(), "LONG_OPERATION"));
            }
            Revamped_Loading_Dialog.hide_dialog();
        }

        @Override
        protected void onPreExecute() {
            Log.e("LongOperation_onPre", "true");
            try {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "PRE_STARTED", "LONG_OPERATION"));
                con = ShowJobInMapViewActivity.this;

                Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this, con
                        .getResources().getString(R.string.alert_switching));
            } catch (Exception ex) {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "PRE_CRASHED" + ex.getMessage(), "LONG_OPERATION"));

            }
            SplashScreen.addLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "PRE_END", "LONG_OPERATION"));


        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void ShowOrphanFiles() {
        LongOrphanOperation op = new LongOrphanOperation();
        op.execute();

    }

    private class LongOrphanOperation extends
            AsyncTask<String, Void, ArrayList<filePathDataID>> {
        @Override
        protected ArrayList<filePathDataID> doInBackground(String... params) {
            Log.e("LongOrphanOperation", "true");
            checkConnectionPost();
            ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
            uploadList = DBHelper.getOrphanQuestionnaireUploadFiles(
                    Constants.UPLOAD_FILE_TABLE, new String[]{
                            Constants.UPLOAD_FILe_MEDIAFILE,
                            Constants.UPLOAD_FILe_DATAID,
                            Constants.UPLOAD_FILe_ORDERID,
                            Constants.UPLOAD_FILe_BRANCH_NAME,
                            Constants.UPLOAD_FILe_CLIENT_NAME,
                            Constants.UPLOAD_FILe_DATE,
                            Constants.UPLOAD_FILe_SET_NAME,
                            Constants.UPLOAD_FILe_SAMPLE_SIZE,
                            Constants.UPLOAD_FILe_PRODUCTID,
                            Constants.UPLOAD_FILe_LOCATIONID,}, null,
                    Constants.DB_TABLE_SUBMITSURVEY_OID, uploadList);
            if (uploadList != null) {
                // ArrayList<SubmitQuestionnaireData> orders =
                // getNumberofQuestionnaire(
                // false, false);
                ArrayList<filePathDataID> uploadNewList = new ArrayList<filePathDataID>();
                for (int i = 0; jobordersss != null && i < uploadList.size(); i++) {
                    boolean isStillInSystem = false;
                    for (int j = 0; j < jobordersss.size(); j++) {
                        if (uploadList.get(i).getUPLOAD_FILe_ORDERID() != null
                                && jobordersss.get(j).getOrderID() != null
                                && jobordersss.get(j).getStatusName() != null && !jobordersss.get(j).getStatusName().toLowerCase().contains("archive")
                                && !jobordersss.get(j).getAsArchive()
                                && uploadList
                                .get(i)
                                .getUPLOAD_FILe_ORDERID()
                                .equals(jobordersss.get(j).getOrderID())) {
                            isStillInSystem = true;
                            break;
                        }
                    }
                    if (isStillInSystem == false) {
                        uploadNewList.add(uploadList.get(i));
                    }
                }
                return uploadNewList;
            }
            return uploadList;
        }

        @Override
        protected void onPostExecute(ArrayList<filePathDataID> uploadList) {
            try {
//                showOrphanImages(uploadList, JobListActivity.this);
                Log.e("LongOrphanOperation_onPost", "true");

            } catch (Exception ex) {
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void ShowDBJobs() {
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        if (myPrefs
                .getBoolean(Constants.SETTINGS_ENABLE_ALTERNATE_ORDER, false)) {
            Constants.setEnableAlternateOrder(true);
        } else {
            Constants.setEnableAlternateOrder(false);
        }

        try {

            jobordersss = DBHelper
                    .getOrders(
                            DBHelper.whereJobListNotArchived,
                            Constants.DB_TABLE_JOBLIST,
                            new String[]{
                                    Constants.DB_TABLE_JOBLIST_ORDERID,
                                    Constants.DB_TABLE_JOBLIST_DATE,
                                    Constants.DB_TABLE_JOBLIST_SN,
                                    Constants.DB_TABLE_JOBLIST_DESC,
                                    Constants.DB_TABLE_JOBLIST_SETNAME,
                                    Constants.DB_TABLE_JOBLIST_SETLINK,
                                    Constants.DB_TABLE_JOBLIST_CN,
                                    Constants.DB_TABLE_JOBLIST_BFN,
                                    Constants.DB_TABLE_JOBLIST_BN,
                                    Constants.DB_TABLE_JOBLIST_CITYNAME,
                                    Constants.DB_TABLE_JOBLIST_ADDRESS,
                                    Constants.DB_TABLE_JOBLIST_BP,
                                    Constants.DB_TABLE_JOBLIST_OH,
                                    Constants.DB_TABLE_JOBLIST_TS,
                                    Constants.DB_TABLE_JOBLIST_TE,
                                    Constants.DB_TABLE_JOBLIST_SETID,
                                    Constants.DB_TABLE_JOBLIST_BL,
                                    Constants.DB_TABLE_JOBLIST_BLNG,
                                    Constants.DB_TABLE_JOBLIST_FN,
                                    Constants.DB_TABLE_JOBLIST_JC,
                                    Constants.DB_TABLE_JOBLIST_JI,
                                    Constants.DB_TABLE_JOBLIST_BLINK,
                                    Constants.DB_TABLE_JOBLIST_MID,
                                    Constants.DB_TABLE_CHECKER_CODE,
                                    Constants.DB_TABLE_CHECKER_LINK,
                                    Constants.DB_TABLE_BRANCH_CODE,
                                    Constants.DB_TABLE_SETCODE,
                                    Constants.DB_TABLE_PURCHASE_DESCRIPTION,
                                    Constants.DB_TABLE_PURCHASE,
                                    Constants.DB_TABLE_JOBLIST_BRIEFING,
                                    Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
                                    Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
                                    Constants.DB_TABLE_JOBLIST_sTransportationPayment,
                                    Constants.DB_TABLE_JOBLIST_sCriticismPayment,
                                    Constants.DB_TABLE_JOBLIST_sBonusPayment,
                                    Constants.DB_TABLE_JOBLIST_AllowShopperToReject,
                                    Constants.DB_TABLE_JOBLIST_sinprogressonserver,
                                    Constants.DB_TABLE_JOBLIST_sProjectName,
                                    Constants.DB_TABLE_JOBLIST_sRegionName,
                                    Constants.DB_TABLE_JOBLIST_sdeletedjob,
                                    Constants.DB_TABLE_JOBLIST_sProjectID,},
                            Constants.DB_TABLE_JOBLIST_JI);

            ArrayList<BranchProperties> branchProps = DBHelper.getBranchPropds(
                    Constants.DB_TABLE_BRANCH_PROPS, new String[]{
                            Constants.DB_TABLE_BRANCH_PROPS_ValueID,
                            Constants.DB_TABLE_BRANCH_PROPS_PropID,
                            Constants.DB_TABLE_BRANCH_PROPS_PropertyName,
                            Constants.DB_TABLE_BRANCH_PROPS_Content,
                            Constants.DB_TABLE_BRANCH_PROPS_BranchID,},
                    Constants.DB_TABLE_BRANCH_PROPS_PropID);

            {
                try {
                    pngItems = validateIcons(DBHelper.getIcons(), jobordersss);
                } catch (Exception ex) {
                    pngItems = new ArrayList<pngItem>();
                }
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                isBranchPropErr = myPrefs.getBoolean(
                        Constants.ALREADY_BRANCHPROPERR, false);

                if (pngItems.size() > 0 || isBranchPropErr == true) {
//                    btnErr.setVisibility(RelativeLayout.VISIBLE);
                } else {
//                    btnErr.setVisibility(RelativeLayout.GONE);
                }
                joborders = new ArrayList<orderListItem>();
                for (int i = 0; jobordersss != null && i < jobordersss.size(); i++) {

                    joborders.add(new orderListItem(jobordersss.get(i), null));
                }
                if (joborders != null) {
                    Orders.setListOrders(jobordersss);
                    Orders.setBranchProps(branchProps);
                    Surveys.setSets(DBHelper.getSurveyyRecords());
                    setOrderList();
                    showDbjobsPostPart();
                }
            }

        } catch (Exception ex) {
            int i = 0;
            i++;
            Toast.makeText(ShowJobInMapViewActivity.this, "DB jobs Crashed here",
                    Toast.LENGTH_LONG).show();
        }

    }

    private boolean isWifiOnly = false;

    public void start_uploading(boolean isProgress) {


        if (Helper.syncing == true) {
            Toast.makeText(
                    ShowJobInMapViewActivity.this,
                    getResources().getString(
                            R.string.synch_is_already_in_progress),
                    Toast.LENGTH_SHORT).show();
        }
        Helper.syncing = true;

        isWifiOnly = myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false);


        if (IsInternetConnectted()) {
            if (isWifiOnly) {
                ShowDBJobs();
                return;
            }

            SubmitSurveyTask sbmtSurveyTask = new SubmitSurveyTask();
            sbmtSurveyTask.isOnlyCertificate = upload_comp_jobs;
            upload_comp_jobs = false;
            sbmtSurveyTask.isProgress = isProgress;
            sbmtSurveyTask.execute();

        } else {
            ShowDBJobs();
            customAlert(
                    ShowJobInMapViewActivity.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));

        }

    }

    private void changeJobStatus(String tab, String orderid, String start_time, String did) {
        try {
            for (int i = 0; Orders.getOrders() != null && i < Orders.getOrders().size(); i++) {
                Order order = Orders.getOrders().get(i);
                if (order != null && order.getOrderID() != null && order.getOrderID().equals(orderid))
                    order.setStatusName(tab);
            }
        } catch (Exception ex) {

        }
        SplashScreen.addLog(new BasicLog(
                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                "Job moved to " + tab + " tab" + orderid, orderid));

        DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
                new String[]{Constants.DB_TABLE_ORDERS_ORDERID,
                        Constants.DB_TABLE_ORDERS_STATUS,
                        Constants.DB_TABLE_ORDERS_START_TIME,},
                orderid, tab,
                start_time, did);
        String where = Constants.DB_TABLE_ORDERS_ORDERID + "="
                + "\"" + orderid + "\"";
        DBHelper.deleteCompletedRecords(where);
    }

    private void showDbjobsPostPart() {
        try {
            setMapView(joborders);
        } catch (Exception ex) {
            int i = 0;
            i++;
        }
    }

    private String cert;
    private String certOrdeId;
    ArrayList<SubmitQuestionnaireData> sqd;

    public class SubmitSurveyTask extends AsyncTask<Void, Integer, String> {
        private String textFilePath = null;
        private SubmitQuestionnaireData archivedSQ;
        public boolean isOnlyCertificate;
        Revamped_Loading_Dialog dialog;
        UploadingProgressBars progressBars;
        int jobProgress = 0;
        int imageProgress = 0;
        int jobProgressTotal = 0;
        int imgProgressTotal = 0;

        String jobmsg = null;
        String imgmsg = null;

        public boolean isProgress;
        private boolean isquotafull;


        public SubmitSurveyTask() {
        }

        public SubmitSurveyTask(SubmitQuestionnaireData archivedSQ) {
            this.archivedSQ = archivedSQ;
        }

        @Override
        protected void onPreExecute() {
            Log.e("SubmitSurveyTask_onPre", "true");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            if (progressBars != null && progressBars.getDialog() != null
                    && progressBars.getDialog().isShowing())
                progressBars.getDialog().dismiss();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("SubmitSurveyTask_onPost", "true");
            try {
                progressBars.dismissDialog();
            } catch (Exception ex) {

            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            upload_comp_jobs = false;
            Helper.syncing = false;
            sendMessage(STOP_UPLOAD, null);
            ShowDBJobs();
            if (isOnlyCertificate) {
                if (ShowJobInMapViewActivity.this.cert != null) {
                    ArrayList<Cert> shortList = new ArrayList<Cert>();
                    Cert c = new Cert();
                    c.setCertID(ShowJobInMapViewActivity.this.cert);
                    shortList.add(c);
                    if (result.contains("Passed")) {
                        Toast.makeText(getApplicationContext(),
                                        "CheckerTificate Passed", Toast.LENGTH_LONG)
                                .show();
                    } else if (result.contains("Blocked")) {
                        Toast.makeText(getApplicationContext(),
                                        "CheckerTificate is Blocked", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        //delete it here
                        QuestionnaireActivity.deleteJobRecords(ShowJobInMapViewActivity.this.certOrdeId);
                        Toast.makeText(getApplicationContext(),
                                        "CheckerTificate not passed", Toast.LENGTH_LONG)
                                .show();
                    }

                    ShowJobInMapViewActivity.this.cert = null;
                    ShowJobInMapViewActivity.this.certOrdeId = null;
                }

            } else if (archivedSQ != null && textFilePath != null) {
                if (result != null) {
                    backToArchiveAlert(ShowJobInMapViewActivity.this, "Server Response: " + result);
                }
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.e("SubmitSurveyTask_onProgress", "true");
            if (jobmsg != null)
                progressBars.setTprogressBarJobsText(jobmsg);
            else
                progressBars.setTprogressBarJobsText("");
            if (imgmsg != null)
                progressBars.setTprogressBarImagesText(imgmsg);
            else
                progressBars.setTprogressBarImagesText("");

            progressBars.setprogressBarJobsMax(jobProgressTotal);
            progressBars.setprogressBarJobsProgress(jobProgress + 1);

            progressBars.setprogressBarImagesMax(imgProgressTotal);
            progressBars.setprogressBarImagesProgress(imageProgress + 1);

        }

        @Override
        protected String doInBackground(Void... params) {
            checkConnectionPost();
            Log.e("SubmitSurveyTask", "true");
            if (Connector.cookies == null) {
                if (showLogin(doLogin()))
                    return "SessionExpire";
            }
            String result = SubmitSurvey(isProgress, isOnlyCertificate);
            if (result.contains("<script>")) {
                doLogin();
                result = SubmitSurvey(isProgress, isOnlyCertificate);
            }
            // sendOrphanImages(dialog);
            return result;
        }

        private String getSetsRecords(String orderId) {
            String out = "";
            if (joborders != null) {
                for (int i = 0; i < joborders.size(); i++) {
                    if (joborders.get(i).orderItem != null
                            && joborders.get(i).orderItem.getSetName() != null
                            && joborders.get(i).orderItem.getOrderID().equals(
                            orderId)) {
                        out = joborders.get(i).orderItem.getSetName();
                        if (orderId.contains("-"))
                            out = joborders.get(i).orderItem.getBranchName();
                        break;
                    }
                }
            }
            return out;
        }

        private String SubmitArchiveJob(SubmitQuestionnaireData sq) {
            String result = "";
            ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
            jobProgressTotal = 1;
            jobProgress = 0;
            isquotafull = false;
            String response = uploadThisJob(sq, uploadList, 0, 1);
            jobProgress = 1;
            publishProgress(null);

            return response;
        }

        private String SubmitSurvey(boolean inProgress, boolean isCertificate) {

            if (textFilePath != null && textFilePath.length() > 0) {

                try {
                    return ParseAndUploadJob(textFilePath);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            String result = "";
            if (archivedSQ != null) {
                return SubmitArchiveJob(archivedSQ);
            }
            if (!inProgress)
                sqd = getNumberofQuestionnaire(inProgress, isCertificate);

            if (sqd == null || sqd.size() < 1) {
                return "";
            }
            if (!inProgress)
                sqd = validateAllSQ(sqd);
            ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
            jobProgressTotal = sqd.size();
            jobProgress = 0;
            boolean isquotafull = false;
            for (int i = 0; i < sqd.size(); i++) {

                result = uploadThisJob(sqd.get(i), uploadList, i, sqd.size());
                if (result.contains("<script>")) {
                    doLogin();
                    result = uploadThisJob(sqd.get(i), uploadList, i, sqd.size());
                }
                jobProgress = i;
                publishProgress(null);

            }

            return result;
        }

        private String ParseAndUploadJob(String input) throws UnsupportedEncodingException {
            byte[] data = Base64.decode(input, Base64.DEFAULT);
            String text = new String(data, "UTF-8");

            List<NameValuePair> nvp = PrepareQuestionnaireNameValuePair(
                    text);
            if (nvp == null || nvp.size() == 0) {
                return null;
            }
            String result = null;
            result = Connector
                    .postForm(Constants.getSubmitSurveyURL(), nvp);

            if (!CheckResponse(result)) {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "PARSEDJOB NOT SUCCESS Order Uploaded: Reply from server:" + result, "PARSED"));

            } else SplashScreen.addLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "PARSEDJOB SUCCESS Order Uploaded: Reply from server:" + result, "PARSED"));

            return "";
        }

        private String uploadThisJob(SubmitQuestionnaireData sq, ArrayList<filePathDataID> uploadList, int i, int size) {
            imgProgressTotal = 0;
            imageProgress = 0;

            String setId = DBHelper.getShelfSetIdItemsForJobList(
                    Constants.DB_TABLE_POS,
                    new String[]{Constants.DB_TABLE_POS_SetId},
                    Constants.DB_TABLE_POS_OrderId + "=" + "\""
                            + sq.getOrderid() + "\"");
            Set set = null;
            if (sq.getOrderid() != null) {
                String setlink = DBHelper.getSetIdFromOrder(
                        Constants.DB_TABLE_JOBLIST,
                        new String[]{Constants.DB_TABLE_JOBLIST_SETID},
                        Constants.DB_TABLE_JOBLIST_ORDERID + "=" + "\""
                                + sq.getOrderid() + "\"");
                sq.setSetid(setlink);
                try {
                    set = (Set) DBHelper.convertFromBytesWithOrder(setlink,
                            sq.getOrderid());
                    sq.setSetVersionID(set.getSetVersionID());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    set = null;
                }

            }
            POS_Shelf pos_shelf_item = null;
            {
                String setName = "";
                if (sq.getSID() != null) {
                    setName = getSetsRecords(sq.getOrderid());

                }
                String message = getResources().getString(
                        R.string.aauploading_job);
                message = message.replace("##", setName);
                jobmsg = (message + ", " + (i + 1) + "/" + size);
                publishProgress(null);
            }

            if (setId != null) {

                if (set == null) {
                    try {
                        set = (Set) DBHelper.convertFromBytesWithOrder(setId,
                                sq.getOrderid());
                    } catch (Exception e) {
                        e.printStackTrace();
                        set = null;
                    }
                }
                if (set != null) {

                    pos_shelf_item = new POS_Shelf(ShowJobInMapViewActivity.this);
                    pos_shelf_item.listProducts = set.getListProducts();
                    pos_shelf_item.listProductLocations = set
                            .getListProductLocations();
                    pos_shelf_item.listProductProperties = set
                            .getListProductProperties();
                    if (pos_shelf_item.price_item == null)
                        pos_shelf_item.price_item = new Price();
                    if (pos_shelf_item.quantity_item == null)
                        pos_shelf_item.quantity_item = new Quantity();
                    if (pos_shelf_item.expiration_item == null)
                        pos_shelf_item.expiration_item = new Expiration();
                    if (pos_shelf_item.note_item == null)
                        pos_shelf_item.note_item = new Note();
                    if (pos_shelf_item.picture_item == null)
                        pos_shelf_item.picture_item = new Picture();
                    pos_shelf_item = DBHelper.getShelfItems(
                            Constants.DB_TABLE_POS, new String[]{
                                    Constants.DB_TABLE_POS_LocationId,
                                    Constants.DB_TABLE_POS_OrderId,
                                    Constants.DB_TABLE_POS_Price,
                                    Constants.DB_TABLE_POS_ProductId,
                                    Constants.DB_TABLE_POS_PropertyId,
                                    Constants.DB_TABLE_POS_Quantity,
                                    Constants.DB_TABLE_POS_SetId,
                                    Constants.DB_TABLE_POS_Notee,
                                    Constants.DB_TABLE_POS_date},
                            Constants.DB_TABLE_POS_OrderId + "=" + "\""
                                    + sq.getOrderid() + "\"",
                            pos_shelf_item, false);
                }
            }
            List<NameValuePair> nvp = PrepareQuestionnaireNameValuePair(
                    isProgress, sq, pos_shelf_item);
            if (nvp == null) {
                return null;
            }
            String nvplog = nvp.toString();
            nvplog = "";
            String result = null;
            result = Connector
                    .postForm(Constants.getSubmitSurveyURL(), nvp);

            if (!CheckResponse(result)) {

                SplashScreen.addLog(new BasicLog(Constants.getSubmitSurveyURL(),
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "NOT SUCCESS Order Uploaded:" + sq.getOrderid() + " Reply from server:" + result + "nvp=" + nvplog, sq.getOrderid()));

                if (result != null && result.toLowerCase().contains("not all questions"))
                    result = Connector
                            .postForm(Constants.getSubmitSurveyURL(), nvp);
                if (!CheckResponse(result)) {
//no suyccess even after re-try
                    SplashScreen.addLog(new BasicLog(Constants.getSubmitSurveyURL(),
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                            "NOT SUCCESS AFTER RETRY Order Uploaded:" + sq.getOrderid() + " Reply from server:" + result + "nvp=" + nvplog, sq.getOrderid()));
                    if (sq.getOrderid() != null
                            && sq.getOrderid().contains("CC")) {
                        DBHelper.updateOrders(
                                Constants.DB_TABLE_ORDERS,
                                new String[]{
                                        Constants.DB_TABLE_ORDERS_ORDERID,
                                        Constants.DB_TABLE_ORDERS_STATUS,
                                        Constants.DB_TABLE_ORDERS_START_TIME,},
                                sq.getOrderid(), "In progress", "", null);
                    }
                    return result;
                } else {
                    SplashScreen.addLog(new BasicLog(Constants.getSubmitSurveyURL(),
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                            "SUCCESS AFTER RETRY Order Uploaded:" + sq.getOrderid() + " Reply from server:" + result, sq.getOrderid()));
                }
            } else {
                SplashScreen.addLog(new BasicLog(Constants.getSubmitSurveyURL(),
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "SUCCESS Order Uploaded:" + sq.getOrderid() + " Reply from server:" + result, sq.getOrderid()));

            }

            uploadList = DBHelper.getQuestionnaireUploadFiles(
                    Constants.UPLOAD_FILE_TABLE, new String[]{
                            Constants.UPLOAD_FILe_MEDIAFILE,
                            Constants.UPLOAD_FILe_DATAID,
                            Constants.UPLOAD_FILe_ORDERID,
                            Constants.UPLOAD_FILe_BRANCH_NAME,
                            Constants.UPLOAD_FILe_CLIENT_NAME,
                            Constants.UPLOAD_FILe_DATE,
                            Constants.UPLOAD_FILe_SET_NAME,
                            Constants.UPLOAD_FILe_SAMPLE_SIZE,},
                    sq.getOrderid(), Constants.DB_TABLE_SUBMITSURVEY_OID,
                    uploadList);
//            renameCamFiles(uploadList, sq.getUnix());

            imgProgressTotal = uploadList.size();

            SplashScreen.addLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "Attachments Uploading:" + sq.getOrderid() + " Total attachments" + imgProgressTotal, sq.getOrderid()));
// uploadFileList.clear();
            if (set == null) {
                String setlink = DBHelper.getSetIdFromOrder(
                        Constants.DB_TABLE_JOBLIST,
                        new String[]{Constants.DB_TABLE_JOBLIST_SETID},
                        Constants.DB_TABLE_JOBLIST_ORDERID + "=" + "\""
                                + sq.getOrderid() + "\"");
                try {
                    set = (Set) DBHelper.convertFromBytes(setlink);
                } catch (Exception e) {
                    e.printStackTrace();
                    set = null;
                }
            }
            for (int j = 0; isquotafull == false && j < uploadList.size(); j++) {
                imageProgress = j;
                String path = uploadList.get(j).getFilePath();
                if (uploadList.get(j).getFilePath().startsWith("content")) {
                    path = getRealPathFromURI(Uri.parse(uploadList.get(j)
                            .getFilePath()));
                } else if (uploadList.get(j).getFilePath()
                        .startsWith("file:///")) {
                    path = path.replace("file:///", "/");
                }

                String str = getString(R.string.uploading_image);
                imgmsg = str + path;
                publishProgress(null);
                // dialog.changeMessage("Uploading image:" + path);
                String did = uploadList.get(j).getDataID();
                String newDataId = null;
                if (did != null && did.contains("#@")) {
                    String[] dids = did.split("#@");
                    newDataId = convertDataIdForNameValuePair(dids[0]);
                    did = getMePrefix(dids[1]);
                    did = did + "obj" + newDataId + "]";
                }

                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                String forceSmping = null;
                if (set != null)
                    forceSmping = set.getForceImageStamp();
                String res = Connector.saveFiletoServer(
                        (forceSmping != null && forceSmping.equals("1")),
                        path, Constants.getAttachmentURL(),
                        sq.getOrderid(), did, sq.getUnix(),
                        uploadList.get(j).getUPLOAD_FILe_Sample_size(), uploadList.get(j).getUPLOAD_FILe_PRODUCTID(), uploadList.get(j).getUPLOAD_FILe_LOCATIONID());
                if (CheckResponse(res)) {
                    SplashScreen.addLog(new BasicLog(Constants.getAttachmentURL(),
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                            "Attachments Uploading:SUCCESS" + path + " ORDER" + sq.getOrderid() + " Total attachments" + imgProgressTotal, sq.getOrderid()));
                    if (path.contains("CAM_O"
                            + uploadList.get(j).getUPLOAD_FILe_ORDERID())) {
                        String newPAth = path.replace("CAM_O"
                                        + uploadList.get(j)
                                        .getUPLOAD_FILe_ORDERID(),
                                "CAM_UNIX"
                                        + sq.getUnix()
                                        + "O"
                                        + uploadList.get(j)
                                        .getUPLOAD_FILe_ORDERID());
                        File f = new File(newPAth);
                        try {
                            // f.delete();
                        } catch (Exception ex) {

                        }
                    }
                    try {
                        String where = Constants.UPLOAD_FILe_MEDIAFILE
                                + "=" + "\""
                                + uploadList.get(j).getFilePath() + "\"";
                        DBAdapter.openDataBase();
                        DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE,
                                where, null);
                        DBAdapter.closeDataBase();
                        DBHelper.deleteFile(sqd.get(i).getOrderid(),
                                uploadList.get(j).getFilePath());
                        if (path.contains(Constants.UPLOAD_PATH)) {
                            File file = new File(path);
//                                file.delete();
                        }
                    } catch (Exception ex) {

                    }

                } else {

                    try {
                        if (res.contains("<script>")) {
                            doLogin();
                            res = Connector.saveFiletoServer(
                                    (forceSmping != null && forceSmping.equals("1")),
                                    path, Constants.getAttachmentURL(),
                                    sq.getOrderid(), did, sq.getUnix(),
                                    uploadList.get(j).getUPLOAD_FILe_Sample_size(), uploadList.get(j).getUPLOAD_FILe_PRODUCTID(), uploadList.get(j).getUPLOAD_FILe_LOCATIONID());
                        }
                    } catch (Exception e) {
                        Log.e("exception", e.toString());
                    }
                    SplashScreen.addLog(new BasicLog(Constants.getAttachmentURL(),
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                            "Attachments Uploading:NOTSUCCESS" + path + " ORDER" + sq.getOrderid() + " Total attachments" + imgProgressTotal + "RESPONSE=" + res, sq.getOrderid()));

                    if (CheckResponseForStorageQuota(res)) {
                        ((Activity) ShowJobInMapViewActivity.this)
                                .runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ShowAlert(
                                                ShowJobInMapViewActivity.this,
                                                "",
                                                "ERROR uploading files, System storage quota is full please contact administrator to upload pending attachments..",
                                                "Ok");
                                    }
                                });
                        isquotafull = true;
                        imgmsg = "Storage Quota Full!";
                        break;
                    }
                }

            }
            imgmsg = "";
            publishProgress(null);
            if (CheckResponse(result)) {

                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "uploading done now deleting job: " + sq.getOrderid(), sq.getOrderid()));
                if (isProgress) {
                } else {
                    try {
                        try {
                            DBHelper.deleteTimeStamp(
                                    sdf.format(new Date()), sq
                                            .getOrderid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String where = Constants.DB_TABLE_JOBLIST_ORDERID
                                + "=" + "\"" + sq.getOrderid()
                                + "\"";
                        Calendar myCalendar = Calendar.getInstance();
                        String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, "");
                        String user = myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, "");
                        ArchiveData archiveData =
                                new ArchiveData(sq, url, user, sq.getOrderid(), nvp, uploadList);
                        try {
                            DBHelper.convertArchiveToBytes(archiveData, "archived_user_" + user + "order_" + sq.getOrderid() + ".txt");
                        } catch (Exception ex) {
                            int io = 0;
                            io++;
                        }

                        changeJobStatus("archived", sq.getOrderid(), sdf.format(myCalendar.getTime()), null);

                    } catch (Exception ex) {
                        String str = "";
                        str += "";
                    }
                }

            } else {
                // if (dialog != null)
                {
                    String setName = "";
                    if (sq.getSID() != null) {
                        setName = getSetsRecords(sq.getOrderid());

                    }
                    String message = getResources().getString(
                            R.string.aauploading_job);
                    message = message.replace("##", setName);
                    jobmsg = (message + " had error from server side ");
                }
                try {
                    String where = Constants.DB_TABLE_JOBLIST_ORDERID + "="
                            + "\"" + sq.getOrderid() + "\"";
                    DBHelper.incrementTriesAgainstOrderId(where, sq
                            .getTries());

                } catch (Exception ex) {
                    String str = "";
                    str += "";
                }
            }
            return result;
        }

    }

    public void backToArchiveAlert(Context context, String textString) {

        final Dialog dialog = new Dialog(ShowJobInMapViewActivity.this);
        dialog.setContentView(R.layout.custom_red_alert);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView1);
        text.setText(textString);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openArciveScreen();
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void openArciveScreen() {
        Intent intent = new Intent(ShowJobInMapViewActivity.this.getApplicationContext(),
                ArchiveActivity.class);
        ArchiveActivity.toBeUploadedSQ = null;
        startActivityForResult(intent, JOB_ARCHIVE_ACTIVITY_CODE);
    }


    private ArrayList<SubmitQuestionnaireData> getNumberofQuestionnaire(
            boolean inProgress, boolean isCerts) {
        ArrayList<SubmitQuestionnaireData> questionnaireData = new ArrayList<SubmitQuestionnaireData>();
        questionnaireData = DBHelper
                .getSubmitQuestionnaireList(
                        Constants.DB_TABLE_SUBMITSURVEY,
                        new String[]{
                                Constants.DB_TABLE_SUBMITSURVEY_OID,
                                Constants.DB_TABLE_SUBMITSURVEY_FT,
                                Constants.DB_TABLE_SUBMITSURVEY_SLT,
                                Constants.DB_TABLE_SUBMITSURVEY_SLNG,
                                Constants.DB_TABLE_SUBMITSURVEY_ELT,
                                Constants.DB_TABLE_SUBMITSURVEY_ELNG,
                                Constants.DB_TABLE_SUBMITSURVEY_REPORTED_START_TIME,
                                Constants.DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME,
                                Constants.DB_TABLE_SUBMITSURVEY_UNEMPTY_QUES_COUNT,
                                Constants.DB_TABLE_SUBMITSURVEY_SID,
                                Constants.POST_FIELD_QUES_UNIX,
                                Constants.DB_TABLE_SUBMITSURVEY_purchase_details,
                                Constants.DB_TABLE_SUBMITSURVEY_purchase_payment,
                                Constants.DB_TABLE_SUBMITSURVEY_purchase_description,
                                Constants.DB_TABLE_SUBMITSURVEY_service_invoice_number,
                                Constants.DB_TABLE_SUBMITSURVEY_service_payment,
                                Constants.DB_TABLE_SUBMITSURVEY_service_description,
                                Constants.DB_TABLE_SUBMITSURVEY_transportation_payment,
                                Constants.DB_TABLE_SUBMITSURVEY_transportation_description,
                                Constants.DB_TABLE_SUBMITSURVEY_RS},
                        null, Constants.DB_TABLE_SUBMITSURVEY_OID);

        questionnaireData = cleanUploaedJobsHere(questionnaireData);
        if (isCerts && questionnaireData != null
                && questionnaireData.size() > 0) {
            ArrayList<SubmitQuestionnaireData> certs = new ArrayList<SubmitQuestionnaireData>();
            for (int i = 0; i < questionnaireData.size(); i++) {
                if (questionnaireData.get(i).getOrderid() != null
                        && questionnaireData.get(i).getOrderid().contains("CC")) {
                    certs.add(questionnaireData.get(i));
                }
            }
            return certs;
        }
        return questionnaireData;
    }

    private ArrayList<SubmitQuestionnaireData> cleanUploaedJobsHere(
            ArrayList<SubmitQuestionnaireData> sqd) {

        String where = "StatusName<>" + "\"Scheduled\"" + " AND StatusName<>"
                + "\"Assigned\"" + " AND StatusName<>" + "\"survey\""
                + " AND StatusName<>" + "\"In progress\"" + " AND StatusName<>"
                + "\"Completed\"";

        ArrayList<Order> uploadedJobs = DBHelper
                .getOrders(
                        where,
                        Constants.DB_TABLE_JOBLIST,
                        new String[]{
                                Constants.DB_TABLE_JOBLIST_ORDERID,
                                Constants.DB_TABLE_JOBLIST_DATE,
                                Constants.DB_TABLE_JOBLIST_SN,
                                Constants.DB_TABLE_JOBLIST_DESC,
                                Constants.DB_TABLE_JOBLIST_SETNAME,
                                Constants.DB_TABLE_JOBLIST_SETLINK,
                                Constants.DB_TABLE_JOBLIST_CN,
                                Constants.DB_TABLE_JOBLIST_BFN,
                                Constants.DB_TABLE_JOBLIST_BN,
                                Constants.DB_TABLE_JOBLIST_CITYNAME,
                                Constants.DB_TABLE_JOBLIST_ADDRESS,
                                Constants.DB_TABLE_JOBLIST_BP,
                                Constants.DB_TABLE_JOBLIST_OH,
                                Constants.DB_TABLE_JOBLIST_TS,
                                Constants.DB_TABLE_JOBLIST_TE,
                                Constants.DB_TABLE_JOBLIST_SETID,
                                Constants.DB_TABLE_JOBLIST_BL,
                                Constants.DB_TABLE_JOBLIST_BLNG,
                                Constants.DB_TABLE_JOBLIST_FN,
                                Constants.DB_TABLE_JOBLIST_JC,
                                Constants.DB_TABLE_JOBLIST_JI,
                                Constants.DB_TABLE_JOBLIST_BLINK,
                                Constants.DB_TABLE_JOBLIST_MID,
                                Constants.DB_TABLE_CHECKER_CODE,
                                Constants.DB_TABLE_CHECKER_LINK,
                                Constants.DB_TABLE_BRANCH_CODE,
                                Constants.DB_TABLE_SETCODE,
                                Constants.DB_TABLE_PURCHASE_DESCRIPTION,
                                Constants.DB_TABLE_PURCHASE,
                                Constants.DB_TABLE_JOBLIST_BRIEFING,
                                Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
                                Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
                                Constants.DB_TABLE_JOBLIST_sTransportationPayment,
                                Constants.DB_TABLE_JOBLIST_sCriticismPayment,
                                Constants.DB_TABLE_JOBLIST_sBonusPayment,
                                Constants.DB_TABLE_JOBLIST_AllowShopperToReject,
                                Constants.DB_TABLE_JOBLIST_sinprogressonserver,
                                Constants.DB_TABLE_JOBLIST_sProjectName,
                                Constants.DB_TABLE_JOBLIST_sRegionName,
                                Constants.DB_TABLE_JOBLIST_sdeletedjob,
                                Constants.DB_TABLE_JOBLIST_sProjectID,},
                        Constants.DB_TABLE_JOBLIST_JI);

        if (uploadedJobs != null && sqd == null) {
            // cleaning will be done here

            for (int i = 0; i < uploadedJobs.size(); i++) {
                Order o = uploadedJobs.get(i);
                if (o != null) {
                    int k = 0;
                    k++;
                    if (o.getStatusName().contains("on")) {
                        // you are on boy
                        String[] splits = o.getStatusName().split(" on ");
                        if (splits.length > 1) {
                            String time = splits[1].trim();
                            boolean shouldDelete = matchWithCurrenTime(time);
                            if (shouldDelete == true) {

                                String wheree = Constants.DB_TABLE_QUESTIONNAIRE_ORDERID
                                        + "=" + "\"" + o.getOrderID() + "\"";
                                DBHelper.deleteJoblistRecords(wheree);
                                DBHelper.deleteRecordbyOrdeid(wheree);
                            }
                        }
                    }
                }
            }
        } else if (uploadedJobs != null && sqd != null) {
            for (int i = 0; i < uploadedJobs.size(); i++) {
                Order o = uploadedJobs.get(i);
                if (o != null) {
                    for (int j = 0; j < sqd.size(); j++) {
                        SubmitQuestionnaireData sq = sqd.get(j);
                        if (sq.getOrderid() != null
                                && uploadedJobs.get(i).getOrderID() != null
                                && sq.getOrderid().equals(
                                uploadedJobs.get(i).getOrderID())) {
                            sqd.remove(sq);
                        }
                    }
                }
            }
        }
        return sqd;

    }

    private boolean matchWithCurrenTime(String time) {
        Calendar myCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.ENGLISH);
        long thisTime = gettime(sdf.format(myCalendar.getTime()));
        long uploadedTime = gettime(time) + (3 * 24 * 60 * 60 * 1000);
        if (thisTime > uploadedTime) {
            return true;
        }
        return false;
    }

    private long gettime(String format) {
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.ENGLISH);
            Date date = sdf.parse(format);
            long l = date.getTime();
            return l;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    private ArrayList<SubmitQuestionnaireData> validateAllSQ(
            ArrayList<SubmitQuestionnaireData> sqd) {

        ArrayList<SubmitQuestionnaireData> newList = new ArrayList<SubmitQuestionnaireData>();
        for (int i = 0; i < sqd.size(); i++) {
            // if (sqd.get(i).getTriesInt() > 5)
            // continue;
            SubmitQuestionnaireData sq = sqd.get(i);
            String status = DBHelper.getOrderStatus(
                    Constants.DB_TABLE_JOBLIST,
                    new String[]{Constants.DB_TABLE_JOBLIST_SN,},
                    Constants.DB_TABLE_JOBLIST_ORDERID + "=" + "\""
                            + sq.getOrderid() + "\"");
            if (status == null || !status.toLowerCase().equals("completed")) {
                continue;
            }
            String setId = DBHelper.getShelfSetIdItemsForJobList(
                    Constants.DB_TABLE_POS,
                    new String[]{Constants.DB_TABLE_POS_SetId},
                    Constants.DB_TABLE_POS_OrderId + "=" + "\""
                            + sq.getOrderid() + "\"");
            POS_Shelf pos_shelf_item = null;
            if (setId != null) {
                Set set = validationSets.getSetAvailable(setId);
                if (set != null) {
                    pos_shelf_item = new POS_Shelf(ShowJobInMapViewActivity.this);
                    pos_shelf_item.listProducts = set.getListProducts();
                    pos_shelf_item.listProductLocations = set
                            .getListProductLocations();
                    pos_shelf_item.listProductProperties = set
                            .getListProductProperties();
                    if (pos_shelf_item.price_item == null)
                        pos_shelf_item.price_item = new Price();
                    if (pos_shelf_item.quantity_item == null)
                        pos_shelf_item.quantity_item = new Quantity();
                    if (pos_shelf_item.expiration_item == null)
                        pos_shelf_item.expiration_item = new Expiration();
                    if (pos_shelf_item.note_item == null)
                        pos_shelf_item.note_item = new Note();
                    if (pos_shelf_item.picture_item == null)
                        pos_shelf_item.picture_item = new Picture();
                    pos_shelf_item = DBHelper.getShelfItems(
                            Constants.DB_TABLE_POS,
                            new String[]{Constants.DB_TABLE_POS_LocationId,
                                    Constants.DB_TABLE_POS_OrderId,
                                    Constants.DB_TABLE_POS_Price,
                                    Constants.DB_TABLE_POS_ProductId,
                                    Constants.DB_TABLE_POS_PropertyId,
                                    Constants.DB_TABLE_POS_Quantity,
                                    Constants.DB_TABLE_POS_SetId,
                                    Constants.DB_TABLE_POS_Notee,
                                    Constants.DB_TABLE_POS_date},
                            Constants.DB_TABLE_POS_OrderId + "=" + "\""
                                    + sq.getOrderid() + "\"", pos_shelf_item,
                            false);
                }

            }
            List<NameValuePair> nvp = PrepareQuestionnaireNameValuePair(false,
                    sq, pos_shelf_item);

            if (nvp != null) {

                newList.add(sq);
            }
        }

        return newList;
    }

    private List<NameValuePair> PrepareQuestionnaireNameValuePair(String input) {
        FileInputStream is;
        BufferedReader reader;
        final File file = new File(input);
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        //if (file.exists())
        {
            try {
                String lines[] = input.split("\n");
                String lastDataid = "";
                for (String line : lines) {
                    line = line.trim();
                    if (extraDataList == null)
                        extraDataList = new ArrayList<NameValuePair>();

                    if (line.contains("[") && line.contains("]") && line.contains("=>")) {
                        String lineParts[] = line.split("=>");
                        String firstPart = lineParts[0].replace("[", "");
                        firstPart = firstPart.replace("]", "");
                        firstPart = firstPart.trim();
                        if (lineParts.length > 1) {

                            String secondPart = lineParts[1].trim();
                            boolean isNunmber = false;
                            try {
                                Integer.parseInt(firstPart);
                                isNunmber = true;
                            } catch (Exception ex) {

                            }
                            if (isNunmber) {
                                firstPart = lastDataid + "[]";
                            }
                            if (firstPart.startsWith("obj") && secondPart.contains("Array")) {
                                lastDataid = firstPart;
                            } else
                                extraDataList.add(Helper.getNameValuePair(
                                        firstPart, secondPart));
                        } else
                            extraDataList.add(Helper.getNameValuePair(firstPart, ""));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return extraDataList;
    }


    private List<NameValuePair> PrepareQuestionnaireNameValuePair(
            boolean isProgress, SubmitQuestionnaireData sqd,
            POS_Shelf pos_shelf_item) {
        ArrayList<QuestionnaireData> questionnaireData = getQuestionnaireData(sqd
                .getOrderid());

        if (questionnaireData == null) {
            questionnaireData = new ArrayList<QuestionnaireData>();
        }

        if (questionnaireData.size() == 0)
            return null;

        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = ShowJobInMapViewActivity.this.getPackageManager().getPackageInfo(
                    ShowJobInMapViewActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_VALUE_QUES_APP_VERSION, app_ver));
        String reqString = Build.MANUFACTURER
                + " "
                + Build.MODEL
                + " "
                + Build.VERSION.RELEASE
                + " "
                + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT]
                .getName() + " RAM=" + getTotalRAM() + "Mbs";
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_DEVICE_INFO, reqString));
        if (!sqd.getOrderid().contains("-")) {
            if (sqd.getOrderid().contains("CC")) {

                this.cert = sqd.getOrderid().replace("CC", "");
                this.certOrdeId = sqd.getOrderid();
                if (cert.contains("SS")) {
                    cert = cert.substring(0, cert.indexOf("SS"));
                }

                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_CERT_ID, cert));
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_QUES_ORDER_ID, "-222"));
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_SETID, sqd.getSetid()));
            } else
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_QUES_ORDER_ID, sqd.getOrderid()));
        }

        if (sqd.getSetVersionID() != null && !sqd.getSetVersionID().equals("")) {
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_SETVERSIONID, sqd.getSetVersionID()));
        }

        if (sqd.getRs() != null && !sqd.getRs().equals("")) {
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_QUES_RS, sqd.getRs()));
        }
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_QUES_CRITFREETEXT, sqd.getFt()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_QUES_CRITSTARTLAT, sqd.getSlt()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_QUES_CRITSTARTLONG, sqd.getSlng()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_QUES_CRITENDLAT, sqd.getElt()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_VALUE_QUES_CRITENDLONG, sqd.getElng()));

        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_PurchaseInvoiceNumber,
                sqd.getDB_TABLE_SUBMITSURVEY_purchase_details()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_PurchasePayment,
                sqd.getDB_TABLE_SUBMITSURVEY_purchase_payment()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_PurchaseDescription,
                sqd.getDB_TABLE_SUBMITSURVEY_purchase_description()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_ServiceInvoiceNumber,
                sqd.getDB_TABLE_SUBMITSURVEY_service_invoice_number()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_ServicePayment,
                sqd.getDB_TABLE_SUBMITSURVEY_service_payment()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_purchase_ServiceDescription,
                sqd.getDB_TABLE_SUBMITSURVEY_service_description()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_transportation_TransportationPayment,
                sqd.getDB_TABLE_SUBMITSURVEY_transportation_payment()));
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_transportation_Description,
                sqd.getDB_TABLE_SUBMITSURVEY_transportation_description()));

        String time = sqd.getFtime();
        sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.ENGLISH);

        if (time == null || time.equals("")) {

            time = sdf.format(new java.util.Date());
        }
        // to stop time from going 24 instead of 00
        if (time != null && time.contains(" 24:")) {
            time = time.replace(" 24:", " 00:");
        }
        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_VALUE_QUES_REPORTED_FINISH_TIME, time));
        {
            String unix = sqd.getUnix();
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_QUES_UNIX, unix));
        }
        time = sqd.getStime();
        if (time == null || time.equals(""))
            time = sdf.format(new java.util.Date());

        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_VALUE_QUES_REPORTED_START_TIME, time));
        int previousCount = 0;
        String previousLoopName = "";
        String previousLoopData = "";

        String thisloopPrefix = null;
        for (int i = 0; i < questionnaireData.size(); i++) {

            String loopKey = null;

            QuestionnaireData qd = questionnaireData.get(i);
            if (qd.getDataID().contains("15095")) {
                int iii = 0;
                iii++;
            }
            if (qd.getDataID().contains("^")) {
                String loopname = "";
                String loopdata = "";
                String listItemId = "";

                String splitData[] = qd.getDataID().split("\\^");
                if (splitData.length > 0
                        && splitData[splitData.length - 1].contains("=")) {
                    splitData = splitData[splitData.length - 1].split("=");
                    loopdata = splitData[1];
                }
                if (qd.getLoopinfo() != null && qd.getLoopinfo().contains("=")) {
                    String loopinfo = qd.getLoopinfo();
                    if (loopinfo.contains("^")) {

                        String[] loopinfos = loopinfo.split("\\^");
                        loopinfo = loopinfos[loopinfos.length - 1];
                    }
                    splitData = loopinfo.split("=");
                    listItemId = splitData[1];
                    loopname = splitData[0];
                    if (splitData.length > 2) {
                        loopdata = splitData[2];
                    }
                }

                int powerCount = qd.getDataID().split("\\^", -1).length - 1;
                if (powerCount == previousCount + 1) {

                    // extraDataList.add(Helper.getNameValuePair(
                    // Constants.POST_FIELD_LOOPS_STARTED + "[]", ""));
                    // Loop started
                    int ii = 0;
                    ii++;
                } else if (thisloopPrefix != null
                        && thisloopPrefix.contains("[items]")) {
                    int lio = thisloopPrefix.lastIndexOf("[" + "items" + "]");
                    String sub = thisloopPrefix.substring(lio);
                    thisloopPrefix = thisloopPrefix.replace(sub, "");
                }
                if (powerCount == previousCount - 1) {
                    // Loop finished
                    // extraDataList.add(Helper.getNameValuePair(
                    // Constants.POST_FIELD_LOOPS_FINISHEd + "[]", ""));

                    int ii = 0;
                    ii++;

                    if (thisloopPrefix != null
                            && thisloopPrefix.contains("[loops]")) {
                        int lio = thisloopPrefix.lastIndexOf("[" + "loops"
                                + "]");
                        String sub = thisloopPrefix.substring(lio);
                        thisloopPrefix = thisloopPrefix.replace(sub, "");
                    }
                    if (powerCount == 1)
                        thisloopPrefix = null;

                }
                previousCount = powerCount;

                if (!loopname.equals(previousLoopName)) {
                    // New loop started
                    // extraDataList.add(Helper.getNameValuePair(""
                    // + Constants.POST_FIELD_LOOPS_STARTED + "", ""));
                    // extraDataList.add(Helper.getNameValuePair("[" + loopname
                    // + "]", ""));
                    // extraDataList.add(Helper.getNameValuePair(
                    // Constants.POST_FIELD_LOOPS_ITEMS + "[]", ""));
                    //
                    if (thisloopPrefix == null)
                        thisloopPrefix = Constants.POST_FIELD_LOOPS_STARTED
                                + "[" + loopname + "]";
                    else
                        thisloopPrefix += "["
                                + Constants.POST_FIELD_LOOPS_STARTED + "]"
                                + "[" + loopname + "]";

                    previousLoopName = loopname;
                }
                if (thisloopPrefix == null)
                    thisloopPrefix = "";
                loopKey = thisloopPrefix = thisloopPrefix + "[items]" + "["
                        + listItemId + "]";
                loopKey = getMePrefix(qd.getLoopinfo());
                if (loopdata != null) {
                    // New loop Index started
                    // extraDataList.add(Helper.getNameValuePair("[" +
                    // listItemId
                    // + "]", ""));
                    if (loopdata != null && loopdata.equals("NA"))
                        loopdata = "";
                    extraDataList.add(Helper.getNameValuePair(loopKey + "["
                            + Constants.POST_FIELD_LOOPS_NAME + "]", loopdata));
                    //
                    // extraDataList.add(Helper.getNameValuePair(loopKey + "["
                    // + Constants.POST_FIELD_LOOPS_RESPONSES + "]", ""));

                    previousLoopData = loopdata;
                }

            } else if (previousCount > 0) {
                // loop finishes
                int ii = 0;
                ii++;
            }

            String newDataId = convertDataIdForNameValuePair(qd.getDataID());

            if (qd.getObjectType() != null
                    && (qd.getObjectType().equals("9") || qd.getObjectType()
                    .equals("10"))) {
                if (qd.getObjectType().equals("10")) {
                    if (loopKey != null)
                        extraDataList = getBranchSelection(loopKey + "["
                                        + Constants.POST_FIELD_LOOPS_RESPONSES + "][",
                                "]", extraDataList, qd, newDataId);
                    else
                        extraDataList = getBranchSelection("", "",
                                extraDataList, qd, newDataId);
                } else {
                    if (loopKey != null)
                        extraDataList = getWorkerSelection(loopKey + "["
                                        + Constants.POST_FIELD_LOOPS_RESPONSES + "][",
                                "]", extraDataList, qd, newDataId);
                    else
                        extraDataList = getWorkerSelection("", "",
                                extraDataList, qd, newDataId);
                }
            } else if (qd.getQuestionTypeLink() != null) {
                if (qd.getQuestionTypeLink().equals("7")
                        || qd.getQuestionTypeLink().equals("3")) {
                    if (loopKey != null)
                        extraDataList = getSingleChoiceText(loopKey + "["
                                        + Constants.POST_FIELD_LOOPS_RESPONSES + "][",
                                "]", extraDataList, qd, newDataId);
                    else
                        extraDataList = getSingleChoiceText("", "",
                                extraDataList, qd, newDataId);
                } else if (qd.getQuestionTypeLink().equals("8")
                        || qd.getQuestionTypeLink().equals("9")
                        || qd.getQuestionTypeLink().equals("12")
                        || qd.getQuestionTypeLink().equals("11")) {
                    if (loopKey != null)
                        extraDataList = getMultiChoiceText(loopKey + "["
                                        + Constants.POST_FIELD_LOOPS_RESPONSES + "][",
                                "]", extraDataList, qd, newDataId);
                    else
                        extraDataList = getMultiChoiceText("", "",
                                extraDataList, qd, newDataId);
                } else if (qd.getQuestionTypeLink().equals("4")) {
                    if (loopKey != null)
                        extraDataList = getTextBoxAnswer(loopKey + "["
                                        + Constants.POST_FIELD_LOOPS_RESPONSES + "][",
                                "]", extraDataList, qd, newDataId);
                    else
                        extraDataList = getTextBoxAnswer("", "", extraDataList,
                                qd, newDataId);
                }
            }
        }
        if (pos_shelf_item != null) {

            extraDataList = pos_shelf_item
                    .PrepareProductValuePair(extraDataList);

        }
        // else if (sqd.getTotalIntSent() <= 0
        // && !sqd.getOrderid().contains("-"))
        // return null;
        if (sqd.getOrderid() != null && sqd.getOrderid().contains("-"))
            extraDataList = Helper.convertQuotasToNameValuePairs(extraDataList,
                    sqd.getQuotas(), sqd.getOrderid().replace("-", ""), false,
                    false);

        extraDataList.add(Helper.getNameValuePair(
                Constants.POST_FIELD_UNEMPTY_QUES_COUNT,
                validateTotalSent(sqd.getTotalSent(), extraDataList,
                        pos_shelf_item != null)));
        if (isProgress)
            return extraDataList;
        if (validateTotalSent(sqd.getTotalSent(), extraDataList,
                pos_shelf_item != null).equals("0")
                && (sqd.getFtime() == null || sqd.getFtime().length() <= 0))
            return null;

        return extraDataList;
    }

    private boolean CheckResponse(String result) {
        if (!Helper.IsValidResponse(result,
                Constants.JOB_DETAIL_RESP_FIELD_PARAM)) {
            return false;
        }
        result = result.substring(
                result.indexOf(Constants.JOB_DETAIL_RESP_FIELD_PARAM),
                result.indexOf("</status>"));
        if (!(result.endsWith("0"))) {
            return false;
        } else
            return true;
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {

            String[] proj = {MediaStore.MediaColumns.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception ex) {
        }
        return null;
        // {
        // String path = contentUri.getPath();
        // path = path.replace("/storage/sdcard0/DCIM/Camera/",
        // Environment.getExternalStorageDirectory() + "/lgimageq/");
        // return path.replace("file://", "");
        // }
    }

    private String convertDataIdForNameValuePair(String dataID2) {
        // TODO Auto-generated method stub
        dataID2 = QuestionnaireActivity.cleanDataIdfromDollarSign(dataID2);
        if (dataID2.contains("^")) {
            int indexOf = dataID2.indexOf("^");
            if (indexOf >= 0) {
                dataID2 = dataID2.replace(dataID2.substring(indexOf), "");
            }
        }
        if (dataID2.contains("_")) {

            String dataId = getDataIdFromDataId(dataID2);
            String groupId = getGroupIdFromDataId(dataID2.replace(dataId + "_",
                    ""));
            if (dataID2.contains("-")) {
                String titleId = getTitleFromDataId(dataID2.replace(dataId
                        + "_" + groupId + "-", ""));
                return groupId + "-" + titleId + "-" + dataId;
            }
            return groupId + "-" + "0" + "-" + dataId;
        }
        if (dataID2.contains("$")) {
            int iOf = dataID2.indexOf("$");
            String datais = dataID2.substring(iOf);
            dataID2 = dataID2.replace(datais, "");
        }
        return dataID2;
    }

    private String getMePrefix(String loopinfo) {
        String[] loopinfos = loopinfo.split("\\^");
        String prefix = null;
        for (int i = 0; i < loopinfos.length; i++) {
            String[] thisLoopInfos = loopinfos[i].split("=");
            if (prefix == null) {
                prefix = "loops";
            } else
                prefix += "[loops]";
            if (thisLoopInfos.length > 2) {
                prefix += "[" + thisLoopInfos[0] + "][items]["
                        + thisLoopInfos[1] + "]";
            }
        }
        return prefix;
    }

    private boolean CheckResponseForStorageQuota(String result) {
        if (!Helper.IsValidResponse(result,
                Constants.JOB_DETAIL_RESP_FIELD_PARAM)) {
            return false;
        }
        if (result.contains("<2>")) {
            return true;
        } else
            return false;
    }

    private ArrayList<QuestionnaireData> getQuestionnaireData(String orderID) {
        // SQLiteDatabase db = DBAdapter.openDataBase();
        ArrayList<QuestionnaireData> questionnaireData = new ArrayList<QuestionnaireData>();
        DBAdapter.LogCommunication("checkerDBLog.txt",
                "joblist-getQuestionnaireList=");
        questionnaireData = DBHelper.getQuestionnaireList(
                Constants.DB_TABLE_QUESTIONNAIRE, new String[]{
                        Constants.DB_TABLE_QUESTIONNAIRE_DATAID,
                        Constants.DB_TABLE_QUESTIONNAIRE_QTEXT,
                        Constants.DB_TABLE_QUESTIONNAIRE_ORDERID,
                        Constants.DB_TABLE_QUESTIONNAIRE_QTL,
                        Constants.DB_TABLE_QUESTIONNAIRE_OT,
                        Constants.DB_TABLE_ANSWERS_BRANCHID,
                        Constants.DB_TABLE_ANSWERS_WORKERID,
                        Constants.DB_TABLE_QUESTIONNAIRE_FT,
                        Constants.DB_TABLE_SUBMITSURVEY_REPORTED_FINISH_TIME,
                        Constants.DB_TABLE_QUESTIONNAIRE_LoopInfo,},
                Constants.DB_TABLE_QUESTIONNAIRE_ORDERID + "=" + "\"" + orderID
                        + "\"", Constants.DB_TABLE_QUESTIONNAIRE_DATAID,
                questionnaireData);
        // DBAdapter.closeDataBase(db);
        return questionnaireData;
    }

    public double getTotalRAM() {
        double mb = 0.0;

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            mb = totRam / 1024.0;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return mb;
    }

    private List<NameValuePair> getBranchSelection(String prefix,
                                                   String postfix, List<NameValuePair> extraDataList,
                                                   QuestionnaireData qd, String newDataId) {
        if (qd.getBranchID() == null || qd.getBranchID().contains("-1")) {
        } else {
            extraDataList.add(Helper.getNameValuePair(prefix + "BranchID"
                    + postfix, qd.getBranchID()));

        }
        return extraDataList;
    }

    private List<NameValuePair> getWorkerSelection(String prefix,
                                                   String postfix, List<NameValuePair> extraDataList,
                                                   QuestionnaireData qd, String newDataId) {
        if (qd == null || qd.getWorkerID() == null
                || qd.getWorkerID().contains("-1")) {
        } else {
            extraDataList.add(Helper.getNameValuePair(prefix + "WorkerID"
                    + postfix, qd.getWorkerID()));

        }
        return extraDataList;
    }

    private List<NameValuePair> getSingleChoiceText(String prefix,
                                                    String postfix, List<NameValuePair> extraDataList,
                                                    QuestionnaireData qd, String newDataId) {
        if (qd.getAnswersList().size() > 0) {
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + postfix, qd.getAnswersList().get(0)
                    .getAnswerID()));
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-answerText1" + postfix, qd.getAnswersList()
                    .get(0).getAnswer()));
            if (qd.getFreetext() != null && !qd.getFreetext().equals("")) {
                if (qd.getMiType() != null && qd.getMiType().equals("8"))// SS
                    extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                                    + newDataId + "-mi" + postfix,
                            "00:00:" + qd.getFreetext()));
                else
                    extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                            + newDataId + "-mi" + postfix, qd.getFreetext()));
            } else {
                if (qd.getMiType() != null && qd.getMiType().equals("8")
                        && qd.getAnswerText() != null
                        && qd.getAnswerText() != "")// SS
                    extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                                    + newDataId + "-mi" + postfix,
                            "00:00:" + qd.getAnswerText()));
                else
                    extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                            + newDataId + "-mi" + postfix, qd.getAnswerText()));
            }
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                            + newDataId + "-questionText" + postfix,
                    qd.getQuestionText()));
        } else if (qd.getAnswerText() != null && !qd.getAnswerText().equals("")) {
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + postfix, ""));
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-answerText1" + postfix, ""));
            if (qd.getMiType() != null && qd.getMiType().equals("8")
                    && qd.getAnswerText() != null && qd.getAnswerText() != "")// SS
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                                + newDataId + "-mi" + postfix,
                        "00:00:" + qd.getAnswerText()));
            else
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                        + newDataId + "-mi" + postfix, qd.getAnswerText()));
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                            + newDataId + "-questionText" + postfix,
                    qd.getQuestionText()));
        } else if (qd.getFreetext() != null && !qd.getFreetext().equals("")) {
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + postfix, ""));
            // extraDataList.add(Helper.getNameValuePair("obj" + newDataId
            // + "-answerText1", ""));
            if (qd.getMiType() != null && qd.getMiType().equals("8")
                    && qd.getFreetext() != null && qd.getFreetext() != "")// SS
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                                + newDataId + "-mi" + postfix,
                        "00:00:" + qd.getFreetext()));
            else
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                        + newDataId + "-mi" + postfix, qd.getFreetext()));
            extraDataList
                    .add(Helper.getNameValuePair("obj" + newDataId + prefix
                            + "-questionText" + postfix, qd.getQuestionText()));
        }
        return extraDataList;
    }

    private List<NameValuePair> getMultiChoiceText(String prefix,
                                                   String postfix, List<NameValuePair> extraDataList,
                                                   QuestionnaireData qd, String newDataId) {
        if ((qd.getAnswerText() == null || qd.getAnswerText().equals(""))
                && (qd.getFreetext() == null || qd.getFreetext().equals(""))
                && ((qd.getAnswersList() == null) || qd.getAnswersList().size() == 0)) {
            // empty question
            return extraDataList;
        }
        extraDataList.add(Helper.getNameValuePair(prefix + "obj" + newDataId
                + "-questionText" + postfix, qd.getQuestionText()));
        if (qd.getAnswersList().size() > 0) {
            for (int i = 0; i < qd.getAnswersList().size(); i++) {
                Answers answer = qd.getAnswersList().get(i);
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                        + newDataId + postfix + "[]", answer.getAnswerID()));
                extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                                + newDataId + "-answerText" + (i + 1) + postfix,
                        answer.getAnswer()));
            }
        }

        if (qd.getFreetext() != null && !qd.getFreetext().equals("")) {
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-mi" + postfix, qd.getFreetext()));
        } else
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-mi" + postfix, qd.getAnswerText()));
        return extraDataList;
    }

    private List<NameValuePair> getTextBoxAnswer(String prefix, String postfix,
                                                 List<NameValuePair> extraDataList, QuestionnaireData qd,
                                                 String newDataId) {
        if (qd.getAnswerText() == null || qd.getAnswerText().trim().equals(""))
            return extraDataList;
        extraDataList.add(Helper.getNameValuePair(prefix + "obj" + newDataId
                + "-answerText1" + postfix, ""));
        if (qd.getAnswerText() != null)
            qd.setAnswerText(qd.getAnswerText().trim());
        extraDataList.add(Helper.getNameValuePair(prefix + "obj" + newDataId
                + "-mi" + postfix, qd.getAnswerText()));
        extraDataList.add(Helper.getNameValuePair(prefix + "obj" + newDataId
                + "-questionText" + postfix, qd.getQuestionText()));
        return extraDataList;
    }

    private String validateTotalSent(String totalSent,
                                     List<NameValuePair> extraDataList, boolean isPOS) {
        String token = "";
        int increment = 0;
        for (int i = 0; i < extraDataList.size(); i++) {
            if (extraDataList.get(i).getName().contains("obj")
                    && extraDataList.get(i).getName().contains("questionText")) {
                if (!token.equals(extraDataList.get(i).getName())) {
                    increment++;
                    token = extraDataList.get(i).getName();
                }
            } else if (extraDataList.get(i).getName().toLowerCase()
                    .startsWith("worker")
                    || extraDataList.get(i).getName().toLowerCase()
                    .startsWith("branch")) {
                increment++;
            }
        }
        // if (isPOS)
        // increment++;

        return increment + "";
    }

    private String getGroupIdFromDataId(String data_id) {

        if (data_id.contains("-")) {
            data_id = data_id.substring(0, data_id.indexOf("-"));
        }
        return data_id;
    }

    private String getDataIdFromDataId(String data_id) {

        if (data_id.contains("_")) {
            data_id = data_id.substring(0, data_id.indexOf("_"));
        }
        return data_id;
    }

    private String getTitleFromDataId(String data_id) {

        if (data_id.contains(";")) {
            data_id = data_id.substring(0, data_id.indexOf(";"));
        }
        return data_id;
    }

    public void someMethod(String str) {
        new JobTask().execute(str, "");
        try {
            if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                mMap.clear();
                LongOperation op = new LongOperation();
                op.execute();
                joborders.clear();
                setMapView(joborders);
                // add markers from database to the map
            }
        } catch (Exception e) {
        }
    }

    private void loadOfflineJobs(boolean login_check) {
        ShowDBJobs();

        if (login_check) {
            if (IsInternetConnectted()) {
                int size = DBHelper.getLanguages(false).size();
                if (size == 0)
                    executeLangList();
                else
                    executeJobList(false, false);
            }
        } else if (joborders == null
                || joborders.size() == 0
                || (getIntent() != null && getIntent().getExtras() != null && getIntent()
                .getExtras().getBoolean(
                        Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY))) {
            if (IsInternetConnectted()) {
                int size = DBHelper.getLanguages(false).size();
                if (size == 0)
                    executeLangList();
                else
                    executeJobList(false, false);
            }
        }
    }

}
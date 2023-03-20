package com.mor.sa.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.InProgressAnswersData;
import com.checker.sa.android.data.Lists;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Sets;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.data.pngItem;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowJobInMapViewActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    private GoogleMap mMap;
    private SupportMapFragment mapfragment;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<String> locationTitle;

    public static ArrayList<orderListItem> joborders;
    ArrayList<Order> jobordersss = null;

    private HashMap<Marker, orderListItem> markersHash;
    private String orderid;
    String title;

    private CardView cardView;
    private ImageView iv_close, ivLocation, iv_map_list, iv_language;
    ListView lvjdsurvey_quotas_list, lvjdsurvey_allocations_list;
    LinearLayout llSchedulesExpandLayout, llAssignedExpandLayout, llStartJob;
    View view1;
    TextView tv_show_status, tvReject, tvAccept, tv, tvBranchFullName, tvBranch, tvCount, tvDec, tvLocation, tvSurveyName, tvClientDesc, tvQuestionnaire, tvBranch_s, tvBranchFullName_s, tvCity, tvAddress, tvBranchPhone, tvOpeningHours, tvStartTime, tvEndTime, tvPurchaseDescription, tvMakePurchase, tvNonRefundableServicePayment, tvTransportationPayment, tvCriticismPayment, tvBonusPayment, tvStatus;

    private final int QUESTIONNAIRE_ACTIVITY_CODE = 1;

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

        locationArrayList = new ArrayList<>();
        locationTitle = new ArrayList<>();

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

        joborders = Constants.orderList;

        try {
            for (int i = 0; i < jobordersss.size(); i++) {
                joborders.add(new orderListItem(jobordersss.get(i), null));
            }


            Double[] latitude = new Double[jobordersss.size()];
            Double[] longitude = new Double[jobordersss.size()];

            for (int i = 0; i < jobordersss.size(); i++) {

                latitude[i] = parseDouble(jobordersss.get(i).getBranchLat());
                longitude[i] = parseDouble(jobordersss.get(i).getBranchLong());
                locationArrayList.add(new LatLng(latitude[i], longitude[i]));
                locationTitle.add(jobordersss.get(i).getBranchFullname());
                title = jobordersss.get(i).getBranchFullname();

            }

        } catch (Exception e) {

        }

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

        initGoogleApiClient();

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

    private double parseDouble(String s) {
        if (s == null || s.isEmpty())
            return -34;
        else
            return Double.parseDouble(s);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

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
                MarkerOptions markerOptions = new MarkerOptions();

                double latitude = Double.valueOf(orders.get(i).orderItem.getBranchLat());
                double longitude = Double.valueOf(orders.get(i).orderItem.getBranchLong());
                LatLng latlng = new LatLng(latitude, longitude);
                markerOptions.position(latlng);
//                markerOptions.title(latlng.latitude + " : " + latlng.longitude);
                markerOptions.title(orders.get(i).orderItem.getBranchFullname());

                if (orders.get(i).orderItem.getStatusName().equals("Assigned")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.assigned_job_location));
                } else if (orders.get(i).orderItem.getStatusName().equals("Scheduled")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.scheduled_job_location));
                } else if (orders.get(i).orderItem.getStatusName().equals("Completed")) {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.completed_job_location));
                } else {
                    markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.in_progress_job_location));
                }

                statusname = orders.get(i).orderItem.getStatusName();

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
            Log.e("getStatusName", thiItem.orderItem.getStatusName());
        } else {
            llStartJob.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.GONE);
            Log.e("getStatusName_else", thiItem.orderItem.getStatusName());
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
                    String get_location = String.valueOf(((JobListActivity) con).CalculationByDistance(Constants.user_location_latitude, location_latitude, Constants.user_location_longitude, location_longitude));
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
                    new JobTask().execute(getString(R.string.jd_accept_btn_text),
                            "");
                    // aceeptbtn.setBackgroundResource(R.drawable.acceptbtn_n);
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

                if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert")) {
                    BeginReview(true, thiItem);

                } else if (thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().toLowerCase().equals("archived")) {
                    BeginReview(true, thiItem);
                }

//                if (thiItem.orderItem.getOrderID().contains("-")) {
//                    Intent intent = new Intent(getApplicationContext(),
//                            QuestionnaireActivity.class);
//                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
//                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
//
//                    if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert"))
//                        startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                    else
//                        startActivity(intent);
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
//                        startActivity(intent);
//                }
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
                Constants.screen_type_dialog = 0;
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
//        double lat1 = StartP.latitude;
//        double lat2 = EndP.latitude;
//        double lon1 = StartP.longitude;
//        double lon2 = EndP.longitude;
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

    Survey survey;
    boolean isSurvey;

    private void setOrder(orderListItem thiItem) {
        Bundle b = getIntent().getExtras();

        if (b == null)
            return;

//        String OrderID = b.getString("OrderID");
//        String SurveyID = b.getString("SurveyID");

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
            // order = Orders.getOrders().get(index);
//            if (survey != null)
//                setValueFieldText(survey);
            isBriefing = false;
        } else {
            for (int i = 0; i < Orders.getOrders().size(); i++) {
                thiItem.orderItem = Orders.getOrders().get(i);
                if (thiItem.orderItem != null && thiItem.orderItem.getOrderID().equals(OrderID))
                    break;
            }
            // order = Orders.getOrders().get(index);
            if (thiItem.orderItem != null) {
                isSurvey = OrderID.contains("-");
//                setValueFieldText(isSurvey);

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
        comunicator.JobList = ShowJobInMapViewActivity.this;
        switch (requestCode) {
            case (QUESTIONNAIRE_ACTIVITY_CODE):
                if (data != null && data.hasExtra("from_watch")) {
                    Intent intent = new Intent();
                    intent.putExtra("from_watch", true);
                    setResult(2, intent);
                    finish();
                }
                break;
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
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        TextView textView = new TextView(context);
//        setFontSize(textView);
        textView.setText(message);
        alert.setView(textView);

        // alert.setMessage(message);
        alert.setPositiveButton(button_lbl,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
        alert.show();
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

            Log.e("Job_doInBackground", cardItemDetails.orderItem.getOrderID());

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
//				if (result.contains("<script>")) {
//
//					result = AcceptJob();
//				}
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
            // DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE, where, null);
            // DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_POS, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_ORDERS, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_SUBMITSURVEY, where, null);
            DBAdapter.openDataBase();
            DBAdapter.db.delete(Constants.DB_TABLE_JOBLIST, where, null);

            // exitAfterSubmitSurveyOrExitandsave(2);
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
                    LoginActivity.class);
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
            // for(int i=0;i<Sets.getSets().size();i++)
            // {
            // Set set = Sets.getSets().get(i);
            // int size = set.getListObjects().size();
            // Log.v("Size= ", ""+size);
            // }
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
                JobListActivity.setAlternateURL(newUrlForEUClients, myPrefs);
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
            // ShowAlert(JobListActivity.this,
            // JobListActivity.this.getString(R.string.alert_working),
            // JobListActivity.this.getString(R.string.alert_working_msg),
            // JobListActivity.this.getString(R.string.button_ok));
        }
    }

    private String removeGarbageFromTop(String thisSet) {
//        if (thisSet!=null && thisSet.trim().toLowerCase().startsWith("<?xml version"))
//            return thisSet;
//        thisSet=thisSet.substring(thisSet.indexOf("<?xml version"));
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
            // String status = getStatusByOrderID(order.getOrderID(),
            // ordersArr);
            // if(!status.equals(""))
            // temporder.get(index).setStatusName(status);
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
//                Revamped_Loading_Dialog.show_dialog(ShowJobInMapViewActivity.this,
//                        getResources().getString(R.string.alert_switching));
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (joborders != null && joborders.size() >= 1) {
                Orders.setListOrders(jobordersss);
                Orders.setBranchProps(branchProps);
                Surveys.setSets(DBHelper.getSurveyyRecords());
                setOrderList();
                // setSurveyList'();
                // seting list here
//                showDbjobsPostPart();
                // ManageTabs(1);
            }

//            if (pngItems != null && pngItems.size() > 0
//                    || isBranchPropErr == true) {
//                if (btnErr != null)
//                    btnErr.setVisibility(RelativeLayout.VISIBLE);
//            } else {
//                if (btnErr != null)
//                    btnErr.setVisibility(RelativeLayout.GONE);
//            }

            Revamped_Loading_Dialog.hide_dialog();
//            cleanUploaedJobsHere(null);

//            TODO JobDetailActivity
//            if (certorderid != null) {
//
//                Intent intent = new Intent(
//                        JobListActivity.this.getApplicationContext(),
//                        JobDetailActivity.class);
//                isJobselected = true;
//                intent.putExtra("OrderID", certorderid);
//                callJobDetail(intent, JOB_DETAIL_ACTIVITY_CODE);
//
//            }

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

            String userName = myPrefs.getString(
                    Constants.POST_FIELD_LOGIN_USERNAME, "");
            // if (Helper.lThread == null) {
            // Helper.lThread = new locationThread();
            // Helper.lThread.isPost = true;
            // } else {
            // Helper.lThread.isPost = true;
            // }
            // Helper.lThread.startLocationThread(getApplicationContext(),
            // userName);
            for (int i = 0; i < joborders.size(); i++) {
                if (joborders.get(i).orderItem.getOrderID()
                        .equals(thiItem.orderItem.getOrderID())) {
                    joborders.get(i).orderItem
                            .setStatusName("Scheduled");
                }
            }

            BeginReview(false, thiItem);

        }
        // else if (locationManager
        // .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        //
        // }
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

                                    // Sent user to GPS settings screen
                                    // final ComponentName toLaunch = new
                                    // ComponentName("com.android.settings","com.android.settings.SecuritySettings");
                                    // final Intent intent = new
                                    // Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    // intent.setComponent(toLaunch);
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    // startActivityForResult(intent, 1);

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
                                    // locationThread lThread = new
                                    // locationThread();
                                    // lThread.startLocationThread(
                                    // getApplicationContext(), userName);
                                    // JobListActivity.joborders.get(localindex).orderItem
                                    // .setStatusName("Scheduled");
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
            if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert"))
                startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
            else
                startActivity(intent);
        } else {
            Intent intent = new Intent(this.getApplicationContext(),
                    QuestionnaireActivity.class);
            if (thiItem.orderItem == null)
                setOrder(thiItem);
            intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
                    thiItem.orderItem.getOrderID());
            intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
            if (thiItem.orderItem.getStatusName().equals("Completed") || thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().equals("archived"))
                startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
            else
                startActivity(intent);
        }
    }

}
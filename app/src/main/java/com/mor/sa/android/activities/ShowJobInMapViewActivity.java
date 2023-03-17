package com.mor.sa.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.RefusalReasonDialog;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowJobInMapViewActivity extends FragmentActivity implements OnMapReadyCallback {

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
    private ImageView iv_close, ivLocation, iv_map_list;
    ListView lvjdsurvey_quotas_list, lvjdsurvey_allocations_list;
    LinearLayout llSchedulesExpandLayout, llAssignedExpandLayout, llStartJob;
    View view1;
    TextView tvReject, tvAccept, tv, tvBranchFullName, tvBranch, tvCount, tvDec, tvLocation, tvSurveyName, tvClientDesc, tvQuestionnaire, tvBranch_s, tvBranchFullName_s, tvCity, tvAddress, tvBranchPhone, tvOpeningHours, tvStartTime, tvEndTime, tvPurchaseDescription, tvMakePurchase, tvNonRefundableServicePayment, tvTransportationPayment, tvCriticismPayment, tvBonusPayment, tvStatus;

    private final int QUESTIONNAIRE_ACTIVITY_CODE = 1;

    orderListItem cardItemDetails;

    private ArrayList<Cert> pendingCerts = null;

    private static jobBoardCertsListener certCallBack;

    SharedPreferences myPrefs;

    public int msgId;

    private boolean refreshJoblist;
    private String groupedNumber;
    private int serverGroupedNumber;


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

        ivLocation = (ImageView) findViewById(R.id.iv_location);
        iv_map_list = (ImageView) findViewById(R.id.iv_map_list);
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapView(JobListActivity.joborders);
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
                            // TODO Auto-generated method stub
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

                if (thiItem.orderItem.getOrderID().contains("-")) {
                    Intent intent = new Intent(getApplicationContext(),
                            QuestionnaireActivity.class);
                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());

                    if (thiItem.orderItem.getStatusName().equals("Scheduled") || thiItem.orderItem.getStatusName().equals("cert"))
                        startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
                    else
                        startActivity(intent);

                } else {
                    Intent intent = new Intent(getApplicationContext(),
                            QuestionnaireActivity.class);
                    if (thiItem.orderItem == null)
                        setOrder(thiItem);
                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
                    if (thiItem.orderItem.getStatusName().equals("Completed") || thiItem.orderItem.getStatusName().equals("in progress") || thiItem.orderItem.getStatusName().equals("In progress") || thiItem.orderItem.getStatusName().equals("archived"))
                        startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
                    else
                        startActivity(intent);
                }
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
//        jobListTaskHandler = new JobListActivity.JobbListTask(false, false);
//        jobListTaskHandler.execute();
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

}
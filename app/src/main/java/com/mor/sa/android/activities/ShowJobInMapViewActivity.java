package com.mor.sa.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.UIHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
        tvAccept = (TextView) findViewById(R.id.tv_accept);

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
                llStartJob.setVisibility(View.GONE);
                tvAccept.setVisibility(View.VISIBLE);
            }
        });

        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llStartJob.setVisibility(View.VISIBLE);
                tvAccept.setVisibility(View.GONE);
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


                Log.e("jobordersss", latitude[i].toString());
                Log.e("jobordersss", longitude[i].toString());
                Log.e("jobordersss", title.toString());
            }

        } catch (Exception e) {

        }

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

                double latitude = Double.valueOf(orders.get(i).orderItem
                        .getBranchLat());
                double longitude = Double.valueOf(orders.get(i).orderItem
                        .getBranchLong());
                LatLng latlng = new LatLng(latitude, longitude);
                markerOptions.position(latlng);
//                markerOptions.title(latlng.latitude + " : " + latlng.longitude);
                markerOptions.title(orders.get(i).orderItem.getBranchFullname());
                markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.show_job_location));

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

        //Assigned tab data
        if (thiItem.orderItem.getOrderID().contains("-")) {
            Survey s = Surveys.getCurrentSurve(thiItem.orderItem.getOrderID().replace(
                    "-", ""));
            if (s != null && s.getSurveyName() != null) {
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

            tvBranch.setVisibility(View.GONE);
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

//            tvReject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                        Toast.makeText(con,
////                                "You are click on scheduled reject button ",
////                                Toast.LENGTH_LONG).show();
//
//                    if (thiItem.orderItem != null
//                            && thiItem.orderItem.getAllowShoppersToRejectJobs() != null
//                            && thiItem.orderItem.getAllowShoppersToRejectJobs().equals("2")) {
//                        Toast.makeText(con,
//                                "You are not allowed to reject jobs.",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    if (thiItem.orderItem != null
//                            && thiItem.orderItem.getAllowShoppersToRejectJobs() != null
//                            && thiItem.orderItem.getAllowShoppersToRejectJobs().equals("1")
//                            && !thiItem.orderItem.getStatusName().equals("Assigned")) {
//
//                        Toast.makeText(con,
//                                "You are not allowed to reject this job.",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    RefusalReasonDialog dialog = new RefusalReasonDialog(con);
//                    dialog.show();
//                }
//            });

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

                if (thiItem.orderItem.getOrderID().contains("-")) {
//                        Toast.makeText(con, "Assigned tab", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(con.getApplicationContext(),
                            QuestionnaireActivity.class);
                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, thiItem.orderItem.getOrderID());
                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
//                        if (isFromWatch)
//                            con.startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                        else
                    con.startActivity(intent);
                } else if (thiItem.orderItem != null && thiItem.orderItem.getSetName() != null
                        && !thiItem.orderItem.getSetName().equals("")) {
//                        Toast.makeText(con, "Scheduled tab", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(con.getApplicationContext(),
                            QuestionnaireActivity.class);
                    if (thiItem.orderItem == null)
                        setOrder(thiItem, con);
                    intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
                            thiItem.orderItem.getOrderID());
                    intent.putExtra(Constants.FIELD_ORDER_SET_ID, thiItem.orderItem.getSetID());
//                        if (isFromWatch)
//                            startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
//                        else
                    con.startActivity(intent);

                } else {
//                        Toast.makeText(con, "Other tab", Toast.LENGTH_SHORT).show();
                }
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

    private void setOrder(orderListItem thiItem, Context con) {
        Bundle b = ((Activity) con).getIntent().getExtras();

        if (b == null)
            return;

        String OrderID = b.getString("OrderID");
        String SurveyID = b.getString("SurveyID");

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

}
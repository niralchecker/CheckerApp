package com.google.maps.android;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import phlox.datepick.CalendarNumbersView.DateSelectionListener;
import phlox.datepick.CalendarPickerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.adapter.JobBoardListAdapter;
import com.checker.sa.android.adapter.JobBoardlistAdapter_CallBack;
import com.checker.sa.android.data.Bounds;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.CountryLatLng;
import com.checker.sa.android.data.Job;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.DateTVListener;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.MultiSelectionSpinner;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.LocationGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.LoginActivity;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.comunicator;

import static android.widget.Toast.makeText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class JobBoardActivityFragment extends FragmentActivity {
    private static final int JOB_DETAIL_ACTIVITY_CODE = 321;
    private GoogleMap googleMap;
    private HashMap<Marker, Job> markersHash;
    private String orderid;
    private String name_of_file;
    CalendarPickerView pickerView;
    private String gStartDate = "2016-12-26";
    private String gEndDate = "2016-12-29";
    RelativeLayout today;
    RelativeLayout nextSevenDays;
    RelativeLayout custom;
    RelativeLayout refresh;
    RelativeLayout back;
    RelativeLayout filter;
    RelativeLayout branchTxtView;
    RelativeLayout branchTxtCodeView;
    RelativeLayout branchView;
    RelativeLayout clientTxtView;
    RelativeLayout mapOrList;
    ScrollView filterLayout;
    RadioButton radioButton;
    TextView ttoday;
    TextView tnextSevenDays;
    TextView tcustom;
    View customBottom;
    View nextSevenBottom;
    View todayBottom;
    View ClientTxtBottom;
    View branchTxtBottom;
    View branchPropsBottom;
    View branchTxtCodeBottom;
    public CheckBox chkbox;
    boolean refreshDate = false;

    JobBoardlistAdapter_CallBack jobBoardlistAdapter_CallBack;
    ListView jobList;
    View mapOrListBtn;
    TextView tvMap;
    TextView tvList;
    TextView branchFilterT;
    TextView branchPropsFilterT;
    TextView branchCodeFilterT;
    TextView ClientFilterT;
    LatLng NorthEastCorner;
    LatLng SouthWestCorner;
    ImageView locationBtn;
    RelativeLayout ClientFilter;
    RelativeLayout customLayout;
    RelativeLayout emptyListLabel;
    // RadioGroup radioFilter;
    private boolean isMovedAlready = false;
    private static final int JOB_GPS_CODE = 1234;
    double lat1 = -1000;
    double lng1 = -1000;
    double lat2 = 1000;
    double lng2 = 1000;
    private ArrayList<Bounds> dataList;
    protected LocationManager locationManager;
    private boolean map_list = true;
    private boolean show_hide_filter = true;
    private Location thisPersonLocaation = null;
    private RelativeLayout branchFilter;
    private MultiSelectionSpinner multipleClientSpinner;
    private MultiSelectionSpinner multipleBranchSpinner;
    private MultiSelectionSpinner multipleBranchCodeSpinner;
    private MultiSelectionSpinner multiplePropsSpinner;
    private Button btnApply;
    private Button btnCancel;
    private boolean isListEmpty = true;
    private Marker thisPersonMarker = null;
    private RelativeLayout bottomBar;
    private Button btnApplyAll;
    private RelativeLayout branchCodeFilter;
    private RelativeLayout propsFilter;
    private RelativeLayout branchTxtPropsView;

    private static jobBoardCertsListener jobboardListener;
    private FrameLayout framView;
    private RelativeLayout layout_map;
    private SupportMapFragment mapfragment;
    private Location currentLot;
    private int resultCode;

    ImageView iv_list, iv_map;

    private ArrayList<Job> result_filter;

    TextView tv_select_all, tv_clearer_all, tv_remove_selected;

    CheckBox checkbox_today, checkbox_next_day;
    TextView s_date, e_date,tv_clients;

    public static void setJobBardCallback(jobBoardCertsListener dateCallback) {
        JobBoardActivityFragment.jobboardListener = dateCallback;
    }

    int getIcon(String iconName) {
        int newResImgId = 0;
        int ResImgId = this.getResources().getIdentifier(iconName, "drawable",
                this.getPackageName());
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)) {
            String temp_iconName = Helper.imgprefix + iconName;
            newResImgId = this.getResources().getIdentifier(temp_iconName,
                    "drawable", this.getPackageName());
            if (newResImgId == 0) {
                newResImgId = this.getResources().getIdentifier(iconName,
                        "drawable", this.getPackageName());
            }
            return newResImgId;
        }
        newResImgId = this.getResources().getIdentifier(iconName, "drawable",
                this.getPackageName());
        return ResImgId;
    }

    public void onStartDevicePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasrecordaudio = this.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO);
            int hasaccessc = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            //int hasaccfinelocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //int hasaccesslocationextracommand = this.checkSelfPermission(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            int hasaccessnetworkstate = this.checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
            int hasintermission = this.checkSelfPermission(android.Manifest.permission.INTERNET);
            int hascamera = this.checkSelfPermission(android.Manifest.permission.CAMERA);
            //int haswriteexternalstorage = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasreadexternalstorage = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int haswakelock = this.checkSelfPermission(android.Manifest.permission.WAKE_LOCK);


            List<String> permissions = new ArrayList<String>();

            if (hasrecordaudio != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.RECORD_AUDIO);

            }

            if (hasaccessc != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

            }
//			if (hasaccfinelocation != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//			}
//			if (hasaccesslocationextracommand != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
//
//			}
            if (hasaccessnetworkstate != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);

            }

            if (hasintermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.INTERNET);

            }
            if (hascamera != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.CAMERA);

            }

//			if (haswriteexternalstorage != PackageManager.PERMISSION_GRANTED) {
//				permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//			}
            //
            if (hasreadexternalstorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (haswakelock != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WAKE_LOCK);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }
    }


    private AutoCompleteTextView actv;
    private RelativeLayout imgarrow;
    private LinearLayoutCompat ll_show_jobs_counts, ll_select_clear;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardfragmentview);
        dataList = new ArrayList<Bounds>();
        bottomBar = (RelativeLayout) findViewById(R.id.bottombar);
        imgarrow = (RelativeLayout) findViewById(R.id.downarrowlayout);
        chkbox = (CheckBox) findViewById(R.id.chkBox);
        tv_select_all = findViewById(R.id.tv_select_all);
        tv_clearer_all = findViewById(R.id.tv_clearer_all);
        tv_remove_selected = findViewById(R.id.tv_remove_selected);
        tv_clients = findViewById(R.id.tv_clients);

        tv_select_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null)
                    adapter.setUnsetCheckBoxes(true);
            }
        });

        tv_clearer_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null)
                    adapter.setUnsetCheckBoxes(false);
            }
        });

        tv_remove_selected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null)
                    adapter.setUnsetCheckBoxes(false);
            }
        });

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (adapter != null)
                    adapter.setUnsetCheckBoxes(isChecked);
            }
        });
        customLayout = (RelativeLayout) findViewById(R.id.customlayout);
        actv = (AutoCompleteTextView) findViewById(R.id.countrybox);
        String[] countries = parseCountriesList();
        ArrayAdapter<String> adappter = new ArrayAdapter<String>
                (this, R.layout.custom_spinner_row_three, countries);
        actv.setAdapter(adappter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = actv.getEditableText().toString();
                position = getIndexofCountry(s);
                LatLng currentLocation = new LatLng(completeListOfCountries.get(position).getLat(), completeListOfCountries.get(position).getLng());
                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(
                        currentLocation, 6);
                googleMap.moveCamera(center);
                CameraChanged(googleMap.getCameraPosition(),
                        currentLocation);
            }
        });
        imgarrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actv.isShown()) actv.dismissDropDown();
                else actv.showDropDown();
            }
        });
        btnApply = (Button) findViewById(R.id.btnApply);
        btnApply.setTag("Apply");
        btnApplyAll = (Button) findViewById(R.id.btnApplyAll);
        btnApplyAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listOFOrders != null && listOFOrders.size() > 0) {
                    openDialog(null, null);
                }
            }
        });
        btnCancel = (Button) findViewById(R.id.btnCancel);
        today = (RelativeLayout) findViewById(R.id.today);
        ttoday = (TextView) findViewById(R.id.ttoday);
        branchFilterT = (TextView) findViewById(R.id.branchFilterT);
        branchCodeFilterT = (TextView) findViewById(R.id.branchCodeFilterT);
        branchPropsFilterT = (TextView) findViewById(R.id.branchTxtPropsFilterT);
        ClientFilterT = (TextView) findViewById(R.id.ClientFilterT);
        tnextSevenDays = (TextView) findViewById(R.id.tnextSevenDays);
        tcustom = (TextView) findViewById(R.id.tcustom);
        customBottom = (View) findViewById(R.id.customBottom);
        nextSevenBottom = (View) findViewById(R.id.nextSevenBottom);
        todayBottom = (View) findViewById(R.id.todayBottom);
        ClientTxtBottom = (View) findViewById(R.id.ClientTxtBottom);
        branchTxtBottom = (View) findViewById(R.id.branchTxtBottom);
        branchTxtCodeBottom = (View) findViewById(R.id.branchCodeTxtBottom);
        branchPropsBottom = (View) findViewById(R.id.branchTxtPropsBottom);
        branchPropsBottom.setVisibility(RelativeLayout.INVISIBLE);
        nextSevenDays = (RelativeLayout) findViewById(R.id.nextSevenDays);
        custom = (RelativeLayout) findViewById(R.id.custom);
        refresh = (RelativeLayout) findViewById(R.id.refresh);
        back = (RelativeLayout) findViewById(R.id.back);
        checkbox_today = (CheckBox) findViewById(R.id.checkbox_today);
        checkbox_next_day = (CheckBox) findViewById(R.id.checkbox_next_day);

        pickerView = new CalendarPickerView(JobBoardActivityFragment.this);
        jobList = (ListView) findViewById(R.id.jobList);
        mapOrList = (RelativeLayout) findViewById(R.id.mapOrList);
        mapOrListBtn = (View) findViewById(R.id.mapOrListBtn);
        mapOrListBtn = (View) findViewById(R.id.mapOrListBtn);
        tvMap = (TextView) findViewById(R.id.tvMap);
        tvList = (TextView) findViewById(R.id.tvList);
        locationBtn = (ImageView) findViewById(R.id.LocationBtn);
        iv_list = (ImageView) findViewById(R.id.iv_list);
        iv_map = (ImageView) findViewById(R.id.iv_map);
        emptyListLabel = (RelativeLayout) findViewById(R.id.emptyListLabel);


        locationBtn.setVisibility(RelativeLayout.GONE);
        filter = (RelativeLayout) findViewById(R.id.filter);
        branchTxtView = (RelativeLayout) findViewById(R.id.branchTxtView);
        branchTxtCodeView = (RelativeLayout) findViewById(R.id.branchTxtCodeView);
        clientTxtView = (RelativeLayout) findViewById(R.id.clientTxtView);
        branchTxtPropsView = (RelativeLayout) findViewById(R.id.branchTxtPropsView);
        filterLayout = (ScrollView) findViewById(R.id.filterLayout);
        // radioFilter = (RadioGroup) findViewById(R.id.radioFilter);
        pickerView.setVisibility(RelativeLayout.GONE);
//        tnextSevenDays.setTextColor(Color.parseColor("#007BFF"));
        custom.setBackgroundColor(Color.parseColor("#ffffff"));
        nextSevenDays.setBackgroundColor(Color.parseColor("#ffffff"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        gStartDate = dateFormat.format(cal.getTime());
        cal.add(cal.DATE, 30);
        gEndDate = dateFormat.format(cal.getTime());

        ImageView backbtn = (ImageView) findViewById(R.id.backBtn);
//		backbtn.setImageDrawable(getResources().getDrawable(
//				getIcon("prev_btnn")));

        ImageView refresh = (ImageView) findViewById(R.id.refreshBtn);
        refresh.setImageDrawable(getResources().getDrawable(
                getIcon("img_in_progress_u")));

        Helper.changeTxtViewColor((TextView) findViewById(R.id.tvMap));
        Helper.changeTxtViewColor((TextView) findViewById(R.id.tvList));
        Helper.changeTxtViewColor(ClientFilterT);
        Helper.changeTxtViewColor(branchFilterT);
        Helper.changeTxtViewColor(branchCodeFilterT);
        Helper.changeTxtViewColor(branchPropsFilterT);
        Helper.changeTxtViewColor(ttoday);
        Helper.changeTxtViewColor(tcustom);
        Helper.changeTxtViewColor(tnextSevenDays);
        Helper.changeTxtViewColor((TextView) findViewById(R.id.tvList));
        Helper.changeTxtViewColor((TextView) findViewById(R.id.tvMap));
        Helper.changeTxtViewColor((TextView) findViewById(R.id.tvList));

        Helper.changeViewColor(findViewById(R.id.mapOrListBtn));
        Helper.changeViewColor(ClientTxtBottom);
        Helper.changeViewColor(branchTxtBottom);
        Helper.changeViewColor(branchTxtCodeBottom);
        Helper.changeViewColor(branchPropsBottom);
        Helper.changeViewColor(customBottom);
        Helper.changeViewColor(nextSevenBottom);
        Helper.changeViewColor(todayBottom);

        Helper.changeViewColor(btnApply);
        Helper.changeViewColor(btnCancel);
        //	onStartDevicePermissions();

        jobBoardlistAdapter_CallBack = new JobBoardlistAdapter_CallBack() {

            @Override
            public void PassIndex(int index, Marker arg0) {
                // TODO Auto-generated method stub

                int id2 = index;
                // Toast.makeText(getApplicationContext(), id2+"", 400).show();
                openDialog(adapter.getValues().get(id2), adapter.getValues()
                        .get(id2).getM());

            }
        };
        pickerView.setListener(new DateSelectionListener() {

            @Override
            public void onDateSelected(Calendar selectedDate,
                                       Calendar enddCalendar) {
                if (selectedDate != null && enddCalendar != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd", Locale.ENGLISH);
                    gStartDate = dateFormat.format(selectedDate.getTime());
                    gEndDate = dateFormat.format(enddCalendar.getTime());
                    // cal API here
                    // set both dates in variables above after changing format
                    // refresh_submit(false);
                    selectedDate = null;
                    enddCalendar = null;
                }
            }
        });

        // today
        today.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.setVisibility(RelativeLayout.GONE);
                customLayout.setVisibility(RelativeLayout.GONE);
                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    ttoday.setTextColor(Color.parseColor(Helper.appColor));
                } else
                    ttoday.setTextColor(Color.parseColor("#007BFF"));
                tcustom.setTextColor(Color.parseColor("#000000"));
                tnextSevenDays.setTextColor(Color.parseColor("#000000"));
                customBottom.setVisibility(View.GONE);
//                nextSevenBottom.setVisibility(View.GONE);
//                todayBottom.setVisibility(View.VISIBLE);
                checkbox_today.setChecked(true);
                checkbox_next_day.setChecked(false);

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                refreshDate = false;
                if (gStartDate == null
                        || !gStartDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gStartDate = dateFormat.format(cal.getTime());
                if (gEndDate == null
                        || !gEndDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gEndDate = dateFormat.format(cal.getTime());
                Log.e("gStartDate_today", gStartDate);
                Log.e("gEndDate_today", gEndDate);

                // refresh_submit(false);
            }
        });

        checkbox_today.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    ttoday.setTextColor(Color.parseColor(Helper.appColor));
                } else
                    ttoday.setTextColor(Color.parseColor("#007BFF"));
                tcustom.setTextColor(Color.parseColor("#000000"));
                tnextSevenDays.setTextColor(Color.parseColor("#000000"));
                customBottom.setVisibility(View.GONE);
//                nextSevenBottom.setVisibility(View.GONE);
//                todayBottom.setVisibility(View.VISIBLE);
                checkbox_today.setChecked(true);
                checkbox_next_day.setChecked(false);

                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                refreshDate = false;
                if (gStartDate == null
                        || !gStartDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gStartDate = dateFormat.format(cal.getTime());
                if (gEndDate == null
                        || !gEndDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gEndDate = dateFormat.format(cal.getTime());
                Log.e("gStartDate_today_checkBox", gStartDate);
                Log.e("gEndDate_today_checkBox", gEndDate);

            }
        });
        // +7days
        nextSevenDays.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                customLayout.setVisibility(RelativeLayout.GONE);
                ttoday.setTextColor(Color.parseColor("#000000"));
                tcustom.setTextColor(Color.parseColor("#000000"));
                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    tnextSevenDays.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    tnextSevenDays.setTextColor(Color.parseColor("#007BFF"));

                customBottom.setVisibility(View.GONE);
//                nextSevenBottom.setVisibility(View.VISIBLE);
                todayBottom.setVisibility(View.GONE);
                checkbox_today.setChecked(false);
                checkbox_next_day.setChecked(true);
                pickerView.setVisibility(RelativeLayout.GONE);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                refreshDate = false;
                if (gStartDate == null
                        || !gStartDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gStartDate = dateFormat.format(cal.getTime());
                cal.add(cal.DATE, 30);
                if (gEndDate == null
                        || !gEndDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gEndDate = dateFormat.format(cal.getTime());
                // refresh_submit(false);
                Log.e("gStartDate_nextSevenDays", gStartDate);
                Log.e("gEndDate_nextSevenDays", gEndDate);
            }
        });

        checkbox_next_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ttoday.setTextColor(Color.parseColor("#000000"));
                tcustom.setTextColor(Color.parseColor("#000000"));
                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    tnextSevenDays.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    tnextSevenDays.setTextColor(Color.parseColor("#007BFF"));

                customBottom.setVisibility(View.GONE);
//                nextSevenBottom.setVisibility(View.VISIBLE);
                todayBottom.setVisibility(View.GONE);
                checkbox_today.setChecked(false);
                checkbox_next_day.setChecked(true);
                pickerView.setVisibility(RelativeLayout.GONE);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                refreshDate = false;
                if (gStartDate == null
                        || !gStartDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gStartDate = dateFormat.format(cal.getTime());
                cal.add(cal.DATE, 30);
                if (gEndDate == null
                        || !gEndDate.equals(dateFormat.format(cal.getTime())))
                    refreshDate = true;
                gEndDate = dateFormat.format(cal.getTime());
                // refresh_submit(false);
                Log.e("gStartDate_nextSevenDays_checkBox", gStartDate);
                Log.e("gEndDate_nextSevenDays_checkBox", gEndDate);
            }
        });
        // custom
        custom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.setVisibility(RelativeLayout.VISIBLE);
                ttoday.setTextColor(Color.parseColor("#000000"));
                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    tcustom.setTextColor(Color.parseColor(Helper.appColor));
                } else
                    tcustom.setTextColor(Color.parseColor("#007BFF"));

                tnextSevenDays.setTextColor(Color.parseColor("#000000"));
                customBottom.setVisibility(View.VISIBLE);
//                nextSevenBottom.setVisibility(View.GONE);
                todayBottom.setVisibility(View.GONE);
                checkbox_today.setChecked(false);
                checkbox_next_day.setChecked(false);
                customLayout.setVisibility(RelativeLayout.VISIBLE);
                refreshDate = true;
            }
        });
        locationBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng latlng = new LatLng(thisPersonLocaation.getLatitude(),
                        thisPersonLocaation.getLongitude());
                CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }
        });
        filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClientFilter == null || branchFilter == null)
                    return;
                showhide_filter();
//                showFilterDialog(JobBoardActivityFragment.this);
            }
        });
        clientTxtView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    customLayout.setVisibility(RelativeLayout.GONE);
                }
                branchFilterT.setTextColor(Color.parseColor("#000000"));
                branchCodeFilterT.setTextColor(Color.parseColor("#000000"));
                branchPropsFilterT.setTextColor(Color.parseColor("#000000"));
                ClientFilterT.setTextColor(Color.parseColor("#000000"));

                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    ClientFilterT.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    ClientFilterT.setTextColor(Color.parseColor("#007BFF"));

                ClientTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtCodeBottom.setVisibility(RelativeLayout.VISIBLE);
                branchPropsBottom.setVisibility(RelativeLayout.VISIBLE);
                propsFilter.setVisibility(RelativeLayout.VISIBLE);
                // branchPropsFilterT.setVisibility(RelativeLayout.VISIBLE);
                ClientFilter.setVisibility(RelativeLayout.VISIBLE);
                branchCodeFilter.setVisibility(RelativeLayout.VISIBLE);
                branchFilter.setVisibility(RelativeLayout.VISIBLE);
            }
        });

        branchTxtPropsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    customLayout.setVisibility(RelativeLayout.GONE);
                }
                branchFilterT.setTextColor(Color.parseColor("#000000"));
                branchCodeFilterT.setTextColor(Color.parseColor("#000000"));
                branchPropsFilterT.setTextColor(Color.parseColor("#000000"));
                ClientFilterT.setTextColor(Color.parseColor("#000000"));

                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    branchPropsFilterT.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    branchPropsFilterT.setTextColor(Color.parseColor("#007BFF"));

                branchTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtCodeBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchPropsBottom.setVisibility(RelativeLayout.VISIBLE);
                ClientTxtBottom.setVisibility(RelativeLayout.VISIBLE);

                ClientFilter.setVisibility(RelativeLayout.VISIBLE);
                branchCodeFilter.setVisibility(RelativeLayout.VISIBLE);
                branchFilter.setVisibility(RelativeLayout.VISIBLE);
                propsFilter.setVisibility(RelativeLayout.VISIBLE);
            }
        });

        branchTxtCodeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    customLayout.setVisibility(RelativeLayout.GONE);
                }
                branchFilterT.setTextColor(Color.parseColor("#000000"));
                branchCodeFilterT.setTextColor(Color.parseColor("#000000"));
                branchPropsFilterT.setTextColor(Color.parseColor("#000000"));
                ClientFilterT.setTextColor(Color.parseColor("#000000"));

                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    branchCodeFilterT.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    branchCodeFilterT.setTextColor(Color.parseColor("#007BFF"));

                ClientFilterT.setTextColor(Color.parseColor("#000000"));
                ClientTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchPropsBottom.setVisibility(RelativeLayout.VISIBLE);
                propsFilter.setVisibility(RelativeLayout.VISIBLE);

                branchTxtCodeBottom.setVisibility(RelativeLayout.VISIBLE);
                ClientFilter.setVisibility(RelativeLayout.VISIBLE);
                branchCodeFilter.setVisibility(RelativeLayout.VISIBLE);
                branchFilter.setVisibility(RelativeLayout.VISIBLE);
            }
        });

        branchTxtView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    customLayout.setVisibility(RelativeLayout.GONE);
                }
                branchFilterT.setTextColor(Color.parseColor("#000000"));
                branchCodeFilterT.setTextColor(Color.parseColor("#000000"));
                branchPropsFilterT.setTextColor(Color.parseColor("#000000"));
                ClientFilterT.setTextColor(Color.parseColor("#000000"));

                if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)) {
                    branchFilterT.setTextColor(Color
                            .parseColor(Helper.appColor));
                } else
                    branchFilterT.setTextColor(Color.parseColor("#007BFF"));

                ClientFilterT.setTextColor(Color.parseColor("#000000"));
                ClientTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtBottom.setVisibility(RelativeLayout.VISIBLE);
                branchTxtCodeBottom.setVisibility(RelativeLayout.VISIBLE);
                branchPropsBottom.setVisibility(RelativeLayout.VISIBLE);
                propsFilter.setVisibility(RelativeLayout.VISIBLE);

                ClientFilter.setVisibility(RelativeLayout.VISIBLE);
                branchFilter.setVisibility(RelativeLayout.VISIBLE);
                branchCodeFilter.setVisibility(RelativeLayout.VISIBLE);
            }
        });
        // radioFilter.setOnCheckedChangeListener(new OnCheckedChangeListener()
        // {
        // public void onCheckedChanged(RadioGroup group, int checkedId) {
        // radioButton = (RadioButton) findViewById(checkedId);
        // if (radioButton.getText().equals("Region")) {
        // if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
        // customLayout.setVisibility(RelativeLayout.GONE);
        // }
        // ClientFilter.setVisibility(RelativeLayout.INVISIBLE);
        // branchFilter.setVisibility(RelativeLayout.VISIBLE);
        // } else if (radioButton.getText().equals("Client")) {
        // if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
        // customLayout.setVisibility(RelativeLayout.GONE);
        // }
        // ClientFilter.setVisibility(RelativeLayout.VISIBLE);
        // branchFilter.setVisibility(RelativeLayout.INVISIBLE);
        // }
        // }
        // });
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // spickerView.setVisibility(RelativeLayout.GONE);
                refresh_submit(true);
                // RadioButton dateRBtn = (RadioButton)
                // findViewById(R.id.radioCity);
                // dateRBtn.setChecked(true);
                if (jobList.getVisibility() == RelativeLayout.VISIBLE) {
                    locationBtn.setVisibility(RelativeLayout.GONE);
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    filterLayout.setVisibility(RelativeLayout.GONE);
                } else if (getIntent() != null && getIntent().hasExtra("lat1")) {
                    ExitFromJobList();
                } else {
                    finish();
                }

            }
        });

        ll_show_jobs_counts = findViewById(R.id.ll_show_jobs_counts);
        ll_select_clear = findViewById(R.id.ll_select_clear);
        mapOrList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doEnableDisableMapListButtons();

            }

        });
        customLayout.addView(pickerView);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Getting a reference to the map

        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().getString("orderid") != null)
            orderid = getIntent().getExtras().getString("orderid");
        else
            orderid = null;

        framView = (FrameLayout) findViewById(R.id.framView);
        layout_map = (RelativeLayout) findViewById(R.id.layout_map);

        // By using this fragment map to load map into fragment and show in
        // screen
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mapfragment = new

                SupportMapFragment();
        transaction.add(R.id.mapView, mapfragment);
        transaction.commit();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//		boolean locatiobpermission =checkpermissionforlocation();
//		if(locatiobpermission)
        loadMap();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

        } else {
            //showGPSDisabledAlertToUser();
        }

        s_date = (TextView) findViewById(R.id.startdate);
        e_date = (TextView) findViewById(R.id.enddate);
        s_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = 1;
                openDatePicker();
            }
        });
        e_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = 2;
                openDatePicker();
            }
        });

    }

    private boolean checkpermissionforlocation() {
//		int hasaccessc = 0;
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//			hasaccessc = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
//		}
//		if (hasaccessc != PackageManager.PERMISSION_GRANTED) {
//			Alertdialogtoopensettings();
//			return false;
//		}
        return true;
    }

    private void Alertdialogtoopensettings() {
        new AlertDialog.Builder(JobBoardActivityFragment.this)
                .setTitle("Permission error")
                .setMessage("This permission does not exist please grant permission to use it, you can do that by going to settings?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null)));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int getIndexofCountry(String s) {
        for (int i = 0; completeListOfCountries != null && i < completeListOfCountries.size(); i++) {
            if (completeListOfCountries.get(i).getCountry() != null &&
                    completeListOfCountries.get(i).getCountry().toLowerCase().equals(s.toLowerCase()))
                return i;
        }
        return -1;
    }

    private ArrayList<CountryLatLng> parsingCountries() throws UnsupportedEncodingException, JSONException {
        String base64 = "W3sNCiBDaXR5TmFtZSA6ICJBZmdoYW5pc3RhbiIsDQogQ2l0eUxhdCA6ICIzMy45MzkxIiwNCiBDaXR5TG9uZyA6ICI2Ny43MTAwIg0KfSwgew0KIENpdHlOYW1lIDogIkFsYmFuaWEiLA0KIENpdHlMYXQgOiAiNDEuMzI3OTUzIiwNCiBDaXR5TG9uZyA6ICIxOS44MTkwMjUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQWxnZXJpYSIsDQogQ2l0eUxhdCA6ICIyOC4wMzM5IiwNCiBDaXR5TG9uZyA6ICIxLjY1OTYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQW1lcmljYW4gU2Ftb2EiLA0KIENpdHlMYXQgOiAiMTQuMjcxMCIsDQogQ2l0eUxvbmcgOiAiMTcwLjEzMjIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQW5nb2xhIiwNCiBDaXR5TGF0IDogIjExLjIwMjciLA0KIENpdHlMb25nIDogIjE3Ljg3MzkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQW5ndWlsbGEiLA0KIENpdHlMYXQgOiAiMTguMjIwNiIsDQogQ2l0eUxvbmcgOiAiNjMuMDY4NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJBbnRpZ3VhICYgQmFyYnVkYSIsDQogQ2l0eUxhdCA6ICIxNy4wNjA4IiwNCiBDaXR5TG9uZyA6ICI2MS43OTY0Ig0KfSwgew0KIENpdHlOYW1lIDogIkFyZ2VudGluYSIsDQogQ2l0eUxhdCA6ICIzOC40MTYxIiwNCiBDaXR5TG9uZyA6ICIgNjMuNjE2NyINCn0sIHsNCiBDaXR5TmFtZSA6ICJBcm1lbmlhIiwNCiBDaXR5TGF0IDogIjQwLjA2OTEiLA0KIENpdHlMb25nIDogIjQ1LjAzODIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQXJ1YmEiLA0KIENpdHlMYXQgOiAiMTIuNTIxMSIsDQogQ2l0eUxvbmcgOiAiNjkuOTY4MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJBdXN0cmFsaWEiLA0KIENpdHlMYXQgOiAiMjUuMjc0NCIsDQogQ2l0eUxvbmcgOiAiMTMzLjc3NTEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQXVzdHJpYSIsDQogQ2l0eUxhdCA6ICI0Ny41MTYyIiwNCiBDaXR5TG9uZyA6ICIxNC41NTAxIg0KfSwgew0KIENpdHlOYW1lIDogIkF6ZXJiYWlqYW4iLA0KIENpdHlMYXQgOiAiNDAuMTQzMSIsDQogQ2l0eUxvbmcgOiAiNDcuNTc2OSINCn0sIHsNCiBDaXR5TmFtZSA6ICJCYWhhbWFzIiwNCiBDaXR5TGF0IDogIjI1LjAzNDMiLA0KIENpdHlMb25nIDogIjc3LjM5NjMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQmFocmFpbiIsDQogQ2l0eUxhdCA6ICIyNi4wNjY3IiwNCiBDaXR5TG9uZyA6ICI1MC41NTc3Ig0KfSwgew0KIENpdHlOYW1lIDogIkJhbmdsYWRlc2giLA0KIENpdHlMYXQgOiAiMjMuNjg1MCIsDQogQ2l0eUxvbmcgOiAiOTAuMzU2MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJCYXJiYWRvcyIsDQogQ2l0eUxhdCA6ICIxMy4xOTM5IiwNCiBDaXR5TG9uZyA6ICI1OS41NDMyIg0KfSwgew0KIENpdHlOYW1lIDogIkJlbGFydXMiLA0KIENpdHlMYXQgOiAiNTMuNzA5OCIsDQogQ2l0eUxvbmcgOiAiMjcuOTUzNCINCn0sIHsNCiBDaXR5TmFtZSA6ICJCZWxnaXVtIiwNCiBDaXR5TGF0IDogIjUwLjUwMzkiLA0KIENpdHlMb25nIDogIjQuNDY5OSINCn0sIHsNCiBDaXR5TmFtZSA6ICJCZWxpemUiLA0KIENpdHlMYXQgOiAiMTcuMTg5OSIsDQogQ2l0eUxvbmcgOiAiODguNDk3NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJCZW5pbiIsDQogQ2l0eUxhdCA6ICI5LjMwNzciLA0KIENpdHlMb25nIDogIjIuMzE1OCINCn0sIHsNCiBDaXR5TmFtZSA6ICJCZXJtdWRhIiwNCiBDaXR5TGF0IDogIjMyLjMwNzgiLA0KIENpdHlMb25nIDogIjY0Ljc1MDUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQmh1dGFuIiwNCiBDaXR5TGF0IDogIjI3LjUxNDIiLA0KIENpdHlMb25nIDogIjkwLjQzMzYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQm9saXZpYSIsDQogQ2l0eUxhdCA6ICIxNi4yOTAyIiwNCiBDaXR5TG9uZyA6ICI2My41ODg3Ig0KfSwgew0KIENpdHlOYW1lIDogIkJvbmFpcmUiLA0KIENpdHlMYXQgOiAiMTIuMjAxOSIsDQogQ2l0eUxvbmcgOiAiNjguMjYyNCINCn0sIHsNCiBDaXR5TmFtZSA6ICJCb3NuaWEgJiBIZXJ6ZWdvdmluYSIsDQogQ2l0eUxhdCA6ICI0My45MTU5IiwNCiBDaXR5TG9uZyA6ICIxNy42NzkxIg0KfSwgew0KIENpdHlOYW1lIDogIkJvdHN3YW5hIiwNCiBDaXR5TGF0IDogIjIyLjMyODUiLA0KIENpdHlMb25nIDogIjI0LjY4NDkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQnJhemlsIiwNCiBDaXR5TGF0IDogIjE0LjIzNTAiLA0KIENpdHlMb25nIDogIjUxLjkyNTMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQnJpdGlzaCBJbmRpYW4gT2NlYW4iLA0KIENpdHlMYXQgOiAiNi4zNDMyIiwNCiBDaXR5TG9uZyA6ICI3MS44NzY1Ig0KfSwgew0KIENpdHlOYW1lIDogIkJydW5laSIsDQogQ2l0eUxhdCA6ICI0LjUzNTMiLA0KIENpdHlMb25nIDogIjExNC43Mjc3Ig0KfSwgew0KIENpdHlOYW1lIDogIkJ1bGdhcmlhIiwNCiBDaXR5TGF0IDogIjQyLjczMzkiLA0KIENpdHlMb25nIDogIjI1LjQ4NTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQnVya2luYSBGYXNvIiwNCiBDaXR5TGF0IDogIjEyLjIzODMiLA0KIENpdHlMb25nIDogIjEuNTYxNiINCn0sIHsNCiBDaXR5TmFtZSA6ICJCdXJ1bmRpIiwNCiBDaXR5TGF0IDogIi0zLjM2MTI2MCIsDQogQ2l0eUxvbmcgOiAiMjkuMzQ3OTE2Ig0KfSwgew0KIENpdHlOYW1lIDogIkNhbWJvZGlhIiwNCiBDaXR5TGF0IDogIjExLjU2MjEwOCIsDQogQ2l0eUxvbmcgOiAiMTA0Ljg4ODUzNSINCn0sIHsNCiBDaXR5TmFtZSA6ICJDYW1lcm9vbiIsDQogQ2l0eUxhdCA6ICI3LjM2OTciLA0KIENpdHlMb25nIDogIjEyLjM1NDciDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ2FuYWRhIiwNCiBDaXR5TGF0IDogIjU2LjEzMDQiLA0KIENpdHlMb25nIDogIjEwNi4zNDY4Ig0KfSwgew0KIENpdHlOYW1lIDogIkNhbmFyeSBJc2xhbmRzIiwNCiBDaXR5TGF0IDogIjI4LjI5MTYiLA0KIENpdHlMb25nIDogIjE2LjYyOTEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiIENhcGUgVmVyZGUiLA0KIENpdHlMYXQgOiAiMTYuNTM4OCIsDQogQ2l0eUxvbmcgOiAiIDIzLjA0MTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ2F5bWFuIElzbGFuZHMiLA0KIENpdHlMYXQgOiAiMTkuMzEzMyIsDQogQ2l0eUxvbmcgOiAiODEuMjU0NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJDZW50cmFsIEFmcmljYW4gUmVwdWJsaWMiLA0KIENpdHlMYXQgOiAiNi42MTExIiwNCiBDaXR5TG9uZyA6ICIyMC45Mzk0Ig0KfSwgew0KIENpdHlOYW1lIDogIkNoYWQiLA0KIENpdHlMYXQgOiAiMTUuNDU0MiIsDQogQ2l0eUxvbmcgOiAiMTguNzMyMiINCn0sIHsNCiBDaXR5TmFtZSA6ICJDaGFubmVsIElzbGFuZHMiLA0KIENpdHlMYXQgOiAiMzMuOTk4MDI4IiwNCiBDaXR5TG9uZyA6ICItMTE5Ljc3Mjk0OSINCn0sIHsNCiBDaXR5TmFtZSA6ICJDaGlsZSIsDQogQ2l0eUxhdCA6ICIzNS42NzUxIiwNCiBDaXR5TG9uZyA6ICI3MS41NDMwIg0KfSwgew0KIENpdHlOYW1lIDogIkNoaW5hIiwNCiBDaXR5TGF0IDogIjM1Ljg2MTciLA0KIENpdHlMb25nIDogIjEwNC4xOTU0Ig0KfSwgew0KIENpdHlOYW1lIDogIkNocmlzdG1hcyBJc2xhbmQiLA0KIENpdHlMYXQgOiAiMTAuNDQ3NSIsDQogQ2l0eUxvbmcgOiAiMTA1LjY5MDQiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ29jb3MgSXNsYW5kIiwNCiBDaXR5TGF0IDogIjEyLjE2NDIiLA0KIENpdHlMb25nIDogIjk2Ljg3MTAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ29sb21iaWEiLA0KIENpdHlMYXQgOiAiNC41NzA5IiwNCiBDaXR5TG9uZyA6ICI3NC4yOTczIg0KfSwgew0KIENpdHlOYW1lIDogIkNvbW9yb3MiLA0KIENpdHlMYXQgOiAiMTEuNjQ1NSIsDQogQ2l0eUxvbmcgOiAiNDMuMzMzMyINCn0sIHsNCiBDaXR5TmFtZSA6ICJDb25nbyIsDQogQ2l0eUxhdCA6ICI0LjAzODMiLA0KIENpdHlMb25nIDogIjIxLjc1ODciDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ29vayBJc2xhbmRzIiwNCiBDaXR5TGF0IDogIjIxLjIzNjciLA0KIENpdHlMb25nIDogIjE1OS43Nzc3Ig0KfSwgew0KIENpdHlOYW1lIDogIkNvc3RhIFJpY2EiLA0KIENpdHlMYXQgOiAiOS43NDg5IiwNCiBDaXR5TG9uZyA6ICI4My43NTM0Ig0KfSwgew0KIENpdHlOYW1lIDogIkNvdGUgREl2b2lyZSIsDQogQ2l0eUxhdCA6ICI3LjU0MDAiLA0KIENpdHlMb25nIDogIjUuNTQ3MSINCn0sIHsNCiBDaXR5TmFtZSA6ICJDcm9hdGlhIiwNCiBDaXR5TGF0IDogIjQ1LjEwMDAiLA0KIENpdHlMb25nIDogIjE1LjIwMDAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiQ3ViYSIsDQogQ2l0eUxhdCA6ICIyMS41MjE4IiwNCiBDaXR5TG9uZyA6ICI3Ny43ODEyIg0KfSwgew0KIENpdHlOYW1lIDogIkN1cmFjYW8iLA0KIENpdHlMYXQgOiAiMTIuMTY5NiIsDQogQ2l0eUxvbmcgOiAiNjguOTkwMCINCn0sIHsNCiBDaXR5TmFtZSA6ICJDeXBydXMiLA0KIENpdHlMYXQgOiAiMzUuMTI2NCIsDQogQ2l0eUxvbmcgOiAiMzMuNDI5OSINCn0sIHsNCiBDaXR5TmFtZSA6ICJDemVjaCBSZXB1YmxpYyIsDQogQ2l0eUxhdCA6ICI0OS44MTc1IiwNCiBDaXR5TG9uZyA6ICIxNS40NzMwIg0KfSwgew0KIENpdHlOYW1lIDogIkRlbm1hcmsiLA0KIENpdHlMYXQgOiAiNTYuMjYzOSIsDQogQ2l0eUxvbmcgOiAiOS41MDE4Ig0KfSwgew0KIENpdHlOYW1lIDogIkRqaWJvdXRpIiwNCiBDaXR5TGF0IDogIjExLjgyNTEiLA0KIENpdHlMb25nIDogIjQyLjU5MDMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRG9taW5pY2EiLA0KIENpdHlMYXQgOiAiMTUuNDE1MCIsDQogQ2l0eUxvbmcgOiAiNjEuMzcxMCINCn0sIHsNCiBDaXR5TmFtZSA6ICJEb21pbmljYW4gUmVwdWJsaWMiLA0KIENpdHlMYXQgOiAiMTguNzM1NyIsDQogQ2l0eUxvbmcgOiAiNzAuMTYyNyINCn0sIHsNCiBDaXR5TmFtZSA6ICJFYXN0IFRpbW9yIiwNCiBDaXR5TGF0IDogIjguODc0MiIsDQogQ2l0eUxvbmcgOiAiMTI1LjcyNzUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRWN1YWRvciIsDQogQ2l0eUxhdCA6ICIxLjgzMTIiLA0KIENpdHlMb25nIDogIjc4LjE4MzQiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRWd5cHQiLA0KIENpdHlMYXQgOiAiMjYuODIwNiIsDQogQ2l0eUxvbmcgOiAiMzAuODAyNSINCn0sIHsNCiBDaXR5TmFtZSA6ICJFbCBTYWx2YWRvciIsDQogQ2l0eUxhdCA6ICIxMy43OTQyIiwNCiBDaXR5TG9uZyA6ICI4OC44OTY1Ig0KfSwgeyANCiBDaXR5TmFtZSA6ICJFcXVhdG9yaWFsIEd1aW5lYSIsDQogQ2l0eUxhdCA6ICIxLjY1MDgiLA0KIENpdHlMb25nIDogIjEwLjI2NzkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRXJpdHJlYSIsDQogQ2l0eUxhdCA6ICIxNSAwMCAiLA0KIENpdHlMb25nIDogIjM5IDAwIg0KfSwgew0KIENpdHlOYW1lIDogIkVzdG9uaWEiLA0KIENpdHlMYXQgOiAiNTguNTk1MyIsDQogQ2l0eUxvbmcgOiAiMjUuMDEzNiINCn0sIHsNCiBDaXR5TmFtZSA6ICJFdGhpb3BpYSIsDQogQ2l0eUxhdCA6ICI5LjE0NTAiLA0KIENpdHlMb25nIDogIjQwLjQ4OTciDQp9LCB7DQogQ2l0eU5hbWUgOiAiRmFsa2xhbmQgSXNsYW5kcyIsDQogQ2l0eUxhdCA6ICI1MS43OTYzIiwNCiBDaXR5TG9uZyA6ICI1OS41MjM2Ig0KfSwgew0KIENpdHlOYW1lIDogIkZhcm9lIElzbGFuZHMiLA0KIENpdHlMYXQgOiAiNjEuODkyNiIsDQogQ2l0eUxvbmcgOiAiNi45MTE4Ig0KfSwgew0KIENpdHlOYW1lIDogIkZpamkiLA0KIENpdHlMYXQgOiAiMTcuNzEzNCIsDQogQ2l0eUxvbmcgOiAiMTc4LjA2NTAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRmlubGFuZCIsDQogQ2l0eUxhdCA6ICI2MS45MjQxIiwNCiBDaXR5TG9uZyA6ICIyNS43NDgyIg0KfSwgew0KIENpdHlOYW1lIDogIkZyYW5jZSIsDQogQ2l0eUxhdCA6ICI0Ni4yMjc2IiwNCiBDaXR5TG9uZyA6ICIyLjIxMzciDQp9LCB7DQogQ2l0eU5hbWUgOiAiRnJlbmNoIEd1aWFuYSIsDQogQ2l0eUxhdCA6ICIzLjkzMzkiLA0KIENpdHlMb25nIDogIjUzLjEyNTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiRnJlbmNoIFBvbHluZXNpYSIsDQogQ2l0eUxhdCA6ICIxNy42Nzk3IiwNCiBDaXR5TG9uZyA6ICIxNDkuNDA2OCINCn0sIHsNCiBDaXR5TmFtZSA6ICJGcmVuY2ggU291dGhlcm4gVGVyIiwNCiBDaXR5TGF0IDogIjQ5LjI4MDQiLA0KIENpdHlMb25nIDogIiA2OS4zNDg2Ig0KfSwgew0KIENpdHlOYW1lIDogIkdhYm9uIiwNCiBDaXR5TGF0IDogIjAuODAzNyIsDQogQ2l0eUxvbmcgOiAiMTEuNjA5NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJHYW1iaWEiLA0KIENpdHlMYXQgOiAiMTMuNDQzMiIsDQogQ2l0eUxvbmcgOiAiMTUuMzEwMSINCn0sIHsNCiBDaXR5TmFtZSA6ICJHZW9yZ2lhIiwNCiBDaXR5TGF0IDogIjMyLjE2NTYiLA0KIENpdHlMb25nIDogIjgyLjkwMDEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiR2VybWFueSIsDQogQ2l0eUxhdCA6ICI1MS4xNjU3IiwNCiBDaXR5TG9uZyA6ICIxMC40NTE1Ig0KfSwgew0KIENpdHlOYW1lIDogIkdoYW5hIiwNCiBDaXR5TGF0IDogIjcuOTQ2NSIsDQogQ2l0eUxvbmcgOiAiMS4wMjMyIg0KfSwgew0KIENpdHlOYW1lIDogIkdpYnJhbHRhciIsDQogQ2l0eUxhdCA6ICIzNi4xNDA4IiwNCiBDaXR5TG9uZyA6ICI1LjM1MzYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiR3JlYXQgQnJpdGFpbiIsDQogQ2l0eUxhdCA6ICI1NS4zNzgxIiwNCiBDaXR5TG9uZyA6ICIzLjQzNjAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiR3JlZWNlIiwNCiBDaXR5TGF0IDogIjM5LjA3NDIiLA0KIENpdHlMb25nIDogIjIxLjgyNDMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiR3JlZW5sYW5kIiwNCiBDaXR5TGF0IDogIjcxLjcwNjkiLA0KIENpdHlMb25nIDogIjQyLjYwNDMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiR3JlbmFkYSIsDQogQ2l0eUxhdCA6ICIxMi4xMTY1IiwNCiBDaXR5TG9uZyA6ICI2MS42NzkwIg0KfSwgew0KIENpdHlOYW1lIDogIkd1YWRlbG91cGUiLA0KIENpdHlMYXQgOiAiMTYuMjY1MCIsDQogQ2l0eUxvbmcgOiAiNjEuNTUxMCINCn0sIHsNCiBDaXR5TmFtZSA6ICJHdWFtIiwNCiBDaXR5TGF0IDogIjEzLjQ0NDMiLA0KIENpdHlMb25nIDogIjE0NC43OTM3Ig0KfSwgew0KIENpdHlOYW1lIDogIkd1YXRlbWFsYSIsDQogQ2l0eUxhdCA6ICIxNS43ODM1IiwNCiBDaXR5TG9uZyA6ICI5MC4yMzA4Ig0KfSwgew0KIENpdHlOYW1lIDogIkd1aW5lYSIsDQogQ2l0eUxhdCA6ICI5Ljk0NTYiLA0KIENpdHlMb25nIDogIjkuNjk2NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJHdXlhbmEiLA0KIENpdHlMYXQgOiAiNC44NjA0IiwNCiBDaXR5TG9uZyA6ICI1OC45MzAyIg0KfSwgew0KIENpdHlOYW1lIDogIkhhaXRpIiwNCiBDaXR5TGF0IDogIjE4Ljk3MTIiLA0KIENpdHlMb25nIDogIiA3Mi4yODUyIg0KfSwgew0KIENpdHlOYW1lIDogIkhhd2FpaSIsDQogQ2l0eUxhdCA6ICIxOS44OTY4IiwNCiBDaXR5TG9uZyA6ICIxNTUuNTgyOCINCn0sIHsNCiBDaXR5TmFtZSA6ICJIb25kdXJhcyIsDQogQ2l0eUxhdCA6ICIxNS4yMDAwIiwNCiBDaXR5TG9uZyA6ICI4Ni4yNDE5Ig0KfSwgew0KIENpdHlOYW1lIDogIkhvbmcgS29uZyIsDQogQ2l0eUxhdCA6ICIyMi4zMTkzIiwNCiBDaXR5TG9uZyA6ICIxMTQuMTY5NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJIdW5nYXJ5IiwNCiBDaXR5TGF0IDogIjQ3LjE2MjUiLA0KIENpdHlMb25nIDogIjE5LjUwMzMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiSWNlbGFuZCIsDQogQ2l0eUxhdCA6ICI2NC45NjMxIiwNCiBDaXR5TG9uZyA6ICIxOS4wMjA4Ig0KfSwgew0KIENpdHlOYW1lIDogIkluZG9uZXNpYSIsDQogQ2l0eUxhdCA6ICIwLjc4OTMiLA0KIENpdHlMb25nIDogIjExMy45MjEzIg0KfSwgew0KIENpdHlOYW1lIDogIkluZGlhIiwNCiBDaXR5TGF0IDogIjIwLjU5MzciLA0KIENpdHlMb25nIDogIjc4Ljk2MjkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiSXJhbiIsDQogQ2l0eUxhdCA6ICIzMi40Mjc5IiwNCiBDaXR5TG9uZyA6ICI1My42ODgwIg0KfSwgew0KIENpdHlOYW1lIDogIklyYXEiLA0KIENpdHlMYXQgOiAiMzMuMjIzMiIsDQogQ2l0eUxvbmcgOiAiNDMuNjc5MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJJcmVsYW5kIiwNCiBDaXR5TGF0IDogIjUzLjE0MjQiLA0KIENpdHlMb25nIDogIjcuNjkyMSINCn0sIHsNCiBDaXR5TmFtZSA6ICJJc2xlIG9mIE1hbiIsDQogQ2l0eUxhdCA6ICI1NC4yMzYxIiwNCiBDaXR5TG9uZyA6ICI0LjU0ODEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiSXNyYWVsIiwNCiBDaXR5TGF0IDogIjMxLjA0NjEiLA0KIENpdHlMb25nIDogIjM0Ljg1MTYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiSXRhbHkiLA0KIENpdHlMYXQgOiAiNDEuODcxOSIsDQogQ2l0eUxvbmcgOiAiMTIuNTY3NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJKYW1haWNhIiwNCiBDaXR5TGF0IDogIjE4LjEwOTYiLA0KIENpdHlMb25nIDogIjc3LjI5NzUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiSmFwYW4iLA0KIENpdHlMYXQgOiAiMzYuMjA0OCIsDQogQ2l0eUxvbmcgOiAiMTM4LjI1MiINCn0sIHsNCiBDaXR5TmFtZSA6ICJKb3JkYW4iLA0KIENpdHlMYXQgOiAiMzAuNTg1MiIsDQogQ2l0eUxvbmcgOiAiMzYuMjM4NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJLYXpha2hzdGFuIiwNCiBDaXR5TGF0IDogIjQ4LjAxOTYiLA0KIENpdHlMb25nIDogIjY2LjkyMzciDQp9LCB7DQogQ2l0eU5hbWUgOiAiS2VueWEiLA0KIENpdHlMYXQgOiAiMC4wMjM2IiwNCiBDaXR5TG9uZyA6ICIzNy45MDYyIg0KfSwgew0KIENpdHlOYW1lIDogIktpcmliYXRpIiwNCiBDaXR5TGF0IDogIjMuMzcwNCIsDQogQ2l0eUxvbmcgOiAiMTY4LjczNDAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiS29yZWEgTm9ydGgiLA0KIENpdHlMYXQgOiAiNDAuMzM5OSIsDQogQ2l0eUxvbmcgOiAiMTI3LjUxMDEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiS29yZWEgU291dGgiLA0KIENpdHlMYXQgOiAiMzUuOTA3OCIsDQogQ2l0eUxvbmcgOiAiMTI3Ljc2NjkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiS3V3YWl0IiwNCiBDaXR5TGF0IDogIjI5LjMxMTciLA0KIENpdHlMb25nIDogIjQ3LjQ4MTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiS3lyZ3l6c3RhbiIsDQogQ2l0eUxhdCA6ICI0MS4yMDQ0IiwNCiBDaXR5TG9uZyA6ICI3NC43NjYxIg0KfSwgew0KIENpdHlOYW1lIDogIkxhb3MiLA0KIENpdHlMYXQgOiAiMTkuODU2MyIsDQogQ2l0eUxvbmcgOiAiMTAyLjQ5NTUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTGF0dmlhIiwNCiBDaXR5TGF0IDogIjU2Ljg3OTYiLA0KIENpdHlMb25nIDogIjI0LjYwMzIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTGViYW5vbiIsDQogQ2l0eUxhdCA6ICIzMy44NTQ3IiwNCiBDaXR5TG9uZyA6ICIzNS44NjIzIg0KfSwgew0KIENpdHlOYW1lIDogIkxlc290aG8iLA0KIENpdHlMYXQgOiAiMjkuNjEwMCIsDQogQ2l0eUxvbmcgOiAiMjguMjMzNiINCn0sIHsNCiBDaXR5TmFtZSA6ICIgTGliZXJpYSIsDQogQ2l0eUxhdCA6ICI2LjQyODEiLA0KIENpdHlMb25nIDogIjkuNDI5NSINCn0sIHsNCiBDaXR5TmFtZSA6ICJMaWJ5YSIsDQogQ2l0eUxhdCA6ICIyNi4zMzUxIiwNCiBDaXR5TG9uZyA6ICIxNy4yMjgzIg0KfSwgew0KIENpdHlOYW1lIDogIkxpZWNodGVuc3RlaW4iLA0KIENpdHlMYXQgOiAiNDcuMTY2MCIsDQogQ2l0eUxvbmcgOiAiOS41NTU0Ig0KfSwgew0KIENpdHlOYW1lIDogIkxpdGh1YW5pYSIsDQogQ2l0eUxhdCA6ICI1NS4xNjk0IiwNCiBDaXR5TG9uZyA6ICIyMy44ODEzIg0KfSwgew0KIENpdHlOYW1lIDogIkx1eGVtYm91cmciLA0KIENpdHlMYXQgOiAiNDkuODE1MyIsDQogQ2l0eUxvbmcgOiAiNi4xMjk2Ig0KfSwgew0KIENpdHlOYW1lIDogIk1hY2F1IiwNCiBDaXR5TGF0IDogIjIyLjE5ODciLA0KIENpdHlMb25nIDogIjExMy41NDM5Ig0KfSwgew0KIENpdHlOYW1lIDogIk1hY2Vkb25pYSIsDQogQ2l0eUxhdCA6ICI0MS42MDg2IiwNCiBDaXR5TG9uZyA6ICIyMS43NDUzIg0KfSwgew0KIENpdHlOYW1lIDogIk1hZGFnYXNjYXIiLA0KIENpdHlMYXQgOiAiMTguNzY2OSIsDQogQ2l0eUxvbmcgOiAiNDYuODY5MSINCn0sIHsNCiBDaXR5TmFtZSA6ICJNYWxheXNpYSIsDQogQ2l0eUxhdCA6ICI0LjIxMDUiLA0KIENpdHlMb25nIDogIjEwMS45NzU4Ig0KfSwgew0KIENpdHlOYW1lIDogIk1hbGF3aSIsDQogQ2l0eUxhdCA6ICIxMy4yNTQzIiwNCiBDaXR5TG9uZyA6ICIzNC4zMDE1Ig0KfSwgew0KIENpdHlOYW1lIDogIk1hbGRpdmVzIiwNCiBDaXR5TGF0IDogIjMuMjAyOCIsDQogQ2l0eUxvbmcgOiAiNzMuMjIwNyINCn0sIHsNCiBDaXR5TmFtZSA6ICJNYWxpIiwNCiBDaXR5TGF0IDogIjE3LjU3MDciLA0KIENpdHlMb25nIDogIjMuOTk2MiINCn0sIHsNCiBDaXR5TmFtZSA6ICIgTWFsdGEiLA0KIENpdHlMYXQgOiAiMzUuOTM3NSIsDQogQ2l0eUxvbmcgOiAiMTQuMzc1NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJNYXJzaGFsbCBJc2xhbmRzIiwNCiBDaXR5TGF0IDogIjcuMTMxNSIsDQogQ2l0eUxvbmcgOiAiMTcxLjE4NDUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTWFydGluaXF1ZSIsDQogQ2l0eUxhdCA6ICIxNC42NDE1IiwNCiBDaXR5TG9uZyA6ICI2MS4wMjQyIg0KfSwgew0KIENpdHlOYW1lIDogIk1hdXJpdGFuaWEiLA0KIENpdHlMYXQgOiAiMjEuMDA3OSIsDQogQ2l0eUxvbmcgOiAiMTAuOTQwOCINCn0sIHsNCiBDaXR5TmFtZSA6ICJNYXVyaXRpdXMiLA0KIENpdHlMYXQgOiAiMjAuMzQ4NCIsDQogQ2l0eUxvbmcgOiAiIDU3LjU1MjIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTWF5b3R0ZSIsDQogQ2l0eUxhdCA6ICIxMi44Mjc1IiwNCiBDaXR5TG9uZyA6ICI0NS4xNjYyIg0KfSwgew0KIENpdHlOYW1lIDogIk1leGljbyIsDQogQ2l0eUxhdCA6ICIyMy42MzQ1IiwNCiBDaXR5TG9uZyA6ICIxMDIuNTUyOCINCn0sIHsNCiBDaXR5TmFtZSA6ICJNaWR3YXkgSXNsYW5kcyIsDQogQ2l0eUxhdCA6ICIyOC4yMDcyIiwNCiBDaXR5TG9uZyA6ICIxNzcuMzczNSINCn0sIHsNCiBDaXR5TmFtZSA6ICJNb2xkb3ZhIiwNCiBDaXR5TGF0IDogIjQ3LjQxMTYiLA0KIENpdHlMb25nIDogIjI4LjM2OTkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiIE1vbmFjbyIsDQogQ2l0eUxhdCA6ICI0My43Mzg0IiwNCiBDaXR5TG9uZyA6ICI3LjQyNDYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiIE1vbmdvbGlhIiwNCiBDaXR5TGF0IDogIjQ2Ljg2MjUiLA0KIENpdHlMb25nIDogIjEwMy44NDY3Ig0KfSwgew0KIENpdHlOYW1lIDogIk1vbnRzZXJyYXQiLA0KIENpdHlMYXQgOiAiMTYuNzQyNSIsDQogQ2l0eUxvbmcgOiAiNjIuMTg3NCINCn0sIHsNCiBDaXR5TmFtZSA6ICJNb3phbWJpcXVlIiwNCiBDaXR5TGF0IDogIjE4LjY2NTciLA0KIENpdHlMb25nIDogIjM1LjUyOSINCn0sIHsNCiBDaXR5TmFtZSA6ICJNeWFubWFyIiwNCiBDaXR5TGF0IDogIjIxLjkxNjIiLA0KIENpdHlMb25nIDogIjk1Ljk1NjAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTmFtaWJpYSIsDQogQ2l0eUxhdCA6ICIyMi45NTc2IiwNCiBDaXR5TG9uZyA6ICIxOC40OTA0Ig0KfSwgew0KIENpdHlOYW1lIDogIk5hdXJ1IiwNCiBDaXR5TGF0IDogIjAuNTIyOCIsDQogQ2l0eUxvbmcgOiAiMTY2LjkzMTUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiTmVwYWwiLA0KIENpdHlMYXQgOiAiMjguMzk0OSIsDQogQ2l0eUxvbmcgOiAiODQuMTI0MCINCn0sIHsNCiBDaXR5TmFtZSA6ICJOZXRoZXJsYW5kIiwNCiBDaXR5TGF0IDogIjUyLjEzMjYiLA0KIENpdHlMb25nIDogIjUuMjkxMyINCn0sIHsNCiBDaXR5TmFtZSA6ICIgTmV2aXMiLA0KIENpdHlMYXQgOiAiMTcuMTU1NCIsDQogQ2l0eUxvbmcgOiAiNjIuNTc5NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJOZXcgQ2FsZWRvbmlhIiwNCiBDaXR5TGF0IDogIjIwLjkwNDMiLA0KIENpdHlMb25nIDogIjE2NS42MTgwIg0KfSwgew0KIENpdHlOYW1lIDogIk5ldyBaZWFsYW5kIiwNCiBDaXR5TGF0IDogIjQwLjkwMDYiLA0KIENpdHlMb25nIDogIjE3NC44ODYwIg0KfSwgew0KIENpdHlOYW1lIDogIk5pY2FyYWd1YSIsDQogQ2l0eUxhdCA6ICIxMi44NjU0IiwNCiBDaXR5TG9uZyA6ICI4NS4yMDcyIg0KfSwgew0KIENpdHlOYW1lIDogIk5pZ2VyIiwNCiBDaXR5TGF0IDogIjE3LjYwNzgiLA0KIENpdHlMb25nIDogIjguMDgxNyINCn0sIHsNCiBDaXR5TmFtZSA6ICIgTmlnZXJpYSIsDQogQ2l0eUxhdCA6ICI5LjA4MjAiLA0KIENpdHlMb25nIDogIjguNjc1MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJOaXVlIiwNCiBDaXR5TGF0IDogIjE5LjA1NDQiLA0KIENpdHlMb25nIDogIjE2OS44NjcyIg0KfSwgew0KIENpdHlOYW1lIDogIk5vcmZvbGsgSXNsYW5kIiwNCiBDaXR5TGF0IDogIjI5LjA0MDgiLA0KIENpdHlMb25nIDogIjE2Ny45NTQ3Ig0KfSwgew0KIENpdHlOYW1lIDogIk5vcndheSIsDQogQ2l0eUxhdCA6ICI2MC40NzIwIiwNCiBDaXR5TG9uZyA6ICI4LjQ2ODkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiT21hbiIsDQogQ2l0eUxhdCA6ICIyMS40NzM1IiwNCiBDaXR5TG9uZyA6ICI1NS45NzU0Ig0KfSwgew0KIENpdHlOYW1lIDogIlBha2lzdGFuIiwNCiBDaXR5TGF0IDogIjMwLjM3NTMiLA0KIENpdHlMb25nIDogIjY5LjM0NTEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUGFsYXUgSXNsYW5kIiwNCiBDaXR5TGF0IDogIjcuNTE1MCIsDQogQ2l0eUxvbmcgOiAiMTM0LjU4MjUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUGFsZXN0aW5lIiwNCiBDaXR5TGF0IDogIjMxLjk1MjIiLA0KIENpdHlMb25nIDogIjM1LjIzMzIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUGFuYW1hIiwNCiBDaXR5TGF0IDogIjguNTM4MCIsDQogQ2l0eUxvbmcgOiAiODAuNzgyMSINCn0sIHsNCiBDaXR5TmFtZSA6ICJQYXB1YSBOZXcgR3VpbmVhIiwNCiBDaXR5TGF0IDogIjYuMzE1MCIsDQogQ2l0eUxvbmcgOiAiMTQzLjk1NTUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUGFyYWd1YXkiLA0KIENpdHlMYXQgOiAiMjMuNDQyNSIsDQogQ2l0eUxvbmcgOiAiNTguNDQzOCINCn0sIHsNCiBDaXR5TmFtZSA6ICJQZXJ1IiwNCiBDaXR5TGF0IDogIjkuMTkwMCIsDQogQ2l0eUxvbmcgOiAiNzUuMDE1MiINCn0sIHsNCiBDaXR5TmFtZSA6ICJQaGlsaXBwaW5lcyIsDQogQ2l0eUxhdCA6ICIxMi44Nzk3IiwNCiBDaXR5TG9uZyA6ICIxMjEuNzc0MCINCn0sIHsNCiBDaXR5TmFtZSA6ICJQaXRjYWlybiBJc2xhbmQiLA0KIENpdHlMYXQgOiAiMjQuMzc2OCIsDQogQ2l0eUxvbmcgOiAiMTI4LjMyNDIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUG9sYW5kIiwNCiBDaXR5TGF0IDogIjUxLjkxOTQiLA0KIENpdHlMb25nIDogIjE5LjE0NTEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUG9ydHVnYWwiLA0KIENpdHlMYXQgOiAiMzkuMzk5OSIsDQogQ2l0eUxvbmcgOiAiOC4yMjQ1Ig0KfSwgew0KIENpdHlOYW1lIDogIlB1ZXJ0byBSaWNvIiwNCiBDaXR5TGF0IDogIjE4LjIyMDgiLA0KIENpdHlMb25nIDogIjY2LjU5MDEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUWF0YXIiLA0KIENpdHlMYXQgOiAiMjUuMzU0OCIsDQogQ2l0eUxvbmcgOiAiNTEuMTgzOSINCn0sIHsNCiBDaXR5TmFtZSA6ICJSZXB1YmxpYyBvZiBNb250ZW5lZ3JvIiwNCiBDaXR5TGF0IDogIjQyLjcwODciLA0KIENpdHlMb25nIDogIjE5LjM3NDQiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUmVwdWJsaWMgb2YgU2VyYmlhIiwNCiBDaXR5TGF0IDogIjQ0LjAxNjUiLA0KIENpdHlMb25nIDogIjIxLjAwNTkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUmV1bmlvbiIsDQogQ2l0eUxhdCA6ICIyMS4xMTUxIiwNCiBDaXR5TG9uZyA6ICI1NS41MzY0Ig0KfSwgew0KIENpdHlOYW1lIDogIlJvbWFuaWEiLA0KIENpdHlMYXQgOiAiNDUuOTQzMiIsDQogQ2l0eUxvbmcgOiAiMjQuOTY2OCINCn0sIHsNCiBDaXR5TmFtZSA6ICJSdXNzaWEiLA0KIENpdHlMYXQgOiAiNjEuNTI0MCIsDQogQ2l0eUxvbmcgOiAiMTA1LjMxODgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiUndhbmRhIiwNCiBDaXR5TGF0IDogIjEuOTQwMyIsDQogQ2l0eUxvbmcgOiAiMjkuODczOSINCn0sIHsNCiBDaXR5TmFtZSA6ICJTdCBCYXJ0aGVsZW15IiwNCiBDaXR5TGF0IDogIjE3LjkwMDAiLA0KIENpdHlMb25nIDogIjYyLjgzMzMiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3QgRXVzdGF0aXVzIiwNCiBDaXR5TGF0IDogIjE3LjQ4OTAiLA0KIENpdHlMb25nIDogIjYyLjk3MzYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3QgSGVsZW5hIiwNCiBDaXR5TGF0IDogIjE1Ljk2NTAiLA0KIENpdHlMb25nIDogIjUuNzA4OSINCn0sIHsNCiBDaXR5TmFtZSA6ICJTdCBLaXR0cy1OZXZpcyIsDQogQ2l0eUxhdCA6ICIxNy4zNTc4IiwNCiBDaXR5TG9uZyA6ICI2Mi43ODMwIg0KfSwgew0KIENpdHlOYW1lIDogIlN0IEx1Y2lhIiwNCiBDaXR5TGF0IDogIjEzLjkwOTQiLA0KIENpdHlMb25nIDogIjYwLjk3ODkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3QgTWFhcnRlbiIsDQogQ2l0eUxhdCA6ICIxOC4wNDI1IiwNCiBDaXR5TG9uZyA6ICI2My4wNTQ4Ig0KfSwgew0KIENpdHlOYW1lIDogIlN0IFBpZXJyZSAmIE1pcXVlbG9uIiwNCiBDaXR5TGF0IDogIjQ2Ljg4NTIiLA0KIENpdHlMb25nIDogIjU2LjMxNTkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3QgVmluY2VudCAmIEdyZW5hZGluZXMiLA0KIENpdHlMYXQgOiAiMTIuOTg0MyIsDQogQ2l0eUxvbmcgOiAiNjEuMjg3MiINCn0sIHsNCiBDaXR5TmFtZSA6ICJTYWlwYW4iLA0KIENpdHlMYXQgOiAiMTUuMTg1MCIsDQogQ2l0eUxvbmcgOiAiMTQ1Ljc0NjciDQp9LCB7DQogQ2l0eU5hbWUgOiAiU2Ftb2EiLA0KIENpdHlMYXQgOiAiMTMuNzU5MCIsDQogQ2l0eUxvbmcgOiAiMTcyLjEwNDYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU2FuIE1hcmlubyIsDQogQ2l0eUxhdCA6ICI0My45NDI0IiwNCiBDaXR5TG9uZyA6ICIxMi40NTc4Ig0KfSwgew0KIENpdHlOYW1lIDogIlNhbyBUb21lICYgUHJpbmNpcGUiLA0KIENpdHlMYXQgOiAiMC4xODY0IiwNCiBDaXR5TG9uZyA6ICI2LjYxMzEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiIFNhdWRpIEFyYWJpYSIsDQogQ2l0eUxhdCA6ICIyMy44ODU5IiwNCiBDaXR5TG9uZyA6ICI0NS4wNzkyIg0KfSwgew0KIENpdHlOYW1lIDogIlNlbmVnYWwiLA0KIENpdHlMYXQgOiAiMTQuNDk3NCIsDQogQ2l0eUxvbmcgOiAiMTQuNDUyNCINCn0sIHsNCiBDaXR5TmFtZSA6ICJWU2V5Y2hlbGxlcyIsDQogQ2l0eUxhdCA6ICI0LjY3OTYiLA0KIENpdHlMb25nIDogIjQ0LjM2MTQ4OCINCn0sIHsNCiBDaXR5TmFtZSA6ICJTaWVycmEgTGVvbmUiLA0KIENpdHlMYXQgOiAiNC42Nzk2IiwNCiBDaXR5TG9uZyA6ICI1NS40OTIwIg0KfSwgew0KIENpdHlOYW1lIDogIlNpbmdhcG9yZSIsDQogQ2l0eUxhdCA6ICIxLjM1MjEiLA0KIENpdHlMb25nIDogIjEwMy44MTk4Ig0KfSwgew0KIENpdHlOYW1lIDogIlNsb3Zha2lhIiwNCiBDaXR5TGF0IDogIjQ4LjY2OTAiLA0KIENpdHlMb25nIDogIjE5LjY5OTAiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU2xvdmVuaWEiLA0KIENpdHlMYXQgOiAiNDYuMTUxMiIsDQogQ2l0eUxvbmcgOiAiMTQuOTk1NSINCn0sIHsNCiBDaXR5TmFtZSA6ICJTb2xvbW9uIElzbGFuZHMiLA0KIENpdHlMYXQgOiAiOS42NDU3IiwNCiBDaXR5TG9uZyA6ICIxNjAuMTU2MiINCn0sIHsNCiBDaXR5TmFtZSA6ICJTb3V0aCBBZnJpY2EiLA0KIENpdHlMYXQgOiAiMzAuNTU5NSIsDQogQ2l0eUxvbmcgOiAiMjIuOTM3NSINCn0sIHsNCiBDaXR5TmFtZSA6ICJTcGFpbiIsDQogQ2l0eUxhdCA6ICI0MC40NjM3IiwNCiBDaXR5TG9uZyA6ICIzLjc0OTIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiIFNyaSBMYW5rYSIsDQogQ2l0eUxhdCA6ICI3Ljg3MzEiLA0KIENpdHlMb25nIDogIjgwLjc3MTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3VkYW4iLA0KIENpdHlMYXQgOiAiMTIuODYyOCIsDQogQ2l0eUxvbmcgOiAiMzAuMjE3NiINCn0sIHsNCiBDaXR5TmFtZSA6ICJTdXJpbmFtZSIsDQogQ2l0eUxhdCA6ICIzLjkxOTMiLA0KIENpdHlMb25nIDogIjU2LjAyNzgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3dhemlsYW5kIiwNCiBDaXR5TGF0IDogIjI2LjUyMjUiLA0KIENpdHlMb25nIDogIjMxLjQ2NTkiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3dlZGVuIiwNCiBDaXR5TGF0IDogIjYwLjEyODIiLA0KIENpdHlMb25nIDogIjE4LjY0MzUiDQp9LCB7DQogQ2l0eU5hbWUgOiAiU3dpdHplcmxhbmQiLA0KIENpdHlMYXQgOiAiNDYuODE4MiIsDQogQ2l0eUxvbmcgOiAiOC4yMjc1Ig0KfSwgew0KIENpdHlOYW1lIDogIlN5cmlhIiwNCiBDaXR5TGF0IDogIjM0LjgwMjEiLA0KIENpdHlMb25nIDogIjM4Ljk5NjgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVGFoaXRpIiwNCiBDaXR5TGF0IDogIjE3LjY1MDkiLA0KIENpdHlMb25nIDogIjE0OS40MjYwIg0KfSwgew0KIENpdHlOYW1lIDogIlRhaXdhbiIsDQogQ2l0eUxhdCA6ICIyMy42OTc4IiwNCiBDaXR5TG9uZyA6ICIxMjAuOTYwNSINCn0sIHsNCiBDaXR5TmFtZSA6ICJUYWppa2lzdGFuIiwNCiBDaXR5TGF0IDogIjM4Ljg2MTAiLA0KIENpdHlMb25nIDogIjcxLjI3NjEiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVGFuemFuaWEiLA0KIENpdHlMYXQgOiAiNi4zNjkwIiwNCiBDaXR5TG9uZyA6ICIzNC44ODg4Ig0KfSwgew0KIENpdHlOYW1lIDogIlRoYWlsYW5kIiwNCiBDaXR5TGF0IDogIjE1Ljg3MDAiLA0KIENpdHlMb25nIDogIjEwMC45OTI1Ig0KfSwgew0KIENpdHlOYW1lIDogIlRvZ28iLA0KIENpdHlMYXQgOiAiOC42MTk1IiwNCiBDaXR5TG9uZyA6ICIwLjgyNDgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVG9rZWxhdSIsDQogQ2l0eUxhdCA6ICI5LjIwMDIiLA0KIENpdHlMb25nIDogIjE3MS44NDg0Ig0KfSwgew0KIENpdHlOYW1lIDogIlRvbmdhIiwNCiBDaXR5TGF0IDogIjkuMjAwMiIsDQogQ2l0eUxvbmcgOiAiMTcxLjg0ODQiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVHJpbmlkYWQgJiBUb2JhZ28iLA0KIENpdHlMYXQgOiAiMTAuNjkxOCIsDQogQ2l0eUxvbmcgOiAiNjEuMjIyNSINCn0sIHsNCiBDaXR5TmFtZSA6ICJUdW5pc2lhIiwNCiBDaXR5TGF0IDogIjMzLjg4NjkiLA0KIENpdHlMb25nIDogIjkuNTM3NSINCn0sIHsNCiBDaXR5TmFtZSA6ICJUdXJrZXkiLA0KIENpdHlMYXQgOiAiMzguOTYzNyIsDQogQ2l0eUxvbmcgOiAiMzUuMjQzMyINCn0sIHsNCiBDaXR5TmFtZSA6ICJUdXJrbWVuaXN0YW4iLA0KIENpdHlMYXQgOiAiMzguOTY5NyIsDQogQ2l0eUxvbmcgOiAiNTkuNTU2MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJUdXJrcyAmIENhaWNvcyBJcyIsDQogQ2l0eUxhdCA6ICIyMS42OTQwIiwNCiBDaXR5TG9uZyA6ICI3MS43OTc5Ig0KfSwgew0KIENpdHlOYW1lIDogIiBUdXZhbHUiLA0KIENpdHlMYXQgOiAiNy4xMDk1IiwNCiBDaXR5TG9uZyA6ICIxNzcuNjQ5MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJVZ2FuZGEiLA0KIENpdHlMYXQgOiAiMS4zNzMzIiwNCiBDaXR5TG9uZyA6ICIzMi4yOTAzIg0KfSwgew0KIENpdHlOYW1lIDogIlVuaXRlZCBLaW5nZG9tIiwNCiBDaXR5TGF0IDogIjU1LjM3ODEiLA0KIENpdHlMb25nIDogIjMuNDM2MCINCn0sIHsNCiBDaXR5TmFtZSA6ICJVa3JhaW5lIiwNCiBDaXR5TGF0IDogIjQ4LjM3OTQiLA0KIENpdHlMb25nIDogIjMxLjE2NTYiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVW5pdGVkIEFyYWIgRW1pcmF0ZXMiLA0KIENpdHlMYXQgOiAiMjMuNDI0MSIsDQogQ2l0eUxvbmcgOiAiNTMuODQ3OCINCn0sIHsNCiBDaXR5TmFtZSA6ICJVbml0ZWQgU3RhdGVzIG9mIEFtZXJpY2EiLA0KIENpdHlMYXQgOiAiMzcuMDkwMiIsDQogQ2l0eUxvbmcgOiAiOTUuNzEyOSINCn0sIHsNCiBDaXR5TmFtZSA6ICJVcnVndWF5IiwNCiBDaXR5TGF0IDogIjMyLjUyMjgiLA0KIENpdHlMb25nIDogIjU1Ljc2NTgiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVXpiZWtpc3RhbiIsDQogQ2l0eUxhdCA6ICI0MS4zNzc1IiwNCiBDaXR5TG9uZyA6ICI2NC41ODUzIg0KfSwgew0KIENpdHlOYW1lIDogIlZhbnVhdHUiLA0KIENpdHlMYXQgOiAiMTUuMzc2NyIsDQogQ2l0eUxvbmcgOiAiMTY2Ljk1OTIiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVmF0aWNhbiBDaXR5IFN0YXRlIiwNCiBDaXR5TGF0IDogIjQxLjkwMjkiLA0KIENpdHlMb25nIDogIjEyLjQ1MzQiDQp9LCB7DQogQ2l0eU5hbWUgOiAiVmVuZXp1ZWxhIiwNCiBDaXR5TGF0IDogIjYuNDIzOCIsDQogQ2l0eUxvbmcgOiAiNjYuNTg5NyINCn0sIHsNCiBDaXR5TmFtZSA6ICJWaWV0bmFtIiwNCiBDaXR5TGF0IDogIjE0LjA1ODMiLA0KIENpdHlMb25nIDogIjEwOC4yNzcyIg0KfSwgew0KIENpdHlOYW1lIDogIldha2UgSXNsYW5kIiwNCiBDaXR5TGF0IDogIjE5LjI3OTYiLA0KIENpdHlMb25nIDogIjE2Ni42NDk5Ig0KfSwgew0KIENpdHlOYW1lIDogIldhbGxpcyAmIEZ1dHVuYSBJcyIsDQogQ2l0eUxhdCA6ICIxNC4yOTM4IiwNCiBDaXR5TG9uZyA6ICIxNzguMTE2NSINCn0sIHsNCiBDaXR5TmFtZSA6ICJZZW1lbiIsDQogQ2l0eUxhdCA6ICIxNS41NTI3IiwNCiBDaXR5TG9uZyA6ICI0OC41MTY0Ig0KfSwgew0KIENpdHlOYW1lIDogIlphaXJlIiwNCiBDaXR5TGF0IDogIjQuMDM4MyIsDQogQ2l0eUxvbmcgOiAiMjEuNzU4NyINCn0sIHsNCiBDaXR5TmFtZSA6ICJaYW1iaWEiLA0KIENpdHlMYXQgOiAiMTMuMTMzOSIsDQogQ2l0eUxvbmcgOiAiMjcuODQ5MyINCn0sIHsNCiBDaXR5TmFtZSA6ICJaaW1iYWJ3ZSIsDQogQ2l0eUxhdCA6ICIxOS4wMTU0IiwNCiBDaXR5TG9uZyA6ICIyOS4xNTQ5Ig0KfQ0KXQ==";
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String json = new String(data, "UTF-8");
        JSONArray jsonarray = new JSONArray(json);
        ArrayList<CountryLatLng> list = new ArrayList<>();
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                //
                JSONObject obj = jsonarray.getJSONObject(i);
                CountryLatLng clng = new CountryLatLng(obj.getString("CityName"),
                        obj.getString("CityLat"),
                        obj.getString("CityLong"));
                list.add(clng);
            } catch (Exception ex) {
                int k = 0;
                k++;
            }
        }
        return list;
    }

    ArrayList<CountryLatLng> completeListOfCountries;

    private String[] parseCountriesList() {
        try {
            completeListOfCountries = parsingCountries();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        String[] arrays = null;
        for (int i = 0; completeListOfCountries != null && i < completeListOfCountries.size(); i++) {
            if (arrays == null) arrays = new String[completeListOfCountries.size()];
            arrays[i] = completeListOfCountries.get(i).getCountry();
        }
        return arrays;
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("gps not enabled");
        alertDialog.setMessage("not enabled");
        alertDialog.setPositiveButton(
                "Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(
                                new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                111);
                    }
                });
        alertDialog.setNegativeButton(
                "use without gps", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void loadMap() {

        final LocationGPS gps = new LocationGPS(this);
        currentLot = gps.getLocation();

        // Here check if Google play service are available in device
        resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            AlertDialog.Builder builder = new AlertDialog.Builder(JobBoardActivityFragment.this);
            if (resultCode == ConnectionResult.SERVICE_MISSING)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_DISABLED)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_INVALID)
                builder.setMessage("Error loaing map");

            builder.setCancelable(true);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            // This handler is using for to to load map after 100 mili sec delay
            // If we load map with handler it does not load and in googlemap
            // variable we receive null
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentLot = gps.getLocation();

                        mapfragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap googleMap) {
                                JobBoardActivityFragment.this.googleMap = googleMap;
                                loadMapNext();
                            }
                        });


                        if (currentLot != null) {
                            double lat = Math.abs(currentLot.getLatitude());
                            double lng = Math.abs(currentLot.getLongitude());

                            if (lat > 0 && lng > 0 && googleMap != null) {
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newLatLngZoom(
                                                new LatLng(
                                                        currentLot.getLatitude(),
                                                        currentLot.getLongitude()),
                                                17.0f));
                                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                                // LatLng(currentLot.getLatitude(),
                                // currentLot.getLongitude()), 14.0f));
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .title("Marker"));
                                    }
                                });
                            } else {
                                makeText(getApplicationContext(),
                                        "Searching current location.",
                                        Toast.LENGTH_SHORT);
                            }

                        } else {
                            makeText(getBaseContext(),
                                    "Location not found. Please try again. ",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // This listener for Google map cameralister when dragging
                        // on map or zooming and changing locaiton view then this
                        // listener called

                    }
                }, 100);
            } catch (Exception ee) {

            }
        }
    }


    private void loadMapNext() {
        if (getIntent() != null
                && getIntent().getExtras().getDouble("lat1") > 0.0) {
            // load notification data

            LatLng currentLocation = new LatLng(getIntent().getExtras()
                    .getDouble("lat1"), getIntent().getExtras()
                    .getDouble("lng1"));
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(
                    currentLocation, 15);
            googleMap.moveCamera(center);
            CameraChanged(googleMap.getCameraPosition(),
                    currentLocation);

        } else {
            Location location = getLocation();
            LatLng currentLocation = new LatLng(0.0, 0.0);
            if (location != null && location.getLatitude() != 0.0
                    && location.getLongitude() != 0.0) {
                currentLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                CameraUpdate center = CameraUpdateFactory
                        .newLatLng(currentLocation);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                CameraChanged(googleMap.getCameraPosition(), null);
            } else {
                CameraUpdate center = CameraUpdateFactory
                        .newLatLng(currentLocation);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                CameraChanged(googleMap.getCameraPosition(), null);
            }

        }
        startLocationChecker();
    }

    private void doEnableDisableMapListButtons() {
        if (map_list == true) {
            //TODO Show list view
            jobList.setVisibility(RelativeLayout.VISIBLE);
            locationBtn.setVisibility(RelativeLayout.GONE);
            btnApplyAll.setVisibility(RelativeLayout.VISIBLE);
            iv_list.setVisibility(View.GONE);
            iv_map.setVisibility(View.VISIBLE);
            actv.setVisibility(View.GONE);
            ll_show_jobs_counts.setVisibility(View.VISIBLE);
            ll_select_clear.setVisibility(View.VISIBLE);
            tvMap.setTypeface(null, Typeface.BOLD);
            tvList.setTypeface(null, Typeface.NORMAL);
            tvMap.setTextSize(18);
            tvList.setTextSize(24);
            openListForCertainPins();
            map_list = false;
            if (isListEmpty == true) {
                emptyListLabel.setVisibility(RelativeLayout.VISIBLE);
                bottomBar.setVisibility(RelativeLayout.INVISIBLE);
                ll_show_jobs_counts.setVisibility(View.GONE);
                ll_select_clear.setVisibility(View.GONE);
            } else {
                emptyListLabel.setVisibility(RelativeLayout.GONE);
                bottomBar.setVisibility(RelativeLayout.VISIBLE);
                ll_show_jobs_counts.setVisibility(View.VISIBLE);
                ll_select_clear.setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyListLabel.getVisibility() == RelativeLayout.VISIBLE) {
                emptyListLabel.setVisibility(RelativeLayout.GONE);
            }
            if (thisPersonLocaation != null)
                addPersonMarker(thisPersonLocaation);
            jobList.setVisibility(RelativeLayout.GONE);
            bottomBar.setVisibility(RelativeLayout.INVISIBLE);
            btnApplyAll.setVisibility(RelativeLayout.GONE);
            iv_list.setVisibility(View.VISIBLE);
            iv_map.setVisibility(View.GONE);
            actv.setVisibility(View.VISIBLE);
            ll_show_jobs_counts.setVisibility(View.GONE);
            ll_select_clear.setVisibility(View.GONE);
            tvList.setTypeface(null, Typeface.BOLD);
            tvMap.setTypeface(null, Typeface.NORMAL);
            tvList.setTextSize(18);
            tvMap.setTextSize(24);
            map_list = true;
        }

    }

    protected void showhide_filter() {
        if (filterLayout.getVisibility() == RelativeLayout.GONE) {
            filterLayout.setVisibility(RelativeLayout.VISIBLE);
        } else {
            filterLayout.setVisibility(RelativeLayout.GONE);
        }
    }

    public Location getLocation() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
//			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//					!= PackageManager.PERMISSION_GRANTED
//					&&
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location lastKnownLocationGPS = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                Location loc = new Location(LocationManager.NETWORK_PROVIDER);
                loc.setLatitude(0);
                loc.setLongitude(0);
                return loc;
            }
        } else {
            return null;
        }
    }

    public void refresh_submit(final boolean isRefresh) {
        class JobTask extends AsyncTask<Void, Integer, ArrayList<Job>> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                refreshDate = false;
                Revamped_Loading_Dialog.show_dialog(
                        JobBoardActivityFragment.this, null);
                this.updateDate = null;
            }

            @Override
            protected void onPostExecute(ArrayList<Job> result) {
                try {
                    Revamped_Loading_Dialog.hide_dialog();

                } catch (Exception ex) {
                }
                if (result.size() != 0) {
                    isListEmpty = false;
                } else {
                    isListEmpty = true;
                }
                result = filterbyDistance(result);
                setMarkers(result, 0);
                setFilters(result);
//                setFilterNewDialog(result,filterDialog);
                result_filter = result;
            }

            @Override
            protected ArrayList<Job> doInBackground(Void... params) {
                if (lat1 > lat2) {
                    double temp = lat1;
                    lat1 = lat2;
                    lat2 = temp;

                }
                if (lng1 > lng2) {
                    double temp = lng1;
                    lng1 = lng2;
                    lng2 = temp;

                }
                ArrayList<Job> jobss = getRecord();
                if (isRefresh == false && (jobss != null && jobss.size() > 0))
                    return jobss;
                String data = BoardListPost();
                if (data.contains("<script>")) {
                    doLogin();
                    data = BoardListPost();
                }
                Parser parser = new Parser();
                for (int i = 0; i <= 9; i++) {
                    data = data.replace("<" + i, "<job" + i);
                    data = data.replace("</" + i, "</job" + i);
                }
                parser.parseXMLValues("<jobs>" + data + "</jobs>",
                        Constants.JOB_RESP_FIELD_PARAM);

                // Save Array list here

                saveRecord(name_of_file, parser.jobBoardList);
                return parser.jobBoardList;
            }
        }
        JobTask jobtaskobj = new JobTask();
        jobtaskobj.execute();
    }

    public void setFilters(final ArrayList<Job> result) {
        ArrayList<NameValuePair> clients = new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> locations = new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> branches = new ArrayList<NameValuePair>();
        ArrayList<NameValuePair> props = new ArrayList<NameValuePair>();
        // clients = insertNewItem("Not Selected", clients, "0");
        // locations = insertNewItem("Not Selected", locations, "0");
        for (int i = 0; i < result.size(); i++) {

            for (int j = 0; result.get(i).getBranchProps() != null
                    && j < result.get(i).getBranchProps().size(); j++) {
                if (result.get(i).getBranchProps().get(j) != null
                        && result.get(i).getBranchProps().get(j)
                        .getPropertyName().length() > 0) {
                    props = insertProp(result.get(i).getBranchProps().get(j)
                            .getPropertyName(), result.get(i).getBranchProps()
                            .get(j).getContent(), props, (i + 1) + "");
                }
            }

            if (result.get(i).getClientName() != null
                    && result.get(i).getClientName().length() > 0) {
                clients = insertNewItem(result.get(i).getClientName(), clients,
                        (i + 1) + "");
            }
            if (result.get(i).getBranchName() != null
                    && result.get(i).getBranchName().length() > 0) {
                branches = insertNewItem(result.get(i).getBranchName(),
                        branches, (i + 1) + "");
            }

            if (result.get(i).getRegionName() != null
                    && result.get(i).getRegionName().length() > 0
                    && result.get(i).getCityName() != null
                    && result.get(i).getCityName().length() > 0) {
                locations = insertNewItem(result.get(i).getCityName() + " ("
                        + result.get(i).getRegionName() + ")", locations, (i + 1)
                        + "");
            }

        }

        ClientFilter = (RelativeLayout) findViewById(R.id.ClientFilter);
        multipleClientSpinner = getMultipleDropdown(toStringArray(clients),
                ClientFilter, new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }, multipleClientSpinner);
        propsFilter = (RelativeLayout) findViewById(R.id.propsFilter);
        multiplePropsSpinner = getMultipleDropdown(toStringArray(props),
                propsFilter, new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, multiplePropsSpinner);

        branchCodeFilter = (RelativeLayout) findViewById(R.id.branchCodeFilter);
        multipleBranchCodeSpinner = getMultipleDropdown(
                toStringArray(branches), branchCodeFilter,
                new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, multipleBranchCodeSpinner);

        branchFilter = (RelativeLayout) findViewById(R.id.branchFilter);
        multipleBranchSpinner = getMultipleDropdown(toStringArray(locations),
                branchFilter, new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, multipleBranchSpinner);

//        ClientFilter.setVisibility(RelativeLayout.VISIBLE);
//        branchFilter.setVisibility(RelativeLayout.INVISIBLE);

//        branchCodeFilter.setVisibility(RelativeLayout.INVISIBLE);
//        if (customLayout.getVisibility() == RelativeLayout.VISIBLE) {
//            customLayout.setVisibility(RelativeLayout.GONE);
//        }
        branchFilterT.setTextColor(Color.parseColor("#000000"));
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)) {
            ClientFilterT.setTextColor(Color.parseColor(Helper.appColor));
        } else
            ClientFilterT.setTextColor(Color.parseColor("#007BFF"));
//        ClientTxtBottom.setVisibility(RelativeLayout.VISIBLE);
//        branchTxtBottom.setVisibility(RelativeLayout.INVISIBLE);
//        ClientFilter.setVisibility(RelativeLayout.VISIBLE);
//        branchFilter.setVisibility(RelativeLayout.INVISIBLE);

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                multipleBranchSpinner.setSelection(-1);
                multipleClientSpinner.setSelection(-1);
                multipleBranchCodeSpinner.setSelection(-1);
                multiplePropsSpinner.setSelection(-1);
                pickerView.unSetDates();
                nextSevenDays.performClick();
                refresh_submit(true);
                showhide_filter();
            }
        });
        btnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.setVisibility(RelativeLayout.GONE);
                if (refreshDate) {
                    refresh_submit(true);
                } else {
                    filterItems(multipleBranchSpinner.getSelectedStrings(),
                            multipleClientSpinner.getSelectedStrings(),
                            multipleBranchCodeSpinner.getSelectedStrings(),
                            multiplePropsSpinner.getSelectedStrings(), result);
                }
                showhide_filter();
            }
        });

    }

    protected void filterItems(List<String> selectedBranches,
                               List<String> selectedClients, List<String> selectedBranchReal,
                               List<String> selectedProps, ArrayList<Job> result) {
        ArrayList<Job> fileredResultList = null;
        if (selectedBranchReal.size() == 0 && selectedProps.size() == 0
                && selectedBranches.size() == 0 && selectedClients.size() == 0)
            fileredResultList = result;
        else {
            fileredResultList = new ArrayList<Job>();

            for (int i = 0; i < result.size(); i++) {
                boolean isOkay = false;
                isOkay = checkExistance(selectedClients, result.get(i)
                        .getClientName());
                if (isOkay)
                    isOkay = checkExistance(selectedBranches, result.get(i)
                            .getCityName()
                            + " ("
                            + result.get(i).getRegionName() + ")");
                if (isOkay)
                    isOkay = checkExistance(selectedBranchReal, result.get(i)
                            .getBranchName());
                if (isOkay)
                    isOkay = checkExistance(selectedProps, result.get(i)
                            .getBranchProps());

                if (isOkay)
                    fileredResultList.add(result.get(i));
            }
        }
        fileredResultList = filterbyDistance(fileredResultList);
        if (fileredResultList.size() > 0) {
            isListEmpty = false;
        } else {
            isListEmpty = true;
        }
        setMarkers(fileredResultList, 0);
    }

    private boolean checkExistance(List<String> selectedProps,
                                   ArrayList<BranchProperties> branchProps) {

        if (selectedProps.size() == 0)
            return true;
        if (branchProps == null || branchProps.size() == 0)
            return false;

        for (int i = 0; i < selectedProps.size(); i++) {

            for (int j = 0; j < branchProps.size(); j++) {

                if (selectedProps.get(i) != null
                        && branchProps.get(j) != null
                        && branchProps.get(j).getPropertyName() != null
                        && branchProps.get(j).getContent() != null
                        && selectedProps.get(i)
                        .equals((branchProps.get(j).getPropertyName()
                                + " - " + branchProps.get(j)
                                .getContent()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Job> filterbyDistance(ArrayList<Job> fileredResultList) {
        for (int i = 0; i < fileredResultList.size(); i++) {
            for (int j = i + 1; j < fileredResultList.size(); j++) {
                float Distance1 = calculateDistance(fileredResultList.get(i)
                        .getBranchLat(), fileredResultList.get(i)
                        .getBranchLong());
                float Distance2 = calculateDistance(fileredResultList.get(j)
                        .getBranchLat(), fileredResultList.get(j)
                        .getBranchLong());
                if (Distance1 > Distance2) {
                    Collections.swap(fileredResultList, i, j);
                }
            }
        }
        return fileredResultList;
    }

    private float calculateDistance(String branchLat, String branchLong) {
        // TODO Auto-generated method stub
        float distance = (float) -1.0;
        if (branchLat != null && branchLat.length() > 0 && branchLong != null
                && branchLong.length() > 0 && thisPersonLocaation != null) {
            Location loc1 = new Location("");
            loc1.setLatitude(Double.parseDouble(branchLat));
            loc1.setLongitude(Double.parseDouble(branchLong));
            distance = thisPersonLocaation.distanceTo(loc1);
        }

        return distance;
    }

    private boolean checkExistance(List<String> selectedClients,
                                   String clientName) {
        if (selectedClients.size() == 0)
            return true;
        for (int i = 0; i < selectedClients.size(); i++) {
            if (selectedClients.get(i) != null
                    && (selectedClients.get(i).equals(clientName))) {
                return true;
            }
        }
        return false;
    }

    private String[] toStringArray(ArrayList<NameValuePair> clients) {
        String[] namesArr = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            namesArr[i] = clients.get(i).getValue();
        }
        return namesArr;
    }

    private ArrayList<NameValuePair> insertProp(String PropName,
                                                String Content, ArrayList<NameValuePair> props, String id) {
        for (int i = 0; i < props.size(); i++) {
            if (props.get(i).getValue().equals(PropName + " - " + Content))
                return props;
        }

        props.add(findSimilarIndexOfProp(props, PropName),
                new BasicNameValuePair(id, PropName + " - " + Content));
        return props;
    }

    private int findSimilarIndexOfProp(ArrayList<NameValuePair> props,
                                       String propName) {
        for (int i = 0; i < props.size(); i++) {
            if (props.get(i).getValue().startsWith(propName))
                return i;
        }
        return 0;
    }

    private ArrayList<NameValuePair> insertNewItem(String clientName,
                                                   ArrayList<NameValuePair> clients, String id) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getValue().equals(clientName))
                return clients;
        }

        clients.add(new BasicNameValuePair(id, clientName));
        return clients;
    }

    public void apply(final String comment, final String orderid,
                      final Dialog dialog, final Marker arg0, final Job thiItem,
                      final boolean bulkApply) {
        class JobTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                dialogg = new ProgressDialog(JobBoardActivityFragment.this);
                dialogg.setMessage(JobBoardActivityFragment.this.getResources()
                        .getString(R.string.s_item_column_0_line_804_file_88));
                dialogg.setCancelable(false);
                dialogg.show();
                this.updateDate = null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    dialogg.dismiss();

                } catch (Exception ex) {
                }
                // <status>0</status>
                // <auto_approve_flag>1</auto_approve_flag>
                if (result != null && result.contains("<status>0</status>")) {
                    try {
                        if (dialog != null)
                            dialog.dismiss();

                    } catch (Exception ex) {
                    }

                    Toast.makeText(
                            JobBoardActivityFragment.this,
                            JobBoardActivityFragment.this
                                    .getResources()
                                    .getString(
                                            R.string.s_item_column_0_line_804_file_88),
                            Toast.LENGTH_SHORT).show();

                    arg0.setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.turqpin));
                    thiItem.setoaID("1");
                    thiItem.setApplicationComment(comment);
                    if (result.contains("1</auto_approve_flag>")) {
                        // job auto/self assigned
                        ShowYesNoAlert(
                                JobBoardActivityFragment.this,
                                JobBoardActivityFragment.this
                                        .getResources()
                                        .getString(
                                                R.string.s_item_column_0_line_838_file_88),
                                JobBoardActivityFragment.this
                                        .getResources()
                                        .getString(
                                                R.string.s_item_column_0_line_839_file_88),
                                "");
                    }
                    if (bulkApply == true && listOFOrders != null
                            && listOFOrders.size() > 0)
                        listOFOrders.remove(0);
                    else if (bulkApply == true && adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    if (bulkApply == true && listOFOrders != null
                            && listOFOrders.size() > 0) {
                        apply(comment,
                                ((Job) listOFOrders.get(0)).getOrderID(), null,
                                ((Job) listOFOrders.get(0)).getM(),
                                ((Job) listOFOrders.get(0)), true);
                    }

                } else {
                    Toast.makeText(
                            JobBoardActivityFragment.this,
                            JobBoardActivityFragment.this
                                    .getResources()
                                    .getString(
                                            R.string.s_item_column_0_line_859_file_88),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(Void... params) {

                String data = ApplyJob(comment, orderid);
                if (data.contains("<script>")) {
                    doLogin();
                    data = ApplyJob(comment, orderid);
                }
                return data;
            }
        }
        JobTask jobtaskobj = new JobTask();
        jobtaskobj.execute();
    }

    public void remove(final String comment, final String orderid,
                       final Dialog dialog, final Marker arg0, final Job thiItem,
                       final boolean bulkApply) {
        class JobTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                dialogg = new ProgressDialog(JobBoardActivityFragment.this);
                dialogg.setMessage(getResources().getString(
                        R.string.removingMsg));
                dialogg.setCancelable(false);
                dialogg.show();
                this.updateDate = null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    dialogg.dismiss();

                } catch (Exception ex) {
                }
                // <status>0</status>
                // <auto_approve_flag>1</auto_approve_flag>
                if (result != null && result.contains("<status>0</status>")) {
                    try {
                        if (dialog != null)
                            dialog.dismiss();

                    } catch (Exception ex) {
                    }

                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.removed),
                            Toast.LENGTH_SHORT).show();

                    arg0.setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.orangepin));
                } else {
                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.errorRemoving),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(Void... params) {

                String data = RemoveJob(comment, orderid);
                if (data.contains("<script>")) {
                    doLogin();
                    data = RemoveJob(comment, orderid);
                }
                return data;
            }
        }
        JobTask jobtaskobj = new JobTask();
        jobtaskobj.execute();
    }

    AlertDialog alertd = null;

    public void ShowYesNoAlert(Context context, String title,
                               final String message, String button_lbl) {
        if (alertd == null || !alertd.isShowing()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setCancelable(false);
            alert.setTitle(title);
            TextView textView = new TextView(context);
            textView.setTextSize(UIHelper.getFontSize(
                    JobBoardActivityFragment.this, textView.getTextSize()));
            textView.setText(Helper.makeHtmlString(message));
            alert.setView(textView);
            alert.setPositiveButton(
                    JobBoardActivityFragment.this.getResources().getString(
                            R.string.s_item_column_0_line_893_file_88),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent();
                            intent.putExtra(
                                    Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY,
                                    true);

                            if (getIntent() != null
                                    && getIntent().getExtras() != null
                                    && getIntent().getExtras().getBoolean(
                                    "service")) {
                                // open joblist here
                                intent.setClass(JobBoardActivityFragment.this,
                                        JobListActivity.class);
                                startActivity(intent);
                            } else {
                                // setResult(RESULT_OK, intent);

                            }
                            if (comunicator.JobList != null) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((JobListActivity) comunicator.JobList)
                                                .executeJobList(false, false);
                                    }
                                }, 2000);

                            }
                            finish();
                            try {
                                dialog.dismiss();
                            } catch (Exception ex) {

                            }
                        }
                    });
            alert.setNegativeButton(
                    JobBoardActivityFragment.this.getResources().getString(
                            R.string.s_item_column_0_line_923_file_88),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dialog.dismiss();
                            } catch (Exception ex) {

                            }
                        }
                    });
            alertd = alert.show();
        }
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

    public String doLogin() {
        try {
            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            return loginPost(
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
                    Constants.POST_VALUE_LOGIN_DO_LOGIN);
        } catch (Exception ex) {
            return null;
        }
    }

    public String doLogin(Context con) {
        SharedPreferences myPrefs = con.getSharedPreferences("pref",
                MODE_PRIVATE);
        return loginPost(
                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
                Constants.POST_VALUE_LOGIN_DO_LOGIN);
    }

    private String BoardListPost() {
        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        String app_ver = "";
        try {
            app_ver = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {

        }

        // http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
        name_of_file = Constants.getBoardListFile("9.7", gStartDate, gEndDate,
                lat1 + "", lng1 + "", lat2 + "", lng2 + "");
        return Connector.postForm(
                Constants.getBoardListURL("11.18", gStartDate, gEndDate, lat1
                        + "", lng1 + "", lat2 + "", lng2 + ""), extraDataList);
    }

    void swap(double a, double b) {
        double temp = a;
        a = b;
        b = temp;
    }

    private String ApplyJob(String comment, String orderId) {
        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        extraDataList.add(Helper
                .getNameValuePair("ApplicationComment", comment));
        extraDataList.add(Helper.getNameValuePair("OrderID", orderId));

        // String url = Helper.getSystemURL()
        // + "/c_pda-apply-order.php?json=1&ver=" + ver;
        return Connector.postForm(Constants.getBoardApplyURL("9.7"),
                extraDataList);
    }

    private String RemoveJob(String comment, String orderId) {
        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        extraDataList.add(Helper
                .getNameValuePair("ApplicationComment", comment));
        extraDataList.add(Helper.getNameValuePair("OrderID", orderId));

        // String url = Helper.getSystemURL()
        // + "/c_pda-apply-order.php?json=1&ver=" + ver;
        return Connector.postForm(Constants.getBoardRemoveURL("9.7"),
                extraDataList);
    }

    private String getAltDates(String orderId) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        return Connector.postForm(Constants.getAlternateDatesUrl(orderId),
                pairs);
    }

    private ArrayList<Job> getRecord() {
        ArrayList<Bounds> tempList = new ArrayList<Bounds>();
        ArrayList<Job> returnData = null;
        long unixTime = System.currentTimeMillis() / 1000L;

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getTimestamp() + 120 >= unixTime) {
                tempList.add(dataList.get(i));
                if (lat1 > dataList.get(i).getLat1()
                        && lat2 < dataList.get(i).getLat2()
                        && lng1 > dataList.get(i).getLng1()
                        && lng2 < dataList.get(i).getLng2()
                        && gEndDate.equals(dataList.get(i).getEndDate())
                        && gStartDate.equals(dataList.get(i).getStartDate())) {
                    returnData = dataList.get(i).getJob_array_list();
                }
            }
        }
        dataList = tempList;
        tempList = null;
        return returnData;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (googleMap != null &&
                intent != null && intent.getExtras().getDouble("lat1") > 0.0) {
            SharedPreferences myPrefs = getSharedPreferences("pref",
                    MODE_PRIVATE);
            Helper.setSystemURL(myPrefs.getString(
                    Constants.SETTINGS_SYSTEM_URL_KEY, ""));
            Helper.setAlternateSystemURL(myPrefs.getString(
                    Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.ENGLISH);
            Calendar cal = Calendar.getInstance();
            gStartDate = dateFormat.format(cal.getTime());
            gEndDate = dateFormat.format(cal.getTime());
            LatLng currentLocation = new LatLng(intent.getExtras().getDouble(
                    "lat1"), intent.getExtras().getDouble("lng1"));
            CameraUpdate center = CameraUpdateFactory
                    .newLatLng(currentLocation);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            CameraChanged(googleMap.getCameraPosition(), currentLocation);
        } else
            makeText(JobBoardActivityFragment.this, "Andorid OS seems to have deletd our data pelase restart app and open map screen.", Toast.LENGTH_LONG).show();
    }

    private void saveRecord(String nameOfFile, ArrayList<Job> records) {
        Bounds Objbound = new Bounds();
        Objbound.setLat1(lat1);
        Objbound.setLng1(lng1);
        Objbound.setLat2(lat2);
        Objbound.setLng2(lng2);
        Objbound.setStartDate(gStartDate);
        Objbound.setEndDate(gEndDate);
        Objbound.setJob_array_list(records);
        Objbound.setTimestamp(System.currentTimeMillis() / 1000L);
        dataList.add(Objbound);
    }

    private void drawCircle(Location point, int radius, boolean isRed) {

        isRed = true;
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(new LatLng(point.getLatitude(), point
                .getLongitude()));

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        if (isRed)
            circleOptions.strokeColor(0x30ff0000);
        else
            circleOptions.strokeColor(0x3000ff00);

        // Fill color of the circle
        if (isRed)
            circleOptions.fillColor(0x30cc0000);
        else
            circleOptions.fillColor(0x3000cc00);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

    }

    public void addPersonMarker(Location l) {
        try {
            if (jobList.getVisibility() == RelativeLayout.GONE) {
                locationBtn.setVisibility(RelativeLayout.VISIBLE);
            }
            thisPersonLocaation = l;
            MarkerOptions markerOptions = new MarkerOptions();

            double latitude = Double.valueOf(l.getLatitude());
            double longitude = Double.valueOf(l.getLongitude());
            LatLng latlng = new LatLng(latitude, longitude);
            // Setting the position for the marker
            markerOptions.position(latlng);

            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.green_pin));

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title("You");

            // Animating to the touched position
            // googleMap.animateCamera(CameraUpdateFactory
            // .newLatLng(latlng));

            // Placing a marker on the touched position
            if (thisPersonMarker == null)
                thisPersonMarker = googleMap.addMarker(markerOptions);
            else
                thisPersonMarker.setPosition(latlng);

        } catch (Exception ex) {

        }
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private List<Job> setMapView(List<Job> orders, int index) {
        // Clears the previously touched position
        if (googleMap != null) {
            try {
                googleMap.clear();
            } catch (Exception ex) {

            }
        }
        thisPersonMarker = null;
        if (thisPersonLocaation != null)
            addPersonMarker(thisPersonLocaation);
        for (int i = 0; i < orders.size(); i++) {
            try {
                MarkerOptions markerOptions = new MarkerOptions();

                double latitude = Double.valueOf(orders.get(i).getBranchLat());
                double longitude = Double
                        .valueOf(orders.get(i).getBranchLong());
                LatLng latlng = new LatLng(latitude, longitude);
                // Setting the position for the marker
                markerOptions.position(latlng);
                if (orders.get(i).getColor() != null
                        && orders.get(i).getColor().length() > 0) {
                    markerOptions.icon(getMarkerIcon(orders.get(i).getColor()));
                } else if (orders.get(i).getoaID() != null
                        && orders.get(i).getoaID().length() > 0) {
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.turqpin));
                } else if (orders.get(i).getCertificates() != null
                        && orders.get(i).getCertificates().size() > 0) {
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.red_marker_small));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.orangepin));
                }
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latlng.latitude + " : " + latlng.longitude);

                if (markersHash == null)
                    markersHash = new HashMap<Marker, Job>();
                if ((orderid == null || orderid.equals("-1"))
                        || orders.get(i).getOrderID().equals(orderid)) {
                    // Animating to the touched position
                    // googleMap.animateCamera(CameraUpdateFactory
                    // .newLatLng(latlng));

                    // Placing a marker on the touched position
                    Marker thismarker = googleMap.addMarker(markerOptions);
                    orders.get(i).setM(thismarker);
                    markersHash.put(thismarker, orders.get(i));

                    // Setting a custom info window adapter for the google map
                    googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

                        @Override
                        public View getInfoContents(Marker arg0) {
                            // Getting view from the layout file
                            // info_window_layout
                            View v = getLayoutInflater().inflate(
                                    R.layout.custom_marker, null);

                            TextView tvBranch = (TextView) v
                                    .findViewById(R.id.txtbranch);

                            ImageView imgNext = (ImageView) v
                                    .findViewById(R.id.btnopen);

                            final Job thiItem = markersHash.get(arg0);

                            if (thiItem != null) {
                                tvBranch.setText(thiItem.getBranchFullname());
                                openDialog(thiItem, arg0);
                            }

                            return v;
                        }

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                    });

                    googleMap
                            .setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {

                                    Job thiItem = markersHash.get(marker);

                                }
                            });

                }
            } catch (Exception ex) {

            }
        }
        return orders;
    }

    protected void CameraChanged(CameraPosition arg0, final LatLng drawCircle) {

        SouthWestCorner = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest;
        NorthEastCorner = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;

        lat1 = NorthEastCorner.latitude;
        lng1 = NorthEastCorner.longitude;
        lat2 = SouthWestCorner.latitude;
        lng2 = SouthWestCorner.longitude;

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isFinishing()) {
                    // dont do anything activity is finished
                } else {
                    if (googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude == lat2
                            && googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude == lat1) {

                        if (dialog == null || !dialog.isShowing()) {
                            // RadioButton dateRBtn = (RadioButton)
                            // findViewById(R.id.radioCity);
                            // dateRBtn.setChecked(true);
                            refresh_submit(false);
                        }

                    }
                }
            }
        }, 1800);

        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {

                CameraChanged(arg0, null);
            }
        });
    }

    JobBoardListAdapter adapter = null;

    public void setMarkers(List<Job> orders, int index) {
        orders = setMapView(orders, index);
        if (getIntent() != null
                && getIntent().getExtras().getDouble("lat1") > 0.0) {

            LatLng currentLocation = new LatLng(getIntent().getExtras()
                    .getDouble("lat1"), getIntent().getExtras().getDouble(
                    "lng1"));
            Location l = new Location("");
            l.setLatitude(currentLocation.latitude);
            l.setLongitude(currentLocation.longitude);
            drawCircle(l, 500, false);
        }
        adapter = new JobBoardListAdapter(JobBoardActivityFragment.this,
                orders, jobBoardlistAdapter_CallBack, thisPersonLocaation,
                btnApplyAll);
        jobList.setAdapter(adapter);

        if (orders.size() <= 0
                && jobList.getVisibility() == RelativeLayout.VISIBLE) {
            bottomBar.setVisibility(RelativeLayout.INVISIBLE);
            emptyListLabel.setVisibility(RelativeLayout.VISIBLE);
            ll_show_jobs_counts.setVisibility(View.GONE);
            ll_select_clear.setVisibility(View.GONE);
        }

    }

    public void openListForCertainPins() {
        ArrayList<Job> listPins = new ArrayList<Job>();

        if (adapter != null && adapter.values != null
                && adapter.values.size() > 0) {
            for (int i = 0; i < adapter.values.size(); i++) {
                if (adapter.values.get(i) != null) {
                    double latitude = Double.valueOf(adapter.values.get(i)
                            .getBranchLat());
                    double longitude = Double.valueOf(adapter.values.get(i)
                            .getBranchLong());
                    if (latitude >= lat1 && latitude <= lat2
                            && longitude >= lng1 && longitude <= lng2) {
                        listPins.add(adapter.values.get(i));
                    }
                }
            }
        }
        Helper.changeBtnColor(btnApplyAll);
        Helper.changeBtnColor(btnCancel);
        adapter = new JobBoardListAdapter(JobBoardActivityFragment.this,
                listPins, jobBoardlistAdapter_CallBack, thisPersonLocaation,
                btnApplyAll);
        jobList.setAdapter(adapter);
        if (listPins.size() <= 0
                && jobList.getVisibility() == RelativeLayout.VISIBLE) {
            bottomBar.setVisibility(RelativeLayout.INVISIBLE);
            emptyListLabel.setVisibility(RelativeLayout.VISIBLE);
            ll_show_jobs_counts.setVisibility(View.GONE);
            ll_select_clear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (filterLayout.getVisibility() == RelativeLayout.VISIBLE) {
            filterLayout.setVisibility(RelativeLayout.GONE);
        } else if (getIntent() != null && getIntent().hasExtra("lat1")) {
            ExitFromJobList();
        } else {
            finish();
        }
    }

    private void ExitFromJobList() {
        Intent intent = new Intent(this.getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
        finish();

    }

    Dialog dialog = null;
    private Spinner altSpinner;

    public void openDialog(final Job thiItem, final Marker arg0) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (JobBoardActivityFragment.jobboardListener != null)
                            JobBoardActivityFragment.jobboardListener
                                    .certCallBack(thiItem.getCertificates());
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        if (thiItem != null && thiItem.getoaID() != null && thiItem.getoaID().length() > 0) {

        } else if (
                thiItem != null && thiItem.getCertificates() != null
                        && thiItem.getCertificates().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    JobBoardActivityFragment.this);
            builder.setCancelable(false);
            builder.setMessage(
                            getResources().getString(R.string.attached_certificate_msg))
                    .setPositiveButton(getResources().getString(R.string.questionnaire_exit_delete_alert_yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.questionnaire_exit_delete_alert_no), dialogClickListener)
                    .setCancelable(false).show();

            return;
        }

        orders = null;
        dialog = new Dialog(JobBoardActivityFragment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Constants.getLoginURL() != null && Constants.getLoginURL().toLowerCase().contains("ajis"))
            dialog.setContentView(R.layout.job_board_dialog_ajis);
        else dialog.setContentView(R.layout.job_board_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(lp.width, lp.height);

        TextView clientName, cityName, branchName, address, description, qustionnaire, shortname, addedAt, startAndndTime, BranchOpeingHours, SurveyPayment, BonusPayment, TarnsportionPayment, branchphone;
        RelativeLayout topbar;
        topbar = (RelativeLayout) dialog.findViewById(R.id.topbar);

        if (thiItem == null) {
            topbar.setBackgroundColor(Color.parseColor("#f18931"));
            dialog.findViewById(R.id.layout_1).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_2).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_3).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_4).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_5).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_6).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_7).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_8).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_9).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_10).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_11).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_12).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_13).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.layout_14).setVisibility(
                    RelativeLayout.GONE);
            dialog.findViewById(R.id.altlayout).setVisibility(
                    RelativeLayout.GONE);
        } else {

            altSpinner = (Spinner) dialog.findViewById(R.id.altdates);
            altSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
            // setOnItemClickListener(new OnItemClickListener() {
            //
            // @Override
            // public void onItemClick(AdapterView<?> parent, View view,
            // int position, long id) {
            // if (thiItem.getAltDates() == null
            // || thiItem.getAltDates().size() == 0) {
            // LoadALTData(thiItem.getOrderID(), altSpinner);
            // }
            // }
            // });
            clientName = (TextView) dialog.findViewById(R.id.ClientName);
            branchName = (TextView) dialog.findViewById(R.id.BranchName);
            address = (TextView) dialog.findViewById(R.id.Address);
            description = (TextView) dialog.findViewById(R.id.Description);
            qustionnaire = (TextView) dialog.findViewById(R.id.Qustionnaire);
            shortname = (TextView) dialog.findViewById(R.id.ShortName);
            startAndndTime = (TextView) dialog
                    .findViewById(R.id.StartAndEndTime);
            addedAt = (TextView) dialog.findViewById(R.id.AddedAt);
            cityName = (TextView) dialog.findViewById(R.id.CityName);
            BranchOpeingHours = (TextView) dialog
                    .findViewById(R.id.BranchOpeingHours);
            branchphone = (TextView) dialog.findViewById(R.id.BranchPhone);
            SurveyPayment = (TextView) dialog.findViewById(R.id.SurveyPayment);
            BonusPayment = (TextView) dialog.findViewById(R.id.BonusPayment);
            TarnsportionPayment = (TextView) dialog
                    .findViewById(R.id.TarnsportionPayment);

            if (thiItem.getoaID() != null && thiItem.getoaID().length() > 0) {
                topbar.setBackgroundColor(Color.parseColor("#2cbdbf"));
                dialog.findViewById(R.id.btnApply).setBackgroundColor(
                        Color.parseColor("#a7a9ab"));
                ((TextView) dialog.findViewById(R.id.btnApply))
                        .setText(JobBoardActivityFragment.this
                                .getResources()
                                .getString(
                                        R.string.s_item_column_0_line_145_file_223));
                // dialog.findViewById(R.id.btnApply).setEnabled(false);
                ((TextView) dialog.findViewById(R.id.btnApply))
                        .setTag("Remove");
                dialog.findViewById(R.id.altlayout).setVisibility(
                        RelativeLayout.GONE);
            } else {
                topbar.setBackgroundColor(Color.parseColor("#f18931"));
                LoadALTData(thiItem.getOrderID(), altSpinner, null);

            }
            topbar.setBackgroundColor(Color.parseColor(thiItem.getColor()));

            // if (thiItem.getoaID() != null && thiItem.getoaID().length() > 0)
            // {
            // topbar.setBackgroundColor(Color.parseColor("#007fff"));
            // } else {
            // topbar.setBackgroundColor(Color.parseColor("#ff0000"));
            // }
            clientName.setText(thiItem.getClientName());
            branchName.setText(thiItem.getBranchFullname());
            cityName.setText(thiItem.getCityName());
            address.setText(thiItem.getAddress());

            description.setText(stripHtml(thiItem.getDescription()));
            qustionnaire.setText(thiItem.getSetName());
            shortname.setText(thiItem.getBranchName());
            if (thiItem.getStart_time() == null || thiItem.getTimeEnd() == null) {
                if (thiItem.getStart_time() == null) {
                    startAndndTime.setText("" + "-" + thiItem.getTimeEnd());
                } else {
                    startAndndTime.setText(thiItem.getStart_time() + "-" + "");
                }
            } else {
                startAndndTime.setText(thiItem.getStart_time() + "-"
                        + thiItem.getTimeEnd());
            }
            BranchOpeingHours.setText(thiItem.getOpeningHours());
            addedAt.setText(thiItem.getDate());
            branchphone.setText(thiItem.getBranchPhone());
            SurveyPayment.setText(thiItem.getCriticismPayment());
            BonusPayment.setText(thiItem.getBonusPayment());
            TarnsportionPayment.setText(thiItem.getTransportationPayment());
        }
        dialog.findViewById(R.id.xbutton).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception ex) {

                        }

                    }
                });
        final EditText txtComment = (EditText) dialog
                .findViewById(R.id.txtComment);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);
        Helper.changeBtnColor(btnApply);
        btnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String ButtonTitle = ((Button) dialog
                        .findViewById(R.id.btnApply)).getTag().toString();
                if (ButtonTitle.equals("Apply")) {
                    if (thiItem == null) {
                        apply(txtComment.getText().toString(),
                                ((Job) listOFOrders.get(0)).getOrderID(),
                                dialog, ((Job) listOFOrders.get(0)).getM(),
                                ((Job) listOFOrders.get(0)), true);
                    } else {
                        if (orders != null
                                && orders.length > 1
                                && altSpinner != null
                                && altSpinner.getSelectedItemPosition() > 0
                                && altSpinner.getSelectedItemPosition() < orders.length) {
                            String date = orders[altSpinner
                                    .getSelectedItemPosition()];
                            if (date != null && date.length() > 0) {
                                date = date.replace(".", "-");
                            }
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                    "dd-MM-yyyy", Locale.ENGLISH);
                            try {
                                Date dateTemp = simpleDateFormat.parse(date);
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
                                        "yyyy-MM-dd", Locale.ENGLISH);
                                date = simpleDateFormat1.format(dateTemp);
                                // date =
                                // simpleDateFormat1.parse(date2);

                            } catch (ParseException ex) {
                            }
                            Toast.makeText(
                                            getApplicationContext(),
                                            txtComment.getText().toString() + " " + "("
                                                    + date + ")", Toast.LENGTH_LONG)
                                    .show();
                            apply(txtComment.getText().toString() + " " + "("
                                            + date + ")", thiItem.getOrderID(), dialog,
                                    arg0, thiItem, false);
                        } else {
                            apply(txtComment.getText().toString(),
                                    thiItem.getOrderID(), dialog, arg0,
                                    thiItem, false);
                        }
                    }
                    refresh_submit(true);
                } else {
                    remove(txtComment.getText().toString() + " ",
                            thiItem.getOrderID(), dialog, arg0, thiItem, false);
                    refresh_submit(true);

                }
            }
        });
        if (thiItem != null && thiItem.getoaID() != null
                && thiItem.getoaID().length() > 0) {
            txtComment.setText(thiItem.getApplicationComment());
            txtComment.setEnabled(false);
            ((Button) dialog.findViewById(R.id.btnApply))
                    .setText("Remove Application");
            // dialog.findViewById(R.id.btnApply).setEnabled(false);
        }
        dialog.show();
    }

    String[] orders = null;

    public void LoadALTData(final String orderID2, final Spinner spinner,
                            final Context con) {
        class JobTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;

            @Override
            protected void onPreExecute() {
                if (con != null) {
                    dialogg = new ProgressDialog(con);
                    dialogg.setMessage(con
                            .getString(R.string.s_item_column_0_line_1549_file_88));
                    dialogg.setCancelable(false);
                    dialogg.show();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (con != null)
                    dialogg.dismiss();
                if (orders != null && orders.length > 0) {
                    if (con != null) {
                        ArrayAdapter adapter = new ArrayAdapter(

                                con, R.layout.custom_spinner_row_zero, orders);
                        adapter.setDropDownViewResource(R.layout.custom_spinner_row_zero);

                        spinner.setAdapter(adapter);
                    } else {
                        ArrayAdapter adapter = new ArrayAdapter(

                                JobBoardActivityFragment.this,
                                R.layout.custom_spinner_row_zero, orders);
                        adapter.setDropDownViewResource(R.layout.custom_spinner_row_zero);

                        spinner.setAdapter(adapter);
                    }
                } else {

                }
            }

            @Override
            protected String doInBackground(Void... params) {

                String data = getAltDates(orderID2);
                if (data.contains("<script>")) {
                    if (con != null)
                        doLogin(con);
                    else
                        doLogin();
                    data = getAltDates(orderID2);
                }
                int count = 1;
                try {
                    JSONObject jsonobjs = new JSONObject(data);
                    if (jsonobjs.has("count")) {
                        count = jsonobjs.getInt("count") + 1;
                        orders = new String[count];
                        for (int i = 0; i < jsonobjs.getInt("count"); i++) {
                            orders[i + 1] = jsonobjs.getJSONObject(
                                    "alloweddays").getString("day" + i);
                        }


                    }
                } catch (Exception ex) {

                }
                if (orders == null) {
                    orders = new String[count];

                    if (con != null)
                        orders[0] = con.getResources().getString(
                                R.string.s_item_column_0_line_1594_file_88);
                    else
                        orders[0] = JobBoardActivityFragment.this
                                .getResources()
                                .getString(
                                        R.string.s_item_column_0_line_1594_file_88);
                }

                if (con != null)
                    orders[0] = con
                            .getString(R.string.s_item_column_0_line_1597_file_88);
                else
                    orders[0] = JobBoardActivityFragment.this.getResources()
                            .getString(
                                    R.string.s_item_column_0_line_1597_file_88);

                return data;
            }
        }
        JobTask jobtaskobj = new JobTask();
        jobtaskobj.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (JOB_GPS_CODE):
                startLocationChecker();
                break;
            case (JOB_DETAIL_ACTIVITY_CODE): {
                // if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle b = data.getExtras();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY,
                            b.getBoolean(Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY));
                    intent.putExtra(
                            Constants.JOB_DETAIL_IS_INVALID_LOGIN_FIELD_KEY,
                            b.getBoolean(Constants.JOB_DETAIL_IS_INVALID_LOGIN_FIELD_KEY));
                    intent.putExtra(Constants.DB_TABLE_QUESTIONNAIRE_ORDERID, b
                            .get(Constants.DB_TABLE_QUESTIONNAIRE_ORDERID)
                            .toString());
                    intent.putExtra(Constants.QUESTIONNAIRE_STAUS,
                            b.getInt(Constants.QUESTIONNAIRE_STAUS));

                    setResult(RESULT_OK, intent);

                    finish();
                } else
                    finish();
            }
        }

    }

    LocationListener locationlistenerN = null;
    LocationListener locationlistenerGPS = null;
    private ArrayList<Object> listOFOrders;

    private void startLocationChecker() {
        locationlistenerGPS = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                if (!locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.removeUpdates(this);
                    //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationlistenerN);
                }
            }

            @Override
            public void onLocationChanged(Location currentLocation) {
                if (!locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.removeUpdates(this);
                    //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationlistenerN);
                }
                newLocationFound(currentLocation);
            }
        };

        locationlistenerN = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.removeUpdates(this);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationlistenerGPS);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.removeUpdates(this);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationlistenerGPS);
                }
            }

            @Override
            public void onLocationChanged(Location currentLocation) {
                newLocationFound(currentLocation);
                //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.removeUpdates(this);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationlistenerGPS);
                }
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && !locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    JobBoardActivityFragment.this);
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
                                    try {

                                        dialog.dismiss();
                                    } catch (Exception ex) {

                                    }

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
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
//if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerN);

        } else {
            //if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            if (ActivityCompat.checkSelfPermission(JobBoardActivityFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, locationlistenerN);
        }

    }

    protected void newLocationFound(Location currentLocation) {
        if (thisPersonMarker == null && currentLocation != null
                && currentLocation.getLatitude() > 0.0
                && currentLocation.getLongitude() > 0.0) {
            isMovedAlready = true;
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
                    currentLocation.getLatitude(), currentLocation
                    .getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            if (getIntent() != null && getIntent().hasExtra("lat1")) {

            } else {
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                CameraChanged(googleMap.getCameraPosition(), null);

            }
            if (currentLocation != null) {
                addPersonMarker(currentLocation);
            }
        } else if (thisPersonMarker != null) {
            if (currentLocation != null) {
                addPersonMarker(currentLocation);
            }
        }

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

    private MultiSelectionSpinner getMultipleDropdown(String[] listAnswers,
                                                      RelativeLayout multiSpinner, OnItemSelectedListener listener,
                                                      MultiSelectionSpinner multiSpinnerIner) {
        if (multiSpinnerIner == null) {
            multiSpinnerIner = new MultiSelectionSpinner(
                    JobBoardActivityFragment.this, 0, null);
            multiSpinnerIner.setBackgroundColor(Color.TRANSPARENT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            multiSpinner.addView(multiSpinnerIner, params);
        }

        if (listAnswers.length > 0) {
            multiSpinnerIner.setId(0);
            multiSpinnerIner.setItems(listAnswers);
            multiSpinnerIner.setSelection(-1);
            multiSpinnerIner.setOnItemSelectedListener(listener);
        }
        return multiSpinnerIner;
    }

    public String stripHtml(String html) {
        if (html == null)
            return "";
        return Html.fromHtml(html).toString();
    }

    public void setUnSetBottomBar(ArrayList<Object> listOfCheckBoxes) {
        this.listOFOrders = listOfCheckBoxes;
        if (btnApplyAll != null && btnApplyAll.getText().toString() != null
                && btnApplyAll.getText().toString().contains("(")) {
            String text = btnApplyAll.getText().toString();
            text = text.substring(0, text.indexOf("("));

            btnApplyAll.setText(text);
        }
        btnApplyAll.setText(btnApplyAll.getText().toString() + "("
                + listOfCheckBoxes.size() + ")");
    }

    int tag = 0;// , d1, m1, y1, d2, m2, y2;
    String[] monthVal = {"01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12"};


    private DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date = getCurrentDate(dayOfMonth, monthOfYear, year);
            if (tag == 1) {
                s_date.setText(date);
                // d1 = dayOfMonth;
                // m1 = monthOfYear;
                // y1 = year;
            } else {
                e_date.setText(date);
                // d2 = dayOfMonth;
                // m2 = monthOfYear;
                // y2 = year;
            }
        }
    };

    private String getCurrentDate(int day, int month, int year) {
        String str = String.valueOf(day) + "-" + monthVal[month] + "-"
                + String.valueOf(year);
        return str;
    }

    private void openDatePicker() {
        Calendar cl = Calendar.getInstance();
        int y = cl.get(Calendar.YEAR);
        int m = cl.get(Calendar.MONTH);
        int d = cl.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(JobBoardActivityFragment.this, setDate, y, m, d);
        dp.show();
    }

}

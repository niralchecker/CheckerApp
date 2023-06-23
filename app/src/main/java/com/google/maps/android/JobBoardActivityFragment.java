package com.google.maps.android;

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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import phlox.datepick.CalendarNumbersView.DateSelectionListener;
import phlox.datepick.CalendarPickerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.adapter.CheckertificateAdapter;
import com.checker.sa.android.adapter.JobBoardListAdapter;
import com.checker.sa.android.adapter.JobBoardlistAdapter_CallBack;
import com.checker.sa.android.adapter.JobItemAdapter;
import com.checker.sa.android.adapter.sideMEnuAdapter;
import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.ArchiveData;
import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Bounds;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.CountryLatLng;
import com.checker.sa.android.data.Expiration;
import com.checker.sa.android.data.InProgressAnswersData;
import com.checker.sa.android.data.Job;
import com.checker.sa.android.data.ListClass;
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
import com.checker.sa.android.data.validationSets;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.JobFilterDialog;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.LanguageDialog;
import com.checker.sa.android.helper.MultiSelectionSpinner;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.LocationGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.mor.sa.android.activities.ArchiveActivity;
import com.mor.sa.android.activities.CritHistoryReportActivity;
import com.mor.sa.android.activities.JobDetailActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.LoginActivity;
import com.mor.sa.android.activities.NewDashboardScreenActivity;
import com.mor.sa.android.activities.NewLoginActivity;
import com.mor.sa.android.activities.NewSettingsActivity;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.ShopperRefundReportActivity;
import com.mor.sa.android.activities.SplashScreen;
import com.mor.sa.android.activities.comunicator;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class JobBoardActivityFragment extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final int JOB_DETAIL_ACTIVITY_CODE = 321;
    private GoogleMap googleMap;
    private HashMap<Marker, Job> markersHash;
    private String orderid, client, city, branch, prop;
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
    private Button btnApplyAll, btnRemoveAll;
    private RelativeLayout branchCodeFilter;
    private RelativeLayout propsFilter;
    private RelativeLayout branchTxtPropsView;

    private static jobBoardCertsListener jobboardListener;
    private FrameLayout framView;
    private RelativeLayout layout_map;
    private SupportMapFragment mapfragment;
    private Location currentLot;
    private int resultCode;

    ImageView iv_list, iv_map, filterBtn;

    TextView tv_select_all, tv_clearer_all;

    CheckBox checkbox_today, checkbox_next_day;
    TextView s_date, e_date, tv_clients;

    Dialog dialog = null;
    private Spinner altSpinner;
    //TODO Menu
    private String cert;
    private String certOrdeId;
    Revamped_Loading_Dialog dialog1;
    ArrayList<SubmitQuestionnaireData> sqd;
    SimpleDateFormat sdf;// = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ArrayList<orderListItem> jobs_CAPI = new ArrayList<orderListItem>();
    TextView tv_waiting_acceptance, tv_waiting_implementation, tv_CAPI_assigned, tv_CAPI_inProgress, tv_CAPI_returned, tv_checker_passed, tv_checker_toBePassed;
    String capi_assigned_count, capi_status_inProgress, capi_status_returned, my_jobs_accept, my_jobs_implement;
    private GoogleApiClient mApiClient;
    private boolean isWifiOnly = false;
    boolean upload_comp_jobs = false, isJobselected = false;
    ArrayList<Order> jobordersss = null;
    private boolean isBranchPropErr;
    public static ArrayList<orderListItem> joborders;
    public static List<orderListItem> filtered;
    public static ArrayList<orderListItem> filtered_other_jobs;
    private JobItemAdapter mAdapter;
    private ShowDBListTask showListTaskHandler;

    private static final String START_ACTIVITY = "/start_activity";
    private static final String STOP_UPLOAD = "/stop_upload";
    private static final String STOP_DOWNLOAD = "/stop_download";
    ArrayList<BranchProperties> branchProps = null;

    JobBoardActivityFragment.JobbListTask jobListTaskHandler = new JobBoardActivityFragment.JobbListTask(false, false);

    private final int JOB_ARCHIVE_ACTIVITY_CODE = 69;
    Parser parser;
    private View menuView;
    private boolean isMenuOpen = false;
    private ListView menuListView;
    private ArrayList<com.checker.sa.android.data.MenuItem> menuItems;

    //TODO MENU
    public static void setJobBardCallback(jobBoardCertsListener dateCallback) {
        JobBoardActivityFragment.jobboardListener = dateCallback;
    }

    SharedPreferences myPrefs;

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

    TextView tv_applied_no;

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
        tv_clients = findViewById(R.id.tv_clients);
        tv_applied_no = findViewById(R.id.tv_applied_no);
        tv_applied_no.setText(Integer.toString(Constants.applied_count));


        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
//        TODO MENU
        isWifiOnly = myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false);

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
        btnRemoveAll = (Button) findViewById(R.id.btnRemoveAll);
        btnApplyAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listOFOrders != null && listOFOrders.size() > 0) {
                    openDialog(null, null, -1);
                }
            }
        });

        btnRemoveAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listOFOrders != null && listOFOrders.size() > 0) {
//                    for (int i = 0; i < listOFOrders.size(); i++) {
//                        ((Job) listOFOrders.get(i)).getM().setIcon(BitmapDescriptorFactory
//                                .fromResource(R.drawable.orangepin));
//                    }
                    openDialog(null, null, -2);
                    doEnableDisableMapListButtons();
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
        tvMap = (TextView) findViewById(R.id.tvMap);
        tvList = (TextView) findViewById(R.id.tvList);
        locationBtn = (ImageView) findViewById(R.id.LocationBtn);
        iv_list = (ImageView) findViewById(R.id.iv_list);
        iv_map = (ImageView) findViewById(R.id.iv_map);
        filterBtn = (ImageView) findViewById(R.id.filterBtn);
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

                Log.e("PassIndex", "true");
                int id2 = index;
                // Toast.makeText(getApplicationContext(), id2+"", 400).show();
                openDialog(adapter.getValues().get(id2), adapter.getValues()
                        .get(id2).getM(), 0);

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
//                Log.e("gStartDate_today_checkBox", gStartDate);
//                Log.e("gEndDate_today_checkBox", gEndDate);

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
//                Log.e("gStartDate_nextSevenDays", gStartDate);
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
        mapfragment = new SupportMapFragment();
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

        // TODO Menu
        menuView = findViewById(R.id.view_side_menu);
        menuListView = findViewById(R.id.view_side_menu_list_view);
        String userName = myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, "");

        TextView tv =  findViewById(R.id.txtsettingmenu);
        String welcome = getString(R.string.login_heder);
        tv.setText(welcome + " " + userName);

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == RelativeLayout.VISIBLE) {
                    filterLayout.setVisibility(RelativeLayout.GONE);
                } else if (getIntent() != null && getIntent().hasExtra("lat1")) {
                    ExitFromJobList();
                }
                if (isMenuOpen) {
                    isMenuOpen = false;
                    menuView.setVisibility(RelativeLayout.GONE);
                } else {
                    isMenuOpen = true;
                    menuListView.setAdapter(new sideMEnuAdapter(JobBoardActivityFragment.this,
                            menuItems));
                    menuView.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // if (!Constants.isQAAllowed)
                // position++;
                if (Helper.getSystemURL() != null
                        && !Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)
                        && Constants.isHS()
                        && onOptionsItemSelectedHS(position)) {
                    menuView.setVisibility(RelativeLayout.GONE);
                    isMenuOpen = false;
                } else if (Helper.getSystemURL() != null
                        && !Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)
                        && onOptionsItemSelected(position)) {
                    menuView.setVisibility(RelativeLayout.GONE);
                    isMenuOpen = false;
                } else if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)
                        && Helper.isMisteroMenu
                        && onOptionsItemSelectedMistero(position)) {
                    menuView.setVisibility(RelativeLayout.GONE);
                    isMenuOpen = false;
                } else if (Helper.getSystemURL() != null
                        && Helper.getSystemURL().toLowerCase()
                        .contains(Helper.CONST_BE_THERE)
                        && !Helper.isMisteroMenu
                        && onOptionsItemSelectedHS(position)) {
                    menuView.setVisibility(RelativeLayout.GONE);
                    isMenuOpen = false;
                }
            }
        });
        findViewById(R.id.view_side_menu_top_green).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                });

        findViewById(R.id.view_side_menu_side_black).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        menuView.setVisibility(RelativeLayout.GONE);
                        isMenuOpen = false;
                    }
                });

        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuView.setVisibility(RelativeLayout.GONE);
                isMenuOpen = false;
            }
        });
        menuItems = new ArrayList<com.checker.sa.android.data.MenuItem>();

//        synchMenuItems = new ArrayList<com.checker.sa.android.data.MenuItem>();
//        synchMenuItems
//                .add(new com.checker.sa.android.data.MenuItem(
//                        getString(R.string.start_download),
//                        getString(R.string.start_download),
//                        getIcon("downoad_ico")));// 0
//        synchMenuItems.add(new com.checker.sa.android.data.MenuItem(
//                getString(R.string.start_upload),
//                getString(R.string.start_upload), getIcon("upload_ico")));// 1
        menuItems
                .add(new com.checker.sa.android.data.MenuItem(
                        getString(R.string.job_list_home),
                        getString(R.string.job_list_home),
                        getIcon("home")));// dash board
        if (Constants.isQAAllowed)
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.questionnaire_send_bug),
                    getString(R.string.questionnaire_send_bug),
                    R.drawable.action_search)); // 0

        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems
                    .add(new com.checker.sa.android.data.MenuItem(
                            getString(R.string.start_download),
                            getString(R.string.start_download),
                            getIcon("downoad_ico")));// 0
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.start_upload),
                    getString(R.string.start_upload), getIcon("upload_ico")));// 1
        if (Constants.isHS()
                || (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE) && Helper.isMisteroMenu)) {
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.settings_job_board),
                    getString(R.string.settings_job_board),
                    getIcon("job_board")));// 2

            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.settings_refund_report),
                    getString(R.string.settings_refund_report),
                    getIcon("refund")));// 3
        }
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.start_download_alt),
                    getString(R.string.start_download_alt),
                    getIcon("downoad_alt")));// 4
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.settings_page_title),
                    getString(R.string.settings_page_title), getIcon("gear")));// 5

//        menuItems.add(new com.checker.sa.android.data.MenuItem(
//                getString(R.string.job_list_menu_upload_complete_job),
//                getString(R.string.job_list_menu_upload_complete_job),
//                getIcon("filterjobs")));// 6

        menuItems.add(new com.checker.sa.android.data.MenuItem(
                getString(R.string.job_list_menu_update_list),
                getString(R.string.job_list_menu_update_list),
                getIcon("worldmap")));// 7
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.start_checkertificates),
                    getString(R.string.start_checkertificates),
                    R.drawable.checkerificate));// 8
        // if (Helper.getSystemURL() != null
        // && Helper.getSystemURL().toLowerCase()
        // .contains(Helper.CONST_BE_THERE)
        // && Helper.isMisteroMenu) {
        // }
        // else
        // menuItems.add(new com.checker.sa.android.data.MenuItem(
        // getString(R.string.start_upload_inprogress),
        // getString(R.string.start_upload_inprogress),
        // R.drawable.downoad_ico_progress));
        // if (Helper.getSystemURL() != null
        // && Helper.getSystemURL().toLowerCase()
        // .contains(Helper.CONST_BE_THERE)
        // && Helper.isMisteroMenu)
        // menuItems.add(new com.checker.sa.android.data.MenuItem(
        // getString(R.string.menu_shopper_info),
        // getString(R.string.menu_shopper_info), getIcon("loader1")));// 8
        if (Helper.getSystemURL() != null
            // && Helper.isMisteroMenu
        )
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.menu_edit_shopper_info),
                    getString(R.string.menu_edit_shopper_info),
                    getIcon("editshopper")));// 8
        if (Helper.getSystemURL() != null
            // && Helper.isMisteroMenu
        )

            menuItems
                    .add(new com.checker.sa.android.data.MenuItem(
                            getString(R.string.menu_review_history),
                            getString(R.string.menu_review_history),
                            getIcon("history")));// 10
        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
            // && Helper.isMisteroMenu
        )
            menuItems.add(new com.checker.sa.android.data.MenuItem(
                    getString(R.string.menu_contact),
                    getString(R.string.menu_contact), getIcon("wbt_loader7")));// 10

        if (Helper.getSystemURL() != null
                && Helper.getSystemURL().toLowerCase()
                .contains(Helper.CONST_BE_THERE)
                && Helper.isMisteroMenu) {
        } else
            menuItems
                    .add(new com.checker.sa.android.data.MenuItem(
                            getString(R.string.job_list_exit),
                            getString(R.string.job_list_exit),
                            getIcon("exit_joblist")));// 8

        menuItems
                .add(new com.checker.sa.android.data.MenuItem(
                        getString(R.string.job_list_archive),
                        getString(R.string.job_list_archive),
                        getIcon("archive")));

        menuListView.setAdapter(new sideMEnuAdapter(JobBoardActivityFragment.this,
                menuItems));


    }

    //TODO Menu
    public void customAlertUploadPendingJobs(Context context) {

        final Dialog dialog = new Dialog(JobBoardActivityFragment.this);
        dialog.setContentView(R.layout.custom_upload_alert);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                start_uploading(false);
            }
        });

        Button dialogButtonCancel = (Button) dialog
                .findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public boolean onOptionsItemSelected(int count) {

        // 0 sendbug//not right now

        // 0 download
        // 1 upload
        // 2 job board
        // 3 refund report
        // 4 download alternate
        // 5 settings
        // 6 filter
        // 7 map
        // ////////////////
        // 8 upload in progress
        // 9 download in progress
        switch (count) {
            case 1:

//                startDownloadingJobs(false, false);
                break;
            case 2:
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//                start_uploading(false);
//
//                break;

            case 3:
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
                break;

            case 4:
                isJobselected = true;
                Intent intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                comunicator.JobList = null;
                startActivity(intent);
                finish();
                break;
            case 5:
                // FILTER jobs
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                break;

            case 6:
                // MAPSSSS
                // Getting status
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            JobBoardActivityFragment.this.getApplicationContext(), MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    comunicator.JobList = null;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;

        }
        return true;
    }

    private Set DownloadThisSet(String currentSet) {
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        String thisSet = Connector.postForm(
                Constants.getQestionnaireOneByOneURL("11.10", currentSet, null),
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

            Parser thisParser = new Parser(Revamped_Loading_Dialog.getDialog());
            if (thisSet != null && thisSet.contains(""))
                thisParser.parseXMLValues(thisSet,
                        Constants.QUES_RESP_FIELD_PARAM, null, null, null);
            return thisParser.set;
        }
        return null;

    }

    public class LangTask extends AsyncTask<Void, Integer, String> {
        public LangTask() {
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(
                    JobBoardActivityFragment.this,
                    getResources().getString(
                            R.string.downloading_system_languages));
        }

        @Override
        protected void onPostExecute(String result) {

            if (DBHelper.getLanguages(false).size() == 0) {
                Revamped_Loading_Dialog.hide_dialog();
                Toast.makeText(
                        JobBoardActivityFragment.this,
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

    private class DownloadSetTask extends AsyncTask<String, Void, Set> {

        private String currentSet;
        Cert cert = null;

        public DownloadSetTask(String setid, Cert cert) {
            currentSet = setid;
            this.cert = cert;
        }

        @Override
        protected Set doInBackground(String... params) {

            Set set = DownloadThisSet(currentSet);
            if (set != null) {
                ArrayList<Set> sets = new ArrayList<Set>();
                sets.add(set);
                DBHelper.AddSetss(sets, Revamped_Loading_Dialog.getDialog(),
                        new ArrayList<ListClass>());


            }
            return set;
        }

        @Override
        protected void onPostExecute(Set set) {
            if (set != null) {

                String orderid = DBHelper.AddCertificateOrder(cert);
                showListTaskHandler = new JobBoardActivityFragment.ShowDBListTask(
                        Revamped_Loading_Dialog.getDialog(), orderid);
                showListTaskHandler.execute();

            } else {
                Revamped_Loading_Dialog.hide_dialog();
            }
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
                    getResources()
                            .getString(R.string.downloadingCheckrtificate));
        }
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

    private void renameCamFiles(ArrayList<filePathDataID> uploadList,
                                String unix) {
        for (int j = 0; j < uploadList.size(); j++) {
            String path = uploadList.get(j).getFilePath();
            if (!path.contains("UNIX")
                    && path.contains("CAM_O"
                    + uploadList.get(j).getUPLOAD_FILe_ORDERID())) {
                String newPAth = path.replace("CAM_O"
                                + uploadList.get(j).getUPLOAD_FILe_ORDERID(),
                        "CAM_UNIX" + unix + "O"
                                + uploadList.get(j).getUPLOAD_FILe_ORDERID());
                File from = new File(path);
                File to = new File(newPAth);
                if (from.exists() && !to.exists()) {
                    from.renameTo(to);
                }
            }
        }
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

    public void backToArchiveAlert(Context context, String textString) {

        final Dialog dialog = new Dialog(JobBoardActivityFragment.this);
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

    public UploadingProgressBars customProgressAlert(Context context) {
        final Dialog dialog = new Dialog(JobBoardActivityFragment.this);
        dialog.setContentView(R.layout.custom_upload_progress);
        UploadingProgressBars bars = new UploadingProgressBars();
        bars.setBluemiddle(dialog
                .findViewById(R.id.middle));
        bars.setTprogressBarImages((TextView) dialog
                .findViewById(R.id.txt_progress_images));
        bars.setTprogressBarJobs((TextView) dialog
                .findViewById(R.id.txt_progress_jobs));
        bars.setProgressBarImages((ProgressBar) dialog
                .findViewById(R.id.progress_bar_images));
        bars.setProgressBarJobs((ProgressBar) dialog
                .findViewById(R.id.progress_bar_jobs));
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.setCancelable(false);
        dialog.show();

        bars.setDialog(dialog);

        return bars;
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
        // if (qd.getFreetext() != null
        // && !qd.getFreetext().equals("")
        // && ((qd.getAnswersList() == null) || qd.getAnswersList().size() ==
        // 0)) {
        // extraDataList.add(Helper.getNameValuePair("obj" + newDataId
        // + "-answerText1", qd.getFreetext()));
        // } else
        if (qd.getFreetext() != null && !qd.getFreetext().equals("")) {
            // extraDataList.add(Helper.getNameValuePair("obj" + newDataId,
            // ""));
            // extraDataList.add(Helper.getNameValuePair("obj" + newDataId
            // + "-answerText1", ""));
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-mi" + postfix, qd.getFreetext()));
        } else
            extraDataList.add(Helper.getNameValuePair(prefix + "obj"
                    + newDataId + "-mi" + postfix, qd.getAnswerText()));
        return extraDataList;
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

    private String getGroupIdFromDataId(String data_id) {

        if (data_id.contains("-")) {
            data_id = data_id.substring(0, data_id.indexOf("-"));
        }
        return data_id;
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
            app_ver = JobBoardActivityFragment.this.getPackageManager().getPackageInfo(
                    JobBoardActivityFragment.this.getPackageName(), 0).versionName;
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
                    pos_shelf_item = new POS_Shelf(JobBoardActivityFragment.this);
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
//            textFilePath="W29iajE1NjA3MC1xdWVzdGlvblRleHRdID0+INeQ15bXldeoINen15XXpNeV16og16nXqNeV16og16LXptee15kg16TXoNeV15kg157Xntek15LXoteZ150g15vXkteV158gLSDXqdeo16TXqNek15nXnSAvINei15LXnNeV16og16jXmden15XXqiDXkNeVINee15zXkNeV16og16HXl9eV16jXlCAvINen16jXmNeV16DXmdedIC8g15zXldec15nXnSBbb2JqMTU2MTA3LW1pXSA9PiBbb2JqMTU2MTIyLWFuc3dlclRleHQxXSA9PiBbb2JqMTU2MDk3LW1pXSA9PiBbb2JqMTU2MTE0LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYzMTMtbWldID0+IFtvYmoxNTYzMDEtcXVlc3Rpb25UZXh0XSA9PiDXlNeQ150g16DXptek15Qg15DXmdepINem15XXldeqINee16nXldeX15cg15HXnteb16nXmdeoINeg15nXmdeTIFtvYmoxNTYzMTUtYW5zd2VyVGV4dDFdID0+INeb158gW29iajE1NjExMC1hbnN3ZXJUZXh0MV0gPT4g16nXqNeV16og15jXldeRINee15DXldeTIFtTZXRJRF0gPT4gMjM4NCBbb2JqMTU2MTAxXSA9PiA2NzQ0IFtvYmoxNTYxMTYtbWldID0+IFtvYmoxNTYwOTZdID0+IDk2MzkgW29iajE1NjExNl0gPT4gNjc2MiBbb2JqMTU2MDcyLW1pXSA9PiBbb2JqMTU2MzAzLWFuc3dlclRleHQxXSA9PiDXmNeV15EgW1RvdGFsQW5zd2Vyc1NlbnRdID0+IDU5IFtvYmoxNTYxMjUtbWldID0+IFtvYmoxNTYzMDFdID0+IDYyMDUgW3B1cmNoYXNlX1B1cmNoYXNlUGF5bWVudF0gPT4gMTQgW29iajE1NjI5Nl0gPT4gNjE3NCBbb2JqMTU2MzE2XSA9PiA3MTk5IFtvYmoxNTYxMjUtcXVlc3Rpb25UZXh0XSA9PiDXp9eV16TXldeqINeU15nXpteZ15DXlCDXnteQ15XXmdeZ16nXldeqINeR15TXqteQ150g15zXnteh16TXqCDXlNec16fXldeX15XXqiDXlNee157XqteZ16DXmdedINeR16fXldek15QgW29iajE1NjA3MV0gPT4gNjU3NCBbb2JqMTU2MzA2LW1pXSA9PiBbb2JqMTU2MTAwLW1pXSA9PiBbb2JqMTYwNzcxLW1pXSA9PiAxMzo1ODo0NCBbb2JqMTU2MDkwLW1pXSA9PiBbb2JqMTU2MTE2LXF1ZXN0aW9uVGV4dF0gPT4g15HXqteX15nXnNeqINee16rXnyDXlNep16jXldeqINeU16jXmdedINeo15DXqdeVINec15zXp9eV15cg15XXkdeo15og15HXkdeo15vXlCDXnteZ15zXldec15nXqiDXkdeQ157Xptei15XXqiDXlNee16nXpNeYICLXqdec15XXnSwg15nXqSDXnNeaINeb16jXmNeZ16Eg15zXmdeZ16Mg16HXmNeZ15nXnD8iIFtvYmoxNTYxMjYtYW5zd2VyVGV4dDFdID0+IFtvYmoxNTYyOTYtbWldID0+IFtyZXBvcnRlZF9TdGFydFRpbWVdID0+IDIwMjAtMDEtMjMgMTM6NTg6NDkgW29iajE1NjExOC1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MDk0LXF1ZXN0aW9uVGV4dF0gPT4g15TXkNedINeR15nXqNeoINec157XmSDXnteZ15XXoteTINeU16rXm9ep15nXqD8gW29iajE1NjEwNy1xdWVzdGlvblRleHRdID0+INeU15XXpteiINee15XXpteoINep15wg15zXmdeZ16Mg15vXldec15wg15TXodeR16gg157Xp9em15XXoteZINec15LXkdeZINeU15nXldeq15Ug15fXnNeV16TXlCDXqNeQ15XXmdeUIFtvYmoxNTYxMTEtcXVlc3Rpb25UZXh0XSA9PiDXqdedINeQ15nXqSDXlNem15XXldeqINeU16DXkdeT16cgW29iajE1NjMxNS1taV0gPT4gW0NyaXRFbmRMYXRdID0+IDMyLjA5MzY2NjA3NjY2MDE2IFtvYmoxNTYwNzItYW5zd2VyVGV4dDFdID0+INecIteoIC0g15DXmdefINen15XXpNeV16og15HXqdeo15XXqiDXotem157XmSBbb2JqMTU2MzA3LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYzMDZdID0+IDYyMzAgW29iajE1NjExOC1taV0gPT4gW29iajE1NjA5MF0gPT4gNjcwNCBbb2JqMTU2Mjk5LXF1ZXN0aW9uVGV4dF0gPT4g15TXkNedINeg16bXpNeUINeQ15nXqSDXpteV15XXqiDXntep15XXl9eXINeR157Xm9ep15nXqCDXoNeZ15nXkyBbb2JqMTU2MTEwXSA9PiA2NzUwIFtvYmoxNTk5MDUtYW5zd2VyVGV4dDFdID0+INeb158gW29iajE1NjMxNi1xdWVzdGlvblRleHRdID0+INee15PXpNeZINeU16HXldek16gg16TXmdeZ15Eg157XnNeQ15nXnSDXldee15XXpteS15nXnSDXkdeU150g15vXnCDXlNee15XXpteo15nXnSDXnteq15XXnteX16jXmdedINeR15TXqteQ150g15zXqteQ16jXmdeaIFtvYmoxNTYxMjVdID0+IDcxMDAgW29iajE1NjMwNy1xdWVzdGlvblRleHRdID0+INee15fXmdeo15kg15TXp9eV16TXlCDXqdecINeU157Xldem16jXmdedINep16DXqNeb16nXlSDXqteV15DXnteZ150g15zXnteX15nXqNeZ150g16LXnCDXpNeZINeU16nXmdec15XXmCAvINeq15XXldeZ15XXqiDXlNee15fXmdeoIFtvYmoxNTYwNzEtcXVlc3Rpb25UZXh0XSA9PiDXlNeo16bXpNeUINeR15DXlteV16gg16fXldek15XXqiDXqdeo15XXqiDXotem157XmSDXoNen15nXlCAo15DXmdefINep16fXmdeV16ov16DXmdeZ16jXldeqL9eb16rXnteZINec15vXnNeV15opIFtvYmoxNjA3NzAtcXVlc3Rpb25UZXh0XSA9PiDXqdei16og15vXoNeZ16HXlDogW29iajE1OTkwNS1taV0gPT4gW29iajE1NjMwMC1hbnN3ZXJUZXh0MV0gPT4g15zXkCBbb2JqMTU2MzA4LW1pXSA9PiDXp9eZ15HXnNeUINeh15nXldeiINee16LXldeR15PXqiDXkNeX16jXqiBbb2JqMTU2MzAyLXF1ZXN0aW9uVGV4dF0gPT4g15vXnCDXk9ec16TXp9eZINeU16fXldek15XXqiDXpNeg15XXmdeZ150gW29iajE1NjA5Ni1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MDk1XSA9PiA2NzI3IFtvYmoxNTYyOTgtbWldID0+IFtvYmoxNTYxMDBdID0+IDk2MzkgW29iajE1NjI5Ny1hbnN3ZXJUZXh0MV0gPT4g15jXldeRIFtvYmoxNTYxMTEtbWldID0+INeQ16jXmdefIFtvYmoxNTYzMTctbWldID0+IFtvYmoxNTYzMDBdID0+IDYyMDAgW29iajE1NjMxNV0gPT4gNzE5NCBbb2JqMTU2MTI2LXF1ZXN0aW9uVGV4dF0gPT4g15TXoteo15XXqiBbb2JqMTU2MDcwXSA9PiA2NTc2IFtvYmoxNTYxMjMtYW5zd2VyVGV4dDFdID0+INecIteoIFtvYmoxNTYxMDVdID0+IDk2MzkgW29iajE1NjMxNi1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MTExLWFuc3dlclRleHQxXSA9PiBbb2JqMTU2MTE3LXF1ZXN0aW9uVGV4dF0gPT4g15TXqdeo15XXqiDXlNeZ15Qg15DXk9eZ15EgLyDXnteX15nXmdeaIC8g16DXoteZ150gW29iajE1NjEwMy1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MzAxLW1pXSA9PiBbb2JqMTU2MzEyLWFuc3dlclRleHQxXSA9PiBbb2JqMTYwNDA3XSA9PiA5NjM5IFtvYmoxNTYzMDQtYW5zd2VyVGV4dDFdID0+INec15zXkCDXntek15LXoteZ150gW29iajE2MDQwNy1taV0gPT4gW29iajE1NjA5NS1xdWVzdGlvblRleHRdID0+INeU15DXnSDXkNeqL9eUINec15XXp9eXL9eqINeq16jXldek15XXqiDXoNeV16HXpNeV16ov16rXldeh16TXmSDXqteW15XXoNeUINeg15XXodek15nXnT8gW29iajE1OTkwNy1taV0gPT4gMTMuOSBbb2JqMTU2MTA0LW1pXSA9PiBbb2JqMTU2MzA1XSA9PiA2MjI5IFtvYmoxNTYwOTQtbWldID0+IFtvYmoxNTYzMTAtbWldID0+INec16ggW29iajE1NjA5MC1xdWVzdGlvblRleHRdID0+INeQ15nXqSDXlNem15XXldeqINeU16DXkdeT16cgW29iajE1NjEwMy1xdWVzdGlvblRleHRdID0+INeU16nXqNeV16og15TXmdeUINeQ15PXmdeRIC8g157Xl9eZ15nXmiAvINeg16LXmdedIFtvYmoxNTYwNjktbWldID0+IFtvYmoxNTYxMjRdID0+IDk2MzcgW29iajE1NjMxNy1xdWVzdGlvblRleHRdID0+INeb15wg15TXnteV16bXqNeZ150g15TXnteV16bXkteZ150g15HXkNeg15Mg15vXoNeZ16HXlCDXntep15XXnNeY15nXnSBbb2JqMTU2MTEzLW1pXSA9PiDXqteSINeU16nXnSDXkNeZ16DXoNeVINeU15nXlCDXnteV15PXpNehINeQ15zXkCDXkdeb16rXkSDXmdeTINeV15PXlNeV15kgW0NyaXRGcmVlVGV4dF0gPT4gW29iajE1NjMwOC1xdWVzdGlvblRleHRdID0+INeb15DXqdeoINeU15zXp9eV15cg15HXmden16kg15zXqdec150g15PXqNeaINeU15DXpNec15nXp9em15nXlCAtINeQ15nXqSDXlNem15XXldeqINeU15vXmdeoINeQ16og15TXqdeo15XXqiDXldeZ15PXoiDXnNeq16TXotecINeQ15XXqteVINee15AnINeV16LXkyDXqicgLSAo15HXl9eZ16jXlCDXkdeQ157Xptei15kg16rXqdec15XXnSDXlNeo15zXldeV16DXmNeZINeR16fXldek15QsINeU16DXl9eZ15nXqiDXlNec16fXldeXINec15zXl9eV16Ug16LXnCDXm9ek16rXldeoINeU16rXqdec15XXnSDXldec15TXlteZ158g16fXldeTINeQ15nXqdeV16gg15HXnyA1INeh16TXqNeV16og15TXnteV16TXmdeiINei15wg15LXkdeZINem15Ig15TXp9eV16TXlCwg15DXmdep15XXqCDXodeV16TXmSDXqdecINeU16rXqdec15XXnSkgW29iajE1NjMxMi1xdWVzdGlvblRleHRdID0+INeU16LXqNeV16ogW29iajE1NjA3Mi1xdWVzdGlvblRleHRdID0+INeU15DXnSDXqtem15XXkteqINeS15Eg16fXldek15XXqiDXqdeo15XXqiDXotem157XmSDXntec15DXldeqPyBbb2JqMTU2MDkzLWFuc3dlclRleHQxXSA9PiBbb2JqMTYwNzcxLXF1ZXN0aW9uVGV4dF0gPT4g16nXoteqINeZ16bXmdeQ15Q6IFtvYmoxNTYxMjItbWldID0+INec16ggW29iajE1NjEwNy1hbnN3ZXJUZXh0MV0gPT4g15zXkCBbb2JqMTU2MDk0XSA9PiA2NzIyIFtvYmoxNTYzMDMtcXVlc3Rpb25UZXh0XSA9PiDXkNeW15XXqCDXoteR15XXk9eUINep15wg15TXp9eV16TXkNeZ16og157XodeV15PXqCAtINeQ15nXnyDXoteS15zXldeqIC8g16fXqNeY15XXoNeZ150g16HXl9eV16jXlCDXnteh15HXmdeRINeV157XkNeX15XXqNeZINeT15zXpNen15kg15TXp9eV16TXldeqIFtvYmoxNTYxMTRdID0+IDY3NTkgW29iajE1NjMwOC1hbnN3ZXJUZXh0MV0gPT4g15vXnyAtINeU15vXmdeoINeQ16og15TXqdeZ16jXldeqINeV15nXk9eiINec16rXpNei15wg15DXldeq15Ug157XkCDXldei15Mg16ogW29iajE1NjMwMy1taV0gPT4gW0NyaXRTdGFydExhdF0gPT4gMzIuMDkyODc2NDM0MzI2MTcgW29iajE2MDQwNy1xdWVzdGlvblRleHRdID0+INeU15DXnSDXlNeo15XXp9eXINep15DXnCDXnNeh15nXkdeqINeg15jXmdec16og15HXqNeW15w/IFtvYmoxNTk5MDYtYW5zd2VyVGV4dDFdID0+IFtvYmoxNjA3NzAtYW5zd2VyVGV4dDFdID0+IFtvYmoxNTYwOTYtbWldID0+IFtyZXBvcnRlZF9GaW5pc2hUaW1lXSA9PiAyMDIwLTAxLTIzIDE0OjA5OjQ0IFtvYmoxNTYzMTItbWldID0+INeU16jXkdeUINeQ16DXqdeZINem15XXldeqINec15zXkCDXnteT15nXnSBbb2JqMTU2MzE0XSA9PiA2MTY4IFtvYmoxNTYxMDRdID0+IDk2MzkgW29iajE1NjEwMC1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MDcxLW1pXSA9PiBbb2JqMTU2MzAxLWFuc3dlclRleHQxXSA9PiDXnNeQIFtvYmoxNTYxMTgtcXVlc3Rpb25UZXh0XSA9PiDXoNek16jXkyDXkdeR16jXm9eUIFtvYmoxNTYwOTctYW5zd2VyVGV4dDFdID0+INeb158gW29iajE1NjEyMi1xdWVzdGlvblRleHRdID0+INep150g15DXmdepINeU16bXldeV16og15TXoNeR15PXpyDXkdeQ15bXldeoINen15XXpNeV16og16nXqNeV16og16LXptee15kgW29iajE1NjA4OS1hbnN3ZXJUZXh0MV0gPT4gW29iajE1NjI5OC1hbnN3ZXJUZXh0MV0gPT4g15zXkCBbb2JqMTU2MDk2LXF1ZXN0aW9uVGV4dF0gPT4g15TXkNedINeg15nXqtefINeU16HXkdeoINen16bXqCDXotecINeU157Xldem16g/IFtvYmoxNTYxMjQtbWldID0+IFtvYmoxNTYxMTMtcXVlc3Rpb25UZXh0XSA9PiDXoteg15Mg16rXkiDXqdedINeg16fXmSDXldee15XXk9ek16EgW29iajE1NjMwNF0gPT4gNjIyMCBbb2JqMTU2MDY5LWFuc3dlclRleHQxXSA9PiDXnCLXqC3XkNeZ158g16fXldek15XXqiDXkdep16jXldeqINei16bXnteZIFtvYmoxNTYyOTldID0+IDYxOTUgW29iajE1NjA4OS1taV0gPT4g16jXldeR15AgW29iajE1NjMwNS1taV0gPT4g157Xldem15LXmdedINee15XXpteo15nXnSBbb2JqMTU2MTIzXSA9PiA5NjM3IFtvYmoxNTYxMDQtcXVlc3Rpb25UZXh0XSA9PiDXqdeQ15wg15DXnSDXpteo15nXmiDXoteV15Mg157XqdeU15UgW29iajE2MDc3MC1taV0gPT4gMTM6NDc6MzkgW29iajE1NjA5MS1xdWVzdGlvblRleHRdID0+INeh15vXnSDXlNeo15LXqdeq15og157XoNeb15XXoNeV16og15TXmdeV16LXpS/XqiDXnNeU16LXoNeZ16cg15zXmiDXqdeo15XXqiDXntei15wg15XXntei15HXqCBbb2JqMTU2MzE4LXF1ZXN0aW9uVGV4dF0gPT4g15vXnCDXnteV16bXqNeZINeQ16DXkyDXm9eg15nXodeUINee15XXpteS15nXnSDXldeR16TXmdeZ16HXmdeg15Ig157XnNeQIFtvYmoxNTYxMjQtYW5zd2VyVGV4dDFdID0+INecIteoIFtvYmoxNTYxMTYtYW5zd2VyVGV4dDFdID0+INeX15zXp9eZLdeZ16bXqCDXp9ep16gg16LXmdefL9eR15nXqNeaINec16nXnNeV150g15HXnNeR15MgW1VuaXhUaW1lc3RhbXBdID0+IDE1Nzk3ODEzOTE5NTIgW29iajE1NjA5OC1taV0gPT4gW29iajE1NjMxNy1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MzE0LW1pXSA9PiBbb2JqMTU2Mjk2LXF1ZXN0aW9uVGV4dF0gPT4g15TXnteT16TXmdedINeg16fXmdeZ150gW29iajE1NjEwNC1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MDY5LXF1ZXN0aW9uVGV4dF0gPT4g15DXlteV16gg16fXldek15XXqiDXqdeo15XXqiDXotem157XmSDXnteQ15XXmdepIFtvYmoxNTYzMDktcXVlc3Rpb25UZXh0XSA9PiDXlNeQ150g16DXotep15Qg16DXodeZ15XXnyDXnNei16DXmdeZ158g15DXldeq15og15HXm9eo15jXmdehINeQ15Ug15zXl9ec15XXpNeZ158g15zXlNei15HXmdeoINeQ15XXqteaINec15DXmdepINem15XXldeqINeQ15fXqCDXnNen15HXnCDXlNeh15HXqCDXotecINeU15vXqNeY15nXoSBbb2JqMTU5OTA1LXF1ZXN0aW9uVGV4dF0gPT4g15DXmdepINeU16bXldeV16og15HXp9eV16TXlCDXlNen16TXmdeTINep15zXkCDXnNeU16bXmdeiINee15HXptei15nXnSDXm9ec16nXlNedINec157XoteYINeS15XXqNejINec15nXmdejIFtvYmoxNTYzMDldID0+IDYyNDggW29iajE1NjMxMy1hbnN3ZXJUZXh0MV0gPT4g15jXldeRIFtvYmoxNTYwNzAtYW5zd2VyVGV4dDFdID0+INecIteoIC0g15DXmdefINen15XXpNeV16og15HXqdeo15XXqiDXotem157XmSBbb2JqMTU2MzA1LWFuc3dlclRleHQxXSA9PiDXnNeQIFtvYmoxNTYzMTMtcXVlc3Rpb25UZXh0XSA9PiDXl9ec15XXoNeV16og15TXl9eW15nXqiDXoNen15nXmdedIC0g15vXkteV158gLSDXnNeb15zXldeaINeV16HXnNeV15jXmdeZ16QgW29iajE1NjExNy1taV0gPT4gW29iajE1OTkwNy1hbnN3ZXJUZXh0MV0gPT4gW29iajE1NjExM10gPT4gOTYzOCBbb2JqMTU2MzA0LXF1ZXN0aW9uVGV4dF0gPT4g15DXlteV16gg15TXp9eV16TXldeqINek16DXldeZINee157XpNeS16LXmdedINeb15LXldefIC0g16nXqNek16jXpNeZ150gLyDXoteS15zXldeqINeo15nXp9eV16og15DXlSDXntec15DXldeqINeR16HXl9eV16jXlCAvINeh15XXnNee15XXqiDXpNeq15XXl9eZ150g16nXkNeZ16DXnSDXnteQ15XXmdeZ16nXmdedIC8g16fXqNeY15XXoNeZ150gLyDXnNeV15zXmdedIC8g16bXlden15zXmdedINek16rXldeX15nXnSBbQ3JpdFN0YXJ0TG9uZ10gPT4gMzQuODY1NDA5ODUxMDc0MjIgW29iajE1NjEyNi1taV0gPT4g15zXqCBbb2JqMTU2MzEzXSA9PiA2MTYxIFtvYmoxNTYzMDctbWldID0+IFtvYmoxNTYwNjldID0+IDY1NjYgW29iajE1NjEwMS1taV0gPT4gW29iajE1NjA5OF0gPT4gNjczMSBbb2JqMTU2MDkxLW1pXSA9PiBbb2JqMTU2Mjk3LW1pXSA9PiBbb2JqMTU2MTE4XSA9PiA5NjM5IFtvYmoxNTYxMDNdID0+IDk2MzkgW29iajE1NjA5NC1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTYwNDA3LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYwOTAtYW5zd2VyVGV4dDFdID0+INeo15XXp9eXIFtvYmoxNTk5MDVdID0+IDk2MzkgW29iajE1NjExMC1taV0gPT4gW29iajE1NjMxNi1taV0gPT4gW29iajE1NjMwOS1hbnN3ZXJUZXh0MV0gPT4g15zXkCBbb2JqMTU2MTIzLXF1ZXN0aW9uVGV4dF0gPT4g15TXkNedINeR15bXntefINep15TXldeq15og15HXkNeW15XXqCDXlNen15XXpNeV16osINeU16bXmdei15Ug15zXmiDXodeZ15XXoi/XlNeb15XXldeg15Q/IFtvYmoxNTYzMDNdID0+IDYyMTQgW29iajE1NjMwMC1xdWVzdGlvblRleHRdID0+INeU15DXnSDXoNem16TXlCDXkNeZ16kg16bXldeV16og16LXnSDXnteb16nXmdeoINeg15nXmdeTINeR15DXldec150g15TXnteb15nXqNeUICjXnteX15bXmdenINeR15vXmdehINeQ15Ug15HXmdeTINeQ15Ug16LXnCDXlNeT15zXpNenKSBbb2JqMTU2Mjk4XSA9PiA2MTkwIFtvYmoxNTYzMThdID0+IDcyMTEgW29iajE1NjA5Ny1xdWVzdGlvblRleHRdID0+INeg16rXoNeVINeU15XXqNeQ15XXqiDXqdeZ157XldepINeR16LXnCDXpNeUIFtvYmoxNTYxMTQtcXVlc3Rpb25UZXh0XSA9PiDXkNeZ16kg15TXpteV15XXqiDXnNeR16kg15fXldec16bXqiDXpNeV15zXlSwg157Xm9eg16Eg15DXqNeV15og15vXldec15wg15In15nXoNehINec15zXkCDXp9eo16LXmdedICjXnNeQINeY16jXmdeZ16DXmdeg15Ig15DXlSDXkdeo157XldeT15QpLCDXoNei15wg16HXkteV16jXlCDXldeR157Xp9eo15Qg16nXnCDXntei15nXnCDXpNec15nXliwg15zXkdepL9eUINeQ15XXqteVINee16LXnCDXl9eV15zXpteqINeU16TXldec15UgKNeo15zXldeV16DXmNeZINeR16LXmden16gg15zXl9eV15PXqdeZINeU15fXldeo16MpIFtvYmoxNjA3NzEtYW5zd2VyVGV4dDFdID0+IFtvYmoxNTYxMDUtcXVlc3Rpb25UZXh0XSA9PiDXoNek16jXkyDXkdeR16jXm9eUIFtDcml0RW5kTG9uZ10gPT4gMzQuODY1NTEyODQ3OTAwMzkgW29iajE1NjMwMC1taV0gPT4gW29iajE1NjEwMS1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MzEwLWFuc3dlclRleHQxXSA9PiBbb2JqMTU5OTA2LW1pXSA9PiA0MTk5NTE5IFtvYmoxNTYxMDAtcXVlc3Rpb25UZXh0XSA9PiDXoteg15Mg16rXkiDXqdedINeg16fXmSDXldee15XXk9ek16EgW2lvc19hcHB2ZXJzaW9uXSA9PiA2LjEzNC45OSB8fGNvbmRpdGlvbj0xfDE2MDQwN3w5NjM5fGNvbmRpdGlvbj0xfDE1NjEwMXw2NzQ0fGNvbmRpdGlvbj0xfDE1NjEwMXw2NzQwfGNvbmRpdGlvbj0xfDE1NjMwMnw2MjA5fGNvbmRpdGlvbj0xfDE1NjA2OXw2NTY3fGNvbmRpdGlvbj0xfDE1NjA3MHw2NTc2IFtvYmoxNTYzMDItYW5zd2VyVGV4dDFdID0+INec15AgW29iajE1NjMwOS1taV0gPT4gW29iajE1NjMwOF0gPT4gNjIzOSBbb2JqMTU2MDk4LWFuc3dlclRleHQxXSA9PiDXnCLXqCAtINeR157Xp9eo15Qg16nXnCDXqNeV16fXlyDXldek16jXlyDXqdeg15HXk9enIFtvYmoxNTYyOTctcXVlc3Rpb25UZXh0XSA9PiDXlNee15PXpNeZ150g157XnNeQ15nXnSDXldee16HXldeT16jXmdedINeR16TXmdeZ16HXmdeg15IgW29iajE1NjMxNC1xdWVzdGlvblRleHRdID0+INep15zXmCDXodeV16TXqCDXpNeQ16jXnSDXntei15wg15fXlteZ16og15TXl9eg15XXqiDXoNen15kg15XXnteV15DXqCDXm9eV15zXlSBbb2JqMTU2MDkzLW1pXSA9PiDXkdeo15bXnCBbcHVyY2hhc2VfUHVyY2hhc2VJbnZvaWNlTnVtYmVyXSA9PiA0MTk5NTE5IFtvYmoxNTYyOTktbWldID0+IFtvYmoxNTYyOTktYW5zd2VyVGV4dDFdID0+INec15AgW29iajE1OTkwNi1xdWVzdGlvblRleHRdID0+INee16HXpNeoINeX16nXkdeV16DXmdeqINen16DXmdeUIFtvYmoxNTYxMDMtbWldID0+IFtvYmoxNTYzMDUtcXVlc3Rpb25UZXh0XSA9PiDXkdeh16DXmdek15nXnSDXkdeU150g15nXqSDXkteRINen15XXpNeUINeg157XldeaINep157XldeoINec15Ag15zXlNem15nXkiDXnteV16bXqNeZ150g16LXnCDXkteR15kg15TXnteT16Mg15TXotec15nXldefIFtvYmoxNTYzMTgtbWldID0+IFtwdXJjaGFzZV9QdXJjaGFzZURlc2NyaXB0aW9uXSA9PiDXnNeR15zXlSBbb2JqMTU2MTI1LWFuc3dlclRleHQxXSA9PiDXlNeZ15Qg16bXldeo15og15HXpNeq15nXl9eqINen15XXpNeUINeV15vXqNeW15UgLyDXpNeq15fXlSBbb2JqMTU2MTE3LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYwOTEtYW5zd2VyVGV4dDFdID0+INep16jXldeqINeY15XXkSDXnteQ15XXkyBbb2JqMTU2MzE4LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYxMTMtYW5zd2VyVGV4dDFdID0+INec15AgW29iajE1NjA5N10gPT4gOTYzOSBbb2JqMTU2MTE3XSA9PiA5NjM5IFtvYmoxNTYxMDUtYW5zd2VyVGV4dDFdID0+INeb158gW29iajE1NjMxNC1hbnN3ZXJUZXh0MV0gPT4g15vXnyBbb2JqMTU2MDcxLWFuc3dlclRleHQxXSA9PiDXnCLXqCAtINeQ15nXnyDXp9eV16TXldeqINeR16nXqNeV16og16LXptee15kgW29iajE1NjMwMi1taV0gPT4g15TXotee15PXldeqINei157Xldeh15XXqiDXkdee15XXpteo15nXnSBbb2JqMTU2MzA2LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYxMDUtbWldID0+IFtvYmoxNTYxMjQtcXVlc3Rpb25UZXh0XSA9PiDXlNeQ150g15TXpNeg15nXlCDXkNec15nXmiDXoNei16nXqteUINeR16bXldeo15Qg15DXk9eZ15HXlCDXldep16jXldeq15nXqiBbT3JkZXJJRF0gPT4gMjU0NTMzIFtvYmoxNTYzMDJdID0+IDYyMTAgW29iajE1NjI5N10gPT4gNjE4MiBbb2JqMTU2MDk1LW1pXSA9PiBbb2JqMTU2MzE3XSA9PiA3MjA1IFtvYmoxNTYwNzJdID0+IDY1ODIgW29iajE1NjA5OC1xdWVzdGlvblRleHRdID0+INeU15DXnSDXoNen15gg15HXpNei15XXnNeUINec15fXmdeW15XXpyDXlNee16nXm9eZ15XXqiDXlNen16nXqCBbb2JqMTU2MTA3XSA9PiA5NjM4IFtvYmoxNTYwNzAtbWldID0+IFtvYmoxNTYxMTQtbWldID0+IFtvYmoxNTYwODktcXVlc3Rpb25UZXh0XSA9PiDXqdedINeQ15nXqSDXlNem15XXldeqINeU16DXkdeT16cgW29iajE1NjA5My1xdWVzdGlvblRleHRdID0+INeh15XXkiDXlNeR16LXmdeUINep15TXldei15zXqteUIFtvYmoxNTYxMTAtcXVlc3Rpb25UZXh0XSA9PiDXodeb150g15DXqiDXlNeo15LXqdeq15og157XoNeb15XXoNeV16og15DXmdepINeU16bXldeV16og15HXp9eV16TXlCDXnNeU16LXoNeZ16cg15zXmiDXqdeo15XXqiDXntei15wg15XXntei15HXqCBbb2JqMTU2MDk1LWFuc3dlclRleHQxXSA9PiDXm9efIFtvYmoxNTYyOTYtYW5zd2VyVGV4dDFdID0+INeY15XXkSBbb2JqMTU2MTAxLXF1ZXN0aW9uVGV4dF0gPT4g15nXldei16Uv16og15TXmNeR16Ig15zXkdepL9eUINeX15XXnNem16og16TXldec15UsINee15vXoNehINeQ16jXldeaINeb15XXnNecINeSJ9eZ16DXoSDXnNec15Ag16fXqNei15nXnSwg16DXotecINeh15LXldeo15Qg15XXkdee16fXqNeUINep15wg157XoteZ15wg16TXnNeZ15YsINec15HXqS/XlCDXkNeV16rXlSDXntei15wg15fXldec16bXqiDXlNek15XXnNeVLiDXkdee16fXqNeUINep15wg16jXlden15cv16osINeX15zXldenINeg16fXmSwg16jXm9eV16Eg15XXnteS15XXlNelLCDXnteb16DXoSDXkNeo15XXmiDXm9eV15zXnCDXkifXmdeg16Eg15zXnNeQINen16jXoteZ150gW29iajE1NjEyMy1taV0gPT4gW29iajE1NjMwN10gPT4gNjIzNSBbb2JqMTU2Mjk4LXF1ZXN0aW9uVGV4dF0gPT4g15TXkNedINeg16bXpNeUINeQ15nXqSDXpteV15XXqiDXotedINee15vXqdeZ16gg16DXmdeZ15Mg15HXkNeV15zXnSDXlNee15vXmdeo15QgKNee15fXlteZ16cg15HXm9eZ16Eg15DXlSDXkdeZ15Mg15DXlSDXotecINeU15PXnNek16cpIFtvYmoxNTYwOTFdID0+IDY3MTYgW29iajE1NjMxNS1xdWVzdGlvblRleHRdID0+INeb15wg157Xldem16jXmSDXlNeh15XXpNeoIDUg15DXqdeoINeg157XpteQ15nXnSDXkdeq16bXldeS15Qg157XqdeV15zXmNeZ150gW29iajE1OTkwNy1xdWVzdGlvblRleHRdID0+INeh15vXldedINen16DXmdeUIFtvYmoxNTYzMDQtbWldID0+IFtvYmoxNTYzMDYtcXVlc3Rpb25UZXh0XSA9PiDXkNeW15XXqCDXlNee16HXmNeZ16fXmdedINeV15TXntee16rXp9eZ150g15HXp9eV16TXlCDXntec15Ag15DXlSDXpNeo15XXoSDXotecINeU15nXl9eZ15PXlCDXnNec15Ag15fXldeo15nXnSBbb2JqMTU2MzEwLXF1ZXN0aW9uVGV4dF0gPT4g16nXnSDXkNeZ16kg15TXpteV15XXqiDXkden15XXpNeU";

            //textFilePath.toLowerCase();
//            File root = android.os.Environment.getExternalStorageDirectory();
//            String path = root.getAbsolutePath() + "/mnt/sdcard/CheckerSecret/submission.txt";
//            File dir = new File(path);
//            if (dir.exists()) {
//                textFilePath=dir.getAbsolutePath();
//            }

        }

        public SubmitSurveyTask(SubmitQuestionnaireData archivedSQ) {
            this.archivedSQ = archivedSQ;
        }

        @Override
        protected void onPreExecute() {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            if (progressBars != null && progressBars.getDialog() != null
                    && progressBars.getDialog().isShowing())
                progressBars.getDialog().dismiss();
            progressBars = customProgressAlert(JobBoardActivityFragment.this);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                progressBars.dismissDialog();
            } catch (Exception ex) {

            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            upload_comp_jobs = false;
            Helper.syncing = false;
            // executeJobList();
//            sendMessage(STOP_UPLOAD, null);
            ShowDBJobs();
            if (isOnlyCertificate) {
                if (JobBoardActivityFragment.this.cert != null) {
                    ArrayList<Cert> shortList = new ArrayList<Cert>();
                    Cert c = new Cert();
                    c.setCertID(JobBoardActivityFragment.this.cert);
                    shortList.add(c);
                    load_certificates(shortList);
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
                        QuestionnaireActivity.deleteJobRecords(JobBoardActivityFragment.this.certOrdeId);
                        Toast.makeText(getApplicationContext(),
                                        "CheckerTificate not passed", Toast.LENGTH_LONG)
                                .show();
                    }

                    JobBoardActivityFragment.this.cert = null;
                    JobBoardActivityFragment.this.certOrdeId = null;
                }

            } else if (archivedSQ != null && textFilePath != null) {
                if (result != null) {
                    backToArchiveAlert(JobBoardActivityFragment.this, "Server Response: " + result);
                }
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
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
            else
                sqd = mAdapter.getInProgressJobs();
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
            List<NameValuePair> nvp = PrepareQuestionnaireNameValuePair(text);
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
                    // set = (Set) DBHelper.convertFromBytes(setId);
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

                    pos_shelf_item = new POS_Shelf(JobBoardActivityFragment.this);
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
            List<NameValuePair> nvp = JobBoardActivityFragment.this.PrepareQuestionnaireNameValuePair(isProgress, sq, pos_shelf_item);
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
            renameCamFiles(uploadList, sq.getUnix());

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
                        ((Activity) JobBoardActivityFragment.this)
                                .runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ShowAlert(
                                                JobBoardActivityFragment.this,
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
//                            DBHelper.deleteJobli.stRecords(where);
//                            DBHelper.deleteRecordbyOrdeid(Constants.DB_TABLE_QUESTIONNAIRE_ORDERID
//                                    + "="
//                                    + "\""
//                                    + sqd.get(i).getOrderid()
//                                    + "\"");
//
//                            DBHelper.updateOrders(
//                                    Constants.DB_TABLE_ORDERS,
//                                    new String[]{
//                                            Constants.DB_TABLE_ORDERS_ORDERID,
//                                            Constants.DB_TABLE_ORDERS_STATUS,
//                                            Constants.DB_TABLE_ORDERS_START_TIME,},
//                                    sqd.get(i).getOrderid(), "uploaded on "
//                                            + sdf.format(myCalendar.getTime()),
//                                    "");
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

    public boolean isWifiStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String removeGarbageFromTop(String thisSet) {
//        if (thisSet!=null && thisSet.trim().toLowerCase().startsWith("<?xml version"))
//            return thisSet;
//        thisSet=thisSet.substring(thisSet.indexOf("<?xml version"));
        return thisSet;
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
//                try {
//                    pngItems = validateIcons(DBHelper.getIcons(), jobordersss);
//                } catch (Exception ex) {
//                    pngItems = new ArrayList<pngItem>();
//                }
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                isBranchPropErr = myPrefs.getBoolean(
                        Constants.ALREADY_BRANCHPROPERR, false);

//                if (pngItems.size() > 0 || isBranchPropErr == true) {
//                    btnErr.setVisibility(RelativeLayout.VISIBLE);
//                } else {
//                    btnErr.setVisibility(RelativeLayout.GONE);
//                }
                joborders = new ArrayList<orderListItem>();
                for (int i = 0; jobordersss != null && i < jobordersss.size(); i++) {

                    joborders.add(new orderListItem(jobordersss.get(i), null));
                }

                filtered = joborders.stream()
                        .filter(string -> string.orderItem.getOrderID().contains("-"))
                        .collect(Collectors.toList());
//                Log.e("filtered", filtered + "  " + filtered.size());

                for (int i = 0; filtered != null && i < filtered.size(); i++) {
                    jobs_CAPI.add(new orderListItem(filtered.get(i).orderItem, null));
                }

                // Get My jobs excluding CAPI....
                List<orderListItem> list1 = filtered;
                List<orderListItem> list2 = joborders;

                List<orderListItem> union = new ArrayList<orderListItem>(list1);
                union.addAll(list2);

                List<orderListItem> intersection = new ArrayList<orderListItem>(list1);
                intersection.retainAll(list2);
                union.removeAll(intersection);
                // Print the result
                filtered_other_jobs = new ArrayList<orderListItem>();
                for (orderListItem n : union) {
                    filtered_other_jobs.add(new orderListItem(n.orderItem, null));
                }
                Log.e("union_size", String.valueOf(filtered_other_jobs.size()));

                //Count jobs for dash board....
                List<orderListItem> filtered_status_my_job_accept = filtered_other_jobs.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("assigned"))
                        .collect(Collectors.toList());
                my_jobs_accept = String.valueOf(filtered_status_my_job_accept.size());
                Log.d("TAG", "ShowDBJobs--Assigned:" + filtered_status_my_job_accept.size());

                List<orderListItem> filtered_status_my_jobs_implement = filtered_other_jobs.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("scheduled") || string.orderItem.getStatusName().equalsIgnoreCase("in Progress") || string.orderItem.getStatusName().equalsIgnoreCase("completed"))
                        .collect(Collectors.toList());
                my_jobs_implement = String.valueOf(filtered_status_my_jobs_implement.size());
                Log.d("TAG", "ShowDBJobs--scheduled:" + filtered_status_my_jobs_implement.size());


                List<orderListItem> filtered_status_assigned = filtered.stream()
                        .filter(string -> (string.orderItem.getStatusName().equalsIgnoreCase("assigned") || string.orderItem.getStatusName().equalsIgnoreCase("survey")))
                        .collect(Collectors.toList());
                capi_assigned_count = String.valueOf(filtered_status_assigned.size());
                Log.d("TAG", "ShowDBJobs--Progress:" + filtered_status_assigned.size());


                List<orderListItem> filtered_status_inProgress = filtered.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("in Progress") || string.orderItem.getStatusName().equalsIgnoreCase("completed"))
                        .collect(Collectors.toList());
                capi_status_inProgress = String.valueOf(filtered_status_inProgress.size());
                Log.d("TAG", "ShowDBJobs--Progress:" + filtered_status_inProgress.size());


//                List<orderListItem> filtered_status_completed = filtered.stream()
//                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("completed"))
//                        .collect(Collectors.toList());
//                capi_status_returned = String.valueOf(filtered_status_completed.size());
//                Log.d("TAG", "ShowDBJobs--completed:" + filtered_status_completed.size());


                if (joborders != null) {
                    Orders.setListOrders(jobordersss);
                    Orders.setBranchProps(branchProps);
                    Surveys.setSets(DBHelper.getSurveyyRecords());
                    setOrderList();
                    // setSurveyList'();
                    // seting list here
//                    showDbjobsPostPart();
                }
            }

        } catch (Exception ex) {
            int i = 0;
            i++;
            Toast.makeText(JobBoardActivityFragment.this, "DB jobs Crashed here",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void start_uploading(boolean isProgress) {


        if (Helper.syncing == true) {
            Toast.makeText(
                    JobBoardActivityFragment.this,
                    getResources().getString(
                            R.string.synch_is_already_in_progress),
                    Toast.LENGTH_SHORT).show();
        }
        Helper.syncing = true;

        isWifiOnly = myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false);


        if (IsInternetConnectted()) {
            if (isWifiOnly) {

                if (isWifiStatus()) {
                    // wifi is enabled
                } else {
                    ShowDBJobs();
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.wifi_not_enabled));
                    return;

                }
            }

            JobBoardActivityFragment.SubmitSurveyTask sbmtSurveyTask = new JobBoardActivityFragment.SubmitSurveyTask();
            sbmtSurveyTask.isOnlyCertificate = upload_comp_jobs;
            upload_comp_jobs = false;
            sbmtSurveyTask.isProgress = isProgress;
            sbmtSurveyTask.execute();

        } else {
            ShowDBJobs();
            customAlert(
                    JobBoardActivityFragment.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));

        }

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
        new Parser(dialog1).parseXMLValues(resultLang,
                Constants.DB_TABLE_language);

    }

    private String LangListPost() {
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = JobBoardActivityFragment.this.getPackageManager().getPackageInfo(
                    JobBoardActivityFragment.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        return Connector.postForm(Constants.getLangListURL(app_ver),
                extraDataList);
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

    public void callJobDetail(Intent intent, int jOB_DETAIL_ACTIVITY_CODE2) {
        startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
    }

    private boolean showLogin(String result) {
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

    private String getStatusByOrderID(String orderid, Order[] ordersArr) {
        if (ordersArr == null)
            return "";
        for (int Ocount = 0; Ocount < ordersArr.length; Ocount++) {
            if (orderid.equals(ordersArr[Ocount].getOrderID()))
                return ordersArr[Ocount].getStatusName();
        }
        return "";
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

    private void saveOfflineData() {
        String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
                + "\"Assigned\"";
        DBHelper.deleteJoblistRecords(where);

        DBHelper.deleteProps();
        DBHelper.saveProps(Orders.getBranchProps());
        branchProps = Orders.getBranchProps();
        DBHelper.AddOrders(Orders.getOrders());
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
                Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
                        getResources().getString(R.string.alert_switching));
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
                tv_CAPI_assigned.setText(capi_assigned_count);
                tv_CAPI_inProgress.setText(capi_status_inProgress);
                tv_CAPI_returned.setText(capi_status_returned);
                tv_waiting_acceptance.setText(my_jobs_accept);
                tv_waiting_implementation.setText(my_jobs_implement);

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
            cleanUploaedJobsHere(null);

            if (certorderid != null) {

                Intent intent = new Intent(
                        JobBoardActivityFragment.this.getApplicationContext(),
                        JobDetailActivity.class);
                isJobselected = true;
                intent.putExtra("OrderID", certorderid);
                callJobDetail(intent, JOB_DETAIL_ACTIVITY_CODE);

            }

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
//                try {
//                    pngItems = validateIcons(DBHelper.getIcons(), jobordersss);
//                } catch (Exception ex) {
//                    pngItems = new ArrayList<pngItem>();
//                }
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                isBranchPropErr = myPrefs.getBoolean(
                        Constants.ALREADY_BRANCHPROPERR, false);

//                joborders = new ArrayList<orderListItem>();
//                for (int i = 0; i < jobordersss.size(); i++) {
//                    joborders.add(new orderListItem(jobordersss.get(i), null));
//                }
                joborders = new ArrayList<orderListItem>();
                for (int i = 0; jobordersss != null && i < jobordersss.size(); i++) {

                    joborders.add(new orderListItem(jobordersss.get(i), null));
                }

                filtered = joborders.stream()
                        .filter(string -> string.orderItem.getOrderID().contains("-"))
                        .collect(Collectors.toList());
//                Log.e("filtered", filtered + "  " + filtered.size());

                for (int i = 0; filtered != null && i < filtered.size(); i++) {
                    jobs_CAPI.add(new orderListItem(filtered.get(i).orderItem, null));
                }

                // Get My jobs excluding CAPI....
                List<orderListItem> list1 = filtered;
                List<orderListItem> list2 = joborders;

                List<orderListItem> union = new ArrayList<orderListItem>(list1);
                union.addAll(list2);

                List<orderListItem> intersection = new ArrayList<orderListItem>(list1);
                intersection.retainAll(list2);
                union.removeAll(intersection);
                // Print the result
                filtered_other_jobs = new ArrayList<orderListItem>();
                for (orderListItem n : union) {
                    filtered_other_jobs.add(new orderListItem(n.orderItem, null));
                }
                Log.e("union_size", String.valueOf(filtered_other_jobs.size()));

                //Count jobs for dash board....
                List<orderListItem> filtered_status_my_job_accept = filtered_other_jobs.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("assigned"))
                        .collect(Collectors.toList());
                my_jobs_accept = String.valueOf(filtered_status_my_job_accept.size());

                List<orderListItem> filtered_status_my_jobs_implement = filtered_other_jobs.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("scheduled"))
                        .collect(Collectors.toList());
                my_jobs_implement = String.valueOf(filtered_status_my_jobs_implement.size());

                List<orderListItem> filtered_status_assigned = filtered.stream()
                        .filter(string -> (string.orderItem.getStatusName().equalsIgnoreCase("assigned") || string.orderItem.getStatusName().equalsIgnoreCase("survey")))
                        .collect(Collectors.toList());
                capi_assigned_count = String.valueOf(filtered_status_assigned.size());

                List<orderListItem> filtered_status_inProgress = filtered.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("in Progress"))
                        .collect(Collectors.toList());
                capi_status_inProgress = String.valueOf(filtered_status_inProgress.size());
                List<orderListItem> filtered_status_completed = filtered.stream()
                        .filter(string -> string.orderItem.getStatusName().equalsIgnoreCase("completed"))
                        .collect(Collectors.toList());
                capi_status_returned = String.valueOf(filtered_status_completed.size());

            }

            return "";
        }

    }

    private void executeShowListTask(Revamped_Loading_Dialog dialog) {
        try {
            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(
                    Constants.IS_DOWNLOADED_FOR_NEW_DOWNLOAD_PATH, true);
            prefsEditor.commit();
            showListTaskHandler = new JobBoardActivityFragment.ShowDBListTask(dialog);
            showListTaskHandler.execute();

        } catch (Exception ex) {
            // ShowAlert(JobListActivity.this,
            // JobListActivity.this.getString(R.string.alert_working),
            // JobListActivity.this.getString(R.string.alert_working_msg),
            // JobListActivity.this.getString(R.string.button_ok));
        }
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
                    Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
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
                    // TODO Auto-generated catch block
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
                                            // TODO Auto-generated catch block
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
                                JobBoardActivityFragment.this);
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
                                JobBoardActivityFragment.this,
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
                        // TODO Auto-generated catch block
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
                        // TODO Auto-generated catch block
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
                                    // TODO Auto-generated catch block
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
                    app_ver = JobBoardActivityFragment.this.getPackageManager()
                            .getPackageInfo(
                                    JobBoardActivityFragment.this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {

                }
                return Connector.postForm(
                        Constants.getInProgressJobsURL(app_ver), extraDataList);
            }
        }
        RefundTask refundtaskobj = new RefundTask(dialogg);
        refundtaskobj.execute();
    }

    private void openDashBoard() {
        Intent intent = new Intent(
                this,
                NewDashboardScreenActivity.class);
        startActivity(intent);
    }

    public boolean startDownloadingJobs(boolean isPullToRefreshLibrary,
                                        boolean isOrderOnly) {
//        btnErr.setVisibility(RelativeLayout.GONE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(Constants.AUTOSYNC_CURRENT_TIME,
                System.currentTimeMillis());
        prefsEditor.commit();

        executeJobList(isPullToRefreshLibrary, isOrderOnly);
        return true;
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

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mApiClient.connect();
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


    class JobbListTask extends AsyncTask<Void, Integer, String> {
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

            Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
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
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
            } else if (result.equals("invalid")) {
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }

            if (result.equals("offline")) {
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
            } else if (result.equals("error=")) {
                ShowAlert(JobBoardActivityFragment.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));

            }
            sendMessage(STOP_DOWNLOAD, null);
            getInProgressJobs(dialog1, Orders.checkDifferenceBWListOfORderIds());
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
                dialog1.changeMessage(getResources().getString(
                        R.string.update_system_languages));

                getLanguages();
            }
            Parser parser = new Parser(dialog1);
            {
                dialog1.changeMessage(getResources().getString(
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

            dialog1.changeMessage(getResources().getString(
                    R.string.aadownloading_Assigned_surveys));
            String fieldResult = FieldListPost();

            Helper helper = new Helper();
            helper.deleteFilesInFolder();
            // helper.deleteSetsInFolder();

            dialog1.changeMessage(getResources().getString(
                    R.string.aaparsing_assigned_jobs));

            dialog1.changeMessage(getResources().getString(
                    R.string.aaparsing_assigned_surveys));
            if (fieldResult != null && fieldResult.contains("<data>"))
                new Parser(dialog1).parseXMLValues(fieldResult,
                        Constants.SURVEY_LIST_RESP_FIELD_PARAM);
            else
                new Parser(dialog1).parseXMLValues(fieldResult,
                        Constants.SURVEY_LIST_RESP_FIELD_PARAM_OLD);
            if (dialog1 != null) {
                dialog1.changeMessage(getResources().getString(
                        R.string.aasaving_assigned_jobs_surveys_job));
            }

            if (!checkConnectionPost())
                return "offline";

            if (Connector.cookies == null) {
                if (showLogin(doLogin()))
                    return "SessionExpire";
            }

            if (dialog1 != null) {
                dialog1.changeMessage(getString(R.string.questionnaire_downlaod_alert));
            } else {
                Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
                        getString(R.string.questionnaire_downlaod_alert));
                dialog1 = Revamped_Loading_Dialog.getDialog();
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
            if (dialog1 != null)
                dialog1.changeMessage(dialog1.getContext().getResources()
                        .getString(R.string.aaparsing_complete));
            DBHelper.deleteRecords(null);
            Parser.allCorrectSets = DBHelper.AddSetss(Sets.getSets(),
                    Revamped_Loading_Dialog.getDialog(), Lists.getAllLists());
            // DBAdapter.closeDataBase(db);
            return true;
        }

        private String FieldListPost() {
            // Initialize the login data to POST
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
            try {
                app_ver = JobBoardActivityFragment.this.getPackageManager()
                        .getPackageInfo(JobBoardActivityFragment.this.getPackageName(),
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
                app_ver = JobBoardActivityFragment.this.getPackageManager()
                        .getPackageInfo(JobBoardActivityFragment.this.getPackageName(),
                                0).versionName;
            } catch (PackageManager.NameNotFoundException e) {

            }

            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            return Connector.postForm(Constants.getJobListURL(app_ver),
                    extraDataList);
        }

    }

    public void ShowAlert(Context context, String title, final String message,
                          String button_lbl) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        TextView textView = new TextView(context);

//        setFontSize(textView);
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

    public boolean onOptionsItemSelectedMistero(int count) {

        // 0 sendbug//not right now

        // 0 download
        // 1 upload
        // 2 job board
        // 3 refund report
        // 4 download alternate
        // 5 settings
        // 6 filter
        // 7 map
        // ////////////////
        // 8 upload in progress
        // 9 download in progress
        switch (count) {

            case 1:

                // open job board
                // MAPSSSS
                // Getting status
                Intent intent = null;
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            JobBoardActivityFragment.this.getApplicationContext(),
                            JobBoardActivityFragment.class);
                    JobBoardActivityFragment
                            .setJobBardCallback(new jobBoardCertsListener() {

                                @Override
                                public void certCallBack(ArrayList<Cert> certs) {
                                    load_certificates(certs);
                                }
                            });
                    intent.putExtra("orderid", "-1");
                    comunicator.JobList = this;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }

                break;
            case 2:
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        ShopperRefundReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                break;
            case 3:// shopper info
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                // showShopperInfo();
                break;

            case 4:
                // MAPSSSS
                // Getting status
                status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    comunicator.JobList = JobBoardActivityFragment.this;
                    intent = new Intent(
                            JobBoardActivityFragment.this.getApplicationContext(),
                            MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;

            // case 4:// shopper info
            // showShopperInfo();
            // break;

            case 5:// edit hopper
                editShopperInfo();
                break;

            case 6:// reviews history
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        CritHistoryReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);

                // historyOfReview();
                break;
            case 7:// contact using whatsapp

                try {

                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse("smsto:" + "" + Helper.helpline + "?body="
                                    + ""));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.whatsapp_not_available));
                    // Intent sendIntent = new Intent(Intent.ACTION_DIAL);
                    // sendIntent.setData(Uri.parse("tel:" + Helper.helpline));
                    // startActivity(sendIntent);
                    // Toast.makeText(JobBoardActivityFragment.this,
                    // "it may be you dont have whatsapp", Toast.LENGTH_LONG)
                    // .show();
                }

                break;
        }
        return true;
    }

    public static void loadUrlInWebViewDialog(final Activity context, String url) {
        Connector.setCookieManager(context);
        final Dialog err_dialog = new Dialog(context,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.dialog_urls);
        err_dialog.findViewById(R.id.textView1).setVisibility(
                RelativeLayout.GONE);

        WebView wv = (WebView) err_dialog.findViewById(R.id.briefingView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);

        wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wv.setScrollbarFadingEnabled(false);
        wv.clearCache(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Revamped_Loading_Dialog.hide_dialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                if (url != null
                        && (url.toLowerCase().contains("c_main.php") || url
                        .toLowerCase().contains("login.php"))) {
                    err_dialog.dismiss();
                    Revamped_Loading_Dialog.hide_dialog();
                    Toast.makeText(context,
                                    context.getResources().getString(
                                            R.string.task_completed), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                Revamped_Loading_Dialog.hide_dialog();
            }

        });
        wv.getSettings().setUserAgentString("Android");
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wv.loadUrl(url);

        Button btnClose = (Button) err_dialog.findViewById(R.id.btnClose);

        Helper.changeBtnColor(btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                err_dialog.dismiss();
                Revamped_Loading_Dialog.hide_dialog();
            }
        });
        err_dialog.show();
        Revamped_Loading_Dialog.show_dialog(context, context.getResources()
                .getString(R.string.jd_please_alert_msg));

    }

    public static void setAlternateURL(String newEurURL, SharedPreferences myPrefs) {
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, newEurURL);
        prefsEditor.commit();
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));

    }

    private void editShopperInfo() {

        if (IsInternetConnectted()) {
            new JobBoardActivityFragment.DoLoginTask(Constants.getEditShopperURL()).execute();
        } else {
            customAlert(
                    JobBoardActivityFragment.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));
        }
    }

    public class DoLoginTask extends AsyncTask<Void, Integer, String> {
        Revamped_Loading_Dialog dialog1;
        private String url;

        public DoLoginTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(JobBoardActivityFragment.this,
                    "Logging in...");
        }

        @Override
        protected void onPostExecute(String result) {
            Revamped_Loading_Dialog.hide_dialog();
            if (Connector.setCookieManager(JobBoardActivityFragment.this))
                loadUrlInWebViewDialog(JobBoardActivityFragment.this, this.url);
            else
                return;
        }

        @Override
        protected String doInBackground(Void... params) {
            checkConnectionPost();
            if (Connector.cookies == null) {
                return doLogin();
            }

            return null;
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

    private void load_certificates(final ArrayList<Cert> shortList) {
        class JobTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                dialogg = new ProgressDialog(JobBoardActivityFragment.this);
                dialogg.setMessage(getResources().getString(
                        R.string.checkertificatesmsg));
                dialogg.setCancelable(false);
                dialogg.show();
                this.updateDate = null;
            }

            @Override
            protected void onPostExecute(String result) {

                // <status>0</status>
                // <auto_approve_flag>1</auto_approve_flag>
                if (result != null && result.contains("<status>1</status>")) {
                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.nocertMsg),
                            Toast.LENGTH_LONG).show();
                } else {
                    // no error parse here
                    ArrayList<Cert> listOfCerts = parseCertificateResult(result);
                    if (shortList == null)
                        showCertificatesDialog(listOfCerts);
                    else if (shortList != null && listOfCerts != null) {
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
                                        .equals(certID)) {
                                    isPresent = true;
                                    break;
                                }

                            }
                            if (isPresent)
                                templistOfCerts.add(listOfCerts.get(i));
                        }

                        showCertificatesDialog(templistOfCerts);
                    }
                }

                try {
                    dialogg.dismiss();

                } catch (Exception ex) {
                }

            }

            private ArrayList<Cert> parseCertificateResult(String result) {

                parser = new Parser();
                parser.parseXMLValues(result, Constants.CERTS_FIELD_PARAM);
                return parser.listCerts;

            }

            @Override
            protected String doInBackground(Void... params) {
                checkConnectionPost();
                // Initialize the login data to POST
                List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
                // extraDataList.add(Helper.getNameValuePair(
                // JobListActivity.this.getResources().getString(
                // R.string.s_item_column_0_line_994_file_88), comment));
                // extraDataList.add(Helper.getNameValuePair("OrderID",
                // orderId));

                String data = Connector.postForm(
                        Constants.getCheckerTificates("9.7", shortList != null), extraDataList);
                if (data.contains("<script>")) {
                    doLogin();
                    data = Connector
                            .postForm(Constants.getCheckerTificates("9.7", shortList != null),
                                    extraDataList);
                }
                return data;
            }
        }
        JobTask jobtaskobj = new JobTask();
        jobtaskobj.execute();
    }

    public void customAlert(Context context, String textString) {
        final Dialog dialog = new Dialog(JobBoardActivityFragment.this);
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
            }
        });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void showCertificatesDialog(final ArrayList<Cert> listOfCerts) {
        final Dialog dialog = new Dialog(JobBoardActivityFragment.this);
        dialog.setContentView(R.layout.dialog_checkertificates);
        ListView listJobs = (ListView) dialog.findViewById(R.id.lvjobs);
        listJobs.setAdapter(new CheckertificateAdapter(JobBoardActivityFragment.this,
                listOfCerts));

        listJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (listOfCerts.get(position).getStatus() != null
                        && (listOfCerts.get(position).getStatus().toLowerCase()
                        .equals("blocked")

                        || listOfCerts.get(position).getStatus().toLowerCase()
                        .contains("maximum"))) {
                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.alreadyBlocked),
                            Toast.LENGTH_LONG).show();

                } else if (listOfCerts.get(position).getStatus() != null
                        && listOfCerts.get(position).getStatus().toLowerCase()
                        .equals("passed")) {
                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.alreadyPassed),
                            Toast.LENGTH_LONG).show();
                } else if (listOfCerts.get(position).getTimesTaken() != null
                        && Integer.parseInt(listOfCerts.get(position)
                        .getTimesTaken()) >= Integer
                        .parseInt(listOfCerts.get(position)
                                .getMaxRetake())) {
                    Toast.makeText(JobBoardActivityFragment.this,
                            getResources().getString(R.string.reachedMaxTries),
                            Toast.LENGTH_LONG).show();
                } else {
                    String msg = getResources().getString(
                            R.string.strtcheckertificatesmsg);
                    if (listOfCerts.get(position).getStatus() != null
                            && listOfCerts.get(position).getStatus()
                            .toLowerCase().contains("re-take"))
                        msg = getResources().getString(
                                R.string.retakecheckertificatesmsg);
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            JobBoardActivityFragment.this);
                    builder.setMessage(msg)
                            .setTitle(
                                    getResources().getString(
                                            R.string._alert_title))
                            .setCancelable(false)
                            .setPositiveButton(
                                    getResources()
                                            .getString(
                                                    R.string.questionnaire_exit_delete_alert_yes),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialogg, int id) {
                                            dialog.dismiss();
                                            JobBoardActivityFragment.DownloadSetTask dSet = new JobBoardActivityFragment.DownloadSetTask(
                                                    listOfCerts
                                                            .get(position)
                                                            .getDependencySetLink(),
                                                    listOfCerts.get(position));
                                            dSet.execute();
                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton(
                                    getResources()
                                            .getString(
                                                    R.string.questionnaire_exit_delete_alert_no),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialogg, int id) {

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        dialog.findViewById(R.id.checkertRefreshImg).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        load_certificates(null);
                        dialog.dismiss();
                    }
                });

        dialog.findViewById(R.id.btnOk).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        android.view.WindowManager.LayoutParams params = dialog.getWindow()
                .getAttributes();
        params.height = LinearLayout.LayoutParams.FILL_PARENT;

        dialog.getWindow().setAttributes(
                (android.view.WindowManager.LayoutParams) params);
        dialog.show();

    }

    public boolean onOptionsItemSelectedHS(int count) {
        switch (count) {
            case 0: // Dashboard
                openDashBoard();
                break;
            case 1:

//                startDownloadingJobs(false, false);
                break;
            case 2:
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//                start_uploading(false);
//                break;
            case 3:
                // open job board
                // MAPSSSS
                // Getting status
                Intent intent = null;
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            JobBoardActivityFragment.this.getApplicationContext(),
                            JobBoardActivityFragment.class);
                    JobBoardActivityFragment
                            .setJobBardCallback(new jobBoardCertsListener() {

                                @Override
                                public void certCallBack(ArrayList<Cert> certs) {
                                    load_certificates(certs);
                                }
                            });
                    intent.putExtra("orderid", "-1");
                    // comunicator.JobList = this;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }

                break;
            case 4:
                // open refund report
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        ShopperRefundReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                break;
            case 5:
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
                break;

            case 6:
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                finish();
                break;
//            case 6:
//                // FILTER jobs
//                JobFilterDialog dialog = new JobFilterDialog(this);
//                dialog.show();
//                isJobselected = true;
//                break;

            case 7:
                // MAPSSSS
                // Getting status
                status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            JobBoardActivityFragment.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            JobBoardActivityFragment.this.getApplicationContext(),
                            MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    // comunicator.JobList = null;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;
            case 8:
                load_certificates(null);
                break;

            case 9:// edit hopper
                editShopperInfo();
                break;

            case 10:// reviews history
                isJobselected = true;
                intent = new Intent(JobBoardActivityFragment.this.getApplicationContext(),
                        CritHistoryReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);

                // historyOfReview();
                break;
            case 11:
                ExitFromJobList();
                break;
            case 12:
                openArciveScreen();
                break;

        }
        return true;
    }

    private void openArciveScreen() {
        Intent intent = new Intent(JobBoardActivityFragment.this.getApplicationContext(),
                ArchiveActivity.class);
        ArchiveActivity.toBeUploadedSQ = null;
        startActivityForResult(intent, JOB_ARCHIVE_ACTIVITY_CODE);
    }

    private void showLanguageDialog(String string, final boolean isFirstTime) {
        final LanguageDialog langDialog = new LanguageDialog(
                JobBoardActivityFragment.this, string);
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
            JobBoardActivityFragment.LangTask langTaskHandler = new JobBoardActivityFragment.LangTask();
            langTaskHandler.execute();

        } catch (Exception ex) {
            ShowAlert(JobBoardActivityFragment.this,
                    JobBoardActivityFragment.this.getString(R.string.alert_working),
                    JobBoardActivityFragment.this.getString(R.string.alert_working_msg),
                    JobBoardActivityFragment.this.getString(R.string.button_ok));
        }
    }

    public class LangTaskLangTask extends AsyncTask<Void, Integer, String> {
        public void LangTask() {
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(
                    JobBoardActivityFragment.this,
                    getResources().getString(
                            R.string.downloading_system_languages));
        }

        @Override
        protected void onPostExecute(String result) {

            if (DBHelper.getLanguages(false).size() == 0) {
                Revamped_Loading_Dialog.hide_dialog();
                Toast.makeText(
                        JobBoardActivityFragment.this,
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
            jobListTaskHandler = new JobBoardActivityFragment.JobbListTask(isPull, isOrderOnly);
            jobListTaskHandler.execute();

        } catch (Exception ex) {
            try {
                ShowAlert(JobBoardActivityFragment.this,
                        JobBoardActivityFragment.this.getString(R.string.alert_working),
                        JobBoardActivityFragment.this
                                .getString(R.string.alert_working_msg),
                        JobBoardActivityFragment.this.getString(R.string.button_ok));

            } catch (Exception e) {
            }
        }
    }

    //TODO MENU COMPLATE CODE
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
            multipleBranchSpinner.setSelection(0);
            multipleClientSpinner.setSelection(0);
            multipleBranchCodeSpinner.setSelection(0);
            multiplePropsSpinner.setSelection(0);

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
                filterBtn.setImageDrawable(getResources().getDrawable(R.drawable.img_filter_off));
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

//        NameValuePair first = clients.get(0);
        clients = insertNewItem("Client", clients, "0");
        locations = insertNewItem("City", locations, "0");
        branches = insertNewItem("Branches", branches, "0");
        props = insertNewItem("Proprieties", props, "0");


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
                        multipleClientSpinner.setSelection(0);
//                        if (parent == null) {
//                            tv_clients.setVisibility(View.VISIBLE);
//                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
//
//                        tv_clients.setVisibility(View.VISIBLE);
                    }
                }, multipleClientSpinner);

        propsFilter = (RelativeLayout) findViewById(R.id.propsFilter);
        multiplePropsSpinner = getMultipleDropdown(toStringArray(props),
                propsFilter, new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        multiplePropsSpinner.setSelection(0);
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
                        multipleBranchCodeSpinner.setSelection(0);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }, multipleBranchCodeSpinner);

        branchFilter = (RelativeLayout) findViewById(R.id.branchFilter);
        multipleBranchSpinner = getMultipleDropdown(toStringArray(locations),
                branchFilter, new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        multipleBranchSpinner.setSelection(0);
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
                filterBtn.setImageDrawable(getResources().getDrawable(R.drawable.img_filter_off));

                multipleBranchSpinner.setSelection(0);
                multipleClientSpinner.setSelection(0);
                multipleBranchCodeSpinner.setSelection(0);
                multiplePropsSpinner.setSelection(0);
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
                filterBtn.setImageDrawable(getResources().getDrawable(R.drawable.img_filter_on));
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
                    try {
                        arg0.setIcon(BitmapDescriptorFactory
                                .fromResource(R.drawable.turqpin));
                    } catch (Exception e) {

                    }

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
                    thiItem.setoaID("1");
                    thiItem.setApplicationComment(comment);

                    if (bulkApply == true && listOFOrders != null
                            && listOFOrders.size() > 0)
                        listOFOrders.remove(0);
                    else if (bulkApply == true && adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    if (bulkApply == true && listOFOrders != null
                            && listOFOrders.size() > 0) {
                        remove(comment,
                                ((Job) listOFOrders.get(0)).getOrderID(), null,
                                ((Job) listOFOrders.get(0)).getM(),
                                ((Job) listOFOrders.get(0)), true);
                    }


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
                            }
                            if (comunicator.JobList != null) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Constants.activity == "NewDashboardScreenActivity") {
                                            ((NewDashboardScreenActivity) comunicator.JobList).executeJobList(false, false);
                                        } else
                                            ((JobListActivity) comunicator.JobList).executeJobList(false, false);
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
        Log.e("TAG", "ApplyJob: " + comment);
        Log.e("TAG", "ApplyJob: " + orderId);
        // String url = Helper.getSystemURL()
        // + "/c_pda-apply-order.php?json=1&ver=" + ver;
        Log.e("TAG", "ApplyJob: " + extraDataList);
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
                double longitude = Double.valueOf(orders.get(i).getBranchLong());
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
                                Log.e("map_view", "true");
                                openDialog(thiItem, arg0, 1);
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
                btnApplyAll, btnRemoveAll);
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
                    double latitude = Double.valueOf(adapter.values.get(i).getBranchLat());
                    double longitude = Double.valueOf(adapter.values.get(i).getBranchLong());
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
                btnApplyAll, btnRemoveAll);
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

    public void openDialog(final Job thiItem, final Marker arg0, int type) {

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
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(lp.width, lp.height);

        TextView clientName, cityName, branchName, address, description, qustionnaire, shortname, addedAt, startAndndTime, BranchOpeingHours, SurveyPayment, BonusPayment, TarnsportionPayment, branchphone, showDistance;
        TextView tv_start_time, tv_end_time;
        RelativeLayout topbar, layout_apply_msg;
        CardView cardView;
        NestedScrollView nestedScroll;
        ImageView iv_down_arrow;
        Button btn_show_apply_d;
        topbar = (RelativeLayout) dialog.findViewById(R.id.topbar);
        cardView = dialog.findViewById(R.id.cardView);
        layout_apply_msg = dialog.findViewById(R.id.layout_apply_msg);
        btn_show_apply_d = dialog.findViewById(R.id.btn_show_apply_d);
        final EditText txtComment = (EditText) dialog
                .findViewById(R.id.txtComment);

        if (thiItem == null && type == -1) {
            layout_apply_msg.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
//            topbar.setBackgroundColor(Color.parseColor("#f18931"));
//            dialog.findViewById(R.id.layout_1).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_2).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_3).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_4).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_5).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_6).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_7).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_8).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_9).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_10).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_11).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_12).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_13).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.layout_14).setVisibility(
//                    RelativeLayout.GONE);
//            dialog.findViewById(R.id.altlayout).setVisibility(
//                    RelativeLayout.GONE);
        } else if (thiItem == null && type == -2) {
            layout_apply_msg.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            dialog.findViewById(R.id.btnApply).setBackgroundColor(
                    Color.parseColor("#a7a9ab"));
            ((TextView) dialog.findViewById(R.id.btnApply))
                    .setText(JobBoardActivityFragment.this
                            .getResources()
                            .getString(
                                    R.string.s_item_column_0_line_145_file_223));
            ((TextView) dialog.findViewById(R.id.btnApply))
                    .setTag("Remove");
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
            showDistance = (TextView) dialog.findViewById(R.id.ShowDistance);
            iv_down_arrow = dialog.findViewById(R.id.iv_down_arrow);
            nestedScroll = dialog.findViewById(R.id.nestedScroll);

            iv_down_arrow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nestedScroll.getVisibility() == View.GONE) {
                        nestedScroll.setVisibility(View.VISIBLE);
                    } else {
                        nestedScroll.setVisibility(View.GONE);
                    }
                }
            });

            float distance = (float) -1.0;
            if (thiItem.getBranchLat() != null && thiItem.getBranchLat().length() > 0
                    && thiItem.getBranchLong() != null
                    && thiItem.getBranchLong().length() > 0 && thisPersonLocaation != null) {
                Location loc1 = new Location("");
                loc1.setLatitude(Double.parseDouble(thiItem.getBranchLat()));
                loc1.setLongitude(Double.parseDouble(thiItem.getBranchLong()));
                distance = thisPersonLocaation.distanceTo(loc1);
            }

            distance = (float) (distance / 1000.0);

            if (distance >= 0) {
                distance = Math.round(distance);
                int dis = (int) distance;
                showDistance.setText(dis + " Km");
            }


            if (thiItem.getoaID() != null && thiItem.getoaID().length() > 0) {
                topbar.setBackgroundColor(Color.parseColor("#2cbdbf"));
                dialog.findViewById(R.id.btnApply).setBackgroundColor(
                        Color.parseColor("#a7a9ab"));
                ((TextView) dialog.findViewById(R.id.btnApply))
                        .setText(JobBoardActivityFragment.this
                                .getResources()
                                .getString(
                                        R.string.s_item_column_0_line_145_file_223));
                ((TextView) dialog.findViewById(R.id.btnApply))
                        .setTag("Remove");
                dialog.findViewById(R.id.altlayout).setVisibility(
                        RelativeLayout.GONE);
            } else {
                topbar.setBackgroundColor(Color.parseColor("#f18931"));
                LoadALTData(thiItem.getOrderID(), altSpinner, null);

            }
            topbar.setBackgroundColor(Color.parseColor(thiItem.getColor()));

            clientName.setText(thiItem.getClientName());
            branchName.setText(thiItem.getBranchFullname());
            cityName.setText(thiItem.getCityName());
            address.setText(thiItem.getAddress());

            description.setText(stripHtml(thiItem.getDescription()));
            qustionnaire.setText(thiItem.getSetName());
            shortname.setText(thiItem.getBranchName());
            if (thiItem.getStart_time() == null || thiItem.getTimeEnd() == null) {
                if (thiItem.getStart_time() == null) {
                    startAndndTime.setText("" + "" + thiItem.getTimeEnd());
                } else {
                    startAndndTime.setText("Start at " + thiItem.getStart_time() + "" + "");
                }
            } else {
                startAndndTime.setText("Start at " + thiItem.getStart_time());
            }

            tv_start_time = dialog.findViewById(R.id.tv_start_time);
            tv_end_time = dialog.findViewById(R.id.tv_end_time);
            tv_start_time.setText(thiItem.getStart_time());
            tv_end_time.setText(thiItem.getTimeEnd());

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
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception ex) {

                        }

                    }
                });

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
                            Toast.makeText(getApplicationContext(), txtComment.getText().toString() + " " + "(" + date + ")", Toast.LENGTH_LONG).show();
                            apply(txtComment.getText().toString() + " " + "(" + date + ")", thiItem.getOrderID(), dialog, arg0, thiItem, false);

                        } else {
                            apply(txtComment.getText().toString(),
                                    thiItem.getOrderID(), dialog, arg0,
                                    thiItem, false);
                        }
                    }
                    refresh_submit(true);
                } else {
                    if (thiItem == null) {
                        remove(txtComment.getText().toString(),
                                ((Job) listOFOrders.get(0)).getOrderID(),
                                dialog, ((Job) listOFOrders.get(0)).getM(),
                                ((Job) listOFOrders.get(0)), true);
                    } else {
                        remove(txtComment.getText().toString() + " ",
                                thiItem.getOrderID(), dialog, arg0, thiItem, false);
                    }
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
        }

        if (type == 0) {
            //PassIndex
            layout_apply_msg.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        } else if (type == 1) {
            layout_apply_msg.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            btn_show_apply_d.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout_apply_msg.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.GONE);
                }
            });
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
            String text1 = btnRemoveAll.getText().toString();
            text1 = text1.substring(0, text1.indexOf("("));

            btnApplyAll.setText(text);
            btnRemoveAll.setText(text1);
        }
        assert btnApplyAll != null;
        btnApplyAll.setText(btnApplyAll.getText().toString() + "(" + listOfCheckBoxes.size() + ")");

        btnRemoveAll.setText(btnRemoveAll.getText().toString() + "(" + listOfCheckBoxes.size() + ")");
    }

    int tag = 0;// , d1, m1, y1, d2, m2, y2;
    String[] monthVal = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

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
        String str = day + "-" + monthVal[month] + "-" + year;
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

    //TODO MENU
    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        sendMessage(START_ACTIVITY, "hiii");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

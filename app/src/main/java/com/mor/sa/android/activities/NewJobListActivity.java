package com.mor.sa.android.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.checker.sa.android.adapter.AlternateJobsAdapter;
import com.checker.sa.android.adapter.CheckertificateAdapter;
import com.checker.sa.android.adapter.JobItemAdapter;
import com.checker.sa.android.adapter.errIconAdapter;
import com.checker.sa.android.adapter.orphansPreviewAdapter;
import com.checker.sa.android.adapter.sideMEnuAdapter;
import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.AlternateJob;
import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.ArchiveData;
import com.checker.sa.android.data.BalloonData;
import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.BranchProperties;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.Expiration;
import com.checker.sa.android.data.FilterData;
import com.checker.sa.android.data.InProgressAnswersData;
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
import com.checker.sa.android.data.pngItem;
import com.checker.sa.android.data.validationSets;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.JobFilterDialog;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.BranchTVListener;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.DateTVListener;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.LanguageDialog;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.maps.android.JobBoardActivityFragment;
import com.google.maps.android.MapActivity;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewJobListActivity extends Activity implements View.OnClickListener,
        BranchTVListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener,
        GestureOverlayView.OnGesturePerformedListener, DateTVListener {


    private boolean isMenuOpen = false;
    private boolean isWifiOnly = false;
    private static final String START_ACTIVITY = "/start_activity";
    private static final String STOP_UPLOAD = "/stop_upload";
    private static final String STOP_DOWNLOAD = "/stop_download";
    ImageButton filterBtn, mapBtn;
    Parser parser;
    ListView jobItemList;
    private final int JOB_DETAIL_ACTIVITY_CODE = 2;
    private final int JOB_ARCHIVE_ACTIVITY_CODE = 69;
    private final int JOB_GPS_CODE = 678;
    private final int JOB_GPS_OFF_CODE = 679;
    private final int MAP_ACTIVITY = 1;
    boolean upload_comp_jobs = false, isJobselected = false;
    private View menuView;
    private ListView menuListView;
    public static ArrayList<orderListItem> joborders;
    protected static Activity JobList;
    ArrayList<SubmitQuestionnaireData> sqd;
    Timer timer, downloadjoblistTimer;
    long lastJobAndQuestionnaireDownloaded;
    TextView tv;
    long MILISECONND_24_HOURS = 86400000;
    long MILISECONND_5_MINS = 300000;
    SharedPreferences myPrefs;
    SimpleDateFormat sdf;// = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar date;
    JobbListTask jobListTaskHandler = new JobbListTask(false, false);
    private String mFilter = "assigned";
    private ToggleButton tabSync;
    private ToggleButton tabOne;
    private ToggleButton tabTwo;
    private ToggleButton tabThree;
    private ToggleButton tabFour;
    private ToggleButton tabSyncb;
    private ToggleButton tabOneb;
    private ToggleButton tabTwob;
    private ToggleButton tabThreeb;
    private ToggleButton tabFourb;

    private View ltabSync;
    private View sidemenuicon;
    private View ltabOne;
    private View ltabTwo;
    private View ltabThree;
    private View ltabFour;
    private ImageView imgtabSync;
    private ImageView imgtabOne;
    private ImageView imgtabTwo;
    private ImageView imgtabThree;
    private ImageView imgtabFour;
    private ImageView bimgtabSync;
    private ImageView bimgtabOne;
    private ImageView bimgtabTwo;
    private ImageView bimgtabThree;
    private ImageView bimgtabFour;
    private TextView txttabSync;
    private TextView txttabOne;
    private TextView txttabTwo;
    private TextView txttabThree;
    private TextView txttabFour;

    private View Side_menu_top_green;

    private JobItemAdapter mAdapter;
    private ImageView btnErr;
    private String color_unselect = "#FFFFFF";
    private String txt_color_unselect = "#CFDA9A";
    private String color_select = "#94BA09";
    private String txt_color_select = "#94BA09";

    Revamped_Loading_Dialog dialog;
    private boolean isBranchPropErr;
    private String alternateResult;


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

    private void ManageTabs(int tabNumber) {
        ltabSync.setBackgroundColor(Color.parseColor(color_unselect));
        layoutWorking.setVisibility(RelativeLayout.VISIBLE);
        layoutWorking.invalidate();
        String filter = "all";
        ltabOne.setBackgroundColor(Color.parseColor(color_unselect));
        ltabTwo.setBackgroundColor(Color.parseColor(color_unselect));
        ltabThree.setBackgroundColor(Color.parseColor(color_unselect));
        ltabFour.setBackgroundColor(Color.parseColor(color_unselect));
        tabSync.setTextColor(Color.parseColor(txt_color_unselect));
        if (tabNumber != 1) {

            imgtabOne.setImageDrawable(getResources().getDrawable(
                    getIcon("img_assigned_u")));
            // imgtabOne.setImageDrawable(getBubble(12));

            // tabOne.setPaintFlags(0);
            tabOne.setText(android.text.Html.fromHtml(getResources().getString(
                    R.string.job_list_tab_assigned)));
            tabOne.setTextColor(Color.parseColor(txt_color_unselect));
            tabOne.setChecked(false);
            tabOneb.setChecked(false);

            tabOneb.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (tabNumber != 2) {
            // tabTwo.setPaintFlags(0);
            imgtabTwo.setImageDrawable(getResources().getDrawable(
                    getIcon("img_scheduled_u")));
            tabTwo.setText(android.text.Html.fromHtml(getResources().getString(
                    R.string.job_list_tab_scheduled)));
            tabTwo.setTextColor(Color.parseColor(txt_color_unselect));
            tabTwo.setChecked(false);
            tabTwob.setChecked(false);
            tabTwob.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ltabTwo.setBackgroundColor(Color.parseColor(color_unselect));
        }
        if (tabNumber != 3) {
            // tabThree.setPaintFlags(0);
            imgtabThree.setImageDrawable(getResources().getDrawable(
                    getIcon("img_in_progress_u")));
            tabThree.setText(android.text.Html.fromHtml(getResources()
                    .getString(R.string.job_list_tab_in_progress)));
            tabThree.setTextColor(Color.parseColor(txt_color_unselect));
            tabThree.setChecked(false);
            tabThreeb.setChecked(false);
            tabThreeb.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ltabThree.setBackgroundColor(Color.parseColor(color_unselect));
        }
        if (tabNumber != 4) {
            // tabFour.setPaintFlags(0);
            imgtabFour.setImageDrawable(getResources().getDrawable(
                    getIcon("img_completed_u")));
            tabFour.setText(android.text.Html.fromHtml(getResources()
                    .getString(R.string.job_list_tab_completed)));
            tabFour.setTextColor(Color.parseColor(txt_color_unselect));
            tabFour.setChecked(false);
            tabFourb.setChecked(false);
            tabFourb.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ltabFour.setBackgroundColor(Color.parseColor(color_unselect));
        }

        if (tabNumber == 1) {

            tabOneb.setBackgroundColor(Color.parseColor(color_select));
            imgtabOne.setImageDrawable(getResources().getDrawable(
                    getIcon("img_assigned")));

            SpannableString spanString = new SpannableString(getResources()
                    .getString(R.string.job_list_tab_assigned));
            // spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
            // 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    spanString.length(), 0);
            tabOne.setTextColor(Color.parseColor(txt_color_select));
            // spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
            // spanString.length(), 0);
            tabOne.setText(spanString);
            // tabOne.setPaintFlags(tabOne.getPaintFlags()
            // | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            filter = "assigned";
            // ltabOne.setBackgroundColor(Color.parseColor(color_select));
        }
        if (tabNumber == 2) {
            tabTwob.setBackgroundColor(Color.parseColor(color_select));
            imgtabTwo.setImageDrawable(getResources().getDrawable(
                    getIcon("img_scheduled")));

            SpannableString spanString = new SpannableString(getResources()
                    .getString(R.string.job_list_tab_scheduled));
            // spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
            // 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    spanString.length(), 0);
            // spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
            // spanString.length(), 0);
            tabTwo.setTextColor(Color.parseColor(txt_color_select));
            tabTwo.setText(spanString);
            filter = "scheduled";
            // ltabTwo.setBackgroundColor(Color.parseColor(color_select));
        }
        if (tabNumber == 3) {
            tabThreeb.setBackgroundColor(Color.parseColor(color_select));
            imgtabThree.setImageDrawable(getResources().getDrawable(
                    getIcon("img_in_progress")));
            SpannableString spanString = new SpannableString(getResources()
                    .getString(R.string.job_list_tab_in_progress));
            // spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
            // 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    spanString.length(), 0);
            // spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
            // spanString.length(), 0);
            tabThree.setText(spanString);
            tabThree.setTextColor(Color.parseColor(txt_color_select));
            // tabThree.setPaintFlags(tabThree.getPaintFlags()
            // | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            filter = "in progress";
            // ltabThree.setBackgroundColor(Color.parseColor(color_select));
        }
        if (tabNumber == 4) {
            tabFourb.setBackgroundColor(Color.parseColor(color_select));
            imgtabFour.setImageDrawable(getResources().getDrawable(
                    getIcon("img_completed")));
            SpannableString spanString = new SpannableString(getResources()
                    .getString(R.string.job_list_tab_completed));
            // spanString.setSpan(new UnderlineSpan(), 0, spanString.length(),
            // 0);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    spanString.length(), 0);
            // spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
            // spanString.length(), 0);
            tabFour.setText(spanString);
            tabFour.setTextColor(Color.parseColor(txt_color_select));
            // | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            filter = "completed";
            // ltabFour.setBackgroundColor(Color.parseColor(color_select));
        }
        try {
            if (mAdapter != null) {
                mFilter = filter;
                LongOperation op = new LongOperation();
                op.execute();
                // mAdapter.doFilter(filter, JobListActivity.this, false);

            }
            layoutWorking.setVisibility(RelativeLayout.GONE);

        } catch (Exception ex) {
        }
    }

    // boolean isBusy = false;

    // boolean login_check=false;

    void stopLocationChecker() {
        Context context = NewJobListActivity.this;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // if (Helper.lThread != null)
        // Helper.lThread.isPost = false;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // OPen GPS settings
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                            getResources().getString(
                                    R.string.questionnaire_gps_off_alert))
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
                                    // comunicator.JobList = null;
                                    startActivityForResult(
                                            new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                            JOB_GPS_OFF_CODE);

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
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void setInvertDisplay() {
        if (Helper.getTheme(NewJobListActivity.this) == 0) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rvinjobscreen);
            if (layout != null)
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.navigation_bar_dark));

            ImageView imglayout = (ImageView) findViewById(R.id.bottombar);
            if (imglayout != null)
                imglayout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.navigation_bar_dark));

            layout = (RelativeLayout) findViewById(R.id.screenBackGround);
            if (layout != null)
                layout.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.background_dark));

            TextView tv = (TextView) findViewById(R.id.jobTitle);
            if (tv != null)
                tv.setTextColor(getResources().getColor(android.R.color.white));

            ListView lv = (ListView) findViewById(R.id.joblist);
            if (lv != null) {
                ColorDrawable sage = new ColorDrawable(this.getResources()
                        .getColor(android.R.color.white));
                lv.setDivider(sage);
            }

        }
    }

    void startLocationChecker() {
        Context context = NewJobListActivity.this;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

            String userName = myPrefs.getString(
                    Constants.POST_FIELD_LOGIN_USERNAME, "");

            // locationThread lThread = new locationThread();
            // lThread.startLocationThread(getApplicationContext(), userName);
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
                                    // comunicator.JobList = null;
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
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Helper.setConfigChange(true);
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        Constants.setLocale(NewJobListActivity.this);
    }

    Dialog err_dialog;
    TextView err_txtProduct;
    TextView err_txtExtra;
    ListView err_listPreview;
    Button err_btnDismiss;
    private RelativeLayout layoutWorking;
    private View layoutAttachErr;
    private RelativeLayout dialogAttach;
    private ShowDBListTask showListTaskHandler;
    private GoogleApiClient mApiClient;
    private ArrayList<com.checker.sa.android.data.MenuItem> menuItems;
    private ArrayList<com.checker.sa.android.data.MenuItem> synchMenuItems;
    private String cert;
    private String certOrdeId;


    public void makeBriefingDialog(Context context, String content, boolean b) {

        final Dialog err_dialog = new Dialog(context);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.dialog_briefing);
        TextView txt = err_dialog.findViewById(R.id.textView1);
        if (content.contains("not allowed to")) {
            err_dialog.findViewById(R.id.textView2).setVisibility(RelativeLayout.VISIBLE);
            err_dialog.findViewById(R.id.briefingView).setVisibility(RelativeLayout.GONE);

        } else {
            //.setVisibility(RelativeLayout.INVISIBLE);
            if (content != null && !content.equals("")) {
                content = content.replace("&lt;", "<");
                content = content.replace("&gt;", ">");
                content = content.replace("&quot;", "\"");
                WebView wv = (WebView) err_dialog.findViewById(R.id.briefingView);
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                wv.loadDataWithBaseURL("", content, mimeType, encoding, "");
            }
        }
        Button btnClose = (Button) err_dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                err_dialog.dismiss();
            }
        });

        if (b == false)//preview dialog
        {

            //txt.setText("Preview");
            txt.setText(context.getResources().getString(R.string.questionnaire_preview_menu));
            btnClose.setText(context.getResources().getString(R.string.close));
            //btnClose.setText("Close");
        }
        err_dialog.show();
    }

    public static void loadUrlInWebViewDialog(final Activity context, String url) {
        Connector.setCookieManager(context);
        final Dialog err_dialog = new Dialog(context,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.dialog_urls);
        err_dialog.findViewById(R.id.textView1).setVisibility(
                RelativeLayout.GONE);
        // err_dialog.findViewById(R.id.btnClose).setVisibility(
        // RelativeLayout.GONE);
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

                // url = url +
                // "";//https://checker-soft.com/testing/c_main.php?msg=Settings%20saved!
                if (url != null
                        && (url.toLowerCase().contains("c_main.php") || url
                        .toLowerCase().contains("login.php"))) {
                    err_dialog.dismiss();
                    Revamped_Loading_Dialog.hide_dialog();
                    Toast.makeText(
                                    context,
                                    context.getResources().getString(
                                            R.string.task_completed), Toast.LENGTH_LONG)
                            .show();

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

    public void makeErrorDialog(Context context) {
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        isBranchPropErr = myPrefs.getBoolean(Constants.ALREADY_BRANCHPROPERR,
                false);

        err_dialog = new Dialog(context);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Helper.getTheme(context) == 0) {
            err_dialog.setContentView(R.layout.dialog_icons_err_night);
        } else {
            err_dialog.setContentView(R.layout.dialog_err_icons);
        }
        err_dialog.setTitle(context.getResources().getString(
                R.string.job_list_err_dialog));

        err_txtProduct = ((TextView) err_dialog.findViewById(R.id.txt_product));
        if (isBranchPropErr) {
            err_txtProduct.setText((context.getResources()
                    .getString(R.string.job_list_err_dialog_branch)));
        } else {
            err_txtProduct.setText((context.getResources()
                    .getString(R.string.job_list_err_dialog)));
        }
        err_txtExtra = ((TextView) err_dialog.findViewById(R.id.txt_extra));
        err_listPreview = ((ListView) err_dialog
                .findViewById(R.id.list_preview));
        err_btnDismiss = ((Button) err_dialog.findViewById(R.id.btn_dismiss));
        err_btnDismiss = ((Button) err_dialog.findViewById(R.id.btn_dismiss));

        err_txtExtra.setText(getResources().getString(
                R.string.err_missing_images));

        List<String> listHeadings = new ArrayList<String>();
        List<String> listValues = new ArrayList<String>();

        ArrayList<Order> jobordersss = DBHelper.getOrders(DBHelper.whereJobListNotArchived,
                Constants.DB_TABLE_JOBLIST, new String[]{
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
                        Constants.DB_TABLE_JOBLIST_BRIEFING,},
                Constants.DB_TABLE_JOBLIST_JI);

        String h = "";
        for (int j = 0; j < pngItems.size(); j++) {
            // if (h.equals(getJobName(jobordersss, pngItems.get(j)))) {
            //
            // } else {
            // h = getJobName(jobordersss, pngItems.get(j));
            // listValues.add("-" + getJobName(jobordersss, pngItems.get(j)));
            // }
            listValues.add(pngItems.get(j).MediaFile);
        }

        ArrayAdapter adapter = new errIconAdapter(NewJobListActivity.this,
                listValues, listHeadings);
        err_listPreview.setAdapter(adapter);
        err_listPreview.setVisibility(RelativeLayout.VISIBLE);

        err_btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                err_dialog.dismiss();
            }
        });

        err_dialog.show();

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

    public void backToArchiveAlert(Context context, String textString) {

        final Dialog dialog = new Dialog(NewJobListActivity.this);
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

    public void customAlert(Context context, String textString) {
        final Dialog dialog = new Dialog(NewJobListActivity.this);
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

    public void customAlertUploadPendingJobs(Context context) {

        final Dialog dialog = new Dialog(NewJobListActivity.this);
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

    private static final String APPLICATION_ID = "mGR78GO99ppjKPFCT4uMmgDwNbCmEPjo7jgjmJoq";
    private static final String REST_API_KEY = "6s5hv6r8qcKdk3l9iJ1aUjDdGHWvc5IDMmUDBZj3";
    private static final String PUSH_URL = "https://api.parse.com/1/push";

    private void sendPost(ArrayList<String> channels, String type, String data,
                          String sound) throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("channels", new JSONArray(channels));

        JSONObject dataObj = new JSONObject();
        dataObj.put("alert", data);
        dataObj.put("sound", sound);

        if (type != null) {
            // ??type?????android?ios???
            jo.put("type", type);
        }
        jo.put("data", dataObj);

        this.pushData(jo.toString());
    }

    private void pushData(String postData) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        HttpEntity entity = null;
        String responseString = null;
        HttpPost httpost = new HttpPost(PUSH_URL);
        httpost.addHeader("X-Parse-Application-Id", APPLICATION_ID);
        httpost.addHeader("X-Parse-REST-API-Key", REST_API_KEY);
        httpost.addHeader("Content-Type", "application/json");
        StringEntity reqEntity = new StringEntity(postData);
        httpost.setEntity(reqEntity);
        response = httpclient.execute(httpost);
        entity = response.getEntity();
        if (entity != null) {
            responseString = EntityUtils.toString(response.getEntity());
        }

        System.out.println(responseString);
    }

    public UploadingProgressBars customProgressAlert(Context context) {
        final Dialog dialog = new Dialog(NewJobListActivity.this);
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

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mApiClient.connect();
    }

    public class GameAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub

            // WRITE_CODE if you need to do
            // anything when animation ends

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
            // WRITE_CODE if you need to repeat
            // any actions on animation repeat
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

            // WRITE_CODE if you need to do
            // anything on start animation

        }
    }

    public boolean onOptionsItemSelectedHS(int count) {
        switch (count) {
            case 0:

                startDownloadingJobs(false, false);
                break;
            case 1:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                start_uploading(false);
                break;
            case 2:
                // open job board
                // MAPSSSS
                // Getting status
                Intent intent = null;
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
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
            case 3:
                // open refund report
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        ShopperRefundReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                break;
            case 4:
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
                break;

            case 5:
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                finish();
                break;
            case 6:
                // FILTER jobs
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                break;

            case 7:
                // MAPSSSS
                // Getting status
                status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
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
                intent = new Intent(NewJobListActivity.this.getApplicationContext(),
                        CritHistoryReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);

                // historyOfReview();
                break;
            // case 11:// contact using whatsapp
            //
            // try {
            //
            // Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
            // Uri.parse("smsto:" + "" + Helper.helpline + "?body="
            // + ""));
            // sendIntent.setPackage("com.whatsapp");
            // startActivity(sendIntent);
            // } catch (Exception e) {
            // e.printStackTrace();
            // customAlert(
            // JobListActivity.this,
            // getResources().getString(
            // R.string.whatsapp_not_available));
            // // Intent sendIntent = new Intent(Intent.ACTION_DIAL);
            // // sendIntent.setData(Uri.parse("tel:" + Helper.helpline));
            // // startActivity(sendIntent);
            // // Toast.makeText(JobListActivity.this,
            // // "it may be you dont have whatsapp", Toast.LENGTH_LONG)
            // // .show();
            // }
            //
            // break;
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
        Intent intent = new Intent(NewJobListActivity.this.getApplicationContext(),
                ArchiveActivity.class);
        ArchiveActivity.toBeUploadedSQ = null;
        startActivityForResult(intent, JOB_ARCHIVE_ACTIVITY_CODE);
    }

    private void editShopperInfo() {

        if (IsInternetConnectted()) {
            new DoLoginTask(Constants.getEditShopperURL()).execute();
        } else {
            customAlert(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));
        }
    }

    private void historyOfReview() {
        new DoLoginTask(Constants.getSurveyHistoryURL()).execute();
    }

    private void showShopperInfo() {
        if (Helper.getSystemURL() != null && Helper.getSystemURL().toLowerCase().contains("ajis"))
            return;
        if (IsInternetConnectted()) {
            new DoLoginTask(Constants.getViewShopperURL()).execute();
        } else {
            customAlert(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));
        }
    }

    private void load_certificates(final ArrayList<Cert> shortList) {
        class JobTask extends AsyncTask<Void, Integer, String> {
            ProgressDialog dialogg = null;
            private String updateDate;

            @Override
            protected void onPreExecute() {
                dialogg = new ProgressDialog(NewJobListActivity.this);
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
                    Toast.makeText(NewJobListActivity.this,
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

    public void showCertificatesDialog(final ArrayList<Cert> listOfCerts) {
        final Dialog dialog = new Dialog(NewJobListActivity.this);
        dialog.setContentView(R.layout.dialog_checkertificates);
        ListView listJobs = (ListView) dialog.findViewById(R.id.lvjobs);
        listJobs.setAdapter(new CheckertificateAdapter(NewJobListActivity.this,
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
                    Toast.makeText(NewJobListActivity.this,
                            getResources().getString(R.string.alreadyBlocked),
                            Toast.LENGTH_LONG).show();

                } else if (listOfCerts.get(position).getStatus() != null
                        && listOfCerts.get(position).getStatus().toLowerCase()
                        .equals("passed")) {
                    Toast.makeText(NewJobListActivity.this,
                            getResources().getString(R.string.alreadyPassed),
                            Toast.LENGTH_LONG).show();
                } else if (listOfCerts.get(position).getTimesTaken() != null
                        && Integer.parseInt(listOfCerts.get(position)
                        .getTimesTaken()) >= Integer
                        .parseInt(listOfCerts.get(position)
                                .getMaxRetake())) {
                    Toast.makeText(NewJobListActivity.this,
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
                            NewJobListActivity.this);
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
                                            DownloadSetTask dSet = new DownloadSetTask(
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

    private String removeGarbageFromTop(String thisSet) {
//        if (thisSet!=null && thisSet.trim().toLowerCase().startsWith("<?xml version"))
//            return thisSet;
//        thisSet=thisSet.substring(thisSet.indexOf("<?xml version"));
        return thisSet;
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

            case 0:

                // open job board
                // MAPSSSS
                // Getting status
                Intent intent = null;
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
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
            case 1:
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        ShopperRefundReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                break;
            case 2:// shopper info
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                // showShopperInfo();
                break;

            case 3:
                // MAPSSSS
                // Getting status
                status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    comunicator.JobList = NewJobListActivity.this;
                    intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
                            MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;

            // case 4:// shopper info
            // showShopperInfo();
            // break;

            case 4:// edit hopper
                editShopperInfo();
                break;

            case 5:// reviews history
                isJobselected = true;
                intent = new Intent(this.getApplicationContext(),
                        CritHistoryReportActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);

                // historyOfReview();
                break;
            case 6:// contact using whatsapp

                try {

                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.parse("smsto:" + "" + Helper.helpline + "?body="
                                    + ""));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.whatsapp_not_available));
                    // Intent sendIntent = new Intent(Intent.ACTION_DIAL);
                    // sendIntent.setData(Uri.parse("tel:" + Helper.helpline));
                    // startActivity(sendIntent);
                    // Toast.makeText(JobListActivity.this,
                    // "it may be you dont have whatsapp", Toast.LENGTH_LONG)
                    // .show();
                }

                break;
        }
        return true;
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
            case 0:

                startDownloadingJobs(false, false);
                break;
            case 1:
                start_uploading(false);
                break;

            case 2:
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
                break;

            case 3:
                isJobselected = true;
                Intent intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                comunicator.JobList = null;
                startActivity(intent);
                finish();
                break;
            case 4:
                // FILTER jobs
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                break;

            case 5:
                // MAPSSSS
                // Getting status
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
                            MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    comunicator.JobList = null;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stubse

        super.onResume();
        Constants.setLocale(NewJobListActivity.this);
        // AutoSync();
    }

    public void onStartDevicePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasrecordaudio = this.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO);
            int hasaccessc = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            // int hasaccfinelocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //int hasaccesslocationextracommand = this.checkSelfPermission(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            int hasaccessnetworkstate = this.checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
            int hasintermission = this.checkSelfPermission(android.Manifest.permission.INTERNET);
            int hascamera = this.checkSelfPermission(android.Manifest.permission.CAMERA);
            // int haswriteexternalstorage = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasreadexternalstorage = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int haswakelock = this.checkSelfPermission(android.Manifest.permission.WAKE_LOCK);


            List<String> permissions = new ArrayList<String>();

            if (hasrecordaudio != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.RECORD_AUDIO);

            }

            if (hasaccessc != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

            }
//            if (hasaccfinelocation != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//            }
//            if (hasaccesslocationextracommand != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
//
//            }
            if (hasaccessnetworkstate != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);

            }

            if (hasintermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.INTERNET);

            }
            if (hascamera != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.CAMERA);

            }

//            if (haswriteexternalstorage != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//            }
            ///hasreadexternalstorage
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_list);
        comunicator.JobList = NewJobListActivity.this;
        Constants.setLocale(NewJobListActivity.this);
        if (Helper.isMisteroMenu) {
            setContentView(R.layout.job_list_mistero);

        } else
            setContentView(R.layout.job_list);


        mFilter = "scheduled";
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        isWifiOnly = myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false);
        // myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        int language = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);

        Locale locale = new Locale(
                Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                        Constants.SETTINGS_LANGUAGE_INDEX, 0)]);
        Locale.setDefault(locale);
        if (Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                Constants.SETTINGS_LANGUAGE_INDEX, 0)] != null && Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                Constants.SETTINGS_LANGUAGE_INDEX, 0)].equals("ja"))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        // prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY, Helper
        // .getSystemURL().replace("https://", "http://"));
        // prefsEditor.commit();
        Helper.setSystemURL(myPrefs.getString(
                Constants.SETTINGS_SYSTEM_URL_KEY, ""));
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));
        if (Helper.getSystemURL() == null || Helper.getSystemURL().equals("")) {

            finish();
        }

//        View menulayout = findViewById(R.id.menulayout);
//        menulayout.setVisibility(RelativeLayout.GONE);

        if (Build.VERSION.SDK_INT <= 10
                || (Build.VERSION.SDK_INT >= 14 && ViewConfiguration.get(this)
                .hasPermanentMenuKey())) {
            // menulayout.setVisibility(RelativeLayout.GONE);
        } else {
            // menulayout.setVisibility(RelativeLayout.VISIBLE);
            // View menubtn = findViewById(R.id.menubtn);
            // menubtn.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // openOptionsMenu();
            // }
            // });
        }
        menuView = findViewById(R.id.view_side_menu);
        menuListView = (ListView) findViewById(R.id.view_side_menu_list_view);
        Side_menu_top_green = (View) findViewById(R.id.view_side_menu_top_green);
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

        synchMenuItems = new ArrayList<com.checker.sa.android.data.MenuItem>();
        synchMenuItems
                .add(new com.checker.sa.android.data.MenuItem(
                        getString(R.string.start_download),
                        getString(R.string.start_download),
                        getIcon("downoad_ico")));// 0
        synchMenuItems.add(new com.checker.sa.android.data.MenuItem(
                getString(R.string.start_upload),
                getString(R.string.start_upload), getIcon("upload_ico")));// 1
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

        menuItems.add(new com.checker.sa.android.data.MenuItem(
                getString(R.string.job_list_menu_upload_complete_job),
                getString(R.string.job_list_menu_upload_complete_job),
                getIcon("filterjobs")));// 6

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
                        getIcon("archive")));// 8

        menuListView.setAdapter(new sideMEnuAdapter(NewJobListActivity.this,
                menuItems));

        initGoogleApiClient();
        NewLoginActivity.dataid = null;
        NewLoginActivity.thisOrder = null;
        NewLoginActivity.thisSet = null;
        NewLoginActivity.thisSavedAnswer = null;

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor outState = myPrefs.edit();

        if (outState != null) {
            outState.putBoolean("ispaused", false);
            outState.commit();
        }

        outState.putString(Constants.Crash_Last_SETID, "");
        outState.putString(Constants.Crash_Last_ORDERID, "");
        outState.commit();
        layoutAttachErr = (View) findViewById(R.id.layout_attach_err);
        dialogAttach = (RelativeLayout) findViewById(R.id.attachLayout);
        btnErr = (ImageView) findViewById(R.id.errorbtn);
        btnErr.setVisibility(RelativeLayout.GONE);
        btnErr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                makeErrorDialog(NewJobListActivity.this);
            }
        });

        layoutWorking = (RelativeLayout) findViewById(R.id.screenworking);
        layoutWorking.setVisibility(RelativeLayout.GONE);
        tabSync = (ToggleButton) findViewById(R.id.tab_sync);
        tabSync.setTextColor(Color.parseColor("#ffffff"));
        tabOne = (ToggleButton) findViewById(R.id.tab_one);
        tabTwo = (ToggleButton) findViewById(R.id.tab_two);
        tabThree = (ToggleButton) findViewById(R.id.tab_three);
        tabFour = (ToggleButton) findViewById(R.id.tab_four);

        tabOneb = (ToggleButton) findViewById(R.id.tab_one_bottom);
        tabTwob = (ToggleButton) findViewById(R.id.tab_two_bottom);
        tabThreeb = (ToggleButton) findViewById(R.id.tab_three_bottom);
        tabFourb = (ToggleButton) findViewById(R.id.tab_four_bottom);
        // tabOne.setPaintFlags(0);
        // tabTwo.setPaintFlags(0);
        // tabThree.setPaintFlags(0);
        // tabFour.setPaintFlags(0);
        sidemenuicon = (View) findViewById(R.id.sidemenuicon);
        ltabSync = (View) findViewById(R.id.layout_tab_sync);
        ltabOne = (View) findViewById(R.id.layout_tab_one);
        ltabTwo = (View) findViewById(R.id.layout_tab_two);
        ltabThree = (View) findViewById(R.id.layout_tab_three);
        ltabFour = (View) findViewById(R.id.layout_tab_four);

        imgtabSync = (ImageView) findViewById(R.id.img_tab_sync);
        imgtabOne = (ImageView) findViewById(R.id.img_tab_one);
        imgtabTwo = (ImageView) findViewById(R.id.img_tab_two);
        imgtabThree = (ImageView) findViewById(R.id.img_tab_three);
        imgtabFour = (ImageView) findViewById(R.id.img_tab_four);

        bimgtabSync = (ImageView) findViewById(R.id.img_tab_sync_balloon);
        bimgtabOne = (ImageView) findViewById(R.id.img_tab_one_balloon);
        bimgtabTwo = (ImageView) findViewById(R.id.img_tab_two_balloon);
        bimgtabThree = (ImageView) findViewById(R.id.img_tab_three_balloon);
        bimgtabFour = (ImageView) findViewById(R.id.img_tab_four_balloon);

        txttabSync = (TextView) findViewById(R.id.txt_tab_sync_balloon);
        txttabOne = (TextView) findViewById(R.id.txt_tab_one_balloon);
        txttabTwo = (TextView) findViewById(R.id.txt_tab_two_balloon);
        txttabThree = (TextView) findViewById(R.id.txt_tab_three_balloon);
        txttabFour = (TextView) findViewById(R.id.txt_tab_four_balloon);

        txttabOne.setTextColor(Color.parseColor("#ffffff"));
        txttabTwo.setTextColor(Color.parseColor("#ffffff"));
        txttabThree.setTextColor(Color.parseColor("#ffffff"));
        txttabFour.setTextColor(Color.parseColor("#ffffff"));

        tabSync.setTextSize(UIHelper.getFontSizeTabs(NewJobListActivity.this,
                tabSync.getTextSize()));
        tabOne.setTextSize(UIHelper.getFontSizeTabs(NewJobListActivity.this,
                tabOne.getTextSize()));
        tabTwo.setTextSize(UIHelper.getFontSizeTabs(NewJobListActivity.this,
                tabTwo.getTextSize()));
        tabThree.setTextSize(UIHelper.getFontSizeTabs(NewJobListActivity.this,
                tabThree.getTextSize()));
        tabFour.setTextSize(UIHelper.getFontSizeTabs(NewJobListActivity.this,
                tabFour.getTextSize()));

        loadViews();

        ManageTabs(2);

        // getRQSLocation loc = new getRQSLocation(NewJobListActivity.this);
        // loc.execute();
        sidemenuicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Refresh db here
                // openOptionsMenu();
                // open side menu here
                if (isMenuOpen) {
                    isMenuOpen = false;
                    menuView.setVisibility(RelativeLayout.GONE);
                } else {
                    isMenuOpen = true;
                    menuListView.setAdapter(new sideMEnuAdapter(NewJobListActivity.this,
                            menuItems));
                    menuView.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });

        tabOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManageTabs(1);
            }
        });
        tabTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManageTabs(2);
            }
        });
        tabThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManageTabs(3);
            }
        });
        tabFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ManageTabs(4);
            }
        });

        tabSync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSyncMenu = true;
                if (isMenuOpen) {
                    isMenuOpen = false;
                    menuView.setVisibility(RelativeLayout.GONE);
                } else {
                    isMenuOpen = true;
                    menuListView.setAdapter(new sideMEnuAdapter(NewJobListActivity.this,
                            synchMenuItems));
                    menuView.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });
        ltabSync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSyncMenu = true;
                if (isMenuOpen) {
                    isMenuOpen = false;
                    menuView.setVisibility(RelativeLayout.GONE);
                } else {
                    isMenuOpen = true;
                    menuListView.setAdapter(new sideMEnuAdapter(NewJobListActivity.this,
                            synchMenuItems));
                    menuView.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });

        ltabOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tabOne.setChecked(true);
                tabOneb.setChecked(true);
                ManageTabs(1);
            }
        });

        ltabTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tabTwo.setChecked(true);
                tabTwob.setChecked(true);
                ManageTabs(2);
            }
        });
        ltabThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tabThree.setChecked(true);
                tabThreeb.setChecked(true);
                ManageTabs(3);
            }
        });
        ltabFour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tabFour.setChecked(true);
                tabFourb.setChecked(true);
                ManageTabs(4);
            }
        });

        setInvertDisplay();

        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        Bundle b = getIntent().getExtras();
        boolean login_check = false;
        if (b != null) {
            login_check = b.getBoolean(Constants.IS_LOGIN);
        }

        System.out.println(login_check);
        DBAdapter db = new DBAdapter(this.getApplicationContext());

        // db.deleteDB();
        try {

            myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

            db.createDataBase(Helper.getSystemURLfromDB(),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Sets.setSets(DBHelper.getSetsRecords())
        // insertDummyData();
        Surveys.setSets(DBHelper.getSurveyyRecords());
        filterBtn = (ImageButton) findViewById(R.id.filterbtn);
        mapBtn = (ImageButton) findViewById(R.id.mapbtn);
        if (Helper.getTheme(NewJobListActivity.this) == 0) {
            findViewById(R.id.joblist).setVisibility(View.GONE);
            jobItemList = (ListView) findViewById(R.id.joblistdark);
        } else {
            findViewById(R.id.joblistdark).setVisibility(View.GONE);
            jobItemList = (ListView) findViewById(R.id.joblist);
        }
        tv = (TextView) findViewById(R.id.darktview);
        // if (Helper.getTheme(NewJobListActivity.this) == 1)
        // tv.setVisibility(View.VISIBLE);

        filterBtn.setOnClickListener(this);
        mapBtn.setOnClickListener(this);
        jobItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public Boolean hideShowRight(String type, ImageView imgRight) {
                if (type.equals("Assigned"))
                    return false;
                else if (type.equals("Scheduled"))
                    return false;
                else if (type.equals("survey"))// ct.getString(R.string.jd_begin_button_status_inprogress)))
                    return false;
                else {

                    // imgRight.setVisibility(RelativeLayout.VISIBLE);
                    return true;
                }
            }

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (mAdapter.joblistarray.get(arg2).listOrders != null
                        && mAdapter.joblistarray.get(arg2).listOrders.size() > 1) {
                    if (mAdapter.joblistarray != null
                            && mAdapter.joblistarray.get(arg2).orderItem != null
                            && mAdapter.joblistarray.get(arg2).orderItem
                            .getStatusName() != null
                            && hideShowRight(
                            mAdapter.joblistarray.get(arg2).orderItem
                                    .getStatusName(), null)) {
                        return;
                    }
                }
                if (mAdapter.joblistarray != null
                        && mAdapter.joblistarray.get(arg2).orderItem != null
                        && mAdapter.joblistarray.get(arg2).orderItem
                        .getStatusName() != null
                        && mAdapter.joblistarray.get(arg2).orderItem
                        .getStatusName().equals("wrong")) {
                    showRAlert(NewJobListActivity.this);
                } else {
                    Intent intent = new Intent(NewJobListActivity.this
                            .getApplicationContext(), JobDetailActivity.class);
                    JobDetailActivity
                            .setCertsCallback(new jobBoardCertsListener() {

                                @Override
                                public void certCallBack(ArrayList<Cert> certs) {
                                    load_certificates(certs);
                                }
                            });
                    isJobselected = true;
                    if (mAdapter.joblistarray != null
                            && mAdapter.joblistarray.size() > 0) {
                        if (mAdapter.joblistarray.get(arg2).orderItem != null) {
                            intent.putExtra("OrderID", mAdapter.joblistarray
                                    .get(arg2).orderItem.getOrderID());
                            intent.putExtra(
                                    Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                                    mAdapter.joblistarray.get(arg2).orderItem
                                            .getCount() + "");
                            String OrderID = mAdapter.joblistarray
                                    .get(arg2).orderItem.getOrderID();
                            String surveyId = "";
                            if (OrderID.contains("-")) {
                                surveyId = (OrderID.replace("-", ""));
                                Survey survey = Surveys.getCurrentSurve(surveyId);
                                boolean b = survey.isAllocationReached();
                                if (b)//ALLOCATION REACHED
                                {
                                    Toast.makeText(NewJobListActivity.this,
                                            getString(R.string.questionnaire_open_survey_alert)
                                            , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        } else if (mAdapter.joblistarray.get(arg2).surveyItem != null) {
                            intent.putExtra("SurveyID", mAdapter.joblistarray
                                    .get(arg2).surveyItem.getSurveyID());

                        }

                    } else
                        intent.putExtra(
                                Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                                "1");
                    intent.putExtra("OrderIndex", arg2);
                    intent.putExtra("Index", arg2);
                    // comunicator.JobList = null;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
            }
        });
        Helper.setConfigChange(false);
        loadOfflineJobs(login_check);
        AutoSync();

        String userName = myPrefs.getString(
                Constants.POST_FIELD_LOGIN_USERNAME, "");

        TextView tv = (TextView) findViewById(R.id.txtsettingmenu);
        String welcome = getString(R.string.login_heder);
        tv.setText(welcome + " " + userName);

        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showShopperInfo();
            }
        });
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();
        SplashScreen.sendCrashReport(myPrefs, NewJobListActivity.this);
    }

    protected int getMinute(String start) {
        // TODO Auto-generated method stub
        // 20:00
        if (start != null && !start.equals("")) {
            start = start.substring(start.indexOf(":") + 1);
            start = start.substring(0, start.indexOf(":"));
            int m = Integer.parseInt(start);
            return m;
        } else return 0;

    }

    protected int getHour(String start) {
        // TODO Auto-generated method stub
        // 20:00
        if (start != null && !start.equals("")) {
            start = start.substring(0, start.indexOf(":"));
            int m = Integer.parseInt(start);
            return m;
        } else return 0;
    }

    protected int getMonth(String date) {
        // TODO Auto-generated method stub
        // 2013-05-27

        date = date.trim();
        String month = date.substring(date.indexOf("-") + 1,
                date.lastIndexOf("-"));
        int m = Integer.parseInt(month);
        if (m == 0)
            return 0;
        return m - 1;

    }

    protected int getYear(String date) {
        // TODO Auto-generated method stub
        // 2013-05-27
        String day = date.substring(0, date.indexOf("-"));
        int d = Integer.parseInt(day);
        return d;
    }

    protected int getDay(String date) {
        // 2013-05-27

        date = date.trim();
        String month = date.substring(date.lastIndexOf("-") + 1);
        int m = Integer.parseInt(month);
        if (m == 0)
            return 0;
        return m;

    }

    String issue = "";

    public void showCalendar(final Order arg2) {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {

                    // @Override
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                String OrderID = null;
                                Order order = arg2;
                                Date d = null;
                                String str = "";
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat(
                                            "yyyy-MM-dd", Locale.ENGLISH);
                                    issue = "2466";
                                    if (order != null && order.getDate() != null)
                                        d = sdf.parse(order.getDate());
                                    DateFormat dateFormat = android.text.format.DateFormat
                                            .getDateFormat(getApplicationContext());
                                    str = dateFormat.format(d);
                                    // date_v.setText(str);
                                    issue = "2473";
                                    Intent intentCalendar = new Intent(Intent.ACTION_EDIT);
                                    intentCalendar.setType("vnd.android.cursor.item/event");
                                    Calendar c = Calendar.getInstance();
                                    issue = "unable to extract year from date:" + order.getDate();
                                    int year = getYear(order.getDate());
                                    issue = "unable to extract month from date:" + order.getDate();
                                    int month = getMonth(order.getDate());
                                    issue = "unable to extract day from date:" + order.getDate();
                                    int day = getDay(order.getDate());
                                    issue = "unable to extract hour from start time:" + order.getTimeStart();
                                    int hourStart = getHour(order.getTimeStart());
                                    issue = "unable to extract min from start time:" + order.getTimeStart();
                                    int minuteStart = getMinute(order.getTimeStart());
                                    issue = "unable to extract hour from end time:" + order.getTimeStart();
                                    int hourEnd = getHour(order.getTimeEnd());
                                    issue = "unable to extract min from end time:" + order.getTimeStart();
                                    int minuteEnd = getMinute(order.getTimeEnd());
                                    issue = "crashed";
                                    // year, month, day, hourOfDay, minute
                                    c.set(year, month, day, hourStart, minuteStart);
                                    long millis = c.getTimeInMillis();
                                    intentCalendar.putExtra("beginTime", millis);
                                    intentCalendar.putExtra("rrule", "FREQ=YEARLY");
                                    issue = "2496";
                                    c.set(year, month, day, hourEnd, minuteEnd);
                                    millis = c.getTimeInMillis();
                                    issue = "2499";
                                    intentCalendar.putExtra("endTime", millis);
                                    intentCalendar.putExtra("eventLocation",
                                            order.getCityName());
                                    issue = "2503";
                                    intentCalendar.putExtra(
                                            "description",
                                            "Client name: " + order.getClientName() + ",\n"
                                                    + "Branch Full name: "
                                                    + order.getBranchFullname() + ",\n"
                                                    + "Description: "
                                                    + order.getDescription());
                                    issue = "2511";
                                    intentCalendar.putExtra("title", "Questionnaire: "
                                            + order.getSetName());
                                    // comunicator.JobList = null;
                                    startActivity(intentCalendar);

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    customAlert(NewJobListActivity.this, issue + "|||Stack:" + e.getMessage() + "|||");
                                    Toast.makeText(NewJobListActivity.this, "parse exception" + order.getDate(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    customAlert(NewJobListActivity.this, issue + "|||Stack:" + e.getMessage() + "|||");
                                    Toast.makeText(NewJobListActivity.this, "simple exception" + order.getDate(), Toast.LENGTH_LONG).show();
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // No button clicked
                                break;
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string._alert_title));
        TextView textView = new TextView(NewJobListActivity.this);
        textView.setTextSize(UIHelper.getFontSize(NewJobListActivity.this,
                textView.getTextSize()));
        textView.setText(getString(R.string.list_enable_calendar));
        builder.setView(textView);
        builder.setPositiveButton(
                        getString(R.string.questionnaire_exit_delete_alert_yes),
                        dialogClickListener)
                .setNegativeButton(
                        getString(R.string.questionnaire_exit_delete_alert_no),
                        dialogClickListener).show();

    }

    private void AutoSync() {
        if (Helper.isMisteroMenu) {
            ArrayList<SubmitQuestionnaireData> sqdList = getNumberofQuestionnaire(
                    false, false);
            if (sqdList != null && sqdList.size() > 0) {
                customAlertUploadPendingJobs(NewJobListActivity.this);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss",
                Locale.ENGLISH);
        date = Calendar.getInstance();
        if (timer == null) {
            // timer = new Timer();
            // timer.scheduleAtFixedRate(new UploadCompletedJobsTask(),
            // date.getTime(), MILISECONND_5_MINS);
        }

        if (downloadjoblistTimer == null) {
            downloadjoblistTimer = new Timer();
            date.set(Calendar.HOUR, 6);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            date.set(Calendar.AM_PM, 0);

            if (System.currentTimeMillis() > date.getTime().getTime()) {
                Date d1 = new Date(date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH) + 1, 6, 0);
                // downloadjoblistTimer.scheduleAtFixedRate(
                // new DownloadJobAndQuestionnaireTask(), d1,
                // MILISECONND_24_HOURS);
            } else {
                // downloadjoblistTimer.scheduleAtFixedRate(
                // new DownloadJobAndQuestionnaireTask(), date.getTime(),
                // MILISECONND_24_HOURS);
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    // /////////////////////////////////Auto
    // Synch///////////////////////////////////

    class DownloadJobAndQuestionnaireTask extends TimerTask {
        @Override
        public void run() {

            if (IsInternetConnectted()) {
                // Helper helper = new Helper();
                // helper.deleteFilesInFolder();
                // long tp = lastJobAndQuestionnaireDownloaded;
                long temp = System.currentTimeMillis()
                        - myPrefs.getLong(Constants.AUTOSYNC_CURRENT_TIME, 0);
                if (temp > MILISECONND_24_HOURS) {
                    if (Connector.cookies == null) {
                        if (showLogin(doLogin()))
                            return;
                        getJoblistData();
                        String result = QuestionnaireListPost();
                        if (result.contains("<script>")) {
                            doLogin();
                            result = QuestionnaireListPost();
                        }
                        if (result.contains("timeout"))
                            return;
                        if (result.contains("error="))
                            return;
                        parser = new Parser();
                        parser.parseXMLValues(result,
                                Constants.QUES_RESP_FIELD_PARAM);
                    }
                }
            }
        }
    }

    private void changeSyncText(final String text) {
        NewJobListActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                tabSync.setText(text);
                ShowDBJobs();
            }
        });

    }

    class UploadCompletedJobsTask extends TimerTask {
        @Override
        public void run() {
            if (IsInternetConnectted()) // check for www.google.com
            {
                new NewJobListActivity.BackGroundThread().start();

                changeSyncText(getResources().getString(
                        R.string.job_list_tab_syncing));

            }
        }
    }

    class BackGroundThread extends Thread {
        @Override
        public void run() {
            if (IsInternetConnectted()) {
                if (Connector.cookies == null) {
                    if (showLogin(doLogin()))
                        return;
                }

                SubmitSurvey();
            }
        }


    }

    private void getJoblistData() {
        try {
            String result = JobListPost();
            if (result.indexOf("<orders>") == -1 && result.contains("<script>")) {
                doLogin();
                result = JobListPost();
            }
            if (result.contains("timeout"))
                return;
            if (result.contains("error="))
                return;
            SplashScreen.addLog(new BasicLog(Constants.getJobListURL("11.90"),
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "DOWNLOADING jobs" + result, "DOWNLOADING"));

            new Parser().parseXMLValues(result,
                    Constants.JOB_LIST_RESP_FIELD_PARAM);
        } catch (Exception ex) {
            Toast.makeText(NewJobListActivity.this,
                    getResources().getString(R.string.errorParsingOrder),
                    Toast.LENGTH_LONG).show();
        }
        // new saveSetThread().start();
    }

    private String JobListPost() {

        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        String app_ver = "";
        try {
            app_ver = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return Connector.postForm(Constants.getJobListURL(app_ver),
                extraDataList);
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

    private String QuestionnaireListGet() {
        // Initialize the login data to POST
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return Connector.getData(Constants.getQestionnaireURL(null));
    }

    private boolean SubmitSurvey() {
        try {
            sqd = getNumberofQuestionnaire(false, false);
        } catch (IllegalStateException ex) {
            sqd = null;
        } catch (IllegalMonitorStateException ex) {
            sqd = null;
        }

        if (sqd == null || sqd.size() < 1) {
            // String where = Constants.DB_TABLE_JOBLIST_SN + "="
            // + "\"Completed\"";
            //
            // DBHelper.deleteJoblistRecords(where);
            //
            changeSyncText(getResources().getString(R.string.job_list_tab_sync));

            return false;
        }
        sqd = validateAllSQ(sqd);
        boolean jobuploaded = false;
        boolean isquotafull = false;
        ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
        try {
            for (int i = 0; i < sqd.size(); i++) {
                SubmitQuestionnaireData sq = sqd.get(i);

                String setId = DBHelper.getShelfSetIdItemsForJobList(
                        Constants.DB_TABLE_POS,
                        new String[]{Constants.DB_TABLE_POS_SetId},
                        Constants.DB_TABLE_POS_OrderId + "=" + "\""
                                + sq.getOrderid() + "\"");
                POS_Shelf pos_shelf_item = null;
                Set set = null;
                if (setId != null) {
                    set = validationSets.getSetAvailable(setId);
                    if (set != null) {
                        pos_shelf_item = new POS_Shelf(NewJobListActivity.this);
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
                        false, sq, pos_shelf_item);

                uploadList = DBHelper.getQuestionnaireUploadFilesInDB(
                        Constants.UPLOAD_FILE_TABLE,
                        new String[]{Constants.UPLOAD_FILe_MEDIAFILE,
                                Constants.UPLOAD_FILe_DATAID,
                                Constants.UPLOAD_FILe_ORDERID,
                                Constants.UPLOAD_FILe_BRANCH_NAME,
                                Constants.UPLOAD_FILe_CLIENT_NAME,
                                Constants.UPLOAD_FILe_DATE,
                                Constants.UPLOAD_FILe_SET_NAME,
                                Constants.UPLOAD_FILe_SAMPLE_SIZE,
                                Constants.UPLOAD_FILe_PRODUCTID,
                                Constants.UPLOAD_FILe_LOCATIONID,},
                        Constants.DB_TABLE_SUBMITSURVEY_OID + "=" + "\""
                                + sq.getOrderid() + "\"",
                        Constants.DB_TABLE_SUBMITSURVEY_OID, uploadList);
                renameCamFiles(uploadList, sq.getUnix());

                String result = Connector.postForm(
                        Constants.getSubmitSurveyURL(), nvp);
                for (int j = 0; isquotafull == false && j < uploadList.size(); j++) {
                    String path = uploadList.get(j).getFilePath();
                    if (uploadList.get(j).getFilePath().startsWith("content")) {
                        path = getRealPathFromURI(Uri.parse(uploadList.get(j)
                                .getFilePath()));
                    } else if (uploadList.get(j).getFilePath()
                            .startsWith("file:///")) {
                        path = path.replace("file:///", "/");
                    }
                    String did = uploadList.get(j).getDataID();
                    if (did.contains("^") || did.contains("=")) {
                        did = getMePrefix(did);
                    }
                    myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
                    String forceSmping = null;
                    if (set == null) {
                        String setlink = DBHelper
                                .getSetIdFromOrder(
                                        Constants.DB_TABLE_JOBLIST,
                                        new String[]{Constants.DB_TABLE_JOBLIST_SETID},
                                        Constants.DB_TABLE_JOBLIST_ORDERID
                                                + "=" + "\"" + sq.getOrderid()
                                                + "\"");
                        try {
                            set = (Set) DBHelper.convertFromBytes(setlink);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            set = null;
                        }
                    }
                    if (set != null)
                        forceSmping = set.getForceImageStamp();
                    String res = Connector.saveFiletoServer(
                            (forceSmping != null && forceSmping.equals("1")),
                            path, Constants.getAttachmentURL(),
                            sq.getOrderid(), did, sq.getUnix(),
                            uploadList.get(j).getUPLOAD_FILe_Sample_size(), uploadList.get(j).getUPLOAD_FILe_PRODUCTID(), uploadList.get(j).getUPLOAD_FILe_LOCATIONID());
                    if (CheckResponse(res)) {
                        try {
                            DBHelper.deleteFile(sqd.get(i).getOrderid(),
                                    uploadList.get(j).getFilePath());
                            String where = Constants.UPLOAD_FILe_ORDERID + "="
                                    + "\"" + sqd.get(i).getOrderid()
                                    + "\" AND "
                                    + Constants.UPLOAD_FILe_MEDIAFILE + "=\""
                                    + uploadList.get(j).getFilePath() + "\"";
                            DBAdapter.openDataBase();
                            DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE,
                                    where, null);
                            DBAdapter.closeDataBase();

                            if (path.contains(Constants.UPLOAD_PATH)) {
                                File file = new File(path);
                                file.delete();
                            }
                        } catch (Exception ex) {
                            String str = "";
                            str += "";
                        }

                    } else {
                        SplashScreen.addLog(new BasicLog(Constants.getAttachmentURL(),
                                myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                                myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                                "Attachments Uploading:NOTSUCCESS" + path + " ORDER" + sq.getOrderid() + "RESPONSE=" + res, "ORPHAN"));

                        if (CheckResponseForStorageQuota(res)) {
                            ((Activity) NewJobListActivity.this)
                                    .runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            ShowAlert(
                                                    NewJobListActivity.this,
                                                    "",
                                                    getResources()
                                                            .getString(
                                                                    R.string.qoutafullMsg),
                                                    "Ok");
                                        }
                                    });
                            isquotafull = true;
                            // imgmsg="Storage Quota Full!";
                            break;
                        }
                    }
                }

                if (CheckResponse(result)) {
                    try {
                        String where = Constants.DB_TABLE_JOBLIST_ORDERID + "="
                                + "\"" + sqd.get(i).getOrderid() + "\"";
                        Calendar myCalendar = Calendar.getInstance();
                        DBHelper.updateOrders(
                                Constants.DB_TABLE_ORDERS,
                                new String[]{
                                        Constants.DB_TABLE_ORDERS_ORDERID,
                                        Constants.DB_TABLE_ORDERS_STATUS,
                                        Constants.DB_TABLE_ORDERS_START_TIME,},
                                sqd.get(i).getOrderid(),
                                "uploaded on "
                                        + sdf.format(myCalendar.getTime()), "", null);
                    } catch (Exception ex) {
                        String str = "";
                        str += "";
                    }
                } else {
                    try {
                        String where = Constants.DB_TABLE_JOBLIST_ORDERID + "="
                                + "\"" + sqd.get(i).getOrderid() + "\"";
                        DBHelper.incrementTriesAgainstOrderId(where, sqd.get(i)
                                .getTries());

                    } catch (Exception ex) {
                        String str = "";
                        str += "";
                    }
                }

            }
        } catch (Exception ex) {
            int i = 0;
            i++;
        }
        if (jobuploaded) {
            jobuploaded = false;
            progressHandler.sendEmptyMessage(0);
        }

        changeSyncText(getResources().getString(R.string.job_list_tab_sync));

        return true;
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

    private String getCorrectDataIdForLoop(String thisDataId,
                                           SubmitQuestionnaireData sqd) {
        ArrayList<QuestionnaireData> questionnaireData = getQuestionnaireData(sqd
                .getOrderid());
        if (questionnaireData == null) {
            questionnaireData = new ArrayList<QuestionnaireData>();
        }

        if (questionnaireData.size() == 0)
            return thisDataId;
        for (int i = 0; i < questionnaireData.size(); i++) {

            QuestionnaireData qd = questionnaireData.get(i);
            if (qd.getDataID().equals(thisDataId)) {
                return getMePrefix(qd.getLoopinfo());
            }
        }
        return thisDataId;
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
            app_ver = NewJobListActivity.this.getPackageManager().getPackageInfo(
                    NewJobListActivity.this.getPackageName(), 0).versionName;
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

    // ////////////////////////////////////////////////////////////////////////////////

    private void executeLangList() {
        try {
            LangTask langTaskHandler = new LangTask();
            langTaskHandler.execute();

        } catch (Exception ex) {
            ShowAlert(NewJobListActivity.this,
                    NewJobListActivity.this.getString(R.string.alert_working),
                    NewJobListActivity.this.getString(R.string.alert_working_msg),
                    NewJobListActivity.this.getString(R.string.button_ok));
        }
    }

    public void executeJobList(boolean isPull, boolean isOrderOnly) {
        try {
            jobListTaskHandler = new JobbListTask(isPull, isOrderOnly);
            jobListTaskHandler.execute();

        } catch (Exception ex) {
            try {
                ShowAlert(NewJobListActivity.this,
                        NewJobListActivity.this.getString(R.string.alert_working),
                        NewJobListActivity.this
                                .getString(R.string.alert_working_msg),
                        NewJobListActivity.this.getString(R.string.button_ok));

            } catch (Exception e) {
            }
        }
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

    private void loadOfflineJobs(boolean login_check) {
        // SQLiteDatabase db = DBAdapter.openDataBase();
        // Toast.makeText(JobListActivity.this, "Showing DB jobs",
        // Toast.LENGTH_LONG).show();
        ShowDBJobs();
        // Toast.makeText(JobListActivity.this, "DB jobs shown",
        // Toast.LENGTH_LONG)
        // .show();
        // validateJobs();

        // /////////////

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

    private String getJobName(ArrayList<Order> jobordersss, pngItem icon) {
        if (jobordersss != null && jobordersss.size() > 0)
            for (int i = 0; i < jobordersss.size(); i++) {
                if (icon.SetID.equals(jobordersss.get(i).getSetLink())) {
                    icon.OrderID = jobordersss.get(i).getSetName();
                    return jobordersss.get(i).getSetName();
                }
            }
        return icon.SetID;
    }

    ArrayList<pngItem> pngItems;
    private String filterString;
    private boolean isSyncMenu;

    private class LongOrphanOperation extends
            AsyncTask<String, Void, ArrayList<filePathDataID>> {

        @Override
        protected ArrayList<filePathDataID> doInBackground(String... params) {
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
                showOrphanImages(uploadList, NewJobListActivity.this);

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
                showListTaskHandler = new ShowDBListTask(
                        Revamped_Loading_Dialog.getDialog(), orderid);
                showListTaskHandler.execute();

            } else {
                Revamped_Loading_Dialog.hide_dialog();
            }
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                    getResources()
                            .getString(R.string.downloadingCheckrtificate));
        }
    }

    private class LongOperation extends AsyncTask<String, Void, ArrayList<orderListItem>> {

        public Context con;
        ArrayList<BranchProperties> branchProps;
        ArrayList<Order> jobordersss = null;

        @Override
        protected ArrayList<orderListItem> doInBackground(String... params) {
            //String ret = ShowDBJobss("task");

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
            ArrayList<orderListItem> joborders = null;
            try {

                if (jobordersss != null) {
                    if (pngItems != null && pngItems.size() > 0 || isBranchPropErr == true) {
                        btnErr.setVisibility(RelativeLayout.VISIBLE);
                    } else {
                        btnErr.setVisibility(RelativeLayout.GONE);
                    }
                    joborders = new ArrayList<orderListItem>();
                    for (int i = 0; i < jobordersss.size(); i++) {

                        joborders.add(new orderListItem(jobordersss.get(i), null));
                    }

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
            return joborders;
        }

        @Override
        protected void onPostExecute(ArrayList<orderListItem> joborders) {
            SplashScreen.addLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                    "POST_STARTED", "LONG_OPERATION"));
            try {
                if (joborders != null && joborders.size() >= 1) {
                    joborders = joborders;
                    showDbjobsPostPart();
                    if (mFilter.equals("completed")) {
                        ShowOrphanFiles();
                    }
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
            try {
                SplashScreen.addLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                        "PRE_STARTED", "LONG_OPERATION"));
                con = NewJobListActivity.this;

                Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this, con
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

    ArrayList<Order> jobordersss = null;

    private void ShowOrphanFiles() {
        LongOrphanOperation op = new LongOrphanOperation();
        op.execute();

    }

    private void showDBListAll(String certorderid) {
        Locale current = getResources().getConfiguration().locale;
        try {
            Revamped_Loading_Dialog.hide_dialog();

        } catch (Exception ex) {
        }
        Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                getResources().getString(R.string.alert_switching));
    }

    ArrayList<BranchProperties> branchProps = null;

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
                Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
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
                showDbjobsPostPart();
                // ManageTabs(1);

            }

            if (pngItems != null && pngItems.size() > 0
                    || isBranchPropErr == true) {
                if (btnErr != null)
                    btnErr.setVisibility(RelativeLayout.VISIBLE);
            } else {
                if (btnErr != null)
                    btnErr.setVisibility(RelativeLayout.GONE);
            }

            Revamped_Loading_Dialog.hide_dialog();
            cleanUploaedJobsHere(null);

            if (certorderid != null) {

                Intent intent = new Intent(
                        NewJobListActivity.this.getApplicationContext(),
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
                    btnErr.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    btnErr.setVisibility(RelativeLayout.GONE);
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
                    // setSurveyList'();
                    // seting list here
                    showDbjobsPostPart();
                }
            }

        } catch (Exception ex) {
            int i = 0;
            i++;
            Toast.makeText(NewJobListActivity.this, "DB jobs Crashed here",
                    Toast.LENGTH_LONG).show();
        }

    }

    public String ShowDBJobss(String input) {
        try {

            ArrayList<Order> jobordersss = DBHelper
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

            // int valueid = c.getColumnIndex(columns[0]);
            // int propid = c.getColumnIndex(columns[1]);
            // int propname = c.getColumnIndex(columns[2]);
            // int content = c.getColumnIndex(columns[3]);
            ArrayList<BranchProperties> branchProps = DBHelper.getBranchPropds(
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

                if (pngItems.size() > 0 || isBranchPropErr == true) {
                    btnErr.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    btnErr.setVisibility(RelativeLayout.GONE);
                }
                joborders = new ArrayList<orderListItem>();
                for (int i = 0; i < jobordersss.size(); i++) {

                    joborders.add(new orderListItem(jobordersss.get(i), null));
                }
                if (joborders != null && joborders.size() >= 1) {
                    Orders.setListOrders(jobordersss);
                    Orders.setBranchProps(branchProps);
                    Surveys.setSets(DBHelper.getSurveyyRecords());
                    setOrderList();
                    // setSurveyList'();
                    // seting list here
                    return "Executed";
                    // ManageTabs(1);

                }
            }
        } catch (Exception ex) {
            int i = 0;
            i++;
        }
        return "exx";
    }

    private void showDbjobsPostPart() {
        if (CheckerApp.globalFilterVar != null)
            FilterJobList(CheckerApp.globalFilterVar);
        else {
            try {
                mAdapter = new JobItemAdapter(NewJobListActivity.this, joborders,
                        mFilter, bimgtabSync, bimgtabOne, bimgtabTwo, bimgtabThree,
                        bimgtabFour, txttabSync, txttabOne, txttabTwo, txttabThree,
                        txttabFour, ltabOne, ltabTwo, ltabThree, ltabFour, null, null);
                mAdapter.setBranchCallback(this);
                mAdapter.setDateCallback(this);
                mAdapter.doFilter(mFilter, NewJobListActivity.this, true);
                updateFiler(null);
                jobItemList.setAdapter(mAdapter);
            } catch (Exception ex) {
                int i = 0;
                i++;
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

    public static void setAlternateURL(String newEurURL, SharedPreferences myPrefs) {
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, newEurURL);
        prefsEditor.commit();
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();

        if (isSyncMenu) {
            // menu.add(0, Constants.MENUID_UPDALOAD_IMGS, 0,
            // "Force Send camera images");
            menu.add(0, Constants.MENUID_UPDALOAD_JOBS, 0,
                    NewJobListActivity.this.getString(R.string.start_upload));
            menu.add(0, Constants.MENUID_DOWNLOAD_JOBS, 0,
                    NewJobListActivity.this.getString(R.string.start_download));
            menu.add(0, Constants.MENUID_DOWNLOAD_JOBS_ALT, 0,
                    NewJobListActivity.this.getString(R.string.start_download_alt));
            isSyncMenu = false;
        } else {

            if (isMenuOpen) {
                isMenuOpen = false;
                menuView.setVisibility(RelativeLayout.GONE);
            } else {
                isMenuOpen = true;
                menuView.setVisibility(RelativeLayout.VISIBLE);
            }
        }
        return true;
    }

    public void showRAlert(Context context) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            // @Override
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        startDownloadingJobs(false, false);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string._alert_title));
        TextView textView = new TextView(context);
        textView.setTextSize(UIHelper.getFontSize(NewJobListActivity.this,
                textView.getTextSize()));
        textView.setText(getString(R.string.save_questionnaire_records_for_offline));
        builder.setView(textView);
        builder.setPositiveButton(
                        getString(R.string.questionnaire_exit_delete_alert_yes),
                        dialogClickListener)
                .setNegativeButton(
                        getString(R.string.questionnaire_exit_delete_alert_no),
                        dialogClickListener).show();

    }

    public boolean startDownloadingJobs(boolean isPullToRefreshLibrary,
                                        boolean isOrderOnly) {
        btnErr.setVisibility(RelativeLayout.GONE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(Constants.AUTOSYNC_CURRENT_TIME,
                System.currentTimeMillis());
        prefsEditor.commit();

        executeJobList(isPullToRefreshLibrary, isOrderOnly);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case Constants.MENUID_UPDALOAD_IMGS:
                uploadingOrphanFiesTask uploadFileTask = new uploadingOrphanFiesTask(
                        null, true);
                uploadFileTask.execute();
                break;

            case Constants.MENUID_UPDALOAD_JOBS:
                start_uploading(false);
                break;

            case Constants.MENUID_DOWNLOAD_JOBS:

                startDownloadingJobs(false, false);
                break;
            case Constants.MENUID_DOWNLOAD_JOBS_ALT:
                showLanguageDialog(
                        getResources().getString(
                                R.string.preffered_questionnaire_language), false);
                break;
            case Constants.MENUID_UPDALOAD_COMPLETE_JOBS:
                // FILTER jobs
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                break;

            case Constants.MENUID_BUG:
                // try {
                // final ParseObject gameScore = new ParseObject("BugReport");
                //
                // gameScore.put("screenshot", Helper
                // .TakeScreenShot(findViewById(android.R.id.content)));
                // Helper.customCrashAlert(JobListActivity.this,
                // "Please enter comment for this bug:", gameScore);
                //
                // } catch (Exception ex) {
                // }

                return true;

            case Constants.MENUID_DOWNLOAD_UPDATED_JOBS:
                // MAPSSSS
                // Getting status
                int status = GooglePlayServicesUtil
                        .isGooglePlayServicesAvailable(getBaseContext());

                if (status != ConnectionResult.SUCCESS) {
                    customAlert(
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.google_services_not_avaliable));
                } else {
                    isJobselected = true;
                    Intent intent = new Intent(
                            NewJobListActivity.this.getApplicationContext(),
                            MapActivity.class);
                    intent.putExtra("orderid", "-1");
                    // comunicator.JobList = null;
                    startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                }
                break;

            case Constants.MENUID_DOWNLOAD_EXIT_JOBLIST:
                ExitFromJobList();
                break;

            case Constants.MENUID_DOWNLOAD_SETTINGS:
                isJobselected = true;
                Intent intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                // comunicator.JobList = null;
                startActivity(intent);
                finish();
                break;
        }
        return true;
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

    public void start_uploading(boolean isProgress) {


        if (Helper.syncing == true) {
            Toast.makeText(
                    NewJobListActivity.this,
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
                            NewJobListActivity.this,
                            getResources().getString(
                                    R.string.wifi_not_enabled));
                    return;

                }
            }

            SubmitSurveyTask sbmtSurveyTask = new SubmitSurveyTask();
            sbmtSurveyTask.isOnlyCertificate = upload_comp_jobs;
            upload_comp_jobs = false;
            sbmtSurveyTask.isProgress = isProgress;
            sbmtSurveyTask.execute();

        } else {
            ShowDBJobs();
            customAlert(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.no_internet_connection_alret_message));

        }

    }

    public void showLanguageDialog(String string, final boolean isFirstTime) {
        final LanguageDialog langDialog = new LanguageDialog(
                NewJobListActivity.this, string);
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

    private void ExitFromJobList() {
        mAdapter = null;
        jobItemList.setAdapter(null);
        isJobselected = true;
        Intent intent = new Intent(this.getApplicationContext(),
                NewLoginActivity.class);
        intent.putExtra("FromJoblist", true);
        // comunicator.JobList = null;
        startActivity(intent);
        finish();
        if (validationSets.sets != null)
            validationSets.sets.clear();

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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        DBAdapter.closeDataBase();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.filterbtn:
                JobFilterDialog dialog = new JobFilterDialog(this);
                dialog.show();
                isJobselected = true;
                break;
            case R.id.mapbtn:
                isJobselected = true;
                Intent intent = new Intent(
                        NewJobListActivity.this.getApplicationContext(),
                        Context.class);
                intent.putExtra("OrderIndex", -1);
                // comunicator.JobList = null;
                startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
                break;
        }
    }

    public String FilterJobList(FilterData fData) {
        CheckerApp.globalFilterVar = fData;
        filterString = "";

        if (!fData.jobtype.equals(getString(R.string.job_filter_default_dd_option))) {
            filterString += fData.jobtype;
        }
        if (!fData.region.equals(getString(R.string.job_filter_default_dd_option))) {
            if (filterString.equals("")) {
                filterString += fData.region;
            } else
                filterString += ", " + fData.region;
        }
        if (!fData.project.equals(getString(R.string.job_filter_default_dd_option))) {
            if (filterString.equals("")) {
                filterString += fData.project;
            } else
                filterString += ", " + fData.project;
        }
        if (!fData.city.equals(getString(R.string.job_filter_default_dd_option))) {
            if (filterString.equals("")) {
                filterString += fData.city;
            } else
                filterString += ", " + fData.city;
        }
        if (!fData.bcode.equals(getString(R.string.job_filter_default_dd_option))) {
            if (filterString.equals("")) {
                filterString += fData.bcode;
            } else
                filterString += ", " + fData.bcode;
        }
        if (!fData.bprop.equals(getString(R.string.job_filter_default_dd_option))) {
            if (filterString.equals("")) {
                filterString += fData.bprop;
            } else
                filterString += ", " + fData.bprop;
        }
        if (!fData.date1.equals("1/1/1900") && !fData.date3.equals("1/1/1900")) {
            if (filterString.equals("")) {
                filterString += fData.date1 + "-" + fData.date3;
            } else
                filterString += ", " + fData.date1 + "-" + fData.date3;
        }

        joborders = getFilterArray(fData);


        mAdapter = new JobItemAdapter(NewJobListActivity.this, joborders, mFilter,
                bimgtabSync, bimgtabOne, bimgtabTwo, bimgtabThree, bimgtabFour,
                txttabSync, txttabOne, txttabTwo, txttabThree, txttabFour,
                ltabOne, ltabTwo, ltabThree, ltabFour,null,null);
        mAdapter.setBranchCallback(this);
        updateFiler(filterString);
        try {
            mAdapter.doFilter(mFilter, NewJobListActivity.this, true);
            jobItemList.setAdapter(mAdapter);
        } catch (Exception ex) {
        }
        return filterString;
    }

    private ArrayList<orderListItem> getFilterArray(FilterData fData) {

        ArrayList<Order> ordrs = new ArrayList<Order>();
        if (fData.jobtype.equals(getString(R.string.job_filter_default_dd_option))
                && fData.city.equals(getString(R.string.job_filter_default_dd_option))
                && fData.bcode.equals(getString(R.string.job_filter_default_dd_option))
                && fData.project
                .equals(getString(R.string.job_filter_default_dd_option))
                && fData.region
                .equals(getString(R.string.job_filter_default_dd_option))
                && fData.bprop.equals(getString(R.string.job_filter_default_dd_option))
                && fData.date1.equals("1/1/1900") && fData.date3.equals("1/1/1900")) {
            // joborders = Orders.getOrders();

            for (int ordercount = 0; ordercount < Orders.getOrders().size(); ordercount++) {
                Order order = Orders.getOrders().get(ordercount);
                {

                    ordrs.add(order);
                }
            }

            // mAdapter = new JobItemAdapter(NewJobListActivity.this, joborders,
            // mFilter, imgtabSync, imgtabOne, imgtabTwo, imgtabThree,
            // imgtabFour, txttabSync, txttabOne, txttabTwo, txttabThree,
            // txttabFour);
            // jobItemList.setAdapter(mAdapter);
            // jobItemList.setAdapter(new JobItemAdapter(NewJobListActivity.this,
            // joborders));
            // return;
        } else {
            for (int ordercount = 0; ordercount < Orders.getOrders().size(); ordercount++) {
                Order order = Orders.getOrders().get(ordercount);
                if (Helper.IsValidOrder(order, fData.region, fData.project, fData.bprop, fData.bcode,
                        fData.jobtype, fData.city, fData.date1, fData.date3,
                        getString(R.string.job_filter_default_dd_option))) {
                    ordrs.add(order);
                }
            }
        }
        joborders.clear();

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
        temporder = (ArrayList<Order>) ordrs.clone();
        int size = temporder.size();
        for (int index = 0; index < size; index++) {
            order = temporder.get(index);
            String status = getStatusByOrderID(order.getOrderID(), ordersArr);
            if (!status.equals(""))
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
                            && (Constants.getDateFilter() || order.getSetLink()
                            .equals(innerorder.getSetLink()))
                            && (Constants.getDateFilter() || order
                            .getStatusName().equals(
                                    innerorder.getStatusName()))) {
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
        return joborders;
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    private void setSurveyList() {

        Surveys.setSets(DBHelper.getSurveyyRecords());
        ArrayList<Survey> ordrs = Surveys.getSets();
        Survey[] ordersArr = null;
        if (ordrs != null) {
            ordersArr = new Survey[ordrs.size()];
            ordrs.toArray(ordersArr);
        }
        Survey order, innerorder;
        if (joborders == null)
            joborders = new ArrayList<orderListItem>();
        // joborders.clear();
        ArrayList<Survey> delete = new ArrayList<Survey>();
        ArrayList<orderListItem> temporder1 = new ArrayList<orderListItem>();
        ArrayList<Survey> temporder = new ArrayList<Survey>();
        temporder = (ArrayList<Survey>) Surveys.getSets().clone();
        int size = temporder.size();
        // for (int index = 0; index < size; index++) {
        // order = temporder.get(index);
        // String status = getStatusByOrderID(order.getOrderID(), ordersArr);
        // if (!status.equals(""))
        // temporder.get(index).setStatusName(status);
        // }

        for (int index = 0; index < size; index++) {
            order = temporder.get(index);
            order.setJobCount(0);
            for (int innerindex = 0; innerindex < size; innerindex++) {
                innerorder = temporder.get(innerindex);
                try {
                    if ((order.getSetLink().equals(innerorder.getSetLink()))) {
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
            delete = new ArrayList<Survey>();
        }

        joborders.addAll(temporder1);
        // delete.clear();
        delete = null;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        boolean dontrun = false;
        comunicator.JobList = NewJobListActivity.this;
        switch (requestCode) {
            case (JOB_GPS_CODE): {
                startLocationChecker();
                break;
            }
            case (JOB_ARCHIVE_ACTIVITY_CODE): {
                if (ArchiveActivity.toBeUploadedSQ != null) {
                    SubmitSurveyTask sbmtSurveyTask = new SubmitSurveyTask(ArchiveActivity.toBeUploadedSQ);
                    sbmtSurveyTask.execute();
                }
                break;
            }
            case (JOB_DETAIL_ACTIVITY_CODE): {
                if (data != null && data.hasExtra("from_watch")) {
                    ShowDBJobs();
                    return;
                }
                String orderid = "";
                // if (resultCode == Activity.RESULT_OK) {
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
                // setSurveyList();
                if (CheckerApp.globalFilterVar != null) {
                    //updateFiler(null);
                    joborders = getFilterArray(CheckerApp.globalFilterVar);
                } else updateFiler(null);
                if (mAdapter == null) {
                    mAdapter = new JobItemAdapter(NewJobListActivity.this, joborders,
                            mFilter, bimgtabSync, bimgtabOne, bimgtabTwo,
                            bimgtabThree, bimgtabFour, txttabSync, txttabOne,
                            txttabTwo, txttabThree, txttabFour, ltabOne, ltabTwo,
                            ltabThree, ltabFour,null,null);
                    mAdapter.setBranchCallback(this);
                } else {
                    mAdapter.mainSetter(NewJobListActivity.this, joborders, mFilter,
                            bimgtabSync, bimgtabOne, bimgtabTwo, bimgtabThree,
                            bimgtabFour, txttabSync, txttabOne, txttabTwo,
                            txttabThree, txttabFour, ltabOne, ltabTwo, ltabThree,
                            ltabFour);
                }


                if (jobItemList.getAdapter() == null)
                    jobItemList.setAdapter(mAdapter);
                else {
                    mAdapter.notifyDataSetChanged();
                }
                if (!dontrun) {
                    ManageTabs(2);
                    ShowOrphanFiles();
                }
            }
        }
    }

    private void updateFiler(String object) {
        if (object == null)
            CheckerApp.globalFilterVar = null;
        final View v = findViewById(R.id.layout_filter);

        TextView tx = (TextView) findViewById(R.id.txtfilter);
        tx.setText(object);
        ImageView btnCross = (ImageView) findViewById(R.id.crossbtn);
        btnCross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                v.setVisibility(RelativeLayout.GONE);
                CheckerApp.globalFilterVar = null;
                ShowDBJobs();
            }
        });
        if (object == null) {
            v.setVisibility(RelativeLayout.GONE);
        } else {
            tx.setText(object);
            v.setVisibility(RelativeLayout.VISIBLE);
        }

    }

    class saveSetThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            saveOfflineData();
            progressHandler.sendEmptyMessage(0);
        }
    }

    Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // ShowDBJobs();
                    // validateJobs();
                    Revamped_Loading_Dialog.hide_dialog();
                    break;
            }
        }
    };

//    Revamped_Loading_Dialog dialog;
//    private boolean isBranchPropErr;
//    private String alternateResult;

    public class LangTask extends AsyncTask<Void, Integer, String> {
        public LangTask() {
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.downloading_system_languages));
        }

        @Override
        protected void onPostExecute(String result) {

            if (DBHelper.getLanguages(false).size() == 0) {
                Revamped_Loading_Dialog.hide_dialog();
                Toast.makeText(
                        NewJobListActivity.this,
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

            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
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
                ShowAlert(NewJobListActivity.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
            } else if (result.equals("invalid")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                Revamped_Loading_Dialog.hide_dialog();
                return;
            }

            if (result.equals("offline")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
            } else if (result.equals("error=")) {
                ShowAlert(NewJobListActivity.this,
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
                Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
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
            if (dialog != null)
                dialog.changeMessage(dialog.getContext().getResources()
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
                app_ver = NewJobListActivity.this.getPackageManager()
                        .getPackageInfo(NewJobListActivity.this.getPackageName(),
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
                app_ver = NewJobListActivity.this.getPackageManager()
                        .getPackageInfo(NewJobListActivity.this.getPackageName(),
                                0).versionName;
            } catch (PackageManager.NameNotFoundException e) {

            }

            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            return Connector.postForm(Constants.getJobListURL(app_ver),
                    extraDataList);
        }

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
                    LoginActivity.class);
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

    private String LangListPost() {
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = NewJobListActivity.this.getPackageManager().getPackageInfo(
                    NewJobListActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        return Connector.postForm(Constants.getLangListURL(app_ver),
                extraDataList);
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

    public void downloadVideo(String DownloadUrl, String videoname) {
        // String DownloadUrl = url + "/" + videoname;
        Helper helper = new Helper();
        helper.DownloadFromUrl(DownloadUrl, videoname);
    }

    @Override
    public void onBackPressed() {
        if (dialogAttach.getVisibility() == RelativeLayout.VISIBLE) {
            dialogAttach.setVisibility(RelativeLayout.GONE);
            return;
        }
        if (Helper.syncing == true) {
            Toast.makeText(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.synch_is_already_in_progress),
                    Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(NewJobListActivity.this,
                getResources().getString(R.string.press_back_again),
                Toast.LENGTH_LONG).show();
        ExitFromJobList();
    }

    public class isAliveTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return checkConnectionPost() + "";
        }
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            isAliveTask performBackgroundTask = new isAliveTask();
                            // PerformBackgroundTask this class is the class
                            // that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 90000); // execute in every 50000
        // ms
    }

    public class QuestionnaireListTask extends AsyncTask<Void, Integer, String> {

        public QuestionnaireListTask(Revamped_Loading_Dialog dialog2) {
            dialog = dialog2;
        }

        @Override
        protected void onPreExecute() {
            if (dialog != null) {
                dialog.changeMessage(getString(R.string.questionnaire_downlaod_alert));
            } else {
                Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                        getString(R.string.questionnaire_downlaod_alert));
                dialog = Revamped_Loading_Dialog.getDialog();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // check list here
            // validateJobs();
            // ///////////////////////////////////////
            ShowDBJobs();
            Revamped_Loading_Dialog.hide_dialog();
            if (result.equals("offline")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string._alert_title),
                        getString(R.string.offline_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            }
            if (result.equals("timeout")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.timeout_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
            } else if (result.equals("error=")) {
                ShowAlert(NewJobListActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));

            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (!checkConnectionPost())
                return "offline";

            if (Connector.cookies == null) {
                if (showLogin(doLogin()))
                    return "SessionExpire";
            }
            Helper helper = new Helper();
            helper.deleteFilesInFolder();

            String result = QuestionnaireListPost();
            Log.e("RESULTTTTTTTTTT", result);
            if (result.contains("<script>")) {
                doLogin();
                result = QuestionnaireListPost();
            }
            if (result.contains("timeout"))
                return "timeout";
            if (result.contains("error="))
                return "error=";
            new Parser(dialog).parseXMLValues(result,
                    Constants.QUES_RESP_FIELD_PARAM);

            return result;
        }

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
        Connector.setContext(NewJobListActivity.this);
        return Connector.postForm(Constants.getLoginURL(), extraDataList);
    }

    private void setFontSize(View v) {
        try {
            TextView textView = (TextView) v;
            textView.setTextSize(UIHelper.getFontSize(NewJobListActivity.this,
                    textView.getTextSize()));
            if (Helper.getTheme(NewJobListActivity.this) == 0) {
                textView.setTextColor(getResources().getColor(
                        android.R.color.white));
            }
        } catch (Exception ex) {

        }

        try {
            Button btnView = (Button) v;
            btnView.setTextSize(UIHelper.getFontSize(NewJobListActivity.this,
                    btnView.getTextSize()));
            btnView.setTextColor(getResources().getColor(android.R.color.black));

        } catch (Exception ex) {

        }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(NewJobListActivity.this,
                    getResources().getString(R.string.press_back_again),
                    Toast.LENGTH_LONG).show();
            ExitFromJobList();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                    pos_shelf_item = new POS_Shelf(NewJobListActivity.this);
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

    public class DoLoginTask extends AsyncTask<Void, Integer, String> {
        Revamped_Loading_Dialog dialog;
        private String url;

        public DoLoginTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                    "Logging in...");
        }

        @Override
        protected void onPostExecute(String result) {
            Revamped_Loading_Dialog.hide_dialog();
            if (Connector.setCookieManager(NewJobListActivity.this))
                loadUrlInWebViewDialog(NewJobListActivity.this, this.url);
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

    public class uploadingOrphanFiesTask extends
            AsyncTask<Void, Integer, String> {
        Revamped_Loading_Dialog dialog;
        private Dialog errDialog;
        private boolean isForced;

        public uploadingOrphanFiesTask(Dialog dialog2, boolean isForced) {
            this.errDialog = dialog2;
            this.isForced = isForced;
        }

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this, null);
        }

        @Override
        protected void onPostExecute(String result) {
            Revamped_Loading_Dialog.hide_dialog();
            if (errDialog != null)
                errDialog.dismiss();
            else
                return;
        }

        @Override
        protected String doInBackground(Void... params) {
            checkConnectionPost();
            if (Connector.cookies == null) {
                if (showLogin(doLogin()))
                    return "SessionExpire";
            }
            if (isForced)
                sendForceImages(Revamped_Loading_Dialog.getDialog());
            else
                sendOrphanImages(Revamped_Loading_Dialog.getDialog());
            return "";
        }

    }

    private void sendForceImages(Revamped_Loading_Dialog dialog) {

        ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
        uploadList = getFolderFiles(uploadList, dialog);

    }

    private ArrayList<filePathDataID> getFolderFiles(
            ArrayList<filePathDataID> uploadList,
            Revamped_Loading_Dialog dialog2) {
        ;
        filePathDataID file = new filePathDataID();
        String path = CheckerApp.localFilesDir//Environment.getExternalStorageDirectory()
                + "/checkerimgss";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            if (files[i].getName().contains("UNIX")) {
                dialog2.changeMessage("Uploading: " + files[i].getName());
                String UNIX = getStringFromName(files[i].getName(), "CAM_UNIX",
                        "O");
                String OID = getStringFromName(files[i].getName(), "O", "_D");
                String DID = getStringFromName(files[i].getName(), "D", "_T");
                if (DID.equals("LAST"))
                    DID = null;
                String res = Connector.saveFiletoServer(myPrefs.getBoolean(
                                Constants.SETTINGS_ENABLE_TIME_STAMP, false), files[i]
                                .getPath(), Constants.getAttachmentURL(), OID, DID,
                        UNIX, "0", "", "");
                if (CheckResponse(res)) {
                    File f = new File(files[i].getPath());
                    try {
                        f.delete();
                    } catch (Exception ex) {
                    }
                } else {
                    if (CheckResponseForStorageQuota(res)) {
                        ((Activity) NewJobListActivity.this)
                                .runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ShowAlert(
                                                NewJobListActivity.this,
                                                "",
                                                "ERROR uploading files, System storage quota is full please contact administrator to upload pending attachments.",
                                                "Ok");
                                    }
                                });
                        break;
                    }
                }
            }
        }
        return null;
    }

    private String getStringFromName(String name, String start_tag,
                                     String end_tag) {

        if (name.contains(start_tag) && name.contains(end_tag)) {
            int startindex = name.indexOf(start_tag) + start_tag.length();
            int endindex = name.indexOf(end_tag);
            String substring = name.substring(startindex, endindex);
            return substring;
        }
        return null;
    }

    ArrayList<filePathDataID> uiList = new ArrayList<filePathDataID>();

    private void sendOrphanImages(Revamped_Loading_Dialog dialog) {
        ArrayList<filePathDataID> uploadList = new ArrayList<filePathDataID>();
        uiList = new ArrayList<filePathDataID>();
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

        uiList.addAll(uploadList);
        for (int j = 0; j < uploadList.size(); j++) {
            try {

                String message = getResources().getString(
                        R.string.aauploading_file);
                message = message
                        .replace("##", uploadList.get(j).getFilePath());
                dialog.changeMessage(message);
                // uploadFileList.add(Uri.parse(uploadList.get(j)));
                String path = uploadList.get(j).getFilePath();
                if (uploadList.get(j).getFilePath().startsWith("content")) {
                    path = getRealPathFromURI(Uri.parse(uploadList.get(j)
                            .getFilePath()));
                } else if (uploadList.get(j).getFilePath()
                        .startsWith("file:///")) {
                    path = path.replace("file:///", "/");
                }

                String did = uploadList.get(j).getDataID();
                String newDataId = null;
                if (did != null && did.contains("#@")) {
                    String[] dids = did.split("#@");
                    newDataId = convertDataIdForNameValuePair(dids[0]);
                    did = getMePrefix(dids[1]);
                    did = did + "obj" + newDataId + "]";
                }
                myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

                String res = Connector.saveFiletoServer(myPrefs.getBoolean(
                                Constants.SETTINGS_ENABLE_TIME_STAMP, false), path,
                        Constants.getAttachmentURL(), uploadList.get(j)
                                .getUPLOAD_FILe_ORDERID(), did,
                        uploadList.get(j).getUPLOAD_FILe_DATE(), uploadList
                                .get(j).getUPLOAD_FILe_Sample_size(), uploadList.get(j).getUPLOAD_FILe_PRODUCTID(), uploadList.get(j).getUPLOAD_FILe_LOCATIONID());
                if (CheckResponse(res)) {

                    if (path.contains("CAM_O"
                            + uploadList.get(j).getUPLOAD_FILe_ORDERID())) {
                        String newPAth = path.replace(
                                "CAM_O"
                                        + uploadList.get(j)
                                        .getUPLOAD_FILe_ORDERID(),
                                "CAM_UNIX"
                                        + uploadList.get(j)
                                        .getUPLOAD_FILe_DATE()
                                        + "O"
                                        + uploadList.get(j)
                                        .getUPLOAD_FILe_ORDERID());
                        File f = new File(newPAth);
                        try {
                            f.delete();
                        } catch (Exception ex) {

                        }
                    }
                    // Toast.makeText(NewJobListActivity.this, "DELETING FILE",
                    // Toast.LENGTH_SHORT).show();
                    dialog.changeMessage(message + " Successful!");
                    try {
                        String where = Constants.UPLOAD_FILe_MEDIAFILE + "="
                                + "\"" + uploadList.get(j).getFilePath() + "\"";
                        DBAdapter.openDataBase();
                        DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE, where,
                                null);
                        DBAdapter.closeDataBase();

                        DBHelper.deleteFile(uploadList.get(j)
                                .getUPLOAD_FILe_ORDERID(), uploadList.get(j)
                                .getFilePath());
                        if (path.contains(Constants.UPLOAD_PATH)) {
                            File file = new File(path);
                            file.delete();
                        }
                        uiList.remove(uploadList.get(j));
                    } catch (Exception ex) {
                        String str = "";
                        str += "";
                    }

                } else {
                    if (CheckResponseForStorageQuota(res)) {
                        ((Activity) NewJobListActivity.this)
                                .runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ShowAlert(
                                                NewJobListActivity.this,
                                                "",
                                                "ERROR uploading files, System storage quota is full please contact administrator to upload pending attachments.",
                                                "Ok");
                                    }
                                });
                        break;
                    }
                    dialog.changeMessage(message + " Not Successful!");
                }
            } catch (Exception ex) {
                int i = 0;
                i++;
            }

        }

        ((Activity) NewJobListActivity.this).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                showOrphanImages(uiList, NewJobListActivity.this);
            }
        });

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

    private void showOrphanImages(ArrayList<filePathDataID> uploadList,
                                  Context context) {

        if (uploadList.size() > 0)
            layoutAttachErr.setVisibility(RelativeLayout.VISIBLE);
        else {
            // Toast.makeText(NewJobListActivity.this, "NO FILES LEFT!",
            // Toast.LENGTH_LONG).show();
            layoutAttachErr.setVisibility(RelativeLayout.GONE);
        }

        // layoutAttachErr.setVisibility(RelativeLayout.VISIBLE);

        // final Dialog dialog = new Dialog(context);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setOnDismissListener(new OnDismissListener() {
        //
        // @Override
        // public void onDismiss(DialogInterface arg0) {
        //
        // }
        // });
        //
        // // if (Helper.getTheme(context) == 0) {
        // // dialog.setContentView(R.layout.pos_shelf_study_night);
        // // } else {
        // //
        // // }
        // dialog.setContentView(R.layout.attach_dialog);
        // dialog.setTitle(("Left Behind files"));

        ListView thisList = (ListView) findViewById(R.id.list_preview);
        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // if (!IsInternetConnectted()) {
                // ShowAlert(NewJobListActivity.this,
                // getString(R.string._alert_title),
                // getString(R.string.offline_alert),
                // getString(R.string.alert_btn_lbl_ok));
                // return;
                // }
                uploadingOrphanFiesTask uploadFileTask = new uploadingOrphanFiesTask(
                        dialog, false);
                uploadFileTask.execute();
            }
        });

        Button btnBack = (Button) findViewById(R.id.btn_dismiss);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialogAttach.getVisibility() == RelativeLayout.VISIBLE) {
                    dialogAttach.setVisibility(RelativeLayout.GONE);
                    return;
                }
            }
        });

        ArrayAdapter<filePathDataID> adapter = new orphansPreviewAdapter(
                context, uploadList, false, layoutAttachErr, dialogAttach);

        thisList.setAdapter(adapter);
        layoutAttachErr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                if (uploadList.size() > 0) {
                    layoutAttachErr.setVisibility(RelativeLayout.VISIBLE);
                    showOrphanImages(uploadList, NewJobListActivity.this);
                    dialogAttach.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    layoutAttachErr.setVisibility(RelativeLayout.GONE);
                }
            }
        });
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
            progressBars = customProgressAlert(NewJobListActivity.this);
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
            sendMessage(STOP_UPLOAD, null);
            ShowDBJobs();
            if (isOnlyCertificate) {
                if (NewJobListActivity.this.cert != null) {
                    ArrayList<Cert> shortList = new ArrayList<Cert>();
                    Cert c = new Cert();
                    c.setCertID(NewJobListActivity.this.cert);
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
                        QuestionnaireActivity.deleteJobRecords(NewJobListActivity.this.certOrdeId);
                        Toast.makeText(getApplicationContext(),
                                        "CheckerTificate not passed", Toast.LENGTH_LONG)
                                .show();
                    }

                    NewJobListActivity.this.cert = null;
                    NewJobListActivity.this.certOrdeId = null;
                }

            } else if (archivedSQ != null && textFilePath != null) {
                if (result != null) {
                    backToArchiveAlert(NewJobListActivity.this, "Server Response: " + result);
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        set = null;
                    }
                }
                if (set != null) {

                    pos_shelf_item = new POS_Shelf(NewJobListActivity.this);
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
                    // TODO Auto-generated catch block
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

                    if (res.contains("<script>")) {
                        doLogin();
                        res = Connector.saveFiletoServer(
                                (forceSmping != null && forceSmping.equals("1")),
                                path, Constants.getAttachmentURL(),
                                sq.getOrderid(), did, sq.getUnix(),
                                uploadList.get(j).getUPLOAD_FILe_Sample_size(), uploadList.get(j).getUPLOAD_FILe_PRODUCTID(), uploadList.get(j).getUPLOAD_FILe_LOCATIONID());
                    }
                    SplashScreen.addLog(new BasicLog(Constants.getAttachmentURL(),
                            myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                            myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
                            "Attachments Uploading:NOTSUCCESS" + path + " ORDER" + sq.getOrderid() + " Total attachments" + imgProgressTotal + "RESPONSE=" + res, sq.getOrderid()));

                    if (CheckResponseForStorageQuota(res)) {
                        ((Activity) NewJobListActivity.this)
                                .runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        ShowAlert(
                                                NewJobListActivity.this,
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
                            // TODO Auto-generated catch block
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

    public static void main(String[] args) {

    }

    public class AlternateOrdersTask extends
            AsyncTask<String, Integer, ArrayList<AlternateJob>> {
        String OrderId;
        String status;
        private String date;
        private boolean isdate;

        public AlternateOrdersTask(String orderid, String status) {
            this.OrderId = orderid;
            this.status = status;
            this.isdate = false;
        }

        public AlternateOrdersTask(String orderid, String date, boolean isDate) {
            this.OrderId = orderid;
            this.date = date;
            this.isdate = isDate;
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(
                    NewJobListActivity.this,
                    getResources().getString(
                            R.string.downloading_alternative_jobs));
        }

        @Override
        protected void onPostExecute(ArrayList<AlternateJob> result) {
            Revamped_Loading_Dialog.hide_dialog();
            showalternateJobs(
                    NewJobListActivity.this,
                    result,
                    this.OrderId,
                    (this.status != null && this.status.toLowerCase().equals(
                            "scheduled")));
        }

        @Override
        protected ArrayList<AlternateJob> doInBackground(String... params) {
            checkConnectionPost();
            return getAlternateJobs(params[0]);
        }
    }

    public class AssignAlternateTask extends AsyncTask<String, Integer, String> {
        private Dialog dialog;

        public AssignAlternateTask(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                    getResources().getString(R.string.assigning_alternate_job));
        }

        @Override
        protected void onPostExecute(String result) {
            Revamped_Loading_Dialog.hide_dialog();
            if (result != null && result.toLowerCase().contains("<status>1")) {
                this.dialog.dismiss();
                Toast.makeText(
                        NewJobListActivity.this,
                        getResources().getString(
                                R.string.order_assigned_refreshing_job_list),
                        100).show();
                startDownloadingJobs(false, true);
            } else {
                Toast.makeText(
                                NewJobListActivity.this,
                                getResources().getString(
                                        R.string.unable_to_assign_this_order), 100)
                        .show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            checkConnectionPost();
            String res = assignAlternateJobs(params[0], params[1]);
            if (res != null && res.toLowerCase().contains("<status>1")) {
                this.dialog.dismiss();
                if (params.length == 3) {
                    String result = AcceptJob(params[1]);
                }
            } else {
                Toast.makeText(
                                NewJobListActivity.this,
                                getResources().getString(
                                        R.string.unable_to_assign_this_order), 100)
                        .show();
            }

            return res;

        }

        private String AcceptJob(String orderid) {
            List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_VALUE_JOB_DETAIL_ACCEPT,
                    Constants.POST_VALUE_JOB_DETAIL_PARAM_VALUE));
            // if (order == null)
            // setOrder();
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_JOB_DETAIL_ORDER_ID, orderid));
            extraDataList.add(Helper.getNameValuePair(
                    Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER, "1"));
            String result = Connector.postForm(Constants.getJobStartURL(),
                    extraDataList);
            return result;
        }
    }

    public String assignAlternateDateJobs(String OldOrderID, String NewOrderID) {
        String resultLang = assignNewJob(OldOrderID, NewOrderID);
        if (resultLang.contains("<script>") || resultLang.contains("error")) {
            doLogin();
            resultLang = assignNewJob(OldOrderID, NewOrderID);
        }

        return resultLang;
    }

    public String assignAlternateJobs(String OldOrderID, String NewOrderID) {
        String resultLang = assignNewJob(OldOrderID, NewOrderID);
        if (resultLang.contains("<script>") || resultLang.contains("error")) {
            doLogin();
            resultLang = assignNewJob(OldOrderID, NewOrderID);
        }
        return resultLang;
    }

    public ArrayList<AlternateJob> getAlternateJobs(String orderid) {
        String resultLang = getAlternateJobsFromAPI(orderid);
        if (resultLang.contains("<script>") || resultLang.contains("error")) {
            doLogin();
            resultLang = getAlternateJobsFromAPI(orderid);
        }

        return parseAlternateJobs(resultLang);
    }

    private ArrayList<AlternateJob> parseAlternateJobs(String resultLang) {
        this.alternateResult = resultLang;
        if (resultLang != null
                && resultLang.toLowerCase().contains("<status>1")) {
            ArrayList<AlternateJob> theseJobs = new ArrayList<AlternateJob>();
            int start = resultLang
                    .indexOf("<alternative_orders type=\"array\">\r\n") + 36;
            int end = resultLang.lastIndexOf("\r\n</alternative_orders") - 1;
            String data = resultLang.substring(start, end);
            String[] alljobs = data.split("\r\n");
            for (int i = 0; i < alljobs.length; i++) {
                AlternateJob thisJob = new AlternateJob();
                start = alljobs[i].indexOf(">") + 1;
                end = alljobs[i].lastIndexOf("<");
                String text = alljobs[i].substring(start, end);
                thisJob.setText(text);

                start = alljobs[i].indexOf("<") + 1;
                end = alljobs[i].indexOf(">");
                String orderid = alljobs[i].substring(start, end);
                thisJob.setOrderID(orderid);
                theseJobs.add(thisJob);
            }
            data += "";
            return theseJobs;

        } else
            return null;
    }

    private String getAlternateJobsFromAPI(String orderid) {
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = NewJobListActivity.this.getPackageManager().getPackageInfo(
                    NewJobListActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        return Connector.postForm(
                Constants.getAlternateJobsURL(app_ver, "0", orderid),
                extraDataList);
    }

    private String assignNewJob(String OldOrderid, String NewOrderid) {
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = NewJobListActivity.this.getPackageManager().getPackageInfo(
                    NewJobListActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }

        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        return Connector.postForm(Constants.assignAlternateJobsURL(app_ver,
                "1", OldOrderid, NewOrderid), extraDataList);
    }

    private String assignNewDate(String OldOrderid, String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date dateTemp = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
                    "yyyy-MM-dd");
            // date = simpleDateFormat1.format(dateTemp);
            // date =
            // simpleDateFormat1.parse(date2);

        } catch (ParseException ex) {
        }
        QuestionnaireActivity.langid = null;
        // Initialize the login data to POST
        String app_ver = Constants.POST_VALUE_QUES_APP_ACTUAL_VERSION;
        try {
            app_ver = NewJobListActivity.this.getPackageManager().getPackageInfo(
                    NewJobListActivity.this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
        extraDataList.add(Helper.getNameValuePair("OrderID", OldOrderid));
        extraDataList.add(Helper.getNameValuePair("change_date", "1"));
        extraDataList.add(Helper.getNameValuePair("selected_date", date));
        return Connector.postForm(Constants.assignAlternateDateJobsURL(app_ver,
                "1", OldOrderid, date), extraDataList);

    }

    @Override
    public void callback(String orderid, String status) {
        AlternateOrdersTask altTask = new AlternateOrdersTask(orderid, status);
        altTask.execute(orderid);

    }

    public void showalternateJobs(Context context,
                                  final ArrayList<AlternateJob> result, final String oldorderid,
                                  final boolean isScheuled) {
        final Dialog dialog = new Dialog(NewJobListActivity.this);
        dialog.setContentView(R.layout.dialog_alternate_branch);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textView1);
        if (result == null) {
            text.setText(getResources().getString(
                    R.string.unable_to_find_alternative_job));
            // text.setText(this.alternateResult);
        }

        ListView listJobs = (ListView) dialog.findViewById(R.id.lvjobs);
        listJobs.setAdapter(new AlternateJobsAdapter(NewJobListActivity.this,
                result));

        listJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NewJobListActivity.this);
                builder.setMessage(
                                getResources().getString(
                                        R.string.do_you_Want_relace_order))
                        .setTitle(
                                getResources().getString(R.string._alert_title))
                        .setCancelable(false)
                        .setPositiveButton(
                                getResources()
                                        .getString(
                                                R.string.questionnaire_exit_delete_alert_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogg, int id) {
                                        AssignAlternateTask atask = new AssignAlternateTask(
                                                dialog);
                                        if (isScheuled)
                                            atask.execute(oldorderid,
                                                    result.get(position)
                                                            .getOrderID(), "");
                                        else

                                            atask.execute(oldorderid, result
                                                    .get(position).getOrderID());
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

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                } catch (Exception ex) {
                    Toast.makeText(NewJobListActivity.this,
                            ex.getLocalizedMessage(), 100).show();
                }

            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        sendMessage(START_ACTIVITY, "hiii");
    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }

    public void sendBalloonData(final int assigned_tab,
                                final int scheduled_tab, final int in_progress_tab,
                                final int completed_tab, final ArrayList<orderListItem> joblistarray) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                        .getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {

                    BalloonData bdata = new BalloonData();
                    bdata.setAssigned(assigned_tab);
                    bdata.setScheduled(scheduled_tab);
                    bdata.setIn_progress(in_progress_tab);
                    bdata.setCompleted(completed_tab);
                    bdata.setJoblistarray(joblistarray);

                    byte[] baloonbytes = SerializationUtils
                            .serialize((BalloonData) bdata);
                    MessageApi.SendMessageResult result = Wearable.MessageApi
                            .sendMessage(mApiClient, node.getId(),
                                    "BALLOON_DATA", baloonbytes).await();

                    if (result.getStatus().isSuccess()) {
                        int i = 0;
                        i++;
                    } else {
                        int i = 0;
                        i++;
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // mEditText.setText( "" );
                    }
                });
            }
        }).start();
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

    public void switch_tab(String dataa) {
        if (dataa.contains("ASSIGNED")) {
            tabOne.setChecked(true);
            tabOneb.setChecked(true);
            ManageTabs(1);
        }
        if (dataa.contains("SCHEDULED")) {
            tabTwo.setChecked(true);
            tabTwob.setChecked(true);
            ManageTabs(2);
        }
        if (dataa.contains("IN PROGRESS")) {
            tabThree.setChecked(true);
            tabThreeb.setChecked(true);
            ManageTabs(3);
        }
        if (dataa.contains("COMPLETED")) {
            tabFour.setChecked(true);
            tabFourb.setChecked(true);
            ManageTabs(4);
        }

    }

    public void callJobDetail(Intent intent, int jOB_DETAIL_ACTIVITY_CODE2) {
        startActivityForResult(intent, JOB_DETAIL_ACTIVITY_CODE);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        // ArrayList<GestureStroke> strokeList = gesture.getStrokes();
        // // prediction = lib.recognize(gesture);
        // float f[] = strokeList.get(0).points;
        // String str = "";
        //
        // if (f[0] < f[f.length - 2]) {
        // str = "Right gesture";
        // menuView.setVisibility(RelativeLayout.VISIBLE);
        // isMenuOpen = true;
        // } else if (f[0] > f[f.length - 2]) {
        // menuView.setVisibility(RelativeLayout.GONE);
        // isMenuOpen = false;
        // str = "Left gesture";
        // } else {
        // str = "no direction";
        // }
        // Toast.makeText(getApplicationContext(), str,
        // Toast.LENGTH_LONG).show();

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
                    Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
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
                                NewJobListActivity.this);
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
                                NewJobListActivity.this,
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
                    // NewJobListActivity.this,"fresh job",
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
                    app_ver = NewJobListActivity.this.getPackageManager()
                            .getPackageInfo(
                                    NewJobListActivity.this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {

                }
                return Connector.postForm(
                        Constants.getInProgressJobsURL(app_ver), extraDataList);
            }
        }
        RefundTask refundtaskobj = new RefundTask(dialogg);
        refundtaskobj.execute();
    }

    public String getOrderStartTime(String orderID) {
        String time = DBHelper.getOrderStartTime(Constants.DB_TABLE_ORDERS,
                new String[]{Constants.DB_TABLE_ORDERS_START_TIME,},
                Constants.DB_TABLE_ORDERS_ORDERID + "=" + "\"" + orderID
                        + "\" and " + Constants.DB_TABLE_ORDERS_STATUS
                        + " = \"In progress\"");
        return "2017-10-11  06:25:25";// "yyyy-MM-dd  kk:mm:ss"
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

    @Override
    public void datecallback(String orderid, String date) {
        JobBoardActivityFragment br = new JobBoardActivityFragment();
        Dialog dialog = openDialog(orderid);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.altdates);
        final Button btnApply = (Button) dialog.findViewById(R.id.btnApply);
        br.LoadALTData(orderid, spinner, NewJobListActivity.this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position > 0) {
                    ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
                    String selectedDate = (String) adapter.getItem(position);
                    btnApply.setTag((String) selectedDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Dialog openDialog(final String orderid) {
        final Dialog dialog = new Dialog(NewJobListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alt_date_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(lp.width, lp.height);
        RelativeLayout topbar;
        topbar = (RelativeLayout) dialog.findViewById(R.id.topbar);
        final Spinner spinner = (Spinner) findViewById(R.id.altdates);
        dialog.findViewById(R.id.xbutton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception ex) {

                        }

                    }
                });
        dialog.findViewById(R.id.btnApply).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag() == null)
                            Toast.makeText(
                                    NewJobListActivity.this,
                                    getResources().getString(
                                            R.string.selectDate),
                                    Toast.LENGTH_LONG).show();
                        applyAlternateDate(orderid, dialog,
                                ((String) v.getTag()));
                    }
                });
        dialog.show();
        return dialog;
    }

    protected void applyAlternateDate(String orderid, Dialog dialog2,
                                      String date) {
        AssignAlternateDateTask dt = new AssignAlternateDateTask(dialog2);
        dt.execute(orderid, date);
    }

    public class AssignAlternateDateTask extends
            AsyncTask<String, Integer, String> {
        private Dialog dialog;

        public AssignAlternateDateTask(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(NewJobListActivity.this,
                    getResources().getString(R.string.assigning_alternate_job));

        }

        @Override
        protected void onPostExecute(String result) {
            Revamped_Loading_Dialog.hide_dialog();
            if (result == null) {
                this.dialog.dismiss();
                Toast.makeText(
                        NewJobListActivity.this,
                        getResources().getString(
                                R.string.order_assigned_refreshing_job_list),
                        100).show();
                startDownloadingJobs(false, true);
            } else {
                Toast.makeText(NewJobListActivity.this, result, 100).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            checkConnectionPost();
            String res = assignNewDate(params[0], params[1]);
            if (res != null && res.toLowerCase().contains("<status>0")) {
                return null;
            } else {
                if (res != null && res.toLowerCase().contains("<reply>")
                        && res.toLowerCase().contains("</reply>")) {
                    String reply = res.substring(res.indexOf("<reply>") + 7,
                            res.indexOf("</reply>"));
                    try {
                        reply = URLDecoder.decode(reply, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return reply;
                }
                return "Error assigning date.";
            }
        }
    }

    private int getIcon(String iconName) {
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

    private void loadViews() {
        Context cntxt = getApplicationContext();
        Helper.changeViewColor(sidemenuicon);
        Helper.changeImageViewSrc(imgtabSync, cntxt);
        Helper.changeImageViewSrc(imgtabOne, cntxt);
        Helper.changeImageViewSrc(imgtabTwo, cntxt);
        Helper.changeImageViewSrc(imgtabThree, cntxt);
        Helper.changeImageViewSrc(imgtabFour, cntxt);
        Helper.changeViewColor(Side_menu_top_green);
        color_select = Helper.getappColor();
        txt_color_select = Helper.getappColor();
        txt_color_unselect = Helper.getunselectedappColor();
    }


}
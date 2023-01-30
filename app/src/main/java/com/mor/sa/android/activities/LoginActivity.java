package com.mor.sa.android.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

public class LoginActivity extends Activity implements OnClickListener {

    EditText username_et, password_et;
    Button signup;
    Button login_btn;
    ImageView reset_btn;
    ImageView setting_btn, appico;
    boolean islogin = true;
    SharedPreferences myPrefs;
    TextView tv, settinglbl;
    private Spinner spinner_bugs;
    private boolean makeHttps = false;
    private TextView forgot;
    // protected List<ParseObject> allBugParseList;
    public static ArrayList<QuestionnaireData> thisSavedAnswer = null;
    public static Order thisOrder = null;
    public static Set thisSet = null;
    public static String dataid = null;
    private String newUrlForEUClients;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        Constants.setLocale(LoginActivity.this);


    }

    private void setInvertDisplay() {
        if (Helper.getTheme(LoginActivity.this) == 0) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.loginlayout);
            if (layout != null)
                layout.setBackgroundColor(Color.parseColor("#ffffff"));

            ImageView imglayout = (ImageView) findViewById(R.id.btmBar);
            if (imglayout != null)
                imglayout.setBackgroundColor(Color.parseColor("#ffffff"));

            layout = (RelativeLayout) findViewById(R.id.screenBackGround);
            if (layout != null)
                layout.setBackgroundColor(Color.parseColor("#ffffff"));

            TextView tv = (TextView) findViewById(R.id.loginHeader);
            if (tv != null)
                tv.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setLocale(LoginActivity.this);
        LoginActivity.thisOrder = null;
        LoginActivity.thisSet = null;
        LoginActivity.thisSavedAnswer = null;
        myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        JobListActivity.setAlternateURL(null, myPrefs);
        Constants.setFullBranchName(myPrefs.getBoolean(
                Constants.SETTINGS_ENABLE_BRANCH_FULL_NAME, false));
        Constants.setDateFilter(myPrefs.getBoolean(
                Constants.SETTINGS_ENABLE_DATE_FILTER, false));
        Constants.setProxyHost(myPrefs.getString(
                Constants.SETTINGS_PROXY_HOST_KEY, ""));
        Constants.setProxyPort(myPrefs.getInt(
                Constants.SETTINGS_PROXY_PORT_KEY, -1));
        Constants.setProxyUsername(myPrefs.getString(
                Constants.SETTINGS_PROXY_HOST_USERNAME, ""));
        Constants.setProxyPassword(myPrefs.getString(
                Constants.SETTINGS_PROXY_HOST_PASSWORD, ""));
        Bundle b = getIntent().getExtras();

        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(Constants.Crash_Last_SETID, "");
        prefsEditor.putString(Constants.Crash_Last_ORDERID, "");
        prefsEditor.commit();

        // Toast.makeText(LoginActivity.this, "Login screen OPENED",
        // Toast.LENGTH_LONG).show();
        if (b == null)
            workInOfflineMode();
        if ((myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""))
                .equals("")) {
            Intent intent = new Intent(this.getApplicationContext(),
                    NewSettingsActivity.class);
            startActivity(intent);
            finish();
        }
        Locale locale = new Locale(
                Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
                        Constants.SETTINGS_LANGUAGE_INDEX, 0)]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.login_new);

        settinglbl = (TextView) findViewById(R.id.settinglbl);
        appico = (ImageView) findViewById(R.id.appico);

        loadData();
        setInvertDisplay();
        spinner_bugs = (Spinner) findViewById(R.id.spinner_bugs);
        if (spinner_bugs != null && Constants.isQAAllowed) {
            loadBugReports(spinner_bugs);
        }
        if (spinner_bugs != null && !Constants.isQAAllowed)
            spinner_bugs.setVisibility(RelativeLayout.GONE);
        else if (spinner_bugs != null)
            spinner_bugs.setVisibility(RelativeLayout.VISIBLE);
        loadViews();
    }

    private void loadBugReports(final Spinner spinner) {
        // final ArrayList<String> allBugs = new ArrayList<String>();
        //
        // ParseQuery<ParseObject> query = ParseQuery.getQuery("BugReport");
        // query.addAscendingOrder("updatedAt");
        // query.findInBackground(new FindCallback<ParseObject>() {
        // @Override
        // public void done(List<ParseObject> userList, ParseException e) {
        // if (e == null) {
        // if (userList.size() > 0) {
        // allBugParseList = userList;
        // allBugs.add("Please Select...");
        // for (int i = 0; i < userList.size(); i++) {
        // ParseObject p = userList.get(i);
        // String bugid = p.getObjectId();
        // String updated_at = p.getUpdatedAt().toString();
        // String comment = p.getString("user_comment");
        // allBugs.add("" + bugid + "-" + updated_at + " "
        // + comment);
        // }
        //
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        // LoginActivity.this,
        // R.layout.custom_spinner_row_two, allBugs);
        // spinner.setAdapter(adapter);
        // }
        //
        // } else {
        // Log.d("score", "Error: " + e.getMessage());
        // // Alert.alertOneBtn(getActivity(),"Something went wrong!");
        // }
        // }
        // });
        //
        // spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        // @Override
        // public void onItemSelected(AdapterView<?> parent, View view,
        // int position, long id) {
        // LoginActivity.dataid = null;
        // LoginActivity.thisOrder = null;
        // LoginActivity.thisSet = null;
        // LoginActivity.thisSavedAnswer = null;
        // if (allBugParseList != null && allBugParseList.size() > 0
        // && position > 0) {
        //
        // ParseObject thisParseObject = allBugParseList
        // .get(position - 1);
        // LoginActivity.dataid = thisParseObject
        // .getString("CurrentDataId");
        // ParseFile set = (ParseFile) thisParseObject.get("Set");
        // set.getDataInBackground(new GetDataCallback() {
        // public void done(byte[] data, ParseException e) {
        // if (e == null) {
        //
        // Object obj = SerializationUtils
        // .deserialize(data);
        // LoginActivity.thisSet = (Set) obj;
        // openBugReport();
        // } else {
        //
        // }
        // }
        // });
        //
        // ParseFile order = (ParseFile) thisParseObject.get("Order");
        // order.getDataInBackground(new GetDataCallback() {
        // public void done(byte[] data, ParseException e) {
        // if (e == null) {
        // Object obj = SerializationUtils
        // .deserialize(data);
        // LoginActivity.thisOrder = (Order) obj;
        // openBugReport();
        // } else {
        //
        // }
        // }
        // });
        //
        // ParseFile saved = (ParseFile) thisParseObject
        // .get("SavedAnswers");
        // saved.getDataInBackground(new GetDataCallback() {
        // public void done(byte[] data, ParseException e) {
        // if (e == null) {
        //
        // Object obj = SerializationUtils
        // .deserialize(data);
        // LoginActivity.thisSavedAnswer = (ArrayList<QuestionnaireData>) obj;
        // openBugReport();
        // } else {
        //
        // }
        // }
        // });
        // }
        // }
        //
        // @Override
        // public void onNothingSelected(AdapterView<?> parent) {
        // // TODO Auto-generated method stub
        //
        // }
        // });

    }

    private void openBugReport() {
        if (LoginActivity.thisOrder != null && LoginActivity.thisSet != null
                && LoginActivity.thisSavedAnswer != null) {
            Intent intent = new Intent(this.getApplicationContext(),
                    QuestionnaireActivity.class);
            intent.putExtra("OrderID", LoginActivity.thisOrder.getOrderID());
            startActivity(intent);
        }
    }

    private void loadData() {
        username_et = (EditText) findViewById(R.id.username);

        username_et.setTextSize(UIHelper.getFontSize(LoginActivity.this,
                username_et.getTextSize()));

        password_et = (EditText) findViewById(R.id.password);
        password_et.setTextSize(UIHelper.getFontSize(LoginActivity.this,
                password_et.getTextSize()));

        login_btn = (Button) findViewById(R.id.loginbtn);

        forgot = (TextView) findViewById(R.id.forgot);
        signup = (Button) findViewById(R.id.signup);
        signup.setVisibility(RelativeLayout.VISIBLE);
        forgot.setVisibility(RelativeLayout.VISIBLE);
//		if (Helper.isMisteroMenu != true) {
//
//			forgot.setVisibility(RelativeLayout.GONE);
//			signup.setVisibility(RelativeLayout.GONE);
//		}

        reset_btn = (ImageView) findViewById(R.id.resetbtn);

        // login_btn.setLayoutParams(setButtonParams());
        // reset_btn.setLayoutParams(setButtonParams());
        setting_btn = (ImageView) findViewById(R.id.settingbtn);
        findViewById(R.id.settinglbl).setOnClickListener(this);

        //
        login_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);

        Helper.setSystemURL(myPrefs.getString(
                Constants.SETTINGS_SYSTEM_URL_KEY, ""));
        Helper.setAlternateSystemURL(myPrefs.getString(
                Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));
        Helper.setParsed(false);
        String username = myPrefs.getString(
                Constants.POST_FIELD_LOGIN_USERNAME, "");
        username_et.setText(username);
        String pwd = myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, "");
        password_et.setText(pwd);
    }

    private void Reset() {
        username_et.setText("");
        password_et.setText("");
        username_et.setHint(getString(R.string.login_username_hint));
        // username_et.setFocusable(true);
    }

    private boolean validateCredencials(String un, String pwd) {
        if (Helper.IsEmptyString(un)) {
            ShowAlert(this, getString(R.string.error_alert_title),
                    getString(R.string.login_clientside_username_alert),
                    getString(R.string.alert_btn_lbl_ok));
            return false;
        }
        if (Helper.IsEmptyString(Helper.getSystemURL())) {
            ShowAlert(this, getString(R.string.error_alert_title),
                    getString(R.string.login_invalid_systemurl_alert),
                    getString(R.string.alert_btn_lbl_ok));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbtn:
                hideKeyBoard();
                String username = username_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
                SplashScreen.addLog(new BasicLog(Constants.getLoginURL(),
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        username,
                        "Login process initiated", "login"));

                if (!validateCredencials(username, password))
                    return;
                if (IsInternetConnectted()) {
                    new LoginTask().execute(username, password);
                } else {
                    workInOfflineMode();
                }
                break;

            case R.id.resetbtn:
                Reset();
                break;

            case R.id.settinglbl:
            case R.id.settingbtn:
                Intent intent = new Intent(this.getApplicationContext(),
                        NewSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signup:
                // Intent settingintent = new Intent(this.getApplicationContext(),
                // Signup.class);
                // startActivity(settingintent);
                // finish();


                String newUrlForEUClients =
                        Constants.CompareWithNewUrlList(getResources().getStringArray(R.array.eusystems));
                if (newUrlForEUClients != null && newUrlForEUClients.length() > 0) {
                    new SignupTask().execute(newUrlForEUClients);

                } else {

                    JobListActivity.loadUrlInWebViewDialog(LoginActivity.this,
                            Constants.getSignUpURL());
                    break;
                }
                break;

            case R.id.forgot:
                // Intent settingintent = new Intent(this.getApplicationContext(),
                // Signup.class);
                // startActivity(settingintent);
                // finish();
                newUrlForEUClients =
                        Constants.CompareWithNewUrlList(getResources().getStringArray(R.array.eusystems));
                if (newUrlForEUClients != null && newUrlForEUClients.length() > 0) {
                    new ForgetTask().execute(newUrlForEUClients);

                } else {

                    JobListActivity.loadUrlInWebViewDialog(LoginActivity.this,
                            Constants.getForgotURL());
                    break;
                }
        }
    }

    public class SignupTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(LoginActivity.this,
                    "Checking Server details...");
        }

        @Override
        protected void onPostExecute(String result) {

            Revamped_Loading_Dialog.hide_dialog();
            if (result != null)
                JobListActivity.setAlternateURL(newUrlForEUClients, myPrefs);
            JobListActivity.loadUrlInWebViewDialog(LoginActivity.this,
                    Constants.getSignUpURL());

        }

        @Override
        protected String doInBackground(String... params) {
            return checkConnectionPost(params[0]);

        }

        private String checkConnectionPost(String url) {


            if (url != null) {
                String chkurl = Constants.getcheckConnectionURL(url);
                boolean isOk = Connector.checkConnection(chkurl);
                if (isOk) {
                    newUrlForEUClients = url;
                    JobListActivity.setAlternateURL(url, myPrefs);
                    return "";
                } else return null;
            }
            return null;
        }

    }

    public class ForgetTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

            Revamped_Loading_Dialog.show_dialog(LoginActivity.this,
                    "Checking Server details...");
        }

        @Override
        protected void onPostExecute(String result) {

            Revamped_Loading_Dialog.hide_dialog();
            if (result != null)
                JobListActivity.setAlternateURL(newUrlForEUClients, myPrefs);
            JobListActivity.loadUrlInWebViewDialog(LoginActivity.this,
                    Constants.getForgotURL());

        }

        @Override
        protected String doInBackground(String... params) {
            return checkConnectionPost(params[0]);

        }

        private String checkConnectionPost(String url) {

            if (url != null) {
                String chkurl = Constants.getcheckConnectionURL(url);
                boolean isOk = Connector.checkConnection(chkurl);
                if (isOk) {
                    newUrlForEUClients = url;
                    JobListActivity.setAlternateURL(url, myPrefs);
                    return "";
                } else return null;
            }
            return null;
        }

    }

    public void loadUrlInWebViewDialog(Context context, String url) {

        final Dialog err_dialog = new Dialog(context);
        err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        err_dialog.setContentView(R.layout.dialog_briefing);
        err_dialog.findViewById(R.id.textView1).setVisibility(
                RelativeLayout.GONE);

        WebView wv = (WebView) err_dialog.findViewById(R.id.briefingView);
        wv.clearCache(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wv.loadUrl(url);

        Button btnClose = (Button) err_dialog.findViewById(R.id.btnClose);
        Helper.changeViewColor(btnClose);
        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                err_dialog.dismiss();
            }
        });
        err_dialog.show();
    }

    private void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void workInOfflineMode() {
        if (myPrefs != null
                && myPrefs.getBoolean(Constants.ALREADY_LOGIN_STATUS, false)) {
            Intent intent = new Intent(this.getApplicationContext(),
                    JobListActivity.class);
            // Toast.makeText(LoginActivity.this, "opening joblist screen ",
            // Toast.LENGTH_LONG).show();

            if (myPrefs.getBoolean(Constants.SETTINGS_switchtracking, true)) {
                SplashScreen.addServiceLog(new BasicLog(
                        myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                        myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "Starting service!", ""));

                // ///////////////////////////////////////////////////////////

//				Intent i = new Intent(LoginActivity.this, comService.class);
//
//				i.putExtra("KEY1", "Value to be used by the service");
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//					startForegroundService(i);
//				else
//					startService(i);
                // ///////////////////////////////////////////////////////////
            } else {
                // switchtracking.setChecked(false);
            }
            startActivity(intent);
            finish();
        } else if (!IsInternetConnectted()) {
            ShowAlert(LoginActivity.this,
                    getString(R.string.error_alert_title),
                    getString(R.string.no_internet_connection_alret_message),
                    getString(R.string.alert_btn_lbl_ok));
            return;
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

    private class LoginTask extends AsyncTask<String, Void, String> {
        private String _username;
        private String _password;

        @Override
        protected void onPreExecute() {
            Revamped_Loading_Dialog.show_dialog(LoginActivity.this, null);
        }

        @Override
        protected String doInBackground(String... params) {
            // _username = "sec17";
            // _password = "sec17";
            _username = params[0];
            _password = params[1];
            String msg = loginPost(_username, _password,
                    Constants.POST_VALUE_LOGIN_DO_LOGIN);
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
            SplashScreen.addLog(new BasicLog(Constants.getLoginURL(),
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    _username,
                    "Login attempt: response from server:" + result, "login"));


            Revamped_Loading_Dialog.hide_dialog();
            if (!Helper.IsValidResponse(result,
                    Constants.LOGIN_RESP_FIELD_PARAM_LOGIN_TAG)) {
                setLogin(false);
                ShowAlert(LoginActivity.this,
                        getString(R.string.error_alert_title),
                        getString(R.string.invalid_server_response_alert),
                        getString(R.string.alert_btn_lbl_ok));
                return;
            }

            String result1 = new Parser().getValue(result,
                    Constants.LOGIN_RESP_FIELD_PARAM);
            // problem
            if (!Helper.AuthenticateLoginResponse(result1)) {
                setLogin(false);
                try {
                    result = result.substring(result.indexOf("<problem>"),
                            result.indexOf("</problem>"));
                    result = result.substring(result.indexOf("<problem>") + 9,
                            result.length());
                    ShowAlert(LoginActivity.this,
                            getString(R.string.error_alert_title), result,
                            getString(R.string.alert_btn_lbl_ok));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setLogin(true);

            saveOfflineData();

            // ACRA.getErrorReporter().putCustomData("USER Id",
            // _username);
            Intent intent = new Intent(
                    LoginActivity.this.getApplicationContext(),
                    JobListActivity.class);
            intent.putExtra(Constants.IS_LOGIN, true);
            startActivity(intent);

            if (myPrefs.getBoolean(Constants.SETTINGS_switchtracking, false)
                    && !isMyServiceRunning(comService.class)) {
//				Intent i = new Intent(LoginActivity.this, comService.class);
//				i.putExtra("KEY1", "Value to be used by the service");
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//					startForegroundService(i);
//				else
//					startService(i);
            } else if (!myPrefs.getBoolean(Constants.SETTINGS_switchtracking,
                    false) && isMyServiceRunning(comService.class)) {
//				Intent i = new Intent(LoginActivity.this, comService.class);
//				i.putExtra("KEY1", "Value to be used by the service");
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//					startForegroundService(i);
//				else
//					startService(i);
            }
            finish();
        }

        private boolean isMyServiceRunning(Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (RunningServiceInfo service : manager
                    .getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(
                        service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        private void setLogin(boolean islogin) {
            SharedPreferences.Editor prefsEditor = myPrefs.edit();

            prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, islogin);
            // prefsEditor.commit();
            // SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putString(Constants.POST_FIELD_LOGIN_USERNAME,
                    _username);
            prefsEditor.putString(Constants.POST_FIELD_LOGIN_PASSWORD,
                    _password);
            if (LoginActivity.this.makeHttps && islogin)
                prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY, Helper
                        .getSystemURL().replace("http://", "https://"));
            prefsEditor.commit();

        }
    }

    public void ShowAlert(Context context, String title, String message,
                          String button_lbl) {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(title);
            TextView textView = new TextView(context);
            textView.setTextSize(UIHelper.getFontSize(LoginActivity.this,
                    textView.getTextSize()));
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
        } catch (Exception e) {

        }
    }

    // ********start****//

    private TableRow.LayoutParams setButtonParams() {
        TableRow.LayoutParams tr = new TableRow.LayoutParams(
                Helper.getViewWidth(this.getApplicationContext()),
                LayoutParams.WRAP_CONTENT);
        return tr;
    }

    // ********end****//

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
        String loginurl = Constants.getLoginURL();
        this.makeHttps = false;
        newUrlForEUClients =
                Constants.CompareWithNewUrlList(getResources().getStringArray(R.array.eusystems));
        JobListActivity.setAlternateURL(null, myPrefs);
        if (newUrlForEUClients != null) {
            String chkurl = Constants.getcheckConnectionURL(newUrlForEUClients);
            boolean isOk = Connector.checkConnection(chkurl);
            if (isOk) {
                JobListActivity.setAlternateURL(newUrlForEUClients, myPrefs);
                loginurl = Constants.getLoginURL();
            }

        }
        if (loginurl != null && loginurl.toLowerCase().contains("http://")) {
            String newloginurl = loginurl.replace("http://", "https://");
            String response = Connector.postForm(newloginurl, extraDataList);

            if (Helper.IsValidResponse(response,
                    Constants.LOGIN_RESP_FIELD_PARAM_LOGIN_TAG)) {
                this.makeHttps = true;
                return response;
            }
        }

        return Connector.postForm(loginurl, extraDataList);
    }

    private void saveOfflineData() {

        DBAdapter dba = new DBAdapter(this.getApplicationContext());
        // db.deleteDB();
        try {

            dba.createDataBase(username_et.getText().toString().trim(),
                    Helper.getSystemURL(), null);

            String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
                    + "\"Assigned\"" + " OR StatusName=" + "\"survey\"";
            DBHelper.deleteJoblistRecords(where);
            DBHelper.deleteRecords(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadViews() {
        Helper.changeBtnColor(login_btn);
        Helper.changeBtnColor(signup);

        if (getPackageName() != null
                && getPackageName().contains(Helper.CONSTPACKAGEPREFIX)) {
            if (Helper.showSignup == false)
                signup.setVisibility(RelativeLayout.GONE);
            else
                signup.setVisibility(RelativeLayout.VISIBLE);
        }
        Helper.changeImageViewSrc(appico, getApplicationContext());
        Helper.changeImageViewSrc(setting_btn, getApplicationContext());
        Helper.changeTxtViewColor(settinglbl);
        Helper.changeTxtViewBG(username_et);
        Helper.changeTxtViewBG(password_et);
    }

}

package com.mor.sa.android.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.QuestionnaireData;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Progress_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

public class RefundFlowActivity extends Activity implements OnClickListener {

	EditText username_et, password_et;
	Button login_btn;
	ImageView reset_btn;
	ImageView setting_btn;
	boolean islogin = true;
	SharedPreferences myPrefs;
	TextView tv;
	private Spinner spinner_bugs;
public static ArrayList<QuestionnaireData> thisSavedAnswer = null;
	public static Order thisOrder = null;
	public static Set thisSet = null;
	public static String dataid = null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

		int language = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);
		Locale locale = new Locale(
				Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
						Constants.SETTINGS_LANGUAGE_INDEX, 0)]);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

	}

	private void setInvertDisplay() {
		if (Helper.getTheme(RefundFlowActivity.this) == 0) {
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
		RefundFlowActivity.thisOrder = null;
		RefundFlowActivity.thisSet = null;
		RefundFlowActivity.thisSavedAnswer = null;
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
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

		loadData();
		setInvertDisplay();
		spinner_bugs = (Spinner) findViewById(R.id.spinner_bugs);
		
		if (spinner_bugs != null && !Constants.isQAAllowed)
			spinner_bugs.setVisibility(RelativeLayout.GONE);
		else if (spinner_bugs != null)
			spinner_bugs.setVisibility(RelativeLayout.VISIBLE);
	}


	private void openBugReport() {
		if (RefundFlowActivity.thisOrder != null && RefundFlowActivity.thisSet != null
				&& RefundFlowActivity.thisSavedAnswer != null) {
			Intent intent = new Intent(this.getApplicationContext(),
					QuestionnaireActivity.class);
			intent.putExtra("OrderID", RefundFlowActivity.thisOrder.getOrderID());
			startActivity(intent);
		}
	}

	private void loadData() {
		username_et = (EditText) findViewById(R.id.username);

		username_et.setTextSize(UIHelper.getFontSize(RefundFlowActivity.this,
				username_et.getTextSize()));

		password_et = (EditText) findViewById(R.id.password);
		password_et.setTextSize(UIHelper.getFontSize(RefundFlowActivity.this,
				password_et.getTextSize()));

		login_btn = (Button) findViewById(R.id.loginbtn);

		reset_btn = (ImageView) findViewById(R.id.resetbtn);

		// login_btn.setLayoutParams(setButtonParams());
		// reset_btn.setLayoutParams(setButtonParams());
		setting_btn = (ImageView) findViewById(R.id.settingbtn);
		findViewById(R.id.settinglbl).setOnClickListener(this);

		//
		login_btn.setOnClickListener(this);
		reset_btn.setOnClickListener(this);
		setting_btn.setOnClickListener(this);
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
					SettingsActivity.class);
			startActivity(intent);
			finish();
			break;
		}
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
			startActivity(intent);
			finish();
		} else if (!IsInternetConnectted()) {
			ShowAlert(RefundFlowActivity.this,
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
		Progress_Dialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new Progress_Dialog(RefundFlowActivity.this);
			dialog.onPreExecute();
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
			dialog.onPostExecute();
			if (!Helper.IsValidResponse(result,
					Constants.LOGIN_RESP_FIELD_PARAM_LOGIN_TAG)) {
				setLogin(false);
				ShowAlert(RefundFlowActivity.this,
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
					ShowAlert(RefundFlowActivity.this,
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
					RefundFlowActivity.this.getApplicationContext(),
					JobListActivity.class);
			intent.putExtra(Constants.IS_LOGIN, true);
			startActivity(intent);
			finish();
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
			prefsEditor.commit();

		}
	}

	public void ShowAlert(Context context, String title, String message,
			String button_lbl) {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(title);
			TextView textView = new TextView(context);
			textView.setTextSize(UIHelper.getFontSize(RefundFlowActivity.this,
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
		return Connector.postForm(Constants.getLoginURL(), extraDataList);
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

}

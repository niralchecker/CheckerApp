package com.mor.sa.android.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;

import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Progress_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ProxyActivity extends Activity implements OnClickListener {

	EditText proxy_host, proxy_port;
	EditText proxy_username, proxy_password;
	Button save_btn, reset_btn;
	SharedPreferences myPrefs;
	TextView tv;

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
		if (Helper.getTheme(ProxyActivity.this) == 0) {
			// RelativeLayout layout = (RelativeLayout)
			// findViewById(R.id.idBackGround);
			// if (layout != null)
			// layout.setBackgroundDrawable(getResources().getDrawable(
			// R.drawable.navigation_bar_dark));

			ImageView imglayout = (ImageView) findViewById(R.id.navBottom);
			if (imglayout != null)
				imglayout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.navigation_bar_dark));

			RelativeLayout lllayout = (RelativeLayout) findViewById(R.id.rvinjobscreen);
			lllayout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.navigation_bar_dark));

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.idBackGround);
			if (layout != null)
				layout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.background_dark));

			TextView tv = (TextView) findViewById(R.id.settingheder);
			if (tv != null)
				tv.setTextColor(getResources().getColor(android.R.color.white));

			TextView tv1 = (TextView) findViewById(R.id.s_urltext1);
			if (tv1 != null)
				tv1.setTextColor(getResources().getColor(android.R.color.white));
			TextView tv2 = (TextView) findViewById(R.id.s_urltext2);
			if (tv2 != null)
				tv2.setTextColor(getResources().getColor(android.R.color.white));
			TextView tv3 = (TextView) findViewById(R.id.s_urltext3);
			if (tv3 != null)
				tv3.setTextColor(getResources().getColor(android.R.color.white));
			TextView tv4 = (TextView) findViewById(R.id.s_urltext4);
			if (tv4 != null)
				tv4.setTextColor(getResources().getColor(android.R.color.white));

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		Bundle b = getIntent().getExtras();

		Locale locale = new Locale(
				Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
						Constants.SETTINGS_LANGUAGE_INDEX, 0)]);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
		setContentView(R.layout.proxy_settings);
		loadData();
		setInvertDisplay();
	}

	private void loadData() {
		proxy_host = (EditText) findViewById(R.id.edit_host);
		proxy_host.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				proxy_host.getTextSize()));

		proxy_port = (EditText) findViewById(R.id.edit_port);
		proxy_port.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				proxy_port.getTextSize()));

		proxy_username = (EditText) findViewById(R.id.edit_username);
		proxy_username.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				proxy_username.getTextSize()));

		proxy_password = (EditText) findViewById(R.id.edit_password);
		proxy_password.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				proxy_password.getTextSize()));

		save_btn = (Button) findViewById(R.id.savebtn);
		save_btn.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				save_btn.getTextSize()));

		save_btn.setLayoutParams(setButtonParams());
		save_btn.setOnClickListener(this);

		reset_btn = (Button) findViewById(R.id.resetbtn);
		reset_btn.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
				reset_btn.getTextSize()));

		reset_btn.setLayoutParams(setButtonParams());
		reset_btn.setOnClickListener(this);
		Constants.setProxyHost(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_KEY, ""));
		Constants.setProxyPort(myPrefs.getInt(
				Constants.SETTINGS_PROXY_PORT_KEY, -1));
		Constants.setProxyUsername(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_USERNAME, ""));
		Constants.setProxyPassword(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_PASSWORD, ""));
		if (Constants.getProxyPort() != -1) {
			proxy_host.setText(Constants.getProxyHost() + "");
			proxy_port.setText(Constants.getProxyPort() + "");
			proxy_username.setText(Constants.getProxyUSername() + "");
			proxy_password.setText(Constants.getProxyPassword() + "");
		}

		if (Constants.getProxyPort() == -1) {
			proxy_host.setText("");
			proxy_port.setText("");
			proxy_username.setText("");
			proxy_password.setText("");
		}

		// ACRA.getErrorReporter().putCustomData("USER Id",
		// username_et.getText().toString());
		//
		//
		// ACRA.getErrorReporter().putCustomData("System Language",
		// Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
		// Constants.SETTINGS_LANGUAGE_INDEX, 0)]);
		// password_et.setText(myPrefs.getString(
		// Constants.POST_FIELD_LOGIN_PASSWORD, ""));
		/*
		 * if (!IsInternetConnectted() &&
		 * !username_et.getText().toString().equals("")) {
		 * username_et.setOnFocusChangeListener(null);
		 * password_et.setOnFocusChangeListener(null);
		 * username_et.setFocusable(false); password_et.setFocusable(false); }
		 */
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resetbtn:
			String host = "";
			String port = "-1";
			String username = "";
			String password = "";
			try {
				Constants.setProxyPort(Integer.parseInt(port));
				Constants.setProxyHost(host);
				Constants.setProxyUsername(username);
				Constants.setProxyPassword(password);
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_KEY, host);
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_USERNAME,
						username);
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_PASSWORD,
						password);
				prefsEditor.putInt(Constants.SETTINGS_PROXY_PORT_KEY,
						Integer.parseInt(port));
				prefsEditor.commit();
				finish();
			} catch (Exception ex) {
				Toast.makeText(ProxyActivity.this, "Please enter correct port",
						Toast.LENGTH_SHORT).show();
				Constants.setProxyPort(-1);
				Constants.setProxyHost(null);
			}

			break;
		case R.id.savebtn:
			hideKeyBoard();
			host = proxy_host.getText().toString().trim();
			port = proxy_port.getText().toString().trim();
			username = proxy_username.getText().toString().trim();
			password = proxy_password.getText().toString().trim();
			try {
				Constants.setProxyPort(Integer.parseInt(port));
				Constants.setProxyHost(host);
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_KEY, host);
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_USERNAME,
						username);
				prefsEditor.putString(Constants.SETTINGS_PROXY_HOST_PASSWORD,
						password);
				prefsEditor.putInt(Constants.SETTINGS_PROXY_PORT_KEY,
						Integer.parseInt(port));
				prefsEditor.commit();
				finish();
			} catch (Exception ex) {
				Toast.makeText(ProxyActivity.this, "Please enter correct port",
						Toast.LENGTH_SHORT).show();
				Constants.setProxyPort(-1);
				Constants.setProxyHost(null);
			}

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
			dialog = new Progress_Dialog(ProxyActivity.this);
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
				ShowAlert(ProxyActivity.this,
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
					ShowAlert(ProxyActivity.this,
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
					ProxyActivity.this.getApplicationContext(),
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
			textView.setTextSize(UIHelper.getFontSize(ProxyActivity.this,
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
			myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
			dba.createDataBase(Helper.getSystemURLfromDB(),
					myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
				+ "\"Assigned\"" + " OR StatusName=" + "\"survey\"";
		DBHelper.deleteJoblistRecords(where);
		DBHelper.deleteRecords(null);
	}

}

package com.mor.sa.android.activities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Progress_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

public class LoginFileActivity extends Activity implements OnClickListener {

	private static String DB_PATH = "/data/data/com.mor.sa.android.activity/databases/";
	EditText username_et, password_et;
	Button login_btn, reset_btn;
	ImageButton setting_btn;
	boolean islogin = true;
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
		if (Helper.getTheme(LoginFileActivity.this) == 0) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.loginlayout);
			if (layout != null)
				layout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.navigation_bar_dark));

			ImageView imglayout = (ImageView) findViewById(R.id.btmBar);
			if (imglayout != null)
				imglayout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.navigation_bar_dark));

			layout = (RelativeLayout) findViewById(R.id.screenBackGround);
			if (layout != null)
				layout.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.background_dark));

			TextView tv = (TextView) findViewById(R.id.loginHeader);
			if (tv != null)
				tv.setTextColor(getResources().getColor(android.R.color.white));
		}
	}

	private String ReadFile() {
		Intent intent = getIntent();
		InputStream is = null;
		FileOutputStream os = null;
		String fullPath = null;

		try {
			String action = intent.getAction();

			if (!Intent.ACTION_VIEW.equals(action))
				return null;

			Uri uri = intent.getData();
			String scheme = uri.getScheme();
			String name = null;

			if (scheme.equals("file")) {
				List<String> pathSegments = uri.getPathSegments();

				if (pathSegments.size() > 0)
					name = pathSegments.get(pathSegments.size() - 1);

			}

			else if (scheme.equals("content")) {
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DISPLAY_NAME },
						null, null, null);

				cursor.moveToFirst();

				int nameIndex = cursor
						.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
				if (nameIndex >= 0)
					name = cursor.getString(nameIndex);

			}

			else
				return null;

			if (name == null)
				return null;

			int n = name.lastIndexOf(".");
			String fileName, fileExt;

			if (n == -1)
				return null;

			else {
				fileName = name.substring(0, n);
				fileExt = name.substring(n);
				if (!fileExt.equals(".txt"))
					return null;

			}

			is = getContentResolver().openInputStream(uri);

			byte[] buffer = new byte[4096];
			int count;
			String file_string = "";
			while ((count = is.read(buffer)) > 0) {

				for (int i = 0; i < buffer.length; i++) {
					file_string += (char) buffer[i];
				}

			}
			is.close();
			return file_string;
		}

		catch (Exception e) {
			if (is != null) {
				try {
					is.close();
				}

				catch (Exception e1) {
				}
			}

		}

		return null;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		Constants.setProxyHost(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_KEY, ""));
		Constants.setProxyPort(myPrefs.getInt(
				Constants.SETTINGS_PROXY_PORT_KEY, -1));
		Constants.setProxyUsername(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_USERNAME, ""));
		Constants.setProxyPassword(myPrefs.getString(
				Constants.SETTINGS_PROXY_HOST_PASSWORD, ""));
		Bundle b = getIntent().getExtras();
		android.content.Intent intent = getIntent();
		String dta = ReadFile();
		String[] data = dta.split("\r\n", 20);
		if (data.length == 3) {
			String url = data[0].replace("\r\n", "");
			String username = data[1].replace("\r\n", "");
			String pwd = data[2].replace("\r\n", "");

			if (url.contains("=")) {
				data = url.split("=");
				url = data[1];
			}
			if (username.contains("=")) {
				data = username.split("=");
				username = data[1];
			}
			if (pwd.contains("=")) {
				data = pwd.split("=");
				pwd = data[1];
			}
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, false);
			// prefsEditor.commit();
			// SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY, url);
			prefsEditor
					.putString(Constants.POST_FIELD_LOGIN_USERNAME, username);
			prefsEditor.putString(Constants.POST_FIELD_LOGIN_PASSWORD, pwd);
			prefsEditor.commit();

			Helper.setSystemURL(url);
			Helper.setAlternateSystemURL(myPrefs.getString(
					Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));
			if (!validateCredencials(username, username))
				return;
			if (IsInternetConnectted()) {
				new LoginTask().execute(username, pwd);
			} else {
				workInOfflineMode();
			}
		} else {

		}
		// if (intent != null) {
		//
		// final android.net.Uri data = intent.getData();
		//
		// if (data != null) {
		//
		// filePath = data.getEncodedPath();
		// File auxFile = new File(filePath.toString());
		// // if (auxFile.canRead())
		// {
		// try {
		//
		// BufferedReader br = new BufferedReader(new FileReader(
		// auxFile));
		// String line;
		//
		// StringBuilder text = new StringBuilder();
		// while ((line = br.readLine()) != null) {
		// text.append(line);
		// text.append('\n');
		// }
		// text.append("");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// // file loading comes here.
		// } // if
		// } // if
		if (b == null)
			workInOfflineMode();
		if ((myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""))
				.equals("")) {
			intent = new Intent(this.getApplicationContext(),
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
		setContentView(R.layout.login_file);
		loadData();
		setInvertDisplay();
	}

	private void loadData() {
		username_et = (EditText) findViewById(R.id.username);
		username_et.setTextSize(UIHelper.getFontSize(LoginFileActivity.this,
				username_et.getTextSize()));

		password_et = (EditText) findViewById(R.id.password);
		password_et.setTextSize(UIHelper.getFontSize(LoginFileActivity.this,
				password_et.getTextSize()));

		login_btn = (Button) findViewById(R.id.loginbtn);
		login_btn.setTextSize(UIHelper.getFontSize(LoginFileActivity.this,
				login_btn.getTextSize()));

		reset_btn = (Button) findViewById(R.id.resetbtn);
		reset_btn.setTextSize(UIHelper.getFontSize(LoginFileActivity.this,
				reset_btn.getTextSize()));

		login_btn.setLayoutParams(setButtonParams());
		reset_btn.setLayoutParams(setButtonParams());
		setting_btn = (ImageButton) findViewById(R.id.settingbtn);

		login_btn.setOnClickListener(this);
		reset_btn.setOnClickListener(this);
		setting_btn.setOnClickListener(this);
		Helper.setAlternateSystemURL(myPrefs.getString(
				Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));
		Helper.setSystemURL(myPrefs.getString(
				Constants.SETTINGS_SYSTEM_URL_KEY, ""));
		Helper.setParsed(false);
		// ACRA.getErrorReporter().putCustomData("System URL",
		// Helper.getSystemURL().toString());
		//
		username_et.setText(myPrefs.getString(
				Constants.POST_FIELD_LOGIN_USERNAME, ""));
		password_et.setText(myPrefs.getString(
				Constants.POST_FIELD_LOGIN_PASSWORD, ""));
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

		case R.id.settingbtn:
			Intent intent = new Intent(this.getApplicationContext(),
					NewSettingsActivity.class);
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

			startActivity(intent);
			finish();
		} else if (!IsInternetConnectted()) {
			ShowAlert(LoginFileActivity.this,
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
			dialog = new Progress_Dialog(LoginFileActivity.this);
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
				ShowAlert(LoginFileActivity.this,
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
					ShowAlert(LoginFileActivity.this,
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
					LoginFileActivity.this.getApplicationContext(),
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
			textView.setTextSize(UIHelper.getFontSize(LoginFileActivity.this,
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
					Helper.getSystemURL(),null);

			String where = "StatusName=" + "\"Scheduled\"" + " OR StatusName="
					+ "\"Assigned\"" + " OR StatusName=" + "\"survey\"";
			DBHelper.deleteJoblistRecords(where);
			DBHelper.deleteRecords(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

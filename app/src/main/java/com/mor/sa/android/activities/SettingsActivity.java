package com.mor.sa.android.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.mor.sa.android.activities.R;

public class SettingsActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	RelativeLayout rvinjobscreen;
	EditText systemUrl;
	TextView tvSystemURL;

	ImageView helpbtn;
	TextView tvTheme, s_urltext, s_image_resizing, s_camra_setting,
			s_job_setting;
	View s_urltext_dividing_line, s_languagetext_dividing_line,
			s_themetexttext_dividing_line,
			s_image_resizingtexttext_dividing_line,
			s_branchwisesorting_dividing_line, s_tracker_setting_dividing_line,
			s_camra_setting_dividing_line, s_job_setting_dividing_line;
	Button save;
	SharedPreferences myPrefs;
	Spinner spinner, theme, resizing_list, branch_spinner;
	int modeSelect;
	TextView tv;
	String[] languages = null;
	private ToggleButton switchSorting;
	private ToggleButton switchTimeStamp;
	private ToggleButton switchFilterDate;
	private ToggleButton switchAnotherPhoto;
	private ToggleButton switchBranchName;
	private ToggleButton switchDeviceCamera;
	private ToggleButton switchCropping;
	private ToggleButton switchResolution;
	private ToggleButton switchWifiOnly;
	private TextView s_branchwisesorting;
	private TextView s_croppingText;
	private TextView s_anotherText;
	private TextView s_switch_branch_name;
	private ToggleButton switchAlternateJobs;
	private ToggleButton switchBackup;
	private ToggleButton switchtracking;
	TextView textView;
	TextView s_switch_time_stamp, s_trackerText;
	private TextView s_switch_filter_date;
	private TextView s_tracker_setting;
	RelativeLayout layout;
	private boolean isFirstTime;
	private ScrollView scroll;
	private TextView tvLanguage;
	private TextView s_switchDeviceCameraText;
	private TextView s_resolutionSelector;
	private TextView s_switch_alternate_jobs;

	public void makeGuideDialog(Context context) {

		final Dialog err_dialog = new Dialog(context);
		err_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		err_dialog.setContentView(R.layout.dialog_briefing);
		TextView txt = err_dialog.findViewById(R.id.textView1);

		WebView wv = (WebView) err_dialog.findViewById(R.id.briefingView);
		final String mimeType = "text/html";
		final String encoding = "UTF-8";
		wv.loadUrl("file:///android_asset/settingguide.html");
		Button btnClose = (Button) err_dialog.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				err_dialog.dismiss();
			}
		});

		//if (b==false)//preview dialog
		{

			//txt.setText("Preview");
			txt.setText("Help");//context.getResources().getString(R.string.questionnaire_preview_menu));
			btnClose.setText(context.getResources().getString(R.string.close));
			//btnClose.setText("Close");
		}
		err_dialog.show();
	}

	private void setInvertDisplay(int i) {
		if (i == 1) {
			layout = (RelativeLayout) findViewById(R.id.jdbottomrv);
			layout = (RelativeLayout) findViewById(R.id.rvjobdetailscreen);
			layout = (RelativeLayout) findViewById(R.id.backGroundDrawable);
			layout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.background_dark));
		} else {
			layout = (RelativeLayout) findViewById(R.id.jdbottomrv);
			layout = (RelativeLayout) findViewById(R.id.rvjobdetailscreen);
			layout = (RelativeLayout) findViewById(R.id.backGroundDrawable);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		Constants.setLocale(SettingsActivity.this);
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_new_latest);
		Constants.setLocale(SettingsActivity.this);
		helpbtn=(ImageView)  findViewById(R.id.btnhelp);
		if (helpbtn!=null)
		helpbtn.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.jd_heder);
		scroll = (ScrollView) findViewById(R.id.jdmiddle);
//		scroll.post(new Runnable() {
//			@Override
//			public void run() {
//				scroll.fullScroll(View.FOCUS_DOWN);
//				modeSelect = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);
//				if (modeSelect!=3)
//					new BubbleShowCaseSequence()
//							.addShowCase(showMenuShowCase()).show();
//			}
//		});
		textView.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				textView.getTextSize()));
		switchtracking = (ToggleButton) findViewById(R.id.switchtracking);
		switchFilterDate = (ToggleButton) findViewById(R.id.switch_filter_date);
		switchAnotherPhoto = (ToggleButton) findViewById(R.id.switchAnother);
		switchTimeStamp = (ToggleButton) findViewById(R.id.switch_time_stamp);
		switchBranchName = (ToggleButton) findViewById(R.id.switch_branch_name);
		switchDeviceCamera= (ToggleButton) findViewById(R.id.switchDeviceCamera);
		switchCropping = (ToggleButton) findViewById(R.id.switchCropping);
		switchResolution = (ToggleButton) findViewById(R.id.switchResolution);
		switchWifiOnly = (ToggleButton) findViewById(R.id.switch_wifi_only);
		switchAlternateJobs = (ToggleButton) findViewById(R.id.switch_alternate_jobs);
		switchBackup = (ToggleButton) findViewById(R.id.switchBackup);
		s_trackerText = (TextView) findViewById(R.id.s_trackerText);
		s_switchDeviceCameraText= (TextView) findViewById(R.id.s_switchDeviceCameraText);
		s_urltext = (TextView) findViewById(R.id.s_urltext);
		s_image_resizing = (TextView) findViewById(R.id.s_image_resizing);
		s_camra_setting = (TextView) findViewById(R.id.s_camra_setting);
		s_job_setting = (TextView) findViewById(R.id.s_job_setting);

		s_switch_time_stamp = (TextView) findViewById(R.id.s_switch_time_stamp);
		s_switch_time_stamp.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_switch_time_stamp.getTextSize()));
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, null);

		systemUrl = (EditText) findViewById(R.id.s_url);
		if (getPackageName() != null
				&& getPackageName().contains(Helper.CONSTPACKAGEPREFIX)) {
			systemUrl.setEnabled(false);
		}
		systemUrl.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				systemUrl.getTextSize()));
		branch_spinner = (Spinner) findViewById(R.id.branch_sorting_list);
		spinner = (Spinner) findViewById(R.id.languagelist);

		s_urltext_dividing_line = (View) findViewById(R.id.s_urltext_dividing_line);
		s_languagetext_dividing_line = (View) findViewById(R.id.s_languagetext_dividing_line);
		s_themetexttext_dividing_line = (View) findViewById(R.id.s_themetexttext_dividing_line);
		s_image_resizingtexttext_dividing_line = (View) findViewById(R.id.s_image_resizingtexttext_dividing_line);
		s_branchwisesorting_dividing_line = (View) findViewById(R.id.s_branchwisesorting_dividing_line);
		s_tracker_setting_dividing_line = (View) findViewById(R.id.s_tracker_setting_dividing_line);
		s_camra_setting_dividing_line = (View) findViewById(R.id.s_camra_setting_dividing_line);
		s_job_setting_dividing_line = (View) findViewById(R.id.s_job_setting_dividing_line);

		String str = android.os.Build.VERSION.SDK;
		int sdk = Integer.valueOf(str);

		Locale[] langs = Locale.getAvailableLocales();
		Boolean isHindi = false;

		languages = getResources().getStringArray(R.array.lang_list);

		String[] branch_sorting_list = new String[2];
		branch_sorting_list[0] = getResources().getString(
				R.string.s_alphabetical);
		branch_sorting_list[1] = getResources().getString(R.string.s_distance);
		ArrayAdapter badapter = new ArrayAdapter(

		this, UIHelper.getSpinnerLayoutSize(SettingsActivity.this, modeSelect),
				branch_sorting_list);
		badapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				SettingsActivity.this, modeSelect));
		branch_spinner.setAdapter(badapter);
		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_SORTING,
				Helper.ENABLESORTINGBYDEFAULT)) {
			branch_spinner.setSelection(1);
		} else {
			branch_spinner.setSelection(0);
		}

		branch_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				boolean arg22 = true;
				if (arg2 == 1)
					arg22 = true;
				else
					arg22 = false;
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor
						.putBoolean(Constants.SETTINGS_ENABLE_SORTING, arg22);
				prefsEditor.commit();
				// ACRA.getErrorReporter().putCustomData("System Language",
				// languages[arg2].toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// switchSorting.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// SharedPreferences.Editor prefsEditor = myPrefs.edit();
		// prefsEditor.putBoolean(Constants.SETTINGS_ENABLE_SORTING,
		// isChecked);
		// prefsEditor.commit();
		// }
		// });

		ArrayAdapter adapter = new ArrayAdapter(

		this, UIHelper.getSpinnerLayoutSize(SettingsActivity.this, modeSelect),
				languages);
		adapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				SettingsActivity.this, modeSelect));
		spinner.setAdapter(adapter);

		theme = (Spinner) findViewById(R.id.themeslist);
		resizing_list = (Spinner) findViewById(R.id.resizing_list);

		String[] themesList = getResources()
				.getStringArray(R.array.themes_list);

		String[] themes = new String[1];
		if (themesList.length > 1) {
			themes[0] = themesList[0];
		} else {
			themes[0] = getResources().getString(R.string.default_theme);
		}
		themes[0] = getResources().getString(R.string.default_theme);
		ArrayList<String> themeList = new ArrayList<String>();
		themeList.add(Helper.getThemeName(getApplicationContext()));
		// themeList.add("VOC theme");
		ArrayAdapter themeadapter = new ArrayAdapter(
				this,
				UIHelper.getSpinnerLayoutSize(SettingsActivity.this, modeSelect),
				themeList);
		themeadapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				SettingsActivity.this, modeSelect));
		theme.setAdapter(themeadapter);

		ArrayList<String> resizeList = new ArrayList<String>();
		resizeList.add(getResources().getString(
				R.string.settings_image_original));
		resizeList
				.add(getResources().getString(R.string.settings_image_medium));
		resizeList.add(getResources().getString(R.string.settings_image_small));
		resizeList.add(getResources().getString(R.string.settings_image_ask));

		ArrayAdapter resizeAdapter = new ArrayAdapter(
				this,
				UIHelper.getSpinnerLayoutSize(SettingsActivity.this, modeSelect),
				resizeList);
		resizeAdapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				SettingsActivity.this, modeSelect));
		resizing_list.setAdapter(resizeAdapter);

		s_branchwisesorting = (TextView) findViewById(R.id.s_branchwisesorting);
		s_branchwisesorting.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_branchwisesorting.getTextSize()));
		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// s_branchwisesorting.setTextColor(getResources().getColor(
		// android.R.color.white));

		s_croppingText = (TextView) findViewById(R.id.s_croppingText);
		s_croppingText.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				s_croppingText.getTextSize()));

		s_resolutionSelector = (TextView) findViewById(R.id.s_resolutionSelector);
		s_resolutionSelector.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_resolutionSelector.getTextSize()));

		s_anotherText = (TextView) findViewById(R.id.s_anotherText);
		s_anotherText.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				s_anotherText.getTextSize()));
		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// s_croppingText.setTextColor(getResources().getColor(
		// android.R.color.white));
		s_switch_filter_date = (TextView) findViewById(R.id.s_switch_filter_date);
		s_switch_filter_date.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_switch_filter_date.getTextSize()));

		s_switch_alternate_jobs = (TextView) findViewById(R.id.s_switch_alternate_jobs);
		s_switch_alternate_jobs.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_switch_alternate_jobs.getTextSize()));

		s_switch_branch_name = (TextView) findViewById(R.id.s_switch_branch_name);
		s_switch_branch_name.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_switch_branch_name.getTextSize()));

		s_tracker_setting = (TextView) findViewById(R.id.s_tracker_setting);
		s_tracker_setting.setTextSize(UIHelper.getFontSize(
				SettingsActivity.this, s_tracker_setting.getTextSize()));

		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// s_croppingText.setTextColor(getResources().getColor(
		// android.R.color.white));

		tvLanguage = (TextView) findViewById(R.id.s_languagetext);
		tvLanguage.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				tvLanguage.getTextSize()));
		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// tvLanguage.setTextColor(getResources().getColor(
		// android.R.color.white));

		tvSystemURL = (TextView) findViewById(R.id.s_urltext);
		tvSystemURL.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				tvSystemURL.getTextSize()));
		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// tvSystemURL.setTextColor(getResources().getColor(
		// android.R.color.white));

		tvTheme = (TextView) findViewById(R.id.s_themetext);
		tvTheme.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				tvTheme.getTextSize()));

		((TextView) findViewById(R.id.s_image_resizing)).setTextSize(UIHelper
				.getFontSize(SettingsActivity.this,
						((TextView) findViewById(R.id.s_image_resizing))
								.getTextSize()));
		// if (Helper.getTheme(SettingsActivity.this) == 1)
		// tvTheme.setTextColor(getResources().getColor(android.R.color.white));

		systemUrl
				.setWidth((Helper.getViewWidth(this.getApplicationContext())) * 2);
		save = (Button) findViewById(R.id.savebtn);
		// save.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
		// save.getTextSize()));
		// save.requestFocus();

		systemUrl.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
				systemUrl.getTextSize()));
		save.setOnClickListener(this);

//		proxy = (Button) findViewById(R.id.btnproxy);
//		proxy.setOnClickListener(this);

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_BRANCH_FULL_NAME,
				Helper.ENABLEBRANCHFULLNAMEBYDEFAULT)) {
			switchBranchName.setChecked(true);
		} else {
			switchBranchName.setChecked(false);
		}

		switchBranchName
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_ENABLE_BRANCH_FULL_NAME,
								isChecked);
						prefsEditor.commit();
					}
				});

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_DATE_FILTER,
				Helper.ENABLEDATEFILTERBYDEFAULT)) {
			switchFilterDate.setChecked(true);
		} else {
			switchFilterDate.setChecked(false);
		}

		switchFilterDate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_ENABLE_DATE_FILTER,
								isChecked);
						prefsEditor.commit();
					}
				});

		if (myPrefs.getBoolean(Constants.SETTINGS_switchtracking,
				Helper.ENABLETRACKINGBYDEFAULT)) {
			switchtracking.setChecked(true);
		} else {
			switchtracking.setChecked(false);
		}
		switchtracking
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_switchtracking, isChecked);
						prefsEditor.commit();
						if (isChecked ) {
							try{
//							Intent i = new Intent(SettingsActivity.this,
//									comService.class);
//							i.putExtra("KEY1",
//									"Value to be used by the service");
//							startService(i);
								Helper.setAN168Log("line 447, bool="+isChecked);
						}
						catch(Exception ex)
						{
							Helper.setAN168Log("crashed line 451");
						}
						} else if (isMyServiceRunning(comService.class)) {
							Helper.setAN168Log("line 454, bool="+isChecked);
//							Intent i = new Intent(SettingsActivity.this,
//									comService.class);
//							i.putExtra("KEY1",
//									"hideicon");
//							stopService(i);
						}
					}
				});
		if (myPrefs.getBoolean(Constants.SETTINGS_SHOW_ANOTHER_PHOTO,
				Helper.ENABLEMULTIPLEPICTUREDEFAULT)) {
			switchAnotherPhoto.setChecked(true);
		} else {
			switchAnotherPhoto.setChecked(false);
		}

		switchAnotherPhoto
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_SHOW_ANOTHER_PHOTO,
								isChecked);
						prefsEditor.commit();
					}
				});

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_TIME_STAMP,
				Helper.ENABLETIMESTAMPBYDEFAULT)) {
			switchTimeStamp.setChecked(true);
		} else {
			switchTimeStamp.setChecked(false);
		}

		switchTimeStamp
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor
								.putBoolean(
										Constants.SETTINGS_ENABLE_TIME_STAMP,
										isChecked);
						prefsEditor.commit();
					}
				});
		if (myPrefs.getBoolean(Constants.SETTINGS_BACKUP, true)) {
			switchBackup.setChecked(true);
		} else {
			switchBackup.setChecked(false);
		}

		switchBackup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor.putBoolean(Constants.SETTINGS_BACKUP, isChecked);
				prefsEditor.commit();
			}
		});

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_CROPPING, false)) {

			switchCropping.setChecked(true);
		} else {
			switchCropping.setChecked(false);
		}

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_DEFAULT_CAMERA, false)) {
			switchDeviceCamera.setChecked(true);
		} else {
			switchDeviceCamera.setChecked(false);
		}

		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_RESOLUTION, false)) {
			switchResolution.setChecked(true);
		} else {
			switchResolution.setChecked(false);
		}
		if (myPrefs.getBoolean(Constants.SETTINGS_WIFI_ONLY, false)) {
			switchWifiOnly.setChecked(true);
		} else {
			switchWifiOnly.setChecked(false);
		}
		if (myPrefs.getBoolean(Constants.SETTINGS_ENABLE_ALTERNATE_ORDER,
				Helper.ENABLEALTERNATEJOBS)) {
			switchAlternateJobs.setChecked(true);
		} else {
			switchAlternateJobs.setChecked(false);
		}
		switchAlternateJobs
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_ENABLE_ALTERNATE_ORDER,
								isChecked);
						prefsEditor.commit();

					}
				});
		switchDeviceCamera
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_ENABLE_DEFAULT_CAMERA, isChecked);
						prefsEditor.commit();
						Helper.setDeviceCamera(isChecked);
					}
				});
		switchCropping
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putBoolean(
								Constants.SETTINGS_ENABLE_CROPPING, isChecked);
						prefsEditor.commit();
					}
				});
		switchResolution
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor
								.putBoolean(
										Constants.SETTINGS_ENABLE_RESOLUTION,
										isChecked);
						prefsEditor.commit();
					}
				});
		switchWifiOnly
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor
								.putBoolean(
										Constants.SETTINGS_WIFI_ONLY,
										isChecked);
						prefsEditor.commit();
					}
				});

		systemUrl.setText(myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY,
				""));
		modeSelect = myPrefs.getInt(Constants.SETTINGS_MODE_INDEX, 1);

		// theme.setSelection(modeSelect);
		// if (modeSelect == 0)
		// setInvertDisplay(0);
		// else
		// setInvertDisplay(1);
		systemUrl.setFocusable(true);
		systemUrl.setOnFocusChangeListener(this);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}

		// systemUrl.setText("www.checker-soft.com/gfk-training-3");

		spinner.setSelection(myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX,
				0));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Locale locale = new Locale(
						Constants.SETTINGS_LOCALE_VAL_ARR[arg2]);
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor.putInt(Constants.SETTINGS_LANGUAGE_INDEX, arg2);
				prefsEditor.commit();
				// ACRA.getErrorReporter().putCustomData("System Language",
				// languages[arg2].toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		theme.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				SharedPreferences.Editor prefsEditor = myPrefs.edit();
				prefsEditor.putInt(Constants.SETTINGS_MODE_INDEX, position);
				prefsEditor.commit();
				Helper.setTheme(position);

				modeSelect = position;
				//
				// theme.setSelection(modeSelect);
				// if (modeSelect == 0)
				// setInvertDisplay(1);
				// else
				// setInvertDisplay(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		resizing_list.setSelection(myPrefs.getInt(
				Constants.SETTINGS_RESIZE_INDEX, 0));
		Helper.setSampleSize(myPrefs.getInt(Constants.SETTINGS_RESIZE_INDEX, 0));
		resizing_list.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				if (arg2 == 2
						&& myPrefs.getInt(Constants.SETTINGS_RESIZE_INDEX, 0) != 2) {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							SettingsActivity.this);
					alert.setCancelable(false);
					alert.setTitle("");
					TextView textView = new TextView(SettingsActivity.this);
					textView.setTextSize(UIHelper.getFontSize(
							SettingsActivity.this, textView.getTextSize()));
					textView.setText(getString(R.string.smalimgWarn));
					alert.setView(textView);
					alert.setPositiveButton(
							getString(R.string.s_item_column_0_line_344_file_210),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									SharedPreferences.Editor prefsEditor = myPrefs
											.edit();
									prefsEditor.putInt(
											Constants.SETTINGS_RESIZE_INDEX,
											arg2);
									prefsEditor.commit();

									Helper.setSampleSize(arg2);
								}
							});

					alert.setNegativeButton(
							getString(R.string.s_item_column_0_line_346_file_210),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									resizing_list.setSelection(myPrefs.getInt(
											Constants.SETTINGS_RESIZE_INDEX, 0));
								}
							});
					alert.show();
				} else {
					SharedPreferences.Editor prefsEditor = myPrefs.edit();
					prefsEditor.putInt(Constants.SETTINGS_RESIZE_INDEX, arg2);
					prefsEditor.commit();

					Helper.setSampleSize(arg2);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		setInvertDisplay(0);
		save.requestFocus();
		systemUrl.clearFocus();

		loadViews();
//		switchDeviceCamera.setFocusable(true);
//		switchDeviceCamera.setFocusableInTouchMode(true);
//		switchDeviceCamera.requestFocus(View.FOCUS_DOWN);

	}
	private BubbleShowCaseBuilder showMenuShowCase() {
		return new BubbleShowCaseBuilder(this) //Activity instance
				.title("Camera") //Any title for the bubble view
				.description("Android's own camera with all features.") //More detailed description
				.arrowPosition(BubbleShowCase.ArrowPosition.RIGHT) //You can force the position of the arrow to change the location of the bubble.
				.backgroundColor(Color.parseColor("#94ba09")) //Bubble background color
				.textColor(Color.WHITE) //Bubble Text color
				.titleTextSize(20) //Title text size in SP (default value 16sp)
				.descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
				.showOnce("BUBBLE_SHOW_CAMERA123") //Id to show only once the BubbleShowCase

				.targetView(switchDeviceCamera);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		if (helpbtn!=null && helpbtn.getId() == arg0.getId()) {
			makeGuideDialog(SettingsActivity.this);
		}
		if (save.getId() == arg0.getId()) {
			hideKeyBoard();
			if (IsInternetConnectted()) {
				String systemurll = Helper.getSystemURLfromDB();

				String editTextURL = systemUrl.getText().toString();

				if (!editTextURL.equals(systemurll)) {
					setLogin(false);
				}
			}
			if (Helper.IsEmptyString(systemUrl.getText().toString())) {
				ShowAlert(SettingsActivity.this,
						getString(R.string.error_alert_title),
						getString(R.string.settings_url_alert),
						getString(R.string.alert_btn_lbl_ok));
				return;
			} else {
				String addedurl = systemUrl.getText().toString();
				if (!addedurl.equals(Helper.getSystemURLfromDB())) {
					Helper.setSystemURL(Helper.getValidURL(systemUrl.getText()
							.toString()));
					// hideKeyBoard();
					SharedPreferences.Editor prefsEditor = myPrefs.edit();
					prefsEditor.putString(Constants.SETTINGS_SYSTEM_URL_KEY,
							Helper.getSystemURLfromDB());
					prefsEditor.commit();
					Connector.cookies = null;
				}

				Intent intent = new Intent(this.getApplicationContext(),
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	private void setLogin(boolean islogin) {
		if (myPrefs == null)
			return;
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, islogin);
		prefsEditor.putString(Constants.POST_FIELD_LOGIN_USERNAME, "");
		prefsEditor.putString(Constants.POST_FIELD_LOGIN_PASSWORD, "");
		prefsEditor.commit();
		// saveOfflineData();
	}

	public void ShowAlert(Context context, String title, String message,
			String button_lbl) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		TextView textView = new TextView(context);
		textView.setTextSize(UIHelper.getFontSize(SettingsActivity.this,
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
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			systemUrl.setSelection(systemUrl.getText().length());
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

	private void loadViews() {

		// EditText systemUrl;
		// Button save;
		// Button proxy;
		// Spinner spinner, theme, resizing_list, branch_spinner;
		Helper.changeBtnColor(save);
		// TextView textView;
		// TextView s_switch_time_stamp;
		// TextView tv;
		// TextView tvSystemURL;
		// TextView tvLanguage;
		// TextView tvTheme;
		// private TextView s_branchwisesorting;
		// private TextView s_croppingText;
		// private TextView s_anotherText;
		// private TextView s_switch_alternate_jobs;
		// private TextView s_switch_branch_name;
		// private TextView s_switch_filter_date;
		// private TextView s_tracker_setting;
		Helper.changeTxtViewColor(textView);

		Helper.changeTxtViewColor(s_urltext);
		Helper.changeTxtViewColor(s_trackerText);
		Helper.changeTxtViewColor(s_image_resizing);
		Helper.changeTxtViewColor(s_camra_setting);
		Helper.changeTxtViewColor(s_job_setting);
		Helper.changeTxtViewColor(s_switch_time_stamp);
		Helper.changeTxtViewColor(tv);
		Helper.changeTxtViewColor(tvLanguage);
		Helper.changeTxtViewColor(tvTheme);
		Helper.changeTxtViewColor(s_branchwisesorting);
		Helper.changeTxtViewColor(s_resolutionSelector);
		Helper.changeTxtViewColor(s_croppingText);
		Helper.changeTxtViewColor(s_anotherText);
		Helper.changeTxtViewColor(s_switch_alternate_jobs);
		Helper.changeTxtViewColor(s_switch_branch_name);
		Helper.changeTxtViewColor(s_switch_filter_date);
		Helper.changeTxtViewColor(s_tracker_setting);

//		private TextView s_switchDeviceCameraText;
//		private TextView s_resolutionSelector;
//		private TextView s_switch_alternate_jobs;
//		Helper.changeText(s_switchDeviceCameraText,"デフォルトのカメラアプリを使用する");
//		Helper.changeText(s_resolutionSelector,"");
//		Helper.changeText(s_switch_alternate_jobs,"");

		Helper.changeViewColor(s_urltext_dividing_line);
		Helper.changeViewColor(s_languagetext_dividing_line);
		Helper.changeViewColor(s_themetexttext_dividing_line);
		Helper.changeViewColor(s_image_resizingtexttext_dividing_line);
		Helper.changeViewColor(s_branchwisesorting_dividing_line);
		Helper.changeViewColor(s_tracker_setting_dividing_line);
		Helper.changeViewColor(s_camra_setting_dividing_line);
		Helper.changeViewColor(s_job_setting_dividing_line);
		// private ToggleButton switchSorting;
		// private ToggleButton switchTimeStamp;
		// private ToggleButton switchFilterDate;
		// private ToggleButton switchAnotherPhoto;
		// private ToggleButton switchBranchName;
		// private ToggleButton switchCropping;
		// private ToggleButton switchAlternateJobs;
		// private ToggleButton switchBackup;
		// private ToggleButton switchtracking;

		Helper.changeToggleColor(switchSorting);
		Helper.changeToggleColor(switchResolution);
		Helper.changeToggleColor(switchTimeStamp);
		Helper.changeToggleColor(switchFilterDate);
		Helper.changeToggleColor(switchAnotherPhoto);
		Helper.changeToggleColor(switchBranchName);
		Helper.changeToggleColor(switchCropping);
		Helper.changeToggleColor(switchAlternateJobs);
		Helper.changeToggleColor(switchBackup);
		Helper.changeToggleColor(switchtracking);

		// RelativeLayout layout;
	}

}

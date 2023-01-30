package com.mor.sa.android.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.acra.util.CrashData;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.checker.sa.android.data.Job;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Devices;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.transport.Connector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mor.sa.android.activities.R;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class AcraActivity extends Activity {
	private CheckerApp globV;
	private SharedPreferences myPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		globV = (CheckerApp) getApplication();
		// setContentView(R.layout.activity_addphone);
		String s = getIntent().getStringExtra("crash");
		CrashData thisCrashData = new CrashData();
		thisCrashData.setSystem_url(getUserDetails());
		thisCrashData.setSet_id(getSetId());
		thisCrashData.setOrder_id(getOrderId());
		thisCrashData.setPhone_details(getPhoneDetails());
		thisCrashData.setStack_trace(s);
		thisCrashData.setUser_comment("without_comment");

		sendCrash(thisCrashData);

		if (!AcraActivity.isAlreadyShown(AcraActivity.this)) {
			customAlert(AcraActivity.this, s);
			dialogShown(true, AcraActivity.this);
		}
	}

	public void customAlert(Context context, final String textString) {
		final Dialog dialog = new Dialog(AcraActivity.this);
		dialog.setContentView(R.layout.custom_crash_alert);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView1);
		// text.setText(textString);

		Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);

		final EditText editCrash = (EditText) dialog
				.findViewById(R.id.editcrash);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String comment = "";
				if (editCrash != null) {
					comment = editCrash.getText().toString();
				}
				CrashData thisCrashData = new CrashData();
				thisCrashData.setSystem_url(getUserDetails());
				thisCrashData.setSet_id(getSetId());
				thisCrashData.setOrder_id(getOrderId());
				thisCrashData.setPhone_details(getPhoneDetails());
				thisCrashData.setStack_trace(textString);
				thisCrashData.setUser_comment(comment);
				sendFireBaseCrash(thisCrashData);
				finish();
				
			}
		});
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
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

	public void sendFireBaseCrash(final CrashData crash)
	{
		String time="";
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy MM/dd_HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("gmt"));
			time=df.format(new Date());
			String month= String.format("MMM",new Date());
			df=new SimpleDateFormat("MMM");
			month=df.format(new Date());
			String year= String.format("yyyy",new Date());
			df=new SimpleDateFormat("yyyy");
			year=df.format(new Date());
			//crash.setTime(time);

			FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference reference = database.getReference().child("crashes").child(year).child(month).child(crash.getSystem_url()).push();
			reference.setValue(crash);
		}
		catch(Exception ec)
		{
			int i=0;
			i++;
		}
	}
	
	public void sendCrash(final CrashData thisCrash) {
		class JobTask extends AsyncTask<Void, Integer, ArrayList<Job>> {
			private String updateDate;

			@Override
			protected void onPreExecute() {

				this.updateDate = null;
			}

			@Override
			protected void onPostExecute(ArrayList<Job> result) {

				
			}

			@Override
			protected ArrayList<Job> doInBackground(Void... params) {
				if (IsInternetConnectted()) {
					//call api here
					
					List<NameValuePair> extraDataList=new ArrayList<NameValuePair>();
					extraDataList.add(new BasicNameValuePair("system_url", thisCrash.getSystem_url()));
					extraDataList.add(new BasicNameValuePair("set_id", thisCrash.getSet_id()));
					extraDataList.add(new BasicNameValuePair("order_id", thisCrash.getOrder_id()));
					extraDataList.add(new BasicNameValuePair("phone_details", thisCrash.getPhone_details()));
					extraDataList.add(new BasicNameValuePair("stack_trace", thisCrash.getStack_trace()));
					extraDataList.add(new BasicNameValuePair("user_comment", thisCrash.getUser_comment()));
					String result=Connector.postForm(Constants.crash_report_url, extraDataList);
					result+="";
				} else {
					try {
						Helper.saveCrashReportToBytes(thisCrash);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
		}
		JobTask jobtaskobj = new JobTask();
		jobtaskobj.execute();
	}

	private String getUserDetails() {
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		String userid = myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME,
				"");
		String url = myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, "");
		return userid + "@" + url;
	}

	private String getPhoneDetails() {
		return Devices.getDeviceName();
	}

	private String getOrderId() {

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		String data = myPrefs.getString(Constants.Crash_Last_ORDERID, "");
		return data;
	}

	private String getSetId() {

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		String data = myPrefs.getString(Constants.Crash_Last_SETID, "");
		return data;
	}

	public static boolean isAlreadyShown(Context con) {

		SharedPreferences mPrefs = con.getSharedPreferences("pref",
				MODE_PRIVATE);
		boolean data = mPrefs.getBoolean(Constants.Crash_isAlreadyShown, false);
		return data;
	}

	public static void dialogShown(boolean isDialogshown, Context con) {

		SharedPreferences mPrefs = con.getSharedPreferences("pref",
				MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = mPrefs.edit();

		prefsEditor.putBoolean(Constants.Crash_isAlreadyShown, isDialogshown);
		prefsEditor.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}

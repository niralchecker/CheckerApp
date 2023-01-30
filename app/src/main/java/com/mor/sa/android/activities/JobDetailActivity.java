package com.mor.sa.android.activities;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.checker.sa.android.adapter.AlternateLanguageAdapter;
import com.checker.sa.android.data.AltLanguage;
import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Cert;
import com.checker.sa.android.data.CustomFields;
import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.data.Set;
import com.checker.sa.android.data.Survey;
import com.checker.sa.android.data.Surveys;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBAdapter;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.dialog.RefusalReasonDialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.helper.jobBoardCertsListener;
import com.checker.sa.android.transport.Connector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.maps.android.MapActivity;

public class JobDetailActivity extends Activity implements OnClickListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

	public static boolean isFromWatch;

	private ArrayList<Cert> pendingCerts=null;
	private static jobBoardCertsListener certCallBack;
	public static void setCertsCallback(jobBoardCertsListener certCallBack) {
		JobDetailActivity.certCallBack = certCallBack;
	}
	TextView tv_header, date_v, status_v, description_v, setname_v, cname_v,
			bname, bname_v, cityname_v, address_v, branchphone_v, openhours_v,
			times_v, timee_v, tv_purchase_desc, tv_make_purchase,
			tvsNonRefundableServicePayment, tvsTransportationPayment,
			tvsCriticismPayment, tvsBonusPayment;
	TextView sBonusPayment_t, sCriticismPayment_t, sTransportationPayment_t,
			sNonRefundableServicePayment_t, jdcanpurchase_t, jdpurchasedes_t,
			jdtimeet, jdtimest, jdopeninght, jdbranchpt, jdaddt, jdcitynt,
			jdbranchfnamet, jdbranchname, jdclientt, jdsetnamet,
			jddescriptiont, jdstaust, jddatet;
	View separator1, separator2, separator3, separator4, separator5,
			separator6, separator7, separator8, separator9, separator10,
			separator11, separator12, separator13, separator14, separator15,
			separator16, separator17, separator18, separator19;

	private static final String WEAR_MESSAGE_PATH = "/other_msgs";
	private static final String START_JOB = "/start_job";
	private static final String START_ACTIVITY = "/start_activity";

	int modeSelect;
	String statusname;
	boolean isSurvey;
	TextView aceeptbtn, rejectbtn;
	ImageView mapbtn, calendarBtn, alBtn, rejectbtnImg, acceptbtnImg;
	Order order;
	Survey survey;
	Revamped_Loading_Dialog dialog;
	private final int MAP_ACTIVITY_CODE = 2;
	private final int QUESTIONNAIRE_ACTIVITY_CODE = 1;
	private final int JOB_GPS_CODE = 678;
	private final int JOB_GPS_OFF_CODE = 679;
	// int index = 0, localindex = 0;
	boolean dataSaved = false;
	SharedPreferences myPrefs;
	TextView tv;
	ScrollView dataView;
	WebView webview;
	private boolean isBriefing;
	String OrderID;
	private String groupedNumber;
	private boolean refreshJoblist;
	private int serverGroupedNumber;

	public void start_job() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mEditText.setText( "" );

				if (aceeptbtn.getText().toString()
						.equals(getString(R.string.jd_begin_review_btn_text))) {
					BeginReview(true);

				} else if (aceeptbtn
						.getText()
						.toString()
						.equals(getString(R.string.jd_continue_review_btn_text))) {

					BeginReview(true);
				}
			}
		});
	}

	private Set getSetsRecords(String setid) {
		// SQLiteDatabase db = DBAdapter.openDataBase();\
		Set set = null;
		try {
			set = (Set) DBHelper.convertFromBytes(setid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// // try {
		// set = DBHelper
		// .getSetsRecordForJobDetail(
		// Constants.DB_TABLE_SETS,
		// new String[] {
		// Constants.DB_TABLE_SETS_SETID,
		// Constants.DB_TABLE_SET_NAME,
		// Constants.DB_TABLE_SET_COMP_LINK,
		// Constants.DB_TABLE_SET_DESC,
		// Constants.DB_TABLE_SET_CODE,
		// Constants.DB_TABLE_SET_SHOWSAVEANDEXIT,
		// Constants.DB_TABLE_SET_SHOWTOC,
		// Constants.DB_TABLE_SET_SHOWPREVIEW,
		// Constants.DB_TABLE_SET_CLIENTNAME,
		// Constants.DB_TABLE_SET_SHOWFREETEXT,
		// Constants.DB_TABLE_SET_ENABLE_NON_ANSWERED_CONFIRMATION,
		// Constants.DB_TABLE_SET_ENABLE_QUESTION_NUMBERING_INFORM,
		// Constants.DB_TABLE_SET_ENABLE_VALIDATION_QUESTION,
		// Constants.DB_TABLE_SET_ALLOW_CHECKER_TO_SET_FINISHTIME,
		// Constants.DB_TABLE_SET_ALLOW_CRIT_FILE_UPLOAD,
		// Constants.DB_TABLE_SET_ALTLANG_ID,
		// Constants.DB_TABLE_SET_SHOWBACK,
		// Constants.DB_TABLE_SET_ANSWERS_ACT_AS_SUBMIT,
		// Constants.DB_TABLE_SET_DefaultPaymentForChecker,
		// Constants.DB_TABLE_SET_DefaultBonusPayment,
		// Constants.DB_TABLE_SET_AskForServiceDetails,
		// Constants.DB_TABLE_SET_AskForPurchaseDetails,
		// Constants.DB_TABLE_SET_AskForTransportationDetails,
		// Constants.DB_TABLE_SET_AutoApproveTransportation,
		// Constants.DB_TABLE_SET_AutoApprovePayment,
		// Constants.DB_TABLE_SET_AutoApproveService,
		// Constants.DB_TABLE_SET_AutoApprovePurchase,
		// Constants.DB_TABLE_SET_AutoApproveService,
		// Constants.DB_TABLE_SETS_AllowCheckerToSetLang,
		// Constants.DB_TABLE_SETS_isDifferentLangsAvailable,
		//
		// }, Constants.DB_TABLE_SETS_SETID + "=" + "\"" + setid
		// + "\"");
		//
		if (set == null) {

			return null;
		}

		if (QuestionnaireActivity.cachedSet == null
				|| (QuestionnaireActivity.cachedSet.getSetID() != null
						&& !QuestionnaireActivity.cachedSet.getSetID().equals(
								set.getSetID()) && (QuestionnaireActivity.cachedSet
						.getListObjects() == null || QuestionnaireActivity.cachedSet
						.getListObjects().size() == 0))) {
			QuestionnaireActivity.cachedSet = set;
		}
		return set;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

		Constants.setLocale(JobDetailActivity.this);
	}

	Order jobOrder = null;
	private ArrayList<CustomFields> customFields;
	private Spinner spinner;
	private Set set;
	public int msgId;

	private int count;

	@Override
	protected void onResume() {
		super.onResume();
		Constants.setLocale(JobDetailActivity.this);
		Bundle b = getIntent().getExtras();

		if (b == null)
		{finish();return;}

		if (b.containsKey(Constants.POST_FIELD_IS_ARCHIVE)) {finish();return;}
		if (JobDetailActivity.isFromWatch) {
			JobDetailActivity.isFromWatch = false;
			finish();
		}
		comunicator.detailJob = JobDetailActivity.this;
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		modeSelect = myPrefs.getInt(Constants.SETTINGS_MODE_INDEX, 1);
		if (myPrefs.contains("ispaused")
				&& myPrefs.getBoolean("ispaused", false)) {

			OrderID = myPrefs.getString("order_id", "");
			if (OrderID.contains("-")) {
				Intent intent = new Intent(this.getApplicationContext(),
						QuestionnaireActivity.class);
				intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
						myPrefs.getString("order_id", ""));
				intent.putExtra(Constants.FIELD_ORDER_SET_ID,
						myPrefs.getString("setid", ""));
				intent.putExtra(Constants.FIELD_ORDER_SET_ID,
						myPrefs.getString("setid", ""));
				intent.putExtra("isPaused", true);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this.getApplicationContext(),
						QuestionnaireActivity.class);
				intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
						myPrefs.getString("order_id", ""));
				intent.putExtra(Constants.FIELD_ORDER_SET_ID,
						myPrefs.getString("setid", ""));
				intent.putExtra("isPaused", true);
				startActivity(intent);
			}
			return;
		}
		if (CheckerApp.getQuestionResult() != null) {

			b = getIntent().getExtras();
			if (b != null && OrderID == null || OrderID.length() == 0) {

				OrderID = b.getString("OrderID");
			}
			onQuestionResult(CheckerApp.getQuestionResult());
		}
	}

	public void customAlertforLanguages(Context context,
			final ArrayList<AltLanguage> result, String selectedLanGID) {
		int selectedIndex = 0;
		for (int i = 0; i < result.size(); i++) {
			if (selectedLanGID != null && result.get(i).getAltLangID() != null
					&& result.get(i).getAltLangID().equals(selectedLanGID)) {
				selectedIndex = i;
			}
		}
		final Dialog dialog = new Dialog(JobDetailActivity.this);
		dialog.setContentView(R.layout.dialog_alternate_branch);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView1);
		text.setText("");
		if (result == null || result.size() == 0)
			text.setText(getResources().getString(
					R.string.unable_to_find_alternative_job));
		final ArrayList<AltLanguage> languages = result;
		ListView listJobs = (ListView) dialog.findViewById(R.id.lvjobs);
		listJobs.setAdapter(new AlternateLanguageAdapter(
				JobDetailActivity.this, result, selectedIndex));

		listJobs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int arg2, long id) {
				try {
					if (arg2 == 0) {
						QuestionnaireActivity.langid = "-1";

						SharedPreferences myPrefs = getSharedPreferences(
								"pref", MODE_PRIVATE);
						SharedPreferences.Editor prefsEditor = myPrefs.edit();

						prefsEditor
								.putString(
										Constants.SETTINGS_LANGUAGE_INDEX_PREFFER,
										"-1");
						prefsEditor.commit();
					} else {
						SharedPreferences myPrefs = getSharedPreferences(
								"pref", MODE_PRIVATE);
						SharedPreferences.Editor prefsEditor = myPrefs.edit();

						prefsEditor.putString(
								Constants.SETTINGS_LANGUAGE_INDEX_PREFFER,
								languages.get(arg2).getAltLangID());
						QuestionnaireActivity.langid = result.get(arg2)
								.getAltLangID();
						prefsEditor.commit();
					}
				} catch (Exception ex) {

				}
				dialog.dismiss();

			}
		});

		dialog.findViewById(R.id.btnOk).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		android.view.WindowManager.LayoutParams params = dialog.getWindow()
				.getAttributes();
		params.height = LayoutParams.FILL_PARENT;

		dialog.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		dialog.show();

	}

	private void showLanguageDialog() {
		final ArrayList<AltLanguage> languages = DBHelper.getLanguages(true);

		QuestionnaireActivity.langid = null;
		QuestionnaireActivity.langs = null;
		if (languages != null && languages.size() > 0) {
			// AltLanguage thisLang = new AltLanguage();
			// thisLang.setAltLangName("Select an alternative language");
			// languages.add(0, thisLang);
			// //////////////////////////////////////////////
			AltLanguage thisLang = new AltLanguage();
			thisLang.setAltLangName("Default language");
			languages.add(0, thisLang);
			// langs[0] = "Select an alternative language";
			// langs[1] = "Default language";
			// for (int i = 0; i < languages.size(); i++) {
			// langs[i + 2] = languages.get(i).getAltLangName();
			// }

			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);

			String selectedLanGID = myPrefs.getString(
					Constants.SETTINGS_LANGUAGE_INDEX_PREFFER, null);
			QuestionnaireActivity.langid = selectedLanGID;
			QuestionnaireActivity.langs = languages;

			customAlertforLanguages(JobDetailActivity.this, languages,
					selectedLanGID);

		}

	}

	int language = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comunicator.detailJob = JobDetailActivity.this;
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		language = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);
		Constants.setLocale(JobDetailActivity.this);
		if (myPrefs.contains("ispaused")
				&& myPrefs.getBoolean("ispaused", false)) {
			OrderID = myPrefs.getString("order_id", "");
			if (OrderID.contains("-")) {
				Intent intent = new Intent(this.getApplicationContext(),
						QuestionnaireActivity.class);
				intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
						myPrefs.getString("order_id", ""));
				intent.putExtra(Constants.FIELD_ORDER_SET_ID,
						myPrefs.getString("setid", ""));
				comunicator.detailJob = null;
				startActivity(intent);
			} else {
				Intent intent = new Intent(this.getApplicationContext(),
						QuestionnaireActivity.class);
				intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
						myPrefs.getString("order_id", ""));
				intent.putExtra(Constants.FIELD_ORDER_SET_ID,
						myPrefs.getString("setid", ""));
				comunicator.detailJob = null;
				startActivity(intent);
			}
			return;
		}
		if (Constants.getLoginURL()!=null && Constants.getLoginURL().toLowerCase().contains("ajis"))
			setContentView(R.layout.job_detail_ajis);
		else setContentView(R.layout.job_detail_hi);
		TextView textView = (TextView) findViewById(R.id.jd_heder);
		textView.setTextSize(UIHelper.getFontSize(JobDetailActivity.this,
				textView.getTextSize()));

		alBtn = (ImageView) findViewById(R.id.jd_alt_btn);
		rejectbtnImg = (ImageView) findViewById(R.id.rejectbtn);
		initGoogleApiClient();
		final ArrayList<AltLanguage> languages = DBHelper.getLanguages(true);

		QuestionnaireActivity.langid = null;
		QuestionnaireActivity.langs = null;
		spinner = (Spinner) findViewById(R.id.languagelist);
		if (languages != null && languages.size() > 0) {
			String[] langs = new String[languages.size() + 2];
			langs[0] = "Select an alternative language";
			langs[1] = "Default language";
			for (int i = 0; i < languages.size(); i++) {
				langs[i + 2] = languages.get(i).getAltLangName();
			}

			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);

			String selectedLanGID = myPrefs.getString(
					Constants.SETTINGS_LANGUAGE_INDEX_PREFFER, null);
			QuestionnaireActivity.langid = selectedLanGID;
			QuestionnaireActivity.langs = languages;
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					try {

						if (arg2 == 0) {
							QuestionnaireActivity.langid = null;

							SharedPreferences myPrefs = getSharedPreferences(
									"pref", MODE_PRIVATE);
							SharedPreferences.Editor prefsEditor = myPrefs
									.edit();

							prefsEditor.putString(
									Constants.SETTINGS_LANGUAGE_INDEX_PREFFER,
									null);
							prefsEditor.commit();
						} else if (arg2 == 1) {
							QuestionnaireActivity.langid = "-1";

							SharedPreferences myPrefs = getSharedPreferences(
									"pref", MODE_PRIVATE);
							SharedPreferences.Editor prefsEditor = myPrefs
									.edit();

							prefsEditor.putString(
									Constants.SETTINGS_LANGUAGE_INDEX_PREFFER,
									"-1");
							prefsEditor.commit();
						} else {
							SharedPreferences myPrefs = getSharedPreferences(
									"pref", MODE_PRIVATE);
							SharedPreferences.Editor prefsEditor = myPrefs
									.edit();

							prefsEditor.putString(
									Constants.SETTINGS_LANGUAGE_INDEX_PREFFER,
									languages.get(arg2 - 2).getAltLangID());
							QuestionnaireActivity.langid = languages.get(
									arg2 - 2).getAltLangID();
							prefsEditor.commit();
						}
					} catch (Exception ex) {
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}
			});

			ArrayAdapter adapter = new ArrayAdapter(this,
					UIHelper.getSpinnerLayoutSize(JobDetailActivity.this,
							modeSelect), langs);
			adapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
					JobDetailActivity.this, modeSelect));
			spinner.setAdapter(adapter);

			spinner.setSelection(0);
			if (QuestionnaireActivity.langid != null
					&& QuestionnaireActivity.langid.equals("-1")) {
				spinner.setSelection(1);
			}
			for (int i = 0; i < languages.size(); i++) {
				if (selectedLanGID != null
						&& languages.get(i).getAltLangID()
								.equals(selectedLanGID)) {
					spinner.setSelection(i + 2);
				}
			}
		} else {
			spinner.setVisibility(RelativeLayout.GONE);
			alBtn.setVisibility(RelativeLayout.INVISIBLE);
		}
		spinner.setVisibility(RelativeLayout.GONE);

		tv_header = (TextView) findViewById(R.id.jd_heder);

		initialiseValueFieldText();
		setOrder();
		setInvertDisplay();
		Bundle b = getIntent().getExtras();

		if (b == null)
			return;

		OrderID = b.getString("OrderID");
		if (b.containsKey(Constants.POST_FIELD_IS_ARCHIVE)) {
			Intent intent = new Intent(this.getApplicationContext(),
					QuestionnaireActivity.class);
			intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
					OrderID);
			intent.putExtra(Constants.FIELD_ORDER_SET_ID,
					b.getString(Constants.POST_FIELD_IS_ARCHIVE));
			intent.putExtra(Constants.POST_FIELD_IS_ARCHIVE,
					b.getString(Constants.POST_FIELD_IS_ARCHIVE));
			startActivity(intent);
		}
		if (CheckerApp.getQuestionResult() != null) {

			onQuestionResult(CheckerApp.getQuestionResult());
		}
		if (OrderID == null) {
			Toast.makeText(
					JobDetailActivity.this,
					"Error retrieving information about the Order, please try again later!",
					Toast.LENGTH_SHORT).show();
			finish();

		}
		if (OrderID.contains("CC")) {
			showBriefing();
		}

		SharedPreferences myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString(Constants.Crash_Last_ORDERID, OrderID);
		prefsEditor.commit();

		this.groupedNumber = b
				.getString(Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER);

		String SurveyID = b.getString("SurveyID");
		if (OrderID.contains("-")) {
			getthisOrderFromListView(OrderID);
			if (OrderID.contains("-")) {
				setSurveyData(OrderID.replace("-", ""));
			} else {
				myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

				String userName = myPrefs.getString(
						Constants.POST_FIELD_LOGIN_USERNAME, "");
				// locationThread lThread = new locationThread();
				// lThread.startLocationThread(getApplicationContext(),
				// userName);
				// JobListActivity.joborders.get(localindex).orderItem
				// .setStatusName("Scheduled");
				BeginReview(false);
			}
		}
		acceptbtnImg = (ImageView) findViewById(R.id.acceptbtn);
		separator1 = (View) findViewById(R.id.separator1);
		separator2 = (View) findViewById(R.id.separator2);
		separator3 = (View) findViewById(R.id.separator3);
		separator4 = (View) findViewById(R.id.separator4);
		separator5 = (View) findViewById(R.id.separator5);
		separator6 = (View) findViewById(R.id.separator6);
		separator7 = (View) findViewById(R.id.separator7);
		separator8 = (View) findViewById(R.id.separator8);
		separator9 = (View) findViewById(R.id.separator9);
		separator10 = (View) findViewById(R.id.separator10);
		separator11 = (View) findViewById(R.id.separator11);
		separator12 = (View) findViewById(R.id.separator12);
		separator13 = (View) findViewById(R.id.separator13);
		separator14 = (View) findViewById(R.id.separator14);
		separator15 = (View) findViewById(R.id.separator15);
		separator16 = (View) findViewById(R.id.separator16);
		separator17 = (View) findViewById(R.id.separator17);
		separator18 = (View) findViewById(R.id.separator18);
		separator19 = (View) findViewById(R.id.separator19);

		sBonusPayment_t = (TextView) findViewById(R.id.sBonusPayment_t);
		sCriticismPayment_t = (TextView) findViewById(R.id.sCriticismPayment_t);
		sTransportationPayment_t = (TextView) findViewById(R.id.sTransportationPayment_t);
		sNonRefundableServicePayment_t = (TextView) findViewById(R.id.sNonRefundableServicePayment_t);
		jdcanpurchase_t = (TextView) findViewById(R.id.jdcanpurchase_t);
		jdpurchasedes_t = (TextView) findViewById(R.id.jdpurchasedes_t);
		jdtimeet = (TextView) findViewById(R.id.jdtimeet);
		jdtimest = (TextView) findViewById(R.id.jdtimest);
		jdopeninght = (TextView) findViewById(R.id.jdopeninght);
		jdbranchpt = (TextView) findViewById(R.id.jdbranchpt);
		jdaddt = (TextView) findViewById(R.id.jdaddt);
		jdcitynt = (TextView) findViewById(R.id.jdcitynt);
		jdbranchfnamet = (TextView) findViewById(R.id.jdbranchfnamet);
		jdbranchname = (TextView) findViewById(R.id.jdbranchname);
		jdclientt = (TextView) findViewById(R.id.jdclientt);
		jdsetnamet = (TextView) findViewById(R.id.jdsetnamet);
		jddescriptiont = (TextView) findViewById(R.id.jddescriptiont);
		jdstaust = (TextView) findViewById(R.id.jdstaust);
		jddatet = (TextView) findViewById(R.id.jddatet);

		loadViews();
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		SplashScreen.sendCrashReport(myPrefs,JobDetailActivity.this);
	}

	private void getthisOrderFromListView(String orderID2) {
		try {
			jobOrder = DBHelper.getOrder(Constants.DB_TABLE_JOBLIST, new String[]{
							Constants.DB_TABLE_JOBLIST_ORDERID,
							Constants.DB_TABLE_JOBLIST_DATE, Constants.DB_TABLE_JOBLIST_SN,
							Constants.DB_TABLE_JOBLIST_DESC,
							Constants.DB_TABLE_JOBLIST_SETNAME,
							Constants.DB_TABLE_JOBLIST_SETLINK,
							Constants.DB_TABLE_JOBLIST_CN, Constants.DB_TABLE_JOBLIST_BFN,
							Constants.DB_TABLE_JOBLIST_BN,
							Constants.DB_TABLE_JOBLIST_CITYNAME,
							Constants.DB_TABLE_JOBLIST_ADDRESS,
							Constants.DB_TABLE_JOBLIST_BP, Constants.DB_TABLE_JOBLIST_OH,
							Constants.DB_TABLE_JOBLIST_TS, Constants.DB_TABLE_JOBLIST_TE,
							Constants.DB_TABLE_JOBLIST_SETID,
							Constants.DB_TABLE_JOBLIST_BL, Constants.DB_TABLE_JOBLIST_BLNG,
							Constants.DB_TABLE_JOBLIST_FN, Constants.DB_TABLE_JOBLIST_JC,
							Constants.DB_TABLE_JOBLIST_JI,
							Constants.DB_TABLE_JOBLIST_BLINK,
							Constants.DB_TABLE_JOBLIST_MID,
							Constants.DB_TABLE_CHECKER_CODE,
							Constants.DB_TABLE_CHECKER_LINK,
							Constants.DB_TABLE_BRANCH_CODE, Constants.DB_TABLE_SETCODE,
							Constants.DB_TABLE_PURCHASE_DESCRIPTION,
							Constants.DB_TABLE_PURCHASE,
							Constants.DB_TABLE_JOBLIST_BRIEFING,
							Constants.DB_TABLE_JOBLIST_sPurchaseLimit,
							Constants.DB_TABLE_JOBLIST_sNonRefundableServicePayment,
							Constants.DB_TABLE_JOBLIST_sTransportationPayment,
							Constants.DB_TABLE_JOBLIST_sCriticismPayment,
							Constants.DB_TABLE_JOBLIST_sBonusPayment,},
					Constants.DB_TABLE_JOBLIST_ORDERID + "='" + OrderID + "'");

			if (jobOrder != null) {
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(JobDetailActivity.this,"Issue while retrieving data, please re-login and try!",Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private void setOrder() {
		Bundle b = getIntent().getExtras();

		if (b == null)
			return;

		String OrderID = b.getString("OrderID");
		String SurveyID = b.getString("SurveyID");

		if (SurveyID != null && !SurveyID.equals("")
				&& Surveys.getSets() != null) {
			for (int i = 0; i < Surveys.getSets().size(); i++) {
				survey = Surveys.getSets().get(i);
				order = null;
				if (survey.getSurveyID().equals(SurveyID))
					break;
			}
			// order = Orders.getOrders().get(index);
			if (survey != null)
				setValueFieldText(survey);
			isBriefing = false;
		} else {
			for (int i = 0; i < Orders.getOrders().size(); i++) {
				order = Orders.getOrders().get(i);
				if (order!=null && order.getOrderID().equals(OrderID))
					break;
			}
			// order = Orders.getOrders().get(index);
			if (order != null) {
				isSurvey = OrderID.contains("-");
				setValueFieldText(isSurvey);

				if (order.getBriefingContent() != null
						&& !order.getBriefingContent().equals("")) {
					isBriefing = true;
				}

			}
		}

		if (order != null) {
			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putString(Constants.Crash_Last_SETID, order.getSetID());
			prefsEditor.commit();
			set = getSetsRecords(order.getSetID());
			if (spinner != null
					&& set != null
					&& set.getAllowCheckerToSetLang() != null
					&& (set.getAllowCheckerToSetLang().equals("1") || (set
							.getListObjects() != null
							&& set.getListObjects().size() > 0
							&& set.getListObjects().get(0) != null
							&& set.getListObjects().get(0).altTexts != null && set
							.getListObjects().get(0).altTexts.size() > 0))) {

			} else if (spinner != null)
				spinner.setVisibility(RelativeLayout.GONE);

		}
		// Set set = getSetsRecords(order.getSetID());
		// int count = set.getListObjects().size();
		// Log.v("Count: ", ""+count);

	}

	private void setInvertDisplay() {
		if (Helper.getTheme(JobDetailActivity.this) == 0) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.jdbottomrv);
			layout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.navigation_bar_dark));

			layout = (RelativeLayout) findViewById(R.id.rvjobdetailscreen);
			layout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.navigation_bar_dark));

			layout = (RelativeLayout) findViewById(R.id.backGroundDrawable);
			layout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.background_dark));
		}
	}

	private void setFontSize(View v) {
		try {
			TextView textView = (TextView) v;
			textView.setTextSize(UIHelper.getFontSize(JobDetailActivity.this,
					textView.getTextSize()));
			if (Helper.getTheme(JobDetailActivity.this) == 0) {
				textView.setTextColor(getResources().getColor(
						android.R.color.white));
			}
		} catch (Exception ex) {

		}

		try {
			Button btnView = (Button) v;
			btnView.setTextSize(UIHelper.getFontSize(JobDetailActivity.this,
					btnView.getTextSize()));
			btnView.setTextColor(getResources().getColor(android.R.color.black));

		} catch (Exception ex) {

		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
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

	private void setSurveyData(String surveyId) {
		ScrollView layout = (ScrollView) findViewById(R.id.jdmiddle);
		TextView tvjdsurvey_name = (TextView) findViewById(R.id.jdsurvey_name);
		TextView tvjdbranchv = (TextView) findViewById(R.id.jdbranchv);
		TextView tvjdbrancht = (TextView) findViewById(R.id.jdbranch);

		ListView lvjdsurvey_quotas_list = (ListView) findViewById(R.id.jdsurvey_quotas_list);
		ListView lvjdsurvey_allocations_list = (ListView) findViewById(R.id.jdsurvey_allocations_list);
		survey = Surveys.getCurrentSurve(surveyId);

		if (Helper.getTheme(JobDetailActivity.this) == 0) {
			layout.setVisibility(RelativeLayout.GONE);
			lvjdsurvey_quotas_list.setVisibility(RelativeLayout.GONE);
			lvjdsurvey_allocations_list.setVisibility(RelativeLayout.GONE);
			lvjdsurvey_quotas_list = (ListView) findViewById(R.id.jdsurvey_quotas_list_night);
			lvjdsurvey_allocations_list = (ListView) findViewById(R.id.jdsurvey_allocations_list_night);
			lvjdsurvey_quotas_list.setVisibility(RelativeLayout.VISIBLE);
			lvjdsurvey_allocations_list.setVisibility(RelativeLayout.VISIBLE);
			tvjdsurvey_name.setVisibility(RelativeLayout.GONE);
			tvjdsurvey_name.setTextColor(android.R.color.white);
			tvjdsurvey_name = (TextView) findViewById(R.id.jdsurvey_name_night);
			TextView tvjdbranchvn = (TextView) findViewById(R.id.jdbranchv_night);
			// tvjdbranchvn.setTextColor(android.R.color.white);
			TextView tvjdbranchtn = (TextView) findViewById(R.id.jdbranch_night);
			// tvjdbranchtn.setTextColor(android.R.color.white);
			if (survey!=null && survey.getBranchFullName() != null
					&& !survey.getBranchFullName().equals("")) {
				tvjdbranchvn.setText(survey.getBranchFullName());
				tvjdbranchtn.setVisibility(RelativeLayout.VISIBLE);
				tvjdbranchvn.setVisibility(RelativeLayout.VISIBLE);
				tvjdbranchtn.setText(getResources().getString(
						R.string.jd_branchfname));
			} else {
				tvjdbranchtn.setVisibility(RelativeLayout.GONE);
				tvjdbranchvn.setVisibility(RelativeLayout.GONE);
			}
			if (jobOrder != null && jobOrder.getBranchLat() != null
					&& !jobOrder.getBranchLat().equals("")) {
				tvjdbranchvn.setText(jobOrder.getBranchLat());
				tvjdbranchtn.setVisibility(RelativeLayout.VISIBLE);
				tvjdbranchvn.setVisibility(RelativeLayout.VISIBLE);

				tvjdbranchtn.setText(getResources().getString(
						R.string.jd_branchname));
			}

			tvjdsurvey_name.setVisibility(RelativeLayout.VISIBLE);
			layout = (ScrollView) findViewById(R.id.jdmiddle_night);
			layout.setVisibility(RelativeLayout.VISIBLE);

		}

		dataView.setVisibility(RelativeLayout.GONE);
		calendarBtn.setVisibility(RelativeLayout.GONE);
		mapbtn.setVisibility(RelativeLayout.GONE);
		layout.setVisibility(RelativeLayout.VISIBLE);
		rejectbtn.setText(getResources().getString(R.string.button_back));
		if ((order!=null && order.getStatusName()!=null && order.getStatusName().toLowerCase().contains("archive")) || (order!=null && order.getAsArchive()))
		{
			rejectbtn
					.setText(getResources().getString(R.string.button_back_archive));

		}

		rejectbtnImg.setImageResource(R.drawable.btn_back);
		Helper.changeImageViewSrc(rejectbtnImg, getApplicationContext());

		if (survey == null) {
			Toast.makeText(JobDetailActivity.this,
					"Error opening this survey!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		if (survey!=null && survey.isAllocationReached()
				&& !(jobOrder != null && jobOrder.getStatusName() != null && (jobOrder
						.getStatusName().contains("Completed") || jobOrder
				.getStatusName().contains("rchived") || jobOrder
						.getStatusName().contains("progress"))))
			finish();

		if (survey!=null && survey.getBranchFullName() != null
				&& !survey.getBranchFullName().equals("")) {
			tvjdbranchv.setText(survey.getBranchFullName());
			tvjdbrancht.setVisibility(RelativeLayout.VISIBLE);
			tvjdbranchv.setVisibility(RelativeLayout.VISIBLE);
			tvjdbrancht.setText(getResources().getString(
					R.string.jd_branchfname));
		} else {
			tvjdbrancht.setVisibility(RelativeLayout.GONE);
			tvjdbranchv.setVisibility(RelativeLayout.GONE);
		}
		if (jobOrder != null && jobOrder.getBranchLat() != null
				&& !jobOrder.getBranchLat().equals("")) {
			tvjdbranchv.setText(jobOrder.getBranchLat());
			tvjdbrancht.setVisibility(RelativeLayout.VISIBLE);
			tvjdbranchv.setVisibility(RelativeLayout.VISIBLE);

			tvjdbrancht.setText(getResources()
					.getString(R.string.jd_branchname));
		}

		tvjdsurvey_name.setText(survey.getSurveyName());

		if (survey!=null && survey.getArrayQuotas() != null) {
			String str[] = survey.getArrayQuotas();

			ArrayAdapter adapter = new ArrayAdapter(this,
					UIHelper.getListLayoutSize(JobDetailActivity.this), str);
			lvjdsurvey_quotas_list.setAdapter(adapter);

			setListViewHeightBasedOnChildren(lvjdsurvey_quotas_list);
		}

		if (survey!=null && survey.getArrayAllocations() != null) {

			String str[] = survey.getArrayAllocations();

			ArrayAdapter adapter = new ArrayAdapter(this,
					UIHelper.getListLayoutSize(JobDetailActivity.this), str);
			lvjdsurvey_allocations_list.setAdapter(adapter);
			setListViewHeightBasedOnChildren(lvjdsurvey_allocations_list);
		}
	}

	/****** Start *******/
	private void initialiseValueFieldText() {
		setFontSize(findViewById(R.id.jdsurvey));
		setFontSize(findViewById(R.id.jdbranchpt));
		setFontSize(findViewById(R.id.jdbranchpv));
		setFontSize(findViewById(R.id.jdbranchv));
		setFontSize(findViewById(R.id.jdbranch));
		setFontSize(findViewById(R.id.jdsurvey_quotas));
		// setFontSize(findViewById(R.id.jdsurvey_name));
		setFontSize(findViewById(R.id.jdsurvey_allocations));

		setFontSize(findViewById(R.id.jd_heder));

		setFontSize(findViewById(R.id.jdaddt));
		setFontSize(findViewById(R.id.jdaddv));
		setFontSize(findViewById(R.id.jdbottomrv));
		setFontSize(findViewById(R.id.jdbranch_name_val));
		setFontSize(findViewById(R.id.jdbranchfnamet));
		setFontSize(findViewById(R.id.jdbranchname));
		setFontSize(findViewById(R.id.jdbranchpt));
		setFontSize(findViewById(R.id.jdbranchpv));
		setFontSize(findViewById(R.id.jdcitynt));
		setFontSize(findViewById(R.id.jdcitynv));
		setFontSize(findViewById(R.id.jdclientt));
		setFontSize(findViewById(R.id.jddatet));
		setFontSize(findViewById(R.id.jddatev));
		setFontSize(findViewById(R.id.jddescriptiont));
		setFontSize(findViewById(R.id.jdbranch_name_val));
		setFontSize(findViewById(R.id.jdopeninght));
		setFontSize(findViewById(R.id.jdsetnamet));
		setFontSize(findViewById(R.id.jdstaust));
		setFontSize(findViewById(R.id.jdtimeet));
		setFontSize(findViewById(R.id.jdtimest));
		setFontSize(findViewById(R.id.jdpurchasedes_t));
		setFontSize(findViewById(R.id.jdpurchasedes_v));
		setFontSize(findViewById(R.id.jdcanpurchase_t));
		setFontSize(findViewById(R.id.jdcanpurchase_v));
		setFontSize(findViewById(R.id.sBonusPayment_t));
		setFontSize(findViewById(R.id.sBonusPayment_v));
		setFontSize(findViewById(R.id.sCriticismPayment_t));
		setFontSize(findViewById(R.id.sCriticismPayment_v));
		setFontSize(findViewById(R.id.sNonRefundableServicePayment_t));
		setFontSize(findViewById(R.id.sNonRefundableServicePayment_v));
		setFontSize(findViewById(R.id.sTransportationPayment_t));
		setFontSize(findViewById(R.id.sTransportationPayment_v));

		findViewById(R.id.reject_layout).setOnClickListener(this);
		findViewById(R.id.accept_layout).setOnClickListener(this);
		findViewById(R.id.accepttxt).setOnClickListener(this);
		findViewById(R.id.rejecttxt).setOnClickListener(this);
		findViewById(R.id.rejectbtn).setOnClickListener(this);
		findViewById(R.id.acceptbtn).setOnClickListener(this);

		aceeptbtn = (TextView) findViewById(R.id.accepttxt);
		rejectbtn = (TextView) findViewById(R.id.rejecttxt);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		// if (Constants.SETTINGS_LOCALE_VAL_ARR[myPrefs.getInt(
		// Constants.SETTINGS_LANGUAGE_INDEX, 0)].equals("fr")) {
		// } else {
		// setFontSize(aceeptbtn);
		//
		// setFontSize(rejectbtn);
		// }
		// alBtn.setVisibility(RelativeLayout.GONE);
		mapbtn = (ImageView) findViewById(R.id.jdmapbtn);
		calendarBtn = (ImageView) findViewById(R.id.jdcalendarbtn);
		calendarBtn.setVisibility(RelativeLayout.GONE);
		// setFontSize(mapbtn);
		if (calendarBtn != null)
			calendarBtn.setOnClickListener(this);
		if (aceeptbtn != null)
			aceeptbtn.setOnClickListener(this);
		if (rejectbtn != null)
			rejectbtn.setOnClickListener(this);
		if (alBtn != null)
			alBtn.setOnClickListener(this);
		if (mapbtn != null)
			mapbtn.setOnClickListener(this);
		date_v = (TextView) findViewById(R.id.jddatev);
		setFontSize(date_v);
		status_v = (TextView) findViewById(R.id.jdstausv);
		setFontSize(status_v);
		description_v = (TextView) findViewById(R.id.jddescriptionv);
		setFontSize(description_v);
		setname_v = (TextView) findViewById(R.id.jdsetnamev);
		setFontSize(setname_v);
		cname_v = (TextView) findViewById(R.id.jdclientv);
		setFontSize(cname_v);
		bname = (TextView) findViewById(R.id.jdbranch_name_val);
		setFontSize(bname);
		bname_v = (TextView) findViewById(R.id.jdbranchfnamev);
		setFontSize(bname_v);
		cityname_v = (TextView) findViewById(R.id.jdcitynv);
		setFontSize(cityname_v);
		address_v = (TextView) findViewById(R.id.jdaddv);
		setFontSize(address_v);
		branchphone_v = (TextView) findViewById(R.id.jdbranchpv);
		setFontSize(branchphone_v);
		openhours_v = (TextView) findViewById(R.id.jdopeninghv);
		setFontSize(openhours_v);
		times_v = (TextView) findViewById(R.id.jdtimesv);
		setFontSize(times_v);
		timee_v = (TextView) findViewById(R.id.jdtimeev);
		setFontSize(timee_v);
		tv_purchase_desc = (TextView) findViewById(R.id.jdpurchasedes_v);
		setFontSize(timee_v);
		tv_make_purchase = (TextView) findViewById(R.id.jdcanpurchase_v);
		setFontSize(timee_v);
		tvsNonRefundableServicePayment = (TextView) findViewById(R.id.sNonRefundableServicePayment_v);
		tvsTransportationPayment = (TextView) findViewById(R.id.sTransportationPayment_v);
		tvsCriticismPayment = (TextView) findViewById(R.id.sCriticismPayment_v);
		tvsBonusPayment = (TextView) findViewById(R.id.sBonusPayment_v);

		tv = (TextView) findViewById(R.id.darktview);
		webview = (WebView) findViewById(R.id.briefingView);
		dataView = (ScrollView) findViewById(R.id.dataView);
		hideBriefing();
		// if (Helper.getTheme(JobDetailActivity.this) == 1)
		// tv.setVisibility(View.VISIBLE);
	}

	private void setValueFieldText(Survey survey2) {

	}

	private void setValueFieldText(Boolean isSurvey) {
		dataSaved = false;
		Date d = null;
		String str = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (order != null && order.getDate() != null)
				d = sdf.parse(order.getDate());
			DateFormat dateFormat = android.text.format.DateFormat
					.getDateFormat(getApplicationContext());
			str = dateFormat.format(d);
			// date_v.setText(str);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date_v = getTextFromHtmlFormate(str, date_v);
		status_v = getTextFromHtmlFormate(order.getStatusName(), status_v);
		description_v = getTextFromHtmlFormate(order.getDescription(),
				description_v);
		setname_v = getTextFromHtmlFormate(order.getSetName(), setname_v);
		cname_v = getTextFromHtmlFormate(order.getClientName(), cname_v);
		bname = getTextFromHtmlFormate(order.getBranchName(), bname);
		bname_v = getTextFromHtmlFormate(order.getBranchFullname(), bname_v);
		cityname_v = getTextFromHtmlFormate(order.getCityName(), cityname_v);
		address_v = getTextFromHtmlFormate(order.getAddress(), address_v);
		branchphone_v = getTextFromHtmlFormate(order.getBranchPhone(),
				branchphone_v);
		openhours_v = getTextFromHtmlFormate(order.getOpeningHours(),
				openhours_v);
		times_v = getTextFromHtmlFormate(order.getTimeStart(), times_v);
		timee_v = getTextFromHtmlFormate(order.getTimeEnd(), timee_v);
		if ((order != null && order.getsPurchase() != null && order
				.getsPurchase().equals("1"))) {
			tv_make_purchase = getTextFromHtmlFormate(
					JobDetailActivity.this
							.getString(R.string.questionnaire_exit_delete_alert_yes),
					tv_make_purchase);
		} else {
			tv_make_purchase = getTextFromHtmlFormate(
					JobDetailActivity.this
							.getString(R.string.questionnaire_exit_delete_alert_no),
					tv_make_purchase);
		}

		tv_purchase_desc = getTextFromHtmlFormate(
				order.getsPurchaseDescription(), tv_purchase_desc);

		tvsNonRefundableServicePayment = getTextFromHtmlFormate(
				order.getsNonRefundableServicePayment(),
				tvsNonRefundableServicePayment);
		tvsTransportationPayment = getTextFromHtmlFormate(
				order.getsTransportationPayment(), tvsTransportationPayment);
		tvsCriticismPayment = getTextFromHtmlFormate(
				order.getsCriticismPayment(), tvsCriticismPayment);
		tvsBonusPayment = getTextFromHtmlFormate(order.getsBonusPayment(),
				tvsBonusPayment);

		// getTextFromHtmlFormate
		// status_v.setText(order.getStatusName());
		// description_v.setText(order.getDescription());
		// setname_v.setText(order.getSetName());
		// cname_v.setText(order.getClientName());
		// bname.setText(order.getBranchName());
		// bname_v.setText(order.getBranchFullname());
		// cityname_v.setText(order.getCityName());
		// address_v.setText(order.getAddress());
		// branchphone_v.setText(order.getBranchPhone());
		// openhours_v.setText(order.getOpeningHours());
		// times_v.setText(order.getTimeStart());
		// timee_v.setText(order.getTimeEnd());\
		statusname = order.getStatusName();
		setButtonText(order.getStatusName(), isSurvey);

		if (!isSurvey && order != null) {
			customFields = DBHelper.getCustomFields(order.getOrderID());
		}
		if (customFields != null) {
			TableLayout listCustomFields = (TableLayout) findViewById(R.id.tblView);
			int childcount = listCustomFields.getChildCount();
			for (int i = 0; i < customFields.size(); i++) {
				View thisView = getCustomFiledView(i, null, null);
				listCustomFields.addView(thisView, childcount + i);
			}
			// listCustomFields.setAdapter(adapter);

			// setListViewHeightBasedOnChildren(listCustomFields);

		}
	}

	public View getCustomFiledView(final int position, View convertView,
			ViewGroup parent) {

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		int language = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 0);

		LayoutInflater inflater = (LayoutInflater) JobDetailActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.custom_field_custom_row,
				parent, false);

		if (language == 3)
			rowView = inflater.inflate(R.layout.custom_field_custom_row_hi,
					parent, false);
		TextView tvLeft = (TextView) rowView.findViewById(R.id.tvleft);
		Helper.changeTxtViewColor(tvLeft);
		tvLeft.setTextSize(UIHelper.getFontSize(JobDetailActivity.this,
				tvLeft.getTextSize()));
		View separator = (View) rowView.findViewById(R.id.separator);
		Helper.changeViewColor(separator);
		TextView tvRight = (TextView) rowView.findViewById(R.id.tvright);
		tvRight.setTextSize(UIHelper.getFontSize(JobDetailActivity.this,
				tvRight.getTextSize()));

		{
			tvLeft.setText(customFields.get(position).getName());
			tvRight.setText(customFields.get(position).getValue());
		}
		return rowView;
	}

	private String[] convertCustomFieldToArray(
			ArrayList<CustomFields> customFields2) {

		String[] arrayCF = new String[customFields2.size()];
		for (int i = 0; i < customFields2.size(); i++) {
			// if (customFields2.get(i).getValue() != null
			// && !customFields2.get(i).getValue().equals(""))
			arrayCF[i] = customFields2.get(i).getName() + " - "
					+ customFields2.get(i).getValue();
		}
		return arrayCF;
	}

	private TextView getTextFromHtmlFormate(String html, TextView tv) {
		if (html == null)
			html = "";
		Spanned sp = Html.fromHtml(html);
		if (tv != null && sp != null)
			tv.setText(sp, BufferType.SPANNABLE);
		return tv;
	}

	private void setButtonText(String statusname, Boolean isSurvey) {
		if (isSurvey)
			aceeptbtn.setText(getString(R.string.jd_begin_btn_text));
		else if (statusname.equals("Assigned"))
			aceeptbtn.setText(getString(R.string.jd_accept_btn_text));
		else if (statusname.equals("Scheduled") || statusname.equals("cert"))
			aceeptbtn.setText(getString(R.string.jd_begin_review_btn_text));
		else if (statusname.equals("in progress") || statusname.equals("In progress") || statusname.toLowerCase().equals("archived"))// (getString(R.string.jd_begin_button_status_inprogress)))
			aceeptbtn.setText(getString(R.string.jd_continue_review_btn_text));
		else if (statusname.equals("Completed"))// (getString(R.string.jd_begin_button_status_completed)))
		{
			// rejectbtn.setText(getString(R.string.jd_synch_button_text));
			aceeptbtn.setText(getString(R.string.jd_continue_review_btn_text));
		}


		if ((order!=null && order.getStatusName()!=null && order.getStatusName().toLowerCase().contains("archive")) || (order!=null && order.getAsArchive()))
		{
			rejectbtn
					.setText(getResources().getString(R.string.button_back_archive));
			rejectbtnImg.setImageResource(R.drawable.btn_back);

		}


	}

	public int IsInternetConnectted() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		conMgr = null;
		if (i == null)
			return 0;
		return -1;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.clear();
		if (Constants.isQAAllowed)
			menu.add(0, Constants.MENUID_BUG, 0,
					getString(R.string.questionnaire_send_bug));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case Constants.MENUID_BUG:
			try {
				// final ParseObject gameScore = new ParseObject("BugReport");
				//
				// if (set != null)
				// gameScore.put("SetId", set.getSetID());
				// if (order != null)
				// gameScore.put("OrderId", order.getOrderID());
				// gameScore.put("screenshot", Helper
				// .TakeScreenShot(findViewById(android.R.id.content)));
				// Helper.customCrashAlert(JobDetailActivity.this,
				// "Please enter comment for this bug:", gameScore);

			} catch (Exception ex) {
			}

			return true;
		}
		JobDetailActivity.this.startActivity(new Intent(JobDetailActivity.this,
				LoginActivity.class));
		JobDetailActivity.this.finish();
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.accepttxt:
		case R.id.accept_layout:
		case R.id.acceptbtn:

            if (aceeptbtn.getText().toString()
					.equals(getString(R.string.jd_begin_btn_text))) {
				if (isSurvey) {
					if (statusname.equals("survey")) {

						if (survey!=null && survey.isAllocationReached()) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									this);
							builder.setMessage(
									getResources()
											.getString(
													R.string.questionnaire_open_survey_alert))
									.setTitle(
											getResources().getString(
													R.string._alert_title))
									.setCancelable(false)
									.setPositiveButton(
											getResources().getString(
													R.string.button_ok),
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
													finish();
												}
											});
							AlertDialog alert = builder.create();
							alert.show();
						} else {
							SplashScreen.addLog(new BasicLog(
									myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
									myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Starting survey!"+order.getSetName()+"status:"+order.getStatusName(),order.getOrderID()));

							startLocationChecker();
						}

					} else {
						SplashScreen.addLog(new BasicLog(
								myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
								myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Starting survey!"+order.getSetName()+"status:"+order.getStatusName(),order.getOrderID()));

						startLocationChecker();
					}
				}
			}
			if (aceeptbtn.getText().toString()
					.equals(getString(R.string.jd_accept_btn_text))) {
				// if(!Helper.isParsed()){
				// ShowAlert(JobDetailActivity.this,
				// getString(R.string.jd_parsing_alert_title),
				// getString(R.string.jd_parsing_alert_msg),
				// getString(R.string.alert_btn_lbl_ok));
				// return;
				// }

				// if(Sets.getSets() == null || Sets.getSets().isEmpty())
				// {
				// ShowAlert(JobDetailActivity.this,
				// getString(R.string.jd_parsing_alert_title),
				// getString(R.string.before_accept_alert),
				// getString(R.string.alert_btn_lbl_ok));
				// return;
				// }
				int ins = IsInternetConnectted();
				if (ins >= 0) {
					ShowAlert(JobDetailActivity.this,
							getString(R.string.jd_parsing_alert_title),
							getString(R.string.accept_job_fail_alert),
							getString(R.string.alert_btn_lbl_ok));
					return;
				}
				new JobTask().execute(getString(R.string.jd_accept_btn_text),
						"");
				// aceeptbtn.setBackgroundResource(R.drawable.acceptbtn_n);
			} else if (aceeptbtn.getText().toString()
					.equals(getString(R.string.jd_begin_review_btn_text))) {
				if (this.isBriefing == true) {
					isBriefing = false;
					showBriefing();
				} else {
					SplashScreen.addLog(new BasicLog(
							myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
							myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Starting Order!"+order.getOrderID()+" = "+order.getSetName()+"status:"+order.getStatusName(),order.getOrderID()));

					startLocationChecker();
				}

			} else if (aceeptbtn.getText().toString()
					.equals(getString(R.string.jd_continue_review_btn_text))) {
				SplashScreen.addLog(new BasicLog(
						myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
						myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Starting Order!"+order.getOrderID()+" = "+order.getSetName()+"status:"+order.getStatusName(),order.getOrderID()));

				if (Settings.Secure.getString(
						JobDetailActivity.this.getContentResolver(),
						Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
					MockGPSALERT();
					return;
				}

				BeginReview(false);
			}
			break;
		case R.id.rejecttxt:
		case R.id.reject_layout:
		case R.id.rejectbtn:

			if (rejectbtn.getText().toString()
					.equals(getString(R.string.button_back_archive))) {
				finish();
			}
			else if (rejectbtn.getText().toString()
					.equals(getString(R.string.jd_reject_btn_text))) {
				if (order != null
						&& order.getAllowShoppersToRejectJobs() != null
						&& order.getAllowShoppersToRejectJobs().equals("2")) {
					Toast.makeText(JobDetailActivity.this,
							"You are not allowed to reject jobs.",
							Toast.LENGTH_LONG).show();
					break;
				}
				if (order != null
						&& order.getAllowShoppersToRejectJobs() != null
						&& order.getAllowShoppersToRejectJobs().equals("1")
						&& !order.getStatusName().equals("Assigned")) {

					Toast.makeText(JobDetailActivity.this,
							"You are not allowed to reject this job.",
							Toast.LENGTH_LONG).show();
					break;
				}
				RefusalReasonDialog dialog = new RefusalReasonDialog(this);
				dialog.show();
			} else if (aceeptbtn.getText().toString()
					.equals(getString(R.string.jd_begin_btn_text))) {
				finish();
			} else {
				if (OrderID.contains("CC")) {
					finish();
				} else {
					isBriefing = true;
					hideBriefing();
				}
			}
			break;

		case R.id.jdmapbtn:

			// count++;
			// sendMessage(WEAR_MESSAGE_PATH, "map button clicked =" + count);
			Intent intent = new Intent(
					JobDetailActivity.this.getApplicationContext(),
					MapActivity.class);
			intent.putExtra("orderid", OrderID);
			startActivityForResult(intent, MAP_ACTIVITY_CODE);
			break;
		case R.id.jd_alt_btn:
			showLanguageDialog();
			break;

		case R.id.jdcalendarbtn:

			Date d = null;
			String str = "";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (order != null && order.getDate() != null) {
				try {
					d = sdf.parse(order.getDate());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DateFormat dateFormat = android.text.format.DateFormat
					.getDateFormat(getApplicationContext());
			str = dateFormat.format(d);

			// date_v.setText(str);

			Intent intentCalendar = new Intent(Intent.ACTION_EDIT);
			intentCalendar.setType("vnd.android.cursor.item/event");
			Calendar c = Calendar.getInstance();
			int year = getYear(order.getDate());
			int month = getMonth(order.getDate());
			int day = getDay(order.getDate());
			int hourStart = getHour(order.getTimeStart());
			int minuteStart = getMinute(order.getTimeStart());
			int hourEnd = getHour(order.getTimeEnd());
			int minuteEnd = getMinute(order.getTimeEnd());
			// year, month, day, hourOfDay, minute
			c.set(year, month, day, hourStart, minuteStart);
			long millis = c.getTimeInMillis();
			intentCalendar.putExtra("beginTime", millis);
			intentCalendar.putExtra("rrule", "FREQ=YEARLY");
			c.set(year, month, day, hourEnd, minuteEnd);
			millis = c.getTimeInMillis();
			intentCalendar.putExtra("endTime", millis);
			intentCalendar.putExtra("eventLocation", order.getCityName());
			intentCalendar.putExtra("description",
					"Client name: " + order.getClientName() + ",\n"
							+ "Branch Full name: " + order.getBranchFullname()
							+ ",\n" + "Description: " + order.getDescription());
			intentCalendar.putExtra("title",
					"Questionnaire: " + order.getSetName());
			startActivity(intentCalendar);

			break;
		}
	}

	private GoogleApiClient mApiClient;

	private void sendMessage(final String path, final String text) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
						.getConnectedNodes(mApiClient).await();
				for (Node node : nodes.getNodes()) {
					if (order != null) {
						byte[] SetsBytes = SerializationUtils
								.serialize((Order) order);
						MessageApi.SendMessageResult result = Wearable.MessageApi
								.sendMessage(mApiClient, node.getId(), path,
										SetsBytes).await();

						if (result.getStatus().isSuccess()) {
							int i = 0;
							i++;
						} else {
							int i = 0;
							i++;
						}
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

	private void hideBriefing() {
		tv_header.setText(getResources().getString(R.string.jd_heder_text));
		rejectbtn
				.setText(getResources().getString(R.string.jd_reject_btn_text));
		rejectbtnImg.setImageResource(R.drawable.btn_decline);
		dataView.setVisibility(RelativeLayout.VISIBLE);
		calendarBtn.setVisibility(RelativeLayout.GONE);
		mapbtn.setVisibility(RelativeLayout.VISIBLE);
		webview.setVisibility(RelativeLayout.GONE);

	}

	private void showBriefing() {
		int modeSelect = myPrefs.getInt(Constants.SETTINGS_LANGUAGE_INDEX, 1);
		tv_header.setText(getResources().getString(R.string.txt_briefing));
		rejectbtn.setText(getResources().getString(R.string.pos_backk));
		if (modeSelect==3)
			rejectbtnImg.setImageResource(R.drawable.btn_begin);
		else
			rejectbtnImg.setImageResource(R.drawable.btn_back);
		//rejectbtnImg.setImageResource(R.drawable.btn_back);
		Helper.changeImageViewSrc(rejectbtnImg, getApplicationContext());
		calendarBtn.setVisibility(RelativeLayout.GONE);
		mapbtn.setVisibility(RelativeLayout.GONE);
		dataView.setVisibility(RelativeLayout.GONE);
		webview.setVisibility(RelativeLayout.VISIBLE);
		String content = "<HTML><BODY>" + order.getBriefingContent()
				+ "</HTML></BODY>";
		if (content != null && !content.equals("")) {
			content = content.replace("&lt;", "<");
			content = content.replace("&gt;", ">");
			content = content.replace("&quot;", "\"");
			WebView wv = webview;
			final String mimeType = "text/html";
			final String encoding = "UTF-8";
			wv.loadDataWithBaseURL("", content, mimeType, encoding, "");
		}
	}

	protected int getMinute(String start) {
		// TODO Auto-generated method stub
		// 20:00
		try {
			start = start.substring(start.indexOf(":") + 1);
			start = start.substring(0, start.indexOf(":"));
			int m = Integer.parseInt(start);
			return m;

		} catch (Exception ex) {
		}
		return 0;
	}

	protected int getHour(String start) {
		// TODO Auto-generated method stub
		// 20:00
		try {
			start = start.substring(0, start.indexOf(":"));
			int m = Integer.parseInt(start);

			return m;
		} catch (Exception ex) {
		}
		return 0;
	}

	protected int getMonth(String date) {
		// TODO Auto-generated method stub
		// 2013-05-27
		try {
			date = date.trim();
			String month = date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-"));
			int m = Integer.parseInt(month);
			if (m == 0)
				return 0;
			return m - 1;

		} catch (Exception ex) {
		}
		return 0;
	}

	protected int getYear(String date) {
		// TODO Auto-generated method stub
		// 2013-05-27
		try {
			String day = date.substring(0, date.indexOf("-"));
			int d = Integer.parseInt(day);
			return d;

		} catch (Exception ex) {
		}
		return 0;
	}

	protected int getDay(String date) {
		// 2013-05-27
		try {
			date = date.trim();
			String month = date.substring(date.lastIndexOf("-") + 1);
			int m = Integer.parseInt(month);
			if (m == 0)
				return 0;
			return m;

		} catch (Exception ex) {
		}
		return 0;
	}

	public void customToast(String string, boolean allCool, int lengthShort) {
		Context context = getApplicationContext();
		LayoutInflater inflater = getLayoutInflater();
		View customToastroot = null;

		customToastroot = inflater.inflate(R.layout.custom_toast_green, null);

		((TextView) customToastroot.findViewById(R.id.textView1))
				.setText(string);

		Toast customtoast = new Toast(context);

		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
		customtoast.setDuration(Toast.LENGTH_LONG);
		customtoast.show();
	}

	private boolean CheckResponse(String result, String failMessage) {

		msgId = R.string.reject_job_sucess_alert;
		// <GroupedNumber>1</GroupedNumber>
		if (result != null && result.toLowerCase().contains("<groupednumber>")) {
			String parsed = result.toLowerCase();
			int indexOf = parsed.indexOf("<groupednumber>") + 15;
			int indexOf2 = parsed.indexOf("</groupednumber>");
			int i = 1;
			try {
				parsed = parsed.substring(indexOf, indexOf2);

				i = Integer.parseInt(parsed);
			} catch (Exception ex) {

			}

			if (groupedNumber != null && !groupedNumber.equals("")) {
				refreshJoblist = false;
				serverGroupedNumber = i;
				return true;
			} else {
				refreshJoblist = false;
				return true;
			}
		}

		if (!Helper.IsValidResponse(result,
				Constants.JOB_DETAIL_RESP_FIELD_PARAM)) {
			ShowAlert(JobDetailActivity.this,
					getString(R.string.error_alert_title), failMessage,
					getString(R.string.alert_btn_lbl_ok));
			return false;
		}
		// result = new Parser().getValue(result,
		// Constants.JOB_DETAIL_RESP_FIELD_PARAM);
		// <?xml version="1.0"?>
		// <status>0</status>
		//
		// <reply>problem+changing+order+status%3A+This+order+is+not+assiged+to+you%21<div
		// dir=ltr align=left><B>user warning</b> -
		// <tt>problem+changing+order+status%3A+This+order+is+not+assiged+to+you%21</tt><BR>
		// <BR>
		// Backtrace: <table
		// border><thead><tr><td><b>file</b></td><td><b>line</b></td><td><b>function</b></td><td><b>args</b></td></tr></thead>
		// <tr><td>file:
		// '/home/checkermor/domains/checker.co.il/public_html/testing/arch/arch.php';
		// </td><td>line: 152; </td><td>function: 'reportErrorOrWarning';
		// </td><td>args: array (
		// 0 => 512,
		// 1 =>
		// 'problem+changing+order+status%3A+This+order+is+not+assiged+to+you%21',
		// ); </td></tr>
		// <tr><td>file:
		// '/home/checkermor/domains/checker.co.il/public_html/testing/c_pda-change-order.php';
		// </td><td>line: 310; </td><td>function: 'ReportWarning';
		// </td><td>args: array (
		// 0 =>
		// 'problem+changing+order+status%3A+This+order+is+not+assiged+to+you%21',
		// ); </td></tr>
		// </table></div></reply>
		if (result != null
				&& result.toLowerCase().contains("order+is+not+assiged")) {
			msgId = R.string.error_order_not_assigned;
			return true;
		}
		result = result.substring(
				result.indexOf(Constants.JOB_DETAIL_RESP_FIELD_PARAM),
				result.indexOf("</status>"));
		if (!(result.endsWith("1"))) {

			ShowAlert(JobDetailActivity.this,
					getString(R.string.error_alert_title), failMessage,
					getString(R.string.alert_btn_lbl_ok));
			return false;
		}
		return true;
	}

	// private boolean CheckResponse(String result)
	// {
	// if(!Helper.IsValidResponse(result,
	// Constants.JOB_DETAIL_RESP_FIELD_PARAM))
	// {
	// return false;
	// }
	// //result = new Parser().getValue(result,
	// Constants.JOB_DETAIL_RESP_FIELD_PARAM);
	//
	// result =
	// result.substring(result.indexOf(Constants.JOB_DETAIL_RESP_FIELD_PARAM),
	// result.indexOf("</status>"));
	// if(!(result.endsWith("1"))){
	// return false;
	// }
	// return true;
	// }

	public void rejectJob(String str) {

		if (IsInternetConnectted() >= 0) {
			ShowAlert(JobDetailActivity.this,
					getString(R.string.jd_parsing_alert_title),
					getString(R.string.reject_job_fail_alert),
					getString(R.string.alert_btn_lbl_ok));
			return;
		}
		new JobTask().execute("", str);
	}

	private void BeginReview(boolean isFromWatch) {
		// if(!Helper.isParsed()){
		// ShowAlert(JobDetailActivity.this,
		// getString(R.string.jd_parsing_alert_title),
		// getString(R.string.jd_parsing_alert_msg),
		// getString(R.string.alert_btn_lbl_ok));
		// return;
		// }
		// if(!dataSaved)
		// new saveSetThread().start();
		if (survey != null) {
			int i = 0;
			i++;
		}
		if (OrderID.contains("-")) {
			Intent intent = new Intent(this.getApplicationContext(),
					QuestionnaireActivity.class);
			intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID, OrderID);
			intent.putExtra(Constants.FIELD_ORDER_SET_ID, order.getSetID());
			if (isFromWatch)
				startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
			else
				startActivity(intent);
		} else {
			Intent intent = new Intent(this.getApplicationContext(),
					QuestionnaireActivity.class);
			if (order == null)
				setOrder();
			intent.putExtra(Constants.POST_FIELD_QUES_ORDER_ID,
					order.getOrderID());
			intent.putExtra(Constants.FIELD_ORDER_SET_ID, order.getSetID());
			if (isFromWatch)
				startActivityForResult(intent, QUESTIONNAIRE_ACTIVITY_CODE);
			else
				startActivity(intent);
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

		// alert.setMessage(message);
		alert.setPositiveButton(button_lbl,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Revamped_Loading_Dialog.hide_dialog();
						if (message
								.equals(getString(R.string.reject_job_sucess_alert))
								|| message
										.equals(getString(R.string.error_order_not_assigned))
								|| message
										.equals(getString(R.string.alert_sync_jobs_again)))
							showJobList();
					}
				});
		alert.show();
	}

	private void showJobList() {
		Intent intent = new Intent();
		intent.putExtra(Constants.JOB_DETAIL_IS_REJECT_FIELD_KEY, true);
		setResult(RESULT_OK, intent);
		finish();
	}

	void stopLocationChecker() {
		Context context = JobDetailActivity.this;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

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
									finish();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	void MockGPSALERT() {
		// OPen GPS settings
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getResources().getString(R.string.questionnaire_mock_gps_alert))
				.setTitle(getResources().getString(R.string._alert_title))
				.setCancelable(false)
				.setPositiveButton(
						getResources().getString(
								R.string.questionnaire_exit_delete_alert_yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								startActivityForResult(
										new Intent(
												android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
										JOB_GPS_CODE);

								dialog.dismiss();

							}
						})
				.setNegativeButton(
						getResources().getString(
								R.string.questionnaire_exit_delete_alert_no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								BeginReview(false);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}

	void startLocationChecker() {
		Context context = JobDetailActivity.this;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			if (Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
				MockGPSALERT();
				return;
			}
			myPrefs = getSharedPreferences("pref", MODE_PRIVATE);

			String userName = myPrefs.getString(
					Constants.POST_FIELD_LOGIN_USERNAME, "");
			// if (Helper.lThread == null) {
			// Helper.lThread = new locationThread();
			// Helper.lThread.isPost = true;
			// } else {
			// Helper.lThread.isPost = true;
			// }
			// Helper.lThread.startLocationThread(getApplicationContext(),
			// userName);
			for (int i = 0; i < JobListActivity.joborders.size(); i++) {
				if (JobListActivity.joborders.get(i).orderItem.getOrderID()
						.equals(OrderID)) {
					JobListActivity.joborders.get(i).orderItem
							.setStatusName("Scheduled");
				}
			}

			BeginReview(false);

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

									// Sent user to GPS settings screen
									// final ComponentName toLaunch = new
									// ComponentName("com.android.settings","com.android.settings.SecuritySettings");
									// final Intent intent = new
									// Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									// intent.addCategory(Intent.CATEGORY_LAUNCHER);
									// intent.setComponent(toLaunch);
									// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									// startActivityForResult(intent, 1);

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
									// JobListActivity.joborders.get(localindex).orderItem
									// .setStatusName("Scheduled");
									BeginReview(false);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	private void onQuestionResult(Intent data) {

		{
			CheckerApp.clearQuestionResult();
			setResult(RESULT_OK, data);
			if (OrderID.contains("-")) {
				if (data.getExtras().getString(Constants.BRANCH_NAME) != null) {
					String thisBranch = data.getExtras().getString(
							Constants.BRANCH_NAME);
					DBHelper.updateSurveySelectedBranch(thisBranch, OrderID);
				}
				if (data.getExtras().getInt(Constants.QUESTIONNAIRE_STAUS) == 142) {
					ArrayList<Order> jobordersss = DBHelper
							.getOrders(
									null,
									Constants.DB_TABLE_JOBLIST,
									new String[] {
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
					if (jobordersss != null) {
					}

				}

				// else finish();
				OrderID = Orders.getNextSurveyOrder(OrderID);
				Bundle b = getIntent().getExtras();
				if (b != null) {

					b.putString("OrderID", OrderID);
				}
				OrderID = b.getString("OrderID");
				if (OrderID.contains("-")) {
					getthisOrderFromListView(OrderID);
					setSurveyData(OrderID.replace("-", ""));
				} else
					finish();
				if (OrderID != null) {

					return;
				}

			}

		}
		finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case (JOB_GPS_OFF_CODE):
			finish();
			break;
		case (JOB_GPS_CODE):
			//startLocationChecker();
			break;
		case (QUESTIONNAIRE_ACTIVITY_CODE):
			if (data != null && data.hasExtra("from_watch")) {
				Intent intent = new Intent();
				intent.putExtra("from_watch", true);
				setResult(2, intent);
				finish();
			}
			break;
		// check if gps is on then finish

		}
	}

	private boolean showLogin(String result) {
		String result1 = new Parser().getValue(result,
				Constants.LOGIN_RESP_FIELD_PARAM);
		if (result1 == null)
			return false;
		if (result1.equals("0")) {
			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
			prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, false);
			prefsEditor.commit();
			Intent intent = new Intent(this.getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return false;
	}

	private void saveOfflineQuestionaire() {
		// SQLiteDatabase db = DBAdapter.openDataBase();
		if (order == null)
			setOrder();
		DBHelper.updateOrders(Constants.DB_TABLE_ORDERS, new String[] {
				Constants.DB_TABLE_ORDERS_ORDERID,
				Constants.DB_TABLE_ORDERS_STATUS,
				Constants.DB_TABLE_ORDERS_START_TIME, }, order.getOrderID(),
				"Scheduled", "",null); // getString(R.string.jd_begin_button_status_scheduled),
									// db);
		// DBAdapter.closeDataBase(db);
		for (int oid = 0; oid < JobListActivity.joborders.size(); oid++) {
			if (JobListActivity.joborders.get(oid).orderItem.getOrderID()
					.equals(order.getOrderID())) {
				JobListActivity.joborders.get(oid).orderItem
						.setStatusName("Scheduled");
				break;
			}
		}
	}

	// private Set getSetsRecords(String setid)
	// {
	// SQLiteDatabase db = DBAdapter.openDataBase();
	// Set set = DBHelper.getSetsRecords(Constants.DB_TABLE_SETS, new String[]{
	// Constants.DB_TABLE_SETS_SETID ,
	// Constants.DB_TABLE_SET_NAME ,
	// Constants.DB_TABLE_SET_COMP_LINK ,
	// Constants.DB_TABLE_SET_DESC ,
	// Constants.DB_TABLE_SET_CODE ,
	// Constants.DB_TABLE_SET_SHOWSAVEANDEXIT ,
	// Constants.DB_TABLE_SET_SHOWTOC ,
	// Constants.DB_TABLE_SET_SHOWPREVIEW ,
	// Constants.DB_TABLE_SET_CLIENTNAME ,
	// Constants.DB_TABLE_SET_SHOWFREETEXT,
	// }, Constants.DB_TABLE_SETS_SETID+"="+"\""+setid+"\"", db);
	// DBAdapter.closeDataBase(db);
	// return set;
	// }

	private class JobTask extends AsyncTask<String, Void, String> {


		@Override
		protected void onPreExecute() {
			Revamped_Loading_Dialog.show_dialog(JobDetailActivity.this, null);
		}
		private ArrayList<Cert> parseCertificateResult(String result) {

			Parser parser = new Parser();
			parser.parseXMLValues(result, Constants.CERTS_FIELD_PARAM);
			return parser.listCerts;

		}
		@Override
		protected String doInBackground(String... params) {
			if (Connector.cookies == null) {
				if (showLogin(doLogin()))
					return "SessionExpire";
			}
			String result = "";
			if (params[0].equals(getString(R.string.jd_accept_btn_text))) {
				if (set != null && set.getCertificates() != null
						&& set.getCertificates().size() > 0) {
					List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
					String data = Connector.postForm(
							Constants.getCheckerTificates("9.7", true), extraDataList);
					if (data.contains("<script>")) {
						doLogin();
						data = Connector
								.postForm(Constants.getCheckerTificates("9.7", true),
										extraDataList);
					}
					if (data != null && data.contains("<status>1</status>")) {
						return null;
					} else {
						// no error parse here
						ArrayList<Cert> listOfCerts = parseCertificateResult(data);

						ArrayList<Cert> shortList = set.getCertificates();

						if (shortList != null && listOfCerts != null) {
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
											.equals(certID)
									&& !listOfCerts.get(i).getStatus().toLowerCase()
											.equals("passed")) {
										isPresent = true;
										break;
									}

								}
								if (isPresent)
									templistOfCerts.add(listOfCerts.get(i));
							}
							if (templistOfCerts!=null && templistOfCerts.size()>0) {
								JobDetailActivity.this.pendingCerts = templistOfCerts;
								return null;
							}
						}
						}
					}

				doLogin();

				result = AcceptJob();
//				if (result.contains("<script>")) {
//
//					result = AcceptJob();
//				}
					return result;
				} else {
				doLogin();
				return RejectJob(params[1]);
				}
			}


		public String doLogin() {
			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);
			return loginPost(
					myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
					myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
					Constants.POST_VALUE_LOGIN_DO_LOGIN);
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

		private void On_ExitanddeleteButton_Click(String orderID) {
			String where = Constants.DB_TABLE_QUESTIONNAIRE_ORDERID + "="
					+ "\"" + orderID + "\"";
			DBAdapter.openDataBase();
			DBAdapter.LogCommunication("checkerDBLog.txt",
					"jobdetail-deleteThisQuestionnaire=" + where);
			DBAdapter.db.delete(Constants.DB_TABLE_QUESTIONNAIRE, where, null);
			DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.DB_TABLE_ANSWERS, where, null);
			DBAdapter.openDataBase();
			// DBAdapter.db.delete(Constants.UPLOAD_FILE_TABLE, where, null);
			// DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.DB_TABLE_POS, where, null);
			DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.DB_TABLE_ORDERS, where, null);
			DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.DB_TABLE_SUBMITSURVEY, where, null);
			DBAdapter.openDataBase();
			DBAdapter.db.delete(Constants.DB_TABLE_JOBLIST, where, null);

			// exitAfterSubmitSurveyOrExitandsave(2);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result==null && pendingCerts!=null)
			{
				ShowCertAlert(pendingCerts);
				Revamped_Loading_Dialog.hide_dialog();
				return;
			}
			if (result==null || result.equals("SessionExpire")) {
				Revamped_Loading_Dialog.hide_dialog();
				return;
			}
			if (result!=null && result.endsWith("r")) {
				SplashScreen.addLog(new BasicLog(
						myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
						myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Rejecting Order!"+order.getOrderID()+" Reply from server= "+result,order.getOrderID()));

				result = result.substring(0, result.length() - 1);
				// dialog.onPostExecute();
				if (CheckResponse(result,
						getString(R.string.reject_job_fail_alert))) {
					ShowAlert(JobDetailActivity.this,
							getString(R.string.jd_parsing_alert_title),
							getString(msgId),
							getString(R.string.alert_btn_lbl_ok));

					// On_ExitanddeleteButton_Click(order.getOrderID());
					DBHelper.updateOrders(Constants.DB_TABLE_ORDERS,
							new String[] { Constants.DB_TABLE_ORDERS_ORDERID,
									Constants.DB_TABLE_ORDERS_STATUS,
									Constants.DB_TABLE_ORDERS_START_TIME, },
							order.getOrderID(), "Scheduled", "",null);
				} else {
					if (refreshJoblist == true) {
						// showSyncAlert();
					}
				}
				Revamped_Loading_Dialog.hide_dialog();
			} else {
				SplashScreen.addLog(new BasicLog(
						myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
						myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),"Accepting Order!"+order.getOrderID()+" Reply from server= "+result,order.getOrderID()));

				if (CheckResponse(result,
						getString(R.string.invalid_server_response_alert))) {
					aceeptbtn
							.setText(getString(R.string.jd_begin_review_btn_text));
					if (order.getCount() < 2
							|| (groupedNumber != null
									&& groupedNumber.length() > 0 && serverGroupedNumber <= 1)) {
						saveOfflineQuestionaire();
						Revamped_Loading_Dialog.hide_dialog();
						return;
					}
					{
						// SQLiteDatabase db = DBAdapter.openDataBase();
						for (int i = 0; i < Orders.getOrders().size(); i++) {
							Order innerorder = Orders.getOrders().get(i);
							if ((order.getBranchLink().equals(innerorder
									.getBranchLink()))
									&& (order.getMassID().equals(innerorder
											.getMassID()))
									&& (order.getDate().equals(innerorder
											.getDate()))
									&& (order.getSetLink().equals(innerorder
											.getSetLink()))
									&& (order.getStatusName().equals(innerorder
											.getStatusName()))) {
								if (!order.getOrderID().equals(
										innerorder.getOrderID())) {
									Orders.getOrders().get(i)
											.setStatusName("Scheduled");
									DBHelper.updateOrders(
											Constants.DB_TABLE_ORDERS,
											new String[] {
													Constants.DB_TABLE_ORDERS_ORDERID,
													Constants.DB_TABLE_ORDERS_STATUS,
													Constants.DB_TABLE_ORDERS_START_TIME, },
											innerorder.getOrderID(),
											"Scheduled", "",null);
								}
							}
						}
					}
					// DBAdapter.closeDataBase(db);
					saveOfflineQuestionaire();
					Revamped_Loading_Dialog.hide_dialog();
				} else {
					if (refreshJoblist == true) {
						// showSyncAlert();
					}
				}

			}
		}

		public String RejectJob(String reason) {
		    if (order==null || order.getOrderID()==null) return null;
			List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
			extraDataList.add(Helper.getNameValuePair(
					Constants.POST_VALUE_JOB_DETAIL_REJECT,
					Constants.POST_VALUE_JOB_DETAIL_PARAM_VALUE));
			extraDataList.add(Helper.getNameValuePair(
					Constants.POST_FIELD_JOB_DETAIL_ORDER_ID,
					order.getOrderID()));

			extraDataList.add(Helper.getNameValuePair(
					Constants.POST_FIELD_JOB_DETAIL_REFUSAL_REASON, reason));
			extraDataList.add(Helper.getNameValuePair(
					Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
					groupedNumber));

			// dialog.onPostExecute();
			String result = Connector.postForm(Constants.getJobStartURL(),
					extraDataList);
			return result + "r";
		}

		private String AcceptJob() {
			List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
            String result = null;
			extraDataList.add(Helper.getNameValuePair(
					Constants.POST_VALUE_JOB_DETAIL_ACCEPT,
					Constants.POST_VALUE_JOB_DETAIL_PARAM_VALUE));
			if (order == null || order.getOrderID()==null)
				setOrder();
			if (order!=null && order.getOrderID()!=null) {
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_JOB_DETAIL_ORDER_ID,
                        order.getOrderID()));
                extraDataList.add(Helper.getNameValuePair(
                        Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
                        groupedNumber));
                result = Connector.postForm(Constants.getJobStartURL(),
                        extraDataList);
            }
			return result;
		}
	}

	public void showSyncAlert() {

		ShowAlert(
				JobDetailActivity.this,
				getString(R.string.jd_parsing_alert_title),
				getString(R.string.alert_sync_jobs_again).replace("#LOCAL#",
						groupedNumber).replace("#SERVER#",
						serverGroupedNumber + ""),
				getString(R.string.alert_btn_lbl_ok),
				getString(R.string.alert_btn_lbl_ok));
	}

	private void ShowAlert(Context context, String title, String message,
			String yes, String no) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		TextView textView = new TextView(context);
		setFontSize(textView);
		textView.setText(message);
		alert.setView(textView);

		// alert.setMessage(message);
		alert.setPositiveButton(yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Revamped_Loading_Dialog.hide_dialog();
				showJobList();
			}
		});

		alert.show();

	}

	private void ShowCertAlert(ArrayList<Cert> pendingCerts) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				JobDetailActivity.this);
		builder.setCancelable(false);
		builder.setMessage(
				getResources().getString(R.string.attached_certificate_msg))
				.setPositiveButton(getResources().getString(R.string.questionnaire_exit_delete_alert_yes), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (JobDetailActivity.certCallBack != null)
							JobDetailActivity.certCallBack
									.certCallBack(JobDetailActivity.this.pendingCerts);
						finish();
					}
				})
				.setNegativeButton(getResources().getString(R.string.questionnaire_exit_delete_alert_no), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setCancelable(false).show();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApiClient.disconnect();
	}

	@Override
	public void onMessageReceived(final MessageEvent messageEvent) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					if (messageEvent.getPath().equalsIgnoreCase(START_JOB)) {

					}
				} catch (Exception ex) {
					Toast.makeText(JobDetailActivity.this,
							ex.getLocalizedMessage(), 100).show();
				}

			}
		});
	}

	private void initGoogleApiClient() {
		mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		mApiClient.connect();

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		int i = 0;
		i++;
	}

	@Override
	public void onConnected(Bundle arg0) {
		sendMessage(WEAR_MESSAGE_PATH, "map button clicked =" + count);
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	private void loadViews() {
		Context cntxt = getApplicationContext();
		Helper.changeImageViewSrc(mapbtn, cntxt);
		Helper.changeImageViewSrc(calendarBtn, cntxt);
		Helper.changeImageViewSrc(alBtn, cntxt);
		Helper.changeImageViewSrc(rejectbtnImg, cntxt);
		Helper.changeImageViewSrc(acceptbtnImg, cntxt);

		Helper.changeTxtViewColor(aceeptbtn);
		Helper.changeTxtViewColor(rejectbtn);
		Helper.changeTxtViewColor(sBonusPayment_t);
		Helper.changeTxtViewColor(sCriticismPayment_t);
		Helper.changeTxtViewColor(sTransportationPayment_t);
		Helper.changeTxtViewColor(sNonRefundableServicePayment_t);
		Helper.changeTxtViewColor(jdcanpurchase_t);
		Helper.changeTxtViewColor(jdpurchasedes_t);
		Helper.changeTxtViewColor(jdtimeet);
		Helper.changeTxtViewColor(jdtimest);
		Helper.changeTxtViewColor(jdopeninght);
		Helper.changeTxtViewColor(jdbranchpt);
		Helper.changeTxtViewColor(jdaddt);
		Helper.changeTxtViewColor(jdcitynt);
		Helper.changeTxtViewColor(jdbranchfnamet);
		Helper.changeTxtViewColor(jdbranchname);
		Helper.changeTxtViewColor(jdclientt);
		Helper.changeTxtViewColor(jdsetnamet);
		Helper.changeTxtViewColor(jddescriptiont);
		Helper.changeTxtViewColor(jdstaust);
		Helper.changeTxtViewColor(jddatet);

		Helper.changeViewColor(separator1);
		Helper.changeViewColor(separator2);
		Helper.changeViewColor(separator3);
		Helper.changeViewColor(separator4);
		Helper.changeViewColor(separator5);
		Helper.changeViewColor(separator6);
		Helper.changeViewColor(separator7);
		Helper.changeViewColor(separator8);
		Helper.changeViewColor(separator9);
		Helper.changeViewColor(separator10);
		Helper.changeViewColor(separator11);
		Helper.changeViewColor(separator12);
		Helper.changeViewColor(separator13);
		Helper.changeViewColor(separator14);
		Helper.changeViewColor(separator15);
		Helper.changeViewColor(separator16);
		Helper.changeViewColor(separator17);
		Helper.changeViewColor(separator18);
		Helper.changeViewColor(separator19);
	}
}

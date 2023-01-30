package com.mor.sa.android.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;

import DateTimePicker.MonthYearPicker;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.checker.sa.android.adapter.refundAdapter;
import com.checker.sa.android.data.RefundData;
import com.checker.sa.android.data.RefundSerialize;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

public class ShopperRefundReportActivity extends Activity implements
		OnClickListener {

	MonthYearPicker picker_month_year;
	TextView heading;
	SharedPreferences myPrefs;
	ListView refundList;
	private Button btnRefund;
	private Button btnCancel;
	View loaderlayout;
	protected int monthVal;
	protected int yearVal;
	private TextView updatedTime;
	private ImageView updateButton;
	boolean isApproved = false;
	boolean isPaid = false;
	Spinner flow_type_spinner;
	Spinner Report_type_Spinner;
	protected String selectedType = null;
	protected int isDetailed;
	private ImageView filter_btn;
	protected boolean ishide = true;
	Spinner Paid_Filter_Spinner;
	Spinner Approved_filter_Spinner;
	ImageView crossButton;
	private ImageView back_btn;
	private String valuePaid = "All";
	String valueApproved = "All";

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

	private String RefundListPost() {
		// Initialize the login data to POST
		List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
		String app_ver = "";
		try {
			app_ver = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}
		return Connector.postForm(
				Constants.getRefundListURL("9.7", yearVal, monthVal),
				extraDataList);
	}

	private String RefundListTypesPost() {
		// Initialize the login data to POST
		List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
		String app_ver = "";
		try {
			app_ver = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}
		return Connector.postForm(Constants.getRefundTypeListURL("9.7"),
				extraDataList);
	}

	int month1 = 1;
	int year1 = 2017;

	@Override
	public void onBackPressed() {
		if (ishide == true) {
			shohidefilter();
		} else
			ShopperRefundReportActivity.this.finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refund_report);

		flow_type_spinner = (Spinner) findViewById(R.id.Flow_Type_Filter);
		picker_month_year = (MonthYearPicker) findViewById(R.id.DateTimePicker);
		Report_type_Spinner = (Spinner) findViewById(R.id.Report_Type_Filter);
		Paid_Filter_Spinner = (Spinner) findViewById(R.id.paidFilter);
		Approved_filter_Spinner = (Spinner) findViewById(R.id.ApprovedFilter);
		heading = (TextView) findViewById(R.id.jd_heder);
		filter_btn = (ImageView) findViewById(R.id.filterbtn);
		filter_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shohidefilter();

			}
		});

		back_btn = (ImageView) findViewById(R.id.back_button);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ishide == true) {
					shohidefilter();
				} else
					ShopperRefundReportActivity.this.finish();

			}
		});
		Calendar c = Calendar.getInstance();
		month1 = c.get(Calendar.MONTH) + 1;
		year1 = c.get(Calendar.YEAR);
		flow_type_list = new String[18];
		flow_type_list[0] = "Not Selected";
		flow_type_list[1] = "Acquisition through review";
		flow_type_list[2] = "Cash";
		flow_type_list[3] = "Credit card withdrawal";
		flow_type_list[4] = "Credit card payment";
		flow_type_list[5] = "Survey payment";
		flow_type_list[6] = "Transportation payment";
		flow_type_list[7] = "Service payment";
		flow_type_list[8] = "Bonus payment";
		flow_type_list[9] = "Phone survey payment";
		flow_type_list[10] = "Reward";
		flow_type_list[11] = "Penalty";
		flow_type_list[12] = "&lt;font color=darkred&gt;Submission bonus&lt;/font&gt;";
		flow_type_list[13] = "Postal services";
		flow_type_list[14] = "SMS alerts charges €";
		flow_type_list[15] = "Reduced payment due to disapproved review";
		flow_type_list[16] = "BC";
		flow_type_list[17] = "PS";

		report_type_list = new String[3];
		report_type_list[0] = "Detailed";
		report_type_list[1] = "Summary";
		report_type_list[2] = "Group By Flow Type";

		Paid_filter_list = new String[3];
		Paid_filter_list[0] = "All";
		Paid_filter_list[1] = "Yes";
		Paid_filter_list[2] = "No";

		Approved_filter_list = new String[3];
		Approved_filter_list[0] = "All";
		Approved_filter_list[1] = "Yes";
		Approved_filter_list[2] = "No";

		ArrayAdapter badapter = new ArrayAdapter(this,
				UIHelper.getSpinnerLayoutSize(ShopperRefundReportActivity.this,
						0), flow_type_list);
		badapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				ShopperRefundReportActivity.this, 0));
		flow_type_spinner.setAdapter(badapter);

		flow_type_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == 0)
							selectedType = null;
						selectedType = flow_type_list[arg2];
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		ArrayAdapter repadapter = new ArrayAdapter(this,
				UIHelper.getSpinnerLayoutSize(ShopperRefundReportActivity.this,
						0), report_type_list);
		badapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				ShopperRefundReportActivity.this, 0));
		Report_type_Spinner.setAdapter(repadapter);

		Report_type_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						isDetailed = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		@SuppressWarnings("unchecked")
		ArrayAdapter paidadapter = new ArrayAdapter(this,
				UIHelper.getSpinnerLayoutSize(ShopperRefundReportActivity.this,
						0), Paid_filter_list);
		badapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				ShopperRefundReportActivity.this, 0));

		Paid_Filter_Spinner.setAdapter(paidadapter);

		Paid_Filter_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == 0) {
							valuePaid = "All";
						} else if (arg2 == 1) {
							valuePaid = "Yes";
						} else if (arg2 == 2) {
							valuePaid = "No";
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		ArrayAdapter approvedAdapter = new ArrayAdapter(this,
				UIHelper.getSpinnerLayoutSize(ShopperRefundReportActivity.this,
						0), Approved_filter_list);
		badapter.setDropDownViewResource(UIHelper.getSpinnerLayoutSize(
				ShopperRefundReportActivity.this, 0));
		Approved_filter_Spinner.setAdapter(approvedAdapter);

		Approved_filter_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == 0) {
							valueApproved = "All";
						} else if (arg2 == 1) {
							valueApproved = "Yes";
						} else if (arg2 == 2) {
							valueApproved = "No";
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		updatedTime = (TextView) findViewById(R.id.update_time);
		updateButton = (ImageView) findViewById(R.id.update_button);
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				picker_month_year.updateDate(year1, month1, 1);
				refresh_submit(true);
			}
		});

		picker_month_year.updateDate(year1, month1 - 1, 1);
		monthVal = month1;
		yearVal = year1;
		heading.setText(monthVal + "/" + yearVal);
		Helper.changeTxtViewColor(heading);
		refundList = (ListView) findViewById(R.id.refundList);
		btnRefund = (Button) findViewById(R.id.btnRefund);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnRefund.setOnClickListener(this);
		loaderlayout = findViewById(R.id.refundLoader);

		shohidefilter();
		refresh_submit(true);
	}

	protected void shohidefilter() {
		if (ishide == true) {
			ShopperRefundReportActivity.this.findViewById(R.id.scroller)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.filterLayout)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.Label)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.montyearpicker)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.flowTypeSp)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.ReportTypeSp)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.btnRefund)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.PaidSp)
					.setVisibility(LinearLayout.GONE);
			ShopperRefundReportActivity.this.findViewById(R.id.ApprovedSp)
					.setVisibility(LinearLayout.GONE);

			// show here
			ishide = false;
		} else {

			ShopperRefundReportActivity.this.findViewById(R.id.Label)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.scroller)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.filterLayout)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.montyearpicker)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.flowTypeSp)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.ReportTypeSp)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.btnRefund)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.ApprovedSp)
					.setVisibility(LinearLayout.VISIBLE);
			ShopperRefundReportActivity.this.findViewById(R.id.PaidSp)
					.setVisibility(LinearLayout.VISIBLE);
			ishide = true;
			// hide here
		}
	}

	String[] flow_type_list;
	String[] report_type_list;
	String[] Paid_filter_list;
	String[] Approved_filter_list;

	//

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRefund) {
			month1 = monthVal = picker_month_year.getMonthInt() + 1;
			year1 = yearVal = picker_month_year.getYearInt();
			heading.setText(monthVal + "/" + yearVal);

			refresh_submit(false);
		} else
			clearFilters();
	}

	private void clearFilters() {
		try {
			flow_type_spinner.setSelection(0);
		} catch (Exception ex) {

		}
		try {
			Report_type_Spinner.setSelection(0);
		} catch (Exception ex) {

		}

		try {
			Paid_Filter_Spinner.setSelection(0);
		} catch (Exception ex) {

		}

		try {
			Approved_filter_Spinner.setSelection(0);
		} catch (Exception ex) {

		}
		try {
			picker_month_year.updateDate(year1, month1 - 1, 1);
		} catch (Exception ex) {

		}

	}

	public void refresh_submit(final boolean isRefresh) {
		class RefundTask extends
				AsyncTask<Void, Integer, ArrayList<RefundData>> {
			private String updateDate;

			@Override
			protected void onPreExecute() {
				// loaderlayout.setVisibility(RelativeLayout.VISIBLE);
				Revamped_Loading_Dialog.show_dialog(
						ShopperRefundReportActivity.this, null);
				this.updateDate = null;
			}

			@Override
			protected void onPostExecute(ArrayList<RefundData> result) {
				// parse it here
				if (result == null || result.size() == 0)
					loaderlayout.setVisibility(RelativeLayout.VISIBLE);
				else
					loaderlayout.setVisibility(RelativeLayout.GONE);
				if (this.updateDate != null) {
					updateButton.setVisibility(RelativeLayout.VISIBLE);
					updatedTime.setVisibility(RelativeLayout.VISIBLE);
					updatedTime.setText(this.updateDate);
				} else {
					updateButton.setVisibility(RelativeLayout.INVISIBLE);
					updatedTime.setVisibility(RelativeLayout.GONE);
					updateButton.setVisibility(RelativeLayout.VISIBLE);
					saveRecord(monthVal, yearVal, result);
				}
				if (isRefresh == false)
					shohidefilter();
				result = filterResults(result, isApproved, isPaid, selectedType);
				ArrayAdapter<RefundData> adapter = new refundAdapter(
						ShopperRefundReportActivity.this, result, isDetailed);
				refundList.setAdapter(adapter);
				Revamped_Loading_Dialog.hide_dialog();
			}

			@Override
			protected ArrayList<RefundData> doInBackground(Void... params) {
				RefundSerialize rf = getRecord(monthVal, yearVal);
				if ((isRefresh == false || !IsInternetConnectted())
						&& rf != null && rf.getRecords() != null
						&& rf.getRecords().size() > 0) {
					this.updateDate = rf.getDate_time();
					return rf.getRecords();
				}

				String data = RefundListPost();
				if (data.contains("<script>")) {
					doLogin();
					data = RefundListPost();
				}
				String dataTypes = RefundListTypesPost();

				Parser parser = new Parser();
				for (int i = 0; i <= 9; i++) {
					data = data.replace("<" + i, "<Flow" + i);
					data = data.replace("</" + i, "</Flow" + i);
				}
				parser.parseXMLValues("<flows>" + data + "</flows>",
						Constants.REFUND_RESP_FIELD_PARAM);

				// check if new api needs to get called
				parser.refundReportList = parseFlowTypes(dataTypes,
						parser.refundReportList);

				if (parser.refundReportList != null
						&& parser.refundReportList.size() <= 0 && rf != null
						&& rf.getRecords() != null
						&& rf.getRecords().size() > 0) {
					this.updateDate = rf.getDate_time();
					return rf.getRecords();
				}
				return parser.refundReportList;
			}
		}
		RefundTask refundtaskobj = new RefundTask();
		refundtaskobj.execute();
	}

	public ArrayList<RefundData> filterResults(ArrayList<RefundData> result,
			boolean isApproved, boolean isPaid, String selectedType2) {
		ArrayList<RefundData> temp = new ArrayList<RefundData>();
		for (int i = 0; i < result.size(); i++) {
			if (selectedType2 != null && !selectedType2.equals("Not Selected")
					&& !result.get(i).getFlowTypeLink().equals(selectedType2))
				continue;

			if (valueApproved == "Yes" && valuePaid == "Yes") {
				if (result.get(i).getPaid().equals("1")
						&& result.get(i).getApproved().equals("1")) {

					temp.add(result.get(i));
				}
			} else if (valueApproved == "Yes" && valuePaid == "No") {
				if (result.get(i).getApproved().equals("1")
						&& result.get(i).getPaid().equals("0")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "No" && valuePaid == "Yes") {
				if (result.get(i).getApproved().equals("0")
						&& result.get(i).getPaid().equals("1")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "All" && valuePaid == "Yes") {
				if ((result.get(i).getApproved().equals("0") || result.get(i)
						.getApproved().equals("1"))
						&& result.get(i).getPaid().equals("1")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "All" && valuePaid == "No") {
				if ((result.get(i).getApproved().equals("0") || result.get(i)
						.getApproved().equals("1"))
						&& result.get(i).getPaid().equals("0")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "Yes" && valuePaid == "All") {
				if ((result.get(i).getPaid().equals("0") || result.get(i)
						.getPaid().equals("1"))
						&& result.get(i).getApproved().equals("1")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "No" && valuePaid == "All") {
				if ((result.get(i).getPaid().equals("0") || result.get(i)
						.getPaid().equals("1"))
						&& result.get(i).getApproved().equals("0")) {
					temp.add(result.get(i));
				}
			} else if (valueApproved == "No" && valuePaid == "No") {
				if (result.get(i).getPaid().equals("0")
						&& result.get(i).getApproved().equals("0")) {
					temp.add(result.get(i));
				}
			} else {
				temp.add(result.get(i));
			}

		}
		return temp;
	}

	public ArrayList<RefundData> parseFlowTypes(String dataTypes,
			ArrayList<RefundData> refundReportList) {
		if (dataTypes != null) {
			for (int i = 0; i < refundReportList.size(); i++) {
				String startTag = "<"
						+ refundReportList.get(i).getFlowTypeLink() + ">";
				String endTag = "</"
						+ refundReportList.get(i).getFlowTypeLink() + ">";
				int startIndex = dataTypes.indexOf(startTag);
				int endIndex = dataTypes.indexOf(endTag);
				String type = dataTypes.substring(
						startIndex + startTag.length(), endIndex);
				refundReportList.get(i).setFlowTypeLink(type);
			}
		}
		return refundReportList;
	}

	private RefundSerialize getRecord(int month, int year) {
		try {
			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);
			RefundSerialize rf = (RefundSerialize) DBHelper.convertFromBytes(
					month + "_" + year,
					myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""));
			return rf;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void saveRecord(int month, int year, ArrayList<RefundData> records) {
		try {
			Calendar c = Calendar.getInstance();
			int seconds = c.get(Calendar.SECOND);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month1 = c.get(Calendar.MONTH);
			int year1 = c.get(Calendar.YEAR);
			int mins = c.get(Calendar.MINUTE);
			int hrs = c.get(Calendar.HOUR);

			RefundSerialize rf = new RefundSerialize(hrs + ":" + mins + ":"
					+ seconds + " " + day + "/" + month1 + "/" + year1, records);
			SharedPreferences myPrefs = getSharedPreferences("pref",
					MODE_PRIVATE);
			DBHelper.convertToBytes(rf, month + "_" + year,
					myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		SharedPreferences myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		return loginPost(
				myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""),
				myPrefs.getString(Constants.POST_FIELD_LOGIN_PASSWORD, ""),
				Constants.POST_VALUE_LOGIN_DO_LOGIN);
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

}

package com.checker.sa.android.dialog;

import java.util.Calendar;
import java.util.Vector;

import com.checker.sa.android.data.FilterData;
import com.checker.sa.android.data.Orders;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.google.maps.android.MapActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class JobFilterDialog extends Dialog implements
		android.view.View.OnClickListener {

	TextView calander_h, sdate_text, edate_text, s_date, e_date;
	Spinner jobTypeSpinner, regionTypeSpinner, bregionspinner;
	ImageView save_btn;
	String jobtype, city, region, project;
	private String branchcode;
	TextView tv;
	int tag = 0;// , d1, m1, y1, d2, m2, y2;
	Activity joblist;
	String[] monthVal = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12" };

	private void setInvertDisplay() {
		if (Helper.getTheme(superContext) == 0) {
			// ImageView ilayout = (ImageView) findViewById(R.id.bottombar);
			// ilayout.setBackgroundDrawable(superContext.getResources().getDrawable(
			// R.drawable.navigation_bar_dark));
			//
			// RelativeLayout layout = (RelativeLayout)
			// findViewById(R.id.topbar);
			// layout.setBackgroundDrawable(superContext.getResources().getDrawable(
			// R.drawable.navigation_bar_dark));

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.backgroundTheme);
			layout.setBackgroundDrawable(superContext.getResources()
					.getDrawable(R.drawable.background_dark));

			// TextView tv = (TextView) findViewById(R.id.questionnaire_title);
			// tv.setTextColor(superContext.getResources().getColor(android.R.color.white));
		}
	}

	private void setFontSize(View v) {

		try {
			if (v.getClass().equals(EditText.class)) {
				EditText btnView = (EditText) v;

				btnView.setTextSize(UIHelper.getFontSize(superContext,
						btnView.getTextSize()));
				if (Helper.getTheme(superContext) == 0) {
					btnView.setTextColor(superContext.getResources().getColor(
							android.R.color.black));
				}
			}

		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(Button.class)) {
				Button btnView = (Button) v;

				btnView.setTextSize(UIHelper.getFontSize(superContext,
						btnView.getTextSize()));
			}
		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(CheckBox.class)) {
				CheckBox btnView = (CheckBox) v;

				btnView.setTextSize(UIHelper.getFontSize(superContext,
						btnView.getTextSize()));
				if (Helper.getTheme(superContext) == 0) {
					btnView.setTextColor(superContext.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}

		try {
			if (v.getClass().equals(RadioButton.class)) {
				RadioButton btnView = (RadioButton) v;

				btnView.setTextSize(UIHelper.getFontSize(superContext,
						btnView.getTextSize()));
				if (Helper.getTheme(superContext) == 0) {
					btnView.setTextColor(superContext.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}
		try {
			if (v.getClass().equals(TextView.class)) {
				TextView textView = (TextView) v;

				textView.setTextSize(UIHelper.getFontSize(superContext,
						textView.getTextSize()));
				if (Helper.getTheme(superContext) == 0) {
					textView.setTextColor(superContext.getResources().getColor(
							android.R.color.white));
				}
			}
		} catch (Exception ex) {

		}

	}

	private Context superContext;
	private Spinner bcodelistSpinner;
	private Spinner bprojectslistSpinner;
	private Spinner bproplistSpinner;
	protected String branchprop;
	protected String regionname;
	protected String projectname;

	public JobFilterDialog(Context context) {
		super(context);
		superContext = context;
		joblist = (Activity) context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.job_filter_dialog_en);
		joblist.setTitle("Filter settings");
//		setInvertDisplay();
		s_date = (TextView) findViewById(R.id.startdate);
		setFontSize(s_date);
		e_date = (TextView) findViewById(R.id.enddate);
		setFontSize(e_date);
		s_date.setText(joblist.getString(R.string.job_filter_select_date));
		s_date.setPaintFlags(s_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		e_date.setText(joblist.getString(R.string.job_filter_select_date));
		e_date.setPaintFlags(e_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		save_btn = (ImageView) findViewById(R.id.dialog_save);
		jobTypeSpinner = (Spinner) findViewById(R.id.jobtypelist);

		fillSpinner(jobTypeSpinner, "jobtype");
		regionTypeSpinner = (Spinner) findViewById(R.id.regionlist);
		fillSpinner(regionTypeSpinner, "city");
		bregionspinner = (Spinner) findViewById(R.id.fromList);
		fillSpinner(bregionspinner, "region");
		bcodelistSpinner = (Spinner) findViewById(R.id.bcodelist);
		fillSpinner(bcodelistSpinner, "bcode");
		bprojectslistSpinner = (Spinner) findViewById(R.id.bprojectslist);
		fillSpinner(bprojectslistSpinner, "bprojects");
		bproplistSpinner = (Spinner) findViewById(R.id.bproplist);
		fillSpinner(bproplistSpinner, "bprop");
		s_date.setOnClickListener(this);
		e_date.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		setFontSize(findViewById(R.id.Label_enddate));
		setFontSize(findViewById(R.id.Label_sdate));
		setFontSize(findViewById(R.id.Label_jobtypelist));
		setFontSize(findViewById(R.id.Label_regionlist));
		setFontSize(findViewById(R.id.jobtypelist));
		setFontSize(findViewById(R.id.Label_jobfilter_title));
		setFontSize(findViewById(R.id.calandertitle));
		setFontSize(save_btn);
		jobTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				jobtype = ((TextView) arg1).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		bregionspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				region = ((TextView) arg1).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		bprojectslistSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						project = ((TextView) arg1).getText().toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		regionTypeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						city = ((TextView) arg1).getText().toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		bcodelistSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						branchcode = ((TextView) arg1).getText().toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		bproplistSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						branchprop = ((TextView) arg1).getText().toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	private void fillSpinner(Spinner spinner, String type) {
		String[] array = getArraY(type);
		if (array != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(joblist,
					android.R.layout.simple_spinner_item, getArraY(type));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		}
	}

	private String[] getArraY(String str) {
		if (str.equals("bcode"))
			return getbranchCodeArraY();
		else if (str.equals("city"))
			return getCityArraY();
		else if (str.equals("region"))
			return getRegionArraY();
		else if (str.equals("bprojects"))
			return getProjectsArraY();
		else if (str.equals("bprop"))
			return getPropList();
		else
			return getJobTypeArraY();
	}

	private String[] getPropList() {
		if (Orders.getBranchProps() == null)
			return null;
		int count = Orders.getBranchProps().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			String value = Orders.getBranchProps().get(ordercount).getContent();
			String client = Orders.getBranchProps().get(ordercount)
					.getPropertyName()
					+ " - " + value;
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits && client != null)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	private String[] getCityArraY() {
		int count = Orders.getOrders().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
				continue;

			String client = Orders.getOrders().get(ordercount).getCityName();
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits && client != null)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	private String[] getRegionArraY() {
		int count = Orders.getOrders().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
				continue;

			String client = Orders.getOrders().get(ordercount).getRegionName();
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (client != null && vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits && client != null)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	private String[] getProjectsArraY() {
		int count = Orders.getOrders().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
				continue;

			String client = Orders.getOrders().get(ordercount).getProjectName();
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (client != null && vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits && client != null)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	private String[] getbranchCodeArraY() {
		int count = Orders.getOrders().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
				continue;

			String client = Orders.getOrders().get(ordercount).getBranchCode();
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (client != null && vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	private String[] getJobTypeArraY() {
		int count = Orders.getOrders().size();
		Vector<String> vector = new Vector<String>();
		vector.add(joblist.getString(R.string.job_filter_default_dd_option));
		boolean isExits = false;
		for (int ordercount = 0; ordercount < count; ordercount++) {
			isExits = false;
			if (Orders.getOrders().get(ordercount).getOrderID().contains("-"))
				continue;

			String client = Orders.getOrders().get(ordercount).getClientName();
			for (int itemcount = 0; itemcount < vector.size(); itemcount++) {
				if (client != null && vector.get(itemcount).equals(client)) {
					isExits = true;
					break;
				}
			}
			if (!isExits)
				vector.add(client);
		}
		String[] items = new String[vector.size()];
		vector.copyInto(items);
		vector.removeAllElements();
		vector = null;
		return items;
	}

	String getDate() {
		Calendar cl = Calendar.getInstance();
		int y = cl.get(Calendar.YEAR);
		int m = cl.get(Calendar.MONTH);
		int d = cl.get(Calendar.DAY_OF_MONTH);
		return getCurrentDate(d, m, y);
	}

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

	private void openDatePicker() {
		Calendar cl = Calendar.getInstance();
		int y = cl.get(Calendar.YEAR);
		int m = cl.get(Calendar.MONTH);
		int d = cl.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dp = new DatePickerDialog(joblist, setDate, y, m, d);
		dp.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.startdate:
			tag = 1;
			openDatePicker();
			break;
		case R.id.enddate:
			tag = 2;
			openDatePicker();
			break;
		case R.id.dialog_save:
			if (s_date.getText().toString()
					.equals(joblist.getString(R.string.job_filter_select_date))
					|| e_date
							.getText()
							.toString()
							.equals(joblist

									.getString(R.string.job_filter_select_date))) {
				if (joblist instanceof JobListActivity) {
					((JobListActivity) joblist).FilterJobList(new FilterData(region, project,
							branchprop, branchcode, jobtype, city, "1/1/1900",
							"1/1/1900"));
				} else if (joblist instanceof MapActivity) {
					((MapActivity) joblist).FilterJobList(new FilterData(region,
							project, branchprop, branchcode, jobtype, city,
							"1/1/1900", "1/1/1900"));
				}
			} else {
				if ((s_date.getText().toString().equals(joblist
						.getString(R.string.job_filter_select_date)))
						|| (e_date.getText().toString().equals(joblist
								.getString(R.string.job_filter_select_date)))) {
					if (joblist instanceof JobListActivity) {
						((JobListActivity) joblist).FilterJobList(new FilterData(region,
								project, branchprop, branchcode, jobtype, city,
								"", ""));
					} else if (joblist instanceof MapActivity) {
						((MapActivity) joblist).FilterJobList(
								new FilterData(region, project, branchprop, branchcode,
								jobtype, city, "", ""));
					}
				} else {
					if (joblist instanceof JobListActivity) {
						((JobListActivity) joblist).FilterJobList(new FilterData(region,
								project, branchprop, branchcode, jobtype, city,
								s_date.getText().toString(), e_date.getText()
										.toString()));
					} else if (joblist instanceof MapActivity) {
						((MapActivity) joblist).FilterJobList(new FilterData(
								region, project, branchprop, branchcode,
								jobtype, city, s_date.getText().toString(),
								e_date.getText().toString()));
					}

				}
			}
			this.dismiss();
			break;
		}
	}

	private String getCurrentDate(int day, int month, int year) {
		String str = String.valueOf(day) + "/" + monthVal[month] + "/"
				+ String.valueOf(year);
		return str;
	}
}

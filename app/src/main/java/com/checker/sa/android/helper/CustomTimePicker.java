package com.checker.sa.android.helper;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ikovac.timepickerwithseconds.view.MyTimePickerDialog.OnTimeSetListener;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.SettingsActivity;

public class CustomTimePicker implements OnClickListener {

	int hour = 0;
	int mm = 0;
	int ss = 0;
	private Button btnHourPlus;
	private Button btnHourMinus;
	private Button btnMinutePlus;
	private Button btnMinuteMinus;
	private Button btnSecondPlus;
	private Button btnSecondMinus;
	private TextView txtHour;
	private TextView txtMinute;
	private TextView txtSecond;
	private TextView txt_time;
	private Context context;
	private Button btnSet;
	private Button btnCancel;
	private Dialog dialog;

	private OnTimeSetListener mCallback;
	private String mitype;

	public void incrementHour() {
		if (hour == 23)
			hour = 0;
		else
			hour++;
		setHour(hour);
	}

	public void decrementHour() {
		if (hour == 0)
			hour = 23;
		else
			hour--;
		setHour(hour);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {

		this.hour = hour;
		txtHour.setText(String.format("%02d", this.hour));
		setTime();
	}

	public void incrementMinute() {
		if (mm == 59)
			mm = 0;
		else
			mm++;
		setMm(mm);
	}

	public void decrementMinute() {
		if (mm == 0)
			mm = 59;
		else
			mm--;
		setMm(mm);
	}

	public int getMm() {
		return mm;
	}

	public void setMm(int mm) {

		this.mm = mm;
		txtMinute.setText(String.format("%02d", this.mm));
		setTime();
	}

	public void incrementSecond() {
		if (ss == 59)
			ss = 0;
		else
			ss++;
		setSs(ss);
	}

	public void decrementSecond() {
		if (ss == 0)
			ss = 59;
		else
			ss--;
		setSs(ss);
	}

	public int getSs() {
		return ss;
	}

	public void setSs(int ss) {
		this.ss = ss;
		txtSecond.setText(String.format("%02d", this.ss));
		setTime();
	}

	public Dialog makeTimeDialog(Activity activity, String mitype, int hh,
			int mm, int ss, OnTimeSetListener calback) {
		this.mitype = mitype;
		if (mitype == null)
			this.mitype = "";
		mCallback = calback;
		context = (Context) activity;
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		Resources r = activity.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 295,
				r.getDisplayMetrics());

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (mitype == null) {
			dialog.setContentView(R.layout.layout_timepicker);
		} else if (mitype.equals("8")) {
			// secs only
			hh = 0;
			mm = 0;
			dialog.setContentView(R.layout.layout_timepicker_seconds_only);
		} else if (mitype.equals("7")) {
			// MM:SS
			hh = 0;
			dialog.setContentView(R.layout.layout_timepicker_mm_ss_only);
		} else if (mitype.equals("6"))// HH:MM
		{
			ss = 0;
			dialog.setContentView(R.layout.layout_timepicker_hh_mm_only);
		} else {
			dialog.setContentView(R.layout.layout_timepicker);
		}
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(width - 100, height-150);
		dialog.setTitle("");

		btnHourPlus = (Button) dialog.findViewById(R.id.btn_hour_plus);
		btnHourPlus.setOnClickListener(this);
		// btnHourPlus.setTextSize(UIHelper.getFontSize(context,
		// btnHourPlus.getTextSize()));
		btnHourMinus = (Button) dialog.findViewById(R.id.btn_hour_minus);
		btnHourMinus.setOnClickListener(this);
		// btnHourMinus.setTextSize(UIHelper.getFontSize(context,
		// btnHourMinus.getTextSize()));
		btnMinutePlus = (Button) dialog.findViewById(R.id.btn_minut_plus);
		btnMinutePlus.setOnClickListener(this);
		// btnMinutePlus.setTextSize(UIHelper.getFontSize(context,
		// btnMinutePlus.getTextSize()));
		btnMinuteMinus = (Button) dialog.findViewById(R.id.btn_minut_minus);
		btnMinuteMinus.setOnClickListener(this);
		// btnMinuteMinus.setTextSize(UIHelper.getFontSize(context,
		// btnMinuteMinus.getTextSize()));
		btnSecondPlus = (Button) dialog.findViewById(R.id.btn_second_plus);
		btnSecondPlus.setOnClickListener(this);
		// btnSecondPlus.setTextSize(UIHelper.getFontSize(context,
		// btnSecondPlus.getTextSize()));
		btnSecondMinus = (Button) dialog.findViewById(R.id.btn_second_minus);
		btnSecondMinus.setOnClickListener(this);
		// btnSecondMinus.setTextSize(UIHelper.getFontSize(context,
		// btnSecondMinus.getTextSize()));

		btnSet = (Button) dialog.findViewById(R.id.btn_set);
		btnSet.setOnClickListener(this);
		btnSet.setTextSize(UIHelper.getFontSize(context, btnSet.getTextSize()));
		btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		btnCancel.setTextSize(UIHelper.getFontSize(context,
				btnCancel.getTextSize()));
		btnCancel.setText(context.getString(R.string.button_cancel));
		btnSet.setText(context.getString(R.string.button_set));
		txtHour = (TextView) dialog.findViewById(R.id.text_hour);
		txtHour.setTextSize(UIHelper.getFontSize(context, txtHour.getTextSize()));
		txtMinute = (TextView) dialog.findViewById(R.id.txt_minute);
		txtMinute.setTextSize(UIHelper.getFontSize(context,
				txtMinute.getTextSize()));
		txtSecond = (TextView) dialog.findViewById(R.id.txt_second);
		txtSecond.setTextSize(UIHelper.getFontSize(context,
				txtSecond.getTextSize()));
		txt_time = (TextView) dialog.findViewById(R.id.txt_time);
		txt_time.setTextSize(UIHelper.getFontSize(context,
				txt_time.getTextSize()));

		setHour(hh);
		setMm(mm);
		setSs(ss);

		setTime();

		return dialog;
	}

	private void setTime() {
		if (mitype.equals("8")) {
			txt_time.setText(String.format("%02d", ss));
		} else if (mitype.equals("7")) {
			// MM:SS
			txt_time.setText(String.format("%02d:%02d", mm, ss));
		} else if (mitype.equals("6"))// HH:MM
		{
			txt_time.setText(String.format("%02d:%02d", hour, mm));
		} else {
			txt_time.setText(String.format("%02d:%02d:%02d", hour, mm, ss));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_hour_plus:
			incrementHour();
			break;
		case R.id.btn_hour_minus:
			decrementHour();
			break;
		case R.id.btn_minut_plus:
			incrementMinute();
			break;
		case R.id.btn_minut_minus:
			decrementMinute();
			break;
		case R.id.btn_second_plus:
			incrementSecond();
			break;
		case R.id.btn_second_minus:
			decrementSecond();
			break;
		case R.id.btn_set:
			mCallback.onTimeSet(null, hour, mm, ss);
			dialog.dismiss();
			break;
		case R.id.btn_cancel:
			dialog.dismiss();
			break;
		}
		;
	}
}

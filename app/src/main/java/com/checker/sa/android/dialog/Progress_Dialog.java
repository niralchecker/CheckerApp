package com.checker.sa.android.dialog;

import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class Progress_Dialog extends ProgressDialog {

	Context context;
	ProgressDialog dialog;
	private String msg;
	private String addition = "";

	public Progress_Dialog(Context context) {
		super(context);
		this.context = context;
//		this.setIndeterminateDrawable(context.getResources().getDrawable(
//				R.drawable.progress_drawable));
	}

	public Progress_Dialog(Context context, int max) {
		super(context);
		this.context = context;
		setProgressBar(max);
	}

	public void setProgressBar(int max) {
		this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		this.setMax(max);
		this.setProgress(0);
	}

	public void setProgressDialog() {
		this.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	}

	public void changeMessage(final String newMEssage) {
		this.msg = newMEssage;
		((Activity) context).runOnUiThread(changeMessage);
	}

	public void AddMessage(final String newMEssage) {
		if (this.msg == null)
			this.msg = context.getResources().getString(
					R.string.prepare_screen_dialogue);
		this.addition = newMEssage;
		((Activity) context).runOnUiThread(addMessage);
	}

	public void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setMessage(context
				.getString(R.string.jd_please_alert_msg));
//		dialog.setIndeterminateDrawable(context.getResources().getDrawable(
//				R.drawable.progress_drawable));
		dialog.show();
	}

	private Runnable changeMessage = new Runnable() {
		@Override
		public void run() {
			try {
				dialog.setMessage(msg);
			} catch (Exception ex) {

			}
		}
	};

	private Runnable addMessage = new Runnable() {
		@Override
		public void run() {
			try {
				dialog.setMessage(msg + " " + addition);
			} catch (Exception ex) {

			}
		}
	};

	public void onPreExecute(String message) {
		try {

			dialog = new ProgressDialog(context);
			dialog.setMessage(message);
//			dialog.setIndeterminateDrawable(context.getResources().getDrawable(
//					R.drawable.progress_drawable));
			dialog.show();
			dialog.setCancelable(false);
		} catch (Exception e) {

		}
	}

	public void onPostExecute() {
		try {
			if (dialog != null)
				dialog.dismiss();
		} catch (Exception e) {

		}
	}
}

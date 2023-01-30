package com.checker.sa.android.data;

import android.app.Dialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UploadingProgressBars {
	private ProgressBar progressBarJobs;
	private ProgressBar progressBarImages;

	private TextView tprogressBarJobs;
	private TextView tprogressBarImages;
	private View bluemiddle;
	private Dialog dialog;

	public View getBluemiddle() {
		return bluemiddle;
	}

	public void setBluemiddle(View bluemiddle) {
		this.bluemiddle = bluemiddle;
	}

	public TextView getTprogressBarJobs() {
		return tprogressBarJobs;
	}

	public void setTprogressBarJobs(TextView tprogressBarJobs) {
		this.tprogressBarJobs = tprogressBarJobs;
	}

	public void setTprogressBarJobsText(String tprogressBarJobs) {
		this.tprogressBarJobs.setText(tprogressBarJobs);
	}

	public void setprogressBarJobsProgress(int tprogressBarJobs) {
		this.progressBarJobs.setProgress(tprogressBarJobs);
	}

	public void setprogressBarJobsMax(int tprogressBarJobs) {
		this.progressBarJobs.setMax(tprogressBarJobs);
	}

	public TextView getTprogressBarImages() {
		return tprogressBarImages;
	}

	public void setTprogressBarImages(TextView tprogressBarImages) {
		this.tprogressBarImages = tprogressBarImages;
	}

	public void setprogressBarImagesMax(int tprogressBarJobs) {
		this.progressBarImages.setMax(tprogressBarJobs);
	}

	public void setTprogressBarImagesText(String tprogressBarJobs) {
		this.tprogressBarImages.setText(tprogressBarJobs);
		if (bluemiddle!=null){
		if (tprogressBarJobs==null || tprogressBarJobs.equals("")) bluemiddle.setVisibility(RelativeLayout.INVISIBLE);
		else bluemiddle.setVisibility(RelativeLayout.VISIBLE);}
	}

	public void setprogressBarImagesProgress(int tprogressBarJobs) {
		this.progressBarImages.setProgress(tprogressBarJobs);
	}

	public ProgressBar getProgressBarImages() {
		return progressBarImages;
	}

	public void setProgressBarImages(ProgressBar progressBarImages) {
		this.progressBarImages = progressBarImages;
	}

	public ProgressBar getProgressBarJobs() {
		return progressBarJobs;
	}

	public void setProgressBarJobs(ProgressBar progressBarJobs) {
		this.progressBarJobs = progressBarJobs;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	public Dialog getDialog() {
		return dialog;
	}
	public void dismissDialog() {
		this.dialog.dismiss();
	}

}

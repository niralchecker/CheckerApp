package com.mor.sa.android.activities;

import org.apache.commons.lang.SerializationUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.checker.sa.android.data.Order;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.helper.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class comunicator extends WearableListenerService implements
		ConnectionCallbacks, OnConnectionFailedListener {
	private static final String START_ACTIVITY = "/start_activity";
	private static final String START_JOB = "/start_job";
	private static final String START_JOB_DETAIL = "/start_job_detail";
	private static final String SWITCH_TAB = "/SWITCH_TAB";
	private static final String NEXT_BTN = "/NEXT_BTN";
	private static final String PREV_BTN = "/PREV_BTN";
	private static final String FINISH_BTN = "/FINISH_BTN";
	private static final String START_DOWNLOADING = "/START_DOWNLOADING";
	private static final String START_UPLOADING = "/START_UPLOADING";
	private static final String GOTO_MAIN = "GOTO_MAIN";
	private final int JOB_DETAIL_ACTIVITY_CODE = 2;

	private GoogleApiClient mApiClient;
	private SharedPreferences prefs;

	public static Activity detailJob = null;
	public static Activity questionJob = null;
	public static Activity JobList = null;

	@Override
	public void onCreate() {
		Log.d("", "##DataService created");
		super.onCreate();
		if (null == mApiClient) {
			mApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
		}

		if (!mApiClient.isConnected()) {
			mApiClient.connect();
			Log.d("", "##Api connecting");
		}
	}

	@Override
	public void onDestroy() {
		Log.d("", "##DataService destroyed");
		if (null != mApiClient) {
			if (mApiClient.isConnected()) {
				mApiClient.disconnect();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		Log.d("", "##DataService Data changed");
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle arg0) {
	}

	@Override
	public void onConnectionSuspended(int arg0) {
	}

	@Override
	public void onMessageReceived(final MessageEvent messageEvent) {
		if (messageEvent.getPath().equalsIgnoreCase(GOTO_MAIN)) {
			if (comunicator.questionJob != null) {
				((QuestionnaireActivity) comunicator.questionJob)
						.finish_screen();
			}

		} else if (messageEvent.getPath().equalsIgnoreCase(FINISH_BTN)) {
			if (comunicator.questionJob != null) {
				((QuestionnaireActivity) comunicator.questionJob).finish_job();
			}

		} else if (messageEvent.getPath().equalsIgnoreCase(START_DOWNLOADING)) {
			if (comunicator.JobList != null) {
				comunicator.JobList.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((JobListActivity) comunicator.JobList)
								.startDownloadingJobs(false, false);
					}
				});

			}

		} else if (messageEvent.getPath().equalsIgnoreCase(START_UPLOADING)) {
			if (comunicator.JobList != null) {
				comunicator.JobList.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((JobListActivity) comunicator.JobList)
								.start_uploading(false);
					}
				});

			}

		} else if ((messageEvent.getPath().equalsIgnoreCase(SWITCH_TAB) || messageEvent
				.getPath().equalsIgnoreCase(START_JOB_DETAIL))
				&& comunicator.JobList != null) {
			if (messageEvent.getPath().equalsIgnoreCase(SWITCH_TAB)) {

				comunicator.JobList.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						byte[] daa = messageEvent.getData();
						String dataa = new String(daa);
						((JobListActivity) comunicator.JobList)
								.switch_tab(dataa);
					}
				});
			} else if (messageEvent.getPath()
					.equalsIgnoreCase(START_JOB_DETAIL)) {
				byte[] daa = messageEvent.getData();
				orderListItem o = (orderListItem) SerializationUtils
						.deserialize(daa);
				final Intent intent = new Intent(
						((JobListActivity) comunicator.JobList),
						JobDetailActivity.class);
				intent.putExtra("OrderID", o.orderItem.getOrderID());
				intent.putExtra(Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
						o.orderItem.getCount() + "");
				if (o.surveyItem != null)
					intent.putExtra("SurveyID", o.surveyItem.getSurveyID());

				if (o != null) {
					if (o.orderItem != null) {
						intent.putExtra("OrderID", o.orderItem.getOrderID());
						intent.putExtra(
								Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER,
								o.orderItem.getCount() + "");
					} else if (o.surveyItem != null) {
						intent.putExtra("SurveyID", o.surveyItem.getSurveyID());
					}
					comunicator.JobList.runOnUiThread(new Runnable() {
						@Override
						public void run() {

							((JobListActivity) comunicator.JobList)
									.callJobDetail(intent,
											JOB_DETAIL_ACTIVITY_CODE);
						}
					});

				}
				// else
				// intent.putExtra(
				// Constants.POST_FIELD_JOB_DETAIL_GROUPED_NUMBER, "1");

			}

		} else if (messageEvent.getPath().equalsIgnoreCase(START_JOB)) {
			if (comunicator.detailJob != null) {

				((JobListActivity) comunicator.detailJob).start_job();
			}

		} else if (comunicator.questionJob != null) {
			byte[] daa = messageEvent.getData();
			final com.mor.sa.android.data.WatchObject o = (com.mor.sa.android.data.WatchObject) SerializationUtils
					.deserialize(daa);

			if (messageEvent.getPath().equalsIgnoreCase(NEXT_BTN)) {
				comunicator.questionJob.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((QuestionnaireActivity) comunicator.questionJob)
								.pressNextBtn(o);
					}
				});

			}
			if (messageEvent.getPath().equalsIgnoreCase(PREV_BTN)) {
				comunicator.questionJob.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((QuestionnaireActivity) comunicator.questionJob)
								.pressBackBtn(o);
					}
				});

			}
		}
	}
}

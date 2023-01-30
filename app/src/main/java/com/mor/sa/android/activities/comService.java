package com.mor.sa.android.activities;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.checker.sa.android.data.BasicLog;
import com.checker.sa.android.data.Job;
import com.checker.sa.android.data.JobToNotify;
import com.checker.sa.android.data.parser.Parser;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.transport.Connector;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.JobBoardActivityFragment;
import com.google.maps.android.SphericalUtil;
import com.mor.sa.android.activities.R;

public class comService extends Service {

	protected static boolean UpdateIcon = false;
	protected double lng2;
	protected double lat2;
	protected double lat1;
	protected double lng1;
	private comService context;
	protected LocationManager locationManager;
	private Location lastLocation = null;
	LocationListener locationListener;
	String apptitle = "";
	private SharedPreferences myPrefs;

	@Override
	public void onDestroy() {

		hideIcon();

		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
        boolean b=myPrefs.getBoolean(
                Constants.SETTINGS_switchtracking, false);
        if (b) {
//            Log.i("EXIT", "ondestroy!");
//            Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
//            SplashScreen.addServiceLog(new BasicLog(
//                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
//                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "destroyed by OS", ""));
//
//            // ///////////////////////////////////////////////////////////
//            Intent i = new Intent(context, comService.class);
//            i.putExtra("KEY1", "Value to be used by the service");
//            startService(i);
//            sendBroadcast(broadcastIntent);
        }
        else {
            SplashScreen.addServiceLog(new BasicLog(
                    myPrefs.getString(Constants.SETTINGS_SYSTEM_URL_KEY, ""),
                    myPrefs.getString(Constants.POST_FIELD_LOGIN_USERNAME, ""), "destroyed by user", ""));

            hideIcon();
        }
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Helper.setAN168Log("ser 75");
		if (getPackageName() != null
				&& getPackageName().contains(Helper.CONSTPACKAGEPREFIX)) {
			apptitle = Helper.serviceTitle;
		} else
			apptitle = getString(R.string.app_title);
		Helper.setAN168Log("ser 81");
		context = this;
		showVishavIcon("notify_001",context.getResources().getString(
				R.string.s_item_column_0_line_206_file_190),apptitle,"icon",notifyID,true,false,pi);
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
										Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}



			@Override
			public void onLocationChanged(Location currentLocation) {
				if (currentLocation != null
						&& currentLocation.getLatitude() != 0.0
						&& currentLocation.getLongitude() != 0.0) {
					Location loc2 = new Location("");
					loc2.setLatitude(lat1);
					loc2.setLongitude(lng1);
					if (lastLocation == null
							|| currentLocation.distanceTo(lastLocation) > 400) {
						lastLocation = currentLocation;
						getLocation(lat1, lng1, 500);
						refresh_submit(
								true,
								toBounds(
										new LatLng(currentLocation
												.getLatitude(), currentLocation
												.getLongitude()), 700));
					}
				}
			}
		};
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 10, 250, locationListener);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					10, 400, locationListener);
	}

	public void getLocation(double x0, double y0, int radius) {

		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(Math.toRadians(y0));

		lng2 = new_x + x0;
		lat2 = y + y0;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}


    @Override
	public IBinder onBind(Intent intent) {
		String action = intent.getExtras().getString("action");
		switch (action) {
		case "uploading":

			break;
		}


		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
				context).setContentTitle(action).setContentText(apptitle)
				.setContentIntent(pi).setSmallIcon(R.drawable.icon);

		Notification n = mNotifyBuilder.build();
		n.flags |= Notification.FLAG_ONGOING_EVENT;
		// mNotificationManager.notify(notifyID, n);
		return null;
	}

	public PendingIntent setNotification(LatLngBounds lt) {
		Intent notificationIntent = new Intent(getApplicationContext(),
				JobBoardActivityFragment.class);
		if (lt==null)
		{
			notificationIntent.putExtra("lat1", 0.0);
			notificationIntent.putExtra("lng1", 0.0);
			notificationIntent.putExtra("lat2", 0.0);
			notificationIntent.putExtra("lng2", 0.0);

		}
		else
		{
			notificationIntent.putExtra("lat1", lt.getCenter().latitude);
			notificationIntent.putExtra("lng1", lt.getCenter().longitude);
			notificationIntent.putExtra("lat2", lat2);
			notificationIntent.putExtra("lng2", lng2);

		}
		notificationIntent.putExtra("service", true);

		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return PendingIntent.getActivity(getApplicationContext(), 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void hideIcon() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) context
				.getSystemService(ns);
		nMgr.cancelAll();
	}

	private int getIcon(String iconName) {
		int newResImgId = 0;
		int ResImgId = this.getResources().getIdentifier(iconName, "drawable",
				this.getPackageName());
		if (Helper.getSystemURL() != null
				&& Helper.getSystemURL().toLowerCase()
						.contains(Helper.CONST_BE_THERE)) {
			iconName = Helper.imgprefix + iconName;
			newResImgId = this.getResources().getIdentifier(iconName,
					"drawable", this.getPackageName());
			if (newResImgId == 0) {
				return ResImgId;
			}
			return newResImgId;
		}
		return ResImgId;
	}

public void showVishavIcon(String channelId,String bigtext,String apptitle,String icon,int nid,boolean ongoing,boolean soundAndVibrate,PendingIntent pendingIntent)
{
	NotificationCompat.Builder mBuilder =
			new NotificationCompat.Builder(context,channelId);
	Intent ii = new Intent(context, JobBoardActivityFragment.class);


	NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
	bigText.bigText(bigtext);
	bigText.setBigContentTitle(apptitle);
	bigText.setSummaryText("");

	mBuilder.setContentIntent(pendingIntent);
	mBuilder.setSmallIcon(getIcon(icon));
	mBuilder.setContentTitle(apptitle);
	mBuilder.setContentText(bigtext);
	mBuilder.setPriority(Notification.PRIORITY_MAX);
	mBuilder.setStyle(bigText);
	mBuilder.setContentIntent(setNotification(null));
	if (ongoing)
		mBuilder.setOngoing(true);

	if (soundAndVibrate) {
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		mBuilder.setVibrate(new long[] { 1000, 1000});
	}
	mNotificationManager =
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

		NotificationChannel channel = new NotificationChannel(channelId,
				apptitle,
				NotificationManager.IMPORTANCE_DEFAULT);
		mNotificationManager.createNotificationChannel(channel);
		mBuilder.setChannelId(channelId);
	}

	mNotificationManager.notify(nid, mBuilder.build());
}
	public void showIcon() {
		try {
//			intent = new Intent(this, JobBoardActivityFragment.class);
//			intent.putExtra("service", true);
//			stackBuilder = TaskStackBuilder.create(this);
//			stackBuilder.addParentStack(JobBoardActivityFragment.class);
//			stackBuilder.addNextIntent(intent);
//			pi = stackBuilder.getPendingIntent(0,
//					PendingIntent.FLAG_UPDATE_CURRENT);

			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// Sets an ID for the notification, so it can be updated

			context = this;

			NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
					context)
					.setContentTitle(apptitle)
					.setContentText(
							context.getResources().getString(
									R.string.s_item_column_0_line_206_file_190))
					.setContentIntent(pi).setSmallIcon(getIcon("icon"))
					.setOngoing(true);

			Notification n = mNotifyBuilder.build();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				String channelId = "YOUR_CHANNEL_ID";
				NotificationChannel channel = new NotificationChannel(channelId,
						"Checker",
						NotificationManager.IMPORTANCE_DEFAULT);
				mNotificationManager.createNotificationChannel(channel);
				mNotifyBuilder.setChannelId(channelId);
			}


			mNotificationManager.notify(notifyID, n);

			final ScheduledExecutorService exec = Executors
					.newScheduledThreadPool(1);

			exec.schedule(new Runnable() {
				@Override
				public void run() {
					ActivityManager activityManager = (ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningTaskInfo> services = activityManager
							.getRunningTasks(Integer.MAX_VALUE);

					RunningTaskInfo runningTaskInfo = services.get(0);
					ComponentName componentName = runningTaskInfo.topActivity;
					String packageName = componentName.getPackageName()
							.toString();
					String packageName_Context = context.getPackageName()
							.toString();

					if (!packageName.equalsIgnoreCase(packageName_Context)) {
						try {
							Log.i("ReaderService", "ThreadPool Shutdown");
							exec.shutdown();
							Log.i("ReaderService", "STOPPED");
							stopSelf();
						} catch (Exception e) {
                            Helper.setAN168Log("crash 306");
						}
					}

					if (comService.UpdateIcon) {
						comService.UpdateIcon = false;
						NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
								context).setContentTitle(apptitle)
								.setContentText(apptitle).setContentIntent(pi)
								.setSmallIcon(getIcon("icon"));

						Notification n = mNotifyBuilder.build();
						n.flags |= Notification.FLAG_ONGOING_EVENT;
						long time = new Date().getTime();
						String tmpStr = String.valueOf(time);
						String last4Str = tmpStr.substring(tmpStr.length() - 5);
						int notificationId = Integer.valueOf(last4Str);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							String channelId = "YOUR_CHANNEL_ID";
							NotificationChannel channel = new NotificationChannel(channelId,
									"Checker",
									NotificationManager.IMPORTANCE_DEFAULT);
							mNotificationManager.createNotificationChannel(channel);
							mNotifyBuilder.setChannelId(channelId);
						}
						mNotificationManager.notify(notifyID, n);

					}
				}
			}, 500, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
            Helper.setAN168Log("crash 306");

		}

	}

	public String tempText = "";
	Intent intent = null;
	TaskStackBuilder stackBuilder = null;
	PendingIntent pi = null;
	public static NotificationManager mNotificationManager = null;
	int notifyID = 1;
	private String gStartDate;
	private String gEndDate;

	public LatLngBounds toBounds(LatLng center, double radius) {
		LatLng southwest = SphericalUtil.computeOffset(center,
				radius * Math.sqrt(2.0), 225);
		LatLng northeast = SphericalUtil.computeOffset(center,
				radius * Math.sqrt(2.0), 45);
		return new LatLngBounds(southwest, northeast);
	}

	public void refresh_submit(final boolean isRefresh, final LatLngBounds lt) {
		class JobTask extends AsyncTask<Void, Integer, ArrayList<Job>> {

			@Override
			protected void onPreExecute() {
				if (Helper.getSystemURL() == null
						|| Helper.getSystemURL().equals("")) {
					SharedPreferences myPrefs = getSharedPreferences("pref",
							MODE_PRIVATE);
					Helper.setAlternateSystemURL(myPrefs.getString(
							Constants.SETTINGS_ALTERNATE_SYSTEM_URL_KEY, null));
					Helper.setSystemURL(myPrefs.getString(
							Constants.SETTINGS_SYSTEM_URL_KEY, ""));
				}
			}

			@Override
			protected void onPostExecute(ArrayList<Job> result) {
				if (result != null && result.size() > 0) {
					ArrayList<JobToNotify> nList = getjobstoNotify(result);
					for (int i = 0; i < nList.size(); i++) {

						lat1 = lt.getCenter().latitude;
						lng1 = lt.getCenter().longitude;

						String notificationTitle = " "
								+ getResources()
										.getString(
												R.string.s_item_column_0_line_321_file_190);
						if (nList.get(i).getCount() == 1) {
							notificationTitle = " "
									+ getResources()
											.getString(
													R.string.s_item_column_0_line_323_file_190);
						}

						NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
								context)
								.setContentTitle(
										nList.get(i).getCount()
												+ notificationTitle)
								.setContentText(
										nList.get(i).getClienName() + ": "
												+ nList.get(i).getBranchNames())
								.setContentIntent(setNotification(lt))
								.setSmallIcon(getIcon("icon"));

						Notification n = mNotifyBuilder.build();
						// n.flags |= Notification.FLAG_ONGOING_EVENT;
						if (i == 0) {
							n.defaults |= Notification.DEFAULT_SOUND;
							n.defaults |= Notification.DEFAULT_VIBRATE;
						}
						long time = new Date().getTime();
						BigInteger bigInt = new BigInteger((nList.get(i)
								.getClienName() + ": " + nList.get(i)
								.getBranchNames()).getBytes());
						int notificationId = bigInt.intValue();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							String channelId = "YOUR_CHANNEL_ID";
							NotificationChannel channel = new NotificationChannel(channelId,
									"Checker",
									NotificationManager.IMPORTANCE_DEFAULT);
							mNotificationManager.createNotificationChannel(channel);
							mNotifyBuilder.setChannelId(channelId);
						}
						mNotificationManager.notify(notificationId, n);
						//showVishavIcon("notify_001",context.getResources().getString(
						//				R.string.s_item_column_0_line_206_file_190),apptitle,"icon",0,true,false,pi);
						//
						showVishavIcon("notify_001",nList.get(i).getCount()
								+ notificationTitle,
								nList.get(i).getClienName() + ": "
										+ nList.get(i).getBranchNames(),"icon",notificationId,false,i==0,setNotification(lt)
								);
					}
				} else {
					NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
							context)
							.setContentTitle(apptitle)
							.setContentText(
									getString(R.string.s_item_column_0_line_206_file_190))
							.setContentIntent(setNotification(lt))
							.setSmallIcon(getIcon("icon"));//icon

					Notification n = mNotifyBuilder.build();
					n.flags |= Notification.FLAG_ONGOING_EVENT;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						String channelId = "YOUR_CHANNEL_ID";
						NotificationChannel channel = new NotificationChannel(channelId,
								"Checker",
								NotificationManager.IMPORTANCE_DEFAULT);
						mNotificationManager.createNotificationChannel(channel);
						mNotifyBuilder.setChannelId(channelId);
					}
					//mNotificationManager.notify(notifyID, n);
				}
			}

			@Override
			protected ArrayList<Job> doInBackground(Void... params) {
				String data = BoardListPost(lt);
				if (data.contains("<script>")) {
					doLogin();
					data = BoardListPost(lt);
				}
				Parser parser = new Parser();
				for (int i = 0; i <= 9; i++) {
					data = data.replace("<" + i, "<job" + i);
					data = data.replace("</" + i, "</job" + i);
				}
				parser.parseXMLValues("<jobs>" + data + "</jobs>",
						Constants.JOB_RESP_FIELD_PARAM);

				// Save Array list here
				String name_of_file = Constants.getBoardListFile("9.7",
						gStartDate, gEndDate, lat1 + "", lng1 + "", lat2 + "",
						lng2 + "");
				ArrayList<Job> unAppliedJobs = new ArrayList<Job>();
				for (int i = 0; i < parser.jobBoardList.size(); i++) {
					if (parser.jobBoardList.get(i) != null
							&& parser.jobBoardList.get(i).getoaID() != null
							&& parser.jobBoardList.get(i).getoaID().length() > 0) {
					} else
						unAppliedJobs.add(parser.jobBoardList.get(i));
				}
				return unAppliedJobs;
			}
		}
		JobTask jobtaskobj = new JobTask();
		jobtaskobj.execute();
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

	private String BoardListPost(LatLngBounds lt) {
		// Initialize the login data to POST
		List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
		String app_ver = "";
		try {
			app_ver = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}

		double latitude = lat1;
		double longitude = lng1;

		// 6378000 Size of the Earth (in meters)
		double longitudeD = (Math.asin(1000 / (6378000 * Math.cos(Math.PI
				* latitude / 180))))
				* 180 / Math.PI;
		double latitudeD = (Math.asin((double) 1000 / (double) 6378000)) * 180
				/ Math.PI;

		// lat1 = NorthEastCorner.latitude;
		// lng1 = NorthEastCorner.longitude;
		// lat2 = SouthWestCorner.latitude;
		// lng2 = SouthWestCorner.longitude;

		double latitudeMin = lt.southwest.latitude;
		double latitudeMax = lt.northeast.latitude;
		double longitudeMin = lt.southwest.longitude;
		double longitudeMax = lt.northeast.longitude;
		//
		// latitudeMax = 1000.00;
		// latitudeMin = -1000.00;
		// longitudeMax = 1000.00;
		// longitudeMin = -1000.00;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		gStartDate = dateFormat.format(cal.getTime());
		gEndDate = dateFormat.format(cal.getTime());
		// http://checker.co.il/testing/c_pda-job-board.php?ver=9.0&json=1&date_start=2016-12-26&date_end=2016-12-29&lat1=28.644800&long1=77.216721&lat2=31.771959&long2=78.217018
		String name_of_file = Constants.getBoardListFile("9.7", gStartDate,
				gEndDate, latitudeMin + "", longitudeMin + "",
				latitudeMax + "", longitudeMax + "");
		return Connector.postForm(Constants.getBoardListURL("9.7", gStartDate,
				gEndDate, latitudeMin + "", longitudeMin + "",
				latitudeMax + "", longitudeMax + ""), extraDataList);
	}

	public ArrayList<JobToNotify> getjobstoNotify(ArrayList<Job> result) {
		ArrayList<JobToNotify> tempArrayofjobs = new ArrayList<JobToNotify>();
		Boolean isMatch = false;
		for (int i = 0; i < result.size(); i++) {
			isMatch = false;
			for (int j = 0; j < tempArrayofjobs.size(); j++) {
				if (result.get(i).getClientName() == tempArrayofjobs.get(j)
						.getClienName()
						&& result.get(i).getBranchName() == tempArrayofjobs
								.get(j).getBranchNames()) {
					tempArrayofjobs.get(j).setCount(
							tempArrayofjobs.get(j).getCount() + 1);
					isMatch = true;
					break;
				}
			}
			if (!isMatch) {
				JobToNotify tempObject = new JobToNotify();
				tempObject.setCount(1);
				tempObject.setClienName(result.get(i).getClientName());
				tempObject.setBranchNames(result.get(i).getBranchName());
				tempArrayofjobs.add(tempObject);
			}

		}
		return tempArrayofjobs;
	}
}
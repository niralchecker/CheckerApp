package com.checker.sa.android.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;

import com.checker.sa.android.data.locationParams;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.transport.Connector;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class locationThread implements LocationListener {
	Context ctx;
	SharedPreferences myPrefs;
	private String user;
	public Boolean isPost = true;

	public void onLocationChangedMethod() {

		final locationThread locationListener = this;
		final LocationManager locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Helper.loc = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			// locationManager.requestLocationUpdates(
			// LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		}
		if ((!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || Helper.loc == null)
				&& locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

			Helper.loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			// locationManager.requestLocationUpdates(
			// LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
		if (Helper.loc != null && distance(Helper.loc, Helper.lastLoc) > 50) {

			double latitude = Helper.loc.getLatitude();
			latitude += 0;
			double longitude = Helper.loc.getLongitude();
			locationParams lParams = new locationParams();
			lParams.setLat(Helper.loc.getLatitude() + "");
			lParams.setLon(Helper.loc.getLongitude() + "");
			lParams.setUserId(user);
			if (isPost)
				Connector.postForm(Constants.getLocationURL(),
						PrepareLocationNameValuePair(lParams));
			Helper.lastLoc = new Location(Helper.loc);
			Helper.lastLoc.setLatitude(Helper.lastLoc.getLatitude() + 0);
			Helper.lastLoc.setLongitude(Helper.lastLoc.getLongitude() + 0);

		}
	}

	private double distance(Location loc2, Location lastLoc2) {
		try {
			if (loc2 != null && lastLoc2 == null)
				return 51;
			else if (loc2 != null) {
				double dist = loc2.distanceTo(lastLoc2);
				;
				return dist;
			} else
				return 0.0;
		} catch (Exception ex) {
			return 0.0;
		}
	}

	@Override
	public void onLocationChanged(Location loc) {
		Helper.loc = loc;
	}

	private List<NameValuePair> PrepareLocationNameValuePair(
			locationParams lParams) {

		List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();
		extraDataList.add(Helper.getNameValuePair(
				Constants.POST_FIELD_LOC_USERID, lParams.getUserId()));
		extraDataList.add(Helper.getNameValuePair(
				Constants.POST_FIELD_LOC_LONG, lParams.getLon()));
		extraDataList.add(Helper.getNameValuePair(Constants.POST_FIELD_LOC_LAT,
				lParams.getLat()));

		return extraDataList;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	public void startLocationThread(final Context context, final String user) {
		ctx = context;
		this.user = user;

		final locationThread locationListener = this;
		final LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		try {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Helper.loc = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, locationListener);

			}
			if ((!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER) || Helper.loc == null)
					&& locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

				Helper.loc = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0,
						locationListener);
			}

			Timer t = new Timer();
			// Set the schedule function and rate
			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					// Called each time when 1000 milliseconds (1 second) (the
					// period parameter)
					onLocationChangedMethod();
				}

			},
			// Set how long before to start calling the TimerTask (in
			// milliseconds)
					0,
					// Set the amount of time between each execution (in
					// milliseconds)
					5000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}

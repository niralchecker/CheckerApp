//
// This class is used for getting current device location 
// In this class we implement location manager for getting current device location 
// This class get application context as constructor
// This class have fields are latitude, longitude and altitude 
// This class have location field in which latitude and longitude both exist
// this class have getLocation function that returns current device location
//

package com.google;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationGPS implements LocationListener {

	private final Context mContext;
	public boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;

	Location location;
	public static double latitude;
	public static double longitude;
	double altitude;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

	protected LocationManager locationManager;

	public LocationGPS(Context context) {
		this.mContext = context;
		getLocation();
	}

	// This function get current devices location
	// getting current devices location when each time it called
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			Log.v("isGPSEnabled", "=" + isGPSEnabled);

			// getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

			if (isGPSEnabled == false && isNetworkEnabled == false) {
				// showSettingsAlert();
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					location = null;
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							altitude = location.getAltitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("GPS Enabled", "GPS Enabled");
					if (locationManager != null) {
						Location lottt = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (lottt != null) {
							location = lottt;
						}
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							altitude = location.getAltitude();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationGPS.this);
		}
	}

	public String getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		return latitude + "";
	}

	public String getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude + "";
	}

	public String getAltitude() {
		if (location != null) {
			altitude = location.getAltitude();
		}
		return altitude + "";
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	// This function is used to show alert dialog if there is off GPS location
	// setting
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("GPS is settings");
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {

		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();

		// for sending broadcast
		// Intent intent = new Intent(Constants.LOCATION_BROADCAST);
		// intent.putExtra("latitude", latitude);
		// intent.putExtra("longitude", longitude);
		// intent.putExtra("altitude", altitude);
		// mContext.sendBroadcast(intent);

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public String getCurrentCountryName() {
		Geocoder geocoder;
		List<Address> addresses = null;
		geocoder = new Geocoder(mContext, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String address = addresses.get(0).getAddressLine(0);
		// String city = addresses.get(0).getAddressLine(1);
		String country = null;
		if (addresses != null && addresses.size() > 0)
			country = addresses.get(0).getAddressLine(2);

		return country;
	}

}
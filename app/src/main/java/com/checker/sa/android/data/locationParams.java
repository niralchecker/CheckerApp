package com.checker.sa.android.data;

import java.io.Serializable;


public 	class locationParams  implements Serializable
{
	private String userId;
	private String lat,lon;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	
	
}

package com.checker.sa.android.data;

import java.util.ArrayList;

public class Bounds {
	public double lat1;
	public double lat2;
	public double lng1;
	public double lng2;
	public String StartDate;
	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String EndDate;

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	ArrayList<com.checker.sa.android.data.Job> job_array_list;

	public long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getLat1() {
		return lat1;
	}

	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}

	public double getLat2() {
		return lat2;
	}

	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}

	public double getLng1() {
		return lng1;
	}

	public void setLng1(double lng1) {
		this.lng1 = lng1;
	}

	public double getLng2() {
		return lng2;
	}

	public void setLng2(double lng2) {
		this.lng2 = lng2;
	}

	public ArrayList<com.checker.sa.android.data.Job> getJob_array_list() {
		return job_array_list;
	}

	public void setJob_array_list(
			ArrayList<com.checker.sa.android.data.Job> job_array_list) {
		this.job_array_list = job_array_list;
	}
}
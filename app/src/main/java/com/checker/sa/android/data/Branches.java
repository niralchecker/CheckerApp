package com.checker.sa.android.data;

import java.io.Serializable;

public class Branches  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String branchID;
	private String branchName;
	private String branchLat;
	private String branchLong;
	float distance;

	public float getDistance() {
		return distance;
	}

	public String getBranchLat() {
		return branchLat;
	}

	public void setBranchLat(String branchLat) {
		this.branchLat = branchLat;
	}

	public String getBranchLong() {
		return branchLong;
	}

	public void setBranchLong(String branchLong) {
		this.branchLong = branchLong;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getDistanceKms() {
		int kms = (int) this.distance / 1000;
		if (kms > 0)
			return " (" + kms + "Kms)";
		else
			return "";
	}

}

package com.checker.sa.android.data;

import java.io.Serializable;

public class ExpirationItem  implements Serializable{

	String ptoductID;
	String productPropertyID;
	String productLocationID;
	String note;

	int month=-1;
	int year=-1;
	int day=-1;
	
	

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	public String getPtoductID() {
		return ptoductID;
	}
	public void setPtoductID(String ptoductID) {
		this.ptoductID = ptoductID;
	}
	public String getProductPropertyID() {
		return productPropertyID;
	}
	public void setProductPropertyID(String productPropertyID) {
		this.productPropertyID = productPropertyID;
	}
	public String getProductLocationID() {
		return productLocationID;
	}
	public void setProductLocationID(String productLocationID) {
		this.productLocationID = productLocationID;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}

package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class InProgressStampData implements Serializable {
	private String timestamp;
	private String OrderID;
	static final long serialVersionUID = 12545121L;

	public InProgressStampData(String temp, String orderid) {
		this.timestamp = temp;
		this.OrderID = orderid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

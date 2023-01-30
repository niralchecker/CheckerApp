package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class AlternateJob implements Serializable {
	private String orderID;
	private String text;

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

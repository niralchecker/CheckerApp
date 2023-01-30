package org.acra.util;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;

public class CrashData implements Serializable{
	// gameScore.put("system_url", getUserDetails());
	// gameScore.put("set_id", getSetId());
	// gameScore.put("order_id", getOrderId());
	// gameScore.put("phone_details", getPhoneDetails());
	// gameScore.put("stack_trace", textString);
	// gameScore.put("user_comment", comment);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String system_url;
	String set_id;
	String order_id;
	String phone_details;
	String stack_trace;
	String user_comment;

	public String getSystem_url() {
		if (system_url!=null) {
			system_url = system_url.replace(".", "_");
			system_url = system_url.replace(":", "_");
			system_url = system_url.replace("/", "_");
		}

		return system_url;
	}

	public void setSystem_url(String system_url) {
		this.system_url = system_url;
	}

	public String getSet_id() {
		return set_id;
	}

	public void setSet_id(String set_id) {
		this.set_id = set_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getPhone_details() {
		return phone_details;
	}

	public void setPhone_details(String phone_details) {
		this.phone_details = phone_details;
	}

	public String getStack_trace() {
		return stack_trace;
	}

	public void setStack_trace(String stack_trace) {
		this.stack_trace = stack_trace;
	}

	public String getUser_comment() {
		return user_comment;
	}

	public void setUser_comment(String user_comment) {
		this.user_comment = user_comment;
	}

}

package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class SubmitQuestionnaireData implements Serializable {
	static final long serialVersionUID=1L;
	String orderid, rs,ft, slt, slng, elt, elng, ftime, stime, totalSent, unix,setid;

	String DB_TABLE_SUBMITSURVEY_purchase_details = "";
	String DB_TABLE_SUBMITSURVEY_purchase_payment = "";
	String DB_TABLE_SUBMITSURVEY_purchase_description = "";
	String DB_TABLE_SUBMITSURVEY_service_invoice_number = "";
	String DB_TABLE_SUBMITSURVEY_service_payment = "";
	String DB_TABLE_SUBMITSURVEY_service_description = "";
	String DB_TABLE_SUBMITSURVEY_transportation_payment = "";
	String DB_TABLE_SUBMITSURVEY_transportation_description = "";
	private String setVersionID="";

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getSetid() {
		return setid;
	}

	public void setSetid(String setid) {
		this.setid = setid;
	}

	public String getDB_TABLE_SUBMITSURVEY_purchase_details() {
		return DB_TABLE_SUBMITSURVEY_purchase_details;
	}

	public void setDB_TABLE_SUBMITSURVEY_purchase_details(
			String dB_TABLE_SUBMITSURVEY_purchase_details) {
		DB_TABLE_SUBMITSURVEY_purchase_details = dB_TABLE_SUBMITSURVEY_purchase_details;
	}

	public String getDB_TABLE_SUBMITSURVEY_purchase_payment() {
		return DB_TABLE_SUBMITSURVEY_purchase_payment;
	}

	public void setDB_TABLE_SUBMITSURVEY_purchase_payment(
			String dB_TABLE_SUBMITSURVEY_purchase_payment) {
		DB_TABLE_SUBMITSURVEY_purchase_payment = dB_TABLE_SUBMITSURVEY_purchase_payment;
	}

	public String getDB_TABLE_SUBMITSURVEY_purchase_description() {
		return DB_TABLE_SUBMITSURVEY_purchase_description;
	}

	public void setDB_TABLE_SUBMITSURVEY_purchase_description(
			String dB_TABLE_SUBMITSURVEY_purchase_description) {
		DB_TABLE_SUBMITSURVEY_purchase_description = dB_TABLE_SUBMITSURVEY_purchase_description;
	}

	public String getDB_TABLE_SUBMITSURVEY_service_invoice_number() {
		return DB_TABLE_SUBMITSURVEY_service_invoice_number;
	}

	public void setDB_TABLE_SUBMITSURVEY_service_invoice_number(
			String dB_TABLE_SUBMITSURVEY_service_invoice_number) {
		DB_TABLE_SUBMITSURVEY_service_invoice_number = dB_TABLE_SUBMITSURVEY_service_invoice_number;
	}

	public String getDB_TABLE_SUBMITSURVEY_service_payment() {
		return DB_TABLE_SUBMITSURVEY_service_payment;
	}

	public void setDB_TABLE_SUBMITSURVEY_service_payment(
			String dB_TABLE_SUBMITSURVEY_service_payment) {
		DB_TABLE_SUBMITSURVEY_service_payment = dB_TABLE_SUBMITSURVEY_service_payment;
	}

	public String getDB_TABLE_SUBMITSURVEY_service_description() {
		return DB_TABLE_SUBMITSURVEY_service_description;
	}

	public void setDB_TABLE_SUBMITSURVEY_service_description(
			String dB_TABLE_SUBMITSURVEY_service_description) {
		DB_TABLE_SUBMITSURVEY_service_description = dB_TABLE_SUBMITSURVEY_service_description;
	}

	public String getDB_TABLE_SUBMITSURVEY_transportation_payment() {
		return DB_TABLE_SUBMITSURVEY_transportation_payment;
	}

	public void setDB_TABLE_SUBMITSURVEY_transportation_payment(
			String dB_TABLE_SUBMITSURVEY_transportation_payment) {
		DB_TABLE_SUBMITSURVEY_transportation_payment = dB_TABLE_SUBMITSURVEY_transportation_payment;
	}

	public String getDB_TABLE_SUBMITSURVEY_transportation_description() {
		return DB_TABLE_SUBMITSURVEY_transportation_description;
	}

	public void setDB_TABLE_SUBMITSURVEY_transportation_description(
			String dB_TABLE_SUBMITSURVEY_transportation_description) {
		DB_TABLE_SUBMITSURVEY_transportation_description = dB_TABLE_SUBMITSURVEY_transportation_description;
	}

	private ArrayList<Quota> quotas;

	public String getUnix() {
		return unix;
	}

	public void setUnix(String unix) {
		this.unix = unix;
	}

	private String SID;

	private String tries;

	public ArrayList<Quota> getQuotas() {
		return quotas;
	}

	public void setQuotas(ArrayList<Quota> quotas) {
		this.quotas = quotas;
	}

	public void addQuota(Quota q) {
		if (quotas == null)
			quotas = new ArrayList<Quota>();
		quotas.add(q);
	}

	public String getTotalSent() {
		return totalSent;
	}

	public int getTotalIntSent() {
		try {
			return Integer.parseInt(totalSent);
		} catch (Exception ex) {
		}
		return 0;
	}

	public void setTotalSent(String totalSent) {
		this.totalSent = totalSent;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getFt() {
		return ft;
	}

	public void setFt(String ft) {
		this.ft = ft;
	}

	public String getSlt() {
		return slt;
	}

	public void setSlt(String slt) {
		this.slt = slt;
	}

	public String getSlng() {
		return slng;
	}

	public void setSlng(String slng) {
		this.slng = slng;
	}

	public String getElt() {
		return elt;
	}

	public void setElt(String elt) {
		this.elt = elt;
	}

	public String getElng() {
		return elng;
	}

	public void setElng(String elng) {
		this.elng = elng;
	}

	public void setSID(String sid) {
		this.SID = sid;
	}

	public String getSID() {
		return this.SID;
	}

	public void setTries(String string) {
		this.tries = string;

	}

	public String getTries() {
		return this.tries;

	}

	public int getTriesInt() {
		// TODO Auto-generated method stub
		int triess = 0;
		try {
			triess = Integer.parseInt(tries);
		} catch (Exception ex) {

		}
		return triess;
	}

	public String getDB_TABLE_SUBMITSURVEY_service_payment(
			String getsNonRefundableServicePayment) {
		if (DB_TABLE_SUBMITSURVEY_service_payment != null
				&& DB_TABLE_SUBMITSURVEY_service_payment.length() > 0) {
			return DB_TABLE_SUBMITSURVEY_service_payment;
		}
		if (getsNonRefundableServicePayment != null
				&& getsNonRefundableServicePayment.length() > 0) {
			return getsNonRefundableServicePayment;
		}

		return null;
	}

	public String getDB_TABLE_SUBMITSURVEY_transportation_payment(
			String getsNonRefundableServicePayment) {
		if (DB_TABLE_SUBMITSURVEY_service_payment != null
				&& DB_TABLE_SUBMITSURVEY_service_payment.length() > 0) {
			return DB_TABLE_SUBMITSURVEY_service_payment;
		}
		if (getsNonRefundableServicePayment != null
				&& getsNonRefundableServicePayment.length() > 0) {
			return getsNonRefundableServicePayment;
		}

		return null;
	}

	public void setSetVersionID(String setVersionID) {
		this.setVersionID=setVersionID;
	}

	public String getSetVersionID()
	{
		return setVersionID;
	}
}

package com.checker.sa.android.data;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

import com.checker.sa.android.helper.Constants;

public class filePathDataID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9013614111808802088L;
	private String DataID;
	private String filePath;
	private String UPLOAD_FILe_MEDIAFILE;
	private String UPLOAD_FILe_DATAID;
	private String UPLOAD_FILe_ORDERID;
	private String UPLOAD_FILe_PRODUCTID;
	private String UPLOAD_FILe_LOCATIONID;
	private String UPLOAD_FILe_BRANCH_NAME;
	private String UPLOAD_FILe_CLIENT_NAME;
	private String UPLOAD_FILe_DATE;
	private String UPLOAD_FILe_SET_NAME;
	private String UPLOAD_FILe_Sample_size;

	public String getUPLOAD_FILe_PRODUCTID() {
		return UPLOAD_FILe_PRODUCTID;
	}

	public void setUPLOAD_FILe_PRODUCTID(String UPLOAD_FILe_PRODUCTID) {
		this.UPLOAD_FILe_PRODUCTID = UPLOAD_FILe_PRODUCTID;
	}

	public String getUPLOAD_FILe_LOCATIONID() {
		return UPLOAD_FILe_LOCATIONID;
	}

	public void setUPLOAD_FILe_LOCATIONID(String UPLOAD_FILe_LOCATIONID) {
		this.UPLOAD_FILe_LOCATIONID = UPLOAD_FILe_LOCATIONID;
	}

	public String getUPLOAD_FILe_Sample_size() {
		return UPLOAD_FILe_Sample_size;
	}

	public void setUPLOAD_FILe_Sample_size(String uPLOAD_FILe_Sample_size) {
		UPLOAD_FILe_Sample_size = uPLOAD_FILe_Sample_size;
	}

	public void setUPLOAD_FILe_Sample_size(int uPLOAD_FILe_Sample_size) {
		UPLOAD_FILe_Sample_size = uPLOAD_FILe_Sample_size + "";
	}

	private String question_name;

	public String getQuestion_name() {
		return question_name;
	}

	public void setQuestion_name(String question_name) {
		this.question_name = question_name;
	}

	private boolean isLast = false;

	public boolean isRead = false;

	public boolean getIsLast() {
		return isLast;
	}

	public String getDataID() {
		return DataID;
	}

	public void setDataID(String dataID, boolean islast) {
		if (!islast)
			DataID = dataID;
		isLast = islast;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		byte[] daa = filePath.getBytes();
		String o = new String(daa);
		this.filePath = o;
	}

	public String getUPLOAD_FILe_MEDIAFILE() {
		return UPLOAD_FILe_MEDIAFILE;
	}

	public void setUPLOAD_FILe_MEDIAFILE(String uPLOAD_FILe_MEDIAFILE) {
		UPLOAD_FILe_MEDIAFILE = uPLOAD_FILe_MEDIAFILE;
	}

	public String getUPLOAD_FILe_DATAID() {
		return UPLOAD_FILe_DATAID;
	}

	public void setUPLOAD_FILe_DATAID(String uPLOAD_FILe_DATAID) {
		UPLOAD_FILe_DATAID = uPLOAD_FILe_DATAID;
	}

	public String getUPLOAD_FILe_ORDERID() {
		return UPLOAD_FILe_ORDERID;
	}

	public void setUPLOAD_FILe_ORDERID(String uPLOAD_FILe_ORDERID) {
		UPLOAD_FILe_ORDERID = uPLOAD_FILe_ORDERID;
	}

	public String getUPLOAD_FILe_BRANCH_NAME() {
		return UPLOAD_FILe_BRANCH_NAME;
	}

	public void setUPLOAD_FILe_BRANCH_NAME(String uPLOAD_FILe_BRANCH_NAME) {
		UPLOAD_FILe_BRANCH_NAME = uPLOAD_FILe_BRANCH_NAME;
	}

	public String getUPLOAD_FILe_CLIENT_NAME() {
		return UPLOAD_FILe_CLIENT_NAME;
	}

	public void setUPLOAD_FILe_CLIENT_NAME(String uPLOAD_FILe_CLIENT_NAME) {
		UPLOAD_FILe_CLIENT_NAME = uPLOAD_FILe_CLIENT_NAME;
	}

	public String getUPLOAD_FILe_DATE() {
		return UPLOAD_FILe_DATE;
	}

	public void setUPLOAD_FILe_DATE(String uPLOAD_FILe_DATE) {
		UPLOAD_FILe_DATE = uPLOAD_FILe_DATE;
	}

	public String getUPLOAD_FILe_SET_NAME() {
		return UPLOAD_FILe_SET_NAME;
	}

	public void setUPLOAD_FILe_SET_NAME(String uPLOAD_FILe_SET_NAME) {
		UPLOAD_FILe_SET_NAME = uPLOAD_FILe_SET_NAME;
	}

}

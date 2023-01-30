package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class InProgressFileData implements Serializable {
	private String fileName;
	private String fileDataID;
	private String fileOrderID;
	private String size;
	private boolean isOnAppSide;
	private int index;
	static final long serialVersionUID = 12545121L;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public InProgressFileData(String dataid, String temp, String orderid,
			boolean isOnAppSide, String size) {
		this.fileDataID = dataid;
		this.fileName = temp;
		this.fileOrderID = orderid;
		this.isOnAppSide = isOnAppSide;
		this.size = size;
	}

	public InProgressFileData(String dataid, String temp, String orderid,
			boolean isOnAppSide, String size, int i) {
		this.fileDataID = dataid;
		this.fileName = temp;
		this.fileOrderID = orderid;
		this.isOnAppSide = isOnAppSide;
		this.size = size;
		this.index = i;
	}

	public boolean isOnAppSide() {
		return isOnAppSide;
	}

	public void setOnAppSide(boolean isOnAppSide) {
		this.isOnAppSide = isOnAppSide;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDataID() {
		return fileDataID;
	}

	public void setFileDataID(String fileDataID) {
		this.fileDataID = fileDataID;
	}

	public String getFileOrderID() {
		return fileOrderID;
	}

	public void setFileOrderID(String fileOrderID) {
		this.fileOrderID = fileOrderID;
	}

}

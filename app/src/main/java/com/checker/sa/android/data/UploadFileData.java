package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadFileData implements Serializable {
	private ArrayList<filePathDataID> itemPath;
	String millis;
	String setName;

	public ArrayList<filePathDataID> getItemPath() {
		return itemPath;
	}

	public void setItemPath(ArrayList<filePathDataID> itemPath) {
		this.itemPath = itemPath;
	}

	public String getMillis() {
		return millis;
	}

	public void setMillis(String millis) {
		this.millis = millis;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

}

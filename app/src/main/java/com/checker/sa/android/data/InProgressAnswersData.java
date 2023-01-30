package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class InProgressAnswersData implements Serializable {

	private String fileDataID;
	private String fileOrderID;
	private String setName;
	private String clientName;
	private String branch;
	private ArrayList<QuestionnaireData> items;
	static final long serialVersionUID = 12545121L;

	public InProgressAnswersData(String dataid, String orderid, String setName,
			String clientName, String branch) {
		this.fileDataID = dataid;
		this.fileOrderID = orderid;
		this.setName = setName;
		this.clientName = clientName;
		this.branch = branch;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public ArrayList<QuestionnaireData> getItems() {
		return items;
	}

	public void setItems(ArrayList<QuestionnaireData> items) {
		this.items = items;
	}

	public void setItem(QuestionnaireData item) {
		if (this.items == null)
			this.items = new ArrayList<QuestionnaireData>();

		this.items.add(item);
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

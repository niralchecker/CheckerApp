package com.checker.sa.android.data;

import java.io.Serializable;

public class Workers  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String workersID,workerID;
	private String workerName,branchLink;
	
	public String getWorkersID() {
		return workersID;
	}
	public void setWorkersID(String workersID) {
		this.workersID = workersID;
	}
	public String getWorkerID() {
		return workerID;
	}
	public void setWorkerID(String workerID) {
		this.workerID = workerID;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getBranchLink() {
		return branchLink;
	}
	public void setBranchLink(String branchLink) {
		this.branchLink = branchLink;
	}
	
	
}

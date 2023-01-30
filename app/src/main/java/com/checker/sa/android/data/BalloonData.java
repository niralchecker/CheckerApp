package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class BalloonData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 905990168536978677L;

	private int assigned = -1;
	private int scheduled = -1;
	private int in_progress = -1;
	private int completed = -1;
	ArrayList<orderListItem> joblistarray;

	public ArrayList<orderListItem> getJoblistarray() {
		return joblistarray;
	}

	public void setJoblistarray(ArrayList<orderListItem> joblistarray) {
		this.joblistarray = joblistarray;
	}

	public int getAssigned() {
		return assigned;
	}

	public void setAssigned(int assigned) {
		this.assigned = assigned;
	}

	public int getScheduled() {
		return scheduled;
	}

	public void setScheduled(int scheduled) {
		this.scheduled = scheduled;
	}

	public int getIn_progress() {
		return in_progress;
	}

	public void setIn_progress(int in_progress) {
		this.in_progress = in_progress;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

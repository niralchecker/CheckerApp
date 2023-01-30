package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Allocations implements Serializable
{
	private String fsqqaID;
	private String Allocation;
	private String SurveyCount;
	public String getfsqqaID() {
		return fsqqaID;
	}
	public void setfsqqaID(String fsqqaID) {
		this.fsqqaID = fsqqaID;
	}
	public String getAllocation() {
		return Allocation;
	}
	public void setAllocation(String allocation) {
		Allocation = allocation;
	}
	public String getSurveyCount() {
		return SurveyCount;
	}
	public void setSurveyCount(String surveyCount) {
		SurveyCount = surveyCount;
	}
	
}

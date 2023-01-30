package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;


public class Quota  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<SurveyQnA> qnas=new ArrayList<SurveyQnA>();
	public void setqna(SurveyQnA qna) {
		qnas.add(qna);
	}
	
	public ArrayList<SurveyQnA> getqnas() {
		return qnas;
	}
	private String squoID,QuotaName,QuotaMin,DoneCount,ActionToTake;
	public String getActionToTake() {
		return ActionToTake;
	}

	public void setActionToTake(String actionToTake) {
		ActionToTake = actionToTake;
	}
	private String surveyId;

	public String getsquoID() {
		return squoID;
	}

	public void setsquoID(String squoID) {
		this.squoID = squoID;
	}

	public String getQuotaName() {
		return QuotaName;
	}

	public void setQuotaName(String quotaName) {
		QuotaName = quotaName;
	}

	public String getQuotaMin() {
		return QuotaMin;
	}

	public void setQuotaMin(String quotaMin) {
		QuotaMin = quotaMin;
	}

	public String getDoneCount() {
		return DoneCount;
	}

	public void setDoneCount(String doneCount) {
		DoneCount = doneCount;
	}
	
	public void setSurveyLink(String doneCount) {
		surveyId = doneCount;
	}

	public String getSurveyLink() {
		// TODO Auto-generated method stub
		return surveyId;
	}
	
	
}

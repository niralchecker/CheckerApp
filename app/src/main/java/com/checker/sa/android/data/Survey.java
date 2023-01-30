package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.mor.sa.android.activities.R;

import android.content.Context;

public class Survey  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Count = 0;
	private String branchName, branchFullname, branchLink;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchFullName() {
		return branchFullname;
	}

	public void setBranchFullname(String branchFullName) {
		this.branchFullname = branchFullName;
	}

	public String getBranchLink() {
		return branchLink;
	}

	public void setBranchLink(String branchLink) {
		this.branchLink = branchLink;
	}

	public void setCount(int count) {
		Count = count;
	}

	private String surveyID;
	private String surveyName, setLink, QuotaReachedMessage, ThankYouMessage,
			LandingPage, RedirectAfter, TargetQuota, SurveyCount;

	private ArrayList<Quota> listQuotas = new ArrayList<Quota>();
	private ArrayList<Allocations> listAllocations = new ArrayList<Allocations>();

	public String getSurveyID() {
		return surveyID;
	}

	public void setSurveyID(String surveyID) {
		this.surveyID = surveyID;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getSetLink() {
		return setLink;
	}

	public void setSetLink(String setLink) {
		this.setLink = setLink;
	}

	public String getQuotaReachedMessage() {

		return QuotaReachedMessage;
	}

	public String getQuotaReachedMessage(Context c) {
		if (QuotaReachedMessage != null && !QuotaReachedMessage.equals(""))
			return QuotaReachedMessage;
		else
			return c.getResources().getString(
					R.string.survey_quota_alert_message);
	}

	public void setQuotaReachedMessage(String quotaReachedMessage) {
		QuotaReachedMessage = quotaReachedMessage;
	}

	public String getThankYouMessage() {
		return ThankYouMessage;
	}

	public void setThankYouMessage(String thankYouMessage) {
		ThankYouMessage = thankYouMessage;
	}

	public String getLandingPage() {
		return LandingPage;
	}

	public void setLandingPage(String landingPage) {
		LandingPage = landingPage;
	}

	public String getRedirectAfter() {
		return RedirectAfter;
	}

	public void setRedirectAfter(String redirectAfter) {
		RedirectAfter = redirectAfter;
	}

	public String getTargetQuota() {
		return TargetQuota;
	}

	public void setTargetQuota(String targetQuota) {
		TargetQuota = targetQuota;
	}

	public String getSurveyCount() {
		return SurveyCount;
	}

	public void setSurveyCount(String surveyCount) {
		SurveyCount = surveyCount;
	}

	public ArrayList<Quota> getListQuotas() {
		return listQuotas;
	}

	public void setListQuotas(ArrayList<Quota> listQuotas) {
		this.listQuotas = listQuotas;
	}

	public ArrayList<Allocations> getListAllocations() {
		return listAllocations;
	}

	public void setListAllocations(ArrayList<Allocations> listAllocations) {
		this.listAllocations = listAllocations;
	}

	public void setQuotas(Quota quota) {
		this.listQuotas.add(quota);

	}

	public void setAllocations(Allocations allocation) {
		this.listAllocations.add(allocation);

	}

	public int getCount() {
		return Count;
	}

	public void setCount() {
		Count++;// = c;
	}

	public void setJobCount(int c) {
		Count = c;
	}

	public String[] getArrayQuotas() {
		if (this.listQuotas != null) {
			if (this.listQuotas != null
					&& this.listQuotas.size() > 0
					&& (listQuotas.get(0).getDoneCount() == null || listQuotas
							.get(0).getDoneCount().equals("null"))
					&& (listQuotas.get(0).getQuotaMin() == null || listQuotas
							.get(0).getQuotaMin().equals("null"))
					&& (listQuotas.get(0).getDoneCount() == null || listQuotas
							.get(0).getDoneCount().equals("null"))) {
				return new String[0];
			}
			String str[] = new String[this.listQuotas.size()];
			for (int i = 0; i < listQuotas.size(); i++) {
				String q = listQuotas.get(i).getQuotaName();
				String qa = listQuotas.get(i).getDoneCount();
				String qb = listQuotas.get(i).getQuotaMin();
				int n = 0;
				int m = 0;
				try {
					n = Integer.valueOf(listQuotas.get(i).getDoneCount());
					m = Integer.valueOf(listQuotas.get(i).getQuotaMin());
				} catch (Exception ex) {

				}
				str[i] = (m - n) + " (" + listQuotas.get(i).getDoneCount()
						+ "/" + listQuotas.get(i).getQuotaMin() + ") "
						+ listQuotas.get(i).getQuotaName();
			}
			return str;
		}

		return null;
	}

	public String[] getArrayAllocations() {
		if (this.listAllocations != null) {
			String str[] = new String[this.listAllocations.size()];
			for (int i = 0; i < listAllocations.size(); i++) {
				int n = 0;
				int m = 0;
				try {
					n = Integer
							.valueOf(listAllocations.get(i).getSurveyCount());
					m = Integer.valueOf(listAllocations.get(i).getAllocation());
				} catch (Exception ex) {

				}
				str[i] = (m - n) + " ("
						+ listAllocations.get(i).getSurveyCount() + "/"
						+ listAllocations.get(i).getAllocation() + ")";

			}
			return str;
		}

		return null;
	}

	public boolean isAllocationReached() {
		if (this.listAllocations != null) {
			String str[] = new String[this.listAllocations.size()];
			for (int i = 0; i < listAllocations.size(); i++) {
				int n = 0;
				int m = 0;
				try {
					n = Integer
							.valueOf(listAllocations.get(i).getSurveyCount());
					m = Integer.valueOf(listAllocations.get(i).getAllocation());
				} catch (Exception ex) {

				}
				// str[i]=(m-n)+" ("+listAllocations.get(i).getSurveyCount()+"/"+listAllocations.get(i).getAllocation()+")";
				if (n >= m) {
					return true;
				}

			}
			return false;
		} else
			return true;
	}

	public Quota getThisQuota(String getsquoID) {
		for (int i = 0; i < listQuotas.size(); i++) {
			if (listQuotas.get(i).getsquoID().equals(getsquoID)) {
				return listQuotas.get(i);
			}
		}
		return null;
	}
}
/********************* Survey Class Closed *****************************************/

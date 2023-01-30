package com.checker.sa.android.data;

import java.util.ArrayList;

import com.checker.sa.android.db.DBHelper;

public class Surveys {

	private static ArrayList<Survey> sets = new ArrayList<Survey>();

	public static ArrayList<Survey> getSets() {
		if (sets == null)
			sets = new ArrayList<Survey>();
		return sets;
	}

	public static void setSets(Survey set) {
		sets.add(set);
	}

	public static void setSets(ArrayList<Survey> set) {
		sets = set;
	}

	private static ArrayList<Survey> getCurrentSurvey(String setId) {
		ArrayList<Survey> s = getSets();
		ArrayList<Survey> currentSurveys = new ArrayList<Survey>();
		for (int i = 0; i < s.size(); i++) {
			if (s.get(i).getSetLink().equals(setId)) {
				currentSurveys.add(s.get(i));
			}
		}
		return currentSurveys;
	}

	public static Survey getCurrentSurve(String surveyId) {
		if (surveyId == null)
			return null;
		ArrayList<Survey> s = getSets();
		if (s == null)
			return null;
		for (int i = 0; i < s.size(); i++) {
			String id = surveyId;
			if (id.contains("_")) {
				id = id.substring(0, id.indexOf("_"));
			}
			if (s.get(i).getSurveyID().equals(id)) {
				return s.get(i);
			}
		}
		return null;
	}

	public static void increaseAllocation(String surveyID) {
		// TODO Auto-generated method stub

		Survey s = getCurrentSurve(surveyID);
		try {
			int k = Integer.valueOf(s.getSurveyCount());
			s.setSurveyCount(++k + "");
		} catch (Exception ex) {

		}
		if (s.getListAllocations() != null) {

			for (int i = 0; i < s.getListAllocations().size(); i++) {
				try {
					int k = Integer.valueOf(s.getListAllocations().get(i)
							.getSurveyCount());
					s.getListAllocations().get(i).setSurveyCount(++k + "");
				} catch (Exception ex) {

				}

			}
		}
	}

	public static void decreaseAllocation(String surveyID) {
		// TODO Auto-generated method stub

		Survey s = getCurrentSurve(surveyID);
		try {
			int k = Integer.valueOf(s.getSurveyCount());
			s.setSurveyCount(--k + "");
		} catch (Exception ex) {

		}
		if (s.getListAllocations() != null) {

			for (int i = 0; i < s.getListAllocations().size(); i++) {
				try {
					int k = Integer.valueOf(s.getListAllocations().get(i)
							.getAllocation());
					// s.getListAllocations().get(i).setAllocation(--k + "");

					k = Integer.valueOf(s.getListAllocations().get(i)
							.getSurveyCount());
					s.getListAllocations().get(i).setSurveyCount(--k + "");

				} catch (Exception ex) {

				}

			}
		}
	}

	public static void decreaseQuotas(ArrayList<Quota> quotas,
			boolean decreaseAllocation, String id) {
		Survey survey = null;
		try {
			if (quotas != null) {
				for (int i = 0; i < quotas.size(); i++) {

					String surveyID = quotas.get(i).getSurveyLink();
					if (surveyID.contains("-"))
						surveyID = surveyID.replace("-", "");
					if (surveyID.contains("_"))
						surveyID = surveyID.substring(0, surveyID.indexOf("_"));
					survey = getCurrentSurve(surveyID);
					Quota q = survey.getThisQuota(quotas.get(i).getsquoID());
					int k = Integer.valueOf(q.getDoneCount());
					q.setDoneCount(--k + "");
					if (survey != null && decreaseAllocation)
						Surveys.decreaseAllocation(surveyID);
				}
			} else {
				if (decreaseAllocation) {
					String surveyID = id;
					if (surveyID.contains("-"))
						surveyID = surveyID.replace("-", "");
					if (surveyID.contains("_"))
						surveyID = surveyID.substring(0, surveyID.indexOf("_"));
					Surveys.decreaseAllocation(surveyID);
				}
			}
		} catch (Exception ex) {

		}
	}

	public static void increaseQuota(Quota surveyID) {
		try {
			int k = Integer.valueOf(surveyID.getDoneCount());
			surveyID.setDoneCount(++k + "");
		} catch (Exception ex) {

		}
	}
}

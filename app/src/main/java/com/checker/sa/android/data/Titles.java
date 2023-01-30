package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.mor.sa.android.activities.CheckerApp;
import com.mor.sa.android.activities.QuestionnaireActivity;

public class Titles  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qgtID;
	private String TitleText;
	private String TitleCode;
	private String DisplayCondition;
	private String AltTitle;

	public ArrayList<AltLangStrings> altTitles = new ArrayList<AltLangStrings>();

	public void setAltTitle(String altTitle) {
		AltTitle = altTitle;
	}

	public String getQgtID() {
		return qgtID;
	}

	public void setQgtID(String qgtID) {
		this.qgtID = qgtID;
	}

	public String getDisplayCondition() {

		String DisplayCondition = this.DisplayCondition;
		if (DisplayCondition != null) {

			DisplayCondition = CheckerApp
					.changeDisplayCondition(DisplayCondition);

		}
		return DisplayCondition;
	}

	public void setDisplayCondition(String displayCondition) {
		DisplayCondition = displayCondition;
	}

	public String getqgtID() {
		return qgtID;
	}

	public void setqgtID(String qgtID) {
		this.qgtID = qgtID;
	}

	public String getTitleText() {

		return TitleText;
	}

	public void setTitleText(String titleText) {
		TitleText = titleText;
	}

	public String getTitleCode() {
		return TitleCode;
	}

	public void setTitleCode(String titleCode) {
		TitleCode = titleCode;
	}

	public String getAltTitle() {
		if (QuestionnaireActivity.langid != null && altTitles != null
				&& altTitles.size() > 0) {
			for (int i = 0; i < altTitles.size(); i++) {
				if (altTitles.get(i) != null
						&& altTitles.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altTitles.get(i).text != null
						&& altTitles.get(i).text.length() > 0) {
					return altTitles.get(i).text;
				}
			}

		}
		return this.AltTitle;
	}

}

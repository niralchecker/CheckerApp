package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.SharedPreferences;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.CheckerApp;
import com.mor.sa.android.activities.QuestionnaireActivity;

public class Answers implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String answerID;
	public ArrayList<AltLangStrings> altanswers = new ArrayList<AltLangStrings>();

	private String answer, altAnswer, value, iconLink, exclude, answerCode,
			color;
	private String bold, italics, underline, jumpTo;
	private String changeWeightTo, updateSubchapterGradeWith,
			hideAdditionalInfo, additionalInfoMandatory,
			answerDisplayCondition, IconName;

	String DefaultTask, AllowCheckerToChange, AutoCreateAction,
			AutoAssignAction, AutoDueDate, AllowCheckerToChangeType,
			TrainingToTake, TaskTypeLink, Mandatory, Mi;

	private ArrayList<JumpParams> listJumpParams;
	private ArrayList<Task> listTask;
	public int index;
	private String rank;

	private String ClearOtherAnswers;
	private String serverAnswer;
	private String isChecked;

	public String getClearOtherAnswers() {
		return ClearOtherAnswers;
	}

	public void setClearOtherAnswers(String clearOtherAnswers) {
		ClearOtherAnswers = clearOtherAnswers;
	}

	public String getMi() {
		return Mi;
	}

	public void setMi(String mi) {
		Mi = mi;
	}

	public String getIconName() {
		return IconName;
	}

	public void setIconName(String iconName) {
		// [URL]/checker-files/media/[CompanyID]/pictures/[IconName]

		IconName = iconName;
		if (iconName != null) {
			String DownloadUrl = Helper.getSystemURL()
					+ "/checker-files/media/" + Helper.getCompanyLink()
					+ "/pictures/" + iconName;
			Helper helper = new Helper();
			helper.DownloadFromUrl(DownloadUrl, iconName);
			helper.readFile(iconName, true);
		}
	}

	public void sethIconName(String iconName) {
		// [URL]/checker-files/media/[CompanyID]/pictures/[IconName]

		IconName = iconName;
	}

	public String getDefaultTask() {
		return DefaultTask;
	}

	public void setDefaultTask(String defaultTask) {
		DefaultTask = defaultTask;
	}

	public String getAllowCheckerToChange() {
		return AllowCheckerToChange;
	}

	public void setAllowCheckerToChange(String allowCheckerToChange) {
		AllowCheckerToChange = allowCheckerToChange;
	}

	public String getAutoCreateAction() {
		return AutoCreateAction;
	}

	public void setAutoCreateAction(String autoCreateAction) {
		AutoCreateAction = autoCreateAction;
	}

	public String getAutoAssignAction() {
		return AutoAssignAction;
	}

	public void setAutoAssignAction(String autoAssignAction) {
		AutoAssignAction = autoAssignAction;
	}

	public String getAutoDueDate() {
		return AutoDueDate;
	}

	public void setAutoDueDate(String autoDueDate) {
		AutoDueDate = autoDueDate;
	}

	public String getAllowCheckerToChangeType() {
		return AllowCheckerToChangeType;
	}

	public void setAllowCheckerToChangeType(String allowCheckerToChangeType) {
		AllowCheckerToChangeType = allowCheckerToChangeType;
	}

	public String getTrainingToTake() {
		return TrainingToTake;
	}

	public void setTrainingToTake(String trainingToTake) {
		TrainingToTake = trainingToTake;
	}

	public String getTaskTypeLink() {
		return TaskTypeLink;
	}

	public void setTaskTypeLink(String taskTypeLink) {
		TaskTypeLink = taskTypeLink;
	}

	public String getMandatory() {
		return Mandatory;
	}

	public void setMandatory(String mandatory) {
		Mandatory = mandatory;
	}

	public String getChangeWeightTo() {
		return changeWeightTo;
	}

	public void setChangeWeightTo(String changeWeightTo) {
		this.changeWeightTo = changeWeightTo;
	}

	public String getUpdateSubchapterGradeWith() {
		return updateSubchapterGradeWith;
	}

	public void setUpdateSubchapterGradeWith(String updateSubchapterGradeWith) {
		this.updateSubchapterGradeWith = updateSubchapterGradeWith;
	}

	public String getHideAdditionalInfo() {
		return hideAdditionalInfo;
	}

	public void setHideAdditionalInfo(String hideAdditionalInfo) {
		this.hideAdditionalInfo = hideAdditionalInfo;
	}

	public String getAdditionalInfoMandatory() {
		return additionalInfoMandatory;
	}

	public void setAdditionalInfoMandatory(String additionalInfoMandatory) {
		this.additionalInfoMandatory = additionalInfoMandatory;
	}

	public String getAnswerDisplayCondition() {

		String answerDisplayCondition = this.answerDisplayCondition;
		if (answerDisplayCondition != null) {

			answerDisplayCondition = CheckerApp
					.changeDisplayCondition(answerDisplayCondition);
		}
		return answerDisplayCondition;
	}

	public void setAnswerDisplayCondition(String answerDisplayCondition) {
		this.answerDisplayCondition = answerDisplayCondition;
	}

	public String getAnswerID() {
		return answerID;
	}

	public void setAnswerID(String answerID) {
		this.answerID = answerID;
	}

	public String getAnswer() {
		if (QuestionnaireActivity.langid != null
				&& !QuestionnaireActivity.langid.equals("-1")
				&& altanswers != null && altanswers.size() > 0) {
			for (int i = 0; i < altanswers.size(); i++) {
				if (altanswers.get(i) != null
						&& altanswers.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altanswers.get(i).text != null
						&& altanswers.get(i).text.length() > 0) {
					return altanswers.get(i).text;
				}
			}

		}
		return answer;
	}

	public void setAnswer(String answer) {
		if (answer.contains("40 and")) {
			int i = 0;
			i++;
		}
		this.answer = answer;
	}

	public String getAltAnswer() {
		return altAnswer;
	}

	public void setAltAnswer(String altAnswer) {
		this.altAnswer = altAnswer;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getAnswerCode() {
		return answerCode;
	}

	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBold() {
		return bold;
	}

	public void setBold(String bold) {
		this.bold = bold;
	}

	public String getItalics() {
		return italics;
	}

	public void setItalics(String italics) {
		this.italics = italics;
	}

	public String getUnderline() {
		return underline;
	}

	public void setUnderline(String underline) {
		this.underline = underline;
	}

	public String getJumpTo() {
		return jumpTo;
	}

	public void setJumpTo(String jumpTo) {
		this.jumpTo = jumpTo;
	}

	public ArrayList<JumpParams> getListJumpParams() {
		return listJumpParams;
	}

	public void setListJumpParams(ArrayList<JumpParams> listJumpParams) {
		this.listJumpParams = listJumpParams;
	}

	public ArrayList<Task> getListTask() {
		return listTask;
	}

	public void setListTask(ArrayList<Task> listTask) {
		this.listTask = listTask;
	}

	public String getRank() {
		// TODO Auto-generated method stub
		return rank;
	}

	public void setRank(int rank) {
		// TODO Auto-generated method stub
		this.rank = rank + "";
	}

	public void setServerAnswer(String name, String answerId2) {
		this.Mi = name;
		this.answer = name;
		this.answerID = answerId2;
	}

	public void setIsChecked(String aTrue) {
		this.isChecked=aTrue;
	}

	public String getIsChecked() {
		return this.isChecked;
	}
}

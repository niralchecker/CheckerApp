package com.checker.sa.android.data;

import java.io.Serializable;

public class JumpParams  implements Serializable
{
	private String changeWeightTo,updateSubchapterGradeWith,hideAdditionalInfo,
	additionalInfoMandatory,answerDisplayCondition;

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
		return answerDisplayCondition;
	}

	public void setAnswerDisplayCondition(String answerDisplayCondition) {
		this.answerDisplayCondition = answerDisplayCondition;
	}
	
}

package com.checker.sa.android.data;

import java.io.Serializable;

public class ChangeConditions  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String changeConditionsID,chcID;
	private String questCode,changeCondition,ansCode,setLink,changeConditionType;
	
	public String getChangeConditionsID() {
		return changeConditionsID;
	}
	public void setChangeConditionsID(String changeConditionsID) {
		this.changeConditionsID = changeConditionsID;
	}
	public String getChcID() {
		return chcID;
	}
	public void setChcID(String chcID) {
		this.chcID = chcID;
	}
	public String getQuestCode() {
		return questCode;
	}
	public void setQuestCode(String questCode) {
		this.questCode = questCode;
	}
	public String getChangeCondition() {
		return changeCondition;
	}
	public void setChangeCondition(String changeCondition) {
		this.changeCondition = changeCondition;
	}
	public String getAnsCode() {
		return ansCode;
	}
	public void setAnsCode(String ansCode) {
		this.ansCode = ansCode;
	}
	public String getSetLink() {
		return setLink;
	}
	public void setSetLink(String setLink) {
		this.setLink = setLink;
	}
	public String getChangeConditionType() {
		return changeConditionType;
	}
	public void setChangeConditionType(String changeConditionType) {
		this.changeConditionType = changeConditionType;
	}
	
	
}

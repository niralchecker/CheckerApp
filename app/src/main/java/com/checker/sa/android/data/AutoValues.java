package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.helper.Helper;
import com.mor.sa.android.activities.CheckerApp;

public class AutoValues  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String DB_TABLE_AUTOVALUES_avID;
	public String DB_TABLE_AUTOVALUES_SetLink;
	public String DB_TABLE_AUTOVALUES_DataLink;
	public String DB_TABLE_AUTOVALUES_Priority;
	public String DB_TABLE_AUTOVALUES_Condition;
	public String DB_TABLE_AUTOVALUES_Value_AnswerCode;

	public String getAvID() {
		return DB_TABLE_AUTOVALUES_avID;
	}

	public void setavID(String dB_TABLE_AUTOVALUES_avID) {
		DB_TABLE_AUTOVALUES_avID = dB_TABLE_AUTOVALUES_avID;
	}

	public String getSetLink() {
		return DB_TABLE_AUTOVALUES_SetLink;
	}

	public void setSetLink(String dB_TABLE_AUTOVALUES_SetLink) {
		DB_TABLE_AUTOVALUES_SetLink = dB_TABLE_AUTOVALUES_SetLink;
	}

	public String getDataLink() {
		return DB_TABLE_AUTOVALUES_DataLink;
	}

	public void setDataLink(String dB_TABLE_AUTOVALUES_DataLink) {
		DB_TABLE_AUTOVALUES_DataLink = dB_TABLE_AUTOVALUES_DataLink;
	}

	public String getPriority() {
		return DB_TABLE_AUTOVALUES_Priority;
	}

	public void setPriority(String dB_TABLE_AUTOVALUES_Priority) {
		DB_TABLE_AUTOVALUES_Priority = dB_TABLE_AUTOVALUES_Priority;
	}

	public String getCondition() {
		String DB_TABLE_AUTOVALUES_Condition = this.DB_TABLE_AUTOVALUES_Condition;
		if (DB_TABLE_AUTOVALUES_Condition != null) {

			DB_TABLE_AUTOVALUES_Condition = CheckerApp
					.changeDisplayCondition(DB_TABLE_AUTOVALUES_Condition);

		}
		return DB_TABLE_AUTOVALUES_Condition;
	}

	public void setCondition(String dB_TABLE_AUTOVALUES_Condition) {
		DB_TABLE_AUTOVALUES_Condition = dB_TABLE_AUTOVALUES_Condition;
	}

	public String getValue_AnswerCode() {
		return DB_TABLE_AUTOVALUES_Value_AnswerCode;
	}

	public void setValue_AnswerCode(String dB_TABLE_AUTOVALUES_Value_AnswerCode) {
		DB_TABLE_AUTOVALUES_Value_AnswerCode = dB_TABLE_AUTOVALUES_Value_AnswerCode;
	}

}

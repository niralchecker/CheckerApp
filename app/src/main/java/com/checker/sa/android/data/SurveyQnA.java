package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyQnA implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fsqqID;
	private String DataLink;
	private String AnswerLink;
	public Quota parentQuota;
//	<QuestionsAnswers type="array">
//    <QuestionsAnswers ID="14" type="array">
//      <fsqqID>14</fsqqID>
//      <DataLink>131543</DataLink>
//      <AnswerLink>9159</AnswerLink>
//    </QuestionsAnswers>
//  </QuestionsAnswers>
//	
	public String getfsqqID() {
		return fsqqID;
	}
	public void setfsqqID(String fsqqID) {
		this.fsqqID = fsqqID;
	}
	public String getDataLink() {
		return DataLink;
	}
	public void setDataLink(String dataLink) {
		DataLink = dataLink;
	}
	public String getAnswerLink() {
		return AnswerLink;
	}
	public void setAnswerLink(String answerLink) {
		AnswerLink = answerLink;
	}
	
}

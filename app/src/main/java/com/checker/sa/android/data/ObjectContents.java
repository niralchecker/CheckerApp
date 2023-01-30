package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ObjectContents  implements Serializable
{
	
	private String picID,pictureFilename,originalFilename,align, miDescription,attachmentTypeLink,
	 textID,text,textType,font,color,size,bold,italics,underline,question,questionDescription,altQuestion,
	 questionTypeLink,customScaleLink,scaleName,
		nonSepQuestionOriginalChapterLink;
	private String isSeperateQuestion,excludeFromGrade,doNotDisplayInReport,showIrrelevant,showCritical,mandatory,
	displayType,displayOrientation,answerOrdering,miType,miMandatory,miDefaultDate,attachment;
	private String miFreeTextMinlength,miFreeTextMaxlength,miFreeTextRows,miFreeTextCols,miNumberMin,
	miNumberMax, questionID,maxAnswersForMultiple, AffectedQuestions, SourceSectionLink;

	public String getAffectedQuestions() {
		return AffectedQuestions;
	}

	public void setAffectedQuestions(String affectedQuestions) {
		AffectedQuestions = affectedQuestions;
	}

	public String getSourceSectionLink() {
		return SourceSectionLink;
	}

	public void setSourceSectionLink(String sourceSectionLink) {
		SourceSectionLink = sourceSectionLink;
	}

	private ArrayList<Answers> listAnswers;
	private ArrayList<AutoValues> listAutoValues;
	private ArrayList<SubchapterLinks> listSubchapterLinks;
	public String getPicID() {
		return picID;
	}
	public void setPicID(String picID) {
		this.picID = picID;
	}
	public String getPictureFilename() {
		return pictureFilename;
	}
	public void setPictureFilename(String pictureFilename) {
		this.pictureFilename = pictureFilename;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getMiDescription() {
		return miDescription;
	}
	public void setMiDescription(String miDescription) {
		this.miDescription = miDescription;
	}
	public String getAttachmentTypeLink() {
		return attachmentTypeLink;
	}
	public void setAttachmentTypeLink(String attachmentTypeLink) {
		this.attachmentTypeLink = attachmentTypeLink;
	}
	public String getTextID() {
		return textID;
	}
	public void setTextID(String textID) {
		this.textID = textID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTextType() {
		return textType;
	}
	public void setTextType(String textType) {
		this.textType = textType;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
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
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public String getAltQuestion() {
		return altQuestion;
	}
	public void setAltQuestion(String altQuestion) {
		this.altQuestion = altQuestion;
	}
	public String getQuestionTypeLink() {
		return questionTypeLink;
	}
	public void setQuestionTypeLink(String questionTypeLink) {
		this.questionTypeLink = questionTypeLink;
	}
	public String getCustomScaleLink() {
		return customScaleLink;
	}
	public void setCustomScaleLink(String customScaleLink) {
		this.customScaleLink = customScaleLink;
	}
	public String getScaleName() {
		return scaleName;
	}
	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}
	public String getNonSepQuestionOriginalChapterLink() {
		return nonSepQuestionOriginalChapterLink;
	}
	public void setNonSepQuestionOriginalChapterLink(
			String nonSepQuestionOriginalChapterLink) {
		this.nonSepQuestionOriginalChapterLink = nonSepQuestionOriginalChapterLink;
	}
	public String getIsSeperateQuestion() {
		return isSeperateQuestion;
	}
	public void setIsSeperateQuestion(String isSeperateQuestion) {
		this.isSeperateQuestion = isSeperateQuestion;
	}
	public String getExcludeFromGrade() {
		return excludeFromGrade;
	}
	public void setExcludeFromGrade(String excludeFromGrade) {
		this.excludeFromGrade = excludeFromGrade;
	}
	public String getDoNotDisplayInReport() {
		return doNotDisplayInReport;
	}
	public void setDoNotDisplayInReport(String doNotDisplayInReport) {
		this.doNotDisplayInReport = doNotDisplayInReport;
	}
	public String getShowIrrelevant() {
		return showIrrelevant;
	}
	public void setShowIrrelevant(String showIrrelevant) {
		this.showIrrelevant = showIrrelevant;
	}
	public String getShowCritical() {
		return showCritical;
	}
	public void setShowCritical(String showCritical) {
		this.showCritical = showCritical;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getDisplayOrientation() {
		return displayOrientation;
	}
	public void setDisplayOrientation(String displayOrientation) {
		this.displayOrientation = displayOrientation;
	}
	public String getAnswerOrdering() {
		return answerOrdering;
	}
	public void setAnswerOrdering(String answerOrdering) {
		this.answerOrdering = answerOrdering;
	}
	public String getMiType() {
		return miType;
	}
	public void setMiType(String miType) {
		this.miType = miType;
	}
	public String getMiMandatory() {
		return miMandatory;
	}
	public void setMiMandatory(String miMandatory) {
		this.miMandatory = miMandatory;
	}
	public String getMiDefaultDate() {
		return miDefaultDate;
	}
	public void setMiDefaultDate(String miDefaultDate) {
		this.miDefaultDate = miDefaultDate;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getMiFreeTextMinlength() {
		return miFreeTextMinlength;
	}
	public void setMiFreeTextMinlength(String miFreeTextMinlength) {
		this.miFreeTextMinlength = miFreeTextMinlength;
	}
	public String getMiFreeTextMaxlength() {
		return miFreeTextMaxlength;
	}
	public void setMiFreeTextMaxlength(String miFreeTextMaxlength) {
		this.miFreeTextMaxlength = miFreeTextMaxlength;
	}
	public String getMiFreeTextRows() {
		return miFreeTextRows;
	}
	public void setMiFreeTextRows(String miFreeTextRows) {
		this.miFreeTextRows = miFreeTextRows;
	}
	public String getMiFreeTextCols() {
		return miFreeTextCols;
	}
	public void setMiFreeTextCols(String miFreeTextCols) {
		this.miFreeTextCols = miFreeTextCols;
	}
	public String getMiNumberMin() {
		return miNumberMin;
	}
	public void setMiNumberMin(String miNumberMin) {
		this.miNumberMin = miNumberMin;
	}
	public String getMiNumberMax() {
		return miNumberMax;
	}
	public void setMiNumberMax(String miNumberMax) {
		this.miNumberMax = miNumberMax;
	}
	public String getQuestionID() {
		return questionID;
	}
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}
	public String getMaxAnswersForMultiple() {
		return maxAnswersForMultiple;
	}
	public void setMaxAnswersForMultiple(String maxAnswersForMultiple) {
		this.maxAnswersForMultiple = maxAnswersForMultiple;
	}
	public ArrayList<AutoValues> getListAutoValues() {
		return listAutoValues;
	}
	public void setListAutoValues(ArrayList<AutoValues> listAutoValues) {
		this.listAutoValues = listAutoValues;
	}
	public ArrayList<Answers> getListAnswers() {
		return listAnswers;
	}
	public void setListAnswers(ArrayList<Answers> listAnswers) {
		this.listAnswers = listAnswers;
	}
	public ArrayList<SubchapterLinks> getListSubchapterLinks() {
		return listSubchapterLinks;
	}
	public void setListSubchapterLinks(
			ArrayList<SubchapterLinks> listSubchapterLinks) {
		this.listSubchapterLinks = listSubchapterLinks;
	}
	
	

	/*********************Answers Class Closed*****************************************/

}

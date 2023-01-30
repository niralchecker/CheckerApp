package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.content.SharedPreferences;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.helper.UIHelper;
import com.mor.sa.android.activities.CheckerApp;
import com.mor.sa.android.activities.QuestionnaireActivity;
import com.mor.sa.android.data.CurrentLoopData;

public class Objects implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Objects() {
		currentIndex = -1;
	}

	private String backPointer = "";

	public String getBackPointer() {
		return backPointer;
	}

	public void setBackPointer(String backPointer) {
		this.backPointer = backPointer;

	}

	public ArrayList<AltLangStrings> altGroupNames = new ArrayList<AltLangStrings>();
	public ArrayList<AltLangStrings> altQuestionTexts = new ArrayList<AltLangStrings>();
	public ArrayList<AltLangStrings> altQuestionDescription = new ArrayList<AltLangStrings>();
	public ArrayList<AltLangStrings> altTexts = new ArrayList<AltLangStrings>();
	public ArrayList<AltLangStrings> altmiDescription = new ArrayList<AltLangStrings>();

	private String objectID, DataID, ObjectOrder, ObjectDisplayCondition,
			ObjectCode, ObjectType, ObjectLink, PropertyLinkToInventory,
			BranchInputCaption, WorkerInputCaption, BranchInputMandatory,
			WorkerInputMandatory;

	public String backJumpDataID = "";
	public int pageCount = -1;
	public int nextQIndex = -1;
	public int viewOnPage = -1;
	int answerIndexCount = 0;
	private String UseInTOC;
	private String index;
	boolean mi;
	private ArrayList<ObjectContents> objectContents;

	private ArrayList<String> QuestionGroups = null;
	private ArrayList<String> QuestionLinks = null;

	private ArrayList<AutoValues> listAutoValues = null;
	private ArrayList<Titles> QuestionTitles = null;

	public ArrayList<AutoValues> getListAutoValues() {
		return listAutoValues;
	}

	public void setListAutoValues(ArrayList<AutoValues> listAutoValues) {
		this.listAutoValues = listAutoValues;
	}

	public void setListAutoValues(AutoValues answer) {
		if (listAutoValues == null)
			listAutoValues = new ArrayList<AutoValues>();
		listAutoValues.add(answer);
	}

	private static ArrayList<Titles> randomizeTitles(
			ArrayList<Titles> listTitles) {
		ArrayList<Titles> randomizeList = new ArrayList<Titles>();
		if (listTitles == null)
			return randomizeList;
		Calendar calendar = Calendar.getInstance();
		System.out.println("Seconds in current minute = "
				+ calendar.get(Calendar.SECOND));
		if (Helper.seed < 0) {
			Helper.seed = calendar.get(Calendar.SECOND);
		}
		Random randomGenerator = new Random(Helper.seed);
		int size = listTitles.size();
		while (randomizeList.size() != size) {
			int n = randomGenerator.nextInt(listTitles.size());
			randomizeList.add(listTitles.get(n));
			listTitles.remove(listTitles.get(n));
		}

		return randomizeList;
	}

	public ArrayList<Titles> getQuestionRandomTitles() {
		if (getRandomTitlesOrder() != null
				&& getRandomTitlesOrder().equals("1")) {
			ArrayList<Titles> randomizeList = new ArrayList<Titles>();
			for (int i = 0; i < QuestionTitles.size(); i++) {
				randomizeList.add(QuestionTitles.get(i));
			}
			return randomizeTitles(randomizeList);
		}
		return QuestionTitles;
	}

	public ArrayList<Titles> getQuestionTitles() {

		return QuestionTitles;
	}

	public void setQuestionTitles(ArrayList<Titles> questionTitles) {
		QuestionTitles = questionTitles;
	}

	public void setQuestionGroups(ArrayList<String> questionGroups) {
		QuestionGroups = questionGroups;
	}

	public void setQuestionLinks(ArrayList<String> questionLinks) {
		QuestionLinks = questionLinks;
	}

	public void addQuestionGroups(String datad) {
		if (QuestionGroups == null) {
			QuestionGroups = new ArrayList<String>();

		}
		QuestionGroups.add(datad);
	}

	public ArrayList<String> getQuestionGroups() {
		return QuestionGroups;
	}

	public void addQuestionLinks(String datad) {
		if (QuestionLinks == null) {
			QuestionLinks = new ArrayList<String>();

		}
		QuestionLinks.add(datad);
	}

	public ArrayList<String> getQuestionLinks() {
		return QuestionLinks;
	}

	private String ftiID, FinishTimeInputCaption, FinishTimeInputMandatory;

	public String getFtiID() {
		return ftiID;
	}

	public void setftiID(String ftiID) {
		this.ftiID = ftiID;
	}

	public String getFinishTimeInputCaption() {
		return FinishTimeInputCaption;
	}

	public void setFinishTimeInputCaption(String finishTimeInputCaption) {
		question = finishTimeInputCaption;
		FinishTimeInputCaption = finishTimeInputCaption;
	}

	public String getFinishTimeInputMandatory() {
		return FinishTimeInputMandatory;
	}

	public void setFinishTimeInputMandatory(String finishTimeInputMandatory) {
		miMandatory = finishTimeInputMandatory;
		FinishTimeInputMandatory = finishTimeInputMandatory;
	}

	private String picID, pictureFilename, originalFilename, align,
			miDescription, attachmentTypeLink, textID, text, textType, font,
			color, size, bold, italics, underline, question,
			questionDescription, altQuestion, altDescription, altText,
			altGroupName, questionTypeLink, customScaleLink, scaleName,
			UrlContent, UrlID, DestinationObject, DestinationDescription,
			nonSepQuestionOriginalChapterLink;

	public String getAltDescription() {
		return altDescription;
	}

	public void setAltDescription(String altDescription) {
		this.altDescription = altDescription;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getAltGroupName() {
		return altGroupName;
	}

	public void setAltGroupName(String altText) {
		this.altGroupName = altText;
	}

	int Index;
	byte[] picturedata;

	private String isSeperateQuestion, excludeFromGrade, doNotDisplayInReport,
			showIrrelevant, showCritical, mandatory, displayType,
			displayOrientation, answerOrdering, miType, miMandatory,
			miDefaultDate, attachment;
	private String miGroupMandatory = null;

	public String getMiGroupMandatory() {
		return miGroupMandatory;
	}

	public void setMiGroupMandatory(String miGroupMandatory) {
		this.miGroupMandatory = miGroupMandatory;
	}

	private String miFreeTextMinlength, miFreeTextMaxlength, miFreeTextRows,
			miFreeTextCols, miNumberMin, miNumberMax, questionID,
			maxAnswersForMultiple;

	private String groupName, questionOrientation, randomQuestionrder,
			randomTitlesOrder, PipingSourceDataLink,
			DynamicTitlesDefaultAmount;

	public String getGroupName() {
		if (QuestionnaireActivity.langid != null
				&& !QuestionnaireActivity.langid.equals("-1")
				&& altGroupNames != null && altGroupNames.size() > 0) {
			for (int i = 0; i < altGroupNames.size(); i++) {
				if (altGroupNames.get(i) != null
						&& altGroupNames.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altGroupNames.get(i).text != null
						&& altGroupNames.get(i).text.length() > 0) {
					return altGroupNames.get(i).text;
				}
			}

		}
		return groupName;
	}

	public String getRandomQuestionOrder() {
		return randomQuestionrder;
	}

	public void setRandomQuestionsOrder(String randomQuestionrder) {
		this.randomQuestionrder = randomQuestionrder;
	}

	public void setGroupName(String groupName) {
		if (groupName != null) {
			int i = 0;
			i++;
		}
		this.groupName = groupName;
	}

	public String getQuestionOrientation() {
		return questionOrientation;
	}

	public void setQuestionsOrientation(String questionOrientation) {
		this.questionOrientation = questionOrientation;
	}

	public String getRandomTitlesOrder() {
		return randomTitlesOrder;
	}

	public void setRandomTitlesOrder(String randomTitlesOrder) {
		this.randomTitlesOrder = randomTitlesOrder;
	}

	public String getPipingSourceDataLink() {
		return PipingSourceDataLink;
	}

	public void setPipingSourceDataLink(String pipingSourceDataLink) {
		PipingSourceDataLink = pipingSourceDataLink;
	}

	public String getDynamicTitlesDefaultAmount() {
		return DynamicTitlesDefaultAmount;
	}

	public void setDynamicTitlesDefaultAmount(String dynamicTitlesDefaultAmount) {
		DynamicTitlesDefaultAmount = dynamicTitlesDefaultAmount;
	}

	public String getBranchInputMandatory() {
		return BranchInputMandatory;
	}

	public void setBranchInputMandatory(String branchInputCaption) {
		BranchInputMandatory = branchInputCaption;
	}

	public String getWorkerInputMandatory() {
		return WorkerInputMandatory;
	}

	public void setWorkerInputMandatory(String workerInputCaption) {
		WorkerInputMandatory = workerInputCaption;
	}

	public String getBranchInputCaption() {
		return BranchInputCaption;
	}

	public void setBranchInputCaption(String branchInputCaption) {
		BranchInputCaption = branchInputCaption;
	}

	public String getWorkerInputCaption() {
		return WorkerInputCaption;
	}

	public void setWorkerInputCaption(String workerInputCaption) {
		WorkerInputCaption = workerInputCaption;
	}

	public boolean isMi() {
		return mi;
	}

	public void setMi(boolean mi) {
		this.mi = mi;
	}

	public String getUrlContent() {
		return UrlContent;
	}

	public void setUrlContent(String urlContent) {
		UrlContent = urlContent;
	}

	public String getUrlID() {
		return UrlID;
	}

	public void setUrlID(String urlID) {
		UrlID = urlID;
	}

	public String getDestinationObject() {
		return DestinationObject;
	}

	public void setDestinationObject(String destinationObject) {
		DestinationObject = destinationObject;
	}

	public String getDestinationDescription() {
		return DestinationDescription;
	}

	public void setDestinationDescription(String destinationDescription) {
		DestinationDescription = destinationDescription;
	}

	public byte[] getPicturedata() {
		return picturedata;
	}

	public void setPicturedata(byte[] picturedata) {
		this.picturedata = picturedata;
	}

	// <AnswersSource>looptest</AnswersSource>
	// <AnswersCondition>1=1</AnswersCondition>
	// <AnswersFormat>$[Brand]$</AnswersFormat>
	private String AnswersSource = null;
	private String AnswersCondition = null;
	private String AnswersFormat = null;

	public String LoopName = "";
	public String LoopSource = "";
	public String RandomizeLoop = "";
	public String LoopCondition = "";
	public String LoopFormat = "";

	public String getRandomizeLoop() {
		return RandomizeLoop;
	}

	public void setRandomizeLoop(String randomizeLoop) {
		RandomizeLoop = randomizeLoop;
	}

	public String getLoopName() {
		return LoopName;
	}

	public void setLoopName(String loopName) {
		LoopName = loopName;
	}

	public String getLoopSource() {
		return LoopSource;
	}

	public void setLoopSource(String loopSource) {
		LoopSource = loopSource;
	}

	public String getLoopCondition() {
		return LoopCondition;
	}

	public void setLoopCondition(String loopCondition) {
		LoopCondition = loopCondition;
	}

	public String getLoopFormat() {
		return LoopFormat;
	}

	public void setLoopFormat(String loopFormat) {
		LoopFormat = loopFormat;
	}

	public String getAnswersSource() {
		return AnswersSource;
	}

	public void setAnswersSource(String answersSource) {
		if (answersSource != null) {
			int i = 0;
			i++;
		}
		AnswersSource = answersSource;
	}

	public String getAnswersCondition() {
		return AnswersCondition;
	}

	public void setAnswersCondition(String answersCondition) {
		AnswersCondition = answersCondition;
	}

	public String getAnswersFormat() {
		return AnswersFormat;
	}

	public void setAnswersFormat(String answersFormat) {
		AnswersFormat = answersFormat;
	}

	private ArrayList<Answers> listAnswers = new ArrayList<Answers>();
	private ArrayList<SubchapterLinks> listSubchapterLinks;

	public int currentIndex;

	public boolean ispagebreakokay;

	private CurrentLoopData loopObject;

	public CurrentLoopData getLoopObject() {
		return loopObject;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public boolean getMiText() {
		return mi;
	}

	public void setMiText(boolean Mi) {
		mi = Mi;
	}

	public int getObjectIndex() {
		return Index;
	}

	public void setIndex(int index) {
		Index = index;
	}

	public String getDataID() {
		return DataID;
	}

	public void setDataID(String dataID) {
		DataID = dataID;
	}

	public String getObjectOrder() {
		return ObjectOrder;
	}

	public void setObjectOrder(String objectOrder) {
		ObjectOrder = objectOrder;
	}

	public String getObjectDisplayCondition() {
		String ObjectDisplayCondition = this.ObjectDisplayCondition;
		if (ObjectDisplayCondition != null) {
			ObjectDisplayCondition = CheckerApp
					.changeDisplayCondition(ObjectDisplayCondition);

		}
		return ObjectDisplayCondition;
	}

	public void setObjectDisplayCondition(String objectDisplayCondition) {
		ObjectDisplayCondition = objectDisplayCondition;
	}

	public String getObjectCode() {
		return ObjectCode;
	}

	public void setObjectCode(String objectCode) {
		ObjectCode = objectCode;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	public String getObjectLink() {
		return ObjectLink;
	}

	public void setObjectLink(String objectLink) {
		ObjectLink = objectLink;
	}

	public String getPropertyLinkToInventory() {
		return PropertyLinkToInventory;
	}

	public void setPropertyLinkToInventory(String propertyLinkToInventory) {
		PropertyLinkToInventory = propertyLinkToInventory;
	}

	public String getUseInTOC() {
		return UseInTOC;
	}

	public void setUseInTOC(String useInTOC) {
		UseInTOC = useInTOC;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public ArrayList<ObjectContents> getObjectContents() {
		return objectContents;
	}

	public void setObjectContents(ArrayList<ObjectContents> objectContents) {
		this.objectContents = objectContents;
	}

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

	public byte[] getPictureData() {
		return picturedata;
	}

	public void setPictureData(byte[] pictureFilename) {
		this.picturedata = pictureFilename;
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
		if (QuestionnaireActivity.langid != null
				&& !QuestionnaireActivity.langid.equals("-1")
				&& altTexts != null && altTexts.size() > 0) {
			for (int i = 0; i < altTexts.size(); i++) {
				if (altTexts.get(i) != null
						&& altTexts.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altTexts.get(i).text != null
						&& altTexts.get(i).text.length() > 0) {
					return altTexts.get(i).text;
				}
			}

		}
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

		if (QuestionnaireActivity.langid != null
				&& !QuestionnaireActivity.langid.equals("-1")
				&& altQuestionTexts != null && altQuestionTexts.size() > 0) {
			for (int i = 0; i < altQuestionTexts.size(); i++) {
				if (altQuestionTexts.get(i) != null
						&& altQuestionTexts.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altQuestionTexts.get(i).text != null
						&& altQuestionTexts.get(i).text.length() > 0) {
					return altQuestionTexts.get(i).text;
				}
			}

		}
		return question;
	}

	public void setQuestionName(String question) {

		this.question = question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionDescription() {
		if (QuestionnaireActivity.langid != null
				&& !QuestionnaireActivity.langid.equals("-1")
				&& altQuestionDescription != null
				&& altQuestionDescription.size() > 0) {
			for (int i = 0; i < altQuestionDescription.size(); i++) {
				if (altQuestionDescription.get(i) != null
						&& altQuestionDescription.get(i).AltLngID
								.equals(QuestionnaireActivity.langid)
						&& altQuestionDescription.get(i).text != null
						&& altQuestionDescription.get(i).text.length() > 0) {
					return altQuestionDescription.get(i).text;
				}
			}

		}
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
		//if (questionTypeLink!=null)
		return questionTypeLink;
		//else return "";
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
		//showCritical="1";
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
		//if (miType==null) miType="";
		return miType;
	}

	public void setMiType(String miType) {
		this.miType = miType;
	}

	public String getMiMandatory() {
		if (miGroupMandatory != null && miGroupMandatory.equals("1"))
			return miGroupMandatory;

		if (miGroupMandatory != null && miGroupMandatory.equals("0"))
			return miGroupMandatory;

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
		long minLength = convertToInt(miFreeTextMinlength);
		long maxLength = convertToInt(miFreeTextMaxlength);

		if (maxLength <= 0) {
			return Integer.MAX_VALUE + "";
		}
		return miFreeTextMaxlength;
	}

	private long convertToInt(String miFreeTextMaxlength2) {
		long i = -1;
		try {
			i = Long.valueOf(miFreeTextMaxlength2);
		} catch (Exception ex) {

		}
		return i;
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
		long minLength = convertToInt(miNumberMin);
		long maxLength = convertToInt(miNumberMax);

		if (minLength > 0 && maxLength <= 0) {
			return Integer.MAX_VALUE + "";
		}
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

	public void setAnswers(Answers answer) {
		answer.index = answerIndexCount;
		listAnswers.add(answer);
		answerIndexCount++;
	}

	public void addTitles(Titles titles) {

		if (QuestionTitles == null) {
			QuestionTitles = new ArrayList<Titles>();

		}
		QuestionTitles.add(titles);
	}

	public ArrayList<Answers> getListAnswers(Set set, String dataid2) {
		if (dataid2.contains("$")) {
			dataid2 = QuestionnaireActivity.cleanDataIdfromDollarSign(dataid2);

		}

		if (AnswersSource != null && AnswersFormat != null) {
			if (listAnswers != null
					&& listAnswers.size() > 0
					&& listAnswers.get(0).getAnswer() != null
					&& listAnswers.get(0).getAnswer().toLowerCase()
							.equals("not filled")) {
				if (dataid2.contains("^")) {
					ArrayList<LoopsEntry> listdata = set.loopData;
					String[] columns = dataid2.split("\\^");

					for (int i = 0; i < columns.length; i++) {
						if (listdata.size() == set.loopData.size()
								&& columns[i].contains("=")) {
							String localFormat = AnswersFormat;

							String[] nameValue = columns[i].split("=");

							if (nameValue.length > 1) {
								listdata = set
										.getLoopAgainstListNamenColumnamenparentname(
												AnswersSource, localFormat
														.replace("$[", "")
														.replace("]$", ""),
												nameValue[0], nameValue[1],
												listdata, true);

							}
						} else {
							if (columns.length > 1) {
								String[] nameValue = columns[i].split("=");
								if (nameValue.length > 1) {
									ArrayList<LoopsEntry> listdataa = new ArrayList<LoopsEntry>();
									for (int j = 0; j < listdata.size(); j++) {
										LoopsEntry thisItem = Set
												.getLoopAgainstListNamenColumDatanRowNumber(
														set.loopData,
														AnswersSource,
														nameValue[1], listdata
																.get(j)
																.getRowNumber());
										if (thisItem != null)
											listdataa.add(thisItem);
									}
									listdata = listdataa;
								}
							}

						}

					}

					ArrayList<LoopsEntry> listdataa = new ArrayList<LoopsEntry>();
					for (int j = 0; j < listdata.size(); j++) {
						LoopsEntry thisItem = Set
								.getLoopAgainstListNamenColumNamenRowNumber(
										set.loopData, AnswersSource,
										AnswersFormat.replace("$[", "")
												.replace("]$", ""), listdata
												.get(j).getRowNumber());
						boolean isAdded = Set.isAlreadyAdded(listdataa,
								thisItem);
						if (thisItem != null && !isAdded)
							listdataa.add(thisItem);
					}
					listdata = listdataa;

					return UIHelper.convertLoopToAnswers(listdata,
							listAnswers.get(0));

				} else
					return UIHelper.convertLoopToAnswers(set
							.getLoopAgainstListNamenColumname(
									AnswersSource,
									AnswersFormat.replace("$[", "").replace(
											"]$", ""), set.loopData, false,
									null), listAnswers.get(0));
			} else {
				if (dataid2.contains("^")) {
					ArrayList<LoopsEntry> listdata = set.loopData;
					String[] columns = dataid2.split("\\^");
					for (int i = 0; i < columns.length; i++) {
						if (listdata.size() == set.loopData.size()
								&& columns[i].contains("=")) {
							String localFormat = AnswersFormat;

							String[] nameValue = columns[i].split("=");

							if (nameValue.length > 1) {
								listdata = set
										.getLoopAgainstListNamenColumnamenparentname(
												AnswersSource, localFormat
														.replace("$[", "")
														.replace("]$", ""),
												nameValue[0], nameValue[1],
												listdata, true);

							}
						} else {
							if (columns.length > 1) {
								String[] nameValue = columns[i].split("=");
								if (nameValue.length > 1) {
									ArrayList<LoopsEntry> listdataa = new ArrayList<LoopsEntry>();
									for (int j = 0; j < listdata.size(); j++) {
										LoopsEntry thisItem = Set
												.getLoopAgainstListNamenColumDatanRowNumber(
														set.loopData,
														AnswersSource,
														nameValue[1], listdata
																.get(j)
																.getRowNumber());
										if (thisItem != null)
											listdataa.add(thisItem);
									}
									listdata = listdataa;
								}
							}

						}

					}

					ArrayList<LoopsEntry> listdataa = new ArrayList<LoopsEntry>();
					for (int j = 0; j < listdata.size(); j++) {
						LoopsEntry thisItem = Set
								.getLoopAgainstListNamenColumNamenRowNumber(
										set.loopData, AnswersSource,
										AnswersFormat.replace("$[", "")
												.replace("]$", ""), listdata
												.get(j).getRowNumber());
						boolean isAdded = Set.isAlreadyAdded(listdataa,
								thisItem);
						if (thisItem != null && !isAdded)
							listdataa.add(thisItem);
					}
					listdata = listdataa;

					return UIHelper.convertLoopToAnswers(listdata, null);

				}

				else
					return UIHelper.convertLoopToAnswers(set
							.getLoopAgainstListNamenColumname(
									AnswersSource,
									AnswersFormat.replace("$[", "").replace(
											"]$", ""), set.loopData, false,
									null), null);
			}
		} else if (listAnswers != null
				&& listAnswers.size() > 0
				&& listAnswers.get(0).getAnswer() != null
				&& (!listAnswers.get(0).getAnswer().toLowerCase()
						.equals("not filled") || (AnswersSource == null && AnswersFormat == null)))
			return listAnswers;

		return new ArrayList<Answers>();
	}

	public void setLoopObject(CurrentLoopData currentLoopDataObj) {
		this.loopObject = currentLoopDataObj;
	}

	String loopInfo;

	private String loopListName;
	private int loopRowNumber;

	public void setLoopInfo(String loopInfo) {
		this.loopInfo = loopInfo;
	}

	public String getLoopInfo() {
		return loopInfo;
	}

	public void setLoopRow(int rowNumber) {

		this.loopRowNumber = rowNumber;
	}

	public int getLoopRow() {
		return loopRowNumber;
	}

	public void setLoopList(String listName) {
		loopListName = listName;
	}

	public String getLoopList() {
		return loopListName;
	}

}

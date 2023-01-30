package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

public class QuestionnaireData implements Serializable {

	String GroupId = null, CritFreeText, answerID, answerText, mi,
			questionText, QuestionTypeLink, FillingEndLocation, DataID,
			ansText, OrderID, ObjectCode, AnswerCode, BranchID, WorkerID,
			ObjectType, brachtext, workertext, freetext, finishtime;

	public String getGroupId() {
		return GroupId;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

	public String getFreetext() {
		return freetext;
	}

	public void setFreetext(String freetext) {
		this.freetext = freetext;
	}

	public String getFinishtime() {
		return finishtime;
	}

	public void setFinishTimeValue(String finishtime) {
		this.finishtime = finishtime;
	}
	
	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}

	public String getBrachtext() {
		return brachtext;
	}

	public void setBrachtext(String brachtext) {
		this.brachtext = brachtext;
	}

	public String getWorkertext() {
		return workertext;
	}

	public void setWorkertext(String workertext) {
		this.workertext = workertext;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	public String getBranchID() {
		return BranchID;
	}

	public void setBranchID(String branchID) {
		BranchID = branchID;
	}

	public String getWorkerID() {
		return WorkerID;
	}

	public void setWorkerID(String workerID) {
		WorkerID = workerID;
	}

	public String getObjectCode() {
		return ObjectCode;
	}

	public void setObjectCode(String objectCode) {
		ObjectCode = objectCode;
	}

	public String getAnswerCode() {
		return AnswerCode;
	}

	public void setAnswerCode(String answerCode) {
		AnswerCode = answerCode;
	}

	public int getPoistion() {
		return poistion;
	}

	public void setPoistion(int poistion) {
		this.poistion = poistion;
	}

	public ArrayList<Answers> getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(ArrayList<Answers> listAnswer) {
		this.listAnswer = listAnswer;
	}

	int poistion;

	public String getAnsText() {
		return ansText;
	}

	public void setAnsText(String ansText) {
		this.ansText = ansText;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		this.OrderID = orderID;
	}

	private String setName;
	private String clientName;
	private String branch;

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	ArrayList<Answers> listAnswer = new ArrayList<Answers>();
	private String loopinfo;
	private String mitype;
	private String SetID;
	private ArrayList<InProgressFileData> attachedFiles;
	private String startTime;

	public String getLoopinfo() {
		return loopinfo;
	}

	public void setLoopinfo(String loopinfo) {
		this.loopinfo = loopinfo;
	}

	public void initListAnswer() {
		listAnswer = new ArrayList<Answers>();
	}

	public int getPosition() {
		return poistion;
	}

	public void setPosition(int pos) {
		poistion = pos;
	}

	public String getDataID() {
		return DataID;
	}

	public void setDataID(String dataID) {
		DataID = dataID;
	}

	public ArrayList<Answers> getAnswersList() {
		return listAnswer;
	}

	public void setAnswers(Answers answer) {
		if (listAnswer!=null && listAnswer.size()>0)
		{
			for (int i=0;i<listAnswer.size();i++)
			{
				if (listAnswer.get(i).getAnswerID()!=null && listAnswer.get(i).getAnswerID().equals(answer.getAnswerID())) return;;
			}
		}
		listAnswer.add(answer);
	}

	public String getCritFreeText() {
		return CritFreeText;
	}

	public void setCritFreeText(String critFreeText) {
		CritFreeText = critFreeText;
	}

	public String getAnswerID() {
		return answerID;
	}

	public void setAnswerID(String answerID) {
		this.answerID = answerID;
	}

	public String getAnswerText() {
		if (answerText == null || answerText.equals(""))
			return freetext;
		else
			return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getMi() {
		return mi;
	}

	public void setMi(String mi) {
		this.mi = mi;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public void setQuestion(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionTypeLink() {
		return QuestionTypeLink;
	}

	public void setQuestionTypeLink(String questionTypeLink) {
		QuestionTypeLink = questionTypeLink;
	}

	public String getFillingEndLocation() {
		return FillingEndLocation;
	}

	public void setFillingEndLocation(String fillingEndLocation) {
		FillingEndLocation = fillingEndLocation;
	}

	public int getAnswerPoistion(ArrayList<Answers> listAnswers) {
		// if (listAnswers!=null && )
		return 0;
	}

	public String getMiType() {
		return this.mitype;
	}

	public void setMiType(String miType) {
		this.mitype = miType;
	}

	public void setSetID(String set) {
		this.SetID = set;
	}

	public String getSetID() {
		return this.SetID;
	}

	public void setAtachedFiles(ArrayList<InProgressFileData> attached_files) {
		this.attachedFiles = attached_files;

	}

	public ArrayList<InProgressFileData> getAtachedFiles() {
		return this.attachedFiles;

	}

	public void setStartTime(String thisStartTime) {
		this.startTime = thisStartTime;
	}

	public String getStartTime() {
		return startTime;
	}

}

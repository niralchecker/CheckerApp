package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.checker.sa.android.helper.CustomCheckButtonGroup;
import com.checker.sa.android.helper.CustomRadioButtonGroup;
import com.checker.sa.android.helper.MultiSelectionSpinner;
import com.checker.sa.android.helper.UIQuestionGroupHelper;

public class dataQuestionGroup implements Serializable {
	String qbjecttype;
	private RadioGroup radioGroup;
	private Spinner spinner;

	private CustomCheckButtonGroup CustomCheckboxgrp;

	public CustomCheckButtonGroup getCustomCheckboxgrp() {
		return CustomCheckboxgrp;
	}

	public void setCustomCheckboxgrp(CustomCheckButtonGroup radioGroup) {
		this.CustomCheckboxgrp = radioGroup;
		checkboxgrp = null;
	}

	public RadioGroup getRadioGroup() {
		return radioGroup;
	}

	public void setRadioGroup(RadioGroup radioGroup) {
		this.radioGroup = radioGroup;
	}

	private CustomRadioButtonGroup customradioGroup;
	private Spinner difspinner;

	public CustomRadioButtonGroup getcustomRadioGroup() {
		return customradioGroup;
	}

	public void setcustomRadioGroup(CustomRadioButtonGroup radioGroup) {
		this.customradioGroup = radioGroup;
		radioGroup = null;
	}

	public Spinner getSpinner() {
		return spinner;
	}

	public void setSpinner(Spinner spinner) {
		this.spinner = spinner;
	}

	boolean isMandatory = false;

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	int qtype;
	int displayType;
	String objcode;
	EditText freeTextbox;
	EditText finishTime;
	EditText textbox;
	Objects questionObject;
	String orderID;
	QuestionnaireData qd;
	ArrayList<Workers> listWorkers;
	ListView workerListView;
	int selectedWorker;
	// ///////Workers
	// ////////////Branches
	ArrayList<Branches> listBranches;
	ListView branchListView;
	int selectedBranch;
	// /////////////Branches
	ArrayList<Answers> listAnswers;
	LinearLayout checkboxgrp;
	private MultiSelectionSpinner multiSpinner;
	private Button btextbox;
	private Answers selectedAnswers = null;
	private View wholeMiView;
	private TextView miextView;

	// ////////////////////////////////////////////////////
	public int getQbjecttype() {
		return Integer.valueOf(qbjecttype);
	}

	public void setQbjecttype(String qbjecttype) {
		this.qbjecttype = qbjecttype;
	}

	public int getQtype() {
		return qtype;
	}

	public void setQtype(int qtype) {
		this.qtype = qtype;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public String getObjcode() {
		return objcode;
	}

	public void setObjcode(String objcode) {
		this.objcode = objcode;
	}

	public EditText getFreeTextbox() {
		return freeTextbox;
	}

	public void setFreeTextbox(EditText freeTextbox) {
		this.freeTextbox = freeTextbox;
	}

	public EditText getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(EditText finishTime) {
		this.finishTime = finishTime;
	}

	public Button getbTextbox() {
		return btextbox;
	}

	public void setbTextbox(Button textbox) {
		this.btextbox = textbox;
	}

	public EditText getTextbox() {
		return textbox;
	}

	public void setTextbox(EditText textbox) {
		this.textbox = textbox;
	}

	public Objects getQuestionObject() {
		return questionObject;
	}

	public Answers getSelectedAnswer(int index) {
		if (index >= 0 && questionObject.getListAnswers() != null
				&& questionObject.getListAnswers().size() > index)
			return questionObject.getListAnswers().get(index);
		return null;
	}

	public void setQuestionObject(Objects questionObject) {
		this.questionObject = questionObject;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public QuestionnaireData getQd() {
		return qd;
	}

	public void setQd(QuestionnaireData qd) {
		this.qd = qd;
	}

	public ArrayList<Workers> getListWorkers() {
		return listWorkers;
	}

	public void setListWorkers(ArrayList<Workers> listWorkers) {
		this.listWorkers = listWorkers;
	}

	public ListView getWorkerListView() {
		return workerListView;
	}

	public void setWorkerListView(ListView workerListView) {
		this.workerListView = workerListView;
	}

	public int getSelectedWorker() {
		return selectedWorker;
	}

	public void setSelectedWorker(int selectedWorker) {
		this.selectedWorker = selectedWorker;
	}

	public ArrayList<Branches> getListBranches() {
		return listBranches;
	}

	public void setListBranches(ArrayList<Branches> listBranches) {
		this.listBranches = listBranches;
	}

	public ListView getBranchListView() {
		return branchListView;
	}

	public void setBranchListView(ListView branchListView) {
		this.branchListView = branchListView;
	}

	public int getSelectedBranch() {
		return selectedBranch;
	}

	public void setSelectedBranch(int selectedBranch) {
		this.selectedBranch = selectedBranch;
	}

	public ArrayList<Answers> getListAnswers() {
		return listAnswers;
	}

	public void setListAnswers(ArrayList<Answers> listAnswers) {
		this.listAnswers = listAnswers;
	}

	public LinearLayout getCheckboxgrp() {
		return checkboxgrp;
	}

	public void setCheckboxgrp(LinearLayout checkboxgrp) {
		this.checkboxgrp = checkboxgrp;
	}

	public void setMultiSpinner(MultiSelectionSpinner multiSpinner) {
		this.multiSpinner = multiSpinner;
	}

	public MultiSelectionSpinner getMultiSpinner() {
		return multiSpinner;
	}

	public void setOrdinarySpinner(Spinner spinner2) {
		if (spinner2 != null)
			this.difspinner = spinner2;

	}

	public void setSelectedAnswer(Answers answers) {

		this.selectedAnswers = answers;
		if (this.wholeMiView != null) {
			UIQuestionGroupHelper.hide_show_mi_view(this, this.selectedAnswers);
		}
		this.selectedAnswers = answers;
	}

	public Answers getSelectedAnswer() {
		return this.selectedAnswers;
	}

	public void setWholeMiView(View v) {
		this.wholeMiView = v;
		if (this.selectedAnswers != null) {
			UIQuestionGroupHelper.hide_show_mi_view(this, this.selectedAnswers);
		}

	}

	public View getWholeMiView() {
		return this.wholeMiView;
	}

	public void setMiTextView(TextView tv) {
		this.miextView = tv;

	}

	public TextView getMiTextView() {
		return this.miextView;

	}
}

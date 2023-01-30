package com.mor.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.Objects;
import com.checker.sa.android.data.QuestionnaireData;

public class WatchObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QuestionnaireData savedQuestion;
	private Objects question;
	private ArrayList<Integer> selectedAnswers;
	private String selectedAnswer;
	public String getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	private String validationString = "";
	private boolean isLast = false;

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public String getValidationString() {
		return validationString;
	}

	public void setValidationString(String validationString) {
		this.validationString = validationString;
	}

	public ArrayList<Integer> getSelectedAnswers() {
		return selectedAnswers;
	}

	public void setSelectedAnswers(ArrayList<Integer> selectedAnswers) {
		this.selectedAnswers = selectedAnswers;
	}

	public QuestionnaireData getSavedQuestion() {
		return savedQuestion;
	}

	public Objects getQuestion() {
		return question;
	}

	public void setThisQuestion(Objects qObj) {
		this.question = qObj;

	}

	public void setThisSavedAnswer(QuestionnaireData questionnaireData) {
		this.savedQuestion = questionnaireData;
	}

}

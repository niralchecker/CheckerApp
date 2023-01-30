package com.checker.sa.android.data;

import java.io.Serializable;

public class Task  implements Serializable
{
	private String mandatory,allowCheckerToChange,autoCreateAction,autoAssignAction,
	autoDueDate,allowCheckerToChangeType,trainingToTake;
	private String defaultTask,taskTypeLink;
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getAllowCheckerToChange() {
		return allowCheckerToChange;
	}
	public void setAllowCheckerToChange(String allowCheckerToChange) {
		this.allowCheckerToChange = allowCheckerToChange;
	}
	public String getAutoCreateAction() {
		return autoCreateAction;
	}
	public void setAutoCreateAction(String autoCreateAction) {
		this.autoCreateAction = autoCreateAction;
	}
	public String getAutoAssignAction() {
		return autoAssignAction;
	}
	public void setAutoAssignAction(String autoAssignAction) {
		this.autoAssignAction = autoAssignAction;
	}
	public String getAutoDueDate() {
		return autoDueDate;
	}
	public void setAutoDueDate(String autoDueDate) {
		this.autoDueDate = autoDueDate;
	}
	public String getAllowCheckerToChangeType() {
		return allowCheckerToChangeType;
	}
	public void setAllowCheckerToChangeType(String allowCheckerToChangeType) {
		this.allowCheckerToChangeType = allowCheckerToChangeType;
	}
	public String getTrainingToTake() {
		return trainingToTake;
	}
	public void setTrainingToTake(String trainingToTake) {
		this.trainingToTake = trainingToTake;
	}
	public String getDefaultTask() {
		return defaultTask;
	}
	public void setDefaultTask(String defaultTask) {
		this.defaultTask = defaultTask;
	}
	public String getTaskTypeLink() {
		return taskTypeLink;
	}
	public void setTaskTypeLink(String taskTypeLink) {
		this.taskTypeLink = taskTypeLink;
	}
	
	
}

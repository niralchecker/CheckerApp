package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class BranchProperties  implements Serializable{
	// <lb2pID>136216</lb2pID>
	// <BranchLink>81445</BranchLink>
	// <PropLink>1526</PropLink>
	// <ValueLink>16276</ValueLink>
	// <PropID>1526</PropID>
	// <PropertyName>Geographic Region</PropertyName>
	// <ClientLink>1013</ClientLink>
	// <Order>2</Order>
	// <Mandatory>0</Mandatory>
	// <ChildPropLink>1527</ChildPropLink>
	// <AllowOtherAddition>0</AllowOtherAddition>
	// <ValueID>16276</ValueID>
	// <Content>CHD</Content>
	// <IsActive>1</IsActive>
	// <TargetFinalGrade>0.000000</TargetFinalGrade>

	private String lb2pID;
	private String ID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	private String BranchLink;
	private String PropLink;
	private String ValueLink;
	private String PropID;
	private String PropertyName;
	private String ClientLink;
	private String Order;
	private String Mandatory;
	private String ChildPropLink;
	private String AllowOtherAddition;
	private String ValueID;
	private String Content;
	private String TargetFinalGrade;
	private String IsActive;

	public String getLb2pID() {
		return lb2pID;
	}

	public void setLb2pID(String lb2pID) {
		this.lb2pID = lb2pID;
	}

	public String getBranchLink() {
		return BranchLink;
	}

	public void setBranchLink(String branchLink) {
		BranchLink = branchLink;
	}

	public String getPropLink() {
		return PropLink;
	}

	public void setPropLink(String propLink) {
		PropLink = propLink;
	}

	public String getValueLink() {
		return ValueLink;
	}

	public void setValueLink(String valueLink) {
		ValueLink = valueLink;
	}

	public String getPropID() {
		return PropID;
	}

	public void setPropID(String propID) {
		PropID = propID;
	}

	public String getPropertyName() {
		return PropertyName;
	}

	public void setPropertyName(String propertyName) {
		PropertyName = propertyName;
	}

	public String getClientLink() {
		return ClientLink;
	}

	public void setClientLink(String clientLink) {
		ClientLink = clientLink;
	}

	public String getOrder() {
		return Order;
	}

	public void setOrder(String order) {
		Order = order;
	}

	public String getMandatory() {
		return Mandatory;
	}

	public void setMandatory(String mandatory) {
		Mandatory = mandatory;
	}

	public String getChildPropLink() {
		return ChildPropLink;
	}

	public void setChildPropLink(String childPropLink) {
		ChildPropLink = childPropLink;
	}

	public String getAllowOtherAddition() {
		return AllowOtherAddition;
	}

	public void setAllowOtherAddition(String allowOtherAddition) {
		AllowOtherAddition = allowOtherAddition;
	}

	public String getValueID() {
		return ValueID;
	}

	public void setValueID(String valueID) {
		ValueID = valueID;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getTargetFinalGrade() {
		return TargetFinalGrade;
	}

	public void setTargetFinalGrade(String targetFinalGrade) {
		TargetFinalGrade = targetFinalGrade;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}

}

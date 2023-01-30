package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Cert implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// <cert ID="2" type="array">
	// <CertificateID>2</CertificateID>
	// <CertificateName>Basic Mystery Shopper</CertificateName>
	// <Description>
	// Jmx0O3AmZ3Q7VGhpcyBpcyBvdXIgbW9zdCBiYXNpYyBjZXJ0aWZpY2F0aW9uLiZsdDsvcCZndDsNCg0KJmx0O3AmZ3Q7QWZ0ZXIgcGFzc2luZyB0aGlzIGNlcnRpZmljYXRpb24sIHlvdSB3aWxsIGJlIGVsaWdpYmxlIGZvciBhbGwgdGhlIG90aGVyIGNlcnRpZmljYXRpb25zIGluIHRoZSBzeXN0ZW0uJmx0Oy9wJmd0Ow==
	// </Description>
	// <IsActive>1</IsActive>
	// <CompanyLink>68</CompanyLink>
	// <CertificateCode/>
	// <DependencySetLink>250</DependencySetLink>
	// <DependencySetGrade>80</DependencySetGrade>
	// <DependencyCertificateLink>0</DependencyCertificateLink>
	// <AddPriorityPoints>0</AddPriorityPoints>
	// <HideInCheckersList>0</HideInCheckersList>
	// <DateGiven>2017-11-23 10:37:54</DateGiven>
	// <Status>Passed</Status>
	// <SetData type="array">
	// <SetID type="array">
	// <SetID ID="0">250</SetID>
	// </SetID>
	// <SetName type="array">
	// <SetName ID="0">Mystery Shopping Basics</SetName>
	// </SetName>
	// </SetData>
	// </cert>

	ArrayList<String> setIds;
	String CertificateID;
	String CertificateName;
	String Description;
	String IsActive;
	String CompanyLink;
	String CertificateCode;
	String DependenceSetLink;
	String DependenceSetGrade;
	String DependencyCertificateLink;
	String AddPriorityPoints;
	String HideInCheckersList;
	String DateGiven;
	String Status;
	String SetStatus;
	String SetID;
	String SetName;
	String TimesTaken;
	String MaxRetake;
	String LastDateTaken;
	String RetakeChillingDays;
	String Grade;
	
	

	public String getDependenceSetLink() {
		return DependenceSetLink;
	}

	public void setDependenceSetLink(String dependenceSetLink) {
		DependenceSetLink = dependenceSetLink;
	}

	public String getDependenceSetGrade() {
		return DependenceSetGrade;
	}

	public void setDependenceSetGrade(String dependenceSetGrade) {
		DependenceSetGrade = dependenceSetGrade;
	}

	public String getSetName() {
		return SetName;
	}

	public void setSetName(String setName) {
		SetName = setName;
	}

	public String getTimesTaken() {
		return TimesTaken;
	}

	public void setTimesTaken(String timesTaken) {
		TimesTaken = timesTaken;
	}

	public String getMaxRetake() {
		return MaxRetake;
	}

	public void setMaxRetake(String maxRetake) {
		MaxRetake = maxRetake;
	}

	public String getLastDateTaken() {
		return LastDateTaken;
	}

	public void setLastDateTaken(String lastDateTaken) {
		LastDateTaken = lastDateTaken;
	}

	public String getRetakeChillingDays() {
		return RetakeChillingDays;
	}

	public void setRetakeChillingDays(String retakeChillingDays) {
		RetakeChillingDays = retakeChillingDays;
	}

	public String getGrade() {
		return Grade;
	}

	public void setGrade(String grade) {
		Grade = grade;
	}

	public String getSetID() {
		return SetID;
	}

	public ArrayList<String> getSetIds() {
		return setIds;
	}

	public void setSetIds(ArrayList<String> setIds) {
		this.setIds = setIds;
	}

	public String getCertificateID() {
		return CertificateID;
	}

	public void setCertID(String certificateID) {
		CertificateID = certificateID;
	}

	public void setCertificateID(String certificateID) {
		CertificateID = certificateID;
	}

	public String getCertificateName() {
		return CertificateName;
	}

	public void setCertName(String certificateName) {
		CertificateName = certificateName;
	}

	public void setCertificateName(String certificateName) {
		CertificateName = certificateName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}

	public String getCompanyLink() {
		return CompanyLink;
	}

	public void setCompanyLink(String companyLink) {
		CompanyLink = companyLink;
	}

	public String getCertificateCode() {
		return CertificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		CertificateCode = certificateCode;
	}

	public String getDependencySetLink() {
		return SetID;
	}

	public void setDependencySetLink(String dependenceSetLink) {
		SetID = dependenceSetLink;
	}

	public String getDependencySetGrade() {
		return DependenceSetGrade;
	}

	public void setDependencySetGrade(String dependenceSetGrade) {
		DependenceSetGrade = dependenceSetGrade;
	}

	public String getDependencyCertificateLink() {
		return DependencyCertificateLink;
	}

	public void setDependencyCertificateLink(String dependencyCertificateLink) {
		DependencyCertificateLink = dependencyCertificateLink;
	}

	public String getAddPriorityPoints() {
		return AddPriorityPoints;
	}

	public void setAddPriorityPoints(String addPriorityPoints) {
		AddPriorityPoints = addPriorityPoints;
	}

	public String getHideInCheckersList() {
		return HideInCheckersList;
	}

	public void setHideInCheckersList(String hideInCheckersList) {
		HideInCheckersList = hideInCheckersList;
	}

	public String getDateGiven() {
		return DateGiven;
	}

	public void setDateGiven(String dateGiven) {
		DateGiven = dateGiven;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSetStatus() {
		return SetStatus;
	}

	public void setSetStatus(String setStatus) {
		SetStatus = setStatus;
	}

	public void setSetID(String temp) {
		if (setIds == null)
			setIds = new ArrayList<String>();
		if (temp != null && !temp.equals(""))
			setIds.add(temp);
	}

}

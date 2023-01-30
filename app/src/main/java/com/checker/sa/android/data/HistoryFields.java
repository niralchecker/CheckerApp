package com.checker.sa.android.data;

import java.util.ArrayList;

public class HistoryFields {
	// <?xml version="1.0"?>
	// <Crits>
	// <HIST0 type="array">
	// <CritID>258956</CritID>
	// <FinishTime>2018-05-10 00:00:00</FinishTime>
	// <ActualFinishTime>2018-08-16 17:16:13</ActualFinishTime>
	// <ClientName>QAGUI</ClientName>
	// <BranchName>ENG</BranchName>
	// <CityName>Staffordshire</CityName>
	// <RegionName>Staffordshire</RegionName>
	// <Address>Shaw Wood Farm</Address>
	// <SetName>Images File?</SetName>
	// <Result>100</Result>
	// <LinkedMoneySum></LinkedMoneySum>
	// <Status>Finished</Status>
	// <WasSentBack>0</WasSentBack>
	// <QaGradeAdjusted></QaGradeAdjusted>
	// <QaDoneByUser></QaDoneByUser>
	// <QaNote></QaNote>
	// <CustomField313 type="array">
	// <FieldName>Start time for order</FieldName>
	// <Value>0000-00-00</Value>
	// </CustomField313>
	// <CustomField497 type="array">
	// <FieldName>Shopper Guidelines</FieldName>
	// <Value></Value>
	// </CustomField497>
	// <CustomField540 type="array">
	// <FieldName>Testing 1</FieldName>
	// <Value></Value>
	// </CustomField540>
	// <CustomField571 type="array">
	// <FieldName>NUMBER</FieldName>
	// <Value>0</Value>
	// </CustomField571>
	// <CustomField572 type="array">
	// <FieldName>TEXTLINE</FieldName>
	// <Value></Value>
	// </CustomField572>
	// <CustomField573 type="array">
	// <FieldName>TEXTBLOCK</FieldName>
	// <Value></Value>
	// </CustomField573>
	// <CustomField574 type="array">
	// <FieldName>DATE</FieldName>
	// <Value>0000-00-00</Value>
	// </CustomField574>
	// <CustomField575 type="array">
	// <FieldName>TIME</FieldName>
	// <Value>00:00:00</Value>
	// </CustomField575>
	// <CustomField576 type="array">
	// <FieldName>PHONE</FieldName>
	// <Value></Value>
	// </CustomField576>
	// <CustomField577 type="array">
	// <FieldName>CHECKBOX</FieldName>
	// <Value>0</Value>
	// </CustomField577>
	// <CustomField578 type="array">
	// <FieldName>FILEUPLOADED</FieldName>
	// <Value></Value>
	// </CustomField578>
	// </HIST0>
	// </Crits>

	// <?xml version="1.0"?>
	// <Crits>
	// <HIST0 type="array">
	// <CritID>258956</CritID>
	// <FinishTime>2018-05-10 00:00:00</FinishTime>
	// <ActualFinishTime>2018-08-16 17:16:13</ActualFinishTime>
	// <ClientName>QAGUI</ClientName>
	// <BranchName>ENG</BranchName>
	// <CityName>Staffordshire</CityName>
	// <RegionName>Staffordshire</RegionName>
	// <Address>Shaw Wood Farm</Address>
	// <SetName>Images File?</SetName>
	// <Result>100</Result>
	// <LinkedMoneySum></LinkedMoneySum>
	// <Status>Finished</Status>
	// <WasSentBack>0</WasSentBack>
	// <QaGradeAdjusted></QaGradeAdjusted>
	// <QaDoneByUser></QaDoneByUser>
	// <QaNote></QaNote>
	private String CritID;
	private String FinishTime;
	private String ActualFinishTime;
	private String ClientName;
	private String BranchName;
	private String CityName;
	private String RegionName;
	private String Address;
	private String SetName;
	private String Result;
	private String LinkedMoneySum;
	private String Status;
	private String WasSentBack;
	private String QaGradeAdjusted;
	private String QaDoneByUser;
	private String QaNote;
	private ArrayList<CustomFields> customFields;
	private CustomFields thisCustomField;

	public String getCritID() {
		return CritID;
	}

	public void setCritID(String critID) {
		CritID = critID;
	}

	public String getFinishTime() {
		return FinishTime;
	}

	public void setFinishTime(String finishTime) {
		FinishTime = finishTime;
	}

	public String getActualFinishTime() {
		return ActualFinishTime;
	}

	public void setActualFinishTime(String actualFinishTime) {
		ActualFinishTime = actualFinishTime;
	}

	public String getClientName() {
		return ClientName;
	}

	public void setClientName(String clientName) {
		ClientName = clientName;
	}

	public String getBranchName() {
		return BranchName;
	}

	public void setBranchName(String branchName) {
		BranchName = branchName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getRegionName() {
		return RegionName;
	}

	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getSetName() {
		return SetName;
	}

	public void setSetName(String setName) {
		SetName = setName;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getLinkedMoneySum() {
		return LinkedMoneySum;
	}

	public void setLinkedMoneySum(String linkedMoneySum) {
		LinkedMoneySum = linkedMoneySum;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getWasSentBack() {
		return WasSentBack;
	}

	public void setWasSentBack(String wasSentBack) {
		WasSentBack = wasSentBack;
	}

	public String getQaGradeAdjusted() {
		return QaGradeAdjusted;
	}

	public void setQaGradeAdjusted(String qaGradeAdjusted) {
		QaGradeAdjusted = qaGradeAdjusted;
	}

	public String getQaDoneByUser() {
		return QaDoneByUser;
	}

	public void setQaDoneByUser(String qaDoneByUser) {
		QaDoneByUser = qaDoneByUser;
	}

	public String getQaNote() {
		return QaNote;
	}

	public void setQaNote(String qaNote) {
		QaNote = qaNote;
	}

	public void addCustomFields(CustomFields field) {
		if (customFields == null)
			customFields = new ArrayList<CustomFields>();
		customFields.add(field);
	}

	public ArrayList<CustomFields> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(ArrayList<CustomFields> customFields) {
		this.customFields = customFields;
	}

	public void setFieldName(String fieldName) {
		if (thisCustomField == null)
			thisCustomField = new CustomFields();
		thisCustomField.setName(fieldName);
	}

	public void setValue(String fieldName) {
		if (thisCustomField != null) {
			thisCustomField.setValue(fieldName);
			if (customFields == null)
				customFields = new ArrayList<CustomFields>();
			customFields.add(thisCustomField);
			thisCustomField = null;
		}
	}
}

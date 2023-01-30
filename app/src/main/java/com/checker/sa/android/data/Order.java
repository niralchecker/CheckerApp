package com.checker.sa.android.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Order implements Serializable {

	String lo2cID, OrderLink, CheckerLink, SpecificStatusLink, CritLink,
			AcceptedByCheckerTime, AssignmentTime, MassID, OrderID, Date,
			StatusName, Description, ClientLink, BranchLink, SetName,
			BriefingContent, SetLink, ClientName, BranchName, BranchFullname,
			CityName, Address, BranchPhone, OpeningHours, TimeStart, TimeEnd,
			SetID, BranchLat, BranchLong, Fullname, CheckerCode, sPurchase,
			sPurchaseDescription, BranchCode, sPurchaseLimit,
			sNonRefundableServicePayment, sTransportationPayment,
			sCriticismPayment, sBonusPayment, AllowShoppersToRejectJobs,
			RegionName, ProjectName;

	boolean asArchive;
	private String ProjectID;

	public SubmitQuestionnaireData getSubmitArchiveData()
	{
		SubmitQuestionnaireData sq=new SubmitQuestionnaireData();
		sq.setDB_TABLE_SUBMITSURVEY_purchase_description("ARCHIVE");
		sq.setDB_TABLE_SUBMITSURVEY_purchase_details("ARCHIVE");
		sq.setDB_TABLE_SUBMITSURVEY_purchase_payment("0");
		sq.setDB_TABLE_SUBMITSURVEY_service_invoice_number("0");
		sq.setDB_TABLE_SUBMITSURVEY_service_description("ARCHIVE");
		sq.setDB_TABLE_SUBMITSURVEY_service_payment("0");
		sq.setDB_TABLE_SUBMITSURVEY_transportation_description("ARCHIVE");
		sq.setDB_TABLE_SUBMITSURVEY_transportation_payment("0");
		sq.setTotalSent("0");
		sq.setQuotas(null);
		sq.setSetid(getSetID());
		sq.setOrderid(getOrderID());
		if (getOrderID().contains("-"))
			sq.setQuotas(new ArrayList<Quota>());
		else sq.setQuotas(null);
		sq.setElng("0.0");
		sq.setElt("0.0");
		sq.setSlng("0.0");
		sq.setSlt("0.0");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss",
				Locale.ENGLISH);
		sq.setFt("ARCHIVE");
		sq.setFtime(sdf.format(new Date().getTime()));
		sq.setStime(getStartTime());

		Date dt = new Date();
		long mili = dt.getTime();
		String millis="0.0";
		if (millis == null || millis.length() <= 0)
			millis = mili + "";
		sq.setUnix(millis);
		return sq;
	}
	public String getRegionName() {
		return RegionName;
	}

	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getProjectID() {
		return ProjectID;
	}

	public void setProjectID(String projectID) {
		ProjectID = projectID;
	}

	int Index, Count;
	private String start_time;

	private ArrayList<String> customFields;

	private ArrayList<String> customFieldVals;

	private String isprogressOnserver;
	private boolean isDeleted;

	public String getAllowShoppersToRejectJobs() {
		return AllowShoppersToRejectJobs;
	}

	public void setAllowShoppersToRejectJobs(String allowShoppersToRejectJobs) {
		AllowShoppersToRejectJobs = allowShoppersToRejectJobs;
	}

	public int getIndex() {
		return Index;
	}

	public void setIndex(int index) {
		Index = index;
	}

	public int getCount() {
		return Count;
	}

	public void setCount() {
		Count++;// = c;
	}

	public void setJobCount(int c) {
		Count = c;
	}

	public String getBranchCode() {
		return BranchCode;
	}

	public void setBranchCode(String branchCode) {
		BranchCode = branchCode;
	}

	public String getLo2cID() {
		return lo2cID;
	}

	public void setLo2cID(String lo2cID) {
		this.lo2cID = lo2cID;
	}

	public String getFullname() {
		return Fullname;
	}

	public void setFullname(String fullname) {
		Fullname = fullname;
	}

	public String getCheckerCode() {
		return CheckerCode;
	}

	public String getsPurchaseLimit() {
		return this.sPurchaseLimit;
	}

	public void setsPurchaseLimit(String sPurchaseLimit) {
		this.sPurchaseLimit = sPurchaseLimit;
	}

	public String getsNonRefundableServicePayment() {
		return sNonRefundableServicePayment;
	}

	public void setsNonRefundableServicePayment(
			String sNonRefundableServicePayment) {
		this.sNonRefundableServicePayment = sNonRefundableServicePayment;
	}

	public String getsTransportationPayment() {
		return sTransportationPayment;
	}

	public void setsTransportationPayment(String sTransportationPayment) {
		this.sTransportationPayment = sTransportationPayment;
	}

	public String getsCriticismPayment() {
		return sCriticismPayment;
	}

	public void setsCriticismPayment(String sCriticismPayment) {
		this.sCriticismPayment = sCriticismPayment;
	}

	public String getsBonusPayment() {
		return sBonusPayment;
	}

	public void setsBonusPayment(String sBonusPayment) {
		this.sBonusPayment = sBonusPayment;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public void setCount(int count) {
		Count = count;
	}

	public void setCheckerCode(String checkerCode) {
		CheckerCode = checkerCode;
	}

	public String getsPurchase() {
		return sPurchase;
	}

	public void setsPurchase(String sPurchase) {
		this.sPurchase = sPurchase;
	}

	public String getsPurchaseDescription() {
		return sPurchaseDescription;
	}

	public void setsPurchaseDescription(String sPurchaseDescription) {
		this.sPurchaseDescription = sPurchaseDescription;
	}

	public String getlo2cID() {
		return lo2cID;
	}

	public void setlo2cID(String lo2cID) {
		this.lo2cID = lo2cID;
	}

	public String getOrderLink() {
		return OrderLink;
	}

	public void setOrderLink(String orderLink) {
		OrderLink = orderLink;
	}

	public String getCheckerLink() {
		return CheckerLink;
	}

	public void setCheckerLink(String checkerLink) {
		CheckerLink = checkerLink;
	}

	public String getSpecificStatusLink() {
		return SpecificStatusLink;
	}

	public void setSpecificStatusLink(String specificStatusLink) {
		SpecificStatusLink = specificStatusLink;
	}

	public String getCritLink() {
		return CritLink;
	}

	public void setCritLink(String critLink) {
		CritLink = critLink;
	}

	public String getAcceptedByCheckerTime() {
		return AcceptedByCheckerTime;
	}

	public void setAcceptedByCheckerTime(String acceptedByCheckerTime) {
		AcceptedByCheckerTime = acceptedByCheckerTime;
	}

	public String getAssignmentTime() {
		return AssignmentTime;
	}

	public void setAssignmentTime(String assignmentTime) {
		AssignmentTime = assignmentTime;
	}

	public String getMassID() {
		return MassID;
	}

	public void setMassID(String massID) {
		MassID = massID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getClientLink() {
		return ClientLink;
	}

	public void setClientLink(String clientLink) {
		ClientLink = clientLink;
	}

	public String getBranchLink() {
		return BranchLink;
	}

	public void setBranchLink(String branchLink) {
		BranchLink = branchLink;
	}

	public String getSetName() {
		return SetName;
	}

	public void setSetName(String setName) {
		SetName = setName;
	}

	public String getBriefingContent() {
		return BriefingContent;
	}

	public void setBriefingContent(String briefingContent) {
		BriefingContent = briefingContent;
	}

	public String getSetLink() {
		return SetLink;
	}

	public void setSetLink(String setLink) {
		SetLink = setLink;
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

	public String getBranchFullname() {
		return BranchFullname;
	}

	public void setBranchFullname(String branchFullname) {
		BranchFullname = branchFullname;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getBranchPhone() {
		return BranchPhone;
	}

	public void setBranchPhone(String branchPhone) {
		BranchPhone = branchPhone;
	}

	public String getOpeningHours() {
		return OpeningHours;
	}

	public void setOpeningHours(String openingHours) {
		OpeningHours = openingHours;
	}

	public String getTimeStart() {
		return TimeStart;
	}

	public void setTimeStart(String timeStart) {
		TimeStart = timeStart;
	}

	public String getTimeEnd() {
		return TimeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		TimeEnd = timeEnd;
	}

	public String getSetID() {
		return SetID;
	}

	public void setSetID(String setID) {
		SetID = setID;
	}

	public String getBranchLat() {
		return BranchLat;
	}

	public void setBranchLat(String branchLat) {
		BranchLat = branchLat;
	}

	public String getBranchLong() {
		return BranchLong;
	}

	public void setBranchLong(String branchLong) {
		BranchLong = branchLong;
	}

	public void setStartTime(String start_time) {
		this.start_time = start_time;

	}

	public String getStartTime() {
		return this.start_time;

	}

	public ArrayList<String> getCustomFields() {
		return this.customFields;
	}

	public ArrayList<String> getCustomFieldVals() {
		return this.customFieldVals;
	}

	public void setCustomField(String name, String temp) {
		if (this.customFields == null || this.customFieldVals == null) {
			this.customFields = new ArrayList<String>();
			this.customFieldVals = new ArrayList<String>();
		}
		this.customFields.add(name);
		this.customFieldVals.add(temp);
	}

	public void setIsJobInProgressOnServer(String string) {
		isprogressOnserver = string;
	}

	public String getIsJobInProgressOnServer() {
		return isprogressOnserver;
	}

	public void setIsJobDeleted(String deleted) {
		if (deleted != null && deleted.equals("true"))
			isDeleted = true;
	}

	public boolean getIsJobDeleted() {
		return isDeleted;
	}

    public void setAsArchive(boolean asArchive) {
        this.asArchive = asArchive;
    }

    public boolean getAsArchive() {
        return asArchive;
    }

    public boolean isDataIdEnabled(Set set,String dataID) {
//		order != null && order.getOrderID()!=null && !order.getOrderID().contains("-") && order.getIsJobInProgressOnServer() != null
//				&& order.getIsJobInProgressOnServer().contains("true")
		if (set.getEditorNotes()==null || set.getEditorNotes().size()==0) return false;
		if (set!=null && set.getEditorNotes()!=null)
		{

			for (int i=0;set.getEditorNotes()!=null && i<set.getEditorNotes().size();i++)
			{
				if (set.getEditorNotes().get(i)!=null &&
						set.getEditorNotes().get(i).getDataLink()!=null && dataID!=null &&
						dataID.contains(set.getEditorNotes().get(i).getDataLink()))
				{
					return false;
				}
			}
		}
        return true;//getIsJobInProgressOnServer().toLowerCase().contains("true");
    }

	public EditorNote getEditorNoteAgainstQuestion(Set set,String dataID) {
		if (set!=null && set.getEditorNotes()!=null)
		{
			for (int i=0;set.getEditorNotes()!=null && i<set.getEditorNotes().size();i++)
			{
				if (set.getEditorNotes().get(i)!=null &&
						set.getEditorNotes().get(i).getDataLink()!=null && dataID!=null &&
						dataID.contains(set.getEditorNotes().get(i).getDataLink()))
				{
					return set.getEditorNotes().get(i);
				}
			}
		}
		return null;//getIsJobInProgressOnServer().toLowerCase().contains("true");
	}

	public EditorNote isEditorNoteAgainstQuestion(Set set,String dataID) {
		EditorNote note=null;
		if (set!=null && set.getEditorNotes()!=null)
		{
			for (int i=0;set.getEditorNotes()!=null && i<set.getEditorNotes().size();i++)
			{
				if (set.getEditorNotes().get(i)!=null &&
						set.getEditorNotes().get(i).getDataLink()!=null && dataID!=null &&
						dataID.contains(set.getEditorNotes().get(i).getDataLink()))
				{
					note= set.getEditorNotes().get(i);
				}
			}
		}
		if (note!=null && note.getUserName()!=null && note.getUserName().length()>0
				&& note.getNoteFromEditor()!=null && note.getNoteFromEditor().length()>0  )
		return note;//getIsJobInProgressOnServer().toLowerCase().contains("true");
		else return null;
	}
}

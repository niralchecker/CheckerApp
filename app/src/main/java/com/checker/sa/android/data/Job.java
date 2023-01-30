package com.checker.sa.android.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.provider.ContactsContract.Data;
import android.text.format.DateFormat;
import android.view.View;

import com.checker.sa.android.helper.Helper;
import com.google.android.gms.maps.model.Marker;

public class Job implements Serializable {
	// <OrderID>193201</OrderID>
	// <FIELDWORKgrp>2017-03-12826602169444201</FIELDWORKgrp>
	// <OrderCount>1</OrderCount>
	// <lo2cID></lo2cID>
	// <OrderLink></OrderLink>
	// <CheckerLink></CheckerLink>
	// <SpecificStatusLink></SpecificStatusLink>
	// <CritLink></CritLink>
	// <AcceptedByCheckerTime></AcceptedByCheckerTime>
	// <AssignmentTime></AssignmentTime>
	// <UserLink></UserLink>
	// <MassID>44420</MassID>
	// <Date>2017-03-12</Date>
	// <StatusName>Ordered</StatusName>
	// <Description>Import Sample</Description>
	// <ClientLink>1047</ClientLink>
	// <ls2chID>286963</ls2chID>
	// <BranchLink>82660</BranchLink>
	// <SetLink>2169</SetLink>
	// <TimeStart>00:00:01</TimeStart>
	// <TimeEnd>00:00:01</TimeEnd>
	// <OrderStatusLink>1</OrderStatusLink>
	// <BriefingLink>932</BriefingLink>
	// <ClientName>A Govt. Hospital</ClientName>
	// <SetName>AGH Survey</SetName>
	// <SetID>2169</SetID>
	// <AltLangLink>0</AltLangLink>
	// <AllowCheckerToSetLang>0</AllowCheckerToSetLang>
	// <RegionName>Delhi</RegionName>
	// <BranchName>CNBC</BranchName>
	// <BranchFullname>Chacha Nehru Bal Chikitsalaya</BranchFullname>
	// <CityName>New Delhi</CityName>
	// <Address>Govt. of NCT of Delhi, Geeta Colony, New Delhi, Delhi</Address>
	// <HouseNumber></HouseNumber>
	// <Zipcode></Zipcode>
	// <BranchPhone></BranchPhone>
	// <OpeningHours></OpeningHours>
	// <BranchCode>H105</BranchCode>
	// <oaID></oaID>
	// <ApplicationComment></ApplicationComment>
	// <BranchLat>28.652089</BranchLat>
	// <BranchLong>77.271202</BranchLong>
	// <PurchaseDescription>Two hundred</PurchaseDescription>
	// <PurchaseLimit>700.00</PurchaseLimit>
	// <AlternateClientName>Health</AlternateClientName>
	// <AlternateClientDescription></AlternateClientDescription>
	// <BundleLink>0</BundleLink>
	// <missmatch>-1</missmatch>
	// <SetMaxCritsPerDay></SetMaxCritsPerDay>
	// <Duration></Duration>

	// <TransportationPayment>200.00</TransportationPayment>
	// <CriticismPayment>100.00</CriticismPayment>
	// <BonusPayment>10.00</BonusPayment>

	String CriticismPayment, BonusPayment, TransportationPayment, oaID, lo2cID,
			OrderLink, CheckerLink, SpecificStatusLink, CritLink,
			AcceptedByCheckerTime, AssignmentTime, MassID, OrderID, d_Date,
			StatusName, Description, ClientLink, BranchLink, SetName,
			BriefingContent, SetLink, ClientName, BranchName, BranchFullname,
			CityName, Address, BranchPhone, OpeningHours, TimeStart, TimeEnd,
			SetID, BranchLat, BranchLong, Fullname, CheckerCode, sPurchase,
			sPurchaseDescription, BranchCode, sPurchaseLimit,
			sNonRefundableServicePayment, sTransportationPayment, OrderCount,
			RegionName, sCriticismPayment, sBonusPayment, ApplicationComment,
			CertID, color;

	public String getColor() {
		if (color == null || color.equals("")) {
			color = "#ff0000";
		}
		return color;// hexColor
		// return CertID;
	}

	public void setColor(String certID) {
		color = certID;
	}

	public String getCertID() {
		return "29";
		// return CertID;
	}

	public void setCertID(String certID) {
		CertID = certID;
	}

	public String getCriticismPayment() {
		return CriticismPayment;
	}

	public void setCriticismPayment(String criticismPayment) {
		CriticismPayment = criticismPayment;
	}

	public String getBonusPayment() {
		return BonusPayment;
	}

	public void setBonusPayment(String bonusPayment) {
		BonusPayment = bonusPayment;
	}

	public String getTransportationPayment() {
		return TransportationPayment;
	}

	public void setTransportationPayment(String transportationPayment) {
		TransportationPayment = transportationPayment;
	}

	public String getRegionName() {
		return RegionName;
	}

	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getOrderCount() {
		return OrderCount;
	}

	public void setOrderCount(String orderCount) {
		OrderCount = orderCount;
	}

	public String getApplicationComment() {
		return ApplicationComment;
	}

	public void setApplicationComment(String applicationComment) {
		ApplicationComment = applicationComment;
	}

	Marker m;

	public Marker getM() {
		return m;
	}

	public void setM(Marker m) {
		this.m = m;
	}

	int Index, Count;
	private String start_time;

	private ArrayList<String> customFields;

	private ArrayList<String> customFieldVals;
	private View row;
	private ArrayList<Cert> certs;
	public void setCertificates(ArrayList<Cert> listCerts) {
		// TODO Auto-generated method stub
		this.certs = listCerts;
	}

	public ArrayList<Cert> getCertificates() {
		// TODO Auto-generated method stub
		return this.certs;
	}
	private ArrayList<BranchProperties> branchProps;

	public String getoaID() {
		return oaID;
	}

	public void setoaID(String oaID) {
		this.oaID = oaID;
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
		return d_Date;
	}

	public void setDate(String date) {
		d_Date = date;
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

	public long getDateTimeStamp() {
		long intdate = 0;
		if (d_Date != null) {
			try {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;

				date = dateFormat.parse(d_Date);

				intdate = date.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return intdate;
	}

	public void setRowView(View rowView) {
		row = rowView;
	}

	public View getRowView() {
		return row;
	}

	public ArrayList<String> getAltDates() {

		return new ArrayList<String>();
	}



	public void addBranchProp(BranchProperties ranchProps) {
		if (branchProps == null)
			branchProps = new ArrayList<BranchProperties>();
		for (int i = 0; i < branchProps.size(); i++) {
			if (branchProps != null && branchProps.get(i) != null
					&& branchProps.get(i).getID() != null
					&& branchProps.get(i).getID().equals(ranchProps.getID())) {
				// return;
			}
		}
		branchProps.add(ranchProps);
	}

	public ArrayList<BranchProperties> getBranchProps() {
		return branchProps;
	}

	public void setBranchProps(ArrayList<BranchProperties> branchProperties) {
		this.branchProps = branchProperties;

	}
}

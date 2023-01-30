package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.helper.Helper;

public class ListClass  implements Serializable{
	// <ListID>5</ListID>
	// <ListName>looptest1</ListName>
	// <SetLink>2037</SetLink>
	// <CompanyLink>68</CompanyLink>
	// <field_count>4</field_count>

	public String ListID;
	public String ListName;
	public String SetLink;
	public String CompanyLink;
	public String field_count;

	public ArrayList<LoopsEntry> loopData = new ArrayList<LoopsEntry>();

	public String getListID() {
		return ListID;
	}

	public void setListID(String listID) {
		ListID = listID;
	}

	public String getListName() {
		return ListName;
	}

	public void setListName(String listName) {
		ListName = listName;
	}

	public String getSetLink() {
		return SetLink;
	}

	public void setSetLink(String setLink) {
		SetLink = setLink;
	}

	public String getCompanyLink() {
		return CompanyLink;
	}

	public void setCompanyLink(String companyLink) {
		CompanyLink = companyLink;
	}

	public String getField_count() {
		return field_count;
	}

	public void setField_count(String field_count) {
		this.field_count = field_count;
	}

	public ArrayList<LoopsEntry> getLoopData() {
		return loopData;
	}

	public void setLoopData(ArrayList<LoopsEntry> loopData) {
		this.loopData = loopData;
	}

}
/********************* set Class Closed *****************************************/

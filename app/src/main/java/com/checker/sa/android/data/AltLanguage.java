package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class AltLanguage  implements Serializable
{
//	<lang_list ID="28" type="array">
//	  <AltLangID>28</AltLangID>
//	  <AltLangName>Español</AltLangName>
//	  <CompanyLink>68</CompanyLink>
//	  <AltLangDirection>1</AltLangDirection>
//	  <IsActive>1</IsActive>
//	  <InterfaceLanguage>19</InterfaceLanguage>
//	</lang_list>
	private String AltLangID;
	private String AltLangName;
	private String CompanyLink;
	private String AltLangDirection;
	private String IsActive;
	private String InterfaceLanguage;
	private String IsSelected;
	public String getAltLangID() {
		return AltLangID;
	}
	public void setAltLangID(String altLangID) {
		AltLangID = altLangID;
	}
	public String getAltLangName() {
		return AltLangName;
	}
	public void setAltLangName(String altLangName) {
		AltLangName = altLangName;
	}
	public String getCompanyLink() {
		return CompanyLink;
	}
	public void setCompanyLink(String companyLink) {
		CompanyLink = companyLink;
	}
	public String getAltLangDirection() {
		return AltLangDirection;
	}
	public void setAltLangDirection(String altLangDirection) {
		AltLangDirection = altLangDirection;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	public String getInterfaceLanguage() {
		return InterfaceLanguage;
	}
	public void setInterfaceLanguage(String interfaceLanguage) {
		InterfaceLanguage = interfaceLanguage;
	}
	public String getIsSelected() {
		return IsSelected;
	}
	public void setIsSelected(String isSelected) {
		IsSelected = isSelected;
	}
	
	
}

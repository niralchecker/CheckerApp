package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductProperties  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// <ProdPropID>52</ProdPropID>
	// <PropertyName>Price range</PropertyName>
	// <ClientLink>1013</ClientLink>
	// <Mandatory>0</Mandatory>
	// <Order>0</Order>
	// <AllowOtherAddition>0</AllowOtherAddition>
	// <IsActive>1</IsActive>
	// <Values type="array">
	private String ProdPropID;
	private String PropertyName;
	private String ClientLink;
	private String Mandatory;
	private String Order;
	private String AllowOtherAddition;
	private String IsActive;
	private ArrayList<ProductPropertyVals> propertyList;

	public String getProdPropID() {
		return ProdPropID;
	}

	public void setProdPropID(String prodPropID) {
		ProdPropID = prodPropID;
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

	public String getMandatory() {
		return Mandatory;
	}

	public void setMandatory(String mandatory) {
		Mandatory = mandatory;
	}

	public String getOrder() {
		return Order;
	}

	public void setOrder(String order) {
		Order = order;
	}

	public String getAllowOtherAddition() {
		return AllowOtherAddition;
	}

	public void setAllowOtherAddition(String allowOtherAddition) {
		AllowOtherAddition = allowOtherAddition;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}

	public ArrayList<ProductPropertyVals> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(ArrayList<ProductPropertyVals> propertyList) {
		this.propertyList = propertyList;
	}
	
	public void setPropertyList(ProductPropertyVals propertyList) {
		if (this.propertyList==null) this.propertyList=new ArrayList<ProductPropertyVals>();
		this.propertyList.add(propertyList);
	}

}

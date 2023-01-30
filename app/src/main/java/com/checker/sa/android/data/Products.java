package com.checker.sa.android.data;

import java.io.Serializable;

public class Products   implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	<Products type="array">
//
//
//
//    <Products ID="155" type="array">
//
//
//
//      <ProductID>155</ProductID>
//
//
//
//      <ProductName>Sony Xperia</ProductName>
//
//
//
//      <ClientLink>1013</ClientLink>
//
//
//
//      <IsActive>1</IsActive>
//
//
//
//      <ProductCode>sx</ProductCode>
//
//
//
//      <CheckQuantity>1</CheckQuantity>
//
//
//
//      <CheckShelfLevel>0</CheckShelfLevel>
//
//
//
//      <CheckPrice>1</CheckPrice>
//
//
//
//      <CheckPacking>0</CheckPacking>
//
//
//
//      <CheckExpiration>0</CheckExpiration>
//
//
//
//      <AddNote>1</AddNote>
//
//
//
//      <TakePicture>1</TakePicture>
//
//
//
//      <Size></Size>
//
//
//
//      <Order></Order>
//
//
//
//      <Bold>0</Bold>
//
//
//
//      <prop_id_51>133</prop_id_51>
//
//
//
//      <prop_id_52>136</prop_id_52>
//
//
//
//    </Products>
	private String ProductID;
	private String ProductName;
	private String ClientLink;
	private String IsActive;
	private String ProductCode;
	private String CheckQuantity;
	private String CheckShelfLevel;
	private String CheckPrice;
	private String CheckPacking;
	private String CheckExpiration;
	private String AddNote;
	private String TakePicture;
	private String Size;
	private String Order;
	private String Bold;
	private String prop_id_51;
	private String prop_id_52;
	private boolean isAlreadyQueried;

	public boolean isAlreadyQueried() {
		return isAlreadyQueried;
	}

	public void setAlreadyQueried(boolean alreadyQueried) {
		isAlreadyQueried = alreadyQueried;
	}

	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getClientLink() {
		return ClientLink;
	}
	public void setClientLink(String clientLink) {
		ClientLink = clientLink;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	public String getProductCode() {
		return ProductCode;
	}
	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}
	public String getCheckQuantity() {
		return CheckQuantity;
	}
	public void setCheckQuantity(String checkQuantity) {
		CheckQuantity = checkQuantity;
	}
	public String getCheckShelfLevel() {
		return CheckShelfLevel;
	}
	public void setCheckShelfLevel(String checkShelfLevel) {
		CheckShelfLevel = checkShelfLevel;
	}
	public String getCheckPrice() {
		return CheckPrice;
	}
	public void setCheckPrice(String checkPrice) {
		CheckPrice = checkPrice;
	}
	public String getCheckPacking() {
		return CheckPacking;
	}
	public void setCheckPacking(String checkPacking) {
		CheckPacking = checkPacking;
	}
	public String getCheckExpiration() {
		return CheckExpiration;
	}
	public void setCheckExpiration(String checkExpiration) {
		CheckExpiration = checkExpiration;
	}
	public String getAddNote() {
		return AddNote;
	}
	public void setAddNote(String addNote) {
		AddNote = addNote;
	}
	public String getTakePicture() {
		return TakePicture;
	}
	public void setTakePicture(String takePicture) {
		TakePicture = takePicture;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getOrder() {
		return Order;
	}
	public void setOrder(String order) {
		Order = order;
	}
	public String getBold() {
		return Bold;
	}
	public void setBold(String bold) {
		Bold = bold;
	}
	public String getprop_id_51() {
		return prop_id_51;
	}
	public void setprop_id_51(String prop_id_51) {
		this.prop_id_51 = prop_id_51;
	}
	public String getprop_id_52() {
		return prop_id_52;
	}
	public void setprop_id_52(String prop_id_52) {
		this.prop_id_52 = prop_id_52;
	}
	
	
}

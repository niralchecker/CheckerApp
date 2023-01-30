package com.checker.sa.android.data;

import java.io.Serializable;

public class PriceItem  implements Serializable{

	String ptoductID;
	String productPropertyID;
	String productLocationID;
	double price;
	public String getPtoductID() {
		return ptoductID;
	}
	public void setPtoductID(String ptoductID) {
		this.ptoductID = ptoductID;
	}
	public String getProductPropertyID() {
		return productPropertyID;
	}
	public void setProductPropertyID(String productPropertyID) {
		this.productPropertyID = productPropertyID;
	}
	public String getProductLocationID() {
		return productLocationID;
	}
	public void setProductLocationID(String productLocationID) {
		this.productLocationID = productLocationID;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}

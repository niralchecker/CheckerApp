package com.checker.sa.android.data;

import java.io.Serializable;

public class QuantityItem  implements Serializable{

	String ptoductID;
	String productPropertyID;
	String productLocationID;
	String productLocationName;
	int quantity;
	
	

	public String getProductLocationName() {
		return productLocationName;
	}

	public void setProductLocationName(String productLocationName) {
		this.productLocationName = productLocationName;
	}

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
//		if (quantity < 0)
//			quantity = 0;
		this.quantity = quantity;
	}

}

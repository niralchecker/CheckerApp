package com.checker.sa.android.data;

import java.io.Serializable;

public class Subchapters  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subchaptersID;
	private String subchapterName,subchapterOrder ;
	private String subchapterWeight;
	public String getSubchaptersID() {
		return subchaptersID;
	}
	public void setSubchaptersID(String subchaptersID) {
		this.subchaptersID = subchaptersID;
	}
	public String getSubchapterName() {
		return subchapterName;
	}
	public void setSubchapterName(String subchapterName) {
		this.subchapterName = subchapterName;
	}
	public String getSubchapterOrder() {
		return subchapterOrder;
	}
	public void setSubchapterOrder(String subchapterOrder) {
		this.subchapterOrder = subchapterOrder;
	}
	public String getSubchapterWeight() {
		return subchapterWeight;
	}
	public void setSubchapterWeight(String subchapterWeight) {
		this.subchapterWeight = subchapterWeight;
	}		
}

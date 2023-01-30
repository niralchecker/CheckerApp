package com.checker.sa.android.data;

import java.io.Serializable;

public class ProductPropertyVals  implements Serializable{
//	<ValueID>134</ValueID>
//    <Content>high</Content>
//    <PropLink>52</PropLink>
//    <Order>0</Order>
//    <IsActive>1</IsActive>
	
	private String ValueID;
	private String Content;
	private String PropLink;
	private String Order;
	private String IsActive;
	public String getValueID() {
		return ValueID;
	}
	public void setValueID(String valueID) {
		ValueID = valueID;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getPropLink() {
		return PropLink;
	}
	public void setPropLink(String propLink) {
		PropLink = propLink;
	}
	public String getOrder() {
		return Order;
	}
	public void setOrder(String order) {
		Order = order;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	
	
	
}

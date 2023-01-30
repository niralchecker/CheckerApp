package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemPositionInBlock  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String SetLink="";
	private String ItemId="";
	private int position;
	private int Absolute;
	private int type;
	private String datalink;
	private String RotationGroup;
	
	
	public String getItemId() {
		return ItemId;
	}
	public void setItemId(String itemId) {
		ItemId = itemId;
	}
	public String getSetLink() {
		return SetLink;
	}
	public void setSetLink(String setLink) {
		SetLink = setLink;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getAbsolute() {
		return Absolute;
	}
	public void setAbsolute(int absolute) {
		Absolute = absolute;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDatalink() {
		return datalink;
	}
	public void setDatalink(String datalink) {
		this.datalink = datalink;
	}
	public String getRotationGroup() {
		return RotationGroup;
	}
	public void setRotationGroup(String rotationGroup) {
		RotationGroup = rotationGroup;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

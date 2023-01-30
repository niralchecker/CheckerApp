package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Block implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String SetLink = "";
	private String StartIndex = "";
	private String Name = "";
	private int position;
	private int Absolute;
	private int type;
	private String datalink;
	private String AnswersSortingSource;
	private String RotationGroup;
	private String Key;
	private ArrayList<Objects> listObjectsInBlock;

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getSetLink() {
		return SetLink;
	}

	public void setSetLink(String setLink) {
		SetLink = setLink;
	}

	public String getStartIndex() {
		return StartIndex;
	}

	public void setStartIndex(String startIndex) {
		StartIndex = startIndex;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public String getAnswersSortingSource() {
		return AnswersSortingSource;
	}

	public void setAnswersSortingSource(String answersSortingSource) {
		AnswersSortingSource = answersSortingSource;
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

	public void setObject(Objects objects) {
		if (listObjectsInBlock == null)
			listObjectsInBlock = new ArrayList<Objects>();
		listObjectsInBlock.add(objects);
	}

	public ArrayList<Objects> getObjectsList() {
		return listObjectsInBlock;
	}

	public ArrayList<Objects> getStructuredObjects() {
		try {
			if (listObjectsInBlock != null && listObjectsInBlock.size() > 0)
				Collections.shuffle(listObjectsInBlock,
						new Random(System.currentTimeMillis()));
		} catch (Exception ex) {

		}
		return listObjectsInBlock;
	}
}

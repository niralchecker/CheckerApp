package com.checker.sa.android.data;

import java.io.Serializable;

public class LoopsEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String setId;
	int columnNumber;
	int rowNumber;
	String columnName;
	String columnData;
	String lastColumn;
	String listName;
	String listID;

	public LoopsEntry(String setId, int columnNumber, String columnName,
			String columnData, String listName, String listID) {
		this.setId = setId;
		this.columnNumber = columnNumber;
		this.columnName = columnName;
		this.columnData = columnData;
		this.listName = listName;
		this.listID = listID;
	}

	public LoopsEntry(String setId, int columnNumber, String columnName,
			String columnData, String lastColumn, String listName, String listID) {
		this.setId = setId;
		this.columnNumber = columnNumber;
		this.columnName = columnName;
		this.columnData = columnData;
		this.lastColumn = lastColumn;
		this.listName = listName;
		this.listID = listID;
	}

	public LoopsEntry() {

	}

	public String getListID() {
		return listID;
	}

	public void setListID(String listID) {
		this.listID = listID;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getListName() {

		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnData() {
		return columnData;
	}

	public void setColumnData(String columnData) {
		this.columnData = columnData;
	}

	public String getLastColumn() {
		return lastColumn;
	}

	public void setLastColumn(String lastColumn) {
		this.lastColumn = lastColumn;
	}

}

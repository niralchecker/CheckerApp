package com.mor.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.data.Objects;

public class CurrentLoopData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loopStartDataID;
	private String loopEndDataID;
	private String currentLoopName;
	private Objects currentLoopObject;
	private int currentLoopCount;
	private ArrayList<Objects> innerObjects;
	private String currentLoopSource;

	public String getCurrentLoopSource() {
		return currentLoopSource;
	}

	public void setCurrentLoopSource(String currentLoopSource) {
		this.currentLoopSource = currentLoopSource;
	}

	public ArrayList<Objects> getInnerObjects() {
		return innerObjects;
	}

	public void setInnerObjects(ArrayList<Objects> innerObjects) {
		this.innerObjects = innerObjects;
	}

	public void setInnerObjects(Objects innerObject) {
		if (innerObjects == null)
			innerObjects = new ArrayList<Objects>();

		this.innerObjects.add(innerObject);
	}

	public int getCurrentLoopCount() {
		return currentLoopCount;
	}

	public void setCurrentLoopCount(int currentLoopCount) {
		this.currentLoopCount = currentLoopCount;
	}

	public String getLoopStartDataID() {
		return loopStartDataID;
	}

	public void setLoopStartDataID(String loopStartDataID) {
		this.loopStartDataID = loopStartDataID;
	}

	public String getCurrentLoopName() {
		return currentLoopName;
	}

	public void setCurrentLoopName(String currentLoopName) {
		this.currentLoopName = currentLoopName;
	}

	public Objects getCurrentLoopObject() {
		return currentLoopObject;
	}

	public void setCurrentLoopObject(Objects currentLoopObject) {
		this.currentLoopObject = currentLoopObject;
	}

	public String getLoopEndDataID() {
		return loopEndDataID;
	}

	public void setLoopEndDataID(String loopEndDataID) {
		this.loopEndDataID = loopEndDataID;
	}

	public String get901Condition() {
		if (this.currentLoopObject != null
				&& this.currentLoopObject.getLoopCondition() != null
				&& this.currentLoopObject.getLoopCondition().contains("&amp;")) {
			String[] splits = this.currentLoopObject.getLoopCondition().split(
					"&amp;");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].contains("in_array")) {
					return splits[i];
				}
			}
		}
		return this.currentLoopObject.getLoopCondition();
	}

	public String get901ConditionsQuestionObjectCode() {
		if (this.currentLoopObject != null
				&& this.currentLoopObject.getLoopCondition() != null) {
			String[] splits = this.currentLoopObject.getLoopCondition().split(
					"&amp;");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].contains("in_array")) {
					String a901 = splits[i];
					splits = a901.split("in_array");
					for (int j = 0; j < splits.length; j++) {
						if (splits[j].contains("16")) {
							return splits[j].replace("$[16,", "").replace("]$",
									"");
						}
					}
				}
			}
		}
		return null;
	}

	public String getWithout901Condition() {
		String str = "";
		if (this.currentLoopObject != null
				&& this.currentLoopObject.getLoopCondition() != null) {
			String[] splits = this.currentLoopObject.getLoopCondition().split(
					"&amp;");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].contains("in_array")) {
					// return splits[i];
				} else {
					if (str.equals(""))
						str += splits[i];
					else
						str += "&amp;" + splits[i];
				}
			}
			return str;
		}
		return this.currentLoopObject.getLoopCondition();
	}

	public String get901ConditionsColumnName() {
		if (this.currentLoopObject != null
				&& this.currentLoopObject.getLoopCondition() != null) {
			String[] splits = this.currentLoopObject.getLoopCondition().split(
					"&amp;");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].contains("in_array")) {
					String a901 = splits[i];
					splits = a901.split("in_array");
					for (int j = 0; j < splits.length; j++) {
						if (splits[j].contains("901")) {
							splits = splits[j].replace("$[901,", "")
									.replace("]$", "").split(",");
							if (splits.length > 1) {
								return splits[1];
							}
						}
					}
				}
			}
		}
		return null;
	}

	public String get901ConditionsColumnData(String columnName, String dataid) {

		if (dataid == null)
			return "";
		if (columnName == null && dataid.contains("^")) {
			String indexof = dataid.substring(dataid.indexOf("^"));
			return indexof;
		} else if (columnName == null)
			return "";
		if (dataid.contains(columnName)) {
			String[] splits = dataid.split("\\^");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].contains(columnName)) {
					splits = splits[i].split("=");
					if (splits.length > 1)
						return splits[1];
				}
			}
		}
		return "";
	}

}

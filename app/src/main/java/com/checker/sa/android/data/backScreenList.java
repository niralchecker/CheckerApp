package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class backScreenList  implements Serializable
{
	private int pageCount;
	private int nextQindex;
	private String dataId;
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getNextQindex() {
		return nextQindex;
	}
	public void setNextQindex(int nextQindex) {
		this.nextQindex = nextQindex;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
}

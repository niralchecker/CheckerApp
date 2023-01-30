package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Chapters  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String chaptersID;
	private String chapterName,chapterOrder;
	private String chapterWeight;
	private ArrayList<Subchapters> listSubchapters;
	
	
	
	public String getChaptersID() {
		return chaptersID;
	}



	public void setChaptersID(String chaptersID) {
		this.chaptersID = chaptersID;
	}



	public String getChapterName() {
		return chapterName;
	}



	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}



	public String getChapterOrder() {
		return chapterOrder;
	}



	public void setChapterOrder(String chapterOrder) {
		this.chapterOrder = chapterOrder;
	}



	public String getChapterWeight() {
		return chapterWeight;
	}



	public void setChapterWeight(String chapterWeight) {
		this.chapterWeight = chapterWeight;
	}



	public ArrayList<Subchapters> getListSubchapters() {
		return listSubchapters;
	}



	public void setListSubchapters(ArrayList<Subchapters> listSubchapters) {
		this.listSubchapters = listSubchapters;
	}
}

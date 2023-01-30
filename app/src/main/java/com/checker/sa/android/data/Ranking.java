package com.checker.sa.android.data;

import java.io.Serializable;

import android.widget.CheckBox;

public class Ranking  implements Serializable{
	private String ansid;
	private int rank;
	private CheckBox cb;

	public String getAnsid() {
		return ansid;
	}

	public void setAnsid(String ansid) {
		this.ansid = ansid;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public CheckBox getCb() {
		return cb;
	}

	public void setCb(CheckBox cb) {
		this.cb = cb;
	}

}

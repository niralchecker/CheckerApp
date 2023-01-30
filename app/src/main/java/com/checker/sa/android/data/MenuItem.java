package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuItem implements Serializable {
	private String name;
	private String desc;
	private int resid;

	public MenuItem(String name, String desc, int resid) {
		this.name = name;
		this.desc = desc;
		this.resid = resid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getResid() {
		return resid;
	}

	public void setResid(int resid) {
		this.resid = resid;
	}

}

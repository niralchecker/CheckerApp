package com.checker.sa.android.adapter;

import java.util.Date;

public class Item {
	public String file;
	public int icon;
	public long lastModified;

	public Item(String file, Integer icon) {
		this.file = file;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return file;
	}
}
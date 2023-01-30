package com.checker.sa.android.data;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArchiveNameValue implements Serializable
{
	String name,value;
	static final long serialVersionUID=1L;
	public ArchiveNameValue(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}


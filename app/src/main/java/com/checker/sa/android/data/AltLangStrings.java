package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class AltLangStrings   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String AltLngID;
	public String text;

	public AltLangStrings(String altid, String txt) {
		AltLngID = altid;
		text = txt;
	}

}

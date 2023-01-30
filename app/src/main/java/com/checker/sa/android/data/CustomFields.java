package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomFields  implements Serializable{

	private String name;
	private String value;


	public String getId() {
		String id ="";
		if (name != null) {
			name = name.replace("_", " ");

			if (name.startsWith("Custom ")) {
				name = name.substring(7);
			}
			int lastIndexOF=name.lastIndexOf(" ");
			if (lastIndexOF>0 && lastIndexOF+1<name.length()) {
				id = name.substring(lastIndexOF+1);
				int id_number =0;
				try {
					id_number = Integer.parseInt(id);
				}
				catch (Exception ex)
				{
					id="";
				}
			}
		}

		return id;
	}

	public String getName() {
		String id ="";
		if (name != null) {
			name = name.replace("_", " ");

			if (name.startsWith("Custom ")) {
				name = name.substring(7);
			}
			int lastIndexOF=name.lastIndexOf(" ");
			if (lastIndexOF>0 && lastIndexOF+1<name.length()) {
				id = name.substring(lastIndexOF+1);
				int id_number =0;
				try {
					id_number = Integer.parseInt(id);
				}
				catch (Exception ex)
				{
					id="";
				}
			}
		}

		return name.replace(id,"");
		//return getId();

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

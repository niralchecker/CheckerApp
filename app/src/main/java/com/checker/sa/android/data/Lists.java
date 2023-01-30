package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Lists  implements Serializable{

	private static ArrayList<ListClass> lists = new ArrayList<ListClass>();

	public static ArrayList<ListClass> getAllLists() {
		return lists;
	}

	public static void setList(ListClass set) {
		lists.add(set);
	}

	public static void setSets(ArrayList<ListClass> set) {
		lists = set;
	}

}

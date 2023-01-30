package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class validationSets  implements Serializable{

	public validationSets(int i) {
	}

	public static ArrayList<Set> sets = new ArrayList<Set>();

	public static ArrayList<Set> getSets() {
		return sets;
	}

	public static boolean isSetAvailableRemove(String id) {
		if (sets == null)
			return false;
		for (int i = 0; i < sets.size(); i++) {
			Set cSet = sets.get(i);
			if (cSet != null && cSet.getSetID() != null
					&& cSet.getSetID().equals(id)) {

				sets.remove(cSet);
				return false;
			}
		}
		return false;
	}

	public static boolean isSetAvailable(String id) {
		if (sets == null)
			return false;
		for (int i = 0; i < sets.size(); i++) {
			Set cSet = sets.get(i);
			if (cSet != null && cSet.getSetID() != null
					&& cSet.getSetID().equals(id)) {
				if (cSet.getListObjects() != null)
					return true;
				else {
					sets.remove(cSet);
					return false;
				}
			}
		}
		return false;
	}

	public static Set getSetAvailable(String id) {
		if (sets == null)
			return null;
		for (int i = 0; i < sets.size(); i++) {
			Set cSet = sets.get(i);
			if (cSet != null && cSet.getSetID() != null
					&& cSet.getSetID().equals(id)) {
				if (cSet.getListObjects() != null)
					return cSet;
				else {
					sets.remove(cSet);
					return null;
				}
			}
		}
		return null;
	}

	public static void setSets(Set set) {
		if (!isSetAvailable(set.getSetID()))
			sets.add(set);
	}

	public static void setOrRemoveSets(Set set) {
		isSetAvailableRemove(set.getSetID());
		sets.add(set);
	}

	public static void setSets(ArrayList<Set> set) {
		sets = set;
	}

	public static boolean isSetViable(String id) {

		for (int i = 0; i < sets.size(); i++) {
			Set cSet = sets.get(i);
			if (cSet != null && cSet.getSetID() != null
					&& cSet.getSetID().equals(id)) {
				if (cSet.getListObjects() != null)
					return true;
			}
		}
		return false;
	}

}

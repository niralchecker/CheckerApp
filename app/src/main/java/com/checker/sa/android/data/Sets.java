package com.checker.sa.android.data;

import java.util.ArrayList;

import com.checker.sa.android.db.DBHelper;
import com.checker.sa.android.helper.Constants;

public class Sets {

	private static ArrayList<String> setIds = new ArrayList<String>();

	public static ArrayList<String> getSetIds() {
		return setIds;
	}

	public static int verifySetIds() {
		ArrayList<String> rSetIds = new ArrayList<String>();
		ArrayList<String> alreadyPresentSetIds = DBHelper.getAllSets(
				Constants.DB_TABLE_SETS, new String[] {
						Constants.DB_TABLE_SETS_SETID,
						Constants.DB_TABLE_SET_NAME });
		for (int i = 0; i < setIds.size(); i++) {
			String setid = setIds.get(i);
			if (isSetIdAvailableInThisList(alreadyPresentSetIds, setid)) {
//				setIds.remove(setid);
			} else {
				rSetIds.add(setid);
			}
		}
//		setIds = rSetIds;
		return rSetIds.size();
	}

	public static void setSetIds(String setid) {
		if (!isSetIdAvailable(setid))
			setIds.add(setid);
	}

	private static boolean isSetIdAvailable(String id) {
		if (setIds == null)
			return false;
		for (int i = 0; i < setIds.size(); i++) {
			String cSet = setIds.get(i);
			if (cSet != null && cSet.equals(id)) {
				return true;
			}
		}
		return false;

	}

	private static boolean isSetIdAvailableInThisList(ArrayList<String> setIds,
			String id) {
		if (setIds == null)
			return false;
		for (int i = 0; i < setIds.size(); i++) {
			String cSet = setIds.get(i);
			if (cSet != null && cSet.equals(id)) {
				return true;
			}
		}
		return false;

	}

	private static ArrayList<Set> sets = new ArrayList<Set>();

	public static ArrayList<Set> getSets() {
		return sets;
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

	public static void setSets(Set set) {
		if (!isSetAvailable(set.getSetID()))
			sets.add(set);
		setIds = removeThisSetFromList(set.getSetID(), setIds);
	}

	private static ArrayList<String> removeThisSetFromList(String id,
			ArrayList<String> setids) {

		ArrayList<String> newsetIds = new ArrayList<String>();
		if (setids == null)
			return null;
		for (int i = 0; i < setids.size(); i++) {
			String cSet = setids.get(i);
			if (cSet != null && cSet.equals(id)) {

			} else {
				newsetIds.add(cSet);
			}
		}
		return newsetIds;

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

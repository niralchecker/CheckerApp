package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.db.DBHelper;

public class Orders implements Serializable {
	private static ArrayList<Order> orders = new ArrayList<Order>();
	private static ArrayList<BranchProperties> branchProps = new ArrayList<BranchProperties>();
	private static ArrayList<String> listOfNewORderIds;

	public static ArrayList<Order> getOrders() {
		if (orders == null)
			orders = new ArrayList<Order>();
		return orders;
	}

	public static void setRejection(String rej) {

		for (int i = 0; orders != null && i < orders.size(); i++) {
			orders.get(i).setAllowShoppersToRejectJobs(rej);
		}
	}

	private static ArrayList<String> listOfOldORderIds;

	public static ArrayList<String> getListOfOldORderIds() {
		return listOfOldORderIds;
	}

	public static void setListOfOldORderIds() {
		listOfOldORderIds = new ArrayList<String>();
		listOfNewORderIds = new ArrayList<String>();
		for (int i = 0; i < Orders.orders.size(); i++) {
			if (Orders.orders.get(i) != null
					&& Orders.orders.get(i).getStatusName() != null)
				listOfOldORderIds.add(Orders.orders.get(i).getOrderID());
		}
	}

	public static ArrayList<String> checkDifferenceBWListOfORderIds() {
		if (listOfOldORderIds == null || listOfNewORderIds == null)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < listOfOldORderIds.size(); i++) {
			if (listOfOldORderIds.get(i) != null) {
				boolean isPresent = false;
				for (int j = 0; j < listOfNewORderIds.size(); j++) {
					if (listOfNewORderIds.get(j) != null
							&& listOfNewORderIds.get(j).equals(
									listOfOldORderIds.get(i))) {
						isPresent = true;
					}
				}
				if (isPresent == false) {
					list.add(listOfOldORderIds.get(i));
				}
			}
		}
		return list;
	}

	public static ArrayList<String> getListOfOrderIds() {
		return listOfNewORderIds;
	}

	public static void setOrders(Order order, boolean isNew) {
		if (isNew && listOfNewORderIds == null)
			listOfNewORderIds = new ArrayList<String>();
		if (isNew)
			listOfNewORderIds.add(order.getOrderID());
		orders.add(order);
	}

	public static ArrayList<BranchProperties> getBranchProps() {
		return Orders.branchProps;
	}


	public static void addBranchProp(BranchProperties branchProps) {
		if (Orders.branchProps == null)
			Orders.branchProps = new ArrayList<BranchProperties>();
		for (int i = 0; i < Orders.branchProps.size(); i++) {
			if (branchProps != null
					&& Orders.branchProps.get(i) != null
					&& Orders.branchProps.get(i).getID() != null
					&& Orders.branchProps.get(i).getID()
					.equals(branchProps.getID())) {
				// return;
			}
		}
		Orders.branchProps.add(branchProps);
	}
//
//	public static void addBranchProp(BranchProperties branchProps) {
//		if (Orders.branchProps == null)
//			Orders.branchProps = new ArrayList<BranchProperties>();
//		for (int i = 0; i < Orders.branchProps.size(); i++) {
//			if (branchProps != null
//					&& Orders.branchProps.get(i) != null
//					&& Orders.branchProps.get(i).getID() != null
//					&& Orders.branchProps.get(i).getID()
//							.equals(branchProps.getID())) {
//				Orders.branchProps.remove(i);
//				break;
//			}
//		}
//		Orders.branchProps.add(branchProps);
//	}

	public static void setBranchProps(ArrayList<BranchProperties> branchProps) {
		Orders.branchProps = branchProps;
	}

	public static void setListOrders(ArrayList<Order> order) {
		orders = order;
	}

	public static String getNextSurveyOrder(String orderId) {
		String id = orderId;
		if (!id.contains("_")) {
			return null;
		} else {

			try {
				id = id.substring(0, id.indexOf("_"));
				int thisAllocationId = Integer.valueOf(id);
				id = orderId.substring(orderId.indexOf("_") + 1);
				int thisOrderId = Integer.valueOf(id);
				ArrayList<Order> orders = getOrders();
				int increment = 1;
				Boolean isEnd = true;

				do {
					orderId = thisAllocationId + "_"
							+ (thisOrderId + increment);
					boolean isOnceFound = false;
					for (int i = 0; i < orders.size(); i++) {
						Order order = orders.get(i);

						if (order.getOrderID().equals(orderId)) {
							isOnceFound = true;
							if (order.getStatusName().equals("Assigned")
									|| order.getStatusName()
											.equals("Scheduled")
									|| order.getStatusName().equals("survey")) {
								return order.getOrderID();

							} else {
								increment++;
								isEnd = false;
								break;
							}

						}
					}
					if (isOnceFound == false)
						break;
				} while (!isEnd);
			} catch (Exception ex) {
				return id;
			}
		}

		return id;
	}

	public static void replaceistOrders(ArrayList<Order> ordrs) {
		if (ordrs == null || orders == null)
			return;
		for (int k = 0; k < ordrs.size(); k++) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);

				if (order.getOrderID().equals(ordrs.get(k).getOrderID())) {
					order.setTimeStart(ordrs.get(k).getTimeStart());

				}
			}
		}
	}

	public static boolean isBranchPropExists(String branchLink, String bprop) {

		//
		if (Orders.getBranchProps() != null
				&& Orders.getBranchProps().size() > 0) {
			for (int i = 0; i < Orders.getBranchProps().size(); i++) {
				if (Orders.getBranchProps().get(i).getID() != null
						&& Orders.getBranchProps().get(i).getID()
								.equals(branchLink)
						&& Orders.getBranchProps().get(i).getPropertyName() != null
						&& Orders.getBranchProps().get(i).getContent() != null
						&& bprop != null
						&& bprop.equals(Orders.getBranchProps().get(i)
								.getPropertyName()
								+ " - "
								+ Orders.getBranchProps().get(i).getContent())) {
					return true;
				}
			}
		}

		return false;
	}

	public static void clearProps() {
		if (branchProps!=null) branchProps.clear();
	}

    public static String getListOfOldORderIds(String currentSet) {
		String orderids="";
		for (int i = 0; orders != null && i < orders.size(); i++) {
			if (orders.get(i)!=null && orders.get(i).getSetLink()!=null
					&& orders.get(i).getSetLink().equals(currentSet) &&
					(orders.get(i).getStatusName().equals("Assigned")
					|| orders.get(i).getStatusName()
					.equals("Scheduled")))
			{
				orderids+=orders.get(i).getOrderID()+",";

			}
		}
		if (orderids.endsWith(","))
			return orderids.substring(0,orderids.length()-1);
		else return "";
    }
}

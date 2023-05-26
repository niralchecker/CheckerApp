package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class orderListItem  implements Serializable{


	private static final long serialVersionUID = 905991168536978677L;
	
	public orderListItem() {

	}

	public orderListItem(Order order, ArrayList<Order> listarray) {
		ArrayList<Order> tmpArray = null;
		if (listarray != null) {
			tmpArray = new ArrayList<Order>();
			for (int i = 0; i < listarray.size(); i++) {
				boolean ispresent = false;
				for (int j = 0; j < tmpArray.size(); j++) {
					if (listarray.get(i).getOrderID() != null
							&& listarray.get(i).getOrderID()
									.equals(tmpArray.get(j).getOrderID())) {
						ispresent = true;
					}
				}
				if (ispresent == false) {
					tmpArray.add(listarray.get(i));
				}
			}
		}
		orderItem = order;
		listOrders = listarray;
		surveyItem = null;
		listSurveys = null;
	}

	public orderListItem(Survey order, ArrayList<Survey> delete) {

		surveyItem = order;
		listSurveys = delete;
		orderItem = null;
		listOrders = null;
	}

	public Survey surveyItem;
	public ArrayList<Survey> listSurveys;

	public Order orderItem;
	public ArrayList<Order> listOrders;
}

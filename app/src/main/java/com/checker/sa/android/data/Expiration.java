package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.adapter.posPreviewAdapter;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Expiration  implements Serializable{
	ArrayList<ExpirationItem> listExpirationItems;

	public Expiration() {
		listExpirationItems = new ArrayList<ExpirationItem>();
	}

	public ListView getListViewReadyWithItems(ListView listView,
			Context context, POS_Shelf shelf_item, String pid, Boolean isPreview) {
		ArrayList<String> listValues = new ArrayList<String>();
		ArrayList<String> listHeadings = new ArrayList<String>();

		try {
			for (int i = 0; i < listExpirationItems.size(); i++) {
				if ((listExpirationItems.get(i).getPtoductID()).equals(pid)) {
					//listExpirationItems.get(i).getNote().trim();
					if (listExpirationItems.get(i).getNote() == null
							|| listExpirationItems.get(i).getNote().equals("")) {
						listExpirationItems.remove(i);
						i--;
					} else {

						listValues.add(listExpirationItems.get(i).getNote());
						listHeadings.add(shelf_item
								.getLocationName(listExpirationItems.get(i)
										.getProductLocationID()));

					}
				}
			}
			ArrayAdapter adapter = new posPreviewAdapter(context, listValues,
					listHeadings, EnumToggleButton.EXPIRATION, shelf_item,
					isPreview);

			listView.setAdapter(adapter);
		} catch (Exception ec) {

		}

		return listView;
	}

	public long getItems(String productId) {
		int count = 0;
		for (int i = 0; i < listExpirationItems.size(); i++) {
			if (listExpirationItems.get(i) != null
					&& listExpirationItems.get(i).getPtoductID() != null
					&& listExpirationItems.get(i).getPtoductID()
							.equals(productId)) {
				count++;
			}
		}
		return count;
	}

	public ExpirationItem getCurrentItem(String productId,
			String productLocation) {
		int index = isItemExist(productId, productLocation);
		if (index < 0)
			return null;
		else
			return listExpirationItems.get(index);
	}

	private int isItemExist(String productId, String productLocation) {
		for (int i = 0; i < listExpirationItems.size(); i++) {
			if (listExpirationItems.get(i) != null
					&& listExpirationItems.get(i).getPtoductID() != null
					&& listExpirationItems.get(i).getPtoductID()
							.equals(productId)
					&& listExpirationItems.get(i).getProductLocationID() != null
					&& listExpirationItems.get(i).getProductLocationID()
							.equals(productLocation)) {
				return i;
			}
		}
		return -1;
	}

	public String getPrice(String productId, String productLocation) {
		int index = -1;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			return listExpirationItems.get(index).note;
		}
		return "";
	}

	public void AddExpirationITem(String date, String productId,
			String productLocation, String ProductProperty) {
		int index = 0;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			ExpirationItem item = listExpirationItems.get(index);
			item.setNote(date);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);

		} else {
			ExpirationItem item = new ExpirationItem();
			item.setNote(date);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);
			listExpirationItems.add(item);
		}
	}

	public ArrayList<ExpirationItem> getListExpirationItems() {
		return listExpirationItems;
	}

	public void setListNoteItems(ArrayList<ExpirationItem> listExpirationItem) {
		this.listExpirationItems = listExpirationItem;
	}

	public String getItemText(String productID, String prodLocationID) {
		int index = 0;
		try {
			if ((index = isItemExist(productID, prodLocationID)) > -1) {
				if (listExpirationItems.get(index).note != null)
					return String.valueOf(listExpirationItems.get(index).note);
			}
		} catch (Exception ex) {

		}
		return "";
	}

	public void deleteItemAt(int pos) {
		listExpirationItems.remove(pos);
	}

}

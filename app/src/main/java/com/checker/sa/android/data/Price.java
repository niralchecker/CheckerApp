package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.adapter.posPreviewAdapter;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;
import com.mor.sa.android.activities.R;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Price  implements Serializable{
	ArrayList<PriceItem> listPriceItems;

	private Context mContext;
	private POS_Shelf mshelf_item;
	private String mPid;
	private ListView mListview;
	private TextView mtxtExtra;

	public Price() {
		listPriceItems = new ArrayList<PriceItem>();
	}

	public ListView getListViewReadyWithItems(ListView listView,
			Context context, POS_Shelf shelf_item, String pid, TextView txtExtra,Boolean isPreview) {

		mContext = context;
		mListview = listView;
		mPid = pid;
		mshelf_item = shelf_item;
		mtxtExtra = txtExtra;

		ArrayList<String> listValues = new ArrayList<String>();
		ArrayList<String> listHeadings = new ArrayList<String>();

		for (int i = 0; i < listPriceItems.size(); i++) {
			if ((listPriceItems.get(i).getPtoductID()).equals(pid)) {
				listValues.add(listPriceItems.get(i).getPrice() + "");
				listHeadings.add(shelf_item.getLocationName(listPriceItems.get(
						i).getProductLocationID()));
			}
		}
		ArrayAdapter adapter = new posPreviewAdapter(context, listValues,
				listHeadings, EnumToggleButton.PRICE, shelf_item,isPreview);

		listView.setAdapter(adapter);

		txtExtra.setText(context.getResources().getString(
				R.string.pos_average)
				+ " "+ 
				+ shelf_item.price_item.getAveragePrice(pid) + "");

		return listView;
	}

	public double getAveragePrice(String productId) {
		double count = getItems(productId);
		if (count == 0)
			count = 1;
		if (listPriceItems != null && listPriceItems.size() > 0) {
			return getTotalPrice(productId) / count;
		}

		return 0;
	}

	public long getItems(String productId) {
		int count = 0;
		for (int i = 0; i < listPriceItems.size(); i++) {
			if (listPriceItems.get(i) != null
					&& listPriceItems.get(i).getPtoductID() != null
					&& listPriceItems.get(i).getPtoductID().equals(productId)) {
				count++;
			}
		}
		return count;
	}

	public double getTotalPrice(String productId) {
		double total = 0;
		for (int i = 0; i < listPriceItems.size(); i++) {
			if (listPriceItems.get(i) != null
					&& listPriceItems.get(i).getPtoductID() != null
					&& listPriceItems.get(i).getPtoductID().equals(productId)) {
				total += listPriceItems.get(i).price;
			}
		}
		return total;
	}

	private int isItemExist(String productId, String productLocation) {
		for (int i = 0; i < listPriceItems.size(); i++) {
			if (listPriceItems.get(i) != null
					&& listPriceItems.get(i).getPtoductID() != null
					&& listPriceItems.get(i).getPtoductID().equals(productId)
					&& listPriceItems.get(i).getProductLocationID() != null
					&& listPriceItems.get(i).getProductLocationID()
							.equals(productLocation)) {
				return i;
			}
		}
		return -1;
	}

	public double getPrice(String productId, String productLocation) {
		int index = -1;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			return listPriceItems.get(index).price;
		}
		return -1;
	}

	public void AddPriceITem(double price, String productId,
			String productLocation, String ProductProperty) {
		int index = 0;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			PriceItem item = listPriceItems.get(index);
			item.setPrice(price);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);

		} else {
			PriceItem item = new PriceItem();
			item.setPrice(price);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);
			listPriceItems.add(item);
		}
	}

	public ArrayList<PriceItem> getListPriceItems() {
		return listPriceItems;
	}

	public void setListPriceItems(ArrayList<PriceItem> listPriceItems) {
		this.listPriceItems = listPriceItems;
	}

	public String getItemText(String productID, String prodLocationID) {
		int index = 0;
		try {
			if ((index = isItemExist(productID, prodLocationID)) > -1) {
				return String.valueOf(listPriceItems.get(index).price);
			}
		} catch (Exception ex) {

		}
		return "";
	}

	public void deleteItemAt(int pos) {
		listPriceItems.remove(pos);

		try {
			mtxtExtra.setText(mContext.getResources().getString(
					R.string.pos_average)
					+ " "+  mshelf_item.price_item.getAveragePrice(mPid) + "");
		} catch (Exception ex) {

		}
	}

}

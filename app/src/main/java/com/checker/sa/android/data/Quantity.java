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

public class Quantity  implements Serializable{
	ArrayList<QuantityItem> listQuantityItems;

	private Context mContext;
	private POS_Shelf mshelf_item;
	private String mPid;
	private ListView mListview;
	private TextView mtxtExtra;

	public Quantity(Context mContext) {
		listQuantityItems = new ArrayList<QuantityItem>();
	}

	public Quantity() {
		listQuantityItems = new ArrayList<QuantityItem>();
	}

	public ListView getListViewReadyWithItems(ListView listView,
			Context context, POS_Shelf shelf_item, String pid,
			TextView txtExtra, Boolean isPreview) {
		mContext = context;
		mListview = listView;
		mPid = pid;
		mshelf_item = shelf_item;
		mtxtExtra = txtExtra;

		ArrayList<String> listValues = new ArrayList<String>();
		ArrayList<String> listHeadings = new ArrayList<String>();

		for (int i = 0; i < listQuantityItems.size(); i++) {
			String product_id = listQuantityItems.get(i).getPtoductID();

			if (product_id.equals(pid)) {

				if (listQuantityItems.get(i).getQuantity() == 0) {
					listQuantityItems.remove(i);
					i--;
				} else {
					listValues.add(listQuantityItems.get(i).getQuantity() + "");
					listHeadings.add(shelf_item
							.getLocationName(listQuantityItems.get(i)
									.getProductLocationID()));
				}
			}
		}
		ArrayAdapter adapter = new posPreviewAdapter(context, listValues,
				listHeadings, EnumToggleButton.COUNT, shelf_item, isPreview);

		listView.setAdapter(adapter);

		mtxtExtra.setText(context.getResources().getString(R.string.pos_total)
				+ " " + +mshelf_item.quantity_item.getTotal(mPid) + "");

		return listView;
	}

	public long getTotalCount(String productId) {
		long total = 0;
		for (int i = 0; i < listQuantityItems.size(); i++) {
			if (listQuantityItems.get(i) != null
					&& listQuantityItems.get(i).getPtoductID() != null
					&& listQuantityItems.get(i).getPtoductID()
							.equals(productId)) {
				total += listQuantityItems.get(i).quantity;
			}
		}
		return total;
	}

	public long getItems(String productId) {
		int count = 0;
		for (int i = 0; i < listQuantityItems.size(); i++) {
			if (listQuantityItems.get(i) != null
					&& listQuantityItems.get(i).getPtoductID() != null
					&& listQuantityItems.get(i).getPtoductID()
							.equals(productId)) {
				count++;
			}
		}
		return count;
	}

	private int isItemExist(String productId, String productLocation) {
		for (int i = 0; i < listQuantityItems.size(); i++) {
			if (listQuantityItems.get(i) != null
					&& listQuantityItems.get(i).getPtoductID() != null
					&& listQuantityItems.get(i).getPtoductID()
							.equals(productId)
					&& listQuantityItems.get(i).getProductLocationID() != null
					&& listQuantityItems.get(i).getProductLocationID()
							.equals(productLocation)) {
				return i;
			}
		}
		return -1;
	}

	public long getTotal(String productId) {
		long total = 0;
		for (int i = 0; i < listQuantityItems.size(); i++) {
			if (listQuantityItems.get(i) != null
					&& listQuantityItems.get(i).getPtoductID() != null
					&& listQuantityItems.get(i).getPtoductID()
							.equals(productId)) {
				total += listQuantityItems.get(i).quantity;
			}
		}
		return total;
	}

	public int getQuantity(String productId, String productLocation) {
		int index = -1;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			return listQuantityItems.get(index).quantity;
		}
		return -1;
	}

	public void AddQuantityITem(int quantity, String productId,
			String productLocation, String ProductProperty) {
		int index = 0;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			QuantityItem item = listQuantityItems.get(index);
			item.setQuantity(item.getQuantity() + quantity);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);
			// item.setProductLocationName(location);

		} else {
			QuantityItem item = new QuantityItem();
			item.setQuantity(quantity);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);
			// item.setProductLocationName(location);
			listQuantityItems.add(item);
		}
	}

	public int SubQuantityITem(int quantity, String productId,
			String productLocation, String ProductProperty) {
		int index = 0;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			QuantityItem item = listQuantityItems.get(index);
			if (item.getQuantity() >= quantity)
				item.setQuantity(item.getQuantity() - quantity);
			else {
				return item.getQuantity();
			}
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);

		}
		return -1;
		// else {
		// QuantityItem item = new QuantityItem();
		// item.setQuantity(quantity);
		// item.setPtoductID(productId);
		// item.setProductLocationID(productLocation);
		// item.setProductPropertyID(ProductProperty);
		// listQuantityItems.add(item);
		// }
	}

	public ArrayList<QuantityItem> getListQuantityItems() {
		return listQuantityItems;
	}

	public void setListQuantityItems(ArrayList<QuantityItem> listQuantityItems) {
		this.listQuantityItems = listQuantityItems;
	}

	public String getItemText(String productID, String prodLocationID) {
		int index = 0;
		try {
			if ((index = isItemExist(productID, prodLocationID)) > -1) {
				return String.valueOf(listQuantityItems.get(index).quantity);
			}
		} catch (Exception ex) {

		}
		return "";
	}

	public void deleteItemAt(int pos) {
		listQuantityItems.remove(pos);
		try {
			mtxtExtra.setText("Total= "
					+ mshelf_item.quantity_item.getTotal(mPid) + "");
		} catch (Exception ex) {

		}

	}

}

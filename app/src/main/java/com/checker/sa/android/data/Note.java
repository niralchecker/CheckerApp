package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.checker.sa.android.adapter.posPreviewAdapter;
import com.checker.sa.android.helper.POS_Toggles.EnumToggleButton;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Note  implements Serializable{
	ArrayList<NoteItem> listNoteItems;

	private Context mContext;
	private POS_Shelf mshelf_item;
	private String mPid;
	private ListView mListview;
	private TextView mtxtExtra;

	public Note() {
		listNoteItems = new ArrayList<NoteItem>();
	}

	public ListView getListViewReadyWithItems(ListView listView,
			Context context, POS_Shelf shelf_item, String pid,
			TextView txtExtra, Boolean isPreview) {

		mContext = context;
		mListview = listView;
		mPid = pid;
		mshelf_item = shelf_item;
		mtxtExtra = txtExtra;

		try {
			ArrayList<String> listValues = new ArrayList<String>();
			ArrayList<String> listHeadings = new ArrayList<String>();

			for (int i = 0; i < listNoteItems.size(); i++) {
				if ((listNoteItems.get(i).getPtoductID()).equals(pid)) {
					//listNoteItems.get(i).getNote().trim();
					if (listNoteItems.get(i).getNote() == null
							|| listNoteItems.get(i).getNote().equals("")) {
						listNoteItems.remove(i);
						i--;
					} else {
						listValues.add(listNoteItems.get(i).getNote());
						listHeadings.add(shelf_item
								.getLocationName(listNoteItems.get(i)
										.getProductLocationID()));

					}
				}
			}
			ArrayAdapter adapter = new posPreviewAdapter(context, listValues,
					listHeadings, EnumToggleButton.NOTE, shelf_item, isPreview);

			listView.setAdapter(adapter);
		} catch (Exception ex) {
		}

		return listView;
	}

	public long getItems(String productId) {
		int count = 0;
		for (int i = 0; i < listNoteItems.size(); i++) {
			if (listNoteItems.get(i) != null
					&& listNoteItems.get(i).getPtoductID() != null
					&& listNoteItems.get(i).getPtoductID().equals(productId)) {
				count++;
			}
		}
		return count;
	}

	private int isItemExist(String productId, String productLocation) {
		for (int i = 0; i < listNoteItems.size(); i++) {
			if (listNoteItems.get(i) != null
					&& listNoteItems.get(i).getPtoductID() != null
					&& listNoteItems.get(i).getPtoductID().equals(productId)
					&& listNoteItems.get(i).getProductLocationID() != null
					&& listNoteItems.get(i).getProductLocationID()
							.equals(productLocation)) {
				return i;
			}
		}
		return -1;
	}

	public String getPrice(String productId, String productLocation) {
		int index = -1;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			return listNoteItems.get(index).note;
		}
		return "";
	}

	public void AddNoteITem(String note, String productId,
			String productLocation, String ProductProperty) {
		int index = 0;
		if ((index = isItemExist(productId, productLocation)) > -1) {
			NoteItem item = listNoteItems.get(index);
			item.setNote(note);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);

		} else {
			NoteItem item = new NoteItem();
			item.setNote(note);
			item.setPtoductID(productId);
			item.setProductLocationID(productLocation);
			item.setProductPropertyID(ProductProperty);
			listNoteItems.add(item);
		}
	}

	public ArrayList<NoteItem> getListNoteItems() {
		return listNoteItems;
	}

	public void setListNoteItems(ArrayList<NoteItem> listNoteItems) {
		this.listNoteItems = listNoteItems;
	}

	public String getItemText(String productID, String prodLocationID) {
		int index = 0;
		try {
			if ((index = isItemExist(productID, prodLocationID)) > -1) {
				if (listNoteItems.get(index).note != null)
					return String.valueOf(listNoteItems.get(index).note);
			}
		} catch (Exception ex) {

		}
		return "";
	}

	public void deleteItemAt(int pos) {
		listNoteItems.remove(pos);
	}

}

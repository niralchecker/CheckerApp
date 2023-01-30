package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Picture  implements Serializable{
	ArrayList<PictureItem> listPictureItems;

	public Picture() {
		listPictureItems = new ArrayList<PictureItem>();
	}

	private ArrayList<String> isItemExist(String productId, String productLocation) {
		for (int i = 0; i < listPictureItems.size(); i++) {
			if (listPictureItems.get(i) != null
					&& listPictureItems.get(i).getPtoductID() != null
					&& listPictureItems.get(i).getPtoductID().equals(productId)
					&& listPictureItems.get(i).getProductLocationID() != null
					&& listPictureItems.get(i).getProductLocationID()
							.equals(productLocation)) {
				return null;
			}
		}
		return null;
	}

	public void AddPictureITem(double price, String productId,
			String productLocation, String ProductProperty) {
		ArrayList<String> index = null;
//		if ((index = isItemExist(productId, productLocation)) !=null) {
//			PictureItem item = listPictureItems.get(index);
//			item.setPrice(price);
//			item.setPtoductID(productId);
//			item.setProductLocationID(productLocation);
//			item.setProductPropertyID(ProductProperty);
//
//		} else {
//			PriceItem item = new PriceItem();
//			item.setPrice(price);
//			item.setPtoductID(productId);
//			item.setProductLocationID(productLocation);
//			item.setProductPropertyID(ProductProperty);
//			listPriceItems.add(item);
//		}
	}

	public ArrayList<PictureItem> getListPictureItems() {
		return listPictureItems;
	}

	public void setListPriceItems(ArrayList<PictureItem> listPictureItems) {
		this.listPictureItems = listPictureItems;
	}

}

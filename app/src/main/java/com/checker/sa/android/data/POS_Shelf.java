package com.checker.sa.android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;

public class POS_Shelf  implements Serializable{
	public Price price_item;
	public Quantity quantity_item;
	public Note note_item;
	public Picture picture_item;
	public Expiration expiration_item;
	public ArrayList<Products> listProducts;
	public ArrayList<ProductLocations> listProductLocations;
	public ArrayList<ProductProperties> listProductProperties;
	public String order_id;
	private Context mContext;
	
	public POS_Shelf(Context c) {
		// TODO Auto-generated constructor stub
		mContext=c;
	}

	public String getLocationName(String locationId) {
		for (int i = 0; i < listProductLocations.size(); i++) {
			if (listProductLocations.get(i).getProdLocationID()
					.equals(locationId)) {
				return listProductLocations.get(i).getProductLocation();
			}
		}
		return locationId;
	}

	public String getProductName(String productId) {
		for (int i = 0; i < listProducts.size(); i++) {
			if (listProducts.get(i).getProductID().equals(productId)) {
				return listProducts.get(i).getProductName();
			}
		}
		return productId;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public POS_Shelf(String order_id,Context c) {
		price_item = new Price();
		quantity_item = new Quantity(mContext);
		note_item = new Note();
		expiration_item = new Expiration();
		listProducts = new ArrayList<Products>();
		listProductLocations = new ArrayList<ProductLocations>();
		listProductProperties = new ArrayList<ProductProperties>();
		this.order_id = order_id;
		mContext=c;
	}

	public void setNote_item(Note note_item) {
		this.note_item = note_item;
	}

	public Note getNote_item() {
		return note_item;
	}

	public Price getPrice_item() {
		return price_item;
	}

	public void setQuantity_item(Quantity quantity_item) {
		this.quantity_item = quantity_item;
	}

	public Quantity getQuantity_item() {
		return quantity_item;
	}

	public void setPrice_item(Price price_item) {
		this.price_item = price_item;
	}

	public ArrayList<Products> getListProducts() {
		return listProducts;
	}

	public void setListProducts(ArrayList<Products> listProducts) {
		this.listProducts = listProducts;
	}

	public ArrayList<ProductLocations> getListProductLocations() {
		return listProductLocations;
	}

	public void setListProductLocations(
			ArrayList<ProductLocations> listProductLocations) {
		this.listProductLocations = listProductLocations;
	}

	public ArrayList<ProductProperties> getListProductProperties() {
		return listProductProperties;
	}

	public void setListProductProperties(
			ArrayList<ProductProperties> listProductProperties) {
		this.listProductProperties = listProductProperties;
	}

	public List<NameValuePair> PrepareProductValuePair(
			List<NameValuePair> extraDataList) {
		if (listProducts == null || listProductLocations == null)
			return extraDataList;
		Products current_product;
		ProductLocations current_location;
		double current_price = -1;
		int current_count = -1;
		String current_note = "";
		String current_date = "";
		int nameValueCount = 0;
		for (int i = 0; i < listProducts.size(); i++) {
			for (int j = 0; j < listProductLocations.size(); j++) {
				current_product = listProducts.get(i);
				current_location = listProductLocations.get(j);
				current_price = -1;
				current_price = price_item.getPrice(
						current_product.getProductID(),
						current_location.getProdLocationID());

				current_count = -1;
				current_count = quantity_item.getQuantity(
						current_product.getProductID(),
						current_location.getProdLocationID());

				current_note = "";
				current_note = note_item.getItemText(
						current_product.getProductID(),
						current_location.getProdLocationID());

				current_date = "";
				current_date = expiration_item.getItemText(
						current_product.getProductID(),
						current_location.getProdLocationID());

				if (current_price > -1 || current_count > -1
						|| !current_note.equals("") || !current_date.equals("")) {
					nameValueCount++;
					extraDataList.add(Helper.getNameValuePair(
							Constants.POST_FIELD_PROD + nameValueCount
									+ Constants.POST_FIELD_PROD_ID,
							String.valueOf(current_product.getProductID())));
					extraDataList.add(Helper.getNameValuePair(
							Constants.POST_FIELD_PROD + nameValueCount
									+ Constants.POST_FIELD_PROD_NAME,
							String.valueOf(current_product.getProductName())));
					extraDataList.add(Helper.getNameValuePair(
							Constants.POST_FIELD_PROD + nameValueCount
									+ Constants.POST_FIELD_PROD_CODE,
							String.valueOf(current_product.getProductCode())));
					extraDataList.add(Helper.getNameValuePair(
							Constants.POST_FIELD_PROD + nameValueCount
									+ Constants.POST_FIELD_PROD_LOCID, String
									.valueOf(current_location
											.getProdLocationID())));
					extraDataList.add(Helper.getNameValuePair(
							Constants.POST_FIELD_PROD + nameValueCount
									+ Constants.POST_FIELD_PROD_LOC, String
									.valueOf(current_location
											.getProductLocation())));
					if (current_price > -1) {

						extraDataList.add(Helper.getNameValuePair(
								Constants.POST_FIELD_PROD + nameValueCount
										+ Constants.POST_FIELD_PROD_PRICE,
								String.valueOf(current_price)));
					}

					if (current_count > -1) {

						extraDataList.add(Helper.getNameValuePair(
								Constants.POST_FIELD_PROD + nameValueCount
										+ Constants.POST_FIELD_PROD_QUANTITY,
								String.valueOf(current_count)));
					}

					if (current_note != null && !current_note.equals("")) {

						extraDataList.add(Helper.getNameValuePair(
								Constants.POST_FIELD_PROD + nameValueCount
										+ Constants.POST_FIELD_PROD_NOTE,
								String.valueOf(current_note)));
					}

					if (current_date != null && !current_date.equals("")) {

						extraDataList.add(Helper.getNameValuePair(
								Constants.POST_FIELD_PROD + nameValueCount
										+ Constants.POST_FIELD_PROD_EXPIRATION,
								String.valueOf(current_date)));
					}
				}

			}

		}
		return extraDataList;
	}

	public void setItemFromdb(String location_ids, String order_ids,
			String prices, String product_ids, String property_ids,
			String quantitys, String setids, String notes, String dates) {
		if (listProducts == null || listProductLocations == null)
			return;
		try {
			
			price_item.AddPriceITem(Double.valueOf(prices), product_ids,
					location_ids, property_ids);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			
			quantity_item.AddQuantityITem(Integer.valueOf(quantitys),
					product_ids, location_ids, property_ids);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			note_item.AddNoteITem(notes, product_ids, location_ids,
					property_ids);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			expiration_item.AddExpirationITem(dates, product_ids, location_ids,
					property_ids);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}

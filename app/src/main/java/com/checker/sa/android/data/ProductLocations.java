package com.checker.sa.android.data;

import java.io.Serializable;

public class ProductLocations  implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	<ProductLocations ID="27" type="array">
//
//
//
//    <ProdLocationID>27</ProdLocationID>
//
//
//
//    <ProductLocation>back wall display</ProductLocation>
//
//
//
//    <ClientLink>1013</ClientLink>
//
//
//
//  </ProductLocations>
	
	private String ProdLocationID;
	private String ProductLocation;
	private String ClientLink;
	public String getProdLocationID() {
		return ProdLocationID;
	}
	public void setProdLocationID(String prodLocationID) {
		ProdLocationID = prodLocationID;
	}
	public String getProductLocation() {
		return ProductLocation;
	}
	public void setProductLocation(String productLocation) {
		ProductLocation = productLocation;
	}
	public String getClientLink() {
		return ClientLink;
	}
	public void setClientLink(String clientLink) {
		ClientLink = clientLink;
	}
	
	
}

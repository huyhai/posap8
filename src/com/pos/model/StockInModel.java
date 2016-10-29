package com.pos.model;

public class StockInModel {

	public static final String ITEM_TABLE_NAME = "items";

	public static final String ID = "ItemID";
	public static final String GROUPID = "Item_group_ID";
	public static final String CODE = "Item_code";
	public static final String IMAGE = "Item_image";
	public static final String BARCODE = "Barcode";

	public static final String UOM = "uom";
	public static final String COSTPRICE = "Cost_price";
	public static final String PRICE1 = "Selling_price_1";
	public static final String PRICE2 = "Selling_price_2";
	public static final String SPECALPRICE = "Special_price";
	public static final String STATUS = "Status";
	public static final String REMARKS = "Remarks";
	public static final String NAME = "Name";

	public final static String[] VATTU_FULL_PROJECTION = { StockInModel.ID,
			StockInModel.GROUPID, StockInModel.CODE, StockInModel.IMAGE,
			StockInModel.BARCODE, StockInModel.UOM, StockInModel.COSTPRICE,
			StockInModel.PRICE1, StockInModel.PRICE2, StockInModel.SPECALPRICE,
			StockInModel.STATUS, StockInModel.REMARKS, StockInModel.NAME };

	private String ItemID;
	private String Item_group_ID;
	private String Item_code;
	private String Item_image;
	private String Barcode;
	private String uom;
	private String Cost_price;
	private String Selling_price_1;
	private String Selling_price_2;
	private String Special_price;
	private String Status;
	private String Remarks;
	private String Name;

	public StockInModel() {

	}

	public String getItemID() {
		return ItemID;
	}

	public void setItemID(String itemID) {
		ItemID = itemID;
	}

	public String getItem_group_ID() {
		return Item_group_ID;
	}

	public void setItem_group_ID(String item_group_ID) {
		Item_group_ID = item_group_ID;
	}

	public String getItem_code() {
		return Item_code;
	}

	public void setItem_code(String item_code) {
		Item_code = item_code;
	}

	public String getItem_image() {
		return Item_image;
	}

	public void setItem_image(String item_image) {
		Item_image = item_image;
	}

	public String getBarcode() {
		return Barcode;
	}

	public void setBarcode(String barcode) {
		Barcode = barcode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getCost_price() {
		return Cost_price;
	}

	public void setCost_price(String cost_price) {
		Cost_price = cost_price;
	}

	public String getSelling_price_1() {
		return Selling_price_1;
	}

	public void setSelling_price_1(String selling_price_1) {
		Selling_price_1 = selling_price_1;
	}

	public String getSelling_price_2() {
		return Selling_price_2;
	}

	public void setSelling_price_2(String selling_price_2) {
		Selling_price_2 = selling_price_2;
	}

	public String getSpecial_price() {
		return Special_price;
	}

	public void setSpecial_price(String special_price) {
		Special_price = special_price;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}


}

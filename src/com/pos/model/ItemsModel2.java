package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class ItemsModel2 {

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

	public final static String[] VATTU_FULL_PROJECTION = { ItemsModel2.ID,
			ItemsModel2.GROUPID, ItemsModel2.CODE, ItemsModel2.IMAGE,
			ItemsModel2.BARCODE, ItemsModel2.UOM, ItemsModel2.COSTPRICE,
			ItemsModel2.PRICE1, ItemsModel2.PRICE2, ItemsModel2.SPECALPRICE,
			ItemsModel2.STATUS, ItemsModel2.REMARKS, ItemsModel2.NAME };

	private String ItemID;
	private String Item_group_ID;
	private String Item_code;
	private String Item_image;
	private String Barcode;
	private String uom;
	private String Cost_price;
	private String Selling_price_1;
	private String Selling_price_2;
	private String Selling_price_3;
	private String Selling_price_4;
	private String Selling_price_5;
	private String Special_price;
	private String Status;
	private String Remarks;
	private String Name;
	private String textSize;
	private String isGST;
	private String isService;
	private String groupname;
	private String groupCode;
	private String nameTau;
	private String language_code;
	private String sale_detailID;
	private String bill;
	private String takeaway;

	public ItemsModel2() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {

		this.setItem_code(Utilities.getDataString(jSonInfo, "id"));
		this.setName(Utilities.getDataString(jSonInfo, "name"));
		this.setItem_image(Utilities.getDataString(jSonInfo, "image"));
		this.setSelling_price_1(Utilities.getDataString(jSonInfo, "price"));
		this.setSelling_price_2(Utilities.getDataString(jSonInfo, "price2"));
		this.setSelling_price_3(Utilities.getDataString(jSonInfo, "price3"));
		this.setSelling_price_4(Utilities.getDataString(jSonInfo, "price4"));
		this.setSelling_price_5(Utilities.getDataString(jSonInfo, "price5"));
		this.setBarcode(Utilities.getDataString(jSonInfo, "barcode"));

		this.setSpecial_price(Utilities.getDataString(jSonInfo, "specialprice"));
		this.setIsGST(Utilities.getDataString(jSonInfo, "tax"));
		this.setIsService(Utilities.getDataString(jSonInfo, "service"));
		this.setGroupname(Utilities.getDataString(jSonInfo, "groupname"));
		this.setGroupCode(Utilities.getDataString(jSonInfo, "item_group_id"));
		this.setSale_detailID(Utilities.getDataString(jSonInfo, "sale_detailID"));
		this.setBill(Utilities.getDataString(jSonInfo, "bill"));
		this.setNameTau(Utilities.getDataString(jSonInfo, "name_cn"));
		setItemID(Utilities.getDataString(jSonInfo, "item_id"));
	}
	public void setDataTau(JSONObject jSonInfo) throws JSONException {

		this.setItem_code(Utilities.getDataString(jSonInfo, "id"));
		this.setItem_image(Utilities.getDataString(jSonInfo, "image"));
		this.setSelling_price_1(Utilities.getDataString(jSonInfo, "price"));
		this.setSelling_price_2(Utilities.getDataString(jSonInfo, "price2"));
		this.setSelling_price_3(Utilities.getDataString(jSonInfo, "price3"));
		this.setSelling_price_4(Utilities.getDataString(jSonInfo, "price4"));
		this.setSelling_price_5(Utilities.getDataString(jSonInfo, "price5"));
		this.setBarcode(Utilities.getDataString(jSonInfo, "barcode"));

		this.setSpecial_price(Utilities.getDataString(jSonInfo, "specialprice"));
		this.setIsGST(Utilities.getDataString(jSonInfo, "tax"));
		this.setIsService(Utilities.getDataString(jSonInfo, "service"));
		this.setGroupname(Utilities.getDataString(jSonInfo, "groupname"));
		this.setGroupCode(Utilities.getDataString(jSonInfo, "item_group_id"));
		this.setLanguage_code(Utilities.getDataString(jSonInfo, "language_id"));
		this.setSale_detailID(Utilities.getDataString(jSonInfo, "sale_detailID"));
		this.setBill(Utilities.getDataString(jSonInfo, "bill"));
		setItemID(Utilities.getDataString(jSonInfo, "item_id"));

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

	public String getTextSize() {
		return textSize;
	}

	public void setTextSize(String textSize) {
		this.textSize = textSize;
	}

	public String getSelling_price_3() {
		return Selling_price_3;
	}

	public void setSelling_price_3(String selling_price_3) {
		Selling_price_3 = selling_price_3;
	}

	public String getSelling_price_4() {
		return Selling_price_4;
	}

	public void setSelling_price_4(String selling_price_4) {
		Selling_price_4 = selling_price_4;
	}

	public String getIsGST() {
		return isGST;
	}

	public void setIsGST(String isGST) {
		this.isGST = isGST;
	}

	public String getIsService() {
		return isService;
	}

	public void setIsService(String isService) {
		this.isService = isService;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * @return the selling_price_5
	 */
	public String getSelling_price_5() {
		return Selling_price_5;
	}

	/**
	 * @param selling_price_5 the selling_price_5 to set
	 */
	public void setSelling_price_5(String selling_price_5) {
		Selling_price_5 = selling_price_5;
	}

	/**
	 * @return the nameTau
	 */
	public String getNameTau() {
		return nameTau;
	}

	/**
	 * @param nameTau the nameTau to set
	 */
	public void setNameTau(String nameTau) {
		this.nameTau = nameTau;
	}

	/**
	 * @return the language_code
	 */
	public String getLanguage_code() {
		return language_code;
	}

	/**
	 * @param language_code the language_code to set
	 */
	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}

	/**
	 * @return the sale_detailID
	 */
	public String getSale_detailID() {
		return sale_detailID;
	}

	/**
	 * @param sale_detailID the sale_detailID to set
	 */
	public void setSale_detailID(String sale_detailID) {
		this.sale_detailID = sale_detailID;
	}

	/**
	 * @return the bill
	 */
	public String getBill() {
		return bill;
	}

	/**
	 * @param bill the bill to set
	 */
	public void setBill(String bill) {
		this.bill = bill;
	}

	/**
	 * @return the takeaway
	 */
	public String getTakeaway() {
		return takeaway;
	}

	/**
	 * @param takeaway the takeaway to set
	 */
	public void setTakeaway(String takeaway) {
		this.takeaway = takeaway;
	}

}

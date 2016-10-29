package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class ListOrderModel {
	private String itemCode;
	private String itemName;
	private String unitPrice;
	private String qualyti;
	private String discount;
	private String amount;
	private String price2;
	private String specialPrice;

	private String price3;
	private String price4;
	private String price5;
	private String isGST;
	private String isSVC;
	private String groupName;
	private String isFOC;
	private String id;
	private String groupCode;
	private String standBy;
	private String groupID;
	private String remarks;
	private String itemTau;
	private String setmenu;
	private String sale_detailID;
	private String bill;
	private String takeaway;
	private String isStyle;

	// private String DetailItemName;
	// private String DetailQuantity;
	// private String DetailUnitPrice;
	// private String DetailDiscount;
	// private String DetailAmount;
	// private String DetailItemCode;
	// public static final String DETAILHOLD_TABLE_NAME = "HoldSales";
	public static final String ItemName = "ItemName";
	public static final String Quantity = "Quantity";
	public static final String UnitPrice = "UnitPrice";
	public static final String Discount = "Discount";
	public static final String Amount = "Amount";
	public static final String ItemCode = "ItemCode";

	public static final String Price2 = "Price2";
	public static final String SpecialPrice = "SpecialPrice";
	public final static String[] DETAIL_HOLD_FULL_PROJECTION = {
			ListOrderModel.ItemName, ListOrderModel.Quantity,
			ListOrderModel.UnitPrice, ListOrderModel.Discount,
			ListOrderModel.Amount, ListOrderModel.ItemCode,
			ListOrderModel.Price2, ListOrderModel.SpecialPrice };

	public ListOrderModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setItemName(Utilities.getDataString(jSonInfo, "item"));
		this.setDiscount(Utilities.getDataString(jSonInfo, "discount_amount"));
		this.setId(Utilities.getDataString(jSonInfo, "id"));
		this.setAmount(Utilities.getDataString(jSonInfo, "subtotal"));
		this.setUnitPrice(Utilities.getDataString(jSonInfo, "selling_price"));
		this.setQualyti(Utilities.getDataString(jSonInfo, "quantity"));
		this.setIsGST(Utilities.getDataString(jSonInfo, "tax"));
		this.setIsSVC(Utilities.getDataString(jSonInfo, "service_charge"));
		this.setGroupName(Utilities.getDataString(jSonInfo, "group"));
		this.setIsFOC(Utilities.getDataString(jSonInfo, "foc"));
		this.setItemCode(Utilities.getDataString(jSonInfo, "item_code"));
//		this.setItemCode(Utilities.getDataString(jSonInfo, "item_code"));
		this.setStandBy(Utilities.getDataString(jSonInfo, "standby"));
		this.setGroupCode(Utilities.getDataString(jSonInfo, "item_group_id"));
		
		this.setPrice2(Utilities.getDataString(jSonInfo, "selling_price_2"));
		this.setPrice3(Utilities.getDataString(jSonInfo, "selling_price_3"));
		this.setPrice4(Utilities.getDataString(jSonInfo, "selling_price_4"));
		this.setPrice5(Utilities.getDataString(jSonInfo, "selling_price_5"));
		this.setRemarks(Utilities.getDataString(jSonInfo, "remark"));
		this.setItemTau(Utilities.getDataString(jSonInfo, "itemCN"));
		this.setSetmenu(Utilities.getDataString(jSonInfo, "set_menu"));
		this.setSale_detailID(Utilities.getDataString(jSonInfo, "sale_detailID"));
		this.setBill(Utilities.getDataString(jSonInfo, "bill"));

	}
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getQualyti() {
		return qualyti;
	}

	public void setQualyti(String qualyti) {
		this.qualyti = qualyti;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice2() {
		return price2;
	}

	public void setPrice2(String price2) {
		this.price2 = price2;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	// public String getDetailItemName() {
	// return DetailItemName;
	// }
	//
	// public void setDetailItemName(String detailItemName) {
	// DetailItemName = detailItemName;
	// }
	//
	// public String getDetailQuantity() {
	// return DetailQuantity;
	// }
	//
	// public void setDetailQuantity(String detailQuantity) {
	// DetailQuantity = detailQuantity;
	// }
	//
	// public String getDetailUnitPrice() {
	// return DetailUnitPrice;
	// }
	//
	// public void setDetailUnitPrice(String detailUnitPrice) {
	// DetailUnitPrice = detailUnitPrice;
	// }
	//
	// public String getDetailDiscount() {
	// return DetailDiscount;
	// }
	//
	// public void setDetailDiscount(String detailDiscount) {
	// DetailDiscount = detailDiscount;
	// }
	//
	// public String getDetailAmount() {
	// return DetailAmount;
	// }
	//
	// public void setDetailAmount(String detailAmount) {
	// DetailAmount = detailAmount;
	// }
	//
	// public String getDetailItemCode() {
	// return DetailItemCode;
	// }
	//
	// public void setDetailItemCode(String detailItemCode) {
	// DetailItemCode = detailItemCode;
	// }
	// public String getAmountOriginal() {
	// return amountOriginal;
	// }
	//
	// public void setAmountOriginal(String amountOriginal) {
	// this.amountOriginal = amountOriginal;
	// }

	public String getPrice3() {
		return price3;
	}

	public void setPrice3(String price3) {
		this.price3 = price3;
	}

	public String getPrice4() {
		return price4;
	}

	public void setPrice4(String price4) {
		this.price4 = price4;
	}

	public String getIsGST() {
		return isGST;
	}

	public void setIsGST(String isGST) {
		this.isGST = isGST;
	}

	public String getIsSVC() {
		return isSVC;
	}

	public void setIsSVC(String isSVC) {
		this.isSVC = isSVC;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getIsFOC() {
		return isFOC;
	}

	public void setIsFOC(String isFOC) {
		this.isFOC = isFOC;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	 * @return the standBy
	 */
	public String getStandBy() {
		return standBy;
	}

	/**
	 * @param standBy the standBy to set
	 */
	public void setStandBy(String standBy) {
		this.standBy = standBy;
	}

	/**
	 * @return the price5
	 */
	public String getPrice5() {
		return price5;
	}

	/**
	 * @param price5 the price5 to set
	 */
	public void setPrice5(String price5) {
		this.price5 = price5;
	}

	/**
	 * @return the groupID
	 */
	public String getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the setMenu
	 */

	/**
	 * @return the itemTau
	 */
	public String getItemTau() {
		return itemTau;
	}

	/**
	 * @param itemTau the itemTau to set
	 */
	public void setItemTau(String itemTau) {
		this.itemTau = itemTau;
	}

	/**
	 * @return the setmenu
	 */
	public String getSetmenu() {
		return setmenu;
	}

	/**
	 * @param setmenu the setmenu to set
	 */
	public void setSetmenu(String setmenu) {
		this.setmenu = setmenu;
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

	/**
	 * @return the isStyle
	 */
	public String getIsStyle() {
		return isStyle;
	}

	/**
	 * @param isStyle the isStyle to set
	 */
	public void setIsStyle(String isStyle) {
		this.isStyle = isStyle;
	}

}

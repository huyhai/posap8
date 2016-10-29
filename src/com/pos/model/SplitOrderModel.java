package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.MainActivity;
import com.pos.Table;
import com.pos.common.Utilities;

public class SplitOrderModel {
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
	private String bill;
	private String itemCN;
	private String saleDetailID;
	private String setMenu;
	private String takeaway;
//	private String isPrintBill;

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
			SplitOrderModel.ItemName, SplitOrderModel.Quantity,
			SplitOrderModel.UnitPrice, SplitOrderModel.Discount,
			SplitOrderModel.Amount, SplitOrderModel.ItemCode,
			SplitOrderModel.Price2, SplitOrderModel.SpecialPrice };

	public SplitOrderModel() {

	}

	int c = 1;

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
		// this.setItemCode(Utilities.getDataString(jSonInfo, "item_code"));
		this.setStandBy(Utilities.getDataString(jSonInfo, "standby"));
		this.setGroupCode(Utilities.getDataString(jSonInfo, "item_group_id"));

		this.setPrice2(Utilities.getDataString(jSonInfo, "selling_price_2"));
		this.setPrice3(Utilities.getDataString(jSonInfo, "selling_price_3"));
		this.setPrice4(Utilities.getDataString(jSonInfo, "selling_price_4"));
		this.setPrice5(Utilities.getDataString(jSonInfo, "selling_price_5"));
		this.setRemarks(Utilities.getDataString(jSonInfo, "remark"));
		this.setItemCN(Utilities.getDataString(jSonInfo, "itemCN"));
		this.setSaleDetailID(Utilities.getDataString(jSonInfo, "sale_detailID"));
		this.setSetMenu(Utilities.getDataString(jSonInfo, "set_menu"));
		this.setBill(Utilities.getDataString(jSonInfo, "bill"));
		this.setTakeaway(Utilities.getDataString(jSonInfo, "TakeAway"));
//		this.setIsPrintBill(Utilities.getDataString(jSonInfo, "IsPrintBill"));
//		if (Integer.parseInt(getBill()) > Utilities.getGlobalVariable(Table.ac).countMutil) {
//			Utilities.getGlobalVariable(Table.ac).countMutil=Integer.parseInt(getBill());
//			Utilities.getGlobalVariable(Table.ac).isMutilBill = true;
//		} 
		// setBill("1");

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
	 * @param groupCode
	 *            the groupCode to set
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
	 * @param standBy
	 *            the standBy to set
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
	 * @param price5
	 *            the price5 to set
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
	 * @param groupID
	 *            the groupID to set
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
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the bill
	 */
	public String getBill() {
		return bill;
	}

	/**
	 * @param bill
	 *            the bill to set
	 */
	public void setBill(String bill) {
		this.bill = bill;
	}

	/**
	 * @return the itemCN
	 */
	public String getItemCN() {
		return itemCN;
	}

	/**
	 * @param itemCN
	 *            the itemCN to set
	 */
	public void setItemCN(String itemCN) {
		this.itemCN = itemCN;
	}

	/**
	 * @return the saleDetailID
	 */
	public String getSaleDetailID() {
		return saleDetailID;
	}

	/**
	 * @param saleDetailID
	 *            the saleDetailID to set
	 */
	public void setSaleDetailID(String saleDetailID) {
		this.saleDetailID = saleDetailID;
	}

	/**
	 * @return the setMenu
	 */
	public String getSetMenu() {
		return setMenu;
	}

	/**
	 * @param setMenu
	 *            the setMenu to set
	 */
	public void setSetMenu(String setMenu) {
		this.setMenu = setMenu;
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
//	 * @return the isPrintBill
//	 */
//	public String getIsPrintBill() {
//		return isPrintBill;
//	}
//
//	/**
//	 * @param isPrintBill the isPrintBill to set
//	 */
//	public void setIsPrintBill(String isPrintBill) {
//		this.isPrintBill = isPrintBill;
//	}

}

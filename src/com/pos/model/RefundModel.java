package com.pos.model;

public class RefundModel {
	private String itemCode;
	private String itemName;
	private String unitPrice;
	private String qty;
	private String discount;
	private String amount;
	private String specialPrice;
	private String price2;
	public static final String ITEMCODE = "ItemCode";
	public static final String ITEMNAME = "ItemName";
	public static final String UNITPRICE = "UnitPrice";
	public static final String QTY = "Quantity";
	public static final String DISCOUNT = "Discount";
	public static final String AMOUNT = "Amount";
	public static final String SPECIALPRICE = "SpecialPrice";
	public static final String PRICE2 = "Price2";

	public final static String[] VATTU_FULL_PROJECTION = { RefundModel.ITEMCODE,
		RefundModel.ITEMNAME, RefundModel.UNITPRICE, RefundModel.QTY,
		RefundModel.DISCOUNT, RefundModel.AMOUNT, RefundModel.SPECIALPRICE,
		RefundModel.PRICE2 };

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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
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

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	public String getPrice2() {
		return price2;
	}

	public void setPrice2(String price2) {
		this.price2 = price2;
	}

}

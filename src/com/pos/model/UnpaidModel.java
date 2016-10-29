package com.pos.model;

public class UnpaidModel {
	private String Receipt_no;
	private String Total_amount;
	private String Status;
	
	private String SaleID;
	private String ItemName;
	private String Quantity;
	private String UnitPrice;
	private String Discount;
	private String Amount;
	private String ItemCode;
	private String Price2;
	private String SpecialPrice;
	private String note;

	public UnpaidModel() {

	}

	public String getTotal_amount() {
		return Total_amount;
	}

	public void setTotal_amount(String total_amount) {
		Total_amount = total_amount;
	}


	public String getSaleID() {
		return SaleID;
	}

	public void setSaleID(String saleID) {
		SaleID = saleID;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getUnitPrice() {
		return UnitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		UnitPrice = unitPrice;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getItemCode() {
		return ItemCode;
	}

	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}

	public String getReceipt_no() {
		return Receipt_no;
	}

	public void setReceipt_no(String receipt_no) {
		Receipt_no = receipt_no;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPrice2() {
		return Price2;
	}

	public void setPrice2(String price2) {
		Price2 = price2;
	}

	public String getSpecialPrice() {
		return SpecialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		SpecialPrice = specialPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}

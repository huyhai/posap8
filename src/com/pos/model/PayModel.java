package com.pos.model;

public class PayModel {
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
	private String percentDis;
	private String dollarDis;
	private String paymentMode;
	private String type1;
	private String type2;
	private String type1Amount;
	private String type2Amount;
	private String change;
	private String printReciept;
	private String subTotal;
	private String GST;
	private String creaditType;
	private String idUser;
	private String TableNumber;
	private String uom;
	private String refund;
	private String gross_beforeDis;
	private String gross_affterDis;
	private String tax_amount;
	private String svc_amount;
	private String group_name;

	public PayModel() {

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

	public String getPercentDis() {
		return percentDis;
	}

	public void setPercentDis(String percentDis) {
		this.percentDis = percentDis;
	}

	public String getDollarDis() {
		return dollarDis;
	}

	public void setDollarDis(String dollarDis) {
		this.dollarDis = dollarDis;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}


	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getPrintReciept() {
		return printReciept;
	}

	public void setPrintReciept(String printReciept) {
		this.printReciept = printReciept;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getGST() {
		return GST;
	}

	public void setGST(String gST) {
		GST = gST;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType1Amount() {
		return type1Amount;
	}

	public void setType1Amount(String type1Amount) {
		this.type1Amount = type1Amount;
	}

	public String getType2Amount() {
		return type2Amount;
	}

	public void setType2Amount(String type2Amount) {
		this.type2Amount = type2Amount;
	}

	public String getCreaditType() {
		return creaditType;
	}

	public void setCreaditType(String creaditType) {
		this.creaditType = creaditType;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getTableNumber() {
		return TableNumber;
	}

	public void setTableNumber(String tableNumber) {
		TableNumber = tableNumber;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getGross_beforeDis() {
		return gross_beforeDis;
	}

	public void setGross_beforeDis(String gross_beforeDis) {
		this.gross_beforeDis = gross_beforeDis;
	}

	public String getGross_affterDis() {
		return gross_affterDis;
	}

	public void setGross_affterDis(String gross_affterDis) {
		this.gross_affterDis = gross_affterDis;
	}

	public String getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(String tax_amount) {
		this.tax_amount = tax_amount;
	}

	public String getSvc_amount() {
		return svc_amount;
	}

	public void setSvc_amount(String svc_amount) {
		this.svc_amount = svc_amount;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

}
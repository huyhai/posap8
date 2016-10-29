package com.pos.model;

public class SearchBillModel {
	private String Receipt_no;
	private String Sales_date;
	private String Total_amount;
	private String Status;
	private String SaleID;
	private String DiscountValues;
	// public static final String HOLD_TABLE_NAME = "HoldSales";
	public static final String RECEPNO = "Receipt_no";
	public static final String SALEDATE = "Sales_date";
	public static final String TOTAL = "Total_amount";
	public static final String ID = "SaleID";
	public static final String STATUS = "Status";
	public static final String DiscountValue = "DiscountValue";

	

	public final static String[] HOLDBILL_FULL_PROJECTION = {
			SearchBillModel.ID, SearchBillModel.RECEPNO,
			SearchBillModel.SALEDATE, SearchBillModel.TOTAL,
			SearchBillModel.STATUS,SearchBillModel.DiscountValue };


	public SearchBillModel() {

	}

	public String getReceipt_no() {
		return Receipt_no;
	}

	public void setReceipt_no(String receipt_no) {
		Receipt_no = receipt_no;
	}

	public String getSales_date() {
		return Sales_date;
	}

	public void setSales_date(String sales_date) {
		Sales_date = sales_date;
	}

	public String getTotal_amount() {
		return Total_amount;
	}

	public void setTotal_amount(String total_amount) {
		Total_amount = total_amount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSaleID() {
		return SaleID;
	}

	public void setSaleID(String saleID) {
		SaleID = saleID;
	}

	public String getDiscountValues() {
		return DiscountValues;
	}

	public void setDiscountValues(String discountValues) {
		DiscountValues = discountValues;
	}

	

}

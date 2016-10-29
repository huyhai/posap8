package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class SalesModel {
	private String subtotal;
	private String svc;
	private String gst;
	private String dis;
	private String total;
	private String saleID;
	private String remarks;
	private String number_of_customers;
	private String isPrintBill;

	public SalesModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setSubtotal(Utilities.getDataString(jSonInfo, "Sub_total"));
		this.setSvc(Utilities.getDataString(jSonInfo, "service_charge_amount"));
		this.setGst(Utilities.getDataString(jSonInfo, "tax_amount"));
		this.setDis(Utilities.getDataString(jSonInfo, "discount_amount"));
		this.setTotal(Utilities.getDataString(jSonInfo, "net_amount"));
		this.setSaleID(Utilities.getDataString(jSonInfo, "id"));
		this.setRemarks(Utilities.getDataString(jSonInfo, "remarks"));
		this.setNumber_of_customers(Utilities.getDataString(jSonInfo, "number_of_customers"));
		this.setIsPrintBill(Utilities.getDataString(jSonInfo, "IsPrintBill"));

	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getSvc() {
		return svc;
	}

	public void setSvc(String svc) {
		this.svc = svc;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getDis() {
		return dis;
	}

	public void setDis(String dis) {
		this.dis = dis;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSaleID() {
		return saleID;
	}

	public void setSaleID(String saleID) {
		this.saleID = saleID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the number_of_customers
	 */
	public String getNumber_of_customers() {
		return number_of_customers;
	}

	/**
	 * @param number_of_customers the number_of_customers to set
	 */
	public void setNumber_of_customers(String number_of_customers) {
		this.number_of_customers = number_of_customers;
	}

	/**
	 * @return the isPrintBill
	 */
	public String getIsPrintBill() {
		return isPrintBill;
	}

	/**
	 * @param isPrintBill the isPrintBill to set
	 */
	public void setIsPrintBill(String isPrintBill) {
		this.isPrintBill = isPrintBill;
	}

}

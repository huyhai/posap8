package com.pos.model;

public class ExportInventoryModel {
	private String date;
	private String group;
	private String itemcode;
	private String itemname;
	private String stockinhand;
	private String totalin;
	private String totalout;
	private String balance;
	private String costprice;
	private String sellingprice1;
	private String username;
	private String uom;

	public ExportInventoryModel() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getStockinhand() {
		return stockinhand;
	}

	public void setStockinhand(String stockinhand) {
		this.stockinhand = stockinhand;
	}

	public String getTotalin() {
		return totalin;
	}

	public void setTotalin(String totalin) {
		this.totalin = totalin;
	}

	public String getTotalout() {
		return totalout;
	}

	public void setTotalout(String totalout) {
		this.totalout = totalout;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCostprice() {
		return costprice;
	}

	public void setCostprice(String costprice) {
		this.costprice = costprice;
	}

	public String getSellingprice1() {
		return sellingprice1;
	}

	public void setSellingprice1(String sellingprice1) {
		this.sellingprice1 = sellingprice1;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

}

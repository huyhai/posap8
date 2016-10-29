package com.pos.model;

public class BillModel {
	private String Receipt_no;
	private String Counter;
	private String Closesession;
	private String Totalamount;
	private String Cash;
	private String CreditCard;
	private String GST;
	private String User;
	// public static final String HOLD_TABLE_NAME = "HoldSales";

	public BillModel() {

	}

	public String getReceipt_no() {
		return Receipt_no;
	}

	public void setReceipt_no(String receipt_no) {
		Receipt_no = receipt_no;
	}

	public String getCounter() {
		return Counter;
	}

	public void setCounter(String _counter) {
		Counter = _counter;
	}

	public String getClosesession() {
		return Closesession;
	}

	public void setClosesession(String _session) {
		Closesession = _session;
	}

	public String getTotalamount() {
		return Totalamount;
	}

	public void setTotalamount(String _amount) {
		Totalamount = _amount;
	}

	public String getCash() {
		return Cash;
	}

	public void setCash(String _Cash) {
		Cash = _Cash;
	}

	public String getCard() {
		return CreditCard;
	}

	public void setCard(String _Card) {
		CreditCard = _Card;
	}
	public String getGST() {
		return GST;
	}

	public void setGST(String _GST) {
		GST = _GST;
	}
	public String getUser() {
		return User;
	}

	public void setUser(String _User) {
		User = _User;
	}

	

}

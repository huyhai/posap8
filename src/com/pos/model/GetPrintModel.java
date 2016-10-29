package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class GetPrintModel {
	private String print1;
	private String print2;
	private String numPrint;

	// public static final String HOLD_TABLE_NAME = "HoldSales";

	public GetPrintModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setPrint1(Utilities.getDataString(jSonInfo, "print1"));
		this.setPrint2(Utilities.getDataString(jSonInfo, "print2"));
		this.setNumPrint(Utilities.getDataString(jSonInfo, "num_print"));



	}
	/**
	 * @return the print1
	 */
	public String getPrint1() {
		return print1;
	}

	/**
	 * @param print1 the print1 to set
	 */
	public void setPrint1(String print1) {
		this.print1 = print1;
	}

	/**
	 * @return the print2
	 */
	public String getPrint2() {
		return print2;
	}

	/**
	 * @param print2 the print2 to set
	 */
	public void setPrint2(String print2) {
		this.print2 = print2;
	}

	/**
	 * @return the numPrint
	 */
	public String getNumPrint() {
		return numPrint;
	}

	/**
	 * @param numPrint the numPrint to set
	 */
	public void setNumPrint(String numPrint) {
		this.numPrint = numPrint;
	}

}

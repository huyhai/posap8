package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class MayinModel {
	private String item_group;
	private String printer_one;
	private String printer_two;
	private String printer_three;
	private String printer_four;
	private String printer_five;

	public MayinModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setItem_group(Utilities.getDataString(jSonInfo, "item_group"));
		this.setPrinter_one(Utilities.getDataString(jSonInfo, "printer_one"));
		this.setPrinter_two(Utilities.getDataString(jSonInfo, "printer_two"));

		this.setPrinter_three(Utilities
				.getDataString(jSonInfo, "printer_three"));
		this.setPrinter_four(Utilities.getDataString(jSonInfo, "printer_four"));
		this.setPrinter_five(Utilities.getDataString(jSonInfo, "printer_five"));
	}

	/**
	 * @return the item_group
	 */
	public String getItem_group() {
		return item_group;
	}

	/**
	 * @param item_group
	 *            the item_group to set
	 */
	public void setItem_group(String item_group) {
		this.item_group = item_group;
	}

	/**
	 * @return the printer_one
	 */
	public String getPrinter_one() {
		return printer_one;
	}

	/**
	 * @param printer_one
	 *            the printer_one to set
	 */
	public void setPrinter_one(String printer_one) {
		this.printer_one = printer_one;
	}

	/**
	 * @return the printer_two
	 */
	public String getPrinter_two() {
		return printer_two;
	}

	/**
	 * @param printer_two
	 *            the printer_two to set
	 */
	public void setPrinter_two(String printer_two) {
		this.printer_two = printer_two;
	}

	/**
	 * @return the printer_three
	 */
	public String getPrinter_three() {
		return printer_three;
	}

	/**
	 * @param printer_three
	 *            the printer_three to set
	 */
	public void setPrinter_three(String printer_three) {
		this.printer_three = printer_three;
	}

	/**
	 * @return the printer_four
	 */
	public String getPrinter_four() {
		return printer_four;
	}

	/**
	 * @param printer_four
	 *            the printer_four to set
	 */
	public void setPrinter_four(String printer_four) {
		this.printer_four = printer_four;
	}

	/**
	 * @return the printer_five
	 */
	public String getPrinter_five() {
		return printer_five;
	}

	/**
	 * @param printer_five
	 *            the printer_five to set
	 */
	public void setPrinter_five(String printer_five) {
		this.printer_five = printer_five;
	}

}

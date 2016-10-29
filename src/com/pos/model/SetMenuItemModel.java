package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class SetMenuItemModel {
	private String item_code;
	private String item_group_id;
	private String name;
	private String selling_price_1;
	private String selling_price_2;
	private String selling_price_3;
	private String selling_price_4;
	private String selling_price_5;
	private String special_price;
	private String tax;
	private String service_charge;
	private String remarks;
	private String nameTau;

	public SetMenuItemModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setItem_code(Utilities.getDataString(jSonInfo, "item_code"));
		this.setItem_group_id(Utilities
				.getDataString(jSonInfo, "item_group_id"));
		this.setName(Utilities.getDataString(jSonInfo, "name"));

		this.setSelling_price_1(Utilities.getDataString(jSonInfo,
				"selling_price_1"));
		this.setSelling_price_2(Utilities.getDataString(jSonInfo,
				"selling_price_2"));
		this.setSelling_price_3(Utilities.getDataString(jSonInfo,
				"selling_price_3"));
		this.setSelling_price_4(Utilities.getDataString(jSonInfo,
				"selling_price_4"));
		this.setSelling_price_5(Utilities.getDataString(jSonInfo,
				"selling_price_5"));
		this.setSpecial_price(Utilities
				.getDataString(jSonInfo, "special_price"));
		this.setTax(Utilities.getDataString(jSonInfo, "tax"));
		this.setService_charge(Utilities.getDataString(jSonInfo,
				"service_charge"));
		this.setRemarks(Utilities.getDataString(jSonInfo, "remarks"));

	}
	public void setDataTau(JSONObject jSonInfo) throws JSONException {
		this.setItem_code(Utilities.getDataString(jSonInfo, "item_code"));
		this.setItem_group_id(Utilities
				.getDataString(jSonInfo, "item_group_id"));

		this.setSelling_price_1(Utilities.getDataString(jSonInfo,
				"selling_price_1"));
		this.setSelling_price_2(Utilities.getDataString(jSonInfo,
				"selling_price_2"));
		this.setSelling_price_3(Utilities.getDataString(jSonInfo,
				"selling_price_3"));
		this.setSelling_price_4(Utilities.getDataString(jSonInfo,
				"selling_price_4"));
		this.setSelling_price_5(Utilities.getDataString(jSonInfo,
				"selling_price_5"));
		this.setSpecial_price(Utilities
				.getDataString(jSonInfo, "special_price"));
		this.setTax(Utilities.getDataString(jSonInfo, "tax"));
		this.setService_charge(Utilities.getDataString(jSonInfo,
				"service_charge"));
		this.setRemarks(Utilities.getDataString(jSonInfo, "remarks"));


	}
	/**
	 * @return the item_code
	 */
	public String getItem_code() {
		return item_code;
	}

	/**
	 * @param item_code
	 *            the item_code to set
	 */
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	/**
	 * @return the item_group_id
	 */
	public String getItem_group_id() {
		return item_group_id;
	}

	/**
	 * @param item_group_id
	 *            the item_group_id to set
	 */
	public void setItem_group_id(String item_group_id) {
		this.item_group_id = item_group_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the selling_price_1
	 */
	public String getSelling_price_1() {
		return selling_price_1;
	}

	/**
	 * @param selling_price_1
	 *            the selling_price_1 to set
	 */
	public void setSelling_price_1(String selling_price_1) {
		this.selling_price_1 = selling_price_1;
	}

	/**
	 * @return the selling_price_2
	 */
	public String getSelling_price_2() {
		return selling_price_2;
	}

	/**
	 * @param selling_price_2
	 *            the selling_price_2 to set
	 */
	public void setSelling_price_2(String selling_price_2) {
		this.selling_price_2 = selling_price_2;
	}

	/**
	 * @return the selling_price_3
	 */
	public String getSelling_price_3() {
		return selling_price_3;
	}

	/**
	 * @param selling_price_3
	 *            the selling_price_3 to set
	 */
	public void setSelling_price_3(String selling_price_3) {
		this.selling_price_3 = selling_price_3;
	}

	/**
	 * @return the selling_price_4
	 */
	public String getSelling_price_4() {
		return selling_price_4;
	}

	/**
	 * @param selling_price_4
	 *            the selling_price_4 to set
	 */
	public void setSelling_price_4(String selling_price_4) {
		this.selling_price_4 = selling_price_4;
	}

	/**
	 * @return the selling_price_5
	 */
	public String getSelling_price_5() {
		return selling_price_5;
	}

	/**
	 * @param selling_price_5
	 *            the selling_price_5 to set
	 */
	public void setSelling_price_5(String selling_price_5) {
		this.selling_price_5 = selling_price_5;
	}

	/**
	 * @return the special_price
	 */
	public String getSpecial_price() {
		return special_price;
	}

	/**
	 * @param special_price
	 *            the special_price to set
	 */
	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}

	/**
	 * @return the tax
	 */
	public String getTax() {
		return tax;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(String tax) {
		this.tax = tax;
	}

	/**
	 * @return the service_charge
	 */
	public String getService_charge() {
		return service_charge;
	}

	/**
	 * @param service_charge
	 *            the service_charge to set
	 */
	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
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
	 * @return the nameTau
	 */
	public String getNameTau() {
		return nameTau;
	}

	/**
	 * @param nameTau the nameTau to set
	 */
	public void setNameTau(String nameTau) {
		this.nameTau = nameTau;
	}

}

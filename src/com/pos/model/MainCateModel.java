package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class MainCateModel {

	public static final String MAINCATE_TABLE_NAME = "item_groups";

	public static final String FIELD_MAIN_ID = "GroupID";
	public static final String FIELD_NAME = "Name";

	public static final String FIELD_IMAGE = "Image";
	public static final String FIELD_Active = "Active";
	public static final String FIELD_GroupCode = "Group_code";

	public final static String[] VATTU_FULL_PROJECTION = {
			MainCateModel.FIELD_MAIN_ID, MainCateModel.FIELD_IMAGE,
			MainCateModel.FIELD_NAME, MainCateModel.FIELD_Active,
			MainCateModel.FIELD_GroupCode };

	// private variables
	private String _ItemGr_ID;
	private String _Name;
	private String _Image;
	private String active;
	private String _groupcode;
	private String textSize;
	private String nameTau;

	public void setData(JSONObject jSonInfo) throws JSONException {

		this.set_ItemGr_ID(Utilities.getDataString(jSonInfo, "id"));
		this.set_Name(Utilities.getDataString(jSonInfo, "name"));
		this.set_Image(Utilities.getDataString(jSonInfo, "image"));
		

	}
	public void setDataTau(JSONObject jSonInfo) throws JSONException {

		this.set_ItemGr_ID(Utilities.getDataString(jSonInfo, "id"));
//		this.set_Name(Utilities.getDataString(jSonInfo, "name"));
		this.set_Image(Utilities.getDataString(jSonInfo, "image"));
		

	}


	// Empty constructor
	public MainCateModel() {

	}

	public String get_ItemGr_ID() {
		return _ItemGr_ID;
	}

	public void set_ItemGr_ID(String _ItemGr_ID) {
		this._ItemGr_ID = _ItemGr_ID;
	}

	public String get_group_code() {
		return _groupcode;
	}

	public void set_groupcode(String _groupcode) {
		this._groupcode = _groupcode;
	}

	public String get_Name() {
		return _Name;
	}

	public void set_Name(String _Name) {
		this._Name = _Name;
	}

	public String get_Image() {
		return _Image;
	}

	public void set_Image(String _Image) {
		this._Image = _Image;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getTextSize() {
		return textSize;
	}

	public void setTextSize(String textSize) {
		this.textSize = textSize;
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

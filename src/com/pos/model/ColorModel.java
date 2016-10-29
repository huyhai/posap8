package com.pos.model;

public class ColorModel {

	public static final String COLOR_TABLE_NAME = "color";

	public static final String FIELD_MAIN_ID = "GroupID";
	public static final String FIELD_NAME = "Name";
	public static final String FIELD_DESCRIPTION = "Description";
	public static final String FIELD_IMAGE = "Image";

	public final static String[] VATTU_FULL_PROJECTION = {
			ColorModel.FIELD_MAIN_ID, ColorModel.FIELD_IMAGE,
			ColorModel.FIELD_NAME, ColorModel.FIELD_DESCRIPTION };

	// private variables
	private String _ItemGr_ID;
	private String _Name;
	private String _Description;
	private String _Image;

	// Empty constructor
	public ColorModel() {

	}

	public String get_ItemGr_ID() {
		return _ItemGr_ID;
	}

	public void set_ItemGr_ID(String _ItemGr_ID) {
		this._ItemGr_ID = _ItemGr_ID;
	}

	public String get_Name() {
		return _Name;
	}

	public void set_Name(String _Name) {
		this._Name = _Name;
	}

	public String get_Description() {
		return _Description;
	}

	public void set_Description(String _Description) {
		this._Description = _Description;
	}

	public String get_Image() {
		return _Image;
	}

	public void set_Image(String _Image) {
		this._Image = _Image;
	}

}

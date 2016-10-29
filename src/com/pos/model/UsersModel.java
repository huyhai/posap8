package com.pos.model;

public class UsersModel {

	public static final String ID = "IDUser";
	public static final String USERNAME = "UserName";
	public static final String IMAGE = "Image";
	public static final String STATUS = "Status";

	public final static String[] VATTU_FULL_PROJECTION = { UsersModel.ID,
			UsersModel.USERNAME, UsersModel.IMAGE, UsersModel.STATUS };

	private String IDUser;
	private String UserName;
	private String Image;
	private String Status;

	public UsersModel() {

	}

	public String getIDUser() {
		return IDUser;
	}

	public void setIDUser(String iDUser) {
		IDUser = iDUser;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}

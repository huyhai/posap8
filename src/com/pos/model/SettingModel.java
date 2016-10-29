package com.pos.model;

public class SettingModel {
	private String companyName;
	private String companyAddress;
	private String companyPhone;
	private String companyFax;
	private String companyMail;
	private String companyGST;
	private String companyWeb;
	private String receiptFooter;
	public static final String COMNAME = "Company_name";
	public static final String COMADDRESS = "Address";
	public static final String COMPHONE = "Telephone";
	public static final String COMFAX = "Fax";
	public static final String COMMAIL = "Email";
	public static final String COMGST = "Company_reg_no";
	public static final String COMWEB = "Website";
	public static final String FOOTER = "Receipt_footer";

	public final static String[] SETTING_FULL_PROJECTION = { SettingModel.COMNAME,
		SettingModel.COMADDRESS, SettingModel.COMPHONE, SettingModel.COMFAX,
		SettingModel.COMMAIL, SettingModel.COMGST, SettingModel.COMWEB,
		SettingModel.FOOTER };
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getCompanyFax() {
		return companyFax;
	}
	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}
	public String getCompanyMail() {
		return companyMail;
	}
	public void setCompanyMail(String companyMail) {
		this.companyMail = companyMail;
	}
	public String getCompanyGST() {
		return companyGST;
	}
	public void setCompanyGST(String companyGST) {
		this.companyGST = companyGST;
	}
	public String getCompanyWeb() {
		return companyWeb;
	}
	public void setCompanyWeb(String companyWeb) {
		this.companyWeb = companyWeb;
	}
	public String getReceiptFooter() {
		return receiptFooter;
	}
	public void setReceiptFooter(String receiptFooter) {
		this.receiptFooter = receiptFooter;
	}

}

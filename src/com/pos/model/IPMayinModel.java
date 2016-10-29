package com.pos.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.pos.common.Utilities;

public class IPMayinModel {
	private String ip_address;
	private String id;

	public IPMayinModel() {

	}

	public void setData(JSONObject jSonInfo) throws JSONException {
		this.setIp_address(Utilities.getDataString(jSonInfo, "ip_address"));
		this.setId(Utilities.getDataString(jSonInfo, "id"));
	}

	public String getIp_address() {
		return ip_address;
	}

	/**
	 * @param ip_address the ip_address to set
	 */
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}

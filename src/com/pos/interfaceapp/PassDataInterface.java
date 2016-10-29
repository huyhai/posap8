package com.pos.interfaceapp;

/**
 * this interface will save all variable use in app, and variable was get by key
 * name [String key]
 * 
 */
public interface PassDataInterface {
	Object getPassedValue(String key);

	void setPassValue(String key, Object value);
}

package com.pos.libs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.logging.Log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;
	// content://media/external/images/media/7296
	// /storage/sdcard0/DCIM/Camera/IMG_20131109_234826.jpg
	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "Wiim";

	// All Shared Preferences Keys

	// User name (make variable public to access from outside)
	public static final String KEY_URI = "uri";
	public static final String STT = "stt";
	public static final String END = "eeee";
	public static final String ENDDATE = "eeees";
	public static final String NUMPRINT = "snum";
	// Email address (make variable public to access from outside)
	public static final String KEY_PATH = "path";
	public static final String KEY_NAME = "name";
	public static final String KEY_PASS = "pass";
	public static final String KEY_ISLOGIN = "log";
	public static final String KEY_LANGUAGE = "lan";
	public static final String KEY_MAILLING = "ass";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void saveListStack(ArrayList<Fragment> set) {
//		editor.putString("key", serialize(set));
		editor.commit();
	}
	public int getStt() {

		return pref.getInt(STT, -1);
	}

	public void saveStt(int uri) {
		editor.putInt(STT, uri);
		editor.commit();
	}
	public int getEND() {

		return pref.getInt(END, 0);
	}

	public void saveEND(int uri) {
		editor.putInt(END, uri);
		editor.commit();
	}
	
	public String getENDDATE() {

		return pref.getString(ENDDATE, "");
	}

	public void saveENDDATE(String uri) {
		editor.putString(ENDDATE, uri);
		editor.commit();
	}
	public String getNumPrint() {

		return pref.getString(NUMPRINT, "1");
	}

	public void saveNumPrint(String uri) {
		editor.putString(NUMPRINT, uri);
		editor.commit();
	}

	public void saveLogin(String name, String pass, boolean islog) {
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_PASS, pass);
		editor.putBoolean(KEY_ISLOGIN, islog);

		editor.commit();
	}

	public void saveMailling(boolean isMaill) {
		editor.putBoolean(KEY_MAILLING, isMaill);
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	// public void checkLogin(){
	// // Check login status
	// if(!this.isLoggedIn()){
	// // user is not logged in redirect him to Login Activity
	// Intent i = new Intent(_context, LoginActivity.class);
	// // Closing all the Activities
	// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//
	// // Add new Flag to start new Activity
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//
	// // Staring Login Activity
	// _context.startActivity(i);
	// }
	//
	// }

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> pic = new HashMap<String, String>();
		// user name
		pic.put(KEY_URI, pref.getString(KEY_URI, null));

		// user email id
		pic.put(KEY_PATH, pref.getString(KEY_PATH, null));

		// return user
		return pic;
	}

	public HashMap<String, String> getUserLogin() {
		HashMap<String, String> pic = new HashMap<String, String>();
		pic.put(KEY_NAME, pref.getString(KEY_NAME, null));
		pic.put(KEY_PASS, pref.getString(KEY_PASS, null));

		return pic;
	}

	public boolean getLanguage() {

		return pref.getBoolean(KEY_LANGUAGE, true);
	}

	public void saveLanguage(boolean name) {
		editor.putBoolean(KEY_LANGUAGE, name);
		editor.commit();
	}

	/**
	 * Clear session details
	 * */
	// public void logoutUser(){
	// // Clearing all data from Shared Preferences
	// editor.clear();
	// editor.commit();
	//
	// // After logout redirect user to Loing Activity
	// Intent i = new Intent(_context, LoginActivity.class);
	// // Closing all the Activities
	// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//
	// // Add new Flag to start new Activity
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	//
	// // Staring Login Activity
	// _context.startActivity(i);
	// }

	/**
	 * Quick check for login
	 * **/
	public boolean isLoggedIn() {
		return pref.getBoolean(KEY_ISLOGIN, false);
	}

	public boolean isMailling() {
		return pref.getBoolean(KEY_MAILLING, false);
	}
//	 private static final Log log = LogFactory.getLog(ObjectSerializer.class);
	    
	    public static String serialize(Serializable obj) throws IOException {
	        if (obj == null) return "";
	        try {
	            ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
	            ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
	            objStream.writeObject(obj);
	            objStream.close();
	            return encodeBytes(serialObj.toByteArray());
	        } catch (Exception e) {
//	        	return e;
//	            throw WrappedIOException.wrap("Serialization error: " + e.getMessage(), e);
	        }
			return null;
	    }
	    
	    public static Object deserialize(String str) throws IOException {
	        if (str == null || str.length() == 0) return null;
	        try {
	            ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
	            ObjectInputStream objStream = new ObjectInputStream(serialObj);
	            return objStream.readObject();
	        } catch (Exception e) {
//	            throw WrappedIOException.wrap("Deserialization error: " + e.getMessage(), e);
	        }
			return str;
	    }
	    
	    public static String encodeBytes(byte[] bytes) {
	        StringBuffer strBuf = new StringBuffer();
	    
	        for (int i = 0; i < bytes.length; i++) {
	            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
	            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
	        }
	        
	        return strBuf.toString();
	    }
	    
	    public static byte[] decodeBytes(String str) {
	        byte[] bytes = new byte[str.length() / 2];
	        for (int i = 0; i < str.length(); i+=2) {
	            char c = str.charAt(i);
	            bytes[i/2] = (byte) ((c - 'a') << 4);
	            c = str.charAt(i+1);
	            bytes[i/2] += (c - 'a');
	        }
	        return bytes;
	    }
}

package com.pos.libs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * App Preferences for store some thing
 * 
 * 
 */
public class AppPreferencesLib {
	private SharedPreferences mAppSharedPrefs;
	private Editor mPrefsEditor;

	/**
	 * Contructor get function return mAppSharedPrefs.getString("key", "");
	 * mPrefsEditor.putString("key", text); mPrefsEditor.commit();
	 * 
	 * @param context
	 */
	@SuppressLint("CommitPrefEdits")
	public AppPreferencesLib(Context context) {
		this.mAppSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.mPrefsEditor = mAppSharedPrefs.edit();
	}

	/**
	 * 
	 * @param firstTime
	 */
	public void saveCalendar(Long firstTime) {
		mPrefsEditor.putLong("time_first", firstTime);
		mPrefsEditor.commit();
	}

	public Long getCalendar() {
		return mAppSharedPrefs.getLong("time_first", 0);

	}

	/**
	 * save evernote token
	 * 
	 * @param token
	 */
	public void saveEvernoteAuthToken(String token) {
		mPrefsEditor.putString("evernote_auth_token", token);
		mPrefsEditor.commit();
	}

	/**
	 * Get Evernote token
	 * 
	 * @return
	 */
	public String getEvernoteAuthToken() {
		return mAppSharedPrefs.getString("evernote_auth_token", "");
	}
	
	public void saveLoginType(String loginType) {
		mPrefsEditor.putString("login_type", loginType);
		mPrefsEditor.commit();
	}
	
	public String getLoginType() {
		return mAppSharedPrefs.getString("login_type", "");
	}

	public void saveUserName(String username) {
		mPrefsEditor.putString("username", username);
		mPrefsEditor.commit();
	}

	public void saveIDUser(int IDuser) {
		mPrefsEditor.putInt("id_user", IDuser);
		mPrefsEditor.commit();
	}

	public String getIDUser() {
		return mAppSharedPrefs.getString("id_user", "");
	}

	public void savePassword(String password) {
		mPrefsEditor.putString("password", password);
		mPrefsEditor.commit();
	}

	public String getUserName() {
		return mAppSharedPrefs.getString("username", "");
	}

	public String getPassword() {
		return mAppSharedPrefs.getString("password", "");
	}

	public void saveIsRemember(Boolean isRemember) {
		mPrefsEditor.putBoolean("is_remmember", isRemember);
		mPrefsEditor.commit();
	}

	public Boolean getIsRemember() {
		return mAppSharedPrefs.getBoolean("is_remmember", false);
	}

}

package com.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.MySQLiteHelper;
import com.pos.db.UpdateAllDatabase;
import com.pos.libs.SessionManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;

public class SplashScreen extends Activity {

	/** The b active event. */
	protected boolean bActiveEvent = true;
	/** The i time display splash. */
	protected int iTimeDisplaySplash = 500;
	/** The is active app. */
	public static boolean isActiveApp = false;
	public static String regId;
	private SessionManager ss;
	MySQLiteHelper a = new MySQLiteHelper(SplashScreen.this);

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_layout);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Utilities.getGlobalVariable(SplashScreen.this).device_height = height;
		Utilities.getGlobalVariable(SplashScreen.this).device_width = width;
		// String str = android.os.Build.MODEL;
		// Log.v("HAI", str);
		ss = new SessionManager(SplashScreen.this);
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int iCount = 0;
					while ((SplashScreen.this.bActiveEvent && (iCount < SplashScreen.this.iTimeDisplaySplash))
							|| !SplashScreen.this.isActiveApp) {
						sleep(100);
						if (SplashScreen.this.bActiveEvent) {
							iCount += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
					e.printStackTrace();
				} finally {
					SplashScreen.this.finish();

					final Intent itTab = new Intent().setClass(
							SplashScreen.this.getApplicationContext(),
							Table.class);
					SplashScreen.this.startActivity(itTab);
				}
			}
		};

		splashThread.start();
		isActiveApp = true;
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		try {
//			SplashScreen.this.unregisterReceiver(receiver);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		IntentFilter intentGetKeySend = new IntentFilter();
//		intentGetKeySend.addAction(UserFunctions.LOGINUSER);
//		SplashScreen.this.registerReceiver(receiver, intentGetKeySend);

	}
}

package com.pos.common;


import com.pos.PosApp;
import com.pos.libs.ComDDUtilities;

import android.app.Activity;

/**
 * The Class Utilities.
 */
public final class Utilities extends ComDDUtilities {

	public static PosApp getGlobalVariable(final Activity act) {

		try {
			if (act != null) {
				return ((PosApp) act.getApplication());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}

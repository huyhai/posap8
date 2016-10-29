package com.pos.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pos.MainActivity;
import com.pos.R;

public class Header {
	private Activity ac;

	public Header(Activity _ac) {
		ac = _ac;

	}

	public ViewGroup getViewGroup() {
		LayoutInflater inflater = ac.getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.history_item,
				MainActivity.lvOrder, false);
		return header;
	}


}

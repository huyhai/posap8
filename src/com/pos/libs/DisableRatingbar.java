package com.pos.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RatingBar;

public class DisableRatingbar extends RatingBar {

	public DisableRatingbar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DisableRatingbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
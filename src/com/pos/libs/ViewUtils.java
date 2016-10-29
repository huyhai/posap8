package com.pos.libs;


import com.pos.CustomFragmentActivity;
import com.pos.common.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Utility methods for Views.
 */
public class ViewUtils {
	private ViewUtils() {}
	public static void hideKeyboard(Context context, EditText editor) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editor.getWindowToken(), 0);
	}
	public static void showKeyboard(Context context, EditText editor) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
		editor.setFocusable(true);
	}
	public static void enableDisableView(View view, boolean enabled) {
		view.setEnabled(enabled);

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			for (int idx = 0; idx < group.getChildCount(); idx++) {
				enableDisableView(group.getChildAt(idx), enabled);
			}
		}
	}

	public static View createViewFromXML(int idLayoutXML, Activity activity) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View layout = (View) inflater.inflate(idLayoutXML, null);
		return layout;
	}

	public static Animation startAnimation(View view, Activity activity,
			int idAnimation) {
		view.clearAnimation();
		Animation anim = AnimationUtils.loadAnimation(activity, idAnimation);
		view.startAnimation(anim);
		return anim;
	}

	public static void setViewWidths(View view, View[] views) {
		int w = view.getWidth();
		int h = view.getHeight();
		for (int i = 0; i < views.length; i++) {
			View v = views[i];
			v.layout((i + 1) * w, 0, (i + 2) * w, h);
			printView("view[" + i + "]", v);
		}
	}

	public static void createEffectTouch(View v, final int inActiveDrawble,
			final int activeDrawble) {
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				LayoutParams lpimg1 = v.getLayoutParams();
//				lpimg1.width = (int) (Utilities.getGlobalVariable(ac).device_width / 14);
//				lpimg1.height = (int) (Utilities.getGlobalVariable(ac).device_height / 27);
//				v.setLayoutParams(lpimg1);
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(activeDrawble);
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundResource(inActiveDrawble);
				return false;
			}
		});
	}
	public static void createEffectTouchForList(View v, final int inActiveDrawble,
			final int activeDrawble) {
		v.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(activeDrawble);
				else 
					v.setBackgroundResource(inActiveDrawble);
				return false;
			}
		});
	}
	public static void printView(String msg, View v) {
		System.out.println(msg + "=" + v);
		if (null == v) {
			return;
		}
		System.out.print("[" + v.getLeft());
		System.out.print(", " + v.getTop());
		System.out.print(", w=" + v.getWidth());
		System.out.println(", h=" + v.getHeight() + "]");
		System.out.println("mw=" + v.getMeasuredWidth() + ", mh="
				+ v.getMeasuredHeight());
		System.out.println("scroll [" + v.getScrollX() + "," + v.getScrollY()
				+ "]");
	}

	public static void initListView(Context context, ListView listView,
			String prefix, int numItems, int layout) {
		String[] arr = new String[numItems];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = prefix + (i + 1);
		}
		listView.setAdapter(new ArrayAdapter<String>(context, layout, arr));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Context context = view.getContext();
				String msg = "item[" + position + "]="
						+ parent.getItemAtPosition(position);
				Toast.makeText(context, msg, 1000).show();
				System.out.println(msg);
			}
		});
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static void unbindReferences(Activity activity, int idView) {
		try {
			View view = activity.findViewById(idView);
			if (view != null) {
				unbindViewReferences(view);
				if (view instanceof ViewGroup)
					unbindViewGroupReferences((ViewGroup) view);
			}
			System.gc();
		} catch (Throwable e) {
		}
	}

	private static void unbindViewGroupReferences(ViewGroup viewGroup) {
		int nrOfChildren = viewGroup.getChildCount();
		for (int i = 0; i < nrOfChildren; i++) {
			View view = viewGroup.getChildAt(i);
			unbindViewReferences(view);
			if (view instanceof ViewGroup)
				unbindViewGroupReferences((ViewGroup) view);
		}
		try {
			viewGroup.removeAllViews();
		} catch (Throwable mayHappen) {

		}
	}

	private static void unbindViewReferences(View view) {
		// set all listeners to null (not every view and not every API level
		// supports the methods)
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnCreateContextMenuListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnFocusChangeListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnKeyListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnLongClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		Log.i("unbindViewReferences: ", view.toString());
		// set background to null
		Drawable d = view.getBackground();

		if (d != null)
			d.setCallback(null);
		if (view instanceof ImageView || view instanceof ImageButton) {
			ImageView imageView = (ImageView) view;
			d = imageView.getDrawable();
			if (d != null)
				d.setCallback(null);
			imageView.setImageDrawable(null);
			imageView.setBackgroundDrawable(null);
		}
		// destroy webview
		if (view instanceof WebView) {
			((WebView) view).destroyDrawingCache();
			((WebView) view).destroy();
		}
	}
	public static void setWidthHeight(View view, double d, double i, Activity ac) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.width = (int) (Utilities
				.getGlobalVariable(ac).device_width / d);
		lp.height = (int) (Utilities
				.getGlobalVariable(ac).device_height / i);
		view.setLayoutParams(lp);
	}
	public static void setWidth(View view, double d,Activity ac) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.width = (int) (Utilities
				.getGlobalVariable(ac).device_width / d);
		view.setLayoutParams(lp);
	}
	public static void setHeight(View view, double d,Activity ac) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.height = (int) (Utilities
				.getGlobalVariable(ac).device_height / d);
		view.setLayoutParams(lp);
	}
}

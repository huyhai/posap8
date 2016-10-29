package com.pos.libs;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.serialport.SerialPort;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.interfaceapp.DialogCallBack;
import com.pos.interfaceapp.LocationCallBack;
import com.pos.model.ListOrderModel;

public class ComDDUtilities {

	private static final String YES = "Yes";

	private static final String NO = "No";

	private static final String OK = "Ok";
	private static Application mApplication;
	private static SerialPort mSerialPort_V;
	private static OutputStream mOutputStream_V;

	public static void closeStream(final Closeable o) {

		if (o != null) {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void showWelcome() {
		try {
			int baudrate = 9600;
			int databits = 8;
			int parity = 0;
			int stopbits = 1;
			int flowctl = 0;
			String path = "/dev/ttymxc2";
			mSerialPort_V = new SerialPort(new File(path), baudrate, databits,
					parity, stopbits, flowctl);
			mOutputStream_V = mSerialPort_V.getOutputStream();
			mOutputStream_V.write(0x1b);
			mOutputStream_V.write(0x40);

			mOutputStream_V.write(new String(
					"WELCOME TO          A1 BEST DIGITAL").getBytes());
			mOutputStream_V.write('\r');
			mOutputStream_V.write('\n');

			mApplication.closeSerialPort();
		} catch (Exception e) {
			// Toast.makeText(MainActivity.this, e.toString(),
			// Toast.LENGTH_LONG).show();
			// throw new RuntimeException(e);
		}
	}

	public static void showMinusPlus(int qly,String mdquantity, String mddiscount) {
		double amount1 = 0;
		try {
			int baudrate = 9600;
			int databits = 8;
			int parity = 0;
			int stopbits = 1;
			int flowctl = 0;
			String path = "/dev/ttymxc2";
			mSerialPort_V = new SerialPort(new File(path), baudrate, databits,
					parity, stopbits, flowctl);
			mOutputStream_V = mSerialPort_V.getOutputStream();
			mOutputStream_V.write(0x1b);
			mOutputStream_V.write(0x40);

			String itemName = PosApp.listOrderData.get(qly).getItemName()
					.toString();
			String spaceName = "";
			if (itemName.length() > 20) {
				itemName = itemName.substring(0, 20);

			} else {
				int tempvar = 20 - itemName.length();
				for (int i = 0; i < tempvar; i++) {
					spaceName += " ";

				}

			}
			mOutputStream_V.write(itemName.getBytes());
			mOutputStream_V.write(spaceName.getBytes());
			String space = "      ";
			// mOutputStream_V.write(space.getBytes());
			int changequantity = Integer.parseInt(PosApp.listOrderData.get(qly)
					.getQualyti());
			double amount = Double.parseDouble(PosApp.listOrderData.get(qly)
					.getUnitPrice())
					* Double.parseDouble(mdquantity)
					- Double.parseDouble(mddiscount);
			int len1 = PosApp.listOrderData.get(qly).getQualyti().toString()
					.length();
			String strtemp = "" + amount;
			int len2 = strtemp.length();
			double spaceneed = 14 - (len1 + len2);
			// mOutputStream_V.write(space.getBytes());
			for (int i = 0; i < spaceneed; i++) {
				space += " ";
			}
			String quantity = "" + changequantity;
			DecimalFormat df1 = new DecimalFormat("0.00");
			amount1 = Double.valueOf(amount1);
			String amountc = "" + df1.format(amount1);
			mOutputStream_V.write(quantity.getBytes());
			mOutputStream_V.write(space.getBytes());
			mOutputStream_V.write(amountc.getBytes());

			mOutputStream_V.write('\r');
			mOutputStream_V.write('\n');

			mApplication.closeSerialPort();
		} catch (Exception e) {
			// Toast.makeText(MainActivity.this, e.toString(),
			// Toast.LENGTH_LONG).show();
			// throw new RuntimeException(e);
		}
		// EO VFD
	}

	public static void saveObJectToFile(Context cont, Object objectToWrite,
			String fileName) {
		try {
			FileOutputStream fos = cont.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os;

			os = new ObjectOutputStream(fos);

			os.writeObject(objectToWrite);
			os.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Object readObjectFromFile(Context cont, String fileName) {
		Object simpleClass = null;
		try {
			FileInputStream fis = cont.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			simpleClass = is.readObject();
			is.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return simpleClass;
	}

	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}

	public static String getDateTimePrint() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * Delete recursive.
	 * 
	 * @param fileOrDirectory
	 *            the file or directory
	 */
	public static void deleteRecursive(final File fileOrDirectory) {

		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles()) {
				deleteRecursive(child);
			}
		}
		fileOrDirectory.delete();
	}

	/**
	 * Switch tab.
	 * 
	 * @param context
	 *            the context
	 * @param index
	 *            the index
	 */
	public static void switchTab(final Context context, final int index) {

		TabHost thCAR = (TabHost) ((Activity) context).getParent()
				.findViewById(android.R.id.tabhost);
		thCAR.setup();
		thCAR.setCurrentTab(index);
	}

	/**
	 * Disable virtual pad.
	 * 
	 * @param activity
	 *            the activity
	 */
	public static void disableVirtualPad(final Activity activity) {

		if (activity.getCurrentFocus() != null) {
			if (activity.getCurrentFocus().getWindowToken() != null) {
				InputMethodManager immVirtualPad = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				immVirtualPad.hideSoftInputFromWindow(activity
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * Gets the device id.
	 * 
	 * @param context
	 *            the context
	 * @return the device id
	 */
	public static final String getDeviceID(final Context context) {

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String result = telephonyManager.getDeviceId();
		if (result == null) {
			return "";
		} else {
			return result;
		}
	}

	/**
	 * Gets the image path from url.
	 * 
	 * @param sURL
	 *            the s url
	 * @return the image path from url
	 */
	public static String getImagePathFromURL(final String sURL) {

		String sFilePath = null;
		try {
			URLConnection conn;
			conn = new URL(sURL).openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			String fileName = sURL.toString().substring(
					sURL.toString().lastIndexOf('/') + 1,
					sURL.toString().lastIndexOf('.'));
			File file = new File(ConstantValue.SDCARD_ROOT);
			if (!file.exists()) {
				file.mkdirs();
			}
			sFilePath = ConstantValue.SDCARD_ROOT + fileName + ".ci";
			OutputStream os = new FileOutputStream(sFilePath);
			byte[] buffer = new byte[1024];
			int byteRead = 0;
			while ((byteRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, byteRead);
			}
			os.flush();
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return loadBitmap(ctx, sFilePath, 3);
		return sFilePath;
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap loadBitmapFromURL(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	/**
	 * Show dialog exit.
	 * 
	 * @param context
	 *            the context
	 * @param activity
	 *            the activity
	 * @param strTitle
	 *            the str title
	 * @param strMessage
	 *            the str message
	 */
	public static void showDialogExit(final Context context,
			final String title, final String message, final Activity _act) {

		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message).setNeutralButton(YES,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						((Activity) context).finish();
						System.gc();
						System.exit(0);
					}
				});

		builder.setPositiveButton(NO, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {

				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	private static EditText edName;
	private static ImageView imgPic;
	private static EditText edNameChina;
	private static EditText edDes;
	private static EditText edDesCN;

	private static TextView tvTitle;
	private static TextView tvnameChina;
	private static TextView tvDes;
	private static TextView tvDesCN;

	public static void showDialogAddCate(final Context context,
			final String title, final String message, final Activity ac) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(new
		// ContextThemeWrapper(context, R.style.AlertDialogCustom));
		Builder builder = new Builder(context);
		builder.setTitle("a");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.event_item, null);
		builder.setView(v);
		edName = (EditText) v.findViewById(R.id.edName);
		edNameChina = (EditText) v.findViewById(R.id.edNameChina);
		// edDes = (EditText) v.findViewById(R.id.edDes);
		// edDesCN = (EditText) v.findViewById(R.id.edDesCN);
		imgPic = (ImageView) v.findViewById(R.id.imgPic);
		tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		tvnameChina = (TextView) v.findViewById(R.id.tvnameChina);
		// tvDes = (TextView) v.findViewById(R.id.tvDes);
		// tvDesCN = (TextView) v.findViewById(R.id.tvDesCN);
		((CustomFragmentActivity) context).setWidthHeight(tvTitle, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvnameChina, 6,
				LayoutParams.WRAP_CONTENT);

		((CustomFragmentActivity) context).setWidthHeight(imgPic, 5.5, 5);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {

			}
		});

		builder.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {

						dialog.dismiss();
					}
				});
		imgPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// // Get the layout inflater
		// LayoutInflater inflater = ac.getLayoutInflater();
		//
		// // Inflate and set the layout for the dialog
		// // Pass null as the parent view because its going in the dialog
		// layout
		// builder.setView(inflater.inflate(R.layout.event_item, null))
		// // Add action buttons
		// .setPositiveButton("a", new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int id) {
		// // sign in the user ...
		// }
		// })
		// .setNegativeButton("b", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.dismiss();
		// }
		// });
		// builder.create();
		//
		// builder.show();

		// Builder builder = new Builder(context);
		// builder.setTitle(title);
		// builder.setMessage(message).setNeutralButton(YES,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(final DialogInterface dialog,
		// final int id) {
		// ((Activity) context).finish();
		// System.gc();
		// System.exit(0);
		// }
		// });
		//
		// builder.setPositiveButton(NO, new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(final DialogInterface dialog, final int which) {
		//
		// dialog.dismiss();
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.setCancelable(false);
		// alert.setCanceledOnTouchOutside(false);
		// alert.show();
	}

	/**
	 * Check external storage.
	 * 
	 * @return true, if successful
	 */
	public static boolean checkExternalStorage() {

		boolean bExtAvailable = false;

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			bExtAvailable = true;
		} else {
			bExtAvailable = false;
		}

		return bExtAvailable;
	}

	/**
	 * Check internet connection.
	 * 
	 * @param context
	 *            the context
	 * @return true, if successful
	 */
	public static boolean checkInternetConnection(final Context context) {

		// SharedPreferences mPref =
		// context.getSharedPreferences(ConstantValue.APP_NAME,
		// Context.MODE_PRIVATE);
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if ((cm.getActiveNetworkInfo() != null)
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the account.
	 * 
	 * @param accountManager
	 *            the account manager
	 * @return the account
	 */
	public static Account getAccount(final AccountManager accountManager) {

		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	/**
	 * Gets the bitmap from path.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param sFilePath
	 *            the s file path
	 * @param inSampleSize
	 *            the in sample size
	 * @return the bitmap from path
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapFromPath(final Context ctx,
			final String sFilePath, final int inSampleSize) {

		Bitmap bm = null;
		try {
			Display display = ((WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			int sclWidth = (int) (display.getWidth() / 1.35);
			int sclHeight = (int) (display.getHeight() / 1.5);

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = inSampleSize;
			opt.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(sFilePath, opt);

			if (opt.outWidth > sclWidth) {
				opt.inSampleSize = opt.outWidth / sclWidth;
			}
			if (opt.outHeight > sclHeight) {
				opt.inSampleSize = opt.outHeight / sclHeight;
			}

			opt.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(sFilePath, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * Gets the bitmap from path.
	 * 
	 * @param sFilePath
	 *            the s file path
	 * @param maxWidth
	 *            the max width
	 * @return the bitmap from path
	 */
	public static Bitmap getBitmapFromPath(final String sFilePath,
			final int maxWidth) {

		Bitmap bm = null;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(sFilePath, opt);
			if (opt.outWidth > maxWidth) {
				opt.inSampleSize = opt.outWidth / maxWidth;
			}
			opt.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(sFilePath, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * Gets the bitmap from string.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param s
	 *            the s
	 * @param inSampleSize
	 *            the in sample size
	 * @return the bitmap from string
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapFromString(final Context ctx, final String s) {

		Bitmap myBitmap = null;
		try {
			if (s == null) {
				return null;
			}
			byte[] bytes = null;
			bytes = Base64.decode(s, Base64.DEFAULT);
			Display display = ((WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();

			int sclWidth = display.getWidth() / 2;
			int sclHeight = display.getHeight() / 2;

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 1;
			opt.inJustDecodeBounds = true;

			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);

			if (opt.outWidth > sclWidth) {
				opt.inSampleSize = opt.outWidth / sclWidth;
			} else if (opt.outHeight > sclHeight) {
				opt.inSampleSize = opt.outHeight / sclHeight;
			}

			opt.inJustDecodeBounds = false;
			myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					opt);
			// myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			opt.requestCancelDecode();
			opt = null;
			bytes = null;
			System.gc();
		} catch (OutOfMemoryError e) {
			// getBitmapFromString(s);
			System.gc();
		}
		return myBitmap;
	}

	/**
	 * Gets the bitmap from string.
	 * 
	 * @param s
	 *            the s
	 * @return the bitmap from string
	 */
	public static Bitmap getBitmapFromString(final String s) {

		try {
			if (s == null) {
				return null;
			}
			byte[] bytes = null;
			bytes = Base64.decode(s, Base64.DEFAULT);
			Bitmap myBitmap = null;
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;

			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);

			if (opt.outWidth > 200) {
				opt.inSampleSize = opt.outWidth / 200;
			} else if (opt.outHeight > 200) {
				opt.inSampleSize = opt.outHeight / 200;
			}

			opt.inJustDecodeBounds = false;
			myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					opt);
			// myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			bytes = null;
			opt.requestCancelDecode();
			opt = null;
			System.gc();

			return myBitmap;
		} catch (OutOfMemoryError e) {
			// getBitmapFromString(s);
			System.gc();
		}
		return null;
	}

	/**
	 * Gets the resized bitmap.
	 * 
	 * @param bm
	 *            the bm
	 * @param newHeight
	 *            the new height
	 * @param newWidth
	 *            the new width
	 * @return the resized bitmap
	 */
	public static Bitmap getResizedBitmap(final Bitmap bm, final int newHeight,
			final int newWidth) {

		if (bm == null) {
			return null;
		}
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		// RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/**
	 * Checks if is valid email address.
	 * 
	 * @param sEmail
	 *            the s email
	 * @return true, if is valid email address
	 */
	public static boolean isValidEmailAddress(final String sEmail) {

		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
		return pattern.matcher(sEmail).matches();
	}

	public static boolean isValidateEmail(String email) {

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * Parses the from date to string format date.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String parseFromDateToStringFormatDate(final Date date) {

		/** The date format. */

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateFormat.format(new Date());
	}

	/**
	 * Parses the from date to string format time.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String parseFromDateToStringFormatTime(final Date date) {

		/** The date format. */

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateFormat.format(new Date());
	}

	/**
	 * Parses the from string to date.
	 * 
	 * @param sDate
	 *            the s date
	 * @return the date
	 */
	public static Date parseFromStringToDate(final String sDate) {

		/** The date format. */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return dateFormat.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * Show dialog back.
	 * 
	 * @param context
	 *            the context
	 * @param message
	 *            the message
	 * @return the alert dialog
	 */
	public static AlertDialog showDialogCallBack(final Context context,
			final String message, final DialogCallBack callback) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setNeutralButton(OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {

						if (null != callback) {
							callback.callBack();
						}
					}
				});
		AlertDialog alert = builder.create();
		return alert;
	}

	/**
	 * Show dialog cannot connect to server.
	 * 
	 * @param context
	 *            the context
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @return the alert dialog
	 */
	public static void showDialogCannotConnectToServer(final Context context,
			final String title, final String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
				.setNeutralButton(OK, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {

						// Do nothing.
						System.exit(0);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show dialog no back.
	 * 
	 * @param context
	 *            the context
	 * @param message
	 *            the message
	 * @return the alert dialog
	 */
	public static AlertDialog showDialogNoBack(final Context context,
			final String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setNeutralButton(OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {

					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		return alert;
	}
	public static AlertDialog showDialogNoBack1(final Context context,
			final String message,final Activity ac) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setNeutralButton(OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						ac.finish();

					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		return alert;
	}

	/**
	 * Show dialog no back.
	 * 
	 * @param context
	 *            the context
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @return the alert dialog
	 */
	public static AlertDialog showDialogNoBack(final Context context,
			final String title, final String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
				.setNeutralButton(OK, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {

						// Do nothing.
					}
				});
		AlertDialog alert = builder.create();
		return alert;
	}

	// public static String getTagFromDBByLanguage(final Activity act, final
	// String dbName, final String table, final String field, final String
	// value,
	// final String select) {
	//
	// SQLiteDatabase DB = (new SqlLiteAdapter(act,
	// dbName)).getWritableDatabase();
	// String result = "";
	// String sQuery = "";
	// if ((field != null) && (value != null)) {
	// sQuery = "select " + select + " from " + table + " where " + field + "="
	// + "'" + value + "'" + " and id_language="
	// + Utilities.getGlobalVariable(act).Language_code;
	// }
	// try {
	// Cursor cursor = DB.rawQuery(sQuery, null);
	// if (cursor.getCount() > 0) {
	// cursor.moveToNext();
	// result = cursor.getString(cursor.getColumnIndex(select));
	// cursor.deactivate();
	//
	// }
	// DB.close();
	// } catch (Exception e) {
	// if (DB != null) {
	// DB.close();
	// }
	// e.printStackTrace();
	// }
	// return result;
	// }

	// public static String getTagFromDB(final Activity act, final String
	// dbName, final String table, final String field, final String value,
	// final String select) {
	//
	// SQLiteDatabase DB = (new SqlLiteAdapter(act,
	// dbName)).getWritableDatabase();
	// String result = "";
	// String sQuery = "";
	// if ((field != null) && (value != null)) {
	// sQuery = "select " + select + " from " + table + " where " + field + "="
	// + "'" + value + "'";
	// }
	// try {
	// Cursor cursor = DB.rawQuery(sQuery, null);
	// if (cursor.getCount() > 0) {
	// cursor.moveToNext();
	// result = cursor.getString(cursor.getColumnIndex(select));
	// cursor.deactivate();
	//
	// }
	// DB.close();
	// } catch (Exception e) {
	// if (DB != null) {
	// DB.close();
	// }
	// e.printStackTrace();
	// }
	// return result;
	// }

	// public static synchronized ArrayList<Map<String, String>>
	// getCursorFromDB(final Activity act, final String dbName, final String
	// table) {
	//
	// System.out.println("started get");
	// SQLiteDatabase DB = (new SqlLiteAdapter(act,
	// dbName)).getWritableDatabase();
	// String sQuery;
	// ArrayList<Map<String, String>> result = new ArrayList<Map<String,
	// String>>();
	// sQuery = "select * from " + table;
	// Cursor cursor = null;
	// System.out.println("started query");
	// try {
	// cursor = DB.rawQuery(sQuery, null);
	// if (cursor.getCount() > 0) {
	// cursor.moveToFirst();
	// Map<String, String> resultNote;
	// while (!cursor.isAfterLast()) {
	// resultNote = new HashMap<String, String>();
	// for (int i = 0; i < cursor.getColumnCount(); i++) {
	// resultNote.put(cursor.getColumnName(i), cursor.getString(i));
	// }
	// result.add(resultNote);
	// cursor.moveToNext();
	// }
	// }
	// DB.close();
	// cursor.deactivate();
	// } catch (Exception e) {
	// if (DB != null) {
	// DB.close();
	// }
	// if (cursor != null) {
	// cursor.deactivate();
	// }
	// e.printStackTrace();
	// }
	// System.out.println("end get");
	// return result;
	// }

	public static void callNumber(final Context cont, final String number) {

		try {
			String url = "tel:" + number;
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			cont.startActivity(intent);
		} catch (Throwable e) {
			// TODO: handle exception
		}
	}

	public static void copyfile(final String srFile, final String dtFile) {

		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void writeSQLiteToFile(final Context context,
			final ArrayList<Map<String, String>> mapList, final String fileName) {

		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(mapList);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Map<String, String>> readSQLiteFromFile(
			final Context context, final String _fileName) {

		String fileName = _fileName;
		ArrayList<Map<String, String>> mapList = null;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			fis = context.openFileInput(fileName);
			is = new ObjectInputStream(fis);

			mapList = (ArrayList<Map<String, String>>) is.readObject();
			Utilities.closeStream(fis);
			Utilities.closeStream(is);

		} catch (Throwable e) {
			e.printStackTrace();
			Utilities.closeStream(fis);
			Utilities.closeStream(is);
		}
		return mapList;
	}

	public static Object[] getListSQLFromFile(final Context cont,
			final String fileName) {

		ArrayList<Map<String, String>> mapList = null;
		mapList = Utilities.readSQLiteFromFile(cont, fileName);
		if (mapList == null) {
			mapList = getSQLListFileFromAsset(cont, fileName);
			Utilities.writeSQLiteToFile(cont, mapList, fileName);
		}
		return parseFromMap(mapList);
	}

	@SuppressWarnings({ "unchecked", "finally" })
	public static ArrayList<Map<String, String>> getSQLListFileFromAsset(
			final Context cont, final String fileName) {

		InputStream myInput = null;
		ObjectInputStream is = null;
		ArrayList<Map<String, String>> mapList = null;
		try {
			// Open your local db as the input stream
			// myInput = new BufferedInputStream(new
			// FileInputStream(ConstantValue.SDCARD_ROOT + DB_NAME));
			myInput = cont.getAssets().open(fileName);
			is = new ObjectInputStream(myInput);

			mapList = (ArrayList<Map<String, String>>) is.readObject();
			Utilities.writeSQLiteToFile(cont, mapList, fileName);
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Utilities.closeStream(myInput);
			Utilities.closeStream(is);
			return mapList;
		}
	}

	public static Object[] parseFromMap(final ArrayList<Map<String, String>> map) {

		Object[] objectForReturn = new Object[map.size()];
		try {
			for (int i = 0; i < map.size(); i++) {
				objectForReturn[i] = map.get(i);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return objectForReturn;
	}

	public static void sendEmail(final Context cont, final String email) {

		Intent iEmail = new Intent(Intent.ACTION_SEND);
		iEmail.putExtra(Intent.EXTRA_EMAIL, email);
		iEmail.setType("message/rfc822");
		cont.startActivity(Intent.createChooser(iEmail,
				"Choose an Email client :"));
	}

	public static void callPhone(final Context cont, final String phone) {

		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + phone));
			cont.startActivity(callIntent);
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}
	}

	public static long getContactId(Context context, long rawContactId) {
		Cursor cur = null;
		try {
			cur = context.getContentResolver().query(
					ContactsContract.RawContacts.CONTENT_URI,
					new String[] { ContactsContract.RawContacts.CONTACT_ID },
					ContactsContract.RawContacts._ID + "=" + rawContactId,
					null, null);
			if (cur.moveToFirst()) {
				return cur
						.getLong(cur
								.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return -1l;
	}

	// get name contact by number
	public static String getContactDisplayNameByNumber(String number, Context c) {
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		String name = "?";

		ContentResolver contentResolver = c.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {
				BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME },
				null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				// String contactId =
				// contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}

	// get number phone contact by name
	public static String getPhoneNumber(String name, Context context) {
		String ret = null;
		String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " like'%" + name + "%'";
		String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
		Cursor c = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
				selection, null, null);
		if (c.moveToFirst()) {
			ret = c.getString(0);
		}
		c.close();
		if (ret == null)
			ret = "Unsaved";
		return ret;
	}

	// save phone to contact
	public static void savePhone(Context c, String name, String number) {

		ContentValues values = new ContentValues();
		values.put(Data.DISPLAY_NAME, name);
		Uri rawContactUri = c.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		values.clear();
		values.put(Phone.NUMBER, number);
		values.put(Phone.TYPE, Phone.TYPE_OTHER);
		values.put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		c.getContentResolver().insert(Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.MIMETYPE, Data.CONTENT_TYPE);
		values.put(
				ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
				name);
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		c.getContentResolver().insert(Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(
				ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
				name);
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		c.getContentResolver().insert(Data.CONTENT_URI, values);
	}

	/**
	 * Gets the string from bitmap.
	 * 
	 * @param bitmap
	 *            the bitmap
	 * @param check
	 *            the check
	 * @return the string from bitmap
	 */

	public static String getStringFromBitmap(final Bitmap bitmap,
			final String check) {

		String s = "";
		byte[] bytes = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (check.equalsIgnoreCase("PNG")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
			}
			bytes = baos.toByteArray();
			s = Base64.encodeToString(bytes, 1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return s;
	}

	public static GPSTracker gps;
	private static double latitude;
	private static double longitude;

	public static void getGPSLocation(Context _con,
			LocationCallBack locationCallback) {
		gps = new GPSTracker(_con);
		LatLng mCurrentPoint;
		// check if GPS enabled
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			Log.v("HAI", "getGPS");
		} else {
			latitude = 10.7358698;
			longitude = 106.71966789999999;
			// gps.showSettingsAlert();
		}
		mCurrentPoint = new LatLng(latitude, longitude);
		LoadLocationAddress newTask = new LoadLocationAddress(locationCallback);
		newTask.execute(mCurrentPoint);
	}

	public static Address getLatLog(String _address, Context context) {
		String add = _address;// address of location u have
								// entered
		Address address = null;
		Geocoder geocoder = new Geocoder(context);
		try {

			List<Address> addresses = geocoder.getFromLocationName(add, 1);

			if (addresses != null && !addresses.isEmpty()) {

				address = addresses.get(0);

			} else {

			}
		} catch (IOException e) {

		}
		return address;
	}

	public static long timeDifferenceInDays(final long firstDate,
			final long secondDate) {
		// final long firstDateMilli = firstDate.getTimeInMillis();
		// final long secondDateMilli = secondDate.getTimeInMillis();
		final long diff = secondDate - firstDate;

		// 24 * 60 * 60 * 1000 because I want the difference in days. Change
		// as your wish.
		// final double diffDays = (double) diff / (double) (24 * 60 * 60 *
		// 1000);

		long days = diff / (24 * 60 * 60 * 1000);
		return days;
	}

	public static void recycle(ImageView iv) {
		if (iv != null) {
			Drawable drawable = iv.getDrawable();
			if (drawable instanceof BitmapDrawable) {

				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				if (null != bitmap && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
					iv.setImageBitmap(null);
					iv.setImageDrawable(null);
				}

			}
		}
	}

	// public static Bitmap getBitmapFromAsset(Context context, String file) {
	// AssetManager assetManager = context.getAssets();
	// Bitmap bitmap = null;
	// InputStream istr;
	// try {
	// istr = getAssets.open(strName);
	// bitmap = BitmapFactory.decodeStream(istr);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return bitmap;
	// }

	public static Bitmap getBitmapFromAsset(Context context, String file) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inPreferredConfig = Bitmap.Config.ARGB_8888;

			InputStream is = context.getAssets().open(file);
			bitmap = BitmapFactory.decodeStream(is, null, op);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	public static Drawable getDrawableFromAsset(Context context, String path) {
		Drawable d = null;
		try {
			d = Drawable.createFromStream(context.getAssets().open(path), path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public void recycleDrawable(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			bitmap.recycle();
		}
	}

	// public static Bitmap getBitmapFromView(View view) {
	// Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
	// view.getHeight(),Bitmap.Config.ARGB_8888);
	// Canvas canvas = new Canvas(returnedBitmap);
	// Drawable bgDrawable =view.getBackground();
	// if (bgDrawable!=null)
	// bgDrawable.draw(canvas);
	// else
	// canvas.drawColor(Color.WHITE);
	// view.draw(canvas);
	// // returnedBitmap.recycle();
	// return returnedBitmap;
	// }

	public static Bitmap recycleBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
				v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.measure(v.getLayoutParams().width, v.getLayoutParams().height); // Change
																			// from
																			// original
																			// post
		v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
		v.draw(c);
		b.recycle();
		return b;
	}

	@SuppressWarnings("unused")
	private int getListViewHeight(ListView list) {
		ListAdapter adapter = list.getAdapter();

		int listviewHeight = 0;

		list.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED));

		listviewHeight = list.getMeasuredHeight() * adapter.getCount()
				+ (adapter.getCount() * list.getDividerHeight());

		return listviewHeight;
	}

	public int roundInt(int number) {
		int index;
		int results = number / 2;
		int balance = number % 2;

		if (balance != 0) {
			index = results + 1;
		} else {
			index = results;
		}
		return index;
	}

	public static long checkSizeMb(File dir) {
		long result = 0;
		try {
			Stack<File> dirlist = new Stack<File>();
			dirlist.clear();

			dirlist.push(dir);

			while (!dirlist.isEmpty()) {
				File dirCurrent = dirlist.pop();

				File[] fileList = dirCurrent.listFiles();
				for (int i = 0; i < fileList.length; i++) {

					if (fileList[i].isDirectory())
						dirlist.push(fileList[i]);
					else
						result += fileList[i].length();
				}
			}

			result = result / (1024 * 1024);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static Boolean checkCamera(Context context) {
		PackageManager pm = context.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	public static String setLanguage(String keyName) {
		String result = "";
		try {
			// result =
			// SplashScreen.keyWorldModel.getListKey().containsKey(keyName) ?
			// SplashScreen.keyWorldModel.getListKey().get(keyName).toString() :
			// "";
		} catch (Exception e) {
			// TODO: handle exception
			result = "N/A";
		}
		return result;
	}

	public static String getDataString(JSONObject jSonOb, String key) {
		String result = "Null";
		try {
			if (!jSonOb.getString(key).equals(JSONObject.NULL)) {
				result = jSonOb.getString(key);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}

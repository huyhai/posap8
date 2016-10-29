package com.pos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.FragmentFactory;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.interfaceapp.PassDataInterface;
import com.pos.interfaceapp.onImageSet;
import com.pos.libs.DatePickerDialogFragment;
import com.pos.libs.SessionManager;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogItems;
import com.pos.ui.LoginDialog;
import com.pos.ui.SplitBillActivity;

@SuppressWarnings("unused")
public class CustomFragmentActivity extends FragmentActivity implements
		PassDataInterface {

	protected ArrayList<Integer> mListView;

	private Map<String, Object> passData;

	private static final String DATE_FRAGMENT = "date_fragment";

	private static final String MAP_FRAGMENT = "map_fragment";

	private FrameLayout frMain;

	private RelativeLayout pdBar;

	private ProgressBar pdBarMain;

	public Boolean isShowDialog = false;

	protected Fragment mFragment;

	public static Boolean isShowProgressBar = false;

	protected Boolean isOnActivityResult = false;

	public Boolean clearCasch = false;

	public static DisplayImageOptions options;

	private int mTitleRes;
	protected Fragment mFrag;
	private SessionManager session;

	private Bundle savedInstanceState;

	public CustomFragmentActivity(int titleRes) {
		System.out.println("tinhvc mTitleRes: " + mTitleRes);
		mTitleRes = titleRes;
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		session = new SessionManager(CustomFragmentActivity.this);
		boolean languageCode = session.getLanguage();
		if (languageCode) {
			Utilities.getGlobalVariable(CustomFragmentActivity.this).englist = true;
			Locale locale = new Locale("en");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			CustomFragmentActivity.this.getApplicationContext().getResources()
					.updateConfiguration(config, null);
			Utilities.getGlobalVariable(CustomFragmentActivity.this).language_code = 1;
			// Main.setText("en");
		} else {
			Utilities.getGlobalVariable(CustomFragmentActivity.this).englist = false;
			Locale locale = new Locale("zh-TW");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			CustomFragmentActivity.this.getApplicationContext().getResources()
					.updateConfiguration(config, null);
			Utilities.getGlobalVariable(CustomFragmentActivity.this).language_code = 2;
			// Main.setText("zh-TW");
		}

		// setSlidingActionBarEnabled(true);
		// setSlidingMenu(new ListMenuFragment(), true);
		// TODO Auto-generated method stub
		if (this.mListView == null) {
			this.mListView = new ArrayList<Integer>();
		}

		// this.frMain = (FrameLayout) this.findViewById(R.id.content_frame);

		this.clearCasch = Utilities
				.getGlobalVariable(CustomFragmentActivity.this).clearCase;

		if (Utilities.getGlobalVariable(CustomFragmentActivity.this).isHavePush == false) {
			getSettingPush(CustomFragmentActivity.this);
		}

		// options = new
		// DisplayImageOptions.Builder().showStubImage(R.drawable.photo_none).showImageForEmptyUri(R.drawable.photo_none).showImageOnFail(R.drawable.photo_none).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		if (clearCasch) {
			PosApp.imageLoader2.clearDiscCache();
			PosApp.imageLoader2.clearMemoryCache();
		}
	}

	public void setWidthHeight(View view, double d, double i) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.width = (int) (Utilities
				.getGlobalVariable(CustomFragmentActivity.this).device_width / d);
		lp.height = (int) (Utilities
				.getGlobalVariable(CustomFragmentActivity.this).device_height / i);
		view.setLayoutParams(lp);
	}

	public void setWidth(View view, double d) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.width = (int) (Utilities
				.getGlobalVariable(CustomFragmentActivity.this).device_width / d);
		view.setLayoutParams(lp);
	}

	public void setHeight(View view, double d) {
		LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.height = (int) (Utilities
				.getGlobalVariable(CustomFragmentActivity.this).device_height / d);
		view.setLayoutParams(lp);
	}

	public void getSettingPush(Context context) {
		Utilities.getGlobalVariable(this).isHavePush = true;
		/**
		 * Function get push from server
		 */
	}

	public void resetListFragment() {
		while (mListView.size() > 0) {
			mListView.remove(mListView.size() - 1);
		}
	}

	/**
	 * get string from bitmap
	 * 
	 * @param bitmap
	 * @param check
	 * @return
	 */
	public String getStringFromBitmap(final Bitmap bitmap, final String check) {

		String s = null;
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

	// Read bitmap
	public Bitmap getBitmapFromURI(Context cont, Uri selectedImage) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 4;
		Bitmap bm = null;
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = cont.getContentResolver().openAssetFileDescriptor(
					selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bm = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, opt);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	/**
	 * end
	 */

	public void startFragment(final int key) {
		// && (key != ConstantValue.SETTING_SIGNUP_FRAGMENT)
		if (!this.mListView.contains(key)) {
			this.mListView.add(key);
		}
		fragmentTransition(key);

	}

	public void replaceFragment(final int key, final Boolean isAnimation) {
		// && (key != ConstantValue.SETTING_SIGNUP_FRAGMENT)
		// // change release map to here
		// if (mFragment instanceof CustomGoogleMapFragment) {
		// releaseMap();
		// }
		if (!this.mListView.contains(key)) {
			this.mListView.add(key);
		}
		if (isAnimation) {
			this.flipit(key, true);
		} else {
			fragmentTransition(key);
		}
	}

	public void replaceFragmentPeer(final int key, final Boolean isAnimation,
			Boolean peer) {
		// && (key != ConstantValue.SETTING_SIGNUP_FRAGMENT)
		// // change release map to here
		// if (mFragment instanceof CustomGoogleMapFragment) {
		// releaseMap();
		// }

		if (peer) {
			if (!this.mListView.contains(key)) {
				this.mListView.add(key);
			}
		} else {
			this.mListView.add(key);
		}
		if (peer) {
			this.mListView.remove(mListView.size() - 1);
			this.mListView.add(key);
		}
		if (isAnimation) {
			this.flipit(key, true);
		} else {
			fragmentTransition(key);
		}
	}

	public void backToFragment(final int key, final Boolean isAnimation) {

		if (isAnimation) {
			this.flipit(key, false);
		} else {
			fragmentTransition(key);
		}

	}

	public void returnToFragment(final int key, final Boolean isAnimation) {

		for (int i = mListView.size() - 1; i >= 0; i--) {
			if (mListView.get(i) == key) {
				break;
			}
			mListView.remove(mListView.size() - 1);
		}

		if (isAnimation) {
			this.flipit(key, false);
		} else {
			fragmentTransition(key);
		}

	}

	FragmentTransaction fragmentTS;

	public void fragmentTransition(final int key) {
		System.gc();
		PosApp.imageLoader2.stop();
		PosApp.imageLoader2.clearMemoryCache();
		onFragmentChange();
		// if (mFragment instanceof CustomGoogleMapFragment) {
		// // releaseMap();
		// }
		if (null != mFragment) {
			fragmentTS.remove(mFragment);
		}
		fragmentTS = this.getSupportFragmentManager().beginTransaction();
		mFragment = FragmentFactory.getFragmentByKey(key);
		// fragmentTS.setCustomAnimations(R.anim.slide_in_left,
		// R.anim.slide_out_right);
		fragmentTS.replace(R.id.content_frame, mFragment);
		// fragmentTS.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTS.commitAllowingStateLoss();
	}

	public Fragment findFragment() {
		return mFragment;
	}

	/**
	 * Gets the picture.
	 */
	public void getPicture() {
		isOnActivityResult = true;
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		this.startActivityForResult(photoPickerIntent,
				ConstantValue.UPLOAD_PHOTO);
		// this.startActivityForResult(new Intent(Intent.ACTION_PICK,
		// android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
		// ConstantValue.UPLOAD_PHOTO);
	}

	public void showDatePickerDialog(Fragment fragment, Boolean isDatepicker) {
		if (!isShowDialog) {
			isShowDialog = true;
			DatePickerDialogFragment newFragment = new DatePickerDialogFragment(
					fragment, isDatepicker);
			newFragment.show(getSupportFragmentManager(), DATE_FRAGMENT);
		}
	}

	public Object getPassedValue(final String key) {

		// TODO Auto-generated method stub
		if (null != passData && passData.containsKey(key)) {
			return this.passData.get(key);
		}
		return -1;
	}

	public void setPassValue(final String key, final Object value) {

		// TODO Auto-generated method stub
		if (this.passData == null) {
			this.passData = new HashMap<String, Object>();
		}
		this.passData.put(key, value);

	}

	// public static Boolean isSignUpCompany = false;

	@Override
	public void onBackPressed() {
		if (this.mListView.size() > 1) {
			Boolean isAnimation = false;
			this.mListView.remove(this.mListView.size() - 1);
			this.backToFragment(this.mListView.get(this.mListView.size() - 1),
					isAnimation);
		} else {
//			Utilities.getGlobalVariable(CustomFragmentActivity.this).paymentMode = "0";
//			Utilities.getGlobalVariable(CustomFragmentActivity.this).statusPay = "0";
//			if (PosApp.listOrderData.size() > 0) {
//				if (Utilities.getGlobalVariable(CustomFragmentActivity.this).isUpdate) {
//					Log.v("HAI", "update");
//					Utils.HoldSale(
//							MainActivity.tvTotalAmount.getText().toString(),
//							"0",
//							MainActivity.btnDisCountValue.getText().toString(),
//							Double.parseDouble(MainActivity.tvTotalAmount
//									.getText().toString())
//									+ Double.parseDouble(MainActivity.btnDisCountValue
//											.getText().toString()) + "",
//							MainActivity.tvTotalAmount.getText().toString(),
//							MainActivity.btnGSTValue.getText().toString(),
//							MainActivity.btnSVCValue.getText().toString(),
//							MainActivity.note,
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).paymentMode,
//							"1",
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).numberCustomer
//									+ "",
//							CustomFragmentActivity.this,
//							MainActivity.btnSubTotalValue.getText().toString(),
//							UserFunctions.getInstance().getSaleModel()
//									.getSaleID(),
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).statusPay,"","","");
//				} else {
//					Utils.HoldSale(
//							MainActivity.tvTotalAmount.getText().toString(),
//							"0",
//							MainActivity.btnDisCountValue.getText().toString(),
//							Double.parseDouble(MainActivity.tvTotalAmount
//									.getText().toString())
//									+ Double.parseDouble(MainActivity.btnDisCountValue
//											.getText().toString()) + "",
//							MainActivity.tvTotalAmount.getText().toString(),
//							MainActivity.btnGSTValue.getText().toString(),
//							MainActivity.btnSVCValue.getText().toString(),
//							MainActivity.note,
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).paymentMode,
//							"1",
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).numberCustomer
//									+ "",
//							CustomFragmentActivity.this,
//							MainActivity.btnSubTotalValue.getText().toString(),
//							"",
//							Utilities
//									.getGlobalVariable(CustomFragmentActivity.this).statusPay,"","","");
//				}
//
//			} else {
//				// Log.v("HAI", "a");
//			}
			PosApp.listOrderSplit.clear();
			SplitBillActivity.listBill.clear();
//			Utils.saveWhenExit(CustomFragmentActivity.this);
			PosApp.listOrderData.clear();
//			PosApp.listOrderData2.clear();
			super.onBackPressed();
			return;
		}
		return;
	}

	// private boolean doubleBackToExitPressedOnce;

	/**
	 * Process display view after makeup
	 * 
	 */
	public void recycle(ImageView iv) {
		Drawable drawable = iv.getDrawable();
		if (drawable instanceof BitmapDrawable) {

			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			iv.setImageBitmap(null);
			iv.setImageDrawable(null);
			iv.postInvalidate();

			if (bitmap != null)
				bitmap.recycle();
			bitmap = null;
		}
	}

	private Boolean isRun = true;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isRun = true;
		isOnActivityResult = false;

	}

	/**
	 * dangtb view rotate animation
	 */
	private Interpolator accelerator = new AccelerateInterpolator();

	private Interpolator decelerator = new DecelerateInterpolator();

	private void flipit(final int key, final Boolean isForward) {

		float visToInvisFrom;
		float visToInvisTo;
		final float invisToVisFrom;
		final float invisToVisTo;
		if (isForward) {
			visToInvisFrom = 0f;
			visToInvisTo = 90f;
			invisToVisFrom = -90f;
			invisToVisTo = 0f;
		} else {
			visToInvisFrom = 0f;
			visToInvisTo = -90f;
			invisToVisFrom = 90f;
			invisToVisTo = 0f;
		}
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(this.frMain,
				"rotationY", visToInvisFrom, visToInvisTo);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(this.frMain,
				"rotationY", invisToVisFrom, invisToVisTo);
		invisToVis.setDuration(250);
		// invisToVis.setInterpolator(decelerator);
		visToInvis.setDuration(250);
		// visToInvis.setInterpolator(accelerator);
		visToInvis.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(final Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(final Animator anim) {
				fragmentTransition(key);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						invisToVis.start();
					}
				}, 1);
			}
		});
		invisToVis.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationStart(final Animator animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(final Animator animation) {

			}

		});
		visToInvis.start();
	}

	public void showProgressBar() {
		pdBar.setVisibility(View.VISIBLE);
		frMain.setEnabled(false);
		isShowProgressBar = true;
		final InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(frMain.getWindowToken(), 0);
	}

	public void hideProgressBar() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pdBar.setVisibility(View.GONE);
				frMain.setEnabled(true);
				isShowProgressBar = false;
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (isShowProgressBar)
			return false;
		return super.dispatchTouchEvent(ev);
	}

	public static void saveObJectToFile(Object objectToWrite, Context con,
			String _fileName) {
		String fileName = _fileName;
		try {
			FileOutputStream fos = con.openFileOutput(fileName,
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isRun = false;
		super.onPause();
		// this.finish();
		// releaseMap();
	}

	// /**
	// * Release mapview when not use
	// */
	// public void releaseMap() {
	// try {
	// FragmentTransaction trans =
	// getSupportFragmentManager().beginTransaction();
	// SupportMapFragment frag = ((SupportMapFragment)
	// this.getSupportFragmentManager().findFragmentById(R.id.mapMain));
	// if (null != frag) {
	// trans.remove(frag);
	// }
	// trans.commitAllowingStateLoss();
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	// }

	public void onFragmentChange() {

	}

	/**
	 * [Explain the description for this method here].
	 * 
	 * @param requestCode
	 *            the request code
	 * @param resultCode
	 *            the result code
	 * @param data
	 *            the data
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	private String strPhotoType;
	public static String selectedImageUriM;
	public static String selectedImagePathM;

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantValue.UPLOAD_PHOTO) {
				try {
					Uri selectedImageUri = data.getData();
					String selectedImagePath = getPath(selectedImageUri);

					strPhotoType = selectedImagePath.toString().substring(
							selectedImagePath.toString().lastIndexOf(".") + 1,
							selectedImagePath.toString().length());
					Bitmap bitmap = null;
					if (selectedImageUri != null) {
						bitmap = getBitmapFromURI(CustomFragmentActivity.this,
								selectedImageUri);
						selectedImageUriM = String.valueOf(selectedImageUri);
					} else {
						File imgFile = new File(selectedImagePath);
						bitmap = BitmapFactory.decodeFile(imgFile
								.getAbsolutePath());
						selectedImagePathM = selectedImagePath;

					}
					try {
						DialogGroup.tvNameImage.setText(selectedImagePath);
						DialogGroup.imgPic.bringToFront();
						DialogGroup.imgPic1.setBackgroundColor(Color.WHITE);
						DialogGroup.imgPic.setImageBitmap(bitmap);
					} catch (Exception e) {
						// TODO: handle exception
					}

					try {
						DialogItems.tvNameImagei.setText(selectedImagePath);
						DialogItems.imgPici.bringToFront();
						DialogItems.imgPic1i.setBackgroundColor(Color.WHITE);
						DialogItems.imgPici.setImageBitmap(bitmap);
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						// DialogItems.tvNameImagei.setText(selectedImagePath);
						// DialogItems.imgPici.bringToFront();
						// DialogItems.imgPic1i.setBackgroundColor(Color.WHITE);
						// DialogItems.imgPici.setImageBitmap(bitmap);
						// LoginDialog.imgAvartar.setImageBitmap(bitmap);
					} catch (Exception e) {
						// TODO: handle exception
					}
					// DialogGroup.image = selectedImagePath;
					exportDB(selectedImagePath, nextSessionId());
					// ((onImageSet) findFragment()).setimage(selectedImageUri,
					// selectedImagePath);
				} catch (Exception e) {
					e.printStackTrace();
					this.calldialog();
				}
			}
		}
	}

	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	private void exportDB(final String a, final String name) {
		final String inFileName = a;
		java.io.File outFileName = new java.io.File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/" + name + ".png");
		// final String outFileName = "/data/app/chiettinh.png";

		// File file = new File(outFileName);
		// if (file != null && !file.exists())
		{
			InputStream myInput = null; // MainApplication.getAppContext().getAssets().open(DB_ASSETS_NAME);
			// Path to the just created empty db
			// Open the empty db as the output stream
			OutputStream myOutput = null; // new FileOutputStream(outFileName);

			try {
				myInput = new FileInputStream(inFileName);
				myOutput = new FileOutputStream(outFileName);

				// transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;

				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}

				// Close the streams
				myOutput.flush();
				myOutput.close();
				myInput.close();
				DialogGroup.image = outFileName.toString();
				DialogItems.image = outFileName.toString();
				LoginDialog.linkImage = outFileName.toString();
				// db.execSQL(TABLE_CHECK_EXIST);
				Log.i("Export DB", "Done copy");
				// Utils.showMess("Sao lÆ°u cÆ¡ sÆ¡ dá»¯ liá»‡u thĂ nh cĂ´ng!");
			} catch (Exception e) {
				Log.i("Export DB", e.toString());

			} finally {

			}
		}
	}

	/**
	 * Calldialog.
	 */
	public void calldialog() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Selection is not a photo").setNeutralButton(
				"OK", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						// Do nothing.\

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Gets the path.
	 * 
	 * @param uri
	 *            the uri
	 * @return the path
	 */
	@SuppressWarnings("deprecation")
	public String getPath(final Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = this.managedQuery(uri, projection, null, null, null);

		int columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(columnIndex);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Utilities.getGlobalVariable(CustomFragmentActivity.this).isPrintBill=false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		isRun = false;
		super.onStop();
	}

	private Boolean isCallingFace = false;

	private Bitmap getBitmapFromAsset(String strName) throws IOException {
		AssetManager assetManager = getAssets();

		InputStream istr = assetManager.open(strName);
		Bitmap bitmap = BitmapFactory.decodeStream(istr);

		return bitmap;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// toggle();
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // getSupportMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	// public Dialog dialogSelectImage;
	// public ImageView imgBackSelectImg;
	// public Button btnChoiceExis;
	// public Button btnTakePhoto;
	// public Button btnNo;
	// public RelativeLayout rlAllDialogSelecImg;
	//
	// // public ImageView imgSet;
	//
	// public void showDialogSelectImg() {
	// // custom dialog
	// if (!isShowDialog) {
	// isShowDialog = true;
	// // imgSet = _imgSet;
	// dialogSelectImage = new Dialog(this);
	// dialogSelectImage.getWindow();
	// dialogSelectImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialogSelectImage
	// .setContentView(R.layout.dialog_select_photo_layout);
	// dialogSelectImage.getWindow().setBackgroundDrawable(
	// new ColorDrawable(android.graphics.Color.TRANSPARENT));
	// // dialog.setTitle("Title...");
	//
	// // set the custom dialog components - text, image and button
	//
	// this.imgBackSelectImg = (ImageView) dialogSelectImage
	// .findViewById(R.id.imgClose);
	// this.btnChoiceExis = (Button) dialogSelectImage
	// .findViewById(R.id.btnChoiceExis);
	// this.btnTakePhoto = (Button) dialogSelectImage
	// .findViewById(R.id.btnTakePhoto);
	// this.btnNo = (Button) dialogSelectImage.findViewById(R.id.btnNo);
	//
	// rlAllDialogSelecImg = (RelativeLayout) dialogSelectImage
	// .findViewById(R.id.rlAllDialogSelecImg);
	//
	// btnChoiceExis.setOnClickListener(this);
	// imgBackSelectImg.setOnClickListener(this);
	// btnTakePhoto.setOnClickListener(this);
	// btnNo.setOnClickListener(this);
	// dialogSelectImage.setOnDismissListener(new OnDismissListener() {
	//
	// @Override
	// public void onDismiss(DialogInterface dialog) {
	// // TODO Auto-generated method stub
	// isShowDialog = false;
	// }
	// });
	//
	// dialogSelectImage.show();
	// }
	//
	// }

}

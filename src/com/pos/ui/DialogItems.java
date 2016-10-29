package com.pos.ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.SpinnerAdapterMy;
import com.pos.common.ConstantValue;
import com.pos.db.ItemsDataSource;
import com.pos.db.PayDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.PayModel;

public class DialogItems implements OnItemSelectedListener {
	private EditText edItemName;
	public static ImageView imgPici;
	private EditText edNameChinai;
	private EditText edItemCode;
	private EditText edUom;

	private TextView tvItemCode;
	private TextView tvChineseName;
	private TextView tvUom;
	private TextView tvBarcode;
	private TextView tvVisiblei;
	CheckBox acci;
	CheckBox inaci;
	public static ImageView imgPic1i;
	public static TextView tvNameImagei;
	private TextView tvTitle1i;
	private Spinner spinneri;
	private TextView tvItemName;
	private TextView tvCostPrice;
	private TextView tvPrice;
	private TextView tvPrice2;
	private TextView tvPriceSpecial;
	private TextView tvRemarks;
	public EditText edBarcode;
	private EditText edCostPrice;
	private EditText edRemarks;
	private EditText edPrice;
	private EditText edPrice2;
	private EditText edPriceSpecial;
	private TextView tvVisible1;
	private Spinner spGroup;
	private TextView lowstock;
	private boolean isedit;
	String image1 = "";
	private int posIDGr = -1;
	private Spinner spTs;
	private TextView tvTs;
	private Activity ac;
	private static String textSizeDB = "";

	public void showDialogAddItem(final Context context, final Activity _ac,
			boolean _isedit, final String itemID) {
		isedit = _isedit;
		ac = _ac;
		// AlertDialog.Builder builder = new AlertDialog.Builder(new
		// ContextThemeWrapper(context, R.style.AlertDialogCustom));
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_manageItems));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.item_slide, null);
		builder.setView(v);
		edItemName = (EditText) v.findViewById(R.id.edItemName);
		edNameChinai = (EditText) v.findViewById(R.id.edChineseName);
		edItemCode = (EditText) v.findViewById(R.id.edItemCode);
		edUom = (EditText) v.findViewById(R.id.edUom);
		imgPici = (ImageView) v.findViewById(R.id.imgPic);
		tvItemCode = (TextView) v.findViewById(R.id.tvItemCode);
		tvChineseName = (TextView) v.findViewById(R.id.tvChineseName);
		tvUom = (TextView) v.findViewById(R.id.tvUom);
		tvBarcode = (TextView) v.findViewById(R.id.tvBarcode);
		tvVisiblei = (TextView) v.findViewById(R.id.tvVisible);
		imgPic1i = (ImageView) v.findViewById(R.id.imgPic1);
		tvNameImagei = (TextView) v.findViewById(R.id.tvNameImage);
		tvTitle1i = (TextView) v.findViewById(R.id.tvTitle1);

		tvItemName = (TextView) v.findViewById(R.id.tvItemName);
		tvCostPrice = (TextView) v.findViewById(R.id.tvCostPrice);
		tvPrice = (TextView) v.findViewById(R.id.tvPrice);
		tvPrice2 = (TextView) v.findViewById(R.id.tvPrice2);
		tvPriceSpecial = (TextView) v.findViewById(R.id.tvPriceSpecial);
		tvRemarks = (TextView) v.findViewById(R.id.tvRemarks);
		edBarcode = (EditText) v.findViewById(R.id.edBarcode);
		edCostPrice = (EditText) v.findViewById(R.id.edCostPrice);
		edRemarks = (EditText) v.findViewById(R.id.edRemarks);
		edPrice = (EditText) v.findViewById(R.id.edPrice);
		edPrice2 = (EditText) v.findViewById(R.id.edPrice2);
		edPriceSpecial = (EditText) v.findViewById(R.id.edPriceSpecial);
		tvVisible1 = (TextView) v.findViewById(R.id.tvVisible1);
		spGroup = (Spinner) v.findViewById(R.id.spGroup);
		spTs = (Spinner) v.findViewById(R.id.spTs);
		tvTs = (TextView) v.findViewById(R.id.tvTs);

		((CustomFragmentActivity) context).setWidthHeight(tvTs, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvVisible1, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvItemCode, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvChineseName, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvUom, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvBarcode, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvVisiblei, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvTitle1i, 6,
				LayoutParams.WRAP_CONTENT);

		((CustomFragmentActivity) context).setWidthHeight(tvItemName, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvCostPrice, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvPrice, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvPrice2, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvPriceSpecial, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvRemarks, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(imgPici, 5.5, 5);
		((CustomFragmentActivity) context).setWidthHeight(imgPic1i, 5.4, 5);

		spinneri = (Spinner) v.findViewById(R.id.spColor);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				context, R.array.planets_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinneri.setAdapter(adapter);
		spinneri.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				context, R.array.size_array,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTs.setAdapter(adapter2);
		spTs.setOnItemSelectedListener(this);

		SpinnerAdapterMy adapter1 = new SpinnerAdapterMy(ac, R.layout.aii,
				Home.listMainCateModel);
		spGroup.setAdapter(adapter1);
		spGroup.setOnItemSelectedListener(this);

		acci = (CheckBox) v.findViewById(R.id.checkbox_active);
		inaci = (CheckBox) v.findViewById(R.id.checkbox_inavtice);

		if (isedit) {
			ItemsDataSource ditem = new ItemsDataSource(ac, ac);
			edItemCode.setText(ditem.checkitemcode(itemID));
			edItemName.setText(ditem.checkname(itemID));
			edNameChinai.setText(ditem.checknameCN(itemID));
			edUom.setText(ditem.checkitemUOM(itemID));
			edBarcode.setText(ditem.checkitemBarCode(itemID));
			edCostPrice.setText(ditem.checkitemCPrice(itemID));
			edPrice.setText(ditem.checkPrice1(itemID));
			edPrice2.setText(ditem.checkPrice2(itemID));
			edPriceSpecial.setText(ditem.checkSPrice(itemID));
			edRemarks.setText(ditem.checkStock(itemID));
			acci.setVisibility(View.GONE);
			inaci.setVisibility(View.GONE);
			edRemarks.setEnabled(false);
			image1 = ditem.getImage(itemID);
			textSizeDB = ditem.getTs(itemID);
			String name = ditem.getGroup(itemID);
			for (int i = 0; i < Home.listMainCateModel.size(); i++) {
				if (name.equals(Home.listMainCateModel.get(i).get_Name())) {
					spGroup.setSelection(i);
					posIDGr = i;
					groupID = Home.listMainCateModel.get(i).get_ItemGr_ID();
					break;
				}
			}
		}
		acci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!acci.isChecked()) {
					acci.setChecked(false);
					inaci.setChecked(true);
				} else {
					acci.setChecked(true);
					inaci.setChecked(false);
				}

			}
		});
		inaci.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!inaci.isChecked()) {
					inaci.setChecked(false);
					acci.setChecked(true);
				} else {
					inaci.setChecked(true);
					acci.setChecked(false);
				}

			}
		});

		builder.setPositiveButton(ac.getString(R.string.str_confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {

			}
		});

		builder.setNegativeButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {

						dialog.dismiss();
					}
				});
		imgPici.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((CustomFragmentActivity) context).getPicture();
			}
		});
		final AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (checkConstraint()) {
							PayDataSource dataSourcePay = new PayDataSource(ac,
									ac);
							exportDB(tvNameImagei.getText().toString(),
									nextSessionId());
							if (isedit) {
								ItemsDataSource ditem = new ItemsDataSource(ac,
										ac);

								ditem.UpdateItem(edItemName.getText()
										.toString(), edNameChinai.getText()
										.toString(), itemID, edItemCode
										.getText().toString(), realImage,
										edBarcode.getText().toString(), edUom
												.getText().toString(),
										edCostPrice.getText().toString(),
										edPrice.getText().toString(), edPrice2
												.getText().toString(),
										edPriceSpecial.getText().toString(),
										edRemarks.getText().toString(),
										groupID, textsize);
								ditem.UpdateItem2(edItemName.getText()
										.toString(), edNameChinai.getText()
										.toString(), itemID, edItemCode
										.getText().toString(), realImage,
										edBarcode.getText().toString(), edUom
												.getText().toString(),
										edCostPrice.getText().toString(),
										edPrice.getText().toString(), edPrice2
												.getText().toString(),
										edPriceSpecial.getText().toString(),
										edRemarks.getText().toString());
								alert.dismiss();
							} else {
								if (TextUtils.isEmpty(edItemName.getText()
										.toString())) {
									edItemName
											.setError("This field is required");
								}
								if (TextUtils.isEmpty(edItemCode.getText()
										.toString())) {
									edItemCode
											.setError("This field is required");
								}
								if (TextUtils.isEmpty(edPrice.getText()
										.toString())) {
									edPrice.setError("This field is required");
								}
								if (!TextUtils.isEmpty(edItemName.getText()
										.toString())
										&& !TextUtils.isEmpty(edItemCode
												.getText().toString())
										&& !TextUtils.isEmpty(edPrice.getText()
												.toString())) {
									ItemsModel mainMd = new ItemsModel();
									ItemsDataSource dataSource = new ItemsDataSource(
											context, ac);

									mainMd.setBarcode(edBarcode.getText()
											.toString());
									mainMd.setCost_price(edCostPrice.getText()
											.toString());
									mainMd.setItem_code(edItemCode.getText()
											.toString());
									mainMd.setItem_group_ID(groupID);
									mainMd.setItem_image(realImage);
									mainMd.setRemarks(edRemarks.getText()
											.toString());
									mainMd.setSelling_price_1(edPrice.getText()
											.toString());
									mainMd.setSelling_price_2(edPrice2
											.getText().toString());
									mainMd.setSpecial_price(edPriceSpecial
											.getText().toString());
									mainMd.setUom(edUom.getText().toString());
									mainMd.setTextSize(textsize);
									// if (acci.isChecked()) {
									mainMd.setStatus("1");
									// } else {
									// mainMd.setStatus("0");
									// }
									if (!dataSource.checkItemCode(edItemCode
											.getText().toString())) {

										long id_insert = dataSource
												.insert(mainMd);
										PayModel up1 = new PayModel();
										up1.setItemCode(edItemCode.getText()
												.toString());
										up1.setItemName(edItemName.getText()
												.toString());
										up1.setUom(dataSourcePay.loadUom(up1
												.getItemCode()));
										PayDataSource pay = new PayDataSource(
												ac, ac);
										long a = pay
												.insertInventoryReportAddnew(
														up1, edRemarks
																.getText()
																.toString(),
														ac, edCostPrice
																.getText()
																.toString());

										for (int i = 0; i < 2; i++) {
											if (i % 2 == 0) {
												long id_insert1 = dataSource
														.insertTranslations(
																id_insert,
																1,
																edItemName
																		.getText()
																		.toString());
											} else {
												long id_insert1 = dataSource
														.insertTranslations(
																id_insert,
																2,
																edNameChinai
																		.getText()
																		.toString());
											}

										}
										alert.dismiss();
									} else {
										edItemCode
												.setError("Item Code already exist");
									}
								}
							}
							// ((CustomFragmentActivity) ac).replaceFragment(
							// ConstantValue.ITEMS, false);
							// if (MainActivity.listMainCateModel != null)
							// MainActivity.listMainCateModel.clear();
							//
							// MainActivity.listMainCateModel =
							// dataSource.loadMainCate();
							// MainActivity.dataAdapter
							// .setItemList(MainActivity.listMainCateModel);
							// MainActivity.dataAdapter.notifyDataSetChanged();

						}
					}
				});

		// alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
		// new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });

	}

	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	private String realImage = "";

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
				realImage = outFileName.toString();
				// DialogItems.image = outFileName.toString();
				// LoginDialog.linkImage = outFileName.toString();
				// db.execSQL(TABLE_CHECK_EXIST);
				Log.i("Export DB", "Done copy");
				// Utils.showMess("Sao lưu cơ sơ dữ liệu thành công!");
			} catch (Exception e) {
				realImage = image;
				Log.i("Export DB", e.toString());

			} finally {

			}
		}
	}

	public Boolean checkConstraint() {

		if (TextUtils.isEmpty(edItemCode.getText().toString())) {
			edItemCode.setError("This field is requered");
			return false;
		}
		if (TextUtils.isEmpty(edItemName.getText().toString())) {
			edItemName.setError("This field is requered");
			return false;
		}
		if (TextUtils.isEmpty(edCostPrice.getText().toString())) {
			edCostPrice.setError("This field is requered");
			return false;
		}
		if (TextUtils.isEmpty(edPrice.getText().toString())) {
			edPrice.setError("This field is requered");
			return false;
		}
		if (TextUtils.isEmpty(edRemarks.getText().toString())) {
			edRemarks.setError("This field is requered");
			return false;
		}

		return true;
	}

	public static String image;
	private static String textsize = "";
	private String groupID;

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == spinneri) {

			imgPic1i.bringToFront();
			tvNameImagei.setText("");
			if (position == 0) {
				image = "red";
				imgPic1i.setBackgroundColor(Color.RED);
			}
			if (position == 1) {
				image = "yellow";
				imgPic1i.setBackgroundColor(Color.YELLOW);
			}
			if (position == 2) {
				image = "blue";
				imgPic1i.setBackgroundColor(Color.BLUE);
			}
			if (position == 3) {
				image = "green";
				imgPic1i.setBackgroundColor(Color.GREEN);
			}
			if (position == 4) {
				image = "black";
				imgPic1i.setBackgroundColor(Color.BLACK);
			}
			if (position == 5) {
				image = "white";
				imgPic1i.setBackgroundColor(Color.WHITE);
			}
			if (position == 6) {
				image = "magenta";
				imgPic1i.setBackgroundColor(Color.MAGENTA);
			}
			if (position == 7) {
				image = "cyan";
				imgPic1i.setBackgroundColor(Color.CYAN);
			}
			// imgPic1i.bringToFront();
			// tvNameImagei.setText("");
			if (image1 != "") {
				imgPic1i.bringToFront();
				tvNameImagei.setText("");
				if (image1.equals("red")) {
					imgPic1i.setBackgroundColor(Color.RED);
					spinneri.setSelection(0);
				} else if (image1.equals("yellow")) {
					imgPic1i.setBackgroundColor(Color.YELLOW);
					spinneri.setSelection(1);
				} else if (image1.equals("blue")) {
					imgPic1i.setBackgroundColor(Color.BLUE);
					spinneri.setSelection(2);
				} else if (image1.equals("green")) {
					imgPic1i.setBackgroundColor(Color.GREEN);
					spinneri.setSelection(3);
				} else if (image1.equals("black")) {
					imgPic1i.setBackgroundColor(Color.BLACK);
					spinneri.setSelection(4);
				} else if (image1.equals("white")) {
					imgPic1i.setBackgroundColor(Color.WHITE);
					spinneri.setSelection(5);
				} else if (image1.equals("magenta")) {
					imgPic1i.setBackgroundColor(Color.MAGENTA);
					spinneri.setSelection(6);
				} else if (image1.equals("cyan")) {
					imgPic1i.setBackgroundColor(Color.CYAN);
					spinneri.setSelection(7);
				} else {
					PosApp.imageLoader2.displayImage("file:///"
							+ image1, imgPici, CustomFragmentActivity.options);
					try {
						tvNameImagei.setText(image1);
						imgPici.bringToFront();
						imgPic1i.setBackgroundColor(Color.WHITE);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				image1 = "";
			}
		}
		if (parent == spGroup) {
			groupID = Home.listMainCateModel.get(position).get_ItemGr_ID();

		}
		if (parent == spTs) {
			if (position == 0) {
				textsize = "small";
				edItemName.setTextAppearance(ac,
						android.R.style.TextAppearance_Small);
			}
			if (position == 1) {
				textsize = "medium";
				edItemName.setTextAppearance(ac,
						android.R.style.TextAppearance_Medium);
			}
			if (position == 2) {
				textsize = "large";
				edItemName.setTextAppearance(ac,
						android.R.style.TextAppearance_Large);
			}
			if (textSizeDB != "") {
				if (textSizeDB.equals("small")) {
					spTs.setSelection(0);
				}
				if (textSizeDB.equals("medium")) {
					spTs.setSelection(1);
				}
				if (textSizeDB.equals("large")) {
					spTs.setSelection(2);
				}
				textSizeDB = "";
			}

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// image = image1;

	}
}

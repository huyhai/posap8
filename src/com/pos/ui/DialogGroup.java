package com.pos.ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.pos.CustomFragmentActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.db.ItemsDataSource;
import com.pos.db.MainCateDataSource;
import com.pos.db.PayDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.MainCateModel;
import com.pos.model.PayModel;

public class DialogGroup implements OnItemSelectedListener {
	private EditText edName;
	public static ImageView imgPic;
	private EditText edNameChina;
	private EditText edDes;
	private EditText edDesCN;

	private TextView tvTitle;
	private TextView tvnameChina;
	private TextView tvDes;
	private TextView tvDesCN;
	private TextView tvVisible;
	private TextView tvVisible1;
	CheckBox acc;
	CheckBox inac;
	public static ImageView imgPic1;
	public static TextView tvNameImage;
	private TextView tvTitle1;
	private Spinner spinner;
	public boolean isEdit;
	private RelativeLayout rNameChina;
	private RelativeLayout rDesCn;
	private TextView tvGroupCode;
	private EditText edGroupCode;
	private String groupida = "";
	private Activity ac_;
	private String image1 = "";
	private String gr;
	private Spinner spTs;
	private static String textsizeDB = "";

	public void showDialogAddCate(final Context context, final Activity ac,
			final boolean _isEdit, final String grID) {
		isEdit = _isEdit;
		ac_ = ac;
		gr = grID;
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_ManageGroup));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.event_item, null);
		builder.setView(v);
		initView(v);
		initData(ac);

		acc.setVisibility(View.GONE);
		inac.setVisibility(View.GONE);
		acc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!acc.isChecked()) {
					acc.setChecked(false);
					inac.setChecked(true);
				} else {
					acc.setChecked(true);
					inac.setChecked(false);
				}

			}
		});
		inac.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!inac.isChecked()) {
					inac.setChecked(false);
					acc.setChecked(true);
				} else {
					inac.setChecked(true);
					acc.setChecked(false);
				}

			}
		});

		builder.setPositiveButton(ac.getString(R.string.str_confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {

				// if (Home.listMainCateModel != null)
				// Home.listMainCateModel.clear();
				//
				// Home.listMainCateModel = dataSource.loadMainCate();
				// Home.dataAdapter.setItemList(Home.listMainCateModel);
				// Home.dataAdapter.notifyDataSetChanged();

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
		imgPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((CustomFragmentActivity) context).getPicture();
			}
		});
		final AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
		alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isEdit) {
							MainCateDataSource mcate = new MainCateDataSource(
									ac);
							exportDB(tvNameImage.getText().toString(),
									nextSessionId());
							mcate.update(realImage, edGroupCode.getText()
									.toString(), groupida, edName.getText()
									.toString(), edNameChina.getText()
									.toString(), textsize);
							mcate.update2(realImage, edGroupCode.getText()
									.toString(), groupida, edName.getText()
									.toString(), edNameChina.getText()
									.toString());
							// image1 = mcate.getImage(grID);
							alert.dismiss();
						} else {
							if (TextUtils.isEmpty(edName.getText().toString())) {
								edName.setError("This field is required");
							}
							// if (TextUtils.isEmpty(edNameChina.getText()
							// .toString())) {
							// edNameChina.setError("This field is required");
							// }
							if (TextUtils.isEmpty(edGroupCode.getText()
									.toString())) {
								edGroupCode.setError("This field is required");
							}
							if (!TextUtils.isEmpty(edName.getText().toString())
									&& !TextUtils.isEmpty(edGroupCode.getText()
											.toString())) {
								MainCateModel mainMd = new MainCateModel();
								MainCateDataSource dataSource = new MainCateDataSource(
										ac);
								mainMd.set_Image(image);
								// if (acc.isChecked()) {
								mainMd.setActive("1");
								// } else {
								// mainMd.setActive("0");
								// }
								mainMd.set_groupcode(edGroupCode.getText()
										.toString());
								mainMd.setTextSize(textsize);
								if (!dataSource.checkGroupCode(edGroupCode
										.getText().toString())) {
									long id_insert = dataSource.insert(mainMd);
									for (int i = 0; i < 2; i++) {
										if (i % 2 == 0) {
											long id_insert1 = dataSource
													.insertTranslations(edName
															.getText()
															.toString(), "",
															id_insert, 1);
										} else {
											long id_insert1 = dataSource
													.insertTranslations(
															edNameChina
																	.getText()
																	.toString(),
															"", id_insert, 2);
										}

									}
									alert.dismiss();
								} else {
									edGroupCode
											.setError("Group Code already exist");
								}

							}

						}

					}
				});

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
				// Utils.showMess("Sao lÆ°u cÆ¡ sÆ¡ dá»¯ liá»‡u thĂ nh cĂ´ng!");
			} catch (Exception e) {
				realImage = image;
				Log.i("Export DB", e.toString());

			} finally {

			}
		}
	}

	private void initData(Activity context) {
		((CustomFragmentActivity) context).setWidthHeight(tvTitle, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvnameChina, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvGroupCode, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvVisible, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvVisible1, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(tvTitle1, 6,
				LayoutParams.WRAP_CONTENT);
		((CustomFragmentActivity) context).setWidthHeight(imgPic, 5.5, 5);
		((CustomFragmentActivity) context).setWidthHeight(imgPic1, 5.4, 5);

		if (isEdit) {
			// rDesCn.setVisibility(View.GONE);
			// rNameChina.setVisibility(View.GONE);
			int i = Utilities.getGlobalVariable(context).posEdit;
			edName.setText(Home.listMainCateModel.get(i).get_Name());
			edGroupCode.setText(Home.listMainCateModel.get(i).get_group_code());
			// edDes.setText(Home.listMainCateModel.get(i).get_Description());
			acc.setChecked(true);
			acc.setVisibility(View.GONE);
			inac.setVisibility(View.GONE);
			MainCateDataSource mcate = new MainCateDataSource(context);
			groupida = mcate.checkgroupid(Home.listMainCateModel.get(i)
					.get_Name());
			String groupcode = mcate.checkgroupcode(groupida);
			String groupnamecn = mcate.checkgroupCN(groupida);
			edNameChina.setText(groupnamecn);
			edGroupCode.setText(groupcode);
			image1 = mcate.getImage(gr);
			textsizeDB = mcate.getTs(gr);
			// ArrayAdapter<CharSequence> adapter = ArrayAdapter
			// .createFromResource(context, R.array.planets_array,
			// android.R.layout.simple_spinner_item);
			// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// spinner.setAdapter(adapter);
			// spinner.setOnItemSelectedListener(this);

			// if (Home.listMainCateModel.get(i).get_Image().endsWith("red")) {
			// imgPic.setBackgroundColor(Color.RED);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("yellow")) {
			// imgPic.setBackgroundColor(Color.YELLOW);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("blue")) {
			// imgPic.setBackgroundColor(Color.BLUE);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("green")) {
			// imgPic.setBackgroundColor(Color.GREEN);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("black")) {
			// imgPic.setBackgroundColor(Color.BLACK);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("white")) {
			// imgPic.setBackgroundColor(Color.WHITE);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("magenta")) {
			// imgPic.setBackgroundColor(Color.MAGENTA);
			// } else if (Home.listMainCateModel.get(i).get_Image()
			// .endsWith("cyan")) {
			// imgPic.setBackgroundColor(Color.CYAN);
			// } else {
			// CustomFragmentActivity.imageLoader2.displayImage("file:///"
			// + Home.listMainCateModel.get(i).get_Image(), imgPic,
			// CustomFragmentActivity.options);
			// // File file = new File(itemList.get(position).getItem_image());
			// // holder.row_icon.setImageBitmap(decodeFile(file));
			// }
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(context, R.array.planets_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter
					.createFromResource(context, R.array.size_array,
							android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spTs.setAdapter(adapter1);
			spTs.setOnItemSelectedListener(this);
		} else {
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(context, R.array.planets_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter
					.createFromResource(context, R.array.size_array,
							android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spTs.setAdapter(adapter1);
			spTs.setOnItemSelectedListener(this);
		}

	}

	private void initView(View v) {
		edName = (EditText) v.findViewById(R.id.edName);
		edNameChina = (EditText) v.findViewById(R.id.edNameChina);
		spTs = (Spinner) v.findViewById(R.id.spTs);
		// edDesCN = (EditText) v.findViewById(R.id.edDesCN);
		imgPic = (ImageView) v.findViewById(R.id.imgPic);
		tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		tvnameChina = (TextView) v.findViewById(R.id.tvnameChina);
		// tvDes = (TextView) v.findViewById(R.id.tvDes);
		// tvDesCN = (TextView) v.findViewById(R.id.tvDesCN);
		tvVisible = (TextView) v.findViewById(R.id.tvVisible);
		tvVisible1 = (TextView) v.findViewById(R.id.tvVisible1);
		imgPic1 = (ImageView) v.findViewById(R.id.imgPic1);
		tvNameImage = (TextView) v.findViewById(R.id.tvNameImage);
		tvTitle1 = (TextView) v.findViewById(R.id.tvTitle1);
		spinner = (Spinner) v.findViewById(R.id.spColor);
		acc = (CheckBox) v.findViewById(R.id.checkbox_active);
		inac = (CheckBox) v.findViewById(R.id.checkbox_inavtice);
		rNameChina = (RelativeLayout) v.findViewById(R.id.rNameChina);
		rDesCn = (RelativeLayout) v.findViewById(R.id.rDesCn);
		tvGroupCode = (TextView) v.findViewById(R.id.tvGroupCode);
		edGroupCode = (EditText) v.findViewById(R.id.edGroupCode);
	}

	public static String image = "";
	public static String textsize = "";

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == spinner) {
			imgPic1.bringToFront();
			tvNameImage.setText("");
			if (position == 0) {
				image = "red";
				imgPic1.setBackgroundColor(Color.RED);
			}
			if (position == 1) {
				image = "yellow";
				imgPic1.setBackgroundColor(Color.YELLOW);
			}
			if (position == 2) {
				image = "blue";
				imgPic1.setBackgroundColor(Color.BLUE);
			}
			if (position == 3) {
				image = "green";
				imgPic1.setBackgroundColor(Color.GREEN);
			}
			if (position == 4) {
				image = "black";
				imgPic1.setBackgroundColor(Color.BLACK);
			}
			if (position == 5) {
				image = "white";
				imgPic1.setBackgroundColor(Color.WHITE);
			}
			if (position == 6) {
				image = "magenta";
				imgPic1.setBackgroundColor(Color.MAGENTA);
			}
			if (position == 7) {
				image = "cyan";
				imgPic1.setBackgroundColor(Color.CYAN);
			}
			if (image1 != "") {
				imgPic1.bringToFront();
				tvNameImage.setText("");
				if (image1.equals("red")) {
					imgPic1.setBackgroundColor(Color.RED);
					spinner.setSelection(0);
				} else if (image1.equals("yellow")) {
					imgPic1.setBackgroundColor(Color.YELLOW);
					spinner.setSelection(1);
				} else if (image1.equals("blue")) {
					imgPic1.setBackgroundColor(Color.BLUE);
					spinner.setSelection(2);
				} else if (image1.equals("green")) {
					imgPic1.setBackgroundColor(Color.GREEN);
					spinner.setSelection(3);
				} else if (image1.equals("black")) {
					imgPic1.setBackgroundColor(Color.BLACK);
					spinner.setSelection(4);
				} else if (image1.equals("white")) {
					imgPic1.setBackgroundColor(Color.WHITE);
					spinner.setSelection(5);
				} else if (image1.equals("magenta")) {
					imgPic1.setBackgroundColor(Color.MAGENTA);
					spinner.setSelection(6);
				} else if (image1.equals("cyan")) {
					imgPic1.setBackgroundColor(Color.CYAN);
					spinner.setSelection(7);
				} else {
					PosApp.imageLoader2.displayImage("file:///"
							+ image1, imgPic, CustomFragmentActivity.options);
					try {
						tvNameImage.setText(image1);
						imgPic.bringToFront();
						imgPic1.setBackgroundColor(Color.WHITE);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				image1 = "";
			}
		}
		if (parent == spTs) {
			if (position == 0) {
				textsize = "small";
				edName.setTextAppearance(ac_,
						android.R.style.TextAppearance_Small);
			}
			if (position == 1) {
				textsize = "medium";
				edName.setTextAppearance(ac_,
						android.R.style.TextAppearance_Medium);
			}
			if (position == 2) {
				textsize = "large";
				edName.setTextAppearance(ac_,
						android.R.style.TextAppearance_Large);
			}
			if (textsizeDB != "") {
				if (textsizeDB.equals("small")) {
					spTs.setSelection(0);
				}
				if (textsizeDB.equals("medium")) {
					spTs.setSelection(1);
				}
				if (textsizeDB.equals("large")) {
					spTs.setSelection(2);
				}
				textsizeDB = "";
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// image = image1;

	}
}

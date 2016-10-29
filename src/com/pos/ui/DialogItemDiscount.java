package com.pos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.MainCateAdapter;
import com.pos.common.Utilities;
import com.pos.db.MainCateDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ViewUtils;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;

public class DialogItemDiscount implements OnClickListener {
	private Button btnSeven;
	private Button btnEight;
	private Button btnNine;
	private Button btnFour;
	private Button btnFive;
	private Button btnSix;
	private Button btnOne;
	private Button btnTwo;
	private Button btnThree;
	private Button btnZero;
	private Activity ac;
	private Button btnPercent;
	private Button btnCash;
	private Button btnDot;
	private Button btnDelete;
	private LinearLayout rlAll;
	private EditText edDis;
	private String numRed = "";
	private int isPercent = 0;
	private int pos;
	private double amoutItem;
	DecimalFormat df = new DecimalFormat("0.00");
	private double amount1;

	public void showDialogItemDiscount(final Context context,
			final Activity _ac, int _pos) {
		ac = _ac;
		pos = _pos;
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_ItemDiscount));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.item_discount, null);
		builder.setView(v);
		initView(v);
		initData();

		builder.setNeutralButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						if (isPercent == 1) {
							ListOrderModel md = new ListOrderModel();
							md.setPrice2(PosApp.listOrderData.get(pos)
									.getPrice2());
							md.setSpecialPrice(PosApp.listOrderData.get(pos)
									.getSpecialPrice());
							md.setDiscount(String.valueOf(CalculationUtils
									.calculatePercent(amoutItem, Double
											.parseDouble(edDis.getText()
													.toString()))));
							md.setItemCode(PosApp.listOrderData.get(pos)
									.getItemCode());
							md.setItemName(PosApp.listOrderData.get(pos)
									.getItemName());
							md.setUnitPrice(PosApp.listOrderData.get(pos)
									.getUnitPrice());
							md.setQualyti(PosApp.listOrderData.get(pos)
									.getQualyti());

							md.setPrice3(PosApp.listOrderData.get(pos)
									.getPrice3());
							md.setPrice4(PosApp.listOrderData.get(pos)
									.getPrice4());
							md.setIsGST(PosApp.listOrderData.get(pos)
									.getIsGST());
							md.setIsSVC(PosApp.listOrderData.get(pos)
									.getIsSVC());
							md.setGroupName(PosApp.listOrderData.get(pos)
									.getGroupName());
							md.setIsFOC(PosApp.listOrderData.get(pos)
									.getIsFOC());
							md.setPrice5(PosApp.listOrderData.get(pos)
									.getPrice5());
							md.setGroupCode(PosApp.listOrderData.get(pos)
									.getGroupCode());
							md.setItemTau(PosApp.listOrderData.get(pos)
									.getItemTau());
							md.setSale_detailID(PosApp.listOrderData.get(pos)
									.getSale_detailID());
							md.setStandBy(PosApp.listOrderData.get(pos)
									.getStandBy());
							md.setRemarks(PosApp.listOrderData.get(pos)
									.getRemarks());
							md.setSetmenu(PosApp.listOrderData.get(pos)
									.getSetmenu());
							amount1 = Double.valueOf(amoutItem
									- Double.parseDouble(md.getDiscount()));
							md.setAmount(df.format(amount1) + "");
							md.setId(PosApp.listOrderData.get(pos).getId());
							md.setBill(PosApp.listOrderData.get(pos).getBill());
							md.setTakeaway(PosApp.listOrderData.get(pos).getTakeaway());
							// md.setAmount(String.valueOf(amoutItem
							// - Double.parseDouble(md.getDiscount())));
							PosApp.listOrderData.set(pos, md);
							MainActivity mai = new MainActivity();
							mai.notifidataOrderList();

						} else if (isPercent == 2) {
							ListOrderModel md = new ListOrderModel();
							md.setPrice2(PosApp.listOrderData.get(pos)
									.getPrice2());
							md.setSpecialPrice(PosApp.listOrderData.get(pos)
									.getSpecialPrice());
							md.setDiscount(""
									+ CalculationUtils.round2(Double
											.parseDouble(edDis.getText()
													.toString()), 2));
							md.setItemCode(PosApp.listOrderData.get(pos)
									.getItemCode());
							md.setItemName(PosApp.listOrderData.get(pos)
									.getItemName());
							md.setUnitPrice(PosApp.listOrderData.get(pos)
									.getUnitPrice());
							md.setQualyti(PosApp.listOrderData.get(pos)
									.getQualyti());
							md.setPrice3(PosApp.listOrderData.get(pos)
									.getPrice3());
							md.setPrice4(PosApp.listOrderData.get(pos)
									.getPrice4());
							md.setIsGST(PosApp.listOrderData.get(pos)
									.getIsGST());
							md.setIsSVC(PosApp.listOrderData.get(pos)
									.getIsSVC());
							md.setGroupName(PosApp.listOrderData.get(pos)
									.getGroupName());
							md.setIsFOC(PosApp.listOrderData.get(pos)
									.getIsFOC());
							md.setPrice5(PosApp.listOrderData.get(pos)
									.getPrice5());
							md.setGroupCode(PosApp.listOrderData.get(pos)
									.getGroupCode());
							md.setItemTau(PosApp.listOrderData.get(pos)
									.getItemTau());
							md.setSale_detailID(PosApp.listOrderData.get(pos)
									.getSale_detailID());
							md.setStandBy(PosApp.listOrderData.get(pos)
									.getStandBy());
							md.setRemarks(PosApp.listOrderData.get(pos)
									.getRemarks());
							md.setSetmenu(PosApp.listOrderData.get(pos)
									.getSetmenu());
							amount1 = Double.valueOf(amoutItem
									- Double.parseDouble(md.getDiscount()));
							md.setAmount(df.format(amount1) + "");
							md.setId(PosApp.listOrderData.get(pos).getId());
							md.setBill(PosApp.listOrderData.get(pos).getBill());
							md.setTakeaway(PosApp.listOrderData.get(pos).getTakeaway());
							// md.setAmount(String.valueOf(amoutItem
							// - Double.parseDouble(md.getDiscount())));
							PosApp.listOrderData.set(pos, md);
							MainActivity mai = new MainActivity();
							mai.notifidataOrderList();
						} else {
							dialog.dismiss();
						}

					}
				});

		builder.setPositiveButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

	private void initData() {
		((CustomFragmentActivity) ac).setHeight(rlAll, 2);
		ViewUtils.createEffectTouch(btnPercent, R.drawable.discount_dollar,
				R.drawable.discount_dollar_nguoc);
		ViewUtils.createEffectTouch(btnCash, R.drawable.discount_dollar,
				R.drawable.discount_dollar_nguoc);
		btnPercent.setOnClickListener(this);
		btnCash.setOnClickListener(this);
		btnSeven.setOnClickListener(this);
		btnOne.setOnClickListener(this);
		btnTwo.setOnClickListener(this);
		btnThree.setOnClickListener(this);
		btnFour.setOnClickListener(this);
		btnFive.setOnClickListener(this);
		btnSix.setOnClickListener(this);
		btnEight.setOnClickListener(this);
		btnNine.setOnClickListener(this);
		btnZero.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnDot.setOnClickListener(this);

		amoutItem = Double.parseDouble(PosApp.listOrderData.get(pos)
				.getUnitPrice())
				* Double.parseDouble(PosApp.listOrderData.get(pos).getQualyti());

	}

	private void initView(View v) {
		rlAll = (LinearLayout) v.findViewById(R.id.rlAll);
		edDis = (EditText) v.findViewById(R.id.edDis);
		btnSeven = (Button) v.findViewById(R.id.btnSeven);
		btnPercent = (Button) v.findViewById(R.id.btnPercent);
		btnCash = (Button) v.findViewById(R.id.btnCash);
		btnOne = (Button) v.findViewById(R.id.btnOne);
		btnTwo = (Button) v.findViewById(R.id.btnTwo);
		btnThree = (Button) v.findViewById(R.id.btnThree);
		btnFour = (Button) v.findViewById(R.id.btnFour);
		btnFive = (Button) v.findViewById(R.id.btnFive);
		btnSix = (Button) v.findViewById(R.id.btnSix);
		btnEight = (Button) v.findViewById(R.id.btnEight);
		btnNine = (Button) v.findViewById(R.id.btnNine);
		btnZero = (Button) v.findViewById(R.id.btnZero);
		btnDelete = (Button) v.findViewById(R.id.btnDelete);
		btnDot = (Button) v.findViewById(R.id.btnDot);

	}

	@Override
	public void onClick(View v) {
		if (v == btnPercent) {
			isPercent = 1;
			// btnPercent.setEnabled(false);
			// btnCash.setEnabled(true);
			btnPercent.setTextColor(Color.RED);
			btnCash.setTextColor(Color.BLACK);
			edDis.setText("");
		}
		if (v == btnCash) {
			isPercent = 2;
			// btnCash.setEnabled(false);
			// btnPercent.setEnabled(true);
			btnPercent.setTextColor(Color.BLACK);
			btnCash.setTextColor(Color.RED);
			edDis.setText("");
		}
		if (isPercent == 0) {
			Toast.makeText(ac, "Choosen Mode", Toast.LENGTH_SHORT).show();
		} else {
			if (v == btnDot) {
				if (numRed.equals("")) {
					numRed = numRed + "0.";
				} else {
					numRed = numRed + ".";
				}

			}
			if (v == btnDelete) {
				edDis.setText("");
				numRed = "";

			}
			if (v == btnZero) {
				numRed = numRed + "0";
				edDis.setText(numRed + "");
			}
			if (v == btnNine) {
				numRed = numRed + "9";
				edDis.setText(numRed + "");
			}
			if (v == btnEight) {
				numRed = numRed + "8";
				edDis.setText(numRed + "");
			}
			if (v == btnSix) {
				numRed = numRed + "6";
				edDis.setText(numRed + "");
			}
			if (v == btnFive) {
				numRed = numRed + "5";
				edDis.setText(numRed + "");
			}
			if (v == btnFour) {
				numRed = numRed + "4";
				edDis.setText(numRed + "");
			}
			if (v == btnThree) {
				numRed = numRed + "3";
				edDis.setText(numRed + "");
			}
			if (v == btnTwo) {
				numRed = numRed + "2";
				edDis.setText(numRed + "");
			}

			if (v == btnOne) {
				numRed = numRed + "1";
				edDis.setText(numRed + "");
			}
			if (v == btnSeven) {
				numRed = numRed + "7";
				edDis.setText(numRed + "");
			}

		}

	}
}

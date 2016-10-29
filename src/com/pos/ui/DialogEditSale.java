package com.pos.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.db.ItemsDataSource;
import com.pos.db.MainCateDataSource;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;

public class DialogEditSale {
	private EditText edItemCode;
	private EditText edQty;
	private EditText edItemname;
	private EditText edUnitPrice;
	private EditText edTotal;
	private int pos;
	private double amount1;

	public void showDialogEditSale(final Context context, final Activity ac,
			int _pos) {
		pos = _pos;
		Builder builder = new Builder(context);
		builder.setTitle("Edit Sale");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.edit_sale, null);
		builder.setView(v);
		initView(v);
		initData();

		builder.setNeutralButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {

						ListOrderModel md = new ListOrderModel();
						md.setPrice2(PosApp.listOrderData.get(pos).getPrice2());
						md.setSpecialPrice(PosApp.listOrderData.get(pos)
								.getSpecialPrice());
						md.setDiscount(PosApp.listOrderData.get(pos)
								.getDiscount());
						md.setItemCode(PosApp.listOrderData.get(pos)
								.getItemCode());
						md.setItemName(PosApp.listOrderData.get(pos)
								.getItemName());
						md.setUnitPrice(edUnitPrice.getText().toString());
						md.setQualyti(edQty.getText().toString());
						amount1 = Double.valueOf(Double.parseDouble(md
								.getUnitPrice())
								* Double.parseDouble(md.getQualyti())
								- Double.parseDouble(md.getDiscount()));
						DecimalFormat df = new DecimalFormat("0.00");
						amount1 = Double.valueOf(amount1);
						md.setAmount(df.format(amount1) + "");
						md.setPrice3(PosApp.listOrderData.get(pos).getPrice3());
						md.setPrice4(PosApp.listOrderData.get(pos).getPrice4());
						md.setPrice5(PosApp.listOrderData.get(pos).getPrice5());
						md.setIsGST(PosApp.listOrderData.get(pos).getIsGST());
						md.setIsSVC(PosApp.listOrderData.get(pos).getIsSVC());
						md.setGroupName(PosApp.listOrderData.get(pos)
								.getGroupName());
						md.setIsFOC(PosApp.listOrderData.get(pos).getIsFOC());
						md.setStandBy(PosApp.listOrderData.get(pos)
								.getStandBy());
						md.setRemarks(PosApp.listOrderData.get(pos)
								.getRemarks());
						md.setGroupCode(PosApp.listOrderData.get(pos)
								.getGroupCode());
						md.setItemTau(PosApp.listOrderData.get(pos)
								.getItemTau());
						md.setSale_detailID(PosApp.listOrderData.get(pos)
								.getSale_detailID());
						md.setSetmenu(PosApp.listOrderData.get(pos)
								.getSetmenu());
						md.setId(PosApp.listOrderData.get(pos).getId());
						md.setBill(PosApp.listOrderData.get(pos).getBill());
						md.setTakeaway(PosApp.listOrderData.get(pos).getTakeaway());
						PosApp.listOrderData.set(pos, md);
						MainActivity main = new MainActivity();
						main.notifidataOrderList();

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
		edItemCode.setText(PosApp.listOrderData.get(pos).getItemCode());
		edQty.setText(PosApp.listOrderData.get(pos).getQualyti());
		edItemname.setText(PosApp.listOrderData.get(pos).getItemName());
		edUnitPrice.setText(PosApp.listOrderData.get(pos).getUnitPrice());
		edTotal.setText(PosApp.listOrderData.get(pos).getAmount());

	}

	private void initView(View v) {
		edItemCode = (EditText) v.findViewById(R.id.edItemCode);
		edQty = (EditText) v.findViewById(R.id.edQty);
		edItemname = (EditText) v.findViewById(R.id.edItemname);
		edUnitPrice = (EditText) v.findViewById(R.id.edUnitPrice);
		edTotal = (EditText) v.findViewById(R.id.edTotal);

	}

}

package com.pos.ui;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.security.auth.PrivateCredentialPermission;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.model.ListOrderModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

@SuppressLint("InflateParams")
public class DialogCustomItem {

	private RelativeLayout lDefineCustom;
	private EditText edproductname;
	private EditText edproductquality;
	private EditText edunitprice;

	public void showDialogDefineCustomItem(final Context context,
			final Activity ac) {
		Builder builder = new Builder(context);
		builder.setTitle("Define Custom Item");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.define_custom_item, null);
		builder.setView(v);
		lDefineCustom = (RelativeLayout) v.findViewById(R.id.lDefineCustom);
		// ((CustomFragmentActivity)ac).setWidthHeight(lDefineCustom, 1, 2);

		edproductname = (EditText) v.findViewById(R.id.edProducName);
		edproductquality = (EditText) v.findViewById(R.id.edProducQuality);
		edunitprice = (EditText) v.findViewById(R.id.edUnitPrice);

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!TextUtils
								.isEmpty(edunitprice.getText().toString())
								&& !TextUtils.isEmpty(edproductquality
										.getText().toString())
								&& !TextUtils.isEmpty(edproductname.getText()
										.toString())) {
							ListOrderModel md = new ListOrderModel();
							String amount;
							amount = String.valueOf(Double
									.parseDouble(edunitprice.getText()
											.toString())
									* Double.parseDouble(edproductquality
											.getText().toString()));
							md.setQualyti(edproductquality.getText().toString());
							md.setAmount(amount);
							md.setDiscount("0");
							md.setItemCode("Custom:"+nextSessionId());
							md.setItemName(edproductname.getText().toString());
							md.setPrice2("0");
							md.setSpecialPrice("0");
							md.setUnitPrice(edunitprice.getText().toString());
							PosApp.listOrderData.add(md);
							MainActivity main = new MainActivity();
							main.notifidataOrderList();
							MainActivity.qly = -1;
						}
					}
				});
		builder.setNegativeButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(20, random).toString(32);
	}

}

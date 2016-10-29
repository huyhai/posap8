package com.pos.ui;

import java.text.DecimalFormat;

import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.libs.CalculationUtils;
import com.pos.libs.SessionManager;
import com.pos.model.SplitBillModel;
import com.pos.model.SplitOrderModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DialogSelectQuantity {

	private EditText ednote;
	private Activity ac;
	double amount1;

	public void showNoteDialog(final Context context, final Activity _ac) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle("Select Quantity");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.selectquantity, null);
		builder.setView(v);
		ednote = (EditText) v.findViewById(R.id.edNote);

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (ednote.getText().toString().equals("")
								|| Integer
										.parseInt(ednote.getText().toString()) < 0) {
							ednote.setError("error");
						} else {
							// SplitBillActivity.listBill.clear();
							Utils.splitUpdateListOrder(ac, MainActivity.qly,
									ednote.getText().toString());
							for (int i = 2; i <= SplitBillActivity.listBill
									.size(); i++) {
								for (int j = 0; j < PosApp.listOrderSplit
										.size(); j++) {
									if (PosApp.listOrderSplit.get(j).getBill()
											.equals(String.valueOf(i))) {
										SplitBillModel md = new SplitBillModel();
										md.setBill("Bill");
										double subtotal = 0;
										subtotal = CalculationUtils
												.calculateSubTotalSplit(
														PosApp.listOrderSplit,
														String.valueOf(i));
										double svc = 0;
										svc = CalculationUtils
												.calculateServiceChargeSplit(
														PosApp.listOrderSplit,
														String.valueOf(i));
										double gst = 0;
										gst = CalculationUtils
												.calculateGSTChargeSplit(
														PosApp.listOrderSplit,
														String.valueOf(i));
										String tong = Utils
												.lamtronTong(subtotal + svc
														+ gst);
										md.setTotalAmount(tong);
										Log.v("HAI", "tog " + tong);
										Log.v("HAI", "tog " + (i - 1));
										SplitBillActivity.listBill.set(i - 1,
												md);
									}
								}

							}

							SplitBillModel md = new SplitBillModel();
							md.setBill("Bill");
							double subtotal = CalculationUtils
									.calculateSubTotal(PosApp.listOrderData);
							double svc = CalculationUtils
									.calculateServiceCharge(PosApp.listOrderData);
							double gst = CalculationUtils
									.calculateGSTCharge(PosApp.listOrderData);
							String tong = Utils.lamtronTong(subtotal + svc
									+ gst);
							md.setTotalAmount(tong);
							SplitBillActivity.listBill.set(0, md);

							SplitBillActivity.splitAdapter
									.notifyDataSetChanged();

						}

					}
				});
		builder.setNeutralButton(ac.getString(R.string.str_cancel),
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
}

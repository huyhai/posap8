package com.pos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.ResendKitchenAdapter;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.print.PrinterFunctions;

public class DialogResendKitchen {
	private ListView lvResend;
	private static Activity ac;
	public static int a = -1;
	static double amount1 = 0;
	private ResendKitchenAdapter dataAdapter;

	public void showDialogEditSale(final Context context, final Activity _ac) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle("Resend Kitchen");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.resendkitchenl, null);
		builder.setView(v);
		initView(v);
		initData();

		builder.setPositiveButton("Resend",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						PosApp.listOrderData2.add(PosApp.listOrderData
								.get(MainActivity.qly));
						PrinterFunctions.PrintPosOne(MainActivity.activity,
								MainActivity.activity, "USB:", "",
								MainActivity.activity.getResources(), "", 0);

						PrinterFunctions.PrintPosOne(MainActivity.activity,
								MainActivity.activity, "TCP:192.168.1.204", "",
								MainActivity.activity.getResources(), "", 0);

						PrinterFunctions.PrintPosOne(MainActivity.activity,
								MainActivity.activity, "TCP:192.168.1.209", "",
								MainActivity.activity.getResources(), "", 0);
						callAdv();
						for (int j = 0; j < UserFunctions.getInstance()
								.getlistMayIn().size(); j++) {
							if (PosApp.listOrderData
									.get(MainActivity.qly)
									.getGroupCode()
									.equals(UserFunctions.getInstance()
											.getlistMayIn().get(j)
											.getItem_group())) {

								if (PosApp.listOrderData.get(MainActivity.qly)
										.getStandBy().equals("0")) {
									int one = Integer.parseInt(UserFunctions
											.getInstance().getlistMayIn()
											.get(j).getPrinter_one());
									getIp1(one);
									PrinterFunctions.PrintPosOne(
											MainActivity.activity,
											MainActivity.activity,
											"TCP:" + ip1, "",
											MainActivity.activity
													.getResources(), "", 0);
								}
							
							}

						}
						PosApp.listOrderData2.remove(0);
						Utilities.showDialogNoBack(ac, "Order have been sent").show();
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
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateGram"));
		params.add(new BasicNameValuePair("saleDetailID", PosApp.listOrderData.get(MainActivity.qly).getSale_detailID()));
		params.add(new BasicNameValuePair("gram", PosApp.listOrderData.get(MainActivity.qly).getRemarks()));
		params.add(new BasicNameValuePair("sell", PosApp.listOrderData.get(MainActivity.qly).getUnitPrice()));
		params.add(new BasicNameValuePair("subtotal", PosApp.listOrderData.get(MainActivity.qly).getAmount()));
		UserFunctions.getInstance().deleteItem(ac, params, false);
	}

	private String ip1;

	private void getIp1(int two) {
		for (int i = 0; i < UserFunctions.getInstance().getlistIPMayIn().size(); i++) {
			if (two == Integer.parseInt(UserFunctions.getInstance()
					.getlistIPMayIn().get(i).getId())) {
				ip1 = UserFunctions.getInstance().getlistIPMayIn().get(i)
						.getIp_address();
				break;
			}
		}

	}

	public void initData() {
		// ((CustomFragmentActivity) ac).setWidth(rlRecieptNo, 7);
		// ((CustomFragmentActivity) ac).setWidth(rlDateTime, 5);
		// adapter = new SearchBillAdapter(ac, list);
		// lvSearch.setAdapter(adapter);
		dataAdapter = new ResendKitchenAdapter(ac, PosApp.listOrderData);
		lvResend.setAdapter(dataAdapter);
		// notifi();

	}

	// public void notifi() {
	// if (list != null)
	// list.clear();
	// SearchBillDataSource dataSource = new SearchBillDataSource(ac, ac);
	// list = dataSource.loadItems();
	// adapter.setItemList(list);
	// adapter.notifyDataSetChanged();
	// }

	private void initView(View v) {
		lvResend = (ListView) v.findViewById(R.id.lvResend);
		// rlRecieptNo = (RelativeLayout) v.findViewById(R.id.rlRecieptNo);
		// rlDateTime = (RelativeLayout) v.findViewById(R.id.rlDateTime);

	}

}

package com.pos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.SessionManager;
import com.pos.model.ListOrderModel;
import com.pos.print.PrinterFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DialogSelectQtyDelete {

	private RelativeLayout lnote;
	private EditText ednote;
	private Activity ac;
	public static String numDelete = "";
	private int qtyReal;

	public void showNoteDialog(final Context context, final Activity _ac,
			final String quantity, final int pos) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle("Select Quantity");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.numprint, null);
		builder.setView(v);
		lnote = (RelativeLayout) v.findViewById(R.id.lnote);
		ednote = (EditText) v.findViewById(R.id.edNote);
		ednote.setText("1");
		// ((CustomFragmentActivity) ac).setWidthHeight(lnote, 1, 2);

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int qt = Integer.parseInt(quantity);
						int inputqt = Integer.parseInt(ednote.getText()
								.toString());
						if (qt <= inputqt) {
							try {
								qtyReal = 0;
								if (Utilities.getGlobalVariable(ac).isHaveSendOrder) {
									numDelete = String.valueOf(inputqt);
									Utilities.getGlobalVariable(ac).isDelete = true;
									List<NameValuePair> params = new ArrayList<NameValuePair>();
									params.add(new BasicNameValuePair("tag",
											"deleteItem"));
									params.add(new BasicNameValuePair(
											"detailID", PosApp.listOrderData
													.get(pos)
													.getSale_detailID()));
									params.add(new BasicNameValuePair("qty",
											qtyReal + ""));
									UserFunctions.getInstance().deleteItem(ac,
											params, false);
									PosApp.listOrderData2
											.add(PosApp.listOrderData.get(pos));
									PrinterFunctions.PrintPosOne(
											MainActivity.activity,
											MainActivity.activity, "USB:", "",
											MainActivity.activity
													.getResources(), "", 0);
									PrinterFunctions.PrintPosOne(
											MainActivity.activity,
											MainActivity.activity,
											"TCP:192.168.1.204", "",
											MainActivity.activity
													.getResources(), "", 0);
									PrinterFunctions.PrintPosOne(
											MainActivity.activity,
											MainActivity.activity,
											"TCP:192.168.1.209", "",
											MainActivity.activity
													.getResources(), "", 0);
									for (int j = 0; j < UserFunctions
											.getInstance().getlistMayIn()
											.size(); j++) {

										if (PosApp.listOrderData
												.get(pos)
												.getGroupCode()
												.equals(UserFunctions
														.getInstance()
														.getlistMayIn().get(j)
														.getItem_group())) {

											// if (PosApp.listOrderData.get(pos)
											// .getStandBy().equals("0")) {
											int one = Integer
													.parseInt(UserFunctions
															.getInstance()
															.getlistMayIn()
															.get(j)
															.getPrinter_one());
											getIp1(one);
											PrinterFunctions.PrintPosOne(
													MainActivity.activity,
													MainActivity.activity,
													"TCP:" + ip1, "",
													MainActivity.activity
															.getResources(),
													"", 0);

											// }
										}

									}

								}

								PosApp.listOrderData.remove(pos);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// Toast.makeText(ac, "Cannot", Toast.LENGTH_SHORT)
							// .show();
						} else {
							numDelete = String.valueOf(inputqt);
							qtyReal = (qt - inputqt);
							ListOrderModel md = new ListOrderModel();
							md.setQualyti("" + qtyReal);
							md.setAmount(PosApp.listOrderData.get(pos)
									.getAmount());
							md.setDiscount(PosApp.listOrderData.get(pos)
									.getDiscount());
							md.setItemCode(PosApp.listOrderData.get(pos)
									.getItemCode());
							md.setItemName(PosApp.listOrderData.get(pos)
									.getItemName());
							md.setPrice2(PosApp.listOrderData.get(pos)
									.getPrice2());
							md.setSpecialPrice(PosApp.listOrderData.get(pos)
									.getSpecialPrice());
							md.setUnitPrice(PosApp.listOrderData.get(pos)
									.getUnitPrice());
							md.setIsFOC(PosApp.listOrderData.get(pos)
									.getIsFOC());
							md.setPrice3(PosApp.listOrderData.get(pos)
									.getPrice3());
							md.setPrice4(PosApp.listOrderData.get(pos)
									.getPrice4());
							md.setPrice5(PosApp.listOrderData.get(pos)
									.getPrice5());
							md.setIsGST(PosApp.listOrderData.get(pos)
									.getIsGST());
							md.setIsSVC(PosApp.listOrderData.get(pos)
									.getIsSVC());
							md.setGroupName(PosApp.listOrderData.get(pos)
									.getGroupName());
							md.setGroupCode(PosApp.listOrderData.get(pos)
									.getGroupCode());
							md.setStandBy(PosApp.listOrderData.get(pos)
									.getStandBy());
							md.setRemarks(PosApp.listOrderData.get(pos)
									.getRemarks());
							md.setSetmenu(PosApp.listOrderData.get(pos)
									.getSetmenu());
							md.setItemTau(PosApp.listOrderData.get(pos)
									.getItemTau());
							md.setSale_detailID(PosApp.listOrderData.get(pos)
									.getSale_detailID());
							md.setBill(PosApp.listOrderData.get(pos).getBill());
							md.setId(PosApp.listOrderData.get(pos).getId());
							md.setTakeaway(PosApp.listOrderData.get(pos)
									.getTakeaway());
							PosApp.listOrderData.set(pos, md);
							if (Utilities.getGlobalVariable(ac).isHaveSendOrder) {

								Utilities.getGlobalVariable(ac).isDelete = true;
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("tag",
										"deleteItem"));
								params.add(new BasicNameValuePair("detailID",
										PosApp.listOrderData.get(pos)
												.getSale_detailID()));
								params.add(new BasicNameValuePair("qty",
										qtyReal + ""));
								UserFunctions.getInstance().deleteItem(ac,
										params, false);
								PosApp.listOrderData2.add(PosApp.listOrderData
										.get(pos));
								PrinterFunctions.PrintPosOne(
										MainActivity.activity,
										MainActivity.activity, "USB:", "",
										MainActivity.activity.getResources(),
										"", 0);

								PrinterFunctions.PrintPosOne(
										MainActivity.activity,
										MainActivity.activity,
										"TCP:192.168.1.204", "",
										MainActivity.activity.getResources(),
										"", 0);
								PrinterFunctions.PrintPosOne(
										MainActivity.activity,
										MainActivity.activity,
										"TCP:192.168.1.209", "",
										MainActivity.activity.getResources(),
										"", 0);
								for (int j = 0; j < UserFunctions.getInstance()
										.getlistMayIn().size(); j++) {
									if (PosApp.listOrderData
											.get(pos)
											.getGroupCode()
											.equals(UserFunctions.getInstance()
													.getlistMayIn().get(j)
													.getItem_group())) {

										// if (PosApp.listOrderData.get(pos)
										// .getStandBy().equals("0")) {
										int one = Integer
												.parseInt(UserFunctions
														.getInstance()
														.getlistMayIn().get(j)
														.getPrinter_one());
										getIp1(one);
										PrinterFunctions.PrintPosOne(
												MainActivity.activity,
												MainActivity.activity, "TCP:"
														+ ip1, "",
												MainActivity.activity
														.getResources(), "", 0);

										// }
									}

								}

							}
						}
						Utilities.getGlobalVariable(ac).isDelete = false;
						PosApp.listOrderData2.clear();
						MainActivity main = new MainActivity();
						main.notifidataOrderList();
						Utilities.showDialogNoBack(ac, "Order have been sent").show();
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

}

package com.pos.common;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.ListOrderAdapter;
import com.pos.adapter.SplitBillAdapter;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.db.PayDataSource;
import com.pos.libs.ComDDUtilities;
import com.pos.model.ListOrderModel;
import com.pos.model.PayModel;
import com.pos.model.SplitOrderModel;
import com.pos.ui.DialogBillDiscount;
import com.pos.ui.Items;
import com.pos.ui.SplitBillActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.serialport.SerialPort;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Utils {
	static double amount1 = 0;
	private static Application mApplication;
	private static SerialPort mSerialPort_V;
	private static OutputStream mOutputStream_V;
	static MainActivity main = new MainActivity();

	public static void showToast(String a, Activity ac) {
		LayoutInflater inflater = ac.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) ac.findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(a);

		final Toast toast = new Toast(ac);
		toast.setGravity(Gravity.LEFT | Gravity.TOP, 210, 310);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		}, 200);

	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String getDeviceID(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	static int check = 0;

	public static void addItemToList(String price1, String itemCode,
			String name, String price2, String specialprice, String price3,
			String price4, String isGST, String isSVC, String groupCode,
			String groupID, String price5, String remarks, String setmenu,
			String itemTau, String sale_detailID, String bill, String itemID,
			String takeaway) {

		if (!Utilities.getGlobalVariable(MainActivity.activity).isPrintBill) {

		check = 0;
		checkItemExits(itemCode);
		if (check == 0) {

				ListOrderModel md = new ListOrderModel();
				String amount;
				if (Items.num != "") {
					amount = String.valueOf(Double.parseDouble(price1)
							* Double.parseDouble(Items.num));
					md.setQualyti(Items.num);
				} else {
					amount = String.valueOf(Double.parseDouble(price1) * 1);
					md.setQualyti("1");
				}
				DecimalFormat df = new DecimalFormat("0.00");
				amount1 = Double.valueOf(amount);
				md.setAmount(df.format(amount1) + "");
				md.setDiscount("0.00");
				md.setItemCode(itemCode);
				md.setItemName(name);
				md.setPrice2(price2);
				md.setSpecialPrice(specialprice);
				md.setUnitPrice(price1);
				md.setIsFOC("0");
				md.setPrice3(price3);
				md.setPrice4(price4);
				md.setPrice5(price5);
				md.setIsGST(isGST);
				md.setIsSVC(isSVC);
				md.setGroupName(groupCode);
				md.setGroupCode(groupID);
				md.setStandBy("0");
				md.setRemarks(remarks);
				md.setSetmenu(setmenu);
				md.setItemTau(itemTau);
				md.setSale_detailID(sale_detailID);
				md.setBill(bill);
				md.setId(itemID);
				md.setTakeaway(takeaway);
				PosApp.listOrderData.add(md);
				// PosApp.listOrderData2.add(md);
			}
			main.notifidataOrderList();
			// Items.num = "";
			MainActivity.qly = -1;
			showPFDShowItems(name, price1);
		} else {
			Utilities.showDialogNoBack(MainActivity.activity, "Not Allowed").show();
		}
	}

	public static void checkItemExits(String itemCode) {
		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			if (itemCode.equals(PosApp.listOrderData.get(i).getItemCode())) {
				ListOrderModel md = new ListOrderModel();
				String amount;
				if (Items.num != "") {
					int qlt = Integer.parseInt(PosApp.listOrderData.get(i)
							.getQualyti()) + Integer.parseInt(Items.num);
					amount = String.valueOf(Double
							.parseDouble(PosApp.listOrderData.get(i)
									.getUnitPrice())
							* qlt);

					md.setQualyti(qlt + "");
				} else {
					int qlt = Integer.parseInt(PosApp.listOrderData.get(i)
							.getQualyti()) + 1;
					amount = String.valueOf(Double
							.parseDouble(PosApp.listOrderData.get(i)
									.getUnitPrice())
							* qlt);

					md.setQualyti(qlt + "");
				}
				DecimalFormat df = new DecimalFormat("0.00");
				amount1 = Double.valueOf(amount);
				md.setAmount(df.format(amount1) + "");
				md.setDiscount("0.00");
				md.setItemCode(PosApp.listOrderData.get(i).getItemCode());
				md.setItemName(PosApp.listOrderData.get(i).getItemName());
				md.setPrice2(PosApp.listOrderData.get(i).getPrice2());
				md.setSpecialPrice(PosApp.listOrderData.get(i)
						.getSpecialPrice());
				md.setUnitPrice(PosApp.listOrderData.get(i).getUnitPrice());
				md.setIsFOC("0");
				md.setPrice3(PosApp.listOrderData.get(i).getPrice3());
				md.setPrice4(PosApp.listOrderData.get(i).getPrice4());
				md.setPrice5(PosApp.listOrderData.get(i).getPrice5());
				md.setIsGST(PosApp.listOrderData.get(i).getIsGST());
				md.setIsSVC(PosApp.listOrderData.get(i).getIsSVC());
				md.setGroupName(PosApp.listOrderData.get(i).getGroupName());
				md.setGroupCode(PosApp.listOrderData.get(i).getGroupCode());
				md.setStandBy(PosApp.listOrderData.get(i).getStandBy());
				md.setRemarks(PosApp.listOrderData.get(i).getRemarks());
				md.setSetmenu(PosApp.listOrderData.get(i).getSetmenu());
				md.setItemTau(PosApp.listOrderData.get(i).getItemTau());
				md.setSale_detailID(PosApp.listOrderData.get(i)
						.getSale_detailID());
				md.setBill(PosApp.listOrderData.get(i).getBill());
				md.setId(PosApp.listOrderData.get(i).getId());
				md.setTakeaway(PosApp.listOrderData.get(i).getTakeaway());
				PosApp.listOrderData.set(i, md);
				check = 1;
				break;
			} else {
				check = 0;
			}
		}
	}

	public static void addItemFOC(String itemCode, String name, String price2,
			String specialPrice, Activity ac, String price3, String price4,
			String isGST, String isSVC, String groupCode, String groupID,
			String price5, String remarks, String setmenu, String itemTau,
			String sale_detailID, String bill, String itemID, String takeaway) {
		if (!Utilities.getGlobalVariable(MainActivity.activity).isPrintBill) {
			ListOrderModel md = new ListOrderModel();
			if (Items.num != "") {
				md.setQualyti(Items.num);
			} else {
				md.setQualyti("1");
			}

			md.setAmount("0");
			md.setDiscount("0");
			md.setItemCode(itemCode);
			md.setItemName(name + "(FOC)");
			md.setPrice2(price2);
			md.setSpecialPrice(specialPrice);
			md.setUnitPrice("0.00");
			md.setPrice3(price3);
			md.setPrice4(price4);
			md.setPrice5(price5);
			md.setIsGST(isGST);
			md.setIsSVC(isSVC);
			md.setIsFOC("1");
			md.setGroupName(groupCode);
			md.setGroupCode(groupID);
			md.setStandBy("0");
			md.setRemarks(remarks);
			md.setSetmenu(setmenu);
			md.setItemTau(itemTau);
			md.setSale_detailID(sale_detailID);
			md.setBill(bill);
			md.setId(itemID);
			md.setTakeaway(takeaway);
			PosApp.listOrderData.add(md);
			// PosApp.listOrderData2.add(md);

			main.notifidataOrderList();
			// Items.num = "";
			MainActivity.qly = -1;
			Utilities.getGlobalVariable(ac).isPOC = false;
			showPFDShowItems(name, "0.00");
		} else {
			Utilities.showDialogNoBack(MainActivity.activity, "Not Allowed").show();
		}
	}

	public static void showPFDShowItems(String name, String price1) {
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

			String itemName = name;
			String spaceName = "";
			if (itemName.length() > 20) {
				itemName = itemName.substring(0, 20);

			} else {
				int tempvar = 20 - itemName.length();
				for (int i = 0; i < tempvar; i++) {
					spaceName += " ";

				}

			}
			int changequantity;
			double amount;
			mOutputStream_V.write(itemName.getBytes());
			mOutputStream_V.write(spaceName.getBytes());
			String space = "      ";
			// mOutputStream_V.write(space.getBytes());
			if (Items.num != "") {
				changequantity = Integer.parseInt(Items.num);
				amount = Double.parseDouble(price1)
						* Double.parseDouble(Items.num);
			} else {
				changequantity = Integer.parseInt("1");
				amount = Double.parseDouble(price1) * Double.parseDouble("1");
			}

			int len1 = price1.toString().length();
			String strtemp = "" + amount;
			int len2 = strtemp.length();
			double spaceneed = 14 - (len1 + len2);
			// mOutputStream_V.write(space.getBytes());
			for (int i = 0; i < spaceneed; i++) {
				space += " ";
			}
			DecimalFormat df = new DecimalFormat("0.00");
			String am = df.format(amount1);
			String quantity = "" + changequantity;
			String amountc = "" + am;
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
	}

	public static String lamtronTong(double total) {
		String stemp = String.format("%.2f", total);
		String stemp1 = stemp.substring(stemp.length() - 2, stemp.length());
		String stemp2 = stemp.substring(0, stemp.length() - 3);
		int i1 = -1;
		try {
			i1 = Integer.parseInt(stemp2);
		} catch (Exception e) {
			// TODO: handle exception
		}

		int i2 = -1;
		try {
			i2 = Integer.parseInt(stemp1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if ((i2 > 0) & (i2 <= 5)) {
			i2 = 5;
		}
		if ((i2 > 5) & (i2 <= 10)) {
			i2 = 10;
		}
		if ((i2 > 10) & (i2 <= 15)) {
			i2 = 15;
		}
		if ((i2 > 15) & (i2 <= 20)) {
			i2 = 20;
		}
		if ((i2 > 20) & (i2 <= 25)) {
			i2 = 25;
		}
		if ((i2 > 25) & (i2 <= 30)) {
			i2 = 30;
		}
		if ((i2 > 30) & (i2 <= 35)) {
			i2 = 35;
		}
		if ((i2 > 35) & (i2 <= 40)) {
			i2 = 40;
		}
		if ((i2 > 40) & (i2 <= 45)) {
			i2 = 45;
		}
		if ((i2 > 45) & (i2 <= 50)) {
			i2 = 50;
		}
		if ((i2 > 50) & (i2 <= 55)) {
			i2 = 55;
		}
		if ((i2 > 55) & (i2 <= 60)) {
			i2 = 60;
		}
		if ((i2 > 60) & (i2 <= 65)) {
			i2 = 65;
		}
		if ((i2 > 65) & (i2 <= 70)) {
			i2 = 70;
		}
		if ((i2 > 70) & (i2 <= 75)) {
			i2 = 75;
		}
		if ((i2 > 75) & (i2 <= 80)) {
			i2 = 80;
		}
		if ((i2 > 80) & (i2 <= 85)) {
			i2 = 85;
		}
		if ((i2 > 85) & (i2 <= 90)) {
			i2 = 90;
		}
		if ((i2 > 90) & (i2 <= 95)) {
			i2 = 95;
		}
		if ((i2 > 95) & (i2 <= 99)) {
			i1++;
			i2 = 0;
		}
		if (i2 == 0) {
			return ("" + i1 + "." + i2 + "0");
		} else {
			if (i2 == 5) {
				return ("" + i1 + ".0" + i2);
			} else {
				return ("" + i1 + "." + i2);
			}

		}

		// if (i2 == 0) {
		// tvTotalAmount.setText("" + i1 + "." + i2 + "0");
		// } else {
		// tvTotalAmount.setText("" + i1 + "." + i2);
		// }
		// if (i2 == 5) {
		// tvTotalAmount.setText("" + i1 + ".0" + i2);
		// }

	}

	public static void updateItem(Activity activity, int qly, String content,
			String stanby, String price,String isStr) {
		try {

			ListOrderModel md = new ListOrderModel();
			md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
			md.setSpecialPrice(PosApp.listOrderData.get(qly).getSpecialPrice());
			md.setDiscount(PosApp.listOrderData.get(qly).getDiscount());
			md.setItemCode(PosApp.listOrderData.get(qly).getItemCode());
			md.setItemName(PosApp.listOrderData.get(qly).getItemName());
			md.setUnitPrice(price);

			md.setQualyti(PosApp.listOrderData.get(qly).getQualyti());
			amount1 = Double.valueOf(Double.parseDouble(price)
					* Double.parseDouble(md.getQualyti())
					- Double.parseDouble(md.getDiscount()));
			// amount1 = Math.round(amount1);
			DecimalFormat df = new DecimalFormat("0.00");
			amount1 = Double.valueOf(amount1);
			md.setAmount(df.format(amount1) + "");
			md.setPrice3(PosApp.listOrderData.get(qly).getPrice3());
			md.setPrice4(PosApp.listOrderData.get(qly).getPrice4());
			md.setPrice5(PosApp.listOrderData.get(qly).getPrice5());
			md.setIsGST(PosApp.listOrderData.get(qly).getIsGST());
			md.setIsSVC(PosApp.listOrderData.get(qly).getIsSVC());
			md.setGroupName(PosApp.listOrderData.get(qly).getGroupName());
			md.setIsFOC(PosApp.listOrderData.get(qly).getIsFOC());
			md.setStandBy(stanby);
			md.setGroupCode(PosApp.listOrderData.get(qly).getGroupCode());
			md.setRemarks(content);
			md.setItemTau(PosApp.listOrderData.get(qly).getItemTau());
			md.setBill(PosApp.listOrderData.get(qly).getBill());
			md.setSale_detailID(PosApp.listOrderData.get(qly)
					.getSale_detailID());
			md.setSetmenu(PosApp.listOrderData.get(qly).getSetmenu());
			md.setId(PosApp.listOrderData.get(qly).getId());
			md.setTakeaway(PosApp.listOrderData.get(qly).getTakeaway());
			md.setIsStyle(isStr);
			// md.setAmount(String.valueOf(amount1));
			PosApp.listOrderData.set(qly, md);
			// PosApp.listOrderData2.set(qly, md);
			// ComDDUtilities
			// .showMinusPlus(qly, md.getQualyti(), md.getDiscount());
			main.notifidataOrderList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void Plus(Activity activity, int qly) {
		try {

			ListOrderModel md = new ListOrderModel();
			md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
			md.setSpecialPrice(PosApp.listOrderData.get(qly).getSpecialPrice());
			md.setDiscount(PosApp.listOrderData.get(qly).getDiscount());
			md.setItemCode(PosApp.listOrderData.get(qly).getItemCode());
			md.setItemName(PosApp.listOrderData.get(qly).getItemName());
			md.setUnitPrice(PosApp.listOrderData.get(qly).getUnitPrice());
			md.setQualyti(String.valueOf(Integer.parseInt(PosApp.listOrderData
					.get(qly).getQualyti()) + 1));
			amount1 = Double.valueOf(Double.parseDouble(PosApp.listOrderData
					.get(qly).getUnitPrice())
					* Double.parseDouble(md.getQualyti())
					- Double.parseDouble(md.getDiscount()));
			// amount1 = Math.round(amount1);
			DecimalFormat df = new DecimalFormat("0.00");
			amount1 = Double.valueOf(amount1);
			md.setAmount(df.format(amount1) + "");
			md.setPrice3(PosApp.listOrderData.get(qly).getPrice3());
			md.setPrice4(PosApp.listOrderData.get(qly).getPrice4());
			md.setPrice5(PosApp.listOrderData.get(qly).getPrice5());
			md.setIsGST(PosApp.listOrderData.get(qly).getIsGST());
			md.setIsSVC(PosApp.listOrderData.get(qly).getIsSVC());
			md.setGroupName(PosApp.listOrderData.get(qly).getGroupName());
			md.setIsFOC(PosApp.listOrderData.get(qly).getIsFOC());
			md.setStandBy(PosApp.listOrderData.get(qly).getStandBy());
			md.setGroupCode(PosApp.listOrderData.get(qly).getGroupCode());
			md.setRemarks(PosApp.listOrderData.get(qly).getRemarks());
			md.setItemTau(PosApp.listOrderData.get(qly).getItemTau());
			md.setBill(PosApp.listOrderData.get(qly).getBill());
			md.setSale_detailID(PosApp.listOrderData.get(qly)
					.getSale_detailID());
			md.setSetmenu(PosApp.listOrderData.get(qly).getSetmenu());
			md.setId(PosApp.listOrderData.get(qly).getId());
			md.setTakeaway(PosApp.listOrderData.get(qly).getTakeaway());
			// md.setAmount(String.valueOf(amount1));
			PosApp.listOrderData.set(qly, md);
			ComDDUtilities
					.showMinusPlus(qly, md.getQualyti(), md.getDiscount());
			main.notifidataOrderList();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void Minus(Activity activity, int qly) {
		try {
			ListOrderModel md = new ListOrderModel();
			int temp = Integer.parseInt(PosApp.listOrderData.get(qly)
					.getQualyti());
			if (temp == 1) {
				PosApp.listOrderData.remove(qly);
			} else {
				md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
				md.setSpecialPrice(PosApp.listOrderData.get(qly)
						.getSpecialPrice());
				md.setDiscount(PosApp.listOrderData.get(qly).getDiscount());
				md.setItemCode(PosApp.listOrderData.get(qly).getItemCode());
				md.setItemName(PosApp.listOrderData.get(qly).getItemName());
				md.setUnitPrice(PosApp.listOrderData.get(qly).getUnitPrice());
				md.setQualyti(String.valueOf(Integer
						.parseInt(PosApp.listOrderData.get(qly).getQualyti()) - 1));
				amount1 = Double.valueOf(Double
						.parseDouble(PosApp.listOrderData.get(qly)
								.getUnitPrice())
						* Double.parseDouble(md.getQualyti())
						- Double.parseDouble(md.getDiscount()));
				DecimalFormat df = new DecimalFormat("0.00");
				amount1 = Double.valueOf(amount1);
				md.setAmount(df.format(amount1) + "");
				md.setPrice3(PosApp.listOrderData.get(qly).getPrice3());
				md.setPrice4(PosApp.listOrderData.get(qly).getPrice4());
				md.setPrice5(PosApp.listOrderData.get(qly).getPrice5());
				md.setIsGST(PosApp.listOrderData.get(qly).getIsGST());
				md.setIsSVC(PosApp.listOrderData.get(qly).getIsSVC());
				md.setGroupName(PosApp.listOrderData.get(qly).getGroupName());
				md.setIsFOC(PosApp.listOrderData.get(qly).getIsFOC());
				md.setStandBy(PosApp.listOrderData.get(qly).getStandBy());
				md.setGroupCode(PosApp.listOrderData.get(qly).getGroupCode());
				md.setRemarks(PosApp.listOrderData.get(qly).getRemarks());
				md.setItemTau(PosApp.listOrderData.get(qly).getItemTau());
				md.setBill(PosApp.listOrderData.get(qly).getBill());
				md.setSale_detailID(PosApp.listOrderData.get(qly)
						.getSale_detailID());
				md.setSetmenu(PosApp.listOrderData.get(qly).getSetmenu());
				md.setId(PosApp.listOrderData.get(qly).getId());
				md.setTakeaway(PosApp.listOrderData.get(qly).getTakeaway());
				// amount1 = Math.round(amount1);
				// md.setAmount(String.valueOf(amount1));
				PosApp.listOrderData.set(qly, md);

			}
			ComDDUtilities
					.showMinusPlus(qly, md.getQualyti(), md.getDiscount());
			main.notifidataOrderList();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void HoldSale(String total, String percenDis,
			String dollarDis, String grossBefore, String grossAfter,
			String taxAmount, String SVC, String note, String paymode,
			String counter, String numcustomer, Activity ac, String subtotal,
			String saleID, String status, String paymode2, String payVl1,
			String payVl2, String change, String voucher) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "sales"));
		params.add(new BasicNameValuePair("receiptno", Utilities
				.getGlobalVariable(ac).receiptNo));
		params.add(new BasicNameValuePair("saleID", saleID));
		params.add(new BasicNameValuePair("voucher", voucher));
		params.add(new BasicNameValuePair("grossbeforedis", grossBefore));
		params.add(new BasicNameValuePair("grossafterdis", grossAfter));
		params.add(new BasicNameValuePair("tax_amount", taxAmount));
		params.add(new BasicNameValuePair("svc_charge", SVC));
		params.add(new BasicNameValuePair("dis_percentbill", percenDis));
		params.add(new BasicNameValuePair("dis_amountbill", dollarDis));
		params.add(new BasicNameValuePair("net_amount", total));
		params.add(new BasicNameValuePair("remarkss", note));
		params.add(new BasicNameValuePair("payment_mode", paymode));
		params.add(new BasicNameValuePair("payment_mode2", paymode2));
		params.add(new BasicNameValuePair("payment_value1", payVl1));
		params.add(new BasicNameValuePair("payment_value2", payVl2));
		params.add(new BasicNameValuePair("counter", counter));
		params.add(new BasicNameValuePair("statuss", status));
		params.add(new BasicNameValuePair("user_id", UserFunctions
				.getInstance().getUserModel().getId()));
		params.add(new BasicNameValuePair("subtotalsale", subtotal));
		params.add(new BasicNameValuePair("table", Utilities
				.getGlobalVariable(ac).tableNum));
		params.add(new BasicNameValuePair("number_customers", numcustomer));
		params.add(new BasicNameValuePair("change", change));
		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			params.add(new BasicNameValuePair("group[]", PosApp.listOrderData
					.get(i).getGroupName().toString()));
			try {
				params.add(new BasicNameValuePair("items[]",
						PosApp.listOrderData.get(i).getItemName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			params.add(new BasicNameValuePair("itemCN[]", PosApp.listOrderData
					.get(i).getItemTau()));
			// Log.v("HAI", PosApp.listOrderData.get(i).getItemTau());
			params.add(new BasicNameValuePair("set_menu[]",
					PosApp.listOrderData.get(i).getSetmenu()));
			// Log.v("HAI", PosApp.listOrderData.get(i).getSetMenu());
			params.add(new BasicNameValuePair("quantity[]",
					PosApp.listOrderData.get(i).getQualyti()));
			params.add(new BasicNameValuePair("selling_price[]",
					PosApp.listOrderData.get(i).getUnitPrice()));
			params.add(new BasicNameValuePair("dis_percent[]", "0"));
			params.add(new BasicNameValuePair("dis_amount[]",
					PosApp.listOrderData.get(i).getDiscount()));

			params.add(new BasicNameValuePair("subtotal[]",
					PosApp.listOrderData.get(i).getAmount()));
			params.add(new BasicNameValuePair("foc[]", PosApp.listOrderData
					.get(i).getIsFOC()));

			try {
				params.add(new BasicNameValuePair("remarks[]",
						PosApp.listOrderData.get(i).getRemarks()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			params.add(new BasicNameValuePair("user[]", UserFunctions
					.getInstance().getUserModel().getUsername()));
			params.add(new BasicNameValuePair("itemID[]", PosApp.listOrderData
					.get(i).getItemCode()));
			params.add(new BasicNameValuePair("status[]", PosApp.listOrderData
					.get(i).getStandBy()));
			params.add(new BasicNameValuePair("bill[]", PosApp.listOrderData
					.get(i).getBill()));
			params.add(new BasicNameValuePair("takeaway[]",
					PosApp.listOrderData.get(i).getTakeaway()));
			params.add(new BasicNameValuePair("item_id[]",
					PosApp.listOrderData.get(i).getId()));
		}
		UserFunctions.getInstance().sales(ac, params, false);

	}

	public static void pay(Activity act, String paymentmode,
			String paymentmode2, String payVl1, String payVl2, String change,
			String voucher) {
		Utilities.getGlobalVariable(act).paymentMode = paymentmode;
		Utilities.getGlobalVariable(act).paymentMode2 = paymentmode2;
		Utilities.getGlobalVariable(act).statusPay = "1";
		// Utilities.getGlobalVariable(act).isUpdate = false;
		HoldSale(
				MainActivity.tvTotalAmount.getText().toString(),
				"0",
				MainActivity.btnDisCountValue.getText().toString(),
				Double.parseDouble(MainActivity.tvTotalAmount.getText()
						.toString())
						+ Double.parseDouble(MainActivity.btnDisCountValue
								.getText().toString()) + "",
				MainActivity.tvTotalAmount.getText().toString(),
				MainActivity.btnGSTValue.getText().toString(),
				MainActivity.btnSVCValue.getText().toString(),
				MainActivity.note,
				Utilities.getGlobalVariable(act).paymentMode, "1",
				Utilities.getGlobalVariable(act).numberCustomer + "", act,
				MainActivity.btnSubTotalValue.getText().toString(),
				UserFunctions.getInstance().getSaleModel().getSaleID(),
				Utilities.getGlobalVariable(act).statusPay,
				Utilities.getGlobalVariable(act).paymentMode2, payVl1, payVl2,
				change, voucher);
		Utilities.getGlobalVariable(act).paymentMode = "0";
		Utilities.getGlobalVariable(act).statusPay = "0";
		Utilities.getGlobalVariable(act).paymentMode2 = "";
	}

	public static void splitUpdateListOrder(Activity ac, int qly,
			String quantity) {
		if (Integer.parseInt(quantity) <= 0) {
			Toast.makeText(ac, "Error!", Toast.LENGTH_SHORT).show();
		} else {

			int realQuantity = Integer.parseInt(PosApp.listOrderData.get(qly)
					.getQualyti()) - Integer.parseInt(quantity);

			if (realQuantity == 0) {
				splitAddTolist(qly, quantity);
				PosApp.listOrderData.remove(qly);
				// PosApp.listOrderSplit.remove(qly);

			} else {
				if (realQuantity < 0) {
					Toast.makeText(ac, "Error!", Toast.LENGTH_SHORT).show();
				} else {
					splitAddTolist(qly, quantity);
					try {
						ListOrderModel md = new ListOrderModel();
						md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
						md.setSpecialPrice(PosApp.listOrderData.get(qly)
								.getSpecialPrice());
						md.setDiscount(PosApp.listOrderData.get(qly)
								.getDiscount());
						md.setItemCode(PosApp.listOrderData.get(qly)
								.getItemCode());
						md.setItemName(PosApp.listOrderData.get(qly)
								.getItemName());
						md.setUnitPrice(PosApp.listOrderData.get(qly)
								.getUnitPrice());
						md.setQualyti(realQuantity + "");
						amount1 = Double.valueOf(Double
								.parseDouble(PosApp.listOrderData.get(qly)
										.getUnitPrice())
								* Double.parseDouble(md.getQualyti())
								- Double.parseDouble(md.getDiscount()));
						DecimalFormat df = new DecimalFormat("0.00");
						amount1 = Double.valueOf(amount1);
						md.setAmount(df.format(amount1) + "");
						md.setPrice3(PosApp.listOrderData.get(qly).getPrice3());
						md.setPrice4(PosApp.listOrderData.get(qly).getPrice4());
						md.setPrice5(PosApp.listOrderData.get(qly).getPrice5());
						md.setIsGST(PosApp.listOrderData.get(qly).getIsGST());
						md.setIsSVC(PosApp.listOrderData.get(qly).getIsSVC());
						md.setGroupName(PosApp.listOrderData.get(qly)
								.getGroupName());
						md.setIsFOC(PosApp.listOrderData.get(qly).getIsFOC());
						md.setStandBy(PosApp.listOrderData.get(qly)
								.getStandBy());
						md.setGroupCode(PosApp.listOrderData.get(qly)
								.getGroupCode());
						md.setRemarks(PosApp.listOrderData.get(qly)
								.getRemarks());
						md.setItemTau(PosApp.listOrderData.get(qly)
								.getItemTau());
						md.setSale_detailID(PosApp.listOrderData.get(qly)
								.getSale_detailID());
						md.setSetmenu(PosApp.listOrderData.get(qly)
								.getSetmenu());
						md.setId(PosApp.listOrderData.get(qly).getId());
						md.setBill(PosApp.listOrderData.get(qly).getBill());
						md.setTakeaway(PosApp.listOrderData.get(qly)
								.getTakeaway());
						if (realQuantity > 1) {
							SplitOrderModel md1 = new SplitOrderModel();
							md1.setPrice2(md.getPrice2());
							md1.setSpecialPrice(md.getSpecialPrice());
							md1.setDiscount(md.getDiscount());
							md1.setItemCode(md.getItemCode());
							md1.setItemName(md.getItemName());
							md1.setUnitPrice(md.getUnitPrice());
							md1.setQualyti(realQuantity + "");
							amount1 = Double.valueOf(Double.parseDouble(md
									.getUnitPrice())
									* Double.parseDouble(md.getQualyti())
									- Double.parseDouble(md.getDiscount()));
							DecimalFormat df1 = new DecimalFormat("0.00");
							amount1 = Double.valueOf(amount1);
							md1.setAmount(df1.format(amount1));
							md1.setPrice3(md.getPrice3());
							md1.setPrice4(md.getPrice4());
							md1.setPrice5(md.getPrice5());
							md1.setIsGST(md.getIsGST());
							md1.setIsSVC(md.getIsSVC());
							md1.setGroupName(md.getGroupName());
							md1.setIsFOC(md.getIsFOC());
							md1.setStandBy(md.getStandBy());
							md1.setGroupCode(md.getGroupCode());
							md1.setRemarks(md.getRemarks());
							md1.setBill(md.getBill());
							md1.setSaleDetailID(md.getSale_detailID());
							md1.setSetMenu(md.getSetmenu());
							md1.setId(md.getId());
							md1.setItemCN(md.getItemTau());
							md1.setTakeaway(md.getTakeaway());
							PosApp.listOrderSplit.add(md1);
						}
						PosApp.listOrderData.set(qly, md);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}

			SplitBillActivity sp = new SplitBillActivity();
			sp.notifi();
		}

	}

	public static void splitAddTolist(int qly, String realQuantity) {
		SplitOrderModel md = new SplitOrderModel();
		md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
		md.setSpecialPrice(PosApp.listOrderData.get(qly).getSpecialPrice());
		md.setDiscount(PosApp.listOrderData.get(qly).getDiscount());
		md.setItemCode(PosApp.listOrderData.get(qly).getItemCode());
		md.setItemName(PosApp.listOrderData.get(qly).getItemName());
		md.setUnitPrice(PosApp.listOrderData.get(qly).getUnitPrice());
		md.setQualyti(realQuantity + "");
		amount1 = Double.valueOf(Double.parseDouble(PosApp.listOrderData.get(
				qly).getUnitPrice())
				* Double.parseDouble(md.getQualyti())
				- Double.parseDouble(md.getDiscount()));
		DecimalFormat df = new DecimalFormat("0.00");
		amount1 = Double.valueOf(amount1);
		md.setAmount(df.format(amount1));
		md.setPrice3(PosApp.listOrderData.get(qly).getPrice3());
		md.setPrice4(PosApp.listOrderData.get(qly).getPrice4());
		md.setPrice5(PosApp.listOrderData.get(qly).getPrice5());
		md.setIsGST(PosApp.listOrderData.get(qly).getIsGST());
		md.setIsSVC(PosApp.listOrderData.get(qly).getIsSVC());
		md.setGroupName(PosApp.listOrderData.get(qly).getGroupName());
		md.setIsFOC(PosApp.listOrderData.get(qly).getIsFOC());
		md.setStandBy(PosApp.listOrderData.get(qly).getStandBy());
		md.setGroupCode(PosApp.listOrderData.get(qly).getGroupCode());
		md.setRemarks(PosApp.listOrderData.get(qly).getRemarks());
		md.setBill("" + (SplitBillAdapter.pos + 1));
		md.setSaleDetailID(PosApp.listOrderData.get(qly).getSale_detailID());
		md.setSetMenu(PosApp.listOrderData.get(qly).getSetmenu());
		md.setId(PosApp.listOrderData.get(qly).getId());
		md.setItemCN(PosApp.listOrderData.get(qly).getItemTau());
		md.setTakeaway(PosApp.listOrderData.get(qly).getTakeaway());
		Log.v("HAI", "pos item= " + md.getBill());
		for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
			if (PosApp.listOrderSplit.get(i).getItemCode()
					.equals(PosApp.listOrderData.get(qly).getItemCode())) {
				PosApp.listOrderSplit.set(i, md);
			}
		}

	}

	public static void saveWhenExit(Activity ac) {
		if (PosApp.listOrderData.size() > 0) {
			if (Utilities.getGlobalVariable(ac).isUpdate) {
				Log.v("HAI", "update");
				Utils.HoldSale(
						MainActivity.tvTotalAmount.getText().toString(),
						"0",
						MainActivity.btnDisCountValue.getText().toString(),
						Double.parseDouble(MainActivity.tvTotalAmount.getText()
								.toString())
								+ Double.parseDouble(MainActivity.btnDisCountValue
										.getText().toString()) + "",
						MainActivity.tvTotalAmount.getText().toString(),
						MainActivity.btnGSTValue.getText().toString(),
						MainActivity.btnSVCValue.getText().toString(),
						MainActivity.note,
						Utilities.getGlobalVariable(ac).paymentMode, "1",
						Utilities.getGlobalVariable(ac).numberCustomer + "",
						ac, MainActivity.btnSubTotalValue.getText().toString(),
						UserFunctions.getInstance().getSaleModel().getSaleID(),
						Utilities.getGlobalVariable(ac).statusPay, "", "", "",
						"0", "0");
			} else {
				Utils.HoldSale(
						MainActivity.tvTotalAmount.getText().toString(),
						"0",
						MainActivity.btnDisCountValue.getText().toString(),
						Double.parseDouble(MainActivity.tvTotalAmount.getText()
								.toString())
								+ Double.parseDouble(MainActivity.btnDisCountValue
										.getText().toString()) + "",
						MainActivity.tvTotalAmount.getText().toString(),
						MainActivity.btnGSTValue.getText().toString(),
						MainActivity.btnSVCValue.getText().toString(),
						MainActivity.note,
						Utilities.getGlobalVariable(ac).paymentMode, "1",
						Utilities.getGlobalVariable(ac).numberCustomer + "",
						ac, MainActivity.btnSubTotalValue.getText().toString(),
						"", Utilities.getGlobalVariable(ac).statusPay, "", "",
						"", "0", "0");
			}

		} else {
			// Log.v("HAI", "a");
		}
		PosApp.listOrderSplit.clear();
		SplitBillActivity.listBill.clear();
	}

	public static void getReceiptNo(Activity ac) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getReceiptNo"));
		UserFunctions.getInstance().getReceiptNo(ac, params, false);

	}

	public static void clearList(Activity ac) {
		PosApp.listOrderSplit.clear();
		SplitBillActivity.listBill.clear();
		PosApp.listOrderData.clear();
		ac.finish();
	}

	public static void splitBill(String total, String percenDis,
			String dollarDis, String grossBefore, String grossAfter,
			String taxAmount, String SVC, String note, String paymode,
			String counter, String numcustomer, Activity ac, String subtotal,
			String saleID, String status, String paymode2, String payVl1,
			String payVl2, String change, String bill) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "splitUpdate"));
		params.add(new BasicNameValuePair("receiptno", Utilities
				.getGlobalVariable(ac).receiptNo));
		params.add(new BasicNameValuePair("saleID", saleID));
		params.add(new BasicNameValuePair("bill", bill));
		params.add(new BasicNameValuePair("grossbeforedis", grossBefore));
		params.add(new BasicNameValuePair("grossafterdis", grossAfter));
		params.add(new BasicNameValuePair("tax_amount", taxAmount));
		params.add(new BasicNameValuePair("svc_charge", SVC));
		params.add(new BasicNameValuePair("dis_percentbill", percenDis));
		params.add(new BasicNameValuePair("dis_amountbill", dollarDis));
		params.add(new BasicNameValuePair("net_amount", total));
		params.add(new BasicNameValuePair("remarkss", note));
		params.add(new BasicNameValuePair("payment_mode", paymode));
		params.add(new BasicNameValuePair("payment_mode2", paymode2));
		params.add(new BasicNameValuePair("payment_value1", payVl1));
		params.add(new BasicNameValuePair("payment_value2", payVl2));
		params.add(new BasicNameValuePair("counter", counter));
		params.add(new BasicNameValuePair("statuss", status));
		params.add(new BasicNameValuePair("user_id", UserFunctions
				.getInstance().getUserModel().getId()));
		params.add(new BasicNameValuePair("subtotalsale", subtotal));
		params.add(new BasicNameValuePair("table", Utilities
				.getGlobalVariable(ac).tableNum));
		params.add(new BasicNameValuePair("number_customers", numcustomer));
		params.add(new BasicNameValuePair("change", change));
		for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
			if (PosApp.listOrderSplit.get(i).getBill().equals(bill)) {

				params.add(new BasicNameValuePair("group[]",
						PosApp.listOrderSplit.get(i).getGroupName().toString()));
				try {
					params.add(new BasicNameValuePair("items[]",
							PosApp.listOrderSplit.get(i).getItemName()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				params.add(new BasicNameValuePair("itemCN[]",
						PosApp.listOrderSplit.get(i).getItemCN()));
				// Log.v("HAI", PosApp.listOrderData.get(i).getItemTau());
				params.add(new BasicNameValuePair("set_menu[]",
						PosApp.listOrderSplit.get(i).getSetMenu()));
				// Log.v("HAI", PosApp.listOrderData.get(i).getSetMenu());
				params.add(new BasicNameValuePair("quantity[]",
						PosApp.listOrderSplit.get(i).getQualyti()));
				params.add(new BasicNameValuePair("selling_price[]",
						PosApp.listOrderSplit.get(i).getUnitPrice()));
				params.add(new BasicNameValuePair("dis_percent[]", "0"));
				params.add(new BasicNameValuePair("dis_amount[]",
						PosApp.listOrderSplit.get(i).getDiscount()));

				params.add(new BasicNameValuePair("subtotal[]",
						PosApp.listOrderSplit.get(i).getAmount()));
				params.add(new BasicNameValuePair("foc[]",
						PosApp.listOrderSplit.get(i).getIsFOC()));

				try {
					params.add(new BasicNameValuePair("remarks[]",
							PosApp.listOrderSplit.get(i).getRemarks()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				params.add(new BasicNameValuePair("user[]", UserFunctions
						.getInstance().getUserModel().getUsername()));
				params.add(new BasicNameValuePair("itemID[]",
						PosApp.listOrderSplit.get(i).getItemCode()));
				params.add(new BasicNameValuePair("status[]",
						PosApp.listOrderSplit.get(i).getStandBy()));
			}
		}
		UserFunctions.getInstance().deleteItem(ac, params, false);
		Log.v("HAI", "goi ham");
	}

	public static void paySplitSale(Activity ac, String isBill1, String saleID,
			String grossBefore, String grossAfter, String taxAmount,
			String SVC, String dollarDis, String total, String remark,
			String counter, String status, String numcus, String paymode,
			String userID, String table,String subtotal) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "paySplit"));
		params.add(new BasicNameValuePair("isBill1", isBill1));
		params.add(new BasicNameValuePair("saleID", saleID));
		params.add(new BasicNameValuePair("reciept", Utilities
				.getGlobalVariable(ac).receiptNo));
		params.add(new BasicNameValuePair("grossbefore", grossBefore));
		params.add(new BasicNameValuePair("grossafter", grossAfter));
		params.add(new BasicNameValuePair("tax", taxAmount));
		params.add(new BasicNameValuePair("svc", SVC));
		params.add(new BasicNameValuePair("discount_amount", dollarDis));
		params.add(new BasicNameValuePair("net_amount", total));
		params.add(new BasicNameValuePair("remarks", remark));
		params.add(new BasicNameValuePair("counter", counter));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("number_of_customers", numcus));
		params.add(new BasicNameValuePair("payment_mode", paymode));
		params.add(new BasicNameValuePair("user_id", userID));
		params.add(new BasicNameValuePair("table_name", table));
		params.add(new BasicNameValuePair("Sub_total", subtotal));
		for (int i = 0; i < PosApp.listOrderData.size(); i++) {
			params.add(new BasicNameValuePair("saleDetailID[]",
					PosApp.listOrderData.get(i).getSale_detailID()));
		}
		UserFunctions.getInstance().sales(ac, params, false);

	}
	// public static boolean checkDataChange() {
	// if (PosApp.listOrderData.size() != PosApp.listOrderDataCompare.size()) {
	// for(int i=0;i<PosApp.listOrderData.size();i++){
	// if(PosApp.listOrderData.get(i).)
	// }
	// return true;
	// }
	// return false;
	// }
}

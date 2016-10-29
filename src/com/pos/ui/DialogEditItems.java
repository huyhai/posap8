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
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.db.BillDataSource;
import com.pos.db.CompanyDataSource;
import com.pos.db.ItemsDataSource;
import com.pos.db.MySQLiteHelper;
import com.pos.db.SaleReportDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ComDDUtilities;
import com.pos.model.ListOrderModel;
import com.pos.print.PrinterFunctions;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogEditItems implements OnClickListener {
	private Dialog dialogEditItems;
	private Boolean isShowDialog = false;
	private TextView tvItemDiscount;
	private TextView tvItemDiscount2;
	private TextView tvEditsale;
	private TextView tvTakeAways;
	private TextView tvSpecialPrice;
	private TextView tvDeleteItem;
	private LinearLayout llAll;
	private Activity ac;
	private int pos;
	private TextView tvSubmit;

	public void showDialogSelectImg(Activity _ac, int _pos) {
		ac = _ac;
		pos = _pos;
		// custom dialog
		if (!isShowDialog) {
			isShowDialog = true;
			// imgSet = _imgSet;
			dialogEditItems = new Dialog(ac);
			dialogEditItems.getWindow();
			dialogEditItems.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogEditItems.setContentView(R.layout.dialog_edit_item);
			dialogEditItems.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			// dialog.setTitle("Title...");

			// set the custom dialog components - text, image and button

			this.tvItemDiscount = (TextView) dialogEditItems
					.findViewById(R.id.tvItemDiscount);
			this.tvEditsale = (TextView) dialogEditItems
					.findViewById(R.id.tvEditsale);
			this.tvTakeAways = (TextView) dialogEditItems
					.findViewById(R.id.tvTakeAways);
			this.tvSpecialPrice = (TextView) dialogEditItems
					.findViewById(R.id.tvSpecialPrice);
			tvSubmit = (TextView) dialogEditItems.findViewById(R.id.tvSubmit);

			tvDeleteItem = (TextView) dialogEditItems
					.findViewById(R.id.tvDeleteItem);
			llAll = (LinearLayout) dialogEditItems.findViewById(R.id.llAll);
			((CustomFragmentActivity) ac).setWidth(llAll, 4);

			// if (PosApp.listOrderData.get(pos).getSpecialPrice().equals("")) {
			// tvSpecialPrice.setVisibility(View.GONE);
			// } else {
			// tvSpecialPrice.setVisibility(View.VISIBLE);
			// }
			tvItemDiscount.setOnClickListener(this);
			tvEditsale.setOnClickListener(this);
			tvTakeAways.setOnClickListener(this);
			tvSpecialPrice.setOnClickListener(this);
			tvDeleteItem.setOnClickListener(this);
			tvSubmit.setOnClickListener(this);
			dialogEditItems.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					isShowDialog = false;
				}
			});

			dialogEditItems.show();
		}

	}

	@Override
	public void onClick(View v) {
		if (v == tvItemDiscount) {
			DialogItemDiscount a = new DialogItemDiscount();
			a.showDialogItemDiscount(ac, ac, pos);
			dialogEditItems.dismiss();

		}
		if (v == tvEditsale) {
			DialogEditSale a = new DialogEditSale();
			a.showDialogEditSale(ac, ac, pos);
			dialogEditItems.dismiss();

		}
		if (v == tvTakeAways) {

			ListOrderModel md = new ListOrderModel();
			md.setQualyti(PosApp.listOrderData.get(pos).getQualyti());
			md.setAmount(PosApp.listOrderData.get(pos).getAmount());
			md.setDiscount("0.00");
			md.setItemCode(PosApp.listOrderData.get(pos).getItemCode());
			md.setItemName(PosApp.listOrderData.get(pos).getItemName()
					+ "(Take-away)");
			md.setPrice2(PosApp.listOrderData.get(pos).getPrice2());
			md.setSpecialPrice(PosApp.listOrderData.get(pos).getSpecialPrice());
			md.setUnitPrice(PosApp.listOrderData.get(pos).getUnitPrice());
			md.setIsFOC(PosApp.listOrderData.get(pos).getIsFOC());
			md.setPrice3(PosApp.listOrderData.get(pos).getPrice3());
			md.setPrice4(PosApp.listOrderData.get(pos).getPrice4());
			md.setPrice5(PosApp.listOrderData.get(pos).getPrice5());
			md.setIsGST(PosApp.listOrderData.get(pos).getIsGST());
			md.setIsSVC("0");
			md.setGroupName(PosApp.listOrderData.get(pos).getGroupName());
			md.setGroupCode(PosApp.listOrderData.get(pos).getGroupCode());
			md.setStandBy(PosApp.listOrderData.get(pos).getStandBy());
			md.setRemarks(PosApp.listOrderData.get(pos).getRemarks());
			md.setSetmenu(PosApp.listOrderData.get(pos).getSetmenu());
			md.setItemTau(PosApp.listOrderData.get(pos).getItemTau()
					+ "(Take-away)");
			md.setSale_detailID(PosApp.listOrderData.get(pos)
					.getSale_detailID());
			md.setBill(PosApp.listOrderData.get(pos).getBill());
			md.setId(PosApp.listOrderData.get(pos).getId());
			md.setTakeaway("1");
			PosApp.listOrderData.set(pos, md);
			MainActivity main = new MainActivity();
			main.notifidataOrderList();
			dialogEditItems.dismiss();

		}
		if (v == tvSpecialPrice) {

		}
		if (v == tvDeleteItem) {

			DialogSelectQtyDelete di = new DialogSelectQtyDelete();
			di.showNoteDialog(ac, ac, PosApp.listOrderData.get(pos)
					.getQualyti(), pos);

			MainActivity main = new MainActivity();
			main.notifidataOrderList();
			dialogEditItems.dismiss();
		

			if (PosApp.listOrderData.size() == 0) {
				ComDDUtilities.showWelcome();
			}
		}
		if (v == tvSubmit) {
			if (PosApp.listOrderData.get(pos).getStandBy().equals("1")) {
				Utilities.getGlobalVariable(ac).subNow = true;
				updateSubmit(PosApp.listOrderData.get(pos).getSale_detailID());

				Utils.updateItem(ac, pos, PosApp.listOrderData.get(pos)
						.getRemarks(), "0", PosApp.listOrderData.get(pos)
						.getAmount(), "1");
				PosApp.listOrderData2.add(PosApp.listOrderData.get(pos));
				PrinterFunctions.PrintPosOne(MainActivity.activity,
						MainActivity.activity, "USB:", "",
						MainActivity.activity.getResources(), "", 0);

				PrinterFunctions.PrintPosOne(MainActivity.activity,
						MainActivity.activity, "TCP:192.168.1.204", "",
						MainActivity.activity.getResources(), "", 0);

				PrinterFunctions.PrintPosOne(MainActivity.activity,
						MainActivity.activity, "TCP:192.168.1.209", "",
						MainActivity.activity.getResources(), "", 0);
				for (int j = 0; j < UserFunctions.getInstance().getlistMayIn()
						.size(); j++) {
					if (PosApp.listOrderData
							.get(pos)
							.getGroupCode()
							.equals(UserFunctions.getInstance().getlistMayIn()
									.get(j).getItem_group())) {
						int one = Integer.parseInt(UserFunctions.getInstance()
								.getlistMayIn().get(j).getPrinter_one());
						getIp1(one);
						PrinterFunctions.PrintPosOne(MainActivity.activity,
								MainActivity.activity, "TCP:" + ip1, "",
								MainActivity.activity.getResources(), "", 0);
						PrinterFunctions.PrintPosOne(MainActivity.activity,
								MainActivity.activity, "TCP:192.168.1.207", "",
								MainActivity.activity.getResources(), "", 0);
						Log.v("HAI", "send request 1");


					}

				}

				PosApp.listOrderData2.clear();
				Utilities.getGlobalVariable(ac).subNow = false;
				Utilities.showDialogNoBack(ac, "Order have been sent").show();
			}

			dialogEditItems.dismiss();
		}

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

	private void updateSubmit(String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateSubmit"));
		params.add(new BasicNameValuePair("detailID", id));
		UserFunctions.getInstance().deleteItem(ac, params, false);
	}

	private void deleteItem(String id) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "deleteItem"));
		params.add(new BasicNameValuePair("detailID", id));
		UserFunctions.getInstance().deleteItem(ac, params, false);
	}
}

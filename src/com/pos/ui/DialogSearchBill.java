package com.pos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.SearchBill2Adapter;
import com.pos.adapter.SearchBillAdapter;
import com.pos.adapter.SplitBillAdapter;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.db.MainCateDataSource;
import com.pos.db.SearchBillDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.SearchBillModel;
import com.pos.model.SplitBillModel;
import com.pos.model.SplitOrderModel;

public class DialogSearchBill {
	private ListView lvSearch;
	private RelativeLayout rlRecieptNo;
	private RelativeLayout rlDateTime;
	private static Activity ac;
	// public static ArrayList<SearchBillModel> list = new
	// ArrayList<SearchBillModel>();
	private static SearchBillAdapter adapter;
	public static int a = -1;
	public static String discountUn;
	public static String recepit;
	public static String idSale = "";
	public static SearchBill2Adapter splitAdapter;
	static double amount1 = 0;

	public void showDialogEditSale(final Context context, final Activity _ac) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_searchBill));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_searchbill, null);
		builder.setView(v);
		initView(v);
		initData();

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						PosApp.listOrderData.clear();
						for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
							if (PosApp.listOrderSplit
									.get(i)
									.getBill()
									.equals(String
											.valueOf(SearchBill2Adapter.pos + 1))) {

								ListOrderModel md = new ListOrderModel();
								md.setQualyti(PosApp.listOrderSplit.get(i)
										.getQualyti());
								DecimalFormat df = new DecimalFormat("0.00");
								amount1 = Double.valueOf(PosApp.listOrderSplit
										.get(i).getAmount());
								md.setAmount(df.format(amount1) + "");
								md.setDiscount(PosApp.listOrderSplit.get(i)
										.getDiscount());
								md.setItemCode(PosApp.listOrderSplit.get(i)
										.getItemCode());
								md.setItemName(PosApp.listOrderSplit.get(i)
										.getItemName());
								md.setPrice2(PosApp.listOrderSplit.get(i)
										.getPrice2());
								md.setSpecialPrice(PosApp.listOrderSplit.get(i)
										.getSpecialPrice());
								md.setUnitPrice(PosApp.listOrderSplit.get(i)
										.getUnitPrice());
								md.setIsFOC(PosApp.listOrderSplit.get(i)
										.getIsFOC());
								md.setPrice3(PosApp.listOrderSplit.get(i)
										.getPrice3());
								md.setPrice4(PosApp.listOrderSplit.get(i)
										.getPrice4());
								md.setPrice5(PosApp.listOrderSplit.get(i)
										.getPrice5());
								md.setIsGST(PosApp.listOrderSplit.get(i)
										.getIsGST());
								md.setIsSVC(PosApp.listOrderSplit.get(i)
										.getIsSVC());
								md.setGroupName(PosApp.listOrderSplit.get(i)
										.getGroupName());
								md.setGroupCode(PosApp.listOrderSplit.get(i)
										.getGroupCode());
								md.setStandBy(PosApp.listOrderSplit.get(i)
										.getStandBy());
								md.setRemarks(PosApp.listOrderSplit.get(i)
										.getRemarks());
								md.setItemTau(PosApp.listOrderSplit.get(i)
										.getItemCN());
								md.setSale_detailID(PosApp.listOrderSplit
										.get(i).getSaleDetailID());
								md.setSetmenu(PosApp.listOrderSplit.get(i)
										.getSetMenu());
								md.setTakeaway(PosApp.listOrderSplit.get(i)
										.getTakeaway());
								PosApp.listOrderData.add(md);
							}
						}

						MainActivity ma = new MainActivity();
						ma.notifidataOrderList();

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

	private int numBill = 0;

	public void initData() {
		for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
			if (Integer.parseInt(PosApp.listOrderSplit.get(i).getBill()) > numBill) {
				numBill = Integer.parseInt(PosApp.listOrderSplit.get(i)
						.getBill());
			}
		}
		for (int j = 1; j <= numBill; j++) {

			if (Utilities.getGlobalVariable(ac).isHaveSendOrder) {
				if (SplitBillActivity.listBill.size() < numBill) {
					SplitBillModel md = new SplitBillModel();
					md.setBill("Bill");
					md.setTotalAmount(UserFunctions.getInstance()
							.getSaleModel().getTotal());
					SplitBillActivity.listBill.add(md);
				}
			}

		}
		for (int i = 1; i <= SplitBillActivity.listBill.size(); i++) {
			for (int j = 0; j < PosApp.listOrderSplit.size(); j++) {
				if (PosApp.listOrderSplit.get(j).getBill()
						.equals(String.valueOf(i))) {
					SplitBillModel md = new SplitBillModel();
					md.setBill("Bill");
					double subtotal = 0;
					subtotal = CalculationUtils.calculateSubTotalSplit(
							PosApp.listOrderSplit, String.valueOf(i));
					double svc = 0;
					svc = CalculationUtils.calculateServiceChargeSplit(
							PosApp.listOrderSplit, String.valueOf(i));
					double gst = 0;
					gst = CalculationUtils.calculateGSTChargeSplit(
							PosApp.listOrderSplit, String.valueOf(i));
					String tong = Utils.lamtronTong(subtotal + svc + gst);
					md.setTotalAmount(tong);
//					Log.v("HAI", "tog " + tong);
//					Log.v("HAI", "tog " + (i - 1));
					SplitBillActivity.listBill.set(i - 1, md);
				}
			}

		}
//
//		SplitBillModel md = new SplitBillModel();
//		md.setBill("Bill");
//		double subtotal = CalculationUtils
//				.calculateSubTotal(PosApp.listOrderData);
//		double svc = CalculationUtils
//				.calculateServiceCharge(PosApp.listOrderData);
//		double gst = CalculationUtils.calculateGSTCharge(PosApp.listOrderData);
//		String tong = Utils.lamtronTong(subtotal + svc + gst);
//		md.setTotalAmount(tong);
//		SplitBillActivity.listBill.set(0, md);
		splitAdapter = new SearchBill2Adapter(ac, SplitBillActivity.listBill);
		lvSearch.setAdapter(splitAdapter);

	}

	private void initView(View v) {
		lvSearch = (ListView) v.findViewById(R.id.lvSearch);
		rlRecieptNo = (RelativeLayout) v.findViewById(R.id.rlRecieptNo);
		rlDateTime = (RelativeLayout) v.findViewById(R.id.rlDateTime);

	}

}

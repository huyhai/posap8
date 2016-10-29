package com.pos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.CopyOfListOrderAdapter;
import com.pos.adapter.ListOrderAdapter;
import com.pos.adapter.SearchBill2Adapter;
import com.pos.adapter.SplitBillAdapter;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.ViewUtils;
import com.pos.model.ListOrderModel;
import com.pos.model.SplitBillModel;
import com.pos.model.SplitOrderModel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SplitBillActivity extends Activity implements OnClickListener {
	private double amount1;
	private RelativeLayout rlItemName;
	private RelativeLayout rlUnitPrice;
	private RelativeLayout rlQty;
	private RelativeLayout rlDiscount;
	private RelativeLayout rlAmount;
	private static Activity ac;
	private LinearLayout rlsss1;
	private ImageView imgBack;
	private Button btnConfirm;
	private Button btnCancel;
	private Button btnAddBill;
	private ListView lvBill;
	private ListView lvOrder;
	private static CopyOfListOrderAdapter adapter;
	public static SplitBillAdapter splitAdapter;
	public static ArrayList<SplitBillModel> listBill = new ArrayList<SplitBillModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.split_bill);
		ac = SplitBillActivity.this;
		initV();
	}

	private void initV() {
		rlAmount = (RelativeLayout) findViewById(R.id.rlAmount);
		rlDiscount = (RelativeLayout) findViewById(R.id.rlDiscount);
		rlItemName = (RelativeLayout) findViewById(R.id.rlItemName);
		rlQty = (RelativeLayout) findViewById(R.id.rlQty);
		rlUnitPrice = (RelativeLayout) findViewById(R.id.rlUnitPrice);
		rlsss1 = (LinearLayout) findViewById(R.id.rlsss1);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnAddBill = (Button) findViewById(R.id.btnAddBill);
		lvOrder = (ListView) findViewById(R.id.lvOrder);
		lvBill = (ListView) findViewById(R.id.lvBill);
		initDa();
	}

	private void initDa() {
		btnAddBill.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		addDefault();
		int h = 12;
		ViewUtils.setWidthHeight(rlItemName, 4, h, ac);
		ViewUtils.setWidthHeight(rlDiscount, 13, h, ac);
		ViewUtils.setWidthHeight(rlQty, 18, h, ac);
		ViewUtils.setHeight(rlAmount, h, ac);
		ViewUtils.setWidthHeight(rlUnitPrice, 14, h, ac);
		ViewUtils.setHeight(rlsss1, h, ac);
		PosApp.listOrderData.clear();
		for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
			ListOrderModel md = new ListOrderModel();
			md.setQualyti(PosApp.listOrderSplit.get(i).getQualyti());
			md.setAmount(PosApp.listOrderSplit.get(i).getAmount());
			md.setDiscount(PosApp.listOrderSplit.get(i).getDiscount());
			md.setItemCode(PosApp.listOrderSplit.get(i).getItemCode());
			md.setItemName(PosApp.listOrderSplit.get(i).getItemName());
			md.setPrice2(PosApp.listOrderSplit.get(i).getPrice2());
			md.setSpecialPrice(PosApp.listOrderSplit.get(i).getSpecialPrice());
			md.setUnitPrice(PosApp.listOrderSplit.get(i).getUnitPrice());
			md.setIsFOC(PosApp.listOrderSplit.get(i).getIsFOC());
			md.setPrice3(PosApp.listOrderSplit.get(i).getPrice3());
			md.setPrice4(PosApp.listOrderSplit.get(i).getPrice4());
			md.setPrice5(PosApp.listOrderSplit.get(i).getPrice5());
			md.setIsGST(PosApp.listOrderSplit.get(i).getIsGST());
			md.setIsSVC(PosApp.listOrderSplit.get(i).getIsSVC());
			md.setGroupName(PosApp.listOrderSplit.get(i).getGroupName());
			md.setGroupCode(PosApp.listOrderSplit.get(i).getGroupCode());
			md.setStandBy(PosApp.listOrderSplit.get(i).getStandBy());
			md.setRemarks(PosApp.listOrderSplit.get(i).getRemarks());
			md.setSale_detailID(PosApp.listOrderSplit.get(i).getSaleDetailID());
			md.setBill(PosApp.listOrderSplit.get(i).getBill());
			md.setId(PosApp.listOrderSplit.get(i).getId());
			md.setTakeaway(PosApp.listOrderSplit.get(i).getTakeaway());
			PosApp.listOrderData.add(md);
		}
		adapter = new CopyOfListOrderAdapter(ac, PosApp.listOrderData);
		lvOrder.setAdapter(adapter);
		splitAdapter = new SplitBillAdapter(ac, listBill);
		lvBill.setAdapter(splitAdapter);

	}

	public void notifi() {
		adapter.notifyDataSetChanged();

	}

	private void addDefault() {
		if (listBill.size() <= 0) {
			SplitBillModel md = new SplitBillModel();
			md.setBill("Bill");
			md.setTotalAmount(MainActivity.tvTotalAmount.getText().toString());
			listBill.add(md);
		}

	}

	private void addBill() {
		SplitBillModel md = new SplitBillModel();
		md.setBill("Bill");
		md.setTotalAmount("0.00");
		listBill.add(md);
		splitAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PosApp.listOrderData.clear();
		if (listBill.size() > 1) {

			for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
				if (PosApp.listOrderSplit.get(i).getBill().equals("1")) {
					ListOrderModel md = new ListOrderModel();
					md.setQualyti(PosApp.listOrderSplit.get(i).getQualyti());
					DecimalFormat df = new DecimalFormat("0.00");
					amount1 = Double.valueOf(PosApp.listOrderSplit.get(i)
							.getAmount());
					md.setAmount(df.format(amount1) + "");
					md.setDiscount(PosApp.listOrderSplit.get(i).getDiscount());
					md.setItemCode(PosApp.listOrderSplit.get(i).getItemCode());
					md.setItemName(PosApp.listOrderSplit.get(i).getItemName());
					md.setPrice2(PosApp.listOrderSplit.get(i).getPrice2());
					md.setSpecialPrice(PosApp.listOrderSplit.get(i)
							.getSpecialPrice());
					md.setUnitPrice(PosApp.listOrderSplit.get(i).getUnitPrice());
					md.setIsFOC(PosApp.listOrderSplit.get(i).getIsFOC());
					md.setPrice3(PosApp.listOrderSplit.get(i).getPrice3());
					md.setPrice4(PosApp.listOrderSplit.get(i).getPrice4());
					md.setPrice5(PosApp.listOrderSplit.get(i).getPrice5());
					md.setIsGST(PosApp.listOrderSplit.get(i).getIsGST());
					md.setIsSVC(PosApp.listOrderSplit.get(i).getIsSVC());
					md.setGroupName(PosApp.listOrderSplit.get(i).getGroupName());
					md.setGroupCode(PosApp.listOrderSplit.get(i).getGroupCode());
					md.setStandBy(PosApp.listOrderSplit.get(i).getStandBy());
					md.setRemarks(PosApp.listOrderSplit.get(i).getRemarks());
					md.setItemTau(PosApp.listOrderSplit.get(i).getItemCN());
					md.setSale_detailID(PosApp.listOrderSplit.get(i)
							.getSaleDetailID());
					md.setBill(PosApp.listOrderSplit.get(i).getBill());
					md.setId(PosApp.listOrderSplit.get(i).getId());
					md.setTakeaway(PosApp.listOrderSplit.get(i).getTakeaway());
					PosApp.listOrderData.add(md);
				}

			}

			MainActivity ma = new MainActivity();
			ma.notifidataOrderList();
			callAdv();
//			for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
				// updateSplit(PosApp.listOrderSplit.get(i).getSaleDetailID(),
				// PosApp.listOrderSplit.get(i).getBill());
//				updateSplit(PosApp.listOrderSplit.get(i).getSaleDetailID(),
//						PosApp.listOrderSplit.get(i).getBill(),
//						PosApp.listOrderSplit.get(i).getItemName(), "-1",
//						PosApp.listOrderSplit.get(i).getQualyti(),
//						PosApp.listOrderSplit.get(i).getUnitPrice(),
//						PosApp.listOrderSplit.get(i).getDiscount(),
//						PosApp.listOrderSplit.get(i).getAmount(),
//						PosApp.listOrderSplit.get(i).getIsFOC(), UserFunctions
//								.getInstance().getSaleModel().getSaleID(),
//						UserFunctions.getInstance().getUserModel()
//								.getUsername(), PosApp.listOrderSplit.get(i)
//								.getRemarks(), PosApp.listOrderSplit.get(i)
//								.getGroupName(), PosApp.listOrderSplit.get(i)
//								.getSetMenu(), PosApp.listOrderSplit.get(i)
//								.getStandBy(), PosApp.listOrderSplit.get(i)
//								.getItemCode(), PosApp.listOrderSplit.get(i)
//								.getItemCN());
//			}
			// sss();
		}
//		Utilities.getGlobalVariable(ac).isMutilBill=true;
//		SearchBill2Adapter.pos=0;
	}

	@Override
	public void onClick(View v) {
		if (v == btnAddBill) {
			addBill();
		}
		if (v == btnConfirm) {
			DialogSelectQuantity di = new DialogSelectQuantity();
			di.showNoteDialog(ac, ac);
		}
		if (v == imgBack) {
			ac.finish();
		}
		if (v == btnCancel) {
			ac.finish();
		}

	}

	private void sss() {
		for (int f = 1; f <= listBill.size(); f++) {
			double subtotal = 0;
			double svc = 0;
			double gst = 0;
			String tong = "";
			for (int i = 0; i < PosApp.listOrderSplit.size(); i++) {
				if (PosApp.listOrderSplit.get(i).getBill()
						.contains(String.valueOf(f))) {

					subtotal += CalculationUtils.calculateSubTotalSplit(
							PosApp.listOrderSplit, String.valueOf(f));

					svc += CalculationUtils.calculateServiceChargeSplit(
							PosApp.listOrderSplit, String.valueOf(f));

					gst += CalculationUtils.calculateGSTChargeSplit(
							PosApp.listOrderSplit, String.valueOf(f));

				}
				tong = Utils.lamtronTong(subtotal + svc + gst);
			}

			Utils.splitBill(tong, "0", "0", tong, tong, String.valueOf(gst),
					String.valueOf(svc), "", "0", "1",
					Utilities.getGlobalVariable(ac).numberCustomer, ac,
					String.valueOf(subtotal), UserFunctions.getInstance()
							.getSaleModel().getSaleID(), "0", "", "", "", "",
					String.valueOf(f));
		}
	}

	private void updateSplit(String saleDetailID, String bill, String item,
			String item_id, String quantity, String salePrice,
			String Disamount, String subtotal, String foc, String sale_id,
			String user, String remark, String group, String set_menu,
			String status, String item_code, String itemcn) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateSplit"));
		params.add(new BasicNameValuePair("saleDetailID", saleDetailID));
		params.add(new BasicNameValuePair("bill", bill));
		params.add(new BasicNameValuePair("item", item));
		params.add(new BasicNameValuePair("item_id", item_id));
		params.add(new BasicNameValuePair("quantity", quantity));
		params.add(new BasicNameValuePair("selling_price", salePrice));
		params.add(new BasicNameValuePair("discount_amount", Disamount));
		params.add(new BasicNameValuePair("subtotal", subtotal));
		params.add(new BasicNameValuePair("foc", foc));
		params.add(new BasicNameValuePair("sale_id", sale_id));
		params.add(new BasicNameValuePair("user", user));
		params.add(new BasicNameValuePair("remark", remark));
		params.add(new BasicNameValuePair("group", group));
		params.add(new BasicNameValuePair("set_menu", set_menu));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("item_code", item_code));
		params.add(new BasicNameValuePair("itemCN", itemcn));

		UserFunctions.getInstance().deleteItem(ac, params, false);
	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateMitilBill"));
		params.add(new BasicNameValuePair("table", Utilities
				.getGlobalVariable(ac).tableNum));
		UserFunctions.getInstance().callListUser(ac, params, false);
	}
}

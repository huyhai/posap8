package com.pos.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.ListOrderAdapter;
import com.pos.adapter.ListRefundAdapter;
import com.pos.adapter.MainCateAdapter;
import com.pos.common.Utilities;
import com.pos.db.MainCateDataSource;
import com.pos.db.RefundDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;

public class DialogRefund {
	private RelativeLayout rlItemCode;
	private RelativeLayout rlItemName;
	private RelativeLayout rlUnitPrice;
	private RelativeLayout rlQty;
	private RelativeLayout rlDiscount;
	private RelativeLayout rlAmount;
	private RelativeLayout rlLeft;
	// private RelativeLayout rlRight;
	private RelativeLayout rlALLRL;
	private EditText edSearch;
	// private Button btn;
	private ListRefundAdapter adapter;
	private ListView lvRefund;
	public static int qly = -1;
	private ArrayList<ListOrderModel> listRefund = new ArrayList<ListOrderModel>();

	// private EditText edQty;
	// private EditText edReasion;

	public void showDialogAddCate(final Context context, final Activity ac) {
	}

	private Activity ac;
	private Dialog dialogEditItems;
	private Boolean isShowDialog = false;
	private RelativeLayout rlOk;
	private RelativeLayout rlCancel;
	private Button btnSearch;

	public void showDialogSelectImg(Activity _ac) {
		ac = _ac;
		// custom dialog
		if (!isShowDialog) {
			isShowDialog = true;
			// imgSet = _imgSet;
			dialogEditItems = new Dialog(ac);
			dialogEditItems.getWindow();
			dialogEditItems.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogEditItems.setContentView(R.layout.refund_dialog);
			dialogEditItems.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			// dialogEditItems.setTitle("Title...");

			// set the custom dialog components - text, image and button

			rlAmount = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlAmount);
			rlDiscount = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlDiscount);
			rlItemCode = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlItemCode);
			rlItemName = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlItemName);
			rlQty = (RelativeLayout) dialogEditItems.findViewById(R.id.rlQty);
			rlUnitPrice = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlUnitPrice);
			rlLeft = (RelativeLayout) dialogEditItems.findViewById(R.id.rlLeft);
			// rlRight = (RelativeLayout) dialogEditItems
			// .findViewById(R.id.rlRight);
			rlALLRL = (RelativeLayout) dialogEditItems.findViewById(R.id.rlBu);
			// btn = (Button) dialogEditItems.findViewById(R.id.btn);
			lvRefund = (ListView) dialogEditItems.findViewById(R.id.lvRefund);
			// edQty = (EditText) dialogEditItems.findViewById(R.id.edQty);
			edSearch = (EditText) dialogEditItems.findViewById(R.id.edSearch);
			rlOk = (RelativeLayout) dialogEditItems.findViewById(R.id.rlOk);
			rlCancel = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlCancel);
			btnSearch = (Button) dialogEditItems.findViewById(R.id.btnSearch);
			if (PosApp.listOrderData != null) {
				PosApp.listOrderData.clear();
			}
			adapter = new ListRefundAdapter(ac, listRefund);
			lvRefund.setAdapter(adapter);
			rlOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// if (qly >= 0) {
					// ListOrderModel md = new ListOrderModel();
					// md.setPrice2(PosApp.listOrderData.get(qly).getPrice2());
					// md.setSpecialPrice(PosApp.listOrderData.get(qly)
					// .getSpecialPrice());
					// md.setDiscount(PosApp.listOrderData.get(qly)
					// .getDiscount());
					// md.setItemCode(PosApp.listOrderData.get(qly)
					// .getItemCode());
					// md.setItemName(PosApp.listOrderData.get(qly)
					// .getItemName());
					// md.setUnitPrice(PosApp.listOrderData.get(qly).getUnitPrice());
					// md.setQualyti(edQty.getText().toString());
					// md.setAmount("-"
					// + String.valueOf(Double
					// .parseDouble(PosApp.listOrderData.get(
					// qly).getUnitPrice())
					// * Double.parseDouble(md.getQualyti())
					// - Double.parseDouble(md.getDiscount())));
					// PosApp.listOrderData.set(qly, md);
					Utilities.getGlobalVariable(ac).oldVl = CalculationUtils
							.calculateSubTotal(listRefund);
					MainActivity main = new MainActivity();
					PosApp.listOrderData.clear();
					PosApp.listOrderData.addAll(listRefund);

					main.notifidataOrderList();
					MainActivity.refund = true;
					dialogEditItems.dismiss();
					// }

				}
			});
			rlCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogEditItems.dismiss();

				}
			});
			((CustomFragmentActivity) ac).setWidth(rlDiscount, 10);
			((CustomFragmentActivity) ac).setWidth(rlItemCode, 9);
			((CustomFragmentActivity) ac).setWidth(rlQty, 25);
			((CustomFragmentActivity) ac).setWidth(rlUnitPrice, 10);
			((CustomFragmentActivity) ac).setWidth(rlItemName, 6);
			((CustomFragmentActivity) ac).setWidth(rlLeft, 1.5);
			((CustomFragmentActivity) ac).setWidthHeight(rlALLRL, 1.5, 1.3);
			// ((CustomFragmentActivity) ac).setWidth(rlRight, 1);

			btnSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RefundDataSource data = new RefundDataSource(ac, ac);
					int a = data.getSaleID(edSearch.getText().toString());
					// if (PosApp.listOrderData != null)
					// PosApp.listOrderData.clear();
					listRefund = data.loadItemsBill(String.valueOf(a));
					adapter.setItemList(listRefund);
					adapter.notifyDataSetChanged();

				}
			});

			edSearch.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					// RefundDataSource data = new RefundDataSource(ac, ac);
					// int a = data.getSaleID(edSearch.getText().toString());
					// if (PosApp.listOrderData != null)
					// PosApp.listOrderData.clear();
					// PosApp.listOrderData =
					// data.loadItemsBill(String.valueOf(a));
					// adapter.setItemList(PosApp.listOrderData);
					// adapter.notifyDataSetChanged();
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}
			});

			dialogEditItems.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					isShowDialog = false;
				}
			});

			dialogEditItems.show();
		}

	}

}

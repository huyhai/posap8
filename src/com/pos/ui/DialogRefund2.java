package com.pos.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.SearchBillAdapter;
import com.pos.common.Utilities;
import com.pos.db.ItemsDataSource;
import com.pos.db.MainCateDataSource;
import com.pos.db.SearchBillDataSource;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.SearchBillModel;

public class DialogRefund2 {
	private ListView lvSearch;
	private RelativeLayout rlRecieptNo;
	private RelativeLayout rlDateTime;
	private Activity ac;
	public static ArrayList<SearchBillModel> list = new ArrayList<SearchBillModel>();
	SearchBillAdapter adapter;
	public static int a = -1;
	public static String discountUn;
	public static String recepit;

	public void showDialogEditSale(final Context context, final Activity _ac) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle("Refund");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_refund2, null);
		builder.setView(v);
		initView(v);
		initData();

		builder.setNeutralButton(ac.getString(R.string.str_confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int id) {
				if (DialogRefund2.a != -1) {
					SearchBillDataSource dataSource = new SearchBillDataSource(
							ac, ac);
					PosApp.listOrderData = dataSource.loadItemsBill(list.get(a)
							.getSaleID());
					MainActivity ma = new MainActivity();
					ma.notifidataOrderList();
				}

			}
		});

		builder.setPositiveButton("Cancel",
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
//		((CustomFragmentActivity) ac).setWidth(rlRecieptNo, 7);
//		((CustomFragmentActivity) ac).setWidth(rlDateTime, 5);
		adapter = new SearchBillAdapter(ac, list);
		lvSearch.setAdapter(adapter);
		if (list != null)
			list.clear();

		SearchBillDataSource dataSource = new SearchBillDataSource(ac, ac);
		list = dataSource.loadItems();
		adapter.setItemList(list);
		adapter.notifyDataSetChanged();
	}

	private void initView(View v) {
		lvSearch = (ListView) v.findViewById(R.id.lvSearch);
		rlRecieptNo = (RelativeLayout) v.findViewById(R.id.rlRecieptNo);
		rlDateTime = (RelativeLayout) v.findViewById(R.id.rlDateTime);
		// tvTitle1 = (TextView) v.findViewById(R.id.tvTitle1);
		// spinner = (Spinner) v.findViewById(R.id.spColor);
		// acc = (CheckBox) v.findViewById(R.id.checkbox_active);
		// inac = (CheckBox) v.findViewById(R.id.checkbox_inavtice);
		// rNameChina= (RelativeLayout) v.findViewById(R.id.rNameChina);
		// rDesCn= (RelativeLayout) v.findViewById(R.id.rDesCn);

	}

}

package com.pos.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.db.ItemsDataSource;
import com.pos.db.SearchBillDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.SearchBillModel;
import com.pos.model.SplitBillModel;
import com.pos.ui.DialogItems;
import com.pos.ui.DialogSearchBill;
import com.pos.ui.Items;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchBillAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<SearchBillModel> itemList;

	static class ViewHolder {
		RelativeLayout rlRecieptNo;
		RelativeLayout rlDateTime;
		TextView tvRecieptNo;
		TextView tvDateTime;
		TextView tvTotalAmount;
		LinearLayout llTop;
	}

	public SearchBillAdapter(final Activity _ac,
			ArrayList<SearchBillModel> _list) {
		this.itemList = _list;
		this.ac = _ac;
	}

	public SearchBillAdapter(final Activity _ac) {
		this.ac = _ac;
	}

	public void setItemList(ArrayList<SearchBillModel> countryList) {
		this.itemList = new ArrayList<SearchBillModel>(countryList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(ac);
			convertView = inflater.inflate(R.layout.item_searchbill, null);
			holder.rlRecieptNo = (RelativeLayout) convertView
					.findViewById(R.id.rlRecieptNo);
			holder.rlDateTime = (RelativeLayout) convertView
					.findViewById(R.id.rlDateTime);
			holder.tvDateTime = (TextView) convertView
					.findViewById(R.id.tvDateTime);
			holder.tvRecieptNo = (TextView) convertView
					.findViewById(R.id.tvRecieptNo);
			holder.tvTotalAmount = (TextView) convertView
					.findViewById(R.id.tvTotalAmount);
			holder.llTop = (LinearLayout) convertView.findViewById(R.id.llTop);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ((CustomFragmentActivity) ac).setWidth(holder.rlRecieptNo, 7);
		// ((CustomFragmentActivity) ac).setWidth(holder.rlDateTime, 5);
//		holder.tvDateTime.setText(itemList.get(position).getSales_date());
//		holder.tvRecieptNo.setText(itemList.get(position).getReceipt_no());
//		holder.tvTotalAmount.setText(itemList.get(position).getTotal_amount());
//		if (itemList.get(position).getStatus().equals("1")) {
//			holder.llTop.setBackgroundColor(Color.parseColor("#055596"));
//		} else {
//			holder.llTop.setBackgroundColor(Color.parseColor("#e2f4fd"));
//		}
//		if (position == DialogSearchBill.a) {
//			holder.llTop.setBackgroundResource(R.color.milky);
//		}
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				DialogSearchBill.a = position;
//				notifyDataSetChanged();
//			}
//		});
//		convertView.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				showDialogOK(ac.getString(R.string.str_YouChoose)
//						+ itemList.get(position).getReceipt_no(), position);
//				return false;
//			}
//		});

		return convertView;
	}

//	public void showDialogOK(final String message, final int pos) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
//		builder.setMessage(message)
//				.setCancelable(false)
//				.setPositiveButton(ac.getString(R.string.str_delete),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								SearchBillDataSource data = new SearchBillDataSource(
//										ac, ac);
//								int  a=data.deleteItem(itemList.get(pos).getSaleID());
//								int  ak=data.deleteItemAll(itemList.get(pos).getSaleID());
//								DialogSearchBill di=new DialogSearchBill();
//								di.notifi();
//
//							}
//						});
//		builder.setNegativeButton(ac.getString(R.string.str_cancel),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//
//					}
//				});
//		AlertDialog alert = builder.create();
//		alert.setCancelable(false);
//		alert.setCanceledOnTouchOutside(false);
//		alert.show();
//	}
}

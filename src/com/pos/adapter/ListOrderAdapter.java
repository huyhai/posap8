package com.pos.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.libs.ViewUtils;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.ui.DialogEditItems;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.serialport.SerialPort;
import android.text.Html;
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
import android.widget.Toast;

public class ListOrderAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<ListOrderModel> itemList;

	private int currentposition;

	static class ViewHolder {
		RelativeLayout rlItemCode;
		TextView tvItemCode;
		RelativeLayout rlItemName;
		TextView tvItemName;
		RelativeLayout rlUnitPrice;
		TextView tvUnitPrice;
		RelativeLayout rlQty;
		TextView tvQty;
		RelativeLayout rlDiscount;
		TextView tvDiscount;
		RelativeLayout rlAmount;
		TextView tvAmount;
		LinearLayout rlAll;
	}

	public ListOrderAdapter(final Activity _ac, ArrayList<ListOrderModel> _list) {
		this.itemList = _list;
		this.ac = _ac;
	}

	public ListOrderAdapter(final Activity _ac) {
		this.ac = _ac;
	}

	public void setItemList(ArrayList<ListOrderModel> countryList) {
		this.itemList = new ArrayList<ListOrderModel>(countryList);
		// MainActivity.qly =-1;
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return itemList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public int getcurrposition() {
		return currentposition;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(ac);
			convertView = inflater.inflate(R.layout.order_item, null);
			holder.rlAmount = (RelativeLayout) convertView
					.findViewById(R.id.rlAmount);
			holder.tvAmount = (TextView) convertView
					.findViewById(R.id.tvAmount);
			holder.rlDiscount = (RelativeLayout) convertView
					.findViewById(R.id.rlDiscount);
			holder.tvDiscount = (TextView) convertView
					.findViewById(R.id.tvDiscount);
			holder.rlItemCode = (RelativeLayout) convertView
					.findViewById(R.id.rlItemCode);
			holder.tvItemCode = (TextView) convertView
					.findViewById(R.id.tvItemCode);
			holder.rlItemName = (RelativeLayout) convertView
					.findViewById(R.id.rlItemName);
			holder.tvItemName = (TextView) convertView
					.findViewById(R.id.tvItemName);
			holder.rlQty = (RelativeLayout) convertView
					.findViewById(R.id.rlQty);
			holder.tvQty = (TextView) convertView.findViewById(R.id.tvQty);
			holder.rlUnitPrice = (RelativeLayout) convertView
					.findViewById(R.id.rlUnitPrice);
			holder.tvUnitPrice = (TextView) convertView
					.findViewById(R.id.tvUnitPrice);
			holder.rlAll = (LinearLayout) convertView.findViewById(R.id.rlAll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ((CustomFragmentActivity) ac).setWidthHeight(holder.rlAll, 1, 16);

		ViewUtils.setWidth(holder.rlDiscount, 14,ac);
		ViewUtils.setWidth(holder.rlQty, 30,ac);
		ViewUtils.setWidth(holder.rlUnitPrice, 14,ac);
		ViewUtils.setWidth(holder.rlItemName, 6,ac);

		holder.tvAmount.setText(itemList.get(position).getAmount());
		holder.tvDiscount.setText(itemList.get(position).getDiscount());
		if (itemList.get(position).getRemarks()==null) {
			holder.tvItemName.setText(Html.fromHtml(itemList.get(position)
					.getItemName()));
		} else {
			holder.tvItemName.setText(Html.fromHtml(itemList.get(position)
					.getItemName()
					+ "<br>"
					+ itemList.get(position).getRemarks()));
		}

		holder.tvQty.setText(itemList.get(position).getQualyti());
		holder.tvUnitPrice.setText(itemList.get(position).getUnitPrice());

		if (itemList.get(position).getStandBy().equals("1")) {
			holder.rlAll.setBackgroundResource(R.color.red);

		} else {
			if (MainActivity.qly == position) {

				holder.rlAll.setBackgroundResource(R.color.milky);
			} else {
				holder.rlAll.setBackgroundResource(R.color.transparent);
			}

		}
		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (Utilities.getGlobalVariable(ac).isPrintBill) {
					Utilities.showDialogNoBack(ac, "Not Allowed").show();
				} else {
					DialogEditItems ab = new DialogEditItems();
					ab.showDialogSelectImg(ac, position);
					MainActivity.qly = position;
					notifyDataSetChanged();
				}

				return false;
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Utilities.getGlobalVariable(ac).isPrintBill) {
					Utilities.showDialogNoBack(ac, "Not Allowed").show();
				} else {
					MainActivity.qly = position;
					currentposition = position;
					notifyDataSetChanged();
				}

				 Toast.makeText(ac, itemList.get(position).getId(),
				 Toast.LENGTH_SHORT).show();
			}
		});

		return convertView;
	}

}

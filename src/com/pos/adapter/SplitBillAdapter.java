package com.pos.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.controllibs.UserFunctions.SaleRemarkModel;
import com.pos.db.CompanyDataSource;
import com.pos.db.ItemsDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.SplitBillModel;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogItems;
import com.pos.ui.Home;
import com.pos.ui.Items;
import com.pos.ui.StyleAndRequestActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData.Item;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.serialport.SerialPort;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplitBillAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<SplitBillModel> itemList;
	public static int pos = -1;

	static class ViewHolder {
		TextView tvBill;
		TextView tvTotalAmount;
		LinearLayout llYY;
	}

	public SplitBillAdapter(final Activity _ac, ArrayList<SplitBillModel> _list) {
		this.itemList = _list;
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public SplitBillAdapter(final Activity _ac) {
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public void setItemList(ArrayList<SplitBillModel> countryList) {
		this.itemList = new ArrayList<SplitBillModel>(countryList);
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
			convertView = inflater.inflate(R.layout.split_bill_item, null);
			holder.tvBill = (TextView) convertView.findViewById(R.id.tvBill);
			holder.tvTotalAmount = (TextView) convertView
					.findViewById(R.id.tvTotalAmount);
			holder.llYY = (LinearLayout) convertView.findViewById(R.id.llYY);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (pos == position) {
			holder.llYY.setBackgroundResource(R.color.milky);
		} else {
			holder.llYY.setBackgroundResource(R.color.transparent);
		}
		int p = position + 1;
		holder.tvBill.setText(itemList.get(position).getBill() +" "+ p);
		holder.tvTotalAmount.setText(itemList.get(position).getTotalAmount());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pos = position;
				notifyDataSetChanged();

			}
		});

		return convertView;
	}

}

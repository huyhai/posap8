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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<String> itemList;
	private static int pos;
	public static String no="";

	static class ViewHolder {
		TextView tvName;
		RelativeLayout d;
	}

	public NoAdapter(final Activity _ac, ArrayList<String> _list) {
		this.itemList = _list;
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public NoAdapter(final Activity _ac) {
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public void setItemList(ArrayList<String> countryList) {
		this.itemList = new ArrayList<String>(countryList);
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
			convertView = inflater.inflate(R.layout.no_item, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.d = (RelativeLayout) convertView.findViewById(R.id.d);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (pos == position) {
			holder.d.setBackgroundResource(R.color.milky);
		} else {
			holder.d.setBackgroundResource(R.color.transparent);
		}
		holder.tvName.setText(itemList.get(position));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StyleAndRequestActivity.ischoosenNoLess) {
					Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests += itemList
							.get(position) + ", ";
//					no=Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests += itemList
//							.get(position) + ", ";;
					pos = position;
					notifyDataSetChanged();
					StyleAndRequestActivity.tvNoVl.setText(Utilities
							.getGlobalVariable(ac).NoLessMoreOnlyRequests);
				} else {
					Toast.makeText(ac, "Choosen Type", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		return convertView;
	}

}

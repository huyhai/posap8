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
import com.pos.Table;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.db.MainCateDataSource;
import com.pos.libs.ViewUtils;
import com.pos.model.MainCateModel;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogItems;
import com.pos.ui.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Table1Adapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<String> itemList;

	static class ViewHolder {
		TextView tvName;
		ImageView row_icon;
		RelativeLayout rlBgIt;
	}

	public Table1Adapter(final Activity _ac, ArrayList<String> _list) {
		this.itemList = _list;
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public Table1Adapter(final Activity _ac) {
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public void setItemList(ArrayList<String> countryList) {
		this.itemList = new ArrayList<String>(countryList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return itemList.size();
		return 48;
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
			convertView = inflater.inflate(R.layout.table1, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.row_title);
			holder.row_icon = (ImageView) convertView
					.findViewById(R.id.row_icon);
			holder.rlBgIt = (RelativeLayout) convertView
					.findViewById(R.id.rlBgIt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ViewUtils.setHeight(holder.rlBgIt, 10, ac);
		holder.tvName.setText("Table "+(position+1));
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ac, MainActivity.class);
				ac.startActivity(i);
			}
		});
		return convertView;
	}

}

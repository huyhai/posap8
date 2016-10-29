package com.pos.adapter;

import java.util.ArrayList;
import java.util.List;

import com.pos.R;
import com.pos.controllibs.UserFunctions.TableModel;
import com.pos.controllibs.UserFunctions.SaleRemarkModel;
import com.pos.model.MainCateModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapterMy extends ArrayAdapter {

	private Activity context;
	private ArrayList<MainCateModel> itemList;

	public SpinnerAdapterMy(Activity context, int textViewResourceId,
			ArrayList<MainCateModel> arrayList) {

		super(context, textViewResourceId);
		this.context = context;
		this.itemList = arrayList;
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
	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater =this.context.getLayoutInflater();
		View row = inflater.inflate(R.layout.aii, parent, false);
		TextView make = (TextView) row.findViewById(R.id.tbii);
		make.setText(itemList.get(position).get_Name());
		return row;
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater =this.context. getLayoutInflater();
		View row = inflater.inflate(R.layout.amm, parent, false);
		TextView make = (TextView) row.findViewById(R.id.spinner_item_dropdown);
		make.setText(itemList.get(position).get_Name());
		return row;
	}

}
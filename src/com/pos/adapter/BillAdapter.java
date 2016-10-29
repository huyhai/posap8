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
import com.pos.common.Utilities;
import com.pos.model.BillModel;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.SearchBillModel;
import com.pos.ui.DialogEditItems;
import com.pos.ui.DialogSearchBill;
import com.pos.ui.DialogTicketReport;
import com.pos.ui.Items;

import android.app.Activity;
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

public class BillAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<BillModel> BillList;
	

	static class ViewHolder {
		LinearLayout rlBillNo;
		LinearLayout rlCounter;
		LinearLayout rlClose;
		LinearLayout rlTotalAmount;
		LinearLayout rlCash;
		LinearLayout rlCard;
		LinearLayout rlGST;
		LinearLayout rlUser;
		
		TextView tvtBill;
		TextView tvtCounter;
		TextView tvtClose;
		TextView tvtTotalAmount;
		TextView tvtCash;
		TextView tvtCard;
		TextView tvtGST;
		TextView tvtUser;
		
		RelativeLayout llEnd;
	}

	public BillAdapter(final Activity _ac,
			ArrayList<BillModel> _list) {
		this.BillList = _list;
		this.ac = _ac;
	}

	public BillAdapter(final Activity _ac) {
		this.ac = _ac;
	}

	public void setItemList(ArrayList<BillModel> countryList) {
		this.BillList = new ArrayList<BillModel>(countryList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return BillList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return BillList.get(arg0);
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
			convertView = inflater.inflate(R.layout.item_bill, null);
			holder.rlBillNo = (LinearLayout) convertView
					.findViewById(R.id.rltBillNo);
			holder.rlCounter = (LinearLayout) convertView
					.findViewById(R.id.rltCounter);
			holder.rlClose = (LinearLayout) convertView
					.findViewById(R.id.rltClose);
			holder.rlTotalAmount = (LinearLayout) convertView
					.findViewById(R.id.rltTotalAmount);
			holder.rlCash = (LinearLayout) convertView
					.findViewById(R.id.rltCash);
			holder.rlCard = (LinearLayout) convertView
					.findViewById(R.id.rltCard);
			holder.rlGST = (LinearLayout) convertView
					.findViewById(R.id.rltGST);
			holder.rlUser = (LinearLayout) convertView
					.findViewById(R.id.rltUser);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvtBill=(TextView)convertView.findViewById(R.id.tvtBill);
		holder.tvtCounter=(TextView)convertView.findViewById(R.id.tvtCounter);
		holder.tvtClose=(TextView)convertView.findViewById(R.id.tvtClose);
		holder.tvtTotalAmount=(TextView)convertView.findViewById(R.id.tvtTotalAmount);
		holder.tvtCash=(TextView)convertView.findViewById(R.id.tvtCash);
		holder.tvtCard=(TextView)convertView.findViewById(R.id.tvtCard);
		holder.tvtGST=(TextView)convertView.findViewById(R.id.tvtGST);
		holder.tvtUser=(TextView)convertView.findViewById(R.id.tvtUser);
		holder.llEnd=(RelativeLayout)convertView.findViewById(R.id.rlShow1);
		
		
		holder.tvtBill.setText(BillList.get(position).getReceipt_no());
		holder.tvtCounter.setText(BillList.get(position).getCounter());
		holder.tvtClose.setText(BillList.get(position).getClosesession());
		holder.tvtTotalAmount.setText(BillList.get(position).getTotalamount());
		holder.tvtCash.setText(BillList.get(position).getCash());
		holder.tvtCard.setText(BillList.get(position).getCard());
		holder.tvtGST.setText(BillList.get(position).getGST());
		holder.tvtUser.setText(BillList.get(position).getUser());
//		holder.llEnd.setBackgroundColor(Color.BLACK);
//		holder.tvtBill.setBackgroundColor(Color.WHITE);
//		holder.tvtCounter.setBackgroundColor(Color.WHITE);
//		holder.tvtClose.setBackgroundColor(Color.WHITE);
//		holder.tvtTotalAmount.setBackgroundColor(Color.WHITE);
//		holder.tvtCash.setBackgroundColor(Color.WHITE);
//		holder.tvtCard.setBackgroundColor(Color.WHITE);
//		holder.tvtGST.setBackgroundColor(Color.WHITE);
//		holder.tvtUser.setBackgroundColor(Color.WHITE);
//		holder.llEnd.setBackgroundColor(Color.WHITE);
		
		if (position == DialogTicketReport.a) {
//			holder.llEnd.setBackgroundColor(Color.RED);
			holder.tvtBill.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtCounter.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtClose.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtTotalAmount.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtCash.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtCard.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtGST.setBackgroundColor(Color.parseColor("#C4C4C4"));
			holder.tvtUser.setBackgroundColor(Color.parseColor("#C4C4C4"));
//			holder.llEnd.setBackgroundColor(Color.RED);
		}else{
			holder.tvtBill.setBackgroundColor(Color.WHITE);
			holder.tvtCounter.setBackgroundColor(Color.WHITE);
			holder.tvtClose.setBackgroundColor(Color.WHITE);
			holder.tvtTotalAmount.setBackgroundColor(Color.WHITE);
			holder.tvtCash.setBackgroundColor(Color.WHITE);
			holder.tvtCard.setBackgroundColor(Color.WHITE);
			holder.tvtGST.setBackgroundColor(Color.WHITE);
			holder.tvtUser.setBackgroundColor(Color.WHITE);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogTicketReport.a = position;
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

}

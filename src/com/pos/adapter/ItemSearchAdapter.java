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
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.ui.Items;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemSearchAdapter extends ArrayAdapter<ItemsModel> implements
		Filterable {
	private Activity ac;
	public ArrayList<ItemsModel> itemList;
	private LayoutInflater vi;
	private static int mResource = R.layout.items_item;

	static class ViewHolder {
		TextView tvName;
		ImageView row_icon;
		RelativeLayout rlBgIt;
		RelativeLayout rlBgIt1;
		TextView tvPrice;
	}

	public ItemSearchAdapter(Activity activity, final ArrayList<ItemsModel> items) {
		super(activity, mResource, items);
		this.itemList = items;
		this.ac = activity;
	}


	public void setItemList(ArrayList<ItemsModel> countryList) {
		this.itemList = new ArrayList<ItemsModel>(countryList);
	}

	public ArrayList<ItemsModel> getItemList() {
		return this.itemList;
		// notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public ItemsModel getItem(int arg0) {
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
			convertView = inflater.inflate(R.layout.items_item_search, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.row_title);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			holder.row_icon = (ImageView) convertView
					.findViewById(R.id.row_icon);
			holder.rlBgIt = (RelativeLayout) convertView
					.findViewById(R.id.rlBgIt);
			holder.rlBgIt1 = (RelativeLayout) convertView
					.findViewById(R.id.rlBgIt1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			((CustomFragmentActivity) ac).setWidthHeight(holder.rlBgIt, 10, 6);
			((CustomFragmentActivity) ac).setWidthHeight(holder.row_icon, 25,
					17);
			holder.row_icon.setBackgroundResource(R.drawable.icon_back);
			holder.rlBgIt.setBackgroundResource(R.color.transparent);
		} else {
			((CustomFragmentActivity) ac).setWidthHeight(holder.rlBgIt, 10, 6);
			((CustomFragmentActivity) ac).setWidthHeight(holder.row_icon, 1, 1);
			if (itemList.get(position).getItem_image().endsWith("red")) {
				holder.row_icon.setBackgroundColor(Color.RED);
			} else if (itemList.get(position).getItem_image()
					.endsWith("yellow")) {
				holder.row_icon.setBackgroundColor(Color.YELLOW);
			} else if (itemList.get(position).getItem_image().endsWith("blue")) {
				holder.row_icon.setBackgroundColor(Color.BLUE);
			} else if (itemList.get(position).getItem_image().endsWith("green")) {
				holder.row_icon.setBackgroundColor(Color.GREEN);
			} else if (itemList.get(position).getItem_image().endsWith("black")) {
				holder.row_icon.setBackgroundColor(Color.BLACK);
			} else if (itemList.get(position).getItem_image().endsWith("white")) {
				holder.row_icon.setBackgroundColor(Color.WHITE);
			} else if (itemList.get(position).getItem_image()
					.endsWith("magenta")) {
				holder.row_icon.setBackgroundColor(Color.MAGENTA);
			} else if (itemList.get(position).getItem_image().endsWith("cyan")) {
				holder.row_icon.setBackgroundColor(Color.CYAN);
			} else {
				PosApp.imageLoader2.displayImage("file:///"
						+ itemList.get(position).getItem_image(),
						holder.row_icon, CustomFragmentActivity.options);
				// File file = new File(itemList.get(position).getItem_image());
				// holder.row_icon.setImageBitmap(decodeFile(file));
			}
		}

		holder.tvName.setText(itemList.get(position).getName());

		holder.tvPrice.setText(itemList.get(position).getSelling_price_1());
		holder.rlBgIt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					((CustomFragmentActivity) ac).onBackPressed();
				} else {
//					MainActivity.btnDisCountValue.setText("0.00");
					if (Utilities.getGlobalVariable(ac).isPOC == true) {
						ListOrderModel md = new ListOrderModel();
						if (Items.num != "") {
							md.setQualyti(Items.num);
						} else {
							md.setQualyti("1");
						}

						md.setAmount("0");
						md.setDiscount("0");
						md.setItemCode(itemList.get(position).getItem_code());
						md.setItemName(itemList.get(position).getName()
								+ "(FOC)");
						md.setPrice2(itemList.get(position)
								.getSelling_price_2());
						md.setSpecialPrice(itemList.get(position)
								.getSpecial_price());
						md.setUnitPrice("0");
						PosApp.listOrderData.add(md);

						MainActivity main = new MainActivity();
						main.notifidataOrderList();
						Items.num = "";
						MainActivity.qly = -1;
						Utilities.getGlobalVariable(ac).isPOC = false;

					} else {
						ListOrderModel md = new ListOrderModel();
						String amount;
						if (Items.num != "") {
							amount = String.valueOf(Double.parseDouble(itemList
									.get(position).getSelling_price_1())
									* Double.parseDouble(Items.num));
							md.setQualyti(Items.num);
						} else {
							amount = String.valueOf(Double.parseDouble(itemList
									.get(position).getSelling_price_1()) * 1);
							md.setQualyti("1");
						}

						md.setAmount(amount);
						md.setDiscount("0");
						md.setItemCode(itemList.get(position).getItem_code());
						md.setItemName(itemList.get(position).getName());
						md.setPrice2(itemList.get(position)
								.getSelling_price_2());
						md.setSpecialPrice(itemList.get(position)
								.getSpecial_price());
						md.setUnitPrice(itemList.get(position)
								.getSelling_price_1());
						PosApp.listOrderData.add(md);

						MainActivity main = new MainActivity();
						main.notifidataOrderList();
						Items.num = "";
						MainActivity.qly = -1;

					}

				}

			}
		});

		return convertView;
	}

	ArrayList<ItemsModel> mOriginalValues;

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				itemList = (ArrayList<ItemsModel>) results.values;
				Log.v("HAI", itemList.size()+"");
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<ItemsModel> FilteredArrList = new ArrayList<ItemsModel>();
				ItemsModel vt1 = new ItemsModel();
				vt1.setName("");
				FilteredArrList.add(vt1);
				if (mOriginalValues == null) {
					mOriginalValues = new ArrayList<ItemsModel>(itemList);
//					Log.v("HAI", mOriginalValues.size()+"");
				}
				if (constraint == null || constraint.length() == 0) {
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
					((CustomFragmentActivity)ac).onBackPressed();
				} else {
					constraint = constraint.toString().toLowerCase();
//					Log.v("HAI",constraint+"");
					for (int i = 0; i < mOriginalValues.size(); i++) {
						String data = "";
						try {
							 data = mOriginalValues.get(i).getName().trim();
						} catch (Exception e) {
							Log.v("HAI", "loi: "+e);
						}
					
						if (data.toLowerCase()
								.startsWith(constraint.toString())) {
							ItemsModel vt = new ItemsModel();
							vt.setBarcode(mOriginalValues.get(i).getBarcode());
							vt.setCost_price(mOriginalValues.get(i)
									.getCost_price());
							vt.setItem_code(mOriginalValues.get(i)
									.getItem_code());
							vt.setItem_group_ID(mOriginalValues.get(i)
									.getItem_group_ID());
							vt.setItem_image(mOriginalValues.get(i)
									.getItem_image());
							vt.setItemID(mOriginalValues.get(i).getItemID());
							vt.setName(mOriginalValues.get(i).getName());
							vt.setRemarks(mOriginalValues.get(i).getRemarks());
							vt.setSelling_price_1(mOriginalValues.get(i)
									.getSelling_price_1());
							vt.setSelling_price_2(mOriginalValues.get(i)
									.getSelling_price_2());
							vt.setSpecial_price(mOriginalValues.get(i)
									.getSpecial_price());
							vt.setUom(mOriginalValues.get(i).getUom());
							FilteredArrList.add(vt);
						}
					}
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				Log.v("HAI", results.count+"");
				return results;
			}
		};
		return filter;
	}

}

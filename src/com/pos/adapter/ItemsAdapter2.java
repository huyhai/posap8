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

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.db.CompanyDataSource;
import com.pos.db.ItemsDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.ItemsModel2;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogItems;
import com.pos.ui.Home;
import com.pos.ui.Items;

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

public class ItemsAdapter2 extends BaseAdapter {
	private Activity ac;
	public ArrayList<ItemsModel2> itemList;
	private SerialPort mSerialPort_V;
	private OutputStream mOutputStream_V;
	private Application mApplication;
	double amount1 = 0;

	static class ViewHolder {
		TextView tvName;
		ImageView row_icon;
		RelativeLayout rlBgIt;
		RelativeLayout rlBgIt1;
		TextView tvPrice;
	}

	public ItemsAdapter2(final Activity _ac, ArrayList<ItemsModel2> _list) {
		this.itemList = _list;
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public ItemsAdapter2(final Activity _ac) {
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public void setItemList(ArrayList<ItemsModel2> countryList) {
		this.itemList = new ArrayList<ItemsModel2>(countryList);
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
			convertView = inflater.inflate(R.layout.items_item2, null);
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
		LayoutParams lpimgAbout = holder.rlBgIt.getLayoutParams();
		lpimgAbout.width = (int) (Utilities.getGlobalVariable(ac).device_width / 10);
		lpimgAbout.height = (int) (Utilities.getGlobalVariable(ac).device_height / 6);
		holder.rlBgIt.setLayoutParams(lpimgAbout);
		if (position == 0) {
			((CustomFragmentActivity) ac).setWidthHeight(holder.row_icon, 25,
					17);
			holder.row_icon.setBackgroundResource(R.drawable.icon_back);
			holder.rlBgIt.setBackgroundResource(R.color.transparent);
		} else {
			if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.RED)) {
				holder.row_icon.setBackgroundColor(Color.RED);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.YELLOW)) {
				holder.row_icon.setBackgroundColor(Color.YELLOW);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.BLUE)) {
				holder.row_icon.setBackgroundColor(Color.BLUE);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.GREEN)) {
				holder.row_icon.setBackgroundColor(Color.GREEN);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.BLACK)) {
				holder.row_icon.setBackgroundColor(Color.BLACK);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.WHITE)) {
				holder.row_icon.setBackgroundColor(Color.WHITE);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.MAGENTA)) {
				holder.row_icon.setBackgroundColor(Color.MAGENTA);
			} else if (itemList.get(position).getItem_image()
					.endsWith(ConstantValue.CYAN)) {
				holder.row_icon.setBackgroundColor(Color.CYAN);
			} else {
				PosApp.imageLoader2.displayImage(itemList.get(position)
						.getItem_image(), holder.row_icon,
						CustomFragmentActivity.options);
				// File file = new File(itemList.get(position).getItem_image());
				// holder.row_icon.setImageBitmap(decodeFile(file));
			}
			holder.tvPrice.setBackgroundColor(Color.WHITE);
			if(MainActivity.langid==1){
				holder.tvName.setText(itemList.get(position).getName());
			}else{
				holder.tvName.setText(itemList.get(position).getNameTau());
			}
			if (!itemList.get(position).getSelling_price_1().equals("null")) {
				holder.tvPrice.setText(itemList.get(position)
						.getSelling_price_1());
			} else {
				holder.tvPrice.setText("");
			}
		}
		// if (itemList.get(position).getTextSize() != null) {
		// if (itemList.get(position).getTextSize().endsWith("small")) {
		// holder.tvName.setTextAppearance(ac,
		// android.R.style.TextAppearance_Small);
		// } else if (itemList.get(position).getTextSize().endsWith("medium")) {
		// holder.tvName.setTextAppearance(ac,
		// android.R.style.TextAppearance_Medium);
		// } else {
		// holder.tvName.setTextAppearance(ac,
		// android.R.style.TextAppearance_Large);
		// }
		// } else {
		// // holder.tvName.setTextAppearance(ac,
		// // android.R.style.TextAppearance_Medium);
		// }

		holder.rlBgIt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (position == 0) {
					((CustomFragmentActivity) ac).onBackPressed();
				} else {
					String name;
					if(MainActivity.langid==1){
						name=itemList.get(position).getName();
					}else{
						name=itemList.get(position).getNameTau();
					}
					// MainActivity.btnDisCountValue.setText("0.00");
					if (Utilities.getGlobalVariable(ac).isPOC == true) {
						Utils.addItemFOC(itemList.get(position).getItem_code(),
								name,
								itemList.get(position).getSelling_price_2(),
								itemList.get(position).getSpecial_price(), ac,
								itemList.get(position).getSelling_price_3(),
								itemList.get(position).getSelling_price_4(),
								itemList.get(position).getIsGST(), itemList
										.get(position).getIsService(), itemList
										.get(position).getGroupname(), itemList
										.get(position).getGroupCode(), itemList
										.get(position).getSelling_price_5(),
								itemList.get(position).getRemarks(), "0",
								itemList.get(position).getNameTau(), itemList
										.get(position).getSale_detailID(), "1",
								itemList.get(position).getItemID(),"0");

					} else {
						Utils.addItemToList(itemList.get(position)
								.getSelling_price_1(), itemList.get(position)
								.getItem_code(),name, itemList.get(position)
								.getSelling_price_2(), itemList.get(position)
								.getSpecial_price(), itemList.get(position)
								.getSelling_price_3(), itemList.get(position)
								.getSelling_price_4(), itemList.get(position)
								.getIsGST(), itemList.get(position)
								.getIsService(), itemList.get(position)
								.getGroupname(), itemList.get(position)
								.getGroupCode(), itemList.get(position)
								.getSelling_price_5(), itemList.get(position)
								.getRemarks(), "0", itemList.get(position)
								.getNameTau(), itemList.get(position)
								.getSale_detailID(), "1", itemList
								.get(position).getItemID(),
								"0");
					}
				}
				Items.num = "";
				MainActivity ac = new MainActivity();
				ac.scrollMyListViewToBottom();
			}
		});
//		holder.rlBgIt1.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				showDialogOK(ac.getString(R.string.str_YouChoose)
//						+ itemList.get(position).getName(), position);
//				return false;
//			}
//		});

		return convertView;
	}

	public void showDialogOK(final String message, final int pos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(ac.getString(R.string.str_inActive),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ItemsDataSource a = new ItemsDataSource(ac, ac);
								String s = a.DelelteItem(itemList.get(pos)
										.getItemID());

								((CustomFragmentActivity) ac).replaceFragment(
										ConstantValue.ITEMS, false);

							}
						});
		builder.setNeutralButton(ac.getString(R.string.str_Edit),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogItems ditem = new DialogItems();
						ditem.showDialogAddItem(ac, ac, true, itemList.get(pos)
								.getItemID());
						dialog.dismiss();

					}
				});
		builder.setNegativeButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

}

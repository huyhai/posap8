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
import com.pos.db.MainCateDataSource;
import com.pos.model.MainCateModel;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogItems;
import com.pos.ui.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MainCateAdapter extends BaseAdapter {
	private Activity ac;
	public ArrayList<MainCateModel> itemList;

	static class ViewHolder {
		TextView tvName;
		ImageView row_icon;
		RelativeLayout rlBgIt;
	}

	public MainCateAdapter(final Activity _ac, ArrayList<MainCateModel> _list) {
		this.itemList = _list;
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public MainCateAdapter(final Activity _ac) {
		this.ac = _ac;
		// imageLoader = new ImageLoader(context);
	}

	public void setItemList(ArrayList<MainCateModel> countryList) {
		this.itemList = new ArrayList<MainCateModel>(countryList);
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
			convertView = inflater.inflate(R.layout.row, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.row_title);
			holder.row_icon = (ImageView) convertView
					.findViewById(R.id.row_icon);
			holder.rlBgIt = (RelativeLayout) convertView
					.findViewById(R.id.rlBgIt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LayoutParams lpimgAbout = holder.rlBgIt.getLayoutParams();
		lpimgAbout.width = (int) (Utilities.getGlobalVariable(ac).device_width / 10);
		lpimgAbout.height = (int) (Utilities.getGlobalVariable(ac).device_height / 6);
		holder.rlBgIt.setLayoutParams(lpimgAbout);
		//
		// LayoutParams lpholderrow_icon = holder.row_icon.getLayoutParams();
		// lpholderrow_icon.width = (int)
		// (Utilities.getGlobalVariable(ac).device_width / 15);
		// lpholderrow_icon.height = (int)
		// (Utilities.getGlobalVariable(ac).device_height / 6);
		// holder.row_icon.setLayoutParams(lpholderrow_icon);
		if(MainActivity.langid==1){
			holder.tvName.setText(itemList.get(position).get_Name());
		}else{
			holder.tvName.setText(itemList.get(position).getNameTau());
		}
		
		if (itemList.get(position).get_Image().endsWith(ConstantValue.RED)) {
			holder.row_icon.setBackgroundColor(Color.RED);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.YELLOW)) {
			holder.row_icon.setBackgroundColor(Color.YELLOW);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.BLUE)) {
			holder.row_icon.setBackgroundColor(Color.BLUE);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.GREEN)) {
			holder.row_icon.setBackgroundColor(Color.GREEN);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.BLACK)) {
			holder.row_icon.setBackgroundColor(Color.BLACK);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.WHITE)) {
			holder.row_icon.setBackgroundColor(Color.WHITE);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.MAGENTA)) {
			holder.row_icon.setBackgroundColor(Color.MAGENTA);
		} else if (itemList.get(position).get_Image().endsWith(ConstantValue.CYAN)) {
			holder.row_icon.setBackgroundColor(Color.CYAN);
		} else {
			PosApp.imageLoader2.displayImage(itemList.get(position).get_Image(), holder.row_icon,
					CustomFragmentActivity.options);
			// File file = new File(itemList.get(position).get_Image());
			// holder.row_icon.setImageBitmap(decodeFile(file));
		}

		if (itemList.get(position).getTextSize() != null) {
			if (itemList.get(position).getTextSize().endsWith("small")) {
				holder.tvName.setTextAppearance(ac,
						android.R.style.TextAppearance_Small);
			} else if (itemList.get(position).getTextSize().endsWith("medium")) {
				holder.tvName.setTextAppearance(ac,
						android.R.style.TextAppearance_Medium);
			} else {
				holder.tvName.setTextAppearance(ac,
						android.R.style.TextAppearance_Large);
			}
		}else{
//			holder.tvName.setTextAppearance(ac,
//					android.R.style.TextAppearance_Medium);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utilities.getGlobalVariable(ac).posGroup = Integer
						.parseInt(itemList.get(position).get_ItemGr_ID());
				((CustomFragmentActivity) ac).replaceFragment(
						ConstantValue.ITEMS, false);

			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showDialogOK(ac.getString(R.string.str_YouChoose) + itemList.get(position).get_Name(),
						position);
				return false;
			}
		});

		return convertView;
	}

	public void showDialogOK(final String message, final int pos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(ac.getString(R.string.str_inActive),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MainCateDataSource a = new MainCateDataSource(
										ac);
								String s = a.DelelteMainCate(itemList.get(pos)
										.get_ItemGr_ID());
							}
						});
		
		builder.setNeutralButton(ac.getString(R.string.str_Edit), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Utilities.getGlobalVariable(ac).posEdit = pos ;
				
				DialogGroup di = new DialogGroup();
				di.showDialogAddCate(ac, ac, true,itemList.get(pos)
						.get_ItemGr_ID());

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

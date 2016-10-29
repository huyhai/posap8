package com.pos.ui;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.R;
import com.pos.adapter.ItemsAdapter;
import com.pos.adapter.MenuAdapter;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.model.ItemsModel;
import com.pos.model.ItemsModel2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.serialport.SerialPort;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class Menu extends Fragment {
	public static GridView gvItem;
	public static ArrayList<ItemsModel2> listItemModel = new ArrayList<ItemsModel2>();
	public static MenuAdapter dataAdapter;
	public static String num = "";
	private static Activity ac;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu, container, false);
		ac = getActivity();
		initField(view);
		initData(getActivity());
		return view;
	}

	public void initData(Activity ac) {
		try {
			dataAdapter = new MenuAdapter(ac, listItemModel);
			gvItem.setAdapter(dataAdapter);
		} catch (Exception e) {
			// TODO: handle exception
		}

		callAdv();

	}

	private void initField(View view) {
		gvItem = (GridView) view.findViewById(R.id.gvItem1);

	}

//	public static void loadVattu() {
//
//		if (listItemModel != null)
//			listItemModel.clear();
//
//		ItemsDataSource dataSource = new ItemsDataSource(ac, ac);
//		listItemModel = dataSource.loadItems();
//
//	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getsetmenu"));
		params.add(new BasicNameValuePair("langid", Utilities
				.getGlobalVariable(ac).language_code + ""));
		UserFunctions.getInstance().getMenu(ac, params, true);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(UserFunctions.GETMENU)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
//					dataAdapter = new ItemsAdapter(ac, listItemModel);
//					gvItem.setAdapter(dataAdapter);
					dataAdapter.setItemList(UserFunctions.getInstance().getListMenu());
					dataAdapter.notifyDataSetChanged();
				} else {
				}
			}
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentGetKeySend = new IntentFilter();
		intentGetKeySend.addAction(UserFunctions.GETMENU);
		ac.registerReceiver(receiver, intentGetKeySend);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			ac.unregisterReceiver(receiver);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

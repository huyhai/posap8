package com.pos.ui;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.Application;
import com.pos.R;
import com.pos.adapter.ItemsAdapter;
import com.pos.adapter.ItemsAdapter2;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.model.ItemsModel;

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

public class Items2 extends Fragment {
	public static GridView gvItem;
	public static ArrayList<ItemsModel> listItemModel = new ArrayList<ItemsModel>();
	public static ItemsAdapter2 dataAdapter;
	public static String num = "";
	private static Activity ac;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.items2, container, false);
		ac = getActivity();
		initField(view);
		initData(getActivity());
		return view;
	}

	public void initData(Activity ac) {
		try {
			dataAdapter = new ItemsAdapter2(ac, UserFunctions.getInstance().getLishItems2Model());
			gvItem.setAdapter(dataAdapter);
		} catch (Exception e) {
			// TODO: handle exception
		}
//
//		callAdv();

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

//	private void callAdv() {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("tag", "getsubgroup"));
//		params.add(new BasicNameValuePair("langid", Utilities
//				.getGlobalVariable(ac).language_code + ""));
//		params.add(new BasicNameValuePair("mainid", Utilities
//				.getGlobalVariable(ac).posGroup + ""));
//		UserFunctions.getInstance().getItem1(ac, params, true);
//	}
//
//	BroadcastReceiver receiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase(UserFunctions.GETITEM1)) {
//				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
////					dataAdapter = new ItemsAdapter(ac, listItemModel);
////					gvItem.setAdapter(dataAdapter);
//					dataAdapter.setItemList(UserFunctions.getInstance().getLishItems1Model());
//					dataAdapter.notifyDataSetChanged();
//				} else {
//				}
//			}
//		}
//	};
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		IntentFilter intentGetKeySend = new IntentFilter();
//		intentGetKeySend.addAction(UserFunctions.GETITEM1);
//		ac.registerReceiver(receiver, intentGetKeySend);
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		try {
//			ac.unregisterReceiver(receiver);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
}

package com.pos.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.pos.MainActivity;
import com.pos.R;
import com.pos.adapter.MainCateAdapter;
import com.pos.adapter.UsersAdapter;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.MainCateDataSource;
import com.pos.model.MainCateModel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class Home extends Fragment {
	private static GridView gvItem;
	public static ArrayList<MainCateModel> listMainCateModel = new ArrayList<MainCateModel>();
	public static MainCateAdapter dataAdapter;
	private static Activity ac;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, container, false);
		ac = getActivity();
		initField(view);
		initData();
		return view;
	}

	public void initData() {
		dataAdapter = new MainCateAdapter(ac, listMainCateModel);
		gvItem.setAdapter(dataAdapter);
		callAdv();

		// try {
		// makePDF();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void initField(View view) {
		gvItem = (GridView) view.findViewById(R.id.gvItem);

	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getmaingroup"));
		params.add(new BasicNameValuePair("langid", Utilities
				.getGlobalVariable(ac).language_code + ""));
		UserFunctions.getInstance().getMainCate(ac, params, true);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(UserFunctions.MAINCATE)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					dataAdapter.setItemList(UserFunctions.getInstance()
							.getlistMainCate());
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
		intentGetKeySend.addAction(UserFunctions.MAINCATE);
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

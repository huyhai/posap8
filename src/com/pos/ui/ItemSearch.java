package com.pos.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.pos.R;
import com.pos.adapter.ItemSearchAdapter;
import com.pos.adapter.ItemsAdapter;
import com.pos.common.Utilities;
import com.pos.db.ItemsDataSource;
import com.pos.model.ItemsModel;

public class ItemSearch extends Fragment {
	public static GridView gvItem;
	public static ArrayList<ItemsModel> listItemModel = new ArrayList<ItemsModel>();
	public static ItemSearchAdapter dataAdapter;
	private Activity ac;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.items_search, container, false);
		ac = getActivity();
		initField(view);
		initData();
		return view;
	}

	public void loadVattu() {

		if (listItemModel != null)
			listItemModel.clear();

		ItemsDataSource dataSource = new ItemsDataSource(ac, ac);
		listItemModel = dataSource.loadItemsBykey();
		dataAdapter.setItemList(listItemModel);
		dataAdapter.notifyDataSetChanged();
	}

	private void initData() {
		dataAdapter = new ItemSearchAdapter(ac, listItemModel);
		gvItem.setAdapter(dataAdapter);
		loadVattu();
		dataAdapter.getFilter().filter(
				Utilities.getGlobalVariable(ac).keySearch);

	}

	public static void filter(CharSequence key) {
		dataAdapter.getFilter().filter(key);
	}

	private void initField(View view) {
		gvItem = (GridView) view.findViewById(R.id.gvItem1);

	}
}

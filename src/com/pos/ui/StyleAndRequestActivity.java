package com.pos.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.AddAdapter;
import com.pos.adapter.NoAdapter;
import com.pos.adapter.ServingAdapter;
import com.pos.adapter.StylesAdapter;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.libs.CalculationUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StyleAndRequestActivity extends Activity implements
		OnClickListener {
	private Activity ac;
	private GridView grStyle;
	private GridView grServing;
	private GridView grNo;
	private GridView grAdd;
	private ArrayList<String> listStyle = new ArrayList<String>();
	private ArrayList<String> listServing = new ArrayList<String>();
	private ArrayList<String> listNo = new ArrayList<String>();
	private ArrayList<String> listAdd = new ArrayList<String>();
	private ImageView imgBack;
	private Button btnConfirm;
	public static TextView tvStyleVl;
	public static TextView tvNoVl;
	public static TextView tvServingVl;
	public static TextView tvAddVl;
	private TextView tvNo;
	private TextView tvLess;
	private TextView tvMore;
	private TextView tvOnly;
	private TextView tvAdd;
	private TextView tvReplace;
	public static CheckBox cbStandBy;

	private TextView tvSmall;
	private TextView tvSmallMedium;
	private TextView tvMEDIUM;
	private TextView tvMediumLarge;
	private TextView tvLarge;
	private String priceS;
	public static EditText edWeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.style_requests);
		ac = StyleAndRequestActivity.this;
		initView();
		getTableNum();
	}

	private void initView() {
		grServing = (GridView) findViewById(R.id.grServing);
		grStyle = (GridView) findViewById(R.id.grStyle);
		grNo = (GridView) findViewById(R.id.grNo);
		grAdd = (GridView) findViewById(R.id.grAdd);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		tvStyleVl = (TextView) findViewById(R.id.tvStyleVl);
		tvNoVl = (TextView) findViewById(R.id.tvNoVl);
		tvServingVl = (TextView) findViewById(R.id.tvServingVl);
		tvAddVl = (TextView) findViewById(R.id.tvAddVl);
		tvNo = (TextView) findViewById(R.id.tvNo);
		tvLess = (TextView) findViewById(R.id.tvLess);
		tvMore = (TextView) findViewById(R.id.tvMore);
		tvOnly = (TextView) findViewById(R.id.tvOnly);
		tvAdd = (TextView) findViewById(R.id.tvAdd);
		tvReplace = (TextView) findViewById(R.id.tvReplace);
		cbStandBy = (CheckBox) findViewById(R.id.btnStandBy);
		tvSmall = (TextView) findViewById(R.id.tvSmall);
		tvSmallMedium = (TextView) findViewById(R.id.tvSmallMedium);
		tvMEDIUM = (TextView) findViewById(R.id.tvMEDIUM);
		tvMediumLarge = (TextView) findViewById(R.id.tvMediumLarge);
		tvLarge = (TextView) findViewById(R.id.tvLarge);
		edWeight = (EditText) findViewById(R.id.edWeight);
		init();

	}

	private void init() {
		// Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests += "No: ";
		// tvNo.setTextColor(Color.RED);
		// Utilities.getGlobalVariable(ac).addReplace += "Add: ";
		// tvAdd.setTextColor(Color.RED);
		imgBack.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		tvNo.setOnClickListener(this);
		tvLess.setOnClickListener(this);
		tvMore.setOnClickListener(this);
		tvOnly.setOnClickListener(this);
		tvAdd.setOnClickListener(this);
		tvReplace.setOnClickListener(this);
		tvSmall.setOnClickListener(this);
		tvSmallMedium.setOnClickListener(this);
		tvMEDIUM.setOnClickListener(this);
		tvMediumLarge.setOnClickListener(this);
		tvLarge.setOnClickListener(this);
		// priceS = PosApp.listOrderData.get(MainActivity.qly).getUnitPrice();
		// setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
		// .getUnitPrice(), tvSmall, tvSmallMedium, tvMEDIUM,
		// tvMediumLarge, tvLarge);

	}

	private void getTableNum() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getSaleRemarks"));
		params.add(new BasicNameValuePair("groupID", PosApp.listOrderData.get(
				MainActivity.qly).getGroupCode()));
		params.add(new BasicNameValuePair("itemcode", PosApp.listOrderData.get(
				MainActivity.qly).getItemCode()));
		UserFunctions.getInstance().getSalesRemarks(ac, params, false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentGetKeySend = new IntentFilter();
		intentGetKeySend.addAction(UserFunctions.SALEREMARKS);
		registerReceiver(receiver, intentGetKeySend);
		if (PosApp.listOrderData.get(MainActivity.qly).getStandBy().equals("1"))
			cbStandBy.setChecked(true);
		setPrice(PosApp.listOrderData.get(MainActivity.qly).getUnitPrice());

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Utilities.getGlobalVariable(ac).addReplace = "";
		Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests = "";
		Utilities.getGlobalVariable(ac).serving = "";
		Utilities.getGlobalVariable(ac).styleRequests = "";
		StyleAndRequestActivity.ischoosenNoLess = false;
		StyleAndRequestActivity.ischoosenAddRep = false;
		try {
			unregisterReceiver(receiver);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(UserFunctions.SALEREMARKS)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					for (int i = 0; i < UserFunctions.getInstance()
							.getlistSaleRemarks().size(); i++) {
						if (UserFunctions.getInstance().getlistSaleRemarks()
								.get(i).getCategory_id().equals("1")) {
							listStyle.add(UserFunctions.getInstance()
									.getlistSaleRemarks().get(i).getOption());
						}
						if (UserFunctions.getInstance().getlistSaleRemarks()
								.get(i).getCategory_id().equals("2")) {
							listNo.add(UserFunctions.getInstance()
									.getlistSaleRemarks().get(i).getOption());
						}
						if (UserFunctions.getInstance().getlistSaleRemarks()
								.get(i).getCategory_id().equals("3")) {
							listServing.add(UserFunctions.getInstance()
									.getlistSaleRemarks().get(i).getOption());
						}
						if (UserFunctions.getInstance().getlistSaleRemarks()
								.get(i).getCategory_id().equals("4")) {
							listAdd.add(UserFunctions.getInstance()
									.getlistSaleRemarks().get(i).getOption());
						}
					}
					StylesAdapter adapter = new StylesAdapter(ac, listStyle);
					grStyle.setAdapter(adapter);
					NoAdapter no = new NoAdapter(ac, listNo);
					grNo.setAdapter(no);
					ServingAdapter sv = new ServingAdapter(ac, listServing);
					grServing.setAdapter(sv);
					AddAdapter add = new AddAdapter(ac, listAdd);
					grAdd.setAdapter(add);

				} else {
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v == imgBack) {
			ac.finish();

		}
		if (v == btnConfirm) {
			if (TextUtils.isEmpty(edWeight.getText().toString())) {
				DialogConfirmSaleRemarks di = new DialogConfirmSaleRemarks();
				di.showNoteDialog(ac, ac, priceS);
			} else {
				size = "";
				DialogConfirmSaleRemarks di = new DialogConfirmSaleRemarks();
				di.showNoteDialog(
						ac,
						ac,
						""
								+ CalculationUtils.calculateGram(Double
										.parseDouble(edWeight.getText()
												.toString()), Double
										.parseDouble(UserFunctions.getInstance().getPrice1())));
			}

		}
		if (v == tvNo) {
			setUnderLine("No: ", tvNo, tvLess, tvMore, tvOnly);
		}
		if (v == tvLess) {
			setUnderLine("Less: ", tvLess, tvNo, tvMore, tvOnly);
		}
		if (v == tvMore) {
			setUnderLine("More: ", tvMore, tvNo, tvLess, tvOnly);
		}
		if (v == tvOnly) {
			setUnderLine("Only: ", tvOnly, tvNo, tvLess, tvMore);
		}
		if (v == tvAdd) {
			setUnderLineAdd("Add: ", tvAdd, tvReplace);
		}
		if (v == tvReplace) {
			setUnderLineAdd("Replace: ", tvReplace, tvAdd);
		}

		if (v == tvSmall) {
			size = "SMALL";
			setUnderLonNho(UserFunctions.getInstance().getPrice1(), tvSmall, tvSmallMedium, tvMEDIUM,
					tvMediumLarge, tvLarge);
		}

		if (v == tvMEDIUM) {
			if (PosApp.listOrderData.get(MainActivity.qly).getPrice3()
					.equals("null")) {
				Toast.makeText(ac, "Size not allowed", Toast.LENGTH_SHORT)
						.show();
			} else {
				size = "MEDIUM";
				setUnderLonNho(UserFunctions.getInstance().getPrice3(), tvMEDIUM, tvSmallMedium, tvSmall,
						tvMediumLarge, tvLarge);
			}

		}

		if (v == tvLarge) {
			if (PosApp.listOrderData.get(MainActivity.qly).getPrice5()
					.equals("null")) {
				Toast.makeText(ac, "Size not allowed", Toast.LENGTH_SHORT)
						.show();
			} else {
				size = "LARGE";
				setUnderLonNho(UserFunctions.getInstance().getPrice5(), tvLarge, tvSmallMedium, tvMEDIUM,
						tvMediumLarge, tvSmall);
			}

		}

		if (v == tvMediumLarge) {
			if (PosApp.listOrderData.get(MainActivity.qly).getPrice4()
					.equals("null")) {
				Toast.makeText(ac, "Size not allowed", Toast.LENGTH_SHORT)
						.show();
			} else {
				size = "MEDIUM-LARGE";
				setUnderLonNho(UserFunctions.getInstance().getPrice4(), tvMediumLarge, tvSmallMedium, tvMEDIUM,
						tvSmall, tvLarge);
			}

		}

		if (v == tvSmallMedium) {

			if (PosApp.listOrderData.get(MainActivity.qly).getPrice2()
					.equals("null")) {
				Toast.makeText(ac, "Size not allowed", Toast.LENGTH_SHORT)
						.show();
			} else {
				size = "SMALL-MEDIUM";
				setUnderLonNho(UserFunctions.getInstance().getPrice2(), tvSmallMedium, tvSmall, tvMEDIUM,
						tvMediumLarge, tvLarge);
			}

		}

	}

	private int price;
	public static String size = "";
	public static boolean ischoosenNoLess = false;
	public static boolean ischoosenAddRep = false;

	public void showDialogOK(final String message, final int pos) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// DialogItems ditem = new DialogItems();
				// ditem.showDialogAddItem(ac, ac, true, itemList.get(pos)
				// .getItemID());
				// dialog.dismiss();

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

	private void setUnderLineAdd(String content1, TextView v, TextView v2) {
		Utilities.getGlobalVariable(ac).addReplace += content1;
		v.setTextColor(Color.RED);
		v2.setTextColor(Color.WHITE);
		ischoosenAddRep = true;
	}

	private void setUnderLine(String content1, TextView v, TextView v2,
			TextView v3, TextView v4) {
		ischoosenNoLess = true;
		Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests += content1;
		v.setTextColor(Color.RED);
		v2.setTextColor(Color.WHITE);
		v3.setTextColor(Color.WHITE);
		v4.setTextColor(Color.WHITE);
	}

	private void setUnderLonNho(String price, TextView v, TextView v2,
			TextView v3, TextView v4, TextView v5) {
		priceS = price;
		v.setTextColor(Color.RED);
		v2.setTextColor(Color.WHITE);
		v3.setTextColor(Color.WHITE);
		v4.setTextColor(Color.WHITE);
		v5.setTextColor(Color.WHITE);
	}

	private void setPrice(String price) {
		if (price.equals(PosApp.listOrderData.get(MainActivity.qly)
				.getUnitPrice())) {
			setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
					.getUnitPrice(), tvSmall, tvSmallMedium, tvMEDIUM,
					tvMediumLarge, tvLarge);
		}
		if (price
				.equals(PosApp.listOrderData.get(MainActivity.qly).getPrice2())) {
			setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
					.getPrice2(), tvSmallMedium, tvSmall, tvMEDIUM,
					tvMediumLarge, tvLarge);
		}
		if (price
				.equals(PosApp.listOrderData.get(MainActivity.qly).getPrice3())) {
			setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
					.getPrice3(), tvMEDIUM, tvSmallMedium, tvSmall,
					tvMediumLarge, tvLarge);
		}
		if (price
				.equals(PosApp.listOrderData.get(MainActivity.qly).getPrice4())) {
			setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
					.getPrice4(), tvMediumLarge, tvSmallMedium, tvMEDIUM,
					tvSmall, tvLarge);
		}
		if (price
				.equals(PosApp.listOrderData.get(MainActivity.qly).getPrice5())) {
			setUnderLonNho(PosApp.listOrderData.get(MainActivity.qly)
					.getPrice5(), tvLarge, tvSmallMedium, tvMEDIUM,
					tvMediumLarge, tvSmall);
		}
	}
}

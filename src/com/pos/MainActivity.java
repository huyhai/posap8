package com.pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.R;
import com.pos.adapter.ListOrderAdapter;
import com.pos.adapter.SearchBill2Adapter;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.db.PayDataSource;
import com.pos.db.SaleReportDataSource;
import com.pos.db.ScanerDataSource;
import com.pos.db.UnpaidDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.SessionManager;
import com.pos.libs.ViewUtils;
import com.pos.model.ItemsModel;
import com.pos.model.ListOrderModel;
import com.pos.model.MainCateModel;
import com.pos.model.PayModel;
import com.pos.model.SettingModel;
import com.pos.model.UnpaidModel;
import com.pos.print.PrinterFunctions;
import com.pos.ui.DialogBillDiscount;
import com.pos.ui.DialogCustomItem;
import com.pos.ui.DialogEditItems;
import com.pos.ui.DialogGroup;
import com.pos.ui.DialogInventoryCheck;
import com.pos.ui.DialogInventoryReport;
import com.pos.ui.DialogItemDiscount;
import com.pos.ui.DialogItems;
import com.pos.ui.DialogNumPrint;
import com.pos.ui.DialogPaxNumber;
import com.pos.ui.DialogPaxNumber2;
import com.pos.ui.DialogPayment;
import com.pos.ui.DialogRefund;
import com.pos.ui.DialogRefund2;
import com.pos.ui.DialogResendKitchen;
import com.pos.ui.DialogSaleReport;
import com.pos.ui.DialogSearchBill;
import com.pos.ui.DialogNote;
import com.pos.ui.DialogStockIn;
import com.pos.ui.DialogTicketReport;
import com.pos.ui.Header;
import com.pos.ui.ItemSearch;
import com.pos.ui.Items;
import com.pos.ui.LoginDialog;
import com.pos.ui.SplitBillActivity;
import com.pos.ui.StyleAndRequestActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.cashdrawer.CashDrawer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends CustomFragmentActivity implements
		OnClickListener {
	private RelativeLayout pdBar;
	private RelativeLayout rlTop;
	private RelativeLayout rlNumber;
	private LinearLayout rlNumberButon;
	private RelativeLayout rlNumber2;
	private static Button btnCustom;
	private static Button btnSearchBill;
	private static Button btnSplitBill;
	private static Button btnPrintBill;
	private Button btnNum0;
	private Button btnNum1;
	private Button btnNum2;
	private Button btnNum3;
	private Button btnNum4;
	private Button btnNum5;
	private Button btnNum6;
	private Button btnNum7;
	private Button btnNum8;
	private Button btnNum9;
	private Button btnScan;
	private static Button btnSendOrder;
	private RelativeLayout rlPayment;
	private RelativeLayout rlOpen;
	private static Button btnSearchItem;
	private static EditText edSearch;
	private static EditText edSearch1;
	private RelativeLayout rlCard2;
	private RelativeLayout rlBottom;
	private RelativeLayout rlListLeft;
	private RelativeLayout rlListRight;
	public static ListView lvOrder;
	private RelativeLayout rlBelowList;
	private RelativeLayout rlgvItesm;
	private Button btnDelete;

	private static Button btnReprintOrder;
	private static Button btnResendKitchen;
	private static Button btnStyleAndRequest;
	private static Button btnViewReport;
	private static Button btnPrinter;

	private static Button btnRefund;
	private static Button btnPOC;
	private static Button btnBillDiscount;
	private static Button btnEditPax;
	private static Button btnCancelBill;
	public static String endshift = "false";
	public static String note = "";
	public static Boolean refund = false;
	public static String searchstring = "";
	public static Button btnSubTotalValue;
	private static Button btnSubTotal;
	public static Button btnGSTValue;
	private Button btnGST;
	public static Button btnDisCountValue;
	private static Button btnDisCount;

	private RelativeLayout rlItemName;
	private RelativeLayout rlUnitPrice;
	private RelativeLayout rlQty;
	private RelativeLayout rlDiscount;
	private RelativeLayout rlAmount;
	private LinearLayout rlAll;
	private static Button btnPlus;
	private static Button btnMinus;
	private static ListOrderAdapter adapter;
	public static Button tvTotalAmount;
	public static int qly = -1;
	private static SessionManager ss;
	private static int returnla = 0;
	private Application mApplication;
	private SerialPort mSerialPort_V;
	private OutputStream mOutputStream_V;
	public static CashDrawer mCashDrawer = null;
	public static String creaditType;
	public static String modepay;
	public static Activity activity;
	public static Button btnGSTValueSv;
	public static TextView tvAllHold;
	private static TextView btnPrintNoSave;
	public static ArrayList<String> listSearch = new ArrayList<String>();
	private static Button btnSvC;
	public static Button btnSVCValue;
	public static TextView tbNum;
	public static int langid = 1;

	public MainActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		this.setContentView(R.layout.content_frame);
		activity = MainActivity.this;
		mCashDrawer = new CashDrawer();
		ComDDUtilities.showWelcome();
		ss = new SessionManager(activity);

		try {
			PosApp.soluongIn = Integer.parseInt(ss.getNumPrint());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pdBar = (RelativeLayout) this.findViewById(R.id.pdBar);
		rlTop = (RelativeLayout) this.findViewById(R.id.rlTop);
		rlNumber = (RelativeLayout) this.findViewById(R.id.rlNumber);
		rlNumberButon = (LinearLayout) this.findViewById(R.id.rlNumberButon);
		rlNumber2 = (RelativeLayout) this.findViewById(R.id.rlNumber2);
		btnSendOrder = (Button) this.findViewById(R.id.btnSendOrder);
		btnCustom = (Button) this.findViewById(R.id.btnCustom);
		btnSearchBill = (Button) this.findViewById(R.id.btnSearchBill);
		btnSplitBill = (Button) this.findViewById(R.id.btnSplitBill);
		btnPrintBill = (Button) this.findViewById(R.id.btnPrintBill);
		btnNum0 = (Button) this.findViewById(R.id.btnNum0);
		btnNum1 = (Button) this.findViewById(R.id.btnNum1);
		btnNum2 = (Button) this.findViewById(R.id.btnNum2);
		btnNum3 = (Button) this.findViewById(R.id.btnNum3);
		btnNum4 = (Button) this.findViewById(R.id.btnNum4);
		btnNum5 = (Button) this.findViewById(R.id.btnNum5);
		btnNum6 = (Button) this.findViewById(R.id.btnNum6);
		btnNum7 = (Button) this.findViewById(R.id.btnNum7);
		btnNum8 = (Button) this.findViewById(R.id.btnNum8);
		btnNum9 = (Button) this.findViewById(R.id.btnNum9);
		btnScan = (Button) this.findViewById(R.id.btnScan);
		btnDelete = (Button) this.findViewById(R.id.btnDelete);

		rlOpen = (RelativeLayout) this.findViewById(R.id.rlOpen);
		btnSearchItem = (Button) this.findViewById(R.id.btnSearchItem);
		edSearch = (EditText) this.findViewById(R.id.edSearch);
		edSearch1 = (EditText) this.findViewById(R.id.edSearch1);
		rlBottom = (RelativeLayout) this.findViewById(R.id.rlBottom);
		rlListLeft = (RelativeLayout) this.findViewById(R.id.rlListLeft);
		rlListRight = (RelativeLayout) this.findViewById(R.id.rlListRight);
		rlBelowList = (RelativeLayout) this.findViewById(R.id.rlBelowList);
		rlgvItesm = (RelativeLayout) this.findViewById(R.id.rlgvItesm);
		lvOrder = (ListView) this.findViewById(R.id.lvOrder);
		btnReprintOrder = (Button) this.findViewById(R.id.btnReprintOrder);
		btnResendKitchen = (Button) this.findViewById(R.id.btnResendKitchen);
		btnStyleAndRequest = (Button) this
				.findViewById(R.id.btnStyleAndRequest);
		btnViewReport = (Button) this.findViewById(R.id.btnViewReport);
		btnPrinter = (Button) this.findViewById(R.id.btnPrinter);
		btnSVCValue = (Button) this.findViewById(R.id.btnSVCValue);
		btnSvC = (Button) this.findViewById(R.id.btnSvC);

		btnRefund = (Button) this.findViewById(R.id.btnRefund);
		btnPOC = (Button) this.findViewById(R.id.btnPOC);
		btnBillDiscount = (Button) this.findViewById(R.id.btnBillDiscount);
		btnEditPax = (Button) this.findViewById(R.id.btnEditPax);
		btnCancelBill = (Button) this.findViewById(R.id.btnCancelBill);

		btnSubTotalValue = (Button) this.findViewById(R.id.btnSubTotalValue);
		btnSubTotal = (Button) this.findViewById(R.id.btnSubTotal);

		btnGSTValue = (Button) this.findViewById(R.id.btnGSTValue);
		btnGST = (Button) this.findViewById(R.id.btnGST);
		btnDisCountValue = (Button) this.findViewById(R.id.btnDisCountValue);
		btnDisCount = (Button) this.findViewById(R.id.btnDisCount);
		rlBelowList.requestFocus();

		rlAmount = (RelativeLayout) findViewById(R.id.rlAmount);
		rlDiscount = (RelativeLayout) findViewById(R.id.rlDiscount);
		rlItemName = (RelativeLayout) findViewById(R.id.rlItemName);
		rlQty = (RelativeLayout) findViewById(R.id.rlQty);
		rlUnitPrice = (RelativeLayout) findViewById(R.id.rlUnitPrice);
		rlAll = (LinearLayout) findViewById(R.id.rlAll);
		btnMinus = (Button) this.findViewById(R.id.btnMinus);
		btnPlus = (Button) this.findViewById(R.id.btnPlus);
		tvTotalAmount = (Button) this.findViewById(R.id.btnTotalValue);
		btnPrintNoSave = (Button) this.findViewById(R.id.btnPrintNoSave);
		tvTotal = (Button) this.findViewById(R.id.btnTotal);
		tvItemCode = (TextView) this.findViewById(R.id.tvItemCode);
		tvItemName = (TextView) this.findViewById(R.id.tvItemName);
		tvUnitPrice = (TextView) this.findViewById(R.id.tvUnitPrice);
		tvQty = (TextView) this.findViewById(R.id.tvQty);
		tvDiscount = (TextView) this.findViewById(R.id.tvDiscount);
		tvAmount = (TextView) this.findViewById(R.id.tvAmount);
		tbNum = (TextView) this.findViewById(R.id.tbNum);

		initLayout();
		initData();
		setText();
		if (!isOnActivityResult) {
			if (this.mListView.size() > 0) {
				this.startFragment(this.mListView.get(this.mListView.size() - 1));
			} else {
				this.startFragment(ConstantValue.HOME_FRAGMENT);

			}
		}
		// setDataForSales();
		getPrinterIndi();
		// listSearch = a1.loadAllItem();
		// notifidataOrderList();
	}

	private void initData() {
		tbNum.setText("TABLE " + Utilities.getGlobalVariable(activity).tableNum
				+ ", PAX "
				+ Utilities.getGlobalVariable(activity).numberCustomer);
		btnGSTValue.setText("0.00");
		btnSendOrder.setOnClickListener(this);
		btnNum0.setOnClickListener(this);
		btnNum1.setOnClickListener(this);
		btnNum2.setOnClickListener(this);
		btnNum3.setOnClickListener(this);
		btnNum4.setOnClickListener(this);
		btnNum5.setOnClickListener(this);
		btnNum6.setOnClickListener(this);
		btnNum7.setOnClickListener(this);
		btnNum8.setOnClickListener(this);
		btnNum9.setOnClickListener(this);
		btnPlus.setOnClickListener(this);
		btnMinus.setOnClickListener(this);
		btnPOC.setOnClickListener(this);
		btnCancelBill.setOnClickListener(this);
		btnBillDiscount.setOnClickListener(this);
		btnEditPax.setOnClickListener(this);
		btnSplitBill.setOnClickListener(this);
		btnSearchBill.setOnClickListener(this);
		btnRefund.setOnClickListener(this);
		btnPrintBill.setOnClickListener(this);
		btnCustom.setOnClickListener(this);
		btnStyleAndRequest.setOnClickListener(this);
		btnResendKitchen.setOnClickListener(this);
		adapter = new ListOrderAdapter(activity, PosApp.listOrderData);

		lvOrder.setAdapter(adapter);
		lvOrder.setLongClickable(true);
		btnReprintOrder.setOnClickListener(this);
		btnSearchItem.setOnClickListener(this);
		btnPrinter.setOnClickListener(this);
		btnViewReport.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnPrintNoSave.setOnClickListener(this);
		edSearch1.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() != KeyEvent.ACTION_DOWN) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
						String text = edSearch1.getText().toString()
								.replace("\n", "");
						addList(text);

						return true;
					}
				}

				return false;

			}

		});
		edSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				replaceFragment(ConstantValue.ITEMSEARCH, false);
				Utilities.getGlobalVariable(activity).keySearch = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private static ArrayList<ItemsModel> listScan = new ArrayList<ItemsModel>();

	private void addList(String text) {
		ScanerDataSource scan = new ScanerDataSource(activity, activity);
		listScan = scan.getitemBarcode(text);

		if (listScan.size() > 0) {

			if (Utilities.getGlobalVariable(activity).isPOC == true) {
				ListOrderModel md = new ListOrderModel();
				if (Items.num != "") {
					md.setQualyti(Items.num);
				} else {
					md.setQualyti("1");
				}

				md.setAmount("0");
				md.setDiscount("0");
				md.setItemCode(listScan.get(0).getItem_code());
				md.setItemName(listScan.get(0).getName() + "(FOC)");
				md.setPrice2(listScan.get(0).getSelling_price_2());
				md.setSpecialPrice(listScan.get(0).getSpecial_price());
				md.setUnitPrice("0.00");
				PosApp.listOrderData.add(md);

				MainActivity main = new MainActivity();
				main.notifidataOrderList();
				// Items.num = "";
				MainActivity.qly = -1;
				Utilities.getGlobalVariable(activity).isPOC = false;

			} else {
				ListOrderModel md = new ListOrderModel();
				String amount;
				if (Items.num != "") {
					amount = String.valueOf(Double.parseDouble(listScan.get(0)
							.getSelling_price_1())
							* Double.parseDouble(Items.num));
					md.setQualyti(Items.num);
				} else {
					amount = String.valueOf(Double.parseDouble(listScan.get(0)
							.getSelling_price_1()) * 1);
					md.setQualyti("1");
				}
				DecimalFormat df = new DecimalFormat("0.00");
				amount1 = Double.valueOf(amount);
				md.setAmount(df.format(amount1) + "");
				md.setDiscount("0.00");
				md.setItemCode(listScan.get(0).getItem_code());
				md.setItemName(listScan.get(0).getName());
				md.setPrice2(listScan.get(0).getSelling_price_2());
				md.setSpecialPrice(listScan.get(0).getSpecial_price());
				md.setUnitPrice(listScan.get(0).getSelling_price_1());

				PosApp.listOrderData.add(md);

				MainActivity main = new MainActivity();
				main.notifidataOrderList();
				// Items.num = "";
				MainActivity.qly = -1;
			}
			try {
				int baudrate = 9600;
				int databits = 8;
				int parity = 0;
				int stopbits = 1;
				int flowctl = 0;
				String path = "/dev/ttymxc2";
				mSerialPort_V = new SerialPort(new File(path), baudrate,
						databits, parity, stopbits, flowctl);
				mOutputStream_V = mSerialPort_V.getOutputStream();
				mOutputStream_V.write(0x1b);
				mOutputStream_V.write(0x40);

				String itemName = listScan.get(0).getName().toString();
				String spaceName = "";
				if (itemName.length() > 20) {
					itemName = itemName.substring(0, 20);

				} else {
					int tempvar = 20 - itemName.length();
					for (int i = 0; i < tempvar; i++) {
						spaceName += " ";

					}

				}
				int changequantity;
				double amount;
				mOutputStream_V.write(itemName.getBytes());
				mOutputStream_V.write(spaceName.getBytes());
				String space = "      ";
				// mOutputStream_V.write(space.getBytes());
				if (Items.num != "") {
					changequantity = Integer.parseInt(Items.num);
					amount = Double.parseDouble(listScan.get(0)
							.getSelling_price_1())
							* Double.parseDouble(Items.num);
				} else {
					changequantity = Integer.parseInt("1");
					amount = Double.parseDouble(listScan.get(0)
							.getSelling_price_1()) * Double.parseDouble("1");
				}

				int len1 = listScan.get(0).getSelling_price_1().toString()
						.length();
				String strtemp = "" + amount;
				int len2 = strtemp.length();
				double spaceneed = 14 - (len1 + len2);
				// mOutputStream_V.write(space.getBytes());
				for (int i = 0; i < spaceneed; i++) {
					space += " ";
				}
				DecimalFormat df = new DecimalFormat("0.00");
				String am = df.format(amount1);
				String quantity = "" + changequantity;
				String amountc = "" + am;
				mOutputStream_V.write(quantity.getBytes());
				mOutputStream_V.write(space.getBytes());
				mOutputStream_V.write(amountc.getBytes());

				mOutputStream_V.write('\r');
				mOutputStream_V.write('\n');

				mApplication.closeSerialPort();
			} catch (Exception e) {
			}

		}
		Items.num = "";
		edSearch1.setText("");

	}

	private int currentPosition;

	public void notifidataOrderList() {

		adapter.setItemList(PosApp.listOrderData);
		adapter.notifyDataSetChanged();
		scrollMyListViewToBottom();

		btnSubTotalValue.setText(CalculationUtils
				.calculateSubTotal(PosApp.listOrderData) + "");
		btnSVCValue.setText(CalculationUtils
				.calculateServiceCharge(PosApp.listOrderData) + "");

		btnGSTValue.setText(CalculationUtils
				.calculateGSTCharge(PosApp.listOrderData) + "");
		tvTotalAmount.setText(Utils.lamtronTong(Double
				.parseDouble(btnSubTotalValue.getText().toString())
				+ Double.parseDouble(btnSVCValue.getText().toString())
				+ Double.parseDouble(btnGSTValue.getText().toString())
				- Double.parseDouble(btnDisCountValue.getText().toString()))
				+ "");
	}

	public void setDataForSales() {

		if (UserFunctions.getInstance().getSaleModel().getSubtotal() != null) {

			btnSubTotalValue.setText(UserFunctions.getInstance().getSaleModel()
					.getSubtotal());
			btnSVCValue.setText(UserFunctions.getInstance().getSaleModel()
					.getSvc());
			btnDisCountValue.setText(UserFunctions.getInstance().getSaleModel()
					.getDis());
			btnGSTValue.setText(UserFunctions.getInstance().getSaleModel()
					.getGst());
			tvTotalAmount.setText(UserFunctions.getInstance().getSaleModel()
					.getTotal());
		}
		// Double.valueOf(btnSubTotalValue.getText().toString()),
		// Utilities.getGlobalVariable(activity).GST) + "");
		// Utils.lamtronTong(total);
	}

	public void scrollMyListViewToBottom() {
		currentPosition = lvOrder.getLastVisiblePosition();
		lvOrder.post(new Runnable() {
			@Override
			public void run() {
				lvOrder.setSelectionFromTop(currentPosition + 1, 0);
			}
		});
	}

	private void initLayout() {
		setWidthHeight(rlTop, 1, 9);
		setWidthHeight(rlNumber, 1, 9);
		setWidthHeight(rlNumberButon, 2.3, LayoutParams.FILL_PARENT);
		setWidthHeight(rlNumber2, 1, 7);
		// setWidthHeight(rlRight,1, 1);
		setWidthHeight(btnCustom, 18, 18);
		setWidthHeight(btnSearchBill, 18, 18);
		setWidthHeight(btnSplitBill, 18, 18);
		setWidthHeight(btnPrintBill, 18, 18);
		setWidthHeight(btnPrintNoSave, 18, 18);

		int wbtn = 23;
		int hbtn = 15;
		setWidthHeight(btnNum0, wbtn, hbtn);
		setWidthHeight(btnNum1, wbtn, hbtn);
		setWidthHeight(btnNum2, wbtn, hbtn);
		setWidthHeight(btnNum3, wbtn, hbtn);
		setWidthHeight(btnNum4, wbtn, hbtn);
		setWidthHeight(btnNum5, wbtn, hbtn);
		setWidthHeight(btnNum6, wbtn, hbtn);
		setWidthHeight(btnNum7, wbtn, hbtn);
		setWidthHeight(btnNum8, wbtn, hbtn);
		setWidthHeight(btnNum9, wbtn, hbtn);
		setWidthHeight(btnScan, wbtn - 2, 18);
		setWidthHeight(btnDelete, wbtn, hbtn);

		int wbtbtnVisan = 19;
		int hbtbtnVisan = 19;
		int wbtbtnVisan1 = 13;
		int hbtbtnVisan1 = 19;
		setWidthHeight(btnRefund, wbtbtnVisan1, hbtbtnVisan1);
		setWidthHeight(btnPOC, wbtbtnVisan1, hbtbtnVisan1);
		setWidthHeight(btnBillDiscount, wbtbtnVisan1, hbtbtnVisan1);
		setWidthHeight(btnSendOrder, wbtbtnVisan1, hbtbtnVisan1);
		setWidthHeight(btnEditPax, wbtbtnVisan1, hbtbtnVisan1);
		setWidthHeight(btnCancelBill, wbtbtnVisan1, hbtbtnVisan1);

		setHeight(btnMinus, hbtbtnVisan1);
		setHeight(btnPlus, hbtbtnVisan1);
		// setWidthHeight(btnMinus, 5, hbtbtnVisan1);
		// setWidthHeight(btnPlus, 5, hbtbtnVisan1);

		// setWidthHeight(btnNettSale, 8, 19);
		setWidthHeight(btnSearchItem, 9, 15);
		setWidthHeight(rlgvItesm, 1, 15);
		setWidthHeight(rlListLeft, 2.3, 1);
		// setWidthHeight(rlListRight, 1, 1);
		setWidthHeight(lvOrder, 1, 2.5);
		setWidthHeight(rlBottom, 1, 7);
		// setWidthHeight(rlBelowList, 1, 1);

		int subw = 14;
		int suvh = 20;
		setWidthHeight(btnSubTotalValue, subw, suvh);
		setWidthHeight(btnSubTotal, subw, suvh);

		setWidthHeight(btnGSTValue, subw, suvh);
		setWidthHeight(btnGST, subw, suvh);
		setWidthHeight(btnDisCountValue, subw, suvh);
		setWidthHeight(btnDisCount, subw, suvh);
		setWidthHeight(btnSvC, subw, suvh);
		setWidthHeight(btnSVCValue, subw, suvh);
		setHeight(tvTotal, suvh);
		setHeight(tvTotalAmount, suvh);
		// setWidthHeight(tvTotalAmount, 1, suvh);

		setWidth(rlDiscount, 14);
		setWidth(rlQty, 30);
		setWidth(rlUnitPrice, 14);
		setWidth(rlItemName, 6);
		setWidthHeight(rlAll, 1, 15);

		ViewUtils.createEffectTouch(btnReprintOrder,
				R.drawable.inventory_check, R.drawable.inventory_check_nguoc);
		ViewUtils.createEffectTouch(btnResendKitchen,
				R.drawable.inventory_check, R.drawable.inventory_check_nguoc);
		ViewUtils.createEffectTouch(btnStyleAndRequest,
				R.drawable.inventory_check, R.drawable.inventory_check_nguoc);
		ViewUtils.createEffectTouch(btnViewReport, R.drawable.inventory_check,
				R.drawable.inventory_check_nguoc);
		ViewUtils.createEffectTouch(btnPrinter, R.drawable.inventory_check,
				R.drawable.inventory_check_nguoc);

		ViewUtils.createEffectTouch(btnRefund, R.drawable.refund,
				R.drawable.refund_nguoc);
		ViewUtils.createEffectTouch(btnPOC, R.drawable.refund,
				R.drawable.refund_nguoc);
		ViewUtils.createEffectTouch(btnBillDiscount, R.drawable.refund,
				R.drawable.refund_nguoc);
		ViewUtils.createEffectTouch(btnSendOrder, R.drawable.refund,
				R.drawable.refund_nguoc);

		ViewUtils.createEffectTouch(btnEditPax, R.drawable.refund,
				R.drawable.refund_nguoc);

		ViewUtils.createEffectTouch(btnCustom, R.drawable.hold_bill,
				R.drawable.hold_bill_nguoc);
		ViewUtils.createEffectTouch(btnSearchBill, R.drawable.hold_bill,
				R.drawable.hold_bill_nguoc);
		ViewUtils.createEffectTouch(btnSplitBill, R.drawable.hold_bill,
				R.drawable.hold_bill_nguoc);
		ViewUtils.createEffectTouch(btnPrintBill, R.drawable.hold_bill,
				R.drawable.hold_bill_nguoc);
		ViewUtils.createEffectTouch(btnSearchItem, R.drawable.search_item,
				R.drawable.search_item_nguoc);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void showProgressBar() {
		pdBar.setVisibility(View.VISIBLE);
	}

	public void hideProgressBar() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pdBar.setVisibility(View.GONE);
			}
		});
	}

	double amount1;

	public void callGroupIP(String group) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "ipgroup"));
		params.add(new BasicNameValuePair("groupid", group));
		UserFunctions.getInstance().callIP(activity, params, true);
	}

	private void getItemSetmenu(String group) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getItemSetMenu"));
		params.add(new BasicNameValuePair("setMenuID", group));
		UserFunctions.getInstance().callListSetmenuItem(activity, params, true);
	}

	public void getPrinterIndi() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getPrinterIndi"));
		UserFunctions.getInstance().callListMayin(activity, params, true);
	}

	private boolean isCan = true;
	public static int count = 0;
	private String ip1;
	private String ip2;

	private void getIp2(int two) {
		for (int i = 0; i < UserFunctions.getInstance().getlistIPMayIn().size(); i++) {
			if (two == Integer.parseInt(UserFunctions.getInstance()
					.getlistIPMayIn().get(i).getId())) {
				ip2 = UserFunctions.getInstance().getlistIPMayIn().get(i)
						.getIp_address();
				break;
			}
		}

	}

	private void getIp1(int two) {
		for (int i = 0; i < UserFunctions.getInstance().getlistIPMayIn().size(); i++) {
			if (two == Integer.parseInt(UserFunctions.getInstance()
					.getlistIPMayIn().get(i).getId())) {
				ip1 = UserFunctions.getInstance().getlistIPMayIn().get(i)
						.getIp_address();
				break;
			}
		}

	}

	private Runnable mMyRunnable = new Runnable() {
		@Override
		public void run() {

			if (UserFunctions.countMenu == PosApp.listOrderData.size()) {
				PrinterFunctions.PrintPosCOPY(activity, activity, "USB:", "",
						activity.getResources(), "");

				PrinterFunctions.PrintPos(activity, activity,
						"TCP:192.168.1.204", "", activity.getResources(), "");
				PrinterFunctions.PrintPos(activity, activity,
						"TCP:192.168.1.209", "", activity.getResources(), "");

				for (int i = 0; i < PosApp.listOrderData2.size(); i++) {
					for (int j = 0; j < UserFunctions.getInstance()
							.getlistMayIn().size(); j++) {
						if (PosApp.listOrderData2
								.get(i)
								.getGroupCode()
								.equals(UserFunctions.getInstance()
										.getlistMayIn().get(j).getItem_group())) {
							Log.v("HAI1", "co may in");
							int two = 0;
							try {
								two = Integer.parseInt(UserFunctions
										.getInstance().getlistMayIn().get(j)
										.getPrinter_two());
							} catch (Exception e) {
								// TODO: handle exception
							}

							int one = Integer.parseInt(UserFunctions
									.getInstance().getlistMayIn().get(j)
									.getPrinter_one());
							if (two > 0) {
								// Log.v("HAI", "2 may");
								getIp1(one);
								getIp2(two);
								if (i % 2 == 0) {
									PrinterFunctions.PrintPosOne(activity,
											activity, "TCP:" + ip1, "",
											activity.getResources(), "", i);
									// if (!PosApp.listOrderData2.get(i)
									// .getGroupCode().equals("37")) {
									// PrinterFunctions.PrintPosOne(activity,
									// activity, "TCP:192.168.1.210",
									// "", activity.getResources(),
									// "", i);
									// }

								} else {
									PrinterFunctions.PrintPosOne(activity,
											activity, "TCP:" + ip2, "",
											activity.getResources(), "", i);

								}

							} else {

								getIp1(one);
								PrinterFunctions.PrintPosOne(activity,
										activity, "TCP:" + ip1, "",
										activity.getResources(), "", i);
							}
						}

					}
					if (PosApp.listOrderData2.get(i).getIsStyle() != null) {
						if (PosApp.listOrderData2.get(i).getIsStyle()
								.equals("1")) {
							PrinterFunctions.PrintPosOne(activity, activity,
									"TCP:192.168.1.207", "",
									activity.getResources(), "", i);
							Log.v("HAI", "send request 1");
						}
					}

				}

				UserFunctions.countMenu = 0;
				PosApp.listOrderData.clear();
				PosApp.listOrderData2.clear();
				Utilities.showDialogNoBack1(activity, "Order have been sent",
						activity).show();

			} else {
				Log.v("HAI", "delay");
				Handler myHandler = new Handler();
				myHandler.postDelayed(mMyRunnable, 1000);
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v == btnSendOrder) {
			if (Utilities.getGlobalVariable(activity).isPrintBill) {
				Utilities.showDialogNoBack(activity, "Not Allowed").show();
			} else {
				Utils.saveWhenExit(MainActivity.activity);

				for (int i = 0; i < PosApp.listOrderData.size(); i++) {
					try {
						int a = Integer.parseInt(PosApp.listOrderData.get(i)
								.getItemCode());
						getItemSetmenu(PosApp.listOrderData.get(i)
								.getItemCode());
					} catch (Exception e) {
						UserFunctions.countMenu++;
						PosApp.listOrderData2.add(PosApp.listOrderData.get(i));
					}

				}

				Handler myHandler = new Handler();
				myHandler.postDelayed(mMyRunnable, 1000);
			}

		}
		if (v == btnSearchItem) {
			if (!isCan) {
				edSearch.setVisibility(View.VISIBLE);
				edSearch1.setVisibility(View.GONE);
				isCan = true;
				edSearch.requestFocus();
				btnSearchItem.setText(getString(R.string.str_scan));

			} else {
				edSearch.setVisibility(View.GONE);
				edSearch1.setVisibility(View.VISIBLE);
				isCan = false;
				edSearch1.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edSearch1.getWindowToken(), 0);
				// btnSearchItem.setText("Scan");
				btnSearchItem.setText(getString(R.string.str_searchItem));
			}

		}
		if (v == btnRefund) {
			refund = true;
			DialogRefund a = new DialogRefund();
			a.showDialogSelectImg(activity);
		}
		if (v == btnReprintOrder) {
			if (Utilities.getGlobalVariable(activity).isHaveSendOrder) {
				if (PosApp.listOrderData.size() > 0) {
						PrinterFunctions.PrintPos1(activity, activity, "USB:",
								"", activity.getResources(), "");
				}

			}
		}
		if (v == btnPrinter) {

			DialogNumPrint a = new DialogNumPrint();
			a.showNoteDialog(activity, activity);
		}
		if (v == btnViewReport) {
			registerForContextMenu(btnViewReport);
			openContextMenu(btnViewReport);
		}
		if (v == btnNum0) {
			Items.num = Items.num + "0";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum1) {
			Items.num = Items.num + "1";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum2) {
			Items.num = Items.num + "2";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum3) {
			Items.num = Items.num + "3";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum4) {
			Items.num = Items.num + "4";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum5) {
			Items.num = Items.num + "5";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum6) {
			Items.num = Items.num + "6";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum7) {
			Items.num = Items.num + "7";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum8) {
			Items.num = Items.num + "8";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnNum9) {
			Items.num = Items.num + "9";
			Utils.showToast(Items.num, activity);
		}
		if (v == btnPlus) {
			Utils.Plus(activity, qly);

		}
		if (v == btnMinus) {
			if (qly >= 0) {

				Utils.Minus(activity, qly);
			}
			if (PosApp.listOrderData.size() == 0) {
				ComDDUtilities.showWelcome();
			}
		}
		if (v == btnCancelBill) {
			btnDisCountValue.setText("0.00");
			PosApp.listOrderData.clear();
			refund = false;
			notifidataOrderList();
			ComDDUtilities.showWelcome();
			MainActivity.count = 0;
			UserFunctions.getInstance().getGetPrintList().clear();
			UserFunctions.countMenu = 0;
			UserFunctions.getInstance().getlistItemMenu().clear();

		}
		if (v == btnPOC) {
			Utilities.getGlobalVariable(activity).isPOC = true;

		}
		if (v == btnBillDiscount) {
			if (Utilities.getGlobalVariable(activity).isPrintBill) {
				Utilities.showDialogNoBack(activity, "Not Allowed").show();
			} else {
				if (PosApp.listOrderData.size() != 0) {
					DialogBillDiscount a = new DialogBillDiscount();
					a.showDialogBillDiscount(activity, activity,
							btnSubTotalValue.getText().toString());
				}
			}

		}
		if (v == btnEditPax) {
			DialogPaxNumber2 pax = new DialogPaxNumber2();
			pax.showNoteDialog(activity, activity);

		}
		if (v == btnSplitBill) {
//			 Intent i = new Intent(activity, SplitBillActivity.class);
//			 startActivity(i);
		}
		if (v == btnSearchBill) {
			DialogSearchBill a = new DialogSearchBill();
			a.showDialogEditSale(activity, activity);

		}
		if (v == btnCustom) {
			// DialogCustomItem a = new DialogCustomItem();
			// a.showDialogDefineCustomItem(activity, activity);
			replaceFragment(ConstantValue.MENU, false);

		}
		if (v == btnStyleAndRequest) {
			if (Utilities.getGlobalVariable(activity).isPrintBill) {
				Utilities.showDialogNoBack(activity, "Not Allowed").show();
			} else {
				if (qly != -1 && PosApp.listOrderData.size() > 0) {
					Intent i = new Intent(activity,
							StyleAndRequestActivity.class);
					startActivity(i);
				}
			}
		}
		if (v == btnResendKitchen) {
			if (Utilities.getGlobalVariable(activity).isHaveSendOrder) {
				if (Utilities.getGlobalVariable(activity).isPrintBill) {
					Utilities.showDialogNoBack(activity, "Not Allowed").show();
				} else {
					DialogResendKitchen di = new DialogResendKitchen();
					di.showDialogEditSale(activity, activity);
				}
			}

		}
		if (v == btnPrintBill) {
			if (Utilities.getGlobalVariable(activity).isHaveSendOrder) {
				// Utils.getReceiptNo(activity);
				PrinterFunctions.PrintPosBill(MainActivity.activity, activity,
						"USB:", "", activity.getResources(),
						Utilities.getGlobalVariable(activity).receiptNo);
				callAdv();
				Utilities.getGlobalVariable(activity).isPrintBill = true;
				UserFunctions.countMenu = 0;
				PosApp.listOrderData.clear();
				PosApp.listOrderData2.clear();
				PosApp.listOrderSplit.clear();
				activity.finish();
			}

		}
		if (v == btnDelete) {
			Items.num = "";
			Utils.showToast("0", activity);
			// addList("123");
		}
		if (v == btnPrintNoSave) {
			isPrintNoSave = true;

		}

	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateRequestBill"));
		params.add(new BasicNameValuePair("table", Utilities
				.getGlobalVariable(activity).tableNum));
		params.add(new BasicNameValuePair("saleID", UserFunctions.getInstance()
				.getSaleModel().getSaleID()));
		UserFunctions.getInstance().callListUser(activity, params, false);
	}

	private Handler handler;
	private Timer myTimer;

	public static boolean isPrintNoSave = false;

	public void printWithoutRec(String recep) {
		PrinterFunctions.PrintPos(activity, activity, "USB:", "",
				activity.getResources(), recep);

	}

	private void payCreaditCard(String credit) {
		if (PosApp.listOrderData.size() != 0) {
			Utils.pay(activity, "2", "", "", "", "0", "0");
			if (Utilities.getGlobalVariable(activity).isHaveSendOrder) {
				PrinterFunctions.PrintPospay(MainActivity.activity, activity,
						"USB:", "", activity.getResources(),
						Utilities.getGlobalVariable(activity).receiptNo);
			}
			Utils.clearList(activity);
		}
	}

	public void updatequantity(String itemid, String qty) {
		ItemsDataSource a = new ItemsDataSource(activity, activity);
		a.Updatequantity(itemid, qty);
	}

	public void openAndPrint(String recep) {
		mCashDrawer.openCashDrawer();
		if (refund == false) {
			PrinterFunctions.PrintPos(activity, activity, "USB:", "",
					activity.getResources(), recep);
		} else {
			PrinterFunctions.PrintPosReturn(activity, activity, "USB:", "",
					activity.getResources(), recep);
			refund = false;
		}

		// PrinterFunctions.PrintSampleReceiptbyDotPrinter(MainActivity.this,
		// "USB:", "");
	}

	public void openCashDrawer() {
		mCashDrawer.openCashDrawer();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		// Context menu
		if (v == btnViewReport) {
			menu.setHeaderTitle(getString(R.string.str_ChooseReport));
			menu.add(Menu.NONE, CONTEXT_MENU_VIEW, Menu.NONE,
					getString(R.string.str_SalesReport));
			Utilities.getGlobalVariable(activity).isReport = true;
		}

	}

	final int CONTEXT_MENU_VIEW = 1;

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case CONTEXT_MENU_VIEW: {
			Intent i = new Intent(this, DialogSaleReport.class);
			startActivity(i);

		}
			break;

		}

		return super.onContextItemSelected(item);
	}

	private void exportDB(final String a, final String name) {
		final String inFileName = a;
		java.io.File outFileName = new java.io.File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
						+ "/" + name);
		// final String outFileName = "/data/app/chiettinh.png";

		// File file = new File(outFileName);
		// if (file != null && !file.exists())
		{
			InputStream myInput = null; // MainApplication.getAppContext().getAssets().open(DB_ASSETS_NAME);
			// Path to the just created empty db
			// Open the empty db as the output stream
			OutputStream myOutput = null; // new FileOutputStream(outFileName);

			try {
				myInput = new FileInputStream(inFileName);
				myOutput = new FileOutputStream(outFileName);

				// transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;

				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}

				// Close the streams
				myOutput.flush();
				myOutput.close();
				myInput.close();
				// DialogGroup.image = outFileName.toString();
				// DialogItems.image = outFileName.toString();
				// LoginDialog.linkImage = outFileName.toString();
				// db.execSQL(TABLE_CHECK_EXIST);
				Log.i("Export DB", "Done copy");
				// Utils.showMess("Sao lÆ°u cÆ¡ sÆ¡ dá»¯ liá»‡u thĂ nh cĂ´ng!");
			} catch (Exception e) {
				Log.i("Export DB", e.toString());

			} finally {

			}
		}
	}

	private static TextView tvItemCode;
	private static TextView tvItemName;
	private static TextView tvUnitPrice;
	private static TextView tvQty;
	private static TextView tvDiscount;
	private static TextView tvAmount;
	private static Button tvTotal;

	public static void setText() {
		Log.v("langid", ""
				+ Utilities.getGlobalVariable(activity).language_code);
		if (langid == 1) {

			Locale locale = new Locale("en");// en
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			activity.getApplicationContext().getResources()
					.updateConfiguration(config, null);
		} else {
			Locale locale = new Locale("zh-TW");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			activity.getApplicationContext().getResources()
					.updateConfiguration(config, null);
		}

		btnCustom.setText(activity.getString(R.string.str_Menu));
		btnSearchBill.setText(activity.getString(R.string.str_searchBill));
		btnSplitBill.setText(activity.getString(R.string.str_splitBill));
		btnPrintBill.setText(activity.getString(R.string.str_printBill));
		btnPrinter.setText(activity.getString(R.string.str_printer));
		btnRefund.setText(activity.getString(R.string.str_refund));
		btnPOC.setText(activity.getString(R.string.str_foc));
		btnBillDiscount.setText(activity.getString(R.string.str_billdiscount));
		btnSendOrder.setText(activity.getString(R.string.str_sendOrder));
		btnEditPax.setText(activity.getString(R.string.str_unPaid));
		btnCancelBill.setText(activity.getString(R.string.str_cancelBill));

		btnSvC.setText(activity.getString(R.string.str_SvC));
		btnSubTotal.setText(activity.getString(R.string.str_subTotal));
		btnDisCount.setText(activity.getString(R.string.str_discount));
		btnPlus.setText(activity.getString(R.string.str_plus));
		btnMinus.setText(activity.getString(R.string.str_minus));
		tvTotal.setText(activity.getString(R.string.str_totalAmount));
		// tvItemCode.setText(activity.getString(R.string.str_itemCode));
		tvItemName.setText(activity.getString(R.string.str_itemName));
		tvUnitPrice.setText(activity.getString(R.string.str_unitPrice));
		tvQty.setText(activity.getString(R.string.str_qty));
		tvDiscount.setText(activity.getString(R.string.str_discount));
		tvAmount.setText(activity.getString(R.string.str_amount));

		btnReprintOrder.setText(activity.getString(R.string.str_manageItems));
		btnResendKitchen.setText(activity.getString(R.string.str_stockInItem));
		btnStyleAndRequest.setText(activity
				.getString(R.string.str_inventoryCheck));
		btnViewReport.setText(activity.getString(R.string.str_viewReport));
		btnPrinter.setText(activity.getString(R.string.str_printer));
		btnPrintNoSave.setText(activity.getString(R.string.str_printer));
		btnSearchItem.setText(activity.getString(R.string.str_scan));

		// tvHome.setText(cont.getString(R.string.str_home));

		((CustomFragmentActivity) activity).replaceFragment(
				ConstantValue.HOME_FRAGMENT, false);
	}

}

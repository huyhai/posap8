package com.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.SessionManager;
import com.pos.libs.ViewUtils;
import com.pos.ui.DialogEditTable;
import com.pos.ui.DialogPaxNumber;
import com.pos.ui.LoginDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Table extends Activity implements OnClickListener {
	private RelativeLayout rlTop;
	private Button btnV1;
	private Button btnV2;
	private Button btnV3;
	private Button btnV9;
	private Button btnV5;
	private Button btnV6;
	private Button btnV7;
	private Button btnV8;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;

	private Button btn11;
	private Button btn11A;
	private Button btn21;
	private Button btn21A;
	private Button btn31;
	private Button btn31A;
	private Button btn51;
	private Button btn51A;

	private Button btn63;
	private Button btn62;
	private Button btn61;

	private Button btn67;
	private Button btn66;
	private Button btn65;

	private Button btn15;
	private Button btn16;
	private Button btn26;

	private Button btn71;
	private Button btn75;
	private Button btn78;
	private Button btn72;
	private Button btn76;
	private Button btn79;
	private Button btn73;
	private Button btn77;
	private Button btnFake;
	private Button btn83;
	private Button btn87;
	private Button btn89;
	private Button btn82;
	private Button btn86;
	private Button btn88;
	private Button btn81;
	private Button btn85;
	private Button btnFake1;
	private Button btn56;
	private Button btn55;
	private Button btn53;
	private Button btn52A;
	private Button btn52;

	private Button btn32;
	private Button btn33;
	private Button btn35;
	private Button btn36;
	private Button btn37;

	private Button btn22;
	private Button btn23;
	private Button btn25;
	private Button btn27;
	private Button btn12;
	private Button btn13;
	private Button btn17;
	private Button btn18;
	private Button btn38;
	private Button btn28;

	private ImageView imgLogoDinner;
	private Button btnRefrest;
	private Button btnRequestPay;
	private Button btnMutilBill;
	private Button btnOccupied;
	private Button btnAvailble;
	private Button btnReserved;
	private Button btnLogo;
	public static Activity ac;
	private Button btnSwitchLang;

	// private Button btnEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ac = Table.this;
		session = new SessionManager(ac);
		init();
		setdata();

	}

	private void getTableStatus() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getTableStatus"));
		UserFunctions.getInstance().getTableStatus(ac, params, false);
	}

	private void getTableNum(String tabnum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getPaxNum"));
		params.add(new BasicNameValuePair("tablenum", tabnum));
		UserFunctions.getInstance().getPaxNumber(ac, params, false);
	}

	private SessionManager session;

	private void checkLogin() {
		if (session.isLoggedIn()) {
			HashMap<String, String> user = session.getUserLogin();
			String name = user.get(SessionManager.KEY_NAME);
			String pass = user.get(SessionManager.KEY_PASS);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "loginnocash"));
			params.add(new BasicNameValuePair("username", name));
			params.add(new BasicNameValuePair("pass", pass));
			UserFunctions.getInstance().callLoginUser(ac, params, false);

		} else {

			LoginDialog a = new LoginDialog();
			a.showDialogSelectImg(ac);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getTableStatus();
		IntentFilter intentGetKeySend = new IntentFilter();
		intentGetKeySend.addAction(UserFunctions.LOGINUSER);
		intentGetKeySend.addAction(UserFunctions.TABLESTATUS);
		intentGetKeySend.addAction(UserFunctions.PAXNUM);
		intentGetKeySend.addAction(UserFunctions.GETSALES);
		intentGetKeySend.addAction(UserFunctions.CHECKEND);
		intentGetKeySend.addAction(UserFunctions.ENDSHIFT);
		registerReceiver(receiver, intentGetKeySend);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equalsIgnoreCase(UserFunctions.LOGINUSER)) {
//				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
//					// getSale(tablenum);
//					Intent i = new Intent(ac, MainActivity.class);
//					ac.startActivity(i);
//
//					Utilities.getGlobalVariable(ac).isLogin = true;
//				} else {
//					Utilities.getGlobalVariable(ac).isLogin = false;
//				}
//			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.TABLESTATUS)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					for (int i = 0; i < UserFunctions.getInstance()
							.getListTable().size(); i++) {
						if (UserFunctions.getInstance().getListTable().get(i)
								.getStatus().equals("0")) {
							compare(UserFunctions.getInstance().getListTable()
									.get(i).getName(), 4);
						} else if (UserFunctions.getInstance().getListTable()
								.get(i).getStatus().equals("1")) {
							compare(UserFunctions.getInstance().getListTable()
									.get(i).getName(), 1);
						} else if (UserFunctions.getInstance().getListTable()
								.get(i).getStatus().equals("2")) {
							compare(UserFunctions.getInstance().getListTable()
									.get(i).getName(), 2);
						} else if (UserFunctions.getInstance().getListTable()
								.get(i).getStatus().equals("3")) {
							compare(UserFunctions.getInstance().getListTable()
									.get(i).getName(), 3);
						}

					}
				} else {
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.PAXNUM)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					Utilities.getGlobalVariable(ac).numberCustomer = UserFunctions
							.getInstance().getPaxModel()
							.getNumber_of_customers();
					DialogPaxNumber pax = new DialogPaxNumber();
					pax.showNoteDialog(ac, ac);
				} else {
					Utilities.getGlobalVariable(ac).numberCustomer = UserFunctions
							.getInstance().getPaxModel()
							.getNumber_of_customers();
					DialogPaxNumber pax = new DialogPaxNumber();
					pax.showNoteDialog(ac, ac);
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.GETSALES)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					if(UserFunctions.getInstance().getSaleModel().getIsPrintBill().equals("1")){
						Utilities.getGlobalVariable(ac).isPrintBill=true;
					}else{
						Utilities.getGlobalVariable(ac).isPrintBill=false;
					}
					Utilities.getGlobalVariable(ac).isUpdate = true;
					 Intent i = new Intent(ac, MainActivity.class);
					 ac.startActivity(i);
					Utilities.getGlobalVariable(ac).numberCustomer = UserFunctions
							.getInstance().getSaleModel()
							.getNumber_of_customers();
				} else {
					DialogPaxNumber pax = new DialogPaxNumber();
					pax.showNoteDialog(ac, ac);

					Utilities.getGlobalVariable(ac).isUpdate = false;
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.CHECKEND)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					Toast.makeText(ac, "Not Allowed", Toast.LENGTH_SHORT)
							.show();
				} else {
					LoginDialog a = new LoginDialog();
					a.showDialogSelectImg(ac);
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.ENDSHIFT)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("Message")
							.setMessage("End Shift Successfuly")
							.setNeutralButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												final DialogInterface dialog,
												final int id) {
											session.saveLogin("", "", false);
											Utilities.getGlobalVariable(ac).isLogin = false;
											Intent intent1 = new Intent(
													getApplicationContext(),
													Table.class);
											intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent1);
											dialog.dismiss();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

					// end shift finish
				} else {
					// end shift failed
				}
			}

		}
	};

	private void compare(String name, int cl) {
		if (name.equals("28")) {
			btn28.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("V1")) {
			btnV1.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V2")) {
			btnV2.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V3")) {
			btnV3.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V9")) {
			btnV9.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V5")) {
			btnV5.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V6")) {
			btnV6.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V7")) {
			btnV7.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("V8")) {
			btnV8.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("1")) {
			btn1.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("2")) {
			btn2.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("3")) {
			btn3.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("5")) {
			btn5.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("6")) {
			btn6.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("7")) {
			btn7.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("8")) {
			btn8.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("11")) {
			btn11.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("11A")) {
			btn11A.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("21")) {
			btn21.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("21A")) {
			btn21A.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("31")) {
			btn31.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("31A")) {
			btn31A.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("51")) {
			btn51.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("51A")) {
			btn51A.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("61")) {
			btn61.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("62")) {
			btn62.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("63")) {
			btn63.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("67")) {
			btn67.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("66")) {
			btn66.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("65")) {
			btn65.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("15")) {
			btn15.setBackgroundResource(setHinhvuongVang(cl));
		} else if (name.equals("16")) {
			btn16.setBackgroundResource(setHinhvuongVang(cl));
		} else if (name.equals("26")) {
			btn26.setBackgroundResource(setHinhvuongVang(cl));
		} else if (name.equals("71")) {
			btn71.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("75")) {
			btn75.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("78")) {
			btn78.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("72")) {
			btn72.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("76")) {
			btn76.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("79")) {
			btn79.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("73")) {
			btn73.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("77")) {
			btn77.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("83")) {
			btn83.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("87")) {
			btn87.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("89")) {
			btn89.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("82")) {
			btn82.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("86")) {
			btn86.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("88")) {
			btn88.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("81")) {
			btn81.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("85")) {
			btn85.setBackgroundResource(setHinhtronVang(cl));
		} else if (name.equals("56")) {
			btn56.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("55")) {
			btn55.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("53")) {
			btn53.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("52A")) {
			btn52A.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("52")) {
			btn52.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("38")) {
			btn38.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("18")) {
			btn18.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("17")) {
			btn17.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("13")) {
			btn13.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("12")) {
			btn12.setBackgroundResource(setHinhvuong(cl));
		} else if (name.equals("27")) {
			btn27.setBackgroundResource(setHinhTugiac(cl));
		} else if (name.equals("25")) {
			btn25.setBackgroundResource(setHinhTugiac(cl));
		} else if (name.equals("23")) {
			btn23.setBackgroundResource(setHinhTugiac(cl));
		} else if (name.equals("22")) {
			btn22.setBackgroundResource(setHinhTugiac(cl));
		} else if (name.equals("37")) {
			btn37.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("36")) {
			btn36.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("35")) {
			btn35.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("33")) {
			btn33.setBackgroundResource(setHinhtron(cl));
		} else if (name.equals("32")) {
			btn32.setBackgroundResource(setHinhtron(cl));
		}

	}

	private int setHinhvuong(int cl) {
		if (cl == 1) {
			return R.drawable.haiblue;
		} else if (cl == 2) {
			return R.drawable.haigreen;
		} else if (cl == 3) {
			return R.drawable.haired;
		}
		return R.drawable.available;
	}

	private int setHinhvuongVang(int cl) {
		if (cl == 1) {
			return R.drawable.haiblue;
		} else if (cl == 2) {
			return R.drawable.haigreen;
		} else if (cl == 3) {
			return R.drawable.haired;
		}
		return R.drawable.vuong_vang;
	}

	private int setHinhtronVang(int cl) {
		if (cl == 1) {
			return R.drawable.namblue;
		} else if (cl == 2) {
			return R.drawable.namgreen;
		} else if (cl == 3) {
			return R.drawable.namred;
		}
		return R.drawable.tron_vang;
	}

	private int setHinhTugiac(int cl) {
		if (cl == 1) {
			return R.drawable.bonblue;
		} else if (cl == 2) {
			return R.drawable.bongreen;
		} else if (cl == 3) {
			return R.drawable.bonred;
		}
		return R.drawable.diamond_table;
	}

	private int setHinhtron(int cl) {
		if (cl == 1) {
			return R.drawable.namblue;
		} else if (cl == 2) {
			return R.drawable.namgreen;
		} else if (cl == 3) {
			return R.drawable.namred;
		}
		return R.drawable.round_table;
	}

	private void setdata() {
	}

	private void init() {
		// btnEnd = (Button) findViewById(R.id.btnEnd);
		btnSwitchLang = (Button) findViewById(R.id.btnSwitchLang);
		rlTop = (RelativeLayout) findViewById(R.id.rlTop);
		btnV1 = (Button) findViewById(R.id.btnV1);
		btnV2 = (Button) findViewById(R.id.btnV2);
		btnV3 = (Button) findViewById(R.id.btnV3);
		btnV9 = (Button) findViewById(R.id.btnV9);
		btnV5 = (Button) findViewById(R.id.btnV5);
		btnV6 = (Button) findViewById(R.id.btnV6);
		btnV7 = (Button) findViewById(R.id.btnV7);
		btnV8 = (Button) findViewById(R.id.btnV8);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn5 = (Button) findViewById(R.id.btn5);
		btn6 = (Button) findViewById(R.id.btn6);
		btn7 = (Button) findViewById(R.id.btn7);
		btn8 = (Button) findViewById(R.id.btn8);
		btn11 = (Button) findViewById(R.id.btn11);
		btn11A = (Button) findViewById(R.id.btn11A);
		btn21 = (Button) findViewById(R.id.btn21);
		btn21A = (Button) findViewById(R.id.btn21A);
		btn31 = (Button) findViewById(R.id.btn31);
		btn31A = (Button) findViewById(R.id.btn31A);
		btn51 = (Button) findViewById(R.id.btn51);
		btn51A = (Button) findViewById(R.id.btn51A);
		btn61 = (Button) findViewById(R.id.btn61);
		btn62 = (Button) findViewById(R.id.btn62);
		btn63 = (Button) findViewById(R.id.btn63);
		btn67 = (Button) findViewById(R.id.btn67);
		btn66 = (Button) findViewById(R.id.btn66);
		btn65 = (Button) findViewById(R.id.btn65);
		btn15 = (Button) findViewById(R.id.btn15);
		btn16 = (Button) findViewById(R.id.btn16);
		btn26 = (Button) findViewById(R.id.btn26);
		btn71 = (Button) findViewById(R.id.btn71);
		btn75 = (Button) findViewById(R.id.btn75);
		btn78 = (Button) findViewById(R.id.btn78);
		btn72 = (Button) findViewById(R.id.btn72);
		btn76 = (Button) findViewById(R.id.btn76);
		btn79 = (Button) findViewById(R.id.btn79);
		btn73 = (Button) findViewById(R.id.btn73);
		btn77 = (Button) findViewById(R.id.btn77);
		btnFake = (Button) findViewById(R.id.btnFake);
		btn83 = (Button) findViewById(R.id.btn83);
		btn87 = (Button) findViewById(R.id.btn87);
		btn89 = (Button) findViewById(R.id.btn89);
		btn82 = (Button) findViewById(R.id.btn82);
		btn86 = (Button) findViewById(R.id.btn86);
		btn88 = (Button) findViewById(R.id.btn88);
		btn81 = (Button) findViewById(R.id.btn81);
		btn85 = (Button) findViewById(R.id.btn85);
		btnFake1 = (Button) findViewById(R.id.btnFake1);
		btn56 = (Button) findViewById(R.id.btn56);
		btn55 = (Button) findViewById(R.id.btn55);
		btn53 = (Button) findViewById(R.id.btn53);
		btn52A = (Button) findViewById(R.id.btn52A);
		btn52 = (Button) findViewById(R.id.btn52);

		btn32 = (Button) findViewById(R.id.btn32);
		btn33 = (Button) findViewById(R.id.btn33);
		btn35 = (Button) findViewById(R.id.btn35);
		btn36 = (Button) findViewById(R.id.btn36);
		btn37 = (Button) findViewById(R.id.btn37);
		btn22 = (Button) findViewById(R.id.btn22);
		btn23 = (Button) findViewById(R.id.btn23);
		btn25 = (Button) findViewById(R.id.btn25);
		btn27 = (Button) findViewById(R.id.btn27);
		btn12 = (Button) findViewById(R.id.btn12);
		btn13 = (Button) findViewById(R.id.btn13);
		btn17 = (Button) findViewById(R.id.btn17);
		btn18 = (Button) findViewById(R.id.btn18);
		btn38 = (Button) findViewById(R.id.btn38);
		btn28 = (Button) findViewById(R.id.btn28);

		imgLogoDinner = (ImageView) findViewById(R.id.imgLogoDinner);
		btnRefrest = (Button) findViewById(R.id.btnRefrest);
		btnRequestPay = (Button) findViewById(R.id.btnRequestPay);
		btnMutilBill = (Button) findViewById(R.id.btnMutilBill);
		btnOccupied = (Button) findViewById(R.id.btnOccupied);
		btnAvailble = (Button) findViewById(R.id.btnAvailble);
		btnReserved = (Button) findViewById(R.id.btnReserved);
		btnLogo = (Button) findViewById(R.id.btnLogo);
		// btnV1.setOnClickListener(this);
		// btnV2.setOnClickListener(this);
		// btnV3.setOnClickListener(this);
		// btnV4.setOnClickListener(this);
		// btnV5.setOnClickListener(this);
		// btnV6.setOnClickListener(this);
		// btnV7.setOnClickListener(this);
		// btnV8.setOnClickListener(this);
		// btnV9.setOnClickListener(this);
		// btnV10.setOnClickListener(this);
		btnV3.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V3");
				return true;
			}
		});
		btnV1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V1");
				return true;
			}
		});
		btnV2.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V2");
				return true;
			}
		});
		btnV5.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V5");
				return true;
			}
		});
		btnV6.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V6");
				return true;
			}
		});
		btnV7.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V7");
				return true;
			}
		});
		btnV8.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V8");
				return true;
			}
		});
		btnV9.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "V9");
				return true;
			}
		});
		btn1.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "1");
				return true;
			}
		});
		btn2.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "2");
				return true;
			}
		});
		btn3.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "3");
				return true;
			}
		});
		btn5.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "5");
				return true;
			}
		});
		btn6.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "6");
				return true;
			}
		});
		btn7.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "7");
				return true;
			}
		});
		btn6.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "6");
				return true;
			}
		});
		btn7.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "7");
				return true;
			}
		});
		btn8.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "8");
				return true;
			}
		});
		btn11.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "9");
				return true;
			}
		});
		btn11A.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "11A");
				return true;
			}
		});
		btn12.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "12");
				return true;
			}
		});
		btn13.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "13");
				return true;
			}
		});
		btn15.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "15");
				return true;
			}
		});
		btn16.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "16");
				return true;
			}
		});
		btn17.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "17");
				return true;
			}
		});
		btn18.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "18");
				return true;
			}
		});
		btn21.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "21");
				return true;
			}
		});
		btn21A.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "21A");
				return true;
			}
		});
		btn22.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "22");
				return true;
			}
		});
		btn23.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "23");
				return true;
			}
		});
		btn25.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "25");
				return true;
			}
		});
		btn26.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "26");
				return true;
			}
		});
		btn27.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "27");
				return true;
			}
		});
		btn28.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "28");
				return true;
			}
		});
		btn31.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "31");
				return true;
			}
		});
		btn31A.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "31A");
				return true;
			}
		});
		btn32.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "32");
				return true;
			}
		});
		btn33.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "33");
				return true;
			}
		});
		btn35.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "35");
				return true;
			}
		});
		btn36.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "36");
				return true;
			}
		});
		btn37.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "37");
				return true;
			}
		});
		btn38.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "38");
				return true;
			}
		});
		btn51.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "51");
				return true;
			}
		});
		btn51A.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "51A");
				return true;
			}
		});
		btn52.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "52");
				return true;
			}
		});
		btn52A.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "52A");
				return true;
			}
		});
		btn53.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "53");
				return true;
			}
		});
		btn55.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "55");
				return true;
			}
		});
		btn56.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "56");
				return true;
			}
		});
		btn61.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "61");
				return true;
			}
		});
		btn62.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "62");
				return true;
			}
		});
		btn63.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "63");
				return true;
			}
		});
		btn65.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "65");
				return true;
			}
		});
		btn66.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "66");
				return true;
			}
		});
		btn67.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "67");
				return true;
			}
		});
		btn71.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "71");
				return true;
			}
		});
		btn72.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "72");
				return true;
			}
		});
		btn73.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "73");
				return true;
			}
		});
		btn75.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "75");
				return true;
			}
		});
		btn76.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "76");
				return true;
			}
		});
		btn77.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "77");
				return true;
			}
		});
		btn78.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "78");
				return true;
			}
		});
		btn79.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "79");
				return true;
			}
		});
		btn81.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "81");
				return true;
			}
		});
		btn82.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "82");
				return true;
			}
		});
		btn83.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "83");
				return true;
			}
		});
		btn85.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "85");
				return true;
			}
		});
		btn86.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "86");
				return true;
			}
		});
		btn87.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "87");
				return true;
			}
		});
		btn88.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "88");
				return true;
			}
		});
		btn89.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DialogEditTable a = new DialogEditTable();
				a.showDialog(ac, "89");
				return true;
			}
		});

		// btnEnd.setOnClickListener(this);
		btnV1.setOnClickListener(this);
		btnV2.setOnClickListener(this);
		btnV3.setOnClickListener(this);
		btnV9.setOnClickListener(this);
		btnV5.setOnClickListener(this);
		btnV6.setOnClickListener(this);
		btnV7.setOnClickListener(this);
		btnV8.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn11.setOnClickListener(this);
		btn11A.setOnClickListener(this);
		btn21.setOnClickListener(this);
		btn21A.setOnClickListener(this);
		btn31.setOnClickListener(this);
		btn31A.setOnClickListener(this);
		btn51.setOnClickListener(this);
		btn51A.setOnClickListener(this);
		btn61.setOnClickListener(this);
		btn62.setOnClickListener(this);
		btn63.setOnClickListener(this);
		btn67.setOnClickListener(this);
		btn66.setOnClickListener(this);
		btn65.setOnClickListener(this);
		btn15.setOnClickListener(this);
		btn16.setOnClickListener(this);
		btn26.setOnClickListener(this);
		btn71.setOnClickListener(this);
		btn75.setOnClickListener(this);
		btn78.setOnClickListener(this);
		btn72.setOnClickListener(this);
		btn76.setOnClickListener(this);
		btn79.setOnClickListener(this);
		btn73.setOnClickListener(this);
		btn77.setOnClickListener(this);
		btn83.setOnClickListener(this);
		btn87.setOnClickListener(this);
		btn89.setOnClickListener(this);
		btn82.setOnClickListener(this);
		btn86.setOnClickListener(this);
		btn88.setOnClickListener(this);
		btn81.setOnClickListener(this);
		btn85.setOnClickListener(this);
		btn56.setOnClickListener(this);
		btn55.setOnClickListener(this);
		btn53.setOnClickListener(this);
		btn52A.setOnClickListener(this);
		btn52.setOnClickListener(this);
		btnSwitchLang.setOnClickListener(this);
		btn32.setOnClickListener(this);
		btn33.setOnClickListener(this);
		btn35.setOnClickListener(this);
		btn36.setOnClickListener(this);
		btn37.setOnClickListener(this);
		btn22.setOnClickListener(this);
		btn23.setOnClickListener(this);
		btn25.setOnClickListener(this);
		btn27.setOnClickListener(this);
		btn12.setOnClickListener(this);
		btn13.setOnClickListener(this);
		btn17.setOnClickListener(this);
		btn18.setOnClickListener(this);
		btn38.setOnClickListener(this);
		btn28.setOnClickListener(this);
		btnRefrest.setOnClickListener(this);
		btnRequestPay.setOnClickListener(this);
		btnMutilBill.setOnClickListener(this);
		btnOccupied.setOnClickListener(this);
		btnAvailble.setOnClickListener(this);
		btnReserved.setOnClickListener(this);
		initlayout();
	}

	private void initlayout() {
		ViewUtils.setWidthHeight(imgLogoDinner, 4.5, 6, Table.this);
		ViewUtils.setHeight(rlTop, 6, Table.this);
		int w = 12;
		int h = 8;
		ViewUtils.setWidthHeight(btnV1, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV2, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV3, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV9, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV5, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV6, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV7, w, h, Table.this);
		ViewUtils.setWidthHeight(btnV8, w, h, Table.this);

		int w1 = 15;
		int h1 = 10;
		ViewUtils.setWidthHeight(btn1, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn2, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn3, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn5, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn6, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn7, w1, h1, Table.this);
		ViewUtils.setWidthHeight(btn8, w1, h1, Table.this);

		int w11 = 18;
		int h11 = 14;
		ViewUtils.setWidthHeight(btn11, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn11A, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn21, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn21A, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn31, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn31A, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn51, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn51A, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn61, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn62, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn63, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn15, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn16, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn26, w11, h11, Table.this);

		ViewUtils.setWidthHeight(btn12, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn13, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn17, w11, h11, Table.this);
		ViewUtils.setWidthHeight(btn18, w11, h11, Table.this);

		ViewUtils.setWidthHeight(btn22, w11, h11 - 2, Table.this);
		ViewUtils.setWidthHeight(btn23, w11, h11 - 2, Table.this);
		ViewUtils.setWidthHeight(btn25, w11, h11 - 2, Table.this);
		ViewUtils.setWidthHeight(btn27, w11, h11 - 1.5, Table.this);

		int wCicle = 16;
		int hCicle = 10;
		ViewUtils.setWidthHeight(btn67, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn66, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn65, wCicle, hCicle, Table.this);

		ViewUtils.setWidthHeight(btn71, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn75, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn78, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn72, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn76, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn79, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn73, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn77, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btnFake, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn83, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn87, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn89, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn82, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn86, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn88, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn56, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn81, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn85, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btnFake1, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn55, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn53, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn52A, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn52, wCicle, hCicle, Table.this);

		ViewUtils.setWidthHeight(btn32, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn33, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn35, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn36, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn37, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn38, wCicle, hCicle, Table.this);
		ViewUtils.setWidthHeight(btn28, wCicle, hCicle, Table.this);

		ViewUtils.setWidth(btnLogo, 3.5, Table.this);
		int wbtn = 9;
		int hbtn = 15;
		ViewUtils.setWidthHeight(btnRefrest, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnRequestPay, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnMutilBill, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnOccupied, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnAvailble, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnReserved, wbtn, hbtn, Table.this);
		ViewUtils.setWidthHeight(btnSwitchLang, wbtn, hbtn, Table.this);
		// ViewUtils.setWidthHeight(btnEnd, wbtn, hbtn, Table.this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnRefrest) {
			getTableStatus();
		}
		if (v == btnSwitchLang) {
			if (Utilities.getGlobalVariable(ac).language_code == 1) {
				Utilities.getGlobalVariable(ac).language_code = 2;
			} else if (Utilities.getGlobalVariable(ac).language_code == 2) {
				Utilities.getGlobalVariable(ac).language_code = 1;
			}
			if (Utilities.getGlobalVariable(ac).language_code == 1) {
				Locale locale = new Locale("en");
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				ac.getApplicationContext().getResources()
						.updateConfiguration(config, null);
				btnReserved.setText(ac.getString(R.string.str_Reserved));
				btnAvailble.setText(ac.getString(R.string.str_Available));
				btnOccupied.setText(ac.getString(R.string.str_Occupied));
				btnMutilBill.setText(ac.getString(R.string.str_MutipleBill));
				btnRequestPay.setText(ac
						.getString(R.string.str_RequestForPayment));
				btnRefrest.setText(ac.getString(R.string.str_Refresh));
				btnSwitchLang.setText(ac.getString(R.string.str_SwitchLang));
				MainActivity.langid = 1;
				// ac.getString(R.string.str_Refresh);

			} else if (Utilities.getGlobalVariable(ac).language_code == 2) {
				Locale locale = new Locale("zh-TW");
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				ac.getApplicationContext().getResources()
						.updateConfiguration(config, null);
				btnReserved.setText(ac.getString(R.string.str_Reserved));
				btnAvailble.setText(ac.getString(R.string.str_Available));
				btnOccupied.setText(ac.getString(R.string.str_Occupied));
				btnMutilBill.setText(ac.getString(R.string.str_MutipleBill));
				btnRequestPay.setText(ac
						.getString(R.string.str_RequestForPayment));
				btnRefrest.setText(ac.getString(R.string.str_Refresh));
				btnSwitchLang.setText(ac.getString(R.string.str_SwitchLang));

				MainActivity.langid = 2;
				// ac.getString(R.string.str_Refresh);

			}
		} else if (v == btn28) {
			choosenTB("28");
		} else if (v == btnV1) {
			choosenTB("V1");
		} else if (v == btnV2) {
			choosenTB("V2");
		} else if (v == btnV3) {
			choosenTB("V3");
		} else if (v == btnV9) {
			choosenTB("V9");
		} else if (v == btnV5) {
			choosenTB("V5");
		} else if (v == btnV6) {
			choosenTB("V6");
		} else if (v == btnV7) {
			choosenTB("V7");
		} else if (v == btnV8) {
			choosenTB("V8");
		} else if (v == btn1) {
			choosenTB("1");
		} else if (v == btn2) {
			choosenTB("2");
		} else if (v == btn3) {
			choosenTB("3");
		} else if (v == btn5) {
			choosenTB("5");
		} else if (v == btn6) {
			choosenTB("6");
		} else if (v == btn7) {
			choosenTB("7");
		} else if (v == btn8) {
			choosenTB("8");
		} else if (v == btn11) {
			choosenTB("11");
		} else if (v == btn11A) {
			choosenTB("11A");
		} else if (v == btn21) {
			choosenTB("21");
		} else if (v == btn21A) {
			choosenTB("21A");
		} else if (v == btn31) {
			choosenTB("31");
		} else if (v == btn31A) {
			choosenTB("31A");
		} else if (v == btn51) {
			choosenTB("51");
		} else if (v == btn51A) {
			choosenTB("51A");
		} else if (v == btn61) {
			choosenTB("61");
		} else if (v == btn62) {
			choosenTB("62");
		} else if (v == btn63) {
			choosenTB("63");
		} else if (v == btn67) {
			choosenTB("67");
		} else if (v == btn66) {
			choosenTB("66");
		} else if (v == btn65) {
			choosenTB("65");
		} else if (v == btn15) {
			choosenTB("15");
		} else if (v == btn16) {
			choosenTB("16");
		} else if (v == btn26) {
			choosenTB("26");
		} else if (v == btn71) {
			choosenTB("71");
		} else if (v == btn75) {
			choosenTB("75");
		} else if (v == btn78) {
			choosenTB("78");
		} else if (v == btn72) {
			choosenTB("72");
		} else if (v == btn76) {
			choosenTB("76");
		} else if (v == btn79) {
			choosenTB("79");
		} else if (v == btn73) {
			choosenTB("73");
		} else if (v == btn77) {
			choosenTB("77");
		} else if (v == btn83) {
			choosenTB("83");
		} else if (v == btn87) {
			choosenTB("87");
		} else if (v == btn89) {
			choosenTB("89");
		} else if (v == btn82) {
			choosenTB("82");
		} else if (v == btn86) {
			choosenTB("86");
		} else if (v == btn88) {
			choosenTB("88");
		} else if (v == btn81) {
			choosenTB("81");
		} else if (v == btn85) {
			choosenTB("85");
		} else if (v == btn56) {
			choosenTB("56");
		} else if (v == btn55) {
			choosenTB("55");
		} else if (v == btn53) {
			choosenTB("53");
		} else if (v == btn52A) {
			choosenTB("52A");
		} else if (v == btn52) {
			choosenTB("52");
		} else if (v == btn38) {
			choosenTB("38");
		} else if (v == btn18) {
			choosenTB("18");
		} else if (v == btn17) {
			choosenTB("17");
		} else if (v == btn13) {
			choosenTB("13");
		} else if (v == btn12) {
			choosenTB("12");
		} else if (v == btn27) {
			choosenTB("27");
		} else if (v == btn25) {
			choosenTB("25");
		} else if (v == btn23) {
			choosenTB("23");
		} else if (v == btn22) {
			choosenTB("22");
		} else if (v == btn37) {
			choosenTB("37");
		} else if (v == btn36) {
			choosenTB("36");
		} else if (v == btn35) {
			choosenTB("35");
		} else if (v == btn33) {
			choosenTB("33");
		} else if (v == btn32) {
			choosenTB("32");
		}
		// else if (v == btnEnd) {
		// Table.isEnd=true;
		// checkend();
		// }

	}

	// public static boolean isEnd = false;

	private void checkend() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "checkToEndShift"));
		UserFunctions.getInstance().checkEndShift(ac, params, false);
	}

	private void endShift() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "endShift"));
		params.add(new BasicNameValuePair("userID", UserFunctions.getInstance()
				.getUserModel().getId()));
		params.add(new BasicNameValuePair("cashEnd", "8888"));
		UserFunctions.getInstance().endShift(ac, params, false);
	}

	private String tablenum;

	private void choosenTB(String table) {
		tablenum = table;
		Utilities.getGlobalVariable(ac).tableNum = table;
		// if (Utilities.getGlobalVariable(ac).isHaveSendOrder) {
		// checkLogin();
		LoginDialog a = new LoginDialog();
		a.showDialogSelectImg(ac);
		// getSale(table);

	}

	private void getSale(String tabnum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getSales"));
		params.add(new BasicNameValuePair("tablenum", tabnum));
		UserFunctions.getInstance().getSales(ac, params, false);
	}
}

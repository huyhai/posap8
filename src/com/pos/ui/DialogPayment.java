package com.pos.ui;

import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.Application;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.controllibs.UserFunctions;
import com.pos.db.ItemsDataSource;
import com.pos.db.PayDataSource;
import com.pos.db.SearchBillDataSource;
import com.pos.libs.CalculationUtils;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.SessionManager;
import com.pos.model.ListOrderModel;
import com.pos.model.PayModel;
import com.pos.print.PrinterFunctions;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.serialport.SerialPort;

import android.serialport.SerialPortFinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DialogPayment implements OnClickListener {
	private static Dialog dialogEditItems;
	private Boolean isShowDialog = false;
	private static Activity ac;
	private RelativeLayout rlAll;
	private RelativeLayout rlCash;
	private RelativeLayout rlMasterCash;
	private RelativeLayout rlChange;
	private Button btnNets;
	private Button btnAme;
	private Button btnMaster;
	private Button btnJCB;
	private Button btnVisa;
	private Button btnDinner;
	private Button btnCash;
	private Button btnConfirm;
	private Button btnConfirmAndRe;
	private TextView tvTotalValue;
	private static TextView tvCash;
	private static EditText tvCashValue;
	private static TextView tvMasterCash;
	private static EditText tvMasterCashValue;
//	private static TextView tvChange;
	private static TextView tvrlChangeValue;

	private Button btnSeven;
	private Button btnEight;
	private Button btnNine;
	private Button btnFour;
	private Button btnFive;
	private Button btnSix;
	private Button btnOne;
	private Button btnTwo;
	private Button btnThree;
	private Button btnZero;
	private Button btnDot;
	private Button btnDelete;
	private static String totala;
	private static SessionManager ss;
	private SerialPort mSerialPort_V;
	private OutputStream mOutputStream_V;
	private Application mApplication;
	private RelativeLayout rlVoucher;
	private Button btnVoucher;
	private EditText edRedeemVCValue;

	public void showDialogSelectImg(Activity _ac, String total) {
		ac = _ac;
		totala = total;
		ss = new SessionManager(ac);
		// custom dialog
		if (!isShowDialog) {
			isShowDialog = true;

			// imgSet = _imgSet;
			dialogEditItems = new Dialog(ac);
			dialogEditItems.getWindow();
			dialogEditItems.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogEditItems.setContentView(R.layout.payment222);
			dialogEditItems.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			// dialog.setTitle("Title...");
			rlAll = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlAppPayment);

			rlCash = (RelativeLayout) dialogEditItems.findViewById(R.id.rlCash);
			rlMasterCash = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlMasterCash);
			rlChange = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlChange);
			btnNets = (Button) dialogEditItems.findViewById(R.id.btnNets);
			btnAme = (Button) dialogEditItems.findViewById(R.id.btnAme);
			btnMaster = (Button) dialogEditItems.findViewById(R.id.btnMaster);
			btnJCB = (Button) dialogEditItems.findViewById(R.id.btnJCB);
			btnVisa = (Button) dialogEditItems.findViewById(R.id.btnVisa);
			btnDinner = (Button) dialogEditItems.findViewById(R.id.btnDinner);
			btnCash = (Button) dialogEditItems.findViewById(R.id.btnCash);
			btnConfirm = (Button) dialogEditItems.findViewById(R.id.btnConfirm);
			btnConfirmAndRe = (Button) dialogEditItems
					.findViewById(R.id.btnConfirmAndRe);
			tvTotalValue = (TextView) dialogEditItems
					.findViewById(R.id.tvTotalValue);
			tvCash = (TextView) dialogEditItems.findViewById(R.id.tvCash);
			tvCashValue = (EditText) dialogEditItems
					.findViewById(R.id.tvCashValue);
			tvMasterCash = (TextView) dialogEditItems
					.findViewById(R.id.tvMasterCash);
			tvMasterCashValue = (EditText) dialogEditItems
					.findViewById(R.id.tvMasterCashValue);
//			tvChange = (TextView) dialogEditItems.findViewById(R.id.tvChange);
			tvrlChangeValue = (TextView) dialogEditItems
					.findViewById(R.id.tvrlChangeValue);
			btnSeven = (Button) dialogEditItems.findViewById(R.id.btnSeven);
			btnOne = (Button) dialogEditItems.findViewById(R.id.btnOne);
			btnTwo = (Button) dialogEditItems.findViewById(R.id.btnTwo);
			btnThree = (Button) dialogEditItems.findViewById(R.id.btnThree);
			btnFour = (Button) dialogEditItems.findViewById(R.id.btnFour);
			btnFive = (Button) dialogEditItems.findViewById(R.id.btnFive);
			btnSix = (Button) dialogEditItems.findViewById(R.id.btnSix);
			btnEight = (Button) dialogEditItems.findViewById(R.id.btnEight);
			btnNine = (Button) dialogEditItems.findViewById(R.id.btnNine);
			btnZero = (Button) dialogEditItems.findViewById(R.id.btnZero);
			btnDot = (Button) dialogEditItems.findViewById(R.id.btnDot);
			btnDelete = (Button) dialogEditItems.findViewById(R.id.btnDelete);
			rlVoucher = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlVoucher);
			btnVoucher = (Button) dialogEditItems.findViewById(R.id.btnVoucher);
			edRedeemVCValue = (EditText) dialogEditItems
					.findViewById(R.id.edRedeemVCValue);

			btnNets.setOnClickListener(this);
			btnDelete.setOnClickListener(this);
			btnAme.setOnClickListener(this);
			btnMaster.setOnClickListener(this);
			btnJCB.setOnClickListener(this);
			btnVisa.setOnClickListener(this);
			btnDinner.setOnClickListener(this);
			btnCash.setOnClickListener(this);
			btnConfirm.setOnClickListener(this);
			btnConfirmAndRe.setOnClickListener(this);
			btnSeven.setOnClickListener(this);
			btnOne.setOnClickListener(this);
			btnTwo.setOnClickListener(this);
			btnThree.setOnClickListener(this);
			btnFour.setOnClickListener(this);
			btnFive.setOnClickListener(this);
			btnSix.setOnClickListener(this);
			btnEight.setOnClickListener(this);
			btnNine.setOnClickListener(this);
			btnZero.setOnClickListener(this);
			btnDot.setOnClickListener(this);
			btnVoucher.setOnClickListener(this);
			tvTotalValue.setText(total);
			edRedeemVCValue.setInputType(InputType.TYPE_NULL);
			tvMasterCashValue.setInputType(InputType.TYPE_NULL);
			tvCashValue.setInputType(InputType.TYPE_NULL);
			isRedem = true;
			((CustomFragmentActivity) ac).setWidthHeight(btnConfirmAndRe, 13.5,
					13.5);

			tvCashValue.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					Double d = -1.0;
					try {
						d = Double.parseDouble(tvTotalValue.getText()
								.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
					Double d1 = -1.0;
					try {
						d1 = Double.parseDouble(tvCashValue.getText()
								.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}

					Double d2 = d - d1;
					DecimalFormat df = new DecimalFormat("####0.00");

					tvMasterCashValue.setText("" + df.format(d2));

					if (d2 < 0) {
						tvrlChangeValue.setText("" + df.format(d2));
						tvMasterCashValue.setText("0.00");
					}
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}
			});
			edRedeemVCValue.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					try {
						double sum = Double.parseDouble(tvTotalValue.getText()
								.toString())
								- Double.parseDouble(edRedeemVCValue.getText()
										.toString());
						tvTotalValue.setText(CalculationUtils.lamtron(sum )+ "");
					} catch (Exception e) {
						tvTotalValue.setText(CalculationUtils.lamtron(Double.parseDouble(totala))+"");
					}
			
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}
			});
			edRedeemVCValue.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					isRedem = true;
					isCard = false;
					isCash = false;
					numRed = "";
					return false;
				}
			});
			tvCashValue.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					isRedem = false;
					isCard = false;
					isCash = true;
					numRed = "";
					return false;
				}
			});
			tvMasterCashValue.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					isRedem = false;
					isCard = true;
					isCash = false;
					numRed = "";
					return false;
				}
			});

			((CustomFragmentActivity) ac).setWidthHeight(rlAll, 1.7, 1.6);

			dialogEditItems.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					isShowDialog = false;
				}
			});

			// VFD
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
				String space = "";
				int lentemp = total.length();
				int tempneed = 27 - lentemp;
				for (int i = 0; i < tempneed; i++) {
					space += " ";
				}
				String str1 = "TOTAL AMOUNT:";
				mOutputStream_V.write(str1.getBytes());
				mOutputStream_V.write(space.getBytes());
				mOutputStream_V.write(total.getBytes());

				mOutputStream_V.write('\r');
				mOutputStream_V.write('\n');

				mApplication.closeSerialPort();
			} catch (Exception e) {
				// Toast.makeText(MainActivity.this, e.toString(),
				// Toast.LENGTH_LONG).show();
				// throw new RuntimeException(e);
			}
			// EO VFD

			dialogEditItems.show();
		}

	}

	private boolean isRedem = false;
	private boolean isCash = false;
	private boolean isCard = false;

	private String numRed = "";
	private int a = 0;
	private boolean isTv = false;

	@Override
	public void onClick(View v) {
		String text1 = tvCash.getText().toString();
		String text2 = tvMasterCash.getText().toString();
		if (v == btnNets) {
			setTenPay(ConstantValue.UNION);
		}
		if (v == btnAme) {
			setTenPay(ConstantValue.AMEX);
		}
		if (v == btnMaster) {
			setTenPay(ConstantValue.MASTER);
		}
		if (v == btnJCB) {
			setTenPay(ConstantValue.JCB);
		}
		if (v == btnVisa) {
			setTenPay(ConstantValue.VISA);
		}
		if (v == btnDinner) {
			setTenPay(ConstantValue.DINERS);
		}
		if (v == btnCash) {
			setTenPay(ConstantValue.CASH);
		}
		if (v == btnConfirm) {
			if (text1.length() < 1 && text2.length() < 1) {
				Toast.makeText(ac, "Choosen Pay Mode", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (PosApp.listOrderData.size() != 0) {
					Utils.pay(ac, getPaymode(tvCash.getText().toString()),
							getPaymode(tvMasterCash.getText().toString()),
							tvCashValue.getText().toString(), tvMasterCashValue
									.getText().toString(), String.valueOf(change),edRedeemVCValue.getText().toString());
					dialogEditItems.dismiss();
					
					Utils.clearList(ac);
				}
			}

		}
		if (v == btnConfirmAndRe) {
			if (text1.length() < 1 && text2.length() < 1) {
				Toast.makeText(ac, "Choosen Pay Mode", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (PosApp.listOrderData.size() != 0) {
					Utils.pay(ac, getPaymode(tvCash.getText().toString()),
							getPaymode(tvMasterCash.getText().toString()),
							tvCashValue.getText().toString(), tvMasterCashValue
									.getText().toString(), String.valueOf(change),edRedeemVCValue.getText().toString());
					dialogEditItems.dismiss();
					if (Utilities.getGlobalVariable(ac).isHaveSendOrder) {
						PrinterFunctions.PrintPosPay(MainActivity.activity,
								ac, "USB:", "", ac.getResources(),
								Utilities.getGlobalVariable(ac).receiptNo, tvCash.getText().toString(),
								tvMasterCash.getText().toString(), tvCashValue.getText().toString(),
								tvMasterCashValue.getText().toString(),edRedeemVCValue.getText().toString());
					}
					Utils.clearList(ac);
				}
			}
		}
		if (v == btnDot) {
			if (numRed.equals("")) {
				numRed = numRed + "0.";
			} else {
				numRed = numRed + ".";
			}
			change();
		}
		if (v == btnZero) {
			numRed = numRed + "0";
			settext(numRed);
			change();
		}
		if (v == btnNine) {
			numRed = numRed + "9";
			settext(numRed);
			change();
		}
		if (v == btnEight) {
			numRed = numRed + "8";
			settext(numRed);
			change();
		}
		if (v == btnSix) {
			numRed = numRed + "6";
			settext(numRed);
			change();
		}
		if (v == btnFive) {
			numRed = numRed + "5";
			settext(numRed);
			change();
		}
		if (v == btnFour) {
			numRed = numRed + "4";
			settext(numRed);
			change();
		}
		if (v == btnThree) {
			numRed = numRed + "3";
			settext(numRed);
			change();
		}
		if (v == btnTwo) {
			numRed = numRed + "2";
			settext(numRed);
			change();
		}

		if (v == btnOne) {
			numRed = numRed + "1";
			settext(numRed);
			change();
		}
		if (v == btnSeven) {
			numRed = numRed + "7";
			settext(numRed);
			change();
		}
		if (v == btnDelete) {
			tvCashValue.setText("");
			tvMasterCashValue.setText("");
			tvrlChangeValue.setText("");
			edRedeemVCValue.setText("");
			a = 0;
			isTv = false;
			numRed = "";

		}
		if (v == btnVoucher) {
			isRedem=true;
			isCard=false;
			isCash=false;
			edRedeemVCValue.requestFocus();

		}
	}

	private void settext(String t) {
		if (isCard) {
			tvMasterCashValue.setText(t);
		} else if (isCash) {
			tvCashValue.setText(t);
		} else if (isRedem) {
			edRedeemVCValue.setText(t);

		}
	}

	private void setTenPay(String t) {
		if (isCard) {
			tvMasterCash.setText(t);
		} else if (isCash) {
			tvCash.setText(t);
		}
	}

	private String getPaymode(String text) {
		if (text.equals(ConstantValue.AMEX)) {
			return "7";
		} else if (text.equals(ConstantValue.MASTER)) {
			return "5";
		} else if (text.equals(ConstantValue.JCB)) {
			return "8";
		} else if (text.equals(ConstantValue.VISA)) {
			return "4";
		} else if (text.equals(ConstantValue.DINERS)) {
			return "6";
		} else if (text.equals(ConstantValue.UNION)) {
			return "3";
		} else if (text.equals(ConstantValue.CASH)) {
			return "1";
		} else {
			return "";
		}
	}

	public static double change = -1;
	public static double sumuser = -1;

	private void change() {
		double b;
		double c;
		try {
			if (TextUtils.isEmpty(tvCashValue.getText().toString())) {
				b = Double.parseDouble("0");

			} else {
				b = Double.parseDouble(tvCashValue.getText().toString());

			}
			if (TextUtils.isEmpty(tvMasterCashValue.getText().toString())) {
				c = Double.parseDouble("0");

			} else {
				c = Double.parseDouble(tvMasterCashValue.getText().toString());

			}
			sumuser = b + c;
			change = CalculationUtils.calculateChange(sumuser,
					Double.parseDouble(tvTotalValue.getText().toString()));
		} catch (Exception e) {
			change = -1;
		}
		tvrlChangeValue.setText("CHANGE: "+String.valueOf(CalculationUtils.lamtron(change)));
	}

	// private String cashAmount;
	// private String creditAmount;

	private String getCreadit() {
		return numRed;
		// if (tvCash.getText().toString().equals("Cash")) {
		// cashAmount = tvCashValue.getText().toString();
		// creditAmount = tvMasterCashValue.getText().toString();
		// return tvMasterCash.getText().toString();
		// } else {
		// cashAmount = tvMasterCashValue.getText().toString();
		// creditAmount = tvCashValue.getText().toString();
		// return tvCash.getText().toString();
		// }

	}

	private boolean check() {
		if (TextUtils.isEmpty(tvCashValue.getText().toString())) {
			return false;
		}
		if (TextUtils.isEmpty(tvCash.getText().toString())) {
			return false;
		}
		if (TextUtils.isEmpty(tvMasterCash.getText().toString())) {
			return false;
		}
		if (TextUtils.isEmpty(tvMasterCashValue.getText().toString())) {
			return false;
		}
		return true;
	}
}

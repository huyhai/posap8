package com.pos.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.SplashScreen;
import com.pos.Table;
import com.pos.adapter.UsersAdapter;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.db.MainCateDataSource;
import com.pos.db.UpdateAllDatabase;
import com.pos.db.UsersDataSource;
import com.pos.libs.SessionManager;
import com.pos.libs.ViewUtils;
import com.pos.model.ListOrderModel;
import com.pos.model.UsersModel;
import com.pos.db.SaleReportDataSource;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginDialog extends Activity implements OnClickListener,
		OnItemClickListener {
	private Dialog dialogEditItems;
	private Boolean isShowDialog = false;
	private static Activity ac;
	private RelativeLayout rlBig;
	private GridView gvUser;
	private EditText edInput;
	private static Button btnOk;
	public static ArrayList<UsersModel> list = new ArrayList<UsersModel>();
	UsersAdapter adapter;
	public static String linkImage;
	private ImageView imgLanguage;
	private static TextView tvName;
	private EditText edCashIn;
	private SessionManager session1;
	private Button btnBack;

	public void showDialogSelectImg(Activity _ac) {
		ac = _ac;
		session1 = new SessionManager(ac);
		// custom dialog
		if (!isShowDialog) {
			isShowDialog = true;
			// imgSet = _imgSet;
			dialogEditItems = new Dialog(ac);
			dialogEditItems.getWindow();
			dialogEditItems.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogEditItems.setContentView(R.layout.login_dialog);
			dialogEditItems.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			// dialog.setTitle("Title...");
			this.rlBig = (RelativeLayout) dialogEditItems
					.findViewById(R.id.rlBig);
			this.gvUser = (GridView) dialogEditItems.findViewById(R.id.gvUser);
			this.btnOk = (Button) dialogEditItems.findViewById(R.id.btnOk);
			edInput = (EditText) dialogEditItems.findViewById(R.id.edInput);
			tvName = (TextView) dialogEditItems.findViewById(R.id.tvName);
			edCashIn = (EditText) dialogEditItems.findViewById(R.id.edCashIn);
			btnBack = (Button) dialogEditItems.findViewById(R.id.btnBack);
			UsersAdapter.a = -1;
			edCashIn.setVisibility(View.GONE);

			imgLanguage = (ImageView) dialogEditItems
					.findViewById(R.id.imgLanguage);
			ViewUtils.setWidthHeight(rlBig, 1.3, 1.4, ac);
			// ((CustomFragmentActivity) ac).setWidthHeight(imgCompanyLogo, 8,
			// 5);

			btnOk.setOnClickListener(this);
			imgLanguage.setOnClickListener(this);
			btnBack.setOnClickListener(this);

			dialogEditItems.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					isShowDialog = false;
				}
			});
			dialogEditItems.setCancelable(false);
			dialogEditItems.setCanceledOnTouchOutside(false);
			dialogEditItems.show();
			if (Utilities.getGlobalVariable(ac).englist) {
				imgLanguage.setBackgroundResource(R.drawable.icon_reverse2);
				// MainActivity.setText("zh-TW");
				// Utilities.getGlobalVariable(ac).englist = false;
				// Utilities.getGlobalVariable(ac).language_code=2;
			} else {
				imgLanguage.setBackgroundResource(R.drawable.icon_reverse1);
				// MainActivity.setText("en");
				// Utilities.getGlobalVariable(ac).language_code=1;
				// Utilities.getGlobalVariable(ac).englist = true;

			}
			IntentFilter intentGetKeySend = new IntentFilter();
			intentGetKeySend.addAction(UserFunctions.LISTUSER);
			intentGetKeySend.addAction(UserFunctions.LOGINUSER);
			ac.registerReceiver(receiver, intentGetKeySend);
			gvUser.setOnItemClickListener(this);
			callAdv();
		}

	}

	private void callAdv() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getlistusercashier"));
		UserFunctions.getInstance().callListUser(ac, params, false);
	}

	private void callLogin() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "loginnocash"));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("pass", edInput.getText().toString()));
		UserFunctions.getInstance().callLoginUser(ac, params, false);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(UserFunctions.LISTUSER)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					adapter = new UsersAdapter(ac, UserFunctions.getInstance()
							.getlistUser());
					gvUser.setAdapter(adapter);

				} else {
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.LOGINUSER)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {

					dialogEditItems.dismiss();
					UsersAdapter.itemList.clear();
					try {
						ac.unregisterReceiver(receiver);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					Utilities.getGlobalVariable(ac).isLogin = true;
					session1.saveLogin(username, edInput.getText().toString()
							.trim(), true);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("tag", "getSales"));
					params.add(new BasicNameValuePair("tablenum", Utilities
							.getGlobalVariable(ac).tableNum));
					UserFunctions.getInstance().getSales(ac, params, false);

				} else {
					Utilities.getGlobalVariable(ac).isLogin = false;
					Utilities.showDialogNoBack(ac,
							UserFunctions.getInstance().getMessage()).show();
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (btnOk == v) {
			callLogin();
		}
		if (btnBack == v) {
			UserFunctions.getInstance().getlistUser().clear();
			dialogEditItems.dismiss();
		}

	}

	private String username = "";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UsersAdapter ua = new UsersAdapter(ac);
		username = ua.returnUsername(position);
		UsersAdapter.a = position;
		adapter.notifyDataSetChanged();

	}

	private void endShift() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "endShift"));
		params.add(new BasicNameValuePair("userID", UserFunctions.getInstance()
				.getUserModel().getId()));
		params.add(new BasicNameValuePair("cashEnd", ""));
		UserFunctions.getInstance().endShift(ac, params, false);
	}
}

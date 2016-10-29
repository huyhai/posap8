package com.pos.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.Table;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.SessionManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DialogPaxNumber {
	private SessionManager session;
	private EditText ednote;
	private Activity ac;

	public void showNoteDialog(final Context context, final Activity _ac) {
		ac = _ac;
		session = new SessionManager(ac);
		Builder builder = new Builder(context);
		builder.setTitle("Pax Number");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.paxnum, null);
		builder.setView(v);
		ednote = (EditText) v.findViewById(R.id.edNote);
//		ednote.setText(UserFunctions.getInstance().getPaxModel().getNumber_of_customers());
		ednote.setText("0");
		// ((CustomFragmentActivity) ac).setWidthHeight(lnote, 1, 2);

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (ednote.getText().toString().equals("")
								|| Integer
										.parseInt(ednote.getText().toString()) < 0) {
							ednote.setError("error");

						} else {
							Utilities.getGlobalVariable(ac).numberCustomer = ednote.getText().toString();
//							checkLogin();
							Intent i = new Intent(ac, MainActivity.class);
							 ac.startActivity(i);
						}

					}
				});
		builder.setNegativeButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

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
	private void getSale(String tabnum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getSales"));
		params.add(new BasicNameValuePair("tablenum", tabnum));
		UserFunctions.getInstance().getPaxNumber(ac, params, false);
	}

}

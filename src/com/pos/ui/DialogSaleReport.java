package com.pos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pos.CustomFragmentActivity;
import com.pos.R;
import com.pos.Table;
import com.pos.common.ConstantValue;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.libs.SessionManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DialogSaleReport extends Activity implements OnClickListener {
	private Activity ac;
	private Button btnEnd;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_report);
		ac = DialogSaleReport.this;
		btnEnd = (Button) findViewById(R.id.btnEnd);
		btnEnd.setOnClickListener(this);
		session = new SessionManager(ac);

	}

	@Override
	public void onClick(View v) {
		if (v == btnEnd) {
//			checkend();
		}

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(UserFunctions.CHECKEND)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					Toast.makeText(ac, "Still working", Toast.LENGTH_SHORT)
							.show();
				} else {
					endShift();
				}
			}
			if (intent.getAction().equalsIgnoreCase(UserFunctions.ENDSHIFT)) {
				if (intent.getExtras().getBoolean(ConstantValue.IS_SUCCESS)) {
					
					session.saveLogin("", "", false);
					Utilities.getGlobalVariable(ac).isLogin=false;
					Intent intent1 = new Intent(getApplicationContext(), Table.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent1);
					//end shift finish
				} else {
					//end shift failed
				}
			}
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		IntentFilter intentGetKeySend = new IntentFilter();
//		intentGetKeySend.addAction(UserFunctions.CHECKEND);
//		intentGetKeySend.addAction(UserFunctions.ENDSHIFT);
//		ac.registerReceiver(receiver, intentGetKeySend);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		try {
//			ac.unregisterReceiver(receiver);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}

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
}

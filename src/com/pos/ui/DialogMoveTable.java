package com.pos.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pos.R;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;
import com.pos.controllibs.UserFunctions.ListUserModel;
import com.pos.libs.SessionManager;
import com.pos.service.JSONCallBack;
import com.pos.service.JSONMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DialogMoveTable {
	private EditText tableno;
	private Activity ac;
	
	private void move(String old,String newvar) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "movetable"));
		params.add(new BasicNameValuePair("old", old));
		params.add(new BasicNameValuePair("new", newvar));
		movetable(ac, params, false);
	}
	public void movetable(Activity cont,
			List<NameValuePair> JSONObjectParams, Boolean _isShowProgressBar) {
		
		@SuppressWarnings("unused")
		JSONMethod method = new JSONMethod(cont, JSONObjectParams, downsoftpin,
				_isShowProgressBar);
		// method.execute();
	}

	JSONCallBack downsoftpin = new JSONCallBack() {

		@Override
		public void callResult(Context activity, String result, long time) {
			try {
				JSONObject resultJson = new JSONObject(result);				
				if (null != result) {
					if ((Boolean) resultJson.get("is_success")) {						      
						}
					
					} 				
			} catch (Exception e) {				
			}
		}
	};
	
	public void showDialog(final Context context, final Activity _ac, String tablenumber) {
		ac = _ac;
		final String tableva2=tablenumber;
		Builder builder = new Builder(context);
		builder.setTitle("Move Table");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.tableno, null);
		builder.setView(v);
		tableno = (EditText) v.findViewById(R.id.tableno);

		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						move(tableva2, tableno.getText().toString());
						

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

}

package com.pos.ui;

import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.common.Utils;
import com.pos.libs.SessionManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogConfirmSaleRemarks {

	private TextView ednote;
	private Activity ac;
	String gram = "";

	public void showNoteDialog(final Context context, final Activity _ac,
			final String price) {
		ac = _ac;
		Builder builder = new Builder(context);
		builder.setTitle("Confirm");
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.saleremarkscf, null);
		builder.setView(v);
		ednote = (TextView) v.findViewById(R.id.edNote);
		// ((CustomFragmentActivity) ac).setWidthHeight(lnote, 1, 2);
		// String stanby = "";
		// if (StyleAndRequestActivity.cbStandBy.isChecked()) {
		// stanby = "Stand-by";
		// }

		if (TextUtils.isEmpty(StyleAndRequestActivity.edWeight.getText()
				.toString())) {
			gram = "";
		} else {
			gram = StyleAndRequestActivity.edWeight.getText().toString()
					+ " gram";
		}
		ednote.setText(Html.fromHtml(Utilities.getGlobalVariable(ac).styleRequests
				+ " "

				+ Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests + " "

				+ Utilities.getGlobalVariable(ac).serving + " "

				+ Utilities.getGlobalVariable(ac).addReplace + " "

				+ " " + StyleAndRequestActivity.size + " " + gram));
		builder.setPositiveButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (StyleAndRequestActivity.cbStandBy.isChecked()) {
							Utilities.getGlobalVariable(ac).statusSaleRemarks = "1";
						} else {
							Utilities.getGlobalVariable(ac).statusSaleRemarks = "0";
						}
						if (Utilities.getGlobalVariable(ac).styleRequests != ""
								|| Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests != ""
								|| Utilities.getGlobalVariable(ac).serving != ""
								|| Utilities.getGlobalVariable(ac).addReplace != ""
								|| gram != ""||StyleAndRequestActivity.cbStandBy.isChecked()) {
							Utils.updateItem(ac, MainActivity.qly, ednote
									.getText().toString(), Utilities
									.getGlobalVariable(ac).statusSaleRemarks,
									price, "1");
						} else {
							Utils.updateItem(ac, MainActivity.qly, ednote
									.getText().toString(), Utilities
									.getGlobalVariable(ac).statusSaleRemarks,
									price, "");
						}

						dialog.dismiss();
						ac.finish();
						// MainActivity ma=new MainActivity();
						// ma.notifidataOrderList();

					}
				});
		builder.setNeutralButton(ac.getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Remove",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Utilities.getGlobalVariable(ac).addReplace = "";
						Utilities.getGlobalVariable(ac).NoLessMoreOnlyRequests = "";
						Utilities.getGlobalVariable(ac).serving = "";
						Utilities.getGlobalVariable(ac).styleRequests = "";
						StyleAndRequestActivity.size="";
						Intent intent = ac.getIntent();
						ac.finish();
						ac.startActivity(intent);

					}
				});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}
}

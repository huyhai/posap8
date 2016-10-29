package com.pos.ui;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.controllibs.UserFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DialogNote {

	private RelativeLayout lnote;
	private EditText ednote;

	public void showNoteDialog(final Context context, final Activity ac) {
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_note));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.note, null);
		builder.setView(v);
		lnote = (RelativeLayout) v.findViewById(R.id.lnote);
		ednote = (EditText) v.findViewById(R.id.edNote);
		((CustomFragmentActivity) ac).setWidthHeight(lnote, 1, 2);
		ednote.setText(UserFunctions.getInstance().getSaleModel().getRemarks());

		builder.setNeutralButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						MainActivity.note=ednote.getText()
								.toString();
						
						
					}
				});
		builder.setPositiveButton(ac.getString(R.string.str_cancel),
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

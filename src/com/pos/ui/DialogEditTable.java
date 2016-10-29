package com.pos.ui;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.libs.ComDDUtilities;
import com.pos.model.ListOrderModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogEditTable implements android.view.View.OnClickListener {
	private Dialog DialogEditTable;
	private Boolean ShowDialog = false;
	private TextView jointable;
	private TextView movetable;
	private LinearLayout llAll;
	private Activity ac;
	private Context context;
	private String tablevar=null;
	
	public void showDialog(Activity _ac,String tablenumber) {
		ac = _ac;
		tablevar=tablenumber;
		// custom dialog
		if (!ShowDialog) {
			ShowDialog = true;
			// imgSet = _imgSet;
			DialogEditTable = new Dialog(ac);
			DialogEditTable.getWindow();
			DialogEditTable.setTitle("You have selected table no. "+tablenumber);
			//DialogEditTable.requestWindowFeature(Window.FEATURE_NO_TITLE);
			DialogEditTable.setContentView(R.layout.dialog_edit_item3);
			DialogEditTable.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.WHITE));

			this.jointable = (TextView) DialogEditTable
					.findViewById(R.id.tvjointable);
			this.movetable = (TextView) DialogEditTable
					.findViewById(R.id.tvmovetable);
		
			llAll = (LinearLayout) DialogEditTable.findViewById(R.id.llAll);
			
			jointable.setOnClickListener(this);
			movetable.setOnClickListener(this);
			DialogEditTable.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					ShowDialog = false;
				}
			});

			DialogEditTable.show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == jointable) {
			DialogJoinTable a=new DialogJoinTable();
			a.showDialog(ac, ac, tablevar);
			DialogEditTable.dismiss();

		}
		if (v == movetable) {
			DialogMoveTable a=new DialogMoveTable();
			a.showDialog(ac, ac, tablevar);
			DialogEditTable.dismiss();

		}
	}

	
}


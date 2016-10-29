package com.pos.ui;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.common.Utilities;
import com.pos.libs.ViewUtils;
import com.pos.model.ListOrderModel;
import com.pos.db.CompanyDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

public class DialogCompany {
        private EditText CompanyName;
        private EditText Address;
        private EditText Telephone;
        private EditText Facsimile;
        private EditText Email;
        private EditText Website;
        private EditText CompanyReg;
        private EditText GSTNo;
        private EditText Footer;
        private EditText Password;
        
        
	public void showDialogSaleReport (final Context context, final Activity ac) {
		Builder builder = new Builder(context);
		builder.setTitle(ac.getString(R.string.str_Organisation));
		builder.setIcon(R.drawable.ic_launcher);
		LayoutInflater inflater = ac.getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_company, null);
		builder.setView(v);
		CompanyDataSource a=new CompanyDataSource(ac, ac);
		
		CompanyName = (EditText) v.findViewById(R.id.ediOpenningCash);
		Address=(EditText) v.findViewById(R.id.ediGrossSale);
		Telephone=(EditText) v.findViewById(R.id.ediItemSale);
		Facsimile=(EditText)v.findViewById(R.id.ediTotalCash);
		Email=(EditText)v.findViewById(R.id.ediNumberOfSale);
		Website=(EditText)v.findViewById(R.id.ediItemDiscount);
		CompanyReg=(EditText)v.findViewById(R.id.ediBillDiscount);
		GSTNo=(EditText)v.findViewById(R.id.ediGST);
		Footer =(EditText)v.findViewById(R.id.ediReceipt);
		Password =(EditText)v.findViewById(R.id.ediPassword);
		
		CompanyName.setText(a.loadCName());
		Address.setText(a.loadCAddress());
		Telephone.setText(a.loadCPhone());
		Facsimile.setText(a.loadCFacsimile());
		Email.setText(a.loadCEmail());
		Website.setText(a.loadCWebsite());
		CompanyReg.setText(a.loadCompanyNo());
		GSTNo.setText(a.loadGST());
		Footer.setText(a.loadCReceipt());
		Password.setText(a.loadCPassword());
		
		
		builder.setNeutralButton(ac.getString(R.string.str_confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CompanyDataSource a=new CompanyDataSource(ac, ac);
						String i=a.UpdateComany(CompanyName.getText().toString(), Address.getText().toString(), Telephone.getText().toString(), Facsimile.getText().toString(), Email.getText().toString(), Website.getText().toString(), CompanyReg.getText().toString(), GSTNo.getText().toString(), Footer.getText().toString(), Password.getText().toString());
                        
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

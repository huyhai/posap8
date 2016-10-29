package com.pos.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pos.CustomFragmentActivity;
import com.pos.MainActivity;
import com.pos.PosApp;
import com.pos.R;
import com.pos.adapter.BillAdapter;
import com.pos.adapter.SearchBillAdapter;
import com.pos.common.Utilities;
import com.pos.libs.ComDDUtilities;
import com.pos.libs.DatePickerDialogFragment;
import com.pos.libs.ViewUtils;
import com.pos.model.BillModel;
import com.pos.model.ListOrderModel;
import com.pos.model.SearchBillModel;
import com.pos.print.PrinterFunctions;
import com.pos.db.BillDataSource;
import com.pos.db.CompanyDataSource;
import com.pos.db.MySQLiteHelper;
import com.pos.db.SaleReportDataSource;
import com.pos.db.SearchBillDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

public class DialogTicketReport extends Activity implements OnClickListener,
		OnTimeSetListener, OnDateSetListener {
	PdfPTable table1;
	private BaseFont bfBold;
	private ListView lvBill;
	public static ArrayList<BillModel> list = new ArrayList<BillModel>();
	private Activity ac;
	BillAdapter adapter;
	public static int a = -1;
	private boolean checkpick = true;
	private LinearLayout rlBillNo;
	private LinearLayout rlCounter;
	private LinearLayout rlClose;
	private LinearLayout rlTotalAmount;
	private LinearLayout rlCash;
	private LinearLayout rlCard;
	private LinearLayout rlGST;
	private LinearLayout rlUser;

	private String todate;
	private String fromdate;
	private String totime;
	private String formdtime;

	private Button btnSearch;
	private Button btnPrint;
	private Button edFromday;
	private Button edFromTime;
	private Button edToday;
	private Button edToTime;
	private Button btnPreview;

	private int mdate;
	private int mmonth;
	private int myear;
	int hour;
	int minute;
	private ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_report);

		lvBill = (ListView) findViewById(R.id.lvBill);
		btnPrint = (Button) findViewById(R.id.btnPrintA4ta);
		rlBillNo = (LinearLayout) findViewById(R.id.rltBillNo);
		rlCounter = (LinearLayout) findViewById(R.id.rltCounter);
		rlClose = (LinearLayout) findViewById(R.id.rltClose);
		rlTotalAmount = (LinearLayout) findViewById(R.id.rltTotalAmount);
		rlCash = (LinearLayout) findViewById(R.id.rltCash);
		rlCard = (LinearLayout) findViewById(R.id.rltCard);
		rlGST = (LinearLayout) findViewById(R.id.rltGST);
		rlUser = (LinearLayout) findViewById(R.id.rltUser);
        
		btnPreview = (Button) findViewById(R.id.btnPrintA4review);
		edToday = (Button) findViewById(R.id.edToday);
		edToTime = (Button) findViewById(R.id.edToTime);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		edFromday = (Button) findViewById(R.id.edFromday);
		edFromTime = (Button) findViewById(R.id.edFromTime);
		imgBack = (ImageView) findViewById(R.id.imgBack);

		adapter = new BillAdapter(this, list);
		lvBill.setAdapter(adapter);
		if (list != null)
			list.clear();

		BillDataSource dataSource = new BillDataSource(this, this);
		list = dataSource.loadBill();
		adapter.setItemList(list);
		adapter.notifyDataSetChanged();
        
		btnPreview.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
		edToday.setOnClickListener(this);
		edToTime.setOnClickListener(this);
		edFromday.setOnClickListener(this);
		edFromTime.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		imgBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v==btnPreview)
		{
			BillDataSource ab = new BillDataSource(this, this);
			String str2 = ab.loadSaleID(list.get(a).getReceipt_no());
			DialogEditItems ab1 = new DialogEditItems();
//			ab1.showDialogSelectImg2(DialogTicketReport.this,list.get(a)
//					.getReceipt_no(),str2);
		}
		if (v == btnSearch) {
			if (edFromday.getText().toString().equals("")
					|| edToday.getText().toString().equals("")
					|| edToday.getText().toString().equals("")
					|| edToTime.getText().toString().equals("")) {
				Toast.makeText(this, "Please input date and time !!!",
						Toast.LENGTH_LONG).show();

			} else if (!edFromday.getText().toString().equals("")
					&& !edToday.getText().toString().equals("")
					&& !edToday.getText().toString().equals("")
					&& !edToTime.getText().toString().equals("")) {

				String form = fromdate + " " + formdtime;
				String to = todate + " " + totime;
				BillDataSource dataSource = new BillDataSource(this, this);
				list = dataSource.loadBills(form, to);
				adapter.setItemList(list);
				adapter.notifyDataSetChanged();

			}
		}
		if (v == btnPrint) {

			if (DialogTicketReport.a != -1) {
				BillDataSource ab = new BillDataSource(this, this);
				String str = ab.loadprinttype(list.get(a).getReceipt_no());
				String str2 = ab.loadSaleID(list.get(a).getReceipt_no());

				if (str.equals("1") || str.equals("2")) {
					PrinterFunctions.RePrintPos(this, this, "USB:", "", str2, list.get(a)
							.getReceipt_no(),this.getResources());
				} else {
					PrinterFunctions.PrintSampleReceiptRE(this, this, "USB:", "", "Raster",
							"3inch (78mm)", str2, list.get(a).getReceipt_no());
				}
			}
		}
		if (v == edFromday) {
			checkpick = true;
			final Calendar c = Calendar.getInstance();
			mdate = c.get(Calendar.DATE);
			mmonth = c.get(Calendar.MONTH);
			myear = c.get(Calendar.YEAR);
			DatePickerDialog d = new DatePickerDialog(this, this, myear,
					mmonth, mdate);
			d.show();
		}
		if (v == edFromTime) {
			checkpick = true;
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			mdate = c.get(Calendar.DATE);
			mmonth = c.get(Calendar.MONTH);
			myear = c.get(Calendar.YEAR);
			TimePickerDialog tm = new TimePickerDialog(this, this, hour,
					minute, true);
			tm.show();
		}
		if (v == edToday) {
			checkpick = false;
			final Calendar c1 = Calendar.getInstance();
			mdate = c1.get(Calendar.DATE);
			mmonth = c1.get(Calendar.MONTH);
			myear = c1.get(Calendar.YEAR);
			DatePickerDialog d1 = new DatePickerDialog(this, this, myear,
					mmonth, mdate);
			d1.show();
		}
		if (v == edToTime) {
			checkpick = false;
			final Calendar c1 = Calendar.getInstance();
			hour = c1.get(Calendar.HOUR_OF_DAY);
			minute = c1.get(Calendar.MINUTE);
			mdate = c1.get(Calendar.DATE);
			mmonth = c1.get(Calendar.MONTH);
			myear = c1.get(Calendar.YEAR);
			TimePickerDialog tm1 = new TimePickerDialog(this, this, hour,
					minute, true);
			tm1.show();
		}
		if (v == imgBack) {
			DialogTicketReport.this.finish();
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		String month;
		String date;
		if ((monthOfYear + 1) < 10) {
			month = "0" + (monthOfYear + 1);
		} else {
			month = String.valueOf(monthOfYear + 1);
		}
		if (dayOfMonth < 10) {
			date = "0" + dayOfMonth;
		} else {
			date = "" + dayOfMonth;
		}
		if (checkpick) {
			edFromday.setText(date + "/" + month + "/" + year);
			fromdate = year + "-" + month + "-" + date;
		} else {
			edToday.setText(date + "/" + month + "/" + year);
			todate = year + "-" + month + "-" + date;
		}

	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (checkpick) {
			formdtime = "";
			if (hourOfDay < 10) {

				formdtime += "0" + hourOfDay + ":";
			} else {

				formdtime += hourOfDay + ":";
			}
			if (minute < 10) {

				formdtime += "0" + minute + ":00";
			} else {

				formdtime += minute + ":00";
			}
			edFromTime.setText(formdtime);
		} else {
			totime = "";
			if (hourOfDay < 10) {

				totime += "0" + hourOfDay + ":";
			} else {

				totime += hourOfDay + ":";
			}
			if (minute < 10) {

				totime += "0" + minute + ":00";
			} else {

				totime += minute + ":00";
			}
			edFromTime.setText(formdtime);

			edToTime.setText(totime);

		}

	}
	private void createHeadings(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 10);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}

	private void createTitle(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 15);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}

	private void initializeFonts() {

		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
